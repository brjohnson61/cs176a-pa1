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
        
        try{
            this.udpSocket = new DatagramSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
        while(true){
            try{
                Integer incomingLength = 0;
                Boolean readyForData = false;
                // byte [] bufferLength = new byte [512];
                // DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
                // try{
                //     this.udpSocket.receive(incoming);
                // }catch(Exception e){
                //     e.printStackTrace();
                // }
                // String incomingLengthMessage = new String(incoming.getData(), 0, incoming.getLength());
                // this.clientAddress = incoming.getAddress();
                // this.clientPort = incoming.getPort();
                // System.out.print("Incoming length: ");
                // System.out.println(incomingLengthMessage);

                String incomingLengthMessage = receiveLength();
                
                // String [] parseCommand = incomingLengthMessage.split(" ");
                // ArrayList<String> parsedArgsArrList = new ArrayList<String>();
                // for(String c : parseCommand){
                //     parsedArgsArrList.add(c);
                //     System.out.println(c);
                // }
                
                
                // System.out.println(parsedArgsArrList.size());
                // if(parsedArgsArrList.size() == 3){
                //     if(parsedArgsArrList.get(0).equals("length") && parsedArgsArrList.get(1).equals("=")){
                //         incomingLength = Integer.valueOf(parsedArgsArrList.get(2));
                //         sendACK();
                //         readyForData = true;
                //         System.out.println("Successfully executed ack block");
                //     }
                // }

                Integer initialLength = parseLength(incomingLengthMessage);
                if(initialLength.equals(0)){
                    readyForData = false;
                }
                else{
                    readyForData = true;
                    sendACK();
                }

                System.out.println(initialLength);

                if(readyForData){
                    System.out.println("ACK sent, ready for command");
                    String incomingCommand = receiveCommand(initialLength);
                    sendACK();
                    System.out.print("Command from client: ");
                    System.out.println(incomingCommand);
                    String commandResult = processMessage(incomingCommand);
                    System.out.println(commandResult);
                    
                    // byte[] bufResult = commandResult.getBytes();
                    // String resultLength = Integer.toString(bufResult.length);
                    // byte[] resultLengthBuf = resultLength.getBytes();

                    if(sendLength(commandResult)){
                        System.out.println("Success sending result length");
                    }
                    else{
                        System.out.println("Error");
                    }
                    if(receiveACK()){
                        System.out.println("ReceivedACK!");
                    }
                    else{
                        System.out.println("Did not receive ACK");
                    }
                    if(sendCommand(commandResult)){
                        System.out.println("Sent command results to client");
                    }
                    else{
                        System.out.println("Failed to send command");
                    }
                    if(receiveACK()){
                        System.out.println("Client received command results");
                    }
                    else{
                        System.out.println("Client did not receive command results");
                    }
                    // DatagramPacket resultLengthToClient = new DatagramPacket(resultLengthBuf, resultLengthBuf.length, this.clientAddress, this.clientPort);
                    // this.udpSocket.send(resultLengthToClient);
                    // DatagramPacket resultToClient = new DatagramPacket(, length)

                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private String receiveLength(){
        //Integer incomingLength = 0;
        byte [] bufferLength = new byte [512];
        DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
        try{
            this.udpSocket.receive(incoming);
        }catch(Exception e){
            e.printStackTrace();
        }
        String incomingLengthMessage = new String(incoming.getData(), 0, incoming.getLength());
        this.clientAddress = incoming.getAddress();
        this.clientPort = incoming.getPort();
        System.out.println("Returning incomingLengthMessage");
        System.out.println(incomingLengthMessage);
        return incomingLengthMessage;
    }

    private Boolean sendLength(String message){
        try{
            byte[] bufResult = message.getBytes();
            String lengthEquals = "length = ";
            String resultLength = lengthEquals.concat(Integer.toString(bufResult.length));
            byte[] resultLengthBuf = resultLength.getBytes();

            DatagramPacket resultLengthToClient = new DatagramPacket(resultLengthBuf, resultLengthBuf.length, this.clientAddress, this.clientPort);
            this.udpSocket.send(resultLengthToClient);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean sendACK(){
        try{
            DatagramPacket ackOutgoing = new DatagramPacket(bufACK, bufACK.length, this.clientAddress, this.clientPort);
            this.udpSocket.send(ackOutgoing);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean sendCommand(String message){
        try{
            byte[] bufferCommand = message.getBytes();
            DatagramPacket outgoingCommandPacket = new DatagramPacket(bufferCommand, bufferCommand.length, this.clientAddress, this.clientPort);
            this.udpSocket.send(outgoingCommandPacket);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Integer parseLength(String lengthMessage){
        Integer incomingLength = 0;
        String [] parseCommand = lengthMessage.split(" ");
        ArrayList<String> parsedArgsArrList = new ArrayList<String>();
        for(String c : parseCommand){
            parsedArgsArrList.add(c);
            //System.out.println(c);
        }
        if(parsedArgsArrList.size() == 3){
            if(parsedArgsArrList.get(0).equals("length") && parsedArgsArrList.get(1).equals("=")){
                incomingLength = Integer.valueOf(parsedArgsArrList.get(2));
            }
        }
        return incomingLength;
    }


    // private String receiveLength(){
    //     //Integer incomingLength = 0;
    //     byte [] bufferLength = new byte [512];
    //     DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
    //     try{
    //         this.udpSocket.receive(incoming);
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
    //     String incomingLengthMessage = new String(incoming.getData(), 0, incoming.getLength());
    //     System.out.println("Returning incomingLengthMessage");
    //     System.out.println(incomingLengthMessage);
    //     return incomingLengthMessage;
    // }

    private Boolean receiveACK(){

        String isACK = receiveCommand(bufACK.length);
        if(isACK.equals(ACK)){
            return true;
        }
        else{
            return false;
        }

    }

    private String receiveCommand(Integer expectedLength){
        String receivedString;
        try{
            byte[] receiveBuf = new byte[expectedLength];
            DatagramPacket receivedPacket = new DatagramPacket(receiveBuf, receiveBuf.length);
            this.udpSocket.receive(receivedPacket);
            receivedString = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
        }catch(Exception e){
            e.printStackTrace();
            receivedString = "Error"; 
        }
        return receivedString;
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
