package search;

public enum SearchType {
	XIAZAIQU_BTYAZHOU("/xiazaiqu/btyazhou/", "1"),
	TUPIANQU_ZIPAI("/tupianqu/zipai/", "2"),
	TUPIANQU_YAZHOU("/tupianqu/yazhou/", "3");
	
	private final String path;
	private final String type;
	
	private SearchType(String path, String type) {
		this.path = path;
		this.type = type;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getType() {
		return type;
	}
	
	public String toString() {
		return this.name();
	}
}
