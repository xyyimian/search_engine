package firstpackage;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HtmlParser {
	//int docseq;
	public int GetDocNum(Elements docs){
		return docs.size();
	}
	
	
	public Info Parse(Element doc) throws Exception{
			Info docInfo = new Info();
			String url = doc.getElementsByTag("url").get(0).ownText();
			String title = doc.getElementsByTag("title").get(0).ownText();
			String content = doc.ownText();
			//注意publishid有可能为空
			String publishid = "";
			try{
			publishid = doc.getElementsByAttributeValue("name","publishid").get(0).attr("content"); //publishid
			}catch(Exception e){
				;
			}
			String subjectid = "";
			try{
				subjectid = doc.getElementsByAttributeValue("name", "subjectid").get(0).attr("content");
			}catch(Exception e){
				;
			}
			
			//注意keywords有可能为空
			String keywords = "";
			try{
				keywords = doc.getElementsByAttributeValue("name","keywords").get(0).attr("content");
			}catch(Exception e){
				;
			}
			String description = "";
			try{
				description = doc.getElementsByAttributeValue("name", "description").get(0).attr("content");
			}catch(Exception e){
				;
			}
			docInfo.set(url,keywords,publishid,subjectid,description,title,content);
		return docInfo;		
	}
	

}
