<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
    .accounttype_radio_label {
        height: 25px;
        line-height: 25px;
        color: #aaaaaa;
    }
    .accounttype_radio_input:disabled:checked:before {
        background-color: #aaa !important;
    }
    .accounttype_radio_input:disabled:after {
        border-color: #aaa !important;
    }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="dripicons-location"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">系统管理</a></li>
                                        <li class="breadcrumb-item "><a href="javascript:$load.a('/user/toPage.html');">用户管理</a>
                                        </li>
                                        <li class="breadcrumb-item "><a
                                                href="javascript:$load.a('/user/toEditPage.html?id=${detail.id }');">编辑用户管理</a>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->
                            <div class="title_text">用户信息</div>
                            <form id="add-form">
                                <input name="id" value="${detail.id }" hidden>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label " style="white-space: nowrap;"><span class="fr"><i class="red">*</i> 姓名<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="name" name="name" required="" placeholder="张三" value="${detail.name }">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-1"></div>
                                    <div class="col-4"></div>
                                </div>


                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label " style="white-space: nowrap;"><span class="fr"><i class="red">*</i> 工号<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="text" id="code" name="code" required="" placeholder="123456" value="${detail.code }">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-1"></div>
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label" style="white-space: nowrap;"><span class="fr"><i class="red">*</i> 登录账号<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="email" id="username" disabled name="username" required="" value="${detail.username }">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 性别<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <select class="form-control" name="sex" id="sex">
                                                    <c:choose>
                                                        <c:when test="${detail.sex eq 'm' }">
                                                            <option value="m" selected>男</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="m">男</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${detail.sex eq 'f' }">
                                                            <option value="f" selected>女</option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <option value="f">女</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-1"></div>
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 电话号码<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="tel" id="tel" name="tel"  required="" value="${detail.tel}">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 邮箱<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input class="form-control" type="email" id="email" name="email" value="${detail.email}">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-1"></div>
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-2 col-form-label "
                                                   style="white-space: nowrap;"><span class="fr"><i
                                                    class="red">*</i> 账号类型<span>：</span></span></label>
                                            <div class="col-10 ml10 line">
                                                <input type="hidden" id="accountType" value="${detail.accountType}">
                                                <c:choose>
                                                    <c:when test="${detail.accountType eq '1' }">
                                                        <label class="accounttype_radio_label"><input type="radio" class="accounttype_radio_input" name="accountType" value="1" checked disabled>普通账号</label>
                                                        <label class="accounttype_radio_label"><input type="radio" class="accounttype_radio_input" name="accountType" value="0" disabled>后台管理员</label>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <label class="accounttype_radio_label"><input type="radio" class="accounttype_radio_input" name="accountType" value="1" disabled>普通账号</label>
                                                        <label class="accounttype_radio_label"><input type="radio" class="accounttype_radio_input" name="accountType" value="0" checked disabled>后台管理员</label>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <div class="form-group mt20 " id="merchantGroup" >
                                <div class="title_text" >
                                    <label style="padding-right: 20px;font-size: 16px;font-weight: bold;">绑定公司</label>
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn-sm"
                                            id="add-list-button">新 增</button>
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn-sm"
                                            id="delete-list-button">删 除</button>
                                </div>
                                <div class="form-row mt20">
                                    <table id="table-list" class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th><input id="checkbox11" type="checkbox" name="all"></th>
                                            <th>公司简称</th>
                                            <th>公司编号</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${merchantInfoModels }" var="merchant">
                                            <tr>
                                                <td>
                                                    <input type='checkbox' name='item-checkbox'>
                                                    <input type="hidden" class="merchant_id" name="merchant_id" value="${merchant.id }">
                                                </td>
                                                <td>${merchant.name}</td>
                                                <td>${merchant.code}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                            <div class="form-group mt20 " id="buGroup" >
                                <div class="title_text" >
                                    <label style="padding-right: 20px;font-size: 16px;font-weight: bold;">绑定事业部</label>
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn-sm"
                                            id="add-bu-list-button">新 增</button>
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn-sm"
                                            id="delete-bu-list-button">删 除</button>
                                </div>
                                <div class="form-row mt20">
                                    <table id="table-bu-list" class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th><input id="checkbox22" type="checkbox" name="all-bu"></th>
                                            <th>事业部名称</th>
                                            <th>事业部编号</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${buRelList }" var="buRel">
                                            <tr>
                                                <td>
                                                    <input type='checkbox' name='item-bu-checkbox'>
                                                    <input type="hidden" class="bu_id" name="bu_id" value="${buRel.buId }">
                                                </td>
                                                <td>${buRel.buName}</td>
                                                <td>${buRel.buCode}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                        </div>
                                        <div class="col-6">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- 新增弹窗部分   start -->
                </div>
                <!-- end row -->
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->

