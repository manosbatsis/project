<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="role-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-group m-b-25">
                        <div class="col-12">
                            <!--  权限树 -->
                            <ul id="permissionTree" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="form-group account-btn text-center m-t-10">
                        <div class="col-12">
                            <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m1022.fn.addPermission();">修改</button>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    $(document).ready(function(){
        $("#permissionTree").myTree($m1022.setting,true);
    });


    var $m1022={
        //方法
        fn:{
            /**
             * 初始化
             *
             */
           init:function(id){
                $m1022.params.roleId=id;
               //显示弹框
               $("#role-modal").modal("show");
               //不选中
               $myTree.fn.cancelAllChecked();
               $custom.request.ajax('/permission/searchByRoleId.asyn?r='+Math.random(),{"roleId":$m1022.params.roleId},function(resultData){
                   if(resultData.state==200){
                       var ids=[];
                       for(var i=0;i<resultData.data.length;i++){
                           ids.push(resultData.data[i].permissionId);
                       }
                       $myTree.fn.checkById(ids);
                   }else{
                       $custom.alert.error(resultData.message);
                   }
               });
           },
           addPermission:function(){
               var ids=$myTree.fn.getCheckedIds();
               if(ids==null || ids==""){
            	   ids=0;
               }
               $custom.request.ajax('/role/addPermission.asyn?r='+Math.random(),{"ids":ids,roleId:$m1022.params.roleId},function(data){
                   if(data.state==200){
                       $custom.alert.success(data.message);
                       $("#role-modal").modal("hide");
                   }else{
                       $custom.alert.error(data.message);
                   }
               });
           }

        },
        //参数
        params:{
            roleId:0,
        },
        /**
         * zTree 配置参数
         */
        setting:{
            check: {
                enable: true
            },
            async: {
                enable: true,
                url:"/permission/listTreeAll.asyn",
            },
            callback: {
                onAsyncSuccess: function(){
                    //展开
                    $myTree.fn.expandAll();
                }
            },
            view: {
                showIcon: false
            }
        }
    };





</script>