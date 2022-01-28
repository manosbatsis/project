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
                                        <li class="breadcrumb-item"><a href="#">上架入库</a></li>
                                    </ol>
                                </div>
                            </div>
                            <div class="title_text">基本信息</div>
                            <div class="info_box">
                                <div class="info_item">
                                    <div class="info_text">
                                        <input value="${saleReturnOrder.id}" type="hidden" id="orderId"/>
                                        <span>销售退货订单编号：</span>
                                        <span>
                                            ${saleReturnOrder.code}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>退出仓库：</span>
                                        <span>
                                            ${saleReturnOrder.outDepotName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>退入仓库：</span>
                                        <span>
                                            ${saleReturnOrder.inDepotName}
                                        </span>
                                    </div>
                                </div>
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>事业部：</span>
                                        <span>
                                            ${saleReturnOrder.buName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>创建人：</span>
                                        <span>
                                            ${saleReturnOrder.createName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>提交时间：</span>
                                        <span>
                                            <fmt:formatDate value="${saleReturnOrder.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 ml10">
                                <div class="row">
                                    <div class="col-10">
                                        <div class="title_text">上架信息</div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-12 ml10">
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <label><i class="red">*</i>入库时间： </label>
                                            <input type="text" class="edit_input form_datetime date-input"
                                                   name="returnInDate" id="returnInDate" />
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
                                            id="allShelf" name="allShelf">整批上架
                                    </button>
                                </div>

                                <table id="table-list" class="table table-striped" style="width: 100%;">
                                    <thead>
                                    <tr>
                                        <th><input id="checkbox11" type="checkbox" name="all">全选</th>
                                        <th>商品货号</th>
                                        <th>商品名称</th>
                                        <th>条形码</th>
                                        <th>海外仓理货单位</th>
                                        <th>计划调入好品量</th>
                                        <th>计划调入坏品量</th>
                                        <th><i class="red">*</i>入库好品量</th>
                                        <th><i class="red">*</i>入库坏品量</th>
                                        <th style='text-align:center;'>批次号</th>
                                        <th style='text-align:center;'>生产日期</th>
                                        <th style='text-align:center;'>失效日期</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${orderItem }" var="item">
                                        <tr>
                                            <td>
                                                <input type='checkbox' name='item-checkbox'>
                                                <input type='hidden' name='goodsId' value='${item.inGoodsId}'>
                                                <input type='hidden' name='outGoodsId' value='${item.outGoodsId}'>
                                            </td>
                                            <td>${item.inGoodsNo}<input type="hidden" name="goodsNo"
                                                                      value="${item.inGoodsNo}"/></td>
                                            <td>${item.inGoodsName}<input type="hidden" name="goodsName"
                                                                        value="${item.inGoodsName}"/></td>
                                            <td>${item.barcode}<input type="hidden" name="barcode"
                                                                      value="${item.barcode}"/></td>
                                            <td>
                                                <input type="hidden" name="tallyingUnit" value="${saleReturnOrder.tallyingUnit}"/>
                                                <c:if test="${saleReturnOrder.tallyingUnit eq '00' }">托盘</c:if>
                                                <c:if test="${saleReturnOrder.tallyingUnit eq '01' }">箱</c:if>
                                                <c:if test="${saleReturnOrder.tallyingUnit eq '02' }">件</c:if>
                                            </td>
                                            <td>${item.returnNum}
                                            <input type="hidden" name='inReturnNum' style='width:70px;text-align:center;' class='goods-num' value="${item.returnNum}">
                                            </td>
                                            <td>${item.badGoodsNum}
                                            <input type="hidden" name='inBadGoodsNum' style='width:70px;text-align:center;' class='goods-num' value="${item.badGoodsNum}">
                                            </td>
                                            <td><input type="text" name="returnNum" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9" value="0"/></td>
                                            <td><input type="text" name="badGoodsNum" style='width:70px;text-align:center;' onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="9" value="0"/></td>
                                            <td style='text-align:center;'><input type="text" name='returnBatchNo' style='width:100px;text-align:center;' class='transferBatchNo-num' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
                                            <td style='text-align:center;'><input type="text" name='productionDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
                                            <td style='text-align:center;'><input type="text" name='overdueDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
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
    <input id="code" type="hidden" value="${saleReturnOrder.code}">

</div>
<!-- content -->
<script type="text/javascript">
    $(document).ready(function () {
        $(function () {
            //取消按钮
            $("#cancel-buttons").click(function () {
                $module.load.pageOrder('act=5001');
            });
        });

        // 点击整单上架按钮
        $("#allShelf").click(function () {
            var flag = true;
            $('#table-list tbody tr').each(function (i) {
                if ($(this).find('input[name="item-checkbox"]').is(":checked")) {
                    $(this).find('input[name="returnNum"]').val($(this).find('input[name="inReturnNum"]').val());
                    $(this).find('input[name="badGoodsNum"]').val($(this).find('input[name="inBadGoodsNum"]').val());
                    flag = false;
                } else {
                    $(this).find('input[name="returnNum"]').val(0);
                    $(this).find('input[name="badGoodsNum"]').val(0);
                }
            });
            // 如果一个框都没选,则提示
            if (flag) {
                $custom.alert.error("您还没未勾选上架商品,请勾选上架商品!");
                return;
            }
        });

        //保存按钮
        $("#save-buttons").click(function () {
            var returnInDate = $('#returnInDate').val();
            if (!returnInDate) {
                $custom.alert.error("入库时间不能为空");
                return;
            }
            var id = $("#orderId").val();
            var check = true;
            //入库时间限制
            var inDepotUrl = serverAddr+"/saleReturn/validInDepotDate.asyn";
            $custom.request.ajax(inDepotUrl,{"id":id, "returnInDate":returnInDate},function(result){           	
                if (result.state == '200' ) {
                    if(result.data.code!='00'){
                        check = false;
                        $custom.alert.warningText(result.data.message);
                    }
                    if (check) {
                        var goodsNos = [];
                        var goodsList = [];
                        $('#table-list tbody tr').each(function (i) {// 遍历 tr
                            var goodsId = $(this).find('input[name="goodsId"]').val();
                            var outGoodsId = $(this).find('input[name="outGoodsId"]').val();
                            var goodsNo = $(this).find('input[name="goodsNo"]').val();
                           // var goodsName = $(this).find('input[name="goodsName"]').val();
                           // var goodsCode = $(this).find('input[name="goodsName"]').val();
                           // var goodsName = $(this).find('input[name="goodsName"]').val();
                           // var barcode = $(this).find('input[name="barcode"]').val();
                            var tallyingUnit = $(this).find('input[name="tallyingUnit"]').val();
                            var inReturnNum = $(this).find('input[name="inReturnNum"]').val();
                            var inBadGoodsNum = $(this).find('input[name="inBadGoodsNum"]').val();
                            var badGoodsNum = $(this).find('input[name="badGoodsNum"]').val();
                            var returnNum = $(this).find('input[name="returnNum"]').val();
                            var returnBatchNo = $(this).find('input[name="returnBatchNo"]').val();	// 退货批次
                            var productionDate = $(this).find('input[name="productionDate"]').val();	// 生产日期
                            var overdueDate = $(this).find('input[name="overdueDate"]').val();	// 失效日期

                            // 好坏品量都不能为空，且需大于等于0
                            if (!returnNum) {
                                $custom.alert.error("入库好品量不能为空");
                                return;
                            }

                            if (!badGoodsNum) {
                                $custom.alert.error("入库坏品量不能为空");
                                return;
                            }

                            var totalNum = parseInt(returnNum) + parseInt(badGoodsNum);
                            var inTotalNum = parseInt(inReturnNum) + parseInt(inBadGoodsNum);
                            if (totalNum != inTotalNum) {
                                goodsNos.push(goodsNo);
                            }

                            if (!(returnNum == 0 && badGoodsNum == 0)) {
                                var goods = {};
                                goods.inGoodsId = goodsId;
                                goods.outGoodsId = outGoodsId;
                                goods.inGoodsNo = goodsNo;
                                goods.tallyingUnit = tallyingUnit;
                                goods.returnNum = returnNum;
                                goods.badGoodsNum = badGoodsNum;
                                goods.returnBatchNo = returnBatchNo;
                                goods.productionDate = productionDate;
                                goods.overdueDate = overdueDate;
                                goodsList.push(goods);
                            }

                        });
                        var json = {};
                        json.returnInDate = returnInDate;
                        json.orderId = id ;
                        json.goodsList = goodsList;
                        var jsonStr = JSON.stringify(json);
                        if (goodsList.length == 0) {
                            $custom.alert.error("上架商品至少存在一个出库量！");
                            return;
                        }
                        if (goodsNos.length > 0) {                       	
                            $custom.alert.warning("货号:" + goodsNos.join(',') +  "上架总量不等于计划调入量，请确认是否提交",function(){
                                var countUrl = serverAddr+"/saleReturn/saveSaleReturnIdepot.asyn";
                                $custom.request.ajax(countUrl,{"json":jsonStr},function(resData){
                                    if (resData.state == '200' ) {
                                        if (resData.data.code == '00') {
                                            $custom.alert.success("保存成功！");
                                            setTimeout(function () {
                                                $module.load.pageOrder('act=5001');
                                            }, 1000);
                                        } else if (resData.data.code == "01") {
                                            var message = resData.data.message;
                                            if (message) {
                                                $custom.alert.error(message);
                                            } else {
                                                $custom.alert.error("保存失败！");
                                            }
                                            $("#save-buttons").attr('disabled',false);
                                        }
                                    }  else {
                                        var message = resData.expMessage;
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
                            var countUrl = serverAddr+"/saleReturn/saveSaleReturnIdepot.asyn";
                            $custom.request.ajax(countUrl,{"json":jsonStr},function(resData){
                                if (resData.state == '200' ) {
                                    if (resData.data.code == '00') {
                                        $custom.alert.success("保存成功！");
                                        setTimeout(function () {
                                            $module.load.pageOrder('act=5001');
                                        }, 1000);
                                    } else if (resData.data.code == "01") {
                                        var message = resData.data.message;
                                        if (message) {
                                            $custom.alert.error(message);
                                        } else {
                                            $custom.alert.error("保存失败！");
                                        }
                                        $("#save-buttons").attr('disabled',false);
                                    }
                                }  else {
                                    var message = resData.expMessage;
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
