<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
   request.setAttribute("orderWebhost", com.topideal.common.tools.ApolloUtil.orderWebhost); 
   request.setAttribute("storageWebhost", com.topideal.common.tools.ApolloUtil.storageWebhost);
   request.setAttribute("inventoryWebhost", com.topideal.common.tools.ApolloUtil.inventoryWebhost); 
   request.setAttribute("reportWebhost", com.topideal.common.tools.ApolloUtil.reportWebhost); 
%> 
<script>
<!-- 子服务ip -->
var orderWebhost = "${orderWebhost}";
var storageWebhost = "${storageWebhost}";
var inventoryWebhost = "${inventoryWebhost}";
var reportWebhost = "${reportWebhost}";
</script>