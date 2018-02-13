package firstpackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.mockfile.FilterPath;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.nlpir.lucene.cn.ictclas.NLPIRTokenizerAnalyzer;



public class buildIndex {
	String PATH_INDEX = "index";
	private Directory directory;
	public NLPIRTokenizerAnalyzer analyzer;
	private IndexWriter indexWriter ;
	
	public Directory IndexSavingDir(){
		return directory;
	}
	public buildIndex() {
		
		analyzer = new NLPIRTokenizerAnalyzer("", 1, "", "", false);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		try{
			File file = new File(PATH_INDEX);
			directory = FSDirectory.open(FileSystems.getDefault().getPath(".",PATH_INDEX));
			System.out.println(FileSystems.getDefault().getPath(PATH_INDEX));
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		}
		catch(IOException e){
			;
		}
	}
	
	public Boolean IndexWriterClose(){
		try{
			indexWriter.close();
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	public boolean Build(Info info) throws Exception{
		if(indexWriter == null) return false;
		Document document = new Document();
		document.add(new StringField("url", info.url, Store.YES));
		document.add(new StringField("publishid", info.publishid, Store.YES));
		document.add(new StringField("subjectid", info.subjectid, Store.YES));
		document.add(new StringField("description", info.description, Store.YES));
		document.add(new StringField("keywords", info.keywords, Store.YES));
		document.add(new StringField("title", info.title, Store.YES));
		document.add(new TextField("content", info.content,Store.YES));
		indexWriter.addDocument(document);
		return true;
	}
	
}
