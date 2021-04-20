import java.net.*;
import java.util.*;
import java.io.*;


class ClientThread extends Thread {


	private Socket socket;
	private int id;

	public ClientThread(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
	}

	public void run() {
		try {
			manageRequest();
		} catch(UnknownHostException e) {
			System.out.println("Host desconocido");
		} catch(IOException e) {

			System.out.println("Juagador desconectado " + id);
		}
	}

	private void manageRequest() throws UnknownHostException, IOException {


		System.out.println("Soy " + id + "turno: " + Server.turn + "players: " + Server.numPlayers);


		OutputStream outputStream = socket.getOutputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

		
		Writer out = new OutputStreamWriter(outputStream, "ASCII");
		// //PrintWriter out = new PrintWriter(outputStream, true);
		BufferedReader in = new BufferedReader(inputStreamReader);


		if (!in.readLine().equals("JOIN_GAME")) {
			socket.close();
		}


		for (int i = 0; i < 15; i++) {

			// Espere torn
			while (Server.turn != id) {
				try {
					sleep(2000);
				} catch (InterruptedException e) {}
			}


			System.out.println("Me toca " + id);

			
			String msg = "MOVE\nArguments: 0, 1, 2, etc...\n";
			out.write(msg);
			out.flush();


			String action = in.readLine();
			String args = in.readLine();

			System.out.println(action + args);

			Server.updateTurn();


			//chooseAction(new String[] {action, args});

			// if (action != null) {
			// 	chooseAction(new String[] {action, null});
			// }
		}



	}



	
}



