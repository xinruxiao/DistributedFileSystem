import java.io.*;
import java.util.*;
import java.net.*;
public class FileClient {
	
	//存储文件编号到文件名字的映射
	public static Map map=new HashMap();
		
	//配置文件中有
	private static String clientName;
	private static String fileServerPort;
	private static String fileServerIP;
	
	public static String getCilentName() {
		return clientName;
	}
	public static String getFileServerPort() {
		return fileServerPort;
	}
	public static String getFileServerIP() {
		return fileServerIP;
	}
	
	public FileClient(Properties p) {
		try {
			clientName=p.getProperty("ClientName");
			fileServerPort=p.getProperty("FileServerPort");
			fileServerIP=p.getProperty("FileServerIP");
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	//上传文件
	public static void upload(String fileName) {
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("服务器不在线，无法提供服务......");
				System.exit(0);
			}

			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			
			String fileID;
			
			dos.writeInt(0);
			dos.writeUTF(fileName.substring(fileName.lastIndexOf("\\")+1));

			File file=new File(fileName);
			FileInputStream fis=new FileInputStream(file);
			dos.writeUTF(file.length()+"");
			dos.flush();
			
			//服务器返回的信息
			fileID=dis.readUTF();
			map.put(fileID, fileName);
			System.out.println("服务器响应:");
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();//断开与服务器的TCP连接
			
			Socket s1;
			try{
				//向主存储节点发出请求
				s1=new Socket(InetAddress.getByName(NodeOneIP),
						Integer.parseInt(NodeOnePort));
				dis=new DataInputStream(s1.getInputStream());
				dos=new DataOutputStream(s1.getOutputStream());
				dos.writeInt(0);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				//告知主存储节点备份节点的信息，由主存储节点负责备份节点的操作
				dos.writeUTF(NodeTwoIP);
				dos.writeUTF(NodeTwoPort);
				dos.flush();
				
				/*进行文件的上传*/
				long allLength=file.length();
				long tempLength=0;
				int length=0;
				//进行文件的传输
				while(tempLength<allLength) {
					//上传时的格式为  字节数组的长度  字节数组
					byte[] bufferData=new byte[1024];
					length=fis.read(bufferData);//从文件中读到bufferData,l是读进去的长度
					tempLength+=length;
					byte[] sendData=Tool.subByteArray(bufferData, 0,length);//读不满把空的去掉
					byte[] encryptedData=Tool.encrypt(sendData);
					byte[] compressedAndEncryptData=Tool.compress(encryptedData);
					dos.writeInt(compressedAndEncryptData.length);
					dos.write(compressedAndEncryptData,0,compressedAndEncryptData.length);
					dos.flush();
				}
				//文件传输结束，写入-1
				dos.writeInt(-1);
				dos.flush();
				fis.close();
				s1.close(); //断开与存储节点的TCP连接
				System.out.println("文件上传成功......");
				
			} catch (ConnectException e) {
				System.out.println("主存储节点不可用......");
				System.out.println("将连接备份节点......");

				s1 = new Socket(InetAddress.getByName(NodeTwoIP), Integer.parseInt(NodeTwoPort));
				dis = new DataInputStream(s1.getInputStream());
				dos = new DataOutputStream(s1.getOutputStream());
				dos.writeInt(3);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				dos.flush();

				/* 进行文件的上传 */
				long allLength = file.length();
				long tempLength = 0;
				int length = 0;
				// 进行文件的传输
				while (tempLength < allLength) {
					// 上传时的格式为 字节数组的长度 字节数组
					byte[] bufferData = new byte[1024];
					length = fis.read(bufferData);// 从文件中读到bufferData,l是读进去的长度
					tempLength += length;
					byte[] sendData = Tool.subByteArray(bufferData, 0, length);// 读不满把空的去掉
					byte[] encryptedData = Tool.encrypt(sendData);
					byte[] compressedAndEncryptData = Tool.compress(encryptedData);
					dos.writeInt(compressedAndEncryptData.length);
					dos.write(compressedAndEncryptData, 0, compressedAndEncryptData.length);
					dos.flush();
				}
				//文件传输结束，写入-1
				dos.writeInt(-1);
				dos.flush();
				fis.close();
				s1.close();
				System.out.println("文件上传成功......");
			}
				
		}catch(FileNotFoundException e) {
			System.out.println("本地不存在该文件,上传失败......");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//下载文件
	public static void download(String fileName) {
		
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("服务器不在线，无法提供服务......");
				System.exit(0);
			}
			
			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			
			String fileID;
			
			dos.writeInt(1);
			dos.writeUTF(fileName);
			dos.flush();
			
			//服务器返回的信息
			fileID=fileName;
			String originalName=dis.readUTF();  //文件的原始名字
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();
			
			Socket s1;
			
			try{
				/*向主存储节点发出请求*/
				s1=new Socket(InetAddress.getByName(NodeOneIP),Integer.parseInt(NodeOnePort));
			}catch(ConnectException e){
				System.out.println("主存储节点不可用......");
				System.out.println("将连接备份节点......");		
				s1=new Socket(InetAddress.getByName(NodeTwoIP),Integer.parseInt(NodeTwoPort));
			}
				
			dis=new DataInputStream(s1.getInputStream());
			dos=new DataOutputStream(s1.getOutputStream());
			dos.writeInt(1);
			dos.writeUTF(FileClient.getCilentName());
			dos.writeUTF(fileID);
			dos.flush();
					
			/*进行文件的下载*/
			File file=new File("E:\\result\\"+originalName);
			file.createNewFile();

			FileOutputStream fos=new FileOutputStream(file);	
			while(true) {
				int length=dis.readInt();
				if(length==-1)
					break;
				byte[] recData=new byte[length];
				dis.read(recData);
				byte[] decompressedData=Tool.decompress(recData);
				byte[] endData=Tool.decrypt(decompressedData);
				fos.write(endData);
			}
			fos.close();
			s1.close();
			System.out.println("文件下载成功......");
			
		}catch(ConnectException e) {
			System.out.println("备份节点也不可用，下载失败......");
		}catch(Exception e){
//			e.printStackTrace();
		}	
	}
	//删除文件
	public static void remove(String fileName) {
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("服务器不在线，无法提供服务......");
				System.exit(0);
			}
			
			
			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			
			String fileID;
			
