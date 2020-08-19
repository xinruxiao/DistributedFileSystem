import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.util.*;

public class MyTableModel implements TableModel {
	private Map<String,Map> al; // ���ݼ��ص����ݼ�������һ�����ģ��
	private int rowCount = 0; // �����������

	public MyTableModel(Map<String,Map> al) {
		this.al = al;
		rowCount = al.size();
	}

	public int getRowCount() { // ���ر��ģ�͵ļ�¼����
		return rowCount;
	}

	public int getColumnCount() { // ���ر��ģ�͵�����
		return 8;
	}

	public String getColumnName(int columnIndex) { // ���ر��ģ�͵�����
		switch (columnIndex) {
		case 0:
			return "�ڵ�����";
		case 1:
			return "�Ƿ����";
		case 2:
			return "IP��ַ";
		case 3:
			return "�˿ں�";
		case 4:
			return "����";
		case 5:
			return "ʣ������";
		case 6:
			return "ʵ������";
		case 7:
			return "�ļ�����";
		}
		return null;
	}

	public Class<?> getColumnClass(int columnIndex) { // ��������ݵ�����
		switch (columnIndex) {
		
		//���ݾ���Ϊ�ַ�������
		case 0: 
		case 1:
		case 2:
		case 3:
		case 4: 
		case 5:
		case 6:
		case 7:
			return String.class;
		}
		return null;
	}

	// ����е������ܷ�༭������Ϊfalse�����ɱ༭��
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) { // ���ر���е�����
		
		int count=0;
		String name="";
		Map tempMap=new HashMap();
		Set set=FileServer.storageNodeMap.keySet();
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Object key=it.next();
			name=(String)key;
			tempMap=FileServer.storageNodeMap.get(key);
			if(count==rowIndex) 
				break;
			++count;
		}

		switch (columnIndex) { // ���������ظ������ݵ�ĳ���е�ֵ
		case 0:
			return name;
		case 1:
			if(tempMap.get("Flag").equals("1"))
				return "����";
			else {
				return "������";
			}
		case 2:
			return tempMap.get("NodeIP");
		case 3:
			return tempMap.get("NodePort");
		case 4:
			return tempMap.get("Volume");
		case 5:
			return tempMap.get("CurrentVolume");
		case 6:
			return tempMap.get("UsedVolume");
		case 7:
			return tempMap.get("FileNumber");
		}
		return null;
	}

	// �����޸ı��ģ���е����ݣ��������У���������ʾ������û��ʵ�֡�
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		return;
	}

	// ����ģ�͵��¼����ڵķ���������Ҳû�б�Ҫʵ�֡�
	public void addTableModelListener(TableModelListener l) {
		return;
	}

	public void removeTableModelListener(TableModelListener l) {
		return;
	}
}