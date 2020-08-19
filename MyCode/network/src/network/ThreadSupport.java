package network;
public class ThreadSupport implements IOStrategy { // ThreadSupport.java
	private IOStrategy ios; // 这个ios变量将引用一个协议对象

	public ThreadSupport(IOStrategy ios) { // 通过构造方法传递进来一个协议对象
		this.ios = ios;
	}

	public void service(java.net.Socket socket) { // 调用一次这个方法，则创建
		new IOThread(socket, ios).start(); // 一个线程，该线程去执行
	} // 和客户端的通信协议
}
