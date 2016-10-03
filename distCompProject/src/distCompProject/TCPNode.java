package distCompProject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.Integer;

public class TCPNode extends JFrame {

	final String HOMEPATH = "FilePath\\";
	private String routerName, address, sock;
	private JTextField sendString; // Declare a Label component
	private JTextField destSock; // Declare a Label component
	private JTextField routerServerSock; // Declare a Label component
	private JTextField destIP; // Declare a Label component
	private JTextField routerServerIP; // Declare a Label component
	private JButton runServer; // Declare a Button component
	private JButton runClient; // Declare a Button component

	
	
	public static void main(String[] args) {

		new TCPNode();

	}

	public static void clientStuffs(String routerName, String address, Path tempFile, String sock) throws IOException {

		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Client machine's IP
		//String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = Integer.parseInt(sock); // port number

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum); // opens port
			out = new PrintWriter(Socket.getOutputStream(), true); // creates
																	// stream of
																	// data
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); 
		} catch (UnknownHostException e) { // dont know the router
			System.err.println("Client: Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) { // cant get data stream
			System.err.println("Client: Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		
		// Variables for message passing
		Reader reader = new FileReader(tempFile.toFile()); // create a file
																// so it can be
																// read?
		BufferedReader fromFile = new BufferedReader(reader); // reader for the
																// string file
		String fromServer; // messages received from ServerRouter
		String fromUser; // messages sent to ServerRouter
		//String address = "ipaddress"; // destination IP (Server)
		long t0, t1, t;

		// Communication process (initial sends/receives
		out.println(address);// initial send (IP of the destination Server)
		fromServer = in.readLine();// initial receive from router (verification
									// of connection)
		System.out.println("ServerRouter: " + fromServer);
		out.println(host); // Client sends the IP of its machine as initial send
		t0 = System.currentTimeMillis();

		// Communication while loop
		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			t1 = System.currentTimeMillis();
			if (fromServer.equals("Bye.")) { // exit statement
				break;
			}
			t = t1 - t0;
			System.out.println("Cycle time: " + t);

			fromUser = fromFile.readLine(); // reading strings from a file
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser); // sending the strings to the Server via
										// ServerRouter
				t0 = System.currentTimeMillis();
			}
		}

		// closing connections
		out.close();
		in.close();
		Socket.close();
	}

	public static void serverStuffs(String routerName, String address, String sock) throws IOException {
		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Server machine's IP
		//String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = Integer.parseInt(sock); // port number

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Server: Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Server: Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		// Variables for message passing
		String fromServer; // messages sent to ServerRouter
		String fromClient; // messages received from ServerRouter
		//String address = "ipaddress"; // destination IP (Client)

		// Communication process (initial sends/receives)
		out.println(address);// initial send (IP of the destination Client)
		fromClient = in.readLine();// initial receive from router (verification
									// of connection)
		System.out.println("ServerRouter: " + fromClient);

		// Communication while loop
		while ((fromClient = in.readLine()) != null) {
			System.out.println("Client said: " + fromClient);
			if (fromClient.equals("Bye.")) { // exit statement
				break;
			}
			fromServer = fromClient.toUpperCase(); // converting received
													// message to upper case
			System.out.println("Server said: " + fromServer);
			out.println(fromServer); // sending the converted message back to
										// the Client via ServerRouter
		}

		// closing connections
		out.close();
		in.close();
		Socket.close();

	}

	public TCPNode() {

		JFrame frame = new JFrame("APP");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new GridLayout(2, 7));

		frame.add(new Label("Router Server IP Address"));
		frame.add(new Label("Dest. IP Address"));
		frame.add(new Label("Router Server Socket"));
		frame.add(new Label("Dest. Socket"));
		frame.add(new Label("String to Send"));
		frame.add(new Label("----"));
		frame.add(new Label("----"));

		routerServerIP.setText("192.168.56.102");
		frame.add(routerServerIP);
		destIP.setText("192.168.56.103");
		frame.add(destIP);
		routerServerSock.setText("5555");
		frame.add(routerServerSock);
		destSock.setText("5555");
		frame.add(destSock);
		
		sendString.setText("string");
		frame.add(sendString);
		
		runClient.setText("Run as Client");
		frame.add(runClient);
		runClient.addActionListener(new goClient());
		
		runServer.setText("Run as Server");
		frame.add(runServer);
		runServer.addActionListener(new goServer());
		
		frame.setSize(1200, 100);

		frame.setVisible(true);

	 

	}

	private class goServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				routerName = routerServerIP.getText();
				address = destIP.getText();
				sock = routerServerSock.getText();

				serverStuffs(routerName, address, sock);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class goClient implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				routerName = routerServerIP.getText();
				address = destIP.getText();
				sock = destSock.getText();
				
				//create temp file
				Path tempFile = createTempFile(sendString);
					
				//TESTING				
				//DELETES TEMP FILE WHILE TESTING		
				//COMMENT OUT ON PROD MACHINE
				//deleteTempFile(tempFile);
		
				clientStuffs(routerName, address, tempFile, sock);
				
				//deletes temp file after we are done with it
				deleteTempFile(tempFile);

				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private static Path createTempFile(JTextField sendString) throws IOException{
		
		
		Path tempFile = Files.createTempFile("TCPNode.", null);
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile.toFile()));
		bw.write(sendString.getText());
		bw.close();
		System.out.println("Temp File Location: " + tempFile.toAbsolutePath());
		return tempFile;
		

	}
	
	private static void deleteTempFile(Path tempFile) throws IOException{
		
		Files.delete(tempFile.toAbsolutePath());
		System.out.println("Temp File deleted...");
		
	}

}
