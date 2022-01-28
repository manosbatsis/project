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
                       <li class="breadcrumb-item"><a href="#">新增采购退货订单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <form id="infoForm">
                    <div class="edit_box mt20">
                    	<input type="hidden" value="${detail.purchaseOrderRelCode}" name="purchaseOrderRelCode" id="purchaseOrderRelCode"/>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 公  司：</label>
                            <input type="hidden" value="${merchantId}" name="merchantId" id="merchantId"/>
                            <input type="text" class="edit_input" value="${merchantName}" name = "merchantName" id = "merchantName" readonly>
                        </div> 
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 供应商：</label>
                            <input type="hidden" value="${detail.supplierId}" name="supplierId" id="supplierId"/>
                            <input type="text" class="edit_input" value="${detail.supplierName}" name="supplierName" id="supplierName" readonly/>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i> 出仓仓库：</label>
                            <input type="hidden" value="${detail.outDepotId}" name="depotId" id="depotId"/>
                            <select class="edit_input" name="outDepotId" id="outDepotId" disabled>
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                            <label class="edit_label"><i class="red depot_type_a" >*</i>是否同关区：</label>
                            <select class="edit_input" name="isSameArea" id="isSameArea" required reqMsg = "是否同关区不能为空">
                            </select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red is_same_area" >*</i> 入仓仓库：</label>
                            <select class="edit_input" name="inDepotId" id="inDepotId" parsley-trigger="change" required reqMsg = "入仓仓库不能为空">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <div class="edit_item">
                     	 	<label class="edit_label"><i class="red">*</i> 事业部：</label>
                            <input type="hidden" value="${detail.buId}" name="buId" id="buId"/>
                            <input type="text" class="edit_input" value="${detail.buName}" name="buName" id="buName" readonly/>
                         </div>
                    </div>
                    <div class="edit_box">
                        <div class="edit_item">
                             <label class="edit_label"><i class="red">*</i> PO单号：</label>
                             <input type="text" required reqMsg = "PO单号不能为空" parsley-type="text" class="edit_input" id="poNo" name="poNo">
                         </div>
                         <div class="edit_item">
	                            <label class="edit_label"><i class="red" id="currency_i">*</i>采购币种：</label>
	                             <select class="edit_input" name="currency" id="currency" parsley-trigger="change" required reqMsg = "采购币种不能为空">
                                	<option value="">请选择</option>
                            	</select>
                        	</div>
                    	<div class="edit_item">
                            <label class="edit_label"><i class="red is_same_area">*</i> 合 同 号：</label>
                            <input type="text" class="edit_input" name="contractNo" id="contractNo" required reqMsg = "合同号不能为空">
                        </div>
                    </div>
                    <div class="edit_box">
		                     <div class="edit_item">
	                        	<label class="edit_label"><i class="red depot_type_c" >*</i>目的地：</label>
	                            <select class="edit_input" name="destinationName" id="destinationName" parsley-trigger="change" required reqMsg = "目的地不能为空">
	                              	<option value="">请选择</option>
	                          	</select>
	                        </div>
	                        <div class="edit_item">
	                            <label class="edit_label">备注：</label>
	                            <input type="text"  parsley-type="text" class="edit_input" name="remark" value="${detail.remark }">
	                        </div>
	                        <div class="edit_item" >
		                        	<label class="edit_label" ><i class="red depot_type_c" id="tallyingUnit_i">*</i> 理货单位：</label>
		                        	 <select class="edit_input" name="tallyingUnit" id="tallyingUnit" parsley-trigger="change" required reqMsg = "理货单位不能为空">
                                	<option value="">请选择</option>
                            		</select>
	                        </div>    
                        </div>
                        <div class="edit_box">
		                     <div class="edit_item">
	                        	<label class="edit_label">目的地址：</label>
	                            <input type="text"  parsley-type="text" class="edit_input" name="destinationAddress" value="${detail.destinationAddress }">
	                        </div>
	                        <div class="edit_item">
	                        </div>
	                        <div class="edit_item" >
	                        </div>    
                        </div>
                        
                        <div class = "depot_type_c_div">
                        <div class="title_text">收件信息</div>
                        <div class="edit_box mt20">
                        	<div class="edit_item" >
		                        	<label class="edit_label" ><i class="red depot_type_c">*</i> 提货方式：</label>
		                        	 <select class="edit_input" name="deliveryMethod" id="deliveryMethod" parsley-trigger="change" required reqMsg = "提货方式不能为空">
                                	<option value="">请选择</option>
                            		</select>
	                        </div>    
		                     <div class="edit_item">
	                        	<label class="edit_label"><i class="red depot_type_c">*</i> 收货人名称 ：</label>
	                            <input type="text" required reqMsg = "收货人名称不能为空" parsley-type="text" class="edit_input" id="deliveryName" name="deliveryName" value="">
	                        </div>
	                        <div class="edit_item">
	                        </div>
                        </div>
                        <div class="edit_box">
		                     <div class="edit_item">
	                        	<label class="edit_label"><i class="red depot_type_c">*</i> 国家：</label>
	                            <input type="text" required reqMsg = "国家不能为空" parsley-type="text" class="edit_input" id="country" name="country" value="">
	                        </div>
	                        <div class="edit_item">
	                            <label class="edit_label"><i class="red depot_type_c">*</i> 详细地址：</label>
	                            <input type="text" required reqMsg = "详细地址不能为空" parsley-type="text" class="edit_input" id="deliveryAddr" name="deliveryAddr" value="">
	                        </div>
	                        <div class="edit_item">
	                        </div>
                        </div>
                        </div>
                        </form>
                   </div>  	
                 <!--  title   基本信息  start -->
                     <!--   商品信息  start -->
                                <div class="title_text">采购退货商品明细</div>
                                <div class="row col-12 mt20 table-responsive">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                           width="100%">
                                        <thead>
                                        <tr>
                                            <th>操作</th> 
                                            <th><i class="red">*</i> &nbsp;&nbsp;&nbsp;商品货号&nbsp;&nbsp;&nbsp;</th>
                                            <th>商品名称</th>
                                            <th>商品条形码</th>
                                            <th><i class="red">*</i>退货商品数量</th>
                                            <th><i class="red">*</i>退货单价</th>
                                            <th><i class="red" id="declarePriceI">*</i>申报单价</th>
                                            <th><i class="red">*</i>退货总金额</th>
                                            <th>品牌类型</th>
                                            <th>&nbsp;&nbsp;&nbsp;箱&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                            <th>&nbsp;&nbsp;合&nbsp;同&nbsp;号&nbsp;&nbsp;&nbsp;</th>
                                            <th>&nbsp;&nbsp;&nbsp;备&nbsp;注&nbsp;&nbsp;&nbsp;</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${detail.itemList}"  var="item">
                                         <tr>
                                            <td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i
                                                    class="fi-minus" onclick="deltr(this);"></i>
	                    						<input type='hidden' name='goodsId' value='${item.goodsId}'>   
	                    					</td>   
                                            <td>
                                            	<input type='hidden' name='goodsNo' value='${item.goodsNo}'>       
                                            	${item.goodsNo}
                                                <button type="button"
                                                        class="btn btn-find waves-effect waves-light btn_default indepot_goods"
                                                        onclick="initGoodsBefore(this);">选择商品
                                                </button>
                                            </td>
                                            <td><input type="hidden"  class="form-control" name="goodsName" value="${item.goodsName}">
                                            	${item.goodsName}
                                            </td>
                                            <td><input type="hidden"  class="form-control" name="barcode" value="${item.barcode}">
                                            	${item.barcode}
                                            </td>
                                          <td>
                                          		<input type="text" class="form-control" name="returnNum" onkeyup="value=value.replace(/[^\d]/g,'')" >
                                          	</td>
                                            <td>
                                            	<input type="text" class="form-control" name="returnPrice" onkeyup="value=value.replace(/[^\d^\.]/g,'')"  >
                                            	</td>
                                           	<td>
                                           		<input type="text" class="form-control" name="declarePrice" onkeyup="value=value.replace(/[^\d^\.]/g,'')"  >
                                           	</td>
                                           <td>
                                           	<input type="text" class="form-control" name="returnAmount" onkeyup="value=value.replace(/[^\d^\.]/g,'')" >
                                            </td>
                                             <td>
                                             	<input type="text" class="form-control" name="brandName" value="${item.brandName}"/>
                                             	</td>
                                             	<td>
                                              	<input type="text" class="form-control" name="boxNo">
                                              	</td>
                                              <td>
                                              	<input type="text" class="form-control" name="contractNo">
                                              	</td>                     
                                              <td>
                                              	<input type="text" class="form-control" name="remark">
                                              	</td>                     
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
                                      <input type="button" class="btn btn-info waves-effect waves-light btn_default delayedBtn" id="save-buttons" value="保 存"/>
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                      <shiro:hasPermission name="purchase_return_audit">
                                      	<input type="button" class="btn btn-info waves-effect waves-light btn_default delayedBtn" id="audit-buttons" value="保 存并审核"/>
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
			       <jsp:include page="/WEB-INF/views/modals/4018-V2.jsp"/>
                  <!-- 弹窗结束 -->
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
	
	var outDepotType = "${depotType}";
	var isInOutInstruction = false ;
	
 $(document).ready(function() {
	 
	 initDepotTypeC(outDepotType) ;
	 
	 changeDeclareNecc($("#depotId").val()) ;

   //根据出库库仓库加载
     $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0], '${detail.outDepotId}', null);
   //根据入库库仓库加载
     $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0], '${detail.inDepotId}', null);
	// 是否同关区
	$module.constant.getConstantListByNameURL.call($('select[name="isSameArea"]')[0],"isSameAreaList",null);
	//加载销售币种的下拉数据
	$module.currency.loadSelectData.call($("select[name='currency']")[0],"");
	// 海外理货单位
	$module.constant.getConstantListByNameURL.call($('select[name="tallyingUnit"]')[0],"order_tallyingUnitList",null);
	
	$module.constant.getConstantListByNameURL.call($('select[name="destinationName"]')[0],"purchaseReturnOrder_destinationList",null);
	
	$module.constant.getConstantListByNameURL.call($('select[name="deliveryMethod"]')[0],"purchaseReturnOrder_mailModeList","1");

	
    //根据供应商选择带出对应的币种
    $("#supplierId").change(function(){
        var val = $(this).val() ;
        if(val){
            $module.currency.loadSelectData.call($("select[name='currency']")[0],val);
        }
    }) ;
    
    $("#outDepotId").change(function(){
    	var depotId = $(this).val() ;
    	changeDeclareNecc(depotId) ;
    }) ;

    $(function(){
        //取消按钮
        $("#cancel-buttons").click(function(){
              $module.load.pageOrder('act=3008');
        });
    });
    
  //提交按钮
	$("#save-buttons").click(function(){
			var url = serverAddr+"/purchaseReturn/savePurchaseReturnOrder.asyn";
			var form = $("#infoForm").serializeArray() ;
			form.push({"name":"outDepotId","value":$("#outDepotId").val()}) ;
			
			var itemList = [];
			var flag = true;
			/**-开始-**/
	    	//商品 
	        var goodsIds = [] ;
			var goodsNos = [] ;
			var goodsNames = [] ;
			var barcodes = [] ;
			var returnNums = [] ;
			var returnPrices = [] ;
			var declarePrices = [] ;
			var returnAmounts = [] ;
			var brandNames = [] ;
			var boxNos = [] ;
			var contactNos = [] ;
			var remarks = [] ;
				
			
	        $("#datatable-buttons tbody tr").each(function (index, item) {
	            var goodsId = $(this).find("td").eq(0).find("input").val();//商品id
	            var goodsNo = $(this).find("td").eq(1).find("input").val();//商品货号
	            var goodsName = $(this).find("td").eq(2).find("input").val();//退入商品名称
	            var barcode = $(this).find("td").eq(3).find("input").val();//退入商品条形码
	            var returnNum = $(this).find("td").eq(4).find("input").val();//
	            var returnPrice = $(this).find("td").eq(5).find("input").val();//退货商品单价
	            var declarePrice = $(this).find("td").eq(6).find("input").val();//申报单价
	            var returnAmount = $(this).find("td").eq(7).find("input").val();//
	            var brandName = $(this).find("td").eq(8).find("input").val();//品牌类型
	            var boxNo = $(this).find("td").eq(9).find("input").val();//箱号
	            var contractNo = $(this).find("td").eq(10).find("input").val();//
	            var remark = $(this).find("td").eq(11).find("input").val();//箱号
	            
	            if(!returnNum){
	            	$custom.alert.error("退货商品数量必填");
	            	flag = false ;
					return false;
	            }
	            if(!returnPrice){
	            	$custom.alert.error("退货单价必填");
	            	flag = false ;
					return false;
	            }
	            if(!returnAmount){
	            	$custom.alert.error("退货总金额必填");
	            	flag = false ;
					return false;
	            }
	            
	            if(isInOutInstruction && !declarePrice){
	            	$custom.alert.error("出仓仓库进出接口指令为是,申报单价必填");
	            	flag = false ;
					return false;
	            }
	            
	            goodsIds.push(goodsId);
	            goodsNames.push(goodsName);
	            goodsNos.push(goodsNo);
	            barcodes.push(barcode);
	            returnPrices.push(returnPrice);
	            declarePrices.push(declarePrice);
	            returnNums.push(returnNum);
	            returnAmounts.push(returnAmount);
	            brandNames.push(brandName);
	            boxNos.push(boxNo);
	            contactNos.push(contractNo);
	            remarks.push(remark);
	        });
			/**-结束-**/
	    	if(!flag){
				return;
			}
			
        	if(goodsIds.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
        	
        	form.push({"name":"goodsIds","value":goodsIds.join(",")});
        	form.push({"name":"goodsNames","value":goodsNames.join(",")});
        	form.push({"name":"goodsNos","value":goodsNos.join(",")});
        	form.push({"name":"barcodes","value":barcodes.join(",")});
        	form.push({"name":"returnPrices","value":returnPrices.join(",")});
        	form.push({"name":"declarePrices","value":declarePrices.join(",")});
        	form.push({"name":"returnNums","value":returnNums.join(",")});
        	form.push({"name":"returnAmounts","value":returnAmounts.join(",")});
        	form.push({"name":"brandNames","value":brandNames.join(",")});
        	form.push({"name":"boxNos","value":boxNos.join(",")});
        	form.push({"name":"contactNos","value":contactNos.join(",")});
        	form.push({"name":"remarks","value":remarks.join(",")});

			// 接下来进行保存
			$custom.request.ajax(url, form, function(data){
				if(data.state==200){
					$custom.alert.success("操作成功");
					//重新加载页面
					$module.load.pageOrder('act=3008');
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});

		//提交按钮
		$("#audit-buttons").click(function(){
				var url = serverAddr+"/purchaseReturn/saveRequirePurchaseReturnOrder.asyn";
				var form = $("#infoForm").serializeArray() ;
				
				//必填项校验
            	if(!$checker.verificationRequired()){
            		return ;
            	}
				
				var outDepotId = $("#outDepotId").val() ;
				var tallyingUnit = $("#tallyingUnit").val() ;
				
				form.push({"name":"outDepotId","value":outDepotId}) ;
				
				var itemList = [];
				var flag = true;
				/**-开始-**/
				
				var goodsIds = [] ;
				var goodsNos = [] ;
				var goodsNames = [] ;
				var barcodes = [] ;
				var returnNums = [] ;
				var returnPrices = [] ;
				var declarePrices = [] ;
				var returnAmounts = [] ;
				var brandNames = [] ;
				var boxNos = [] ;
				var contactNos = [] ;
				var remarks = [] ;
				
				//数量汇总集合
				var numData = {} ;
	        $("#datatable-buttons tbody tr").each(function (index, item) {
	            var goodsId = $(this).find("td").eq(0).find("input").val();//商品id
	            var goodsNo = $(this).find("td").eq(1).find("input").val();//商品货号
	            var goodsName = $(this).find("td").eq(2).find("input").val();//退入商品名称
	            var barcode = $(this).find("td").eq(3).find("input").val();//退入商品条形码
	            var returnNum = $(this).find("td").eq(4).find("input").val();//
	            var returnPrice = $(this).find("td").eq(5).find("input").val();//退货商品单价
	            var declarePrice = $(this).find("td").eq(6).find("input").val();//退货申报单价
	            var returnAmount = $(this).find("td").eq(7).find("input").val();//
	            var brandName = $(this).find("td").eq(8).find("input").val();//品牌类型
	            var boxNo = $(this).find("td").eq(9).find("input").val();//箱号
	            var contractNo = $(this).find("td").eq(10).find("input").val();//
	            var remark = $(this).find("td").eq(11).find("input").val();//箱号
	            
	            if(!returnNum){
	            	$custom.alert.error("退货商品数量必填");
	            	flag = false ;
					return false;
	            }
	            if(!returnPrice){
	            	$custom.alert.error("退货单价必填");
	            	flag = false ;
					return false;
	            }
	            if(!returnAmount){
	            	$custom.alert.error("退货总金额必填");
	            	flag = false ;
					return false;
	            }
	            
	            if(isInOutInstruction && !declarePrice){
	            	$custom.alert.error("出仓仓库进出接口指令为是,申报单价必填");
	            	flag = false ;
					return false;
	            }
	            
	            if (outDepotType == 'c') {
					if ('00'==tallyingUnit) {
						tallyingUnit='0';
					}else if ('01'==tallyingUnit) {
						tallyingUnit='1';
					}else if ('02'==tallyingUnit) {
						tallyingUnit='2';
					}
				}
	            
	            //查询是否存在原货号ID，若有返回，若无返回传入goodsId
	            var tempGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(goodsId, goodsNo) ;
	            if(!tempGoodsId){
	            	tempGoodsId = goodsId ;
	            }
	            
	            if(!numData[tempGoodsId]){
	            	numData[tempGoodsId] = {"returnNum" : returnNum , "goodsNo" : goodsNo} ;
	            }else{
	            	var tempJson = numData[tempGoodsId] ;
	            	
	            	var tempNum = tempJson['returnNum'] ;
	            	
	            	tempNum = parseInt(tempNum) + parseInt(returnNum) ;
	            	
	            	tempJson['returnNum'] = tempNum ;
	            	tempJson['goodsNo'] = goodsNo ;
	            	
	            	numData[tempGoodsId] = tempJson ;
	            }
	            
	          
	            goodsIds.push(goodsId);
	            goodsNames.push(goodsName);
	            goodsNos.push(goodsNo);
	            barcodes.push(barcode);
	            returnPrices.push(returnPrice);
	            declarePrices.push(declarePrice);
	            returnNums.push(returnNum);
	            returnAmounts.push(returnAmount);
	            brandNames.push(brandName);
	            boxNos.push(boxNo);
	            contactNos.push(contractNo);
	            remarks.push(remark);
	        });
				/**-结束-**/
				
				for(var goodsId in numData){
					
					var tempJson = numData[goodsId] ;
					var returnNum = tempJson['returnNum'] ;
					var goodsNo = tempJson['goodsNo']
					
					//查询可用量
	        	  	var availableNum = null ;
					
					if(outDepotType == 'c'){
						availableNum = $module.inventory.queryAvailability(outDepotId,goodsId,null,tallyingUnit,null,null,null);
					}else{
						availableNum = $module.inventory.queryAvailability(outDepotId,goodsId);
					}
			       	
					var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(goodsId, goodsNo) ;
					
	        	  //判断库存量是否足够
	        	  if(!availableNum || availableNum ==-1){
	        		  flag = false;
	        		  
	        		  if(tempGoodsNo){
	        			  $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+goodsNo+",未查到库存可用量");
	        		  }else{
		        		  $custom.alert.warningText("商品货号："+goodsNo+",未查到库存可用量");
	        		  }
	        		  
				      return false ;
	        	  }else if(returnNum>availableNum){
	        		  flag = false;
							        		  
	        		  if(tempGoodsNo){
						  $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo +" 商品货号："+goodsNo+",可用量："+availableNum);
	        		  }else{
						  $custom.alert.warningText("库存不足，商品货号："+goodsNo+",可用量："+availableNum);
	        		  }
					  return false ;
				  }
				}
				
		    	if(!flag){
					return;
				}
				
	        	if(goodsIds.length==0){
					$custom.alert.error("至少选择一个商品");
					return;
				}
	        	
	        	form.push({"name":"goodsIds","value":goodsIds.join(",")});
	        	form.push({"name":"goodsNames","value":goodsNames.join(",")});
	        	form.push({"name":"goodsNos","value":goodsNos.join(",")});
	        	form.push({"name":"barcodes","value":barcodes.join(",")});
	        	form.push({"name":"returnPrices","value":returnPrices.join(",")});
	        	form.push({"name":"declarePrices","value":declarePrices.join(",")});
	        	form.push({"name":"returnNums","value":returnNums.join(",")});
	        	form.push({"name":"returnAmounts","value":returnAmounts.join(",")});
	        	form.push({"name":"brandNames","value":brandNames.join(",")});
	        	form.push({"name":"boxNos","value":boxNos.join(",")});
	        	form.push({"name":"contactNos","value":contactNos.join(",")});
	        	form.push({"name":"remarks","value":remarks.join(",")});
	
				// 接下来进行保存
				$custom.request.ajax(url, form, function(data){
					if(data.state==200){
						$custom.alert.success("操作成功");
						//重新加载页面
						$module.load.pageOrder('act=3008');
					}else{
						if(!!data.expMessage){
							$custom.alert.error(data.expMessage);
						}else{
							$custom.alert.error(data.message);
						}
					}
				});
			});
	
		 	$("input[name='all']").click(function(){
				//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
				if($(this).is(":checked")){
					$("input[name='item-checkbox']").prop("checked",true);
				}else{
					$("input[name='item-checkbox']").prop("checked",false);
				}
			});
		 	
		//同关区选择
		 $("#isSameArea").change(function() {
			 var isSameArea = $(this).val() ;
			 
			 if(isSameArea == '0'){
				 $(".is_same_area").hide() ;
				 removeAttrByClazz("is_same_area") ;
			 }else if(isSameArea == '1'){
				 $(".is_same_area").show() ;
				 addAttrByClazz("is_same_area") ;
			 }
			 
		 });
		
		//提货方式
		 $("#deliveryMethod").change(function() {
			 var deliveryMethod = $(this).val() ;
			 
			 if(deliveryMethod == '1'){
				 $("#deliveryName").val("");
				 $("#country").val("");
				 $("#deliveryAddr").val("");
			 }else if(deliveryMethod == '2'){
				 $("#deliveryName").val("卓志香港仓");
				 $("#country").val("中国香港");
				 $("#deliveryAddr").val("香港 新界 元朗 流浮山路DD129");
			 }
			 
		 });
	
	 });
 
	 //添加行
	 function addtr() {
	     $("#datatable-buttons").append('<tr>' +
	         '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
	         '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default indepot_goods"onclick="initGoodsBefore(this);">选择商品</button></td>'+ 
	         '<td></td>' +
	         '<td></td>' +
	         '<td><input type="text" class="form-control" name="returnNum"onkeyup="value=value.replace(/[^\\d.]/g,\'\')" ></td>' +
	         '<td><input type="text" class="form-control" name="returnPrice" onkeyup="value=value.replace(/[^\\\d^\\\.]/g,\'\')"   ></td>'+
	         '<td><input type="text" class="form-control" name="declarePrice" onkeyup="value=value.replace(/[^\\\d^\\\.]/g,\'\')"   ></td>'+
	         '<td><input type="text" class="form-control" name="returnAmount" onkeyup="value=value.replace(/[^\\\d^\\\.]/g,\'\')"   value="0"></td>' +
	         '<td><input type="text"  class="form-control" name="brandName"></td>' +
	         '<td><input type="text" class="form-control" name="boxNo"></td> ' +
	         '<td><input type="text" class="form-control" name="contractNo"/></td>' +
	         '<td><input type="text" class="form-control" name="remark"/></td>' +
	         '</tr>');
	 }
	 //删除行
	 function deltr(obj) {
	     var rows = $(obj).parent("td").parent("tr").parent("tbody").find("tr").length;
	     
	     var goodId = $(obj).parent("td").parent("tr").find("input[name='goodsId']").val() ;
	     
	     var str = $("#unNeedIds").val();
         var unNeedIds = str.split(",");
         
         if(unNeedIds){
	         var tempStr = "" ;
        	 for(var i = 0 ; i < unNeedIds.length ; i ++){
        		 
        		 if(unNeedIds[i] != goodId){
        			 if(tempStr == ""){
        				 tempStr = goodId ;
        			 }else{
        				 tempStr += "," + goodId ;
        			 }
        		 }
        		 
        	 }
        	 
        	 $("#unNeedIds").val(tempStr);
        }
	     
	     if (rows > 1) {
	         $(obj).parent("td").parent("tr").remove();
	     } else {
	         $(obj).parent("td").parent("tr").replaceWith('<tr>' +
	                 '<td class="tc nowrap"><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>' +
	                 '<td><button type="button"class="btn btn-find waves-effect waves-light btn_default indepot_goods"onclick="initGoodsBefore(this);">选择商品</button></td>'+ 
	                 '<td></td>' +
	                 '<td></td>' +
	                 '<td><input type="text" class="form-control" name="returnNum"onkeyup="value=value.replace(/[^\\d.]/g,\'\')" ></td>' +
	                 '<td><input type="text" class="form-control" name="returnPrice" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"   ></td>'+
	                 '<td><input type="text" class="form-control" name="declarePrice" onkeyup="value=value.replace(/[^\\d.]/g,\'\')"   ></td>'+
	                 '<td><input type="text" class="form-control" name="returnAmount"  onkeyup="value=value.replace(/[^\\d.]/g,\'\')"  value="0"></td>' +
	                 '<td><input type="text"  class="form-control" name="brandName"></td>' +
	                 '<td><input type="text" class="form-control" name="boxNo"></td> ' +
	                 '<td><input type="text" class="form-control" name="contractNo"/></td>' +
	                 '<td><input type="text" class="form-control" name="remark"/></td>' +
	                 '</tr>');
	     }
	 }

	 function initDepotTypeC(outDepotType){
		 
		 $(".depot_type_c").hide() ;
		 $(".depot_type_c_div").hide() ;
		 $(".is_same_area").hide() ;
		 
		 removeAttrByClazz("depot_type_c") ;
		 removeAttrByClazz("is_same_area") ;
		 
		 if("d" == outDepotType){
			 $(".depot_type_a").hide() ;
			 removeAttrByClazz("depot_type_a") ;
		 }else if("c" == outDepotType){
			 $(".depot_type_c").show() ;
			 $(".depot_type_c_div").show() ;
			 addAttrByClazz("depot_type_c") ;
		 }
	 }
	 
	 function initGoodsBefore(obj){
		 
		 thisObj = obj ;
		 $erpPopup.orderGoods.init('1');
	 }
	 
	 //监听采购价格的离开事件
	    $("#datatable-buttons").on("blur",'input[name="returnPrice"]',function(){
	    	var that = $(this);
	    	//获取当前价格
	    	var price = that.val();
	    	//判断是否数字
	    	if(!price || isNaN(price)){
	    		return ;
	    	}
	    	price = parseFloat(price) ;
	    	price = price.toFixed(8) ; 
	    	that.val(price) ; 
	    	//获取采购数量
	    	var num = that.parent().prev().find("input").val();
	    	
	    	if(!num || isNaN(num)){
	    		return ;
	    	}
	    	
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().next().find('input').val(sum.toFixed(2));
	    }); 
		
	    //监听数量的离开事件
	    $("#datatable-buttons").on("blur",'input[name="returnNum"]',function(){
	    	var that = $(this); 
	    	//获取当前数量
	    	var num = that.val();
	    	
	    	if(!num || isNaN(num)){
	    		return ;
	    	}
	    	
	    	//获取价格
	    	var price = that.parent().next().find("input").val();
	    	
	    	if(!price || isNaN(price)){
	    		return ;
	    	}
	    	
	    	//设置总金额
	    	var sum = price*num;
	    	that.parent().next().next().next().find('input').val(sum.toFixed(2));
	    	//设置总毛重、总净重
	    });
	    
	  //监听金额的离开事件
	    $("#datatable-buttons").on("blur",'input[name="returnAmount"]',function(){
	    	var that = $(this); 
	    	//获取当前金额
	    	var sum = that.val();
	    	
	    	if(!sum || isNaN(sum)){
	    		return ;
	    	}
	    	
	    	sum = parseFloat(sum).toFixed(2) ;
	    	that.val(sum);
	    	
	    	//获取数量
	    	var num = that.parent().prev().prev().prev().find("input").val();
	    	
	    	if(!num || isNaN(num)){
	    		return ;
	    	}
	    	
	    	//设置总金额
	    	var price = sum / num;
	    	that.parent().prev().prev().find('input').val(price.toFixed(8));
	    });
	    
	    function removeAttrByClazz(clz){
	    	$("." + clz).each(function(index, element){
	    		$(element).parent().parent().find(".edit_input").removeAttr("required") ;
	    	}) ;
	    }
	    
	    function addAttrByClazz(clz){
	    	$("." + clz).each(function(index, element){
	    		$(element).parent().parent().find(".edit_input").attr("required","") ;
	    	}) ;
	    }
	    
	    function changeDeclareNecc(depotId){
	    	$custom.request.ajax("/depot/checkDepotMerchantRel.asyn", {"depotId":depotId}, function(result){
				if(result.data.isInOutInstruction == '1'){
					$("#declarePriceI").show() ;
					isInOutInstruction = true ;
				}else{
					$("#declarePriceI").hide() ;
					isInOutInstruction = false ;
				}
			}); 
	    }
	 
</script>
