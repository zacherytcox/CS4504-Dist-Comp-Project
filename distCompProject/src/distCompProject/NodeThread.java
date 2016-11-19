package distCompProject;

import java.io.IOException;

public class NodeThread extends Thread {
	int numberOfSRs, socketNum;
	String myIp, nodeType, name;
	
	NodeThread(String nodeName, String type, int numSR, int sockNum, String ip){
		numberOfSRs = numSR;
		socketNum = sockNum;
		myIp = ip;
		nodeType = type;
		name = nodeName;
		
	}
	
	public void run(){
		
		if (nodeType.equals("sr")){
			TCPServerRouter sr = new TCPServerRouter(name, numberOfSRs, socketNum, myIp);
			sr.start();
		}	
		
//		else if (nodeType.equals("server")){
//			try {
//				TCPNode.serverStuffs(routerName, address, sock);(numberOfSRs, socketNum, myIp);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		else if (nodeType.equals("client")){
//			try {
//				TCPNode.clientStuffs(routerName, address, tempFile, sock, logPath);(numberOfSRs, socketNum, myIp);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}	
	}
}
