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
                            <button type="button" class="close" onclick="$m13011.hide()">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">同步确认</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt20">
                                <span>确认将本次开票应收信息同步至NC：</span>
                                <table id="synNc-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">账单编号</th>
                                        <th style="text-align: center;">事业部</th>
                                        <th style="text-align: center;">结算类型</th>
                                        <th style="text-align: center;">销售模式</th>
                                        <th style="text-align: center;">渠道</th>
                                        <th style="text-align: center;">品牌</th>
                                        <th style="text-align: center;">收支项目名称</th>
                                        <th style="text-align: center;">应收金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="synNc-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-2"></div>
                                        <div class="col-8">
                                            <shiro:hasPermission name="receiveBillInvoice_billExport">
                                                <button type="button" class="btn btn-info waves-effect waves-light btn_default btn-sm" onclick="exportBillInvoice()">导出</button>
                                            </shiro:hasPermission>
                                            <button type="button" class="btn btn-info waves-effect waves-light btn_default btn-sm" onclick='synNC();'>同步</button>
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13011.hide();'>取消</button>
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
    var $m13011={
        init:function(id){
            $m13011.params.id = id;
            $("#synNc-tbody").empty();
            //查询关联应收账单
            $custom.request.ajax($m13011.params.listReceiveBillUrl, {"id":id, "dataSource":"1"}, function(result){
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
                            html += '<tr>'
                                + '<td>' + m.billCode + '</td>'
                                + '<td>' + m.buName + '</td>'
                                + '<td>' + m.settlementTypeLabel + '</td>'
                                + '<td>' + m.saleModelLabel + '</td>'
                                + '<td>' + m.ncChannelName + '</td>'
                                + '<td>' + parentBrandName + '</td>'
                                + '<td>' + m.paymentSubjectName + '</td>'
                                + '<td>' + m.price + '</td>'
                                + '</tr>';
                            totalPrice = (parseFloat(totalPrice) + parseFloat(m.price)).toFixed(2);
                        });
                        html += '<tr>'
                            + '<td>合计</td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td></td>'
                            + '<td>' + totalPrice + '</td>'
                            + '</tr>';
                        $("#synNc-detail").append(html);
                    }
                    $($m13011.params.modalId).modal({backdrop: 'static', keyboard: false});
                }
            });

        },
        hide:function(){
            $($m13011.params.modalId).modal("hide");
        },
        params:{
            listReceiveBillUrl:serverAddr + '/receiveBillInvoice/listReceiveBillsToNC.asyn?r='+Math.random(),
            synNCUrl:serverAddr + '/receiveBillInvoice/synNC.asyn?r='+Math.random(),
            modalId:'#synNC-modal',
            id:null,
        }
    };

    function synNC() {
        $custom.request.ajax($m13011.params.synNCUrl, {"id":$m13011.params.id, "dataSource": "1"}, function(res){
            if(res.state == 200) {
                if (res.data.code == '00') {
                    $custom.alert.success("已将应收信息同步至NC结算中转层，请知悉！");
                    $($m13011.params.modalId).modal("hide");
                    setTimeout(function () {
                        $module.revertList.serializeListForm() ;
                        $module.revertList.isMainList = true;
                        $module.load.pageOrder('act=14001');
                    }, 1000);
                } else {
                    $custom.alert.error(res.data.message);
                }
            } else {
                $custom.alert.error(res.expMessage);
            }
        });
    }

    function exportBillInvoice() {
        //导出
        var url = serverAddr+"/receiveBillInvoice/download.asyn?ids="+$m13011.params.id;
        $downLoad.downLoadByUrl(url);
        // $($m13011.params.modalId).modal("hide");
    };

    function uniqueArray(arr){
        if(null == arr || arr.length == 0 ){
            return arr;
        }else{
            var innerHashMap = {};
            for(var i = 0, j = arr.length; i<j; i++){
                innerHashMap[arr[i]] = null;
            }
            var rs = [];
            for (obj in innerHashMap){
                rs.push(obj);
            }
            return rs;
        }
    }
</script>