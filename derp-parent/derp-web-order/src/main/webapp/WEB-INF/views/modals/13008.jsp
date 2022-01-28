<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="viewFile-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;left: -200px; top: 100px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">查看发票文件</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt10 ml15">
                                <div class="col-4">
                                    <div class="row">
                                        <div class="fr">&nbsp;源发票文件：
                                            <span id="invoiceNo"></span>
                                            <a href="###" id="viewInvoice">预览</a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4">

                                </div>
                                <div class="col-4">

                                </div>
                            </div>
                            <div class="form-row mt20">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="multi_down" value="批量下载"/>
                            </div>
                            <div class="form-row mt20">
                                <table id="attachment-table" class="table table-bordered" cellspacing="0" width="100%">
                                    <thead></thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-5"></div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13008.hide();'>返回</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">
    var $m13008={
        init:function(id, invoiceNo, invoicePath){
            $('#invoiceNo').html(invoiceNo);
            $m13008.params.id = id;
            $m13008.params.invoiceNo = invoiceNo;
            $m13008.params.invoicePath = invoicePath;
            $m13008.params.invoiceName = invoiceNo + '应收账单发票.pdf';
            this.show();
        },
        list:function(){
            dttable = $('#attachment-table').dataTable();
            dttable.fnClearTable(false); //清空一下table
            dttable.fnDestroy(); //还原初始化了的datatable
            //加载表格
            saleOrder();
        },
        show:function(){
            $($m13008.params.modalId).modal("show")
            this.list();
        },
        hide:function(){
            $($m13008.params.modalId).modal("hide");
        },
        params:{
            modalId:'#viewFile-modal',
            id:null,
            invoiceNo:null,
            invoicePath:null,
            invoiceName:null,
        }
    };

    $('#viewInvoice').click(function () {
        $("#viewInvoice").attr("href","javascript:viewInvoice(\'" + $m13008.params.invoiceNo+"\')");
    });

    /**
     * 全选
     */
    function selectAll(obj) {
        if($(obj).is(':checked')){
            $(":checkbox", '#attachment-table').prop("checked",$(obj).prop("checked"));
            $('#attachment-table tbody tr').addClass('success');
        }else{
            $(":checkbox", '#attachment-table').prop("checked",false);
            $('#attachment-table tbody tr').removeClass('success');
        }
    }

    function selectTable(id, invoiceNo, invoicePath){
        var thead = '<tr>'
            +' <th>'
            +'<input type="checkbox" name="checkGroup-checkable" class="group-checkable" onclick="selectAll(this)" />'
            +'</th>'
            +'  <th>文件名称</th>'
            +'  <th>上传时间</th>'
            +'  <th>上传人员</th>'
            +'  <th>操作</th>'
            +'</tr>';
        $('#attachment-table').find("thead").html(thead);
        $m13008.init(id, invoiceNo, invoicePath);
    }

    //原生的datatable
    function saleOrder(){
        var $table = $('#attachment-table');
        var _table = $table.dataTable({
            //自定义
            language: $mytable.lang,
            //是否允许用户改变表格每页显示的记录数
            lengthChange: false,
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
            //显示字段数据
            columns:[
                { //复选框单元格
                    className : "td-checkbox",
                    orderable : false,
                    data : null,
                    render : function(data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data : 'attachmentName'},{data : 'createDate'},{data : 'creatorName'},
                { //操作
                    orderable: false,
                    width: "120px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var operateHtml = '<a href="javascript:$attachTable.fn.preview(\''+row.attachmentUrl+'\')">预览</a> ';
                        return operateHtml;
                    }
                }
            ],
            //异步获取数据
            ajax:function(data, callback, settings){
                $custom.request.ajax(serverAddr + '/attachment/listAttachment.asyn?r='+Math.random() + '&code=' + $m13008.params.invoiceNo,{},
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
        $('#attachment-table').mydatatables();
    }

    $("#multi_down").click(function(){
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        var count = 0;
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                $attachTable.fn.download(row.attachmentUrl , row.attachmentName) ;
                count++;
            }
        }
        if (count == 0) {
            $custom.alert.error("请选择下载附件！");
            return false;
        }

    }) ;

    function viewInvoice(invoiceNo) {
        var url = serverAddr+"/receiveBillInvoice/preview/"+invoiceNo;
        window.open(url);
    }
</script>