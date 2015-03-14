package search;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;

import org.jdesktop.jdic.browser.BrowserEngineManager;
import org.jdesktop.jdic.browser.WebBrowser;

public class Test1 extends JFrame {
    // 链接
    private String index_url = "http://www.baidu.com";
    private JEditorPane jep = new JEditorPane(); 		
 
    /**
     * 
     */
    public Test1() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 控制全屏
        // 添加浏览器支持
        setSize(new Dimension(850, 700));
        getContentPane().setLayout(new BorderLayout());
        JScrollPane jsPane = new JScrollPane(jep);
		jsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(jsPane, BorderLayout.CENTER);
		
		jep.setContentType("text/html"); 
		jep.setEditable(false); 
		jep.setText("<img src='http://img4.cache.netease.com/photo/0001/2015-03-14/AKLL6TEE00AP0001.jpg'><br><br> <img src='http://img169.com/images/2015/01/20/TMHP'><br><br> <img src='http://img169.com/images/2015/01/20/VRTM'><br><br>");
 
    }
    
    public void hyperlinkUpdate(HyperlinkEvent event) 
    { 
        if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
        { 
            try 	
            { 
                jep.setPage(event.getURL()); 
            } 
            catch(IOException ioe) 
            { 
                ioe.printStackTrace(); 
            } 
        }        
    } 
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Test1();
                frame.setVisible(true);
            }
        });
    }
 
}
