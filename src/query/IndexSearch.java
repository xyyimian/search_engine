package query;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

import javax.naming.directory.SearchResult;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.nlpir.lucene.cn.ictclas.NLPIRTokenizerAnalyzer;


import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;

public class IndexSearch {
	public NLPIRTokenizerAnalyzer analyzer = new NLPIRTokenizerAnalyzer(".", 1, "", "", false);
	IndexReader reader;
	IndexSearcher searcher;
	MultiFieldQueryParser mp;
	TopDocs results;
	public Query q;
	Sort dateSortNewer;
	Sort dateSortOlder;
	TopDocs topDocs;
	

	public IndexSearch() {
		try{
		Directory directory = FSDirectory.open(FileSystems.getDefault().getPath("E:\\program\\java\\TomcatTest","/index/"));
		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
		}
		catch(IOException e){
			System.out.println(System.getProperty("user.dir"));
			System.out.println("cannot open index file!sb");
			searcher = null;
		}
		NLPIRTokenizerAnalyzer analyzer = new NLPIRTokenizerAnalyzer("", 1, "", "", false);
		String[] fields = {"title", "description","keywords","content"};
		mp = new MultiFieldQueryParser(fields, analyzer);
	}
	public TopDocs Search(String queryStr)throws Exception{
		q = mp.parse(queryStr);
		topDocs = searcher.search(q, 65);
		return topDocs;
	}
	
	public int GetResultNum(){
		return topDocs.totalHits;
	}
	
	public Document GetResults(int i){
		try{
			return searcher.doc(topDocs.scoreDocs[i].doc);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/*
	public int GetResultCount()
	{
		return results.totalHits;
	}
	
	public Document GetResults(int i)
	{
		try 
		{
			return searcher.doc(results.scoreDocs[i].doc);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean SetQueryFields(String[] Fields,Float[] Boosts)
	{
		if(Fields.length != Boosts.length){
			return false;
		}
		return true;
	}
	
	private Query BuildMultiFieldQuery(String[] Fields,String[] Keys,float Boosts[],boolean[] IsOccur,
			int dateLowerRange,int dateUpperRange)
	{
		Query query;
		if(Fields.length != Keys.length||IsOccur.length!=Fields.length||
				Boosts.length!=Fields.length||dateLowerRange>dateUpperRange)
		{
			return null;
		}
		try {
			BooleanQuery.Builder combinedQueryBuilder = new BooleanQuery.Builder();
			for(int i = 0;i<Fields.length;i++)
			{
				QueryParser newQueryParser = new QueryParser(Fields[i], analyzer);
				Query newQuery = newQueryParser.parse(Keys[i]);
				BoostQuery newBoostQuery = new BoostQuery(newQuery, Boosts[i]);
				if(IsOccur[i])
				{
					combinedQueryBuilder.add(newBoostQuery, Occur.MUST);
					
				}
				else
				{
					combinedQueryBuilder.add(newBoostQuery,Occur.MUST_NOT);
				}
			}
			combinedQueryBuilder.add(NumericDocValuesField.newRangeQuery("date",dateLowerRange,dateUpperRange), Occur.MUST);
//			combinedQueryBuilder.add(NumericDocValuesField.newRangeQuery("month", dateLowerRange[1], dateUpperRange[1]), Occur.MUST);
//			combinedQueryBuilder.add(NumericDocValuesField.newRangeQuery("day", dateLowerRange[2], dateUpperRange[2]), Occur.MUST);
			query = combinedQueryBuilder.build();
		}
		catch(ParseException e)
		{
			return null;
		}
		return query;
	}
	
	
	private Query BuildMultiFieldQuery(String[] Fields,String[] Keys,float Boosts[],boolean[] IsOccur)
	{
		return BuildMultiFieldQuery(Fields, Keys, Boosts, IsOccur, 
				Integer.MIN_VALUE,Integer.MAX_VALUE
		);
	}
	public boolean Search(String[] Fields,String[] Keys,float Boosts[],boolean[] IsOccur)
	{
		Query query = BuildMultiFieldQuery(Fields, Keys, Boosts, IsOccur,dateLowerRange,dateUpperRange);
		
		if(query == null)
		{
			return false;
		}
		q = query;
		try {
			if(dateMode == 0)
			{
				results = searcher.search(query, 1000);
			}
			else if(dateMode == 1)
			{
				results = searcher.search(query, 1000, dateSortNewer);
			}
			else if(dateMode == 2)
			{
				results = searcher.search(query, 1000, dateSortOlder);
			}
			else 
			{
				return false;
			}
		}
		catch (IOException e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	*/
	

	
	
	/**
     * 精准匹配
     */
/*
    public void TermSearch(String searchField,String field){
        // 得到读取索引文件的路径
        try {
            Term t = new Term(searchField, field);
            Query q = new TermQuery(t);
            // 获得查询的hits
            TopDocs hits = searcher.search(q, 10);
            // 显示结果
            System.out.println("匹配 '" + q + "'，总共查询到" + hits.totalHits + "个文档");
            for (ScoreDoc scoreDoc : hits.scoreDocs){
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("id:"+doc.get("id")+":"+doc.get("name")+",email:"+doc.get("email"));
            }
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    */
    /*
    public static void main(String[] args) throws Exception{
		//TermSearch("keywords","今年成全球大选年 美俄法等国成焦点,大选");
    	System.out.println(Search("1,1,23732929"));
	
    
    }
	*/
    
    

}
