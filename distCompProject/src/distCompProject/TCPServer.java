package distCompProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
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
			
			RunPhase2.addToLogFile(f, name + " Connecting to SR: " + srSockNum);
	
			
			
			// Tries to connect to the ServerRouter
			try {
				System.out.println("Connect to ServerRouter...");	
				Socket = new Socket(ip, srSockNum);
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
			out.println(clientSockNum);// initial send (IP of the destination Client)
			
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
			
			out.println("ok");
			
			
			
			//change the ports to the client
			
			try{
				// Communication while loop
				while ((fromClient = in.readLine()) != null) {
					System.out.println("Client said: " + fromClient);

					
					if (fromClient.toString().equals("Timeout.")){
						out.println(fromServer);
						System.err.println("Timeout! Resend!");
						
					}
					
					else{
					
						//operation, in this case turn to uppercase
						fromServer = fromClient.toUpperCase(); // converting received message to upper case
						
						System.out.println("Server said: " + fromServer);
						
						out.println(fromServer); // sending the converted message back to the Client via ServerRouter
						
						//Space
						System.out.println();
					}	
				}
			} catch (SocketTimeoutException e){
				System.err.println("Timeout, resend please...");
				out.println("Timeout.");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			// closing connections
			System.out.println("Closing Sockets...");	
			out.close();
			in.close();
			Socket.close();
			
		} catch(SocketException e){
			System.out.println(e);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
