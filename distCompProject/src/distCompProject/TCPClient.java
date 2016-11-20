package distCompProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class TCPClient {
	
	TCPClient(){
		
	}
	
	
	public void run(){
		
		
		try{
			// Variables for setting up connection and communication
			Socket Socket = null; // socket to connect with ServerRouter
			PrintWriter out = null; // for writing to ServerRouter
			BufferedReader in = null; // for reading form ServerRouter
			InetAddress addr = InetAddress.getLocalHost();
			String host = addr.getHostAddress(); // Client machine's IP
			int SockNum = Integer.parseInt(sock); // port number 
	
			// Tries to connect to the ServerRouter
			try {
				Socket = new Socket(routerName, SockNum); // opens port
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true); // creates stream of data
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); 
			} catch (UnknownHostException e) { // dont know the router
				System.err.println("Client: Don't know about router: " + routerName);
				return;
			} catch (IOException e) { // cant get data stream
				System.err.println("Client: Couldn't get I/O for the connection to: " + routerName);
				return;
			}
	
			// read file
			Reader reader = new FileReader(tempFile.toFile()); // create a file
			System.out.println("Reading File...");												
																	
			//get buffered file data from read file
			BufferedReader fromFile = new BufferedReader(reader); // reader for the
																	// string file
			String fromServer = null; // messages received from ServerRouter
			String fromUser = null; // messages sent to ServerRouter

	
			// Communication process (initial sends/receives
					
			out.println(address);// initial send (IP of the destination Server)
			
			try{
				fromServer = in.readLine();// initial receive from router (verification of connection)
				if(fromServer.toString().equals("Full.")){
					System.err.println("Routing Table is full!");
					fromFile.close();
					Socket.close();
					return;
				}				
			} catch (SocketTimeoutException e){
				System.err.println("Timeout!");
				// closing connections
				fromFile.close();
				out.close();
				in.close();
				Socket.close();
				return;
			}
			
			System.out.println("ServerRouter: " + fromServer);
			
			out.println("7 IM 5"); //// 7 IM 5
			
			
			try{
				fromServer = in.readLine();
				System.out.println(fromServer + " from server, connected");
				
			} catch(SocketTimeoutException e) {
				System.err.println("Timeout!");
				// closing connections
				fromFile.close();
				out.close();
				in.close();
				Socket.close();
				return;
			}
			

			//send to new node
			//new sockets for server node
			 try {
				System.out.println("Connect to ServerRouter...");	
				Socket = new Socket(address, SockNum);
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			} catch (IOException e) {
				System.err.println("Server: Couldn't get I/O for the connection to: " + routerName);
				return;
			}

			 
			//reads data from temp file
			fromUser = fromFile.readLine(); // reading strings from a file
		
			//if the file is not null
			if (fromUser != null) {
			//get the size of the file	
				}

			 
			//this is the results
			fromServer = in.readLine();
			

			// closing connections
			fromFile.close();
			out.close();
			in.close();
			Socket.close();
			return;

////////////////////////////////////////////////////////////////
		} catch(SocketException e){
		System.out.println(e);
			return;
		}
		
	}
	
	

}
