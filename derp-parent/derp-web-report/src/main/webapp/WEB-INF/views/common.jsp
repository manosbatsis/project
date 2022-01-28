<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<% request.setAttribute("reportWebhost", com.topideal.common.tools.ApolloUtil.reportWebhost); %>
<%
    //jsp禁用缓存
    response.addHeader("Cache-Control", "no-store, must-revalidate");
    response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
%>
<script type="text/javascript">
	var serverAddr="${reportWebhost}";
	var pageUrl="/module/loadPage.asyn?url="+serverAddr+"/load/page.asyn?act=";
</script>
<script type="text/javascript"   src='/resources/js/system/base.js?r=<%=System.currentTimeMillis()%>' ></script>