package distCompProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class test {

	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		// TODO Auto-generated method stub

		Socket in = new ServerSocket(5555).accept();
		
		
	}
}
