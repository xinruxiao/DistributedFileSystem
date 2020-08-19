import java.net.*;
public class IOStoThread extends Thread {
	private IOStoStrategy ios=null;
	private DatagramPacket packet=null;
	
	public IOStoThread(IOStoStrategy ios) { 
		this.ios = ios; 
	}

	public boolean isIdle() { // ���socket����Ϊ�գ���ô����̵߳�Ȼ�ǿ��е�
		return packet == null;
	}

	public synchronized void setPacket(DatagramPacket packet) {
		this.packet = packet; 
		notify();
	}

	public synchronized void run() { // ���ͬ�����������Ǳ���ʲô�������ݣ�
		while (true) { // ������Ϊwait�������ñ���ӵ�ж�����
			try {
				wait(); // �����߳�������̽����������ȴ�״̬
				ios.serviceNode(packet); // �����Ѻ����̿�ʼִ�з���Э��
				packet = null; // ������������̷��ص�����״̬
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
