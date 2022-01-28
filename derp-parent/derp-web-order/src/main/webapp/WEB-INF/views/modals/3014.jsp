<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="detail-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog modal-lg" style="margin:10% auto;">
            <div class="modal-content col-9" style="margin:0 auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">箱装明细导入</h5>
                </div>
                <div class="modal-body" >
                        <div class="form-row  col-12" style="text-align:center;">
                            <div class="col-6">
                                <div class="row col-12">
                                    <label  class="col-3 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-4 mll">
                                        <button type="button" class="btn btn-gradient btn-file" id="btn-detail-file">选择文件</button>
                                        <form enctype="multipart/form-data" id="detail-upload">
                                            <input type="file" name="file" id="detailFile" class="btn-hidden file-import">
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-2" style="margin-top:10px;">
                            </div>
                            <div class="col-4" style="margin-top:10px;" id="downloadDetailTemplate">
                                <!--                               <a href="javascript:void(0);" onclick="downloadTemp();"> 模版下载</a> -->
                                <a href="#">模板下载</a>
                            </div>
                        </div>

                        <div class="col-10 mt10 p-20 ml30"  id="border">
                            <div class="col-12 row mt10">
                                <h5 class="black">导入详情:</h5>
                            </div>
                            <div class="col-12 row mt10">
                                <div class="col-2"  style="white-space:nowrap;">新增数据<span>：</span></div>
                                <div class="col-2 ml15"><span id="successNum">0</span>条</div>
                            </div>
                            <div class="col-12 row mt10">
                                <div class="col-2"  style="white-space:nowrap;">错误数据<span>：</span></div>
                                <div class="col-2 ml15"><span id= "failureNum" class="red">0</span>条</div>
                            </div>
                            <div class="col-12 row mt10">
                                <table id="table-list-error" class="table table-striped">
                                    <thead>
                                    <tr>
                                        <td>错误行</td>
                                        <td>错误原因</td>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>

                        <div class="col-10 mt10 p-20 ml30" >
                            <div class="col-12 row mt10">
                                <h5 class="black"> 填写Excle模板小贴士:</h5>
                            </div>
                            <div class="col-12 row mt10">
                                <div class="col-1"  style="white-space:nowrap;">1.按照格式进行填写数据;</div>
                            </div>
                        </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m3014={
        init:function(orderId){
            $m3014.params.orderId = orderId;
            this.show();
        },
        show:function(){
            $($m3014.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m3014.params.modalId).modal("hide");
        },
        params:{
            modalId:'#detail-modal',
            orderId:''
        },
    };

    //点击上传文件
    $("#btn-detail-file").click(function(){
        var input = '<input type="file" name="file" id="detailFile" class="btn-hidden file-import">';
        $("#detail-upload").empty();
        $("#detail-upload").append(input);
        $("#detailFile").click();
    });

    //上传文件到后端
    $("#detail-upload").on("change",'.file-import',function(){
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length-1];
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#detail-upload")[0]);
            formData.append('id', $m3014.params.orderId);
            $custom.request.ajaxUpload(serverAddr+"/transfer/importPackingDetails.asyn", formData, function(result){
                var html = "";
                $("#table-list-error").empty();//清空错误消息=
                $("#successNum").text("0");//新增条数
                $("#failureNum").text("0");//错误条数
                if(result.state == '200' && result.data.failure <= 0){
                    $custom.alert.success("导入成功！");
                    $("#successNum").text(result.data.success);//新增条数
                    $m3014.hide();
                }else{
                    $custom.alert.error("导入失败！");
                    $("#failureNum").text(result.data.failure);//错误条数

                    $.each(result.data.errorList, function(index, item){
                        html += "<tr><td>"+ (item.row+1)+"</td><td>"+item.message+"</td></tr>";
                    });
                    $("#table-list-error").html(html);
                }
            });
        }else{
            $custom.alert.error("请上传正确的excel格式文件");
        }
    });

    // 模板下载
    $("#downloadDetailTemplate").click(function(){
        $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=157");
    })

</script>