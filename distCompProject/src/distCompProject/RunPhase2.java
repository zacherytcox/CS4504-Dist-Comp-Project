package distCompProject;

//Author: Zachery Cox
//Date: 11/25/16


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class RunPhase2 {
	
	//DO NOT CHANGE
	public static String folder = "\\TCPPhase2Testing";
	
	public static String logFilePath = "C:\\Users\\Zach\\Desktop" + folder;
	
	//string file
	public static File s = null;
	
	//timeout file
	public static File t = null;
	
	//performance file
	public static File p = null;
	
	//Server Router performance file
	public static File sr = null;
	
	
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
			p = createPerformanceFile(logFilePath);
			sr = createSRFile(logFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//logging stuffz
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
			TimeUnit.SECONDS.sleep(3);
			
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
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//Wait for SR to launch and configure
		try {
			addToLogFile(f, "Waiting for Servers to launch..." );
			TimeUnit.SECONDS.sleep(10);
			
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
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	//This method creates a temp file containing the string inserted
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
	
	//This method creates a temp file containing the string inserted
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
	
	//This method creates a temp file containing the string inserted
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
	
	//This method creates a temp file containing the string inserted
	public static File createPerformanceFile(String path) throws IOException{
		
		File s = new File(path + File.separator + "TCPPerformance.csv");
		
		try {
			s.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(s.getAbsoluteFile(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write("Source,String,Size,Time,,Avg Size,Avg Time,Rate,,Drops? (Total pairs -(Number of lines - 1))");
			bw.write("\n");
			bw.close();
		}
		catch (IOException e){
			
		}

		return s;
	}
	
	//This method creates a temp file containing the string inserted
	public static File createSRFile(String path) throws IOException{
		
		File s = new File(path + File.separator + "TCPSRTablePerformance.csv");
		
		try {
			s.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(s.getAbsoluteFile(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write("Source,Time,,Avg Time");
			bw.write("\n");
			bw.close();
		}
		catch (IOException e){
			
		}

		return s;
	}
	
	//This method adds data to log file
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

	//This method adds data to performance file
	public static void addToPerformanceFile(File f, String source, String str, int size, long time){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsoluteFile(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(source + "," + str + "," + size + "," + time);
			bw.write("\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//This method adds data to server router performance file
	public static void addToSRPerformanceFile(File f, String source, int size, long time){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(f.getAbsoluteFile(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(source + "," + time + "," );
			bw.write("\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
}
