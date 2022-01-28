<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form">
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
                                        <li class="breadcrumb-item"><a href="#">to_c结算列表</a></li>
                                        <li class="breadcrumb-item"><a href="#">新增</a></li>
                                    </ol>
                                </div>
                            </div>
                            <div class="edit_box mt20">
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="settlementDate_i">*</i>结算日期 ：</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="settlementDate"
                                           id="settlementDate" value="${bill.settlementDateStr}">
                                </div>
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="buId_i">*</i>事业部 ：</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                </div>
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="shopTypeCode_i">*</i>运营类型 ：</span>
                                    <select class="input_msg" name="shopTypeCode" id="shopTypeCode"></select>
                                </div>
                            </div>
                            <div class="edit_box mt20">
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="storePlatformCode_i">*</i>平台名称 ：</span>
                                    <select class="input_msg" name="storePlatformCode" id="storePlatformCode"></select>
                                </div>
                                <div class="edit_item" >
                                    <span class="msg_title"><i class="red" id="shopCode_i">*</i>店铺名称 ：</span>
                                    <select name="shopCode" class="input_msg goods_select2" id="shopCode" disabled>
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="settlementCurrency_i">*</i>结算币种 ：</span>
                                    <select class="input_msg" name="settlementCurrency" id="settlementCurrency"></select>
                                </div>
                            </div>
                            <div class="edit_box mt20">
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="customerId_i">*</i>客户 ：</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                                <div class="edit_item">
                                    <span class="msg_title">外部结算单号：</span>
                                    <input type="text" class="input_msg" name="externalCode"
                                           id="externalCode" value="${bill.externalCode}">
                                </div>
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="oriCurrency_i">*</i>平台结算原币 ：</span>
                                    <select class="input_msg" name="oriCurrency" id="oriCurrency">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                            <div class="edit_box mt20">
                                <div class="edit_item">
                                    <span class="msg_title"><i class="red" id="file_i">*</i>导入结算单 ：</span>
                                    <button type="button" class="btn btn-sm btn-gradient btn-file"
                                            id="btn-file">上传文件
                                    </button>
                                    <a style="padding: 10px; color: blue;" id="downloadTemplate">通用模板下载</a>
                                    <form enctype="multipart/form-data" id="form-upload">
                                        <input type="file" name="file" id="file" class="btn-hidden">
                                    </form>
                                </div>
                                <div class="edit_item"></div>
                                <div class="edit_item"></div>
                            </div>
                            <!--  ================================导入信息显示部分  start ===============================================   -->
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <button type="button"
                                                    class="btn btn-info waves-effect waves-light fr btn_default"
                                                    id="save-buttons">生成应收单
                                            </button>
                                        </div>
                                        <div class="col-6">
                                            <button type="button"
                                                    class="btn btn-light waves-effect waves-light btn_default"
                                                    id="cancel-buttons">取 消
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--  ================================导入信息显示部分  end ===============================================   -->
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <div class="popupbg" style="display: none">
                        <div class="popup_box">
                            <img src="/resources/assets/images/uploading.gif" alt="">
                            <p>正在上传中...</p>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
            </div>
        </form>
    </div>
    <!-- container -->
</div>

