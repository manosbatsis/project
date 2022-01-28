<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="merchant-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>--%>
                            <h5 class="modal-title" id="myLargeModalLabel">选择公司主体</h5>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal" id="merchantForm">
                                <div class="form-row ml15" id="merchantDiv">

                                </div>
                            </form>

                            <div class="form-row m-t-50">
                                <div class="col-12" style="padding-bottom: 10px;" >
                                    <div class="row">
                                        <div class="col-3"></div>
                                        <div class="col-6"><span id="warnTip" style="color:#FF0000;"></span></div>
                                        <div class="col-3"></div>
                                    </div>

                                </div>
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" onclick="$m0002.save()">确 定</button>
                                        </div>
                                        <div class="col-6">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick="$m0002.hide()">取 消</button>
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

    //js 方法
    var $m0002={
        init:function(username, password, rememberMe){
            $("#warnTip").html('');
            this.params.username = username;
            this.params.password = password;
            this.params.rememberMe = rememberMe;
            this.show();
        },
        show:function(){
            $($m0002.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $("#merchantDiv").html('');
            $($m0002.params.modalId).modal("hide");
        },
        save:function () {
            var merchantId = $("input[type='radio'][name='merchantId']:checked").val();
            if (!merchantId) {
                $("#warnTip").html("请选择公司主体！");
                return;
            }
            var url = "/login/submitUser.asyn";
            $.post(url,{username:this.params.username,password:this.params.password,remember:this.params.rememberMe,merchantId:merchantId},function(result){
                if(result.state==200){
                    window.location.href = "/index.html";
                } else {
                    $("#tips").html("登陆失败！");
                }
            });
        },
        params:{
            url:'/user/modifyPwd.asyn?r='+Math.random(),
            formId:'#merchantForm',
            modalId:'#merchant-modal',
            username:'',
            password:'',
            rememberMe:''
        }
    };

</script>