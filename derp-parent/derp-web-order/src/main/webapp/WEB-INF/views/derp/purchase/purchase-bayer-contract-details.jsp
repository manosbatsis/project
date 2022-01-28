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
	    text-align: center;
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
                                    <input type="hidden" id="fileTempCode" name="fileTempCode" value="Bayer">
                                    <input type="hidden" id="code" name="code" value="${detail.code }">
                                    <input type="hidden" id="direction" name="direction" value="horizontal">
                                    	<table class="pure-table pure-table-bordered" >
                                    		<tr style="text-align:center;">
                                    			<td colspan="11" >
	                                    			${detail.buyerEnName }
	                                    			<br>
	                                    			${detail.buyerAddress }
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="11" style="text-align:center;font-weight: 750 ;font-size: 16px;">
                                    				Order Placement
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				PO Number
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.poNo }
                                    			</td>
                                    			<td colspan="2">
                                    				Week Number
                                    			</td>
                                    			<td colspan="6">
                                    				${detail.weekNumber }
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				Buyer Name
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.buyerEnName }
                                    			</td>
                                    			<td colspan="2">
                                    				Seller Name
                                    			</td>
                                    			<td colspan="6">
                                    				${detail.sellerEnName }
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2">
                                    				Buyer Address
                                    			</td>
                                    			<td style="width:150px;">
                                    				${detail.buyerAddress }
                                    			</td>
                                    			<td colspan="2">
                                    				Seller Address
                                    			</td>
                                    			<td colspan="6">
                                    				${detail.sellerAddress }
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="11" style="text-align: right;padding-right: 50px;">
                                    				Value (${detail.currency})
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td>NO</td>
                                    			<td>Brand</td>
                                    			<td>Product Name</td>
                                    			<td>SKUs</td>
                                    			<td>Globan</td>
                                    			<td>条码</td>
                                    			<td>BatchNo.</td>
                                    			<td>Expired date</td>
                                    			<td>合计总数</td>
                                    			<td>Purchase Price</td>
                                    			<td>Net Sales </td>
                                    		</tr>
                                    		
                                    		<c:forEach items="${goodsList }" var="item">
                                    			<tr>
	                                    			<td>
	                                    			    ${item.index }
	                                    			</td>
	                                    			<td>
	                                    			    ${item.brand }
	                                    			</td>
	                                    			<td>${item.productName}
	                                    			</td>
	                                    			<td>
	                                    			    ${item.skus}
	                                    			</td>
	                                    			<td>${item.globan }</td>
	                                    			<td>
	                                    			    ${item.barcode}
	                                    			</td>
	                                    			<td>${item.batch }</td>
	                                    			<td>${item.expiredDate }</td>
	                                    			<td>
	                                    			    ${item.num}
	                                    			</td>
	                                    			<td>
	                                    			    ${item.price}
	                                    			</td>
	                                    			<td>
	                                    			    ${item.amount}
	                                    			</td>
	                                    		</tr>
                                    		</c:forEach>
                                    		
                                    		<tr>
                                    			<td><br></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    		</tr>
                                    		
                                    		<tr>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td></td>
                                    			<td>
                                    			     ${detail.totalNum}
                                    			</td>
                                    			<td></td>
                                    			<td>
                                    			     ${detail.currency} ${detail.totalAccount}
                                    			</td>
                                    		</tr>
                                    		<tr>
                                    			<td><br></td>
                                    			<td colspan="10"></td>
                                    		</tr>
                                    		<tr>
                                    			<td rowspan="2" colspan="2">Signature</td>
                                    			<td colspan="2"></td>
                                    			<td colspan="7">Seller Signature</td>
                                    		</tr>
                                    		<tr>
                                    			<td colspan="2"><br></td>
                                    			<td colspan="7"></td>
                                    		</tr>  
                                    		<tr>
                                    			<td colspan="11" style="text-align: left;">Delivery address：${detail.deliveryAddress }</td>
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
	    var url = serverAddr+"/purchase/exportTempDateFile.asyn?token="+ token + "&orderCode=" + code + "&fileTempCode=" + fileTempCode + "&direction=" + direction;
	    
        
        window.open(url);
	});
	
	
</script>
