<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style type="text/css">
	.not-arrow{
		padding: 5px 10px;
		border:1px solid #dcd8d8;
		-webkit-appearance:none;
		-moz-appearance:none;
		appearance:none; /*去掉下拉箭头*/
	}
	.not-arrow::-ms-expand { display: none; }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="form1">
        	<input type="hidden" name='settlementPriceId' id='settlementPriceId' value='${detail.id}'/>
        </form>
        <form id="add-form">
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
							                      <li class="breadcrumb-item"><a href="#">标准成本单价管理</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13001');">标准成本单价列表</a></li>
							                      <li class="breadcrumb-item"><a href="javascript:$module.load.pageReport('act=13003&id=' + ${detail.id});">标准成本单价编辑</a></li>
							            </ol>
                                    </div>
                                </div>
                                <!--   商品信息  start -->
                                <div class="col-12 ml10">
                                	<div class="row">
				                         <div class="col-10">
				                             <div class="title_text">选择事业部</div>
				                        </div>
				                    </div>
				                    <div class="row">
				                         <div class="col-10 mt10">
				                             <select class="input_msg" name="buId" id="buId" disabled>
                                    		 </select>
				                        </div>
				                    </div>
                                </div>
				                <div class="col-12 ml10">
                                    <div class="row">
                                         <div class="col-10">
                                             <div class="title_text">商品信息</div>
                                        </div>
                                    </div>
                                 </div>                       
				                 <div class="form-row mt20 ml15">
                                    <table id="table-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
					                           <th>条形码</th>
					                           <th>商品名称</th>
					                           <th>品牌</th>
					                           <th>规格</th>
					                           <th><i class="red">*</i>结算币种</th>
					                           <th><i class="red">*</i>标准成本单价</th>
					                           <th><i class="red">*</i>生效年月</th>
					                           <th>是否组合</th>
					                           <th>操作</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                      			<c:forEach items="${itemList}" var="item">
				                      			<tr>
							    	            	<td style='text-align:center'>
							    	            		${item.barcode}
							    	            		<input type='hidden' name='goodsId' value='${item.goodsId}'/>
					                      				<input type="hidden" name='id' value='${item.id}'/>
								    	            	<input type='hidden' name='barcode' value='${item.barcode}'/>
								    	            	<input type='hidden' name='goodsNo' value='${item.goodsNo}'/>
								    	            	<input type='hidden' name='unitId' value='${item.unitId}'/>
								    	            	<input type='hidden' name='unitName' value='${item.unitName}'/>
								    	            	<input type='hidden' name='productId' value='${item.productId}'/>
							    	            	</td>
							    	            	<td style='text-align:center'>
								    	            	${item.goodsName}
								    	            	<input type='hidden' name='goodsName' value='${item.goodsName}'/>
							    	            	</td>
							    	            	<td style='text-align:center'>
							    	            		${item.brandName}
							    	            		<input type='hidden' name='brandId' value='${item.brandId}'/>
							    	            		<input type='hidden' name='brandName' value='${item.brandName}'/>
							    	            	</td>
							    	            	<td style='text-align:center'>
							    	            		${item.goodsSpec}
							    	            		<input type='hidden' name='goodsSpec' value='${item.goodsSpec}'/>
							    	            	</td>
							    	            	<td style='text-align:center'>
							    	            		<input type="hidden" value="${item.currency}" name="currencyValue">
							    	            		<select class='goods-unit' style='width:70px;height:25px;' name='currency' id='currency'>
							    	            		</select>
							    	            	</td>
							    	            	<td style='text-align:center'>
							    	            		<input type='text' name='price' class='goods-price' style='width:70px;text-align:center;' value='${item.price}'>
							    	            	</td>
							    	            	<td><input type='text' class='date-input form-control' name='effectiveDate' id='effectiveDate' >
							    	            		<input type="hidden" name='startDate' id='startDate' value='${item.startDate}'>
							    	            		<input type="hidden" id='effectiveDateShow' value='${item.effectiveDate}'>
							    	            	</td>
							                        <td>
							                        	<c:if test="${item.isGroup eq '0'}">否</c:if>
							                        	<c:if test="${item.isGroup eq '1'}">是</c:if>
								                        <input type='hidden' name='isGroup' value='${item.isGroup}'/>
								                    </td>
								                      <td>
							                        	<c:if test="${item.isGroup eq '0'}"></c:if>
							                        	<c:if test="${item.isGroup eq '1'}"><a href='javascript:showGroup(${item.goodsId})' >组合详情</a></c:if>
								                    </td>
						                        </tr>
						                        </c:forEach>
			                         	</tbody>
                                    </table>
                                </div>
                             <div class="col-10 ml12">
                              		<div class="edit_item">
                              		 <label class="edit_label"><i class="red">*</i> 调价原因：</label>
                                	 <textarea style="overflow-y:scroll;overflow-x:hidden;width:630px;height:200px;" id="adjustPriceResult" name="adjustPriceResult"></textarea>
                                    </div>
                                	 <div class="edit_item">
                                    </div>
                                    <div class="edit_item">
                                    </div>
                                </div>
                                 <!--   商品信息  end -->
                                <!--   价格变更记录    -->
                                <div class="col-12 ml10">
				                    <div class="row" >	
				                         <div class="col-10">
				                             <div class="title_text">修改历史记录</div>
				                        </div>
				                    </div>
				                </div>   
				                <div class="form-row mt20 ml15">
                                    <table id="table-record-list" class="table table-striped" cellspacing="0"  width="100%">
                                        <thead>
	                                        <tr>
	                                           <th>序号</th>
	                                           <th>事业部</th>
                                               <th>生效年月</th>
                                               <th>标准成本单价</th>
                                               <th>结算币种</th>
                                               <th>修改时间</th>
                                               <th>修改人员</th>
                                               <th>调价原因</th>
                                               <th>审核时间</th>
                                               <th>审核人员</th>
                                               <th>状态</th>
				                      		</tr>
			                      		</thead>
			                      		<tbody>
			                         	</tbody>
                                    </table>
                                </div>  
				                <!--   价格变更记录  end   -->
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons" >保 存</button>
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
                    <!-- end row -->
                </div>
            </div>
        </form>
        <!-- 弹窗开始 -->
        <!-- 弹窗结束 -->
    </div>
    <!-- container -->
