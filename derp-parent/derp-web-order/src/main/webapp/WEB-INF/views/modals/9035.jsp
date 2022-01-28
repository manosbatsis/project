<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
	<div class="col-12">
		<div>
			<!-- Signup modal content -->
			<div id="chooseSupply-modal" class="modal fade bs-example-modal-lg"
				tabindex="-1" role="dialog"
				aria-labelledby="custom-width-modalLabel" aria-hidden="true"
				style="display: none; overflow-y: auto; margin-left: -170px;">
				<div class="modal-dialog">
					<div class="modal-content" style="width: 1000px;">
						<div class="modal-header">
							<h5 class="modal-title" id="myLargeModalLabel">生成采购订单</h5>
						</div>
						<div class="modal-body">
							<form class="form-horizontal" id="createPurchaseForm">
								<!-- 自定义参数  -->
								<div class="row">
									<input class="form-control" id="modal_sale_id" type="hidden">
									<div class="col-md-6">
										<label style="margin-left: 10px"><i style="color: red">*&nbsp;</i>供应商:</label>
										<select name="modalSupplierId" id="modalSupplierId"
											class="edit_input">
											<option value="">请选择</option>
										</select>
									</div>
									<div class="col-md-6">
										<label><i class="dripicons-information"
											style="color: #00a0e9;" data-toggle="tooltip"
											data-placement="bottom" title="采购单价=销售单价*折扣比例"></i> 折扣比例: </label> <input
											style="width: 70%" class="input_msg" name="modalDiscount"
											id="modalDiscount" placeholder="请输入，最多5个小数位" />
									</div>
								</div>
								<div class="row">
									<div class="col-md-8">
										<label>采购PO号:</label> 
										<input class="input_msg" name="modalPoNo" style="width: 52%" />
										<a style="color: #00a0e9; cursor: pointer" onclick="javascript:getSalePoNo()">取销售订单PO号</a>
									</div>
								</div>

							</form>
							<div class="form-row mt20 ml15 table-responsive">
								<table id="purchaseTable-list" class="table table-striped" style="width: 100%;">
									<thead>
										<tr>
											<th>商品货号</th>
											<th>商品名称</th>
											<th>采购数量</th>
											<th><i class="red">*</i>采购单价</th>
											<th><i class="red">*</i>采购金额</th>
										</tr>
									</thead>
									<tbody id="purchaseTbody">
									</tbody>
									<tr>
										<td colspan="2">合计</td>
										<td id="purchaseTotalNum"></td>
										<td></td>
										<td id="purchaseTotalAmount" ></td>
									</tr>
								</table>
							</div>
							<div class="form-row mt20">
								<div class="form-inline m0auto">
									<div class=form-group>
										<button type="button"
											class="btn btn-info waves-effect waves-light fr delayedBtn" onclick='chooseSave()'>确定</button>
										<button type="button"
											class="btn btn-light waves-effect waves-light ml15" onclick='chooseCancel()'>取消</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
	</div>
	<!-- end col -->
</div>
<script type="text/javascript">

