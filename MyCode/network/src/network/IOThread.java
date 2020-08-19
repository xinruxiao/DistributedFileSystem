package network;
import java.net.Socket;

public class IOThread extends Thread { // ��չ���߳��࣬IOThread.java
	private Socket socket; // ִ�кͿͻ��˵�ͨ��Э��
	private IOStrategy ios; // iosָ��һ��Э�����

	public IOThread(Socket socket, IOStrategy ios) {
		this.socket = socket;
		this.ios = ios;
	}

	public void run() {
		ios.service(socket); // ִ��Э��
	}
}
