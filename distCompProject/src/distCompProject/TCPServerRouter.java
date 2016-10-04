package distCompProject;

import java.net.*;
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
            System.out.println("ServerRouter is Listening on port: 5555.");
        }
        catch (IOException e) {
            System.err.println("Could not listen on port: 5555.");
            System.exit(1);
        }
        
        // Creating threads with accepted connections
        while (Running == true){
            try {
                nodeSocket = serverSocket.accept();
                ind = getNonNullArrayLenth(RoutingTable);
                SThread t = new SThread(RoutingTable, nodeSocket, ind); // creates a thread with a random port
                t.start(); // starts the thread
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
    
    
    private static int getNonNullArrayLenth(Object [][] table){
    	
    	for(int i = 0; i<10; i++){
    		if(table[i][0] == null){
    			return i;
    		}
    	}
    	    	
    	return table.length;
    }
    
    
    
}