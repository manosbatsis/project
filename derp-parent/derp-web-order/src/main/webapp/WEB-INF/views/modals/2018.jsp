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
    var $m2018={
        init:function(id){
        	
        	$m2018.params.orderId = id ;
        	
            this.show();
        },
        show:function(){
            $($m2018.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m2018.params.modalId).modal("hide");
        },
        params:{
            modalId:'#signup-modal',
            orderId: ''
        },
    };

    //点击上传文件
    $("#btn-file").click(function(){
        var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
        var orderIdInput = '<input type="hidden" name="orderId" id="orderId" class="btn-hidden file-import" value="'+ $m2018.params.orderId +'">';
        $("#form-upload").empty();
        $("#form-upload").append(input);
        $("#form-upload").append(orderIdInput);
        $("#file").click();
    });

    //上传文件到后端
    $("#form-upload").on("change",'.file-import',function(){
        //判断是否为excel格式
        var data = $(this).val().split(".");
        var suffix = data[data.length-1];
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#form-upload")[0]);
            $custom.request.ajaxUpload(serverAddr + "/declare/importGoods.asyn", formData , function(result){
                if(result.state == '200'){
                	
                	$custom.alert.success("导入完成。");
                	
                	var itemList = result.data["itemList"] ;
                	
                	if(itemList){
                		
                		var html = "" ;
                		
                		$(itemList).each(function(index,event){
        	            	
                			html += "<tr><td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.goodsId+"'></td>"+
                        	"<td><input type='text' name='seqs' class='goods-seqs' style='width:70px;text-align:center;' value='"+ event.seq +"' onkeyup='value=value.replace(/[^\\d]/g,\"\")'></td>"+
                        	"<td><input type='text' name='contractNos' class='goods-contractNos' style='width:70px;text-align:center;' value=''></td>"+
                            "<td>"+(event.goodsCode==null?'':event.goodsCode)+"</td>"+
                            "<td>"+(event.goodsName==null?'':event.goodsName)+"</td>"+
                            "<td>"+(event.goodsNo==null?'':event.goodsNo)+"</td>"+
                            "<td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='"+ event.num +"'></td>"+
                            "<td><input type='hidden' name='purchasePrice' class='goods-purchase-price' style='width:70px;text-align:center;' value='"+ event.purchasePrice +"'>"+ event.purchasePrice +"</td>"+
                            "<td><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='" + event.price + "'></td>"+
                            "<td><input type='text' name='goods-amount' class='goods-amount' style='width:70px;text-align:center;' value='" + event.amount + "'></td>"+
                            "<td><input type='text' name='brandName' class='brandName' value='" + event.brandName + "'  onMouseMove='this.title=this.value'></td>"+
                            "<td><input type='hidden' name='grossWeights-hidden' class='goods-grossWeight-hidden' value='"+(event.grossWeight==null?0:event.grossWeight)+"'><input type='text' name='grossWeights' class='goods-grossWeight' style='width:70px;text-align:center;' value='"+(event.grossWeightSum==null?0:event.grossWeightSum)+"' onkeyup='amountVal(this)'></td>"+
                            "<td><input type='hidden' name='netWeight-hidden' class='goods-netWeight-hidden' value='"+(event.netWeight==null?0:event.netWeight)+"'><input type='text' name='netWeight' class='goods-netWeight' style='width:70px;text-align:center;' value='"+(event.netWeightSum==null?0:event.netWeightSum)+"' onkeyup='amountVal(this)'></td>"+
                            "<td><input type='text' name='boxNo' class='goods-boxNo' style='width:70px;text-align:center;' value=''></td>"+
                            "<td><input type='text' name='batchNo' class='goods-batchNo' style='width:70px;text-align:center;' value=''></td>"+
                            "<td><textarea name='constituentRatio' class='goods-constituentRatio' cols='30' rows='3' align='center'>"+(event.constituentRatio==null?' ':event.constituentRatio)+"</textarea></td>"+
                            "</tr>";
                            
                        	unNeedIds.push(event.goodsId);
                        });
                		
        	            $("#unNeedIds").val(unNeedIds);
        	            $("#table-list").find("tbody").html(html); 
                		
                	}
                	
                	var obj_grossWeight = document.getElementsByName("grossWeights");
        	    	var grossWeight = 0;
        			for(var i = 0; i < obj_grossWeight.length; i++){
        				grossWeight += Number(obj_grossWeight[i].value);
        			}
        			$("#billWeight").val(parseFloat(grossWeight).toFixed(5));
                	
                    
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
    	var itemList = [];
        var flag = true;
        
        //页面表单获取列表商品
        $('#table-list tbody tr').each(function(i){// 遍历 tr
            var item ={};
            var seq= $(this).find('input[name="seqs"]').val();
            item['seq']=seq;
            var goodsId = $(this).find('input[name="goodsId"]').val();
            item['goodsId']=goodsId;
            var num = $(this).find('input[name="num"]').val();
            item['num']=num;
            var amount = $(this).find('input[name="goods-amount"]').val();
            item['amount']=amount;
            var brandName = $(this).find('input[name="brandName"]').val();
            item['brandName']=brandName;
            var price = $(this).find('input[name="price"]').val();//申报单价
            item['price']=price;
            var purchasePrice = $(this).find('input[name="purchasePrice"]').val();//采购单价
            item['purchasePrice']=purchasePrice;
            var grossWeightSum = $(this).find('input[name="grossWeights"]').val();//总毛重
            item['grossWeightSum'] = grossWeightSum;
            var netWeightSum = $(this).find('input[name="netWeight"]').val();//总净重
            item['netWeightSum'] = netWeightSum;

            itemList.push(item);
        });
        
        var jsonStr= JSON.stringify(itemList);
        
        var url = serverAddr+"/declare/exportGoods.asyn";
        var form = $("<form></form>").attr("action", url).attr("method", "post").attr("target","_blank");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
        form.appendTo('body').submit().remove();
    }) ;


</script>