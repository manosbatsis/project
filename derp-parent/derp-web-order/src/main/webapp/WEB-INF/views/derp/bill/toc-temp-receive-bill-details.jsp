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
                                <li class="breadcrumb-item"><a href="#">To C暂估应收表</a></li>
                                <li class="breadcrumb-item"><a href="#">详情</a></li>
                            </ol>
                        </div>
                    </div>

                    <form id="detailForm">
                        <input type="hidden" parsley-type="text" class="input_msg"
                               name="billId" id="billId" value="${detail.id }">
                    </form>
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="info_item">
                            <div class="info_text">
                                <span>平台：</span>
                                <span>${detail.storePlatformName }</span>
                            </div>
                            <div class="info_text">
                                <span>客户：</span>
                                <span>${detail.customerName }</span>
                            </div>
                            <div class="info_text">
                                <span>应收状态：</span>
                                <span>${detail.settlementStatusLabel }</span>
                            </div>
                        </div>
                        <div class="info_item">
                            <div class="info_text">
                                <span>店铺名称：</span>
                                <span>${detail.shopName }</span>
                            </div>
                            <div class="info_text">
                                <span>应收月份：</span>
                                <span>${detail.month }</span>
                            </div>
                            <div class="info_text">
                                <span>暂估币种：</span>
                                <span>RMB</span>
                            </div>
                        </div>

                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt20">
                        <div class="w100">
                            <ul class="nav nav-tabs">
                                <li class="nav-item">
                                    <a href="#receiveDetail" data-toggle="tab" class="nav-link active" onclick="loadItemTable()">暂估应收核销明细</a>
                                </li>

                                <li class="nav-item">
                                    <a href="#costDetail" data-toggle="tab" class="nav-link" onclick="loadCostItemTable();">暂估费用核销明细</a>
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

                    <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-5">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!--======================Modal框===========================  -->
                <!-- end row -->

            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        loadItemTable();
    })

    function loadItemTable() {
        var thead = '<tr>'
            +'  <th>外部订单号</th>'
            +'  <th>系统订单号</th>'
            +'  <th>事业部</th>'
            +'  <th>商品货号</th>'
            +'  <th>商品名称</th>'
            +'  <th>销售数量</th>'
            +'  <th>暂估应收金额（RMB）</th>'
            +'  <th>退款金额（RMB）</th>'
            +'  <th>平台结算货款（RMB）</th>'
            +'  <th>平台结算货款（原币）</th>'
            +'  <th>结算标识</th>'
            +'  <th>结算日期</th>'
            +'  <th>结算单号</th>'
            +'</tr>';
        $('#receive-detail-table').find("thead").html(thead);
        loadReceiveBillDetail(${detail.id});
    }

    function loadCostItemTable() {
        var thead = '<tr>'
            +'  <th>外部订单号</th>'
            +'  <th>系统订单号</th>'
            +'  <th>事业部</th>'
            +'  <th>商品货号</th>'
            +'  <th>商品名称</th>'
            +'  <th>系统费项名称</th>'
            +'  <th>平台费项名称</th>'
            +'  <th>暂估费用金额（RMB）</th>'
            +'  <th>平台结算费用（RMB）</th>'
            +'  <th>平台结算费用（原币）</th>'
            +'  <th>结算日期</th>'
            +'  <th>结算单号</th>'
            +'  <th>核销状态</th>'
            +'</tr>';
        $('#receive-cost-table').find("thead").html(thead);
        loadReceiveCostDetail(${detail.id});
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
                {data : 'externalCode'},{data : 'orderCode'},{data : 'buName'},{data : 'goodsNo'}, {data : 'goodsName'},
                {data : 'saleNum'}, {data : 'temporaryRmbAmount'},{data : 'refundAmount'},
                {data : 'settlementRmbAmount'},
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {
                        var originalCurrency = '';
                        if (row.originalCurrency) {
                            originalCurrency = row.originalCurrency;
                        }
                        var settlementOriAmount = '';
                        if (row.settlementOriAmount) {
                            settlementOriAmount = row.settlementOriAmount;
                        }
                        return '<span>' + originalCurrency + '&nbsp;'
                            + settlementOriAmount + '</span>';
                    }
                },
                {data : 'settlementMarkLabel'},
                {data : 'settlementDate'},{data : 'settlementCode'}
            ],
            //异步获取数据
            ajax:function(data, callback, settings){
                $custom.request.ajax(serverAddr+'/tocTempReceiveBill/listToCTempReceiveItem.asyn?r=' + Math.random(), {"billId" : billId,"begin":data.start,"pageSize":data.length},
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
                {data : 'externalCode'},{data : 'orderCode'},{data : 'buName'},{data : 'goodsNo'}, {data : 'goodsName'},
                {data : 'projectName'}, {data : 'platformProjectName'}, {data : 'temporaryRmbCost'},{data : 'settlementRmbCost'},
                {
                    orderable: false,
                    data: null,
                    width: '120px',
                    render: function (data, type, row, meta) {
                        var originalCurrency = '';
                        if (row.originalCurrency) {
                            originalCurrency = row.originalCurrency;
                        }
                        var settlementOriCost = '';
                        if (row.settlementOriCost) {
                            settlementOriCost = row.settlementOriCost;
                        }
                        return '<span>' + originalCurrency + '&nbsp;'
                            + settlementOriCost + '</span>';
                    }
                },
                {data : 'settlementDate'},{data : 'settlementCode'},{data : 'settlementMarkLabel'}
            ],
            //异步获取数据
            ajax:function(data, callback, settings){
                $custom.request.ajax(serverAddr+'/tocTempReceiveBill/listToCTempReceiveCostItem.asyn?r=' + Math.random(), {"billId":billId,"begin":data.start,"pageSize":data.length},
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

    //取消按钮
    $("#cancel-buttons").click(function(){
        $module.load.pageOrder('act=14015');
    });
</script>