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

    public void connect(String IPAddress, Integer port, String command, String fileName){
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
        String fileName = "";
        String COULD_NOT_CONNECT = "Could not connect to server.";
        String INVALID_PORT = "Invalid port number";
        String FAIL_SEND = "Failed to connect to server. Terminating.";
        String CANNOT_FETCH = "Could not fetch file";
        
        System.out.print("Enter server name or IP address: ");
        scanner = new Scanner(System.in);
        IPAddress = scanner.nextLine();
        String [] parsed = IPAddress.split(".");
        //Check to see if there aren't 4 sections of IP Address.
        if(parsed.length != 4){
            System.out.println(parsed.length);
            System.out.println(COULD_NOT_CONNECT);
            System.exit(0);
        }
        //Checks to make sure ip is in valid range for each section.
        else{
            for(int i=0; i<parsed.length; i++){
                Integer temp = Integer.valueOf(parsed[i]);
                System.out.print("Temp = ");
                System.out.println(temp);
                if(temp > 255 || temp < 0){
                    System.out.println(COULD_NOT_CONNECT);
                    System.exit(0);
                }
            }
        }

        System.out.print("Enter server port number:");
        port = Integer.valueOf(scanner.nextLine());
        if(port < 0 || port > 65535){
            System.out.println(INVALID_PORT);
            System.exit(0);
        }

        System.out.print("Enter Command: ");
        command = scanner.nextLine();
        client_java_tcp client = new client_java_tcp();
        String [] cmdparse = command.split(" > ");

        if(cmdparse.length > 1){
            System.out.print("Command is: ");
            System.out.println(cmdparse[0]);

            System.out.print("Filename is: ");
            System.out.println(cmdparse[1]);
            client.connect(IPAddress, port, cmdparse[0], cmdparse[1]);
        }
        else{
            System.out.print("Command is: ");
            System.out.println(command);
            client.connect(IPAddress, port, command, fileName);
        }

        
        
}
}