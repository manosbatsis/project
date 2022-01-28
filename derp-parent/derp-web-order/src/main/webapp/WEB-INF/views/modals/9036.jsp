<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <!-- amount Adjust content -->
    <div id="amountAdjust-modal"  class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none; overflow-y: auto;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content modal-lg" style="width:800px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="amountAdjustLabel">调整金额</h5>
                </div>
               <!--  <div class="col-12">
                    <h5 style="margin-top:15px;">销售金额确认</h5>
                </div> -->
                <div class="modal-body">
                    <form class="form-horizontal" id="pay-from">
                        <!-- 自定义参数  -->
                        <div class="form-group row m-b-20">
                            <div class="col-12">
                                <label><i class="red">*</i>销售币种：</label>
                                <select class="edit_input" style="width:150px;" name="currency" id="currency" parsley-trigger="change" required reqMsg="请选择销售币种">
                                    <option value="">请选择</option>
                                </select>
                                <input id="orderId" type="hidden" />
                            </div>
                        </div>
                    </form>
                    <div class="form-row mt20 ml15 table-responsive">
                        <table id="table-list" class="table table-striped" style="width: 100%;">
                            <thead>
                            <tr>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>销售数量</th>
                                <th><i class="red">*</i>销售单价<br>(不含税)</th>
                                <th><i class="red">*</i>销售金额<br>(不含税)</th>
                                <th>销售金额<br>(含税)</th>
                            </tr>
                            </thead>
                            <tbody id="tbody">
                            </tbody>
                            <tr>
                                <td colspan="2">合计</td>
                                <td id="totalNum"></td>
                                <td></td>
                                <td id="totalAmount"></td>
                                <td id="totalTaxAmount"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" id="saveButton" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m9036.save();'>确定</button>
                                <button type="button" id="confirmYButton" style="display:none" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m9036.confirm(1);'>确定通过</button>
                                <button type="button" id="confirmNButton" style="display:none" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9036.confirm(2);'>确定不通过</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9036.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9036 = {
        init: function(data,type){
        	
            //加载销售币种的下拉数据
            $('select[name="currency"]').empty();
            $module.currency.loadSelectData.call($('select[name="currency"]')[0],"");
            //币种赋值
            var currency = data.currency ;
            if(currency){
                $("#currency").val(currency) ;
            }

            $("#orderId").val(data.id);            
            $m9036.show(data,type);
        },
        show: function(data,type){
            $("#tbody").empty();
            var html = "" ;
            var totalNum = 0;
            var totalAmount = 0;
            var totalTaxAmount = 0;
            for (var i = 0; i < data.itemList.length; i++) {
                var m = data.itemList[i];
                var id = m.id;
                var goodsId = m.goodsId;
                var goodsCode = m.goodsCode;
                var goodsNo = m.goodsNo;
                var goodsName = m.goodsName;
                var num = m.num;
                var price = m.price;
                var amount = m.amount;
                var taxAmount = m.taxAmount;
                var taxRate = m.taxRate;
                html += '<tr>'
                    + '<td>'+goodsNo+'<input type="hidden" name="goodsNo" value="'+goodsNo+'"/><input type="hidden" name="id" value="'+id+'"/><input type="hidden" name="goodsId" value="'+goodsId+'"/><input type="hidden" name="goodsCode" value="'+goodsCode+'"/></td>'
                    + '<td>'+goodsName+'<input type="hidden" name="goodsName" value="'+goodsName+'"/></td>'
                    + '<td>'+num+'<input type="hidden" name="goods-num" value="'+num+'"/></td>'
                    + '<td><input type="text" name="price" class="goods-price" style="width:100px;text-align:center;" value="'+parseFloat(price).toFixed(8)+'"/></td>'
                    + '<td><input type="text" name="amount" class="goods-amount" style="width:100px;text-align:center;" value="'+parseFloat(amount).toFixed(2)+'" onkeyup="numFormat(this)" /></td>'
                    + '<td><input type="text" name="taxAmount" style="width:100px;text-align:center;" value="'+(taxAmount == null ? parseFloat(amount).toFixed(2):parseFloat(taxAmount).toFixed(2))+'" disabled /><input type="hidden" name="taxRate" value="'+(taxRate == null ? 0:taxRate)+'"/></td>'
                    + '</tr>';
                totalNum += num;
                totalAmount += amount;
                if(!taxAmount){
                	taxAmount = 0;
                }
                totalTaxAmount += taxAmount;
            }
            $("#tbody").append(html);
            $("#totalNum").html(totalNum);
            $("#totalAmount").html(parseFloat(totalAmount).toFixed(2));
            $("#totalTaxAmount").html(parseFloat(totalTaxAmount).toFixed(2));
          	//type == 2 为金额确认 
			if(type == 2){
				 $("#currency").attr("disabled",true);
				 $("#saveButton").css("display","none");
				 $("#confirmYButton").css("display","inline-block");
				 $("#confirmNButton").css("display","inline-block");
				 $("#amountAdjustLabel").text("确认金额");
				 $('#tbody tr').each(function(){
					 $(this).find('input[name="price"]').attr("disabled",true);
	                 $(this).find('input[name="amount"]').attr("disabled",true); 
				 });
        	}else{
        		$("#currency").attr("disabled",false);
				 $("#saveButton").css("display","inline-block");
				 $("#confirmYButton").css("display","none");
				 $("#confirmNButton").css("display","none");
				 $("#amountAdjustLabel").text("调整金额");
        	}
            $($m9036.params.modalId).modal("show");
        },
        hide:function(){
            $($m9036.params.modalId).modal("hide");
        },
        params:{
            modalId:'#amountAdjust-modal'
        },
        save:function () {        	
            var itemList = [];
            var flag = true;
            var orderId = $("#orderId").val();
            var totalAmount = 0;
            var currency = $("#currency").val();
            if(!currency || currency == ""  ){
                $custom.alert.error("销售币种不能为空");
                return ;
            }
            $('#tbody tr').each(function(i){
                var item = {};
                var id = $(this).find('input[name="id"]').val();
                var goodsId = $(this).find('input[name="goodsId"]').val();
                var goodsCode = $(this).find('input[name="goodsCode"]').val();
                var goodsNo = $(this).find('input[name="goodsNo"]').val();
                var price = $(this).find('input[name="price"]').val();
                var num = $(this).find('input[name="goods-num"]').val();
                var amount = $(this).find('input[name="amount"]').val();
                var taxAmount = $(this).find('input[name="taxAmount"]').val();

                if(!price || parseFloat(price) < 0){
                    $custom.alert.error("商品单价不能为空或小于0");
                    flag = false;
                    return;
                }
                // var amount =  parseFloat(price)*parseFloat(num)+"";
                if(!amount || parseFloat(amount) < 0 || isNaN(parseFloat(amount))){
                    $custom.alert.error("总金额不能为空或小于0");
                    flag = false;
                    return;
                }
                else{
                    var indexOf = amount.indexOf(".");
                    // 如果是小数
                    if (indexOf != -1) {
                        var count = (parseFloat(amount)+"").length - 1 - indexOf;
                        if (count > 2) {
                            $custom.alert.error("总金额只能为两位小数");
                            flag = false;
                            return;
                        }
                    }
                }
                if(amount.length>21){
                    $custom.alert.error("总金额长度只能输入21位（包含小数点）");
                    flag = false;
                    return;
                }
                item['id']=id;
                item['goodsId']=goodsId;
                item['goodsCode']=goodsCode;
                item['goodsNo']=goodsNo;
                item['num']=num;
                item['amount']=amount;
                item['price']=price;
                item['taxAmount']=taxAmount;
                itemList.push(item);
                totalAmount += parseFloat(amount);
            });
            if(!flag){
                return false;
            }
            if(!itemList || itemList.length==0){
                $custom.alert.error("该销售订单没有商品！");
                return;
            }
            var json = {};
            json['orderId']=orderId;
            json['totalAmount']=totalAmount;
            json['currency']=currency;
            json['itemList']=itemList;
            var jsonStr= JSON.stringify(json);
            var form = {};
            form['json']=jsonStr;
            $custom.request.ajax(serverAddr+"/sale/amountAdjust.asyn",form,function(result){
                if(result.state==200){
                    $custom.alert.success("金额调整成功！");
                    $m9036.hide();
                    $mytable.fn.reload();
                }else{
                    if(!!result.expMessage){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
            });
       		
       	},
       	confirm:function(status){
       		var json = {};
       		var orderId = $("#orderId").val();
            json['orderId']=orderId;
            json['amountConfirmState']=status;
            var form = {};
            form['json']=JSON.stringify(json);
            $custom.request.ajax(serverAddr+"/sale/amountConfirm.asyn",form,function(result){
                if(result.state==200){
                    $custom.alert.success("金额确认成功！");
                    $m9036.hide();
                    $mytable.fn.reload();
                }else{
                    if(!!result.expMessage){
                        $custom.alert.error(result.expMessage);
                    }else{
                        $custom.alert.error(result.message);
                    }
                }
            });
       	}
       	
    };
    //监听销售单价的离开事件
    $("#table-list").on("blur",'.goods-price',function(){
        var that = $(this);
        var tr = that.parent().parent();
        //获取当前价格
        var price= that.val();
        //判断是否数字
        if(isNaN(price)){
            $custom.alert.error("销售单价请输入数字");
            that.val("");
            return ;
        }
        var indexOf = price.indexOf(".");
    	if (indexOf != -1) {
            var count = (parseFloat(price)+"").length - 1 - indexOf;
            if (count > 8) {
            	$(this).val("");
            	tr.find("input[name='amount']").val("");
                $custom.alert.error("销售单价只能为8位小数");
                return;
            }
        }
       
        //获取销售数量
        var num = tr.find("input[name='goods-num']").val();
        //设置总金额
        var sum = 0;
        if(!!num && !!price){
            sum = Number(price*num).toFixed(2);
        }
        tr.find("input[name='amount']").val(sum);
        $(this).val(parseFloat(price).toFixed(8)) ;
        
        var taxRate = tr.find("input[name='taxRate']").val();
        if(!taxRate){
        	taxRate = 0;
        }
        var taxAmount = parseFloat(sum) * (1+parseFloat(taxRate));
        tr.find("input[name='taxAmount']").val(parseFloat(taxAmount).toFixed(2));
        
        calAmount();
    });

    //监听销售金额的离开事件
    $("#table-list").on("blur",'.goods-amount',function(){
        var that = $(this);
        //获取总金额
        var sum= that.val();
        var tr = that.parent().parent();
        //获取销售数量
        var num = tr.find("input[name='goods-num']").val();
        //判断是否数字
        if(isNaN(sum)){
            $custom.alert.error("总金额请输入数字");
            that.val("");
            return ;
        }
        sum = parseFloat(sum) ;
        sum = sum.toFixed(2) ;
        $(this).val(sum) ;
        //设置单价
        var price = 0;
        if(!!num && !!sum){
            price = Number(sum/num).toFixed(8);
        }
        tr.find("input[name='price']").val(price);
        
        var taxRate = tr.find("input[name='taxRate']").val();
        
        if(!taxRate){
        	taxRate = 0;
        }
        var taxAmount = parseFloat(sum) * (1+parseFloat(taxRate));
        tr.find("input[name='taxAmount']").val(parseFloat(taxAmount).toFixed(2));
        calAmount();
    });
function calAmount(){
   var total = 0;
    $("#tbody tr").each(function (i) {
        var ch = $(this).find('td:eq(4) input').val();
        total = parseFloat(total) + parseFloat(ch);
    })
    $("#totalAmount").html(parseFloat(total).toFixed(2));
    
    var total2 = 0;
    $("#tbody tr").each(function (i) {
        var ch = $(this).find('input[name="taxAmount"]').val();
        if(!ch){
        	ch = 0;
        }
        total2 = parseFloat(total2) + parseFloat(ch);
    })
    $("#totalTaxAmount").html(parseFloat(total2).toFixed(2));
    
}
    function numFormat(obj){
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
        if (obj.value.indexOf(".") < 0 && obj.value != "") {//如果没有小数点，首位不能为类似于 01、02的金额
            obj.value = parseFloat(obj.value);
        }
    }
</script>