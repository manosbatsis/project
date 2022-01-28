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
                       <li class="breadcrumb-item"><a href="#">上架商品</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <input name="code" id="code2" type="hidden" value="${detail.code}"/>
                                <span>销售订单编号：</span>
                                <span id="code1">
                                    ${detail.code}
                                </span>
                            </div>
                            <div class="info_text">
                                <span>出仓仓库：</span>
                                <span id="outDepotName">
                                    ${detail.outDepotName}
                                </span>
                                <input type="hidden" id="getOutDepotCode"  value="${detail.outDepotCode}">
                            </div>
                             <div class="info_text">
                                <span>客户：</span>
                                <span id="customerName">
                                    ${detail.customerName}
                                </span>
                            </div>
                        </div>
                        <div class="info_item">
                              <div class="info_text">
                                <span>销售出库单号：</span>
                               <span id="saleOutDepotCode">
                                    ${detail.saleOutDepotCode}
                                </span>
                            </div>
                            <div class="info_text">
                               <span>销售类型：</span>
                                <span id="businessModelLabel">
                                    ${detail.businessModelLabel}
                                </span>
                            </div>
                             <div class="info_text">
                               <span>入库仓库：</span>
                                <span id="inDepotName">
                                    ${detail.inDepotName}
                                </span>
                            </div>
                        </div>
                         <div class="info_item">
                          <div class="info_text">
                            	<span>事业部：</span>
                                <span id="buName">
                                    ${detail.buName}
                                </span>
                            </div>
                             <div class="info_text"></div>
                              <div class="info_text"></div>
                        </div>
                          <div class="info_item">
                              <div class="info_text">
                                <span>po单号：</span>
                                <span>
                                    <textarea rows="3" cols="145" style="border: none;">${detail.poNo}</textarea>
                                </span>
                                <input type="hidden" name="poNo2" value="${detail.poNo}" id="poNo2">
                            </div>
                             <div class="info_text"></div>
                             <div class="info_text"></div>
                        </div>
                    </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                         <div class="col-10">
                             <div class="title_text">商品上架信息</div>
                        </div>
                    </div> 
                    <br/>
                    <div class="edit_box">
                         <div class="edit_item">
	                            <div class="info_text">
	                                <label ><i class="red">*</i>本次上架PO号：</label>
			                    	 <select class="edit_input" name="poNo" id="poNo" parsley-trigger="change" required>
		                                		<option value="">请选择</option>
												<c:forEach  items="${poList}" var="po">
				                                		<option value="${po}">${po}</option>
				                                </c:forEach>
		                            </select>              
	                            </div>
	                             <div class="info_text">
	                            <label class="red"> 提示：仅单PO上架</label> 
	                            </div>	
	                        <!--      <div class="info_text">
	                            </div> -->
	                    </div>
                    </div> 
                      <div class="edit_box">
                         <div class="edit_item">
	                            <div class="info_text">
	                                <label ><i class="red">*</i>本次上架时间  ： </label>
			                    	<input  autocomplete="off" type="text" class="edit_input form_datetime date-input" name="shelfDate" id="shelfDate"  value="" >
	                            </div>
	                            <div class="info_text">
	                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="allShelf"  name="allShelf">整单上架</button>
	                            </div>	
	                    <!--          <div class="info_text">
	                            </div> -->
	                    </div>
                    </div>  
                      <div class="edit_box">
                      <div class="edit_item">
	                            <div class="info_text">
	                                <label >&nbsp上传理货报告&nbsp： </label>
	                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="btn-file" >上传理货报告</button>
		                            <form enctype="multipart/form-data" id="form-upload">
	                              		<input type="file" name="file" id="file" class="btn-hidden file-import">
                                	</form>
	                            </div>
	                             <div class="info_text">	
	                             	<a href="#" id="fileName" onclick="void(0) ;"></a>
	                            </div>
	                      <!--       <div class="info_text">	
	                            </div> -->
	                    </div>
                    </div>   
                    <div class="form-row mt20 ml15 table-responsive">
                   <table id="table-list" class="table table-striped" style="width: 1550px;">
                   <thead>
                      <tr>
                         <th><input id="checkbox11" type="checkbox" name="all">全选</th>
                         <th>商品货号</th>
                         <th>商品名称</th>
                         <th>条码</th>
                         <th>海外仓理货单位</th>
                         <th>出库数量</th>
                         <th>已上架量</th>
                         <th>已计残损量</th>
                         <th>已计少货量</th>
                         <th>待上架数量</th>
                         <th>本次上架数量</th>
                         <th>本次残损数量</th>
                         <th>本次少货数量</th>
                         <th>备注</th>
                      </tr>
                      </thead>
                      <tbody id="tbody">
                      <c:forEach items="${shelfList}" var="item">
                    	<tr>
                    		<td>
	                    		<input type='checkbox' name='item-checkbox'>
		                    	<input type='hidden' name='goodsId' value='${item.goodsId}'>
		                    	<input type='hidden' name='orderType' value='${item.orderType}'>
		                    	<input type='hidden' name='orderId' value='${item.orderId}'>
		                    	<input type='hidden' name='commbarcode' value='${item.commbarcode}'>
	                    	</td>
                            <td>${item.goodsNo}<input type="hidden" name="goodsNo" value="${item.goodsNo}"/></td>
                            <td>${item.goodsName}<input type="hidden" name="goodsName" value="${item.goodsName}"/></td>
                            <td>${item.barcode}<input type="hidden" id="barcode" name="barcode" value="${item.barcode}"/></td>
                            <td>
                            	<input type="hidden" name="tallyingUnit" value="${item.tallyingUnit}"/>
								<c:if test="${item.tallyingUnit eq '00' }">托盘</c:if>
								<c:if test="${item.tallyingUnit eq '01' }">箱</c:if>
								<c:if test="${item.tallyingUnit eq '02' }">件</c:if>
							</td> 
                            <td>${item.num}<input type="hidden" name='num' style='width:70px;text-align:center;' class='goods-num1' value="${item.num}"></td>
                            <td>${item.totalShelfNum}<input type="hidden" name='totalShelfNum' style='width:70px;text-align:center;' class='goods-num1' value="${item.totalShelfNum}"></td>
                            <td>${item.totalDamagedNum}<input type="hidden" name='totalDamagedNum' style='width:70px;text-align:center;' class='goods-num1' value="${item.totalDamagedNum}"></td>
                            <td>${item.totalLackNum}<input type="hidden" name='totalLackNum' style='width:70px;text-align:center;' class='goods-num1' value="${item.totalLackNum}"></td>
                            <td>${item.stayShelfNum}<input type="hidden" name='stayShelfNum' style='width:70px;text-align:center;' class='goods-num1' value="${item.stayShelfNum}"></td>
	                        <c:choose>
								<c:when test="${item.stayShelfNum == 0}">
									<td><input type="text" name='shelfNum' style='width:70px;text-align:center;' class='goods-num' value="0" disabled></td>
								</c:when>
								<c:otherwise>
									<td><input type="text" name='shelfNum' style='width:70px;text-align:center;' class='goods-num' value="${item.shelfNum}"></td>
								</c:otherwise>
							</c:choose>
                            
                            <td><input type="text" name='damagedNum' style='width:70px;text-align:center;' class='goods-num' value="${item.damagedNum}"<c:if test="${item.stayShelfNum == 0}">disabled</c:if>></td>
                            <td><input type="text" name='lackNum' style='width:70px;text-align:center;' class='goods-num' value="${item.lackNum}"<c:if test="${item.stayShelfNum == 0}">disabled</c:if>></td>
                        	<td><input type="text" name="remark" value="${item.remark}" style='width:70px;text-align:center;'/></td>
                        </tr>
                      </c:forEach>

                      </tbody>
                       <tr>
                           <td></td>
                           <td>汇总</td>
                           <td></td>
                           <td></td>
                           <td></td>
                           <td><span id="num" ></span></td>
                           <td><span id="totalShelfNum"></span></td>
                           <td><span id="totalDamagedNum"></span></td>
                           <td><span id="totalLackNum"></span></td>
                           <td><span id="stayShelfNum"></span></td>
                           <td><div style="margin:0 32px ;"><span id="shelfNum"></span></div></td>
                           <td><div style="margin:0 32px ;"><span id="damagedNum"></span></div></td>
                           <td><div style="margin:0 32px ;"><span id="lackNum"></span></div></td>
                           <td></td>
                       </tr>
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
        $derpTimer.lessThanNowDateTimer("#shelfDate", "yyyy-MM-dd") ;

        //取消按钮
        $("#cancel-buttons").click(function(){
            $module.load.pageOrder('act=4001');
        });

        //触发汇总统计
        shelfNumSum();
	
	 	// 点击整单上架按钮
	 	var isTrue=0;
	 	$("#allShelf").click(function(){
            $(".goods-num").val(0);
            $('#table-list tbody tr').each(function(i){
                if($(this).find('input[name="item-checkbox"]').is(":checked")) {
                    $(this).find('input[name="shelfNum"]').val($(this).find('input[name="stayShelfNum"]').val());
                    isTrue=1;
                 }else{
                     $(this).find('input[name="shelfNum"]').val(0);
                 }
            });
            // 如果一个框都没选,则提示
            if(isTrue!=1){
                 $custom.alert.error("您还没未勾选上架商品,请勾选上架商品");
            }
            //触发汇总统计
            shelfNumSum();
        });
	 
		//保存按钮
		$("#save-buttons").click(function(){
		    var code = $("#code2").val();
            var poNo = $('#poNo').val();// PO单号
            var shelfDate =$('#shelfDate').val();
            if(poNo==""||poNo==undefined){
                $custom.alert.error("请选择PO号");
                return false;
            }
            if(!shelfDate){
                $custom.alert.error("上架时间不能为空");
                return false;
            }
            var flag = true;//检查是否通过
			var form = {};
			var json = {};
			var itemList = [];
			$('#tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var orderType = $(this).find('input[name="orderType"]').val();
				var goodsId = $(this).find('input[name="goodsId"]').val();
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				var goodsName = $(this).find('input[name="goodsName"]').val();
				var barcode = $(this).find('input[name="barcode"]').val();
				var tallyingUnit = $(this).find('input[name="tallyingUnit"]').val();
				var num = $(this).find('input[name="num"]').val();
				var shelfNum = $(this).find('input[name="shelfNum"]').val();
				var damagedNum = $(this).find('input[name="damagedNum"]').val();
				var lackNum = $(this).find('input[name="lackNum"]').val();
				var remark = $(this).find('input[name="remark"]').val();
				var commbarcode = $(this).find('input[name="commbarcode"]').val();
				var reg = /^\d+$/;
				if (!reg.test(shelfNum)) {
		       		$custom.alert.error("上架数量只能输入正整数");
                    flag = false;
		    		return false;
		       	}
				if (!reg.test(damagedNum)) {
		       		$custom.alert.error("残损数量只能输入正整数");
                    flag = false;
                    return false;
		       	}
				if (!reg.test(lackNum)) {
		       		$custom.alert.error("少货数量只能输入正整数");
                    flag = false;
                    return false;
		       	}

				var getOutDepotCode=$("#getOutDepotCode").val();
				item['orderType']=orderType;
				item['goodsId']=goodsId;
				item['goodsNo']=goodsNo;
				item['goodsName']=goodsName;
				item['barcode']=barcode;
				item['tallyingUnit']=tallyingUnit;
				item['num']=num;
				item['shelfNum']=shelfNum;
				item['damagedNum']=damagedNum;
				item['lackNum']=lackNum;				
				item['remark']=remark;
				item['commbarcode']=commbarcode;
				itemList.push(item);
			});
            if( flag == true) {
                json['code'] = code;
                json['poNo'] = poNo;
                json['shelfDate'] = shelfDate;
                json['itemList'] = itemList;
                var jsonStr = JSON.stringify(json);
                form['json'] = jsonStr;
                var url = serverAddr + "/sale/saveSaleShelf.asyn";
                $custom.request.ajax(url, form, function (result) {
                    if (result.state == '200') {
                        $custom.alert.success("上架成功！");
                        if(result.data.state == "031"){//部分上架, 重新加载上架页面
                        	setTimeout(function () {
                        		$load.a(pageUrl +"40016&id=" + result.data.id);
	                        }, 500);  
                        }else{//返回销售订单列表页面
	                        setTimeout(function () {
	                            $load.a(pageUrl + "4001");
	                        }, 1000);                        	
                        }
                    } else {
                        if (!!result.expMessage) {
                            $custom.alert.error(result.expMessage);
                        } else {
                            $custom.alert.error(result.message);
                        }
                    }
                });
            }

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
	$("#attachmentTable").createAttachTables(code);
});