<!-- content -->
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="shopTypeCode"]')[0],"merchantShopRel_shopTypeList",'${bill.shopTypeCode}');
    $module.constant.getConstantListByNameURL.call($('select[name="settlementCurrency"]')[0],"currencyCodeList",'${bill.settlementCurrency}');
    $module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",'${bill.storePlatformCode}');
    $module.constant.getConstantListByNameURL.call($('select[name="oriCurrency"]')[0],"currencyCodeList",'${bill.settlementCurrency}');
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],'${bill.buId}', null,null);

    $(document).ready(function () {
        var billId = '${bill.id}';
        if (billId) {
            $("#customerId").append("<option selected value='"+ ${bill.customerId}+"'>"+'${bill.customerName}'+"</option>");
            if (${bill.shopTypeCode == "001"}) {
                $("#shopCode").append("<option selected value='"+ '${bill.shopCode}' +"'>"+ '${bill.shopName}'+"</option>");
            }
        }

        laydate.render({
            elem: '#settlementDate', //指定元素
            value: '${bill.settlementDate}'
        });
        if (billId) {
            $("#shopCode").attr("disabled", true);
            $("#customerId").attr("disabled", true);
            $("#storePlatformCode").attr("disabled", true);
            $("#buId").attr("disabled", true);
            $("#shopTypeCode").attr("disabled", true);
            $("#settlementCurrency").attr("disabled", true);
            $("#oriCurrency").attr("disabled", true);
            $("#settlementDate").attr("disabled", true);
            $("#externalCode").attr("disabled", true);
        }

    });

    //点击上传文件
    $("#btn-file").click(function () {
        var settlementDate = $("#settlementDate").val();
        var buId = $("#buId").val();
        var shopTypeCode = $("#shopTypeCode").val();
        var storePlatformCode = $("#storePlatformCode").val();
        var shopCode = $("#shopCode").val();
        var settlementCurrency = $("#settlementCurrency").val();
        var oriCurrency = $("#oriCurrency").val();
        var customerId = $("#customerId").val();

        if (!settlementDate) {
            $custom.alert.error("请选择结算时间");
            return;
        }

        if (!buId) {
            $custom.alert.error("请选择事业部");
            return;
        }

        if (!shopTypeCode) {
            $custom.alert.error("请选择运营类型");
            return;
        }

        if (!storePlatformCode) {
            $custom.alert.error("请选择平台");
            return;
        }

        if (storePlatformCode == "001" && !shopCode) {
            $custom.alert.error("请选择店铺");
            return;
        }

        if (!settlementCurrency) {
            $custom.alert.error("请选择结算币种");
            return;
        }

        if (!customerId) {
            $custom.alert.error("请选择客户");
            return;
        }

        if (!oriCurrency) {
            $custom.alert.error("请选择平台结算原币");
            return;
        }

        var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
        $("#form-upload").empty();
        $("#form-upload").append(input);
        $("#file").click();

        $("#shopCode").attr("disabled", true);
        $("#customerId").attr("disabled", true);
        $("#storePlatformCode").attr("disabled", true);
        $("#buId").attr("disabled", true);
        $("#shopTypeCode").attr("disabled", true);
        $("#settlementCurrency").attr("disabled", true);
        $("#oriCurrency").attr("disabled", true);
        $("#settlementDate").attr("disabled", true);
        $("#externalCode").attr("disabled", true);
    });

    //上传文件到后端
    $("#save-buttons").on("click", function () {
        var billId = '${bill.id}';
        var settlementDate = $("#settlementDate").val();
        var buId = $("#buId").val();
        var shopTypeCode = $("#shopTypeCode").val();
        var storePlatformCode = $("#storePlatformCode").val();
        var shopCode = $("#shopCode").val();
        var settlementCurrency = $("#settlementCurrency").val();
        var customerId = $("#customerId").val();
        var externalCode = $("#externalCode").val();
        var oriCurrency = $("#oriCurrency").val();

        //判断是否为excel格式
        var file = $.find('#file')[0].files[0];

        if (!file) {
            $custom.alert.error("请上传文件");
            return;
        }

        var length = file.name.split(".").length;
        var suffix = file.name.split(".")[length-1];
        if (suffix == "xlsx" || suffix == "xls") {
            $(".popupbg").show();
            var formData = new FormData();
            formData.append('id', billId);
            formData.append('settlementDateStr', settlementDate);
            formData.append('buId', buId);
            formData.append('shopTypeCode', shopTypeCode);
            formData.append('storePlatformCode', storePlatformCode);
            formData.append('shopCode', shopCode);
            formData.append('settlementCurrency', settlementCurrency);
            formData.append('oriCurrency', oriCurrency);
            formData.append('customerId', customerId);
            formData.append('externalCode', externalCode);
            formData.append('file', file);
            $custom.request.ajaxUpload(serverAddr + "/toCReceiveBill/saveTocSettlementBill.asyn", formData, function (result) {
                $(".popupbg").hide();
                if (result.state == 200) {
                    $custom.alert.success("导入数据验证中，请稍后");
                    setTimeout($module.load.pageOrder("act=14018"), 1000);
                } else {
                    if (result.expMessage) {
                        $custom.alert.error(result.expMessage);
                    } else {
                        $custom.alert.error(result.message);
                    }

                }
            });
        } else {
            $custom.alert.error("请上传正确的excel格式文件");
        }
    });

    //返回
    $("#cancel-buttons").click(function () {
        $module.load.pageOrder("act=14018");
    });



    $("#storePlatformCode").change(function () {
        var storePlatformCode = $("#storePlatformCode").val();
        var shopTypeCode = $("#shopTypeCode").val();
        //获取店铺信息
        if (shopTypeCode == "001") {
            $custom.request.ajaxSync(serverAddr + "/toCReceiveBill/getShopInfo.asyn",
                {"storePlatformCode":storePlatformCode, "shopTypeCode":shopTypeCode}, function (result) {
                if (result.state == 200 && result.data && result.data.length > 0) {
                    var shopList = result.data ;
                    $("#shopCode").empty();
                    $("#shopCode").append('<option value="">请选择</option>');
                    for (var i=0; i<shopList.length; i++) {
                        $("#shopCode").append("<option value='"+ shopList[i].shopCode+"'>"+shopList[i].shopName+"</option>");
                    }
                }
            });
        }

    });

    $("#shopCode").click(function () {
        var shopTypeCode = $("#shopTypeCode").val();
        var storePlatformCode = $("#storePlatformCode").val();
        if (!shopTypeCode) {
            $custom.alert.error("请先选择运营类型！");
            return;
        }

        if (!storePlatformCode) {
            $custom.alert.error("请先选择平台名称！");
            return;
        }
        var shopCode = $("#shopCode").val();
        //获取客户和平台结算币种信息
        $custom.request.ajaxSync(serverAddr + "/toCReceiveBill/getShopInfo.asyn",
            {"storePlatformCode":storePlatformCode, "shopTypeCode":shopTypeCode, "shopCode" :shopCode},
            function (result) {
                if (result.state == 200 && result.data) {
                    var shop = result.data[0] ;
                    $("#customerId").empty();
                    $("#customerId").append('<option value="">请选择</option>');
                    $("#customerId").append("<option value='"+ shop.customerId+"'>"+shop.customerName+"</option>");
                    $("#oriCurrency").find("option[value='" + shop.currency + "']").attr("selected", "selected");
                } else {
                    $custom.alert.error(result.message);
                }
        });

    });

    $("#customerId").click(function () {
        var shopTypeCode = $("#shopTypeCode").val();
        if (!shopTypeCode) {
            $custom.alert.error("请先选择运营类型！");
            return;
        }

        /**1、当运营类型为一件代发时，可选枚举值为店铺信息表中已有维护的“一件代发”店铺关联的客户名称。
         * 2、当运营类型为”POP“时，根据已选择的店铺名称，获取现有店铺信息表中对应已有维护的客户名称。
         */
        if (shopTypeCode == '001') { //pop
            var shopCode = $("#shopCode").val();
            if (!shopCode) {
                $custom.alert.error("请先选择店铺！");
                return;
            }
        }
    });

    $("#shopTypeCode").change(function() {
        var shopTypeCode = $("#shopTypeCode").val();
        $("#storePlatformCode").val('');
        $("#shopCode").empty();
        $("#shopCode").append('<option value="">请选择</option>');
        $("#customerId").empty();
        $("#customerId").append('<option value="">请选择</option>');
        /**1、若运营类型为”POP“时，
         * 1.1 根据选择的平台名称，获取现有店铺信息表中对应已有维护的店铺信息选择。运营类型为POP时，店铺名称必填。
         * 1.3 根据已选择的店铺名称，获取现有店铺信息表中对应已有维护的客户名称。
         * 2、当运营类型为一件代发时，
         * 2.1 店铺名称隐藏字段，非必填；
         * 2.2 默认结算币种为港币；
         * 2.3 客户可选枚举值为店铺信息表中已有维护的“一件代发”店铺关联的客户名称
         */
        if (shopTypeCode == '001') { //pop
            $("#shopCode").attr("disabled", false);
        }
        if (shopTypeCode == '002') { //一件代发
            $("#shopCode").attr("disabled", true);
            $("#settlementCurrency").val("HKD");
            $custom.request.ajaxSync(serverAddr + "/toCReceiveBill/getShopInfo.asyn",
                {"shopTypeCode":shopTypeCode}, function (result) {
                    if (result.state == 200 && result.data && result.data.length > 0) {
                        var shopList = result.data ;
                        for (var i=0; i<shopList.length; i++) {
                            if (shopList[i].customerId) {
                                $("#customerId").append("<option value='"+ shopList[i].customerId+"'>"+shopList[i].customerName+"</option>");
                            }
                        }
                        deduplication("customerId");
                    }
                });

        }

    });

    //下拉框去重
    function deduplication(selectId){
        opts = $("#"+selectId).find("option");
        opts.each(function () {
            optVal = $(this).val();
            if(optVal == ""){
                return true;
            }
            if($("#"+selectId+" option[value='" + optVal + "']").length > 1) {
                $("#"+selectId+" option[value='" + optVal + "']:gt(0)").remove();
            }
        })
    }

    // 模板下载
    $("#downloadTemplate").click(function(){
        $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=151");
    })
</script>
