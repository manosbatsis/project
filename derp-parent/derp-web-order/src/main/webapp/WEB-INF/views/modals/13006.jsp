<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="poNos-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;top: 150px;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 600px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">勾选本次开票PO号</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt20">
                                <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">
                                            <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                <input id="checkAll" type="checkbox" name="keeperMerchantGroup-checkable" class="group-checkable" />
                                            </label>
                                        </th>
                                        <th style="text-align: center;">PO号</th>
                                        <th style="text-align: center;">上架数量</th>
                                        <th style="text-align: center;">总金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="data-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row mt20">
                                <div class='col-12' style="text-align: left;">
                                    <span>已选择：</span>
                                    <span id="poNum">0</span>个PO，
                                    <span id="chooseNum"></span>数量，
                                    <span id="chooseAmount"></span><span id="currency"></span>
                                </div>

                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-4"></div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm" onclick='$m13006.save()'>确认</button>
                                        </div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13006.hide();'>取消</button>
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
    var $m13006={
        init:function(relCode, buId, orderType,currency, list){
            $("#data-tbody").empty();
            $("#checkAll").prop("checked", false);
            $("#currency").html(currency);
            $('#poNum').html('');
            $('#chooseNum').html('');
            $('#chooseAmount').html('');
            $m13006.params.relCode = relCode;
            $m13006.params.buId = buId;
            $m13006.params.orderType = orderType;
            if(list){
                var html = "" ;
                $(list).each(function(i, m) {
                    var poNo = m.poNo;
                    var amount = m.amount;
                    var totalShelfNum = m.totalShelfNum;
                    html += '<tr>'
                        + '<td class="po-checkbox"><input type="checkbox" class="iCheck" onclick="choosePoNo();"></td>'
                        + '<td>' + poNo + '</td>'
                        + '<td>' + totalShelfNum + '</td>'
                        + '<td>' + amount + '</td>'
                        + '</tr>';

                });
                $("#datatable-detail").append(html);
            }
            $($m13006.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13006.params.modalId).modal("hide");
        },
        save:function () {
            var checked = $(".po-checkbox").find("input[type=checkbox]:checked") ;
            if (checked.length == 0) {
                $custom.alert.warningText("请选择开票的PO号！");
                return;
            }
            var count = 0;
            var totalNum = 0;
            var totalAmount = 0;
            var poNoArr = [];
            $(checked).each(function(i,m){
                var poNo = $(m).parents("tr").find("td").eq(1).text();
                var num = $(m).parents("tr").find("td").eq(2).text();
                var amount = $(m).parents("tr").find("td").eq(3).text();
                count++;
                totalNum += parseInt(num);
                totalAmount += parseFloat(amount);
                poNoArr.push(poNo);
            });
            var poNos = poNoArr.join("&");
            $('#poNum').html(count);
            $('#chooseNum').html(totalNum);
            $('#chooseAmount').html(totalAmount);
            $m13006.hide();
            $m13001.billSave($m13006.params.relCode, $m13006.params.buId, $m13006.params.orderType, poNos);
        },
        params:{
            modalId:'#poNos-modal',
            relCode:null,
            orderType:0,
            buId:null
        }
    };

    //全选
    $("#checkAll").on("click",function(){
        var flag = $("#checkAll").is(":checked");
        var checks = $("#data-tbody").find('tr').find('td').find("input[type='checkbox']");
        var count = 0;
        var totalNum = 0;
        var totalAmount = 0;
        checks.each(function(){
            $(this).prop("checked", flag);
            if (flag) {
                var num = $(this).parents("tr").find("td").eq(2).text();
                var amount = $(this).parents("tr").find("td").eq(3).text();
                count++;
                totalNum += parseInt(num);
                totalAmount += parseFloat(amount);
            }
        });
        $('#poNum').html(count);
        $('#chooseNum').html(totalNum);
        $('#chooseAmount').html(totalAmount);
    });

    function choosePoNo() {
        var checked = $(".po-checkbox").find("input[type=checkbox]:checked") ;
        var count = 0;
        var totalNum = 0;
        var totalAmount = 0;
        $(checked).each(function(i,m){
            var num = $(m).parents("tr").find("td").eq(2).text();
            var amount = $(m).parents("tr").find("td").eq(3).text();
            count++;
            totalNum += parseInt(num);
            totalAmount += parseFloat(amount);
        });
        $('#poNum').html(count);
        $('#chooseNum').html(totalNum);
        $('#chooseAmount').html(totalAmount);
    }
</script>