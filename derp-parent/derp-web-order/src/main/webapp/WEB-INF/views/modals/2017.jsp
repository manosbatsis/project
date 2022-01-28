<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog modal-lg" style="margin:10% auto;">
            <div class="modal-content col-9" style="margin:0 auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">商品导入</h5>
                </div>
                <div class="modal-body" >
                        <div class="form-row  col-12" style="text-align:center;">
                            <div class="col-6">
                                <div class="row col-12">
                                    <label  class="col-3 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-4 mll">
                                        <button type="button" class="btn btn-gradient btn-file" id="btn-file">选择文件</button>
                                        <form enctype="multipart/form-data" id="form-upload">
                                            <input type="file" name="file" id="file" class="btn-hidden file-import">
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-2" style="margin-top:10px;">
                            </div>
                            <div class="col-4" style="margin-top:10px;" id="downloadTemplate">
                                <a href="#">模板下载</a>
                            </div>
                        </div>

                        <div class="col-10 mt10 p-20 ml30"  id="border">
                            <div class="col-12 row mt10">
                                <h5 class="black">导入详情:</h5>
                            </div>
                            <div class="col-12 row mt10">
                                <div class="col-2"  style="white-space:nowrap;">新增数据<span>：</span></div>
                                <div class="col-2 ml15"><span id="successCount">0</span>条</div>
                            </div>
                            <div class="col-12 row mt10">
                                <div class="col-2"  style="white-space:nowrap;">错误数据<span>：</span></div>
                                <div class="col-2 ml15"><span id= "failureCount" class="red">0</span>条</div>
                            </div>
                            <div class="col-12 row mt10">
                                <table id="table-list_tc" class="table table-striped">
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

                        <div class="col-10 mt10 p-20 ml30"  id="border">
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
    var $m2017={
        init:function(){
            this.show();
        },
        show:function(){
            $($m2017.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m2017.params.modalId).modal("hide");
        },
        params:{
            modalId:'#signup-modal',
        },
    };

    //点击上传文件
    $("#btn-file").click(function(){
        var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
        $("#form-upload").empty();
        $("#form-upload").append(input);
        $("#file").click();
    });

    //上传文件到后端
    $("#form-upload").on("change",'.file-import',function(){
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length-1];
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#form-upload")[0]);
            $custom.request.ajaxUpload(serverAddr + "/sdPurchaseConfig/importSdPurchaseConfig.asyn", formData , function(result){
                if(result.state == '200'){
                	
                	$custom.alert.success("导入完成。");
                	
                	var itemList = result.data["mutiItemList"] ;
                	
                	if(itemList){
                		
                		$(itemList).each(function(index, item){
                			
                			var html = "<tr>" ;
                			
                			html += "<td><input type='checkbox' class='muti-iCheck'></td>" ;
                			html += "<td><select style='width: 80px;' class='edit_input multiSelect' onchange='getDetails(this, \"2\")' required reqMsg='SD类型不能为空'><option value=''>请选择</option></select></td>" ;
                			html += "<td>"+ item.sdConfigSimpleName +"</td>" ;
                			html += "<td>" + item.brandParent + "</td>" ;
                			html += "<td><input type='hidden' class='commbarcode' value='"+ item.commbarcode +"'>" + item.commbarcode + "</td>" ;
                			html += "<td>" + item.goodsName + "</td>" ;
                			html += "<td><input class='edit_input proportion' type='text'required reqMsg='比例不能为空' value='"+ item.proportion +"'></td>" ;
                			
                			html += "</tr>" ;
                			
                			$("#multi-tbody").append(html) ;
                			
                			getSelect("2", item.sdConfigId) ;
                			
                		}) ;
                		
                		$("#skus").html("合计SKU：" + $("#multi-tbody").find("tr").length) ;
                		
                	}
                	
                    
                }else{
                    $custom.alert.error("导入失败！");
                }
                $("#successCount").text(result.data["success"]);//新增条数
                $("#failureCount").text(result.data["failure"]);//错误条数
                //错误信息
                var msgList = result.data.message;
                var str="";
                for(var i = 0; i<msgList.length;i++){
                    var map = msgList[i];
                    str+="<tr>"
                        +"   <td>"+map.rows+"</td>"
                        +"   <td>"+map.message+"</td>"
                        +"</tr>";
                }
                $('#table-list_tc tbody').html(str);
            });
        }else{
            $custom.alert.error("请上传正确的excel格式文件");
        }
    });

    // 模板下载
    $("#downloadTemplate").click(function(){
        $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=148");
    }) ;


</script>