$('#poNo').change(function() {
	
	var poNo = $(this).find("option:selected").text();
    var code = $("#code1").text().trim();
    $("#shelfDate").val("");
    $(".goods-num").val(0);
    if(poNo != '请选择'){
        var url = serverAddr+"/platformPurchaseOrder/listPlatformPurchaseOrderByCodeAndPoNo.asyn";
        $custom.request.ajax(url, {"poNo":poNo}, function(result){
            for(var j=0; j < $('#tbody tr').length;j++){
                var tr = $('#tbody tr').eq(j);
            	var stayShelfNum = tr.find('td').eq(9).text();
            	if(parseInt(stayShelfNum)  > 0){            		
	                var barcode = tr.find('td').eq(3).text();
	                for(var i=0; i < result.data.length;i++){
	                    var deli = result.data[i].deliverDate;
	                    if(deli){
		                    $("#shelfDate").val(deli.split(" ",1));	                    	
	                    }
	                    if (result.data[i].platformBarcode.indexOf(barcode) >= 0){
	                        tr.find('td').eq(10).children().val(result.data[i].receiptOkNum);
	                        tr.find('td').eq(11).children().val(result.data[i].receiptBadNum);
	                        tr.find('td').eq(12).children().val(0);
	                    }
	                }
            	}
            }
        })
    }
    //触发汇总统计
    shelfNumSum();
})
//汇总统计
$("#table-list").on("blur",'.goods-num',function(){
    shelfNumSum();
});

