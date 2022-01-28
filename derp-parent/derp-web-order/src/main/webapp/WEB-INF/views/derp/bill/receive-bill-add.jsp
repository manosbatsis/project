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
                                        <li class="breadcrumb-item"><a href="#">新增</a></li>
                                    </ol>
                                </div>
                            </div>
                            <form id="add-form">
                                <div class="col-12 ml10">
                                    <div class="row">
                                        <div class="col-12">
                                            <span class="msg_title"><i class="red">*</i>分类 :</span>
                                            <select class="input_msg" name="sortType" id="sortType"></select>
                                            <span class="msg_title"><i class="red">*</i>客户 :</span>
                                            <select class="input_msg" name="customerId" id="customerId">
                                                <option value="">请选择</option>
                                            </select>
                                            <span class="msg_title"><i class="red">*</i>账单月份 :</span>
                                            <input type="text" parsley-type="text" class="input_msg date-input"
                                                   name="billMonth" id="billMonth">
                                        </div>
                                        <div class="col-12 mt10">
                                            <span class="msg_title"><i class="red">*</i>事业部 :</span>
                                            <select class="input_msg" name="buId" id="buId"></select>
                                            <span class="msg_title"><i class="red">*</i>结算币种 :</span>
                                            <select class="input_msg" name="currency" id="currency"></select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 ml10">
                                    <div class="row">
                                        <div class="col-3 mt10">
                                            <button type="button"
                                                    class="btn btn-find waves-effect waves-light btn_default"
                                                    onclick="addtr();">添加补扣款
                                            </button>
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"  onclick='importItems()'>导入</button>
                                            <button type="button"
                                                    class="btn btn-find waves-effect waves-light btn_default"
                                                    onclick="deltr();">删 除
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="row col-12 mt20 " style="min-height:350px; overflow-x: auto;">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0">
                                        <thead>
                                        <tr id="table-th">
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
                                            <th><i class="red" id="platformRed">*</i>核销平台</th>
                                            <th>备注</th>
                                        </tr>
                                        </thead>
                                        <tbody id="table-list">
                                        <tr>
                                            <td class="tc nowrap"><input type="checkbox" name="item-checkbox"></td>
                                            <td class="tc nowrap">
                                                <input name="projectId" class="form-control" type="hidden">
                                                <input name="projectName" class="form-control" type="text" onclick="chooseSettlement(this)">
                                            </td>
                                            <td class="tc nowrap">
                                                <select name="billType" class="form-control" style="width: 100px;">
                                                    <option value="">请选择</option>
                                                    <option value="0">补款</option>
                                                    <option value="1">扣款</option>
                                                </select>
                                            </td>
                                            <td class="tc nowrap"><input type="text" name="poNo" class="form-control" style="width: 150px;">
                                            </td>
                                            <td><input name="invoiceDescription" type="text" class="form-control" style="width: 180px;"></td>
                                            <td><input type="hidden" class="form-control" name="goodsId">
                                                <button type="button"
                                                        class="btn btn-find waves-effect waves-light btn_default"
                                                        onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品
                                                </button>
                                            </td>
                                            <td><input type="text" name="goodsName" class="form-control" readonly></td>
                                            <td>
                                                <select class="input_msg" name="superiorParentBrandCode">
                                                </select>
                                            </td>
                                            <td><input name="num" type="text" class="form-control" style="width: 50px;"
                                                       onblur="validNum(this)"></td>
                                            <td><input name="taxRate" type="text" class="form-control"
                                                       onkeyup="value=value.replace(/[^\d^\.]/g,'')"></td>
                                            <td><input name="price" type="text" class="form-control"
                                                       onkeyup="value=value.replace(/[^\d^\.]/g,'')"></td>

                                            <td>
                                                <select class="input_msg" name="storePlatformList" disabled>
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${storePlatformList }" var="storePlatform" >
                                                        <option value='${storePlatform.value }' >${storePlatform.label}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td><input name="remark" type="text" class="form-control" style="width: 180px;"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                            <!--   商品信息  end -->
                            <div class="form-row m-t-50">
                                <div style="margin:0 auto;">
                                    <button type="button"
                                            class="btn btn-info waves-effect waves-light btn_default"
                                            id="save-temp-buttons" onclick="saveBill();">生成应收单
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
        <jsp:include page="/WEB-INF/views/modals/13019.jsp"/>
        <!-- 弹窗结束 -->
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">

    $(document).ready(function () {
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
        $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
        $module.constant.getConstantListByNameURL.call($('select[name="currency"]')[0],"currencyCodeList",'${bill.settlementCurrency}');
        $module.constant.getConstantListByNameURL.call($('select[name="sortType"]')[0],"receiveBill_sortTypeList",'');

        laydate.render({
            elem: '#billMonth', //指定元素
            type: "month"
        });
        $("#platformRed").hide();
        initParentBrand();

    });

    //添加行
    function addtr() {
        $("#datatable-buttons").append('<tr>' +
            '<td class="tc nowrap"><input type="checkbox" name="item-checkbox" class="iCheck"></td>' +
            '<td class="tc nowrap">' +
            '<input name="projectId" type="hidden">' +
            '<input name="projectName" type="text" class="form-control" onclick="chooseSettlement(this)">' +
            '</td>' +
            '<td class="tc nowrap">' +
            '<select name="billType" class="form-control" style="width: 100px;">' +
            '<option value="">请选择</option>' +
            '<option value="0">补款</option>' +
            '<option value="1">扣款</option>' +
            '</select>' +
            '</td>' +
            '<td class="tc nowrap"> <input type="text" name="poNo" class="form-control"></td>' +
            '<td><input name="invoiceDescription" type="text" class="form-control"></td>'+
            '<td><input type="hidden" class="form-control" name="goodsId">' +
            '<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(8, this, null);">选择商品' +
            '</button>' +
            '</td>' +
            '<td><input type="text" name="goodsName" class="form-control" readonly></td>' +
            '<td><select class="input_msg" name="superiorParentBrandCode" >' +
            '</select></td>' +
            '<td><input name="num" type="text" class="form-control"  onblur="validNum(this)"></td>' +
            '<td><input name="taxRate" type="text" class="form-control" onkeyup="value=value.replace(/[^\\d^\\.]/g,\'\')"></td>' +
            '<td><input name="price" type="text" class="form-control" onkeyup="value=value.replace(/[^\\d^\\.]/g,\'\')"></td>' +

            '<td><select class="input_msg" name="storePlatformList" >' +
            '<option value="">请选择</option>' +
            '<c:forEach items="${storePlatformList }" var="storePlatform" >' +
            '<option value=\'${storePlatform.value }\' >${storePlatform.label}</option>' +
            '</c:forEach>' +
            ' </select>' +
            '</td>' +
            '<td><input name="remark" type="text" class="form-control">' +
            '</td>');
        initParentBrand();
        changeSortType();
    }

    //删除行
    function deltr() {
        $("input[name='item-checkbox']:checked").each(function () { // 遍历选中的checkbox
            var n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
            $("#table-list").find("tr:eq(" + n + ")").remove();
        });
    }

    //取消按钮
    $("#cancel-buttons").click(function () {
        $module.load.pageOrder('act=14001');
    });

    //保存
    function saveBill() {
        var check = true;
        var sortType = $("#sortType").val();
        if (!sortType) {
            $custom.alert.error("分类不能为空！");
            check = false;
            return;
        }
        var customerId = $("#customerId").val();
        if (!customerId) {
            $custom.alert.error("客户不能为空！");
            check = false;
            return;
        }
        var buId = $("#buId").val();
        if (!buId) {
            $custom.alert.error("事业部不能为空！");
            check = false;
            return;
        }
        var currency = $("#currency").val();
        if (!currency) {
            $custom.alert.error("结算币种不能为空！");
            check = false;
            return;
        }
        var billMonth = $("#billMonth").val();
        if (!billMonth) {
            $custom.alert.error("应收月份不能为空！");
            check = false;
            return;
        }
        var goodsList = []
        $("#datatable-buttons tbody tr").each(function (index, item) {
            var projectId = $(this).find("td").eq(1).find("input[name=projectId]").val();
            var projectName = $(this).find("td").eq(1).find("input[name=projectName]").val();
            var billType = $(this).find("td").eq(2).find("select").val();
            var poNo = $(this).find("td").eq(3).find("input").val();
            var goodsId = $(this).find("td").eq(5).find("input").val();
            var goodsNo = $(this).find("td").eq(5).find("span").text();
            var goodsName = $(this).find("td").eq(6).find("input").val();
            var brandParent = $(this).find("td").eq(7).find("option:selected").val();
            var brandParentName = $(this).find("td").eq(7).find("option:selected").text();
            var num = $(this).find("td").eq(8).find("input").val();
            var taxRate = $(this).find("td").eq(9).find("input").val();
            var price = $(this).find("td").eq(10).find("input").val();
            var invoiceDescription = $(this).find("td").eq(4).find("input").val();
            var storePlatformCode = $(this).find("td").eq(11).find("option:selected").val();
            var storePlatformName = $(this).find("td").eq(11).find("option:selected").text();
            var remark = $(this).find("td").eq(12).find("input").val();
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

            if (!taxRate) {
                $custom.alert.error("税率不能为空！");
                check = false;
                return false;
            }

            if (!price) {
                $custom.alert.error("费用金额不能为空！");
                check = false;
                return false;
            }

            if (!brandParent) {
                $custom.alert.error("母品牌不能为空！");
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
            goods.taxRate = taxRate;
            goods.price = price;
            goods.invoiceDescription = invoiceDescription;
            goods.storePlatformCode = storePlatformCode;
            goods.storePlatformName = storePlatformName;
            goods.remark = remark;
            goodsList.push(goods);
        });
        if (check && goodsList.length == 0) {
            $custom.alert.error("至少存在一条结算明细！");
            check = false;
            return;
        }
        if (check) {
            var json = {};
            json.sortType = sortType;
            json.customerId = customerId;
            json.buId = buId;
            json.currency = currency;
            json.billMonth = billMonth;
            json.costItemList = goodsList;
            var url = serverAddr + "/receiveBill/saveAddReceiveBill.asyn?";
            var jsonStr = JSON.stringify(json);
            $custom.request.ajaxSync(url, {"json": jsonStr}, function (result) {
                if (result.state == '200' && result.data.code == '00') {
                    $custom.alert.success("保存成功！");
                    $module.load.pageOrder('act=14002&id=' + result.data.billId + '&pageSource=1');
                } else {
                    var message = result.data.msg;
                    if (message) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("保存失败！");
                    }
                }
            });
        }
    }

    //全选
    $("#checkAll").on("click", function () {
        var flag = $("#checkAll").is(":checked");
        var checks = $("#table-list").find('tr').find('td').find("input[type='checkbox']");
        checks.each(function () {
            $(this).attr("checked", flag);
        });
    });

    function validNum(org) {
        var regStrs = '^-?[1-9]\\d*$';
        var reg = new RegExp(regStrs);
        if (!reg.test(org.value)) {
            org.value = '';
        }
    }

    function chooseSettlement(obj) {
        var customerId = $("#customerId").val();
        if (!customerId) {
            $custom.alert.error("请选择客户");
        }
        selectTable(obj, customerId)
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
            selectObj.append("<option value='"+ ${t.value} +"'>"+ "${t.label}" +"</option>");
            </c:forEach>

            $(selectObj).selectpicker({width: width});
            $(selectObj).selectpicker('refresh');
            $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
            $(".selectpicker").prev().css({"z-index":"99"});
            $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
            if(selectId){
                $(selectObj).selectpicker('val', selectId);
            }
        })
    }

    $("#sortType").change(function () {
        changeSortType();
    });

    function changeSortType() {
        var sortType = $("#sortType").val();
        if (sortType != "2") {
            $("select[name='storePlatformList']").val('');
            $("select[name='storePlatformList']").attr("disabled","disabled");
            $("#platformRed").hide();
        } else {
            $("select[name='storePlatformList']").removeAttr("disabled");
            $("#platformRed").show();
        }
    }

    function importItems() {
        var customerId = $("#customerId").val();
        if (!customerId) {
            $custom.alert.error("请先选择客户！");
            return;
        }
        $m13019.init(customerId);
    }

    function formatStr(obj) {
        if (typeof obj == "number") {
            return obj;
        }
        if (typeof obj == "undefined" || obj == null || obj == "") {
            return '';
        }

        return obj;
    }

</script>
