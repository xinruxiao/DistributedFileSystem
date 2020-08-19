import java.net.ServerSocket;
import java.net.Socket;

public class StorageNodeServer {

	public StorageNodeServer(int port,IOStrategy ios) {
		try{
			//TCPͨ��
			ServerSocket ss=new ServerSocket(port);//�ڵ�Ķ˿ں�
			System.out.println("The StorageNode Server is Ready:");
			while(true) {
				Socket socket=ss.accept();
				ios.service(socket);
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
