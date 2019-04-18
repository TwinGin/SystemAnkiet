import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* 
 * System zarz¹dzania ankietami
 * @author Piotr Bajorek 
 * @version 1.0
 * @since 08.01.2019
 * */
public class Server {
	
	 public void startRunning() {
		Database db = new Database("localhost", 3306);
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9991);
			while (true) {
				Socket socket = serverSocket.accept();
				(new TCPThread(socket, db)).start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}finally{
			if(serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


	public static void main(String[] args) {
		Server server = new Server();
        server.startRunning();

	}

}
