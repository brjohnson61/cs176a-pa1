import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;


class client_java_tcp{
    private String serverIPAddress;
    private Integer serverPortNumber;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket tcpSocket;
    private static Scanner scanner;

    public void connect(String IPAddress, Integer port, String command){
        String fromServer = "";
        this.serverPortNumber = port;
        this.serverIPAddress = IPAddress;
        try{
            this.tcpSocket = new Socket(this.serverIPAddress, this.serverPortNumber);
            System.out.println("Socket created successfully");
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            System.out.println("Setting up streams");
            this.outStream = new ObjectOutputStream(this.tcpSocket.getOutputStream());
            this.inStream = new ObjectInputStream(this.tcpSocket.getInputStream());

            System.out.print("Streams set up, about to write command:");
            System.out.println(command);
            this.outStream.writeObject(command);
            System.out.println("Wrote command to server");
            fromServer = (String) this.inStream.readObject();

            System.out.print("Message received: ");
            System.out.println(fromServer);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String args[]){      
        String IPAddress = "";
        Integer port;
        String command;
        System.out.print("Enter server name or IP address: ");
        scanner = new Scanner(System.in);
        IPAddress = scanner.nextLine();
        
        System.out.print("Enter server port number:");
        port = Integer.valueOf(scanner.nextLine());

        System.out.print("Enter Command: ");
        command = scanner.nextLine();

        System.out.print("Command is: ");
        System.out.println(command);

        client_java_tcp client = new client_java_tcp();
        client.connect(IPAddress, port, command);
    }
}