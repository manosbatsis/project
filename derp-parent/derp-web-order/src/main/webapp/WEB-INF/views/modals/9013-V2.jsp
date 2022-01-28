<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
                                       <input type="hidden" class="form-control" id="id" name="depotId">
                                       <input type="hidden" class="form-control" id="goodsId" name="id">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                      <input type="hidden" class="form-control" id="popupType" name="popupType" value="3">
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
            //选择商品后把商品信息读取过新增页面
            if(outOrInDepotId=='outDepotId'){//退出
                var inDepotId = $("#inDepotId").val();
                var outDepotId = $("#outDepotId").val();
                var resultJson = $erpPopup.merchandise.loadMerchandisesByIds(ids);
                var event = resultJson[0];
                //更新退出商品的货号单元格
                var td = '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+event.id+'"><span class="outGoodsNo">'+(event.goodsNo==null?'':event.goodsNo)+'</span>'+
                        '<button type="button" class="btn btn-add waves-effect waves-light"   onclick="initGoodsBefore(this, \'outDepotId\')">选择商品</button>'+
                      '</td>';
              $(thisObj).parent("td").eq(17).replaceWith('<td><input type="hidden"  class="form-control" value="'+event.commbarcode+'"></td>'); // 退出商品标准条码
              $(thisObj).parent("td").replaceWith(td); //要放最后面替换
            }else{//退入
                var outDepotId = $("#outDepotId").val();
                var tr = "";
                if(outDepotId) {
                    //根据调出仓库/调入仓库查询对应的调入商品
                    resultJson = $erpPopup.merchandise.getListByIdsAndTypeAndInOutDepot(ids, '3', outDepotId);
                    $.each(resultJson,function(index,event){

                        var maxIndex = getGoodsMaxIndex();
                        maxIndex = parseInt(maxIndex) + index ;

                        tr+='<tr>'+
                            '<td class="tc nowrap">'+
                            '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+
                            '</td>'+

                            '<td><input type="text" style="width: 80px;" class="good-seqs"  name="seq" value="'+maxIndex+'"></td>'+

                            '<td><input type="text" style="width: 80px;" class="form-control" name="poNo"></td>'+
                            '<td><input type="text" style="width: 80px;" class="form-control" name="poDate"></td>'+
                            '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.inGoodsId+'">'+(event.inGoodsNo==null?'':event.inGoodsNo)+
                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods"  onclick="initGoodsBefore(this, \'inDepotId\')">选择商品</button>'+
                            '</td>'+
                            '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+event.outGoodsId+'"><span class="outGoodsNo">'+(event.outGoodsNo==null?'':event.outGoodsNo)+'</span>'+
                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default"onclick="initGoodsBefore(this, \'outDepotId\')">选择商品</button>'+
                            '</td>'+
                            '<td><input type="hidden"  class="form-control" name="inGoodsName" value="'+event.inGoodsName+'">'+(event.inGoodsName==null?'':event.inGoodsName)+'</td>'+
                            '<td><input type="hidden"  class="form-control" name="barcode" value="'+event.barcode+'">'+(event.barcode==null?'':event.barcode)+'</td>'+
                            '<td><input type="text" class="form-control" name="price"  onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
                            '<td><input type="text" class="form-control" name="totalNum" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" readonly="readonly"></td>'+
                            '<td><input type="text" class="form-control" name="returnNum" onchange="setTotalNum(this);"  onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" value="0"></td>'+
                            '<td><input type="text" class="form-control" name="badGoodsNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" value="0"></td>'+
                            '<td><input type="text" style="width: 50px;" class="form-control" name="returnBatchNo"/></td>'+
                            '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" ></td>'+
                            '<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(event.grossWeight==null?'0':event.grossWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onchange="calculateWeight()" maxlength="11"></td>'+
                            '<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(event.netWeight==null?'0':event.netWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
                            '<td><input type="text" class="form-control" name="boxNo"></td>'+
                            '<td><input type="hidden"  class="form-control" value="'+event.inCommbarcode+'"></td>'+
                            '<td><input type="hidden"  class="form-control" value="'+event.outCommbarcode+'"></td>'+
                            '</tr>';
                    });
                }else{
                    resultJson = $erpPopup.merchandise.loadMerchandisesByIds(ids);
                    $.each(resultJson,function(index,event){

                        var maxIndex = getGoodsMaxIndex();
                        maxIndex = parseInt(maxIndex) + index ;

                        tr+='<tr>'+
                            '<td class="tc nowrap">'+
                            '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+
                            '</td>'+

                            '<td><input type="text" style="width: 80px;"  class="good-seqs" name="seq" value="'+maxIndex+'"></td>'+

                            '<td><input type="text" style="width: 80px;" class="form-control" name="poNo"></td>'+
                            '<td><input type="text" style="width: 80px;" class="form-control" name="poDate"></td>'+
                            '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.id+'">'+(event.goodsNo==null?'':event.goodsNo)+
                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods"  onclick="initGoodsBefore(this, \'inDepotId\')">选择商品</button>'+
                            '</td>'+
                            '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+event.id+'"><span class="outGoodsNo">'+(event.goodsNo==null?'':event.goodsNo)+'</span>'+
                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default"onclick="initGoodsBefore(this, \'outDepotId\')">选择商品</button>'+
                            '</td>'+
                            '<td><input type="hidden"  class="form-control" name="inGoodsName" value="'+event.name+'">'+(event.name==null?'':event.name)+'</td>'+
                            '<td><input type="hidden"  class="form-control" name="barcode" value="'+event.barcode+'">'+(event.barcode==null?'':event.barcode)+'</td>'+
                            '<td><input type="text" class="form-control" name="price"  onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
                            '<td><input type="text" class="form-control" name="totalNum" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" readonly="readonly"></td>'+
                            '<td><input type="text" class="form-control" name="returnNum" onchange="setTotalNum(this);"  onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" value="0"></td>'+
                            '<td><input type="text" class="form-control" name="badGoodsNum" onchange="setTotalNum(this);" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  maxlength="9" value="0"></td>'+
                            '<td><input type="text" style="width: 50px;" class="form-control" name="returnBatchNo"/></td>'+
                            '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" ></td>'+
                            '<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(event.grossWeight==null?'0':event.grossWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" onchange="calculateWeight()" maxlength="11"></td>'+
                            '<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(event.netWeight==null?'0':event.netWeight)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
                            '<td><input type="text" class="form-control" name="boxNo"></td>'+
                            '<td><input type="hidden"  class="form-control" value="'+event.commbarcode+'"></td>'+
                            '<td><input type="hidden"  class="form-control" value="'+event.commbarcode+'"></td>'+
                            '</tr>';
                    });
                }
            $(thisObj).parent("td").parent("tr").replaceWith(tr);
            }
            calculateWeight();
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
    //计算毛重之和
    function calculateWeight() {
        var totalGrossWeight = 0;
        $("#datatable-buttons tbody tr").each(function (index, item) {
            var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//毛重  yigai
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
        $("#billWeight").val(totalGrossWeight.toFixed(3));
    }

    function getGoodsMaxIndex() {
        var temp = 0 ;
        $("input[name='seq']").each(function(index, event){
            var val = $(event).val() ;
            if(val && parseInt(val) > parseInt(temp)){
                temp = val ;
            }
        }) ;
        return parseInt(temp) + 1;
    }



</script>