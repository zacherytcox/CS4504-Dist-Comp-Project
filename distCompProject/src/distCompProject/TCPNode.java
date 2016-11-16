package distCompProject;

//Author: Zachery Cox / Andrew Marks
//Date: 10/11/16


/*//////////////////////////////// INSTRUCTIONS ////////////////////////////////////////
NOTE:
Server Node MUST connect to server router first. Error otherwise.

CLIENT:
Launch TCPNode, fill in fields accordingly
Click "Run as Client" to run client code and it will send string to server router to send to destination node.
When process is finished, the GUI will then be responsive and will be allowed to send another string.


SERVER:
Launch TCPNode, fill in fields accordingly
Click "Run as Server" to run server code and receive data to compute
Server code will continue to run even when connection is finished. It will reconnect after processing a request.
This will allow the user, implementing this software, to just have to operate the client node.

//////////////////////////////////////////////////////////////////////////////////*/




import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;

@SuppressWarnings("serial")
public class TCPNode extends JFrame {

	private String routerName, address, sock;
	private JTextField logPath = new JTextField(); // Declare a JTextField component
	private JTextField routerServerIP = new JTextField(); // Declare a JTextField component
	private JTextField sendString = new JTextField(); // Declare a JTextField component
	private JTextField mySock = new JTextField(); // Declare a JTextField component
	private JTextField routerServerSock = new JTextField(); // Declare a JTextField component
	private JTextField destIP = new JTextField(); // Declare a JTextField component
	private JButton runServer = new JButton(); // Declare a JButton component
	private JButton runClient = new JButton(); // Declare a JButton component
	private static int timeout = 60000;
	

	
	
	public static void main(String[] args) throws IOException {

		//Create TCPNode object
		new TCPNode();
		
	}
	
	
	private TCPNode() {

		//GUI  creation 
		JFrame frame = new JFrame("TCPNode");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(2, 8));
		frame.add(new Label("Router Server IP Address"));
		frame.add(new Label("Dest. IP Address"));
		frame.add(new Label("Router Server Socket"));
		frame.add(new Label("My Socket"));
		frame.add(new Label("String to Send"));
		frame.add(new Label("Path to LogFile Folder"));
		frame.add(new Label("Operate as ClientNode"));
		frame.add(new Label("Operate as ServerNode"));

		routerServerIP.setText("192.168.1.2");
		frame.add(routerServerIP);
		
		destIP.setText("192.168.1.2");
		frame.add(destIP);
		
		routerServerSock.setText("5001");
		frame.add(routerServerSock);
		
		mySock.setText("1001");
		frame.add(mySock);
		
		sendString.setText("string");
		frame.add(sendString);
		
		logPath.setText("C:\\This\\Format\\Please");
		frame.add(logPath);
		
		runClient.setText("Run as Client");
		frame.add(runClient);
		runClient.addActionListener(new goClient());
		
		runServer.setText("Run as Server");
		frame.add(runServer);
		runServer.addActionListener(new goServer());
		
		frame.setSize(1200, 100);

		frame.setVisible(true);

	}

	
	
	public static void clientStuffs(String routerName, String address, Path tempFile, String sock, String logPath) throws IOException {
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

	public static void serverStuffs(String routerName, String address, String sock) throws IOException {
		try{
			// Variables for setting up connection and communication
			Socket Socket = null; // socket to connect with ServerRouter
			PrintWriter out = null; // for writing to ServerRouter
			BufferedReader in = null; // for reading from ServerRouter
			//InetAddress addr = InetAddress.getLocalHost();
			//String host = addr.getHostAddress(); // Server machine's IP
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

	private class goServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				//get data
				routerName = routerServerIP.getText();
				address = destIP.getText();
				sock = routerServerSock.getText();

				//do server stuffs
				//keeps running because whenever a single compute is finished, it will reconnect to ServerRouter
				while(true){
					serverStuffs(routerName, address, sock);
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class goClient implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				//get data
				routerName = routerServerIP.getText();
				address = destIP.getText();
				sock = mySock.getText();
				
				//create temp file
				Path tempFile = createTempFile(sendString);
					
				//TESTING				
				//DELETES TEMP FILE WHILE TESTING		
				//COMMENT OUT ON PROD MACHINE
				//deleteTempFile(tempFile);
		
				clientStuffs(routerName, address, tempFile, sock, logPath.toString());
				
				//deletes temp file after we are done with it
				try{
					deleteTempFile(tempFile);
				} catch (FileSystemException e2){
					System.out.println("Can Not Delete Temp File: " + e2);
					
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//This method creates a temp file containing the string inserted in the GUI
	private static Path createTempFile(JTextField sendString) throws IOException{
		
		Path tempFile = Files.createTempFile("TCPNode.", null);
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile.toFile()));
		bw.write(sendString.getText());
		bw.close();
		System.out.println("Temp File Location: " + tempFile.toAbsolutePath());
		return tempFile;
		
	}
	
	//This method deletes the temp file after we are done with it
	private static void deleteTempFile(Path tempFile) throws IOException{
		
		Files.delete(tempFile.toAbsolutePath());
		System.out.println("Temp File deleted...");
		
	}

	//this method, given the string, time, and size, will append data to csv log file
	private static void addToLogFile(String line, long time, long size, String logPath) throws IOException{
		try{
			Path logFile = Paths.get(logPath + "\\logFile.csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.toFile().getName(), true));
			bw.append(line + "," + time + "," + size + "\n");
			bw.close();
		}catch(FileNotFoundException e){
			System.err.println(e);
		}
	}
	
}















