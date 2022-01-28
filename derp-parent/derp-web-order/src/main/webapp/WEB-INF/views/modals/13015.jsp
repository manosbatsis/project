<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<style>
    .choose {
        color: red;
    }
</style>
<div>
    <!-- Signup modal content -->
    <div id="advance-modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
         aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;" data-backdrop="static">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content" style="width: 920px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h5 class="modal-title" id="myLargeModalLabel">选择预收单</h5>
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
                                        <div class="fr">预收单号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        <input class="form-control" name="code" id="code">
                                    </div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="form-inline m0auto">
                                    <div class=form-group>
                                        <button type="button" class="btn btn-info waves-effect waves-light fr"
                                                onclick='queryData();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light ml15">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
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
                                        onclick='$m13015.save();'>确定
                                </button>
                                <button type="button" class="btn btn-light waves-effect waves-light ml15"
                                        onclick='$m13015.hide();'>取消
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
    var $m13015 = {
        init: function (buId, customerId, currency,verifyIds,receiveBillId) {
            var thead = '<tr>'
                + ' <th>'
                + '<input type="checkbox" name="checkGroup-checkable" class="group-checkable" />'
                + '</th>'
                + '  <th>预收单号</th>'
                + '  <th>销售单号</th>'
                + '  <th>PO号</th>'
                + '  <th>预收金额</th>'
                + '  <th>单据状态</th>'
                + '</tr>';
            $('#datatable-buttons1').find("thead").html(thead);

            this.params.buId = buId;
            this.params.customerId = customerId;
            this.params.currency = currency;
            this.params.verifyIds = verifyIds;
            this.params.receiveBillId = receiveBillId;
            this.show();

        },
        list: function () {
            dttable = $('#datatable-buttons1').dataTable();
            dttable.fnClearTable(false); //清空一下table
            dttable.fnDestroy(); //还原初始化了的datatable
            saleOrder();
        },
        show: function () {
            $($m13015.params.modalId).modal("show")
            this.list();
        },
        hide: function () {
            $($m13015.params.modalId).modal("hide");
        },
        save: function () {
            var ids=[];
            var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
            for (var i = 0; i < nTrs.length; i++) {
                if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                    var row = $mytable.tableObj.fnGetData(nTrs[i]);
                    ids.push(row.advanceItemId);
                }
            }
            if(ids.length == 0){
                $custom.alert.warningText("请选择一条记录！");
                return ;
            }

            for (var i = 0; i < nTrs.length; i++) {
                if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                    var row = $mytable.tableObj.fnGetData(nTrs[i]);

                    var receiveDate = '';

                    if (row.receiveDate) {
                        receiveDate = row.receiveDate;
                    }

                    var trObj = '<tr><td style="font: 13px bold">' +
                        '<input type="checkbox" class="iCheck" value="' + row.advanceItemId + '"></td>' +
                        '<td style="font: 13px bold">' + row.code + '</td>' +
                        '<td style="font: 13px bold">预收款单</td>' +
                        '<td style="font: 13px bold">' + receiveDate + '</td>' +
                        '<td style="font: 13px bold">' + row.amount + '</td>' +
                        '<td style="font: 13px bold">' + row.orderCode + '</td>' +
                        '<td style="font: 13px bold">' + row.verifier + '</td>' +
                        '<td style="font: 13px bold">' + formatDate(row.verifyDate, "yyyy-MM-dd hh:mm:ss") + '</td>' +
                        '</tr>';
                    $("#verify-table").append(trObj);
                }


            }
            if($('#verify-table tr:eq(1)').find("td").length == 1) {
                $("#verify-table tr:eq(1)").remove();
            }
            $("#advance-modal").modal('hide');
        },
        params: {
            formId: '#order_form',
            modalId: '#advance-modal',
            buId: '',
            customerId: '',
            currency: '',
            verifyIds:'',
            receiveBillId:''
        }
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
                        return '<input type="checkbox" class="iCheck" >';
                    }
                },
                {data: 'code'},{data: 'orderCode'}, {data: 'poNo'},
                {
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        return row.currency + ' ' + row.amount;
                    }
                },
               {data: 'billStatusLabel'}
            ],
            //异步获取数据
            ajax: function (data, callback, settings) {
                $custom.request.ajax(serverAddr + '/receiveBill/listBeVerifyAdvanceBill.asyn?r=' + Math.random(), getParams(data),
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

    function queryData() {
        $m13015.list();
    }

    function getParams(data) {
        var jsonData = null;
        //设置开始页和每页最大记录数
        jsonData = $json.setJson(jsonData, "currency", $m13015.params.currency);
        jsonData = $json.setJson(jsonData, "buId", $m13015.params.buId);
        jsonData = $json.setJson(jsonData, "customerId", $m13015.params.customerId);
        jsonData = $json.setJson(jsonData, "ids", $m13015.params.verifyIds);
        jsonData = $json.setJson(jsonData, "receiveBillId", $m13015.params.receiveBillId);
        jsonData = $json.setJson(jsonData, "begin", data.start);
        jsonData = $json.setJson(jsonData, "pageSize", data.length);
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

</script>