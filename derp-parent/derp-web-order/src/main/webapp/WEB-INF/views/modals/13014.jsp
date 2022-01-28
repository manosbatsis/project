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
                            <h5 class="modal-title" id="myLargeModalLabel">结算作废申请</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt10">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="fr" style="padding: 0 20px;">平台结算单：<span id="zfBillCode" ></span> </div>
                                        <div class="fr" style="padding: 0 20px;">客户/平台：<span id="zfPlatformName" ></span> </div>
                                        <div class="fr" style="padding: 0 20px;">结算日期：<span id="zfBillDate" ></span> </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row mt10">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="fr" style="padding-left: 20px;"><i class="red">* </i>作废备注：</div>
                                        <textarea id="invalidRemark" rows="3" cols="60"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-4"></div>
                                        <div class="col-3">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick='$m13014.save();'>提交</button>
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13014.hide();'>取消</button>
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
    var $m13014={
        init:function(id, code, storePlatformName, billDate){
            $m13014.params.id = id;
            $("#zfBillCode").text(code);
            $("#zfPlatformName").text(storePlatformName);
            $("#zfBillDate").text(billDate);
            $($m13014.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13014.params.modalId).modal("hide");
        },
        save:function () {
            var invalidRemark = $("#invalidRemark").val();
            if (!invalidRemark) {
                $custom.alert.error("作废备注不能为空！");
                return false;
            }

            $custom.request.ajax($m13014.params.saveUrl, {"id": $m13014.params.id, "invalidRemark":invalidRemark} , function(res){
                if (res.state == '200') {
                    if (res.data.code == '00') {
                        $custom.alert.success("提交成功！");
                        $m13014.hide();
                        setTimeout(function () {
                            $module.revertList.serializeListForm() ;
                            $module.revertList.isMainList = true;
                            $module.load.pageOrder('act=14018');
                        }, 1000);
                    } else {
                        var message = res.data.message;
                        if (message != null && message != '' && message != undefined) {
                            $custom.alert.error(message);
                        }
                    }
                } else {
                    $custom.alert.error(res.message);
                    $m13014.hide();
                }
            });

        },
        params:{
            saveUrl: serverAddr+"/toCReceiveBill/saveInvalidBill.asyn",
            modalId:'#review-modal',
            id:null,
        }
    };

</script>