<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="toSale-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:10px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;margin-left: -90px;">
	                	<div class="header" >
			              <span class="header-title" >选择出库仓库</span>
			          </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="toSale-from">
                                <!-- 自定义参数  -->                              
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                        <label><a style="color:red">*</a>出库仓库:</label>
                                        <select class="edit_input" name="outDepotId" id="outDepotId" parsley-trigger="change" require reqMsg="请选择出仓仓库">
			                                <option value="">请选择</option>
			                            </select>
                                    </div>
                                </div>
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                         <label style="margin-left: 13px;"><a style="color:red">*</a>事业部:</label>                                	
                                      	 <select class="edit_input" name="buId" id="buId" parsley-trigger="change" require reqMsg="请选择事业部">
			                                <option value="">请选择</option>
			                            </select>
                                    </div>
                                </div>
			                      <div class="form-row mt20">
			                      	<div class="row col-12 table-responsive" id="itemTable" >
										<table class="table table-striped dataTable no-footer"
											cellspacing="0" width="100%" >
											<thead>
												<tr>
													<th>PO号</th>
													<th>条形码</th>
													<th>商品名称</th>
													<th>采购数量</th>
													<th>销售数量</th>
												</tr>
											</thead>
											<tbody id="itemtoSaleTbody" style="height: 200px; overflow-y: scroll">
											</tbody>
										</table>
									</div>
			                      </div>
			                      <div class="form-row mt20">
			                      		<div class="form-inline m0auto">
			                          		<div class=form-group>
			                          			<button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m17001.save();'>确定</button>
			                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m17001.hide();'>取消</button>
			                          		</div>
			                          	</div>
			                      </div>
                                </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0]);
	$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0], null, {"type":"a,c,d"});
	
	var $m17001 = {
	        init: function (ids) {
	        	var url = serverAddr + "/platformPurchaseOrder/getPlatformPurchaseOrderItems.asyn" ;
	        	
	        	$m17001.params.ids = ids ;
	        	$($m17001.params.modalId).modal({backdrop: 'static', keyboard: false});
	        	
	        	var html = "" ;
	        	$custom.request.ajax(url, {"ids": ids}, function(result){
	        		
	        		var list = result.data ;
	        		
	        		var totalNum = 0 ;
	        		$(list).each(function(index, item){
	        			html += "<tr>" +
	        				"<td>"+ item.poNo +"</td>" +
	        				"<td class=\"platformBarcode\">"+ item.platformBarcode +"</td>" +
	        				"<td>"+ item.platformGoodsName +"</td>" +
	        				"<td>"+ item.num +"</td>" +
	        				"<td><input type=\"text\" class=\"toSalesNum\" value=\""+ item.num +"\" onblur=\"sumSalesTotal()\" onkeyup=\"value=value.replace(/[^\\d^\.]/g,'')\"></td>" +
	        			"</tr>" ;
	        			
	        			totalNum = parseInt(totalNum) + parseInt(item.num) ;
	        		}) ;
	        		
	        		var arr = ids.split(",") ;
	        		
	        		html += "<tr>" +
	    				"<td>合计："+ arr.length +"个PO号</td>" +
	    				"<td></td>" +
	    				"<td></td>" +
	    				"<td>"+ totalNum +"</td>" +
	    				"<td id=\"totalSalesNum\">"+ totalNum +"</td>" +
    				"</tr>" ;
    				
    				$("#itemtoSaleTbody").html(html) ;
	        		
	        	}) ;
	        },
	        hide: function () {
	        	$("#outDepotId").val("") ;
	        	$("#buId").val("") ;
	        	$('.modal-backdrop').remove();
	            $($m17001.params.modalId).modal("hide");
	        },
	        save: function () {
	        	var outDepotId = $("#outDepotId").val() ;
	        	var buId = $("#buId").val() ;
	        	
	        	if(!outDepotId){
	        		$custom.alert.error("请选择出仓仓库");
	        		
	        		return ;
	        	}
	        	
	        	if(!buId){
	        		$custom.alert.error("请选择事业部");
	        		
	        		return ;
	        	}
	        	
	        	var flag = true ;
	        	var platformSalesNum = "" ;
	        	$(".toSalesNum").each(function(index, item){
	        		if(!$(item).val()){
	        			flag &= false ;
	        			
	        			return false ;
	        		}else{
	        			var platformBarcode = $(item).parent().parent().find(".platformBarcode").text() ;
	        			
	        			platformSalesNum += platformBarcode + "__" + $(item).val() + "," ;
	        		}
	        	}) ;
	        	
	        	if(!flag){
					$custom.alert.error("请输入销售数量");
	        		
	        		return ;
	        	}
	        	
	        	$m17001.hide() ;
	        	$load.a(pageUrl+"40011&pageSource=2&outDepotId=" + outDepotId 
	        			+ "&buId=" + buId + "&platformPurchaseIds=" + $m17001.params.ids 
	        			+ "&platformSalesNum=" + platformSalesNum);
	        },
	        params: {
	            modalId: '#toSale-modal',
	            url: null,
	            ids: null
	        }
	    };

	function sumSalesTotal(){
		
		var totalSalesNum = 0 ;
		$(".toSalesNum").each(function(index, item){
			var val = $(item).val() ;
			
			if(val){
				totalSalesNum = parseInt(totalSalesNum) + parseInt(val) ;
			}
		}) ;
		
		$("#totalSalesNum").html(totalSalesNum) ;
	}
	
</script>