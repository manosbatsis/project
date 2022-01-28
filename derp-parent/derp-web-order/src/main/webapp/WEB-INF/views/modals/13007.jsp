<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="review-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none; top:150px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">应收作废申请</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt10">
                                <div class="col-12">
                                    <div class="row">
                                        <span style="color: blue">查询该发票号关联对应一下应收单，请确认是否提交应收作废：</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row mt20">
                                <table id="invoiceTable-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">
                                            <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                <input type="checkbox" name="keeperMerchantGroup-checkable" class="group-info-checked" />
                                            </label>
                                        </th>
                                        <th style="text-align: center;">应收单号</th>
                                        <th style="text-align: center;">事业部</th>
                                        <th style="text-align: center;">应收金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="invoiceData-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row mt10">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="fr"><i class="red">* </i>作废原因：</div>
                                        <textarea id="invalidRemark" rows="3" cols="60"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-5"></div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13007.save();'>提交</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
    var $m13007={
        init:function(id, invoiceId, code, buName, receiveAmount){
            $m13007.params.id = id;
            $("#invoiceData-tbody").empty();
            if (invoiceId) {
                //查询关联应收账单
                $custom.request.ajax($m13007.params.listReceiveBillUrl, {"id":invoiceId}, function(result){
                    if(result.state == 200) {
                        var list = result.data;
                        if(list){
                            var html = "" ;
                            $(list).each(function(i, m) {
                                if (m.billStatus != '06' || m.billStatus != '05') {
                                    var checkTh = '<td class="invoiceTd-checkbox"><input type="checkbox"  name="item-checkbox"  value="' + m.id + '" class="iCheck" ' + '></td>';
                                    if (m.id == id) {
                                        checkTh = '<td class="invoiceTd-checkbox"><input type="checkbox"  name="item-checkbox" checked value="' + m.id + '" class="iCheck" ' + '></td>';
                                    }
                                    html += '<tr>'
                                        + checkTh
                                        + '<td>' + m.code + '</td>'
                                        + '<td>' + m.buName + '</td>'
                                        + '<td>' + m.receivablePrice + '</td>'
                                        + '</tr>';
                                }
                            });
                            $("#invoiceTable-detail").append(html);
                        }
                    }
                });
            } else {
                var checkTh = '<td class="invoiceTd-checkbox"><input type="checkbox"  name="item-checkbox" checked value="' + id + '" class="iCheck" ' + '></td>';
                var html = '<tr>'
                    + checkTh
                    + '<td>' + code + '</td>'
                    + '<td>' + buName + '</td>'
                    + '<td>' + receiveAmount + '</td>'
                    + '</tr>';
                $("#invoiceTable-detail").append(html);
            }
            $($m13007.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13007.params.modalId).modal("hide");
        },
        save:function () {
            var checked = $(".invoiceTd-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.error("请选择作废应收账单！");
                return;
            }
            var invalidRemark = $("#invalidRemark").val();
            if (!invalidRemark) {
                $custom.alert.error("作废原因不能为空！");
                return false;
            }
            var rows = [] ;
            $(checked).each(function(i,m){
                var id = $(m).val() ;
                rows.push(id);
            });
            rows = rows.join(",") ;
            $custom.alert.warning("对勾选的账单进行确认操作\n" + "注确认后将不予修改调账，请知悉！",function(){
                var json = {"ids":rows, "invalidRemark":invalidRemark}
                $custom.request.ajax($m13007.params.saveUrl, json , function(res){
                    if (res.state == '200') {
                        if (res.data.code == '00') {
                            $custom.alert.success("提交成功！");
                            $m13007.hide();
                            setTimeout(function () {
                                $module.revertList.serializeListForm() ;
                                $module.revertList.isMainList = true;
                                $module.load.pageOrder('act=14001');
                            }, 1000);
                        } else {
                            var message = res.data.message;
                            if (message != null && message != '' && message != undefined) {
                                $custom.alert.error(message);
                            }
                        }
                    } else {
                        $custom.alert.error(res.expMessage);
                        $m13007.hide();
                    }
                });
            });

        },
        params:{
            listReceiveBillUrl:serverAddr + '/receiveBillInvoice/listReceiveBills.asyn?r='+Math.random(),
            saveUrl: serverAddr+"/receiveBill/saveInvalidBill.asyn",
            modalId:'#review-modal',
            id:null,
        }
    };

    $(".group-info-checked").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#invoiceTable-detail').prop("checked",$(this).prop("checked"));
            $('#invoiceTable-detail tbody tr').addClass('success');
        }else{
            $(":checkbox", '#invoiceTable-detail').prop("checked",false);
            $('#invoiceTable-detail tbody tr').removeClass('success');
        }
    }) ;
</script>