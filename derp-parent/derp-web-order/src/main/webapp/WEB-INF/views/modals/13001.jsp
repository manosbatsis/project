<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<style>
    .choose {
        color: red;
    }
</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal1" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
         aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content" style="width: 920px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">选择单据</h5>
                </div>
                <div class="modal-body">
                    <!-- 加载中图片 -->
                    <img id="loadingDiv" src="/resources/assets/images/loading.gif" width="180" height="180"
                         style="display: none;position:absolute; z-index:2; filter:alpha(opacity=60);opacity:0.3;top: 25%;left: 50%"
                         alt="加载中...">

                    <form id="order_form">
                        <div class="form-row">
                            <div class="col-6">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">客户<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <select class="form-control" name="customerId" id="queryCustomerId">
                                            <option value="">请选择</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="row">
                                    <label class="col-5 col-form-label " style="white-space:nowrap;">
                                        <div class="fr">单据类型<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <select class="form-control" name="orderType" id="orderTypeSelect">
                                        </select>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-6">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">PO单号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <textarea style="width: 240px;border: 1px solid #dadada;" rows="2" name="poNo" placeholder="多PO查询时以&字符或换行隔开"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">业务单据号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <textarea style="width: 240px;border: 1px solid #dadada;" rows="2" name="relCode" placeholder="多单号查询时以&字符或换行隔开"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-row mt20">
                            <div class="form-inline m0auto">
                                <div class=form-group>
                                    <button type="button" class="btn btn-info waves-effect waves-light fr"
                                            onclick='qureyData();'>查询
                                    </button>
                                    <button type="reset" class="btn btn-light waves-effect waves-light ml15"
                                            onclick="formReset();">重置</button>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="form-row mt20">
                        <div class="col-12">
                            <div style="display:inline-block">
                                <span>已选</span>
                                <span class="choose" id="chooseNum"></span>
                                <span>行记录，</span>
                                <span>PO</span>
                                <span class="choose" id="choosePoNum"></span>
                                <span>个，</span>
                                <span>数量</span>
                                <span class="choose" id="totalNum"></span>
                                <span>件，</span>
                                <span>金额</span>
                                <span class="choose" id="chooseCurrency"></span>
                                <span class="choose" id="totalAmount"></span>
                            </div>

                            <div id="isAddWornDiv" style="display:inline-block">
                                <input type="checkbox" id="isAddWorn" name="isAddWorn" value="0"><span class="choose">应收增加残损金额</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-row mt20">
                        <table id="datatable-buttons1" class="table table-bordered" cellspacing="0" width="100%">
                            <thead></thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <div class="form-row mt20">
                        <div class="form-inline m0auto">
                            <div class=form-group>
                                <button type="button" id="billSaveBtn" class="btn btn-info waves-effect waves-light fr"
                                        onclick='$m13001.save();'>生成应收单
                                </button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"
                                        onclick='$m13001.hide();'>取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
        <%--        <jsp:include page="/WEB-INF/views/modals/13006.jsp" />--%>
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m13001 = {
        init: function () {
            //加载客户的下拉数据
            $module.customer.loadSelectData.call($('select[id="queryCustomerId"]')[0]);
            $module.constant.getConstantListByNameURL.call($('select[name="orderType"]')[0], "receiveBill_orderTypeList", '1');
            $("#orderTypeSelect option[text='请选择']").remove();
            $('#chooseNum').html(0);
            $('#choosePoNum').html(0);
            $('#totalNum').html(0);
            $('#totalAmount').html(0);
            $('#chooseCurrency').html('');
            this.show();
        },
        list: function () {
            dttable = $('#datatable-buttons1').dataTable();
            dttable.fnClearTable(false); //清空一下table
            dttable.fnDestroy(); //还原初始化了的datatable
            //加载表格
            saleOrder();
        },
        show: function () {
            $($m13001.params.modalId).modal("show")
            this.list();
        },
        hide: function () {
            $($m13001.params.modalId).modal("hide");
        },
        save: function () {
            var isAddWorn = $("input[name=isAddWorn]:checked").val();
            var check = true;
            var orderType = null;
            var buId = null;
            var customerId = null;
            var currency = null;
            var orderList = []
            var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
            var index = 0;
            for (var i = 0; i < nTrs.length; i++) {
                if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                    var row = $mytable.tableObj.fnGetData(nTrs[i]);
                    if (index == 0) {
                        buId = row.buId;
                        customerId = row.customerId;
                        currency = row.currency;
                        orderType = row.orderType;
                    } else {
                        if (buId != row.buId) {
                            check = false;
                            $custom.alert.error("勾选的记录必须为相同的事业部！");
                            return;
                        }
                        if (customerId != row.customerId) {
                            check = false;
                            $custom.alert.error("勾选的记录必须为相同的客户！");
                            return;
                        }
                        if (currency != row.currency) {
                            check = false;
                            $custom.alert.error("勾选的记录必须为相同的币种！");
                            return;
                        }
                        if (orderType != row.orderType) {
                            check = false;
                            $custom.alert.error("勾选的记录必须为相同的单据类型！");
                            return;
                        }
                    }

                    var order = {};
                    order.relCode = row.relCode;
                    order.orderType = row.orderType;
                    order.poNo = row.poNo;
                    order.buId = row.buId;
                    order.currency = row.currency;
                    if (row.orderType == '1' && isAddWorn) {
                        order.isAddWorn = isAddWorn;
                    } else {
                        order.isAddWorn = '1';
                    }
                    orderList.push(order);
                    index++;
                }
            }
            if (orderList.length == 0) {
                check = false;
                $custom.alert.error("请至少选择一单");
                return;
            } else if (orderList[0].orderType == '2' && orderList.length > 1) {
                check = false;
                $custom.alert.error("当关联业务单类型为账单出库单时，仅能选择一单生成应收账单!");
                return;
            }
            if (check) {
                var json = {};
                json.orderList = orderList;
                var jsonStr = JSON.stringify(json);
                $m13001.billSave(jsonStr);
            }
        },
        billSave: function (jsonStr) {
            $("#billSaveBtn").attr('disabled', 'disabled');
            /**判断选中的业务单据是否已生成应收账单，若生成则提示已生成应收账单*/
            var validUrl = serverAddr + "/receiveBill/isExistReceiveBill.asyn"
            var saveUrl = serverAddr + "/receiveBill/saveReceiveBill.asyn"
            $custom.request.ajax(validUrl, {"json": jsonStr}, function (result) {
                if (result.state != '200') {
                    $custom.alert.warningText(result.message);
                    $("#billSaveBtn").attr('disabled', false);
                } else if (result.data.code != '00') {
                    $custom.alert.warningText(result.data.message);
                    $("#billSaveBtn").attr('disabled', false);
                } else { //保存账单并跳转到编辑页面
                    $("#signup-modal1").modal('hide');
                    $("#loadingDiv").show();// loading 显示
                    $custom.request.ajax(saveUrl, {"json": jsonStr}, function (res) {
                        var x = 0;
                        if (res.state == '200' && res.data.code == '00') {
                            $m13001.isBillSave(res.data.billCode, x);
                        } else {
                            $("#loadingDiv").hide();// loading 显示
                            var expMessage = res.expMessage;
                            if (expMessage != null && expMessage != '' && expMessage != undefined) {
                                $custom.alert.error(expMessage);
                            } else {
                                $custom.alert.error("保存失败！");
                            }
                        }
                    });
                }
            });

        },
        isBillSave: function (billCode, x) {
            if (x < 10) {
                setTimeout($custom.request.ajax(serverAddr + "/receiveBill/isBillSave.asyn", {"billCode": billCode}, function (res) {
                    if (res.state == '200' && res.data.code == '00') {
                        $('.modal-backdrop').remove();//去掉遮罩层
                        $("#loadingDiv").hide();// loading 显示
                        $module.load.pageOrder('act=14002&id=' + res.data.billId + '&pageSource=1');
                    } else {
                        x++;
                        setTimeout("$m13001.isBillSave('" + billCode + "'," + x + ")", 6000);
                    }
                }), 2000)
            } else {
                $("#loadingDiv").hide();// loading 显示
                $("#billSaveBtn").attr('disabled', false);
                $custom.alert.error("请联系系统管理员！");
            }
        },
        params: {
            formId: '#order_form',
            modalId: '#signup-modal1',
        }
    }

    function selectTable() {
        var thead = '<tr>'
            + ' <th>'
            + '<input type="checkbox" name="checkGroup-checkable" class="group-checkable" onclick="selectAll(this)" />'
            + '</th>'
            + '  <th>业务单据号</th>'
            + '  <th>客户</th>'
            + '  <th>PO号</th>'
            + '  <th>事业部</th>'
            + '  <th>销售金额</th>'
            + '  <th>数量</th>'
            + '  <th>单据类型</th>'
            + '</tr>';
        $('#datatable-buttons1').find("thead").html(thead);
        $m13001.init();
    }

    //原生的datatable
    function saleOrder() {
        var $table = $('#datatable-buttons1');
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
            columns: [
                { //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck" onclick="selectOrder()">';
                    }
                },
                {data: 'relCode'}, {data: 'customerName'}, {data: 'poNo'}, {data: 'buName'},
                {
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        return row.currency + ' ' + row.amount;
                    }
                },
                {data: 'num'}, {data: 'orderTypeLabel'}
            ],
            //异步获取数据
            ajax: function (data, callback, settings) {
                $custom.request.ajax(serverAddr + '/receiveBill/listAddOrder.asyn?r=' + Math.random(), getParams(data),
                    function (result) {
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
        $('#datatable-buttons1').mydatatables();
    }

    function qureyData() {
        $m13001.list();
    }

    function getParams(data) {
        var jsonData = null;
        var isAddWorn = $("input[name=isAddWorn]:checked").val();
        //设置开始页和每页最大记录数
        jsonData = $json.setJson(jsonData, "begin", data.start);
        jsonData = $json.setJson(jsonData, "pageSize", data.length);
        jsonData = $json.setJson(jsonData, "isAddWorn", isAddWorn);
        //设置过滤条件参数
        var formid = "#order_form";
        var jsonArray = $(formid).serializeArray();
        $.each(jsonArray, function (i, field) {
            if (field.value != null && field.value != '' && field.value != undefined) {
                jsonData = $json.setJson(jsonData, field.name, field.value);
            }
        });
        return JSON.parse(jsonData);
    }

    function selectOrder() {
        var check = true;
        var count = 0;
        var totalNum = 0;
        var totalAmount = 0;
        var currency = null;
        var poNoArr = [];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for (var i = 0; i < nTrs.length; i++) {
            if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                var num = row.num;
                var amount = row.amount;
                var poStr = row.poNo;
                totalNum = totalNum + parseInt(num);
                totalAmount = totalAmount + parseFloat(amount);
                count++;
                if (poStr) {
                    var poArr = poStr.split("&");
                    for (var poIndex = 0; poIndex < poArr.length; poIndex++) {
                        if (!poNoArr.includes(poArr[poIndex])) {
                            poNoArr.push(poArr[poIndex]);
                        }
                    }
                }
                if (!currency && row.currency) {
                    currency = row.currency;
                } else if (currency != row.currency) {
                    check = false;
                    $custom.alert.error("勾选的记录必须为相同的币种！");
                    $('#chooseNum').html(0);
                    $('#choosePoNum').html(0);
                    $('#totalNum').html(0);
                    $('#totalAmount').html(0);
                    $('#chooseCurrency').html('');
                    $(":checkbox", '#datatable-buttons1').prop("checked", false);
                    $('#datatable-buttons1 tbody tr').removeClass('success');
                    return;
                }
            }
        }
        if (check) {
            $('#chooseNum').html(count);
            $('#choosePoNum').html(poNoArr.length);
            $('#totalNum').html(totalNum);
            $('#totalAmount').html(totalAmount.toFixed(2));
            $('#chooseCurrency').html(currency);
        }

    }

    /**
     * 全选
     */
    function selectAll(obj) {
        if ($(obj).is(':checked')) {
            $(":checkbox", '#datatable-buttons1').prop("checked", $(obj).prop("checked"));
            $('#datatable-buttons1 tbody tr').addClass('success');
        } else {
            $(":checkbox", '#datatable-buttons1').prop("checked", false);
            $('#datatable-buttons1 tbody tr').removeClass('success');
        }
        selectOrder();
    }

    $("#orderTypeSelect").change(function () {
        var orderType = $("#orderTypeSelect").val();
        if (orderType != '1') {
            $("#isAddWorn").prop("checked", false);
            ;
            $("#isAddWornDiv").hide();
        } else {
            $("#isAddWornDiv").show();
        }
    });

    $("#isAddWorn").change(function () {
        qureyData();
    });


    function formReset(){
        $('#queryCustomerId').selectpicker('refresh');
        $("#order_form")[0].reset() ;
    }
</script>