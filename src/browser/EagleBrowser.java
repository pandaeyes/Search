package browser;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import search.TestFrame;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class EagleBrowser extends JPanel {

	private JPanel webBrowserPanel;

	private JWebBrowser webBrowser;

	private String url;

	public static void main(String[] args) {
		final String url = "http://www.baidu.com";
		final String title = "电信营业厅缴费终端";
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();

		SwingUtilities.invokeLater(new TestFrame(null, 0));
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				JFrame frame = new JFrame(title);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				frame.getContentPane().add(new EagleBrowser(url),
//						BorderLayout.CENTER);
//				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//				frame.setLocationByPlatform(true);
//				// frame.setUndecorated(true);
//				frame.setVisible(true);
//			}
//		});
		NativeInterface.runEventPump();
	}

}