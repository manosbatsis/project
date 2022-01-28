<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content-->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">预售单编辑</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 客   户：</label>
                            <select class="edit_input" name="customerId" id="customerId" parsley-trigger="change" required reqMsg="客户不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公   司：</label>
                            <input type="hidden" value="${detail.id}" name="orderId" id="orderId"/>
                            <input type="hidden" value="${detail.code}" name="orderCode" id="orderCode">
                            <input type="hidden" value="${detail.merchantId}" name="merchantId" id="merchantId"/>
                            <input type="text" class="edit_input" value="${detail.merchantName}" name = "merchantName" id = "merchantName" disabled required reqMsg="公司不能为空">
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 销售类型：</label>
	                            <select class="edit_input" name="businessModel" id="businessModel" required reqMsg="销售类型不能为空"></select>
                        </div>
                    </div>
                    <div class="edit_box">
                    	<div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 出仓仓库：</label>
                            <select class="edit_input" name="outDepotId" id="outDepotId" parsley-trigger="change" required reqMsg="请选择出仓仓库">
                                <option value="">请选择</option>
                            </select>
                        </div>
                         <div class="edit_item">
	                        <label class="edit_label"><i class="red">*</i> 事业部：</label>
                            <select class="edit_input" name="buId" id="buId" parsley-trigger="change" required reqMsg="请选择事业部">
                                <option value="">请选择</option>
                            </select>
                         </div>
                        <div class="edit_item">
                            <label class="edit_label">
                                <i class="red">*</i> PO单号：</label>
                            <input type="text" class="edit_input" name="poNo" id="poNo" value="${detail.poNo}" placeholder="多PO输入时以&字符隔开" required reqMsg="PO单号不能为空">
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>销售币种：</label>
                            <select class="edit_input" name="currency" id="currency" parsley-trigger="change" required reqMsg="请选择销售币种">
                                <option value="">请选择</option>
                            </select>
                            <input id="currency_hidden" type="hidden" value="${detail.currency }">
                        </div>
                        <c:if test="${isTallyingRequired==1}">
                            <div class="edit_item" id="tallyingUnitDiv" >
                                <c:if test="${not empty detail.tallyingUnit}">
                                    <label class="edit_label"><i class="red">*</i> 理货单位：</label>
                                    <select name="tallyingUnit" id="tallyingUnit" class="edit_input">
                                    </select>
                                </c:if>
                            </div>
                        </c:if>
                        <c:if test="${isTallyingRequired==0}"><div class="edit_item" id="tallyingUnitDiv" ></div></c:if>
                        <div class="edit_item"></div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item" style="flex: 2">
                            <label class="edit_label"  style="flex: 0.369">备 注：</label>
                            <textarea rows="" cols="" class="edit_input" name="remark" id="remark">${detail.remark}</textarea>
                        </div>
                        <div class="edit_item"></div>
                    </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-10">
                             <div class="title_text">商品信息</div>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(2, null, null);">选择商品</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                    </div>                       
                    <div class="form-row mt20 ml15">
                   <table id="table-list" class="table table-striped">
                   <thead>
                      <tr>
                         <th><input id="checkbox11" type="checkbox" name="all"></th>
                          <th>商品货号</th>
                          <th>商品名称</th>
                          <th>条码</th>
                          <th>预售数量</th>
                          <th>预售单价</th>
                          <th>预售总金额</th>
                          <th>品牌类型</th>
                      </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${detail.itemList}" var="item">
                            <tr>
                                <td><input type='checkbox' name='item-checkbox'><input type='hidden' name='goodsId' value="${item.goodsId}"></td>
                                <input type='hidden' name='id' value='${item.id}'>
                                <td>${item.goodsNo}<input type="hidden" name="goodsNo" /></td>
                                <td>${item.goodsName}<input type="hidden" name="goodsName" /></td>
                                <td>${item.barcode}<input type="hidden" name="barcode" /></td>
                                <td><input type='text' name='num' style='width:70px;text-align:center;' class='goods-num' value="${item.num}"></td>
                                <td><input type='text' name='price' value='<fmt:formatNumber value="${item.price}" pattern="#.########" minFractionDigits="8" />'  class='goods-price' style='width:70px;text-align:center;'></td>
                                <td><input type='text' name='amount' value="<fmt:formatNumber value="${item.amount}" pattern="#.##" minFractionDigits="2"/>" class='goods-amount'  style='width:70px;text-align:center;'  onkeyup="numFormat(this)"></td>
                                <td>${item.brandName}<input type="hidden" name="brandName" /></td>
                            </tr>
                        </c:forEach>
                      </tbody>
                  </table>                              
                   </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-4">
                                  </div>
                                  <div class="col-4">
                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-temp-buttons" value="保 存"/>
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                      <shiro:hasPermission name="preSale_addPreSaleOrder">
                                      		<input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保存并审核"/>
                                 	  </shiro:hasPermission>
                                  </div>
                                  <div class="col-4">
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
             </div>
              <!-- end row -->
              <!-- 弹窗开始 -->
              <jsp:include page="/WEB-INF/views/modals/9051.jsp" />
              <!-- 弹窗结束 -->
              <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
