<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                                <li class="breadcrumb-item"><a href="#">平台采购单</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">客户 :</span>
									<select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
                                    </select>
                                    <span class="msg_title">PO号:</span>
                                    <input class="input_msg" name="poNo"/>
                                    <span class="msg_title">下单日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="orderTimeStartDate" id="orderTimeStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="orderTimeEndDate" id="orderTimeEndDate">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">单据状态 :</span>
                                    <select  class="input_msg" name="status">
                                    </select>
                                    <span class="msg_title">平台状态 :</span>
                                    <select  class="input_msg" name="platformStatus">
                                    </select>
                                    <span class="msg_title">PR号:</span>
                                    <input class="input_msg" name="prNo"/>
                                </div>
                            </div>
                            
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <%-- <shiro:hasPermission name="platform_purchase_submit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="submitOrder">提交</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platform_purchase_reject">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="rejectOrder">驳回</button>
                            </shiro:hasPermission> --%>
                            <shiro:hasPermission name="platform_purchase_toSales">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="toSalesOrder">转销售订单</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platform_purchase_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">批量导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                            	<th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>PO号</th>
                                <th>客户</th>
                                <th>下单时间</th>
                                <th>入库时间</th>
                                <th>金额</th>
                                <th>数量</th>
                                <th>客户仓库</th>
                                <th>平台状态</th>
                                <th>单据状态</th>
                                <th>操作</th>
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
        </div>
        <!-- container -->
    </div>
<jsp:include page="/WEB-INF/views/modals/17001.jsp" />
<jsp:include page="/WEB-INF/views/modals/17002.jsp" />
</div> <!-- content -->
<script type="text/javascript">

	$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"platformPurchase_statusList",null);
	$module.constant.getConstantListByNameURL.call($('select[name="platformStatus"]')[0],"platformPurchase_platformStatusList",null);
	//加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
	
/* 	$("#submitOrder").click(function(){
		changeStatus("2") ;
	}) ; */
	
/* 	$("#rejectOrder").click(function(){
		changeStatus("1") ;
	}) ; */
	
	$("#toSalesOrder").click(function(){
		toSalesOrder() ;
	}) ;
	
	$("#exportOrder").click(function(){
		var url = serverAddr + "/platformPurchaseOrder/exportPlatformPurchaseOrder.asyn?" ;
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			url += "ids=" + ids;
		}else{
			url += $("#form1").serialize();
		}
		
		$custom.alert.warning("确定导出选中对象？",function(){
			$downLoad.downLoadByUrl(url) ;
        });
	}) ;
 
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformPurchaseOrder/listPlatformPurchaseOrder.asyn?r='+Math.random(),
            columns:[
            	{ //复选框单元格
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data:'poNo'},{data:'customerName'},{data:'orderTime'},{data:'deliverDate'},
                {data:null,
                	render:function(data, type, row, meta){
                		
                		var html = "" ;
                		
                		if(row.currency){
                			html += row.currency + " ";
                		}
                		
                		if(row.amount){
                			html += row.amount ;
                		}
                		
                		return html ;
                	}
                },
                {data:null,
                	render:function(data, type, row, meta){
                		var html = "" ;
                		
                		if(row.skusNum){
                			html += "sku个数：" + row.skusNum;
                		}
                		
                		html += "<br/>" ;
                		
                		if(row.num){
                			html += "数量：" + row.num;
                		}
                		
                		return html ;
                	}
                },{data:'platformDepotName'},{data:'platformStatusLabel'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width:70,
                    data: null,
                    render: function (data, type, row, meta) {
                    	return '<shiro:hasPermission name="platform_purchase_details"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){};
        //生成列表信息
        $('#datatable-buttons').mydatatables();
		
    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //详情
    function details(id){
    	$custom.request.ajax(serverAddr+"/platformPurchaseOrder/toDetailsPage.asyn",{"id":id},function(data){
			if(data.state==200){
				$m17002.init(data.data) ;
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
		}) ;
    }
  	
	// 时间插件
    laydate.render({
        elem: '#orderTimeStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
    laydate.render({
        elem: '#orderTimeEndDate', //指定元素
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
    
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
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
    });
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    });
    
    function toSalesOrder(){
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		
		$custom.request.ajax(serverAddr+"/platformPurchaseOrder/checkOrderToSales.asyn",{"ids":ids},function(data){
			if(data.state==200){
				
				if(data.data.status == '01'){
					$custom.alert.warning(data.data.msg, function(){
						$m17001.init(ids) ;
					});
				}else{
					$m17001.init(ids) ;
				}

        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
		}) ;
    }

/*     function changeStatus(status){
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		
		var msg = "" ;
		if(status == '1'){
			msg = "驳回" ;
		}else if(status == '2'){
			msg = "提交" ;
		}
		
		$custom.alert.warning("确定" + msg + "？",function(){
			$custom.request.ajax(serverAddr+"/platformPurchaseOrder/changeStatus.asyn",{"ids":ids, "status": status},function(data){
				if(data.state==200){
					$custom.alert.success("操作成功");
					
					setTimeout('$module.load.pageOrder("act=17001");', 500) ;
	        	}else{
	            	if(!!data.expMessage){
	            		$custom.alert.error(data.expMessage);

	            	}else{
	            		$custom.alert.error(data.message);

	            	}
	            }
			}) ;
		}) ;
    } */
</script>
