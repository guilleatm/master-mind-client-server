import java.net.*;
import java.util.*;
import java.io.*;

public class Client {

	public static void main(String[] args) {



	System.setProperty("line.separador","\r\n");
	

	String serverName = "localhost";
	int port = 420;

	
	try {

		Socket socket = new Socket();
		InetAddress IPDir = InetAddress.getByName(serverName);
		SocketAddress address = new InetSocketAddress (IPDir, port);

		socket.connect(address);

		System.out.print("Conexion establecida" + "\r\n");
		System.out.print("Nombre del servidor: " + serverName + " " + port + "\r\n");

		// OutputStream outputStream = socket.getOutputStream();
		// InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
		// PrintWriter comunicator = new PrintWriter(outputStream, true);
		// comunicator.println(message);


		// BufferedReader lecture = new BufferedReader(inputStreamReader);


		// for (String line = lecture.readLine(); line != null; line = lecture.readLine()) {
        //     System.out.print("\r\n" + line);
    	// }
		

		socket.close();
		

	} catch(UnknownHostException e) {
		System.out.print("Nombre del servidor desconocido \n" + e + "\r\n");
	} catch(IOException e) {
		System.out.print("No es posible realizar la conexiÃ³n\n" + e + "\r\n");
	}

	}
}