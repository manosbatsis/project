<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .select2-container{border: 1px solid #dadada;}
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
	            $.each(resultJson,function(index,event){
	            	
	            	var maxIndex = getGoodsMaxIndex() ;
	            	maxIndex = parseInt(maxIndex) + index ;
	            	
                	$tr+="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'></td>"+
                	"<td><input type='text' name='seqs' class='goods-seqs' style='width:70px;text-align:center;' value='"+ maxIndex +"' onkeyup='value=value.replace(/[^\\d]/g,\"\")'></td>"+
                	"<td><input type='text' name='contractNos' class='goods-contractNos' style='width:70px;text-align:center;' value=''></td>"+
                    "<td>"+(event.goodsCode==null?'':event.goodsCode)+"</td>"+
                    "<td>"+(event.name==null?'':event.name)+"</td>"+
                    "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
                    "<td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='1'></td>"+
                    "<td><input type='hidden' name='purchasePrice' class='goods-purchase-price' style='width:70px;text-align:center;' value=''></td>"+
                    "<td><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(event.supplyMinPrice==null?0:event.supplyMinPrice)+"'></td>"+
                    "<td><input type='text' name='goods-amount' class='goods-amount' style='width:70px;text-align:center;' value='0'></td>"+
                    "<td><input type='text' name='brandName' class='brandName' value='境外品牌(其他)'  onMouseMove='this.title=this.value'></td>"+
                    "<td><input type='hidden' name='grossWeights-hidden' class='goods-grossWeight-hidden' value='"+(event.grossWeight==null?0:event.grossWeight)+"'><input type='text' name='grossWeights' class='goods-grossWeight' style='width:70px;text-align:center;' value='"+(event.grossWeight==null?0: event.grossWeight)+"' onkeyup='amountVal(this, \"5\")'></td>"+
                    "<td><input type='hidden' name='netWeight-hidden' class='goods-netWeight-hidden' value='"+(event.netWeight==null?0:event.netWeight)+"'><input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='"+(event.netWeight==null?0:event.netWeight)+"' onkeyup='amountVal(this, \"5\")'></td>"+
                    "<td><input type='text' name='boxNo' class='goods-boxNo' style='width:70px;text-align:center;' value=''></td>"+
                    "<td><input type='text' name='batchNo' class='goods-batchNo' style='width:70px;text-align:center;' value=''></td>"+
                    "<td><textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>"+(event.component==null?' ':event.component)+"</textarea></td>"+
                    "</tr>";
                	unNeedIds.push(event.id);
                });
	            $("#unNeedIds").val(unNeedIds);
	            $("#table-list").append($tr); 
	            
	            sumBillWeight() ;
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
	
	function getGoodsMaxIndex() {
		
		var temp = 0 ;
		
		$(".goods-seqs").each(function(index, element){
			var val = $(element).val() ;
			
			if(val && parseInt(val) > parseInt(temp)){
				temp = val ;
			}
		}) ;
		
		return parseInt(temp) + 1;
	}
</script>