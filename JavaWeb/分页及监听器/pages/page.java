import java.util.List;
/**
 * 分页实例
 *
 */
public class Page {
	private int pageSize = 10; //每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	private List<?> result ;//存放查询的结果集
	private String formName = ""; //查询条件在页面表单中的位置
	
	public Page() {
	}
	public Page(int pageSize) {
		this.pageSize = pageSize;
	}
	public Page(int pageSize,String formName) {
		this.pageSize = pageSize;
		this.formName = formName;
	}
	public Page(String formName) {
		this.formName = formName;
	}
	public int getTotalPage() {
		if(totalResult%pageSize==0)
			totalPage = totalResult/pageSize;
		else
			totalPage = totalResult/pageSize+1;
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	public int getCurrentPage() {
		if(currentPage<=0)
			currentPage = 1;
		if(currentPage>getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getPageStr() {
		StringBuffer sb = new StringBuffer();
		if(totalResult>0){
			sb.append("<div class='pages_bar'>\n");
			if(currentPage==1){
				sb.append("	<a href='javascript:void(0);'>首页</a>\n");
				sb.append("	<a href='javascript:void(0);'>上页</a>\n");
			}else{	
				sb.append("	<a onclick=\"nextPage(1);return false;\" href=\"javascript:void(0);\">首页</a>\n");
				sb.append("	<a onclick=\"nextPage("+(currentPage-1)+");return false;\" href=\"javascript:void(0);\">上页</a>\n");
			}
			int showTag = 5;	//分页标签显示数量
			int startTag = 1;
			if(currentPage>showTag){
				startTag = currentPage-1;
			}
			int endTag = startTag+showTag-1;
			for(int i=startTag; i<=totalPage && i<=endTag; i++){
				if(currentPage==i)
					sb.append("<a href='javascript:void(0);' class='current_page'>"+i+"</a>\n");
				else
					sb.append("	<a onclick=\"nextPage("+i+");return false;\" href=\"javascript:void(0);\">"+i+"</a>\n");
			}
			if(currentPage==totalPage){
				sb.append("	<a href='javascript:void(0);'>下页</a>\n");
				sb.append("	<a href='javascript:void(0);'>尾页</a>\n");
			}else{
				sb.append("	<a onclick=\"nextPage("+(currentPage+1)+");return false;\" href=\"javascript:void(0);\">下页</a>\n");
				sb.append("	<a onclick=\"nextPage("+totalPage+");return false;\" href=\"javascript:void(0);\">尾页</a>\n");
			}
			sb.append("	<span style='border:0px;padding:0px;'>到第<input id='txtPage' type='text' size='3'/>页</span>\n");
			sb.append("	<label class='btn_gray_s' style='margin:0px;margin-bottom:4px;'><input type='button' value='GO' onclick='nextPage(0);return false;'/></label>\n");
			sb.append("	<span>第"+currentPage+"页</span>\n");
			sb.append("	<span>共"+totalPage+"页</span>\n");
			sb.append("	<span>每页"+pageSize+"条记录,共"+totalResult+"条</span>\n");
			sb.append("</div>\n");
			
			
			sb.append("<script type=\"text/javascript\">\n");
			sb.append("function nextPage(page){\n");
			if(StringUitl.IsNotNull(formName)){//存在form表单
				// 添加输入页号跳转的数字验证
				sb.append("	if(page == 0){\n");
				sb.append("		page = jQuery.trim(document.getElementById('txtPage').value);\n");
				sb.append("		if (page > 0 && page <= " + totalPage + " && page.indexOf('.') < 0) {\n");
				sb.append("		} else {\n");
				sb.append("			realAlert('请输入正确的页号');\n");
				sb.append("			return false;\n");
				sb.append("		}\n");
				sb.append("		if (page == " + currentPage + " || page == '') {\n");
				sb.append("			realAlert('请输入正确的页号');\n");
				sb.append("			return false;\n");
				sb.append("		}\n");
				sb.append("	}\n");
				sb.append("	if(true && document.forms['").append(formName).append("']){\n");
				sb.append("		var url = document.forms['").append(formName).append("'].getAttribute(\"action\");\n");
				sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		document.forms['").append(formName).append("'].action = url+page+\"&pageChangeFlag=1\";\n");
				sb.append("		document.forms['").append(formName).append("'].submit();\n");
				sb.append("	}else{\n");
				sb.append("		var url = document.location+'';\n");
				sb.append("		if(url.indexOf('?')>-1){\n");
				sb.append("			if(url.indexOf('currentPage')>-1){\n");
				sb.append("				var reg = /currentPage=\\d*/g;\n");
				sb.append("				url = url.replace(reg,'currentPage=');\n");
				sb.append("			}else{\n");
				sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
				sb.append("			}\n");
				sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		document.location = url + page + \"&pageChangeFlag=1\";\n");
				sb.append("	}\n");
			}else{
				sb.append("		if(page == 0){\n");
				sb.append("			page = trim(document.getElementById('txtPage').value);\n");
				sb.append("			if (page > 0 && page <= " + totalPage + " && page.indexOf('.') < 0) {\n");
				sb.append("			} else {\n");
				sb.append("				realAlert('请输入正确的页号');\n");
				sb.append("				return false;\n");
				sb.append("			}\n");
				sb.append("			if (page == " + currentPage + " || page == '') {\n");
				sb.append("				realAlert('请输入正确的页号');\n");
				sb.append("				return false;\n");
				sb.append("			}\n");
				sb.append("		}\n");
				
				sb.append("		var url = document.location+'';\n");
				sb.append("		if(url.indexOf('?')>-1){\n");
				sb.append("			if(url.indexOf('currentPage')>-1){\n");
				sb.append("				var reg = /currentPage=\\d*/g;\n");
				sb.append("				url = url.replace(reg,'currentPage=');\n");
				sb.append("			}else{\n");
				sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
				sb.append("			}\n");
				sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
				sb.append("		document.location = url + page;\n");
			}
			sb.append("}\n");
			sb.append("</script>\n");
		}
		pageStr = sb.toString();
		return pageStr;
	}
	/**
	 * 获取简单版的分页组件，无form，非url提交； 包括 首页  上页 5 下页 尾页 共15页 每页....
	 */
	public String getSimplePageStr(String nextPageMethod) {
		StringBuffer sb = new StringBuffer();
		if(totalResult>0){
			sb.append("<div class='simple_pages_bar'>\n");
			if(currentPage==1){
				sb.append("	<a href='javascript:void(0);'>上一页</a>\n");
			}else{	
				sb.append("	<a onclick=\"" + nextPageMethod +"("+(currentPage-1)+");return false;\" href=\"javascript:void(0);\">上一页</a>\n");
			}
			int startTag = currentPage;
			//int showTag = 5;	//分页标签显示数量
			//int startTag = 1;
			//if(currentPage>showTag){
			//	startTag = currentPage-1;
			//}
			//int endTag = startTag+showTag-1;
			sb.append("<span>第"+currentPage+"页</span>\n");
			sb.append("<span>共"+totalPage+"页</span>\n");
			if(currentPage==totalPage){
				sb.append("	<a href='javascript:void(0);'>下一页</a>\n");
			}else{
				sb.append("	<a onclick=\"" + nextPageMethod +"("+(currentPage+1)+");return false;\" href=\"javascript:void(0);\">下一页</a>\n");
			}
			sb.append("<input type=\"hidden\" id=\"page_total_page_size\" value=\"" + totalPage + "\">");
			sb.append("<input type=\"hidden\" id=\"page_current_page_size\" value=\"" + currentPage + "\">");
			//sb.append("	<span style='border:0px;padding:0px;'>到第<input id='txtPage' type='text' size='3'/>页</span>\n");
			//sb.append("	<label class='btn_gray_s' style='margin:0px;margin-bottom:4px;'><input type='button' value='GO' onclick='nextPage(0);return false;'/></label>\n");
			//sb.append("	<span>第"+currentPage+"页</span>\n");
			sb.append("</div>\n");
			
		}
		pageStr = sb.toString();
		return pageStr;
	}
	
	/**
	 * 获取简单版的分页组件，无form，非url提交； 包括 首页  上页 5 下页 尾页 共15页 每页....
	 * type 判断是否显示 分页标签
	 */
	public String getAjaxPageStr(String nextPageMethod,String type) {
		StringBuffer sb = new StringBuffer();
		if(totalResult>0){
			sb.append("<div class='pages_bar'>\n");
			if(currentPage==1){
				sb.append("	<a href='javascript:void(0);'>首页</a>\n");
				sb.append("	<a href='javascript:void(0);'>上页</a>\n");
			}else{	
				if("depotSearch".equals(type)){
					sb.append("	<a onclick=\""+nextPageMethod+",1);return false;\" href=\"javascript:void(0);\">首页</a>\n");
					sb.append("	<a onclick=\""+nextPageMethod+","+(currentPage-1)+");return false;\" href=\"javascript:void(0);\">上页</a>\n");
				}else{
					sb.append("	<a onclick=\""+nextPageMethod+"(1);return false;\" href=\"javascript:void(0);\">首页</a>\n");
					sb.append("	<a onclick=\""+nextPageMethod+"("+(currentPage-1)+");return false;\" href=\"javascript:void(0);\">上页</a>\n");
				}
			}
			if("have".equals(type)){
				int showTag = 5;	//分页标签显示数量
				int startTag = 1;
				if(currentPage>showTag){
					startTag = currentPage-1;
				}
				int endTag = startTag+showTag-1;
				for(int i=startTag; i<=totalPage && i<=endTag; i++){
					if(currentPage==i)
						sb.append("<a href='javascript:void(0);' class='current_page'>"+i+"</a>\n");
					else
						sb.append("	<a onclick=\""+nextPageMethod+"("+i+");return false;\" href=\"javascript:void(0);\">"+i+"</a>\n");
				}
			}
			if(currentPage==totalPage){
				sb.append("	<a href='javascript:void(0);'>下页</a>\n");
				sb.append("	<a href='javascript:void(0);'>尾页</a>\n");
			}else{
				if("depotSearch".equals(type)){
					sb.append("	<a onclick=\""+nextPageMethod+","+(currentPage+1)+");return false;\" href=\"javascript:void(0);\">下页</a>\n");
					sb.append("	<a onclick=\""+nextPageMethod+","+totalPage+");return false;\" href=\"javascript:void(0);\">尾页</a>\n");
					sb.append("	<span style='border:0px;padding:0px;'>到第<input id='txtPagesearchDepotId' type='text' size='3'/>页</span>\n");
					sb.append("	<label class='btn_gray_s' id = 'gotoNum' style='margin:0px;margin-bottom:4px;'><input type='button' value='GO' onclick='"+nextPageMethod+",this.value);return false;'/></label>\n");
				}else{
					sb.append("	<a onclick=\""+nextPageMethod+"("+(currentPage+1)+");return false;\" href=\"javascript:void(0);\">下页</a>\n");
					sb.append("	<a onclick=\""+nextPageMethod+"("+totalPage+");return false;\" href=\"javascript:void(0);\">尾页</a>\n");
					sb.append("	<span style='border:0px;padding:0px;'>到第<input id='txtPage"+nextPageMethod+"' type='text' size='3'/>页</span>\n");
					sb.append("	<label class='btn_gray_s' id = 'gotoNum' style='margin:0px;margin-bottom:4px;'><input type='button' value='GO' onclick='"+nextPageMethod+"(this.value);return false;'/></label>\n");
				}
			}
			sb.append("	<span>第"+currentPage+"页</span>\n");
			sb.append("	<span>共"+totalPage+"页</span>\n");
			if("have".equals(type)){
				sb.append("	<span>每页"+pageSize+"条记录,共"+totalResult+"条</span>\n");
			}
			sb.append("</div>\n");
			
		}
		pageStr = sb.toString();
		return pageStr;
	}
	
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentResult() {
		currentResult = (getCurrentPage()-1)*getPageSize();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	public boolean isEntityOrField() {
		return entityOrField;
	}
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
}

	    			