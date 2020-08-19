package network;
import java.io.*;
import java.net.*;

public class TClient { // TClient.java����ɴ����������������ӣ�����ĵ���
	public static void main(String[] args) throws Exception {
		int port = 4321; // Ҳ���Դ������ļ��м�������˿�����.
		String host = "localhost";
		if (args.length == 2) {
			port = Integer.parseInt(args[1]);
			host = args[0];
		}
		Socket s = new Socket(host, port); // ����Socket����
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeInt(1); // Э������
		dos.writeInt(5); // �����������ݸ�������������ӷ�����
		dos.writeInt(6);
		dos.flush(); // ǿ����������е�����
		System.out.println("5 + 6 = " + dis.readInt()); // �ӷ������˶�����
		dos.writeInt(2); // Э������
		dos.writeUTF("nihao"); // �����ַ�������������
		dos.flush(); // ǿ����������е�����
		System.out.println(dis.readUTF()); // �ӷ������˶�����
		s.close(); // ����ͨ�Ž������ر�Socket����
	}
}
