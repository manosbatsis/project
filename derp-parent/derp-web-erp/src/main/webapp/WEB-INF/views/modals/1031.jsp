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
                            <input type="hidden" name="isEnabled" value="1" />
                            <div class="col-12">
                                <label ><i class="red">* </i>权限名称</label>
                                <input class="form-control" type="text" id="authorityName" name="authorityName"
                                       required reqMsg="权限名称不能为空！" >
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label ><i class="red">* </i>上级权限</label>
                                <!-- 显示 -->
                                <input id="parentName" class="form-control" type="text" id="parentName"  onclick="showMenu();"
                                       required reqMsg="上级权限不能为空！">
                                <!-- 传值  -->
                                <input class="form-control" type="hidden" name="parentId"   >
                                <div id="menuContent" class="menuContent" style="display:none; position: absolute;">
                                    <ul id="parentTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >权限类型</label>
                                <select class="form-control" name="type" id="type" required reqMsg="权限类型不能为空！">
                                    <option value="1">菜单</option>
                                    <option value="2">按钮</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >权限URL</label>
                                <input class="form-control" type="text" name="url"   >
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >子服务URL</label>
                                <input class="form-control" type="text" name="serverAddr"   >
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label ><i class="red">* </i>模块</label>
                                <select class="form-control" name="module" id="module" required reqMsg="模块不能为空！">
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >授权码</label>
                                <input class="form-control" type="text" name="permission" id="permission"  >
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >序号</label>
                                <input class="form-control" type="text" name="seq"   >
                            </div>
                        </div>
                        <div class="form-group m-b-25">
                            <div class="col-12">
                                <label >备注</label>
                                <input class="form-control" type="text" name=""   >
                            </div>
                        </div>
                        <div class="form-group account-btn text-center m-t-10">
                            <div class="col-12">
                                <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m1031.add();">新增</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="module"]')[0],"permissionInfo_moduleList",null);

    function showMenu() {
        var cityObj = $("#parentName");
        var cityOffset = $("#parentName"). position();
        $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
        $("body").bind("mousedown", onBodyDown);
    }

    function onBodyDown(event) {
        if (!(event.target.id == "menuBtn" || event.target.id == "parentName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
            hideMenu();
        }
    }
    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }


    $(document).ready(function(){
        $("#parentTree").myTree($m1031.setting,false);
    });

    var $m1031={
        init:function(){
            //刷新树结构
            $myTree.fn.refresh();
            //显示窗体
            this.show();
            //重置表单
            $($m1031.params.formId)[0].reset();
        },
        add:function(){
            if($("#type").val()!="" && $("#type").val()==2){
                $("#permission").attr("required", "");
                $("#permission").attr("reqMsg", "授权码不能为空！");
            } else {
                $("#permission").removeAttr("required");
                $("#permission").removeAttr("reqMsg");
            }
            //必填项校验
            if(!$checker.verificationRequired()){
                return ;
            }

            $custom.request.ajax(
                    $m1031.params.url,
                    $($m1031.params.formId).serializeArray(),
                    function(data){
                        if(data.state==200){
                            //刷新列表
                            $mytable.fn.reload();
                            //刷新树结构
                            $myTree.fn.refresh();
                            //隐藏新增 窗体
                            $m1031.hide();
                        }else{
                            $custom.alert.error(data.expMessage);
                        }
                    });
        },
        show:function(){
            $($m1031.params.modalId).modal("show")
        },
        hide:function(){
            $($m1031.params.modalId).modal("hide");
        },
        params:{
            url:'/permission/save.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#signup-modal',
        },
        setting:{
            async: {
                enable: true,
                url:"/permission/listTree.asyn",
            },
            callback:{
                onClick:function(event, treeId, treeNode){
                    $('input[name="parentId"]').val(treeNode.id);
                    $('#parentName').val(treeNode.name);
                }
            },
            view: {
                showIcon: false
            }
        }
    }


</script>