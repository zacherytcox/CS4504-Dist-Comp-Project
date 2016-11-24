package distCompProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class TCPClient extends Thread {
	private String ip, name;
	private int numSR, mySock;
	public static File f = null;
	
	TCPClient(String myName, String ipAddr, int socket, int numberOfSR, File file){
		ip = ipAddr;
		mySock = socket;
		numSR = numberOfSR;
		name = myName;
		f = file;
		
	}
	
	
	@SuppressWarnings("resource")
	public void run(){
		
		
		try{
			// Variables for setting up connection and communication
			Socket Socket = null; // socket to connect with ServerRouter
			PrintWriter out = null; // for writing to ServerRouter
			BufferedReader in = null; // for reading form ServerRouter
			int mySockNum = mySock; // port number
			int serverSockNum = mySockNum + 10000;
			int timeout = 60000;
			
			Random rand = new Random();
			//pick random ServerRouter socket number
			int srSockNum = 40000 + rand.nextInt(numSR) + 1;
			//srSockNum = 40001;
			
			RunPhase2.addToLogFile(f, name + " Connecting to SR: " + srSockNum);
	
			
			
			// Tries to connect to the ServerRouter
			try {
				Socket = new Socket(ip, srSockNum, InetAddress.getByName(ip), mySockNum);
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true); // creates stream of data
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); 
			} catch (UnknownHostException e) { // dont know the router
				System.err.println("Client: Don't know about router: " + srSockNum);
				return;
			} catch (IOException e) { // cant get data stream
				System.err.println("Client: Couldn't get I/O for the connection to: " + srSockNum);
				return;
			}
			
			
			
			//random string
			char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < (rand.nextInt(10000) + 1); i++) {
			    char c = chars[random.nextInt(chars.length)];
			    sb.append(c);
			}
			String output = sb.toString();
			
			
			
			Path tempFile = Files.createTempFile("TCPNode.", null);
			BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile.toFile()));
			bw.write(output);
			bw.close();
			System.out.println("Temp File Location: " + tempFile.toAbsolutePath());
			
			
			
			
	
			// read file
			Reader reader = new FileReader(tempFile.toFile()); // create a file
			System.out.println("Reading File...");												
																	
			//get buffered file data from read file
			BufferedReader fromFile = new BufferedReader(reader); // reader for the
																	// string file
			String fromServer = null; // messages received from ServerRouter
			String fromUser = null; // messages sent to ServerRouter

	
			// Communication process (initial sends/receives
					
			out.println(mySockNum);// initial send (IP of the destination Server)
			
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
			
			out.println(serverSockNum); 
			
			
			try{
				fromServer = in.readLine();
				System.out.println(fromServer + " from server, connected");
				
				//end thread
				out.println("Thread Bye.");
				
			} catch(SocketTimeoutException e) {
				System.err.println("Timeout!");
				// closing connections
				fromFile.close();
				out.close();
				in.close();
				Socket.close();
				return;
			}
			

			RunPhase2.addToLogFile(f, name + " Ready to communicate with " + serverSockNum + " directly!");
			
			
			//send to new node
			//new sockets for server node
			 try {
				System.out.println("Connect to Server...");	
				Socket = new Socket(ip, serverSockNum);
				Socket.setSoTimeout(timeout);
				out = new PrintWriter(Socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
						 
				//reads data from temp file
				fromUser = fromFile.readLine(); // reading strings from a file
			
				//if the file is not null
				if (fromUser != null) {
					RunPhase2.addToLogFile(f, name + ": sent " + fromUser);
					out.println(fromUser);
					}

				 
				//this is the results
				fromServer = in.readLine();
				RunPhase2.addToLogFile(f, name + " Recieved: " + fromServer);
				

				// closing connections
				fromFile.close();
				out.close();
				in.close();
				Socket.close();
				return;
		
				
			} catch (IOException e) {
				System.err.println("Client: Couldn't get I/O for the connection to: " + serverSockNum);
				Socket.close();
				fromFile.close();
				return;
			}



////////////////////////////////////////////////////////////////
		} catch(SocketException e){
		System.out.println(e);
			return;
		}
		catch(IOException e){
			
		}
		
	}
	
	

}
