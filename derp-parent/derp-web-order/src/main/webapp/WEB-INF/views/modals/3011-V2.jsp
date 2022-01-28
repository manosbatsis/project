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
                                      <input type="hidden" class="form-control" id="popupType" name="popupType" value="4">
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
    var outOrInDepotId = null;//当前是调出/调入
    var thisObj=null;//当前点击对象
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
                if(outOrInDepotId=='outDepotId'){//调出
                    var inDepotId = $("#inDepotId").val();
                    var outDepotId = $("#outDepotId").val();
                    //根据调出仓库/调入仓库查询对应的调入商品
                    var resultJson = $erpPopup.merchandise.getListByIdsAndTypeAndInOutDepot(ids, '4', inDepotId);
                    var tr = "";
                    $.each(resultJson,function(index,event){
                        var priceDeclareRatio = event.priceDeclareRatio;
                        if(!priceDeclareRatio) {
                            priceDeclareRatio = 1;
                        }
                        tr+='<tr>'+
                               '<td class="tc nowrap">'+
                                   '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+
                               '</td>'+
                               '<td><input class="form-control" name="seq" ></td>'+
                               '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+event.outGoodsId+'">'+(event.outGoodsCode==null?'':event.outGoodsCode)+
                                    '<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'outDepotId\');">选择商品</button>'+
                               '</td>'+
                               '<td>'+(event.outGoodsNo==null?'':event.outGoodsNo)+'</td>'+
                               '<td><input type="hidden"  class="form-control" value="'+event.outCommbarcode+'">'+(event.outGoodsName==null?'':event.outGoodsName)+'</td>'+
                               '<td>'+
                                   '<select name="outUnit" class="edit_input" style="width: 60px;">'+
                                       '<option value="02">件</option>'+
                                       '<option value="00">托盘</option>'+
                                       '<option value="01">箱</option>'+
                                   '</select>'+
                               '</td>'+
                               '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>' +
                               '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.inGoodsId+'">'+(event.inGoodsCode==null?'':event.inGoodsCode)+
                                   '<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods" onclick="$erpPopup.orderGoods.init(\'4\',this,\'inDepotId\');">选择商品</button>'+
                               '</td>'+
                               '<td>'+(event.inGoodsNo==null?'':event.inGoodsNo)+'</td>'+
                               '<td><input type="hidden"  class="form-control" value="'+event.inCommbarcode+'">'+(event.inGoodsName==null?'':event.inGoodsName)+'</td>'+
                               '<td>'+
                                   '<select name="inUnit" class="edit_input" style="width: 60px;">'+
                                       '<option value="02">件</option>'+
                                       '<option value="00">托盘</option>'+
                                       '<option value="01">箱</option>'+
                                   '</select>'+
                               '</td>'+
                               '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="inTransferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9"></td>'+
                               '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" readonly="readonly"></td>'+
                               '<td><input type="text"  class="form-control" name="price" value="'+(event.outFilingPrice==null?'0':(event.outFilingPrice*priceDeclareRatio).toFixed(2))+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
                               '<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(event.grossWeight==null?'0':event.grossWeight)+'" onkeyup="amount5Val(this)" onchange="calculateWeight()"></td>'+
                               '<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(event.netWeight==null?'0':event.netWeight)+'" onchange="sumTotal()" onkeyup="amount5Val(this)" ></td>'+
                               '<td><input type="text"  class="form-control" name="contNo"></td>'+
                               '<td><input type="text"  class="form-control" name="bargainno"></td>'+
                               '<td><input type="text"  class="form-control" name="outBarcode" style="width:100px;"value="'+(event.outBarcode==null?'':event.outBarcode)+'"></td>' +
                               '<td><input type="text"  class="form-control" name="boxNum" value="'+(event.boxNum==null?'0':event.boxNum)+'"></td>' +
                               '<td><input type="text"  class="form-control" name="torrNo" value="'+(event.torrNo==null?'0':event.torrNo)+'"></td>' +
                               '</tr>';
                    });
                    $(thisObj).parent("td").parent("tr").replaceWith(tr);
                    sortSeq();
                }else{//调入
                    var resultJson = $erpPopup.merchandise.loadMerchandisesByIds(ids);
                    var event = resultJson[0];
                    //更新调入三个单元格
                    var td = '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.id+'">'+(event.goodsCode==null?'':event.goodsCode)+
                              '<button type="button" class="btn btn-add waves-effect waves-light" onclick="$erpPopup.orderGoods.init(\'4\',this,\'inDepotId\');">选择商品</button>'+
                            '</td>';
                    $(thisObj).parent("td").next().replaceWith('<td>'+(event.goodsNo==null?'':event.goodsNo)+'</td>');
                    $(thisObj).parent("td").next().next().replaceWith('<td><input type="hidden"  class="form-control" value="'+event.commbarcode+'">'+(event.name==null?'':event.name)+'</td>');
                    $(thisObj).parent("td").parent("tr").find("td").eq(12).replaceWith('<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" readonly="readonly"></td>');
                   // $(thisObj).parent("td").parent("tr").find("td").eq(14).replaceWith('<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(event.grossWeight==null?'0':event.grossWeight)+'" onchange="calculateWeight()" onkeyup="amountVal(this)"></td>');
                   // $(thisObj).parent("td").parent("tr").find("td").eq(15).replaceWith('<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(event.netWeight==null?'0':event.netWeight)+'" onchange="sumTotal()" onkeyup="amountVal(this)" ></td>');
                    $(thisObj).parent("td").replaceWith(td); //要放最后面替换
                }
                setUnit();//设置理货单位
                calculateWeight();
                sumTotal();
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
            var grossWeight = $(this).find("td").eq(14).children('input[name="grossWeight"]').val();//毛重
            if (!grossWeight) {
                grossWeight = 0;
            }
            totalGrossWeight += parseFloat(grossWeight);
        });
        $("#billWeight").val(totalGrossWeight.toFixed(5));
        $("#totalGrossWeight").html(totalGrossWeight.toFixed(5)) ;
    }

    /**
     *	汇总调出数量、调入数量、净重
     */
    function sumTotal() {
        var transferInTotalNum = 0 ;
        $("#item-table").find("input[name='inTransferNum']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseInt(val);
                transferInTotalNum = parseInt(transferInTotalNum) ;
                transferInTotalNum += val ;

                $("#transferInTotalNum").html(transferInTotalNum) ;
            }

        }) ;

        var transferOutTotalNum = 0.0 ;
        $("#item-table").find("input[name='transferNum']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseInt(val);
                transferOutTotalNum = parseInt(transferOutTotalNum) ;
                transferOutTotalNum += val ;

                $("#transferOutTotalNum").html(transferOutTotalNum) ;
            }

        }) ;

        var totalNetWeight = 0.0 ;
        $("#item-table").find("input[name='netWeight']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseFloat(val);
                totalNetWeight = parseFloat(totalNetWeight) ;
                totalNetWeight += val ;
                totalNetWeight = parseFloat(totalNetWeight).toFixed(5);

                $("#totalNetWeight").html(totalNetWeight) ;
            }

        }) ;

        var totalNetWeight = 0.0 ;
        $("#item-table").find("input[name='netWeight']").each(function(i, m){
            let val = $(m).val() ;

            if(val && isNumber(val)){
                val = parseFloat(val);
                totalNetWeight = parseFloat(totalNetWeight) ;
                totalNetWeight += val ;
                totalNetWeight = parseFloat(totalNetWeight).toFixed(5);

                $("#totalNetWeight").html(totalNetWeight) ;
            }

        }) ;
    }

    /**判断是否数字*/
    function isNumber(val){
        if(val === "" || val ==null){
            return false;
        }
        if(!isNaN(val)){
            //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
            //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
            return true;
        }else{
            return false;
        }

    }

    /**限制输入框不能超过2位小数*/
    function amountVal(org){
        var regStrs = [
            ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
            ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
            ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
        ];
        for(i=0; i<regStrs.length; i++){
            var reg = new RegExp(regStrs[i][0]);
            org.value = org.value.replace(reg, regStrs[i][1]);
        }
    }

    /**限制输入框不能超过5位小数*/
    function amount5Val(org){
        var regStrs = [
            ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
            ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
            ['^(\\d+\\.\\d{5}).+', '$1'] //禁止录入小数点后两位以上
        ];
        for(i=0; i<regStrs.length; i++){
            var reg = new RegExp(regStrs[i][0]);
            org.value = org.value.replace(reg, regStrs[i][1]);
        }
    }
</script>