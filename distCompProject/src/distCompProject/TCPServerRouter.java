package distCompProject;

import java.net.*;
import java.util.concurrent.TimeoutException;
import java.io.*;

public class TCPServerRouter {
    public static void main(String[] args) throws IOException {
        Socket nodeSocket = null; // socket for the thread
        Object [][] RoutingTable = new Object [10][2]; // routing table
        int SockNum = 5555; // port number
        Boolean Running = true;
        int ind; // indext in the routing table	

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(5555);
            serverSocket.setSoTimeout(60000);
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
                //get the next available position within table
                ind = getNextNullArrayPostion(RoutingTable);
                
                if(ind == -1){
                	System.err.println("Routing Table is full!");
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
    
    //This method, given a table, will return the amount of non null values within table
    private static int getNonNullArrayLenth(Object [][] table){
    	for(int i = 0; i<10; i++){
    		if(table[i][0] == null){
    			return i;
    		}
    	}
    	    	
    	return table.length;
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