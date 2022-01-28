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
								<li class="breadcrumb-item"><a href="#">销售退货订单</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">销退单号 :</span>
									<input class="input_msg" name="code" />
									<span class="msg_title">退出仓库 :</span>
									<select name="outDepotId" class="input_msg">
										<option value="">请选择</option>
										<c:forEach items="${depotBean }" var="depot">
											<option value="${depot.value }">${depot.label }</option>
										</c:forEach>
									</select>
									<span class="msg_title">客户  :</span>
									<select class="input_msg" id="customerId" name="customerId">
										<option value="">请选择</option>
									</select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">单据状态 :</span>
									<select class="input_msg" name="status">
									</select>
									<span class="msg_title">关联销售单号 :</span>
									<input type="text" class="input_msg" name="saleOrderRelCode">
									<span class="msg_title">退货类型 :</span>
									<select class="input_msg" name="returnType">
									</select>
								</div>
								<div class="form_item h35">
									<span class="msg_title">事业部 :</span>
                                     <select class="input_msg" name="buId" id="buId"></select>
									<span class="msg_title">创建时间 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="createStartDate" id="createStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createEndDate" id="createEndDate">
									</select>
								</div>
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a>  -->
						</div>
					</form>
					<div class="row col-12 mt20">
						<div class="button-list">
							<shiro:hasPermission name="saleReturn_add">
							<div class="btn-group">
								<button type="button"
										class="btn btn-find dropdown-toggle waves-effect waves-light btn-sm btn_default"
										data-toggle="dropdown" aria-expanded="false">
									新增 <span class="caret"></span>
								</button>
								<div class="dropdown-menu" aria-labelledby="btnGroupDrop1"
									 x-placement="bottom-start"
									 style="position: absolute; transform: translate3d(0px, 36px, 0px); top: 0px; left: 0px; will-change: transform;">
									<a class="dropdown-item" href="javascript:selectTable();">购销退货</a>
									<a class="dropdown-item" href="javascript:$module.load.pageOrder('act=50014');">非购销退货</a>
								</div>
							</div>
						</shiro:hasPermission>
							<shiro:hasPermission name="saleReturn_import">
								<div class="btn-group">
									<button type="button"
											class="btn btn-find dropdown-toggle waves-effect waves-light btn-sm btn_default"
											data-toggle="dropdown" aria-expanded="false">
										导入 <span class="caret"></span>
									</button>
									<div class="dropdown-menu" aria-labelledby="btnGroupDrop1"
										 x-placement="bottom-start"
										 style="position: absolute; transform: translate3d(0px, 36px, 0px); top: 0px; left: 0px; will-change: transform;">
								<!-- 		<a class="dropdown-item" href="javascript:$module.load.pageOrder('act=50012');">代销退货</a> -->
										<a class="dropdown-item" href="javascript:$module.load.pageOrder('act=50013');">消费者退货</a>
									</div>
								</div>
							</shiro:hasPermission>

							<shiro:hasPermission name="saleReturn_delSaleReturnOrder">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del();">删除</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="saleReturn_auditSaleReturnOrder">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="audit">审核</button>
							</shiro:hasPermission>
<!-- 							<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons-1">代销退货导入</button>
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons-2">消费者退货导入</button> -->
							<shiro:hasPermission name="saleReturn_export">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
							</shiro:hasPermission>
						</div>
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
								<th style="white-space: nowrap;" class="tc">销售退货订单号</th>
								<th style="white-space: nowrap;" class="tc">关联销售单号</th>
								<th style="white-space: nowrap;" class="tc">退出仓库</th>
								<th style="white-space: nowrap;" class="tc">退入仓库</th>
								<th style="white-space: nowrap;" class="tc">事业部</th>
								<th style="white-space: nowrap;" class="tc">客户</th>
								<th style="white-space: nowrap;" class="tc">服务类型</th>
								<th style="white-space: nowrap;" class="tc">计划退货数量</th>
								<th style="white-space: nowrap;" class="tc">单据状态</th>
								<th style="white-space: nowrap;" class="tc">创建时间</th>
								<th style="white-space: nowrap;" class="tc">退货类型</th>
								<th style="white-space: nowrap;" class="tc">操作</th>
							</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!--  ================================表格  end ===============================================   -->
				</div>
				<!--======================Modal框===========================  -->
				<jsp:include page="/WEB-INF/views/modals/9013.jsp" />
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
	</div>
