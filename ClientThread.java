import java.net.*;
import java.util.*;
import java.io.*;


class ClientThread extends Thread {


	Socket socket;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			manageRequest();
		} catch(UnknownHostException e) {
	
		} catch(IOException e) {
		
		}
	}

	private void manageRequest() throws UnknownHostException, IOException {
		System.out.println("Soy un thread");
	}

}