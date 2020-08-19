import java.net.*;
public class IOStoThread extends Thread {
	private IOStoStrategy ios=null;
	private DatagramPacket packet=null;
	
	public IOStoThread(IOStoStrategy ios) { 
		this.ios = ios; 
	}

	public boolean isIdle() { // 如果socket变量为空，那么这个线程当然是空闲的
		return packet == null;
	}

	public synchronized void setPacket(DatagramPacket packet) {
		this.packet = packet; 
		notify();
	}

	public synchronized void run() { // 这个同步方法并不是保护什么共享数据，
		while (true) { // 仅仅因为wait方法调用必须拥有对象锁
			try {
				wait(); // 进入线程体后，立刻进入阻塞，等待状态
				ios.serviceNode(packet); // 被唤醒后，立刻开始执行服务协议
				packet = null; // 服务结束后，立刻返回到空闲状态
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
