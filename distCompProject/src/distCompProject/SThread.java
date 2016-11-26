package distCompProject;

//Author: Zachery Cox
//Date: 11/25/16


import java.io.*;
import java.net.*;
import java.lang.Exception;

	
public class SThread extends Thread {
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
	private BufferedReader in; // reader (for reading from the machine connected to)
	private String inputLine, nodeSockNum, destinationSock, addr, name, tmp, ip, response; // communication strings
	private Socket outSocket; // socket for communicating with a destination
	private int numSR; // indext in the routing table
	private  int port, port2;
	private static int timeout = 120000;
	public  File f;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index, int numberSR, String thisName, String thisIp, File file) throws IOException{
		
		toClient.setSoTimeout(timeout);
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        port =toClient.getLocalPort();
        port2 = toClient.getPort();
        
        ip = thisIp;
        RTable[index][0] = addr; // IP addresses 
        RTable[index][1] = toClient; // sockets for communication
        numSR = numberSR;
        name = thisName;
        f = file;
        
        
        
        
        
        
	}
	

	
	// Run method (will run for each machine that connects to the ServerRouter)
	@SuppressWarnings("static-access")
	public void run(){
		long t0, t1, t;
		
		try{
			
			RunPhase2.addToLogFile(f, "ADDED: " + name + ": Port " + port2 + " to routing table");
			
			//System.out.println(port);
			// Initial sends/receives
			try{
				nodeSockNum = in.readLine(); // initial read (the destination for writing)
			}catch (SocketTimeoutException e) {
				System.err.println(e);
				RunPhase2.addToLogFile(f, name + ": initial Socket read failed...");
				out.println("Timeout.");
				
			}
			RunPhase2.addToLogFile(f, name + ": " + nodeSockNum + " Wants to forward to " + (Integer.parseInt(nodeSockNum) + 10000));
			out.println("Connected to the router."); // confirmation of connection
			
		
			// waits 10 seconds to let the routing table fill with all machines' information
			try{
				Thread.currentThread().sleep(15000); 
			}
			catch(InterruptedException ie){
				RunPhase2.addToLogFile(f, name + ": Could not put thread to sleep...");
				System.out.println("Thread interrupted");
			}


			
			// Communication loop	
			while ((inputLine = in.readLine()) != null) {

				destinationSock = inputLine;//other sock num
				
				//If "Thread Bye." gets sent, the thread will end
				if (inputLine.toString().equals("Thread Bye.")){ // exit statement
					System.out.println("Thread Terminated for: " + nodeSockNum + "\n");
					//removeTableEntry(RTable, addr, ind, f, tmp);
					break;
				}

				
	    		
				// loops through the routing table to find the destination in the route table
				Boolean found = false;
				
				//System.out.println(Arrays.deepToString(RTable));
				t0 = System.currentTimeMillis();
				for ( int i=0; i<RTable.length; i++){
					if(RTable[i][0] != null){
						Socket tmpSock = ((Socket)RTable[i][1]);
						//System.out.println(tmpSock.getPort() + " IS PORT");
						int port = tmpSock.getPort();
						if (Integer.parseInt(destinationSock) == port){
							found = true;
							//System.out.println(name + " found sock");
							outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
							//System.out.println("Found destination: " + destinationSock + "\n");
							outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
							outTo.println(destinationSock);
							break;
						}
					}
					
				}
				t1 = System.currentTimeMillis();
				
				if (found == false){
				
					RunPhase2.addToLogFile(f, name + ": " + nodeSockNum + " could not find " + destinationSock + " locally!");
					//NEED TO CHECK OTHER SRs!
					

					for (int i = 1;i<=numSR;i++){
						int tmp = i + 40000;
						if(tmp != port){
							RunPhase2.addToLogFile(f, "CHECK: " + name + ": Searching for: " + destinationSock + " at SR: " + tmp );
							int nextSR = (tmp + 10000);
			        		outSocket = new Socket(ip, nextSR);
			        		PrintWriter outOut = new PrintWriter(outSocket.getOutputStream(), true); // creates stream of data
			        		BufferedReader outIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream())); 
			        		
			        		
			        		outOut.println(destinationSock);
			        		//System.out.println(name + " contact " + outSocket);
			        		
			        		response = outIn.readLine();
			        		RunPhase2.addToLogFile(f, "RESPONSE: " + name + ": Looking for " + destinationSock + " at SR: " + tmp + " and sent back: " + response );
			        		//System.out.println(response);
			        		if(response.equals("found")){
			        			found = true;
			        			RunPhase2.addToLogFile(f, name + ": " + outSocket + " found " + destinationSock + "!");
			        			break;
			        			
			        		}
						}
					}
	
					t1 = System.currentTimeMillis();
					//System.exit(1);
					
					t = t1 - t0;
					int tmpp = ((int)t)/1000;
					
					
					RunPhase2.addToSRPerformanceFile(RunPhase2.sr, name, RTable.length,  tmpp);
				}
				if(found == false){
					RunPhase2.addToLogFile(RunPhase2.t, name + " Cant find destination: " + destinationSock);
					RunPhase2.addToLogFile(f, "ERROR! " + name + ": Did not find destination on any of the SRs... " + destinationSock + response);
					System.err.println("Cant find a node. Somethings off...");
					//System.exit(1);
					
				}
				
				System.out.println("Node " + addr + " said: " + inputLine + "\n");
				
				
			}// end while	
			
		}// end try
		catch (IOException e) {
			RunPhase2.addToLogFile(f, name + ": While loop failed with IOExeception on SThread...: " + tmp + " and " + outSocket + e);
			System.err.println("Could not listen to socket. " + tmp);
			return;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}