package search;

public class SearchInfo {
	
	private String root = "";
	private String searchStr = "";
	private SearchType type = SearchType.XIAZAIQU_BTYAZHOU;
	private boolean rebuild = false;
	private int page = 2;
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getSearchStr() {
		return searchStr;
	}
	public void setSearchStr(String searchStr) {
		this.searchStr = searchStr;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public SearchType getType() {
		return type;
	}
	public void setType(SearchType type) {
		this.type = type;
	}
	public boolean isRebuild() {
		return rebuild;
	}
	public void setRebuild(boolean rebuild) {
		this.rebuild = rebuild;
	}
}
