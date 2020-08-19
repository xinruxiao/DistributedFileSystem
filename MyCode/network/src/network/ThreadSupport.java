package network;
public class ThreadSupport implements IOStrategy { // ThreadSupport.java
	private IOStrategy ios; // ���ios����������һ��Э�����

	public ThreadSupport(IOStrategy ios) { // ͨ�����췽�����ݽ���һ��Э�����
		this.ios = ios;
	}

	public void service(java.net.Socket socket) { // ����һ������������򴴽�
		new IOThread(socket, ios).start(); // һ���̣߳����߳�ȥִ��
	} // �Ϳͻ��˵�ͨ��Э��
}
