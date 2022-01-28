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
	<div id="4004-add-signup-modal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="custom-width-modalLabel"
		aria-hidden="true" style="display: none; top: 10px;">
		<div class="modal-dialog">
			<div class="modal-content" style="width: 900px; margin-left: -120px;">
				<div class="header">
					<span class="header-title">转销售订单</span>
				</div>
				<div id="4004-modal-body" class="modal-body">
					<form class="form-horizontal" id="4004Form">
						<div class="edit_box mt20">
							<input type="hidden" name="id" id="4004modalId">
							<div class="edit_item" style="min-width: 350px; max-width: 350px;">
								<label class="edit_label" style="margin-right: 5px;"><i class="red">*</i>客户 :</label> 
								<select
									name="customerId" style="" class="edit_input"
									id="4004customerId">
									<option value="">请选择</option>
								</select>
							</div>
							<div class="edit_item">
								<label class="edit_label" style="width: 41%;">
									<a title="销售单价=采购单价*（1+加价比例）" href="#" class="dripicons-information" style="color: #00a0e9;padding-left: 5px;margin-left:5px;"></a>
									<i class="red">*</i>加价比例 :
								</label>
								<input type="text" parsley-type="text" class="edit_input" id="4004rate" placeholder="请输入，最多5位小数">
							</div>
							<div class="edit_item"></div>
						</div>
					</form>
					<div class="form-group m-b-25">
						<div class="row col-12 table-responsive" id="4004itemTable">
							<table class="table table-striped dataTable no-footer"
								cellspacing="0" width="100%">
								<thead>
									<tr>
										<th>商品货号</th>
										<th>商品名称</th>
										<th>销售数量</th>
										<th><i class="red">*</i>销售单价</th>
										<th><i class="red">*</i>销售金额</th>
									</tr>
								</thead>
								<tbody id="4004modelItemTbody">
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button
						class="btn btn-info waves-effect waves-light fr btn_default delayedBtn"
						type="button" onclick="$m4004.submit();">确定</button>
					<button class="btn btn-light waves-effect waves-light btn_default"
						type="button" onclick="$m4004.cancel();">取消</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</div>
