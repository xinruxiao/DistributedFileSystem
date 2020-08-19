import java.io.*;
import java.util.*;
import java.net.*;
public class FileClient {
	
	//�洢�ļ���ŵ��ļ����ֵ�ӳ��
	public static Map map=new HashMap();
		
	//�����ļ�����
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
	
	//�ϴ��ļ�
	public static void upload(String fileName) {
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("�����������ߣ��޷��ṩ����......");
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
			
			//���������ص���Ϣ
			fileID=dis.readUTF();
			map.put(fileID, fileName);
			System.out.println("��������Ӧ:");
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();//�Ͽ����������TCP����
			
			Socket s1;
			try{
				//�����洢�ڵ㷢������
				s1=new Socket(InetAddress.getByName(NodeOneIP),
						Integer.parseInt(NodeOnePort));
				dis=new DataInputStream(s1.getInputStream());
				dos=new DataOutputStream(s1.getOutputStream());
				dos.writeInt(0);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				//��֪���洢�ڵ㱸�ݽڵ����Ϣ�������洢�ڵ㸺�𱸷ݽڵ�Ĳ���
				dos.writeUTF(NodeTwoIP);
				dos.writeUTF(NodeTwoPort);
				dos.flush();
				
				/*�����ļ����ϴ�*/
				long allLength=file.length();
				long tempLength=0;
				int length=0;
				//�����ļ��Ĵ���
				while(tempLength<allLength) {
					//�ϴ�ʱ�ĸ�ʽΪ  �ֽ�����ĳ���  �ֽ�����
					byte[] bufferData=new byte[1024];
					length=fis.read(bufferData);//���ļ��ж���bufferData,l�Ƕ���ȥ�ĳ���
					tempLength+=length;
					byte[] sendData=Tool.subByteArray(bufferData, 0,length);//�������ѿյ�ȥ��
					byte[] encryptedData=Tool.encrypt(sendData);
					byte[] compressedAndEncryptData=Tool.compress(encryptedData);
					dos.writeInt(compressedAndEncryptData.length);
					dos.write(compressedAndEncryptData,0,compressedAndEncryptData.length);
					dos.flush();
				}
				//�ļ����������д��-1
				dos.writeInt(-1);
				dos.flush();
				fis.close();
				s1.close(); //�Ͽ���洢�ڵ��TCP����
				System.out.println("�ļ��ϴ��ɹ�......");
				
			} catch (ConnectException e) {
				System.out.println("���洢�ڵ㲻����......");
				System.out.println("�����ӱ��ݽڵ�......");

				s1 = new Socket(InetAddress.getByName(NodeTwoIP), Integer.parseInt(NodeTwoPort));
				dis = new DataInputStream(s1.getInputStream());
				dos = new DataOutputStream(s1.getOutputStream());
				dos.writeInt(3);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				dos.flush();

				/* �����ļ����ϴ� */
				long allLength = file.length();
				long tempLength = 0;
				int length = 0;
				// �����ļ��Ĵ���
				while (tempLength < allLength) {
					// �ϴ�ʱ�ĸ�ʽΪ �ֽ�����ĳ��� �ֽ�����
					byte[] bufferData = new byte[1024];
					length = fis.read(bufferData);// ���ļ��ж���bufferData,l�Ƕ���ȥ�ĳ���
					tempLength += length;
					byte[] sendData = Tool.subByteArray(bufferData, 0, length);// �������ѿյ�ȥ��
					byte[] encryptedData = Tool.encrypt(sendData);
					byte[] compressedAndEncryptData = Tool.compress(encryptedData);
					dos.writeInt(compressedAndEncryptData.length);
					dos.write(compressedAndEncryptData, 0, compressedAndEncryptData.length);
					dos.flush();
				}
				//�ļ����������д��-1
				dos.writeInt(-1);
				dos.flush();
				fis.close();
				s1.close();
				System.out.println("�ļ��ϴ��ɹ�......");
			}
				
		}catch(FileNotFoundException e) {
			System.out.println("���ز����ڸ��ļ�,�ϴ�ʧ��......");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//�����ļ�
	public static void download(String fileName) {
		
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("�����������ߣ��޷��ṩ����......");
				System.exit(0);
			}
			
			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			
			String fileID;
			
			dos.writeInt(1);
			dos.writeUTF(fileName);
			dos.flush();
			
			//���������ص���Ϣ
			fileID=fileName;
			String originalName=dis.readUTF();  //�ļ���ԭʼ����
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();
			
			Socket s1;
			
			try{
				/*�����洢�ڵ㷢������*/
				s1=new Socket(InetAddress.getByName(NodeOneIP),Integer.parseInt(NodeOnePort));
			}catch(ConnectException e){
				System.out.println("���洢�ڵ㲻����......");
				System.out.println("�����ӱ��ݽڵ�......");		
				s1=new Socket(InetAddress.getByName(NodeTwoIP),Integer.parseInt(NodeTwoPort));
			}
				
			dis=new DataInputStream(s1.getInputStream());
			dos=new DataOutputStream(s1.getOutputStream());
			dos.writeInt(1);
			dos.writeUTF(FileClient.getCilentName());
			dos.writeUTF(fileID);
			dos.flush();
					
			/*�����ļ�������*/
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
			System.out.println("�ļ����سɹ�......");
			
		}catch(ConnectException e) {
			System.out.println("���ݽڵ�Ҳ�����ã�����ʧ��......");
		}catch(Exception e){
//			e.printStackTrace();
		}	
	}
	//ɾ���ļ�
	public static void remove(String fileName) {
		try{
			
			Socket s=null;
			try {
				s=new Socket(InetAddress.getByName((FileClient.getFileServerIP())),Integer.parseInt(FileClient.getFileServerPort()));
			}catch(ConnectException e){
				System.out.println("�����������ߣ��޷��ṩ����......");
				System.exit(0);
			}
			
			
			DataInputStream dis=new DataInputStream(s.getInputStream());
			DataOutputStream dos=new DataOutputStream(s.getOutputStream());
			
			String fileID;
			
			dos.writeInt(2);
			dos.writeUTF(fileName);
			dos.flush();
			
			//���������ص���Ϣ
			fileID=fileName;
			dis.readUTF();  //������Ϣ���ļ���ԭʼ����
			String NodeOneIP=dis.readUTF();
			String NodeOnePort=dis.readUTF();
			String NodeTwoIP=dis.readUTF();
			String NodeTwoPort=dis.readUTF();
			s.close();
			
			Socket s1;
			
			
			try{
				/*�����洢�ڵ㷢������*/
				s1=new Socket(InetAddress.getByName(NodeOneIP),
						Integer.parseInt(NodeOnePort));
				dis=new DataInputStream(s1.getInputStream());
				dos=new DataOutputStream(s1.getOutputStream());
				dos.writeInt(2);
				dos.writeUTF(FileClient.getCilentName());
				dos.writeUTF(fileID);
				//��֪���洢�ڵ㱸�ݽڵ����Ϣ�������洢�ڵ㸺�𱸷ݽڵ�Ĳ���
				dos.writeUTF(NodeTwoIP);
				dos.writeUTF(NodeTwoPort);
				dos.flush();
				
			}catch(ConnectException e) {
				
				System.out.println("���洢�ڵ㲻����......");
				System.out.println("�����ӱ��ݽڵ�......");		
				
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
			System.out.println("�������......");
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
			
			//���������в����ж��ϴ������أ�ɾ��
			if(args[1].equals("upload"))  
				upload(args[2]);  //���������洢�ڵ��ϴ�
			else if(args[1].equals("download"))  //�����ļ�
				download(args[2]);
			else if(args[1].equals("remove"))  //ɾ���ļ�
				remove(args[2]);
			else
				System.out.println("�����������......");
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
}
