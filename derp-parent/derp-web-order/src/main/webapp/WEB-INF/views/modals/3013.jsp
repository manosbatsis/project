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
                                <!--                               <a href="javascript:void(0);" onclick="downloadTemp();"> 模版下载</a> -->
                                <a href="#">商品导出</a>
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
    var $m3013={
        init:function(){
            // 先选择出仓仓库
            var outDepotId = $("#outDepotId").val();
            var inDepotId = $("#inDepotId").val();
            var buId = $("#buId").val();
            if (outDepotId == null || outDepotId == "") {
                $custom.alert.error("请先选择调出仓库！");
            }else if (inDepotId == null || inDepotId == "") {
                $custom.alert.error("请先选择调入仓库！");
            }else if (buId == null || buId == "") {
                $custom.alert.error("请先选择事业部！");
            }else {
                this.show();
            }
        },
        show:function(){
            $($m3013.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m3013.params.modalId).modal("hide");
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
            var inDepotId = $("#inDepotId").val();
            formData.append('outDepotId',$("#outDepotId").val());
            formData.append('inDepotId',inDepotId);
            $custom.request.ajaxUpload(serverAddr+"/transfer/importTransferGoods.asyn?r=1", formData, function(result){
                if(result.state == '200' && result.data["failure"] <= 0){
                    var ids = result.data.idList.join(",");
                    var goodsList = result.data.stringList;
                    var resultJson = $erpPopup.merchandise.getListByIdsAndTypeAndInOutDepot(ids, '4', inDepotId);
                    var $tr = "";
                    if ($("#item-table tr").length <= 1){
                    	$custom.alert.success("导入完成。");
	                    $.each(resultJson,function(index,event){
	                    	$tr +='<tr>'+
	                        '<td class="tc nowrap">'+
	                            '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+
	                        '</td>'+
	                        '<td><input class="form-control" name="seq" value="'+(goodsList[index].seq)+'"></td>'+
	                        '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+(event.outGoodsId)+'">'+(event.outGoodsCode==null?'':event.outGoodsCode)+
	                             '<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'outDepotId\');">选择商品</button>'+
	                        '</td>'+
	                        '<td>'+(event.outGoodsNo==null?'':event.outGoodsNo)+'</td>'+
	                        '<td><input type="hidden"  class="form-control" value="'+event.outCommbarcode+'">'+(event.outGoodsName==null?'':event.outGoodsName)+'</td>'+
	                        '<td>'+
	                            '<select name="outUnit" class="edit_input" style="width: 60px;">'+
	                                '<option value="02">件</option>'+
	                                '<option value="00">托盘</option>'+
	                                '<option value="01">箱</option>'+
	                            '</select>'+
	                        '</td>'+
	                        '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9" value="'+(goodsList[index].num)+'"></td>' +
	                        '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+(goodsList[index].inGoodsId==null?event.inGoodsId:goodsList[index].inGoodsId) +'">'+(goodsList[index].inGoodsCode==null?event.inGoodsCode:goodsList[index].inGoodsCode)+
	                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods" onclick="$erpPopup.orderGoods.init(\'4\',this,\'inDepotId\');">选择商品</button>'+
	                        '</td>'+
	                        '<td>'+(goodsList[index].inGoodsNo==null?event.inGoodsNo:goodsList[index].inGoodsNo)+'</td>'+
	                        '<td><input type="hidden"  class="form-control" value="'+(goodsList[index].inCommbarcode==null?event.inCommbarcode:goodsList[index].inCommbarcode) +'">'+(goodsList[index].inGoodsName==null?event.inGoodsName:goodsList[index].inGoodsName)+'</td>'+
	                        '<td>'+
	                            '<select name="inUnit" class="edit_input" style="width: 60px;">'+
	                                '<option value="02">件</option>'+
	                                '<option value="00">托盘</option>'+
	                                '<option value="01">箱</option>'+
	                            '</select>'+
	                        '</td>'+
	                        '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="inTransferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9" value='+(goodsList[index].num)+'></td>'+
	                        '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" readonly="readonly"></td>'+
	                        '<td><input type="text"  class="form-control" name="price" value="'+(goodsList[index].price)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
	                        '<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(goodsList[index].grossWeight!=null ? goodsList[index].grossWeight : (parseFloat(event.grossWeight*goodsList[index].num).toFixed(5)))+'" onkeyup="amountVal(this)" onchange="calculateWeight()"></td>'+
	                        '<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(goodsList[index].netWeight!=null ? goodsList[index].netWeight :(parseFloat(event.netWeight*goodsList[index].num).toFixed(5)))+'" onchange="sumTotal()" onkeyup="amountVal(this)" ></td>'+
	                        '<td><input type="text"  class="form-control" name="contNo"></td>'+
	                        '<td><input type="text"  class="form-control" name="bargainno"></td>'+
	                        '<td><input type="text"  class="form-control" name="outBarcode" style="width:100px;" value='+(goodsList[index].outBarcode)+'></td>'+
                            '<td><input type="text"  class="form-control" name="boxNum" value='+(goodsList[index].boxNum)+'></td>'+
                            '<td><input type="text"  class="form-control" name="torrNo"  value='+(goodsList[index].torrNo)+'></td>'+
	                        '</tr>';
	                    });
	                    $("#datatable-buttons tr:gt(0)").remove();  //删除
	                    $("#datatable-buttons").append($tr);
	                    setUnit();//设置理货单位
	                    calculateWeight();
	                    sumTotal();
                    }else {
                        $custom.alert.warning("确认覆盖已存在的所有商品信息？", function () {
                            $custom.alert.success("导入完成。");
                            $.each(resultJson,function(index,event){
                            	$tr +='<tr>'+
    	                        '<td class="tc nowrap">'+
    	                            '<i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i>'+
    	                        '</td>'+
    	                        '<td><input class="form-control" name="seq" value="'+(goodsList[index].seq)+'"></td>'+
    	                        '<td><input type="hidden"  class="form-control" name="outGoodsId" value="'+(event.outGoodsId)+'">'+(event.outGoodsCode==null?'':event.outGoodsCode)+
    	                             '<button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="initGoodsBefore(this,\'outDepotId\');">选择商品</button>'+
    	                        '</td>'+
    	                        '<td>'+(event.outGoodsNo==null?'':event.outGoodsNo)+'</td>'+
    	                        '<td><input type="hidden"  class="form-control" value="'+event.outCommbarcode+'">'+(event.outGoodsName==null?'':event.outGoodsName)+'</td>'+
    	                        '<td>'+
    	                            '<select name="outUnit" class="edit_input" style="width: 60px;">'+
    	                                '<option value="02">件</option>'+
    	                                '<option value="00">托盘</option>'+
    	                                '<option value="01">箱</option>'+
    	                            '</select>'+
    	                        '</td>'+
    	                        '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="transferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9" value="'+(goodsList[index].num)+'"></td>' +
    	                        '<td><input type="hidden"  class="form-control" name="inGoodsId" value="'+event.inGoodsId+'">'+(event.inGoodsCode==null?'':event.inGoodsCode)+
    	                            '<button type="button" class="btn btn-find waves-effect waves-light btn_default indepot_goods" onclick="$erpPopup.orderGoods.init(\'4\',this,\'inDepotId\');">选择商品</button>'+
    	                        '</td>'+
    	                        '<td>'+(event.inGoodsNo==null?'':event.inGoodsNo)+'</td>'+
    	                        '<td><input type="hidden"  class="form-control" value="'+event.inCommbarcode+'">'+(event.inGoodsName==null?'':event.inGoodsName)+'</td>'+
    	                        '<td>'+
    	                            '<select name="inUnit" class="edit_input" style="width: 60px;">'+
    	                                '<option value="02">件</option>'+
    	                                '<option value="00">托盘</option>'+
    	                                '<option value="01">箱</option>'+
    	                            '</select>'+
    	                        '</td>'+
    	                        '<td><input type="text"  class="form-control" onchange="setTransferNum(this);" name="inTransferNum" onkeyup="value=value.replace(/[^\\d]/g,\'\')" maxlength="9" value='+(goodsList[index].num)+'></td>'+
    	                        '<td><input type="text"  class="form-control" name="brandName" value="'+(event.brandName==null?'':event.brandName)+'" readonly="readonly"></td>'+
    	                        '<td><input type="text"  class="form-control" name="price" value="'+(goodsList[index].price)+'" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="11"></td>'+
    	                        '<td><input type="hidden"  name="grossWeight-hidden" value="'+(event.grossWeight==null?'0':event.grossWeight)+'"><input type="text"  class="form-control" name="grossWeight" value="'+(goodsList[index].grossWeight!=null ? goodsList[index].grossWeight : (parseFloat(event.netWeight*goodsList[index].num).toFixed(5)))+'" onkeyup="amountVal(this)" onchange="calculateWeight()"></td>'+
    	                        '<td><input type="hidden"  name="netWeight-hidden" value="'+(event.netWeight==null?'0':event.netWeight)+'"><input type="text"  class="form-control" name="netWeight" value="'+(goodsList[index].netWeight!=null ? goodsList[index].netWeight : (parseFloat(event.netWeight*goodsList[index].num).toFixed(5)))+'" onchange="sumTotal()" onkeyup="amountVal(this)" ></td>'+
    	                        '<td><input type="text"  class="form-control" name="contNo"></td>'+
    	                        '<td><input type="text"  class="form-control" name="bargainno"></td>'+
                                '<td><input type="text"  class="form-control" name="outBarcode" style="width:100px;" value='+(goodsList[index].outBarcode)+'></td>'+
                                '<td><input type="text"  class="form-control" name="boxNum" value='+(goodsList[index].boxNum)+'></td>'+
                                '<td><input type="text"  class="form-control" name="torrNo"  value='+(goodsList[index].torrNo)+'></td>'+
    	                        '</tr>';
                            });
                            $("#datatable-buttons tr:gt(0)").remove();  //删除
                            $("#datatable-buttons").append($tr);
                            setUnit();//设置理货单位
                            calculateWeight();
                            sumTotal();
                        })
                    }  
                    $($m3013.params.modalId).modal("hide");
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
                        +"   <td>"+map.row+"</td>"
                        +"   <td>"+map.msg+"</td>"
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
        $('#item-table tr').each(function(i){// 遍历 tr
            var item ={};
            var seq= $(this).find('input[name="seq"]').val();
            var outGoodsNo = $(this).find("td").eq(3).text();//调出商品货号
            var inGoodsNo = $(this).find("td").eq(8).text();//调入商品货号
            var num = $(this).find('input[name="transferNum"]').val();
            var price = $(this).find('input[name="price"]').val();
            var grossWeightSum = $(this).find('input[name="grossWeight"]').val();//毛重
            var netWeightSum = $(this).find('input[name="netWeight"]').val();//净重
            var outBarcode=$(this).find('input[name="outBarcode"]').val();//调出条形码
            var boxNum=$(this).find('input[name="boxNum"]').val();//箱数
            var torrNo=$(this).find('input[name="torrNo"]').val();//托盘号
            item['seq']=seq;
            item['outGoodsNo']=outGoodsNo;
            item['inGoodsNo']=inGoodsNo;
            item['num']=num;
            item['price']=price;
            item['grossWeightSum'] = grossWeightSum;
            item['netWeightSum'] = netWeightSum;
            item['outBarcode'] = outBarcode;
            item['boxNum'] = boxNum;
            item['torrNo'] = torrNo;
            itemList.push(item);
        });
        if(!flag){
            return;
        }
        var json = {};
        json['itemList']=itemList;
        var jsonStr= JSON.stringify(json);
        var url = serverAddr+"/transfer/exportItems.asyn";
        var form = $("<form></form>").attr("action", url).attr("method", "post").attr("target","_blank");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
        form.appendTo('body').submit().remove();
    })

</script>