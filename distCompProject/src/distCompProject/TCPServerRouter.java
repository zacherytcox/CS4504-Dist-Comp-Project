package distCompProject;

//Author: Zachery Cox
//Date: 11/25/16


import java.net.*;
import java.io.*;

public class TCPServerRouter extends Thread {
	private static int RTMax = 2000;
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


    public void run(){

    	
        Socket nodeSocket = null; // socket for the thread
        int SockNum = sockNum; // port number
        Boolean Running = true;
        int ind = 0; // indext in the routing table	

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(SockNum);
            serverSocket.setSoTimeout(timeout);
            RunPhase2.addToLogFile(f, name + " is Listening on port: " + SockNum);
            
            
            Thread srct = new SRComThread(RoutingTable, name, sockNum, ip, numSR, f );
   	        srct.start();  
            
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


                //creates a new thread
                SThread t = new SThread(RoutingTable, nodeSocket, ind, numSR, name, ip,  f); // creates a thread with a random port
                
                //executes the run method within the SThread object
                t.start(); // starts the thread
                
                ind++;
                //data + space
                System.out.println("ServerRouter connected with Node: " + nodeSocket.getPort());
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