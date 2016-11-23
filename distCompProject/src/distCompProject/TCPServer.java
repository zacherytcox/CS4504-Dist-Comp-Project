package distCompProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Random;

public class TCPServer extends Thread {
	private String ip, name;
	private int numSR, mySock;
	public static File f = null;
	
	TCPServer(String myName, String ipAddr, int socket, int numberOfSR, File file){
		
		ip = ipAddr;
		mySock = socket;
		numSR = numberOfSR;
		name = myName;
		f = file;
		
	}
	
	
	public void run(){
		try{
			// Variables for setting up connection and communication
			Socket Socket = null; // socket to connect with ServerRouter
			PrintWriter out = null; // for writing to ServerRouter
			BufferedReader in = null; // for reading from ServerRouter
			int mySockNum = mySock; // port number
			int clientSockNum = mySockNum - 10000;
			int timeout = 60000;
			
			Random rand = new Random();
			//pick random ServerRouter socket number
			int srSockNum = 40000 + rand.nextInt(numSR) + 1;
			//srSockNum = 40001;
			
			RunPhase2.addToLogFile(f, name + " Connecting to SR: " + srSockNum);
	
			
			
			// Tries to connect to the ServerRouter
			try {
				System.out.println("Connect to ServerRouter...");	
				Socket = new Socket(ip, srSockNum,InetAddress.getByName(ip), mySockNum);
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println(name + " Don't know about router: SR-" + srSockNum);
				return;
				
			} catch (IOException e) {
				System.err.println("Server: Couldn't get I/O for the connection to: " + srSockNum);
				return;
				
			}
	
			// Variables for message passing
			String fromServer = ""; // messages sent to ServerRouter
			String fromClient = ""; // messages received from ServerRouter
			
			// Communication process (initial sends/receives)
			out.println(mySockNum);// initial send (IP of the destination Client)
			
			try{

				fromClient = in.readLine();// initial receive from router (verification of connection)
				if(fromServer.toString().equals("Full.")){
					System.err.println("Routing Table is full!");
					Socket.close();
					return;
				}

			}catch(SocketTimeoutException e){
				System.err.println(e);
				try {
					Socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ServerRouter: " + fromClient);
	

			try{

				fromClient = in.readLine();// waiting on client to handshake

			}catch(SocketTimeoutException e){
				System.err.println("Waiting on client, timeout");
				try {
					Socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ServerRouter: " + fromClient);
			
			out.println(clientSockNum);
			
			out.println("Thread Bye.");
			
			Socket.close();
			
			

			RunPhase2.addToLogFile(f, name + " Ready to communicate with " + clientSockNum + " directly!");
			
			
	        Socket clientSocket = null; // socket for the thread
	        //Accepting connections
	        ServerSocket serverSocket = null; // server socket for accepting connections
	        try {
	            serverSocket = new ServerSocket(mySockNum);
	            serverSocket.setSoTimeout(timeout);
	            //System.out.println(name + "is Listening on port: " + SockNum);
	        }
	        catch (IOException e) {
	            System.err.println("Could not listen on port: " + mySockNum);
	            System.exit(1);
	        }
			
	        
	        //Waiting on client to send request directly
            try {
            	//create a block for a request from a node
            	try{
            		clientSocket = serverSocket.accept(); 
            		RunPhase2.addToLogFile(f, name + " accepted connection from " + (mySockNum - 10000));
            	}
                catch(SocketTimeoutException e){
                	System.err.println("Socket Timeout! 60 Seconds!");
                	return;
                }
 
            	clientSocket.setSoTimeout(timeout);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            	BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            	
            	
            	
            	
            	String firstPacketIn = clientIn.readLine();
            	System.out.println("Client said: " + firstPacketIn);
            	
            	
            	out.println(firstPacketIn.toUpperCase());

            	
            	
    			// closing connections
    			System.out.println("Closing Sockets...");	
    			out.println("Thread Bye.");
    			out.close();
    			in.close();
    			clientSocket.close();
            	
            	
            }
            catch (IOException e) {
                System.err.println("Node failed to connect: " + e);
                return;
            }
            
            			
		} catch(SocketException e){
			System.out.println(e);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
