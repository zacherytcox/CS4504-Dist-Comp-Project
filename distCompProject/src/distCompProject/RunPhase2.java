package distCompProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JTextField;

public class RunPhase2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numSR = 3;
		int numPairs = 2;
		String ip = "192.168.10.10";
		String name;
		File f = null;
		
		try {
			f = createLogFile("C:\\Users\\Zach\\Desktop");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addToLogFile(f, "Number of Server Routers: " + numSR);
		addToLogFile(f, "Number of Node Pairs: " + numPairs);
		addToLogFile(f, "Ip address: " + ip );
		addToLogFile(f, "\n####################START####################\n");
		
		
		//Start SRs
		for (int i=0;i<numSR;i++){
			name = "SR-" + ((i + 1) + 40000);
			TCPServerRouter sr = new TCPServerRouter(name, numSR, ((i + 1) + 40000), ip, f);
			sr.start();
		}
		
//		//Start Servers
//		for (int i=0;i<numPairs;i++){
//			name = "S-" + ((i + 1) + 30000);
//			//String routerName, String address, String sock
//			TCPServer s = new TCPServer(name, numPairs, ((i + 1) + 30000) , ip);
//			s.start();
//		}
//		
//		
//		//Start Clients
//		for (int i=0;i<numPairs;i++){
//			name = "C-" + ((i + 1) + 20000);
//			//String routerName, String address, Path tempFile, String sock, String logPath
//			TCPClient c = new TCPClient(name, numSR, ((i + 1) + 20000) , ip);
//			c.start();
//		}
		
				
		//removes log file. Ment for testing
		//f.delete();

		

	}
	
	
	
	
	//This method creates a temp file containing the string inserted in the GUI
	public static File createLogFile(String path) throws IOException{
		
		File f = new File(path + File.separator + "TCPLog.txt");
				
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
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
