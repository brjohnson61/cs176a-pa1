import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

class server_java_udp{
    private DatagramSocket udpSocket;
    private Integer clientPort;
    private InetAddress clientAddress;
    private static Scanner scanner;
    private static final String ACK = "ACK";
    private static final byte [] bufACK = ACK.getBytes();

    public void setupUDPServer(Integer port){
        Integer incomingLength = 0;
        try{
            this.udpSocket = new DatagramSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            try{
                Boolean readyForData = false;
                byte [] bufferLength = new byte [512];
                DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
                try{
                    this.udpSocket.receive(incoming);
                }catch(Exception e){
                    e.printStackTrace();
                }
                String incomingData = new String(incoming.getData(), 0, incoming.getLength());
                InetAddress clientAddress = incoming.getAddress();
                Integer clientPort = incoming.getPort();
                System.out.print("Incoming length: ");
                System.out.println(incomingData);
                
                String [] parseCommand = incomingData.split(" ");
                ArrayList<String> parsedArgsArrList = new ArrayList<String>();
                for(String c : parseCommand){
                    parsedArgsArrList.add(c);
                    System.out.println(c);
                }
                
                System.out.print("parseCommand.length: ");
                System.out.println(parsedArgsArrList.size());
                if(parsedArgsArrList.size() == 2){
                    if(parsedArgsArrList.get(0).equals("length") && parsedArgsArrList.get(1).equals("=")){
                        incomingLength = Integer.valueOf(parsedArgsArrList.get(2));
                        DatagramPacket ackOutgoing = new DatagramPacket(bufACK, bufACK.length, clientAddress, clientPort);
                        this.udpSocket.send(ackOutgoing); 
                        readyForData = true;

                    }
                }

                if(readyForData){
                    System.out.println("ACK sent, ready for command");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private String processMessage(String message){
        String processedOutput = "";
        try{
            Process terminalCommand = Runtime.getRuntime().exec(message);
            InputStreamReader fromTerminal = new InputStreamReader(terminalCommand.getInputStream());
            BufferedReader chainFromTerminal = new BufferedReader(fromTerminal);
            for(String bufferString = chainFromTerminal.readLine(); bufferString != null; bufferString = chainFromTerminal.readLine()){
                processedOutput = processedOutput.concat(bufferString);
                processedOutput = processedOutput.concat("\n");
            }
            chainFromTerminal.close();
    
            terminalCommand.waitFor();
            terminalCommand.destroy();
        }catch(Exception e){
            e.printStackTrace();
        }
        return processedOutput;
    }





public static void main(String[] args) {
    scanner = new Scanner(System.in);
    String IPAddress = "";
    try{
        InetAddress ipAddress = InetAddress.getLocalHost(); 
        IPAddress = ipAddress.getHostAddress();
    }catch(UnknownHostException uhe){
        uhe.printStackTrace();
    }
    System.out.print("This machine's IP: ");
    System.out.println(IPAddress);

    System.out.println("Server Listening Port:");
    Integer userInput = Integer.valueOf(scanner.nextLine());

    if((userInput > 0) && (userInput < 65535)){
        server_java_udp server = new server_java_udp();
        server.setupUDPServer(userInput);
    }
    else{
        System.out.println("Invalid port number.");
    }
}
}
