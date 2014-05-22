package search;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.jdesktop.jdic.browser.BrowserEngineManager;
import org.jdesktop.jdic.browser.WebBrowser;
import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListenerAdapter;

public class ShowPhotoFrame extends BaseFrame {
	
	private WebBrowser browser = null;
	private List<Link> linkList = null;
	private int index = 0;
	private JButton openBut = new JButton(" 打开 ");
	private JButton preBut = new JButton("前一页");
	private JButton nextBut = new JButton("下一页");
	private JTextField browserUrl = new JTextField();
	private long time = System.currentTimeMillis();
	
	public ShowPhotoFrame(List<String> list, Link link, String jandownRef, List<Link> linkList, int index) {
		super();
		this.linkList = linkList;
		this.index = index;
		BrowserEngineManager bem = BrowserEngineManager.instance();
		bem.setActiveEngine(BrowserEngineManager.IE);
		initComponents(list, link, jandownRef);
	}

	public void initComponents(List<String> list, Link link, String jandownRef) {
		super.initComponents();
		JPanel defaultPane = (JPanel) getContentPane();
		defaultPane.setLayout(new BorderLayout(2, 2));
		JPanel navPanel = new JPanel();
		navPanel.setLayout(new BorderLayout(15, 5));
		JPanel navGPanel = new JPanel();
//		navGPanel.setLayout(new GridLayout(1, 4, 5, 5));
		navGPanel.setLayout(new BoxLayout(navGPanel, BoxLayout.X_AXIS));
		openBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					browser.setURL(getUrl());
//					browser.setURL(new java.net.URL(SearchService.getInstance().getRoot().trim() + linkList.get(index).getUrl()));
//					java.awt.Desktop.getDesktop().browse(new java.net.URL(SearchService.getInstance().getRoot().trim() + linkList.get(index).getUrl()).toURI());
				}catch(Exception ioe){
					ioe.printStackTrace();
				}
			}
		});
		preBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				browserUrl.setText("");
				page("previous");
			}
		});
		nextBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				browserUrl.setText("");
				page("next");
			}
		});
		changeState();
		navGPanel.add(openBut);
		navGPanel.add(new Label());
		navGPanel.add(preBut);
		navGPanel.add(nextBut);
//		navGPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 1));
		navPanel.add(navGPanel, BorderLayout.EAST);
		JPanel urlPanel = new JPanel();
		urlPanel.setLayout(new BorderLayout());
		urlPanel.add(browserUrl, BorderLayout.CENTER);
		navPanel.add(urlPanel, BorderLayout.CENTER);
		browser = new WebBrowser();
		browser.setContent(buildFile(list, jandownRef));
		JPanel browserPanel = new JPanel();
		browserPanel.setLayout(new BorderLayout());
		browserPanel.add(browser, BorderLayout.CENTER);
		defaultPane.add(browserPanel, BorderLayout.CENTER);
		defaultPane.add(navPanel, BorderLayout.NORTH);
//		browser.addKeyListener(new KeyListener() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//			}
//			
//			@Override
//			public void keyReleased(KeyEvent evt) {
//				int code = evt.getKeyCode();
//				System.out.println("=======>" + code);
//				long now = 0;
//				switch (code) {
//					case KeyEvent.VK_LEFT : 
//						now = System.currentTimeMillis();
//						if ((now - time) > 800) {
//							page("previous");
//							time = now;
//						}
//						break;
//					case KeyEvent.VK_RIGHT : 
//						now = System.currentTimeMillis();
//						if ((now - time) > 800) {
//							page("next");
//							time = now;
//						}
//						break;
//					default:
//						break;
//				}
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e) {
//			}
//		});
		browser.addWebBrowserListener(new WebBrowserListen());
		InputMap inputMapAncestor = defaultPane.getInputMap();
		ActionMap actionMap = defaultPane.getActionMap();
		inputMapAncestor.put(KeyStroke.getKeyStroke(10, 0), "enter");
		inputMapAncestor.put(KeyStroke.getKeyStroke(37, 0), "left");
		inputMapAncestor.put(KeyStroke.getKeyStroke(39, 0), "right");
		actionMap.put("left", new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				browserUrl.setText("");
				long now = System.currentTimeMillis();
				if ((now - time) > 500) {
					page("previous");
					time = now;
				}
			}
		});
		actionMap.put("right", new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				browserUrl.setText("");
				long now = System.currentTimeMillis();
				if ((now - time) > 500) {
					page("next");
					time = now;
				}
			}
		});
		actionMap.put("enter", new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				try {
					browser.setURL(getUrl());
				}catch(Exception ioe){
					ioe.printStackTrace();
				}
			}
		});
		setTitle("图片[" + link.getIndex() + "][" + link.getDate() + "] " + link.getTitle());
		setSize(1000, 650);
		GUIUtils.centerWindow(this);
	}
	
	public String buildFile(List<String> list, String jandownRef) {
		StringBuffer confBf = new StringBuffer("");
		confBf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n");
		confBf.append("<HTML>\r\n");
		confBf.append("<HEAD>\r\n");
		confBf.append("<TITLE> Photo </TITLE>\r\n");
		confBf.append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
		confBf.append("</HEAD>\r\n");
		confBf.append("<BODY>\r\n");
		for (String url : list) {
			confBf.append("<img src='" + url + "'><br><br>\r\n");
		}
		if (jandownRef != null) {
			confBf.append("<form method=POST action='http://www.jandown.com/fetch.php' enctype=multipart/form-data>");
			confBf.append("<input type=text name=code size=30 value=" + jandownRef + "> &nbsp;");
			confBf.append("<input type=\"submit\" height=27 width=174 border=0 valign=\"bottom\" value=\"点击下载\">");
			confBf.append("</form>");
		}
		confBf.append("</BODY>");
		confBf.append("</HTML>\r\n");
		String str = null;
		try {
			str = new String(confBf.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			str = confBf.toString();
		}
		return str;
	}
	
	public void fireDataChange(List<String> list, Link link, String jandownRef, List<Link> linkList, int index) {
		this.linkList = linkList;
		this.index = index;
		changeState();
		browser.setContent(buildFile(list, jandownRef));
		setTitle("图片[" + link.getIndex() + "][" + link.getDate() + "] " + link.getTitle());
	}
	
	public void dialogExit() {
		setTitle("");
		setVisible(false);
	}
	
	private void page(String type) {
		if ("previous".equals(type)) {
			if (index > 0) {
				ShowPhotoThread thread = new ShowPhotoThread(linkList, index - 1);
				thread.start();
			}
		} else {
			if ((index + 1) < linkList.size()) {
				ShowPhotoThread thread = new ShowPhotoThread(linkList, index + 1);
				thread.start();
			}
		}
	}
	
	public java.net.URL getUrl() throws Exception{
		if (browserUrl.getText() != null && browserUrl.getText().trim().length() > 0) {
			URL nrl = new java.net.URL(browserUrl.getText().trim());
			return nrl;
		} else {
			URL nrl = new java.net.URL(SearchService.getInstance().getRoot().trim() + linkList.get(index).getUrl());
			return nrl;
		}
	}
	private void changeState() {
		if ((index + 1) == linkList.size()) {
			nextBut.setEnabled(false);
		} else {
			nextBut.setEnabled(true);
		}
		if (index == 0) {
			preBut.setEnabled(false);
		} else {
			preBut.setEnabled(true);
		}
	}
	
	class WebBrowserListen extends WebBrowserListenerAdapter {
		public void statusTextChange(WebBrowserEvent paramWebBrowserEvent) {
			ShowPhotoFrame.this.getCenterPane().requestFocus();
		}
	}
}
