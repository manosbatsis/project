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
    var thisObj=null;//当前点击对象
	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	//储存选择过的商品id
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
                        var brandParentId = '';
                        $custom.request.ajaxSync($module.params.getBrandParentOrderURL,{"commbarcode":event.commbarcode},function(data){
                            var jsonData = data.data;
                            if (jsonData.superiorParentBrandId) {
                                brandParentId = data.data.superiorParentBrandId;
                            }
                        });

                        var td = '<td><input type="hidden"  class="form-control" name="goodsId" value="'+event.id+'">'+'<span>'+event.goodsNo+'</span>' +
                            '<button type="button" class="btn btn-add waves-effect waves-light" onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品</button>'+
                            '</td>';
                        $(thisObj).parent("td").next().replaceWith('<td><input name="goodsName" class="form-control" value="'+ event.name + '"></td>');
                        $(thisObj).parent("td").next().next().find("select").selectpicker('val', brandParentId);
                        $(thisObj).parent("td").parent("tr").find("td").eq(5).replaceWith(td);
                        // replaceWith('<td class="tc nowrap"><input type="hidden"  class="form-control" value="'+event.commbarcode+'">'+event.name+'</td>');
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
</script>