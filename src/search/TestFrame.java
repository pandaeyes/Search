package search;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import browser.EagleBrowser;

public class TestFrame extends Thread {
	private Link link = null;
	private List<Link> list = null;
	private int index = 0;
	private String jandownRef = null;
	private String dirStr = null;
	String url = "http://www.163.com";
	
	public TestFrame(List<Link> list, int row) {
		this.list = list;
		this.index = row;
	}
	
	public void run() {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		JPanel content = new JPanel();
		frame.getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new BorderLayout());
		content.add(getPanel(), BorderLayout.CENTER);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationByPlatform(true);
		// frame.setUndecorated(true);
		frame.setVisible(true);
	}
	
	public JPanel getPanel() {
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		JPanel webBrowserPanel = new JPanel(new BorderLayout());
		JWebBrowser webBrowser = new JWebBrowser();
		webBrowser.navigate(url);
//		webBrowser.setHTMLContent("");
		webBrowser.setButtonBarVisible(false);
		webBrowser.setMenuBarVisible(false);
		webBrowser.setBarsVisible(false);
		webBrowser.setStatusBarVisible(false);
		webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
		content.add(webBrowserPanel, BorderLayout.CENTER);
		return content;
	}

}
