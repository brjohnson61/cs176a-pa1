import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

class client_java_udp{
    private DatagramSocket udpSocket;
    private InetAddress serverIPAddress;
    private Integer port;
    private String clientAddress;
    private static Scanner scanner;
    private static final String ACK = "ACK";
    private static final byte [] bufACK = ACK.getBytes();


    public void connect(InetAddress IPAddress, Integer portInput, String command, String fileName){
        try{
            this.udpSocket = new DatagramSocket();
            this.port = portInput;
            this.serverIPAddress = IPAddress;
            DatagramPacket incomingACK = new DatagramPacket(bufACK, bufACK.length);
            String lengthEquals = "length = ";
            byte[] bufferCommand = command.getBytes();
            Integer outgoingLength = Integer.valueOf(bufferCommand.length);
            String lengthMessage = lengthEquals.concat(Integer.toString(outgoingLength));
            byte[] bufferLengthMessage = lengthMessage.getBytes();
            
            System.out.print("outgoingLength");
            System.out.println(outgoingLength);
            System.out.print("lengthMessage");
            System.out.println(lengthMessage);
            
            DatagramPacket outgoingLengthPacket = new DatagramPacket(bufferLengthMessage, bufferLengthMessage.length, IPAddress, port);
            this.udpSocket.send(outgoingLengthPacket);
            this.udpSocket.receive(incomingACK);
            String incomingACKMessage = new String(incomingACK.getData(), 0, incomingACK.getLength());
            if(incomingACKMessage.equals(ACK)){
                System.out.println("ACK received.");   
            }
            DatagramPacket outgoingCommandPacket = new DatagramPacket(bufferCommand, bufferCommand.length, IPAddress, port);
            System.out.println("Sending command packet");
            this.udpSocket.send(outgoingCommandPacket);
            System.out.println("Command Packet Sent");
            if(receiveACK()){
                System.out.println("Server received command");
            }
            else{
                System.out.println("Server did not receive command");
            }
            String commandLength = receiveLength();
            System.out.println(commandLength);
            System.out.println("Received command");
            sendACK();
            
        }catch(Exception e){
            e.printStackTrace();
        }
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
        //Integer incomingLength = 0;
        byte [] bufferLength = new byte [512];
        DatagramPacket incoming = new DatagramPacket(bufferLength, bufferLength.length);
        try{
            this.udpSocket.receive(incoming);
        }catch(Exception e){
            e.printStackTrace();
        }
        String incomingLengthMessage = new String(incoming.getData(), 0, incoming.getLength());
        System.out.println("Returning incomingLengthMessage");
        System.out.println(incomingLengthMessage);
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

        System.out.print("Enter server port number:");
        port = Integer.valueOf(scanner.nextLine());
        if(port < 0 || port > 65535){
            System.out.println(INVALID_PORT);
            System.exit(0);
        }

        System.out.print("Enter Command: ");
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