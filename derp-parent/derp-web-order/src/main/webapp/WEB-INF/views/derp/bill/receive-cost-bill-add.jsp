<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp"/>
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->

            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                            <li class="breadcrumb-item">
                                                <a href="javascript:$module.load.pageOrder('act=14001');">应收账单列表</a>
                                            </li>
                                            <li class="breadcrumb-item"><a href="#">补充费用信息</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <form id="add-form">
                                    <div class="col-12 ml10">
                                        <div class="row">
                                            <div class="mt10">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="addtr();">添加补扣款</button>
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='$m13018.init(${bill.id})'>导入</button>
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="deltr();">删 除</button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row col-12 mt20" style="min-height:250px; overflow-x: auto;">
                                        <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                               width="100%">
                                            <thead>
                                            <tr>
                                                <th><input id="checkAll" type="checkbox" name="all"></th>
                                                <th><i class="red">*</i>费用项目</th>
                                                <th><i class="red">*</i>类型</th>
                                                <th>PO号</th>
                                                <th>发票描述</th>
                                                <th>商品货号</th>
                                                <th>商品名称</th>
                                                <th><i class="red">*</i>母品牌</th>
                                                <th>数量</th>
                                                <th><i class="red">*</i>税率</th>
                                                <th><i class="red">*</i>费用金额（不含税）</th>
                                                <th>税额</th>
                                                <th><i class="red">*</i>费用金额（含税）</th>
                                                <th>核销平台</th>
                                                <th>备注</th>
                                            </tr>
                                            </thead>
                                            <tbody id="table-list">
                                            <c:choose>
                                                <c:when test="${orderItems.size() !=0}">
                                                    <c:forEach items="${orderItems }" var="item">
                                                        <tr>
                                                            <td class="tc nowrap">
                                                                <input type='checkbox' name='item-checkbox'>
                                                            </td>
                                                            <td class="tc nowrap">
                                                                <input  name="projectId" class="form-control" type="hidden" value="${item.projectId}">
                                                                <input  name="projectName" class="form-control" type="text" value="${item.projectName}"
                                                                        onclick="selectTable(this, ${bill.customerId});">
                                                            </td>
                                                            <td class="tc nowrap">
                                                                <select name="billType" class="form-control" style="width: 100px;">
                                                                    <option value="0" ${item.billType=='0'?"selected='selected'":''}>补款</option>
                                                                    <option value="1" ${item.billType=='1'?"selected='selected'":''}>扣款</option>
                                                                </select>
                                                            </td>
                                                            <td class="tc nowrap">
                                                                <input type='text' name='poNo' value="${item.poNo}">
                                                            </td>
                                                            <td><input value="${item.invoiceDescription}" type="text" name="invoiceDescription" ></td>
                                                            <td><input type="hidden" class="form-control" name="goodsId"
                                                                       value="${item.goodsId}"><span>${item.goodsNo}</span>
                                                                <button type="button"
                                                                        class="btn btn-find waves-effect waves-light btn_default"
                                                                        onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品
                                                                </button>
                                                            </td>
                                                            <td><input name="goodsName" value="${item.goodsName}" readonly></td>
                                                            <td>
                                                                <select class="input_msg" name="superiorParentBrandCode" >
                                                                    <option value="" >请选择</option>
                                                                    <c:forEach items="${brandSelectBeans }" var="brandSelect" >
                                                                        <option value='${brandSelect.value }' <c:if test="${brandSelect.value == item.parentBrandId}">selected</c:if>>${brandSelect.label}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </td>
                                                            <td>
                                                               <input name="num" type="text" value="${item.num}" onblur="validNum(this)">
                                                            </td>
                                                            <td><input value="${item.taxRate}" type="text" name="taxRate" onblur="calculateTaxAmount(this)"></td>
                                                            <td><input value="${item.price}" type="text" name="price" onblur="calculateTaxPrice(this)" onkeyup="value=value.replace(/[^\d^\.]/g,'')"></td>
                                                            <td><input value="${item.tax}" type="text" name="tax" readonly></td>
                                                            <td><input value="${item.taxAmount}" type="text" name="taxAmount" readonly></td>
                                                            <td>
                                                                <select class="input_msg" name="storePlatformList" disabled>
                                                                    <option value="">请选择</option>
                                                                    <c:forEach items="${storePlatformList }" var="storePlatform" >
                                                                        <option value='${storePlatform.value }' <c:if test="${storePlatform.value == item.verifyPlatformCode}">selected</c:if>>${storePlatform.label}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </td>
                                                            <td><input value="${item.remark}" type="text" name="remark" ></td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td class="tc nowrap"><input type="checkbox" name="item-checkbox"></td>
                                                        <td class="tc nowrap">
                                                            <input  name="projectId" class="form-control" type="hidden">
                                                            <input  name="projectName" class="form-control" type="text" onclick="selectTable(this, ${bill.customerId});">
                                                        </td>
                                                        <td class="tc nowrap">
                                                            <select name="billType" class="form-control" style="width: 100px;">
                                                                <option value="">请选择</option>
                                                                <option value="0">补款</option>
                                                                <option value="1">扣款</option>
                                                            </select>
                                                        </td>
                                                        <td class="tc nowrap"> <input type="text" name="poNo" class="form-control" style="width:100px;"></td>
                                                        <td><input name="invoiceDescription" type="text" class="form-control"></td>
                                                        <td><input type="hidden" class="form-control" name="goodsId">
                                                            <button type="button"
                                                                    class="btn btn-find waves-effect waves-light btn_default"
                                                                    onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品
                                                            </button>
                                                        </td>
                                                        <td><input name="goodsName"></td>
                                                        <td>
                                                            <select class="input_msg" name="superiorParentBrandCode" >
                                                            </select>
                                                        </td>
                                                        <td><input name="num" type="text" class="form-control"  onblur="validNum(this)"></td>
                                                        <td><input  type="text" name="taxRate"onkeyup="value=value.replace(/[^\d^\.]/g,'')" onblur="calculateTaxAmount(this)"></td>
                                                        <td><input  type="text" name="price" onkeyup="value=value.replace(/[^\d^\.]/g,'')" onblur="calculateTaxPrice(this)"></td>
                                                        <td><input  type="text" name="tax" readonly></td>
                                                        <td><input  type="text" name="taxAmount" readonly></td>
                                                        <td>
                                                            <select class="input_msg" name="storePlatformList" disabled>
                                                                <option value="">请选择</option>
                                                                <c:forEach items="${storePlatformList }" var="storePlatform" >
                                                                    <option value='${storePlatform.value }'>${storePlatform.label}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                        <td><input name="remark" type="text" class="form-control"></td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                </form>
                                <div class="form-row mt20 attachDetail">
                                    <div class='col-12'>
                                        <div class="info-box">
                                            <button type="button"
                                                    class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                                    id="btn-file">上传附件
                                            </button>
                                            <form enctype="multipart/form-data" id="form-upload">
                                                <input type="file" name="file" id="file"
                                                       class="btn-hidden file-import">
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row mt20 attachDetail">
                                    <div class='col-12'>
                                        <div class="info-box">
                                            <div id="attachmentTable" multiple></div>
                                        </div>
                                    </div>
                                </div>
                                <!--   商品信息  end -->
                                <div class="form-row m-t-50">
                                    <div style="margin:0 auto;">
                                        <button type="button"
                                                class="btn btn-info waves-effect waves-light btn_default"
                                                id="save-temp-buttons" onclick="saveOrder();">保 存
                                        </button>
                                        <a class="btn btn-light waves-effect waves-light btn_default"
                                           id="cancel-buttons">取 消</a>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
            </div>
        </form>
        <!-- 弹窗开始 -->
        <jsp:include page="/WEB-INF/views/modals/13002.jsp"/>
        <jsp:include page="/WEB-INF/views/modals/13003.jsp"/>
        <jsp:include page="/WEB-INF/views/modals/13018.jsp"/>
        <!-- 弹窗结束 -->
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">

    var billId = ${bill.id};
    $(document).ready(function () {
        //点击上传文件
        $("#btn-file").click(function () {
            var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
            $("#form-upload").empty();
            $("#form-upload").append(input);
            $("#file").click();
        });
        var code = "${bill.code}"
        //上传文件到后端
        $("#form-upload").on("change", '.file-import', function () {
            console.log('ddddd');
            var formData = new FormData($("#form-upload")[0]);
            $custom.request.ajaxUpload(serverAddr + "/attachment/uploadFiles.asyn?code=" + code, formData, function (result) {
                if (result.state == 200 && result.data && result.data.code == 200) {
                    $attachTable.fn.reload();
                    $custom.alert.success("上传成功");
                } else {
                    $custom.alert.error("上传失败");
                }
            });
        });
        $("#attachmentTable").createAttachTables(code);

        initParentBrand();
        changeSortType();
    });

    //添加行
    function addtr() {
        $("#datatable-buttons").append('<tr>' +
            '<td class="tc nowrap"><input type="checkbox" name="item-checkbox" class="iCheck"></td>' +
            '<td class="tc nowrap">' +
            '<input name="projectId" type="hidden">' +
            '<input name="projectName" type="text" class="form-control" onclick="selectTable(this, ${bill.customerId});">' +
            '</td>' +
            '<td class="tc nowrap">' +
            '<select name="billType" class="form-control" style="width: 100px;">'+
            '<option value="">请选择</option>'+
            '<option value="0">补款</option>'+
            '<option value="1">扣款</option>'+
            '</select>'+
            '</td>' +
            '<td class="tc nowrap"> <input type="text" name="poNo" class="form-control"  style="width:100px;"></td>' +
            '<td><input name="invoiceDescription" type="text" class="form-control">' +
            '<td><input type="hidden" class="form-control" name="goodsId">' +
            '<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品' +
            '</button>' +
            '</td>' +
            '<td><input type="text" name="goodsName" class="form-control" readonly></td>' +
            '<td><select class="input_msg" name="superiorParentBrandCode" >' +
            '</select></td>' +
            '<td><input name="num" type="text" class="form-control"  onblur="validNum(this)"></td>' +
            '<td><input type="text" name="taxRate" onkeyup="value=value.replace(/[^\\d^\\.]/g,\'\')" onblur="calculateTaxAmount(this)"></td>' +
            '<td><input name="price" type="text" class="form-control" onblur="calculateTaxPrice(this)" onkeyup="value=value.replace(/[^\\d^\\.]/g,\'\')"></td>' +
            '<td><input  type="text" name="tax" readonly></td>'+
            '<td><input  type="text" name="taxAmount" readonly></td>' +
            '<td><select class="input_msg" name="storePlatformList" >' +
            '<option value="">请选择</option>' +
            '<c:forEach items="${storePlatformList }" var="storePlatform" >' +
            '<option value=\'${storePlatform.value }\' >${storePlatform.label}</option>' +
            '</c:forEach>' +
            ' </select>' +
            '</td>' +
            '<td><input name="remark" type="text" class="form-control">' +
            '</td></tr>');

        initParentBrand();
        changeSortType();
    }

    //删除行
    function deltr() {
        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
            var n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
            $("#table-list").find("tr:eq("+n+")").remove();
        });
    }

    //取消按钮
    $("#cancel-buttons").click(function(){
        $load.a($module.params.loadOrderPageURL+'act=14002&id='+${bill.id} + '&pageSource=1');
    });

    //保存
    function saveOrder() {
        var sortType = '${bill.sortType}';
        var check = true;
        var goodsList = []
        $("#datatable-buttons tbody tr").each(function (index, item) {
            var projectId = $(this).find("td").eq(1).find("input[name=projectId]").val();
            var projectName = $(this).find("td").eq(1).find("input[name=projectName]").val();
            var billType = $(this).find("td").eq(2).find("select").val();
            var poNo = $(this).find("td").eq(3).find("input").val();
            var invoiceDescription = $(this).find("td").eq(4).find("input").val();
            var goodsId = $(this).find("td").eq(5).find("input").val();
            var goodsNo = $(this).find("td").eq(5).find("span").text();
            var goodsName = $(this).find("td").eq(6).find("input").val();
            var brandParent = $(this).find("td").eq(7).find("option:selected").val();
            var brandParentName = $(this).find("td").eq(7).find("option:selected").text();
            var num = $(this).find("td").eq(8).find("input").val();
            var taxRate = $(this).find("td").eq(9).find("input").val();
            var price = $(this).find("td").eq(10).find("input").val();
            var storePlatformCode = $(this).find("td").eq(13).find("option:selected").val();
            var storePlatformName = $(this).find("td").eq(13).find("option:selected").text();

            var remark = $(this).find("td").eq(14).find("input").val();
            if (!projectId) {
                $custom.alert.error("费用项目不能为空！");
                check = false;
                return false;
            }

            if (!billType) {
                $custom.alert.error("类型不能为空！");
                check = false;
                return false;
            }

            if (!price) {
                $custom.alert.error("费用金额（不含税）不能为空！");
                check = false;
                return false;
            }

            if (!brandParent) {
                $custom.alert.error("母品牌不能为空！");
                check = false;
                return false;
            }

            if (!taxRate) {
                $custom.alert.error("税率不能为空！");
                check = false;
                return false;
            }

            if (sortType == '2' && !storePlatformCode) {
                $custom.alert.error("核销平台不能为空！");
                check = false;
                return false;
            }

            var goods = {};
            goods.projectId = projectId;
            goods.projectName = projectName;
            goods.billType = billType;
            goods.poNo = poNo;
            goods.goodsId = goodsId;
            goods.goodsNo = goodsNo;
            goods.goodsName = goodsName;
            goods.brandParent = brandParent;
            goods.brandParentName = brandParentName;
            goods.num = num;
            goods.price = price;
            goods.storePlatformCode = storePlatformCode;
            goods.invoiceDescription = invoiceDescription;
            goods.remark = remark;
            goods.taxRate = taxRate;
            goodsList.push(goods);
        });
        if (check) {
            var json = {};
            json.id = ${bill.id};
            json.costItemList = goodsList;
            var url = serverAddr + "/receiveBill/saveReceiveBillCost.asyn?";
            var jsonStr = JSON.stringify(json);
            $custom.request.ajaxSync(url,{"json":jsonStr},function(result){
                if (result.state == '200' && result.data.code == '00') {
                    $custom.alert.success("保存成功！");
                    $load.a($module.params.loadOrderPageURL+'act=14002&id='+${bill.id} + '&pageSource=1');
                } else {
                    if (result.data.message) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("保存失败！");
                    }
                }
            });
        }
    }

    //全选
    $("#checkAll").on("click",function(){
        var flag = $("#checkAll").is(":checked");
        var checks = $("#table-list").find('tr').find('td').find("input[type='checkbox']");
        checks.each(function(){
            $(this).attr("checked", flag);
        });
    });

    function validNum(org){
        var regStrs = '^-?[1-9]\\d*$';
        var reg = new RegExp(regStrs);
        if (!reg.test(org.value)) {
            org.value = '';
        }
    }

    function initParentBrand() {
        $('select[name="superiorParentBrandCode"]').each(function(){
            var selectObj = $(this);
            var selectId = $(this).val();
            selectObj.empty();
            selectObj.append("<option value=''>请选择</option>");

            var width = "100%" ;
            if($(selectObj).hasClass("input_msg")){
                width = "136px" ;

                $(selectObj).removeClass("input_msg") ;
            } else if($(selectObj).hasClass("edit_input")){
                width = "70%" ;

                $(selectObj).removeClass("edit_input") ;
            }

            $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;

            $(selectObj).addClass("selectpicker").addClass("show-tick") ;
            $(selectObj).attr("data-live-search", "true") ;

            <c:forEach items="${brandSelectBeans}" var="t">
            selectObj.append("<option value='"+ ${t.value} +"'>"+ "${t.label}"+"</option>");
            </c:forEach>

            $(selectObj).selectpicker({width: width});
            $(selectObj).selectpicker('refresh');
            $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
            $(".selectpicker").prev().css({"z-index":"99"});
            $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"150px"}) ;
            if(selectId){
                $(selectObj).selectpicker('val', selectId);
            }
        })
    }

    function changeSortType() {
        var sortType = '${bill.sortType}';
        if (sortType != "2") {
            $("select[name='storePlatformList']").val('');
            $("select[name='storePlatformList']").attr("disabled","disabled");
        } else {
            $("select[name='storePlatformList']").removeAttr("disabled");
        }
    }

    function calculateTaxAmount(obj) {
        var taxRate = $(obj).val();
        if(taxRate == "" || taxRate == null ){
            taxRate=0;
        }
        var tr= $(obj).parents("tr");
        var amount = tr.find("input[name='price']").val();

        debugger
        var taxAmount = 0;
        var tax = 0;
        if(!!taxRate && !!amount){
            taxAmount = parseFloat(amount)*(1+parseFloat(taxRate));
            taxAmount = parseFloat(taxAmount).toFixed(2);
            tax = parseFloat(taxAmount) - parseFloat(amount);
            tax=parseFloat(tax).toFixed(2);
        }
        tr.find("input[name='taxAmount']").val(taxAmount);
        tr.find("input[name='tax']").val(tax);
    }

    function  calculateTaxPrice(obj){
        debugger
        //费用金额（不含税）
        var amount = $(obj).val();
        if(amount == "" || amount == null ) {
            amount = 0;
        }
        //税率
        var tr= $(obj).parents("tr");
        var taxRate = tr.find("input[name='taxRate']").val();
        if(taxRate == "" || taxRate == null ){
            taxRate=0;
        }
        debugger
        var taxAmount = 0;
        var tax = 0;
        if(!!taxRate && !!amount){
            taxAmount = parseFloat(amount)*(1+parseFloat(taxRate));
            taxAmount = parseFloat(taxAmount).toFixed(2);
            tax = parseFloat(taxAmount) - parseFloat(amount);
            tax=parseFloat(tax).toFixed(2);
        }
        tr.find("input[name='taxAmount']").val(taxAmount);
        tr.find("input[name='tax']").val(tax);
    }
</script>
