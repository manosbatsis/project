<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="comission-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog" style="max-width:650px;">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="invoice-from">
                                <!-- 自定义参数  -->  
                                <input type="hidden" id="modelId" value="" >
                                <div class="form-group row m-b-20">
                                    <div class="col-6">
                                        <i class="red">*</i><label>配&nbsp;置&nbsp;类&nbsp;型&nbsp;：</label>
                                        <select class="input_msg" name="modelConfigType" id="modelConfigType"></select>
                                    </div>
                                    <div class="col-6"></div>
                                </div>
                                <div class="form-group row m-b-20">
                                    <div class="col-6">
                                         <i class="red">*</i><label>客&nbsp;户&nbsp;名&nbsp;称&nbsp;：</label>                                	
                                      	 <select class="input_msg" name="modelCustomerId" id="modelCustomerId">
			                                <option value="">请选择</option>
			                            </select>        
                                    </div>
                                    <div class="col-6">
                                         <i class="red">*</i><label>供应商名称：</label>                                	
                                      	 <select class="input_msg" name="modelSupplierId" id="modelSupplierId">
			                            </select>        
                                    </div>
                                </div>      
                                                            
                                <div class="form-group row m-b-20">                                                                                           
                                    <div class="col-6">
                                        <i class="red">*</i><label>销售价回扣：</label>
                                        <input class="input_msg"  title="请输入销售价回扣"  id="saleRebate" name="saleRebate"  >
                                    	<a style="color:black">%</a> 
                                    </div>
                                    <div class="col-6">                                   
                                        <i class="red">*</i><label>采购价佣金：</label>                                      
                                        <input class="input_msg"  title="请输入采购价佣金"  id="purchaseCommission" name="purchaseCommission"  >
                                        <a style="color:black">%</a>                                 
                                    </div>
                                </div>
                                <div class="form-row mt20">
                      		<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='invoiceSave()'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='invoiceCancel()'>取消</button>
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
	$module.customer.loadSelectData.call($('select[name="modelCustomerId"]')[0]);
    //加载配置类型的下拉数据
    $module.constant.getConstantListByNameURL.call($('select[name="modelConfigType"]')[0],"purchaseCommission_configTypeList",null);


    $(document).ready(function() {
		initModelSupplierId() ;
	});
	
	// 保存
	function invoiceSave(){
		//必填项校验
        if($("#modelConfigType").val()==""){
            $custom.alert.error("请选择配置类型");
            return ;
        }
        if($("#modelCustomerId").val()==""){
			$custom.alert.error("请选择客户");
			return ;
		}	
		if ($("#modelSupplierId").val()=="") {
			$custom.alert.error("请选择供应商");
			return ;
		}
		
		if ($("#saleRebate").val()=="") {
			$custom.alert.error("销售价回扣不能为空");
			return ;
		}
		
		if( isNaN($("#saleRebate").val()) || parseFloat($("#saleRebate").val()) < 0 || parseFloat($("#saleRebate").val()) > 100){
			$("#saleRebate").val("");
			$custom.alert.error("销售价回扣请输入0-100的小数");
			return ;
		}
		
		if($("#saleRebate").val().toString().split(".").length > 1
				&& $("#saleRebate").val().toString().split(".")[1].length > 3){
			$custom.alert.error("请保留3位小数");
			return ;
		}
		
		if ($("#purchaseCommission").val()=="") {
			$custom.alert.error("采购价佣金不能为空");
			return ;
		}
		// 配置类型为2-公司加价比例时,采购价佣金不能为空
		if($("#modelConfigType").val()=="2" && $("#purchaseCommission").val()==0){
            $custom.alert.error("配置类型为公司加价比例时,采购价佣金不能为0");
            return ;
        }
		
		if( isNaN($("#purchaseCommission").val()) || parseFloat($("#purchaseCommission").val()) < 0 || parseFloat($("#purchaseCommission").val()) > 100){
			$("#purchaseCommission").val("");
			$custom.alert.error("采购价佣金请输入0-100的小数");
			return ;
		}
		
		if($("#purchaseCommission").val().toString().split(".").length > 1
				&& $("#purchaseCommission").val().toString().split(".")[1].length > 3){
			$custom.alert.error("请保留3位小数");
			
			return ;
		}
		
		
		var modelConfigType = $("#modelConfigType").val();//配置类型
		var modelCustomerId = $("#modelCustomerId").val();
		var modelSupplierId = $("#modelSupplierId").val();
		var saleRebate = $("#saleRebate").val();
		var purchaseCommission = $("#purchaseCommission").val();
		
		var requestUrl = "" ;
		var data = {} ;
		
		//判断是否存在ID，如存在执行编辑，否则进行新增
		if($("#modelId").val()){
			var id = $("#modelId").val() ;
			url = "/purchaseCommission/modifyPurchaseCommission.asyn" ; 
			data = {"customerId":modelCustomerId,"supplierId":modelSupplierId,"saleRebate":saleRebate,"purchaseCommission":purchaseCommission,"id":id,"configType":modelConfigType} ;
		}else{
			url = "/purchaseCommission/savePurchaseCommission.asyn" ; 
			data = {"customerId":modelCustomerId,"supplierId":modelSupplierId,"saleRebate":saleRebate,"purchaseCommission":purchaseCommission,"configType":modelConfigType} ;
		}
		
		//保存或编辑
		$custom.request.ajaxSync(url, data, function(data){
			if(data.state == '200'){
				if(data.data.status == '0'){	
					$custom.alert.error(data.data.message);
				}else{
					$module.revertList.serializeListForm() ;
		        	$module.revertList.isMainList = true ; 
		        	
					$custom.alert.success("操作成功");
					$("#modelId").val("");
					$("#modelConfigType").val("");
					$("#modelCustomerId").val("");
					$("#modelSupplierId").val("");
					$("#saleRebate").val("");
					$("#purchaseCommission").val("");
					$('#comission-modal').modal('hide');	
					 
					setTimeout("$load.a('/purchaseCommission/toPage.html');" , 1000) ;
				}
				 
			}else{
                $("#modelConfigType").val("");
                $("#modelCustomerId").val("");
				$("#modelSupplierId").val("");
				$("#saleRebate").val("");
				$("#purchaseCommission").val("");
				$("#modelId").val("");
				$custom.alert.error(data.message);
				$('#comission-modal').modal('hide');						
			}
		});
	};
	function invoiceCancel(){
		$('#comission-modal').modal('hide');
        $("#modelConfigType").val("");
        $("#modelCustomerId").val("");
		$("#modelSupplierId").val("");
		$("#saleRebate").val("");
		$("#purchaseCommission").val("");
		$("#modelId").val("");
	}
	
	function initModelSupplierId(){
		//将供应商查询下拉赋值
		var opts = $("#supplierId").find("option") ; 
		
		var html = "";
		$(opts).each(function(i , m){
			html += $(m).prop("outerHTML") ;
		});
		$("#modelSupplierId").html(html);
		
		$("#modelSupplierId").val("");
		
	}

</script>