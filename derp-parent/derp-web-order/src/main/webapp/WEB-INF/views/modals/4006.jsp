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
            <div id="4006-add-signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:10px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 900px; margin-left: -120px;" >
                    	<div class="header" >
		                    <span class="header-title" >采购金额确认</span>
		                </div>
                        <div id="4006-modal-body" class="modal-body" >
                            <form class="form-horizontal" id="4006addForm">
                            	<input type="hidden" name="id" id="4006modalId">
                            	<div class="title_text">采购金额确认</div>
                                <div class="form-group m-b-25">
                                    <div class="col-12" style="line-height: 40px;">
                                        <i class="red" style="margin-left: 55px;">*</i> <label>采购币种 :</label>
	                                    <select name="currency" style="" class="input_msg" id="4006modalCurrency" disabled>
	                                       <option value="">请选择</option>
	                                    </select>
                                    </div>
                                </div>
                             </form>
                                <div class="form-group m-b-25">
                                    <div class="row col-12 table-responsive" id="4006itemTable" >
										<table class="table table-striped dataTable no-footer"
											cellspacing="0" width="100%" >
											<thead>
												<tr>
													<th>商品货号</th>
													<th>商品名称</th>
													<th>采购数量</th>
													<th>采购单价</th>
													<th>采购金额(不含税)</th>
													<th>采购金额(含税)</th>
												</tr>
											</thead>
											<tbody id="4006modelItemTbody" >
											</tbody>
										</table>
									</div>
                                </div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="$m4006.submit('2');">确定通过</button>
                            <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="$m4006.submit('1');">确定不通过</button>
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m4006.cancel();">取消</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m4006={
        init:function(id){
        	// 动态高度
        	var height = window.screen.height * 0.6 ;
        	
        	$("#4006-modal-body").css("height", height + "px") ;
        	$("#4006-modal-body").css("overflow-y", "scroll") ;
        	$module.currency.loadSelectData.call($("#4006modalCurrency"));
        	
        	var url = serverAddr + "/purchase/getAmountAdjustmentDetail.asyn" ;
        	$custom.request.ajax(url, {"id": id}, function(result){
        		if(result.state == '200'){
        			
        			$("#4006modalId").val(id) ;
        			
        			var data = result.data ;
        			
        			var order = data.order ;
        			
        			if(order.currency){
        				$("#4006modalCurrency").find("option").each(function(i, option){
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
        					
        					var taxAmount = "" ;
        					if(item.taxAmount){
        						taxAmount = item.taxAmount ;
        						taxAmount += parseFloat(item.taxAmount) ;
        					}
        					
        					html += "<td>"+ item.goodsNo +"</td>" ;
        					html += "<td>"+ item.goodsName +"</td>";
        					html += "<td>"+ item.num +"</td>";
        					html += "<td>"+ item.price +"</td>";
        					html += "<td>"+item.amount+"</td>";
        					html += "<td>"+taxAmount+"</td>";
        					
        					html += "</tr>" ;
        					
        					num += parseInt(item.num) ;
        					amount += parseFloat(item.amount) ;
        					
        				}) ;
        				
        				amount = parseFloat(amount).toFixed(2) ;
        				
        				html += "<tr>" ;
    					
    					html += "<td colspan='2'> 合计 </td>";
    					html += "<td>"+ num +"</td>";
    					html += "<td></td>";
    					html += "<td>"+amount+"</td>";
    					html += "<td>"+taxAmount+"</td>";
    					
    					html += "</tr>" ;
    					
    					$("#4006modelItemTbody").html(html) ;
    					
        			}
        			
        			$m4006.show();
        			
                }else{
                	if(result.expMessage != null){
    					$custom.alert.error(result.expMessage);
    				}else{
    					$custom.alert.error(result.message);
    				}
                }
        	}) ;
        	
        },
        submit:function(status){
        	var id = $("#4006modalId").val() ;
        	$m4006.add(id, status);
        },
        add:function(id, status){
        	
        	$custom.request.ajax(
                    $m4006.params.addUrl,
                    {"id": id, "amountConfirmStatus": status},
                    $m4006.succFun
            );
        },
        show:function(){
             $($m4006.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4006.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchase/saveConfirmAmountAdjustment.asyn?r='+Math.random(),
            formId:'#4006addForm',
            modalId:'#4006-add-signup-modal',
            topidealCode: '' 
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4006.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
            	
            	$module.revertList.serializeListForm() ;
                $module.revertList.isMainList = true ;
            	
                $custom.alert.success(data.message);
                //成功隐藏
                $m4006.hide();
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

    
</script>