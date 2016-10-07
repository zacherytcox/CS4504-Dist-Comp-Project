package distCompProject;

//Author: Zachery Cox
//Date: 10/6/16


import java.net.*;
import java.util.Arrays;
import java.io.*;

public class TCPServerRouter {
	private static int timeout = 0;
	
    public static void main(String[] args) throws IOException {
        Socket nodeSocket = null; // socket for the thread
        Object [][] RoutingTable = new Object [10][2]; // routing table
        int SockNum = 5555; // port number
        Boolean Running = true;
        int ind; // indext in the routing table	
        

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(SockNum);
            serverSocket.setSoTimeout(timeout);
            System.out.println("ServerRouter is Listening on port: 5555.");
        }
        catch (IOException e) {
            System.err.println("Could not listen on port: 5555.");
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
            	
            	System.out.println(Arrays.deepToString(RoutingTable));
            	//get the next available position within table
                ind = getNextNullArrayPostion(RoutingTable);
                
                if(ind == -1){
                	System.err.println("Routing Table is full!");
                	PrintWriter out;
                	out = new PrintWriter(nodeSocket.getOutputStream(), true);
                	out.println("Full.");
                	break;
                }
                
                
                //creates a new thread
                SThread t = new SThread(RoutingTable, nodeSocket, ind); // creates a thread with a random port
                
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
        nodeSocket.close();
        serverSocket.close();

    }
    

    private static int getNextNullArrayPostion(Object [][] table){
    	for(int i = 0; i<10; i++){
    		if(table[i][0] == null){
    			return i;
    		}
    	}
    	    	
    	return -1;
    }
    
    
    
}