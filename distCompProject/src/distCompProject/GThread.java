package distCompProject;

//Author: Zachery Cox
//Date: 10/11/16

//This code is ran within TCPServerRouter.java


import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class GThread extends Thread {
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, outputLine, destination, addr; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table
	private static int timeout = 60000;
	private MulticastSocket multiSocket;
	private String packetString;
	private String toAddress, fromAddress;

	// Constructor
	GThread(MulticastSocket toGroup, Object [][] RTable, Socket nodeSocket) throws IOException{
		
		nodeSocket.setSoTimeout(timeout);
        out = new PrintWriter(nodeSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
        addr = nodeSocket.getInetAddress().getHostAddress();
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
			



			// waits 10 seconds to let the routing table fill with all machines' information
			try{
				Thread.currentThread().sleep(10000); 
			}
			catch(InterruptedException ie){
				System.out.println("Thread interrupted");
			}
    		
    		//lookup table variable times
    		long rtl0, rtl1, rtlt;
            
    		
    		byte[] buf = new byte[1000];
    		DatagramPacket recv = new DatagramPacket(buf, buf.length);
            
			// Communication loop	
			while (1==1) { //inputLine
				
				multiSocket.receive(recv);
				packetString = recv.toString();
				
				
				//parse through packetstring for ip address
				toAddress = "THIS NEEDS TO BE DEST ADDR";
				fromAddress = "THIS NEEDS TO BE SOURCE ADDR";
				
	    		
	    		//get initial time
	    		rtl0 = System.currentTimeMillis();
	    		
				// loops through the routing table to find the destination in the route table
				for ( int i=0; i<10; i++){
					if (RTable[i][0]).equals(toAddress)){
						//SEND BY TCP TO THE NODE ON THIS SERVER ROUTER
						out.println(packetString);
						//BOMB OUT AFTER
					}
				}
				
					
				rtl1 = System.currentTimeMillis();
				rtlt = rtl0 - rtl1;
				//Prints out Routing Table lookup time
				//System.out.println(rtlt);
					
				
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