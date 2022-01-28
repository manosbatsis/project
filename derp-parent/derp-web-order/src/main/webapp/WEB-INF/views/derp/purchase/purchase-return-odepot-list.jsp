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
								<li class="breadcrumb-item"><a href="#">采购退货出库订单</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">采购退货单号 :</span>
									<input class="input_msg" name="purchaseReturnCode"  id="purchaseReturnCode"/>
									<span class="msg_title">出仓仓库 :</span>
									<select name="outDepotId" class="input_msg" id="outDepotId">
									</select>
									<span class="msg_title">出库清单号 :</span>
									<input class="input_msg" name="code" id="code"/>
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">PO号 :</span>
									<input type="text" class="input_msg" name="poNo" id="poNo">
									<span class="msg_title">单据状态 :</span>
									<select class="input_msg" name="status">
									</select>
									<span class="msg_title">供应商  :</span>
									<select name="supplierId" class="input_msg" id="supplierId">
										<option value="">请选择</option>
                                    </select>
								</div>
								<div class="form_item h35">
									<span class="msg_title">事业部 :</span>
									<select class="input_msg" name="buId" id="buId">
									</select>
									<span class="msg_title">出库时间 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="returnOutStartDate" id="returnOutStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="returnOutEndDate" id="returnOutEndDate">
									</select>
								</div>
							</div>
							<!-- <a href="javascript:" class="unfold">展开功能</a>  -->
						</div>
					</form>
					<div class="row col-12 mt20">
						<div class="button-list">
							<shiro:hasPermission name="purchase_return_odepot_import">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="toImportPage();">
									导入
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purchase_return_odepot_export">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purchase_return_odepot_audit">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="audit();">审核</button>
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
								<th style="white-space: nowrap;" class="tc">出库清单号</th>
								<th style="white-space: nowrap;" class="tc">PO号</th>
								<th style="white-space: nowrap;" class="tc">出仓仓库</th>
								<th style="white-space: nowrap;" class="tc">事业部</th>
								<th style="white-space: nowrap;" class="tc">公司</th>
								<th style="white-space: nowrap;" class="tc">供应商</th>
								<th style="white-space: nowrap;" class="tc">采购退货订单号</th>
								<th style="white-space: nowrap;" class="tc">出库时间</th>
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
				<!-- end row -->
			</div>
			<!-- container -->
		</div>
	</div>
</div>
<!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"purchaseReturnOrderOdepot_statusList");
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
                url : serverAddr+'/purchaseReturnOdepot/listPurchaseReturnOdepot.asyn?r=' + Math.random(),
                columns : [
                    { //复选框单元格
                        className : "td-checkbox",
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return '<input type="checkbox" class="iCheck">';
                        }
                    },
                    {data : 'code'},{data : 'poNo'},{data : 'outDepotName'},{data : 'buName'},{data : 'merchantName'},{data : 'supplierName'},
                    {data : 'purchaseReturnCode'},{data : 'returnOutDate'},{data:'statusLabel'},
                    { //操作
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return ' <shiro:hasPermission name="purchase_return_odepot_details"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
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
        $module.load.pageOrder("act=30091&id=" + id);
    }
    
    //导入
    function toImportPage() {
        $module.load.pageOrder("act=30092");
    }
    
  	//审核
    function audit() {
  		var url = serverAddr+"/purchaseReturnOdepot/auditPurchaseReturnOrder.asyn"
  		var ids=$mytable.fn.getCheckedRow();
		$custom.request.ajax(url, { "ids" : ids }, function(data){
			if(data.state==200){
				$custom.alert.success("操作成功");
				//重新加载页面
				setTimeout("$module.load.pageOrder('act=3009');", 1000);
			}else{
				if(!!data.expMessage){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
			}
		});
    }
	
    laydate.render({
        elem: '#returnOutStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
    laydate.render({
        elem: '#returnOutEndDate', //指定元素
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
    	
		var purchaseReturnCode = $("#purchaseReturnCode").val();
    	
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
    	
    	var createStartDate = $("#returnOutStartDate").val();
    	if(!createStartDate){
    		createStartDate = "" ;
    	}
    	
    	var createEndDate = $("#returnOutEndDate").val();
    	if(!createEndDate){
    		createEndDate = "" ;
    	}
        $custom.alert.warning("确定导出选中对象？",function(){
        	$downLoad.downLoadByUrl(serverAddr+"/purchaseReturnOdepot/exportPurchaseReturnOdepot.asyn?code="+code
            		+"&outDepotId="+depotId+"&supplierId="+supplierId+"&buId=" + buId
            		+"&poNo="+poNo+"&status="+status+"&returnOutStartDate="+createStartDate+"&returnOutEndDate="+createEndDate);
        });
    }) ;
    
</script>