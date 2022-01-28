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
	    text-align: center;
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
							                      <li class="breadcrumb-item"><a href="#">销售管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=18001');">销售目标列表</a></li>
							                      <li class="breadcrumb-item"><a href="#">销售目标详情</a></li>
							            </ol>
                                    </div>
                                </div>
                                <form id="form1">
                                </form>
                                <div class="title_text">基本信息</div>
                                <div class="info_box">
			                        <div class="info_item">
			                            <div class="info_text">
											<span>事业部：</span>
											<span>${buName }</span>
	                                    </div>
	                                    <div class="info_text">
			                            	<span>销售计划月份：</span>
			                            	<span>${month }</span>
			                            </div>
			                            <div class="info_text">
			                            	<span>计划维度：</span>
			                            	<span>${typeLabel }</span>
			                            </div>
                                	</div>
                                    <div class="info_item">
                                    </div>
                                </div>
                                <c:choose>
                                	<c:when test="${type == '1' }">
                                		<table class="pure-table pure-table-bordered">
                                			<thead>
                                				<tr>
                                					<td>商品条码</td>
                                					<td>商品名称</td>
                                					<td>标准品牌</td>
                                					<td>品类</td>
                                					<td>购销</td>
                                					<td>电商订单</td>
                                				</tr>
                                			</thead>
		                                	<tbody>
		                                		<c:forEach items="${list }" var="item">
		                                			<tr>
			                                			<td>${item.barcode }</td>
	                                					<td>${item.goodsName }</td>
	                                					<td>${item.brandParent }</td>
	                                					<td>${item.typeName }</td>
	                                					<td>${item.toBNum }</td>
	                                					<td>${item.toCNum }</td>
                                					</tr>
		                                		</c:forEach>
		                                	</tbody>
		                                </table>
                                	</c:when>
                                	<c:when test="${type == '2' }">
                                		<table class="pure-table pure-table-bordered">
                                			<thead>
                                				<tr>
                                					<td>商品条码</td>
                                					<td>商品名称</td>
                                					<td>标准品牌</td>
                                					<c:forEach items="${platFormlist }" var="platForm">
                                						<td>${platForm }</td>
                                					</c:forEach>
                                				</tr>
                                			</thead>
		                                	<tbody>
		                                		<c:forEach items="${list }" var="item">
		                                			<tr>
			                                			<td>${item.barcode }</td>
	                                					<td>${item.goodsName }</td>
	                                					<td>${item.brandParent }</td>
	                                					<c:forEach items="${item.platformCount }" var="map">
	                                						<td>${map.value }</td>
	                                					</c:forEach>
                                					</tr>
		                                		</c:forEach>
		                                	</tbody>
		                                </table>
                                	</c:when>
                                	<c:when test="${type == '3' }">
                                		<table class="pure-table pure-table-bordered">
                                			<thead>
                                				<tr>
                                					<td>商品条码</td>
                                					<td>商品名称</td>
                                					<td>标准品牌</td>
                                					<c:forEach items="${shopNamelist }" var="shopName">
                                						<td>${shopName }</td>
                                					</c:forEach>
                                				</tr>
                                			</thead>
		                                	<tbody>
		                                		<c:forEach items="${list }" var="item">
		                                			<tr>
			                                			<td>${item.barcode }</td>
	                                					<td>${item.goodsName }</td>
	                                					<td>${item.brandParent }</td>
	                                					<c:forEach items="${item.shopCount }" var="map">
	                                						<td>${map.value }</td>
	                                					</c:forEach>
                                					</tr>
		                                		</c:forEach>
		                                	</tbody>
		                                </table>
                                	</c:when>
                                	<c:otherwise>
                                	</c:otherwise>
                                </c:choose>
                                

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
			$module.load.pageReport("act=18001");
		});
	});
</script>
