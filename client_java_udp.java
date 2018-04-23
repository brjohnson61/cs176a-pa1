import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

class client_java_udp{
    private DatagramSocket udpSocket;
    private InetAddress serverIPAddress;
    private Integer port;
    private static Scanner scanner;
    private static final String ACK = "ACK";
    private static final byte [] bufACK = ACK.getBytes();


    public void connect(InetAddress IPAddress, Integer portInput, String command, String fileName){
        try{
            this.udpSocket = new DatagramSocket();
            this.port = portInput;
            this.serverIPAddress = IPAddress;
            
            if(sendLength(command)){
                System.out.println("Sent command length");
            }
            else{
                System.out.println("Did not send command length successfully");
            }
            if(receiveACK()){
                System.out.println("ACK received");
            }
            else{
                System.out.println("Did not receive ACK");
            }

            if(sendCommand(command)){
                System.out.println("Command Packet Sent");
            }
            else{
                System.out.println("Command Packet not sent");
            }
            if(receiveACK()){
                System.out.println("Server received command");
            }
            else{
                System.out.println("Server did not receive command");
            }
            String commandLength = receiveLength();
            commandLength = Integer.toString(parseLength(commandLength));
            System.out.println(commandLength);
            System.out.println("Received command length");
            sendACK();
            String finalOutput = receiveCommand(Integer.valueOf(commandLength));
            sendACK();
            
            BufferedWriter toFile = new BufferedWriter(new FileWriter(fileName, true));
            toFile.write(finalOutput);
            toFile.close();
            System.out.println(finalOutput);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        this.udpSocket.close();
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

    private Boolean sendCommand(String message){
        try{
            byte[] bufferCommand = message.getBytes();
            DatagramPacket outgoingCommandPacket = new DatagramPacket(bufferCommand, bufferCommand.length, this.serverIPAddress, this.port);
            this.udpSocket.send(outgoingCommandPacket);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean sendACK(){
        try{
            byte[] ackbuf = ACK.getBytes();
            DatagramPacket ackOutgoing = new DatagramPacket(ackbuf, ackbuf.length, this.serverIPAddress, this.port);
            this.udpSocket.send(ackOutgoing);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String receiveLength(){
        byte [] bufferLength = new byte [512];
        DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
        try{
            this.udpSocket.receive(incoming);
        }catch(Exception e){
            e.printStackTrace();
        }
        String incomingLengthMessage = new String(incoming.getData(), 0, incoming.getLength());
        return incomingLengthMessage;
    }

    private Boolean receiveACK(){

        String isACK = receiveCommand(bufACK.length);
        if(isACK.equals(ACK)){
            return true;
        }
        else{
            return false;
        }

    }

    private Boolean sendLength(String message){
        try{
            byte[] bufResult = message.getBytes();
            String lengthEquals = "length = ";
            String resultLength = lengthEquals.concat(Integer.toString(bufResult.length));
            byte[] resultLengthBuf = resultLength.getBytes();

            DatagramPacket resultLengthToClient = new DatagramPacket(resultLengthBuf, resultLengthBuf.length, this.serverIPAddress, this.port);
            this.udpSocket.send(resultLengthToClient);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
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


    public static void main(String[] args) {
        String IPAddress = "";
        Integer port;
        String command;
        String fileName = "";
        String COULD_NOT_CONNECT = "Could not connect to server.";
        String INVALID_PORT = "Invalid port number";
        String FAIL_SEND = "Failed to connect to server. Terminating.";
        String CANNOT_FETCH = "Could not fetch file";
        
        System.out.print("Enter server name or IP address: ");
        scanner = new Scanner(System.in);
        IPAddress = scanner.nextLine();
        ArrayList<String> ipParse = new ArrayList<String>();

        String[] ipSplit = IPAddress.split(".");
        for(String c : ipSplit){
            ipParse.add(c);
        }

        System.out.print("iParse size: ");
        System.out.println(ipParse.size());

        System.out.print("Enter server port number:");
        port = Integer.valueOf(scanner.nextLine());
        if(port < 0 || port > 65535){
            System.out.println(INVALID_PORT);
            System.exit(0);
        }

        System.out.print("Enter command: ");
        command = scanner.nextLine();
        client_java_udp client = new client_java_udp();

        System.out.print("Command is: ");
        System.out.println(command);

        System.out.println("Filename: ");
        fileName = scanner.nextLine();
        try{
            InetAddress ip = InetAddress.getByName(IPAddress);
            client.connect(ip, port, command, fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}