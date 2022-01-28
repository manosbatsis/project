<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="confirm-modal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                            <h5 class="modal-title" id="myLargeModalLabel">请分配事业部</h5>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="confirm-from">
                                <!-- 自定义参数  -->
                                <div class="form-group">
                                    <div class="col-12">
                                        <label style="margin-left: 48px;">事业部: </label>
                                        <input class="form-control" id="id" name="id" type="hidden">
                                        <input class="form-control" id="modelDepotId" name="depotId" type="hidden">
                                        <select class="input_msg" id="modalBuId" name="buId">
                                            <option value="">请选择</option>
                                        </select> <a style="color:red">(必填)</a>
                                    </div>
                                </div>

                                <div class="form-row mt20">
                                    <div class="form-inline m0auto">
                                        <div class=form-group>
                                            <button type="button" class="btn btn-info waves-effect waves-light fr"
                                                    onclick='inDepotSave()'>确定
                                            </button>
                                            <button type="button" class="btn btn-light waves-effect waves-light ml15"
                                                    onclick='inDepotCancel()'>取消
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
    // 选择事业部
    function inDepotSave() {
        var id = $("#id").val();
        var buId = $('#modalBuId').val();
        var buName = $("#modalBuId").find("option:selected").text();
        if (!buId) {
            $custom.alert.error("请选择事业部！");
            inDepotCancel();
            return;
        }
        var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
        $(checked).each(function(i,m){
            var html = "<input type='hidden' value='" + buId + "'/>" + buName
            $(m).parent("td").parent("tr").find("td").eq(4).html(html);
            $(m).prop('checked',false);
            $("input[name='keeperMerchantGroup-checkable']").prop("checked", false);
        });
        inDepotCancel();
    }
    function inDepotCancel() {
        $('#confirm-modal').modal('hide');
        $("#id").val("");
        $("#depotId").val("");
    }
</script>