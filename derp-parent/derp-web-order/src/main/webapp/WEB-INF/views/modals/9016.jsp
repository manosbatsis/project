<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="chooseBuDepot-modal"  class="modal fade" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 528px;text-align: center;">
                        <div class="modal-header">
                            <h5 class="modal-title" id="myLargeModalLabel">生成采购订单</h5>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="pay-from">
                            	<input type="hidden" name="9016modalId" id="9016modalId">
	                            <div class="edit_box mt20">
									<p id="9016merchantName"></p>公司下无对应的采购订单，是否生成采购订单？
								</div>
								<div class="edit_box mt20">
									<div class="edit_item">
										<label><input type="radio" name="isGenerate" value="0"> 不生成</label>
									</div>
									<div class="edit_item">
										<label><input type="radio" name="isGenerate" value="1"> 生成</label>
									</div>
								</div>
                                <!-- 自定义参数  -->
                                <div id="DepotBuDiv" style="display:none">
	                                <div class="form-group row m-b-20">
	                                    <div class="col-6">
	                                        <label>入库仓库:<a style="color:red">*</a></label>
	                                        <select class="edit_input" name="modalDepotId" id="modalDepotId">
	                                            <option value="">请选择</option>
	                                        </select>
	                                    </div>
	                                    <div class="col-6">
	                                        <label>事业部:<a style="color:red">*</a></label>
	                                        <select class="edit_input" name="modalBuId" id="modalBuId">
	                                            <option value="">请选择</option>
	                                        </select>
	                                    </div>
	                                </div>	                                
                                </div>                                                         
                            </form>
                            <div class="form-row mt20">
                                <div class="form-inline m0auto">
                                    <div class=form-group>
                                        <button type="button" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='chooseSave()'>确定</button>
                                        <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='chooseCancel()'>取消</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">   
    var $m9016={
            init:function(){
            	$module.businessUnit.getAllSelectBean.call($("select[name='modalBuId']")[0]);
            	$module.depot.loadSelectData.call($("select[name='modalDepotId']")[0]);
            	this.show();
            },
            show:function(){
                $($m9016.params.modalId).modal({keyboard: true});
            },
            hide:function(){
                $($m9016.params.modalId).modal("hide");
            },
            params:{
                modalId:'#chooseBuDepot-modal',
            },
        };
    
    // 保存
    function chooseSave(){
    	var isGenerate = $("input[name='isGenerate']:checked").val() ;
    	if(isGenerate == '1'){
	        var modalDepotId = $("#modalDepotId").val();
	        var modalBuId = $("#modalBuId").val();
	        var modalSaleId = $("#9016modalId").val();
	        //必填项校验
	        if(modalDepotId == ""){
	            $custom.alert.error("请选择入库仓库!");
	            return ;
	        }      
	        if(modalBuId == ""){
	            $custom.alert.error("请选择事业部!");
	            return ;
	        }
	       
			var url = serverAddr+"/sale/createPurchaseOrder.asyn";
			$custom.request.ajax(url, {"saleId":modalSaleId,"buId":modalBuId,"depotId":modalDepotId}, function(result){
				if (result.state == 200) {
					$m9016.hide();
	                $custom.alert.success(result.data);
	                //重新加载页面
	                setTimeout(function () {
	                    $load.a(pageUrl + "4001");
	                }, 1000);
	            } else {
	                if (!!result.expMessage) {
	                    $custom.alert.error(result.expMessage);
	                } else {
	                    $custom.alert.error(result.message);
	                }
	            }
			});
    	}
    	if(isGenerate == '0'){
    		chooseCancel();    		
    	}
        
    };    

    function chooseCancel(){
    	$m9016.hide();
        $("#modalDepotId").val("");
        $("#modalBuId").val("");
       //重新加载页面
        setTimeout(function () {
            $load.a(pageUrl + "4001");
        }, 1000);
    }
    $("input[name=isGenerate]").click(function(){
        var val = $(this).val();
        if(val == '1'){
        	$("#DepotBuDiv").show();
        }else{
        	$("#DepotBuDiv").hide();
        }
    });
    
</script>