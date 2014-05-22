package search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import sun.swing.table.DefaultTableCellHeaderRenderer;

public class MainFrame extends JFrame {
	
	private String title = "搜索工具";
	private Dimension dimension = new Dimension(850, 500);
	private JTextField root = new JTextField();
	private JTextField search = new JTextField();
	private JButton runBut = new JButton("运气不错");
	private JLabel rLabel = new JLabel("根目录:");
	private JLabel sLabel = new JLabel("请输入:");
	private JLabel lLabel = new JLabel("请双击:");
	private JRadioButton btyazhouBut = new JRadioButton("BT");
	private JRadioButton zipaiPhotoBut = new JRadioButton("TP");
	private JRadioButton yazhouPhotoBut = new JRadioButton("YZ");
	private JCheckBox rebuildBox = new JCheckBox("RB");
	private JTextField page = new JTextField("15");
	private DataTableModel tableModel = new DataTableModel();
	private JTable table = new JTable(tableModel);
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SearchService.getInstance();
		new MainFrame();
	}

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPane = new JPanel();
		mainPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPane, BorderLayout.CENTER);
		mainPane.setLayout(new BorderLayout());
		JPanel rootPane = new JPanel();
		rootPane.setLayout(new BorderLayout());
		rootPane.add(root, BorderLayout.CENTER);
		rootPane.add(rLabel, BorderLayout.WEST);
		mainPane.add(rootPane, BorderLayout.NORTH);
		JPanel searchPane = new JPanel();
		JPanel resultPane = new JPanel();
		searchPane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		searchPane.setLayout(new BorderLayout());
		resultPane.setLayout(new BorderLayout());
		mainPane.add(resultPane, BorderLayout.CENTER);
		
		JPanel typePane = new JPanel();
		JPanel typegridPane = new JPanel();
		typePane.setLayout(new BorderLayout());
		typegridPane.setLayout(new GridLayout(1, 5, 0, 0));
		ButtonGroup bg = new ButtonGroup();
		btyazhouBut.setSelected(true);
		bg.add(btyazhouBut);
		bg.add(zipaiPhotoBut);
		bg.add(yazhouPhotoBut);
		page.setColumns(3);
		typegridPane.add(btyazhouBut);
		typegridPane.add(zipaiPhotoBut);
		typegridPane.add(yazhouPhotoBut);
		rebuildBox.setToolTipText("重建索引");
		rebuildBox.setSelected(false);
		typegridPane.add(rebuildBox);
		typegridPane.add(page);
		typePane.add(search, BorderLayout.CENTER);
		typePane.add(typegridPane, BorderLayout.EAST);
		
		searchPane.add(typePane, BorderLayout.CENTER);
		searchPane.add(runBut, BorderLayout.EAST);
		searchPane.add(sLabel, BorderLayout.WEST);
		resultPane.add(searchPane, BorderLayout.NORTH);
		runBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				runSearch();
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2) {
					int row =((JTable)e.getSource()).getSelectedRow();
					Link link = tableModel.getList().get(row);
					int column =((JTable)e.getSource()).getSelectedColumn();
					if (column == 3) {
						ShowPhotoThread thread = new ShowPhotoThread(tableModel.getList(), row);
						thread.start();
					} else {
						try {
							java.awt.Desktop.getDesktop().browse(new java.net.URL(root.getText().trim() + link.getUrl()).toURI());
						}catch(Exception ioe){
							ioe.printStackTrace();
						}
					}
				}
			}
		});
		search.addKeyListener(new MyKeyListener());
		page.addKeyListener(new MyKeyListener());
		JPanel listPane = new JPanel();
		JPanel llPane = new JPanel();
		listPane.setLayout(new BorderLayout());
		listPane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		llPane.setLayout(new BorderLayout());
		llPane.add(lLabel, BorderLayout.NORTH);
		listPane.add(llPane, BorderLayout.WEST);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(28);
		table.setShowVerticalLines(false);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(2).setMaxWidth(50);
		table.getColumnModel().getColumn(2).setMinWidth(50);
		table.getColumnModel().getColumn(3).setMaxWidth(50);
		table.getColumnModel().getColumn(3).setMinWidth(50);
		table.getColumnModel().getColumn(3).setCellRenderer(new DataTableCellRenderer());
//		table.getColumnModel().getColumn(4).setCellRenderer(new DataTableCellRenderer());
		JScrollPane jsPane = new JScrollPane(table);
		jsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPane.add(jsPane, BorderLayout.CENTER);
		resultPane.add(listPane, BorderLayout.CENTER);
		root.setText(SearchService.getInstance().getRootUrl());
		SearchService.getInstance().setRebuildBox(rebuildBox);
		setSize(dimension);
		setTitle(title);
		GUIUtils.centerWindow(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SearchService.getInstance().writeCache();
			}
		});
		setVisible(true);
	}
	
	public void runSearch() {
		runBut.setEnabled(false);
		SearchService.getInstance().setRoot(root.getText().trim());
		SearchInfo info = new SearchInfo();
		info.setRoot(root.getText().trim());
		info.setSearchStr(search.getText().trim());
		if (btyazhouBut.isSelected()) {
			info.setType(SearchType.XIAZAIQU_BTYAZHOU);
		} else if (zipaiPhotoBut.isSelected()) {
			info.setType(SearchType.TUPIANQU_ZIPAI);
		} else if (yazhouPhotoBut.isSelected()) {
			info.setType(SearchType.TUPIANQU_YAZHOU);
		}
		if (rebuildBox.isSelected()) {
			info.setRebuild(true);
		} else {
			info.setRebuild(false);
		}
		SearchService.getInstance().setCurrentType(info.getType());
		int p = 2;
		try {
			p = Integer.parseInt(page.getText().trim());
		} catch (Exception e) {
		}
		info.setPage(p);
		tableModel.getList().clear();
		tableModel.fireTableDataChanged();
		SearchThread searchThread = new SearchThread(tableModel, info, runBut);
		searchThread.start();
	}
	
	class MyKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER && runBut.isEnabled()) {
				runSearch();
			}
		}
		public void keyReleased(KeyEvent e) {
		}
	}
}
