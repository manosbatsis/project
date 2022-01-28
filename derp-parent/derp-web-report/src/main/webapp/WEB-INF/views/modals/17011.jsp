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
                                      <input type="hidden" class="form-control" id="popupType" name="popupType" value="">
                                  	  <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
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
			                            <th>外仓统一码</th>
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
    <jsp:include page="/WEB-INF/views/modals/17012.jsp" />
<script type="text/javascript">

	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	//储存选择过的商品id
	var unNeedIds = [];
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
    	            $.each(resultJson,function(index,event){
    	            
    	            	//获取未关账的最早日期
    	            	var startDate = "";
    	            	$custom.request.ajax(serverAddr+"/settlementPrice/getMinMonthByStatus.asyn", {"barcode":event.barcode}, function(data){
    	            		if(data.state == '200'){
								 startDate = data.data;
							 }
    	            		
    	            		var $tr="<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/><input type='hidden' name='id'/></td>"+
        	            	"<td>"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/>"+
        	            	"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/><input type='hidden' name='unitId' value='"+(event.unit==null?'':event.unit)+"'/>"+
        	            	"<input type='hidden' name='unitName' value='"+(event.unitName==null?'':event.unitName)+"'/><input type='hidden' name='productId' value='"+(event.productId==null?'':event.productId)+"'/></td>"+
        	            	"<td>"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
        	            	"<td>"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='brandId' value='"+(event.brandId==null?'':event.brandId)+"'/><input type='hidden' name='brandName' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
        	            	"<td>"+(event.spec==null?'':event.spec)+"<input type='hidden' name='goodsSpec' value='"+(event.spec==null?'':event.spec)+"'/></td>"+
        	            	"<td><select class='goods-unit' style='width:70px;height:25px;' name='currency' id='currency'></select></td>"+
        	            	"<td><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='0'></td>"+
        	            	"<td><input type='text' class='date-input' id='effectiveDate' name='effectiveDate' value=''><input type='hidden' name='startDate' value='"+startDate+"'/></td>"+
                            "<td>"+(event.isGroup == '0'?'否':event.isGroup == '1'?'是':'')+
                            "<input type='hidden' name='isGroup' value='"+(event.isGroup==null?'':event.isGroup)+"'/></td>"+
                            "<td><a href='javascript:showGroup("+event.id+")' name='groupGoodsId'>"+(event.isGroup=='1'?'组合详情':'')+"</a><input type='hidden' id='goodsId' value='"+event.id+"'/></td>"+ 
                            "</tr>";
        	            	unNeedIds.push(event.id);
        	            	$("#unNeedIds").val(unNeedIds);
                            $("#table-list").append($tr); 
                            
                            $module.currency.loadSelectData.call($("select[name='currency']"));
                            
                            //日期控件初始化
                    		$(".date-input").datetimepicker({
                    	        language:  'zh-CN',
                    	        format: "yyyy-mm",
                    			autoclose: true,
                    			todayHighlight: true,
                    			startView: 3,
                    			minView: 3,
                    			startDate: startDate,
                    			initialDate:startDate
                    	    });
						 });
    	          
                    });
                }else{
                    $custom.alert.error(data.message);
                }
			$($erpPopup.orderGoods.params.modalId).modal("hide");
        }
    }
    // 展示所有组合商品详情
    function showGroup(goodsId){
    	
    	$custom.request.ajaxSync(serverAddr+"/settlementPrice/listAllGroupMerchandiseByGroupId.asyn",{goodsId:goodsId},function(data){
    		var groupList =data.data.groupList;
    		console.log(data.data.groupList);
        	$('#groupListDiv').html("");
        	for (var i = 0; i < groupList.length; i++) {
        		var goods=groupList[i];
        		
        		var groupListDivStr="<tr><td>"+goods.barcode+"</td>"+
    											  "<td>"+goods.goodsNo+"</td>"+
    											  "<td>"+goods.name+"</td>"+
    											  "<td>"+goods.goodsCode+"</td>"+
    											  "<td>"+goods.groupNum+"</td>"+
    											  "<td>"+goods.groupPrice+"</td></tr>";												  	    		
    			$('#groupListDiv').append(groupListDivStr);
    		}  
    	});      
    	$("#invoice-modal").modal("show");
    };
    
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

</script>