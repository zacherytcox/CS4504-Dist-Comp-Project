package distCompProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class SRComThread extends Thread{
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, destinationSock, addr, ip, name; // communication strings
	private int mySockNum, numSR; // indext in the routing table
	private Socket socket, outSocket;
	private static int timeout = 60000;
	public File f;
	
	SRComThread(Object [][] table, String myName, int thisSockNumber , Socket thisSocket, String myIp, File file) throws IOException{

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        RTable = table;
        addr = socket.getInetAddress().getHostAddress();
        ip = myIp;
        mySockNum = thisSockNumber + 10000;
        name = myName;
        f = file;
        socket = thisSocket;
        
		
	}
	
	public void run(){
		
        try {
        	
            //Accepting connections
            ServerSocket serverSocket = null; // server socket for accepting connections
            try {
                serverSocket = new ServerSocket(mySockNum);
                serverSocket.setSoTimeout(timeout);
                RunPhase2.addToLogFile(f, name + " is Listening on port: " + mySockNum);
                //System.out.println(name + "is Listening on port: " + SockNum);
            }
            catch (IOException e) {
                System.err.println("Could not listen on port: " + mySockNum);
                System.exit(1);
            }

        	//create a block for a request from a node
        	try{
        		socket = serverSocket.accept(); 
        	}
            catch(SocketTimeoutException e){
            	System.err.println("Socket Timeout! 60 Seconds!");
            	return;
            }
        	
        	
			while ((destination = in.readLine()) != null) {
					
					
					Boolean found = false;
					Socket tmpSock = null;
					
					for ( int i=0; i<RTable.length; i++){
						if (destinationSock.equals((String) RTable[i][1])){
							tmpSock = (Socket) RTable[i][1]; // gets the socket for communication from the table
							found = true;
							break;
						}
					}
					
					if(found){
						
						//tell the original that we found it
						out.println("yes");
						
						//send destination the packet
						
						outSocket = tmpSock; // gets the socket for communication from the table
						System.out.println("Found destination: " + destinationSock + "\n");
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
						outTo.println(destinationSock);
					} else{
						
						out.println("no");
					}
			

	            
			}
        }
        catch (IOException e) {
            System.err.println("Node failed to connect: " + e);
            return;
        }
		
	}

}