</div>
    <jsp:include page="/WEB-INF/views/modals/17012.jsp" />
<script type="text/javascript">
	//事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],'${detail.buId }');
	
	$module.currency.loadSelectData.call($("select[name='currency']"),"");


	// 展示所有组合商品详情
	function showGroup(goodsId){
		
		$custom.request.ajaxSync(serverAddr+"/settlementPrice/listAllGroupMerchandiseByGroupId.asyn",{goodsId:goodsId},function(data){
			var groupList =data.data.groupList;
			
	    	$('#groupListDiv').html("");
	    	for (var i = 0; i < groupList.length; i++) {
	    		var goods=groupList[i];
	    		
	    		var groupListDivStr="<tr><td>"+goods.barcode+"</td>"+
												  "<td>"+goods.goodsNo+"</td>"+
												  "<td>"+goods.name+"</td>"+
												  "<td>"+goods.goodsCode+"</td>"+
												  "<td>"+goods.groupNum+"</td>"+
												  "<td>"+goods.groupPrice+"</td></tr>";																			  	    		
				$('#groupListDiv').append(groupListDivStr);
			}     
		});   
		$("#invoice-modal").modal("show");
		
	};
	
	$(function(){
		$('#table-list tbody tr').each(function(i){// 遍历 tr
			var startDate = $(this).find('input[name="startDate"]').val();
			var effectiveDate = $("#effectiveDateShow").val() ;
			//日期控件初始化
	        $(".date-input").datetimepicker({
		        language:  'zh-CN',
		        format: "yyyy-mm",
				autoclose: true,
				todayHighlight: true,
				startView: 3,
				minView: 3,
				maxView:3,
				startDate: startDate
		    });
			$("#effectiveDate").val(effectiveDate) ;
			
			/**
			* 若最大关账时间与选择时间年份不一致 ， 年份框设置最大关账时间年份可选
			*/
			$(".datetimepicker-years .year .active").prev().removeClass("") ;
			
			var currency = $(this).find("input[name='currencyValue']").val() ;
			var currencySelect = $(this).find("select[name='currency']") ;
			
			if(currency){
				currencySelect.val(currency) ;
			}
			
		});
		//取消按钮
		$("#cancel-buttons").click(function(){
			$module.load.pageReport("act=13001");
		});
		//保存
		$("#save-buttons").click(function(){
			var url = serverAddr+"/settlementPrice/modifySettlementPrice.asyn";
			var form = {};
			var json = {};
			var itemList = [];
			var flag = true;
			
			var buId = $("#buId").val() ;
			
			if(!buId){
				$custom.alert.error("事业部不能为空");
	    		return;
			}
			
			json['buId']=buId;
			
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
				var id = $(this).find('input[name="id"]').val();
				var goodsId = $(this).find('input[name="goodsId"]').val();
				var goodsNo = $(this).find('input[name="goodsNo"]').val();
				var barcode = $(this).find('input[name="barcode"]').val();
				var goodsName = $(this).find('input[name="goodsName"]').val();
				var brandId = $(this).find('input[name="brandId"]').val();
				var brandName = $(this).find('input[name="brandName"]').val();
				var goodsSpec = $(this).find('input[name="goodsSpec"]').val();
				var currency = $(this).find('select[name="currency"]').val();
				var price = $(this).find('input[name="price"]').val();
				var effectiveDate = $(this).find('input[name="effectiveDate"]').val();
				var startDate = $(this).find('input[name="startDate"]').val();
				var isGroup = $(this).find('input[name="isGroup"]').val();
				var unitId = $(this).find('input[name="unitId"]').val();
				var unitName = $(this).find('input[name="unitName"]').val();
				var productId = $(this).find('input[name="productId"]').val();
				var adjustPriceResult = $("#adjustPriceResult").val();
				//===============参数校验==========================================
				//价格
				if(!price || price < 0){
					$custom.alert.error("标准成本单价不能为空或为负");
		       		flag = false;
		    		return;
				}
		    	var reg1 = /^\d+(\.\d+)?$/;
		       	if (!reg1.test(price)) {
		       		$custom.alert.error("标准成本单价只能输入数字");
		       		flag = false;
		    		return;
		       	}
		       	if(price.length>20){
		       		$custom.alert.error("标准成本单价长度最多只能输入20位（包含小数点）");
		       		flag = false;
		    		return;
		       	}
		       	if(!effectiveDate){
					$custom.alert.error("生效年月不能为空");
		       		flag = false;
		    		return;
				}
		       	if(adjustPriceResult==''||adjustPriceResult==null){
					$custom.alert.error("调价原因不能为空");
		       		flag = false;
		    		return;
				}
		       	item['id']=id;
		       	item['goodsId']=goodsId;
		       	item['goodsNo']=goodsNo;
		       	item['barcode']=barcode;
		       	item['goodsName']=goodsName;
		       	item['brandId']=brandId;
		       	item['brandName']=brandName;
		       	item['goodsSpec']=goodsSpec;
		       	item['currency']=currency;
		       	item['price']=price;
		       	item['effectiveDate']=effectiveDate;
		    	item['startDate']=startDate;
		       	item['isGroup']=isGroup;
		       	item['unitId']=unitId;
		       	item['unitName']=unitName;
		        item['productId']=productId;
		        item['adjustPriceResult']=adjustPriceResult;
				itemList.push(item);
			});
			//商品参数是否通过校验
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
			json['itemList']=itemList;
			json['adjustPriceResult']=adjustPriceResult;
			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("编辑成功！");
					setTimeout(function () {
						$module.load.pageReport("act=13001");
					}, 1000);
				}else{
					if(!!result.expMessage){
						$custom.alert.error("编辑失败："+result.expMessage);
					}else{
						$custom.alert.error(result.message);
					}
					
				}
			});
		});
		
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr+'/settlementPrice/listRecordPrice.asyn?r=' + Math.random(),
            columns: [{  //序号
                data : null,
                bSortable : false,
                className : "",
                render : function(data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                   }
                },{data: 'buName'},
                {data: 'effectiveDate'}, {data: 'price'}, {data: 'currencyLabel'}, 
                {data: 'modifyDate'},{data:'modifier'},{data: 'adjustPriceResult'},
                {data: 'examineDate'},{data: 'examiner'},
                {data: 'statusLabel'}
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            
        };
        //生成列表信息
        $('#table-record-list').mydatatables();
	});
</script>
