<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
    }
    .header-title {
        padding: 15px;
        padding-right: 400px;
        font-weight: bolder;
        font-size: 18px;
    }
    .header-button {
        background-color: #fff;
        border: none;
    }
    .header-close {
    }
    .header-close::after {
        content: "\2716";
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="invoice-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
                <div class="header" >
                    <span class="header-title" >录入发票时间</span>
                </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="invoice-from">
                        <!-- 自定义参数  -->
                        <div class="form-group">
                            <div class="col-12">
                                <label >发票号码 : </label>
                                <input type="text" class="input_msg" id="invoiceNo" name="invoiceNo" style="width: 202px;">
                                <label><i class="red">*</i>收到发票日期: </label>
                                <input class="form-control" id="id" name="id"  type="hidden" >
                                <input style="width: 200px;" class="input_msg form_datetime invoice_date-input" readonly="true" id="invoiceDate" name="invoiceDate"  require reqrMsg="收到发票日期不能为空！">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label><i class="red">*</i>发票日期: </label>
                                <input style="width: 200px;" class="input_msg form_datetime invoice_date-input" readonly="true" id="drawInvoiceDate" name="drawInvoiceDate"  require reqrMsg="发票日期不能为空！">
                                <label style="margin-left: 48px;">开票人: </label>
                                <input style="width: 200px;" class="input_msg" id="invoiceName" name="invoiceName">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row col-12 table-responsive" id="itemTable" >
                                <table class="table table-striped dataTable no-footer"
                                       cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>商品货号</th>
                                        <th>商品名称</th>
                                        <th>采购数量</th>
                                        <th>采购单价</th>
                                        <th><i class="red">*</i>采购金额</th>
                                    </tr>
                                    </thead>
                                    <tbody id="itemTbody">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="invoiceSave();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="invoiceCancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    $(document).ready(function() {
       

        $(".invoice_date-input").datetimepicker({
            language:  'zh-CN',
            format: "yyyy-mm-dd",
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2
        });




    });

    // 保存
    function invoiceSave(){
        var flag = 0;
        var id = $("#id").val();
        var invoiceDate = $("#invoiceDate").val();
        var invoiceName = $("#invoiceName").val();
        var drawInvoiceDate = $("#drawInvoiceDate").val();
        var invoiceNo = $("#invoiceNo").val();
        var tag="1";

        //汇总商品信息
        var goodsNos = new Array() ;
        var prices = new Array() ;
        var amounts = new Array() ;

        var trs = $("#itemTbody").find("tr") ;

        var flag = true ;

        $(trs).each(function(index , tr){

            var goodsNo = $(tr).find("td").eq(0).text() ;
            var price = $(tr).find("td").eq(3).text() ;
            var amount = $(tr).find("input[name='amount']").val() ;

            if(!amount || ! /^[0-9]+(.[0-9]+)?$/.test(amount)){
                $custom.alert.error("请输入数值");
                flag = false ;

                return flag ;
            }

            amount = parseFloat(amount) ;

            if(amount <= 0) {
                $custom.alert.error("请输入大于0的数值");
                flag = false ;

                return flag ;
            }

            goodsNos.push(goodsNo) ;
            prices.push(price) ;
            amounts.push(amount) ;
        }) ;

        if(!flag){
            return ;
        }

        goodsNos = goodsNos.join(",") ;
        prices = prices.join(",") ;
        amounts = amounts.join(",") ;

        var jsonData = {"id":id,"invoiceDate":invoiceDate,"invoiceName":invoiceName,
            "drawInvoiceDate":drawInvoiceDate,"tag":tag,"invoiceNo":invoiceNo,
            "goodsNos":goodsNos , "prices" : prices , "amounts" : amounts} ;

        //校验合同号是否存在，存在给予提示
        $custom.request.ajaxReqrCheck($module.params.serverAddrByOrder+"/purchase/updatePurchaseOrderInvoice.asyn", jsonData , function(data){

            if(data.state == '200'){
                $custom.alert.success("修改成功");

                modalHide() ;

                setTimeout("getPendingRecordOrders();" , 1000) ;

            }else{
                modalHide() ;
            }
        } ,null , function(){});
    };

    function invoiceCancel(){
        $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
            modalHide() ;
        });
    }

    function modalHide(){
        $('#invoice-modal').modal('hide');
        $("#id").val("");
        $("#invoiceDate").val("");
        $("#invoiceName").val("");
        $("#drawInvoiceDate").val("");
        $("#invoiceNo").val("");
    }

</script>