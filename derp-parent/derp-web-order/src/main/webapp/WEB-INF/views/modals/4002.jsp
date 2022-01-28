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
	<div id="add-signup-modal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="custom-width-modalLabel"
		aria-hidden="true" style="display: none; top: 10px;">
		<div class="modal-dialog">
			<div class="modal-content" style="width: 900px; margin-left: -120px;">
				<div class="header">
					<span class="header-title">修改SD金额</span>
				</div>
				<div id="4002-modal-body" class="modal-body">
					<div class="info_box">
						<div class="info_item">
                          	<div class="info_text">
	                            <label>SD单号 :</label>
		                        <span id="4002code"></span>
	                        </div>
	                        <div class="info_text">
		                        <label>单据类型 :</label>
		                        <span id="4002typeLabel"></span>
	                        </div>
	                        <div class="info_text">
	                            <label>关联单号 :</label>
	                            <span id="4002purchaseOrderCode"></span>
                            </div>
	                     </div>
	                     <div class="info_item">
	                     	<div class="info_text">
	                     		<label>PO 号:</label>
	                        	<span id="4002poNo"></span>
	                     	</div>
	                     	<div class="info_text">
	                     		<label>币种 :</label>
	                        	<span id="4002currencyLabel"></span>
	                     	</div>
	                     	<div class="info_text">
	                     		<label>出入库时间 :</label>
                            	<span id="4002inboundDate"></span>
	                     	</div>
	                     </div>
                    </div>
					<div class="form-group m-b-25 itemsd">
						<div class="row col-12 table-responsive" id="itemsdTable">
							<table class="table table-striped dataTable no-footer"
								cellspacing="0" width="100%">
								<thead id="sditemHeader">
								</thead>
								<tbody id="sditemTbody">
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button
						class="btn btn-info waves-effect waves-light fr btn_default delayedBtn"
						type="button" onclick="$m4002.submit();">确定</button>
					<button class="btn btn-light waves-effect waves-light btn_default"
						type="button" onclick="$m4002.cancel();">取消</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</div>

