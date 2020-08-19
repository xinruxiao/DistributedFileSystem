package network;
import java.net.Socket;

public class IOThread extends Thread { // 扩展的线程类，IOThread.java
	private Socket socket; // 执行和客户端的通信协议
	private IOStrategy ios; // ios指向一个协议对象

	public IOThread(Socket socket, IOStrategy ios) {
		this.socket = socket;
		this.ios = ios;
	}

	public void run() {
		ios.service(socket); // 执行协议
	}
}
