<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <!-- amount Adjust content -->
    <div id="financeApply-modal"  class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none; overflow-y: auto;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content modal-lg" style="width:1000px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" >融资申请</h5>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="applyFinanceForm">
						<div class="row">
							<div class="col-md-4">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;客户：</span>
                                <span id="apply_customerName"></span>
                            </div>
							<div class="col-md-4">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PO号：</span>
                                <span id="apply_poNo"></span>
                            </div>
                            <div class="col-md-4">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币种：</span>
                                <span id="apply_currency"></span>
                            </div>
						</div>
						<div class="row">
							<div class="col-md-4">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;事业部：</span>
                                <span id="apply_buName"></span>
                            </div>
							<div class="col-md-4">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;创建人：</span>
                                <span id="apply_createName"></span>
                            </div>
                            <div class="col-md-4">
                                <span>&nbsp;&nbsp;创建时间：</span>
                                <span id="apply_createDate"></span>
                            </div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<label><i style="color: red">*&nbsp;</i>订单金额:</label> 
								<input class="input_msg" name="apply_saleAmount" id="apply_saleAmount" />
							</div>
							<div class="col-md-4">
								<label><i style="color: red">*&nbsp;</i>实收保证金:</label> 
								<input class="input_msg" name="apply_actualMarginAmount" id="apply_actualMarginAmount"  onkeyup="numFormat(this)"/>
							</div>
							<div class="col-md-4">
								<label><i style="color: red">*&nbsp;</i>起算日期:</label> 
								<input type="text" class="input_msg form_datetime date-input" name="apply_applyDate" id="apply_applyDate">
							</div>
						</div>

					</form>
                    <div class="form-row mt20 ml15 table-responsive">
                        <table id="apply-table-list" class="table table-striped" style="width: 100%;">
                            <thead>
                            <tr>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>销售数量</th>
                                <th>销售单价</th>
                                <th><i class="red">*</i>销售金额</th>
                            </tr>
                            </thead>
                            <tbody id="apply-tbody">
                            </tbody>
                            <tr>
                                <td colspan="2">合计</td>
                                <td id="apply_totalNum"></td>
                                <td></td>
                                <td id="apply_totalAmount"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" id="saveButton" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m9038.save();'>确定</button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9038.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9038 = {
        init: function(id){
        	$m9038.params.orderId =id;        
            $m9038.show();
        },
        show: function(){
            $($m9038.params.modalId).modal({backdrop: 'static', keyboard: false});
            $("#apply-tbody").empty();
            var html = "" ;
            var totalNum = 0;
            var totalAmount = 0;
            var saleAmount ='';
            var customerName ='';
            var poNo ='';
            var currency ='';
            var buName ='';
            var createName ='';
            var createDate ='';
            $custom.request.ajaxSync(serverAddr+"/sale/getFinanceDetail.asyn", {"orderId":$m9038.params.orderId}, function(result){
                if(result.state==200 && result.data){
                	saleAmount = result.data.saleAmount == null ? '':parseFloat(result.data.saleAmount).toFixed(2);
                    customerName =result.data.customerName;
                    poNo = result.data.poNo;
                    currency = result.data.currency;
                    buName = result.data.buName;
                    createName = result.data.createName;
                    createDate = result.data.createDate;

                	for (var i = 0; i < result.data.itemList.length; i++) {
                        var m = result.data.itemList[i];
                        var goodsId = m.goodsId;
                        var goodsCode = m.goodsCode;
                        var goodsNo = m.goodsNo;
                        var goodsName = m.goodsName;
                        var num = m.num;
                        var price = m.price == null ? '':parseFloat(m.price).toFixed(4);
                        var amount = m.amount == null ? '':parseFloat(m.amount).toFixed(2);
                        html += '<tr>'
                            + '<td>'+goodsNo+'<input type="hidden" name="goodsNo" value="'+goodsNo+'"/><input type="hidden" name="goodsId" value="'+goodsId+'"/><input type="hidden" name="goodsCode" value="'+goodsCode+'"/></td>'
                            + '<td>'+goodsName+'<input type="hidden" name="goodsName" value="'+goodsName+'"/></td>'
                            + '<td>'+num+'<input type="hidden" name="goods-num" value="'+num+'"/></td>'
                            + '<td><input type="text" name="price" class="goods-price" style="width:100px;text-align:center;" disabled value="'+price+'"/></td>'
                            + '<td><input type="text" name="amount" class="goods-amount" style="width:100px;text-align:center;" value="'+amount+'" onkeyup="numFormat(this)" /></td>'                    
                            + '</tr>';
                        totalNum += num;
                        if(!amount){
                        	amount = 0;
                        }
                        totalAmount += amount;                        
                    }
                }
            });
            
            $("#apply-tbody").append(html);
            $("#apply_totalNum").html(totalNum);
            $("#apply_totalAmount").html(parseFloat(totalAmount).toFixed(2));
            $("#apply_saleAmount").val(saleAmount);
            $("#apply_customerName").html(customerName);
            $("#apply_poNo").html(poNo);
            $("#apply_currency").html(currency);
            $("#apply_buName").html(buName);
            $("#apply_createName").html(createName);
            $("#apply_createDate").html(createDate);
            $($m9038.params.modalId).modal("show");
        },
        hide:function(){
            $("#applyFinanceForm")[0].reset();
            $("#apply-tbody").empty() ;
            $($m9038.params.modalId).modal("hide");
        },
        params:{
        	orderId:null,
            modalId:'#financeApply-modal'
        },
        save:function () {        	
            var itemList = [];
            var flag = true;
            var orderId = $m9038.params.orderId;
            var totalAmount = 0;
          
           var saleAmount = $("#apply_saleAmount").val();
           if(!saleAmount || saleAmount == ""  ){
               $custom.alert.error("订单金额不能为空");
               return ;
           }
           if(parseFloat(saleAmount) <= 0 && isNaN(saleAmount)){
        	   $custom.alert.error("订单金额必须大于0的数字");
               return ;
           }
           var actualMarginAmount = $("#apply_actualMarginAmount").val();
           if(!actualMarginAmount || actualMarginAmount == ""  ){
               $custom.alert.error("实收保证金不能为空");
               return ;
           }
           if(parseFloat(actualMarginAmount) < 0 && isNaN(actualMarginAmount)){
        	   $custom.alert.error("实收保证金必须大于等于0的数字");
               return ;
           }
           var applyDate = $("#apply_applyDate").val();
           if(!applyDate || applyDate == ""  ){
               $custom.alert.error("起算日期不能为空");
               return ;
           }
            $('#apply-tbody tr').each(function(i){
                var item = {};
                var goodsId = $(this).find('input[name="goodsId"]').val();
                var goodsCode = $(this).find('input[name="goodsCode"]').val();
                var goodsNo = $(this).find('input[name="goodsNo"]').val();
                var price = $(this).find('input[name="price"]').val();
                var num = $(this).find('input[name="goods-num"]').val();
                var amount = $(this).find('input[name="amount"]').val();

                if(!price || parseFloat(price) < 0){
                    $custom.alert.error("商品单价不能为空或小于0");
                    flag = false;
                    return;
                }
                // var amount =  parseFloat(price)*parseFloat(num)+"";
                if(!amount || parseFloat(amount) < 0 || isNaN(parseFloat(amount))){
                    $custom.alert.error("金额不能为空或小于0");
                    flag = false;
                    return;
                }
                else{
                    var indexOf = amount.indexOf(".");
                    // 如果是小数
                    if (indexOf != -1) {
                        var count = (parseFloat(amount)+"").length - 1 - indexOf;
                        if (count > 2) {
                            $custom.alert.error("金额只能为两位小数");
                            flag = false;
                            return;
                        }
                    }
                }
                if(amount.length>21){
                    $custom.alert.error("金额长度只能输入21位（包含小数点）");
                    flag = false;
                    return;
                }
                
                item['goodsId']=goodsId;
                item['goodsCode']=goodsCode;
                item['goodsNo']=goodsNo;
                item['num']=num;
                item['amount']=amount;
                item['price']=price;
                itemList.push(item);
                totalAmount += parseFloat(amount);
            });
            if(!flag){
                return false;
            }
            if(!itemList || itemList.length==0){
                $custom.alert.error("该订单没有商品！");
                return;
            }
            if(parseFloat(totalAmount).toFixed(2) != parseFloat(saleAmount).toFixed(2)){
            	$custom.alert.error("合计的商品金额与订单金额不一致！");
                return;
            }

            var form = [];
            form.push({"name":"orderId","value":orderId});
            form.push({"name":"saleAmount","value":saleAmount});
            form.push({"name":"actualMarginAmount","value":actualMarginAmount});
            form.push({"name":"applyDate","value":applyDate+" 00:00:00"});
            form.push({"name":"items","value":JSON.stringify(itemList)});
            $custom.request.ajax(serverAddr+"/sale/applyFinance.asyn",form,function(result){
                if(result.state==200){
                    $custom.alert.success("融资申请成功！");
                    $m9038.hide();
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

    //监听销售金额的离开事件
    $("#apply-table-list").on("blur",'.goods-amount',function(){
        var that = $(this);
        //获取总金额
        var sum= that.val();
        var tr = that.parent().parent();
        //获取销售数量
        var num = tr.find("input[name='goods-num']").val();
        if(!sum){
            that.val("");
            return ;
        }
        //判断是否数字
        if(isNaN(sum)){
            $custom.alert.error("金额请输入数字");
            that.val("");
            return ;
        }
        sum = parseFloat(sum) ;
        sum = sum.toFixed(2) ;
        $(this).val(sum) ;
        //设置单价
        var price = 0;
        if(!!num && !!sum){
            price = Number(sum/num).toFixed(4);
        }
        tr.find("input[name='price']").val(price);
        calAmount();
    });
function calAmount(){
   var total = 0;
    $("#apply-tbody tr").each(function (i) {
        var ch = $(this).find('input[name="amount"]').val();
        if(!ch){
            ch = 0;
        }
        total = parseFloat(total) + parseFloat(ch);
    })
    $("#apply_totalAmount").html(parseFloat(total).toFixed(2));
    
}
 function numFormat(obj){
     obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d{1,2}).*$/, '$1$2.$3');
     if (obj.value.indexOf(".") < 0 && obj.value != "") {//如果没有小数点，首位不能为类似于 01、02的金额
         obj.value = parseFloat(obj.value);
     }
 }

laydate.render({
    elem: '#apply_applyDate', //指定元素
    type: 'date',
    format: 'yyyy-MM-dd',
    ready: function () {
        $('.laydate-btns-time').remove();
    }
});
</script>