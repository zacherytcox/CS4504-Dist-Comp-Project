package distCompProject;

import java.io.IOException;
import java.nio.file.Path;

public class RunPhase2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numSR = 3000;
		int numPairs = 2;
		String ip = "192.168.10.10";
		String name;
		
		//Start SRs
		for (int i=0;i<numSR;i++){
			name = "SR-" + ((i + 1) + 40000);
			TCPServerRouter sr = new TCPServerRouter(name, numSR, ((i + 1) + 40000) , ip);
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
		
		

		

	}

}
