<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="modify-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <form class="form-horizontal" id="resetPwdForm">
                                <!-- 自定义参数  -->
                                <input type="hidden" name="id" value="100108" />
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                        <label for="oldPwd">旧密码</label>
                                        <input class="form-control" type="password" id="oldPwd" name="oldPwd"  placeholder="******">
                                    </div>
                                </div>
                                <div class="form-group row m-b-20">
                                    <div class="col-12">
                                        <label for="password">新密码</label>
                                        <input class="form-control" type="password" id="password" name="password"  placeholder="******">
                                    </div>
                                </div>
                                <div class="form-group row text-center m-t-10">
                                    <div class="col-12">
                                        <button type="button" class="btn btn-block btn-gradient waves-effect waves-light" onclick="$m0001.resetPwd();">修改密码</button>
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

    //js 方法
    var $m0001={
        init:function(){
         //弹出框体
         this.show();
        },
        resetPwd:function(){
            //设置请求地址
            //获取请求数据
            var dataJson=$($m0001.params.formId).serializeArray();
            //发送ajax
            $custom.request.ajax($m0001.params.url,dataJson,function(result){
                if(result.state==200&&result.data.code=='0000'){
                    $custom.alert.success(result.data.message);
                    //隐藏窗体
                    $m0001.hide();
                }else{
                    $custom.alert.error(result.data.message);
                }

            });
        },
        show:function(){
            $($m0001.params.modalId).modal("show")
        },
        hide:function(){
            $($m0001.params.modalId).modal("hide");
        },
        params:{
            url:'/user/modifyPwd.asyn?r='+Math.random(),
            formId:'#resetPwdForm',
            modalId:'#modify-modal',
        }
    };


</script>