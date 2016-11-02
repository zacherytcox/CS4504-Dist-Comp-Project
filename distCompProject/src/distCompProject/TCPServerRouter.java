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
import java.io.*;

public class TCPServerRouter {
	private static int timeout = 0;
    public static Object [][] RoutingTable = new Object [10][2]; // routing table
	
    public static void main(String[] args) throws IOException {
        Socket nodeSocket = null; // socket for the thread

        int SockNum = 5555; // port number
        Boolean Running = true;
        int ind; // indext in the routing table	
        
        //Multicast Stuff
        InetAddress group = InetAddress.getByName("239.6.6.5");
        MulticastSocket mulSocket = new MulticastSocket(4504);
        mulSocket.joinGroup(group);
        

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

        GThread g = new GThread(mulSocket, RoutingTable, nodeSocket);
        g.run();
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
                SThread t = new SThread(RoutingTable, nodeSocket, ind, mulSocket); // creates a thread with a random port
                
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
        mulSocket.close();

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