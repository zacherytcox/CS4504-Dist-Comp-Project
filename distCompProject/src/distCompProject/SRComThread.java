package distCompProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SRComThread extends Thread{
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, destinationSock, addr, ip, name; // communication strings
	private int mySockNum, numSR; // indext in the routing table
	private Socket socket, outSocket;
	private static int timeout = 60000;
	public File f;
	
	SRComThread(Object [][] table, String myName, int thisSockNumber , String myIp, int SRs, File file) throws IOException{

		
		socket = null;
        RTable = table;
        ip = myIp;
        mySockNum = thisSockNumber + 10000;
        name = myName;
        f = file;
        numSR = SRs;
        
        
		
	}
	
	@SuppressWarnings("resource")
	public void run(){

        Boolean Running = true;
		
        try {
        	        	
            //Accepting connections
        	ServerSocket serverSocket = null; // server socket for accepting connections
            try {
            	serverSocket = new ServerSocket(mySockNum);
                serverSocket.setSoTimeout(timeout);
                RunPhase2.addToLogFile(f, name + " is Listening for SRs on port: " + mySockNum);
                //System.out.println(name + "is Listening on port: " + mySockNum);
            }
            catch (IOException e) {
                System.err.println("Could not listen on port: " + mySockNum);
                System.exit(1);
            }
            
            
            
            
	
        	//RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));


			while (Running == true) {
				
	        	//create a block for a request from a node
	        	try{
	        		Socket socket = null;
	        		
	        		socket = serverSocket.accept(); 
	        		
	        		
					out = new PrintWriter(socket.getOutputStream(), true);
		            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        		
	        		
	        	}
	            catch(SocketTimeoutException e){
	            	System.err.println("Socket Timeout! 60 Seconds!");
	            	return;
	            }
					
	        	destination = in.readLine();
	        	
					Boolean found = false;
					Socket tmpSock = null;
					
					System.out.println(name + " " + Arrays.deepToString(RTable));
					for ( int i=0; i<RTable.length; i++){
						if(RTable[i][0] != null ){
							tmpSock = (Socket) RTable[i][1];
							int tmpPort = tmpSock.getPort();
							int tmp2Port = Integer.parseInt(destination);
							System.out.println(tmpPort + " and " + destination);
							if (tmpPort == tmp2Port){
								System.out.println("match!!");
								tmpSock = (Socket) RTable[i][1]; // gets the socket for communication from the table
								
								PrintWriter tmpOut = new PrintWriter(tmpSock.getOutputStream(), true);
					            BufferedReader tmpIn = new BufferedReader(new InputStreamReader(tmpSock.getInputStream()));
					            
					            tmpOut.println(destination);
								
								found = true;
								break;
							}
						}
					}
					
					if(found == true){
						
						//tell the original that we found it
						out.println("found");
						
						//send destination the packet
						
						outSocket = tmpSock; // gets the socket for communication from the table
						System.out.println("Found destination: " + destinationSock + "\n");
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
						outTo.println(destinationSock);
					} else{
						
						//out.println("no");
						out.println(Arrays.deepToString(RTable));
					}
			

	            
			}
        }
        catch (IOException e) {
            System.err.println("Node failed to connect: " + e);
            return;
        }
		
	}

}
