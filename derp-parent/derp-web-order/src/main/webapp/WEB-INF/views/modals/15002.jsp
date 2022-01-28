<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
        <div>
            <!-- Signup modal content -->
            <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;">
                <div class="modal-dialog modal-lg" style="margin:10% auto;">
                    <div class="modal-content col-9" style="margin:0 auto;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">新增NC收支费项</h5>
                        </div>
                        <div class="modal-body" >
                            <form id="addForm" class="form-horizontal">
                                <div class="fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> NC收支费项<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="name" name="name" value="" required="" placeholder="">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 收支费项编码<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="code" name="code" required="" placeholder="">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 科目编码<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="subCode" name="subCode" required="" placeholder="">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 科目名称<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="subName" name="subName" required="" placeholder="">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row m-t-50 fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <div class="col-5">
                                                <button type="button"
                                                        class="btn btn-info waves-effect waves-light fr btn_default"
                                                        onclick="$m15002.add();">保 存</button>
                                            </div>
                                            <div class="col-5">
                                                <button type="button"
                                                        class="btn btn-light waves-effect waves-light btn_default"
                                                        onclick="$m15002.hide();">取 消</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m15002={
        init:function(){
            this.show();
            //重置表单
            $($m15002.params.formId)[0].reset();
        },
        add:function(){
            //必填项校验
            if($("#name").val()==""){
                $custom.alert.error("NC收支费项不能为空！");
                return ;
            }
            if($("#code").val()==""){
                $custom.alert.error("收支费项编码不能为空！");
                return ;
            }
            if($("#subCode").val()==""){
                $custom.alert.error("科目编码不能为空！");
                return ;
            }
            if($("#subName").val()==""){
                $custom.alert.error("科目名称不能为空！");
                return ;
            }

            var param = $($m15002.params.formId).serialize();
                $custom.request.ajax(
                    $m15002.params.url,
                    param,
                    function(data){
                        if(data.state==200){
                            $custom.alert.success(data.message);
                            //成功隐藏
                            $m15002.hide();
                            //重新加载table表格
                            setTimeout(function () {
                                $mytable.fn.reload();
                            }, 1000);
                        }else{
                            if (data.expMessage) {
                                $custom.alert.error(data.expMessage);
                            } else {
                                $custom.alert.error(data.message);
                            }
                        }
                    });
            $mytable.fn.reload();
        },
        show:function(){
            $($m15002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m15002.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr+'/receivePaymentSubject/saveNcPay.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#signup-modal',
        },
    }

</script>