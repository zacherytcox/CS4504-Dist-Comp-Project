package distCompProject;

//Author: Zachery Cox
//Date: 10/11/16

//This code is ran within TCPServerRouter.java


import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class GThread extends Thread {
	private PrintWriter out; // writers (for writing back to the machine and to destination)
	private Socket outSocket; // socket for communicating with a destination
	private static int timeout = 60000;
	private MulticastSocket multiSocket;
	private String packetString;
	private String toAddress, fromAddress;
	Object [][] RTable = TCPServerRouter.RoutingTable;
	private String delims = "[ ]+";
	private String [] packetParts;


	// Constructor
	GThread(MulticastSocket toGroup, Socket nodeSocket) throws IOException{

        out = new PrintWriter(nodeSocket.getOutputStream(), true);
		nodeSocket.setSoTimeout(timeout);
        multiSocket = toGroup;
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
			while (true) { //inputLine
				RTable = TCPServerRouter.RoutingTable;
				
				multiSocket.receive(recv);
				packetString = recv.toString();
				
				//parse through packetstring for ip address
				packetParts = packetString.split(delims);

				toAddress = packetParts[0];
				fromAddress = packetParts[2];
				
	    		
	    		//get initial time
	    		rtl0 = System.currentTimeMillis();
	    		
				// loops through the routing table to find the destination in the route table
				for ( int i=0; i<10; i++){
					if ((RTable[i][0]).equals(toAddress)){
						//SEND BY TCP TO THE NODE ON THIS SERVER ROUTER
						out.println(packetString);
						//BOMB OUT AFTER
						break;
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