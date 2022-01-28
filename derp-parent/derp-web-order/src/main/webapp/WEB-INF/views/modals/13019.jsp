<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="receive-cost-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true"
         style="display: none;">
        <div class="modal-dialog modal-lg" style="margin:10% auto;">
            <div class="modal-content col-9" style="margin:0 auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">费用明细导入</h5>
                </div>
                <div class="modal-body" >
                        <div class="form-row  col-12" style="text-align:center;">
                            <div class="col-6">
                                <div class="row col-12">
                                    <label  class="col-3 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-4 mll">
                                        <button type="button" class="btn btn-gradient btn-file" id="cost-file">选择文件</button>
                                        <form enctype="multipart/form-data" id="cost-form-upload">
                                            <input type="file" name="file" id="file" class="btn-hidden file-import">
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
    var $m13019={
        init:function(customerId){
            $m13019.params.customerId = customerId;
            this.show();
        },
        show:function(){
            $($m13019.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m13019.params.modalId).modal("hide");
        },
        params:{
            modalId:'#receive-cost-modal',
            customerId: ''
        },
    };

    //点击上传文件
    $("#cost-file").click(function(){
        var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
        $("#cost-form-upload").empty();
        $("#cost-form-upload").append(input);
        $("#file").click();
    });

    //上传文件到后端
    $("#cost-form-upload").on("change",'.file-import',function(){
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length-1];
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#cost-form-upload")[0]);
            formData.append('customerId',$m13019.params.customerId);
            $custom.request.ajaxUpload(serverAddr+"/receiveBill/importAddReceiveItems.asyn?r=1", formData, function(result){
                if(result.state == '200' && result.data["failure"] <= 0){
                    var itemList = result.data.costItemList;
                    var $tr = "";
                    if ($("#table-list tr").length <= 1){
                    	$custom.alert.success("导入完成。");
	                    $.each(itemList,function(index,event){
                            $tr += "<tr>" +
                                "<td class='tc nowrap'><input type='checkbox' name='item-checkbox'></input></td>" +
                                "<td class='tc nowrap'>" +
                                "<input name='projectId' type='hidden' value='" + event.projectId + "'></input>" +
                                "<input name='projectName' type='text' class='form-control' value='" + event.projectName + "' onclick='selectTable(this, " + $m13019.params.customerId + ");'></input> " +
                                "</td>" +
                                "<td class='tc nowrap'>" +
                                "<select name='billType' class='form-control' style='width: 100px;'>" +
                                "<option value=''>请选择</option>" +
                                "<option value='0' " + (event.billType == "0" ? " selected " : "") + ">补款</option>" +
                                "<option value='1' " + (event.billType == "1" ? " selected " : "") + ">扣款</option>"+
                                "</select>"+
                                "</td>" +
                                "<td class='tc nowrap'> <input type='text' name='poNo' class='form-control' value='" + event.poNo + "'></input></td>" +
                                "<td><input name='invoiceDescription' type='text' class='form-control' value='" + event.invoiceDescription + "'></input></td>" +
                                "<td><input type='hidden' class='form-control' name='goodsId' value='" + event.goodsId + "'></input><span>" + event.goodsNo + "</span>" +
                                "<button type='button' class='btn btn-find waves-effect waves-light btn_default' onclick='$erpPopup.orderGoods.init(8, this, null);'>选择商品" +
                                "</button>" +
                                "</td>" +
                                "<td><input type='text' name='goodsName' class='form-control' readonly value='" + event.goodsName + "'></input></td>" +
                                "<td><select class='input_msg' name='superiorParentBrandCode'>" +
                                "<option value=''>请选择</option><c:forEach items='${brandSelectBeans }' var='brandSelect'><option value='${brandSelect.value}'"+ (event.parentBrandId  ==  '${brandSelect.value}'? " selected " : "")+">${brandSelect.label}</option></c:forEach></select></td>" +
                                "<td><input name='num' type='text' class='form-control'  onblur='validNum(this)' value='" + formatStr(event.num) + "'></input></td>" +
                                "<td><input  type='text' name='taxRate' readonly value='" + event.taxRate  + "'></input></td>"+
                                "<td><input name='price' type='text' class='form-control' onkeyup='value=value.replace(/[^\\d^\\.]/g,\"\")' value='" + event.price  + "'></input></td>" +
                                "<td><select class='input_msg' name='storePlatformList'>"+
                                "<option value=''>请选择</option><c:forEach items='${storePlatformList }' var='storePlatform'><option value='${storePlatform.value}'"+ (event.verifyPlatformCode == '${storePlatform.value}'? " selected " : "")+">${storePlatform.label}</option></c:forEach></select></td>" +
                                "<td><input name='remark' type='text' class='form-control'></input>" +
                                "</td></tr>";
	                    });
                        $('#datatable-buttons').find("tbody").html($tr);
                    }else {
                        $.each(itemList,function(index,event){
                            $tr += "<tr>" +
                                "<td class='tc nowrap'><input type='checkbox' name='item-checkbox'></input></td>" +
                                "<td class='tc nowrap'>" +
                                "<input name='projectId' type='hidden' value='" + event.projectId + "'></input>" +
                                "<input name='projectName' type='text' class='form-control' value='" + event.projectName + "' onclick='selectTable(this, " + $m13019.params.customerId + ");'></input> " +
                                "</td>" +
                                "<td class='tc nowrap'>" +
                                "<select name='billType' class='form-control' style='width: 100px;'>" +
                                "<option value=''>请选择</option>" +
                                "<option value='0' " + (event.billType == "0" ? " selected " : "") + ">补款</option>" +
                                "<option value='1' " + (event.billType == "1" ? " selected " : "") + ">扣款</option>"+
                                "</select>"+
                                "</td>" +
                                "<td class='tc nowrap'> <input type='text' name='poNo' class='form-control' value='" + event.poNo + "'></input></td>" +
                                "<td><input name='invoiceDescription' type='text' class='form-control' value='" + event.invoiceDescription + "'></input></td>" +
                                "<td><input type='hidden' class='form-control' name='goodsId' value='" + event.goodsId + "'></input><span>" + event.goodsNo + "</span>" +
                                "<button type='button' class='btn btn-find waves-effect waves-light btn_default' onclick='$erpPopup.orderGoods.init(8, this, null);'>选择商品" +
                                "</button>" +
                                "</td>" +
                                "<td><input type='text' name='goodsName' class='form-control' readonly value='" + event.goodsName + "'></input></td>" +
                                "<td><select class='input_msg' name='superiorParentBrandCode'>" +
                                "<option value=''>请选择</option><c:forEach items='${brandSelectBeans }' var='brandSelect'><option value='${brandSelect.value}'"+ (event.parentBrandId  ==  '${brandSelect.value}'? " selected " : "")+">${brandSelect.label}</option></c:forEach></select></td>" +
                                "<td><input name='num' type='text' class='form-control'  onblur='validNum(this)' value='" + formatStr(event.num)  + "'></input></td>" +
                                "<td><input  type='text' name='taxRate' readonly  value='" + event.taxRate  + "'></input></td>"+
                                "<td><input name='price' type='text' class='form-control' onkeyup='value=value.replace(/[^\\d^\\.]/g,\"\")' value='" + event.price  + "'></input></td>" +
                                "<td><select class='input_msg' name='storePlatformList'>"+
                                "<option value=''>请选择</option><c:forEach items='${storePlatformList }' var='storePlatform'><option value='${storePlatform.value}'"+ (event.verifyPlatformCode == '${storePlatform.value}'? " selected " : "")+">${storePlatform.label}</option></c:forEach></select></td>" +
                                "<td><input name='remark' type='text' class='form-control'></input>" +
                                "</td></tr>";
                        });
                        $('#datatable-buttons').find("tbody").html($tr);
                    }
                    $($m13019.params.modalId).modal("hide");
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
        $('#table-list tr').each(function(i){// 遍历 tr
            debugger
            var projectId = $(this).find("td").eq(1).find("input[name=projectId]").val();
            var billType = $(this).find("td").eq(2).find("select").val();
            var poNo = $(this).find("td").eq(3).find("input").val();
            var goodsNo = $(this).find("td").eq(5).find("span").text();
            var brandParent = $(this).find("td").eq(7).find("option:selected").val();
            var num = $(this).find("td").eq(8).find("input").val();
            var taxRate = $(this).find("td").eq(9).find("input").val();
            var price = $(this).find("td").eq(10).find("input").val();
            var invoiceDescription = $(this).find("td").eq(4).find("input").val();

            var remark = $(this).find("td").eq(12).find("input").val();

            if (!projectId && !billType && !poNo && !goodsNo && !brandParent && !num && !taxRate && !price && !invoiceDescription) {
                return;
            }

            var item ={};
            item['projectId']=projectId;
            item['billType']=billType;
            item['poNo']=poNo;
            item['invoiceDescription']=invoiceDescription;
            item['goodsNo'] = goodsNo;
            item['taxRate'] = taxRate;
            item['num'] = num;
            item['price'] = price;
            item['remark'] = remark;
            item['brandParent'] = brandParent;
            itemList.push(item);
        });
        if(!flag){
            return;
        }
        var json = {};
        json['itemList']=itemList;
        var jsonStr= JSON.stringify(json);
        var url = serverAddr+"/receiveBill/exportReceiveCostItems.asyn";
        var form = $("<form></form>").attr("action", url).attr("method", "post").attr("target","_blank");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
        form.appendTo('body').submit().remove();
    })

</script>