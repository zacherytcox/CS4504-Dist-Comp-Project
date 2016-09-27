package distCompProject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

public class TCPNode extends Applet {

	final String HOMEPATH = "FilePath\\";
	private String routerName, address;
	private Label strRouterServerIP; // Declare a Label component	
	private Label strDestIP; // Declare a Label component	
	private Label strSendString; // Declare a Label component	
	private TextField sendString; // Declare a Label component
	private TextField destIP; // Declare a Label component
	private TextField routerServerIP; // Declare a Label component
	private Button runServer; // Declare a Button component
	private Button runClient; // Declare a Button component

	
	
	public static void main(String[] args) {

		new TCPNode();

	}

	public static void clientStuffs(String routerName, String address) throws IOException {

		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Client machine's IP
		//String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = 5555; // port number

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
		Reader reader = new FileReader("FilePath\\file.txt"); // create a file
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

	public static void serverStuffs(String routerName, String address) throws IOException {
		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Server machine's IP
		//String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = 5555; // port number

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
	      setLayout(new GridLayout(2,5));
	         // "super" Frame (a Container) sets its layout to FlowLayout, which arranges
	         // the components from left-to-right, and flow to next row from top-to-bottom.

	      this.setSize(300, 300);
	      
	      add(new Label("Router Server IP Address"));
	      add(new Label("Dest. IP Address"));
	      add(new Label("String to Send"));
	      add(new Label("----"));
	      add(new Label("----"));
	      
	      
	      routerServerIP = new TextField("255.255.255.255");  // construct the Label component	      
	      add(routerServerIP);
	      
	      destIP = new TextField("255.255.255.255");  // construct the Label component	      
	      add(destIP);
	      
	      
	      sendString = new TextField("string");  // construct the Label component	      
	      add(sendString);
	      
	      
	      runClient = new Button("Run Client");  // construct the Label component
	      add(runClient);                    // "super" Frame adds Label
	      runClient.addActionListener(new goClient());
	      
	      
	      
	      runServer = new Button("Run Server");  // construct the Label component
	      add(runServer); // "super" Frame adds Label
	      runServer.addActionListener(new goServer());
	      

	 

	}

	private class goServer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				routerName = routerServerIP.getText();
				address = destIP.getText();
				
				//create file.txt
				PrintWriter writer = new PrintWriter("FilePath\\file.txt", "UTF-8");
				writer.println(sendString.getText());
				writer.close();

				
				serverStuffs(routerName, address);
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
				
				//create file.txt
				PrintWriter writer = new PrintWriter("FilePath\\file.txt", "UTF-8");
				writer.println(sendString.getText());
				writer.close();
				
				clientStuffs(routerName, address);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
