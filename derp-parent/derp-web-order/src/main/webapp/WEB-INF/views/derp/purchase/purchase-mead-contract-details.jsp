<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
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
        text-align: left;
    }
     
    .pure-table caption {
        color: #000;
        font: italic 85%/1 arial,sans-serif;
        padding: 1em 0;
        text-align: center;
    }
     
    .pure-table td,.pure-table th {
        border-left: 2px solid #cbcbcb;
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
        border-bottom: 2px solid #cbcbcb;
    }
     
    .pure-table-bordered tbody>tr:last-child>td {
        border-bottom-width: 0;
    }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="edit-form">
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
                                            <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                			<li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单</a></li>
                                            <li class="breadcrumb-item"><a href="#">采购合同详情</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                    <div>
                                    <input type="hidden" id="orderId" value="${id }">
                                    <form id="edit-form">
                                    <input type="hidden" id="fileTempCode" name="fileTempCode" value="mead">
                                    <input type="hidden" id="code" name="code" value="${detail.code }">
                                    <input type="hidden" id="direction" name="direction" value="horizontal">
                                    	<table class="pure-table pure-table-bordered">
                                    		<tr>
                                    			<td colspan="4">
                                    			</td>
                                    			<td colspan="4" style="border-bottom: 1px solid #000000;">
                                    			   <b>採購單Purchase Order</b>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td colspan="8">
                                                    Company Name:<b>${detail.buyerEnName }</b>
                                                </td>
                                            </tr>
                                    		<tr>
                                    			<td colspan="6">
                                                    Address:<b>${detail.buyerAddress }</b>
                                                </td>
                                                <td colspan="2">
                                                	日期: ${detail.date }
                                                </td>
                                    		</tr>
                                    		<tr style="border-bottom: 2px solid #000000;">
                                                <td colspan="6">
                                                    ${detail.title }
                                                </td>
                                                <td colspan="2">
                                                    PO No:${detail.poNo }
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="6">
                                                    Supplier:${detail.sellerEnName }
                                                </td>
                                                <td colspan="2">
                                                                                                                                                            聯絡人: ${detail.linkman }
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="6">
                                                                                                                                                            交貨地址: ${detail.deliveryAddress }
                                                </td>
                                                <td colspan="2">
                                                                                                                                                            電話: ${detail.tel }
                                                </td>
                                            </tr>
                                            <tr style="border-bottom: 2px solid #000000;">
                                                <td colspan="6">
                                                </td>
                                                <td colspan="2">
                                                                                                                                                            手機: ${detail.moblie }
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td colspan="5">
                                                                                                                                                            送貨日期: ${detail.deliveryDate }
                                                </td>
                                                <td colspan="3">
                                                                                                                                                            送貨方式: <c:if test="${ empty detail.deliveryMethod }">By Truck</c:if><c:if test="${ not empty detail.deliveryMethod }">${detail.deliveryMethod }</c:if>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="8">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td >
                                                    SAP CODE
                                                </td>
                                                <td >
                                                                                                                                                            產品名稱
                                                </td>
                                                <td >
                                                                                                                                                            板數
                                                </td>
                                                <td >
                                                                                                                                                            每板數量/罐
                                                </td>
                                                <td >
                                                                                                                                                            數量/罐
                                                </td>
                                                <td >
                                                    List price/${detail.currency }
                                                </td>
                                                <td >
                                                    Net price ${detail.codDiscount }% COD/${detail.currency }
                                                </td>
                                                <td >
                                                    Total
                                                </td>
                                            </tr>
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td >
	                                    			    ${item.sapCode }
	                                    			</td>
	                                    			<td >
	                                    			    ${item.goodsName}
	                                    			</td>
	                                    			<td >
	                                    			    ${item.platesNum }
	                                    			</td>
	                                    			<td >
	                                    			    ${item.preNum }
	                                    			</td>
	                                    			<td >
	                                    			    ${item.num}
	                                    			</td>
	                                    			<td >
                                                        ${item.listPrice }
                                                    </td>
                                                    <td >
                                                        ${item.price}
                                                    </td>
                                                    <td >
                                                        ${item.amount}
                                                    </td>
	                                    		</tr>
                                    		</c:forEach>
                                    		    <tr>
                                                    <td >
                                                        Total：
                                                    </td>
                                                    <td ></td>
                                                    <td >
                                                        ${detail.totalPlatesNum }
                                                    </td>
                                                    <td ></td>
                                                    <td >
                                                        ${detail.totalNum}
                                                    </td>
                                                    <td ></td>
                                                    <td >Grand Total：</td>
                                                    <td >
                                                        ${detail.totalAccount}
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="8"></td>
                                                </tr>
                                    		    <tr>
                                                    <td>備註: </td>
                                                    <td colspan="7">
                                                        ${detail.remark }
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>公司蓋章: </td>
                                                    <td colspan="5"></td>
                                                    <td>COD Discount:</td>
                                                    <td>${detail.codDiscount }%</td>
                                                </tr>
                                    	</table>
                                    </form>
								<div class="form-row m-t-50">
                                      <div class="col-12">
                                          <div class="row">
                                              <div class="col-6">
                                                  <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="export-buttons" value="导出">
                                              </div>
                                              <div class="col-6">
                                                  <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="取 消">
                                              </div>
                                          </div>
                                      </div>
                                  </div>

                            </div>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
                </div>
            </div>
        </form>
    </div>
    <!-- container -->
</div>
<jsp:include page="/WEB-INF/views/modals/2013.jsp" />
</div> <!-- content -->
<script type="text/javascript">

	//返回
	$("#cancel-buttons").click(function(){
	    $module.load.pageOrder("act=3001");
	});
	
	//保存按钮
	$("#export-buttons").click(function(){
	    
	    var code = $("#code").val() ;
	    var fileTempCode = $("#fileTempCode").val() ;
	    var direction = $("#direction").val() ;
	    var url = serverAddr+"/purchase/exportTempDateFile.asyn?token=" + token + "&orderCode=" + code + "&fileTempCode=" + fileTempCode + "&direction=" + direction;
	    
	    
	    window.open(url);
	});
	
	
</script>
