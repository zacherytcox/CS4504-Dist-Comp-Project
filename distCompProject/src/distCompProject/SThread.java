package distCompProject;

//Author: Zachery Cox
//Date: 10/11/16

//This code is ran within TCPServerRouter.java


import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.lang.Exception;

	
public class SThread extends Thread {
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, destinationSock, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind, numSR; // indext in the routing table
	private static int timeout = 60000;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index, int numberSR) throws IOException{
		
		toClient.setSoTimeout(timeout);
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses 
        RTable[index][1] = toClient; // sockets for communication
        ind = index;
        numSR = numberSR;
        
	}
	
    private static void removeTableEntry(Object [][] table, String ip, int ind){    	
    	
    	// loops through the routing table to find the route saved and delete it
		for ( int i=0; i<10; i++){
			if (ip.equals((String) table[i][0])){
				table[i][0] = null;
				table[i][1] = null;
				System.out.println("Removed " + ip + " from Routing Table...\n");
			}
		}
    	   	
    }
	
	// Run method (will run for each machine that connects to the ServerRouter)
	@SuppressWarnings("static-access")
	public void run(){
		try{
			

			// Initial sends/receives
			try{
				destination = in.readLine(); // initial read (the destination for writing)
			}catch (SocketTimeoutException e) {
				System.err.println(e);
				out.println("Timeout.");
				
			}
			System.out.println(addr + " Wants to forward to " + destination);
			out.println("Connected to the router."); // confirmation of connection
			
		
			// waits 10 seconds to let the routing table fill with all machines' information
			try{
				Thread.currentThread().sleep(10000); 
			}
			catch(InterruptedException ie){
				System.out.println("Thread interrupted");
			}


			
			// Communication loop	
			while ((inputLine = in.readLine()) != null) {

				destinationSock = inputLine;
	    		
				// loops through the routing table to find the destination in the route table
				System.out.println(Arrays.deepToString(RTable));
				for ( int i=0; i<RTable.length; i++){
					if (destinationSock.equals((String) RTable[i][1])){
						outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
						System.out.println("Found destination: " + destinationSock + "\n");
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
						outTo.println(destinationSock);
						break;
					}

					
				}

				
				System.out.println("Node " + addr + " said: " + inputLine + "\n");
				
				//If "Thread Bye." gets sent, the thread will end
				if (inputLine.toString().equals("Thread Bye.")){ // exit statement
					System.out.println("Thread Terminated for: " + addr + "\n");
					removeTableEntry(RTable, addr, ind);
					break;
				}

				
			}// end while	
			
		}// end try
		catch (IOException e) {
			System.err.println("Could not listen to socket.");
			return;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}