//加载客户的下拉数据
$module.customer.loadSelectData.call($('select[name="customerId"]')[0],'${detail.customerId}');
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],'${detail.buId}', null,'${detail.outDepotId}',null);
$module.constant.getConstantListByNameURL.call($('select[name="businessModel"]')[0],"preSaleOrder_businessModelList",'${detail.businessModel}');
$module.constant.getConstantListByNameURL.call($('select[name="currency"]')[0],"currencyCodeList",'${detail.currency}');
//加载仓库的下拉数据（可选项为该公司下已有关联且已启用的仓库（仅保税仓、海外仓））
$module.depot.getSelectBeanByMerchantRel.call($('select[name="outDepotId"]')[0],'${detail.outDepotId}',{"type":"a,c"});
 $(document).ready(function() {
        // 若是海外仓
        var isRequired= ${isTallyingRequired};
        if( isRequired == 1){
         $module.constant.getConstantListByNameURL.call($('select[name="tallyingUnit"]')[0],"order_tallyingUnitList",'${detail.tallyingUnit}');
        }
        //根据客户选择带出对应的币种
        $("#customerId").change(function(){
            var val = $(this).val() ;
            if(val){
                $module.currency.loadSelectData.call($("select[name='currency']")[0],val);
            }
        }) ;
        $(function(){
            //取消按钮
            $("#cancel-buttons").click(function(){
                $module.load.pageOrder('act=6001');
            });
        });
        // 失焦时
        $("#poNo").blur(function(){
            var poNoVal = $("#poNo").val();
            // 若PO不为空
            if(poNoVal){
                var results	= poNoVal.split("&");
                var result = results.sort();
                for(var i=0;i<result.length-1;i++){
                    if(result[i]==result[i+1]){	// PO号重复了
                        $custom.alert.error("PO号重复了");
                    }
                }
            }else{
                $custom.alert.error("PO号不能为空");
            }
        });
     // 出库仓改变的时候
     $("#outDepotId").change(function() {
         var outId=$("#outDepotId").val();
         $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId,null, null,outId,null);
     });
		//保存按钮
		$("#save-temp-buttons").click(function(){
			var url = serverAddr+"/preSale/saveOrModifyTempOrder.asyn";
			var form = {
			};
			var json = {};
            var orderId = $("#orderId").val();
            var orderCode = $("#orderCode").val();
			var customerId = $("#customerId").val();
            var merchantId = $("#merchantId").val();
            var merchantName = $("#merchantName").val();
            var businessModel = $("#businessModel").val();
			var outDepotId = $("#outDepotId").val();
            var buId = $("#buId").val();	// 事业部
			var poNo = $("#poNo").val();
            var currency = $("#currency").val();
            var remark = $("#remark").val();
            var tallyingUnit = $("#tallyingUnit").val();// 当出仓仓库为海外仓时，理货单位必填

            if(!outDepotId){
                $custom.alert.error("出仓仓库不能为空");
                return;
            }
            if(!buId){
                $custom.alert.error("事业部不能为空");
                return;
            }
			if(poNo!=null){
                var results	= poNo.split("&");
                var result = results.sort();
                for(var i=0;i<result.length-1;i++){
                  if(result[i]==result[i+1]){	// PO号重复了
                      $custom.alert.error("修改失败，PO号重复了");
                      return;
                  }
                }
			}
			var itemList = [];
			var flag = true;
            // 商品信息
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
                var id = $(this).find('input[name="id"]').val();
                item['id']=id;
				var goodsId = $(this).find('input[name="goodsId"]').val();
				item['goodsId']=goodsId;
                var goodsCode = $(this).find('input[name="goodsCode"]').val();
                item['goodsCode']=goodsCode;
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				item['goodsNo']=goodsNo;
                var goodsName = $(this).find('input[name="goodsName"]').val();
                item['goodsName']=goodsName;
				var barcode = $(this).find('input[name="barcode"]').val();
				item['barcode']=barcode;
				var num = $(this).find('input[name="num"]').val();
				item['num']=num;
				var price = $(this).find('input[name="price"]').val();
				item['price']=price;
				var amount = $(this).find('input[name="amount"]').val();
				item['amount']=amount;
				var brandName = $(this).find('input[name="brandName"]').val();
				item['brandName']=brandName;
				if(!num || isNaN(num) ||num<=0){
					$custom.alert.error("商品数量必须是大于0的数");
		       		flag = false;
		    		return;
				}
				if(!price || price == 0){
					$custom.alert.error("商品单价不能为空或0");
		       		flag = false;
		    		return;
				}
		    	var reg1 = /^\d+(\.\d+)?$/;
		       	if (!reg1.test(price)) {
		       		$custom.alert.error("单价只能输入数字");
		       		flag = false;
		    		return;
		       	}
		       	if(price.length>15){
		       		$custom.alert.error("单价长度只能输入15位（包含小数点）");
		       		flag = false;
		    		return;
		       	}

				var reg = /^\d+$/;
				if(!amount || amount < 0 || isNaN(amount)){
					$custom.alert.error("总金额必须是大于0的数");
		       		flag = false;
		    		return;
				}
		       	if(amount.length>21){
		       		$custom.alert.error("总金额长度只能输入21位（包含小数点）");
		       		flag = false;
		    		return;
		       	}
				itemList.push(item);
			});
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
            json['orderId']=orderId;
            json['orderCode']=orderCode;
			json['customerId']=customerId;
			json['merchantId']=merchantId;
			json['merchantName']=merchantName;
            json['businessModel']=businessModel;
			json['outDepotId']=outDepotId;
			json['buId']=buId;
			json['poNo']=poNo;
			json['currency']=currency;
			json['remark']=remark;
            json['itemList']=itemList;
			if(!!tallyingUnit){
				json['tallyingUnit']=tallyingUnit;
			}
			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("修改成功！");
					setTimeout(function () {
						$load.a(pageUrl+"6001");
					}, 1000);
				}else{
					if(!!result.expMessage){
						$custom.alert.error(result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
					
				}
			});
		});
		//提交并审核存按钮
		$("#save-buttons").click(function(){
            //必填项校验
            if(!$checker.verificationRequired()){
                return ;
            }
			$custom.alert.warning("确定审核该预售单？",function(){
			var url = serverAddr+"/preSale/modifyPreSaleOrder.asyn";
			var form = {
			};
			var json = {};
            var orderId = $("#orderId").val();
            var orderCode = $("#orderCode").val();
			var customerId = $("#customerId").val();
            var merchantId = $("#merchantId").val();
            var merchantName = $("#merchantName").val();
            var businessModel = $("#businessModel").val();
			var outDepotId = $("#outDepotId").val();
            var buId = $("#buId").val();
            var poNo = $("#poNo").val();
			var results	= poNo.split("&");
			var result = results.sort();
			for(var i=0;i<result.length-1;i++){
              if(result[i]==result[i+1]){	// PO号重复了
                  $custom.alert.error("修改失败，PO号重复了");
                  return;
              }
			}
            var currency = $("#currency").val();
            var remark = $("#remark").val();//非必填
			// 出库仓库是海外仓 必填字段校验
			var tallyingUnit='';
			var outDepotType = $("#outDepotId").find("option:selected").attr("type");
			if ('c'==outDepotType) {
                tallyingUnit = $("#tallyingUnit").val();
                if (!tallyingUnit) {
                    $custom.alert.error("修改失败,出库仓库是海外仓，理货单位不能为空");
                    return;
                }
			}
			var itemList = [];
			var flag = true;
			
			var checkInventoryGoods = [];
			var goodsArr = [];
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
                var id = $(this).find('input[name="id"]').val();
                item['id']=id;
                var goodsId = $(this).find('input[name="goodsId"]').val();
                item['goodsId']=goodsId;
                var goodsCode = $(this).find('input[name="goodsCode"]').val();
                item['goodsCode']=goodsCode;
                var goodsNo = $(this).find('input[name="goodsNo"]').val();
                item['goodsNo']=goodsNo;
                var goodsName = $(this).find('input[name="goodsName"]').val();
                item['goodsName']=goodsName;
                var barcode = $(this).find('input[name="barcode"]').val();
                item['barcode']=barcode;
                var num = $(this).find('input[name="num"]').val();
                item['num']=num;
                var price = $(this).find('input[name="price"]').val();
                item['price']=price;
                var amount = $(this).find('input[name="amount"]').val();
                item['amount']=amount;
                var brandName = $(this).find('input[name="brandName"]').val();
                item['brandName']=brandName;
				if(!num || isNaN(num) ||num<=0){
					$custom.alert.error("商品数量必须是大于0的数");
		       		flag = false;
		    		return;
				}
				var reg = /^\d+$/;
				if(!amount || amount < 0 || isNaN(amount)){
					$custom.alert.error("总金额必须是大于0的数");
		       		flag = false;
		    		return;
				}else{
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

		       	if(isExist(goodsArr, goodsId)) {
		       		var newArr = checkInventoryGoods.filter(function(p){
		       		  return p.goodsId === goodsId;
		       		});
		       		newArr[0].num = parseInt(newArr[0].num) +parseInt(num);
		       		checkInventoryGoods.push({"goodsId":newArr[0].goodsId, "goodsNo":newArr[0].goodsNo, "num": newArr[0].num });
		       	} else {
		       		goodsArr.push(goodsId);
		       	 	checkInventoryGoods.push({"goodsId":goodsId, "goodsNo":goodsNo, "num":num});
		       	}
				itemList.push(item);
			});
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
        	if(!flag){
				return false;
			}
            json['orderId']=orderId;
            json['orderCode']=orderCode;
            json['customerId']=customerId;
            json['merchantId']=merchantId;
            json['merchantName']=merchantName;
            json['businessModel']=businessModel;
            json['outDepotId']=outDepotId;
            json['buId']=buId;
            json['poNo']=poNo;
            json['currency']=currency;
            json['remark']=remark;
            json['itemList']=itemList;
            if(!!tallyingUnit){
                json['tallyingUnit']=tallyingUnit;
            }

			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;

			// 不用校验库存余量，校验完必填项直接进行保存并审核
			$custom.request.ajax(url, form, function(data){
				if(data.state==200){
					var str = data.data;
					$custom.alert.success(str);
					//重新加载页面
					setTimeout(function () {
						$load.a(pageUrl+"6001");
					}, 1000);
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});
		});
		//删除选中行
	 	$("#delete-list-button").click(function() {
	 		var goodsId = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            goodsId.push($(this).next().val());
	            $("#table-list").find("tr:eq("+n+")").remove();
	        });
	        var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        var newUnNeedIds = delArrElement(goodsId,temp);
	        $("#unNeedIds").val(newUnNeedIds);
	        $("input[name='all']").prop("checked",false);
	    });
		function delArrElement(goodsId,temp){
			for (var i = 0; i < goodsId.length; i++) {
	        	for (var j = 0; j < temp.length; j++) {
					if(goodsId[i] == temp[j]){
						temp.splice(j,1);
						delArrElement(goodsId,temp);
					}
				}
			}
			return temp;
		}
	 	$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
		//监听预售数量的离开事件
	 $("#table-list").on("blur",'.goods-num',function(){
			var that = $(this);
	    	//获取预售数量
	    	var num = that.val();
	    	//判断是否数字
	    	if(isNaN(num)){
	    		$custom.alert.error("预售数量只能输入正整数");
	    		return ;
	    	}
	    	var tr = that.parent().parent();
             //获取预售单价
             var price = tr.find("input[name='price']").val();
             //设置总金额
             var sum = 0;
             if(!!num && !!price){
                 sum = Number((price*num).toFixed(2));
             }
             tr.find("input[name='amount']").val(sum);
    }); 
    //监听预售总金额的离开事件
	$("#table-list").on("blur",'.goods-amount',function(){
		var that = $(this);
		//获取总金额
    	var sum= that.val();
    	var tr = that.parent().parent();
    	//获取采购数量
    	var num = tr.find("input[name='num']").val();
    	//判断是否数字
    	if(isNaN(sum)){
    		$custom.alert.error("预售总金额请输入数字");
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
    });
	$("#table-list").on("blur",'.goods-price',function(){
		var that = $(this);
		//获取当前价格
    	var price= that.val();
    	//判断是否数字
    	if(isNaN(price)){
    		$custom.alert.error("预售单价请输入数字");
    		return ;
    	}
    	var indexOf = price.indexOf(".");
    	if (indexOf != -1) {
            var count = (parseFloat(price)+"").length - 1 - indexOf;
            if (count > 8) {
            	$(this).val("");
            	tr.find("input[name='amount']").val("");
                $custom.alert.error("预售单价只能为8位小数");
                return;
            }
        }
    	var tr = that.parent().parent();
    	//获取预售数量
    	var num = tr.find("input[name='num']").val();
    	//设置总金额
    	var sum = 0;
    	if(!!num && !!price){
	    	sum = Number((price*num).toFixed(2));
    	}
    	tr.find("input[name='amount']").val(sum);
    	$(this).val(parseFloat(price).toFixed(8)) ;
    });

	// 出库仓改变的时候
    $("#outDepotId").change(function() {
    	var outId = $("#outDepotId").val();
        $custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id":outId}, function(result){
             // 当出仓仓库为海外仓时，理货单位必填，其他场景下隐藏该字段；
             if(result.data.type == 'c'){
                //显示
                $("#tallyingUnitDiv").html('<label class="edit_label"><i class="red">*</i> 理货单位：</label><select name="tallyingUnit" id="tallyingUnit" class="edit_input"><option value="00">托盘</option><option value="01">箱</option><option value="02" selected>件</option></select>');
            }else{
                //隐藏
                $("#tallyingUnitDiv").html('');
            }
        });
    });
 });
 function isExist(goodsArr, goodsId) {
		for(var i=0;i<goodsArr.length;i++){
			  if(goodsArr[i]==goodsId){	// 商品重复了
				  return true;
			  }else{
				  return false;
			  }							
		}	
 }
</script>
