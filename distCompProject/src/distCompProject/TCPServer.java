package distCompProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class TCPServer {
	private String ip, sock;
	
	
	TCPServer(String ipAddr, String socket){
		
		ip = ipAddr;
		sock = socket;
		
	}
	
	
	public void run(){
		try{
			// Variables for setting up connection and communication
			Socket Socket = null; // socket to connect with ServerRouter
			PrintWriter out = null; // for writing to ServerRouter
			BufferedReader in = null; // for reading from ServerRouter
			int SockNum = Integer.parseInt(sock); // port number
	
			// Tries to connect to the ServerRouter
			try {
				System.out.println("Connect to ServerRouter...");	
				Socket = new Socket(routerName, SockNum);
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Server: Don't know about router: " + routerName);
				return;
				
			} catch (IOException e) {
				System.err.println("Server: Couldn't get I/O for the connection to: " + routerName);
				return;
				
			}
	
			// Variables for message passing
			String fromServer = ""; // messages sent to ServerRouter
			String fromClient = ""; // messages received from ServerRouter
			
			// Communication process (initial sends/receives)
			out.println(address);// initial send (IP of the destination Client)
			try{

				fromClient = in.readLine();// initial receive from router (verification of connection)
				if(fromServer.toString().equals("Full.")){
					System.err.println("Routing Table is full!");
					Socket.close();
					return;
				}

			}catch(SocketTimeoutException e){
				System.err.println(e);
				Socket.close();
				return;
			}
			System.out.println("ServerRouter: " + fromClient);
	

			try{

				fromClient = in.readLine();// waiting on client to handshake

			}catch(SocketTimeoutException e){
				System.err.println("Waiting on client, timeout");
				Socket.close();
				return;
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
				
			}
	
			// closing connections
			System.out.println("Closing Sockets...");	
			out.close();
			in.close();
			Socket.close();
			
		} catch(SocketException e){
			System.out.println(e);
			return;
		}
	}

}
