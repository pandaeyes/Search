package search;

public class Link {

	private String url = "";
	private String title = "";
	private String date = "";
	private int index = 0;
	private int image = 0; //0 未读 1 已读
	private int seed = 0; //0 未读 1 已读
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public int getSeed() {
		return seed;
	}
	public void setSeed(int seed) {
		this.seed = seed;
	}
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Link) {
			return ((Link)obj).getUrl().equals(url);
		} else {
			return false;
		}
	}
}
