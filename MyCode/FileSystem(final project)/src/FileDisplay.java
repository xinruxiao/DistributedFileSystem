import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class FileDisplay extends JFrame{
	JTabbedPane jTabbedPane = null; // ѡ����
	JScrollPane jscrollPane = null;
	JTable jTable = null; // Swing������

	public FileDisplay(Map<String,Map> map) {
		jTable = new JTable(new MyTableModel2(map)); // ����ģ�ʹ���UI
		jTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12)); // ��������

		jTable.setFillsViewportHeight(true); // �߶Ⱥ͹�������ĸ߶�һ��
		// �����п�
		jTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		// �����и�
		jTable.setRowHeight(22);
		jscrollPane = new JScrollPane(jTable);
		// ���ù�������
		jscrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// ����һ��ѡ����
		jTabbedPane = new JTabbedPane();
		// ���һ��ѡ�������ΪTable
		jTabbedPane.add("�ļ���Ϣ", jscrollPane);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jTabbedPane); // ���ѡ���������
		// ������ʾ���ھ�����Ļ����λ��
		Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((sd.width - 600) / 2, (sd.height - 400) / 2);
		setSize(600, 400);
		setVisible(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
	public void doFlush(Map<String,Map> map) {
		
		jTable.setModel(new MyTableModel2(map));
		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);
	}
}