<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<jsp:include page="/WEB-INF/views/common.jsp"/>
<style type="text/css">
    .title_text_new {
        line-height: 40px;
        font-size: 24px;
        font-weight: bold;
        text-align: center;
    }

    .info_box {
        padding: 10px;
    }

    table {
        border-collapse: collapse;
        border-spacing: 0;
    }

    td, th {
        padding: 0;
    }

    .pure-table {
        border-collapse: collapse;
        border-spacing: 0;
        empty-cells: show;
        border: 1px solid #cbcbcb;
        width: 95%;
        margin-top: 25px;
        text-align: center;
    }

    .pure-table caption {
        color: #000;
        font: italic 85%/1 arial, sans-serif;
        padding: 1em 0;
        text-align: center;
    }

    .pure-table td, .pure-table th {
        border-left: 1px solid #cbcbcb;
        border-width: 0 0 0 1px;
        font-size: inherit;
        margin: 0;
        overflow: visible;
        padding: .5em 1em;
    }

    .pure-table thead {
        background-color: #e0e0e0;
        color: #000;
        text-align: left;
        vertical-align: bottom;
    }

    .pure-table td {
        background-color: transparent;
    }

    .pure-table-bordered td {
        border-bottom: 1px solid #cbcbcb;
    }

    .pure-table-bordered tbody > tr:last-child > td {
        border-bottom-width: 0;
    }
    .text_font {
        font: 14px blod;
    }
    .long_span {
        word-break:normal;
        width: 250px;
        display:block;
        white-space:pre-wrap;
        word-wrap : break-word ;
        overflow: hidden ;
    }
    .a_right {
        float: right;
    }
    .totalDiv {
        float: right;
        padding: 10px;
    }
