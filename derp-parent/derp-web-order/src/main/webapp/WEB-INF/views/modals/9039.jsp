<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <!-- amount Adjust content -->
    <div id="financeRansom-modal"  class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none; overflow-y: auto;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content modal-lg" style="width:1000px;">
                <div class="modal-header">
                    <h5 class="modal-title" >融资赎回</h5>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="ransomFinanceForm">
						<div class="row">
							<input class="form-control" id="modal_finance_id" type="hidden">
							 <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;客户：</span>
                                <span id="ransom_customerName"></span>
                            </div>
							<div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PO号：</span>
                                <span id="ransom_poNo"></span>
                            </div>
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币种：</span>
                                <span id="ransom_currency"></span>
                            </div>
						</div>
						<div class="row">
							<div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;事业部：</span>
                                <span id="ransom_buName"></span>
                            </div>
							<div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;创建人：</span>
                                <span id="ransom_createName"></span>
                            </div>
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;创建时间：</span>
                                <span id="ransom_createDate"></span>
                            </div>
						</div>
						<div class="row">
							<div class="col-md-4">
								<label><i style="color: red">*&nbsp;</i>订单金额:</label> 
								<input class="input_msg" name="ransom_saleAmount" id="ransom_saleAmount" disabled/>
							</div>
							<div class="col-md-4">
								<label><i style="color: red">*&nbsp;</i>起算日期:</label> 
								<input type="text" class="ransom_input_msg form_datetime date-input" name="ransom_applyDate" id="ransom_applyDate" disabled>
							</div>
                            <div class="col-md-4">
                                <label><i style="color: red">*&nbsp;</i>实际还款日期:</label>
                                <input type="text" class="ransom_input_msg form_datetime date-input" name="ransom_dealineDate" id="ransom_dealineDate">
                            	<input class="form-control" id="modalRansomDealineDate" type="hidden" value="" />
                            </div>
						</div>
                        <div class="row">
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;保证金：</span>
                                <span id="ransom_marginAmount"></span>
                            </div>
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;还款本金：</span>
                                <span id="ransom_principalAmount"></span>
                            </div>
                            <div class="info_text">
                                <span>资金占用费：</span>
                                <span id="ransom_occupationAmount"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;代理费：</span>
                                <span id="ransom_agencyAmount"></span>
                            </div>
                            <div class="info_text">
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;滞纳金：</span>
                                <span id="ransom_delayAmount"></span>
                            </div>
                            <div class="info_text">
                                <span>应还款金额：</span>
                                <span id="ransom_payableAmount"></span>
                            </div>
                        </div>
					</form>
					<br>
					<span><i class="dripicons-information" style="color: #00a0e9;"></i><i style="color: #00a0e9;font-style:normal;">将用于销售开票，请输入客户实际收货数量</i></span>
                    <div class="form-row mt20 ml15 table-responsive">
                        <table id="ransom-table-list" class="table table-striped" style="width: 100%;">
                            <thead>
                            <tr>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>销售数量</th>
                                <th>销售单价</th>
                                <th><i class="red">*</i>销售金额</th>
                            </tr>
                            </thead>
                            <tbody id="ransom-tbody">
                            </tbody>
                            <tr>
                                <td colspan="2">合计</td>
                                <td id="ransom_totalNum"></td>
                                <td></td>
                                <td id="ransom_totalAmount"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" id="calAmountButton" class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='$m9039.calAmount();'>还款试算</button>
                                <button type="button" id="applyButton" class="btn btn-info waves-effect waves-light fr ml15" onclick='$m9039.apply();'>申请赎回</button>
                                <button type="button" id="confirmButton" class="btn btn-info waves-effect waves-light fr ml15" onclick='$m9039.confirm();' style="display: none">确定赎回</button>
                                <button type="button" id="cancelButton" class="btn btn-info waves-effect waves-light fr ml15" onclick='$m9039.cancel();' style="display: none">作废</button>
                                <button type="button" id="hideButton" class="btn btn-light waves-effect waves-light ml15"  onclick='$m9039.hide();'>取消</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m9039 = {
        init: function(id){
            $m9039.params.orderId = id;
            $m9039.show();
        },
        show: function(){
            $($m9039.params.modalId).modal({backdrop: 'static', keyboard: false});
            $("#ransom-tbody").empty();
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
            var applyDate ='';
            $custom.request.ajaxSync(serverAddr+"/sale/getFinanceDetail.asyn", {"orderId":$m9039.params.orderId}, function(result){
                if(result.state== 200 && result.data){
                    saleAmount = result.data.saleAmount;
                    customerName =result.data.customerName;
                    poNo = result.data.poNo;
                    currency = result.data.currency;
                    buName = result.data.buName;
                    createName = result.data.createName;
                    createDate = result.data.createDate;
                    applyDate = result.data.applyDate;

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
                            + '<td><input type="text" name="amount" class="goods-amount" style="width:100px;text-align:center;" disabled value="'+amount+'" /></td>'
                            + '</tr>';
                        totalNum += num;
                        if(!amount){
                        	amount = 0;
                        }
                        totalAmount += parseFloat(amount);                        
                    }
                }
            });
            
            $("#ransom-tbody").append(html);
            $("#ransom_totalNum").html(totalNum);
            $("#ransom_totalAmount").html(parseFloat(totalAmount).toFixed(2));
            $("#ransom_saleAmount").val(parseFloat(saleAmount).toFixed(2));
            $("#ransom_customerName").html(customerName);
            $("#ransom_poNo").html(poNo);
            $("#ransom_currency").html(currency);
            $("#ransom_buName").html(buName);
            $("#ransom_createName").html(createName);
            $("#ransom_createDate").html(createDate);
            // 返回日期格式化校验
            var aDate=new Date(applyDate);
            var year = aDate.getFullYear();
            var month = aDate.getMonth()+ 1, month = month < 10 ? '0' + month : month;
            var day = aDate.getDate(), day = day < 10 ? '0' + day : day;
            $("#ransom_applyDate").val(year + '-' + month + '-' + day);
            $($m9039.params.modalId).modal("show");
        },
        hide:function(){
            $("#applyButton").css("display","block");
            $("#confirmButton").css("display","none");
            $("#ransomFinanceForm")[0].reset();
            $("#ransom-tbody").empty() ;
            $($m9039.params.modalId).modal("hide");
        },
        params:{
        	orderId:null,
            modalId:'#financeRansom-modal'
        },
        calAmount:function(){
            var saleAmount = $("#ransom_saleAmount").val();
            if(!saleAmount || saleAmount == ""  ){
                $custom.alert.error("订单金额不能为空");
                return ;
            }
            var applyDate = $("#ransom_applyDate").val();
            if(!applyDate || applyDate == ""  ){
                $custom.alert.error("起算日期不能为空");
                return ;
            }
            var dealineDate = $("#ransom_dealineDate").val();
            if(!dealineDate || dealineDate == ""  ){
                $custom.alert.error("实际还款日不能为空");
                return ;
            }
            var startDate = new Date(applyDate);
            var endDate = new Date(dealineDate);
            if(startDate.getTime() > endDate.getTime()){
                $custom.alert.error("实际还款日必须大于起算日期");
                return ;
            }
            $custom.request.ajaxSync(serverAddr+"/sale/calRepayAmount.asyn", {"orderId":$m9039.params.orderId,"dealineDate":dealineDate}, function(result){
                var dDate = '';//还款日
                var marginAmount = '';//保证金
                var principalAmount = '';//还款本金
                var occupationAmount = '';//资金占用费
                var agencyAmount = '';//代理费
                var delayAmount = '';//滞纳金
                var payableAmount = '';//应还款金额
                if(result.state == 200 && result.data){
                    dDate = result.data.dealineDate;//还款日
                    marginAmount = result.data.marginAmount == null ? '' : parseFloat(result.data.marginAmount).toFixed(2);//保证金
                    principalAmount = result.data.principalAmount  == null ? '' : parseFloat(result.data.principalAmount).toFixed(2);//还款本金
                    occupationAmount = result.data.occupationAmount == null ? '' : parseFloat(result.data.occupationAmount).toFixed(2);//资金占用费
                    agencyAmount = result.data.agencyAmount == null ? '' : parseFloat(result.data.agencyAmount).toFixed(2);//代理费
                    delayAmount = result.data.delayAmount == null ? '' : parseFloat(result.data.delayAmount).toFixed(2);//滞纳金
                    payableAmount = result.data.payableAmount == null ? '' : parseFloat(result.data.payableAmount).toFixed(2);//应还款金额
                }else{
                	if(!!result.expMessage){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
                    return;
                }
                $("#ransom_marginAmount").html(marginAmount);
                $("#ransom_principalAmount").html(principalAmount);
                $("#ransom_occupationAmount").html(occupationAmount);
                $("#ransom_agencyAmount").html(agencyAmount);
                $("#ransom_delayAmount").html(delayAmount);
                $("#ransom_payableAmount").html(payableAmount);
            });
        },
        apply:function(){
            var orderId = $m9039.params.orderId;
            var saleAmount = $("#ransom_saleAmount").val();
            if(!saleAmount || saleAmount == ""  ){
                $custom.alert.error("订单金额不能为空");
                return ;
            }
            var applyDate = $("#ransom_applyDate").val();
            if(!applyDate || applyDate == ""  ){
                $custom.alert.error("起算日期不能为空");
                return ;
            }
            var dealineDate = $("#ransom_dealineDate").val();
            if(!dealineDate || dealineDate == ""  ){
                $custom.alert.error("实际还款日不能为空");
                return ;
            }
            var startDate = new Date(applyDate);
            var endDate = new Date(dealineDate);
            if(startDate.getTime() > endDate.getTime()){
                $custom.alert.error("实际还款日必须大于起算日期");
                return ;
            }
            $custom.request.ajaxSync(serverAddr+"/sale/applyRansom.asyn", {"orderId":orderId,"dealineDate":dealineDate}, function(result){
                var dDate = '';//还款日
                var marginAmount = '';//保证金
                var principalAmount = '';//还款本金
                var occupationAmount = '';//资金占用费
                var agencyAmount = '';//代理费
                var delayAmount = '';//滞纳金
                var payableAmount = '';//应还款金额
                var totalAmount = 0;
                if(result.state== 200 && result.data){
                    dDate = result.data.dealineDate;//还款日
                    marginAmount = result.data.marginAmount == null ? '' : parseFloat(result.data.marginAmount).toFixed(2);//保证金
                    principalAmount = result.data.principalAmount  == null ? '' : parseFloat(result.data.principalAmount).toFixed(2);//还款本金
                    occupationAmount = result.data.occupationAmount == null ? '' : parseFloat(result.data.occupationAmount).toFixed(2);//资金占用费
                    agencyAmount = result.data.agencyAmount == null ? '' : parseFloat(result.data.agencyAmount).toFixed(2);//代理费
                    delayAmount = result.data.delayAmount == null ? '' : parseFloat(result.data.delayAmount).toFixed(2);//滞纳金
                    payableAmount = result.data.payableAmount == null ? '' : parseFloat(result.data.payableAmount).toFixed(2);//应还款金额
                    if(result.data.itemList){
                        $("#ransom-tbody tr").each(function (i) {
                            var goodId =  $(this).find('input[name="goodsId"]').val();
                            var goodNum =  $(this).find('input[name="goods-num"]').val();
                            a:for (var j = 0; j < result.data.itemList.length; j++) {
                                var m = result.data.itemList[i];
                                var resultGoodsId = m.goodsId;
                                if(goodId ==resultGoodsId ){
                                    var amount = m.amount == '' ? '':parseFloat(m.amount).toFixed(2);
                                    $(this).find('input[name="amount"]').val(amount);
                                    if(!amount){
                                        amount = 0;
                                    }
                                    var price = parseFloat(m.amount).toFixed(2) / parseInt(goodNum);
                                    $(this).find('input[name="price"]').val(parseFloat(price).toFixed(4));
                                    totalAmount = parseFloat(totalAmount) +parseFloat(amount) ;
                                    break a;
                                }
                            }
                        });
                    }
                   
                }else{
                	if(!!result.expMessage){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
                    return;
                }
                $("#ransom_marginAmount").html(marginAmount);
                $("#ransom_principalAmount").html(principalAmount);
                $("#ransom_occupationAmount").html(occupationAmount);
                $("#ransom_agencyAmount").html(agencyAmount);
                $("#ransom_delayAmount").html(delayAmount);
                $("#ransom_payableAmount").html(payableAmount);
                $("#ransom_totalAmount").html(parseFloat(totalAmount).toFixed(2));
                
                $("#applyButton").css("display","none");
                $("#hideButton").css("display","none");
                $("#confirmButton").css("display","block");
                $("#cancelButton").css("display","block");
                $("#modalRansomDealineDate").val($("#ransom_dealineDate").val());

            });
        },
        confirm:function () {
           var orderId = $m9039.params.orderId;
            var saleAmount = $("#ransom_saleAmount").val();
           if(!saleAmount || saleAmount == ""  ){
               $custom.alert.error("订单金额不能为空");
               return ;
           }
           var applyDate = $("#ransom_applyDate").val();
           if(!applyDate || applyDate == ""  ){
               $custom.alert.error("起算日期不能为空");
               return ;
           }
           var dealineDate = $("#ransom_dealineDate").val();
           if(!dealineDate || dealineDate == ""  ){
               $custom.alert.error("实际还款日不能为空");
               return ;
           }
           $custom.request.ajaxSync(serverAddr+"/sale/confirmRansom.asyn", {"orderId":orderId,"dealineDate":dealineDate}, function(result){
               if(result.state== 200){
                   $custom.alert.success("赎回成功！");
                   $m9039.hide();
                   $mytable.fn.reload();
               }else{
            	   if(!!result.expMessage){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
            	   return;
               }
           });
       	},cancel:function(){
       		var orderId = $m9039.params.orderId;
            var saleAmount = $("#ransom_saleAmount").val();
           if(!saleAmount || saleAmount == ""  ){
               $custom.alert.error("订单金额不能为空");
               return ;
           }
           var applyDate = $("#ransom_applyDate").val();
           if(!applyDate || applyDate == ""  ){
               $custom.alert.error("起算日期不能为空");
               return ;
           }
           var dealineDate = $("#ransom_dealineDate").val();
           if(!dealineDate || dealineDate == ""  ){
               $custom.alert.error("实际还款日不能为空");
               return ;
           }
       		$custom.request.ajaxSync(serverAddr+"/sale/cancelRansom.asyn", {"orderId":orderId,"dealineDate":dealineDate}, function(result){
                if(result.state== 200){
                    $custom.alert.success("作废成功！可以重新申请赎回");
                    $("#applyButton").css("display","block");
                    $("#hideButton").css("display","block");
                    $("#confirmButton").css("display","none");
                    $("#cancelButton").css("display","none");
                }else{
             	   if(!!result.expMessage){
 						$custom.alert.error(result.expMessage);
 					}else{
 						$custom.alert.error(result.message);
 					}
             	    return;
                }
            });
        }
       	
    };

laydate.render({
    elem: '#ransom_dealineDate', //指定元素
    type: 'date',
    format: 'yyyy-MM-dd',
    ready: function () {
        $('.laydate-btns-time').remove();
    },done:function(value,date){
    	changeDeadLineDate();
    }
});
//修改实际还款日 按钮重置为 “申请赎回”
function changeDeadLineDate(){
	if($("#confirmButton").is(":visible")){
		var beforDate = $("#modalRansomDealineDate").val();
		var nowDate = $("#ransom_dealineDate").val();
		
		if(beforDate){
			if( beforDate != nowDate ){
				$custom.alert.warning("修改实际还款日重新申请赎回，是否作废上次申请？",function(){
			        $m9039.cancel();    		
				});
			}
		}else{
			$("#modalRansomDealineDate").val(nowDate);
		}		
	}
	
}


</script>