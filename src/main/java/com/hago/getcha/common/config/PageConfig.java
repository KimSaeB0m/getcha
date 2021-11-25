package com.hago.getcha.common.config;

public class PageConfig {
	public static String getNavi(int currentPage, int pageBlock, int totalCount, String url) {
		int blockCnt = totalCount / pageBlock; //총페이지 수
		if(totalCount % pageBlock > 0)
			blockCnt++;
		String result="";
		if(currentPage != 1)
			result +="<a href='"+url+(currentPage-1)+"'><&nbsp;</a>";
		
		for(int i = 1; i <= blockCnt; i++) {
		if(currentPage == i) result += "<b>";
		result += " <a href='"+url+i+"'>"+ i +"</a>";
		if(currentPage == i) result += "</b>";
		}

		if(currentPage!=blockCnt)
			result += "<a href='"+url+(currentPage+1)+"'>&nbsp;></a>";
		return result;
	}
}
