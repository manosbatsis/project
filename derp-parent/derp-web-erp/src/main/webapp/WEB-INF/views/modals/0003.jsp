<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .confirm-btn {
        background-color: #4fbde9;
        border: 1px solid #4fbde9;
        line-height: 15px;
        height: 30px;
        border-radius: 0;
        font-size: 13px;
    }
    .cancel-btn {
        line-height: 15px;
        height: 30px;
        border-radius: 0;
        font-size: 13px;
    }
</style>
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
                                    <c:forEach items="${merchantSelectBeans }" var="merchant">
                                        <div class="col-6" style="padding-left: 20px">
                                            <div class="row">
                                                <label style="height: 25px; line-height: 25px">
                                                    <input type="radio" name="merchantId" value="${merchant.id}"
                                                        <c:if test="${merchant.id == merchantId}">checked</c:if>
                                                    >${merchant.code}&nbsp;&nbsp;${merchant.name}
                                                </label>
                                            </div>
                                        </div>
                                    </c:forEach>
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
                                            <button type="button"
                                                    class="btn btn-info waves-effect waves-light fr btn_default confirm-btn" id="save-buttons">确 定</button>
                                        </div>
                                        <div class="col-6">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default cancel-btn" id="cancel-buttons">取 消</button>
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
    var $m0003={
        init:function(){
            $("#warnTip").html('');
            this.show();
        },
        show:function(){
            $($m0003.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m0003.params.modalId).modal("hide");
        },
        params:{
            url:'/user/modifyPwd.asyn?r='+Math.random(),
            formId:'#merchantForm',
            modalId:'#merchant-modal',
        }
    };

    $("#save-buttons").click(function () {
        var merchantId = $("input[type='radio'][name='merchantId']:checked").val();
        var url = "/login/updateLoginMerchant.asyn";
        if (!merchantId) {
            $("#warnTip").html("请选择公司主体！");
            return;
        }
        $custom.request.ajax(url,{merchantId:merchantId},function(result){
            if(result.state==200){
                window.location.href = "/index.html";
            } else {
                $custom.alert.error(result.message);
            }
        });

    });

    $("#cancel-buttons").click(function () {
        $m0003.hide();
    });

</script>