package network;
import java.io.*;
import java.net.*;

public class TClient { // TClient.java，完成创建到服务器的连接，服务的调用
	public static void main(String[] args) throws Exception {
		int port = 4321; // 也可以从属性文件中加载这个端口数据.
		String host = "localhost";
		if (args.length == 2) {
			port = Integer.parseInt(args[1]);
			host = args[0];
		}
		Socket s = new Socket(host, port); // 创建Socket对象
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		dos.writeInt(1); // 协议命令
		dos.writeInt(5); // 传递两个数据给服务器，请求加法计算
		dos.writeInt(6);
		dos.flush(); // 强制输出缓存中的数据
		System.out.println("5 + 6 = " + dis.readInt()); // 从服务器端读数据
		dos.writeInt(2); // 协议命令
		dos.writeUTF("nihao"); // 发送字符串给服务器端
		dos.flush(); // 强制输出缓存中的数据
		System.out.println(dis.readUTF()); // 从服务器端读数据
		s.close(); // 本次通信结束，关闭Socket对象
	}
}
