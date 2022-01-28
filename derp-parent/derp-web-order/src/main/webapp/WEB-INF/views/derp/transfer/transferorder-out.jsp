<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>

<!-- Start content -->
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
                                        <li class="breadcrumb-item"><a href="#">打托出库</a></li>
                                    </ol>
                                </div>
                            </div>
                            <div class="title_text">基本信息</div>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <input value="${transferOrder.id}" type="hidden" id="orderId"/>
                                        <span>调拨订单编号：</span>
                                        <span>
                                            ${transferOrder.code}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调出仓库：</span>
                                        <span>
                                            ${transferOrder.outDepotName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>调入仓库：</span>
                                        <span>
                                            ${transferOrder.inDepotName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>事业部：</span>
                                        <span>
                                            ${transferOrder.buName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建人：</span>
                                        <span>
                                            ${transferOrder.createUsername}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>提交时间：</span>
                                        <span>
                                            <fmt:formatDate value="${transferOrder.submitTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 ml10">
                                <div class="row">
                                    <div class="col-10">
                                        <div class="title_text">出库信息</div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-12 ml10">
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <label><i class="red">*</i>出库时间： </label>
                                            <input type="text" class="edit_input form_datetime date-input"
                                                   name="transferOutDate" id="transferOutDate" />
                                        </div>
                                        <div class="info_text">

                                        </div>
                                        <div class="info_text">

                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <label>&nbsp;&nbsp;附件上传： </label>
                                            <button type="button"
                                                    class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                                    id="btn-file">上传理货报告
                                            </button>
                                            <form enctype="multipart/form-data" id="form-upload">
                                                <input type="file" name="file" id="file" class="btn-hidden file-import">
                                            </form>
                                        </div>
                                        <div class="info_text">
                                            <a href="#" id="fileName" onclick="void(0) ;"></a>
                                        </div>
                                        <div class="info_text">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row mt20 ml15 table-responsive">
                                <div class="mt10" style="margin-bottom: 10px; font-size: 13px">
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn-sm"
                                            id="allShelf" name="allShelf">整批打托
                                    </button>
                                </div>

                                <table id="table-list" class="table table-striped" >
                                    <thead>
                                    <tr>
                                        <th><input id="checkbox11" type="checkbox" name="all">全选</th>
                                        <th>商品货号</th>
                                        <th>商品名称</th>
                                        <th>条形码</th>
                                        <th>海外仓理货单位</th>
                                        <th>计划调出量</th>
                                        <th><i class="red">*</i>出库好品量</th>
                                        <th><i class="red">*</i>出库坏品量</th>
                                        <th>批次号</th>
                                        <th>生产日期</th>
                                        <th>失效日期</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${orderItem }" var="item">
                                        <tr>
                                            <td>
                                                <input type='checkbox' name='item-checkbox'>
                                                <input type='hidden' name='goodsId' value='${item.outGoodsId}'>
                                            </td>
                                            <td>${item.outGoodsNo}<input type="hidden" name="goodsNo"
                                                                      value="${item.outGoodsNo}"/></td>
                                            <td>${item.outGoodsName}<input type="hidden" name="goodsName"
                                                                        value="${item.outGoodsName}"/></td>
                                            <td>${item.outBarcode}<input type="hidden" name="barcode"
                                                                      value="${item.outBarcode}"/></td>
                                            <td>
                                                <input type="hidden" name="outUnit" value="${item.outUnit}"/>
                                                <c:if test="${item.outUnit eq '00' }">托盘</c:if>
                                                <c:if test="${item.outUnit eq '01' }">箱</c:if>
                                                <c:if test="${item.outUnit eq '02' }">件</c:if>
                                            </td>
                                            <td>${item.transferNum}<input type="hidden" name='outTransferNum'
                                                                  style='width:70px;text-align:center;'
                                                                  class='goods-num' value="${item.transferNum}"></td>
                                            <td><input type="text" name="transferNum" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9"/></td>
                                            <td><input type="text" name="wornNum" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9" value="0"/></td>
                                            <td><input type="text" name="batchNo" style='width:70px;text-align:center;' /></td>
                                            <td><input type="text" name="productionDate" class="date-input form-control" style='width:70px;' /></td>
                                            <td><input type="text" name="overdueDate" class="date-input form-control" style='width:70px;' /></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="form-row mt20 ml15 attachDetail">
                                <div class='col-12'>
                                    <div class="info-box">
                                        <div class="title_text">附件列表</div>
                                        <div id="attachmentTable" multiple></div>
                                    </div>
                                </div>
                            </div>

                            <!--   商品信息  end -->
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-6">
                                            <button type="button"
                                                    class="btn btn-info waves-effect waves-light fr btn_default"
                                                    id="save-buttons">保 存
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
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
            <!-- 弹窗开始 -->
            <!-- 弹窗结束 -->
            <!-- end row -->
        </div>
        <!-- container -->
    </div>

    <!-- 用于附件上传code -->
    <input id="code" type="hidden" value="${transferOrder.code}">

</div>
<!-- content -->
<script type="text/javascript">
    $(document).ready(function () {
        $(function () {
            //取消按钮
            $("#cancel-buttons").click(function () {
                $module.load.pageOrder('act=8001');
            });
        });

        // 点击整批打托
        $("#allShelf").click(function () {
            var flag = true;
            $('#table-list tbody tr').each(function (i) {
                if ($(this).find('input[name="item-checkbox"]').is(":checked")) {
                    $(this).find('input[name="transferNum"]').val($(this).find('input[name="outTransferNum"]').val());
                    $(this).find('input[name="wornNum"]').val(0);
                    flag = false;
                } else {
                    $(this).find('input[name="transferNum"]').val(0);
                    $(this).find('input[name="wornNum"]').val(0);
                }
            });
            // 如果一个框都没选,则提示
            if (flag) {
                $custom.alert.error("您还没未勾选出库商品,请勾选出库商品!");
                return;
            }
        });

        //保存按钮
        $("#save-buttons").click(function () {
            var outDepotBatchValidation = '${transfer.outDepotBatchValidation}';
            var transferOutDate = $('#transferOutDate').val();
            if (!transferOutDate) {
                $custom.alert.error("出库时间不能为空");
                return;
            }
            var id = $("#orderId").val();

            var check = true;
            //入库时间限制
            var outDepotUrl = serverAddr+"/transfer/validOutDepotDate.asyn";
            $custom.request.ajax(outDepotUrl,{"id":id, "transferOutDate":transferOutDate},function(result){
                if (result.state == '200' ) {
                    if(result.data.code!='00'){
                        check = false;
                        $custom.alert.warningText(result.data.message);
                    }
                    if (check) {
                        var validUrl = serverAddr+"/transfer/isExistTransferOutOrInOrder.asyn"
                        $custom.request.ajax(validUrl,{"id":id, "orderType":0},function(result){
                            if(result.data.code!='00'){
                                $custom.alert.warningText(result.data.message);
                                $module.load.pageOrder('act=8001');
                            }else{
                                var goodsNos = [];
                                var goodsList = [];
                                $('#table-list tbody tr').each(function (i) {// 遍历 tr
                                    var goodsId = $(this).find('input[name="goodsId"]').val();
                                    var goodsNo = $(this).find('input[name="goodsNo"]').val();
                                    var goodsName = $(this).find('input[name="goodsName"]').val();
                                    var goodsCode = $(this).find('input[name="goodsName"]').val();
                                    var goodsName = $(this).find('input[name="goodsName"]').val();
                                    var barcode = $(this).find('input[name="barcode"]').val();
                                    var tallyingUnit = $(this).find('input[name="tallyingUnit"]').val();
                                    var outTransferNum = $(this).find('input[name="outTransferNum"]').val();
                                    var transferNum = $(this).find('input[name="transferNum"]').val();
                                    var wornNum = $(this).find('input[name="wornNum"]').val();
                                    var batchNo = $(this).find('input[name="batchNo"]').val();
                                    var productionDate = $(this).find('input[name="productionDate"]').val();
                                    var overdueDate = $(this).find('input[name="overdueDate"]').val();

                                    // 好坏品量都不能为空，且需大于等于0
                                    if (!transferNum) {
                                        check = false;
                                        $custom.alert.error("入库好品量不能为空");
                                        return;
                                    }

                                    if (!wornNum) {
                                        check = false;
                                        $custom.alert.error("入库坏品量不能为空");
                                        return;
                                    }

                                    //若出库仓库为批次效期强校验，则批次效期必填
                                    if (outDepotBatchValidation == '1') {
                                        if (!batchNo) {
                                            check = false;
                                            $custom.alert.error("出库仓库为批次效期强校验，批次不能为空");
                                            return;
                                        }

                                        if (!productionDate) {
                                            check = false;
                                            $custom.alert.error("出库仓库为批次效期强校验，生产日期不能为空");
                                            return;
                                        }

                                        if (!overdueDate) {
                                            check = false;
                                            $custom.alert.error("出库仓库为批次效期强校验，失效日期不能为空");
                                            return;
                                        }
                                    }

                                    var totalNum = parseInt(transferNum) + parseInt(wornNum);
                                    if (totalNum != parseInt(outTransferNum)) {
                                        goodsNos.push(goodsNo);
                                    }

                                    if (!(transferNum == 0 && wornNum == 0)) {
                                        var goods = {};
                                        goods.outGoodsId = goodsId;
                                        goods.outGoodsNo = goodsNo;
                                        goods.outGoodsName = goodsName;
                                        goods.tallyingUnit = tallyingUnit;
                                        goods.transferNum = transferNum;
                                        goods.barcode = barcode;
                                        goods.wornNum = wornNum;
                                        goods.transferBatchNo = batchNo;
                                        goods.productionDateStr = productionDate;
                                        goods.overdueDateStr = overdueDate;
                                        goodsList.push(goods);
                                    }

                                });

                                if (check) {
                                    var json = {};
                                    json.transferOutDate = transferOutDate;
                                    json.transferOrderId = id ;
                                    json.goodsList = goodsList;
                                    var jsonStr = JSON.stringify(json);
                                    if (goodsList.length == 0) {
                                        $custom.alert.error("上架商品至少存在一个入库量！");
                                        return;
                                    }
                                    if (goodsNos.length > 0) {
                                        $custom.alert.warning("货号:" + goodsNos.join(',') +  "上架总量不等于计划调出量，请确认是否提交",function(){
                                            var url = serverAddr+"/transferOut/saveTransferOutDepot.asyn";
                                            $custom.request.ajax(url,{"json":jsonStr},function(result){
                                                if (result.state == '200' ) {
                                                    if (result.data.code == '00') {
                                                        $custom.alert.success("保存成功！");
                                                        setTimeout(function () {
                                                            $module.load.pageOrder('act=8001');
                                                        }, 1000);
                                                    } else if (result.data.code == "01") {
                                                        var message = result.data.message;
                                                        if (message) {
                                                            $custom.alert.error(message);
                                                        } else {
                                                            $custom.alert.error("保存失败！");
                                                        }
                                                        $("#save-buttons").attr('disabled',false);
                                                    }
                                                }  else {
                                                    var message = result.expMessage;
                                                    if (message) {
                                                        $custom.alert.error(message);
                                                    } else {
                                                        $custom.alert.error("保存失败！");
                                                    }
                                                    $("#save-buttons").attr('disabled',false);
                                                }
                                            });
                                        });
                                    } else {
                                        var url = serverAddr+"/transferOut/saveTransferOutDepot.asyn";
                                        $custom.request.ajax(url,{"json":jsonStr},function(result){
                                            if (result.state == '200' ) {
                                                if (result.data.code == '00') {
                                                    $custom.alert.success("保存成功！");
                                                    setTimeout(function () {
                                                        $module.load.pageOrder('act=8001');
                                                    }, 1000);
                                                } else if (result.data.code == "01") {
                                                    var message = result.data.message;
                                                    if (message) {
                                                        $custom.alert.error(message);
                                                    } else {
                                                        $custom.alert.error("保存失败！");
                                                    }
                                                    $("#save-buttons").attr('disabled',false);
                                                }
                                            }  else {
                                                var message = result.expMessage;
                                                if (message) {
                                                    $custom.alert.error(message);
                                                } else {
                                                    $custom.alert.error("保存失败！");
                                                }
                                                $("#save-buttons").attr('disabled',false);
                                            }
                                        });
                                    }
                                }

                            }
                        });
                    }
                } else {
                    $custom.alert.warningText(result.data.message);
                }

            });
        });
        // 点击全选
        $("input[name='all']").click(function () {
            //判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
            if ($(this).is(":checked")) {
                $("input[name='item-checkbox']").prop("checked", true);
            } else {
                $("input[name='item-checkbox']").prop("checked", false);
            }
        });

        $(".date-input").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2
        });

        //点击上传文件
        $("#btn-file").click(function () {
            var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
            $("#form-upload").empty();
            $("#form-upload").append(input);
            $("#file").click();
        });

        //上传文件到后端
        $("#form-upload").on("change", '.file-import', function () {

            var code = $("#code").val();

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

        var code = $("#code").val();
        $("#attachmentTable").createAttachTables(code);

    });

</script>
