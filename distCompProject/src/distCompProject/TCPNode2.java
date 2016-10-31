package distCompProject;



/*/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




NO WORK! USE TCPNODE










/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/
















/*/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////








NO WORK! USE TCPNODE






/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/













/*/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




NO WORK! USE TCPNODE










/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/





























import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;

public class TCPNode2 extends JFrame {

	final String HOMEPATH = "FilePath\\";
	private String routerName, address, sock;
	private JTextField routerServerIP = new JTextField(); // Declare a Label component
	private JTextField sendString = new JTextField(); // Declare a Label component
	private JTextField destSock = new JTextField(); // Declare a Label component
	private JTextField routerServerSock = new JTextField(); // Declare a Label component
	private JTextField destIP = new JTextField(); // Declare a Label component
	private JButton runServer = new JButton(); // Declare a Button component
	private JButton runClient = new JButton(); // Declare a Button component
	
	

	
	
	public static void main(String[] args) throws IOException {

		new TCPNode2();
		//serverStuffs("1.1.1.1","1.1.1.1","5555");
	}
	
	
	private TCPNode2() {

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
		runClient.addActionListener(new goNode());
		
		runServer.setText("Run as Server");
		frame.add(runServer);
		runServer.addActionListener(new sendStuffs());
		
		frame.setSize(1200, 100);

		frame.setVisible(true);

	}

	

	public static void nodeStuffs(String routerName, String address, Path tempFile, String sock) throws IOException {
		try{
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
				System.err.println("ClientNode: Don't know about router: " + routerName);
				return;
			} catch (IOException e) { // cant get data stream
				System.err.println("ClientNode: Couldn't get I/O for the connection to: " + routerName);
				return;
			}
	
			
			// Variables for message passing
			Reader reader = new FileReader(tempFile.toFile()); // create a file
			System.out.println("Reading File...");														// so it can be											// read?
			BufferedReader fromFile = new BufferedReader(reader); // reader for the
																	// string file
			InputStreamReader is = new InputStreamReader(System.in);
			BufferedReader consoleInput = new BufferedReader(is);
			String input = "";

			
			
			String fromServerRouter; // messages received from ServerRouter
			String toServerRouter; // messages sent to ServerRouter
			//String address = "ipaddress"; // destination IP (Server)
			//long t0, t1, t;
	
			// Communication process (initial sends/receives
			out.println(address);// initial send (IP of the destination Server)
			fromServerRouter = in.readLine();// initial receive from router (verification
										// of connection)
			System.out.println("RouterServer: " + fromServerRouter);
			out.println(host); // Client sends the IP of its machine as initial send
			//t0 = System.currentTimeMillis();
	
			// Communication while loop
			while ((fromServerRouter = in.readLine()) != null) {
				
				System.out.println(input.toString());
				System.out.println("ServerNode: " + fromServerRouter);
				//t1 = System.currentTimeMillis();
				if (fromServerRouter.equals("Bye.") || input.toString().equals("done")) { // exit statement
					break;
				}
				
				//t = t1 - t0;
				//System.out.println("Cycle time: " + t);
				toServerRouter = fromServerRouter.toUpperCase();
				System.out.println("ServerNode");
				out.println(toServerRouter);
				
				input = consoleInput.readLine().toString();
				//toServerRouter = fromFile.readLine(); // reading strings from a file
				if (input.equals("send")) {
					
					long fileSize = tempFile.toFile().length();
					//System.out.println("String Size: " + fileSize);
					System.out.println("ClientNode: " + toServerRouter);
					out.println(toServerRouter); // sending the strings to the Server via
					//t0 = System.currentTimeMillis();
					
				}
			}
	
			// closing connections
			out.close();
			in.close();
			Socket.close();
			return;
		} catch(SocketException e){
			System.out.println(e);
			return;
		}
	}


	
	private class goNode implements ActionListener {
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
		
				nodeStuffs(routerName, address, tempFile, sock);
				
				//deletes temp file after we are done with it
				try{
					deleteTempFile(tempFile);
				} catch (FileSystemException e2){
					System.out.println("Can Not Delete Temp File: " + e2);
					
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	private static class sendStuffs implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
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