<script type="text/javascript">
    var $m4002={
        init:function(id){
        	
        	var url = serverAddr + "/purchaseSdOrder/getAmountAdjustmentDetail.asyn" ;
        	$custom.request.ajax(url, {"id": id}, function(result){
        		if(result.state == '200'){
        			
        			// 动态高度
                	var height = window.screen.height * 0.6 ;
                	
                	$("#4002-modal-body").css("height", height + "px") ;
                	$("#4002-modal-body").css("overflow-y", "scroll") ;
        			
        			$m4002.orderId = id ;
        			var data = result.data ; 
        			
        			var order = data.order ;
        			var itemList = data.itemList ;
        			$m4002.orderType = order.type ;
        			
        			$("#4002code").html(order.code) ;
        			$("#4002typeLabel").html(order.typeLabel) ;
        			$("#4002purchaseOrderCode").html(order.purchaseCode) ;
        			$("#4002poNo").html(order.poNo) ;
        			$("#4002currencyLabel").html(order.currencyLabel) ;
        			
        			if(order.inboundDate){
	        			$("#4002inboundDate").html(order.inboundDate) ;
        			}
        			
        			var headerHtml = "<tr>" ;
    				headerHtml += "<td>商品货号</td>" ;
    				headerHtml += "<td>商品名称</td>" ;
    				headerHtml += "<td>采购数量</td>" ;
    				headerHtml += "<td>采购单价</td>" ;
    				headerHtml += "<td>采购金额 </td>" ;
    				headerHtml += "<td>"+ order.sdTypeName +"金额 </td>" ;
    				headerHtml += "</tr>" ;
    				
    				$("#sditemHeader").html(headerHtml) ;
        			
        			if(itemList){
            				
           				var sdHtml = "" ;
           				
           				$(itemList).each(function(index, item){
           					sdHtml += "<tr>" ;
           					
           					var num = "" ;
           					if(item.num){
           						num = item.num ;
           					}
           					
           					var price = "" ;
           					if(item.price){
           						price = item.price ;
           					}
           					
           					var amount = "" ;
           					if(item.amount){
           						amount = item.amount ;
           					}
           					
           					sdHtml += "<td>"+ item.goodsNo + " <input type='hidden' name='itemId' value='"+ item.id + "'></td>";
           					sdHtml += "<td>"+ item.goodsName +"</td>";
           					sdHtml += "<td>"+ num +"</td>";
           					sdHtml += "<td>"+ price +"</td>";
           					sdHtml += "<td>"+ amount +"</td>";
           					sdHtml += "<td>"+
   							"<input type='text' class='sdAmount' name='sdAmount' value='" + item.sdAmount +
   							"' onkeyup='value=value.replace(/[^-\\\d^\\\.]/g,\"\")'  onblur='itemAmountChange(this)'>" + 
   							"</td>" ;
           					
           					sdHtml += "</tr>" ;
           					
           				}) ;
           				
           				sdHtml += "<tr>" ;
       					
           				sdHtml += "<td colspan='4'> 合计 </td>";
           				sdHtml += "<td class='totalAmount'>" + data.totalAmount + "</td>";
           				sdHtml += "<td class='totalSdAmount'>" + data.totalSdAmount + "</td>";
       					
           				sdHtml += "</tr>" ;
           				
           				$("#sditemTbody").html(sdHtml) ;
           				
        			}
        			
        			$m4002.show();
        			
                }else{
                	if(result.expMessage != null){
    					$custom.alert.error(result.expMessage);
    				}else{
    					$custom.alert.error(result.message);
    				}
                }
        	}) ;
        	
        },
        submit:function(){
        	var id = $("#id").val() ;
        	$m4002.add();
        },
        add:function(){
        	
        	var json = [];
        	var itemList = [] ;
        	var flag = true ;
        	
        	var itemTrs = $("#sditemTbody").find("tr") ;
        	$(itemTrs).each(function(index, tr){
        		debugger ;
        		if(index == itemTrs.length - 1){
        			return false ;
        		}
        		
        		var sdAmount = $(tr).find("input[name='sdAmount']").val() ; 
        		
        		if(!sdAmount){
        			$custom.alert.error("金额不能为空");
        			flag = false ;
                    
                    return false;
        		}
        		
        		if($m4002.orderType == '1' && parseFloat(sdAmount) < 0){
        			$custom.alert.error("单据类型是采购SD的金额不能为负数");
        			flag = false ;
                    
                    return false;
        		}
        		
        		if($m4002.orderType == '2' && parseFloat(sdAmount) < 0){
        			$custom.alert.error("采购退SD的金额不能为负数");
        			flag = false ;
                    
                    return false;
        		}
        		
        		var id = $(tr).find("input[name='itemId']").val() ; 
        		
        		var itemJson = {"id": id, "sdAmount": sdAmount} ;
        		
        		itemList.push(itemJson) ;
        	}) ;
        	
        	if(!flag){
        		return ;
        	}
        		
        	itemList=JSON.stringify(itemList);
        	
        	json.push({"name": "items", "value": itemList}) ;
        	json.push({"name": "id", "value": $m4002.orderId}) ;
        	
        	$custom.request.ajax(
                    $m4002.params.addUrl,
                    json,
                    $m4002.succFun
            );
        },
        show:function(){
             $($m4002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4002.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchaseSdOrder/saveAmountAdjustment.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
            orderId: '' ,
            orderType: ''
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4002.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
            	
            	$module.revertList.serializeListForm() ;
                $module.revertList.isMainList = true ;
            	
                $custom.alert.success(data.message);
                //成功隐藏
                $m4002.hide();
                //重新加载table表格
                setTimeout(function () {
                	$module.load.pageOrder("act=3010");
                }, 500);
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

    function itemAmountChange(org){
    	
    	var amount = $(org).val() ;
    	if(!amount){
    		amount = 0 ;
    	}
    	amount = parseFloat(amount);
    	
    	var parent = $(org).parent() ;
    	
    	var totalAmount = 0 ; 
    	$("input[name='sdAmount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    	}) ;
    	
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".totalSdAmount").text(totalAmount) ;
    }
    
    
</script>