<script type="text/javascript">
    var $m4004={
        init:function(id){
        	// 动态高度
        	var height = window.screen.height * 0.6 ;
        	
        	$("#4004-modal-body").css("height", height + "px") ;
        	$("#4004-modal-body").css("overflow-y", "scroll") ;
        	//加载客户的下拉数据
        	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
        	
        	var url = serverAddr + "/purchase/getPurchaseOrderDetails.asyn" ;
        	$custom.request.ajax(url, {"id": id}, function(result){
        		if(result.state == '200'){
        			
        			$("#4004modalId").val(id) ;
        			
        			var order = result.data ;
        			
        			if(order.itemList){
        				var itemList = order.itemList ;
        				
        				var html = "" ;
        				
        				var num = 0 ;
        				var amount = 0 ;
        				
        				$(itemList).each(function(index, item){
        					html += "<tr>" ;
        					
        					html += "<td>"+ item.goodsNo +" <input type='hidden' name='itemId' value='"+ item.id +"'><input type='hidden' class='goodsNo' value='"+item.goodsNo+"'></td>" ;
        					html += "<td>"+ item.goodsName +"</td>";
        					html += "<td>"+ item.num +"<input type='hidden' name='num' value='"+ item.num +"'></td>";
        					html += "<td><input type='text' name='price' value='"+item.price+"' onkeyup='value=value.replace(/[^\\\d^\\\.]/g,\"\")'  onblur='saleItemPirceChange(this)'></td>";
        					html += "<td><input type='text' name='amount' value='"+item.amount+"' onkeyup='value=value.replace(/[^\\\d^\\\.]/g,\"\")'  onblur='saleItemAmountChange(this)'><input type='hidden' name='hidden-amount' value='"+item.amount+"' ></td>";
        					
        					html += "</tr>" ;
        					
        					num += parseInt(item.num) ;
        					amount += parseFloat(item.amount) ;
        				}) ;
        				
        				amount = parseFloat(amount).toFixed(2) ;
        				
        				html += "<tr>" ;
    					
    					html += "<td colspan='2'> 合计 </td>";
    					html += "<td>"+ num +"</td>";
    					html += "<td></td>";
    					html += "<td class='itemTotalAmount'>"+amount+"</td>";
    					
    					html += "</tr>" ;
    					
    					$("#4004modelItemTbody").html(html) ;
    					
        			}
        			
        			$m4004.show();
        			
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
        	$m4004.add();
        },
        add:function(){
        	
        	var flag = true ;
        	
        	if(!$("#4004customerId").val()){
        	    $custom.alert.error("请选择客户");
                
                return ;
        	}
        	
        	var json = $($m4004.params.formId).serializeArray();
        	var itemList = [] ;
        	
        	var itemTrs = $("#4004modelItemTbody").find("tr") ;
        	$(itemTrs).each(function(index, tr){
        		
        		if(index == itemTrs.length - 1){
        			return false ;
        		}
        		
        		var amount = $(tr).find("input[name='amount']").val() ; 
        		
        		if(!amount){
        			$custom.alert.error("请补全销售金额");
        			flag = false ;
                    
                    return false;
        		}
        		
        		var price = $(tr).find("input[name='price']").val() ; 
        		
        		if(!price){
        			$custom.alert.error("请补全销售单价");
        			flag = false ;
                    
                    return false;
        		}
        		
        		var id = $(tr).find("input[name='itemId']").val() ; 
        		
        		var itemJson = {"id": id, "amount": amount, "price": price} ;
        		
        		itemList.push(itemJson) ;
        	}) ;
        	
        	if(!flag){
        		return ;
        	}
        	
        	itemList=JSON.stringify(itemList);
        	
        	json.push({"name": "items", "value": itemList}) ;
        	
        	//提交前校验是否存在
        	$custom.request.ajax(
                    $m4004.params.checkUrl,
                    {"id": $("#4004modalId").val() },
                    function(data){
                    	if(data.state==200){
                        	
                    		var flag = data.data ;
                    		
                    		if(flag){
                    			$custom.alert.warning("已存在相同PO号的销售单，是否继续生成销售单？", function(){
                    				$custom.request.ajax(
                    	                    $m4004.params.addUrl,
                    	                    json,
                    	                    $m4004.succFun) ;
                    			}) ;
                    		}else{
                    			$custom.request.ajax(
                                        $m4004.params.addUrl,
                                        json,
                                        $m4004.succFun) ;
                    		}
                    		
                        }else{
                        	if(data.expMessage != null){
            					$custom.alert.error(data.expMessage);
            				}else{
            					$custom.alert.error(data.message);
            				}
                        }
                    }
            );
        	
        },
        show:function(){
             $($m4004.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4004.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchase/saveSaleOrder.asyn?r='+Math.random(),
            checkUrl: serverAddr + '/purchase/toSaleBeforeCheck.asyn?r='+Math.random(),		
            formId:'#4004Form',
            modalId:'#4004-add-signup-modal',
            topidealCode: '' 
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4004.hide();
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
	                $m4004.hide();
	                //重新加载table表格
	                setTimeout(function () {
	                	$module.load.pageOrder("act=40012&id=" + saleId);
	                }, 500);
    			}, function(dismiss){ 
    			    if(dismiss == 'cancel'){ 
    			    	$module.revertList.serializeListForm() ;
    	                $module.revertList.isMainList = true ;
    	            	
    	                //成功隐藏
    	                $m4004.hide();
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
    
    $("#4004rate").blur(function(){
    	var val = $(this).val() ;
    	
    	if(!val){
    		return ;
    	}
    	val = parseFloat(val) ;
    	
    	if(!(val >= 0 && val < 10)){
    		$custom.alert.error("请输入0.00000~9.99999之间的数字");
    		$(this).val(0) ;
            
            return false;
    	}
    	
    	if(String(val).indexOf(".") > -1){
    		let x = String(val).indexOf('.') ; //小数点的位置
    		let y = String(val).length - x; //小数的位数

    		if(y - x > 5){
    			$custom.alert.error("请输入5位小数内的数字");
        		$(this).val(0) ;
                
                return false;
    		}
    	}
    	
    	var totalAmount = 0 ; 
    	$("input[name='amount']").each(function(index, item){
    		
    		var amount = $(item).parent().find("input[name='hidden-amount']").val() ;
    		amount = parseFloat(amount) ;
    		amount = (1 + val) * amount ;
    		
    		var num = $(item).parent().parent().find("input[name='num']").val() ;
    		
    		var price = amount / num ;
        	price = price.toFixed(8) ;
        	
        	$(item).parent().parent().find("input[name='price']").val(price) ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    		amount = amount.toFixed(2) ;
    		
    		$(item).val(amount) ;
    		
    		
    	}) ;
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".itemTotalAmount").text(totalAmount) ;
    }) ;

    function saleItemAmountChange(org){
    	
    	var amount = $(org).val() ;
    	if(!amount){
    		amount = 0 ;
    	}
    	amount = parseFloat(amount);
    	
    	var parent = $(org).parent().parent() ;
    	
    	var num = parent.find("input[name='num']").val() ;
    	num = parseFloat(num) ;
    	
    	var price = amount / num ;
    	price = price.toFixed(8) ;
    	
    	parent.find("input[name='price']").val(price) ;
    	
    	var totalAmount = 0 ; 
    	$("input[name='amount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".itemTotalAmount").text(totalAmount) ;
    	
    }
    
	function saleItemPirceChange(org){
    	
    	var price = $(org).val() ;
    	if(!price){
    		price = 0 ;
    	}
    	price = parseFloat(price);
		price = price.toFixed(8) ;
    	
    	var parent = $(org).parent().parent() ;
    	$(org).val(price) ;
    	
    	
    	var num = parent.find("input[name='num']").val() ;
    	num = parseFloat(num) ;
    	
    	
    	var amount = 0 ;
    	amount = price * num ;
    	amount = amount.toFixed(2) ;
    	parent.find("input[name='amount']").val(amount) ;
    	
    	
    	var totalAmount = 0 ; 
    	$("input[name='amount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".itemTotalAmount").text(totalAmount) ;
    	
    }
    
</script>