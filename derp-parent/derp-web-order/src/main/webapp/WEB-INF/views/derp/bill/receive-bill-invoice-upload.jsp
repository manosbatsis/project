<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
                                        <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                        <li class="breadcrumb-item"><a
                                                href="javascript:$module.load.pageOrder('act=14010');">应收发票库</a></li>
                                        <li class="breadcrumb-item"><a href="#">附件上传</a></li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <div class="info_box" style="margin-left: 20px;">
                                <div class="info_item">
                                    <div class="info_text">
                                        <span>发票号码：</span>
                                        <span>
                                            ${invoice.invoiceNo }
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>客户：</span>
                                        <span>
                                            ${invoice.customerName}
                                        </span>
                                    </div>
                                    <div class="info_text">
                                        <span>开票日期：</span>
                                        <span>
                                                <fmt:formatDate value="${invoice.invoiceDate}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row mt20 attachDetail">
                                <div class='col-12'>
                                    <div class="info-box">
                                        <button type="button" style="margin-left: 20px; margin-top: 10px;"
                                                class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                                id="btn-file">上传盖章发票
                                        </button>
                                        <span style="color: red">仅支持上传PDF、PNG、JPG格式类型文件</span>
                                        <form enctype="multipart/form-data" id="form-upload">
                                            <input type="file" name="file" id="file"
                                                   class="btn-hidden file-import">
                                        </form>
                                        <div id="attachmentTable"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-4"></div>
                                        <div class="col-4">
                                            <a class="btn btn-find waves-effect waves-light btn_default"
                                               href="javascript:void(0);" onclick="completeSignature();">完成发票签章</a>
                                        </div>
                                        <div class="col-4"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end row -->
                <!--======================Modal框===========================  -->
                <jsp:include page="/WEB-INF/views/modals/13009.jsp" />
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
</div>
<script>
    $(function () {
        sumTotal();
        var code = "${invoice.invoiceNo}";
        //点击上传文件
        $("#btn-file").click(function () {
            var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
            $("#form-upload").empty();
            $("#form-upload").append(input);
            $("#file").click();
        });

        //上传文件到后端
        $("#form-upload").on("change", '.file-import', function () {
            var file = $.find('#file')[0].files[0];
            var suffixs = file.name.split(".");
            var suffix = suffixs[suffixs.length-1];
            if (suffix.toLowerCase() == 'pdf' || suffix.toLowerCase() == 'png' || suffix.toLowerCase() == 'jpg') {
                var formData = new FormData($("#form-upload")[0]);
                $custom.request.ajaxUpload(serverAddr + "/attachment/uploadFiles.asyn?code=" + code, formData, function (result) {
                    if (result.state == 200 && result.data && result.data.code == 200) {
                        $attachTable.fn.reload();
                        $custom.alert.success("上传成功");
                    } else {
                        $custom.alert.error("上传失败");
                    }
                });
            } else {
                $custom.alert.error("仅支持上传PDF、PNG、JPG格式类型文件上传！");
            }
        });

        $("#attachmentTable").createAttachTables(code);
    });

    function returnBack() {
        var pageSource = '${pageSource}';
        if (pageSource == "1") {
            $load.a("/list/menu.asyn?act=100&r=" + Math.random());
        } else {
            $module.load.pageOrder('act=8001');
        }
    }

    /**
     *    汇总调出数量、调入数量
     */
    function sumTotal() {
        var transferInTotalNum = 0;
        $("#item-table").find("input[name='inTransferNum']").each(function (i, m) {
            let val = $(m).val();

            if (val && isNumber(val)) {
                val = parseInt(val);
                transferInTotalNum = parseInt(transferInTotalNum);
                transferInTotalNum += val;
                $("#transferInTotalNum").html(transferInTotalNum);
            }
        });


        var transferOutTotalNum = 0.0;
        $("#item-table").find("input[name='transferNum']").each(function (i, m) {
            let val = $(m).val();

            if (val && isNumber(val)) {
                val = parseInt(val);
                transferOutTotalNum = parseInt(transferOutTotalNum);
                transferOutTotalNum += val;
                $("#transferOutTotalNum").html(transferOutTotalNum);
            }

        });

        var totalNetWeight = 0.0;
        $("#item-table").find("input[name='netWeight']").each(function (i, m) {
            let val = $(m).val();

            if (val && isNumber(val)) {
                val = parseFloat(val);
                totalNetWeight = parseFloat(totalNetWeight);
                totalNetWeight += val;
                totalNetWeight = parseFloat(totalNetWeight).toFixed(3);

                $("#totalNetWeight").html(totalNetWeight);
            }

        });

        var totalGrossWeight = 0.0;
        $("#item-table").find("input[name='grossWeight']").each(function (i, m) {
            let val = $(m).val();

            if (val && isNumber(val)) {
                val = parseFloat(val);
                totalGrossWeight = parseFloat(totalGrossWeight);
                totalGrossWeight += val;
                totalGrossWeight = parseFloat(totalGrossWeight).toFixed(3);

                $("#totalGrossWeight").html(totalGrossWeight);
            }

        });
    }

    /**判断是否数字*/
    function isNumber(val) {
        if (val === "" || val == null) {
            return false;
        }
        if (!isNaN(val)) {
            //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
            //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
            return true;
        } else {
            return false;
        }

    }

    /**完成发票签章*/
    function completeSignature() {
        var id = '${invoice.id}';
        var type = '${invoice.invoiceType}';
        var invoiceRelIds = '${invoice.invoiceRelIds}';
        var url = serverAddr + "/receiveBillInvoice/modifyInvoice.asyn";
        $custom.alert.warning("确认完成发票签章？",function(){
            $custom.request.ajax(url, {"id":id}, function(result){
                if (result.state == '200') {
                    if (result.data.code == '00') {
                        var status = result.data.status;
                        if (type == '0') {
                            $m13009.init(id,status, type);
                        } else {
                            $m13009.init(invoiceRelIds,status, type);
                        }

                    } else {
                        $custom.alert.error(result.data.message);
                    }
                } else {
                    var message = result.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("完成发票签章失败！");
                    }
                }
            });
        });
    }
</script>
