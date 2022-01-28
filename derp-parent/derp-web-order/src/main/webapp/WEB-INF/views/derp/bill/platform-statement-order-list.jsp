<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!DOCTYPE html>
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
                                <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                <li class="breadcrumb-item"><a href="#">平台结算单</a></li>
                            </ol>
                        </div>
                    </div>
                    <ul class="nav nav-tabs">
						<shiro:hasPermission name="platformStatement_orderList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=14012');">平台结算单</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="platformCost_orderList"> 
								<li class="nav-item">
									<a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageOrder('act=14008');">平台费用单</a>
								</li>
				 		</shiro:hasPermission> 
		           	</ul>
		           	<div class="tab-content">
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">平台结算单号:</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="billCode" id="billCode">
                                    <span class="msg_title">客户:</span>
                                    <select class="input_msg" name="customerType" id="customerType"></select>
                                    <span class="msg_title">是否已开票 :</span>
                                    <select class="input_msg" name="isInvoice"></select>

                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">账单月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" name="month" id="month">
                                    <span class="msg_title">应收结算单号:</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="receiveCode" id="receiveCode">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
					<div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="platformStatement_invoice">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="invoiceBtn" value="开票" />
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platformStatement_export">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="exportBtn" value="导出" />
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platformStatement_flash">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="flashBtn" value="刷新" />
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platformStatement_generateBill">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="generateBtn" value="生成应收单" />
                            </shiro:hasPermission>
                        </div>
                    </div>

                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable"
                                           class="group-checkable" /> <span></span>
                                </label></th>
                                <th>平台结算单号</th>
                                <th>账单月份</th>
                                <th>客户</th>
                                <th>账单金额</th>
                                <th>是否已开票</th>
                                <th>发票号码</th>
                                <th>开票人</th>
                                <th>开票日期</th>
                                <th>是否创建应收</th>
                                <th>应收结算单号</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                	</div>
                </div>
                <!--======================Modal框===========================  -->
                <!-- end row -->
                <jsp:include page="/WEB-INF/views/modals/16001.jsp" />
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    //加载事业部
    $module.constant.getConstantListByNameURL.call($('select[name="customerType"]')[0],"platformStatement_customerTypeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="isInvoice"]')[0],"platformStatement_isInvoiceList",null);
   
    // 日期选择
    $(".date-input").datetimepicker({
         format: 'yyyy-mm',
            autoclose: true,
            todayBtn: true,
            startView: 'year',
            minView:'year',
            maxView:'decade',
            language:  'zh-CN',
    });
    
    $(document).ready(function() {
    	
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformStatementOrder/listPlatformStatementOrder.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },           
                {data:'billCode'},{data:'month'},{data:'customerName'},
                {	
                	data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        html += data.currency + " " + data.billAmount;
                        return html;
                    }
                },
                {data:'isInvoiceLabel'},
                {
                    data: null,
                    render: function (data, type, row, meta) {
                        if (row.invoiceNo) {
                            var invoiceNoHtml = '<shiro:hasPermission name="receiveBill_view"><a href="javascript:viewInvoice(\''+row.invoiceNo+'\')">'+row.invoiceNo+'</a><br></shiro:hasPermission>' ;
                            return  invoiceNoHtml;
                        }
                        return "";
                    }
                },
                {data:'invoiceDrawer'},
                {data:'invoiceDate'},
                {data:'isCreateReceiveLabel'},
                {data:'receiveCode'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        html += '<a href="javascript:details(\''+row.id+'\')">详情</a> ';
                        
                        if('3' != row.customerType){
                        	html += '<a href="javascript:toAttachment(\''+row.billCode+'\')">附件管理</a> ';
                        }else{
                        	html += '<a href="javascript:toAttachment(\''+row.billCode+ '_' + row.currency +'\')">附件管理</a> ';
                        }
                        
                        return html;
                    }
                },
            ],
            formid:'#form1'
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
    }) ;
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    }) ;



    function details(id){
        $module.load.pageOrder('act=14013&id='+id);
    }
    
  //附件管理
    function toAttachment(code){
        $module.load.pageOrder("act=200001&codeid=" + code);
    }
    
    //导出
    $("#exportBtn").click(function () {
    	var ids=$mytable.fn.getCheckedRow();
    	
    	if(ids){
    		url = serverAddr+"/platformStatementOrder/exportOrders.asyn?ids="+ids ;
    	}else{
    		var params = $("#form1").serialize() ;
        	url = serverAddr+"/platformStatementOrder/exportOrders.asyn?" +params ;
    	}
    	
    	$custom.alert.warning("确定导出选中对象？",function(){
    		$downLoad.downLoadByUrl(url) ;
        });
    }) ;
    
    $("#flashBtn").click(function () {
    	
    	var month = $("#month").val() ;
    	var customerType = $("#customerType").val() ;
    	var billCode = $("#billCode").val() ;
    	
    	if(!customerType){
            $custom.alert.error("请选择客户");
            return ;
        }
    	
    	if(!month){
    		$custom.alert.error("请选择账单月份");
    		return ;
    	}
    	
    	if(!billCode){
    		billCode = "" ;
    	}
    	
    	$custom.alert.warning("确定刷新该月份?",function(){
            $custom.request.ajax(serverAddr+'/platformStatementOrder/flashPlatformStatementOrders.asyn',
            		{"month":month , "customerType" : customerType, "billCode": billCode },function(result){
               console.log("log:"+result.state);
               if(result.state==200&&result.data.code=='00'){
                    $custom.alert.success("正在刷新，稍后点击【查询】，查询结果");
                }else{
                    $custom.alert.error(result.data.message);
                }
           });
    	});
    });

    $("#invoiceBtn").click(function () {
        var check = true;
        var ids = $mytable.fn.getCheckedRow();
        if (!ids || ids.indexOf(",") > -1) {
            $custom.alert.error("只能选择一单进行开票！");
            return ;
        }
        var customer = null;
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                if (row.isInvoice == '1') {
                    check = false;
                    $custom.alert.error("仅对标识为“未开票”的平台结算单进行开票！");
                    return ;
                }
                customer = row.customerId;
            }
        }
        if (check) {
            //查询平台结算单关联的应收账单是否已开票，若开票则无法开票
            $custom.request.ajax(serverAddr + '/platformStatementOrder/validateInfo.asyn',{"ids":ids},function(data){
                if(data.state!=200) {
                    if(data.expMessage != null){
                        $custom.alert.error(data.expMessage);
                    }else{
                        $custom.alert.error(data.message);
                    }
                    check = false;
                    return ;
                } else {
                    if (data.data.code == '01') {
                        $custom.alert.error(data.data.msg);
                        check = false;
                        return;
                    }
                }
                if (check) {
                    $m16001.init($module.params.loadOrderPageURL+"act=14014&ids="+ids, customer, "2");
                }
            });
        }
    });

    //生成应收单
    $("#generateBtn").click(function () {
        var check = true;
        var ids=$mytable.fn.getCheckedRow();
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        var index = 0;
        for (var i = 0; i < nTrs.length; i++) {
            if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                var row = $mytable.tableObj.fnGetData(nTrs[i])
                if (row.isCreateReceive != '0') {
                    check = false;
                    $custom.alert.error("勾选的记录必须为“未创建应收”！");
                    return;
                }
                if (index == 0) {
                    customerType = row.customerType;
                    shopCode = row.shopCode;
                    customerId = row.customerId;
                    buId = row.buId;
                    currency = row.currency;
                    month = row.month;
                } else {
                    if (customerType != row.customerType) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的平台！");
                        return;
                    }
                    if (shopCode != row.shopCode) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的店铺！");
                        return;
                    }
                    if (customerId != row.customerId) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的客户！");
                        return;
                    }
                    if (buId != row.buId) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的事业部！");
                        return;
                    }
                    if (currency != row.currency) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的币种！");
                        return;
                    }
                    if (month != row.month) {
                        check = false;
                        $custom.alert.error("勾选的记录必须为相同的月份！");
                        return;
                    }
                }
                index++;
            }
        }
        if(check && !ids){
            $custom.alert.error("请至少选择一单");
            return;
        }
        if (check) {
            var url = serverAddr+"/platformStatementOrder/saveToCReceiveBill.asyn"
            $custom.request.ajax(url,{"ids":ids},function(res){
                if (res.state == '200' && res.data.code == '00') {
                    $custom.alert.success("正在生成中！");
                    $module.load.pageOrder('act=14012');
                } else {
                    var message = res.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("生成失败！");
                    }
                }
            });
        }


    });

    function viewInvoice(invoiceNo) {
        var url = serverAddr+"/receiveBillInvoice/preview/"+invoiceNo;
        window.open(url);
    }
</script>