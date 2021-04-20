import java.net.*;
import java.io.*;
import java.util.Date;


public class Server {

	public final static int PORT = 1200;

	private static int clients = 0;
	
	
	public static void main(String[] args) {

		System.setProperty("line.separador","\r\n");
		

		ServerSocket socket = createSocket();

		
		while (true) {
			
			try (Socket conexion = socket.accept()) {
				



				OutputStream outputStream = conexion.getOutputStream();
				Writer writer = new OutputStreamWriter(outputStream, "ASCII");
				
				
				String message = "MOVE\nArguments: 0, 1, 2, etc...";
				
				writer.write(message);
				writer.flush();
			
				conexion.close();

			} catch (IOException ex) {
				// problem with one client; don't shut down the server
				System.err.println(ex.getMessage());
			}
		}
	}







	private static ServerSocket createSocket() {
		ServerSocket socket = null;

		try {
			socket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.print("Fail creating the socket at port " + PORT + "\n");
		}

		return socket;
	}

}