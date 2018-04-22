import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;


class server_java_tcp{
private Integer ListeningPort;
private ServerSocket tcpServerSocket;
private Socket tcpSocket;
private BufferedReader bufInStream;
private ObjectInputStream inStream;
private OutputStreamWriter outStream;
private BufferedWriter bufOutStream;
private static Scanner scanner;

public void setListeningPort(Integer lp){
    this.ListeningPort = lp;
}

void setupTCPServer(){
    while(true){
        try{
            //create new server socket and listen
            this.tcpServerSocket = new ServerSocket(this.ListeningPort);
            System.out.println("Server waiting for request");
            this.tcpSocket = this.tcpServerSocket.accept();
            
            System.out.println("SocketAccepted");

            //set up streams
            this.inStream = new ObjectInputStream(this.tcpSocket.getInputStream());
            //this.bufInStream = new BufferedReader(this.inStream);
            
            System.out.println("Input streams created");

            //get terminal command from client
            String message = (String) this.inStream.readObject();
            //bufInStream.readLine();

            System.out.print("Message is: ");
            System.out.println(message);

            //run command on server
            String outputToClient = processMessage(message);

            System.out.println("Ran command, output is: ");
            System.out.println(outputToClient);

            //send output back to client
            this.outStream = new OutputStreamWriter(this.tcpSocket.getOutputStream());
            this.bufOutStream = new BufferedWriter(this.outStream);
            this.bufOutStream.write(outputToClient);

            //close streams
            // this.bufInStream.close();
            // this.bufOutStream.close();
            // this.inStream.close();
            // this.outStream.close();
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
            processedOutput += bufferString;
        }
        chainFromTerminal.close();

        terminalCommand.waitFor();
        terminalCommand.destroy();
    }catch(Exception e){
        e.printStackTrace();
    }
    return processedOutput;
}


public static void main(String args[]){
    String IPAddress = "";
    try{
        InetAddress ipAddress = InetAddress.getLocalHost(); 
        IPAddress = ipAddress.getHostAddress();
    }catch(UnknownHostException uhe){
        uhe.printStackTrace();
    }
    System.out.print("This machine's IP: ");
    System.out.println(IPAddress);

    scanner = new Scanner(System.in);
    System.out.println("Server Listening Port:");
    Integer userInput = Integer.valueOf(scanner.nextLine());

    if((userInput > 0) && (userInput < 65535)){
        server_java_tcp server = new server_java_tcp();
        server.setListeningPort(userInput);
        server.setupTCPServer();
    }
}

}