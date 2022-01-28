<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >角色名称</label>
                                <input class="form-control" type="text" name="name" required="" placeholder="超级管理员">
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >备注</label>
                                <textarea class="form-control" rows="5" name="remark"></textarea>
                            </div>
                        </div>
                        <div class="form-group account-btn text-center m-t-10">
                            <div class="col-12">
                                <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m1021.add();">新增</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    var $m1021={
        init:function(){
            this.show();
            //重置表单
            $($m1021.params.formId)[0].reset();
        },
        add:function(){
            $custom.request.ajax(
                    $m1021.params.url,
                    $($m1021.params.formId).serializeArray(),
                    function(data){
                        if(data.state==200){
                            $custom.alert.success(data.message);
                            $m1021.hide();
                            $mytable.fn.reload();
                        }else{
                            $custom.alert.error(data.message);
                        }
                    });
        },
        show:function(){
            $($m1021.params.modalId).modal("show")
        },
        hide:function(){
            $($m1021.params.modalId).modal("hide");
        },
        params:{
            url:'/role/saveRole.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>