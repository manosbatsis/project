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
                                <!--                               <a href="javascript:void(0);" onclick="downloadTemp();"> 模板下载</a> -->
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
    var $m9014={
        init:function(){
            // 先选择出仓仓库
            var outDepotId = $("#outDepotId").val();
            if (outDepotId == null || outDepotId == "") {
                $custom.alert.error("请先选择仓库！");
            }else {
                this.show();
            }
        },
        show:function(){
            $($m9014.params.modalId).modal({keyboard: true});
        },
        hide:function(){
            $($m9014.params.modalId).modal("hide");
        },
        params:{
            getListByIdsUrl:'/merchandise/getListByIds.asyn?r='+Math.random(),
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
        var unitName = $("#tallyingUnit").find("option:selected").text(); 
        var unitId = $("#tallyingUnit").val(); 
        if(suffix=="xlsx" || suffix=="xls"){
            var formData = new FormData($("#form-upload")[0]);
            formData.append('outDepotId',$("#outDepotId").val());
            formData.append('customerId',$("#customerId").val());
            formData.append('currency',$("#currency").val());
            formData.append('salePriceManage',salePriceManage);
            formData.append('unitId',unitId);
            $custom.request.ajaxUpload(serverAddr+"/sale/importSaleGoods.asyn?r=1", formData , function(result){
                if(result.state == '200'){
                    var ids = result.data.idList.join(",");
                    var goodsList = result.data.stringList;
                    //储存选择过的商品id
                	var unNeedIds = [];
                    $custom.request.ajax(
                        $m9014.params.getListByIdsUrl,{"ids":ids},function(data){
                            if(data.state==200){
                                if ($("#table-list tr").length <= 1){
                                    $custom.alert.success("导入完成。");                                   
                                    var $tr = "";
                                    $.each(data.data,function(index,event){
                                    	 /*     	                	
	                  	                   以（公司+事业部）+（公司+客户）查询是否同时启用销售价格管理，若开启
	                  	                   1、检查海外仓理货单位是否填写，若填写且值不为件，查询是否存在箱件的换算，若存在则将销售价换算为理货单位单价，填入该商品的采购单价中，计算总价;
	                  	                   2、若海外仓理货单位未填写或已填写但值为件，则将报价直接填入该商品的销售单价中，计算总价 
	                  	                 */
     	             	                var price = "" ;
                  	                    var amount = "";
     	             	            	if(salePriceManage){     	             	            		
               	            					price = goodsList[index].price;
               	            					amount = goodsList[index].amount;
                  	                        
     	             	            	}
     	             	            	var priceDeclareRatio = event.priceDeclareRatio;
     	                                if(!priceDeclareRatio) {
     	                                    priceDeclareRatio = 1;
     	                                }
     	             	            	
                                    	$tr+="<tr><td style='text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/></td>"+
                                        "<td style='text-align:center'><input type='text' name='seq' style='width:50px;text-align:center;' value='"+(goodsList[index].seq)+"'/></td>"+
                                        "<td style='text-align:center'>"+(event.goodsCode==null?'':event.goodsCode)+"<input type='hidden' name='goodsCode' value='"+(event.goodsCode==null?'':event.goodsCode)+"'/></td>"+
                                        "<td style='text-align:center'>"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                                        "<td style='text-align:center'>"+(event.goodsNo==null?'':event.goodsNo)+"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/></td>"+
                                        "<td style='text-align:center'>"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/></td>"+
                                        "<td style='text-align:center'><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='"+(goodsList[index].num)+"'></td>"+        /*销售数量*/
                                        "<td style='text-align:center'><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(salePriceManage == true ? parseFloat(price).toFixed(8) : parseFloat(goodsList[index].price).toFixed(8))+"'></td>"+   /*销售单价*/
                                        "<td style='text-align:center'><input type='text' name='declarePrice' class='goods-declarePrice' style='width:70px;text-align:center;' value='"+(goodsList[index].declarePrice!="" ? goodsList[index].declarePrice : event.filingPrice * priceDeclareRatio)+"'></td>"+   /*申报单价*/
                                        "<td style='text-align:center'><input type='text' name='amount' class='goods-amount' style='width:70px;text-align:center;' onkeyup='numFormat(this,2)' value='"+(salePriceManage == true ?  Number(amount).toFixed(2): parseFloat(goodsList[index].amount).toFixed(2))+"'></td>"+   /*销售总金额*/
                                        "<td style='text-align:center'><input type='text' name='taxAmount'  class='goods-taxAmount' style='width:70px;text-align:center;' value='"+(goodsList[index].taxAmount == null ?'0': parseFloat(goodsList[index].taxAmount).toFixed(2))+"'  onkeyup='numFormat(this,2)'></td>"+
                                        "<td style='text-align:center'><input type='hidden' name='goodsTaxRate' value='"+(goodsList[index].taxRate == null ?'0': parseFloat(goodsList[index].taxRate).toFixed(2))+"'/><select name='taxRate' class='edit_input' style='width: 80px;' onchange='calculateTaxAmount(this)'></select></td>"+
                                        "<td style='text-align:center'><input type='text' name='tax' class='goods-tax' value='"+(goodsList[index].tax == null ?'0':  parseFloat(goodsList[index].tax).toFixed(2))+"' onkeyup='numFormat(this,2)'/></td>"+
                                        "<td style='text-align:center'>"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='goodsCode' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
                                        "<td style='text-align:center'><input type='hidden'  name='grossWeight-hidden' value='"+(event.grossWeight==null?'0':event.grossWeight)+"'><input type='text' name='grossWeight' style='width:60px;text-align:center;' class='form-control goods-rough' onchange='calculateWeight1()' onkeyup='numFormat(this,5)' maxlength='11' value='"+(goodsList[index].grossWeight!=null ? parseFloat(goodsList[index].grossWeight).toFixed(5) : (parseFloat(event.grossWeight*goodsList[index].num).toFixed(5)))+"'/></td>"+
                                        "<td style='text-align:center'><input type='hidden'  name='netWeight-hidden' value='"+(event.netWeight==null?'0':event.netWeight)+"'><input type='text' name='netWeight' style='width:60px;text-align:center;' class='form-control goods-suttle' onchange='suttleWeight1()' onkeyup='numFormat(this,5)' maxlength='11' value='"+(goodsList[index].netWeight!=null ? parseFloat(goodsList[index].netWeight).toFixed(5) : (parseFloat(event.netWeight*goodsList[index].num).toFixed(5)))+"'/></td>"+
                                        "<td style='text-align:center'><input type='text' name='boxNo' value='' /></td>"+
                                        "<td style='text-align:center'><input type='text' name='itemContractNo' value='' /></td>"+
                                        "<td style='text-align:center'><input type='text' name='itemRemark' value='' /></td>"+
                                        "<td style='text-align:center'><input type='text' name='torrNo' value='"+(goodsList[index].torrNo==null?'':goodsList[index].torrNo)+"'/></td>"+
                                        "<td style='text-align:center'><input type='text' name='itemBoxNum' value='"+(goodsList[index].boxNum==null?'':goodsList[index].boxNum)+"' onchange='calTotalBoxNum()'/></td>"+
                                        "<td><textarea name='component' class='goods-component' cols='30' rows='3' align='center'></textarea></td>"+
                                        "</tr>";
                                    	unNeedIds.push(event.id);
                                    });
                                    // $("#table-list tr:gt(0)").remove();  //删除第一行外的所有行
                                    $("#table-list").append($tr);
                                    $("#unNeedIds").val(unNeedIds);
                                    goods_change1();
                                    var billTotal = 0;
                                    $("#table-list .goods-rough").each(function () {
                                        var val = $(this).val();
                                        if (!val) {
                                        	val = 0;
                                        }
                                        billTotal += parseFloat(val);
                                    })
                                    $("#billWeight").val(billTotal.toFixed(2));
                                    if("${saleOrderItemList}" != null){
                                   	 	var selectObj = $("#tbody").find("tr").find("select[name='taxRate']");
                                   	 	$(selectObj).each(function(){
                                   	 		$(this).empty();
                                   		 	var taxRate = $(this).prev('input[name="goodsTaxRate"]').val();
                                   		 	$(this).append(getTaxRateSelectHtml(taxRate));
                                   	 	});
                                    }
                                }else {
                                    $custom.alert.warning("确认覆盖已存在的所有商品信息？", function () {
                                        $custom.alert.success("导入完成。");
                                        var $tr = "";
                                        $.each(data.data,function(index,event){
                                        	/*     	                	
	 	                  	                   以（公司+事业部）+（公司+客户）查询是否同时启用销售价格管理，若开启
	 	                  	                   1、检查海外仓理货单位是否填写，若填写且值不为件，查询是否存在箱件的换算，若存在则将销售价换算为理货单位单价，填入该商品的采购单价中，计算总价;
	 	                  	                   2、若海外仓理货单位未填写或已填写但值为件，则将报价直接填入该商品的销售单价中，计算总价 
	 	                  	                 */
	      	             	                var price = "" ;
	                   	                    var amount = "";
	      	             	            	if(salePriceManage){
               	            					price = goodsList[index].price;
               	            					amount = goodsList[index].amount;	               	            				
	      	             	            	}
	      	             	            	var priceDeclareRatio = event.priceDeclareRatio;
		      	                            if(!priceDeclareRatio) {
		      	                               priceDeclareRatio = 1;
		      	                            }
                                        	$tr+="<tr><td style='text-align:center'><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value='"+event.id+"'/></td>"+
                                            "<td style='text-align:center'><input type='text' name='seq' style='width:50px;text-align:center;' value='"+(goodsList[index].seq)+"'/></td>"+
                                            "<td style='text-align:center'>"+(event.goodsCode==null?'':event.goodsCode)+"<input type='hidden' name='goodsCode' value='"+(event.goodsCode==null?'':event.goodsCode)+"'/></td>"+
                                            "<td style='text-align:center'>"+(event.name==null?'':event.name)+"<input type='hidden' name='goodsName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                                            "<td style='text-align:center'>"+(event.goodsNo==null?'':event.goodsNo)+"<input type='hidden' name='goodsNo' value='"+(event.goodsNo==null?'':event.goodsNo)+"'/></td>"+
                                            "<td style='text-align:center'>"+(event.barcode==null?'':event.barcode)+"<input type='hidden' name='barcode' value='"+(event.barcode==null?'':event.barcode)+"'/></td>"+
                                            "<td style='text-align:center'><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value='"+(goodsList[index].num)+"'></td>"+        /*销售数量*/
                                            "<td style='text-align:center'><input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='"+(salePriceManage == true ? parseFloat(price).toFixed(8) : parseFloat(goodsList[index].price).toFixed(8))+"'></td>"+   /*销售单价*/
                                            "<td style='text-align:center'><input type='text' name='declarePrice' class='goods-declarePrice' style='width:70px;text-align:center;' value='"+(goodsList[index].declarePrice!="" ? goodsList[index].declarePrice : event.filingPrice * priceDeclareRatio)+"'></td>"+   /*申报单价*/
                                            "<td style='text-align:center'><input type='text' name='amount' class='goods-amount' style='width:70px;text-align:center;' onkeyup='numFormat(this,2)' value='"+(salePriceManage == true ?  Number(amount).toFixed(2): parseFloat(goodsList[index].amount).toFixed(2))+"'></td>"+   /*销售总金额*/
                                            "<td style='text-align:center'><input type='text' name='taxAmount'  class='goods-taxAmount' style='width:70px;text-align:center;' value='"+(goodsList[index].taxAmount == null ?'0': parseFloat(goodsList[index].taxAmount).toFixed(2))+"'  onkeyup='numFormat(this,2)'></td>"+
                                            "<td style='text-align:center'><input type='hidden' name='goodsTaxRate' value='"+(goodsList[index].taxRate == null ?'0': parseFloat(goodsList[index].taxRate).toFixed(2))+"'/><select name='taxRate' class='edit_input' style='width: 80px;' onchange='calculateTaxAmount()'></select></td>"+
                                            "<td style='text-align:center'><input type='text' name='tax' class='goods-tax' value='"+(goodsList[index].tax == null ?'0':  parseFloat(goodsList[index].tax).toFixed(2))+"' onkeyup='numFormat(this,2)'/></td>"+
                                            "<td style='text-align:center'>"+(event.brandName==null?'':event.brandName)+"<input type='hidden' name='goodsCode' value='"+(event.brandName==null?'':event.brandName)+"'/></td>"+
                                            "<td style='text-align:center'><input type='hidden'  name='grossWeight-hidden' value='"+(event.grossWeight==null?'0':event.grossWeight)+"'><input type='text' name='grossWeight' id='goods-rough' style='width:60px;text-align:center;' class='form-control goods-rough' onchange='calculateWeight1()' onkeyup='numFormat(this,5)' maxlength='11' value='"+(goodsList[index].grossWeight!=null ? parseFloat(goodsList[index].grossWeight).toFixed(5) : (parseFloat(event.grossWeight*goodsList[index].num).toFixed(5)))+"'/></td>"+
                                            "<td style='text-align:center'><input type='hidden'  name='netWeight-hidden' value='"+(event.netWeight==null?'0':event.netWeight)+"'><input type='text' name='netWeight' id='goods-suttle' style='width:60px;text-align:center;' class='form-control goods-suttle' onchange='suttleWeight1()' onkeyup='numFormat(this,5)' maxlength='11' value='"+(goodsList[index].netWeight!=null ? parseFloat(goodsList[index].netWeight).toFixed(5) : (parseFloat(event.netWeight*goodsList[index].num).toFixed(5)))+"'/></td>"+
                                            "<td style='text-align:center'><input type='text' name='boxNo' value='' /></td>"+
                                            "<td style='text-align:center'><input type='text' name='itemContractNo' value='' /></td>"+
                                            "<td style='text-align:center'><input type='text' name='itemRemark' value='' /></td>"+
                                            "<td style='text-align:center'><input type='text' name='torrNo' value='"+(goodsList[index].torrNo==null?'':goodsList[index].torrNo)+"'/></td>"+
                                            "<td style='text-align:center'><input type='text' name='itemBoxNum' value='"+(goodsList[index].boxNum==null?'':goodsList[index].boxNum)+"' onchange='calTotalBoxNum()'/></td>"+
                                            "<td><textarea name='component' class='goods-component' cols='30' rows='3' align='center'></textarea></td>"+
                                            "</tr>";
                                            
                                        	unNeedIds.push(event.id);
                                        });
                                        $("#table-list tr:gt(0)").remove();  //删除第一行外的所有行
                                        $("#table-list").append($tr);
                                        $("#unNeedIds").val(unNeedIds);
                                        goods_change1();
                                        var billTotal = 0;
                                        $("#table-list .goods-rough").each(function () {
                                            var val = $(this).val();
                                            if (!val) {
                                            	val = 0;
                                            }
                                            billTotal += parseFloat(val);
                                        })
                                        $("#billWeight").val(billTotal.toFixed(2));
                                        if("${saleOrderItemList}" != null){
                                       	 	var selectObj = $("#tbody").find("tr").find("select[name='taxRate']");
                                       	 	$(selectObj).each(function(){
                                       	 		$(this).empty();
                                       		 	var taxRate = $(this).prev('input[name="goodsTaxRate"]').val();
                                       		 	$(this).append(getTaxRateSelectHtml(taxRate));
                                       	 	});
                                        }
                                    })
                                }
                                $($m9014.params.modalId).modal("hide");
                            }else{
                                $custom.alert.error("导入失败！");
                            }
                        }
                    );
                    
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
        /* var platformPurchaseIds = $("#platformPurchaseIds").val();//平台采购订单号
        //新增 直接导出模板
        if(platformPurchaseIds == "" || platformPurchaseIds == null || platformPurchaseIds == undefined) {
            $downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=143");
        }else{//平台转销售 导出相关商品 */
            var itemList = [];
            var flag = true;
            //页面表单获取列表商品
            $('#table-list tbody tr').each(function(i){// 遍历 tr
                var item ={};
                var seq= $(this).find('input[name="seq"]').val();
                item['seq']=seq;
                var goodsNo = $(this).find('input[name="goodsNo"]').val();
                item['goodsNo']=goodsNo;
                var num = $(this).find('input[name="num"]').val();
                item['num']=num;
                var amount = $(this).find('input[name="amount"]').val();
                item['amount']=amount;
                var declarePrice = $(this).find('input[name="declarePrice"]').val();//申报单价
                item['declarePrice']=declarePrice;
                var grossWeightSum = $(this).find('input[name="grossWeight"]').val();//总毛重
                item['grossWeightSum'] = grossWeightSum;
                var netWeightSum = $(this).find('input[name="netWeight"]').val();//总净重
                item['netWeightSum'] = netWeightSum;
                var torrNo = $(this).find('input[name="torrNo"]').val();//托盘号
                item['torrNo'] = torrNo;
                var itemBoxNum = $(this).find('input[name="itemBoxNum"]').val();//箱数
                if(itemBoxNum && isNaN(itemBoxNum)){
                    $custom.alert.error("商品箱数请输入数值！");
                    return ;
                }
                item['boxNum'] = itemBoxNum;
                var barcode = $(this).find('input[name="barcode"]').val();//条形码
                item['barcode'] = barcode;
                
                itemList.push(item);
            });
            if(!flag){
                return;
            }
            if(itemList.length < 1){
            	$downLoad.downLoadByUrl("/excelTemplate/download.asyn?ids=143");
            	return;
            }
            var json = {};
            json['itemList']=itemList;
            var jsonStr= JSON.stringify(json);
            // var url=serverAddr+"/sale/exportItems.asyn?json="+jsonStr;
            // $downLoad.downLoadByUrl(url);
            var url = serverAddr+"/sale/exportItems.asyn";
            var form = $("<form></form>").attr("action", url).attr("method", "post").attr("target","_blank");
            form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
            form.appendTo('body').submit().remove();
        //}
    })

    //监听毛重、净重的离开事件
    $("#table-list").on("blur",'.goods-rough',function(){
      goods_change1()
    });
    $("#table-list").on("blur",'.goods-suttle',function(){
      goods_change1()
    });

</script>