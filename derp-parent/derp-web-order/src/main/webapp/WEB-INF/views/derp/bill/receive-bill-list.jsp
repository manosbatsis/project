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
                                <li class="breadcrumb-item"><a href="#">应收账单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">入账月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="billMonth" id="billMonth">
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='query()'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">开票状态 :</span>
                                    <select class="input_msg" name="invoiceStatus"></select>
                                    <span class="msg_title">应收账单号 :</span>
                                    <input type="text" class="input_msg" name="code">
                                    <span class="msg_title">来源业务单 :</span>
                                    <input type="text" class="input_msg" name="relCode">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">发票号码 :</span>
                                    <input type="text" required="" class="input_msg" name="invoiceNo">
                                    <span class="msg_title">账单状态 :</span>
                                    <select class="input_msg" name="billStatus"></select>
                                    <span class="msg_title">NC应收状态 :</span>
                                    <select class="input_msg" name="ncStatus"></select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">发票月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="invoiceMonth" id="invoiceMonth">
                                    <span class="msg_title">创建人 :</span>
                                    <input type="text" class="input_msg" name="creater">
                                    <span class="msg_title">会计期间 :</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="period" id="period">
                                </div>
                            </div>
<%--                            <a href="javascript:" class="unfold">展开功能</a>--%>
                            <input type="hidden" value="" name="ids" id="ids">
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                                <%--<input type="button"
                                       class="btn btn-find waves-effect waves-light btn-sm"
                                       id="importBtn" value="导出" />--%>
                                <shiro:hasPermission name="receiveBill_refresh">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       onclick="refresh()" value="刷新" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_add">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       onclick="selectTable();" value="新增" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_del">
                                    <input type="button"
                                       class="btn btn-find waves-effect waves-light btn-sm"
                                       onclick="del();" value="删除" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_open"><input type="button"
                                           class="btn btn-find waves-effect waves-light btn-sm"
                                                      id="openView" value="开票" /></shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_invalidApply">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="invalidApply()" value="申请作废" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_exportBill">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="exportBill()" value="导出PDF" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_exportExcelBill">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="exportExcelBill()" value="导出Excel" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_updateVoucher">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="updateVoucher()" value="更新凭证信息" />
                                </shiro:hasPermission>
                                <shiro:hasPermission name="receiveBill_addBill">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                           onclick="add()" value="创建应收" />
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
                                <th>应收账单号</th>
                                <th>客户</th>
                                <th>关联单号</th>
                                <th>应收金额</th>
                                <th>账单状态</th>
                                <th>账单月份</th>
                                <th>发票信息</th>
                                <th>NC单据</th>
                                <th>凭证信息</th>
                                <th>创建人</th>
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
                <jsp:include page="/WEB-INF/views/modals/13001.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13004.jsp" />
                <jsp:include page="/WEB-INF/views/modals/16001.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13007.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13011.jsp" />
                <!-- end row -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $module.constant.getConstantListByNameURL.call($('select[name="billStatus"]')[0],"receiveBill_billStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="invoiceStatus"]')[0],"receiveBill_invoiceStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="ncStatus"]')[0],"receiveBill_nvSynList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);

    var initYear;
    laydate.render({
        elem: '#billMonth', //指定元素
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
                    $("#billMonth").val(value);
                    $(".layui-laydate").remove();
                }
            }
            initYear = selectYear;
        }
    });

    laydate.render({
        elem: '#invoiceMonth', //指定元素
        type: 'month',
        showBottom: false,
        ready: function(date){ // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
            initYear = date.year;
        },
        change: function(value, date, endDate){ // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
            var selectYear = date.year;
            var differ = selectYear - initYear;
            if (differ == 0) {
                if ($(".layui-laydate").length) {
                    $("#invoiceMonth").val(value);
                    $(".layui-laydate").remove();
                }
            }
            initYear = selectYear;
        }
    });

    laydate.render({
        elem: '#period', //指定元素
        type: 'month',
        showBottom: false,
        ready: function (date) { // 控件在打开时触发，回调返回一个参数：初始的日期时间对象
            initYear = date.year;
        },
        change: function (value, date, endDate) { // 年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
            var selectYear = date.year;
            var differ = selectYear - initYear;
            if (differ == 0) {
                if ($(".layui-laydate").length) {
                    $("#period").val(value);
                    $(".layui-laydate").remove();
                }
            }
            initYear = selectYear;
        }
    });

    $(document).ready(function () {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/receiveBill/listReceiveBill.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
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
                {data:'customerName'},
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        var relCodesHtml = '<span>关联单号：</span><br>';
                        if (row.relCode) {
                            var itemList = new Array();
                            if (row.relCode.indexOf("&") != -1) {
                                itemList.push(row.relCode.split("&"));
                                relCodesHtml += "<div name='partRelCode'>" + row.relCode.split("&")[0] + "</div>" +
                                    "<div name = 'showRelCodes' style='display:none;'>" + row.relCode + "</div><a href='###' onclick='showHide(this)''>查看更多</a>";
                            } else {
                                relCodesHtml += '<div>' + row.relCode + '</div>';
                            }
                        }
                        var poNosHtml = '<span>PO：</span>';
                        if (row.poNo) {
                            var itemList = new Array();
                            if (row.poNo.indexOf("&") != -1) {
                                itemList.push(row.poNo.split("&"));
                                poNosHtml += "<div name='partRelCode'>" + row.poNo.split("&")[0] + "</div>" +
                                    "<div name = 'showRelCodes' style='display:none;'>" + row.poNo + "</div><a href='###' onclick='showHide(this)''>查看更多</a>";
                            } else {
                                poNosHtml += '<div>' + row.poNo + '</div>';
                            }
                        }
                        return poNosHtml + '<br>' + relCodesHtml;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '100px',
                    render: function (data, type, row, meta) {
                        return row.currency + '&nbsp;' + row.receivablePrice;
                    }
                },
                {data:'billStatusLabel'},
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return formatDate(row.creditDate, "yyyy-MM");
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {

                        var invoiceNoHtml = '';
                        if (row.invoiceNo) {
                            invoiceNoHtml = '<shiro:hasPermission name="receiveBill_view"><a href="javascript:viewInvoice(\''+row.invoiceNo+'\')">'+row.invoiceNo+'</a><br></shiro:hasPermission>' ;
                        }
                        var invoiceDateHtml = '';
                        if (row.invoiceDate) {
                            invoiceDateHtml = formatDate(row.invoiceDate, "yyyy-MM-dd") + '<br>';
                        }
                        return invoiceNoHtml + invoiceDateHtml + row.invoiceStatusLabel;
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
                        return '<span>' + ncCode + '</span><br><span>'
                            + row.ncStatusLabel + '</span>';
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {
                        var voucherName = '';
                        var period = '';
                        if (row.voucherCode) {
                            voucherName = row.voucherName;
                        }
                        if (row.period) {
                            period = row.period;
                        }
                        return '<span>' + voucherName + '</span><br><span>'
                            + row.voucherStatusLabel + '</span><br><span>'
                            + period + '</span>';
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '80px',
                    render: function (data, type, row, meta) {
                        return '<span>' + row.creater + '</span><br><span>'
                            + row.createDate + '</span>';
                    }
                },
                { //操作
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        if(row.billStatus == '00'){
                            html += '<shiro:hasPermission name="receiveBill_edit"><a href="javascript:edit(\''+row.id+'\')">编辑</a><br></shiro:hasPermission>' ;
                        }
                        if(row.billStatus == '01' ||row.billStatus == '05'){
                            html += '<shiro:hasPermission name="receiveBill_audit"><a href="javascript:audit(\''+row.id+'\')">审核</a><br></shiro:hasPermission>' ;
                        }
                        if(row.billStatus == '02' || row.billStatus == '03'){
                            if (row.invoiceNo) {
                                html += '<shiro:hasPermission name="receiveBill_verify"><a href="javascript:$m13004.init('+row.id+ ',\''+ row.code + '\','+ row.receivablePrice + ',\''+ row.invoiceNo + '\',\''+ row.customerName + '\',\'' + row.currency + '\')">核销</a><br></shiro:hasPermission>' ;
                            } else {
                                html += '<shiro:hasPermission name="receiveBill_verify"><a href="javascript:$m13004.init('+row.id+ ',\''+ row.code + '\','+ row.receivablePrice + ','+ row.invoiceNo + ',\''+ row.customerName + '\',\'' + row.currency + '\')">核销</a><br></shiro:hasPermission>' ;
                            }
                        }
                        if((row.billStatus == '02' || row.billStatus == '03' || row.billStatus == '04') && row.ncStatus == '10'){
                            html += '<shiro:hasPermission name="receiveBillInvoice_submitNC"><a href="javascript:$m13011.init(\''+row.id+'\')">同步NC应收</a><br></shiro:hasPermission>' ;
                        }
                        html += '<shiro:hasPermission name="receiveBill_detail"><a href="javascript:detail(\''+row.id+'\')">详情</a></shiro:hasPermission>'
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

    //详情
    function detail(id){
        $module.load.pageOrder('act=14002&id='+id);
    }

    //编辑
    function edit(id) {
        $module.load.pageOrder('act=14002&id='+id + '&pageSource=1');
    }

    //审核
    function audit(id) {
        $module.load.pageOrder('act=14005&id='+id);
    }

    //刷新
    function refresh() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids = [];
        var orderTypes = [];
        var billStatus = [];
        var check = true;
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                ids.push(row.id);
                orderTypes.push(row.orderType);
                billStatus.push(row.billStatus);
                if (!row.orderType) {
                    $custom.alert.warningText("手工创建的应收账单不能刷新！");
                    check = false;
                    return ;
                }
                if (row.billStatus != "00") {
                    $custom.alert.warningText("只能刷新账单状态为待提交的应收账单！");
                    check = false;
                    return ;
                }
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }
        if (check) {
            var refreshUrl = serverAddr+"/receiveBill/refreshReceiveBill.asyn"
            $custom.request.ajax(refreshUrl,{"id":ids.join(",")},function(res){
                if (res.state == '200' && res.data.code == '00') {
                    $custom.alert.success("正在刷新，稍后点击【查询】，查询结果")
                    $module.load.pageOrder('act=14001');
                } else {
                    var message = res.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("刷新失败！");
                    }
                }
            });
        }
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
                if (row.billStatus != "00") {
                    $custom.alert.warningText("只能删除账单状态为待提交的应收账单！");
                    return ;
                }
            }
        }
        if(ids.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }

        $custom.alert.warning("确定删除选中对象？",function(){
            $custom.request.ajax(serverAddr + '/receiveBill/delReceiveBill.asyn',{"ids":ids.join(",")},function(data){
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
    //开票
    $("#openView").click(function () {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();

        var ids=$mytable.fn.getCheckedRow();

        if (!ids) {
            $custom.alert.error("请至少选择一单");
            return;
        }

        var idList = ids.split(",");

        if(idList.length == 0){
            $custom.alert.error("请至少选择一单");
            return;
        }

        var check = true;
        var customerIds = [];
        var billStatus = [];
        var invoiceStatus = [];
        var currencys = [];
        var orderTypes = [];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                customerIds.push(row.customerId);
                billStatus.push(row.billStatus);
                invoiceStatus.push(row.invoiceStatus);
                currencys.push(row.currency);
                orderTypes.push(row.orderType);
            }
        }

        billStatus.forEach(function( val, index ) {
            if (idList.length == 1) {
                if (val == "05" || val == '06') {
                    check = false;
                    $custom.alert.warningText("开票的应收账单状态不能为“作废待审”、“已作废”！");
                    return ;
                }
            } else {
                if (!(val == "02" || val == '03' || val == '04')) {
                    check = false;
                    $custom.alert.warningText("合并开票时账单状态需为“待核销”、“部分核销”、“已核销”！");
                    return ;
                }
            }

        });
        if (check) {
            invoiceStatus.forEach(function( val, index ) {
                if (!(val == "00" || val == "02")) {
                    check = false;
                    $custom.alert.warningText("开票的应收账单开票状态只能为“待开票”、“已作废”！");
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
                    $custom.alert.warningText("仅能对同结算币种的应收账单进行同时合并开票！");
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
                    $custom.alert.warningText("仅能对同客户的应收账单进行同时合并开票！");
                    return ;
                }
            });
        }
        if (check) {
            $custom.request.ajax(serverAddr + '/receiveBill/validateInfo.asyn',{"ids":idList.join(",")},function(data){
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
                    $m16001.init($module.params.loadOrderPageURL+"act=14007&ids="+idList.join(","), customerId0, "2");
                }
            });
        }

    });

    //查看发票
    function viewInvoice(id) {
        var url = serverAddr+"/receiveBill/preview.asyn?id=" + id;
        window.open(url);
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
        var customerName = null;
        var billDate = null;
        var buName = null;
        var invoiceId = null;
        var receiveAmount = null;
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                billStatus = row.billStatus;
                code = row.code;
                invoiceId = row.invoiceId;
                customerName = row.customerName;
                buName = row.buName;
                receiveAmount = row.receivablePrice;
                billDate = formatDate(row.billDate, "yyyy-MM");
            }
        }
        if (billStatus != '02') {
            $custom.alert.error("仅对账单状态为“待核销”可操作作废！");
            return;
        }

        $m13007.init(ids, invoiceId, code, buName, receiveAmount);
        /*//检查对应的发票是否存在漏提交的应收账单 ——本期删除该校验（2020.10.25）
        var validUrl = serverAddr+"/receiveBill/isExistRelInvoice.asyn";
        $custom.request.ajax(validUrl,{"ids":ids},function(result){
            if (result.state != '200' ) {
                $custom.alert.warningText(result.message);
            } else if (result.data.code == '01'){
                $custom.alert.warningText(result.data.message);
                return;
            } else if (result.data.code == '02'){
                var msg = result.data.msg;
                $custom.alert.warning(msg,function(){
                    $m13007.init(ids, code, customerName, billDate);
                });
            } else if (result.data.code == '00') {
                $m13007.init(ids, code, customerName, billDate);
            }
        });*/

    }

    function showHide(obj){
        $(obj).prev().toggle();
        //被隐藏了
        if($(obj).prev().is(":hidden")){
            $(obj).html('查看更多');
            $(obj).prev().prev().show();
        }else{
            $(obj).html('收起');
            $(obj).prev().prev().hide();
        }
    }

    function exportBill() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(!ids){
            $custom.alert.error("请选择导出的应收账单！");
            return ;
        }
        //导出
        var url = serverAddr+"/receiveBillInvoice/download.asyn?ids="+ids;
        $downLoad.downLoadByUrl(url);
    }
    
    function exportExcelBill() {
        var param = $("#form1").serialize();
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();

        var url = serverAddr+"/receiveBill/exportReceiveBills.asyn?";
        var ids=$mytable.fn.getCheckedRow();
        if(ids){
            url += "ids=" + ids;
        }else{
            url += $("#form1").serialize();
        }
        $custom.alert.warning("确定导出？",function(){
            //导出

            $downLoad.downLoadByUrl(url);
        });
    }

    function updateVoucher() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择更新的账单！");
            return ;
        }
        $custom.request.ajax(serverAddr + '/receiveBill/updateVoucher.asyn',{"ids":ids},function(result){
            if(result.state==200){
                if (result.data.code = '00') {
                    $custom.alert.success("正在更新，请稍后！");
                    //重新加载页面
                    $mytable.fn.reload();
                } else {
                    $custom.alert.error(result.data.message);
                }
            }else{
                $custom.alert.error(result.message);
            }
        });
    }

    //创建应收
    function add(){
        $module.load.pageOrder('act=14026');
    }

    function query() {
        $mytable.tableObj=$("#datatable-buttons").dataTable();
        $mytable.dataTableOjb=$mytable.tableObj.api();
        $mytable.fn.reload();
    }

    function viewInvoice(invoiceNo) {
        var url = serverAddr+"/receiveBillInvoice/preview/"+invoiceNo;
        window.open(url);
    }
</script>