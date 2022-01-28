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
                       <li class="breadcrumb-item"><a href="#">销售退出库报告</a></li>
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
                                <span>销售退单号：</span>
                                <span>
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>退出仓库：</span>
                                <span>
                                    ${detail.outDepotName}
                                    <input name="outDepotId" type="hidden" value="${detail.outDepotId}">
                                    <input name="outDepotName" type="hidden" value="${detail.outDepotName}">
                                </span>
                            </div>
                             <div class="info_text">
                                <span>退入仓库：</span>
                                <span>
                                    ${detail.inDepotName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>事业部：</span>
                               <span>
                                    ${detail.buName}
                                </span>
                            </div>
                            <div class="info_text">
                               <span>创建人：</span>
                                <span>
                                    ${detail.createName}
                                </span>
                            </div>
                             <div class="info_text">
                               <span>提交时间：</span>
                                <span>
                                    ${detail.auditDate}
                                </span>
                            </div>
                        </div>
                              <div class="info_item">
                            <div class="info_text">
                                <span>海外仓理货单位：</span>
                               <span>
                                    ${detail.tallyingUnitLabel}
                                </span>
                            </div>
                             <div class="info_text"></div>
                             <div class="info_text"></div>
                        </div>
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
			                    	<input type="text" class="edit_input form_datetime date-input" name="returnOutDate" id="returnOutDate"  value="" >   
	                            </div>
	                            <div class="info_text">
	                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="allShelf"  name="allShelf">整批打托</button>
	                            </div>	
	                             <div class="info_text">
	                            </div>
	                        </div>
	                    </div>
                    </div>  
                    <div class="col-12 ml10">
                        <div class="info_box">
	                        <div class="info_item">
	                            <div class="info_text">
	                                <label >附件上传： </label>
	                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="btn-file" >上传理货报告</button>
		                            <form enctype="multipart/form-data" id="form-upload">
	                              		<input type="file" name="file" id="file" class="btn-hidden file-import">
                                	</form>
	                            </div>
	                             <div class="info_text">	
	                             	<a href="#" id="fileName" onclick="void(0) ;"></a>
	                            </div>
	                            <div class="info_text">	
	                            </div>
	                        </div>
	                    </div>
                    </div>   
                    <div class="form-row mt20 ml15 table-responsive">
                   <table id="table-list" class="table table-striped" style="width: 100%;">
                   <thead>
                      <tr>
                         <th><input id="checkbox11" type="checkbox" name="all">全选</th>
                         <th>商品货号</th>
                         <th>商品名称</th>
                         <th>条码</th>
                         <th>计划退出量</th>
                         <th>出库好品量</th>
                         <th>出库坏品量</th>
                         <th style='text-align:center;'>批次号</th>
                         <th style='text-align:center;'>生产日期</th>
                         <th style='text-align:center;'>失效日期</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach items="${detail.itemList }" var="item">
                    	<tr>
                    		<td>
	                    		<input type='checkbox' name='item-checkbox'>
		                    	<input type='hidden' name='outGoodsId' value='${item.outGoodsId}'>
		                    	<input type='hidden' name='outGoodsCode' value='${item.outGoodsCode}'>
		                    	<input type='hidden' name='outGoodsName' value='${item.outGoodsName}'>
		                    	<%--<input type='hidden' name='returnBatchNo' value='${item.returnBatchNo}'>--%>
		                    	<input type='hidden' name='poNo' value='${item.poNo}'>
	                    	</td>
                            <td>${item.inGoodsNo}<input type="hidden" name="outGoodsNo" value="${item.outGoodsNo}"/></td>
                            <td>${item.outGoodsName}<input type="hidden" name="goodsName" value="${item.outGoodsName}"/></td>
                            <td>${item.barcode}<input type="hidden" name="barcode" value="${item.barcode}"/></td>
                            <td>${item.returnNum+item.badGoodsNum}<input type="hidden" name='totalNum' style='width:70px;text-align:center;' class='goods-num' value="${item.returnNum+item.badGoodsNum}"></td>
                            <td><input type="text" name='returnNum' style='width:70px;text-align:center;' class='goods-num' value="0"<c:if test="${item.returnNum+item.badGoodsNum == 0}">disabled</c:if>></td>
                            <td><input type="text" name='badGoodsNum' style='width:70px;text-align:center;' class='goods-num' value="0"<c:if test="${item.returnNum+item.badGoodsNum == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='returnBatchNo' style='width:100px;text-align:center;' class='transferBatchNo-num' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='productionDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
                            <td style='text-align:center;'><input type="text" name='overdueDate' style='width:100px;text-align:center;' class='edit_input form_datetime date-input' value=""<c:if test="${(item.returnNum+item.badGoodsNum) == 0}">disabled</c:if>></td>
                        </tr>
                      </c:forEach>
                      </tbody>
                  </table>                              
                   </div>
                   
                   <div class="form-row mt20 ml15 attachDetail">
                   		<div class='col-12'>
							<div class="info-box">
								<div class="title_text">附件列表</div>
								<div id="attachmentTable" multiple></div>
							</div>
						</div>
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
    
    <!-- 用于附件上传code -->
    <input id="code" type="hidden" value="${detail.code}">

</div> <!-- content -->
<script type="text/javascript">
$(document).ready(function() {
    $(function(){
			//取消按钮
			$("#cancel-buttons").click(function(){
				$module.load.pageOrder('act=5001');
			});
		});
	
	 	// 点击整批打托出库按钮
	 	var isTrue=0;
	 	$("#allShelf").click(function(){
				$('#table-list tbody tr').each(function(i){
					if ($(this).find('input[name="item-checkbox"]').is(":checked")) {
						$(this).find('input[name="returnNum"]').val($(this).find('input[name="totalNum"]').val());
						isTrue=1;
					 }else{
						 $(this).find('input[name="shelfNum"]').val(0);
					 }
				});  
				// 如果一个框都没选,则提示
			 	if(isTrue!=1){
			 		 $custom.alert.error("您还没未勾选上架商品,请勾选出库商品");
			 	}
			});  
	 
		//保存按钮
		$("#save-buttons").click(function(){
			var url = serverAddr+"/saleReturn/saveOdepotReport.asyn";
			var form = {};
			var json = {};
			var itemList = [];
			var flag = true;
            var outDepotId = $('input[name="outDepotId"]').val();//退出仓仓库id
            var outDepotName = $('input[name="outDepotName"]').val();//退出仓仓库名称
			var isExistsShelfNum=0;// 0 表示本次出库好品数量和本次出库坏品数量都为0  , 1表示存在一个大于0的
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var orderId = $(this).find('input[name="orderId"]').val();
				var outGoodsId = $(this).find('input[name="outGoodsId"]').val();
				var outGoodsCode = $(this).find('input[name="outGoodsCode"]').val();
				var outGoodsNo = $(this).find('input[name="outGoodsNo"]').val();
				var outGoodsName = $(this).find('input[name="outGoodsName"]').val();
				var barcode = $(this).find('input[name="barcode"]').val();
				var totalNum = $(this).find('input[name="totalNum"]').val();	// 计划退出量
				var returnNum = $(this).find('input[name="returnNum"]').val();	// 好品量
				var badGoodsNum = $(this).find('input[name="badGoodsNum"]').val();	// 坏品量
				var returnBatchNo = $(this).find('input[name="returnBatchNo"]').val();	// 退货批次
                var productionDate = $(this).find('input[name="productionDate"]').val();	// 生产日期
                var overdueDate = $(this).find('input[name="overdueDate"]').val();	// 失效日期
				var poNo = $(this).find('input[name="poNo"]').val();	// PO单号
				var reg = /^\d+$/;

				
				// 好品量、坏品量必须有一个要大于0
				if (returnNum>0 || badGoodsNum>0) {
					isExistsShelfNum=1;
				}
			
				if (!reg.test(returnNum)) {
		       		$custom.alert.error("出库好品量只能输入正整数");
		       		flag = false;
		    		return;
		       	}
				if (!reg.test(badGoodsNum)) {
		       		$custom.alert.error("出库坏品量只能输入正整数");
		       		flag = false; 
		    		return;
		       	}
				
				var allNum = parseInt(returnNum)+parseInt(badGoodsNum);
				// 好品量+坏品量=计划退出量  做弱校验，出现不等时，不做强校验，仅做提示；
				if(allNum>parseInt(totalNum)){
					$custom.alert.error("出库好品量+出库坏品量不能大于计划退出量");
				}

                //检查可用量
                var availableNum =$module.inventory.queryAvailability(outDepotId,outGoodsId,null,null,null,returnBatchNo);
                var tempGoodsNo = $module.inventoryLocationMapping.getOriginalGoodsNo(outGoodsId, outGoodsNo) ;

                if (availableNum == -1) {
                    flag = false;
                    if(tempGoodsNo){
                        $custom.alert.warningText("原货号：" + tempGoodsNo + " 商品货号："+outGoodsNo+",未查到库存可用量");
                    }else{
                        $custom.alert.warningText("未查到库存余量货号：" + outGoodsNo + ",仓库:" + outDepotName);
                    }
                    return;
                } else if ((parseInt(returnNum)+parseInt(badGoodsNum)) > availableNum) {
                    flag = false;
                    if(tempGoodsNo){
                        $custom.alert.warningText("库存不足，"+"原货号：" + tempGoodsNo + " 商品货号："+ outGoodsNo +",余量："+availableNum);
                    }else{
                        $custom.alert.warningText("库存不足,货号：" + outGoodsNo + ",仓库:" + outDepotName + ",余量：" + availableNum);
                    }
                    return;
                }
			
				item['outGoodsId']=outGoodsId;
				item['outGoodsCode']=outGoodsCode;
				item['outGoodsNo']=outGoodsNo;
				item['outGoodsName']=outGoodsName;
				item['barcode']=barcode;
				item['totalNum']=totalNum;
				item['returnNum']=returnNum;
				item['badGoodsNum']=badGoodsNum;				
				item['returnBatchNo']=returnBatchNo;
				item['poNo']=poNo;
                item['productionDate']=productionDate;
                item['overdueDate']=overdueDate;
				itemList.push(item);
			});
			if(!flag){
	    		return;
			}
			// 出库好品量、出库坏品量必须有一个要大于0
			if (isExistsShelfNum==0) {
				$custom.alert.error("出库好品量、出库坏品量必须有一个要大于0");
				return;
			}
			var orderId = $("#orderId").val();
			var returnOutDate =$('#returnOutDate').val();	// 退货出库时间
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
						$load.a(pageUrl+"5001");
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
	
	//点击上传文件
	$("#btn-file").click(function(){
		var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
		$("#form-upload").empty();
		$("#form-upload").append(input);
		$("#file").click();
	});
	
	//上传文件到后端
	$("#form-upload").on("change",'.file-import',function(){
		var code = $("#code").val() ;
		var formData = new FormData($("#form-upload")[0]); 
		$custom.request.ajaxUpload(serverAddr+"/attachment/uploadFiles.asyn?code=" + code, formData, function(result){
			if(result.state == 200 && result.data && result.data.code == 200 ){
				$attachTable.fn.reload();
				$custom.alert.success("上传成功");
			}else{
				$custom.alert.error("上传失败");
			}
		});
	});
	
	var code =  $("#code").val()   ;
	$("#attachmentTable").createAttachTables(code);
	
});

</script>
