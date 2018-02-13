package main_prog;
import java.io.File;

import javax.print.attribute.DocAttributeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import firstpackage.*;

public class main_prog {

	public static void IndexBuilding() throws Exception{
		HtmlParser htmlParser = new HtmlParser();
		buildIndex BuildIndex = new buildIndex();
		String[] materials = {"material/2012.q1.txt","mat/2012.q2.txt","mat/2012.q3.txt","mat/2012.q4.txt","mat/2013.q1.txt"};
		System.out.println("IndexBuilding...\n");
		for(int i = 0; i < 5; ++i){			
			System.out.println("Processing  "+materials[i]);
			File newsfile = new File(materials[i]);
			Document document = Jsoup.parse(newsfile,"utf-8");
			Elements docs = document.getElementsByTag("doc");
			int counter = 0;
			float whole_num = htmlParser.GetDocNum(docs);
			for(Element doc:docs){
				counter+=1;
				Info docinfo = htmlParser.Parse(doc);
				BuildIndex.Build(docinfo);
				if(counter % 100 == 0)
					System.out.println("[%" + counter / whole_num + "]");
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		IndexBuilding();
	}

	
	
}
