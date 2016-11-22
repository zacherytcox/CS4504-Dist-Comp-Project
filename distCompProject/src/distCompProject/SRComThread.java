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
	
	public void run(){

        Boolean Running = true;
		
        try {
        	        	
            //Accepting connections
        	ServerSocket serverSocket = null; // server socket for accepting connections
            serverSocket = null; // server socket for accepting connections
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

            socket = new Socket(ip, mySockNum); // opens port
            socket.setSoTimeout(timeout);
            
    		out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            
	//SR connectivity
            RunPhase2.addToLogFile(f, name + " Wanting to talk to other SRs!");
            //if first SR
        	if(mySockNum == 50001){
        		
        		int nextSR = mySockNum + 1;
        		outSocket = new Socket(ip, nextSR);
        		PrintWriter outOut = new PrintWriter(outSocket.getOutputStream(), true); // creates stream of data
        		BufferedReader outIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream())); 
        		
        		TimeUnit.SECONDS.sleep(5);
        		
        		outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
        		System.out.println(name + " contact " + outSocket);
        		
        		System.out.println(name + " waiting for input on " + socket);
				socket = serverSocket.accept();
        		
        		String input = in.readLine();
        		System.out.println(input);
        		
        		int index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication
        		RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));
        		
        		nextSR = numSR + 50000;
        		
        		
        		outSocket = new Socket(ip, nextSR); // opens port
				outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
				
				
				socket = serverSocket.accept();
				input = in.readLine();
        		System.out.println(input);
        		
        		index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication
        		RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));
				
        		
        	}
        	//if last SR
        	else if(mySockNum == numSR + 50000){
        		System.out.println(name + " waiting for input on " + socket);
        		socket = serverSocket.accept();
        		
        		String input = in.readLine();
        		System.out.println(input);
        		
        		int index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication
        		RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));
        		
        		
        		int nextSR = 50001;
        		outSocket = new Socket(ip, nextSR);
        		PrintWriter outOut = new PrintWriter(outSocket.getOutputStream(), true); // creates stream of data
        		BufferedReader outIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream())); 
        		
        		outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
        		
        		nextSR = numSR - 1;

				socket = serverSocket.accept();
				input = in.readLine();
        		System.out.println(input);
        		
        		index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication
        		RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));

        		
        		outSocket = new Socket(ip, nextSR); // opens port
				outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
				
				
				
        		
        	}
        	//if other
        	else{

        		System.out.println(name + " waiting for input on " + socket);
        		socket = serverSocket.accept();
				
        		String input = in.readLine();
        		System.out.println(input);
        		
        		int index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication
        		RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));
        		
        		
        		int nextSR = mySockNum + 1;
        		outSocket = new Socket(ip, nextSR);
        		PrintWriter outOut = new PrintWriter(outSocket.getOutputStream(), true); // creates stream of data
        		BufferedReader outIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream())); 
        		
        		outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
        		
        		nextSR = numSR - 1;

				socket = serverSocket.accept();
				input = in.readLine();
        		System.out.println(input);
        		
        		index = TCPServerRouter.getNextNullArrayPostion(RTable);
        		RTable[index][0] = ip; // IP addresses 
        		RTable[index][1] = socket; // sockets for communication

        		
        		outSocket = new Socket(ip, nextSR); // opens port
				outOut.println(name + " wants to know you!");
        		System.out.println(name + " wants to know you!");
				
        		
        	}
        	RunPhase2.addToLogFile(f, name + " Knows other SRs!");
        	RunPhase2.addToLogFile(f, name + Arrays.deepToString(RTable));


			while (Running == true) {
				
	        	//create a block for a request from a node
	        	try{
	        		socket = serverSocket.accept(); 
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
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
