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


    public void setupUDPServer(Integer port){
        try{
            this.udpSocket = new DatagramSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            byte [] buffer = new byte [512];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            try{
                this.udpSocket.receive(incoming);
            }catch(Exception e){
                e.printStackTrace();
            }
            String incomingData = new String(incoming.getData(), 0, incoming.getLength());
            System.out.print("Incoming message: ");
            System.out.println(incomingData);
            
            String [] parseCommand = incomingData.split(" ");
            for(String c : parseCommand){
                System.out.println(c);
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
