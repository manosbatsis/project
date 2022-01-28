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
                                <li class="breadcrumb-item"><a href="#">应收发票库</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">发票编码 :</span>
                                    <input type="text" class="input_msg" name="invoiceNo">
                                    <span class="msg_title">开票人 :</span>
                                    <input type="text" class="input_msg" name="creater">
                                    <span class="msg_title">发票状态 :</span>
                                    <select class="input_msg" name="status"></select>
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">开票日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="invoiceStartDate" id="invoiceStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="invoiceEndDate" id="invoiceEndDate">
                                </div>
                            </div>
<%--                            <a href="javascript:" class="unfold">展开功能</a>--%>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="receiveBillInvoice_export">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="exportInvoice" value="导出" />
                            </shiro:hasPermission>
                            <shiro:hasPermission name="receiveBillInvoice_billExport">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                       id="exportBillInvoice" value="应收对账导出" />
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
                                <th>发票号</th>
                                <th>客户</th>
                                <th>开票人</th>
                                <th>开票日期</th>
                                <th>发票金额</th>
                                <th>关联应收账单</th>
                                <th>发票状态</th>
                                <th>发票类型</th>
                                <th>操作人</th>
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
                <jsp:include page="/WEB-INF/views/modals/13005.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13008.jsp" />
                <jsp:include page="/WEB-INF/views/modals/13010.jsp" />
                <!-- end row -->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"receiveBillInvoice_statusList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);

    laydate.render({
        elem: '#invoiceStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });

    laydate.render({
        elem: '#invoiceEndDate', //指定元素
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

    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/receiveBillInvoice/listReceiveBillInvoice.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = '<a href="javascript:viewInvoice(\''+row.invoiceNo+'\')">' + row.invoiceNo + '</a> ';
                        return html;
                    }
                },{data:'customerName'},{data:'creater'},{data:'invoiceDate'},
                {data:'invoiceAmount'},
                {
                    orderable: false,
                    width: "100px",
                    data:null,
                    render: function (data, type, row, meta) {
                        var codes = '';
                        var invoiceRelCodes = row.invoiceRelCodes;
                        var type = row.invoiceType;
                        if (invoiceRelCodes) {
                            var invoiceRelCodeArr = new Array(); //定义一数组
                            invoiceRelCodeArr = invoiceRelCodes.split(","); //字符分割
                            for (i=0; i<invoiceRelCodeArr.length; i++ ){
                                if (i<3) {
                                    codes += invoiceRelCodeArr[i]+"<br/>";
                                }
                            }
                            if (invoiceRelCodeArr.length > 3) {
                                codes += "……";
                            }
                            if (type == '0') {
                                var html = '<a href="javascript:$m13005.init('+row.id+')">' + codes + '</a>'
                                return html;
                            } else {
                                return codes;
                            }
                        }
                        return '';
                    }
                },
                {data:'statusLabel'},
                {data:'invoiceTypeLabel'},
                {data:'synchronizer'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = '';
                        if (row.status == '02' || row.status == '03') {
                            html += '<shiro:hasPermission name="receiveBillInvoice_view"><a href="javascript:selectTable('+ row.id + ',\''+row.invoiceNo+ '\',\'' + row.invoicePath +'\')">查看发票</a><br></shiro:hasPermission>'

                        }
                        if (row.status == '01') {
                            html += '<shiro:hasPermission name="receiveBillInvoice_upload"><a href="javascript:uploadInvoice(\''+row.id+'\')">上传盖章发票</a><br></shiro:hasPermission>'
                        }
                        html += '<shiro:hasPermission name="receiveBillInvoice_replace"><a href="javascript:replaceInvoice('+ row.id + ')">替换发票</a><br></shiro:hasPermission>'
                        return html;
                    }
                },
            ],
            formid:'#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            $('td:eq(5)', nRow).html(aData.currency + "&nbsp;" + aData.invoiceAmount);
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

    function replaceInvoice(id) {
        $module.load.pageOrder('act=14021&id='+id);
    }

    function uploadInvoice(id) {
        $module.load.pageOrder('act=14011&id='+id);
    }

    $('#exportInvoice').click(function () {
        var param = $("#form1").serialize();
        //导出
        var url = serverAddr+"/receiveBillInvoice/exportInvoice.asyn?"+param;
        $downLoad.downLoadByUrl(url);
    });

    $('#exportBillInvoice').click(function () {
        var param = $("#form1").serialize();
        //导出
        var url = serverAddr+"/receiveBillInvoice/exportBillInvoice.asyn?"+param;
        $downLoad.downLoadByUrl(url);
    });

    function viewInvoice(invoiceNo) {
        var url = serverAddr+"/receiveBillInvoice/preview/"+invoiceNo;
        window.open(url);
    }

</script>