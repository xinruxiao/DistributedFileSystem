import java.net.*;

public class FileCliServer {
	public FileCliServer(int port,IOStrategy ios) {
		try{
			//StorageNode与fileClient之间的TCP通信
			ServerSocket ss=new ServerSocket(port);
			System.out.println("The Server is Ready:");
			while(true) {
				Socket socket=ss.accept();
				ios.service(socket);
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
