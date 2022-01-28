<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">销售订单出库编辑</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                         <input type='hidden' name='orderId'  id='orderId' value='${detail.id}'>
                            <div class="info_text">
                                <span>销售订单号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出库仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                </span>
                            </div>
                             <div class="info_text">
                                <span>入库仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>销售类型</span>
                               <span>
                                    ${detail.businessModelLabel}
                                </span>
                            </div>
                            <div class="info_text">
                               <span>事业部：</span>
                                <span>
                                    ${detail.buName}
                                </span>
                            </div>
                             <div class="info_text">
                               <span>客户：</span>
                                <span>
                                    ${detail.customerName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>PO号：</span>
                               <span>
                                    ${detail.poNo}
                                </span>
                            </div>
                             <div class="info_text"></div>
                             <div class="info_text"></div>
                        </div>
                        <input type="hidden" name="outDepotId" value="${detail.outDepotId}"/>
                        <input type="hidden" name="outDepotCode" value="${detail.outDepotCode}"/>
                        <input type="hidden" name="outDepotName" value="${detail.outDepotName}"/>
                        <input type="hidden" name="inDepotId" value="${detail.inDepotId}"/>
                        <input type="hidden" name="inDepotCode" value="${detail.inDepotCode}"/>
                        <input type="hidden" name="businessModel" value="${detail.businessModel}"/>
                        <input type="hidden" name="buId" value="${detail.buId}"/>
                        <input type="hidden" name="customerId" value="${detail.customerId}"/>
                    </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-10">
                             <div class="title_text">出库信息</div>
                        </div>
                        </div>
                    </div> 
                    <div class="col-12 ml10">
                        <div class="info_box">
	                        <div class="info_item">
	                            <div class="info_text">
	                                <label ><i class="red">*</i>出库时间： </label>
			                    	<input type="text" class="edit_input form_datetime date-input" name="returnOutDate" id="returnOutDate"  value="" style="width: 120px">
	                            </div>
                                <div class="info_text">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="allOut"  name="allOut">整单出库</button>
                                </div>
	                        </div>
	                    </div>
                    </div>
                    <div class="form-row mt20 ml15 table-responsive">
                   <table id="table-list" class="table table-striped" style="width: 100%;">
                   <thead>
                      <tr>
                         <th ><input id="checkbox11" type="checkbox" name="all"></th>
                         <th>序号</th>
                         <th>商品货号</th>
                         <th>商品名称</th>
                         <th>条形码</th>
                         <th>销售数量</th>
                         <th><i class="red">*</i>好品出库量</th>
                         <th><i class="red">*</i>坏品出库量</th>
                         <th style='text-align:center;'>批次号</th>
                         <th>生产日期</th>
                         <th>失效日期</th>
                      </tr>
                      </thead>
                      <tbody id="tbody">
                      <c:forEach items="${detail.itemList }" var="item">
                    	<tr>
                    		<td>
	                    		<input type='checkbox' name='item-checkbox'>
		                    	<input type='hidden' name='goodsId' value='${item.goodsId}'>
		                    	<input type='hidden' name='goodsCode' value='${item.goodsCode}'>
	                    	</td>
                            <td>${item.seq}<input type="hidden" name="seq" value="${item.seq}"/></td>
                            <td>${item.goodsNo}<input type="hidden" name="goodsNo" value="${item.goodsNo}"/></td>
                            <td>${item.goodsName}<input type="hidden" name="goodsName" value="${item.goodsName}"/></td>
                            <td>${item.barcode}<input type="hidden" name="barcode" value="${item.barcode}"/></td>
                            <td>${item.num}<input type="hidden" name='num' value="${item.num}"></td>
                            <td style='text-align:center;'><input type="text" name='transferNum' style='width:50px;text-align:center;' class='transferNum' value="0"<c:if test="${item.num == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='wornNum' style='width:50px;text-align:center;' class='wornNum' value="0"<c:if test="${item.num == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='transferBatchNo' style='width:100px;text-align:center;' class='transferBatchNo-num' value=""<c:if test="${item.num == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='productionDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${item.num == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='overdueDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${item.num == 0}">disabled</c:if>></td>
                        </tr>
                      </c:forEach>
                      </tbody>
                       <tr>
                           <td colspan="5" style="text-align:center">汇总</td>
                           <td><span id="totalNum" ></span></td>
                           <td style='text-align:center;'><span id="totalTransferNum"></span></td>
                           <td style='text-align:center;'><span id="totalWornNum"></span></td>
                           <td></td>
                           <td></td>
                           <td></td>
                       </tr>
                  </table>                              
                   </div>

                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
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
                  <!-- 弹窗结束 -->
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>


</div> <!-- content -->
<script type="text/javascript">
$(document).ready(function() {
    $(function (){
        var totalNum=0;
        $('#tbody tr').each(function (i) {
            var ch = $(this).find('input[name="num"]').val();
            totalNum = parseFloat(totalNum) + parseFloat(ch);
        })
        $("#totalNum").html(totalNum);
    });

    $(function(){

			//取消按钮
		$("#cancel-buttons").click(function(){
            var pageSource = '${pageSource}';
            if (pageSource == "1") {
                $load.a("/list/menu.asyn?act=100&r="+Math.random());
            } else {
                $module.load.pageOrder('act=4001');
            }
        });
    });
	

		//保存按钮
		$("#save-buttons").click(function(){
			var url = serverAddr+"/sale/saveSaleOut.asyn";
			var form = {};
			var json = {};
			var itemList = [];
			var flag = true;
			var isExistsShelfNum=0;// 0 表示本次出库好品数量和本次出库坏品数量都为0  , 1表示存在一个大于0的
            var outDepotId = $('input[name="outDepotId"]').val();//出仓仓库id
            var outDepotCode = $('input[name="outDepotCode"]').val();//出仓仓库code
            var outDepotName = $('input[name="outDepotName"]').val();//出仓仓库

            $('#tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var orderId = $(this).find('input[name="orderId"]').val();
				var goodsId = $(this).find('input[name="goodsId"]').val();
				var goodsCode = $(this).find('input[name="goodsCode"]').val();
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				var goodsName = $(this).find('input[name="goodsName"]').val();
				var barcode = $(this).find('input[name="barcode"]').val();
                var num = parseFloat($(this).find('input[name="num"]').val());	// 销售数量
				var transferNumStr = $(this).find('input[name="transferNum"]').val();	// 好品量str
				var wornNumStr = $(this).find('input[name="wornNum"]').val();	// 坏品量str
				var transferBatchNo = $(this).find('input[name="transferBatchNo"]').val();	// 批次号
				var productionDate = $(this).find('input[name="productionDate"]').val();	// 生产日期
				var overdueDate = $(this).find('input[name="overdueDate"]').val();	// 失效日期

                var transferNum = parseFloat($(this).find('input[name="transferNum"]').val());	// 好品量
                var wornNum = parseFloat($(this).find('input[name="wornNum"]').val());	// 坏品量

                // 好品量、坏品量必须有一个要大于0
				if (transferNum > 0 || wornNum > 0) {
					isExistsShelfNum=1;
				}
				if((transferNum+wornNum) > num){
                    $custom.alert.warningText("商品货号：" + goodsNo + "，出库数量不能大于销售数量");
                    flag = false;
                    return;
                }
                // 销售订单出库时，校验出库仓若为“中转仓”，则仅校验必填项，不校验库存可用量，不做库存冻结；
                // 查询仓库信息
                var mergeJson = {} ;
                $custom.request.ajaxSync("/depot/getDepotDetails.asyn", {"id": outDepotId}, function (result) {
                    if (result.data.type != 'd') {
                        //合并相同项
                        $.each(checkInventoryGoods, function (index, value) {
                            var originalGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(value.goodsId, value.goodsNo) ;
                            if (originalGoodsId) {
                                if (!mergeJson[originalGoodsId]) {
                                    mergeJson[originalGoodsId] = value;
                                } else {
                                    var num = mergeJson[originalGoodsId].num;
                                    value.num = parseFloat(num) + parseFloat(value.num);
                                    mergeJson[originalGoodsId] = value;
                                }
                            } else {
                                mergeJson[value.goodsId] = value;
                            }
                        });
                        //查询可用量
                        $.each(mergeJson, function (index, value) {
                            var jsonDepot = $module.depot.getDepotById(outDepotId);
                            var tallyingUnitVal = '';
                            if (jsonDepot.type=="c") {
                                if ('00' == tallyingUnit) {
                                    tallyingUnitVal = '0';
                                } else if ('01' == tallyingUnit) {
                                    tallyingUnitVal = '1';
                                } else if ('02' == tallyingUnit) {
                                    tallyingUnitVal = '2';
                                }
                            }
                            var availableNum =$module.inventory.queryAvailability(outDepotId,index,jsonDepot.code,tallyingUnitVal,null,null,transferBatchNo);
                            var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(index, value.goodsNo) ;

                            if (availableNum == -1) {
                                flag = false;
                                if(tempGoodsNo){
                                    $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",未查到库存可用量");
                                }else{
                                    $custom.alert.warningText("未查到库存余量货号：" + value.goodsNo + ",仓库:" + jsonDepot.name);
                                }
                                return;
                            } else if (value.num > availableNum) {
                                flag = false;
                                if(tempGoodsNo){
                                    $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo + " 商品货号："+value.goodsNo+",余量："+availableNum);
                                }else{
                                    $custom.alert.warningText("库存不足,货号：" + value.goodsNo + ",仓库:" + jsonDepot.name + ",余量：" + availableNum);
                                }

                                return;
                            }
                        });
                    }
                });
                if (!flag) {
                    return false;
                }

				item['goodsId']=goodsId;
				item['goodsCode']=goodsCode;
				item['goodsNo']=goodsNo;
				item['goodsName']=goodsName;
				item['barcode']=barcode;
				item['num']=num;
				item['transferNum']=transferNumStr;
				item['wornNum']=wornNumStr;
				item['transferBatchNo']=transferBatchNo;
				item['productionDate']=productionDate;
                item['overdueDate']=overdueDate;
				itemList.push(item);
			});
			if(!flag){
	    		return;
			}
            // 出库好品量、出库坏品量必须有一个要大于0
            if (isExistsShelfNum == 0) {
                $custom.alert.error("出库好品量、出库坏品量必须有一个要大于0");
                return;
            }
			var orderId = $("#orderId").val();
			var returnOutDate =$('#returnOutDate').val();	// 出库时间
			if(!returnOutDate){
				$custom.alert.error("出库时间不能为空");
				flag = false;
	    		return;
			}
			json['orderId'] = orderId;
			json['returnOutDate'] = returnOutDate;
			json['itemList']=itemList;
			var jsonStr= JSON.stringify(json);
			form['json']=jsonStr;
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("出库成功！");
					setTimeout(function () {
						$load.a(pageUrl+"4001");
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
		// 点击全选
	 	$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
	
	$(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2
    });

    $("input[name='transferNum']").blur(function(){
        var that = $(this);
        var transferNum = that.val();
        //判断是否数字
        if(isNaN(transferNum)){
            $custom.alert.error("好品数量请输入数字");
            return ;
        }
        if(transferNum == "" || transferNum == undefined){
            $custom.alert.error("好品数量不能为空");
            that.val(0);
            return ;
        }
        var total=0;
        $('#tbody tr').each(function (i) {
            var ch = $(this).find('input[name="transferNum"]').val();
            total = parseFloat(total) + parseFloat(ch);
        })
        $("#totalTransferNum").html(total);
    });

    $("input[name='wornNum']").blur(function(){
        var that = $(this);
        var wornNum = that.val();
        //判断是否数字
        if(isNaN(wornNum)){
            $custom.alert.error("坏品数量请输入数字");
            that.val(0);
            return ;
        }
        if(wornNum == "" || wornNum == undefined){
            $custom.alert.error("坏品数量不能为空");
            return ;
        }
        var total=0;
        $('#tbody tr').each(function (i) {
            var ch = $(this).find('input[name="wornNum"]').val();
            total = parseFloat(total) + parseFloat(ch);
        })
        $("#totalWornNum").html(total);
    });

    $("#allOut").click(function(){
        var totalNum = 0;
        $('#table-list #tbody tr').each(function(i){
            $(this).find('input[name="transferNum"]').val($(this).find('input[name="num"]').val());
            totalNum += parseFloat($(this).find('input[name="num"]').val());
        });
        $("#totalTransferNum").html(parseFloat(totalNum));
    });
	
});

</script>
