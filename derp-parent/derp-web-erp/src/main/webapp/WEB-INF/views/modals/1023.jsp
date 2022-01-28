<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="user-select-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-group m-b-25">
                        <div class="col-12">
                            <!--  权限树 -->
                            <h2>选择绑定用户</h2>
                            <select id='pre-selected-options' multiple='multiple'>
                            </select>
                        </div>
                    </div>
                    <div class="form-group account-btn text-center m-t-10">
                        <div class="col-12">
                            <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m1023.add();">授予</button>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    $(document).ready(function(){
        $('#pre-selected-options').multiSelect();
    });

    var $m1023={
        init:function(roleId){
            $m1023.params.roleId=roleId;
            //显示窗体
            this.show();
            //清空选中下拉框
            $("#pre-selected-options").multiSelect("deselect_all");
            //加载数据
            $custom.request.ajax(
                    '/role/searchUserInfo.asyn?r='+Math.random(),
                    {roleId:roleId},
                    function(result){
                        if(result.state==200){
                            //所有用户
                            $('#pre-selected-options').multiSelect('addOption',result.data.allUser);
                            $('#pre-selected-options').multiSelect('select',result.data.bindUser);
                            $('#pre-selected-options').multiSelect('refresh');
                            /*  var json=null;
                            for(var i=0;i<result.data.bindUser.length;i++){
                                $('#pre-selected-options').multiSelect('select',{"value":String(result.data.bindUser[i].userId)});
                                //初始化 加载选中的值
                                $('#pre-selected-options option[value="'+result.data.bindUser[i].userId+'"]').attr("selected","selected");
                            } */
                            //已选中用户

                        }else{
                            $custom.alert.error(data.message);
                        }
                    });
        },
        add:function(){
            //获取先中的值
            var ids =  $('#pre-selected-options').val();
            console.log(ids);
            $custom.request.ajax(
                    '/role/bindUser.asyn?r='+Math.random(),
                    {ids:ids.join(","),roleId:$m1023.params.roleId},
                    function(data){
                        if(data.state==200){
                            $custom.alert.success(data.message);
                            //隐藏新增 窗体
                            $m1023.hide();
                        }else{
                            $custom.alert.error(data.message);
                        }
                    });
        },
        show:function(){
            $($m1023.params.modalId).modal("show")
        },
        hide:function(){
            $($m1023.params.modalId).modal("hide");
        },
        params:{
            url:'/permission/save.asyn?r='+Math.random(),
            modalId:'#user-select-modal',
            roleId:''
        }

    }






</script>