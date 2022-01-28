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
                                <li class="breadcrumb-item"><a href="#">收款管理</a></li>
                                <li class="breadcrumb-item"><a href="#">To C应收账单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformCode" id="storePlatformCode">
                                    </select>
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>,
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">平台账单号 :</span>
                                    <input type="text" class="input_msg" name="code">
                                    <span class="msg_title">账单月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="month" id="month">
                                    <span class="msg_title">账单状态 :</span>
                                    <select class="input_msg" name="billStatus"></select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">发票编号：</span>
                                    <input type="text" class="input_msg" name="invoiceNo">
                                    <span class="msg_title">发票状态 :</span>
                                    <select class="input_msg" name="invoiceStatus"></select>
                                    <span class="msg_title">外部结算单号 :</span>
                                    <input type="text" class="input_msg" name="externalCode">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">会计期间：</span>
                                    <input type="text" class="input_msg" name="period">

                                </div>
                            </div>
<%--                            <a href="javascript:" class="unfold">展开功能</a>--%>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                                <shiro:hasPermission name="toc_receiveBill_export">
                                    <input type="button"
                                           class="btn btn-find waves-effect waves-light btn-sm"
                                           id="importBtn" value="导出" onclick="exportBill();"/>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_del">
                                    <input type="button"
                                           class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="del();" value="删除" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_add">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       onclick="add();" value="新增" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_invalidApply">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="invalidApply();" value="作废" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_open"><input type="button"
                                     class="btn btn-find waves-effect waves-light btn-sm"
                                     id="openView" value="开票" /></shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_exportBill">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="exportPDFBill()" value="导出PDF" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="toc_receiveBill_exportSummaryBill">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="exportSummaryBill()" value="导出结算单汇总" />
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
                                <th>运营类型</th>
                                <th>平台</th>
                                <th>应收金额</th>
                                <th>账单月份</th>
                                <th>账单状态</th>
                                <th>发票信息</th>
                                <th>外部结算单号</th>
                                <th>创建人</th>
                                <th>NC单据</th>
                                <th>凭证信息</th>
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
                <jsp:include page="/WEB-INF/views/modals/13013.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13014.jsp" />
                <jsp:include page="/WEB-INF/views/modals/16001.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13020.jsp" />
                <!-- end row -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $module.constant.getConstantListByNameURL.call($('select[name="billStatus"]')[0],"tocReceiveBill_billStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="invoiceStatus"]')[0],"receiveBill_tocInvoiceStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);

    var initYear;
    laydate.render({
        elem: '#month', //指定元素
        type: 'month',
        showBottom: false,
        ready: function(date){ // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
            initYear = date.year;
        },
        change: function(value, date, endDate){ // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
            var selectYear = date.year;
            var differ = selectYear-initYear;
            if (differ == 0) {
                if($(".layui-laydate").length){
                    $("#month").val(value);
                    $(".layui-laydate").remove();
                }
            }
            initYear = selectYear;
        }
    });

    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/toCReceiveBill/listTocReceiveBill.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    data: null,
                    width: '30px',
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '110px',
                    render: function (data, type, row, meta) {
                        return row.code + '<br>' + row.buName;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        var html = '';
                        if (row.shopTypeName) {
                            html += '<div>' + row.shopTypeName + '</div>';
                        }
                        if (row.customerName) {
                            html += '<br><div>' + row.customerName + '</div>';
                        }
                        return html;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        var html = '';
                        if (row.storePlatformName) {
                            html += '<div>' + row.storePlatformName + '</div>';
                        }
                        if (row.shopName) {
                            html += '<br><div>' + row.shopName + '</div>';
                        }
                        return html;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '100px',
                    render: function (data, type, row, meta) {
                        return 'CNY&nbsp;' + row.receivableAmount;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return formatDate(row.settlementDate, "yyyy-MM");
                    }
                },
                {data:'billStatusLabel'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        var invoiceNoHtml = '';
                        if (row.invoiceNo) {
                            invoiceNoHtml = '<shiro:hasPermission name="receiveBill_view"><a href="javascript:viewInvoice(\''+row.invoiceNo+'\')">'+row.invoiceNo+'</a><br></shiro:hasPermission>' ;
                        }

                        return invoiceNoHtml + row.invoiceStatusLabel;
                    }
                },
                {data:'externalCode'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return '<span>' + row.creater + '</span><br><span>'
                            + row.createDate + '</span>';
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {
                        var ncCode = '';
                        if (row.ncCode) {
                            ncCode = row.ncCode;
                        }
                        var ncStatus = '';
                        if (row.ncStatusLabel) {
                            ncStatus = row.ncStatusLabel;
                        }
                        return '<span style="color: blue;">' + ncCode + '</span><br><span>'
                            + ncStatus + '</span>';
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {
                        var voucherName = '';
                        if (row.voucherCode) {
                            voucherName = row.voucherName;
                        }
                        var voucherStatus = '';
                        if (row.voucherStatusLabel) {
                            voucherStatus = row.voucherStatusLabel;
                        }

                        var period = '';
                        if (row.period) {
                            period = row.period;
                        }
                        return '<span>' + voucherName + '</span><br><span>'
                            + voucherStatus + '</span><br><span>'
                            + period + '</span>';
                    }
                },

                { //操作
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        //待提交 —— 编辑页面
                        if (row.billStatus == '00') {
                            html += '<shiro:hasPermission name="toc_receiveBill_edit"><a href="javascript:edit(\''+row.id+'\')">编辑</a><br></shiro:hasPermission>' ;
                        }
                        //待审核/作废待审 —— 审核
                        if(row.billStatus == '01' ||row.billStatus == '05'){
                            html += '<shiro:hasPermission name="toc_receiveBill_audit"><a href="javascript:audit(\''+row.id+'\')">审核</a><br></shiro:hasPermission>' ;
                        }
                        //账单状态为待核销或部分核销、已核销的账单状态，且NC同步状态为未同步 —— 同步NC应收
                        if((row.billStatus == '02' || row.billStatus == '03' || row.billStatus == '04') && row.ncStatus == '10'){
                            html += '<shiro:hasPermission name="toc_receiveBill_submitNC"><a href="javascript:$m13013.init(\''+row.id+'\')">同步NC应收</a><br></shiro:hasPermission>' ;
                        }
                        //生成失败 —— 导出错误信息
                        if(row.billStatus == '08'){
                            html += '<shiro:hasPermission name="toc_receiveBill_error"><a href="javascript:exportError(\''+row.id+'\')">失败原因</a><br></shiro:hasPermission>' ;
                            html += '<shiro:hasPermission name="toc_receiveBill_add"><a href="javascript:reUpload(\''+row.id+'\')">重新上传</a><br></shiro:hasPermission>' ;
                        }
                        //除了生成中/生成失败 —— 详情
                        if(!(row.billStatus == '07' || row.billStatus == '08')){
                            html += '<shiro:hasPermission name="toc_receiveBill_detail"><a href="javascript:detail(\''+row.id+'\')">详情</a><br></shiro:hasPermission>'
                        }
                        //账单状态为待核销或部分核销账单状态 —— 核销
                        if(row.billStatus == '02' || row.billStatus == '03'){
                            html += '<shiro:hasPermission name="toc_receiveBill_verify"><a href="javascript:$m13020.init('+row.id+ ',\''+ row.code + '\','+ row.receivableAmount + ',\''+ row.customerName + '\',\'' + row.settlementCurrency + '\')">核销</a><br></shiro:hasPermission>' ;
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
        searchData() ;

        function searchData() {
            $mytable.fn.reload();
        }
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

    //新增
    function add() {
        $module.load.pageOrder("act=14019");
    }

    //详情
    function detail(id){
        $module.load.pageOrder('act=14023&id='+id);
    }

    //编辑
    function edit(id) {
        $module.load.pageOrder('act=14023&id='+id + '&pageSource=1');
    }

    //审核
    function audit(id) {
        $module.load.pageOrder('act=14025&id='+id);
    }

    //删除
    function del() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids = [];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
                if (!(row.billStatus == "00" || row.billStatus == "08")) {
                    $custom.alert.warningText("只能删除账单状态为待提交或者生成失败的应收账单！");
                    return ;
                }
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }

        $custom.alert.warning("确定删除选中对象？",function(){
            $custom.request.ajax(serverAddr + '/toCReceiveBill/delToCReceiveBill.asyn',{"ids":ids.join(",")},function(data){
                if(data.state==200){
                    $custom.alert.success("删除成功");
                    //删除成功，重新加载页面
                    $mytable.fn.reload();
                }else{
                    if(data.expMessage != null){
                        $custom.alert.error(data.expMessage);
                    }else{
                        $custom.alert.error(data.message);
                    }
                }
            });
        });
    }

    //导出
    function exportBill() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(!ids){
            $custom.alert.error("请选择导出的应收账单！");
            return ;
        }
        var str = ids.split(',');
        if(str.length>1){
            $custom.alert.warningText("只能选择一条记录导出！");
            return ;
        }
        //导出
        var url = serverAddr+"/toCReceiveBill/exportBill.asyn?id="+ids;
        $downLoad.downLoadByUrl(url);
    }

    function formatDate(date, fmt) {
        if (!date) {
            return '';
        }
        date = date;
        fmt=fmt || 'yyyy-MM-dd hh:mm:ss';
        if(Object.prototype.toString.call(date).slice(8,-1)!=='Date'){
            date=new Date(date)
        }
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
        }
        let o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        }
        for (let k in o) {
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
        return fmt
    }

    //导出失败原因
    function exportError(id) {
        var url = serverAddr+"/toCReceiveBill/downLoad.asyn?id="+id;
        $downLoad.downLoadByUrl(url);
    }

    //重新上传
    function reUpload(id) {
        $module.load.pageOrder("act=14019&id="+id);
    }

    //申请作废
    function invalidApply() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(!ids || ids.indexOf(",") > -1){
            $custom.alert.error("只能选择一单！");
            return ;
        }
        var billStatus = null;
        var code = null;
        var storePlatformName = null;
        var customerName = null;
        var billDate = null;
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                billStatus = row.billStatus;
                code = row.code;
                customerName = row.customerName;
                storePlatformName = row.storePlatformName;
                billDate = formatDate(row.settlementDate, "yyyy-MM-dd");
            }
        }
        if (billStatus != '02') {
            $custom.alert.error("仅对账单状态为”待核销“的结算单可操作作废申请！");
            return;
        }

        $m13014.init(ids, code, storePlatformName, billDate);
    }

    //开票
    $("#openView").click(function () {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var check = true;
        var ids = [];
        var customerIds = [];
        var billStatus = [];
        var invoiceStatus = [];
        var currencys = [];
        var billMonths = [];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
                customerIds.push(row.customerId);
                billStatus.push(row.billStatus);
                invoiceStatus.push(row.invoiceStatus);
                currencys.push(row.currency);
                billMonths.push(formatDate(row.settlementDate, "yyyy-MM"))
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }
        billStatus.forEach(function( val, index ) {
            if (!(val == "02" || val == '03' || val == '04')) {
                check = false;
                $custom.alert.warningText("开票的ToC应收账单状态只能为“待核销”、“部分核销”、“已核销”！");
                return ;
            }
        });
        if (check) {
            invoiceStatus.forEach(function( val, index ) {
                if (val != "00") {
                    check = false;
                    $custom.alert.warningText("开票的ToC应收账单开票状态只能为“待开票”！");
                    return ;
                }
            });
        }
        if (check) {
            var currency0 = null;
            currencys.forEach(function (val, index) {
                if (index == 0) {
                    currency0 = val;
                } else if (currency0 != val) {
                    check = false;
                    $custom.alert.warningText("仅能对同结算币种的ToC应收账单进行同时合并开票！");
                    return ;
                }
            });
        }
        if (check) {
            var customerId0 = null;
            customerIds.forEach(function (val, index) {
                if (index == 0) {
                    customerId0 = val;
                } else if (customerId0 != val) {
                    check = false;
                    $custom.alert.warningText("仅能对同客户的ToC应收账单进行同时合并开票！");
                    return ;
                }
            });
        }

        if (check) {
            var billMonth0 = null;
            billMonths.forEach(function (val, index) {
                if (index == 0) {
                    billMonth0 = val;
                } else if (billMonth0 != val) {
                    check = false;
                    $custom.alert.warningText("仅能对同账单月份的ToC应收账单进行同时合并开票！");
                    return ;
                }
            });
        }
        if (check) {
            $custom.request.ajax(serverAddr + '/toCReceiveBill/validateInfo.asyn',{"ids":ids.join(",")},function(data){
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
                    $m16001.init($module.params.loadOrderPageURL+"act=14027&ids="+ids.join(","), customerId0, "2");
                }
            });
        }

    });

    function exportPDFBill() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(!ids){
            $custom.alert.error("请选择导出的ToC应收账单！");
            return ;
        }
        //导出
        var url = serverAddr+"/toCReceiveBill/exportPDF.asyn?ids="+ids;
        $downLoad.downLoadByUrl(url);
    }

    function exportSummaryBill() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(!ids){
            $custom.alert.error("请选择导出的ToC应收账单！");
            return ;
        }
        //导出
        var url = serverAddr+"/toCReceiveBill/exportSummaryBill.asyn?ids="+ids;
        $downLoad.downLoadByUrl(url);
    }

    function viewInvoice(invoiceNo) {
        var url = serverAddr+"/receiveBillInvoice/preview/"+invoiceNo;
        window.open(url);
    }
</script>