import java.net.*;
public class IOThread extends Thread {
	private IOStrategy ios=null;
	private Socket socket=null;
	
	public IOThread(IOStrategy ios) { 
		this.ios = ios; 
	}

	public boolean isIdle() { // ���socket����Ϊ�գ���ô����̵߳�Ȼ�ǿ��е�
		return socket == null;
	}

	public synchronized void setSocket(Socket socket) {
		this.socket = socket; 
		notify();
	}

	public synchronized void run() { // ���ͬ�����������Ǳ���ʲô�������ݣ�
		while (true) { // ������Ϊwait�������ñ���ӵ�ж�����
			try {
				wait(); // �����߳�������̽����������ȴ�״̬
				ios.service(socket); // �����Ѻ����̿�ʼִ�з���Э��
				socket = null; // ������������̷��ص�����״̬
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
