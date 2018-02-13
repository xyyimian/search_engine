package main_prog;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;

import query.IndexSearch;


public class TestSearch {
	public static void main(String[] args)throws Exception
	{
		IndexSearch indexSearch = new IndexSearch();
		
		indexSearch.Search("朝鲜");
		int num = indexSearch.GetResultNum();
		System.out.println(num);
		for(int i = 0; i < (num > 20 ? 20 : num); ++i){
			Document document = indexSearch.GetResults(i);
			System.out.println(document.get("title"));
			System.out.println(document.get("url"));
		}
	}
}