</div>
<!-- content -->
<script type="text/javascript">
//加载客户的下拉数据
$module.customer.loadSelectData.call($('select[name="customerId"]')[0],"");
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"saleReturnOrder_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="returnType"]')[0],"saleReturnOrder_returnTypeList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, null,null);

    $(document).ready(
        function() {
        	$module.depot.loadSelectData.call($('select[name="outDepotId"]')[0]);
            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url : serverAddr+'/saleReturn/listSaleReturnOrder.asyn?r=' + Math.random(),
                columns : [
                    { //复选框单元格
                        className : "td-checkbox",
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return '<input type="checkbox" class="iCheck">';
                        }
                    },
                    {data : 'code'},{data : 'saleOrderRelCode'},{data : 'outDepotName'},{data : 'inDepotName'},{data : 'buName'},{data : 'customerName'},{data : 'serveTypesLabel'},{data : 'totalReturnNum'},{data : 'statusLabel'},{data : 'createDate'},{data:'returnTypeLabel'},
                    { //操作
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                        	var edit = "";
                        	// 当单据状态为“待审核”时，可操作项为“详情、编辑”；
                        	if (row.status == '001') {	// 待审核
								edit = '<shiro:hasPermission name="saleReturn_edit"><a href="javascript:edit('+ row.id + ')">编辑</a></shiro:hasPermission></br>'
							}
                        	// 当单据状态为“已审核”、“已入库”时，可操作项为“详情、出库打托”;注意：消费者退货的不用出库打托
                        	// 打托出库：出库仓库为仓库管理中设置为非下推接口，且不以入定出，才可有打托出库按钮操作权限
                        	if(row.returnType != '1' && (row.outBatchValidation=='0' || row.outBatchValidation == '2') && row.outDepotIsInDependOut=='0'){
                        		if (row.status == '003' || row.status == '012') {	
    								edit = '<shiro:hasPermission name="saleReturn_outDepotReport"><a href="javascript:outDepotReport('+ row.id + ')">出库打托</a></shiro:hasPermission></br>'
    							}
                        	}
                        	 if ((row.inBatchValidation == '0' || row.inBatchValidation == '2') && row.inDepotisOutDependIn == '0'&&(row.status == '003'|| row.status == '016' || row.status == '028')) {
                        		 edit=edit+ '<shiro:hasPermission name="saleReturn_in"><a href="javascript:saleReturnIn('+row.id+')">入库上架</a></shiro:hasPermission></br>';
                             }
                             if (row.status == '012' || row.status == '016' || row.status == '007') {
                            	 edit=edit+ '<shiro:hasPermission name="saleReturn_attachment"><a href="javascript:toAttachment(\''+row.code+'\')">附件</a></shiro:hasPermission></br>';
                             }
                             edit=edit+ '<shiro:hasPermission name="saleReturn_detail"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
                            return edit ;
                        }
                    }, ],
                formid : '#form1'
            };
          //每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	            if(aData.saleOrderRelCode != null){
	            	 var itemList = new Array();
	            	 if(aData.saleOrderRelCode.indexOf(",")!=-1){
	            		 itemList.push(aData.saleOrderRelCode.split(","));
		            	 for (var i = 0; i < itemList.length; i++) {
		            		// 如果只有一个关联销售号
		            		if(itemList[i].length==1){
		   	        			 $('td:eq(2)', nRow).html(aData.saleOrderRelCode);
		   	        		}else{	// 如果只有多个关联销售号
		   	        			// 显示关联销售号
		   	        			var length = aData.saleOrderRelCode.length;
		   	        			$('td:eq(2)', nRow).html("<div name='partSaleOrderRelCode'>"+aData.saleOrderRelCode.split(",")[0]+"</div><div name = 'showdiv' style='display:none;'>"+aData.saleOrderRelCode.substring(0,length-1)+"</div><a href='###' onclick='showHideCode(this)''>查看更多</a>");
		   	        		}
						}
	            	 }
	          	}
			};
            //生成列表信息
            $('#datatable-buttons').mydatatables();
        });
	// 出库打托
	function outDepotReport(id){
	    $custom.request.ajax(serverAddr+"/saleReturn/isOutdepotReport.asyn",{"id":id},function(result){
            if(result.data.code!='00'){
                $custom.alert.error(result.data.message);
            }else{
            	$module.load.pageOrder("act=50016&id=" + id);
            }
        });
	}
	
	  //入库上架
    function saleReturnIn(id) {
        var validUrl = serverAddr+"/saleReturn/isExistSaleReturnIdepot.asyn"
        $custom.request.ajax(validUrl,{"id":id},function(result){
            if(result.data.code!='00'){
                $custom.alert.warningText(result.data.message);
            }else{
                $module.load.pageOrder("act=50017&id="+id);
            }
        });
    }
	
    function searchData() {
        $mytable.fn.reload();
    }
    
    //附件管理
    function toAttachment(code){
        $module.load.pageOrder("act=200001&codeid=" + code);
    }
    //详情
    function details(id) {
        $module.load.pageOrder("act=50011&id=" + id);
    }
	//编辑
	function edit(id) {
		$module.load.pageOrder("act=50015&id=" + id);
	}
