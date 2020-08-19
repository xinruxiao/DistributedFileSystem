import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.util.*;

public class MyTableModel2 implements TableModel {
	private Map<String,Map> al; // ���ݼ��ص����ݼ�������һ�����ģ��
	private int rowCount = 0; // �����������

	public MyTableModel2(Map<String,Map> al) {
		this.al = al;
		rowCount = al.size();
	}

	public int getRowCount() { // ���ر��ģ�͵ļ�¼����
		return rowCount;
	}

	public int getColumnCount() { // ���ر��ģ�͵�����
		return 5;
	}

	public String getColumnName(int columnIndex) { // ���ر��ģ�͵�����
		switch (columnIndex) {
		case 0:
			return "���";
		case 1:
			return "�ļ�ԭʼ����";
		case 2:
			return "�ļ���С";
		case 3:
			return "���洢�ڵ�";
		case 4:
			return "���ݴ洢�ڵ�";
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
		Set set=FileServer.clientFileMap.keySet();
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Object key=it.next();
			name=(String)key;
			tempMap=FileServer.clientFileMap.get(key);
			if(count==rowIndex) 
				break;
			++count;
		}
		
		switch (columnIndex) { // ���������ظ������ݵ�ĳ���е�ֵ
		case 0:
			return name;
		case 1:
			return tempMap.get("FileName");
		case 2:
			return tempMap.get("FileLength");
		case 3:
			return tempMap.get("StorageNodeOne");
		case 4:
			return tempMap.get("StorageNodeTwo");
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
