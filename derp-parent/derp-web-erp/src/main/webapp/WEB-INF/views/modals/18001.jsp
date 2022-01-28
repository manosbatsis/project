<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="prompt-modal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;left: 300px; top: 200px;">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content" style="width: 300px;text-align: center;">
                        <div class="modal-body">
                            <div class="form-row">
                                <div style="width: 300px; text-align: center; font-size: 18px;padding-bottom: 20px;">
                                    商品审核结果
                                </div>
                                <div style="width: 300px; text-align: center; font-size: 18px;padding-bottom: 20px;">
                                    <label><input type="radio" name="auditType" value="0" checked id="auditType0">通过</label>
                                    <label><input type="radio" name="auditType" value="1" id="auditType1">驳回</label>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-1"></div>
                                        <div class="col-10">
                                            <button type="button"
                                                    class="btn btn-info waves-effect waves-light btn_default btn-sm"
                                                    onclick='$m18001.save();'>确认
                                            </button>
                                            <button type="button"
                                                    class="btn btn-light waves-effect waves-light btn_default"
                                                    onclick='$m18001.hide();'>取消
                                            </button>
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
    var $m18001 = {
        init: function (ids) {
            $("#auditType0").val("0");
            $("#auditType1").val("1");
            $m18001.params.ids = ids;
            $($m18001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide: function () {
            $($m18001.params.modalId).modal("hide");
        },
        save: function () {
            var auditType = $('input[name="auditType"]:checked').val();

            var ids = new Array();
            $($m18001.params.ids).each(function (i, m) {
                ids.push($(m).val());
            });
            var idsStr = ids.join(",");
            var dataJson = {"ids":idsStr, "auditType":auditType} ;

            $custom.request.ajaxReqrCheck(
                "/supplierMerchandisePrice/auditSMPrice.asyn",
                dataJson,
                function (data) {
                    if (data.state == 200) {
                        $custom.alert.success("审核成功");
                        $($m18001.params.modalId).modal("hide");

                    } else {
                        if (!!data.expMessage) {
                            $custom.alert.error(data.expMessage);
                        } else {
                            $custom.alert.error(data.message);
                        }
                    }
                    //重新加载页面
                    searchData();
                },
                null,
                function() {
                    /*$module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;*/
                }
            );
        },
        params: {
            modalId: '#prompt-modal',
            ids: null
        }
    };

</script>