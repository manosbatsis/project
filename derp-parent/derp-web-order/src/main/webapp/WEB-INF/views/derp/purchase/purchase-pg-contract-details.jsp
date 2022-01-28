<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
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
	    border: 2px solid #cbcbcb;
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
                                    <input type="hidden" id="fileTempCode" name="fileTempCode" value="PG">
                                    <input type="hidden" id="code" name="code" value="${detail.code }">
                                    <input type="hidden" id="direction" name="direction" value="horizontal">
                                    	<table class="pure-table pure-table-bordered" >
                                    		<tr>
                                    			<td>
	                                    			Sales Organisation
                                    			</td>
                                    			<td>
                                                    HK01
                                                </td>
                                                <td>
                                                    Sold to code
                                                </td>
                                                <td>
                                                    ${detail.soldToCode }
                                                </td>
                                                <td>
                                                    Internal order #
                                                </td>
                                                <td>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td>
                                                    Distribution Channel
                                                </td>
                                                <td>
                                                    ${detail.distributionChannel }
                                                </td>
                                                <td>
                                                    Ship to code
                                                </td>
                                                <td>
                                                    ${detail.shipToCode }
                                                </td>
                                                <td>
                                                    GL account #
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                    		<tr>
                                    			<td>
                                                    Division
                                                </td>
                                                <td>
                                                    ${detail.division }
                                                </td>
                                                <td>
                                                    Requesting department
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Cost center #
                                                </td>
                                                <td>
                                                </td>
                                    		</tr>
                                    		<tr>
                                                <td>
                                                    Order Type
                                                </td>
                                                <td>
                                                    Z001
                                                </td>
                                                <td>
                                                    Shipping Condition
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Delivery Plant
                                                </td>
                                                <td>
                                                    ${detail.deliveryPlant}
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Order date
                                                </td>
                                                <td>
                                                    ${detail.orderDate }
                                                </td>
                                                <td>
                                                    Order Reason
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    PO Number
                                                </td>
                                                <td>
                                                    ${detail.poNo}
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Request delivery date
                                                </td>
                                                <td>
                                                    ${detail.requestDeliveryDate }
                                                </td>
                                                <td>
                                                    SLOG bracket
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    Sold-to Name
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                    		<tr>
                                                <td>
                                                    Remark
                                                </td>
                                                <td colspan="2">
                                                </td>
                                                <td colspan="2">
                                                    MAXIMUM 30 LETTERS
                                                </td>
                                                <td>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="6">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="6">
                                                    Order list
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Description
                                                </td>
                                                <td>
                                                </td>
                                                <td>
                                                    PG code
                                                </td>
                                                <td>
                                                    Barcode
                                                </td>
                                                <td>
                                                    Order Qty
                                                </td>
                                                <td>
                                                    UOM
                                                </td>
                                            </tr>
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td>
	                                    			    ${item.description }
	                                    			</td>
	                                    			<td>
	                                    			</td>
	                                    			<td>
	                                    			    ${item.pgCode}
	                                    			</td>
	                                    			<td>
	                                    			</td>
	                                    			<td>
	                                    			    ${item.orderQty}
	                                    			</td>
	                                    			<td>
                                                        CS
                                                    </td>
	                                    		</tr>
                                    		</c:forEach>
                                    		
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
	    var url = serverAddr+"/purchase/exportTempDateFile.asyn?token="+ token +"&orderCode=" + code + "&fileTempCode=" + fileTempCode + "&direction=" + direction;
	    
	    
	    window.open(url);
	});
	
</script>
