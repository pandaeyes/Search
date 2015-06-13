package search;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowPhotoThread extends Thread {
	
	private Link link = null;
	private List<Link> list = null;
	private int index = 0;
	private String jandownRef = null;
	private String dirStr = null;
	
	public ShowPhotoThread(List<Link> list, int row) {
		this.list = list;
		this.link = list.get(row);
		this.index = row;
		dirStr = "search" + File.separator + SearchService.getInstance().getCurrentType().getType() + "_" + link.getIndex() + File.separator;
	}
	
	public void run() {
		System.out.println("==========ShowPhotoThread===run======");
		List<String> photoList = new ArrayList<String>();
//		File dir = new File(dirStr);
//		if (link.getImage() == 1 && dir.exists() && !SearchService.getInstance().getRebuildBox().isSelected()) {
//			photoList = readPhotoFile();
//		} else {
//			List<String> list = new ArrayList<String>();
//			parsePhoto(list);
//			photoList = download(list);
//			link.setImage(1);
//			SearchService.getInstance().setHasNew(true);
//		}
		parsePhoto(photoList);
//		ShowPhotoFrame photoFrame = new ShowPhotoFrame(photoList, link, jandownRef, list, index);
//		photoFrame.setVisible(true);
		SearchService.getInstance().showPhotoFrame(photoList, link, jandownRef, list, index);
	}
	
	public void parsePhoto(List<String> list) {
		String urlStr = SearchService.getInstance().getRoot().trim() + link.getUrl();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestProperty("contentType", "utf-8");  
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			String line = in.readLine();
			String regEx = null;
			Pattern p = null;
			Matcher m = null;
			regEx = "<img src=[\"']?([^\"' ]+)[\"']?";
			while (line != null) {
				if (line.indexOf("<img") != -1) {
					String [] imgArray  = line.split("<img");
					for (String img : imgArray) {
						p = Pattern.compile(regEx);
						m = p.matcher("<img" + img);
						if (m.find()) {
							list.add(m.group(1));
						}
					}
				}
				parseRef(line);
				line = in.readLine();
			}
			in.close();
			in = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<String> download(List<String> list) {
		List<String> photoList = new ArrayList<String>();
		File dir = new File(dirStr);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			String photo = list.get(i);
			doDownload(photoList, photo, dirStr + "photo_" + (i + 1) + "." + getExp(photo));
		}
		return photoList;
	}
	
	private void doDownload(List<String> list, String path, String fileName) {
		try {
			URL url = new URL(path);
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setDoInput(true);
			connect.setRequestMethod("GET");
			FileOutputStream outFile=new FileOutputStream(fileName);
			DataOutputStream out=new DataOutputStream(outFile);
			DataInputStream in = new DataInputStream(connect.getInputStream());
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.close();
			in.close();
			out=null;
			url=null;
			list.add(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getExp(String path) {
		String regEx = "(\\w+)$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(path);
		if (m.find()) {
			return m.group(1);
		} else {
			return "img";
		}
	}
	
	private List<String> readPhotoFile() {
		List<String> list = new ArrayList<String>();
		File dir = new File(dirStr);
		File [] fileList  = dir.listFiles();
		for (File file : fileList) {
			if (file.isFile() && file.getName().startsWith("photo")) {
				list.add(dirStr + file.getName());
			}
		}
		return list;
	}
	
	private void parseRef(String line) {
		if (jandownRef != null || SearchService.getInstance().getCurrentType() != SearchType.XIAZAIQU_BTYAZHOU) return;
		if ((line.indexOf("jandown") != -1 
				|| line.indexOf("mimima") != -1
				|| line.indexOf("xauuk") != -1
			) && line.indexOf("ref=") != -1) {
			String regEx = "ref=(\\w+)";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(line);
			if (m.find()) {
				jandownRef = m.group(1);
			}
		}
	}
}
