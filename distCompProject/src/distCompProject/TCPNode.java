package distCompProject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.event.*;

public class TCPNode extends Frame {

	private Label lblCount; // Declare a Label component
	private TextField tfCount; // Declare a TextField component
	private Button btnCount; // Declare a Button component
	private int count = 0; // Counter's value

	public static void main(String[] args) {

		new TCPNode();

	}

	public static void clientStuffs() throws IOException {

		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Client machine's IP
		String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = 5555; // port number

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum); // opens port
			out = new PrintWriter(Socket.getOutputStream(), true); // creates
																	// stream of
																	// data
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream())); // creates
																						// a
																						// buffer,
																						// that
																						// is
																						// in
																						// stream
																						// of
																						// data,
																						// from
																						// the
																						// opened
																						// port
		} catch (UnknownHostException e) { // dont know the router
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) { // cant get data stream
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
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
		String address = "ipaddress"; // destination IP (Server)
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

	public static void serverStuffs() throws IOException {
		// Variables for setting up connection and communication
		Socket Socket = null; // socket to connect with ServerRouter
		PrintWriter out = null; // for writing to ServerRouter
		BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		String host = addr.getHostAddress(); // Server machine's IP
		String routerName = "ipaddress"; // ServerRouter host name
		int SockNum = 5555; // port number

		// Tries to connect to the ServerRouter
		try {
			Socket = new Socket(routerName, SockNum);
			out = new PrintWriter(Socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about router: " + routerName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + routerName);
			System.exit(1);
		}

		// Variables for message passing
		String fromServer; // messages sent to ServerRouter
		String fromClient; // messages received from ServerRouter
		String address = "ipaddress"; // destination IP (Client)

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
	      setLayout(new FlowLayout());
	         // "super" Frame (a Container) sets its layout to FlowLayout, which arranges
	         // the components from left-to-right, and flow to next row from top-to-bottom.
	 
	      lblCount = new Label("Counter");  // construct the Label component
	      add(lblCount);                    // "super" Frame adds Label
	 
	      tfCount = new TextField("0", 10); // construct the TextField component
	      tfCount.setEditable(false);       // set to read-only
	      add(tfCount);                     // "super" Frame adds TextField
	 
	      btnCount = new Button("Count");   // construct the Button component
	      add(btnCount);                    // "super" Frame adds Button
	 
	      //btnCount.addActionListener(this);
	         // btnCount is the source object that fires ActionEvent when clicked.
	         // The source add "this" instance as an ActionEvent listener, which provides
	         //  an ActionEvent handler called actionPerformed().X
	         // Clicking btnCount invokes actionPerformed().
	 
	      setTitle("AWT Counter");  // "super" Frame sets its title
	      setSize(250, 100);        // "super" Frame sets its initial window size
	 
	      // For inspecting the components/container objects
	      // System.out.println(this);
	      // System.out.println(lblCount);
	      // System.out.println(tfCount);
	      // System.out.println(btnCount);
	 
	      setVisible(true);         // "super" Frame shows
	 
	      // System.out.println(this);
	      // System.out.println(lblCount);
	      // System.out.println(tfCount);
	      // System.out.println(btnCount);

	}

}
