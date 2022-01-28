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
                                    <span class="msg_title">运营类型 :</span>
                                    <select class="input_msg" name="shopTypeCode"></select>

                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>,
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">结算状态 :</span>
                                    <select class="input_msg" name="settlementStatus"></select>
                                    <span class="msg_title">店铺 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;border:1px solid #D3D3D3">
                                        <select name="shopCode" class="input_msg goods_select2" id="shopCode">
                                            <option value="">请选择</option>
                                            <c:forEach items="${shopList }" var="shop">
                                                <option value="${shop.shopCode }">${shop.shopName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <span class="msg_title">账单月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="monthStart" id="monthStart"> -
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="monthEnd" id="monthEnd">
                                </div>
                            </div>
<%--                            <a href="javascript:" class="unfold">展开功能</a>--%>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="toC_temp_refresh">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="refreshBill">刷新</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="toC_temp_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="toC_temp_exportTemp">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportTempOrder">累计暂估应收导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="toC_temp_exportSummary">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportSummaryOrder">暂估汇总导出</button>
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
                                <th>应收月份</th>
                                <th>运营类型</th>
                                <th>事业部</th>
                                <th>平台</th>
                                <th>总暂估应收</th>
                                <th>已结算订单</th>
                                <th>剩余暂估应收</th>
                                <th>结算完成月份</th>
                                <th>结算状态</th>
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
    </div>
</div>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="settlementStatus"]')[0],"toctempbill_settlementStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="shopTypeCode"]')[0],"merchantShopRel_shopTypeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);

    var initYear;
    laydate.render({
        elem: '#monthStart', //指定元素
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
                    $("#monthStart").val(value);
                    $(".layui-laydate").remove();
                }
            }
            initYear = selectYear;
        }
    });

    laydate.render({
        elem: '#monthEnd', //指定元素
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
                    $("#monthEnd").val(value);
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
            url:serverAddr+'/tocTempReceiveBill/listToCTempReceiveBill.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    data: null,
                    width: '30px',
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data:'month'},
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
                },{data:'buName'},
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        var html = '';
                        if (row.storePlatformName) {
                            html += '<div>' + row.storePlatformName + '</div>';
                        }
                        if (row.shopTypeCode == '001' && row.shopName) {
                            html += '<br><div>' + row.shopName + '</div>';
                        }
                        return html;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        return '<span>' + row.totalReceiveNum + '</span><br>'  + row.currency + '&nbsp;' + row.totalReceiveAmount;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        return '<span>' + row.alreadyReceiveNum + '</span><br>'  + row.currency + '&nbsp;' + row.alreadyReceiveAmount;
                    }
                },
                {
                    orderable: false,
                    data: null,
                    width: '150px',
                    render: function (data, type, row, meta) {
                        var lastNum = row.totalReceiveNum-row.alreadyReceiveNum;
                        return '<span>' + lastNum + '</span><br>' + row.currency + '&nbsp;' + row.lastReceiveAmount;
                    }
                },
                {data:'settlementEndMonth'},
                {data:'settlementStatusLabel'},
                { //操作
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = '<shiro:hasPermission name="toC_temp_detail"><a href="javascript:detail(\''+row.id+'\')">详情</a></shiro:hasPermission>'
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
        $module.load.pageOrder('act=14016&id='+id);
    }

    //导出
    $("#exportOrder").on("click",function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        $custom.request.ajax(serverAddr+"/tocTempReceiveBill/getBillCount.asyn", {"ids" : ids}, function(data){
            //判断导出的数量是否超出10W
            if(data.state==200){
                var total = data.data.total;//总数
                if(total>150000){
                    $custom.alert.error("数据超过15W行，请缩小导出时间范围");
                    return;
                }
                //导出数据
                var url = serverAddr+"/tocTempReceiveBill/exportBill.asyn?ids=" + ids;
                $downLoad.downLoadByUrl(url);
            }else{
                if(!data.expMessage){
                    $custom.alert.error(data.expMessage);

                }else{
                    $custom.alert.error(data.message);
                }
            }
        });
    });//导出

    //累计暂估应收导出
    $("#exportTempOrder").on("click",function(){
        var ids=$mytable.fn.getCheckedRow();
        var formParams = {};
        var param = '';
        if (ids) {
            formParams.ids = ids;
            param = "ids=" + ids;
        } else {
            formParams = $("#form1").serializeArray()
            param = $("#form1").serialize();
        }

        $custom.request.ajax(serverAddr+"/tocTempReceiveBill/getBillCount.asyn", formParams, function(data){
            //判断导出的数量是否超出10W
            if(data.state==200){
                var total = data.data.total;//总数
                if(total>150000){
                    $custom.alert.error("数据超过15W行，请缩小导出时间范围");
                    return;
                }
                //导出数据
                var url = serverAddr+"/tocTempReceiveBill/exportTempBill.asyn?"+param;
                $downLoad.downLoadByUrl(url);
            }else{
                if(!data.expMessage){
                    $custom.alert.error(data.expMessage);
                }else{
                    $custom.alert.error(data.message);
                }
            }
        });

    });

    //暂估汇总导出
    $("#exportSummaryOrder").on("click",function(){
        var param = $("#form1").serialize();
        var url = serverAddr+"/tocTempReceiveBill/exportSummaryOrder.asyn?"+param;
        $downLoad.downLoadByUrl(url);
    });

    //刷新
    $("#refreshBill").on("click",function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        $custom.request.ajax(serverAddr+"/tocTempReceiveBill/refreshBill.asyn", {"ids" : ids}, function(data){
            //判断导出的数量是否超出10W
            if(data.state==200){
                $custom.alert.success("正在刷新，请稍后再查询");
                //重新加载页面
                setTimeout(function () {
                    $module.load.pageOrder('act=14015');
                }, 1000);
            }else{
                $custom.alert.error(data.message);
            }
        });
    });
</script>