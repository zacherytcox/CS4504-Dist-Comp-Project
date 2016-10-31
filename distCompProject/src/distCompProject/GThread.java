package distCompProject;

//Author: Zachery Cox
//Date: 10/11/16

//This code is ran within TCPServerRouter.java


import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class GThread extends Thread {
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
	private static int timeout = 60000;
	private MulticastSocket multiSocket;

	// Constructor
	GThread(MulticastSocket toGroup, Object [][] Table) throws IOException{
		
		toClient.setSoTimeout(timeout);
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses 
        RTable[index][1] = toClient; // sockets for communication
        ind = index;
        multiSocket = toGroup;
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
			

			System.out.println(addr + " Thread Created...");
            System.out.println();
            System.out.println();
			
            
    		
    		
    		//lookup table variable times
    		long rtl0, rtl1, rtlt;
            
            
			// Communication loop	
			while ((inputLine = in.readLine()) != null) {
				
	    		
	    		//get initial time
	    		rtl0 = System.currentTimeMillis();
	    		
				// loops through the routing table to find the destination in the route table
				for ( int i=0; i<10; i++){
					if (destination.equals((String) RTable[i][0])){
						outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
						System.out.println("Found destination: " + destination + "\n");
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
					}
				}
				
				rtl1 = System.currentTimeMillis();
				rtlt = rtl0 - rtl1;
				//Prints out Routing Table lookup time
				//System.out.println(rtlt);
				
				System.out.println("Node " + addr + " said: " + inputLine + "\n");
				
				//If "Thread Bye." gets sent, the thread will end
				if (inputLine.toString().equals("Thread Bye.")){ // exit statement
					System.out.println("Thread Terminated for: " + addr + "\n");
					removeTableEntry(RTable, addr, ind);
					break;
				}
				
				
				outputLine = inputLine; // passes the input from the machine to the output string for the destination
				
				// if the socket is not null and actually has a port, it will send out the response from outputLine
				if ( outSocket != null){				
					outTo.println(outputLine); // writes to the destination
					System.out.println("");
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