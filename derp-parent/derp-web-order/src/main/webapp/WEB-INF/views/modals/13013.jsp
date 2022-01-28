<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="synNC-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;left: -200px; top: 100px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" onclick="$m13013.hide()">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">同步确认</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt20">
                                <span>确认将本次开票应收信息同步至NC：</span>
                                <table id="synToCNc-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">账单编号</th>
                                        <th style="text-align: center;">事业部</th>
                                        <th style="text-align: center;">结算类型</th>
                                        <th style="text-align: center;">销售模式</th>
                                        <th style="text-align: center;">母品牌</th>
                                        <th style="text-align: center;">收支项目名称</th>
                                        <th style="text-align: center;">应收金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="synTocNc-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-2"></div>
                                        <div class="col-8">
                                            <button type="button" class="btn btn-info waves-effect waves-light btn_default btn-sm" onclick='synTocNC();'>同步</button>
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13013.hide();'>取消</button>
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
    var $m13013={
        init:function(id){
            $m13013.params.id = id;
            $("#synTocNc-tbody").empty();
            //查询关联应收账单
            $custom.request.ajax($m13013.params.listReceiveBillUrl, {"ids":id}, function(result){
                if(result.state == 200) {
                    var list = result.data;
                    if(list){
                        var html = "" ;
                        var totalPrice = 0;
                        $(list).each(function(i, m) {
                            var parentBrandName = '';
                            if (m.parentBrandName) {
                                parentBrandName = m.parentBrandName;
                            }
                            var price = 0;
                            if (m.price) {
                                price = m.price;
                            }
                            html += '<tr>'
                                + '<td>' + m.billCode + '</td>'
                                + '<td>' + m.buName + '</td>'
                                + '<td>' + m.settlementTypeLabel + '</td>'
                                + '<td>' + m.saleModelLabel + '</td>'
                                + '<td>' + parentBrandName + '</td>'
                                + '<td>' + m.paymentSubjectName + '</td>'
                                + '<td>' + price + '</td>'
                                + '</tr>';
                            totalPrice = (parseFloat(totalPrice) + parseFloat(price)).toFixed(2);
                        });
                        html += '<tr>'
                            + '<td>合计</td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td>' + totalPrice + '</td>'
                            + '</tr>';
                        $("#synToCNc-detail").append(html);
                    }
                    $($m13013.params.modalId).modal({backdrop: 'static', keyboard: false});
                }
            });

        },
        hide:function(){
            $($m13013.params.modalId).modal("hide");
        },
        params:{
            listReceiveBillUrl:serverAddr + '/toCReceiveBill/listReceiveBillsToNC.asyn?r='+Math.random(),
            synNCUrl:serverAddr + '/toCReceiveBill/synNC.asyn?r='+Math.random(),
            modalId:'#synNC-modal',
            id:null,
        }
    };

    function synTocNC() {
        $custom.request.ajax($m13013.params.synNCUrl, {"ids":$m13013.params.id}, function(res){
            if(res.state == 200) {
                if (res.data.code == '00') {
                    $custom.alert.success("已将应收信息同步至NC结算中转层，请知悉！");
                    $($m13013.params.modalId).modal("hide");
                    setTimeout(function () {
                        $module.revertList.serializeListForm() ;
                        $module.revertList.isMainList = true;
                        $module.load.pageOrder('act=14018');
                    }, 1000);
                } else {
                    $custom.alert.error(res.data.message);
                }
            } else {
                $custom.alert.error(res.expMessage);
            }
        });
    }

</script>