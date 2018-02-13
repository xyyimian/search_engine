package firstpackage;

import java.io.File;

public class Info {
	String url;
	String publishid;
	String subjectid;
	String description;
	String title;
	String keywords;
	String content;
	
	public Info set(String _url,String _keywords,String _publishid,String _subjectid,String _description, String _title,String _content){
		url = _url;
		publishid = _publishid;
		title = _title;
		keywords = _keywords;
		content = _content;
		subjectid = _subjectid;
		description = _description;
		return this;
	}
}

