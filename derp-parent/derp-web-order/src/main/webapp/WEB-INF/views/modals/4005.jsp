<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<!-- Custom Modals -->
<div>
	<!-- Signup modal content -->
	<div id="4005-add-signup-modal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="custom-width-modalLabel"
		aria-hidden="true" style="display: none; top: 10px;">
		<div class="modal-dialog">
			<div class="modal-content" style="width: 900px; margin-left: -120px;">
				<div class="header">
					<span class="header-title">转销售订单</span>
				</div>
				<div id="4005-modal-body" class="modal-body">
					<form class="form-horizontal" id="4005Form">
						<div class="edit_box mt20">
							<p id="merchantName"></p>公司下无对应的销售订单，是否生成销售订单？
						</div>
						<div class="edit_box mt20">
							<input type="hidden" name="id" id="4005modalId">
							<div class="edit_item">
								<label><input type="radio" name="isGenerate" value="0"> 不生成</label>
							</div>
							<div class="edit_item">
								<label><input type="radio" name="isGenerate" value="1"> 生成</label>
							</div>
							<div class="edit_item"></div>
						</div>
						<div class="edit_box mt20 saleOrder">
							<div class="edit_item">
								<label class="edit_label"><i class="red">*</i> 出仓仓库：</label>
	                            <select class="edit_input" name="outDepotId" id="outDepotId" parsley-trigger="change" required reqMsg="请选择出仓仓库">
	                                <option value="">请选择</option>
	                            </select>
							</div>
							<div class="edit_item">
								<label class="edit_label"><i class="red">*</i> 事业部：</label>
	                            <select class="edit_input" name="buId" id="buId" parsley-trigger="change" required reqMsg="请选择事业部">
	                                <option value="">请选择</option>
	                            </select>
							</div>
							<div class="edit_item"></div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button
						class="btn btn-info waves-effect waves-light fr btn_default delayedBtn"
						type="button" onclick="$m4005.submit();">确定</button>
					<button class="btn btn-light waves-effect waves-light btn_default"
						type="button" onclick="$m4005.cancel();">取消</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</div>
<script type="text/javascript">
    var $m4005={
        init:function(orderId, merchantName, callback){
        	
        	// 动态高度
        	var height = window.screen.height * 0.4 ;
        	
        	$("#4005-modal-body").css("height", height + "px") ;
        	$("#4005-modal-body").css("overflow-y", "scroll") ;
        	
        	$module.businessUnit.getAllSelectBean.call($("select[name='buId']")[0]);
        	$module.depot.loadSelectData.call($("select[name='outDepotId']")[0]);
        	
        	$m4005.params.orderId = orderId ;
        	$m4005.params.callback = callback ;
        	
        	$(".saleOrder").hide() ;
        	$("#merchantName").html(merchantName) ;
        	
        	$m4005.show() ;
        },
        submit:function(){
        	$m4005.add();
        },
        add:function(){
        	
        	var isGenerate = $("input[name='isGenerate']:checked").val() ;
        	
        	
        	
        	if(isGenerate == '0'){
        		
        		$m4005.hide() ;
        		
        		$m4005.params.callback() ;
        	}else{
        		
        		var outDepotId = $("#outDepotId").val() ;
        		
        		if(!outDepotId){
        			$custom.alert.error("出仓仓库不能为空");
        			
        			return ;
        		}
        		
        		var buId = $("#buId").val() ;
        		if(!buId){
        			$custom.alert.error("事业部不能为空");
        			
        			return ;
        		}
        		
        		$custom.request.ajax($m4005.params.addUrl, {"id": $m4005.params.orderId, "buId": buId, "outDepotId": outDepotId}, function(result){
        			if(result.state == '200'){
        				
        				$m4005.hide() ;
        				
        				if(result.data){
        					$custom.alert.success("审核成功，生成销售单失败");
        				}else{
        					$custom.alert.success("审核成功，生成销售订单成功");
        				}
        				
        				$m4005.params.callback() ;
        				
        			}else{
        				if(result.expMessage != null){
        					$custom.alert.error(result.expMessage);
        				}else{
        					$custom.alert.error(result.message);
        				}
        			}
        		}) ;
        	}
        	
        },
        show:function(){
             $($m4005.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4005.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchase/saveInnerMerchantSaleOrders.asyn?r='+Math.random(),
            formId:'#4005Form',
            modalId:'#4005-add-signup-modal',
            orderId: '',
            callback: '' 
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4005.hide();
                
                $module.load.pageOrder("act=3001");
            });
        },
        succFun:function(data){
            if(data.state==200){
            	
            	var saleId = data.data ;
            	
            	swal({
    				title: "转销售订单成功，是否进入销售订单编辑页面",
    				type: 'warning',
    				showCancelButton: true,
    				confirmButtonColor: '#4fa7f3',
    				cancelButtonColor: '#d57171',
    			}).then(function(){ 
    				//成功隐藏
	                $m4005.hide();
	                //重新加载table表格
	                setTimeout(function () {
	                	$module.load.pageOrder("act=40012&id=" + saleId);
	                }, 500);
    			}, function(dismiss){ 
    			    if(dismiss == 'cancel'){ 
    			    	$module.revertList.serializeListForm() ;
    	                $module.revertList.isMainList = true ;
    	            	
    	                //成功隐藏
    	                $m4005.hide();
    	                //重新加载table表格
    	                setTimeout(function () {
    	                	$module.load.pageOrder("act=3001");
    	                }, 500);
    			    } 
    			}) ;
            	
            	
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
            
		},
		logicFun:function(){
        	
        }
    }
    
    $("input[name=isGenerate]").click(function(){
        var val = $(this).val();
        
        if(val == '1'){
        	$(".saleOrder").show() ;
        }else{
        	$(".saleOrder").hide() ;
        }
    });
    
</script>