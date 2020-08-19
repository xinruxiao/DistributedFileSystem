import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NodeProtocol implements IOStrategy{
	
	public void service(Socket socket) {
		try{
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			int command = 0;
			String fileID=null;
			String clientName=null;
			String nodeTwoIP=null;
			String nodeTwoPort=null;
			
			while (true) {	
				
				//命令信息
				command = dis.readInt(); 
				clientName=dis.readUTF();
				fileID=dis.readUTF();
				File file=new File(StorageNode.getRootFolder());
				
				if(command==0) {   //上传文件
					
					//备份节点的IP和端口
					nodeTwoIP=dis.readUTF();
					nodeTwoPort=dis.readUTF();
					
					/*读字节流到文件中*/					
					new File(file.getAbsolutePath()+"\\"+clientName).mkdir();
					File file1=new File(file.getAbsolutePath()+"\\"+clientName);
					File file2=new File(file1.getAbsolutePath()+"\\"+fileID+".dat");
					file2.createNewFile();
					FileOutputStream fos=new FileOutputStream(file2);
					DataOutputStream dosFile2=new DataOutputStream(fos);
					
					//读传输的文件，并存入文件file2中
					while(true) {
						int length=dis.readInt();
						if(length==-1) 
							break;
						byte[] recData=new byte[length];
						dis.read(recData);
						dosFile2.writeInt(length);
						dosFile2.write(recData);
					}
					//文件接收完成
					dosFile2.writeInt(-1);
					dosFile2.close();
					fos.close();
					
					StorageNode.setCurrentVolume(file2.length(), true);
					
					/*并且访问备份节点，再将该文件传给备份文件*/
					if(!nodeTwoIP.equals("null")) {
						Socket socketTwo=new Socket(InetAddress.getByName(nodeTwoIP),Integer.parseInt(nodeTwoPort));
						dis=new DataInputStream(socketTwo.getInputStream());
						dos=new DataOutputStream(socketTwo.getOutputStream());
						dos.writeInt(3);
						dos.writeUTF(clientName);
						dos.writeUTF(fileID);
						dos.flush();
						
						FileInputStream fis=new FileInputStream(file2);
						DataInputStream disFile2=new DataInputStream(fis);
						while(true) {
							int length=disFile2.readInt();
							if(length==-1)
								break;
							byte[] sendData=new byte[length];
							disFile2.readFully(sendData);
							dos.writeInt(length);
							dos.write(sendData,0,length);
							dos.flush();
						}
						dos.writeInt(-1);
						disFile2.close();
						fis.close();
						socketTwo.close();
					}				
				}
				else if(command==1) { //下载文件
					
					/*从文件中读字节并输入到串口中*/
					new File(file.getAbsolutePath()+"\\"+clientName).mkdir();
					File file1=new File(file.getAbsolutePath()+"\\"+clientName);
					File[] allFile=file1.listFiles();
					int i=0;
					for(;i<allFile.length;++i) {
						if(fileID.equals(allFile[i].getName().substring(0,allFile[i].getName().indexOf(".")))) {			
							//找到要下载的文件之后，进行文件的传输
							FileInputStream fis=new FileInputStream(allFile[i]);
							DataInputStream disFile=new DataInputStream(fis);
							
							while(true) {
								int length=disFile.readInt();
								if(length==-1)
									break;
								byte[] sendData=new byte[length];
								disFile.readFully(sendData);
								dos.writeInt(length);
								dos.write(sendData,0,length);
								dos.flush();
							}
							disFile.close();
							fis.close();
							dos.writeInt(-1);
							dos.flush();
							
							break;
						}
					}
					if(i==allFile.length) {
						System.out.println("客户"+clientName+"并没有上传过此文件，无法进行下载......");
					}
				}
				else if(command==2) {
					//删除
					//备份节点的信息
					nodeTwoIP=dis.readUTF();
					nodeTwoPort=dis.readUTF();
					
					/*删除对应的文件*/
					new File(file.getAbsolutePath()+"\\"+clientName).mkdir();
					File file1=new File(file.getAbsolutePath()+"\\"+clientName);
					File[] allFile=file1.listFiles();
					int i=0;
					for(;i<allFile.length;++i) {
						if((fileID+".dat").equals(allFile[i].getName())) {
							StorageNode.setCurrentVolume(allFile[i].length(), false);
							allFile[i].delete();
							break;
						}
					}
					if(i==allFile.length){
						System.out.println("客户"+clientName+"并没有上传过此文件，无法进行删除......");
					}
					
					/*并且访问备份节点，删除备份节点中相应的文件*/
					if(nodeTwoIP!=null) {
						Socket socketTwo=new Socket(InetAddress.getByName(nodeTwoIP),Integer.parseInt(nodeTwoPort));
						dis=new DataInputStream(socketTwo.getInputStream());
						dos=new DataOutputStream(socketTwo.getOutputStream());
						dos.writeInt(4);
						dos.writeUTF(clientName);
						dos.writeUTF(fileID);
						dos.flush();
						
						socketTwo.close();
					}
				}
				else if(command==3) {	
					
					/*主存储结点向备份节点上传文件*/				
   				    //读字节流到文件中					
					new File(file.getAbsolutePath()+"\\"+clientName).mkdir();
					File file1=new File(file.getAbsolutePath()+"\\"+clientName);
					File file2=new File(file1.getAbsolutePath()+"\\"+fileID+".dat");
					file2.createNewFile();
					FileOutputStream fos=new FileOutputStream(file2);
					DataOutputStream dosFile2=new DataOutputStream(fos);
					
					//读传输的文件				
					while(true) {
						int length=dis.readInt();
						if(length==-1) 
							break;
						byte[] recData=new byte[length];
						dis.read(recData);
						dosFile2.writeInt(length);
						dosFile2.write(recData);
					}
					dosFile2.writeInt(-1);
					
					dosFile2.close();
					fos.close();
					
					StorageNode.setCurrentVolume(file2.length(), true);
				}
				else if(command==4) {
					
					/*备份节点删除文件*/
					new File(file.getAbsolutePath()+"\\"+clientName).mkdir();
					File file1=new File(file.getAbsolutePath()+"\\"+clientName);
					File[] allFile=file1.listFiles();
					int i=0;
					for(;i<allFile.length;++i) {
						if((fileID+".dat").equals(allFile[i].getName())) {
							StorageNode.setCurrentVolume(allFile[i].length(), false);
							allFile[i].delete();
							break;
						}
					}
					if(i==allFile.length){
						System.out.println("客户"+clientName+"并没有上传过此文件，无法进行删除......");
					}
				}
			}

		} 
		catch(Exception e) {
			System.out.println("");
		}		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
