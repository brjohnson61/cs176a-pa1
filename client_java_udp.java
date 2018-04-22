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

    public void connect(InetAddress IPAddress, Integer port, String command, String fileName){
        try{
            this.udpSocket = new DatagramSocket();
        }catch(Exception e){
            e.printStackTrace();
        }
        byte[] buffer = command.getBytes();
        DatagramPacket outgoing = new DatagramPacket(buffer, buffer.length, IPAddress, port);
        try{
            this.udpSocket.send(outgoing);
        }catch(Exception e){
            e.printStackTrace();
        }
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