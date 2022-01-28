<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                                        <label >姓名</label>
                                        <input class="form-control" type="text" name="name" required="" placeholder="张三">
                                    </div>
                                </div>
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label >用户姓名</label>
                                        <input class="form-control" type="email" name="username" required="" placeholder="zhangsan">
                                    </div>
                                </div>
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label >电话号码</label>
                                        <input class="form-control" type="tel" name="tel"  required="" placeholder="13815458754">
                                    </div>
                                </div>
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label >性别</label>
                                        <select class="form-control" name="sex">
                                            <option value="m">男</option>
                                            <option value="f">女</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label >邮箱</label>
                                        <input class="form-control" type="email" name="email"   placeholder="john@deo.com">
                                    </div>
                                </div>
                                <div class="form-group m-b-25">
                                    <div class="col-12">
                                        <label >企业名称</label>
                                        <c:choose>
                                            <c:when test="${tag==0}" >
                                                <select class="form-control" name="merchantId" >
                                                    <c:forEach items="${merchantInfoModels}" var="merchant" >
                                                        <option value="${merchant.id}">${merchant.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </c:when>
                                            <c:otherwise>
                                                <input class="form-control" type="text" name="merchantName"  value="${merchantName}" readonly>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                </div>
                                <div class="form-group account-btn text-center m-t-10">
                                    <div class="col-12">
                                        <button class="btn w-lg btn-rounded btn-primary waves-effect waves-light" type="button" onclick="$m1011.add();">新增</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
<script type="text/javascript">
    var $m1011={
        init:function(){
            this.show();
            //重置表单
            $($m1011.params.formId)[0].reset();
        },
        add:function(){
            $custom.request.ajax(
                    $m1011.params.url,
                    $($m1011.params.formId).serializeArray(),
                    function(data){
                        if(data.state==200){
                            $custom.alert.success(data.message);
                            //成功隐藏
                            $m1011.hide();
                            //重新加载table表格
                            $mytable.fn.reload();
                        }else{
                            $custom.alert.error(data.message);
                        }

            });
        },
        show:function(){
             $($m1011.params.modalId).modal("show")
        },
        hide:function(){
             $($m1011.params.modalId).modal("hide");
        },
        params:{
            url:'/user/saveUser.asyn?r='+Math.random(),
            formId:'#addForm',
            modalId:'#signup-modal',
        }
    }


</script>