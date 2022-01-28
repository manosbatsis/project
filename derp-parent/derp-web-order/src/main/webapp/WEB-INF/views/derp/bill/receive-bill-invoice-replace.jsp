<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">

        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="dripicons-location"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                <li class="breadcrumb-item"><a href="#">应收发票库</a></li>
                                <li class="breadcrumb-item"><a href="#">替换发票</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <!--  ================================上传文件部分 start  ===============================================   -->
                    <div class="form-row  col-12">
                        <div class="col-4">
                            <div class="row col-12">
                                <label class="col-4 col-form-label"
                                       style="white-space:nowrap;">上传文件<span>：</span></label>
                                <div class="col-7 mll">
                                    <input type="hidden" id="invoiceId" value="${invoiceId}" name="id">
                                    <button type="button" class="btn btn-gradient btn-file" id="btn-file">选择文件</button>
                                    <form enctype="multipart/form-data" id="form-upload">
                                        <input type="file" name="file" id="file" class="btn-hidden">
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--  ================================上传文件部分  end===============================================   -->
                </div>
            </div>
        </div>


        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div>
<!-- content -->
<footer class="footer text-right">
</footer>


<script type="text/javascript">
    //点击上传文件
    $("#btn-file").click(function () {
        $("#file").click();
    });

    //上传文件到后端
    $("#file").change(function () {
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length - 1];
        if (suffix.toLocaleLowerCase() == "pdf") {
            var formData = new FormData($("#form-upload")[0]);
            formData.append("id", $("#invoiceId").val());
            $custom.request.ajaxUpload(serverAddr + "/receiveBillInvoice/replaceInvoice.asyn", formData, function (result) {
                if (result.state == '200') {
                    if (result.data.code == '00') {
                        $custom.alert.success("替换成功！");
                    } else {
                        $custom.alert.error(result.data.message);
                    }
                } else {
                    $custom.alert.error(result.message);
                }
            });
        } else {
            $custom.alert.error("请上传正确的pdf格式文件");
        }
        $("#file").val("");//清空
    });

</script>