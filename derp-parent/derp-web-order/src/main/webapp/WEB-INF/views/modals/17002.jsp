<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
    }
    .header-title {
        padding: 15px;
        padding-right: 400px;
        font-weight: bolder;
        font-size: 18px;
    }
    .header-button {
        background-color: #fff;
        border: none;
    }
    .header-close {
    }
    .header-close::after {
        content: "\2716";
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="details-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -400px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 1000px;">
            	<div class="header" >
              <span class="header-title" >客户采购单详情</span>
          </div>
                <div class="modal-body" >
                        <!-- 自定义参数  -->  
                  	<div class="info_box">
                  		<div class="info_item">
	                          <div class="info_text">
	                              <span>PO号：</span>
	                              <span id="poNo"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>客户：</span>
	                              <span id="customerName"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>下单时间：</span>
	                              <span id="orderTime"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>平台状态：</span>
	                              <span id="platformStatus"></span>
	                          </div>
	                      </div>
	                      <div class="info_item">
	                      		<div class="info_text">
	                              <span>采购币种：</span>
	                              <span id="currency"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>客户仓库：</span>
	                              <span id="platformDepotName"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>入库时间：</span>
	                              <span id="deliverDate"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>单据状态：</span>
	                              <span id="status"></span>
	                          </div>
	                      </div>
	                      <div class="info_item">
	                      		
	                          <div class="info_text">
	                              <span>提交人：</span>
	                              <span id="submitName"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>转销人：</span>
	                              <span id="resaleName"></span>
	                          </div>
	                          <div class="info_text">
	                              <span>转销时间：</span>
	                              <span id="resaleDate"></span>
	                          </div>
	                      </div>
	                      <div class="info_item">
	                      		<div class="info_text">
	                              <span>销售订单号：</span>
	                              <span id="saleCode"></span>
	                          </div>
	                          
	                      </div>
                  	</div>
                 	<div class="form-group">
                       	<div class="row col-12 table-responsive" id="itemTable" style="position: relative; overflow-x: hidden; overflow-y: scroll; height: 250px;">
							<table class="table table-striped dataTable no-footer"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th>条形码</th>
										<th>商品名称</th>
										<th>采购数量</th>
										<th>单价</th>
										<th>采购金额</th>
										<th>实收好品数</th>
										<th>实收坏品数</th>
									</tr>
								</thead>
								<tbody id="itemTbody">
								</tbody>
							</table>
						</div>
                       </div>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m17002.hide() ;">取消</button>
                      </div>
                  </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">	
var $m17002 = {
        init: function (data) {
        	
        	if(data.poNo){
        		$("#poNo").html(data.poNo) ;
        	}
        	
        	if(data.customerName){
        		$("#customerName").html(data.customerName) ;
        	}
        	
        	if(data.orderTime){
        		$("#orderTime").html(data.orderTime) ;
        	}
        	
        	if(data.platformStatusLabel){
        		$("#platformStatus").html(data.platformStatusLabel) ;
        	}
        	
        	if(data.currencyLabel){
        		$("#currency").html(data.currencyLabel) ;
        	}
        	
        	if(data.platformDepotName){
        		$("#platformDepotName").html(data.platformDepotName) ;
        	}
        	
        	if(data.deliverDate){
        		$("#deliverDate").html(data.deliverDate) ;
        	}
        	
        	if(data.saleCode){
        		$("#saleCode").html(data.saleCode) ;
        	}
        	
        	if(data.submitName){
        		$("#submitName").html(data.submitName) ;
        	}
        	
        	if(data.resaleName){
        		$("#resaleName").html(data.resaleName) ;
        	}
        	
        	if(data.resaleDate){
        		$("#resaleDate").html(data.resaleDate) ;
        	}
        	
        	if(data.status){
        		$("#status").html(data.statusLabel) ;
        	}
        	
        	if(data.itemList){
        		var list = data.itemList ;
        		
        		var html = "" ;
        		var totalNum = 0 ;
        		var totalAmount = 0.0 ;
        		var totalGoodNum = 0 ;
        		var totalBadNum = 0 ;
        		$(list).each(function(i, item){
        			html += "<tr>" ;
        			html += "<td>" + item.platformBarcode + "</td>" ;
        			html += "<td>" + item.platformGoodsName + "</td>" ;
        			html += "<td>" + item.num + "</td>" ;
        			html += "<td>" + item.price + "</td>" ;
        			html += "<td>" + item.amount + "</td>" ;
        			html += "<td>" + item.receiptOkNum + "</td>" ;
        			html += "<td>" + item.receiptBadNum + "</td>" ;
        			html += "</tr>" ;
        			
        			totalNum = totalNum + parseInt(item.num) ;
        			totalAmount = totalAmount + parseFloat(item.amount) ;
        			totalGoodNum = totalGoodNum + parseInt(item.receiptOkNum) ;
        			totalBadNum = totalBadNum + parseInt(item.receiptBadNum) ;
        		}) ;
        		
        		html += "<tr>" ;
        		html += "<td>合计</td>" ;
    			html += "<td></td>" ;
    			html += "<td>" + totalNum + "</td>" ;
    			html += "<td></td>" ;
    			html += "<td>" + totalAmount.toFixed(3) + "</td>" ;
    			html += "<td>" + totalGoodNum + "</td>" ;
    			html += "<td>" + totalBadNum + "</td>" ;
    			html += "</tr>" ;
    			
    			$("#itemTbody").html(html) ;
        	}
        	
        	$($m17002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide: function () {
        	$('.modal-backdrop').remove();
            $($m17002.params.modalId).modal("hide");
        },
        params: {
            modalId: '#details-modal'
        }
    };
	
</script>