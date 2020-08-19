import java.net.*;
import java.util.ArrayList;
public class StoThreadPoolSupport implements IOStoStrategy{

	private ArrayList threads = new ArrayList();
	private final int MAX_THREADS = 100;
	private IOStoStrategy ios = null;

	public StoThreadPoolSupport(IOStoStrategy ios) { // �����̳߳�
		this.ios = ios;
		for (int i = 0; i < MAX_THREADS; i++) {
			IOStoThread t = new IOStoThread(ios); // ����Э����󣬵��ǻ�û��socket
			t.start(); // �����̣߳������߳������wait
			threads.add(t);
		}
		try {
			Thread.sleep(300);
		} catch (Exception e) {
		} // �ȴ��̳߳ص��̶߳������С�
	}

	public void serviceNode(DatagramPacket packet) {
		IOStoThread t = null; // �ѿͻ��˽�������������
		boolean found = false;
		for (int i = 0; i < threads.size(); i++) {
			t = (IOStoThread) threads.get(i);
			if (t.isIdle()) {
				found = true;
				break;
			}
		}
		if (!found) // �̳߳��е��̶߳�æ��û�а취�ˣ�ֻ�д���
		{ // һ���߳��ˣ�ͬʱ��ӵ��̳߳��С�
			t = new IOStoThread(ios);
			t.start();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
			threads.add(t);
		}
		t.setPacket(packet); // ���������˵�socket���󴫵ݸ�������е��߳�
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
