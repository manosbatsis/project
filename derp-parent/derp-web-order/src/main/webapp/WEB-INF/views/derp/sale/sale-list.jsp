<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .date-input {
        font-size: 13px;
    }
</style>
<div class="content">
	<div class="container-fluid mt80">
		<!-- Start row -->
		<div class="row">
			<!--  title   start  -->
			<div class="col-12">
				<div class="card-box">
					<div class="col-12 row">
						<div>
							<ol class="breadcrumb">
								<li><i class="block"></i> 当前位置：</li>
								<li class="breadcrumb-item"><a href="#">销售订单列表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">销售订单编号 :</span>
									<input class="input_msg" name="code" />
									<span class="msg_title">出仓仓库 :</span>
									<select name="outDepotId" class="input_msg">
										<option value="">请选择</option>
									</select>
									<span class="msg_title">客户 :</span>
									<select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchSaleOrder();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">PO单号 :</span>
									<input type="text" class="input_msg" name="poNo">
									<span class="msg_title">订单状态 :</span>									
									<select class="input_msg" name="stateSelect" id="stateSelect"></select>	
									<input type="hidden" name="stateList"/>
									<span class="msg_title">销售类型:</span>
									<select class="input_msg" name="businessModel">
									</select>	
								</div>
								<div class="form_item h35">
									<span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
									<span class="msg_title">单据标识 :</span>
									<select class="input_msg" name="orderType" id="orderType"></select>
									<span class="msg_title">订单日期 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="createDateStartDate" id="createDateStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateEndDate" id="createDateEndDate">
								</div>
								<div class="form_item h35">
									<span class="msg_title">金额调整状态 :</span>
									<select name="amountStatus" class="input_msg"> </select>
									<span class="msg_title">金额确认状态 :</span>
									<select name="amountConfirmStatus" class="input_msg"></select>
									<span class="msg_title">上架日期 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="shelfDateStartDate" id="shelfDateStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="shelfDateEndDate" id="shelfDateEndDate">
								</div>
								
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a>  -->
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="sale_add">
						<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="save-buttons">新增</button>
						</shiro:hasPermission>

						<shiro:hasPermission name="sale_import">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default"  onclick="javascript:$module.load.pageOrder('act=40014');">导入</button>
						</shiro:hasPermission>

						<shiro:hasPermission name="sale_saleOrderStockOut">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="stockOut">中转仓出库</button>
						</shiro:hasPermission>

						<shiro:hasPermission name="sale_delSaleOrder">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" onclick="del();">删除</button>
						</shiro:hasPermission>

						<shiro:hasPermission name="sale_exportSaleOrder">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="exportOrder">导出</button>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="sale_endStockOut">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="endStockOut">完结出库</button>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="sale_createPurchase">
							<input type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="createPurchase" value="生成采购订单"/>
						</shiro:hasPermission>
						<shiro:hasPermission name="ownSaleOrder_add">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="saveOwnSaleOrder-buttons">新增内部销售订单</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="sale_copy">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="saleCopy-buttons">复制</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="sale_importSaleShelf">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="importShelf-buttons">上架导入</button>
						</shiro:hasPermission>
						<shiro:hasPermission name="sale_exportCustoms">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="exportCustomsInfo-buttons">导出清关资料</button>
						</shiro:hasPermission>
					</div>
					<!--  ================================表格 ===============================================   -->
					<div class="mt10 table-responsive">
						<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
									<th>销售订单编号</th>
									<th>出仓仓库</th>
									<th>事业部</th>
									<th>公司</th>
									<th>PO单号</th>
									<th>客户</th>
									<th>销售订单总数量</th>
									<th>销售订单总金额</th>
									<th style="min-width: 100px">金额调整状态</th>
									<th>订单日期</th>
									<th>订单状态</th>
									<th>销售类型</th>
									<th style="min-width: 60px">操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!--  ================================表格  end ===============================================   -->
				</div>
				 <jsp:include page="/WEB-INF/views/modals/9031.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9033.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9034.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9035.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9036.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9037.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9038.jsp" />
				 <jsp:include page="/WEB-INF/views/modals/9039.jsp" />
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		</div>
	</div>
	<!-- content -->
	<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="businessModel"]')[0],"saleOrder_businessModelList",null);
	// $module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"saleOrder_stateList",null);
	$module.constant.getConstantListByNameURL.call($('select[name="amountStatus"]')[0],"saleOrder_amountStatusList",null);
	$module.constant.getConstantListByNameURL.call($('select[name="amountConfirmStatus"]')[0],"saleOrder_amountConfirmStatusList",null);	
	//加载事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null);
	$module.constant.getConstantListByNameURL.call($('select[name="orderType"]')[0],"saleOrder_orderTypeList",null);
	  function showHideCode(obj){
   		$(obj).prev().toggle();
   		//被隐藏了
    	if($(obj).prev().is(":hidden")){
    		$(obj).prev().prev().show();
   		}else{
   			$(obj).prev().prev().hide();
   		} 
       }
	$(document).ready(function() {
		showState();
			//初始化
			$mytable.fn.init();
			
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/sale/listSaleOrder.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" class="iCheck">';
							}
						},
						{data : 'code'},{data : 'outDepotName'},{data : 'buName'},{data : 'merchantName'},{data : 'poNo'},{data : 'customerName'},{data : 'totalNum'},{data : 'totalAmount'},{data : 'amountStatusLabel'},{data : 'createDate'},{data : 'stateLabel'},{data : 'businessModelLabel'},
						{ //操作
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								var edit = "";
								var operate ="";
								// 订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库,027:出库中,025：已签收 026：已上架 031-部分上架
								if (row.state == '008') {
									if(row.orderType=="2"){// 非预售转销
										edit = '<shiro:hasPermission name="sale_edit"><a href="javascript:edit('+ row.id +","+row.businessModel+ ',1)">编辑</a></shiro:hasPermission>&nbsp'
									}else if(row.orderType=="1"){	// 预售转销
										edit = '<shiro:hasPermission name="sale_edit"><a href="javascript:editPreSaleBySale('+row.id +',1)">编辑</a></shiro:hasPermission>&nbsp'
									}
								}
								if (row.state == '001') {
									if(row.orderType=="2"){// 非预售转销
										edit = '<shiro:hasPermission name="sale_edit"><a href="javascript:edit('+ row.id +","+row.businessModel+ ',2)">审核</a></shiro:hasPermission>&nbsp'
									}else if(row.orderType=="1"){	// 预售转销
										edit = '<shiro:hasPermission name="sale_edit"><a href="javascript:editPreSaleBySale('+row.id +',2)">审核</a></shiro:hasPermission>&nbsp'
									}
								}
								// 若销售类型为：1 购销-整批结算 、4 购销-实销实结  3 采销执行
								if(row.businessModel == '1' || row.businessModel == '4' || row.businessModel == '3'){
									// 当订单状态为已出库，可操作项为 签收和详情、附件；(购销-整批结算订单，订单日期在2020以前的屏蔽掉签收入口)
									if((row.businessModel == '1' && row.createDate>='2020-01-01 00:00:00' && row.state == '018') ||
											(row.businessModel == '4' && row.state == '018') || (row.businessModel == '3' && row.state == '018')){
										edit = '<shiro:hasPermission name="sale_receive"><input value='+row.id+' type="hidden" name = "receiveId"><input value='+row.businessModel+' type="hidden" name = "receiveBusinessModel"><input value="'+row.poNo+'" type="hidden" name = "receivePoNo"><a href="###"  onclick="javascript:receive(this)">签收</a></shiro:hasPermission>&nbsp';
									}
									
									// 当订单状态为已签收、部分上架，可操作项为 上架和详情;
									if((row.state == '025' || row.state == '031')){
										edit = '<shiro:hasPermission name="sale_shelfIsEnd"><a href="javascript:saleShelf('+ row.id + ')">上架</a></shiro:hasPermission>&nbsp';
									}
								}
								//当订单状态为已审核、仓库类型为非批次效期强校验的仓库 可操作 打托出库(中转仓除外)
								var ourDepotData = $module.depot.getDepotById(row.outDepotId);
								if(ourDepotData != null) {
									if (ourDepotData.type != 'd') {
										if (row.state == '003' && (row.outDepotBatchValidation == '0' || row.outDepotBatchValidation == '2' )) {
											if (!edit == "") edit += '<br>';
											edit += '<shiro:hasPermission name="sale_out"><a href="javascript:saleOrderOut(' + row.id + ')">打托出库</a></shiro:hasPermission></br>';
										}
									}
								}
								//当订单状态不为待审核、待提交，销售类型为采销执行，融资申请状态为待申请时，可操作融资申请
								if(row.financeStatus == '0' && row.state !='001' && row.state != '008' && row.businessModel == '3'){
									edit += '<shiro:hasPermission name="finance_apply"><a href="javascript:$m9038.init(' + row.id + ')">融资申请</a></shiro:hasPermission><br>';
								}
								//当订单状态不为待审核、待提交，销售类型为采销执行，融资申请状态为已申请时，可操作融资赎回
								if(row.financeStatus == '1' && row.state !='001' && row.state != '008' && row.businessModel == '3'){
									edit += '<shiro:hasPermission name="finance_ransom"><a href="javascript:$m9039.init(' + row.id + ')">融资赎回</a></shiro:hasPermission><br>';
								}
								return edit + ' <shiro:hasPermission name="sale_toAttachment"><a href="javascript:toAttachment(\''+row.code+'\')">附件</a></shiro:hasPermission>&nbsp<shiro:hasPermission name="sale_detail"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
							}
						}, ],
				formid : '#form1'
			};
			
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	            if(aData.poNo!=null){
	            	 var itemList = new Array();
	            	 if(aData.poNo.indexOf("&")!=-1){
	            		 itemList.push(aData.poNo.split("&"));
		            	 for (var i = 0; i < itemList.length; i++) {
		            		// 如果只有一个PO号
		            		if(itemList[i].length==1){
		   	        			 $('td:eq(5)', nRow).html(aData.poNo);
		   	        		}else{	// 如果只有多个PO号
		   	        			// 显示poNo
		   	        			$('td:eq(5)', nRow).html("<div name='partPoNo'>"+aData.poNo.split("&")[0]+"</div><div name = 'showdiv' style='display:none;'>"+aData.poNo+"</div><a href='###' onclick='showHideCode(this)''>查看更多</a>");
		   	        		}
						}
	            	 }
	          	}
	            //单据状态为“非待审核”，金额确认状态为“非确认通过”，上架月份未关账，可以修改金额
	            var amountEdit = "";
	            if(aData.state != "001" && aData.state != "008" && aData.amountConfirmStatus != "1" && aData.hasFinanceClose =="0"){
	            	amountEdit = aData.amountStatusLabel + '&nbsp<shiro:hasPermission name="sale_amount_adjust"><a href="javascript:amountAdjust('+ aData.id + ', 1)">修改</a></shiro:hasPermission>';
				}else{
					amountEdit = aData.amountStatusLabel;
				}
	            // 单据状态不为“待审核”“待提交”，上架月份未关账，可以确认金额
	            if(aData.state != "001" && aData.state != "008" && aData.hasFinanceClose =="0"){
		            $('td:eq(9)', nRow).html(amountEdit + '<br>' +aData.amountConfirmStatusLabel + '&nbsp<shiro:hasPermission name="sale_amount_confirm"><a href="javascript:amountAdjust('+ aData.id + ', 2)">确认</a></shiro:hasPermission>');	            	
	            }else{
	            	$('td:eq(9)', nRow).html(amountEdit + '<br>' +aData.amountConfirmStatusLabel);
	            }
	            $('td:eq(8)', nRow).html(aData.currency +"&nbsp;"+aData.totalAmount);
			};
			//加载仓库的下拉数据
			$module.depot.loadSelectData.call($('select[name="outDepotId"]')[0]);
			//生成列表信息
			$('#datatable-buttons').mydatatables();
			
			var statesIds = $('input[name="stateList"]').val();
			var statesArr = statesIds.split(",");
			$('select[name="stateSelect"]').selectpicker('val', statesArr);
		});

		function searchData() {
			$mytable.fn.reload();
		}
		//详情
		function details(id) {
			$module.load.pageOrder("act=40013&id=" + id);
		}
		//编辑
		function edit(id,businessModel,operate) {
			//if(businessModel=='3'){
				//$module.load.pageOrder("act=40019&id=" + id+"&operate=" +operate);
			//}else{				
				$module.load.pageOrder("act=40012&id=" + id+"&operate=" +operate);
			//}	
		}
	//编辑预售单转换后的销售单
	function editPreSaleBySale(id,operate) {
		var saveJson = {};
		saveJson.type="2";
		saveJson.saleOrderId=id;
		saveJson.operate=operate;
		var jsonStr = encodeURIComponent(JSON.stringify(saveJson));
		$module.load.pageOrder("act=40020&data="+jsonStr);
	}
		// 签收
	    function receive(obj){
	    	var poNo=$(obj).prev().val();
	    	var businessModel=$(obj).prev().prev().val();
	    	var id=$(obj).prev().prev().prev().val();
	    	$('#id').val(id);
	    	$('#businessModel2').val(businessModel);
	    	if(poNo=="null"){
	    		poNo="";
	    	}
	    	$('#poNoReceive').val(poNo);
	    	$('#receive-modal').modal('show');
	    	$module.revertList.serializeListForm() ;
		    $module.revertList.isMainList = true ;    	
	    }

		/**
		 * 全选
		 */
        $("input[name='keeperUserGroup-checkable']").on("change", function(){
            if($(this).is(':checked')){
                $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
                $('#datatable-buttons tbody tr').addClass('success');
            }else{
                $(":checkbox", '#datatable-buttons').prop("checked",false);
                $('#datatable-buttons tbody tr').removeClass('success');
            }
        })
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })

		// 点击中转仓出库，弹框选择出库时间
		$("#stockOut").on("click",function(){
			//获取选中的id信息
			var ids=$mytable.fn.getCheckedRow();
			if(ids==null||ids==''||ids==undefined){
				$custom.alert.warningText("至少选择一条记录！");
				return ;
			}
			$("#stockOutIds").val(ids);
			$('#stockOut-modal').modal('show');
		});
        		
		//完结出库
		$("#endStockOut").on("click",function(){
			var url = serverAddr+"/sale/endStockOut.asyn";
			//获取选中的id信息
			var ids=$mytable.fn.getCheckedRow();
			if(ids==null||ids==''||ids==undefined){
				$custom.alert.warningText("请选择一条记录！");
				return ;
			}
			var idArr = ids.split(",");
			if(idArr.length >1){
				$custom.alert.warningText("只能选择一条记录！");
				return ;
			}
			$custom.request.ajax(serverAddr+"/sale/differenceRatio.asyn",{"ids":ids},function(data1){
				if(data1.state==200){
					$custom.alert.warning("销售订单【"+data1.data.orderCode+"】出库数量已达到"+data1.data.percent+"，是否完结出库?",function(){
						$custom.request.ajax(url,{"ids":ids},function(data){
							if(data.state==200){
								$custom.alert.success("完结出库成功");
								//重新加载页面
								$mytable.fn.reload();
							}else{
								if(!!data.expMessage){
									$custom.alert.error(data.expMessage);
								}else{
									$custom.alert.error(data.message);
								}
							}
						});
					});
				}else{
					if(!!data1.expMessage){
						$custom.alert.error(data1.expMessage);
					}else{
						$custom.alert.error(data1.message);
					}
				}
			});
		});
		//生成采购订单
		$("#createPurchase").on("click",function(){
			//获取选中的id信息
			var id=$mytable.fn.getCheckedRow();
			
			if(id==null||id==''||id==undefined || id.split(",").length > 1){
				$custom.alert.warningText("请选择一条记录！");
				return ;
			}
			$custom.request.ajax(serverAddr+"/sale/checkOrderState.asyn",{"id":id},function(data){
				if(data.state==200){
					$m9035.init(data.data);
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});
		
		$("#createDateStartDate,#shelfDateStartDate").each(function(){
	        laydate.render({
	        	elem: this, //指定元素
		        type: 'datetime',
		        format: 'yyyy-MM-dd HH:mm:ss',
		        ready: function () {
		            $('.laydate-btns-time').remove();
		        }
	        });
	    });
		$("#createDateEndDate,#shelfDateEndDate").each(function(){
	        laydate.render({
	        	elem: this, //指定元素
	        	type: 'datetime',
		        format: 'yyyy-MM-dd HH:mm:ss',
		        ready: function () {
		            $('.laydate-btns-time').remove();
		        },
		        done: function(value){
		            this.dateTime.hours = 23;
		            this.dateTime.minutes = 59;
		            this.dateTime.seconds = 59;
		        }
	        });
	    });
	   /*  laydate.render({
	        elem: '#createDateStartDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        }
	    });
	    laydate.render({
	        elem: '#createDateEndDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        done: function(value){
	            this.dateTime.hours = 23;
	            this.dateTime.minutes = 59;
	            this.dateTime.seconds = 59;
	        }
	    }); */

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
	$(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    });
    // 上架导入
   	$("#importShelf-buttons").click(function(){
   		$module.load.pageOrder("act=40018");
   	});

	// 销售复制
	$("#saleCopy-buttons").click(function(){
		var ids=$mytable.fn.getCheckedRow();
		if(!ids || ids.indexOf(",") > -1){
			$custom.alert.error("请选择一项复制");
			return ;
		}
		$module.load.pageOrder('act=40011&ids='+ids);
	});
	// 新增内部销售订单
	$("#saveOwnSaleOrder-buttons").click(function(){
		$custom.request.ajax(serverAddr+"/sale/showOwnPurchaseOrder.asyn",{"json":null},function(data1){
			if(data1.state==200){
				$m9034.init(data1.data);
			}else{
				if(!!data1.expMessage){
					$custom.alert.error(data1.expMessage);
				}else{
					$custom.alert.error(data1.message);
				}
			}
		});
	});
    // 新增
   	$("#save-buttons").click(function(){
   		$module.load.pageOrder('act=40011&pageSource=2')
   	});
    //删除
    function del(){
    	$custom.request.delChecked(serverAddr+'/sale/delSaleOrder.asyn');
    }
 	 //加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
 	 
	//导出
    $("#exportOrder").on("click",function(){    	
    	var jsonData=null;    	
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
			var jsonArray=$("#form1").serializeArray();
	        $.each(jsonArray, function(i, field){
	            if(field.value!=null&&field.value!=''&&field.value!=undefined){
	                jsonData=$json.setJson(jsonData,field.name,field.value);
	            }
	        });
		}
    	
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/sale/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/sale/exportSaleOrder.asyn";
        		var i =0;
            	if(!!form){
           		    for(var key in form){
           		    	if(i==0){
               	    		url +="?";
               	    	}else{
               	    		url +="&";
               	    	}
           		    	url +=key+"="+form[key];
           		    	i++;
           		    }
            	}
            	$downLoad.downLoadByUrl(url);
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
        });
    });
	//上架
	function saleShelf(id){
		$custom.request.ajax(serverAddr+"/sale/shelfIsEnd.asyn", {"id":id}, function(data){
			if(data.state==200){
				if(data.data){
					$custom.alert.error("待上架数量为0，无需进行上架操作，请查看详情。");
        			return;					
				}
				$module.load.pageOrder("act=40016&id=" + id);
			}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
		});
	}
	//附件管理
    function toAttachment(code){
    	$module.load.pageOrder("act=200001&codeid=" + code);
    }
    
    function saleOrderOut(id) {
		$custom.request.ajax(serverAddr+"/sale/checkOutDepotOrder.asyn",{"id":id},function(result){
			if(result.data.code!='00'){
				$custom.alert.error(result.data.message);
			}else{
				$module.load.pageOrder("act=40015&id=" + id);
			}
		});
	}
	//	金额调整、确认
	function amountAdjust(id,type){
		var url = serverAddr+"/sale/showOrderAmount.asyn";
		$custom.request.ajax(serverAddr+"/sale/checkExistFinanceClose.asyn",{"id":id},function(result){
			if(result.data.code =='00'){
				$custom.request.ajax(url,{"id":id},function(result1){
					if(result1.state==200){
						$m9036.init(result1.data,type);
					}else{
						if(!!result1.expMessage){
							$custom.alert.error(result1.expMessage);
						}else{
							$custom.alert.error(result1.message);
						}
					}
				});
			}else{
				$custom.alert.error(result.data.message);
			}
		});		
		
	}

	function showState(){		
		var selectObj=$('select[name="stateSelect"]');
		var jsonData=$module.constant.ajax($module.params.getConstantListByNameURL,{listName:"saleOrder_stateList"});
		
	    selectObj.empty();
       
       	width = "136px" ;         	
       	$(selectObj).removeClass("input_msg") ;
         
         $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;
         
         $(selectObj).addClass("selectpicker").addClass("show-tick") ;
         $(selectObj).attr("multiple","multiple") ;
         
         jsonData.forEach(function(o,i){
             selectObj.append("<option value='"+ o.key+"'>"+o.value+"</option>");
         });
         
         $(selectObj).selectpicker({width: width});
         $(selectObj).selectpicker({noneSelectedText : '请选择'});
		 $(selectObj).selectpicker('refresh');
		
		 $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
	     $(".selectpicker").prev().css({"z-index":"99"});
	     $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
         
	}
	
	function searchSaleOrder(){
		var statesIds = $('select[name="stateSelect"]').val();
		$('input[name="stateList"]').val(statesIds);
		$mytable.fn.reload();
	}
	
	//导出清关资料
    $("#exportCustomsInfo-buttons").click(function(){
    	var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        if(ids.indexOf(",")!=-1){
            $custom.alert.warningText("目前只支持单条数据导出！");
            return ;
        }
        
        var isSameArea = "";//是否同关区
        var outDepotId = "";//出仓id
        var inDepotId = "";//入仓 id
        var outCustomsId = "";//出仓关区
        var inCustomsId = "";//入仓同关区
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                isSameArea = row.isSameArea;//是否同关区
                outDepotId = row.outDepotId;//出仓id
                inDepotId = row.inDepotId;//入仓 id
                outCustomsId = row.outCustomsId;//出仓关区
                inCustomsId = row.inCustomsId;//入仓同关区
                break;
            }
        } 
        $m9037.init(isSameArea,outDepotId,inDepotId,outCustomsId,inCustomsId,serverAddr+"/sale/exportCustomsInfo.asyn",ids);
    });
	</script>