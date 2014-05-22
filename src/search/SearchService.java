package search;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;

public class SearchService {
	
	private static SearchService service = null;
	
	private String root = "";
	private SearchType currentType = SearchType.XIAZAIQU_BTYAZHOU;
	private Map<SearchType, List<Link>> linkMap = new HashMap<SearchType, List<Link>>();
	private boolean hasNew = false;
	private JCheckBox rebuildBox = null;
	private ShowPhotoFrame photoFrame = null;
	private String rootUrl = "";
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getRootUrl() {
		return rootUrl;
	}
	public Map<SearchType, List<Link>> getLinkMap() {
		return linkMap;
	}
	public void setLinkMap(Map<SearchType, List<Link>> linkMap) {
		this.linkMap = linkMap;
	}
	public SearchType getCurrentType() {
		return currentType;
	}
	public void setCurrentType(SearchType currentType) {
		this.currentType = currentType;
	}
	public void setHasNew(boolean hasNew) {
		this.hasNew = hasNew;
	}
	public JCheckBox getRebuildBox() {
		return rebuildBox;
	}
	public void setRebuildBox(JCheckBox rebuildBox) {
		this.rebuildBox = rebuildBox;
	}
	private SearchService(){
		for (SearchType type : SearchType.values()) {
			linkMap.put(type, new ArrayList<Link>());
		}
		initCache();
		initRootUrl();
	}
	
	public static SearchService getInstance() {
		if (service == null)
			service = new SearchService();
		return service;
	}
	
	public void search(DataTableModel tableModel, SearchInfo info) {
		List<Link> linkList = linkMap.get(info.getType());
		if (linkList == null) {
			linkList = new ArrayList<Link>();
			linkMap.put(info.getType(), linkList);
		}
		if (info.isRebuild()) {
			searchForBuild(linkList, info);
		}
		if (hasNew) {
			LinkComparator camparator = new LinkComparator();
			Collections.sort(linkList, camparator);
		}
		List<Link> filterList = new ArrayList<Link>();
		int count = 0;
		int total = info.getPage() * 20;
		for (Link link : linkList) {
			if ( isNull(info.getSearchStr()) || link.getTitle().indexOf(info.getSearchStr()) != -1) {
				filterList.add(link);
				count++;
			}
			if (count >= total)
				break;
		}
		tableModel.getList().clear();
		tableModel.getList().addAll(filterList);
//		tableModel.setList(filterList);
	}

	public void searchForBuild(List<Link> linkList, SearchInfo info) {
		String url = info.getRoot() + info.getType().getPath();
		for (int i = 1; i <= info.getPage(); i++) {
			String url2 = null;
			if (i == 1) {
				url2 = url + "index.html";
			} else {
				url2 = url + "index_" + i + ".html";
			}
			appendLink(linkList, info, url2);
		}
	}
	
	public void appendLink(List<Link> list, SearchInfo info, String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestProperty("contentType", "utf-8");  
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream(), "UTF-8"));
			String line = in.readLine();
			String regEx = null;
			Pattern p = null;
			Matcher m = null;
			while (line != null) {
				if (line.startsWith("<li><a")) {
					regEx = "<li><a href=\"(.+/(\\d+)\\.\\w+)\" title=\"(.+)\" target=\"_blank\"><span>(.+)</span>(.+)</a> </li>";
					p = Pattern.compile(regEx);
					m = p.matcher(line);
					if (m.find()) {
						Link link = new Link();
						link.setUrl(m.group(1));
						link.setIndex(Integer.parseInt(m.group(2)));
						link.setTitle(m.group(3));
						link.setDate(m.group(4));
						if (!list.contains(link)) {
							hasNew = true;
							list.add(link);
						}
					}
				}
				line = in.readLine();
			}
			in.close();
			in = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeCache() {
		try {
			if (hasNew) {
				LinkComparator camparator = new LinkComparator();
				File file = new File("search.data");
				OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
				BufferedWriter dataBf =new BufferedWriter(write);
				for (SearchType type : linkMap.keySet()) {
					List<Link> linkList = linkMap.get(type);
					Collections.sort(linkList, camparator);
					for (Link link : linkList) {
						dataBf.write(type.getType()
								+ " " + link.getIndex()
								+ " " + URLEncoder.encode(link.getDate(), "UTF-8") 
								+ " " + URLEncoder.encode(link.getUrl(), "UTF-8")
								+ " " + URLEncoder.encode(link.getTitle(), "UTF-8")
								+ " " + link.getImage()
								+ " " + link.getSeed()
								+ "\r\n");
					}
				}
				dataBf.close();
				write.close();
			}
		} catch (Exception e) {
		}
	}
	
	public void initCache() {
		try {
			File file = new File("search.data");
			if (file.isFile()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(reader);
				String line = null;
				String [] data = null;
				SearchType [] types = SearchType.values();
				while ((line = br.readLine()) != null){
					if (line.trim().length() > 0) {
						data = line.split(" ");
						if (data.length == 7) {
							Link link = new Link();
							link.setIndex(Integer.parseInt(data[1]));
							link.setDate(URLDecoder.decode(data[2], "UTF-8"));
							link.setUrl(URLDecoder.decode(data[3], "UTF-8"));
							link.setTitle(URLDecoder.decode(data[4], "UTF-8"));
							for (SearchType type : types) {
								if (type.getType().equals(URLDecoder.decode(data[0], "UTF-8"))) {
									linkMap.get(type).add(link);
								}
							}
							link.setImage(Integer.parseInt(data[5]));
							link.setSeed(Integer.parseInt(data[6]));
						}
					}
	            }
				br.close();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initRootUrl() {
		try {
			InputStream is = new BufferedInputStream(new FileInputStream("root.properties"));
			Properties p = new Properties();
			p.load(is);
			rootUrl = p.getProperty("root.url");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPhotoFrame(List<String> list, Link link, String jandownRef, List<Link> linkList, int index) {
		if (photoFrame == null) {
			photoFrame = new ShowPhotoFrame(list, link, jandownRef, linkList, index);
			photoFrame.setVisible(true);
		} else {
			photoFrame.fireDataChange(list, link, jandownRef, linkList, index);
			photoFrame.setVisible(true);
		}
	}
	
	private boolean isNull(String str) {
		if (str == null) return true;
		if (str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
}

class LinkComparator implements Comparator {
	
	public LinkComparator(){
	}
	public int compare(Object obj1, Object obj2) {
		if (obj1 == null || obj2 == null)
			return obj1 != null || obj2 != null ? obj1 != null ? 1 : -1 : 0;
		if ((obj1 instanceof Link) && (obj2 instanceof Link)) {
			int idx1 = ((Link)obj1).getIndex();
			int idx2 = ((Link)obj2).getIndex();
			if (idx1 > idx2) return -1;
			if (idx1 == idx2)
				return 0;
			else
				return 1;
		} else {
			return obj1.toString().compareTo(obj2.toString());
		}
	}
	
}
