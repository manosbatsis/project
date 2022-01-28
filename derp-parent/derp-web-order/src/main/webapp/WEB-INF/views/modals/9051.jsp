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
                    console.log(event);
                    $tr+="<tr><td ><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/></td>"+
                        "<td >"+(event.goodsNo==null?'':event.goodsNo)+"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/><input type='hidden' name='goodsCode' value='"+(event.goodsCode==null?'':event.goodsCode)+"'/></td>"+
                        "<td >"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                        "<td >"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/></td>"+
                        "<td ><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='1'></td>"+
                        "<td ><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(event.price==null?0:event.price)+"'></td>"+
                        "<td ><input type='text' name='amount' class='goods-amount' style='width:70px;text-align:center;' value='0' onkeyup='numFormat(this)'></td>"+
                        "<td >"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='brandName' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
                        "</tr>";
                    unNeedIds.push(event.id);
                });
                $("#unNeedIds").val(unNeedIds);
                $("#table-list").append($tr);

                //监听预售单数量的离开事件
                $("#table-list").on("blur",'.goods-num',function(){
                    var that = $(this);
                    //获取预售单数量
                    var num = that.val();
                    //判断是否数字
                    if(isNaN(num)){
                        $custom.alert.error("数量只能输入正整数");
                        return ;
                    }
                    var tr = that.parent().parent();
                    //获取当前总金额
                    var amount= tr.find("input[name='amount']").val();
                    //设置单价
                    var price = 0;
                    if(!!num && !!amount){
                        price = Number((amount/num).toFixed(8));
                    }
                    tr.find("input[name='price']").val(price);
                });
                //监听预售单总金额的离开事件
                $("#table-list").on("blur",'.goods-amount',function(){
                    var that = $(this);
                    //获取总金额
                    var amount= that.val();
                    //判断是否数字
                    if(isNaN(amount)){
                        $custom.alert.error("预售总金额请输入数字");
                        return ;
                    }
                    amount = parseFloat(amount) ;
                    amount = amount.toFixed(2) ;
                    $(this).val(amount) ;
                    var tr = that.parent().parent();
                    //获取预售数量
                    var num = tr.find("input[name='num']").val();
                    //设置单价
                    var price = 0;
                    if(!!num && !!amount){
                        price = Number((amount/num).toFixed(8));
                    }
                    tr.find("input[name='price']").val(price);
                });
                $("#table-list").on("blur",'.goods-price',function(){
                    var that = $(this);
                    //获取当前价格
                    var price= that.val();
                    //判断是否数字
                    if(isNaN(price)){
                        $custom.alert.error("预售单价请输入数字");
                        return ;
                    }
                    var indexOf = price.indexOf(".");
                	if (indexOf != -1) {
                        var count = (parseFloat(price)+"").length - 1 - indexOf;
                        if (count > 8) {
                        	$(this).val("");
                        	tr.find("input[name='amount']").val("");
                            $custom.alert.error("预售单价只能为8位小数");
                            return;
                        }
                	}
                    
                    var tr = that.parent().parent();
                    //获取采购数量
                    var num = tr.find("input[name='num']").val();
                    //设置总金额
                    var sum = 0;
                    if(!!num && !!price){
                        sum = Number((price*num).toFixed(2));
                    }
                    tr.find("input[name='amount']").val(sum);
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
    
    function numFormat(obj){
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        if (obj.value.indexOf(".") < 0 && obj.value != "") {//如果没有小数点，首位不能为类似于 01、02的金额
            obj.value = parseFloat(obj.value);
        }
    }
</script>