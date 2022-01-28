<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.not-arrow{
		padding: 5px 10px;
		border:1px solid #dcd8d8;
		-webkit-appearance:none;
		-moz-appearance:none;
		appearance:none; /*去掉下拉箭头*/
	}
	.not-arrow::-ms-expand { display: none; }
	
	.title_text_new{
	    line-height: 40px;
	    font-size: 24px;
	    font-weight: bold;
	    text-align: center;
	}
	.info_box { 
		padding: 10px;
	}
	
	table {
	    border-collapse: collapse;
	    border-spacing: 0;
	}
	 
	td,th {
	    padding: 0;
	}
	 
	.pure-table {
	    border-collapse: collapse;
	    border-spacing: 0;
	    empty-cells: show;
	    border: 1px solid #cbcbcb;
	    width: 95% ;
	    margin-top:25px ;
	    text-align: center;
	}
	 
	.pure-table caption {
	    color: #000;
	    font: italic 85%/1 arial,sans-serif;
	    padding: 1em 0;
	    text-align: center;
	}
	 
	.pure-table td,.pure-table th {
	    border-left: 1px solid #cbcbcb;
	    border-width: 0 0 0 1px;
	    font-size: inherit;
	    margin: 0;
	    overflow: visible;
	    padding: .5em 1em;
	}
	 
	.pure-table thead {
	    background-color: #e0e0e0;
	    color: #000;
	    text-align: left;
	    vertical-align: bottom;
	}
	 
	.pure-table td {
	    background-color: transparent;
	}
	 
	.pure-table-bordered td {
	    border-bottom: 1px solid #cbcbcb;
	}
	 
	.pure-table-bordered tbody>tr:last-child>td {
	    border-bottom-width: 0;
	}
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
							                      <li class="breadcrumb-item"><a href="#">报表管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=12008');">商品在库天数统计</a></li>
							                      <li class="breadcrumb-item"><a href="#">商品在库天数统计详情</a></li>
							            </ol>
                                    </div>
                                </div>
                                <form id="form1">
        	                       <input type="hidden" name='startDay' id='startDay' value='${startDay}'/>
        	                       <input type="hidden" name='month' id='month' value='${month}'/>
                                </form>
                                <div class="title_text">基本信息</div>
                                <div class="info_box">
			                        <div class="info_item">
			                            <div class="info_text">
											<span>事业部：</span>
											<span>${buName }</span>
	                                    </div>
	                                    <div class="info_text">
			                            	<span>数据截止时间：</span>
			                            	<span><fmt:formatDate value="${statisticsDate}" pattern="yyyy-MM-dd"/></span>
			                            </div>
			                            <div class="info_text">
			                            	<span>报表月份：</span>
			                            	<span>${month }</span>
			                            </div>
                                	</div>
                                	<div class="info_item">
			                            <div class="info_text">
			                            	<span>库存总量：</span>
			                            	<span>${totalNum }</span>
			                            </div>
			                            <div class="info_text">
			                            	<span>库存总金额：</span>
			                            	<span><fmt:formatNumber value='${totalAmount }' pattern='#.##'  /></span>
			                            </div>
			                            <div class="info_text">
			                            	<span>统计币种：</span>
			                            	<span>${currency }</span>
			                            </div>
                                    </div>
                                    <div class="info_item">
                                    </div>
                                </div>
                                <table class="pure-table pure-table-bordered">
                                	<tbody>
                                		<tr>
                                			<td>在库天数</td>
                                			<c:forEach items="${detailList}" var="item" >  
                                				<td>${item.inWarehouseRange }</td>
                                			</c:forEach>
                                		</tr>
                                		<tr>
                                			<td>库存数量</td>
                                			<c:forEach items="${detailList}" var="item" >  
                                				<td>${item.totalNum }</td>
                                			</c:forEach>
                                		</tr>
                                		<tr>
                                			<td>库存金额</td>
                                			<c:forEach items="${detailList}" var="item" >  
                                				<td><fmt:formatNumber value='${item.totalAmount }' pattern='#.##'  /></td>
                                			</c:forEach>
                                		</tr>
                                		<tr>
                                			<td>加权金额</td>
                                			<c:forEach items="${detailList}" var="item" >  
                                				<td><fmt:formatNumber value='${item.weightedAmount }' pattern='#.##'  /></td>
                                			</c:forEach>
                                		</tr>
                                		<tr>
                                			<td>库存金额占比</td>
                                			<c:forEach items="${detailList}" var="item" >  
                                				<td>${item.proportion }</td>
                                			</c:forEach>
                                		</tr>
                                	</tbody>
                                </table>

                                <div class="form-row m-t-50">
		                          <div class="col-12">
		                              <div class="row">
		                                  <div class="col-5">
		                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
		                                  </div>
		                              </div>
		                          </div>
		                      </div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
            </div>
    </div>
    <!-- container -->
</div>

<script type="text/javascript">
	$(function(){
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.revertList.isMainList = true ;
			$module.load.pageReport("act=12008");
		});
	});
</script>
