package distCompProject;

import java.io.IOException;

public class RunPhase2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numSR = 2;
		int numPairs = 2;
		String ip = "192.168.10.10";
		String name;
		
		//Start SRs
		for (int i=0;i<numSR;i++){
			name = "SR-" + ((i + 1) +40000);
			TCPServerRouter sr = new TCPServerRouter(name, numSR, ((i + 1) +40000) , ip);
			sr.start();
		}
		
		
//		//Start servers
//		for (int i=0;i<numPairs;i++){
//			name = "Server-" + ((i + 1) +30000);
//			
//			// (#of server routers, specify socket #, ipaddress, );
//			NodeThread t = new NodeThread(name, "s", 3, ((i + 1) +30000) , ip); // creates a thread with a random port
//			t.start();
//			System.out.println("");
//		}
//		
//		
//		
//		//Start clients
//		for (int i=0;i<numPairs;i++){
//			name = "Client-" + ((i + 1) +20000);
//			
//			// (#of server routers, specify socket #, ipaddress, );
//			NodeThread t = new NodeThread(name, "c", 3, ((i + 1) +20000) , ip); // creates a thread with a random port
//			t.start();
//			System.out.println("");
//		}
		
		

	}

}
