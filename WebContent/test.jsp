<%@page import="org.apache.lucene.search.highlight.*"%>
<%@page import="org.apache.lucene.analysis.Analyzer"%>
<%@page import="org.apache.lucene.index.memory.*"%>
<%@page import="org.apache.lucene.queries.*"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page
	import="firstpackage.*,org.nlpir.lucene.cn.ictclas.*,org.nlpir.segment.*,org.nlpir.segment.exception.*,query.*,main_prog.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Search Engine</title>

<script language="javascript">
		var searchInput;
		
		function getQueryString(name)
		{
		     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		     var r = window.location.search.substr(1).match(reg);
		     if(r!=null)return  decodeURI(r[2]); return null;
		}
	    function search()
	    {	 
	    	searchStr = searchInput.value;
	        window.location.href="test.jsp"+"?query="+encodeURI(searchStr) +"&p=0&c=0";
		}
	    window.onload = function()
	    {	
	    	searchInput = document.getElementsByName("SearchInput")[0];
	    	searchInput.value = getQueryString("query");
	    }
	</script>

	<style>
		.dropdown{
			position:relative;
			display:inline-block;
			}
		.dropdown-content{
			display: none;
			position: absolute;
			background-color: #f9f9f9;
			min-width: 160px;
			box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
			padding: 12px 16px;
		}
		.dropdown:hover .dropdown-content {
	 		display: block;
		}
			
		.mainBody {
			height: auto !important;
			height: 100px;
			min-height: 100px
		}
		
		.titlebox {
			margin-left: 120px;
			font-size: 20px
		}
		
		.contentbox {
			margin-left: 120px;
			font-size: 16px;
			width: 40%
		}
		
		.pages {
			margin-left: 120px;
			text-align: center;
			font-size: 18px;
		}
		
		em {
			font-style: normal;
			color: red
		}
	</style>
</head>
<body>

	<div style="margin-left: 30px">		
		<img alt="Gogle" height=46px width=100px src="./Gogle.jpg" />
		<div class="dropdown">
			<input type="text" name="SearchInput" style="height:40px;width:300px;display:inline-block;font-size:22px" />
			<div class="dropdown-content">
			
			<script type="text/javascript">
			var x;
			for (x in history_input){
				document.write("<p>" + history_input[x] + "</p>");
			}
			</script>
			
			</div>
		</div>
		<input type="button" value="Gogle一下"
			style="height: 40px; width: 120px; background-color: #6495ed; text-align: center; display: inline-block; font-size: 22px"
			onclick="search()" />
	</div>
	<!-- simple query is a global var -->
	<%!
		IndexSearch indexSearch = new IndexSearch();
		int resultPerPage = 13;
	%>
	<div class="mainBody">
		<%

		String queryString = request.getParameter("query");
		
		if(queryString!=null)
		{
			String decodeStr = new String(queryString.getBytes("ISO-8859-1"),"utf8");
			indexSearch.Search(decodeStr);//+1 for the upperbound!
			//Those are just for highlighting!!!
			NLPIRTokenizerAnalyzer analyzer = indexSearch.analyzer;
			QueryScorer scorer = new QueryScorer(indexSearch.q);
			SimpleHTMLFormatter formatter=new SimpleHTMLFormatter("<em>", "</em>");
			Highlighter highlighter = new Highlighter(formatter,scorer);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			highlighter.setTextFragmenter(fragmenter);
			int currentPage = Integer.parseInt(request.getParameter("p"));
			for(int i = currentPage * resultPerPage;i<(currentPage+1) * resultPerPage&& i <indexSearch.GetResultNum();i++)
			{
				org.apache.lucene.document.Document result = indexSearch.GetResults(i);
				String docTitle = result.get("title");
				String titleFragment = highlighter.getBestFragment(analyzer, "title", docTitle);
				String docContent = result.get("content").replace(" ","");
				String contentFragment = highlighter.getBestFragment(analyzer, "content", docContent);
				out.println("<div class=\"titlebox\"><p><a href=" + result.get("url") +">" + docTitle+"</a></p></div>");
				out.println("<div class=\"contentbox\"><p>"+ contentFragment + "</p></div>");
			}
		}
	%>
	</div>
	<div class="pages">
			<%
		int pageNum;
		try{
			int return_num = indexSearch.GetResultNum() > 65 ? 65 :indexSearch.GetResultNum();
			pageNum = return_num/13;
		}
		catch(Exception e)
		{
			pageNum = 0;
		}
		int currentPage = (request.getParameter("p") != null)?Integer.parseInt(request.getParameter("p")):0;
		if(request.getParameter("p") != null)
		{
			String requestStr = request.getRequestURL() + "?" +request.getQueryString();
			String[] requestStrArr = requestStr.split("&");
			String newRequestStr = "";
			for(int i = 0;i<requestStrArr.length - 2;i++)
			{
				newRequestStr += requestStrArr[i];
				newRequestStr += "&";
			}
			for(int i = currentPage -5;i<currentPage+5 && i<pageNum;i++)
			{
				if(i >= 0&&i!=currentPage)
				{
					out.println("<div style=\"border-style:dotted;display:inline\" >" + "<a  href=\""+ newRequestStr + "p=" + Integer.toString(i)
						+ "&c=1\">" + "   " +  Integer.toString(i + 1) + "</a>" + "</div>");
				}
				if(i==currentPage)
				{
					out.println("<em>" + Integer.toString(i+1) + "</em>");
				}
			}
		}
	%>
	</div>
</body>
</html>