/*     //代销退货类型导入
    $("#import-buttons-1").click(function(){
        $module.load.pageOrder("act=50012");
    });
    //消费者退货类型导入(新加的功能)
    $("#import-buttons-2").click(function(){
        $module.load.pageOrder("act=50013");
    }); */
    laydate.render({
        elem: '#createStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
    laydate.render({
        elem: '#createEndDate', //指定元素
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
    // 点击审核按钮
    $("#audit").on("click",function(){
    	
    	// 定义一个url
        var url = serverAddr+"/saleReturn/auditSaleReturnOrder.asyn";
        //获取选中的id信息
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
		var checkInventoryGoods = [];
    	$custom.alert.warning("确定审核并提交？",function(){
			//获取选中订单的所有商品和对应数量（相同商品数量叠加）
			$custom.request.ajax(serverAddr+"/saleReturn/getOrderGoodsInfo.asyn",{"ids":ids},function(data){
				var flag = true;
				var map = data.data;
				// 循环比较库存
				var mergeJson = {};
				var badMergeJson = {};
				for(var key in map){
					var keys = key.split('-');
					var outDepotId = keys[1];
					var goodsId = keys[0];
					var outDepotCode = keys[2];
					var goodsNo = keys[3];
					var isSameArea = keys[4];		// 是否为同关区
					var returnType=keys[5];	// 退货类型
					var badGoodNum = keys[6];	// 坏品数量
					var num = map[key];				// 好品数量
					if(returnType==2){	// 代销退货
						checkInventoryGoods.push({"goodsId":goodsId, "goodsNo":goodsNo, "num":num, "badGoodNum":badGoodNum});
					}
				}
				for(var key in map){
					var keys = key.split('-');
					var outDepotId = keys[1];
					var goodsId = keys[0];
					var outDepotCode = keys[2];
					var goodsNo = keys[3];
					var isSameArea = keys[4];		// 是否为同关区
					var returnType=keys[5];	// 退货类型
					var badGoodNum = keys[6];	// 坏品数量
					var num = map[key];				// 好品数量
					if(returnType==2){	// 代销退货
						//合并相同项
						$.each(checkInventoryGoods, function (index, value) {
							var originalGoodsId = $module.inventoryLocationMapping.getOriginalGoodsId(value.goodsId, value.goodsNo) ;
							if (originalGoodsId) {
								if (!mergeJson[originalGoodsId]) {
									mergeJson[originalGoodsId] = value;
								} else {
									var num = mergeJson[originalGoodsId].num;
									value.num = parseInt(num) + parseInt(value.num);
									mergeJson[originalGoodsId] = value;
								}
							} else {
								mergeJson[value.goodsId] = value;
							}
							if (originalGoodsId) {
								if (!badMergeJson[originalGoodsId]) {
									badMergeJson[originalGoodsId] = value;
								} else {
									var badNum = badMergeJson[originalGoodsId].badGoodNum;
									value.badGoodNum = parseInt(badNum) + parseInt(value.badGoodNum);
									badMergeJson[originalGoodsId] = value;
								}
							} else {
								badMergeJson[value.goodsId] = value;
							}
						});
						// 如果是跨关区 是否同关区（0-否，1-是）
						if(isSameArea==0){
							if(num!=0){	//好品
								//查询可用量
								$.each(mergeJson, function (index, value) {
									var DepotData = $module.depot.getDepotById(outDepotId);
									var depotType = DepotData.type;
									var searchUnit = null;
									if (depotType == 'c') {
										if(tallyingUnit == "00"){//转换为库存的理货单位
											searchUnit = "0";//托盘
										}else if(tallyingUnit == "01"){
											searchUnit = "1";//箱
										}else if(tallyingUnit == "02"){
											searchUnit = "2";//件
										}
									}
									var goodAvailableNum =$module.inventory.queryAvailability(outDepotId,index,DepotData.code,searchUnit,0,null,null);
									// 判断好品库存量是否足够
									if(goodAvailableNum ==-1){
										flag = false;
										$custom.alert.warningText("商品货号："+goodsNo+",未查到好品库存可用量");
										return;
									}else if(value.num>goodAvailableNum){
										flag = false;
										$custom.alert.warningText("库存不足，商品货号："+goodsNo+",好品可用量："+goodAvailableNum);
										return;
									}
								});
							}
							if(badGoodNum!=0){
								//查询可用量
								$.each(mergeJson, function (index, value) {
									var DepotData = $module.depot.getDepotById(outDepotId);
									var depotType = DepotData.type;
									var searchUnit = null;
									if (depotType == 'c') {
										if(tallyingUnit == "00"){//转换为库存的理货单位
											searchUnit = "0";//托盘
										}else if(tallyingUnit == "01"){
											searchUnit = "1";//箱
										}else if(tallyingUnit == "02"){
											searchUnit = "2";//件
										}
									}
									// 查询坏品可用量(type=1)
									var badAvailableNum =$module.inventory.queryAvailability(outDepotId,index,DepotData.code,searchUnit,1,null,null);
									// 判断坏品库存量是否足够
									if(badAvailableNum!=null){
										if(badAvailableNum ==-1){
											flag = false;
											$custom.alert.warningText("商品货号："+goodsNo+",未查到坏品库存可用量");
											return;
										}else if(badGoodNum>badAvailableNum){
											flag = false;
											$custom.alert.warningText("坏品库存不足，商品货号："+goodsNo+",坏品可用量："+badAvailableNum);
											return;
										}
									}
								});
							}
						}else{	// 如果是同关区
							// 查询好品可用量(type=0)
							var goodAvailableNum = $module.inventory.queryAvailability(outDepotId,goodsId,outDepotCode,null,0,null,null);
							// 判断好品库存量是否足够
							if(goodAvailableNum ==-1){
								flag = false;
								$custom.alert.warningText("商品货号："+goodsNo+",未查到库存可用量");
								return;
							}else if(num>goodAvailableNum){
								flag = false;
								$custom.alert.warningText("库存不足，商品货号："+goodsNo+",可用量："+goodAvailableNum);
								return;
							}
						}
					}
				}
				if(!flag){
					return;
				}
				//执行审核
				$custom.request.ajax(url,{"ids":ids},function(data){
					if(data.state==200){
						var str = "成功："+data.data.success+"条";
						if(!!data.data.msg){
							str = "成功："+data.data.success+"条\n失败："+data.data.failure+"条\n失败原因：\n"+data.data.msg;
						}
						$custom.alert.success(str);
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
		});
    });
    //删除
    function del(){
        $custom.request.delChecked(serverAddr+'/saleReturn/delSaleReturnOrder.asyn');
    }
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
    })
    
    //导出
    $("#exportOrder").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value != null && field.value !="" && field.value != undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/saleReturn/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/saleReturn/exportSaleReturn.asyn";
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
    function showHideCode(obj){
   		$(obj).prev().toggle();
   		//被隐藏了
    	if($(obj).prev().is(":hidden")){
    		$(obj).prev().prev().show();
   		}else{
   			$(obj).prev().prev().hide();
   		} 
    }
</script>