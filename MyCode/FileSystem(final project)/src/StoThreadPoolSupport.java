import java.net.*;
import java.util.ArrayList;
public class StoThreadPoolSupport implements IOStoStrategy{

	private ArrayList threads = new ArrayList();
	private final int MAX_THREADS = 100;
	private IOStoStrategy ios = null;

	public StoThreadPoolSupport(IOStoStrategy ios) { // 创建线程池
		this.ios = ios;
		for (int i = 0; i < MAX_THREADS; i++) {
			IOStoThread t = new IOStoThread(ios); // 传递协议对象，但是还没有socket
			t.start(); // 启动线程，进入线程体后都是wait
			threads.add(t);
		}
		try {
			Thread.sleep(300);
		} catch (Exception e) {
		} // 等待线程池的线程都“运行”
	}

	public void serviceNode(DatagramPacket packet) {
		IOStoThread t = null; // 把客户端交给“它”处理
		boolean found = false;
		for (int i = 0; i < threads.size(); i++) {
			t = (IOStoThread) threads.get(i);
			if (t.isIdle()) {
				found = true;
				break;
			}
		}
		if (!found) // 线程池中的线程都忙，没有办法了，只有创建
		{ // 一个线程了，同时添加到线程池中。
			t = new IOStoThread(ios);
			t.start();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
			threads.add(t);
		}
		t.setPacket(packet); // 将服务器端的socket对象传递给这个空闲的线程
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
