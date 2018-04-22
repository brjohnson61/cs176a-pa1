import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;


class client_java_tcp{
    private String serverIPAddress;
    private Integer serverPortNumber;
    private InputStreamReader inStream;
    private BufferedReader bufInStream;
    private OutputStreamWriter outStream;
    private BufferedWriter bufOutStream;
    private Socket tcpSocket;
    private static Scanner scanner;

    public void connect(String IPAddress, Integer port, String command){
        String fromServer = "";
        this.serverPortNumber = port;
        this.serverIPAddress = IPAddress;
        try{
            this.tcpSocket = new Socket(this.serverIPAddress, this.serverPortNumber);
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            this.outStream = new OutputStreamWriter(this.tcpSocket.getOutputStream());
            this.bufOutStream = new BufferedWriter(this.outStream);

            this.inStream = new InputStreamReader(this.tcpSocket.getInputStream());
            this.bufInStream = new BufferedReader(this.inStream);

            this.bufOutStream.write(command);
            for(String receive = this.bufInStream.readLine(); receive != null; receive = this.bufInStream.readLine()){
                fromServer += receive;
            }
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

        client_java_tcp client = new client_java_tcp();
        client.connect(IPAddress, port, command);
    }
}