</style>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
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
                                        <li class="breadcrumb-item"><a href="#">收款管理</a></li>
                                        <li class="breadcrumb-item"><a href="#">To C应收账单列表</a></li>
                                        <li class="breadcrumb-item"><a href="javascript:;">应收详情</a></li>
                                    </ol>
                                </div>
                            </div>
                            <ul class="nav nav-tabs">
                                <li class="nav-item">
                                    <a href="#total" data-toggle="tab" class="nav-link active">应收汇总</a>
                                </li>

                                <li class="nav-item">
                                    <a href="#detail" data-toggle="tab" class="nav-link" onclick="loadItemTable();">应收详情</a>
                                </li>

                                <li class="nav-item">
                                    <a href="#attachment" data-toggle="tab" class="nav-link" id="attachmentTab">附件审批记录</a>
                                </li>

                                <li class="nav-item">
                                    <a href="#verify" data-toggle="tab" class="nav-link">核销记录</a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade show active table-responsive" id="total">
                                    <div class="title_text_new">${month}月${bill.customerName}应收账单</div>
                                    <div class="info_box" style="float: left;">
                                        <font style="color:blue;">事业部：${ bill.buName}</font>
                                    </div>
                                    <div class="info_box" style="float: right;margin-right: 20px;">
                                        <font style="color:blue;">账单创建日期：
                                            <fmt:formatDate value="${ bill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </font>
                                    </div>
                                    <div class="info_box" style="margin-top: 50px;">
                                        <div class="info_item text_font">
                                            <div class="info_text">
                                                <span>平台结算单：</span>
                                                <span>${bill.code}</span>
                                            </div>
                                            <div class="info_text">
                                                <span>平台：</span>
                                                <span>${bill.storePlatformName}</span>
                                            </div>
                                            <div class="info_text">
                                                <span>店铺：</span>
                                                <span>${bill.shopName}</span>
                                            </div>
                                        </div>
                                        <div class="info_item text_font">
                                            <div class="info_text">
                                                <span>运营类型：</span>
                                                <span>${bill.shopTypeName}</span>
                                            </div>
                                            <div class="info_text">
                                                <span>客户名称：</span>
                                                <span>${bill.customerName}</span>
                                            </div>
                                            <div class="info_text">
                                                <span>结算日期：</span>
                                                <span>${bill.settlementDate}</span>
                                            </div>
                                        </div>
                                        <div class="info_item text_font">
                                            <div class="info_text">
                                                <span >结算币种：</span>
                                                <span>${bill.settlementCurrency}</span>
                                            </div>
                                            <div class="info_text">
                                                <span >外部结算单号：</span>
                                                <span>${bill.externalCode}</span>
                                            </div>
                                            <div class="info_text">
                                                <span >发票编号：</span>
                                                <span>${bill.invoiceNo}</span>
                                            </div>
                                        </div>
                                        <div class="info_item text_font">
                                            <div class="info_text">
                                                <span >平台结算原币：</span>
                                                <span>${bill.oriCurrency}</span>
                                            </div>
                                        </div>

                                        <table class="pure-table pure-table-bordered">
                                            <tbody>
                                            <tr>
                                                <td colspan="6">应收款项</td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">项目</td>
                                                <td>数量</td>
                                                <td>金额</td>
                                                <td>币种</td>
                                                <td>金额(RMB)</td>
                                            </tr>
                                            <c:forEach items="${receiveMap}" var="item" varStatus="status">
                                                <tr>
                                                    <c:if test="${status.index == 0}">
                                                        <td rowspan="${receiveMap.size()}">商品销售收入</td>
                                                    </c:if>
                                                    <td>${item.project_name}</td>
                                                    <td>${item.totalNum }</td>
                                                    <td>${item.totalPrice }</td>
                                                    <td>${bill.settlementCurrency}</td>
                                                    <td>${item.totalRmbPrice}</td>
                                                </tr>
                                            </c:forEach>
                                            <c:forEach items="${deductionMap}" var="item" varStatus="status">
                                                <tr>
                                                    <td>${item.parentProjectName}</td>
                                                    <td>${item.projectName}</td>
                                                    <td>${item.totalNum }</td>
                                                    <td>${item.totalPrice }</td>
                                                    <td>${bill.settlementCurrency}</td>
                                                    <td>${item.totalRmbPrice}</td>
                                                </tr>
                                            </c:forEach>
                                            <tr>
                                                <td colspan="2">合计总额：${totalPriceLabel }</td>
                                                <td>${totalNum}</td>
                                                <td>${totalPrice}</td>
                                                <td>${bill.settlementCurrency }</td>
                                                <td>${totalRmbPrice}</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="tab-pane fade  table-responsive" id="detail">
                                    <div class="title_text_new">${month}月${bill.customerName}应收账单</div>
                                    <div class="info_box" style="float: left;">
                                        <font style="color:blue;">事业部：${ bill.buName}</font>
                                    </div>
                                    <div class="info_box" style="float: right;margin-right: 20px;">
                                        <font style="color:blue;">账单创建日期：<fmt:formatDate value="${ bill.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></font>
                                    </div>
                                    <div class="info_box" style="margin-top: 40px;">
                                        <div style="margin-top: 20px;">
                                            <ul class="nav nav-tabs">
                                                <li class="nav-item">
                                                    <a href="#receiveDetail" data-toggle="tab" class="nav-link active" onclick="loadItemTable()">应收明细</a>
                                                </li>

                                                <li class="nav-item">
                                                    <a href="#costDetail" data-toggle="tab" class="nav-link" onclick="loadCostItemTable();">费用明细</a>
                                                </li>
                                            </ul>
                                            <div class="tab-content">
                                                <div class="tab-pane fade show active table-responsive"
                                                     id="receiveDetail">
                                                    <div>
                                                        <table id="receive-detail-table" class="table table-striped" cellspacing="0" width="100%">
                                                            <thead>
                                                            </thead>
                                                            <tbody>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="tab-pane fade  table-responsive" id="costDetail">
                                                    <div>
                                                        <table id="receive-cost-table" class="table table-striped" cellspacing="0" width="100%">
                                                            <thead>
                                                            </thead>
                                                            <tbody>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane fade  table-responsive" id="attachment" style="overflow-x: hidden;">
                                    <div class="form-row mt20 attachDetail">
                                        <div class='col-12'>
                                            <div class="info-box">
                                                <div class="title_text">附件列表</div>
                                                <button type="button" style="margin-left: 20px; margin-top: 10px;"
                                                        class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                                        id="btn-file">上传附件
                                                </button>
                                                <form enctype="multipart/form-data" id="form-upload">
                                                    <input type="file" name="file" id="file"
                                                           class="btn-hidden file-import">
                                                </form>
                                                <div id="attachmentTable" ></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row mt20">
                                        <div class='col-12'>
                                            <div class="info-box">
                                                <div class="title_text">审批记录</div>
                                                <table class="table table-striped" cellspacing="0" width="100%">
                                                    <thead>
                                                    <tr>
                                                        <th style="white-space:nowrap;">审批类型</th>
                                                        <th style="white-space:nowrap;">提交人</th>
                                                        <th style="white-space:nowrap;">提交时间</th>
                                                        <th style="white-space:nowrap;">提交备注</th>
                                                        <th style="white-space:nowrap;">审批人</th>
                                                        <th style="white-space:nowrap;">审批时间</th>
                                                        <th style="white-space:nowrap;">审批结果</th>
                                                        <th style="white-space:nowrap;">审批备注</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:if test="${receiveBillAuditItems.size() ==0}">
                                                        <tr>
                                                            <td colspan="7" align="center">表中数据为空</td>
                                                        </tr>
                                                    </c:if>
                                                    <c:forEach var="item" items="${receiveBillAuditItems}"
                                                               varStatus="status">
                                                        <tr>
                                                            <td>${item.auditTypeLabel}</td>
                                                            <td>${item.submitter}</td>
                                                            <td><fmt:formatDate value="${item.submitDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                            <td>${item.submitRemark}</td>
                                                            <td>${item.auditor}</td>
                                                            <td><fmt:formatDate value="${item.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                            <td>${item.auditResultLabel}</td>
                                                            <td>${item.auditRemark}</td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane fade  table-responsive" id="verify" style="overflow-x: hidden;">
                                    <div class="form-row">
                                        <div class='col-12 info_box'>
                                            <div class="title_text">NC信息</div>
                                            <div class="info_item text_font ml15 mt10">
                                                <div class="info_text">
                                                    <span>NC单据号：</span>
                                                    <span>${bill.ncCode}</span>
                                                </div>
                                                <div class="info_text">
                                                    <span>NC单据状态：</span>
                                                    <span>${bill.ncStatusLabel}</span>
                                                </div>
                                                <div class="info_text">
                                                    <span>凭证编号：</span>
                                                    <span>${bill.voucherCode}</span>
                                                </div>
                                            </div>
                                            <div class="info_item text_font ml15">
                                                <div class="info_text">
                                                    <span>凭证状态：</span>
                                                    <span>${bill.voucherStatusLabel}</span>
                                                </div>
                                                <div class="info_text">
                                                    <span>凭证名称：</span>
                                                    <span>${bill.voucherName}</span>
                                                </div>
                                                <div class="info_text">
                                                    <span>凭证序号：</span>
                                                    <span>${bill.voucherIndex}</span>
                                                </div>
                                            </div>
                                            <div class="info_item text_font ml15">
                                                <div class="info_text">
                                                    <span>会计期间：</span>
                                                    <span>${bill.period}</span>
                                                </div>
                                                <div class="info_text"></div>
                                                <div class="info_text"></div>
                                            </div>
                                        </div>
                                        <div class='col-12 mt20'>
                                            <div class="title_text">核销记录</div>
                                            <table class="table table-striped" cellspacing="0" width="100%">
                                                <thead>
                                                <tr>
                                                    <th style="white-space:nowrap;">核销金额</th>
                                                    <th style="white-space:nowrap;">结算币种</th>
                                                    <th style="white-space:nowrap;">收款日期</th>
                                                    <th style="white-space:nowrap;">收款流水号</th>
                                                    <th style="white-space:nowrap;">核销时间</th>
                                                    <th style="white-space:nowrap;">核销人</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:if test="${verifyItems.size() ==0}">
                                                    <tr>
                                                        <td colspan="6" align="center">表中数据为空</td>
                                                    </tr>
                                                </c:if>
                                                <c:forEach var="item" items="${verifyItems}" varStatus="status">
                                                    <tr>
                                                        <td style="font: 13px bold">${item.price}</td>
                                                        <td style="font: 13px bold">RMB</td>
                                                        <td style="font: 13px bold"><fmt:formatDate value="${item.receiveDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                        <td style="font: 13px bold">${item.receiceNo}</td>
                                                        <td style="font: 13px bold"><fmt:formatDate value="${item.verifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                        <td style="font: 13px bold">${item.verifier}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-row m-t-50">
                                <div style="margin: 0 auto;">
                                    <c:if test="${pageSource == '1'}">
                                        <button type="button"
                                                class="btn btn-find waves-effect waves-light btn_default"
                                                id="add-buttons">添加补扣款
                                        </button>
                                    </c:if>
                                    <button type="button"
                                            class="btn btn-find waves-effect waves-light btn_default"
                                            id="cancel-buttons">返回
                                    </button>
                                    <c:if test="${pageSource == '1'}">
                                        <shiro:hasPermission name="toc_receiveBill_submitReceiveBill">
                                        <button type="button"
                                                class="btn btn-find waves-effect waves-light btn_default"
                                                id="submit-buttons">提交
                                        </button>
                                        </shiro:hasPermission>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end row -->
            </div>
        </div>
    </div>
    <!-- container -->
