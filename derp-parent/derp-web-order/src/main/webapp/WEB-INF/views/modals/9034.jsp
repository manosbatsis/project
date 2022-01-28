<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel"
         aria-hidden="true" style="display: none; overflow-y: auto;">
        <div class="modal-dialog  modal-lg" style="max-width:1200px  !important;">
            <div class="modal-content" style="max-width:1200px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">选择内部采购单</h5>
                </div>
                <div class="modal-body">
                    <form id="form2">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <label >客户 : </label>
                                    <select  class="edit_input"  name="customerId3" id="customerId3"style="width:200px;" parsley-trigger="change" required>
                                        <option value="">请选择</option>
                                    </select>
                                    <label >采购订单号 : </label>
                                    <input  type="text" class="input_msg" id="purchaseCode3" name="purchaseCode3"style="width: 400px;" >
                                    <label >PO号 : </label>
                                    <input placeholder="PO号" type="text" class="edit_input" name="poNoStr3" id="poNoStr3" style="width: 200px;">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$m9034.searchInfo();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="form-row mt20">
                        <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperMerchantGroup-checkable" class="group-checkable" />
                                    </label>
                                </th>
	                     	   <th>采购订单编号</th>
	                           <th>客户</th>
	                           <th>PO号</th>
	                           <th>事业部</th>
	                           <th>采购入库仓</th>
	                           <th>采购订单创建时间</th>
	                           <th>单据创建人</th>
	                           <th>销售出库仓</th>
                            </tr>
                            </thead>
                            <tbody id="data-tbody">
                            </tbody>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m9034.save()'>生成销售订单</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9034.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9034={
        init:function(data){
            //加载客户的下拉数据
            $('select[name="customerId3"]').empty();
            $module.merchantAll.loadSelectData.call($('select[name="customerId3"]')[0],"");
            $m9034.show(data);
        },
        show:function(data){
            $("#data-tbody").empty();
            $("input[name='keeperMerchantGroup-checkable']").prop("checked", false);
                var html = "" ;
                for (var i = 0; i < data.length; i++) {
                    var m = data[i];
                    var id = m.id;
                    var code = m.code;
                    var merchantName = m.merchantName;
                    var poNo = m.poNo;
                    var buName = m.buName;
                    var depotName = m.depotName;
                    var createDate = m.createDate;
                    var createName = m.createName;
                    html += '<tr><td class=" td-checkbox"><input type="checkbox"  name="item-checkbox"  value="' + id + '" class="iCheck" ' + '></td>'
                        + '<td><input type="hidden" name="code3" value="'+$m9034.strFormat(code)+'">' + $m9034.strFormat(code) + '</td>'
                        + '<td><input type="hidden" name="merchantName3" value="'+$m9034.strFormat(merchantName)+'">' + $m9034.strFormat(merchantName) + '</td>'
                        + '<td><input type="hidden" name="poNo" value="'+$m9034.strFormat(poNo)+'">' + $m9034.strFormat(poNo) + '</td>'
                        + '<td><input type="hidden" name="buName" value="'+$m9034.strFormat(buName)+'">' + $m9034.strFormat(buName) + '</td>'
                        + '<td><input type="hidden" name="depotName3" value="'+$m9034.strFormat(depotName)+'">' + $m9034.strFormat(depotName) + '</td>'
                        + '<td><input type="hidden" name="createDate3" value="'+$m9034.strFormat(createDate)+'">' + $m9034.strFormat(createDate) + '</td>'
                        + '<td><input type="hidden" name="createName3" value="'+$m9034.strFormat(createName)+'">' + $m9034.strFormat(createName) + '</td>'
                        + '<td><select class="edit_input" name="outDepotId3" id="outDepotId3" parsley-trigger="change" required></select></td>'
                        + '</tr>';
                }
                $("#datatable-detail").append(html);
            //加载仓库的下拉数据(卓志保税仓、中转仓、海外仓)
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId3']"),"", {"type":"a,c,d"});
            $($m9034.params.modalId).modal("show");
        },
        hide:function(){
            $($m9034.params.modalId).modal("hide");
        },
        searchInfo:function(){
            var customerId3=$("#customerId3").val();
            var purchaseCode3=$("#purchaseCode3").val();
            var poNoStr3=$("#poNoStr3").val();
            var saveJson = {};
            saveJson.customerId=customerId3;
            saveJson.purchaseOrderCodeStr=purchaseCode3;
            saveJson.poNoStr=poNoStr3;

            var jsonStr = JSON.stringify(saveJson);
            $custom.request.ajax(serverAddr+"/sale/showOwnPurchaseOrder.asyn",{"json":jsonStr},function(data1){
                if(data1.state==200){
                    $m9034.show(data1.data);
                }else{
                    if(!!data1.expMessage){
                        $custom.alert.error(data1.expMessage);
                    }else{
                        $custom.alert.error(data1.message);
                    }
                }
            });
        },
        save:function(){
            var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.error("请选择采购单！");
                return;
            }else if (checked.length>1) {
                $custom.alert.error("只能选择一条采购单！");
                return;
            }
            var saveJson = {};
            $("#data-tbody").find("tr").each(function(){
                if($(this).find('td').eq(0).find("input").is(':checked')){
                    var outDepotId3 = $(this).find('td').eq(8).find("select").val();// 出仓仓库
                    if(!outDepotId3){
                        $custom.alert.error("请选择销售出库仓！");
                        return false;
                    }
                    var purchaseId = $(this).find('td').eq(0).find("input").val(); //采购单id
                    // 根据采购订单商品标准条码查该公司主体该销售出库仓下对应的商品选品限制的对应商品
                    $custom.request.ajax(serverAddr+"/purchase/checkGoodsInfo.asyn",{"purchaseId":purchaseId,"outDepotId":outDepotId3},function(result){
                        if (result.state == 200) {
                            $custom.alert.warning(result.data,function(){
                                $load.a($module.params.loadOrderPageURL+"act=40012&purchaseId="+purchaseId+"&outDepotId="+outDepotId3);
                            });
                            $('.modal-backdrop').remove();//去掉遮罩层
                            $("#signup-modal").modal('hide');
                        } else {
                            $custom.alert.error(result.message);
                        }
                    });
                }
            });
        },
        strFormat:function (str) {
            if (!str) {
                return "";
            }
            return str;
        },
        params:{
            modalId:'#signup-modal'
        }
    };
    $("input[name='keeperMerchantGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-detail').prop("checked",$(this).prop("checked"));
            $('#datatable-detail tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-detail').prop("checked",false);
            $('#datatable-detail tbody tr').removeClass('success');
        }
    })
    $('#datatable-detail').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })


</script>