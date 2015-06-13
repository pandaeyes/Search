package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

public class DownloadThread extends Thread {
	
	private List<String> downList = new ArrayList<String>();
	private Link link = null;
	private String name = null;
	private JButton downBut = null;
	
	public DownloadThread(List<String> downList, Link link, JButton downBut, String name) {
		this.downList = downList;
		this.name = name;
		this.downBut = downBut;
		this.link = link;
	}
	
	public void run() {
		File dir = new File(link.getDate());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		int count = downList.size();
		downBut.setText("下载(" + count + ")");
		downBut.setEnabled(false);
		String requestUrl = "http://www.jandown.com/fetch.php";
		String fileName = "temp/" + link.getDate() + "/" + name + ".torrent";
		Map<String, Object> requestParamsMap = new HashMap<String, Object>();
		requestParamsMap.put("code", name);
		StringBuffer params = new StringBuffer();
		HttpURLConnection httpURLConnection = null;
		PrintWriter printWriter = null;
		InputStream is = null;
		FileOutputStream fos = null;
		
		// 组织请求参数
		Iterator it = requestParamsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry element = (Map.Entry) it.next();
			params.append(element.getKey());
			params.append("=");
			params.append(element.getValue());
			params.append("&");
		}
		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}

		try {
			URL realUrl = new URL(requestUrl);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty(
							"accept",
							"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			httpURLConnection.setRequestProperty("Content-Length",
					String.valueOf(params.length()));
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(params.toString());
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode != 200) {
			} else {
			}
			is = httpURLConnection.getInputStream();
			fos = new FileOutputStream(fileName);
			byte[] buffer = new byte[512];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

			is.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				is.close();
				fos.flush();
				fos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			downList.remove(name);
			count = downList.size();
			downBut.setText("下载(" + count + ")");
		}
	}
}
