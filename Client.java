import java.net.*;
import java.util.*;
import java.io.*;

public class Client {

	// Server parameters
	private final static String SERVER_NAME = "localhost";
	private final static int PORT = 1200;

	// Conexion variables
	private static Socket socket;
	private static PrintWriter out;
	private static BufferedReader in;

	// Client actions
	private static final String[] actions = {"MOVE", "MOVE_RESPONSE", "WIN", "LOSE"};
	private static boolean end = false;
	private static int totalTurns = 15;
	private static int turn = 0;

	private static String[] log;


	public static void main(String[] args) {

		System.setProperty("line.separador","\r\n");
		log = new String[totalTurns + 1];


		initSocket();
		
		try {

			OutputStream outputStream = socket.getOutputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			
			out = new PrintWriter(outputStream, true);
			in = new BufferedReader(inputStreamReader);


			joinGame();

			System.out.println("");

			String[] serverMessage;

			while (turn < totalTurns && !end) {
				serverMessage = lookForServerResponse();
				chooseAction(serverMessage);
			}


			if (!end) {
				System.out.println("EMPATE!!, se han acabado los turnos de todos los jugadores.");
			}
			

		} catch(IOException e) {
			System.out.println("No es posible realizar la conexión\n" + e);
		}

	}



	private static void initSocket() {
		try {
			socket = new Socket();
			InetAddress IPDir = InetAddress.getByName(SERVER_NAME);
			SocketAddress address = new InetSocketAddress (IPDir, PORT);

			socket.connect(address);

			System.out.println("Conexión establecida");
			System.out.println("Servidor: " + SERVER_NAME + " " + PORT);

		} catch(UnknownHostException e) {
			System.out.println("Nombre del servidor desconocido\n" + e);
		} catch (IOException e) {
			System.out.println("No es posible realizar la conexión\n" + e);
		} 
	}

	private static void joinGame() {
		out.println("JOIN_GAME\n");
	}

	private static String[] lookForServerResponse() {
		String action = null;
		String args = "null";
			try {

				while (action == null || action.length() < 1) {
					action = in.readLine();
				}
				args = in.readLine();

			} catch (IOException e) {
				System.out.print("Error de lectura");
			}

		return new String[] {action, args};
	}

	private static void move() {

		System.out.print("Te toca, introduce tu jugada: ");


		String move;

		do {
			move = System.console().readLine();
			System.out.print("INTRODUCE 4 LETRAS:\n");
		} while (move.length() != 4);


		log[turn] = move;
		out.println("C_MOVES" + "\n" + move + "\n");
	}

	private static void showMoveResponse(String response) {

		log[turn++] += " --> " + response;


		clearScreen();
		for (int i = 0; i < turn; i++) {
			System.out.println(log[i]);
		}


		System.out.println("\nResultado: " + log[turn - 1]);
		System.out.println("Esperando a los oponentes...\n");

		
	}

	private static void showWinMessage(String winMessage) {

		end = true;
		clearScreen();
		System.out.println(winMessage);
	}

	private static void chooseAction(String[] serverMessage) {

		String action = serverMessage[0];
		String args = serverMessage[1];


		for (int i = 0; i < actions.length; i++) {
			
			if (actions[i].equals(action)) {

				switch (i) {

					case 0: // MOVE
						move();
						break;
					case 1: // MOVE RESPONSE
						showMoveResponse(args);
						break;
					case 2: // WIN
						showWinMessage(args);
						break;
					case 3: // LOSE
						gameOver(args);
						break;
					default:
						System.out.println("Invalid action key " + action);
						break;
				}
				return;
			}
		}
	}

	private static void gameOver(String id) {

		int fakeID = Integer.parseInt(id) + 1;
		
		clearScreen();
		System.out.println("Ha ganado el jugador " + fakeID + "\nGAME OVER");
		end = true;

	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch(IOException ex) {} 
		catch (InterruptedException ex) {}
	}
}