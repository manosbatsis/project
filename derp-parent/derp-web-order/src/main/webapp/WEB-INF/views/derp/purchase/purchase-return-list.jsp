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
								<li class="breadcrumb-item"><a href="#">采购退货订单</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">采购退货单号 :</span>
									<input class="input_msg" name="code" id="code"/>
									<span class="msg_title">出仓仓库 :</span>
									<select name="outDepotId" class="input_msg" id="outDepotId">
									</select>
									<span class="msg_title">供应商  :</span>
									<select name="supplierId" class="input_msg" id="supplierId">
										<option value="">请选择</option>
                                    </select>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">PO号 :</span>
									<input type="text" class="input_msg" name="poNo" id="poNo">
									<span class="msg_title">单据状态 :</span>
									<select class="input_msg" name="status" id="status">
									</select>
									<span class="msg_title">事业部 :</span>
									<select class="input_msg" name="buId" id="buId">
									</select>
								</div>
								<div class="form_item h35">
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
							<shiro:hasPermission name="purchase_return_add">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="selectTable();">
									新增
								</button>
						</shiro:hasPermission>
							<shiro:hasPermission name="purchase_return_del">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del();">删除</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purchase_return_export">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
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
								<th style="white-space: nowrap;" class="tc">采购退货订单号</th>
								<th style="white-space: nowrap;" class="tc">出仓仓库</th>
								<th style="white-space: nowrap;" class="tc">事业部</th>
								<th style="white-space: nowrap;" class="tc">公司</th>
								<th style="white-space: nowrap;" class="tc">PO号</th>
								<th style="white-space: nowrap;" class="tc">供应商</th>
								<th style="white-space: nowrap;" class="tc">创建时间</th>
								<th style="white-space: nowrap;" class="tc">单据状态</th>
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
				<jsp:include page="/WEB-INF/views/modals/2012.jsp" />
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
	</div>
</div>
<!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"purchaseReturnOrder_statusList");
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0]);

//根据供应商加载
$module.supplier.loadSelectData.call($("select[name='supplierId']")[0],"");

    $(document).ready(
        function() {
        	//根据入库仓库加载
            $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0], null, null);
            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url : serverAddr+'/purchaseReturn/listPurchaseReturnOrder.asyn?r=' + Math.random(),
                columns : [
                    { //复选框单元格
                        className : "td-checkbox",
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return '<input type="checkbox" class="iCheck">';
                        }
                    },
                    {data : 'code'},{data : 'outDepotName'},{data : 'buName'},{data : 'merchantName'},{data : 'poNo'},{data : 'supplierName'},
                    {data : 'createDate'},{data:'statusLabel'},
                    { //操作
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                        	var edit = "";
                        	// 当单据状态为“待审核”时，可操作项为“详情、编辑”；
                        	if (row.status == '001') {	// 待审核
								edit = '<shiro:hasPermission name="purchase_return_edit"><a href="javascript:edit('+ row.id + ')">编辑</a></shiro:hasPermission><br>'
							}
                        	if (row.isOutDepotAble == '1') {	// 打托出库
								edit = '<shiro:hasPermission name="purchase_return_outDepot"><a href="javascript:outDepot('+ row.id + ')">打托出库</a></shiro:hasPermission><br>'
							}
                            return edit +' <shiro:hasPermission name="purchase_return_details"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
                        }
                    }, ],
                formid : '#form1'
            };
            //每一行都进行 回调
            $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            };
            //生成列表信息
            $('#datatable-buttons').mydatatables();
        });
    
    function searchData() {
        $mytable.fn.reload();
    }
    //详情
    function details(id) {
        $module.load.pageOrder("act=30081&id=" + id);
    }
	//编辑
	function edit(id) {
		$module.load.pageOrder("act=30082&id=" + id);
	}
	//打托出库
	function outDepot(id) {
		$module.load.pageOrder("act=30084&id=" + id);
	}
	
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
    
    //删除
    function del(){
        $custom.request.delChecked(serverAddr+'/purchaseReturn/delPurchaseReturnOrder.asyn');
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
    });
    
    $("#export").on("click", function(){
    	var code = $("#code").val();
    	
    	if(!code){
    		code ="" ;
    	}
    	
    	var depotId = $("#outDepotId").val();
    	if(!depotId){
    		depotId = "" ;
    	}
    	
    	var supplierId = $("#supplierId").val();
    	if(!supplierId){
    		supplierId = "" ;
    	}
    	
    	var poNo = $("#poNo").val();
    	if(!poNo){
    		poNo = "" ;
    	}
    	
    	var status = $("#status").val();
    	if(!status){
    		status = "" ;
    	}
    	
    	var buId = $("#buId").val();
    	if(!buId){
    		buId = "" ;
    	}
    	
    	var createStartDate = $("#createStartDate").val();
    	if(!createStartDate){
    		createStartDate = "" ;
    	}
    	
    	var createEndDate = $("#createEndDate").val();
    	if(!createEndDate){
    		createEndDate = "" ;
    	}
        $custom.alert.warning("确定导出选中对象？",function(){
        	$downLoad.downLoadByUrl(serverAddr+"/purchaseReturn/exportPurchaseReturn.asyn?code="+code
            		+"&outDepotId="+depotId+"&supplierId="+supplierId+"&buId=" + buId
            		+"&poNo="+poNo+"&status="+status+"&createStartDate="+createStartDate+"&createEndDate="+createEndDate);
        });
    }) ;
    
</script>