</div>
    <script type="text/javascript">
        $(function () {

            var accountType = $("#accountType").val();
            if (accountType != '1') {
                $(".form-group").attr("style","display:none;");
            }
            //重新加载select2
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            });

            //保存按钮
            $("#save-buttons").click(function () {
                var url = "/user/editUser.asyn";
                var form = $("#add-form").serializeArray();
                if ($("#name").val() == "") {
                    $custom.alert.error("姓名不能为空！");
                    return;
                }
                if ($("#username").val() == "") {
                    $custom.alert.error("登录账号不能为空！");
                    return;
                } /*else {
                    var reg = /^[a-zA-Z]([a-zA-Z0-9]){1,}$/;
                    if (!reg.test($("#username").val())) {
                        $custom.alert.error("登录账号只能以字母开头，且不包含中文！");
                        return;
                    }
                }*/
                if ($("#tel").val() == "") {
                    $custom.alert.error("电话号码不能为空！");
                    return;
                }
                if ($("#email").val() == "") {
                    $custom.alert.error("邮箱不能为空！");
                    return;
                }
                if ($("#sex").val() == "") {
                    $custom.alert.error("性别不能为空！");
                    return;
                }
                if(!accountType){
                    $custom.alert.error("账户类型不能为空！");
                    return ;
                }

                if (accountType == '1') {
                    //获取公司信息
                    var ids = "";//储存所有公司ID
                    var obj_ids = document.getElementsByName("merchant_id");
                    for (var i = 0; i < obj_ids.length; i++) {
                        if (ids == "") {
                            ids = obj_ids[i].value;
                        } else {
                            ids = ids + "," + obj_ids[i].value;
                        }
                    }
                    if (!ids) {
                        $custom.alert.error("至少绑定一个公司！");
                        return ;
                    }
                    
                    var buIds = "";//储存所有公司ID
                    var bu_ids = document.getElementsByName("bu_id");
                    for (var i = 0; i < bu_ids.length; i++) {
                        if (buIds == "") {
                        	buIds = bu_ids[i].value;
                        } else {
                        	buIds = buIds + "," + bu_ids[i].value;
                        }
                    }
                    if (!buIds) {
                        $custom.alert.error("至少绑定一个事业部！");
                        return ;
                    }
                }

                if (ids) {
                    var merchantIds = {"name": "merchantIds", "value": ids}
                    form.push(merchantIds);
                }
                
                if (buIds) {
                    var buIdKeyVal = {"name": "buIds", "value": buIds}
                    form.push(buIdKeyVal);
                }
                
                var account = {"name": "accountType", "value": accountType}
                form.push(account);
                $custom.request.ajax(url, form, function (result) {
                    if (result.state == '200') {
                        $custom.alert.success("编辑成功！");
                        setTimeout(function () {
                            $load.a("/user/toPage.html");
                        }, 1000);
                    } else {
                        $custom.alert.error("编辑失败！");
                    }
                });
            });

            //返回
            $("#cancel-buttons").click(function () {
                $load.a("/user/toPage.html");
            });

            //限制新增条数每次只能一条，填完信息后才可以继续填写
            var flag = 0;
            //储存已经选择过的商品ID
            var merchantId = [];
            //新增公司列表按钮
            $("#add-list-button").click(function(){
                if(flag == 0){
                    var listArr = "<tr>"+
                        "<td style='width:50px;text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='merchant_id'></td>"+
                        "<td><select class='form-control modal_input goods_select2' id='merchantId'></select></td>"+
                        "<td></td>"+
                        "</tr>";
                    $("#table-list").append(listArr);
                    var url = "/merchant/getUserMerchantSelectBean.asyn?r="+Math.random();
                    $custom.request.ajax(url, {}, function(result){
                        selLoad(result.data, "merchantId");
                        //数据库中的公司移出下拉框
                        $(".merchant_id").each(function(){
                            merchantId.push($(this).val());
                        });
                        //选择过的公司移出下拉框
                        if(merchantId.length>0){
                            for(var i = 0; i < merchantId.length; i++){
                                $("#merchantId").children().each(function(){
                                    if($(this).val()==merchantId[i]){
                                        $(this).remove();
                                    }
                                });
                            }
                        }
                        //重新加载select2
                        $(".goods_select2").select2({
                            language:"zh-CN",
                            placeholder: '请选择',
                            allowClear:true
                        });
                    });
                    flag = 1;
                }
                //选择商品
                $("#merchantId").change(function(){
                    var that = $(this);
                    var url = "/merchant/getMerchantInfoDetails.asyn?r="+Math.random();
                    var data = {"id":that.val()};
                    $custom.request.ajax(url, data, function(result){
                        //回写公司id
                        that.parent().prev().find("input[name='merchant_id']").val(result.data.id);
                        //回写公司编码
                        that.parent().next().text(result.data.code);
                        //回写公司名称
                        that.parent().text(result.data.name);
                        flag = 0;
                        merchantId.push(result.data.id);
                    });
                });
            });

            $("#delete-list-button").click(function() {
                var del_ids = [];//储存删除行的id
                $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
                    n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
                    del_ids.push($(this).parents("tr").find("input[name='merchant_id']").val());
                    $("#table-list").find("tr:eq("+(n+1)+")").remove();
                });
                for (var i = 0; i < merchantId.length; i++) {
                    for (var j = 0; j < del_ids.length; j++) {
                        if(merchantId[i] == del_ids[j]){
                            merchantId.splice(i, 1);
                        }
                    }
                }
                flag = 0;
                $("input[name='all']").prop("checked",false);
            });
        });
        
        var buFlag = 0;
        //储存已经选择过的商品ID
        var buId = [];
        //新增事业部列表按钮
        $("#add-bu-list-button").click(
            function() {
                if (buFlag == 0) {
                    var listArr = "<tr>"
                        + "<td><input type='checkbox' name='item-bu-checkbox'><input type='hidden' name='bu_id'></td>"
                        + "<td><select class='form-control goods_select2' id='buId'></select></td>"
                        + "<td></td>" + "</tr>";
                    $("#table-bu-list tbody").append(listArr);
                    var url = "/businessUnit/getAllSelectBean.asyn?r=" + Math.random();
                    $custom.request.ajax(url,{}, function(result) {
                        selLoad(result.data, "buId");
                        
                        $(".bu_id").each(function(){
                        	buId.push($(this).val());
                        });
                        
                        //选择过的商品移出下拉框
                        if (buId.length > 0) {
                            for (var i = 0; i < buId.length; i++) {
                                $("#buId").children().each(function() {
                                    if ($(this).val() == buId[i]) {
                                        $(this).remove();
                                    }
                                });
                            }
                        }
                        //重新加载select2
                        $("#buId").select2({
                            language : "zh-CN",
                            placeholder : '请选择',
                            allowClear : true
                        });
                    });
                    buFlag = 1;
                }
                $("#buId").change(function() {
                    var that = $(this);
                    var url = "/businessUnit/detail.asyn?r="+ Math.random();
                    var data = {"id" : that.val()};
                    $custom.request.ajax(url,data,function(result) {
                        //回写事业部id
                        that.parent().prev().find("input[name='bu_id']").val(result.data.id);
                        //回写事业部编码
                        that.parent().next().text(result.data.code);
                        //回写事业部简称
                        that.parent().text(result.data.name);
                        buFlag = 0;
                        buId.push(result.data.id);
                    });
                });
            });

        //删除选中行
        $("#delete-bu-list-button").click(function() {
            var del_ids = [];//储存删除行的id
            $("input[name='item-bu-checkbox']:checked").each(
                function() { // 遍历选中的checkbox
                    n = $(this).parents("tr").index(); // 获取checkbox所在行的顺序
                    del_ids.push($(this).parents("tr").find(
                        "input[name='bu_id']").val());
                    $("#table-bu-list").find("tr:eq(" + (n + 1) + ")").remove();
                });
            for (var i = 0; i < buId.length; i++) {
                for (var j = 0; j < del_ids.length; j++) {
                    if (buId[i] == del_ids[j]) {
                    	buId.splice(i, 1);
                    }
                }
            }
            buFlag = 0;
            $("input[name='all-bu']").prop("checked", false);
        });
        
        $("input[name='all']").on("change", function(){
            if($(this).is(':checked')){
                $(":checkbox", '#table-list').prop("checked",$(this).prop("checked"));
                $('#table-list tbody tr').addClass('success');
            }else{
                $(":checkbox", '#table-list').prop("checked",false);
                $('#table-list tbody tr').removeClass('success');
            }
        })
        $('#table-list').on("change", ":checkbox", function() {
            $(this).parents('tr').toggleClass('success');
        })
        
        $("input[name='all-bu']").on("change", function(){
            if($(this).is(':checked')){
                $(":checkbox", '#table-bu-list').prop("checked",$(this).prop("checked"));
                $('#table-bu-list tbody tr').addClass('success');
            }else{
                $(":checkbox", '#table-bu-list').prop("checked",false);
                $('#table-bu-list tbody tr').removeClass('success');
            }
        })
        $('#table-bu-list').on("change", ":checkbox", function() {
            $(this).parents('tr').toggleClass('success');
        })
    </script>