			dos.writeInt(2);
			dos.writeUTF(fileName);
			dos.flush();
			
			//服务器返回的信息
			fileID=fileName;
			dis.readUTF();  //无用信息，文件的原始名字
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();
			
			Socket s1;
			
			
			try{
				/*向主存储节点发出请求*/
				s1=new Socket(InetAddress.getByName(NodeOneIP),
						Integer.parseInt(NodeOnePort));
				dis=new DataInputStream(s1.getInputStream());
				dos=new DataOutputStream(s1.getOutputStream());
				dos.writeInt(2);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				//告知主存储节点备份节点的信息，由主存储节点负责备份节点的操作
				dos.writeUTF(NodeTwoIP);
				dos.writeUTF(NodeTwoPort);
				dos.flush();
				
			}catch(ConnectException e) {
				
				System.out.println("主存储节点不可用......");
				System.out.println("将连接备份节点......");		
				
				s1=new Socket(InetAddress.getByName(NodeTwoIP),
						Integer.parseInt(NodeTwoPort));
				dis=new DataInputStream(s1.getInputStream());
				dos=new DataOutputStream(s1.getOutputStream());
				dos.writeInt(4);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				dos.flush();
			}	
			s1.close();
			
		}catch(SocketException e){
			System.out.println("操作完成......");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{		
			
			Properties p=new Properties();
			p.load(new FileInputStream(args[0]));
			new FileClient(p);
			
			//根据命令行参数判断上传，下载，删除
			if(args[1].equals("upload"))  
				upload(args[2]);  //优先向主存储节点上传
			else if(args[1].equals("download"))  //下载文件
				download(args[2]);
			else if(args[1].equals("remove"))  //删除文件
				remove(args[2]);
			else
				System.out.println("命令输入错误......");
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
}
