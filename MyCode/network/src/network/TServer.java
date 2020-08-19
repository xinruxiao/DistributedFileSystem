package network;
import java.io.*;
import java.net.*;

public class TServer { // TServer.java����ɽ��ܿͻ��˵��������󣬽�������ṩ����
	public static void main(String[] args) {
		try {
			int port = 4321; // �Ϳͻ��˵�����Ķ˿ں�Ҫһ��
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			// ��ָ���Ķ˿ڼ����ͻ��˵���������
			ServerSocket ss = new ServerSocket(port);
			System.out.println("server is ready to provide service.");
			Socket s = ss.accept(); // �÷����ṩ���������������������������
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			while (true) {
				int command = dis.readInt(); // �ӿͻ��˽���Э������
				switch (command) // ���ݲ�ͬ�Ŀͻ�Э�����ִ�в�ͬ�ĳ�����
				{
				case 1: // ִ�мӷ�����
					dos.writeInt(dis.readInt() + dis.readInt());
					break;
				case 2: // ִ��echo����
					dos.writeUTF("echo " + dis.readUTF());
					break;
				}
				dos.flush(); // ǿ����������е�����
			}
		} catch (Exception e) {
		}
	}
}
