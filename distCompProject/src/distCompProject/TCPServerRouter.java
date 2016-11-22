package distCompProject;

//Author: Zachery Cox
//Date: 10/11/16

/*//////////////////////////////// INSTRUCTIONS ////////////////////////////////////////
NOTE:
Server Node MUST connect to server router first. Error otherwise.
Just run script to allow this node to be ran as a server router.
It will operate on port 5555.
No GUI, since user interaction will be minimal
//////////////////////////////////////////////////////////////////////////////////*/


import java.net.*;
import java.util.Arrays;
import java.io.*;

public class TCPServerRouter extends Thread {
	private static int RTMax = 100;
	private int timeout = 0;
    private Object [][] RoutingTable = new Object [RTMax][2]; // routing table
    private String name, ip;
    private int numSR, sockNum;
    public static File f;
    
    TCPServerRouter (String thisName, int thisNumSR, int thisSockNum, String thisIp, File file){
    	name = thisName;
    	numSR = thisNumSR;
    	sockNum = thisSockNum;
    	ip = thisIp;
    	f = file;
    	
    	
    }

    

    private static int getNextNullArrayPostion(Object [][] table){
    	for(int i = 0; i<RTMax; i++){
    		if(table[i][0] == null){
    			return i;
    		}
    	}
    	    	
    	return -1;
    }
    
    
    public void run(){

    	
        Socket nodeSocket = null; // socket for the thread
        int SockNum = sockNum; // port number
        Boolean Running = true;
        int ind; // indext in the routing table	

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(SockNum);
            serverSocket.setSoTimeout(timeout);
            RunPhase2.addToLogFile(f, name + " is Listening on port: " + SockNum);
            //System.out.println(name + "is Listening on port: " + SockNum);
            
            
//            Thread srct = new SRComThread(RoutingTable, name, sockNum, ip, f );
//   	        srct.start();  
//            
//            
            //SR connectivity
            
            //if first SR
        	if(sockNum == 50001){
        		int nextSR = sockNum+1;
        		
				Socket thisSocket = new Socket(ip, nextSR); // opens port
				thisSocket.setSoTimeout(timeout);
				PrintWriter out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
				
				serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		String input = in.readLine();
        		
        		int index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
        		
        		nextSR = numSR + 50000;
        		
        		thisSocket = new Socket(ip, nextSR); // opens port
        		thisSocket.setSoTimeout(timeout);
				out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
				
				serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		input = in.readLine();
        		
        		index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
				
				
        		
        	}
        	//if last SR
        	else if(sockNum == numSR + 50000){
        		
        		serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		String input = in.readLine();
        		
        		int index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
        		
        		int nextSR = 50001;
        		
        		Socket thisSocket = new Socket(ip, nextSR); // opens port
        		thisSocket.setSoTimeout(timeout);
        		PrintWriter out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
				
				
				serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		input = in.readLine();
        		
        		index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
        		
        		nextSR = sockNum - 1 + 10000;
        		
        		thisSocket = new Socket(ip, nextSR); // opens port
        		thisSocket.setSoTimeout(timeout);
        		out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
        		
        	}
        	//if other
        	else{
        		serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		String input = in.readLine();
        		
        		int index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
        		
        		int nextSR = sockNum + 10001;
        		
        		Socket thisSocket = new Socket(ip, nextSR); // opens port
        		thisSocket.setSoTimeout(timeout);
        		PrintWriter out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
				
				
				serverSocket = new ServerSocket(SockNum);
				nodeSocket = serverSocket.accept();
				
				in = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream())); 
        		input = in.readLine();
        		
        		index = getNextNullArrayPostion(RoutingTable);
        		RoutingTable[index][0] = ip; // IP addresses 
        		RoutingTable[index][1] = nodeSocket; // sockets for communication
        		
        		nextSR = sockNum - 1 + 10000;
        		
        		thisSocket = new Socket(ip, nextSR); // opens port
        		thisSocket.setSoTimeout(timeout);
        		out = new PrintWriter(thisSocket.getOutputStream(), true);
				out.println(name + " wants to know you!");
        		
        	}
        	
        	serverSocket = new ServerSocket(SockNum);
            
            
        }
        catch (IOException e) {
            System.err.println("Could not listen on port: " + SockNum);
            System.exit(1);
        }

        
        // Creating threads with accepted connections
        while (Running == true){
            try {
            	//create a block for a request from a node
            	try{
            		nodeSocket = serverSocket.accept(); 
            	}
                catch(SocketTimeoutException e){
                	System.err.println("Socket Timeout! 60 Seconds!");
                	return;
                }

                ind = getNextNullArrayPostion(RoutingTable);

                if(ind == -1){
                	System.out.println(name +  Arrays.deepToString(RoutingTable));
                	System.err.println("Routing Table is full!");
                	PrintWriter out;
                	out = new PrintWriter(nodeSocket.getOutputStream(), true);
                	out.println("Full.");
                	break;
                }

                //creates a new thread
                SThread t = new SThread(RoutingTable, nodeSocket, ind, numSR); // creates a thread with a random port
                
                //executes the run method within the SThread object
                t.start(); // starts the thread
                
                //data + space
                System.out.println("ServerRouter connected with Node: " + nodeSocket.getInetAddress().getHostAddress());
                System.out.println();
                System.out.println();
            }
            catch (IOException e) {
                System.err.println("Node failed to connect: " + e);
                return;
            }
            
        }//end while

        
        //closing connections
        try {
			nodeSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	
    	
    }
}