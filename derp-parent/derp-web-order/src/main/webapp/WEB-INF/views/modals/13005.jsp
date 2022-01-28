<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="signup-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;left: -200px; top: 100px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">关联应收单</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt20">
                                <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">账单编号</th>
                                        <th style="text-align: center;">业务单号</th>
                                        <th style="text-align: center;">PO单号</th>
                                        <th style="text-align: center;">事业部</th>
                                        <th style="text-align: center;">应收金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="data-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-5"></div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13005.hide();'>关闭</button>
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
    var $m13005={
        init:function(id){
            $("#data-tbody").empty();
            //查询关联应收账单
            $custom.request.ajax($m13005.params.listReceiveBillUrl, {"id":id}, function(result){
                if(result.state == 200) {
                    var list = result.data;
                    if(list){
                        var html = "" ;
                        var totalPrice = 0;
                        $(list).each(function(i, m) {
                            var poNo = "";
                            if (m.poNo) {
                                poNo = m.poNo;
                            }
                            html += '<tr>'
                                + '<td>' + m.code + '</td>'
                                + '<td>' + m.relCode + '</td>'
                                + '<td>' + poNo + '</td>'
                                + '<td>' + m.buName + '</td>'
                                + '<td>' + m.receivablePrice + '</td>'
                                + '</tr>';
                            totalPrice = (parseFloat(totalPrice) + parseFloat(m.receivablePrice)).toFixed(2);
                        });
                        html += '<tr>'
                            + '<td>合计</td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td>' + totalPrice + '</td>'
                            + '</tr>';
                        $("#datatable-detail").append(html);
                    }
                }
            });
            $($m13005.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13005.params.modalId).modal("hide");
        },
        params:{
            listReceiveBillUrl:serverAddr + '/receiveBillInvoice/listReceiveBills.asyn?r='+Math.random(),
            modalId:'#signup-modal',
        }
    };

</script>