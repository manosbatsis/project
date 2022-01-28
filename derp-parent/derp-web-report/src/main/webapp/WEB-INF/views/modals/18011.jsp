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
                            <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m18011.add();">确认</button>
                            <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m18011.hide();">取消</button>
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

    var $m18011={
        init:function(roleId){
            $m18011.params.roleId=roleId;
            //显示窗体
            this.show();
            //清空选中下拉框
            $("#pre-selected-options").multiSelect("deselect");
          
            //加载数据
            $custom.request.ajax(
                    '/role/getUserInfo.asyn?r='+Math.random(),
                    {},
                    function(result){
                        if(result.state==200){
                            //所有用户
                            $('#pre-selected-options').multiSelect('addOption',result.data.allUser);
                            $('#pre-selected-options').multiSelect('refresh');

                        }else{
                            $custom.alert.error(data.message);
                        }
                    });
        },
        add:function(){
            //获取先中的值
            var ids =  $('#pre-selected-options').val();
			var emailStr="";
			var userIdStr="";
			var userNameStr="";
            for (var i = 0; i < ids.length; i++) {
				var userIdVal=ids[i];
			     $custom.request.ajaxSync(
		                    '/user/list.asyn?r='+Math.random(),
		                    {id:userIdVal},
		                    function(result){
		                        if(result.state==200){
		                        	if(result.data.list[0].email !=null){
		                        		emailStr+=result.data.list[0].email+";";
		                        		userIdStr+=result.data.list[0].id+";";
		                        		userNameStr+=result.data.list[0].name+";";
		                        	}
		                        }else{
		                            $custom.alert.error(data.message);
		                        }
		                    });
			}
            $("#receiverEmail").val(emailStr);
            $("#receiverId").val(userIdStr);
            $("#receiverName").val(userNameStr);
            
            
            //隐藏新增 窗体
            $m18011.hide();
        },
        show:function(){
            $($m18011.params.modalId).modal("show")
        },
        hide:function(){
            $($m18011.params.modalId).modal("hide");
        },
        params:{
            url:'',
            modalId:'#user-select-modal',
            roleId:''
        }
    }
</script>