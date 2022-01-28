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
            <div id="4003-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top:10px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 900px; margin-left: -120px;" >
                    	<div class="header" >
		                    <span class="header-title" >生成采购SD确认</span>
		                </div>
                        <div id="4003-modal-body" class="modal-body" >
                            <form class="form-horizontal" id="sdForm">
                            	<input type="hidden" name="id" id="sdModalId">
                             </form>
                                <div class="title_text itemsd">生成采购SD金额</div>
                                <div class="form-group m-b-25 itemsd" >
                                    <div class="row col-12 table-responsive" id="itemsdTable" >
										<table class="table table-striped dataTable no-footer"
											cellspacing="0" width="100%" >
											<thead id="sditemHeader">
											</thead>
											<tbody id="sditemTbody" >
											</tbody>
										</table>
									</div>
                                </div>
                        </div>
                        <div class="modal-footer">
                            <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="$m4003.submit();">确定</button>
                            <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m4003.cancel();">取消</button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m4003={
        init:function(id){
        	// 动态高度
        	var height = window.screen.height * 0.6 ;
        	
        	$("#4003-modal-body").css("height", height + "px") ;
        	$("#4003-modal-body").css("overflow-y", "scroll") ;
        	
        	var url = serverAddr + "/purchase/getAmountAdjustmentDetail.asyn" ;
        	$custom.request.ajax(url, {"id": id}, function(result){
        		if(result.state == '200'){
        			
        			$("#sdModalId").val(id) ;
        			
        			var data = result.data ;
        			
           			if(data.header){
           				
           				var headerList = data.header ;
           				
           				var headerHtml = "<tr>" ;
           				headerHtml += "<td>商品货号</td>" ;
           				headerHtml += "<td>商品名称</td>" ;
           				
           				$(headerList).each(function(index, item){
           					headerHtml += "<td>"+ item.sdConfigName +"</td>" ;
           				}) ;
           				
           				headerHtml += "<td>采购SD总金额</td>" ;
           				headerHtml += "</tr>" ;
           				
           				$("#sditemHeader").html(headerHtml) ;
           				
           				var sdHtml = "" ;
           				var amountMap = data.amountMap ;
           				var totalAmount = amountMap['totalAmount'] ;
           				
           				$(data.itemList).each(function(index, item){
           					sdHtml += "<tr>" ;
           					
           					sdHtml += "<td style='width:80px;'>"+ item.goodsNo + " <input type='hidden' name='itemId' value='"+ item.id + "'></td>";
           					sdHtml += "<td style='width:150px;'>"+ item.goodsName +"</td>";
           					
           					$(headerList).each(function(index, headerItem){
           						
           						var type = headerItem.sdConfigName ;
           						
           						var key = type + "_" + item.goodsNo ;
           						var keyProproportion = type + "_" + item.goodsNo + "_proproportion";
           						
           						var amount = amountMap[key] ;
           						var proproportion = amountMap[keyProproportion] ;
           						
           						if(!amount){
           							amount = "" ;
           						}
           						
           						if(!proproportion){
           							proproportion = 0 ;
           						}
           						
           						sdHtml += "<td>"+
           							"<input type='text' class='" + type + " " + item.goodsNo + 
           							"' name='" + type + "-amount' style='width:80px;' value='" + amount +
           							"' onkeyup='value=value.replace(/[^\\\d^\\\.]/g,\"\")' onblur='sditemAmountChange(this)'>" +
           							"<input type='hidden' class='"+keyProproportion+"' value='"+ proproportion +"'>" + 
           							"</td>" ;
               				}) ;
           					
           					sdHtml += "<td class='sditemTotalAmount'>"+ amountMap[item.goodsNo] +"</td>";
           					
           					sdHtml += "</tr>" ;
           					
           				}) ;
           				
           				sdHtml += "<tr>" ;
       					
           				sdHtml += "<td colspan='2'> 合计 </td>";
           				$(headerList).each(function(index, headerItem){
           					
           					var type = headerItem.sdConfigName ;
           					
           					var configTotalAmount = 0 ;
           					
           					if(amountMap[type]){
           						configTotalAmount = amountMap[type] ;
           					}
           					
       						sdHtml += "<td class='"+type+"-amount'>"+configTotalAmount+"</td>" ;
           				}) ;
           				sdHtml += "<td class='totalAmount'>"+totalAmount+"</td>";
       					
           				sdHtml += "</tr>" ;
           				
           				$("#sditemTbody").html(sdHtml) ;
           				
           				$(".itemsd").show() ;
           				
           			}
        			
        			$m4003.show();
        			
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
        	$m4003.add();
        },
        add:function(){
        	
        	//所有SD格数量
        	var totalNum = 0 ;
        	//含值SD格数量
        	var valNum = 0 ;
        	
        	var json = $($m4003.params.formId).serializeArray();
        	
			var sditemList = [] ;
        	
			var sdItemTrs = $("#sditemTbody").find("tr") ;
        	$(sdItemTrs).each(function(index, tr){
        		
        		if(index == sdItemTrs.length - 1){
        			return false ;
        		}
        		
        		var id = $(tr).find("input[name='itemId']").val() ; 
        		var tds = $(tr).find("input[name$='-amount']") ;
        		
        		var itemJson = {} ;
        		
        		itemJson["id"] = id ;  
        		
        		$(tds).each(function(tempIndex, itemInput){
        			
        			totalNum += 1 ;
        			
        			var type = $(itemInput).attr("class") ;
        			type = type.split(" ")[0] ;
        			
        			var amount = $(itemInput).val() ;
        			
        			if(!amount){
                        return true;
        			}
        			
        			valNum += 1 ;
        			
        			if(parseFloat(amount) == 0){
        				return true;
        			}
        			
        			itemJson[type] = amount ;
        		}) ;
        		
        		
        		sditemList.push(itemJson) ;
        	}) ;
        	
        	if(valNum == 0){
        		$custom.alert.error("SD金额为空，不需要生成");
        		
        		return ;
        	}
        	
        	if(totalNum > valNum ){
        		$custom.alert.warning("存在SD金额为空的商品，是否继续生成", function(){
        			sditemList=JSON.stringify(sditemList);
                	
                	json.push({"name": "sdItems", "value": sditemList}) ;
                	
                	$custom.request.ajax(
                            $m4003.params.addUrl,
                            json,
                            $m4003.succFun
                    );
        		}) ;
        		
        	}else{
        		sditemList=JSON.stringify(sditemList);
            	
            	json.push({"name": "sdItems", "value": sditemList}) ;
            	
            	$custom.request.ajax(
                        $m4003.params.addUrl,
                        json,
                        $m4003.succFun
                );
        	}
        	
        	
        },
        show:function(){
             $($m4003.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
             $($m4003.params.modalId).modal("hide");
        },
        params:{
            addUrl: serverAddr + '/purchaseSdOrder/saveSdOrder.asyn?r='+Math.random(),
            formId:'#sdForm',
            modalId:'#4003-modal',
            topidealCode: '' 
        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m4003.hide();
            });
        },
        succFun:function(data){
            if(data.state==200){
            	
            	$module.revertList.serializeListForm() ;
                $module.revertList.isMainList = true ;
            	
                $custom.alert.success(data.message);
                //成功隐藏
                $m4003.hide();
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

    function sditemAmountChange(org){
    	
    	var amount = $(org).val() ;
    	if(!amount){
    		amount = 0 ;
    	}
		amount = parseFloat(amount) ;
    	
		var clz = $(org).attr('class');
		
		var clzArr = clz.split(" ") ;
		
		var type = clzArr[0] ;
		var goodsNo = clzArr[1] ;
		
		var goodsNoTotalAmount = 0 ;  
		$("." + goodsNo).each(function(i, m){
			var amount = $(m).val() ;
			
			if(!amount){
				amount = 0 ;
			}
			
			amount = parseFloat(amount) ;
			
			goodsNoTotalAmount += amount ;
		}) ;
		goodsNoTotalAmount = parseFloat(goodsNoTotalAmount).toFixed(2) ;
		
		$(org).parent().parent().find(".sditemTotalAmount").text(goodsNoTotalAmount) ;
		
		var typeTotalAmount = 0 ;  
		$("." + type).each(function(i, m){
			var amount = $(m).val() ;
			
			if(!amount){
				amount = 0 ;
			}
			
			amount = parseFloat(amount) ;
			
			typeTotalAmount += amount ;
		}) ;
		typeTotalAmount = parseFloat(typeTotalAmount).toFixed(2) ;
		
		$("#sditemTbody").find("." + type + "-amount").text(typeTotalAmount) ;
		
		var totalAmount = 0 ;  
		$(".sditemTotalAmount").each(function(i, m){
			var amount = $(m).text() ;
			
			if(!amount){
				amount = 0 ;
			}
			
			amount = parseFloat(amount) ;
			
			totalAmount += amount ;
		}) ;
		
		totalAmount = parseFloat(totalAmount).toFixed(2) ;
		$(".totalAmount").text(totalAmount) ;
    }
    
</script>