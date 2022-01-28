<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
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
                                <li class="breadcrumb-item"><a href="#">电商订单</a></li>
                                <li class="breadcrumb-item"><a href="#">事业部补录列表</a></li>
                            </ol>
                        </div>
                    </div>
                <ul class="nav nav-tabs">
						<shiro:hasPermission name="order_orderList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link"  onclick="$module.load.pageOrder('act=4002');">发货订单管理</a>
							</li>
						</shiro:hasPermission> 
					<shiro:hasPermission name="order_orderReturnIdepotList"> 
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageOrder('act=4008');">退货订单管理</a>
							</li>
			 		</shiro:hasPermission> 
					<shiro:hasPermission name="order_businessUnitRecordList"> 
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link active"  class="nav-link" onclick="$module.load.pageOrder('act=40023');">事业部补录列表</a>
							</li>
			 		</shiro:hasPermission> 
	           	</ul>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">出仓仓库 :</span>
									<select name="depotId" class="input_msg" id="depotId">
										<option value="">请选择</option>
									</select>
									<span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformName" id="storePlatformName">
                                    </select>
                                  <span class="msg_title">店铺名称 :</span>
	                              <div style="min-width:136px;max-width: 635px;display: inline-block;border:1px solid #D3D3D3">
	                                  <select name="shopCode" class="input_msg goods_select2" id="shopCode">
	                                      <option value="">请选择</option>
	                                      <c:forEach items="${shopList }" var="shop">
	                                          <option value="${shop.shopCode }">${shop.shopName}</option>
	                                      </c:forEach>
			                          </select>
		                              </div>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">商品货号 :</span>
									<input type="text" class="input_msg" name="goodsNo" id="goodsNo">	
									<span class="msg_title">商品条码 :</span>
									<input type="text" class="input_msg" name="barcode" id="barcode">	
									<span class="msg_title">外部交易单号 :</span>
                                    <input type="text" class="input_msg" name="externalCode" id="externalCode">
								</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="order_businessUnitRecord_update">
						<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="batchUpdate-buttons">批量更新</button>
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
									<th>外部订单号</th>
									<th>平台</th>
									<th>店铺名称</th>
									<th>出仓仓库</th>
									<th></th>
									<th>商品货号</th>
									<th>商品条码</th>
									<th>商品名称</th>
									<th>发货时间</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!--  ================================表格  end ===============================================   -->
				</div>
				 <jsp:include page="/WEB-INF/views/modals/9041.jsp" />
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
		</div>
	</div>
	<!-- content -->
	<script type="text/javascript">
	$("button[type='reset']").click(function(){
	    $(".goods_select2").each(function(){
	       $(this).val("");
	   }); 
	    $("#shopCode").select2({
			 language:"zh-CN",
			 placeholder: '请选择',
			 multiple: false // 是否允许选择多个值
		});
	});
	//加载仓库的下拉数据
	$module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
	$module.constant.getConstantListByNameURL.call($('select[name="storePlatformName"]')[0],"storePlatformList",null);
	$(document).ready(function() {
	    $("#shopCode").select2({
			 language:"zh-CN",
			 placeholder: '请选择',
			 multiple: false // 是否允许选择多个值
		});
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/order/listBusinessUnitRecord.asyn?r=' + Math.random(),
				columns : [
						{ //复选框单元格
							className : "td-checkbox",
							orderable : false,
							data : null,
							render : function(data, type, row, meta) {
								return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.goodsId+'">';
							}
						},
						{data : 'externalCode'},{data : 'storePlatformNameLabel'},{data : 'shopName'},{data : 'depotName'},{data : 'depotId'},{data : 'goodsNo'},{data : 'barcode'},{data : 'goodsName'},{data : 'deliverDate'},
						 ],
				formid : '#form1'
			};
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	        	 $('td:eq(5)', nRow).html("<td style='display: none'>"+aData.depotId+"</td>");	// 存放仓库Id
			};
			//生成列表信息
			$('#datatable-buttons').mydatatables();
		});
    function searchData(){
    	// 查询支持100个
    	var goodsNos=$("#goodsNo").val();
    	var goodsNoStr=goodsNos.split(",");
    	if(goodsNoStr.length>100){
    		$custom.alert.warningText("货号查询仅支持100个商品货号");
    		return;
    	}
       	var barcodes=$("#barcode").val();
    	var barcodeStr=barcodes.split(",");
    	if(barcodeStr.length>100){
    		$custom.alert.warningText("条码查询仅支持100个商品条码");
    		return;
    	} 
        $mytable.fn.reload();
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

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    	// 更新
        $("#batchUpdate-buttons").click(function () {
        	//获取选中的id信息
    	 	var checkeds = $.find("input[name='item-checkbox']:checked") ;
        	
       		var infoArray = new Array() ;
       		var depotArray = new Array() ;	// 存放仓库
       		if(checkeds.length == 0){
       			$custom.alert.warningText("至少选择一条记录！");
       			return ;
       		} 
       		var selectDepotId=null;
    		$(checkeds).each(function(i , m){
    			var externalCodes=$(m).parent().next().html();	// 获取选中的外部交易单号
    			selectDepotId=$(m).parent().next().next().next().next().next().text();	// 获取选中单的仓库
    			var goodsIdInfo = $(m).val();// 获取选中的商品ID
    			var groupInfo=externalCodes+":"+goodsIdInfo;	// 拼接=外部交易单号：商品ID
    			infoArray.push(groupInfo);
    			depotArray.push(selectDepotId);
	   		}) ;
	   		var infoArrayStr = infoArray.join(",") ; 

	   		var depotStr=depotArray.join(",");
	   		var depotSplit=depotStr.split(",");
			/*var result = depotSplit.sort();
			for(var i=0;i<result.length-1;i++){
				  if(result[i]!=result[i+1]){	// 仓库不一样
					  $custom.alert.warningText("出仓仓库必须一致，请重新勾选");
					  return;
				  }	
			}	*/
	    	$('#info2').val(infoArrayStr);
	    	//加载事业部
            $module.businessUnit.getSelectBeanByManyDepot.call($("select[name='buId2']")[0],null,null,depotStr);
	    	$('#receive-modal').modal('show');
	    	$module.revertList.serializeListForm() ;
		    $module.revertList.isMainList = true ;    	
        });

	</script>