var $m9035 = {
		init: function(data){
			$('input[name="modalPoNo"]').val('');
			$('input[name="modalDiscount"]').val('');
            //加载销售币种的下拉数据
            $('select[name="modalSupplierId"]').empty();
            $module.supplier.loadSelectData.call($('select[name="modalSupplierId"]')[0],"");             
            
            $("#modal_sale_id").val(data.id);
            this.show(data);
            this.params.paramJson = data;
        },
        show: function(data){
            $("#purchaseTbody").empty();
            var html = "" ;
            var totalNum = 0;
            var totalAmount = 0;
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
                html += '<tr>'
                    + '<td>'+goodsNo+'<input type="hidden" name="purchaseGoodsNo" value="'+goodsNo+'"/><input type="hidden" name="purchaseGid" value="'+id+'"/><input type="hidden" name="purchaseGoodsId" value="'+goodsId+'"/><input type="hidden" name="purchaseGoodsCode" value="'+goodsCode+'"/></td>'
                    + '<td>'+goodsName+'<input type="hidden" name="purchaseGoodsName" value="'+goodsName+'"/></td>'
                    + '<td>'+num+'<input type="hidden" name="purchaseGoods-num" value="'+num+'"/></td>'
                    + '<td><input type="text" name="purchasePrice" class="purchase-price" style="width:100px;text-align:center;" value="'+parseFloat(price).toFixed(8)+'" /><input type="hidden" name="modalSalePrice" value="'+parseFloat(price).toFixed(8)+'"/></td>'
                    + '<td><input type="text" name="purchaseAmount" class="purchase-amount" style="width:100px;text-align:center;" value="'+parseFloat(amount).toFixed(2)+'"/><input type="hidden" name="modalSaleAmount" value="'+parseFloat(amount).toFixed(2)+'"/></td>'
                    + '</tr>';
                totalNum += num;
                totalAmount += amount;
            }
            $("#purchaseTbody").append(html);
            $("#purchaseTotalNum").html(totalNum);
            $("#purchaseTotalAmount").html(parseFloat(totalAmount).toFixed(2));
            $($m9035.params.modalId).modal("show");
        },
        hide:function(){
            $($m9035.params.modalId).modal("hide");
        },
        params:{
            modalId:'#chooseSupply-modal',
            paramJson:{}
        }
}

    // 保存
    function chooseSave(){
        var modalSupplierId = $("#modalSupplierId").val(); 
        var modalPoNo = $("input[name='modalPoNo']").val();       
        //必填项校验
        if(modalSupplierId =="" || modalSupplierId == null || modalSupplierId == undefined){
            $custom.alert.error("请选择供应商!");
            return ;
        }
       
       if(modalPoNo !="" && modalPoNo != null && modalPoNo != undefined){
    	   $custom.request.ajax(serverAddr+"/sale/checkExistPurchaseByPO.asyn",{"poNo":modalPoNo},function(result){
               if(result.data.code =='01'){     	   
            	   $custom.alert.warningBtnText("已存在相同PO号的采购单，是否继续生成采购单？","继续","取消",function(){
            		   saveData();
           	 		});
               }else{
            	   saveData();
               }
    	   });
    	  
       }else{
    	   saveData(); 
       }
      
 };
 
	 function saveData(){
		 var saleOrderId = $("#modal_sale_id").val();
         var modalSupplierId = $("#modalSupplierId").val(); 
         var modalPoNo = $("input[name='modalPoNo']").val();
         
		 var itemList = [];
	     var flag = true;	        
		 $('#purchaseTbody tr').each(function(i){
            var item = {};
            var id = $(this).find('input[name="purchaseGid"]').val();
            var goodsId = $(this).find('input[name="purchaseGoodsId"]').val();
            var goodsCode = $(this).find('input[name="purchaseGoodsCode"]').val();
            var goodsNo = $(this).find('input[name="purchaseGoodsNo"]').val();
            var price = $(this).find('input[name="purchasePrice"]').val();
            var num = $(this).find('input[name="purchaseGoods-num"]').val();
            var amount = $(this).find('input[name="purchaseAmount"]').val();
            if(isNaN(price)){
                $custom.alert.error("采购单价请输入数字");
                $(this).val("");
                flag = false;
                return ;
            }
            if(!price || parseFloat(price) < 0){
                $custom.alert.error("采购单价不能为空或小于0");
                flag = false;
                return;
            }
            if(isNaN(amount)){
                $custom.alert.error("采购金额请输入数字");
                flag = false;
                return;
            }
           if(!amount || parseFloat(amount) < 0){
                $custom.alert.error("采购金额不能为空或小于0");
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
            itemList.push(item);
        });
        if(!flag){
            return false;
        }
        if(!itemList || itemList.length==0){
            $custom.alert.error("该采购订单没有商品！");
            return;
        }
        var json = {};
        json['saleOrderId']=saleOrderId;
        json['supplierId']=modalSupplierId;
        json['poNo']=modalPoNo;
        json['itemList']=itemList;
        var jsonStr= JSON.stringify(json);
        var form = {};
        form['json']=jsonStr;
	        
		 $custom.request.ajax(serverAddr+"/sale/GeneratePurchaseOrder.asyn",form,function(result){
            if(result.state==200){
            	if(result.data == null){
            		 $custom.alert.error("生成采购单失败");
            	}else{
            		swal({
        				title: "生成采购单成功，是否进入采购订单编辑页面？",
        				type: 'warning',
        				showCancelButton: true,
        				confirmButtonColor: '#4fa7f3',
        				cancelButtonColor: '#d57171',
        				confirmButtonText: "是",
        				cancelButtonText: "否",
        			}).then(function(){
	            			$m9035.hide(); 
							setTimeout(function () {
								$module.load.pageOrder("act=30012&id="+result.data);
				            }, 500);
	            		},function(dismiss){
	               			if(dismiss == 'cancel'){
	               				$m9035.hide();
	    		                $mytable.fn.reload();
	               			}
	           		});
            	};
            }else{
                if(!!result.expMessage){
                    $custom.alert.error(result.expMessage);
                }else{
                    $custom.alert.error(result.message);
                }
            }
   		});
	 }
   
    function chooseCancel(){
        $('#chooseSupply-modal').modal('hide');     
    }
    
    function getSalePoNo(){
    	var poNo = $m9035.params.paramJson.poNo;
    	$('input[name="modalPoNo"]').val(poNo);
    }
   
    //采购单价=销售单价*折扣比例，保留8位小数 ,采购金额=销售销售*折扣比例，保留2位小数
    $("#modalDiscount").blur(function(){
    	var discount = $(this).val();
    	if(isNaN(discount)){
            $custom.alert.error("折扣比例请输入数字");
            $(this).val("");
            return ;
        }
    	if(discount < 0 || discount >= 10){
    		$custom.alert.error("请输入0.00000~9.99999之间的数字");
            return ;
    	}
    	if(discount == '' || discount == null || discount == undefined || discount == 0){
    		discount = "1";
    	}
    	var indexOf = discount.indexOf(".");
        // 如果是小数
        if (indexOf != -1) {
            var count = (parseFloat(discount)+"").length - 1 - indexOf;
            if (count > 5) {
                $custom.alert.error("折扣比例最多5个小数位");
                return;
            }
        } 
        var total = 0;
    	$('#purchaseTbody tr').each(function (i) {
    		var price = $(this).find('input[name="modalSalePrice"]').val();
    		var num = $(this).find('input[name="purchaseGoods-num"]').val();
    		price = parseFloat(price) * parseFloat(discount);    		
    		var amount = parseInt(num) * price;
    		
    		$(this).find('input[name="purchasePrice"]').val(parseFloat(price).toFixed(8));
    		$(this).find('input[name="purchaseAmount"]').val(parseFloat(amount).toFixed(2));    		
    		total =  parseFloat(total) + parseFloat(parseFloat(amount).toFixed(2));
    	});
    	
    	$("#purchaseTotalAmount").html(parseFloat(total).toFixed(2));
    });
    $("#purchaseTable-list").on("blur",'.purchase-price',function(){
    	var price = $(this).val();
    	if(isNaN(price)){
            $custom.alert.error("采购单价请输入数字");
            $(this).val("");
            return ;
        }
    	var indexOf = price.indexOf(".");
    	if (indexOf != -1) {
            var count = (parseFloat(price)+"").length - 1 - indexOf;
            if (count > 8) {
                $custom.alert.error("采购单价只能为8位小数");
                return;
            }
        }
    	
    	var tr = $(this).parent().parent();
    	//获取采购数量
    	var num = tr.find("input[name='purchaseGoods-num']").val(); 
   		var amount = parseInt(num) * price;
   		tr.find("input[name='purchaseAmount']").val(parseFloat(amount).toFixed(2));  		
   		tr.find("input[name='purchasePrice']").val(parseFloat(price).toFixed(8));
   		calAmount2();
    });
    
    $("#purchaseTable-list").on("blur",'.purchase-amount',function(){
    	var amount = $(this).val();
    	if(isNaN(amount)){
            $custom.alert.error("采购金额请输入数字");
            $(this).val("");
            return ;
        }
    	var indexOf = amount.indexOf(".");
    	if (indexOf != -1) {
            var count = (parseFloat(amount)+"").length - 1 - indexOf;
            if (count > 2) {
                $custom.alert.error("采购金额只能为2位小数");
                return;
            }
        }
    	
    	var tr = $(this).parent().parent();
    	//获取采购数量
    	var num = tr.find("input[name='purchaseGoods-num']").val(); 
   		var  price = amount / parseInt(num);
   		tr.find("input[name='purchasePrice']").val(parseFloat(price).toFixed(8));
   		tr.find("input[name='purchaseAmount']").val(parseFloat(amount).toFixed(2));
   		calAmount2();
    	
    });
    
    function calAmount2(){
   	   var total = 0;
   	    $("#purchaseTbody tr").each(function (i) {
   	        var ch =$(this).find('input[name="purchaseAmount"]').val();
   	        total = parseFloat(total) + parseFloat(ch);
   	    })
   	    $("#purchaseTotalAmount").html(parseFloat(total).toFixed(2));
   	}
</script>