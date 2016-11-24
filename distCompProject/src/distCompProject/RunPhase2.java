package distCompProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;


public class RunPhase2 {
	
	//DO NOT CHANGE
	public static String folder = "\\TCPPhase2Testing";
	
	public static String logFilePath = "C:\\Users\\Zach\\Desktop" + folder;
	
	//string file
	public static File s = null;
	
	//timeout file
	public static File t = null;
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//3 is the standard number. No change
		int numSR = 3;
		
		
		int numPairs = 1000;
		String ip = "192.168.50.157";
		

		
		String name;
		File f = null;
		
		try {
			f = createLogFile(logFilePath);
			s = createStringFile(logFilePath);
			t = createTimeoutFile(logFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		addToLogFile(f, "\n###############################################################################################################################\n");
		addToLogFile(f, "Number of Server Routers: " + numSR);
		addToLogFile(f, "Number of Node Pairs: " + numPairs);
		addToLogFile(f, "Ip address: " + ip );
		addToLogFile(f, "\n#################### START PHASE 2 SIMULATION ####################\n");
		
		
		//Start SRs
		for (int i=0;i<numSR;i++){
			name = "SR-" + ((i + 1) + 40000);
			TCPServerRouter sr = new TCPServerRouter(name, numSR, ((i + 1) + 40000), ip, f);
			sr.start();
		}
		
		//Wait for SR to launch and configure
		try {
			addToLogFile(f, "Waiting for SRs to launch..." );
			TimeUnit.SECONDS.sleep(5);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Start Servers
		for (int i=0;i<numPairs;i++){
			name = "S-" + ((i + 1) + 30000);
			//String routerName, String address, String sock
			TCPServer s = new TCPServer(name, ip, ((i + 1) + 30000) , numSR, f);
			s.start();
		}
		
		
		//Wait for SR to launch and configure
		try {
			addToLogFile(f, "Waiting for Servers to launch..." );
			TimeUnit.SECONDS.sleep(20);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Start Clients
		for (int i=0;i<numPairs;i++){
			name = "C-" + ((i + 1) + 20000);
			//String myName, String ipAddr, int socket, int numberOfSR, File file
			TCPClient c = new TCPClient(name, ip, ((i + 1) + 20000), numSR, f);
			c.start();
		}

	}
	
	
	//This method creates a temp file containing the string inserted in the GUI
	public static File createLogFile(String path) throws IOException{
		
	    File directory = new File(String.valueOf(path));
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		
		File f = new File(path + File.separator + "TCPLog.txt");
				
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return f;
	}
	

	
	//This method creates a temp file containing the string inserted in the GUI
	public static File createStringFile(String path) throws IOException{
		
		File s = new File(path + File.separator + "TCPStrings.txt");
		
		try {
			s.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		return s;
	}
	
	//This method creates a temp file containing the string inserted in the GUI
	public static File createTimeoutFile(String path) throws IOException{
		
		File s = new File(path + File.separator + "TCPTimeouts.txt");
		
		try {
			s.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		return s;
	}
	
	
	
	public static void addToLogFile(File f, String content){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsoluteFile(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(content);
			bw.write("\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
