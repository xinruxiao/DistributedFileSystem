import java.io.*;
import java.net.*;
import java.util.*;

public class FileProtocol implements IOStrategy {
	
	public void upload(DataInputStream dis,DataOutputStream dos) {
		try {
			String fileName=dis.readUTF();
			String fileLength=dis.readUTF();
			
			String fileID=UUID.randomUUID().toString();
					
			/*找到合适的主存储和备份节点*/
			String storageNodeOne=null;
			long currentVolumeOne=0;
			
			
			Set set=FileServer.storageNodeMap.keySet();
			Iterator it=set.iterator();
			Object key;
			while(it.hasNext()) {
				key=it.next();
				Map nodeMap=(Map) FileServer.storageNodeMap.get(key);
				if(currentVolumeOne<=
						StorageNode.dealWithVolume((String)nodeMap.get("CurrentVolume"))) {		
					currentVolumeOne =StorageNode.dealWithVolume((String)nodeMap.get("CurrentVolume"));
					storageNodeOne=(String)key;
				}
			}
			
			String storageNodeTwo=null;
			long currentVolumeTwo=0;
			set=FileServer.storageNodeMap.keySet();
			it=set.iterator();
			while(it.hasNext()) {
				key=it.next();
				Map nodeMap=(Map) FileServer.storageNodeMap.get(key);
				if((!((String)key).equals(storageNodeOne))&&currentVolumeTwo<=
						StorageNode.dealWithVolume((String)nodeMap.get("CurrentVolume"))) {		
					currentVolumeTwo =StorageNode.dealWithVolume((String)nodeMap.get("CurrentVolume"));
					storageNodeTwo=(String)key;
				}
			}
			
			Map fileMap=new HashMap();
			fileMap.put("FileName", fileName);
			fileMap.put("FileLength", fileLength);
			fileMap.put("StorageNodeOne",storageNodeOne);
			fileMap.put("StorageNodeTwo", storageNodeTwo);
			
			FileServer.clientFileMap.put(fileID, fileMap);
			
			//返回文件的编号以及该文件存储的主节点和备份节点的信息
			dos.writeUTF(fileID);
			Map tempMap=new HashMap();
			tempMap=FileServer.storageNodeMap.get(storageNodeOne);
			dos.writeUTF((String)tempMap.get("NodeIP"));
			dos.writeUTF((String)tempMap.get("NodePort"));
					
			if(storageNodeTwo==null) {
				dos.writeUTF("null");
				dos.writeUTF("null");
			}
			else {
				tempMap=FileServer.storageNodeMap.get(storageNodeTwo);
				dos.writeUTF((String)tempMap.get("NodeIP"));
				dos.writeUTF((String)tempMap.get("NodePort"));
			}			
			dos.flush();
		} catch(Exception e) {

		}
		
	}
	public void findNode(String fileID,DataOutputStream dos) {
		try {
			
			/*找到主存储和备份节点*/
			Map fileMap=null;
			Set set=FileServer.clientFileMap.keySet();
			Iterator it=set.iterator();
			while(it.hasNext()) {
				String key=(String)it.next();
				if(key.equals(fileID)) {
					fileMap=(Map)FileServer.clientFileMap.get(key);
					break;
				}			
			}
			
			
			dos.writeUTF((String)fileMap.get("FileName"));
			if(fileMap.get("StorageNodeOne")==null){
				dos.writeUTF("null");
				dos.writeUTF("null");
			}
			else {
				dos.writeUTF((String)FileServer.storageNodeMap.get(fileMap.get("StorageNodeOne")).get("NodeIP"));
				dos.writeUTF((String)FileServer.storageNodeMap.get(fileMap.get("StorageNodeOne")).get("NodePort"));
			}	
			if(fileMap.get("StorageNodeTwo")==null) {
				dos.writeUTF("null");
				dos.writeUTF("null");
			}
			else {
				dos.writeUTF((String)FileServer.storageNodeMap.get(fileMap.get("StorageNodeTwo")).get("NodeIP"));
				dos.writeUTF((String)FileServer.storageNodeMap.get(fileMap.get("StorageNodeTwo")).get("NodePort"));
			}	
			dos.flush();
			
		} catch(NullPointerException e){
			
			System.out.println("输入错误,不存在以该文件编号命名的文件......");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void service(Socket socket) { 
		try { 
			
			DataInputStream dis = 
					new DataInputStream(socket.getInputStream());
			DataOutputStream dos = 
					new DataOutputStream(socket.getOutputStream());
			
			int command = 0;
			String fileID=null;
			while (true) {
				command = dis.readInt(); 
				switch (command) {
				case 0:
					upload(dis,dos);
					break;
				case 1:
					fileID=dis.readUTF();
					findNode(fileID,dos);
					break;
				case 2: 
					fileID=dis.readUTF();
					findNode(fileID,dos);
					FileServer.clientFileMap.remove(fileID);
					break;
				}
			}
				
		} catch (Exception e) {
			System.out.println("client disconnected.");
		}
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
