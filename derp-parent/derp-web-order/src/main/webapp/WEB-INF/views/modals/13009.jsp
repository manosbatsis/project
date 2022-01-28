<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="prompt-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;left: 300px; top: 200px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 250px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" onclick="$m13009.hide()">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">提示</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row">
                               <div style="width: 250px; text-align: center; font-size: 18px;padding-bottom: 20px;">发票签章完成</div>
                            </div>
                            <div class="form-row">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-1"></div>
                                        <div class="col-10">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick='$m13009.hide();'>返回</button>
                                            <button type="button" class="btn btn-info waves-effect waves-light btn_default btn-sm"
                                                    id="ncSyn" style="display: none;">同步NC</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
            <jsp:include page="/WEB-INF/views/modals/13010.jsp" />
            <jsp:include page="/WEB-INF/views/modals/13013.jsp" />
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
    var $m13009={
        init:function(invoiceId, status, type){
            $m13009.params.id = invoiceId;
            $m13009.params.type = type;
            //若有至少一个已同步，则不予操作同步NC
            $custom.request.ajax(serverAddr + '/receiveBillInvoice/validateSynNC.asyn?r='+Math.random(), {"id":invoiceId}, function(result){
                if(result.state == 200 && result.data.code == '00') {
                    $("#ncSyn").css("display","inline-block");
                }
                $($m13009.params.modalId).modal({backdrop: 'static', keyboard: false});
            });

        },
        hide:function(){
            $($m13009.params.modalId).modal("hide");
            setTimeout(function () {
                $module.load.pageOrder('act=14010');
            }, 1000);
        },
        params:{
            modalId:'#prompt-modal',
            id:null,
            type:''
        }
    };

    $('#ncSyn').click(function () {
        $($m13009.params.modalId).modal("hide");
        if ($m13009.params.type == '0') {
            $m13010.init($m13009.params.id, '1');
        } else {
            $m13013.init($m13009.params.id);
        }

    });
</script>