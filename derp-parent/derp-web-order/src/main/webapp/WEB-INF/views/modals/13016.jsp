<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="receive-item-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog modal-lg" style="margin:10% auto;">
            <div class="modal-content col-9" style="margin:0 auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">应收明细导入</h5>
                </div>
                <div class="modal-body" >
                        <div class="form-row  col-12" style="text-align:center;">
                            <div class="col-6">
                                <div class="row col-12">
                                    <label  class="col-3 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-4 mll">
                                        <button type="button" class="btn btn-gradient btn-file" id="detail-file">选择文件</button>
                                        <form enctype="multipart/form-data" id="item-form-upload">
                                            <input type="file" name="file" id="item-file" class="btn-hidden file-import">
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-2" style="margin-top:10px;">
                            </div>
                            <div class="col-4" style="margin-top:10px;" id="downloadTemplate">
                                <!--                               <a href="javascript:void(0);" onclick="downloadTemp();"> 模版下载</a> -->
                                <a href="#">明细导出</a>
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
    var $m13016={
        init:function(billId){
            $m13016.params.billId = billId;
            $('#table-list_tc tbody').html('');
            $("#successCount").text(0);//新增条数
            $("#failureCount").text(0);//错误条数
            this.show();
        },
        show:function(){
            $($m13016.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m13016.params.modalId).modal("hide");
        },
        params:{
            modalId:'#receive-item-modal',
            billId: ''
        },
    };

    //点击上传文件
    $("#detail-file").click(function(){
        var input = '<input type="file" name="file" id="item-file" class="btn-hidden file-import">';
        $("#item-form-upload").empty();
        $("#item-form-upload").append(input);
        $("#item-file").click();
    });

    //上传文件到后端
    $("#item-form-upload").on("change",'.file-import',function(){
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length-1];
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#item-form-upload")[0]);
            formData.append('billId',$m13016.params.billId);
            $custom.request.ajaxUpload(serverAddr+"/receiveBill/importReceiveItems.asyn?r=1", formData, function(result){
                if(result.state == '200' && result.data["failure"] <= 0){
                    var itemList = result.data.itemList;
                    var $tr = "";
                    if ($("#receive-detail-td-table tr").length <= 1){
                    	$custom.alert.success("导入完成。");
	                    $.each(itemList,function(index,event){
	                    	$tr += '<tr>'
                                +'<td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'
                                +'<input type="checkbox" name="receive-item-check" class="group-checkable" /></label></td>'
                                + '<td><input type="hidden" value="'+event.projectId+'">' + formatStr(event.code) + '</td>'
                                + '<td><input type="hidden" value="'+event.projectCode+'">' + formatStr(event.projectName) + '</td>'
                                + '<td>' + formatStr(event.poNo) + '</td>'
                                + '<td>' + formatStr(event.platformSku) + '</td>'
                                + '<td>' + formatStr(event.commbarcode) + '</td>'
                                + '<td><input type="hidden" value="event.goodsId">' + formatStr(event.goodsNo) + '</td>'
                                + '<td>' + formatStr(event.goodsName) + '</td>'
                                + '<td>' + formatStr(event.taxRate) + '</td>'
                                + '<td>' + formatStr(event.num) + '</td>'
                                + '<td>' + formatStr(event.price) + '</td>'
                                + '<td>' + formatStr(event.tax) + '</td>'
                                + '<td>' + formatStr(event.taxAmount) + '</td>'
                                + '<td><input type="hidden" name="parentBrandId" value="'+event.parentBrandId+'">' + formatStr(event.parentBrandName) + '</td>'
                                + '<td>' + formatStr(event.paymentSubjectName) + '</td>'
                                + '</tr>';
	                    });
                        $('#receive-detail-td-table').find("tbody").html($tr);
                    }else {
                        $.each(itemList,function(index,event){
                            $tr += '<tr>'
                                +'<td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">'
                                +'<input type="checkbox" name="receive-item-check" class="group-checkable" /></label></td>'
                                + '<td><input type="hidden" value="'+event.projectId+'">' + formatStr(event.code) + '</td>'
                                + '<td><input type="hidden" value="'+event.projectCode+'">' + formatStr(event.projectName) + '</td>'
                                + '<td>' + formatStr(event.poNo) + '</td>'
                                + '<td>' + formatStr(event.platformSku) + '</td>'
                                + '<td>' + formatStr(event.commbarcode) + '</td>'
                                + '<td><input type="hidden" value="event.goodsId" name ="goodsId">' + formatStr(event.goodsNo) + '</td>'
                                + '<td>' + formatStr(event.goodsName) + '</td>'
                                + '<td>' + formatStr(event.taxRate) + '</td>'
                                + '<td>' + formatStr(event.num) + '</td>'
                                + '<td>' + formatStr(event.price) + '</td>'
                                + '<td>' + formatStr(event.tax) + '</td>'
                                + '<td>' + formatStr(event.taxAmount) + '</td>'
                                + '<td><input type="hidden" name="parentBrandId" value="'+event.parentBrandId+'">' + formatStr(event.parentBrandName) + '</td>'
                                + '<td>' + formatStr(event.paymentSubjectName) + '</td>'
                                + '</tr>';
                        });
                        $('#receive-detail-td-table').find("tbody").html($tr);
                    }
                    $($m13016.params.modalId).modal("hide");
                }else{
                    $custom.alert.error("导入失败！");
                }
                $("#successCount").text(result.data["success"]);//新增条数
                $("#failureCount").text(result.data["failure"]);//错误条数
                //错误信息
                var msgList = result.data.msgList;
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
        var itemList = [];
        var flag = true;
        //页面表单获取列表商品
        $('#receive-detail-td-table tr').each(function(i){// 遍历 tr
            var item ={};
            var projectCode = $(this).find("td").eq(2).find("input").val();
            var barcode = $(this).find("td").eq(7).find("input").val();
            var code = $(this).find("td").eq(1).text();
            var projectName = $(this).find("td").eq(2).text();
            var poNo = $(this).find("td").eq(3).text();
            var platformSku = $(this).find("td").eq(4).text();
            var goodsNo = $(this).find("td").eq(6).text();
            var taxRate = $(this).find("td").eq(8).text();
            var num = $(this).find("td").eq(9).text();
            var price = $(this).find("td").eq(10).text();
            item['projectCode']=projectCode;
            item['barcode']=barcode;
            item['code']=code;
            item['projectName']=projectName;
            item['poNo']=poNo;
            item['platformSku']=platformSku;
            item['goodsNo'] = goodsNo;
            item['taxRate'] = taxRate;
            item['num'] = num;
            item['price'] = price;
            itemList.push(item);
        });
        if(!flag){
            return;
        }
        var json = {};
        json['itemList']=itemList;
        var jsonStr= JSON.stringify(json);
        var url = serverAddr+"/receiveBill/exportReceiveItems.asyn";
        var form = $("<form></form>").attr("action", url).attr("method", "post").attr("target","_blank");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
        form.appendTo('body').submit().remove();
    })

</script>