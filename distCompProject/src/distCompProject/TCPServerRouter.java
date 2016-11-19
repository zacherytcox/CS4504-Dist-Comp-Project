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

public class TCPServerRouter {
	private static int timeout = 0;
    public static Object [][] RoutingTable = new Object [100000][2]; // routing table
	
    //string socket num, string ip address
    public static void main(int numSR, int sockNum, String ip) throws IOException {
    	
    	
    	//adding server routers to table
    	for(int i=0;i<3;i++){
			//System.out.println("mySock: " + sockNum + " option: " +(40000 + (i+1)));
    		RoutingTable[i][0] = ip;
    		RoutingTable[i][1] = 40000 + (i+1);

    	}
    	

    	//System.out.print(Arrays.deepToString(RoutingTable));
        Socket nodeSocket = null; // socket for the thread
        
        
        
        int SockNum = sockNum; // port number
        Boolean Running = true;
        int ind; // indext in the routing table	

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(SockNum);
            serverSocket.setSoTimeout(timeout);
            System.out.println("ServerRouter is Listening on port: " + SockNum);
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
            	
            	
            	//need to get socket and pass to "doesipexist"
            	
            	
            	//System.out.println(Arrays.deepToString(RoutingTable));
            	//get the next available position within table
                ind = getNextNullArrayPostion(RoutingTable);
                int ind2 = doesIpExist(RoutingTable, nodeSocket.getInetAddress().getHostAddress());
                
                if(ind == -1){
                	System.err.println("Routing Table is full!");
                	PrintWriter out;
                	out = new PrintWriter(nodeSocket.getOutputStream(), true);
                	out.println("Full.");
                	break;
                }
                
                //if the ip is already in the table, just send the same ip
                if (ind2 != -1){
                	ind = ind2;
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
    
    //if ip already exists within table
    private static int doesIpExist(Object [][] table, String ip){
    	
    	for(int i = 0; i<10; i++){
    		if(table[i][0] == ip){
    			return i;
    		}
    	}
    	
    	return -1;
    	
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