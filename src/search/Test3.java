package search;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Test3 {
	public static void main(String[] args) {
		String requestUrl = "http://www.jandown.com/fetch.php";
		String fileName = "AtVBup1T16.torrent";
		Map<String, Object> requestParamsMap = new HashMap<String, Object>();
		requestParamsMap.put("code", "AtVBup1T16");
		// requestParamsMap.put("areaCode1", "中国");
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		// BufferedReader bufferedReader = null;
		StringBuffer params = new StringBuffer();
		HttpURLConnection httpURLConnection = null;
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
			httpURLConnection
					.setRequestProperty(
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
			InputStream is = httpURLConnection.getInputStream();
			FileOutputStream fos = new FileOutputStream(fileName);
			byte[] buffer = new byte[512];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

			is.close();
			fos.flush();
			fos.close();
			// 定义BufferedReader输入流来读取URL的ResponseData
			// bufferedReader = new BufferedReader(new
			// InputStreamReader(httpURLConnection.getInputStream()));
			// File file = new File(fileName);
			// OutputStreamWriter write = new OutputStreamWriter(new
			// FileOutputStream(file));
			// BufferedWriter dataBf =new BufferedWriter(write);
			//
			// String line;
			// while ((line = bufferedReader.readLine()) != null) {
			// System.out.println(line);
			// dataBf.write(line);
			// }
			// dataBf.close();
			// write.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}
}
