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
	private String inputLine, outputLine, nodeSockNum, destinationSock, addr, name; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int ind, numSR; // indext in the routing table
	private static int timeout = 60000;
	public File f;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index, int numberSR, String thisName, File file) throws IOException{
		
		toClient.setSoTimeout(timeout);
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses 
        RTable[index][1] = toClient; // sockets for communication
        RTable[index][2] = thisName;
        ind = index;
        numSR = numberSR;
        name = thisName;
        f = file;
        
        
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
				nodeSockNum = in.readLine(); // initial read (the destination for writing)
			}catch (SocketTimeoutException e) {
				System.err.println(e);
				out.println("Timeout.");
				
			}
			RunPhase2.addToLogFile(f, name + ": " + nodeSockNum + " Wants to forward to " + (Integer.parseInt(nodeSockNum) + 10000));
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

				destinationSock = inputLine;//other sock num
				
				//If "Thread Bye." gets sent, the thread will end
				if (inputLine.toString().equals("Thread Bye.")){ // exit statement
					System.out.println("Thread Terminated for: " + nodeSockNum + "\n");
					removeTableEntry(RTable, addr, ind);
					break;
				}

				
	    		
				// loops through the routing table to find the destination in the route table
				System.out.println(Arrays.deepToString(RTable));
				for ( int i=0; i<RTable.length; i++){
					if(RTable[i][0] != null){
						Socket tmpSock = ((Socket)RTable[i][1]);
						System.out.println(tmpSock.getPort() + " IS PORT");
						int port = tmpSock.getPort();
						if (Integer.parseInt(destinationSock) == port){
							System.out.print(name + " found sock");
							outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
							System.out.println("Found destination: " + destinationSock + "\n");
							outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
							outTo.println(destinationSock);
							break;
						}
					}
					
					RunPhase2.addToLogFile(f, nodeSockNum + " could not find " + destinationSock + "!");
					//NEED TO CHECK OTHER SRs!
					System.exit(1);
					
				}

				
				System.out.println("Node " + addr + " said: " + inputLine + "\n");
				
				
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