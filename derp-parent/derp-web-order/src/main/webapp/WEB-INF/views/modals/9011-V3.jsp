<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
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
                                      <input type="hidden" class="form-control" id="id2" name="merchantId">
                                      <input type="hidden" class="form-control" id="id" name="depotId">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                      <input type="hidden" class="form-control" id="popupType" name="popupType" value="2">
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
                                  <select class="form-control" name="brandId">
                                     	<option value="">请选择</option>
                                     </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品货号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品条形码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">工厂源码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <input type="text" class="form-control" name="factoryNo">
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
			                                    <span></span>
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



	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	//储存选择过的商品id
	var unNeedIds = [];
    var $popupGoods={
            save:function(){
            	var customerId = $("#customerId").val() ;
            	var currency = $("#currency").val() ;
            	var unitName = $("#tallyingUnit").find("option:selected").text(); 
            	var unitId = $("#tallyingUnit").val(); 
            	if(salePriceManage && !customerId){
            		$custom.alert.error("该公司事业部已开启销售价格管理，请先选择客户");
            		
            		$($erpPopup.orderGoods.params.modalId).modal("hide");
            		
            		return ;
            	}
            	
            	if(salePriceManage && !currency){
                    $custom.alert.error("该公司事业部已开启销售价格管理，请先选择销售币种");
                    
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
    	            var flag =true;
    	            $.each(resultJson,function(index,event){
    	            	var form = {};
    	    			var jsonData = {};
    	    			jsonData['customerId'] = customerId;
    	    			jsonData['currency'] = currency;
    	    			jsonData['commbarcode'] = event.commbarcode;
    	    			if(!unitId) unitId="";
    	    			jsonData['unitId'] = unitId;
    	    			jsonData['goodId'] = event.id;
    	            	var jsonStr= JSON.stringify(jsonData);
    	        		form['json']=jsonStr;
    	            	var maxIndex = getGoodsMaxIndex();
	                    maxIndex = parseInt(maxIndex) + index;
	
    	                /*     	                	
    	                   以（公司+事业部）+（公司+客户）查询是否同时启用销售价格管理，若开启
    	                   1、检查海外仓理货单位是否填写，若填写且值不为件，查询是否存在箱件的换算，若存在则将销售价换算为理货单位单价，填入该商品的采购单价中，计算总价;
    	                   2、若海外仓理货单位未填写或已填写但值为件，则将报价直接填入该商品的销售单价中，计算总价 
    	                 */
    	                var price = event.filingPrice ;
    	            	if(salePriceManage){
    	            		$custom.request.ajaxSync(serverAddr +"/sale/getSalePrice.asyn", form, function(result){
    	            			if(result.state == 200){
    	            				if(result.data){
   	            						price = parseFloat(result.data).toFixed(8);
   	            					}else{
   	            						price = result.data;
   	            					}
    	                        }else{
    	                        	if(!!result.expMessage){
    	                                $custom.alert.error(result.expMessage);
    	                                flag=false;
    	                                return;
    	                            }else{
    	                                $custom.alert.error(result.message);
    	                                flag=false;
    	                                return;
    	                            }
    	                        }		                       
    	            		});
    	            	}
    	            	if(!flag){
    	            		return;
    	            	}
    	            	 var priceDeclareRatio = event.priceDeclareRatio;
                         if(!priceDeclareRatio) {
                             priceDeclareRatio = 1;
                         }
                        $tr+="<tr><td style='text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/></td>"+	
                        "<td style='text-align:center'><input class='good-seqs' type='text' style='width:70px;text-align:center;' name='seq' value='"+ maxIndex +"'/></td>"+	
                        "<td style='text-align:center'>"+(event.goodsCode==null?'':event.goodsCode)+"<input type='hidden' name='goodsCode' value='"+(event.goodsCode==null?'':event.goodsCode)+"'/></td>"+
                        "<td style='text-align:center'>"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                        "<td style='text-align:center'>"+(event.goodsNo==null?'':event.goodsNo)+"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/></td>"+	
                        "<td style='text-align:center'>"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/></td>"+
                        "<td style='text-align:center'><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='1'></td>"+
                        "<td style='text-align:center'><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+price+"'></td>"+
                        "<td style='text-align:center'><input type='text' name='declarePrice' class='goods-declarePrice' style='width:70px;text-align:center;' value='"+(event.filingPrice==null?0:event.filingPrice * priceDeclareRatio)+"'></td>"+
                        "<td style='text-align:center'><input type='text' name='amount' class='goods-amount' style='width:70px;text-align:center;' value='0'  onkeyup='numFormat(this,2)'></td>"+
                        "<td style='text-align:center'><input type='text' name='taxAmount'  class='goods-taxAmount' style='width:70px;text-align:center;' value='0'  onkeyup='numFormat(this,2)'></td>"+
                        "<td style='text-align:center'><input type='hidden' name='goodsTaxRate' value=''/><select name='taxRate' class='edit_input' style='width: 80px;' onchange='calculateTaxAmount(this)'></select></td>"+
                        "<td style='text-align:center'><input type='text' name='tax' value='' class='goods-tax' onkeyup='numFormat(this,2)'/></td>"+
                        "<td style='text-align:center'>"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='brandName' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
                        "<td><input type='hidden'  name='grossWeight-hidden' value='"+(event.grossWeight==null?'0':event.grossWeight)+"'><input type='text' style=\'width:60px;text-align:center;\'  class='form-control' id='goods-rough' name='grossWeight' value='"+(event.grossWeight==null?'0':parseFloat(event.grossWeight).toFixed(5))+"' onkeyup='numFormat(this,5)' onchange='calculateWeight1()' maxlength='11'></td>"+
                        "<td><input type='hidden'  name='netWeight-hidden' value='"+(event.netWeight==null?'0':event.netWeight)+"'><input type='text' style=\'width:60px;text-align:center;\'  class='form-control' id='goods-suttle' name='netWeight' value='"+(event.netWeight==null?'0':parseFloat(event.netWeight).toFixed(5))+"' onkeyup='numFormat(this,5)' maxlength='11'></td>"+
                        "<td style='text-align:center'><input type='text' name='boxNo' value=''/></td>"+
                        "<td style='text-align:center'><input type='text' name='itemContractNo' value=''/></td>"+
                        "<td style='text-align:center'><input type='text' name='itemRemark' value=''/></td>"+
                        "<td style='text-align:center'><input type='text' name='torrNo' value=''/></td>"+
                        "<td style='text-align:center'><input type='text' name='itemBoxNum' value='' onchange='calTotalBoxNum()'/></td>"+
                        "<td><textarea name='component' class='goods-component' cols='30' rows='3' align='center'>"+(event.component==null?'':event.component)+"</textarea></td>"+
                        "</tr>";
                    	unNeedIds.push(event.id);
    	            	  
                    });
					$("#unNeedIds").val(unNeedIds);
	     		    $("#table-list").append($tr); 
	     		    calculateWeight1();
	            	goods_change1();
	            	
	            	var selectObj = $("#tbody").find("tr").find("select[name='taxRate']");
	           	 	$(selectObj).each(function(){
	           	 		$(this).empty();
	           		 	$(this).append(getTaxRateSelectHtml());
	           	 	});
			 	
                }else{
                    $custom.alert.error(data.message);
                }                
			$($erpPopup.orderGoods.params.modalId).modal("hide");
        }
    }
    
    /**
     * 全选
     */
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
    //计算毛重之和
    function calculateWeight1() {
        var totalGrossWeight = 0;
        $("#table-list tbody tr").each(function (index, item) {
            var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//毛重
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
        $("#billWeight").val(totalGrossWeight.toFixed(5));
        $("#totalRough").text(totalGrossWeight.toFixed(5));
    }
    //计算净重之和
    function suttleWeight1() {
        var totalGrossWeight = 0;
        $("#table-list tbody tr").each(function (index, item) {
            var grossWeight = $(this).find("td").eq(15).children('input[name="netWeight"]').val();//净重
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
       // $("#billWeight").val(totalGrossWeight.toFixed(5));
        $("#totalSuttle").text(totalGrossWeight.toFixed(5));
    }

    function getGoodsMaxIndex() {
        var temp = 0;
        $("input[name='seq']").each(function(index, event){
            var val = $(event).val();
            console.log(val);
            if(val && parseInt(val) > parseInt(temp)){
                temp = val;
            }
        });
        return parseInt(temp) + 1;
    }

    function goods_change1() {
        var total = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(6) input').val();
            if (!ch) {
            	ch = 0;
            }
            total = parseFloat(total) + parseFloat(ch);
        })
        $("#totalNum").html(total);


        var total2 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(9) input').val();
            if (!ch) {
            	ch = 0;
            }
            total2 = parseFloat(total2) + parseFloat(ch);
        })
        $("#totalAmount").html(total2.toFixed(2));

        var total3 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();
            if (!ch) {
            	ch = 0;
            }
            total3 = parseFloat(total3) + parseFloat(ch);
        })
        $("#totalRough").html(total3.toFixed(2));

        var total4 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find("td").eq(15).children('input[name="netWeight"]').val();
            if (!ch) {
            	ch = 0;
            }
            total4 = parseFloat(total4) + parseFloat(ch);
        })
        $("#totalSuttle").html(total4.toFixed(5));
        
        var total5 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(10) input').val();
            if (!ch) {
            	ch = 0;
            }
            total5 = parseFloat(total5) + parseFloat(ch);
        })
        $("#totalTaxAmount").html(total5.toFixed(2));
       
        var total6 = 0;
        $("#tbody tr").each(function (i) {
            var ch = $(this).find('td:eq(12) input').val();
            if(!ch){
            	ch = 0;
            }
            total6 = parseFloat(total6) + parseFloat(ch);
        })
        $("#totalTax").html(total6.toFixed(2));

    }
    
</script>