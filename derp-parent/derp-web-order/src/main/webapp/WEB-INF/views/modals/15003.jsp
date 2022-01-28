<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
        <div>
            <!-- Signup modal content -->
            <div id="signup-modal2" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;">
                <div class="modal-dialog modal-lg" style="margin:10% auto;">
                    <div class="modal-content col-9" style="margin:0 auto;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">编辑NC收支费项</h5>
                        </div>
                        <div class="modal-body" >
                            <form id="editForm" class="form-horizontal">
                                <input class="form-control" type="hidden" id="id2" name="id" value="" required="" placeholder="">
                                <div class="fr col-10">
                                    <div class="col-10">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> NC收支费项<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="name2" name="name" value="" required="" placeholder="">
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
                                                <input class="form-control" type="text" id="code2" name="code" required="" placeholder="">
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
                                                <input class="form-control" type="text" id="subCode2" name="subCode" required="" placeholder="">
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
                                                <input class="form-control" type="text" id="subName2" name="subName" required="" placeholder="">
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
                                                        onclick="$m15003.add();">保 存</button>
                                            </div>
                                            <div class="col-5">
                                                <button type="button"
                                                        class="btn btn-light waves-effect waves-light btn_default"
                                                        onclick="$m15003.hide();">取 消</button>
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
    var $m15003={
        init:function(id){
            this.show();
            //重置表单
            $($m15003.params.formId)[0].reset();
            var url = serverAddr+"/receivePaymentSubject/toEditPage.html" ;
            $custom.request.ajax(url, {"id": id}, function (result) {
                $("#id2").val(result.data.id);
                $("#name2").val(result.data.name);
                $("#code2").val(result.data.code);
                $("#subCode2").val(result.data.subCode);
                $("#subName2").val(result.data.subName);
            });
        },
        add:function(){
            //必填项校验
            if($("#name2").val()==""){
                $custom.alert.error("NC收支费项不能为空！");
                return ;
            }
            if($("#code2").val()==""){
                $custom.alert.error("收支费项编码不能为空！");
                return ;
            }
            if($("#subCode2").val()==""){
                $custom.alert.error("科目编码不能为空！");
                return ;
            }
            if($("#subName2").val()==""){
                $custom.alert.error("科目名称不能为空！");
                return ;
            }

            var param = $($m15003.params.formId).serialize();
                $custom.request.ajax(
                    $m15003.params.url,
                    param,
                    function(data){
                        if(data.state==200){
                            $custom.alert.success(data.message);
                            //成功隐藏
                            $m15003.hide();
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
            $($m15003.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m15003.params.modalId).modal("hide");
        },
        params:{
            url:serverAddr+'/receivePaymentSubject/modifyNcPay.asyn?r='+Math.random(),
            formId:'#editForm',
            modalId:'#signup-modal2',
        },
    }

</script>