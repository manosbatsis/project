<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <div id="signup-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">

            <div class="modal-content" style="width: 680px;">
                <div class="header" >
                    <span class="header-title" style="width: 560px;">适用客户</span>
                    <button class="header-button" ><span class="header-close" onclick="$m15001.cancel()"></span></button>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" id="addForm">
                        <input type="hidden" id="id" name="id" >
                        <div class="form-group">
                            <div class="col-12">
                                <label style="width: 115px; padding-left: 55px;"><i class="red">*</i>状态：</label>
                                <input type="radio" value="1" name="status">启用
                                <input type="radio" value="0" name="status">禁用
                            </div>
                            <div class="col-12">
                                <label ><i class="red">*</i>选择适用客户：</label>
                                <select id="customer_select" class="selectpicker show-tick form-control" multiple data-live-search="true">
                                </select>
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="callback()">确定</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="row col-12 table-responsive" id="commTable" style="display:none;">
                                <table class="table table-striped dataTable no-footer"
                                       cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th>客户名称</th>
                                        <th>客户编码</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table-list">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group" style="float: right;">
                            <div class="col-12">
                                <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick="$m15001.submit();">确定</button>
                                <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="$m15001.cancel();">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">


    var $m15001={
        init:function(id, cusType, status){
            $m15001.params.id = id;
            $m15001.params.cusType = cusType;
            $("input[name='status'][value='"+ status + "']").attr("checked",true);
            //初始化下拉搜索框
            getCustomerSelect() ;
            //重置表单
            $($m15001.params.formId)[0].reset();
            var url = serverAddr + "/fileTemp/getRelCustomer.asyn";
            $custom.request.ajax(url,{"fileId":id},function(data) {
                if (data.state == 200) {
                    var itemList = data.data ;

                    var html = "" ;
                    $(itemList).each(function(index , item){
                        var customerId = item.customerId ;
                        var customerName = item.customerName ;
                        var customerCode = item.customerCode ;

                        if(!customerName){
                            customerName = "" ;
                        }

                        html += '<tr><td><input type="hidden" value="'+customerId +'"><span>'+customerName+'</span></td>' + '<td>'+customerCode+'</td>' +
                                '<td><a href="#" onclick="delCustomer(this)" >删除</a></td></tr>' ;
                    });

                    $("#commTable").find("tbody").append(html) ;
                    $("#commTable").show() ;
                    $("#id").val(id) ;

                } else {
                    $custom.alert.error(data.message);
                }
            });
            this.show();
        },
        submit:function(){
            $module.revertList.serializeListForm() ;
            $module.revertList.isMainList = true ;
            //保存数据
            saveData() ;

        },
        cancel:function() {
            $custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
                $m15001.hide();
            });
        },
        show:function(){
            $('#customer_select').selectpicker('val',['noneSelectedText']);
            $($m15001.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $("#commTable").hide() ;
            $("#commTable").find("tbody").empty() ;
            document.getElementById("customer_select").options.selectedIndex = null;
            $('#customer_select').selectpicker('refresh');
            $($m15001.params.modalId).modal("hide");
        },
        params:{
            formId:'#addForm',
            modalId:'#signup-modal',
            id:null,
            cusType:'1',
        }
    }

    /**
     *	下拉搜索
     */
    $("#customer_select").prev().find(".bs-searchbox").find("input").keyup(
        function(){
            var customerName = $("#addForm .open input").val() ;
            getCustomerSelect(customerName) ;
        }
    );

    /**
     * 获取适用客户下拉
     */
    function getCustomerSelect(customerName){
        $("#customer_select").empty();
        $custom.request.ajax($module.params.getCustomerByDTOURL,{"name":customerName, "cusType": $m15001.params.cusType},function(data) {
            if (data.state == 200) {
                var html = "" ;

                var customerList = data.data ;

                $(customerList).each(function(index , customer){
                    html += '<option value="'+customer.id+'" code="' + customer.code + '">' +customer.name+'</option>' ;

                }) ;

                $("#customer_select").append(html) ;
                $('#customer_select').selectpicker({width: '300px'});
                $('#customer_select').selectpicker('refresh');

            } else {
                $custom.alert.error(data.message);
            }
        });
    }

    /**
     *	确定回调
     */
    function callback(){

        var val = "", vals = [], codes = [];
        //循环的取出插件选择的元素(通过是否添加了selected类名判断)
        //获取标准条码
        for (var i = 0; i < $("li.selected").length; i++) {
            val = $("li.selected").eq(i).find(".text").text();
            if (val != '') {
                vals.push(val);
            }
        }

        //获取品名
        var customerIds = [] ;
        $($("#customer_select").find("option:selected").selectpicker('val')).each(function(i , m){
            var val = $(m).val() ;
            var code = $(m).attr("code");
            if (val != '') {
                customerIds.push(val);
                codes.push(code);
            }
        });

        var html = "" ;
        $(customerIds).each(function( i , m){
            var customerName = $(vals).get(i) ;
            var code = $(codes).get(i) ;

            var customerId = '' ;
            if(judgeCodeExistOrNot(customerName)){
                return true ;
            }

            if('null' != m){
                customerId = m ;
            }

            html += '<tr><td><input type="hidden" value="'+customerId +'"><span>'+customerName+'</span></td>' + '<td>'+code+'</td>' +
                '<td><a href="#" onclick="delCustomer(this)" >删除</a></td></tr>' ;

        });

        $("#commTable").find("tbody").append(html) ;
        $("#commTable").show() ;

        //清空选择框
        $('#customer_select').selectpicker('val',['noneSelectedText']);
        $('#customer_select').selectpicker('refresh');
    }

    /**
     *删除当前的表格行
     */
    function delCustomer(org){
        $(org).parent().parent().remove() ;
    }

    /*
    *保存数据
    */
    function saveData(){
        var status = '1';
        $("input[name='status']").each(function(){
            if($(this).prop("checked")){
                status = $(this).val();
            }
        });
        var trs = $("#table-list").find("tr") ;
        var customers = [];
        $(trs).each(function(i , tr){
            var customerId = $(tr).find("td").eq(0).find("input").val() ;
            var customerName = $(tr).find("td").eq(0).find("span").text() ;
            var customerCode = $(tr).find("td").eq(1).text() ;
            var customer = { "customerId" : customerId ,
                             "customerName" : customerName ,
                             "customerCode" : customerCode};
            customers.push(customer);
        }) ;

        var jsonData = {};
        jsonData.id = $m15001.params.id;
        jsonData.status = status;
        jsonData.customerRelList = customers;
        var jsonStr = JSON.stringify(jsonData);
        var url = serverAddr + "/fileTemp/saveCustomerRel.asyn" ;
        $custom.request.ajax(url, {"json" : jsonStr} ,function(data) {
            if (data.state == 200) {
                setTimeout(function () {
                    $module.load.pageOrder('act=16001');
                }, 500);
                $m15001.hide();
            } else {
                $custom.alert.error(data.expMessage);
            }
        });
    }

    function judgeCodeExistOrNot(commbarcode){
        var trs = $("#commTable").find("tr") ;

        var flag = false ;

        $(trs).each(function(index , tr){
            temp = $(tr).find("td").eq(0).text() ;

            if(temp == commbarcode){
                flag =true ;

                return false ;
            }
        }) ;

        return flag ;
    }
</script>