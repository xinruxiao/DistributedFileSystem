import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class NodeDisplay extends JFrame{
	JTabbedPane jTabbedPane = null; // 选项卡组件
	JScrollPane jscrollPane = null;
	JTable jTable = null; // Swing表格组件

	public NodeDisplay(Map<String,Map> map) {
		jTable = new JTable(new MyTableModel(map)); // 根据模型创建UI
		jTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12)); // 设置字体

		jTable.setFillsViewportHeight(true); // 高度和滚动窗格的高度一致
		// 设置列宽
		jTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		jTable.getColumnModel().getColumn(3).setPreferredWidth(60);
		jTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		jTable.getColumnModel().getColumn(6).setPreferredWidth(100);
		// 设置行高
		jTable.setRowHeight(22);
		jscrollPane = new JScrollPane(jTable);
		// 设置滚动策略
		jscrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jscrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 创建一个选项卡组件
		jTabbedPane = new JTabbedPane();
		// 添加一个选项卡，名称为Table
		jTabbedPane.add("节点信息", jscrollPane);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jTabbedPane); // 添加选项卡到窗口中
		// 设置显示窗口居于屏幕中央位置
		Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((sd.width - 600) / 2, (sd.height - 400) / 2);
		setSize(600, 400);
		setVisible(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
	public void doFlush(Map<String,Map> map) {
		
		jTable.setModel(new MyTableModel(map));
		jTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		jTable.getColumnModel().getColumn(3).setPreferredWidth(60);
		jTable.getColumnModel().getColumn(5).setPreferredWidth(100);
		jTable.getColumnModel().getColumn(6).setPreferredWidth(100);
	}
}
