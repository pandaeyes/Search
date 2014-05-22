package search;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class GUIUtils {

	public GUIUtils() {
	}

	public static void centerWindow(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension winSize = win.getSize();
		if (winSize.height > screenSize.height)
			winSize.height = screenSize.height;
		if (winSize.width > screenSize.width)
			winSize.width = screenSize.width;
		win.setLocation((screenSize.width - winSize.width) / 2,
				(screenSize.height - winSize.height) / 2 - 20);
	}
	
	public static long getUnixTime() {
		return System.currentTimeMillis()/1000;
	}
	
	public static void main(String [] args) {
		System.out.println(getUnixTime());
		
	}
}
