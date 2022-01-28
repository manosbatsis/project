<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            <div id="add-signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:10px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 900px; margin-left: -120px;" >
                    	<div class="header" >
		                    <span class="header-title" >采购金额确认</span>
		                </div>
                        <div id="4001-modal-body" class="modal-body" >
                            <form class="form-horizontal" id="addForm">
                            	<input type="hidden" name="id" id="modalId">
                            	<div class="title_text">采购金额确认</div>
                                <div class="form-group m-b-25">
                                    <div class="col-12" style="line-height: 40px;">
                                        <i class="red" style="margin-left: 55px;">*</i> <label>采购币种 :</label>
	                                    <select name="currency" style="" class="input_msg" id="modalCurrency" >
	                                       <option value="">请选择</option>
	                                    </select>
                                    </div>
                                </div>
                             </form>
                                <div class="form-group m-b-25">
                                    <div class="row col-12 table-responsive" id="itemTable" >
										<table class="table table-striped dataTable no-footer"
											cellspacing="0" width="100%" >
											<thead>
												<tr>
													<th>商品货号</th>
													<th>商品名称</th>
													<th>采购数量</th>
													<th>采购单价</th>
													<th><i class="red">*</i>采购金额(不含税)</th>
													<th>采购金额(含税)</th>
												</tr>
											</thead>
											<tbody id="modelItemTbody" >
											</tbody>
										</table>
									</div>
                                </div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="$m4001.submit();">确定</button>
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m4001.cancel();">取消</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m4001={
        init:function(id){
        	// 动态高度
        	var height = window.screen.height * 0.6 ;
        	
        	$("#4001-modal-body").css("height", height + "px") ;
        	$("#4001-modal-body").css("overflow-y", "scroll") ;
        	$module.currency.loadSelectData.call($("#modalCurrency"));
        	
        	var url = serverAddr + "/purchase/getAmountAdjustmentDetail.asyn" ;
        	$custom.request.ajax(url, {"id": id}, function(result){
        		if(result.state == '200'){
        			
        			$("#modalId").val(id) ;
        			
        			var data = result.data ;
        			
        			var order = data.order ;
        			
        			if(order.currency){
        				$("#modalCurrency").find("option").each(function(i, option){
        					if($(option).val() == order.currency){
        						$(option).prop("selected","selected") ;
        						
        						return false ;
        					}
        				}) ;
        			}
        			
        			if(data.itemList){
        				var itemList = data.itemList ;
        				
        				var html = "" ;
        				
        				var num = 0 ;
        				var amount = 0 ;
        				var taxAmount = 0 ;
        				
        				$(itemList).each(function(index, item){
        					html += "<tr>" ;
        					
        					html += "<td>"+ item.goodsNo +" <input type='hidden' name='itemId' value='"+ item.id +"'><input type='hidden' class='goodsNo' value='"+item.goodsNo+"'></td>" ;
        					html += "<td>"+ item.goodsName +"</td>";
        					html += "<td>"+ item.num +"</td>";
        					html += "<td>"+ item.price +"</td>";
        					html += "<td><input type='hidden' class='original-amount' value='"+ item.amount +"'>"
        						+"<input type='text' name='amount' value='"+item.amount+"' onkeyup='value=value.replace(/[^\\\d^\\\.]/g,\"\")'  onblur='itemAmountChange(this)'></td>";
       						
        					var taxRate = item.taxRate;
        					var taxAmount = item.taxAmount;
        					
        					if(!taxRate){
        						taxRate = "" ;
        					}
        					
        					if(!taxAmount){
        						taxAmount = "" ;
        					}
        					
        					html += "<td><input type='hidden' class='taxRate' value='"+ taxRate +"'>"
       						+"<input type='text' name='taxAmount' value='"+taxAmount+"' onkeyup='value=value.replace(/[^\\\d^\\\.]/g,\"\")'  onblur='itemTaxAmountChange(this)'></td>";
        					
        					html += "</tr>" ;
        					
        					num += parseInt(item.num) ;
        					amount += parseFloat(item.amount) ;
        					
        					if(item.taxAmount){
	        					taxAmount += parseFloat(item.taxAmount) ;
        					}
        					
        				}) ;
        				
        				amount = parseFloat(amount).toFixed(2) ;
        				taxAmount = parseFloat(taxAmount).toFixed(2) ;
        				
        				html += "<tr>" ;
    					
    					html += "<td colspan='2'> 合计 </td>";
    					html += "<td>"+ num +"</td>";
    					html += "<td></td>";
    					html += "<td class='itemTotalAmount'>"+amount+"</td>";
    					html += "<td class='itemTotalTaxAmount'>"+taxAmount+"</td>";
    					
    					html += "</tr>" ;
    					
    					$("#modelItemTbody").html(html) ;
    					
        			}
        			
        			$m4001.show();
        			
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
        	$m4001.add();
        },
        add:function(){
        	
        	var flag = true ;
        	
        	if(!$("#modalCurrency").val()){
        	    $custom.alert.error("请选择采购币种");
                
                return ;
        	}
        	
        	var json = $($m4001.params.formId).serializeArray();
        	var itemList = [] ;
        	
        	var itemTrs = $("#modelItemTbody").find("tr") ;
        	$(itemTrs).each(function(index, tr){
        		
        		if(index == itemTrs.length - 1){
        			return false ;
        		}
        		
        		var amount = $(tr).find("input[name='amount']").val() ; 
        		
        		if(!amount){
        			$custom.alert.error("请补全采购金额(不含税)");
        			flag = false ;
                    
                    return false;
        		}
        		
        		var taxAmount = "" ;
        		var taxPrice = "" ;
        		
        		var num = $(tr).find("input[name='amount']").parent().prev().prev().text() ; 
        		
        		if($(tr).find("input[name='taxAmount']").val()){
        			taxAmount = $(tr).find("input[name='taxAmount']").val() ;
        			
        			taxPrice = parseFloat(taxAmount) / parseFloat(num);
        			taxPrice = parseFloat(taxPrice).toFixed(8) ;
        		}
        		
        		var id = $(tr).find("input[name='itemId']").val() ; 
        		var price = $(tr).find("input[name='amount']").parent().prev().text() ; 
        		
        		var itemJson = {"id": id, "amount": amount, "price": price, "taxAmount": taxAmount, "num": num, "taxPrice": taxPrice} ;
        		
        		itemList.push(itemJson) ;
        	}) ;
        	
        	if(!flag){
        		return ;
        	}
        	
        	itemList=JSON.stringify(itemList);
        	
        	json.push({"name": "items", "value": itemList}) ;
        	
        	$custom.request.ajax(
                    $m4001.params.addUrl,
                    json,
                    $m4001.succFun
            );
        },
        show:function(){
             $($m4001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4001.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchase/saveAmountAdjustment.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#add-signup-modal',
            topidealCode: '' 
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4001.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
            	
            	$module.revertList.serializeListForm() ;
                $module.revertList.isMainList = true ;
            	
                $custom.alert.success(data.message);
                //成功隐藏
                $m4001.hide();
                //重新加载table表格
                setTimeout(function () {
                	$module.load.pageOrder("act=3001");
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
    	
    	var goodsNo = parent.parent().find(".goodsNo").val() ;
    	var taxRate = parent.parent().find(".taxRate").val() ;
    	
    	if(taxRate){
    		var taxAmount = parseFloat(amount) * ( 1 + parseFloat(taxRate));
    		taxAmount = parseFloat(taxAmount).toFixed(2) ;
    		
    		parent.parent().find("input[name='taxAmount']").val(taxAmount) ;
    	}
    	
    	var num = parent.prev().prev().text() ;
    	num = parseFloat(num) ;
    	
    	var price = amount / num ;
    	price = price.toFixed(8) ;
    	
    	parent.prev().text(price) ;
    	
    	var totalAmount = 0 ; 
    	$("input[name='amount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".itemTotalAmount").text(totalAmount) ;
    	
    	var totalTaxAmount = 0 ; 
    	$("input[name='taxAmount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		if(!amount){
    			amount = 0 ;
    		}
    		
    		totalTaxAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalTaxAmount = parseFloat(totalTaxAmount).toFixed(2) ;
    	
    	$(".itemTotalTaxAmount").text(totalTaxAmount) ;
    }
    
    function itemTaxAmountChange(org){

    	var taxAmount = $(org).val() ;
    	if(!taxAmount){
    		taxAmount = 0 ;
    	}
    	taxAmount = parseFloat(taxAmount);
    	
    	var parent = $(org).parent() ;
    	
    	var goodsNo = parent.parent().find(".goodsNo").val() ;
    	var taxRate = parent.parent().find(".taxRate").val() ;
    	
    	if(taxRate){
    		var amount = parseFloat(taxAmount) / ( 1 + parseFloat(taxRate));
    		amount = parseFloat(amount).toFixed(2) ;
    		
    		parent.parent().find("input[name='amount']").val(amount) ;
    		
    		var num = parent.prev().prev().prev().text() ;
        	num = parseFloat(num) ;
        	
        	var price = amount / num ;
        	price = price.toFixed(8) ;
        	
        	parent.prev().prev().text(price) ;
    	}
    	
    	var totalAmount = 0 ; 
    	$("input[name='amount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		totalAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalAmount = parseFloat(totalAmount).toFixed(2) ;
    	
    	$(".itemTotalAmount").text(totalAmount) ;
    	
    	var totalTaxAmount = 0 ; 
    	$("input[name='taxAmount']").each(function(index, item){
    		
    		var amount = $(item).val() ;
    		
    		if(!amount){
    			amount = 0 ;
    		}
    		
    		totalTaxAmount += parseFloat(amount) ;
    		
    	}) ;
    	totalTaxAmount = parseFloat(totalTaxAmount).toFixed(2) ;
    	
    	$(".itemTotalTaxAmount").text(totalTaxAmount) ;
    }
    
</script>