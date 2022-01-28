<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .select2-container{border: 1px solid #dadada;}
    .taxRate{height: 28px;  width: -webkit-fill-available;}
</style>
<div>
    <!-- Signup modal content -->
    <div id="popup-signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择商品</h5>
                </div>
                <div class="modal-body">
                	<form id="popup-goods-form">
                	<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">公司名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="merchantName">
                                      <input type="hidden" class="form-control" id="popupPurchaseDepotId" name="depotId">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                      <input type="hidden" class="form-control" id="popupType" name="popupType" value="1">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div> 
                      </div>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品品牌<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  <select class="form-control goods_select2" name="brandId" id="brandId">
                                     	<option value="">请选择</option>
                                     </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="goodsNo" id="goodsNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                      	<div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                  	<input type="text" class="form-control" name="barcode" id="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">工厂编码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="factoryNo" id="factoryNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$erpPopup.orderGoods.reloadTable();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="popup-datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span>全选</span>
			                                </label>
			                            </th>
			                            <th>公司名称</th>
			                            <th>商品名称</th>
			                            <th>商品货号</th>
			                            <th>品牌名称</th>
			                            <th>商品条形码</th>
			                            <th>工厂编码</th>
			                            <th>计量单位</th>
                                        <th>平台备案关区</th>
			                        </tr>
			                        </thead>
			                    </table>
			                    <div style="display: block;float: right;width: 100%;">
				                    <div class="page_txt" style="display: inline-block;float: left;line-height: 38px;"></div>
				                    <div class="pageUtils" style="display: inline-block;float: right;"></div>
				                </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$popupGoods.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$erpPopup.orderGoods.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
	if("${unNeedIds}"){
		$("#unNeedIds").val('${unNeedIds}');
	}
	
	var purchasePriceManage = null ;
	$("#buId").change(function(){
		
		purchasePriceManage = getPurchasePriceManage() ;
		
		if(!purchasePriceManage){
			$(".goods-price").removeAttr("readonly") ;
			$(".goods-amount").removeAttr("readonly") ;
			$(".taxAmount").removeAttr("readonly") ;
		}
	}) ;
	
	//重置按钮
	$("button[type='reset']").click(function(){
		$("#popup-goods-form").find(".goods_select2").each(function(){
	    	$(this).val("");
	    });
		$("#popup-goods-form").find(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
	});
	
	$module.brand.loadSelectData.call($("select[name='brandId']")[0], "");
	
	//sava方法
    var $popupGoods={
        save:function(){

        	var supplierId = $("#supplierId").val() ;
        	var currency = $("#currency").val() ;
        	var attributionDate = $("#attributionDate").val() ;
        	
        	if(purchasePriceManage && !supplierId){
        		$custom.alert.error("该公司事业部已开启采购价格管理，请先选择供应商");
        		
        		$($erpPopup.orderGoods.params.modalId).modal("hide");
        		
        		return ;
        	}
        	
        	if(purchasePriceManage && !currency){
                $custom.alert.error("该公司事业部已开启采购价格管理，请先选择币种");
                
                $($erpPopup.orderGoods.params.modalId).modal("hide");
                
                return ;
            }
        	
        	var checkIds = document.getElementsByName("checkId");
        	var ids = "";
        	
        	var i = 0;
        	for(k in checkIds){
                if(checkIds[k].checked){
                	if(i==0){
                		ids = checkIds[k].value;
                		i=1;
                	}else{
                		ids = ids + "," + checkIds[k].value;
                	}
                }
            }
        	
        	if(ids!=""){
        		var resultJson = $erpPopup.merchandise.loadMerchandisesByIds(ids);
	            var $tr = "";
	            var str = $("#unNeedIds").val();
	            var unNeedIds = str.split(",");
	            
	            var goodsFlag = true ;
            	var goodsNo = "" ;
            	
            	var unitChangeFlag = true ;
            	var unitMsg = "" ;
            	
            	var selectHtml = getTaxRateSelectHtml() ;
	            
	            $.each(resultJson,function(index,event){
	            	
	            	var jsonData = null ;
	            	if(attributionDate){
	    	        	
	    	        	if(attributionDate.indexOf(" ") < 0){
		    	        	attributionDate += " 00:00:00" ;
	    	        	}
	    	        	
	    	        	jsonData = {"customerId":supplierId, "currency": currency, "commbarcode": event.commbarcode, "effectiveDate": attributionDate}
	                }else{
	                	jsonData = {"customerId":supplierId, "currency": currency, "commbarcode": event.commbarcode}
	                }

	            	if(purchasePriceManage){
	            		$custom.request.ajaxSync("/supplierMerchandisePrice/getSMPriceByPurchaseOrder.asyn"
	            				, jsonData, function(result){
	                        
	            			var price = "" ;
	            			if(result.state == 200 && result.data &&result.data.supplyPrice ){
            					price = result.data.supplyPrice ;
            					
            					if('c' == depotType){
            						var tallyingUnit = $("#tallyingUnit").val() ;
            						
            						if(tallyingUnit == '00'){
            							
            							if(!result.data.torrToUnit){
            								
	            							unitChangeFlag &= false ;
	            							
	            							unitMsg = "货号：" + event.goodsNo + " 未找到托转件数据" ;
	            							
	            							return false ;
	            							
            							}else{
            								price = parseFloat(price) * result.data.torrToUnit ;
            							}
            							
            						}else if(tallyingUnit == '01'){
            							
        								if(!result.data.boxToUnit){
            							
											unitChangeFlag &= false ;
	            							
	            							unitMsg = "货号：" + event.goodsNo + " 未找到箱转件数据" ;
	            							
	            							return false ;
        								}else{
        									price = parseFloat(price) * result.data.boxToUnit ;
        								}
            						}
            						
            					}
            					
	                        }else{
	                        	goodsFlag &= false ;
	                        	goodsNo = event.goodsNo ;
	                        	
	                        	return false ;
	                        }
	            			
	            			$tr+="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'></td>"+
	                        "<td><input type='text' name='contractNo' class='goods-contractNo' style='width:70px;text-align:center;' value=' '></td>"+
	                        "<td>"+(event.name==null?'':event.name)+"</td>"+ 
	                        "<td><input type='hidden' name='barcode' value='"+(event.barcode==null?' ':event.barcode)+"'>"+(event.barcode==null?'':event.barcode)+"</td>"+
	                        "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
	                        "<td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='0' onkeyup='value=value.replace(/[^\d^\.]/g,'')'></td>"+
	                        "<td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='"+ price +"' readonly></td>"+
	                        "<td><input type='text' name='goods-amount' class='goods-amount' style='width:100px;text-align:center;' value='0' readonly></td>"+
	                        "<td><input type='hidden' name='taxPrice' class='taxPrice' ><input type='text' name='taxAmount' class='taxAmount' style='width:100px;text-align:center;' value='0' readonly></td>"+
	                        "<td><select name='taxRate' class='taxRate' onchange='taxRateChange(this) ;'>" + selectHtml + "</select></td>"+
	                        "<td><input type='text' name='tax' class='tax' style='width:100px;text-align:center;' value='0' readonly></td>"+
	                        "<td><input type='text' name='brandName' class='brandName' value='境外品牌(其他)' style='text-align:center;'  onMouseMove='this.title=this.value'></td>"+
	                        "<td><input type='hidden' name='grossWeights-hidden' class='goods-grossWeight-hidden' value='"+(event.grossWeight==null?0: event.grossWeight )+"'><input type='text' name='grossWeights' class='goods-grossWeight' style='width:70px;text-align:center;' value='"+(event.grossWeight==null?0: event.grossWeight )+"' onkeyup='amountVal(this, \"5\")'></td>"+
	                        "<td><input type='hidden' name='netWeight-hidden' class='goods-netWeight-hidden' value='"+(event.netWeight==null?0:event.netWeight)+"'><input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='"+(event.netWeight==null?0:event.netWeight)+"' onkeyup='amountVal(this, \"5\")'></td>"+
	                        "<td><input type='text' name='boxNo' class='goods-boxNo' style='width:70px;text-align:center;' value=' '></td>"+
	                        "<td><input type='text' name='batchNo' class='goods-batchNo' style='width:70px;text-align:center;' value=' '></td>"+
	                        "<td><textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>"+(event.component==null?' ':event.component)+"</textarea></td>"+
	                        "<td><textarea name='goodsRemark' class='goods-remark' cols='30' rows='3' align='center'> </textarea></td>"+
	                        "</tr>";
	                        
	            			unNeedIds.push(event.id);
	                    });
	            	}else{
	            		$tr+="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'></td>"+
                        "<td><input type='text' name='contractNo' class='goods-contractNo' style='width:70px;text-align:center;' value=' '></td>"+
                        "<td>"+(event.name==null?'':event.name)+"</td>"+ 
                        "<td><input type='hidden' name='barcode' value='"+(event.barcode==null?' ':event.barcode)+"'>"+(event.barcode==null?'':event.barcode)+"</td>"+
                        "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
                        "<td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='1' onkeyup='value=value.replace(/[^\d^\.]/g,'')'></td>"+
                        "<td><input type='text' name='price' class='goods-price' style='width:100px;text-align:center;' value='"+(event.supplyMinPrice==null?0:event.supplyMinPrice)+"' ></td>"+
                        "<td><input type='text' name='goods-amount' class='goods-amount' style='width:100px;text-align:center;' value='0'></td>"+
                        "<td><input type='hidden' name='taxPrice' class='taxPrice' ><input type='text' name='taxAmount' class='taxAmount' style='width:100px;text-align:center;' value='0' ></td>"+
                        "<td><select name='taxRate' class='taxRate' onchange='taxRateChange(this) ;'>" + selectHtml + "</select></td>"+
                        "<td><input type='text' name='tax' class='tax' style='width:100px;text-align:center;' value='0' readonly></td>"+
                        "<td><input type='text' name='brandName' class='brandName' value='境外品牌(其他)' style='text-align:center;'  onMouseMove='this.title=this.value'></td>"+
                        "<td><input type='hidden' name='grossWeights-hidden' class='goods-grossWeight-hidden' value='"+(event.grossWeight==null?0:event.grossWeight)+"'><input type='text' name='grossWeights' class='goods-grossWeight' style='width:70px;text-align:center;' value='"+(event.grossWeight==null?0: event.grossWeight)+"' onkeyup='amountVal(this, \"5\")'></td>"+
                        "<td><input type='hidden' name='netWeight-hidden' class='goods-netWeight-hidden' value='"+(event.netWeight==null?0:event.netWeight)+"'><input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='"+(event.netWeight==null?0:event.netWeight)+"' onkeyup='amountVal(this, \"5\")'></td>"+
                        "<td><input type='text' name='boxNo' class='goods-boxNo' style='width:70px;text-align:center;' value=' '></td>"+
                        "<td><input type='text' name='batchNo' class='goods-batchNo' style='width:70px;text-align:center;' value=' '></td>"+
                        "<td><textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>"+(event.component==null?' ':event.component)+"</textarea></td>"+
                        "<td><textarea name='goodsRemark' class='goods-remark' cols='30' rows='3' align='center'> </textarea></td>"+
                        "</tr>";
                        
	            		unNeedIds.push(event.id);
	            	}
	            	
	               
	            });
	            
	            if(!unitChangeFlag){
	            	$custom.alert.error(unitMsg);
	            }
	            
	            if(goodsFlag){
		            $("#unNeedIds").val(unNeedIds);
		            $("#table-list").append($tr); 
		            sumTotal() ;
	            }else{
	            	$custom.alert.error("该公司事业部已开启采购价格管理，货号：" + goodsNo + " 未找到供应商商品价格");
	            }
	            
        	}
        	$($erpPopup.orderGoods.params.modalId).modal("hide");
        },
    }
    
    //全选
    $('#popup-datatable-buttons').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#popup-datatable-buttons').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#popup-datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#popup-datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
	
	function getPurchasePriceManage(){
		var buId = $("#buId").val() ;
		var supplierId = $("#supplierId").val() ;
		var purchasePriceManage ; 
		
		$custom.request.ajaxSync(serverAddr + "/purchase/getPurchasePriceManage.asyn", {"buId":buId, "supplierId":supplierId}, function(result){
			if(result.state == 200 && result.data ){
				purchasePriceManage = true ;
			}else{
				purchasePriceManage = false ;
			}
		});
		
		return purchasePriceManage ;
	}
	
	function chooseGoods(){
		var depotId = $("#depotId").val() ;
		
		if(!depotId){
			$custom.alert.error("请先选择仓库");
			
			return ;
		}
		
		var buId = $("#buId").val() ;
		
		if(!buId){
			$custom.alert.error("请先选择事业部");
			
			return ;
		}
		
		var supplierId = $("#supplierId").val() ;
		
		if(!supplierId){
			$custom.alert.error("请先选择供应商");
			
			return ;
		}
		
		purchasePriceManage = getPurchasePriceManage() ;
		
		if(purchasePriceManage && 'c' == depotType){
			var tallyingUnit = $("#tallyingUnit").val() ;
			
			if(!tallyingUnit){
				$custom.alert.error("仓库为海外仓时，请先选择【海外仓理货单位】");
				
				return ;
			}
			
		}
		
		$erpPopup.orderGoods.init(1, null, null);
	}
	
	function getTaxRateSelectHtml(){
		
		/**根据供应商带出对应税率*/
		var supplierId = $("#supplierId").val() ;
		var getRateurl = "/webapi/system/customer/getRateByCustomerAndMerchant.asyn" ;
		
		var taxRateId = "" ;
		$custom.request.ajaxSync(getRateurl, {"customerId": supplierId}, function(result){

			if(result.code == '10000'
					&& result.data){
				
				taxRateId = result.data.rateId ;
				
			}
		});
		
		var url = "/webapi/system/tariffRate/getSelectBean.asyn" ;
		
		var html = "<option value=''>请选择</option>" ;
		$custom.request.ajaxSync(url, {}, function(result){

			if(result.code == '10000'
					&& result.data){
				
				var list = result.data ;
				
				$(list).each(function(index, item){
					
					var selected = "" ;
					if(taxRateId
							&& taxRateId == item.value){
						selected = "selected" ;
					}
					
					html += "<option value='"+item.value+"' "+ selected +" >"+item.label+"</option>" ;
				}) ;
			}
		});
		
		return html ;
	}
	
	function taxRateChange(org){

		var that = $(org) ;
		
		var taxRate = $(org).val() ;
		
		var sum = that.parent().parent().find(".goods-amount").val() ;
		var num = that.parent().parent().find(".goods-num").val() ;
		
		if(!sum || isNaN(sum)){
			sum = 0 ;
		}
		
		if(!num || isNaN(num)){
			return ;
		}
		
		if(taxRate){
			
			taxRate = $(org).find("option:selected").text() ;
			
			var taxAmount = parseFloat(sum) * (1 + parseFloat(taxRate))  ;
    		that.parent().parent().find(".taxAmount").val(parseFloat(taxAmount).toFixed(2)) ;
    		
    		var taxPrice = parseFloat(taxAmount) / parseInt(num)  ;
    		that.parent().parent().find(".taxPrice").val(parseFloat(taxPrice).toFixed(8)) ;
    		
    		var tax = parseFloat(taxAmount) - parseFloat(sum) ;
    		that.parent().parent().find(".tax").val(parseFloat(tax).toFixed(2)) ;
		}
		
		sumTotal() ;
	}
	
	/**初始化税率下拉框*/
	function initTaxRate(){
		
		var url = "/webapi/system/tariffRate/getSelectBean.asyn" ;
		
		$custom.request.ajax(url, {}, function(result){
			
			if(result.code == '10000'
					&& result.data){
				
				var list = result.data ;
				var html = "<option value=''>请选择</option>" ;
				
				$(list).each(function(index, item){
					html += "<option value='"+item.value+"'>"+item.label+"</option>" ;
				}) ;
				
				
				$(".taxRate").each(function(index, select){
					
					$(select).html(html) ;
					
					var taxRateHidden = $(select).parent().find(".taxRateHidden").val() ;
					
					if(taxRateHidden){
						
						$(select).find("option").each(function(tempIndex, option){
							
							if($(option).text() == taxRateHidden){
								$(option).prop("selected", "selected") ;
								
								return false ;
							}
							
						}) ;
						
					}
					
				}) ;
				
			}
			
			
		}) ;
		
	}
	
	/**
  	*	汇总净重,总毛重，总数，总金额
  	*/
  	function sumTotal() {
  		
  		var totalGrossWeights = 0.0 ;
  		$("#grossWeight").val(totalGrossWeights) ;
  		$("#table-list").find("input[name='grossWeights']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseFloat(val);
    			totalGrossWeights = parseFloat(totalGrossWeights) ;
    			totalGrossWeights += val ;
    			totalGrossWeights = parseFloat(totalGrossWeights).toFixed(5);
    			
    			$("#grossWeight").val(totalGrossWeights) ;
    			$("#totalGrossWright").html(totalGrossWeights) ;
    		}
    		
    	}) ;
  		
  		var totalNum = 0 ;
  		$("#table-list").find("input[name='num']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseInt(val);
    			totalNum = parseInt(totalNum) ;
    			totalNum += val ;
    			
    			$("#totalNum").html(totalNum) ;
    		}
    		
    	}) ;
  		
  		var totalAmount = 0.0 ;
  		$("#table-list").find("input[name='goods-amount']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseFloat(val);
    			totalAmount = parseFloat(totalAmount) ;
    			totalAmount += val ;
    			totalAmount = parseFloat(totalAmount).toFixed(2);
    			
    			$("#totalAmount").html(totalAmount) ;
    		}
    		
    	}) ;
  		
  		var totalTaxAmount = 0.0 ;
  		$("#table-list").find("input[name='taxAmount']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseFloat(val);
    			totalTaxAmount = parseFloat(totalTaxAmount) ;
    			totalTaxAmount += val ;
    			totalTaxAmount = parseFloat(totalTaxAmount).toFixed(2);
    			
    			$("#totalTaxAmount").html(totalTaxAmount) ;
    		}
    		
    	}) ;
  		
  		var totalTax = 0.0 ;
  		$("#table-list").find("input[name='tax']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseFloat(val);
    			totalTax = parseFloat(totalTax) ;
    			totalTax += val ;
    			totalTax = parseFloat(totalTax).toFixed(2);
    			
    			$("#totalTax").html(totalTax) ;
    		}
    		
    	}) ;
  		
  		var totalNetWright = 0.0 ;
  		$("#table-list").find("input[name='netWeight']").each(function(i, m){
    		let val = $(m).val() ;
    		
    		if(val && isNumber(val)){
    			val = parseFloat(val);
    			totalNetWright = parseFloat(totalNetWright) ;
    			totalNetWright += val ;
    			totalNetWright = parseFloat(totalNetWright).toFixed(5);
    			
    			$("#totalNetWright").html(totalNetWright) ;
    		}
    		
    	}) ;
  	}
</script>