<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
    }
    .header-title {
        padding: 15px;
        padding-right: 400px;
        font-weight: bolder;
        font-size: 18px;
    }
    .header-button {
        background-color: #fff;
        border: none;
    }
    .header-close {
    }
    .header-close::after {
        content: "\2716";
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 560px;">
                <div class="header" >
                    <span class="header-title" ></span>
                    <button class="header-button" ><span class="header-close" onclick="$m10001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                    	<input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label >事业部编码<i class="red">*</i>：</label>
                                <input style="width: 360px;" type="text" name="code" id="code" require reqrMsg="事业部编码不能为空">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >事业部名称<i class="red">*</i>：</label>
                                <input style="width: 360px;" type="text" name="name" id="name" require reqrMsg="事业部名称不能为空！" >
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label >部门名称<i class="red">*</i>：</label>
                                <select class="input_msg" style="width: 360px;" id="departmentName" name="departmentName" require reqrMsg="部门名称不能为空！">
                                    <option value="">请选择</option>
                                </select>

                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m10001.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m10001.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                    <input type="hidden" value="" id="departmentId">
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m10001={
        init:function(flag, id){
            //重置表单
            $($m10001.params.formId)[0].reset();
            //判断新增/编辑
            if (flag == 0) {
            	$(".header-title").html("新增事业部") ;
                $("#code").removeAttr("disabled");
                $("#departmentId").val("");
                getDepartmentNameList($('select[name="departmentName"]'),"");
            } else if (flag ==1) {
            	$(".header-title").html("编辑事业部") ;
                var url = "/businessUnit/detail.asyn";
                $custom.request.ajax(url,{"id":id},function(data) {
                    if (data.state == 200) {
                        $('#code').val(data.data.code);
                        $("#code").attr("disabled","disabled");
                        $('#name').val(data.data.name);
                        $('#id').val(data.data.id);
                        $("#departmentId").val(data.data.departmentId);
                        getDepartmentNameList($('select[name="departmentName"]'),$("#departmentId").val());
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
            this.show();
        },
        submit:function(){
            var code = $('#code').val();
            var name = $('#name').val();
            var id = $('#id').val();
            var departmentName=$("#departmentName option:selected").text();
            var departmentId=$("#departmentName option:selected").val();
            var dataJson = {"id":id, "code":code,"name":name,"departmentId":departmentId,"departmentName":departmentName} ;

            $custom.request.ajaxReqrCheck(

                $m10001.params.url,
                dataJson,
                function(result) {
                    if(result.data.code =='00') {
                        $custom.alert.success(result.data.message);
                        $m10001.hide();
                        setTimeout("$load.a('/list/menu.asyn?act=1901');", 1000) ;
                    } else {
                   		$custom.alert.error(result.data.message);
                   		return ;
                    }
                },
                null,
                function() {
                    $module.revertList.serializeListForm() ;
                    $module.revertList.isMainList = true ;
                }
            );

        },
        cancel:function() {
                $m10001.hide();
        },
        show:function(){
            $($m10001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m10001.params.modalId).modal("hide");
        },
        params:{
            url:'/businessUnit/saveBusinessUnit.asyn',
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }

    function getDepartmentNameList(obj,id){
        var selectObj=$(obj);
        var jsonData;
        $custom.request.ajaxSync('/businessUnit/getDepartSelectBean.asyn',{},function(result) {
            jsonData=result.data;
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");
            jsonData.forEach(function(o,i){
                selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
            });
            debugger
            if(id!=""&&id!=null){
                debugger
                selectObj.find("option[value = '"+id+"']").attr("selected","selected");
            }
        });

    }


</script>