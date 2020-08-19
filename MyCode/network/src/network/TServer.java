package network;
import java.io.*;
import java.net.*;

public class TServer { // TServer.java，完成接受客户端的连接请求，接受命令，提供服务
	public static void main(String[] args) {
		try {
			int port = 4321; // 和客户端的请求的端口号要一致
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			// 在指定的端口监听客户端的连接请求
			ServerSocket ss = new ServerSocket(port);
			System.out.println("server is ready to provide service.");
			Socket s = ss.accept(); // 该方法提供阻塞，如果有连接请求则解除阻塞
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			while (true) {
				int command = dis.readInt(); // 从客户端接受协议命令
				switch (command) // 根据不同的客户协议命令，执行不同的程序处理
				{
				case 1: // 执行加法操作
					dos.writeInt(dis.readInt() + dis.readInt());
					break;
				case 2: // 执行echo操作
					dos.writeUTF("echo " + dis.readUTF());
					break;
				}
				dos.flush(); // 强制输出缓存中的数据
			}
		} catch (Exception e) {
		}
	}
}