//汇总统计
function shelfNumSum() {
    //出库数量
    var total = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(5) input').val();
        if(ch=='') ch = 0;
        total = parseInt(total) + parseInt(ch);
    })
    $("#num").text(total);
    //已上架数量
    var total2 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(6) input').val();
        if(ch=='') ch = 0;
        total2 = parseInt(total2) + parseInt(ch);
    })
    $("#totalShelfNum").text(total2);
    //已计残损量
    var total3 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(7) input').val();
        if(ch=='') ch = 0;
        total3 = parseInt(total3) + parseInt(ch);
    })
    $("#totalDamagedNum").text(total3);
    //已计少货量
    var total4 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(8) input').val();
        if(ch=='') ch = 0;
        total4 = parseInt(total4) + parseInt(ch);
    })
    $("#totalLackNum").text(total4);
    //待上架数量
    var total5 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(9) input').val();
        if(ch=='') ch = 0;
        total5 = parseInt(total5) + parseInt(ch);
    })
    $("#stayShelfNum").text(total5);
    //本次上架数量
    var total6 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(10) input').val();
        if(ch=='') ch = 0;
        total6 = parseInt(total6) + parseInt(ch);
    });
    $("#shelfNum").text(total6);
    //本次残损数量
    var total7 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(11) input').val();
        if(ch=='') ch = 0;
        total7 = parseInt(total7) + parseInt(ch);
    })
    $("#damagedNum").text(total7);
    //少货数量
    var total8 = 0;
    $("#tbody tr").each(function () {
        var ch = $(this).find('td:eq(12) input').val();
       if(ch=='') ch = 0;
        total8 = parseInt(total8) + parseInt(ch);
    })
    $("#lackNum").text(total8);
}

</script>