</div>

<script type="text/javascript">

    $(function () {

        //点击上传文件
        $("#btn-file").click(function () {
            var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
            $("#form-upload").empty();
            $("#form-upload").append(input);
            $("#file").click();
        });
        var code = "${bill.code}"
        var billId = "${bill.id}"
        //上传文件到后端
        $("#form-upload").on("change", '.file-import', function () {
            var formData = new FormData($("#form-upload")[0]);
            var billStatus = '${bill.billStatus}'
            $custom.request.ajaxUpload(serverAddr + "/attachment/uploadFiles.asyn?code=" + code, formData, function (result) {
                if (result.state == 200 && result.data && result.data.code == 200) {
                    $attachTable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
                        if (billStatus == '03' || billStatus =='04') {
                            $('td:eq(6)', nRow).find('a:eq(0)').remove();
                        }
                    };
                    $attachTable.fn.reload();
                    $custom.alert.success("上传成功");
                } else {
                    if (result.message) {
                        $custom.alert.success(result.message);
                    } else {
                        $custom.alert.error("上传失败");
                    }
                }
            });
        });

        //取消按钮
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder("act=14018");
        });

        $("#submit-buttons").click(function () {
            var id = "${bill.id}";
            $custom.request.ajax(serverAddr + "/toCReceiveBill/submitReceiveBill.asyn", {"id": id}, function (res) {
                if (res.state == '200') {
                    $custom.alert.success("提交成功！");
                    $module.load.pageOrder('act=14018');
                } else {
                    $custom.alert.error("保存失败！");
                }
            });
        });

        $("#add-buttons").click(function () {
            var id = "${bill.id}";
            $load.a($module.params.loadOrderPageURL + 'act=14024&id=' + id);
        });

    });

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var targetId = e.target.id;//获取事件触发元素id
        if (targetId == 'attachmentTab') {
            var code = "${bill.code}";
            var attachmentHtml = $('#attachmentTable').html();
            var billStatus = '${bill.billStatus}'
            if (!attachmentHtml) {
                $attachTable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
                    if (billStatus == '03' || billStatus =='04') {
                        $('td:eq(6)', nRow).find('a:eq(0)').remove();
                    }
                };

                $("#attachmentTable").createAttachTables(code);
            }

        }
    })

    function loadItemTable() {
        var thead = '<tr>'
            +'  <th>外部订单号</th>'
            +'  <th>商品货号</th>'
            +'  <th>商品名称</th>'
            +'  <th>销售数量</th>'
            +'  <th>结算费项</th>'
            +'  <th>母品牌</th>'
            +'  <th>结算金额（原币）</th>'
            +'  <th>结算汇率</th>'
            +'  <th>结算金额（RMB）</th>'
            +'  <th>类型</th>'
            +'</tr>';
        $('#receive-detail-table').find("thead").html(thead);
        loadReceiveBillDetail(${bill.id});
    }

    function loadCostItemTable() {
        var thead = '<tr>'
            +'  <th>外部订单号</th>'
            +'  <th>销售数量</th>'
            +'  <th>结算费项</th>'
            +'  <th>平台费项</th>'
            +'  <th>母品牌</th>'
            +'  <th>结算金额（原币）</th>'
            +'  <th>结算汇率</th>'
            +'  <th>结算金额（RMB）</th>'
            +'  <th>类型</th>'
            +'</tr>';
        $('#receive-cost-table').find("thead").html(thead);
        loadReceiveCostDetail(${bill.id});
    }

    //加载应收明细
    function loadReceiveBillDetail(billId) {
        dttable = $('#receive-detail-table').dataTable();
        dttable.fnClearTable(false); //清空一下table
        dttable.fnDestroy(); //还原初始化了的datatable
        var $table = $('#receive-detail-table');
        var _table = $table.dataTable({
            //自定义
            language: $mytable.lang,
            //是否允许用户改变表格每页显示的记录数
            // lengthChange: false,
            //启用服务器端分页
            serverSide: true,
            //禁用原生搜索
            searching: false,
            //允许多次初始化同一表格
            retrieve: true,
            //禁用排序
            "ordering": false, // 禁止排序
            //显示数字的翻页样式
            sPaginationType: "full_numbers",
            "dom": '<"top"rt><"bottom"lip><"clear">',
            //显示字段数据
            columns:[
                {data : 'externalCode'},{data : 'goodsNo'}, {data : 'goodsName'}, {data : 'num'}, {data : 'projectName'},
                {data : 'parentBrandName'}, {data : 'originalAmount'}, {data : 'settlementRate'}, {data : 'rmbAmount'}, {data : 'billTypeLabel'}
            ],
            //异步获取数据
            ajax:function(data, callback, settings){
                $custom.request.ajax(serverAddr+'/toCReceiveBill/listReceiveItem.asyn?r=' + Math.random(), {"billId" : billId,"begin":data.start,"pageSize":data.length},
                    function(result) {
                        //异常判断与处理
                        if (result.errorCode) {
                            $custom.alert.error("查询失败");
                            return;
                        }
                        //封装返回数据
                        var returnData = {};
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.data.total;//总记录数
                        returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data.list;
                        //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                        //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                        callback(returnData);
                    });
            },
        });
        //生成列表信息
        $('#receive-detail-table').mydatatables();
    }

    //加载费项明细
    function loadReceiveCostDetail(billId) {
        dttable = $('#receive-cost-table').dataTable();
        dttable.fnClearTable(false); //清空一下table
        dttable.fnDestroy(); //还原初始化了的datatable
        var $table = $('#receive-cost-table');
        var _table = $table.dataTable({
            //自定义
            language: $mytable.lang,
            //是否允许用户改变表格每页显示的记录数
            // lengthChange: false,
            //启用服务器端分页
            serverSide: true,
            //禁用原生搜索
            searching: false,
            //允许多次初始化同一表格
            retrieve: true,
            //禁用排序
            "ordering": false, // 禁止排序
            //显示数字的翻页样式
            sPaginationType: "full_numbers",
            "dom": '<"top"rt><"bottom"lip><"clear">',
            //显示字段数据
            columns:[
                {data : 'externalCode',"orderable":false}, {data : 'num'}, {data : 'projectName'},{data : 'platformProjectName'},
                {data : 'parentBrandName'}, {data : 'originalAmount'}, {data : 'settlementRate'}, {data : 'rmbAmount'},{data : 'billTypeLabel'}
            ],
            //异步获取数据
            ajax:function(data, callback, settings){
                $custom.request.ajax(serverAddr+'/toCReceiveBill/listReceiveCostItem.asyn?r=' + Math.random(), {"billId":billId,"begin":data.start,"pageSize":data.length},
                    function(result) {
                        //异常判断与处理
                        if (result.errorCode) {
                            $custom.alert.error("查询失败");
                            return;
                        }
                        //封装返回数据
                        var returnData = {};
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.data.total;//总记录数
                        returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data.list;
                        //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                        //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                        callback(returnData);
                    });
            },
        });
        //生成列表信息
        $('#receive-cost-table').mydatatables();
    }


</script>
