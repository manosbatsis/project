<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
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
                                <li class="breadcrumb-item"><a href="#">公司档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/tradeLinkConfig/toPage.html');">交易链路配置列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="tradeLinkConfig_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="tradeLinkConfig_copy">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="copy-buttons">复制</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable"
                                               class="group-checkable"/>
                                        <span></span>
                                    </label>
                                </th>
<%--                                <th>ID</th>--%>
                                <th>链路名称</th>
                                <th>起点供应商</th>
                                <th>起点公司</th>
                                <th>客户1</th>
                                <th>客户2</th>
                                <th>客户3</th>
                                <th>客户4</th>
                                <th>创建时间</th>
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
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>
    <jsp:include page="/WEB-INF/views/modals/18001.jsp" />
    <jsp:include page="/WEB-INF/views/modals/18002.jsp" />
</div> <!-- content -->
<script type="text/javascript">
$(function(){
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params = {
        url : serverAddr+'/tradeLinkConfig/listTradeLinkConfig.asyn?r=' + Math.random(),
        columns : [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" name="item-checkbox" class="iCheck" value="' + data.id + '">';
                }
            },{data : 'linkName'},{data : 'startSupplierName'},
            {data : 'startPointMerchantName'},{data : 'oneCustomerName'},
            {data : 'twoCustomerName'},{data : 'threeCustomerName'},{data : 'fourCustomerName'},{data : 'createDate'},
            { //操作
                orderable : false,
                data : null,
                render : function(data, type, row, meta) {
                    return "<shiro:hasPermission name='tradeLinkConfig_edit'><a href='javascript:edit(" + data.id + ")'>编辑</a></shiro:hasPermission>";
                }
            }, ],
        formid : '#form1'
    };
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        $('td:eq(0)', nRow).html("<input type='checkbox' name='item-checkbox' class='iCheck' value='"+(aData.id==null?'':aData.id)+"'>");
        $('td:eq(1)', nRow).html("<p class='nowrap'>"+(aData.linkName==null?'':aData.linkName)+"</p>");
        $('td:eq(2)', nRow).html("<p class='nowrap'>"+(aData.startSupplierName==null?'':aData.startSupplierName)+"</p>");
        $('td:eq(3)', nRow).html("<p class='nowrap'>"+(aData.startPointMerchantName==null?'':aData.startPointMerchantName)+"</p>"
           + "<p class='nowrap'>"+(aData.startPointBuName==null?'':aData.startPointBuName)+"</p>"
            + "<p class='nowrap'>加价:"+(aData.startPointPremiumRate==null?'':aData.startPointPremiumRate)+"%</p>");
        $('td:eq(4)', nRow).html("<p class='nowrap'>"+(aData.oneCustomerName==null?'':aData.oneCustomerName)+"</p>"
            +"<p class='nowrap'>"+(aData.oneBuName==null?'':aData.oneBuName)+"</p>"
            +"<p class='nowrap'>"+(aData.onePremiumRate==null?'':aData.onePremiumRate+"%")+"</p>");
        $('td:eq(5)', nRow).html("<p class='nowrap'>"+(aData.twoCustomerName==null?'':aData.twoCustomerName)+"</p>"
            +"<p class='nowrap'>"+(aData.twoBuName==null?'':aData.twoBuName)+"</p>"
            +"<p class='nowrap'>"+(aData.twoPremiumRate==null?'':aData.twoPremiumRate+"%")+"</p>");
        $('td:eq(6)', nRow).html("<p class='nowrap'>"+(aData.threeCustomerName==null?'':aData.threeCustomerName)+"</p>"
            +"<p class='nowrap'>"+(aData.threeBuName==null?'':aData.threeBuName)+"</p>"
            +"<p class='nowrap'>"+(aData.threePremiumRate==null?'':aData.threePremiumRate+"%")+"</p>");
        $('td:eq(7)', nRow).html("<p class='nowrap'>"+(aData.fourCustomerName==null?'':aData.fourCustomerName)+"</p>"
            +"<p class='nowrap'>"+(aData.fourBuName==null?'':aData.fourBuName)+"</p>");
        $('td:eq(8)', nRow).html("<p class='nowrap'>"+(aData.createDate==null?'':aData.createDate)+"</p>");
        $('td:eq(9)', nRow).html("<p class='nowrap'><shiro:hasPermission name='tradeLinkConfig_edit'><a href='javascript:edit(" + aData.id + ',1'+")'>编辑</a></shiro:hasPermission></p>"
            +"<p class='nowrap'><shiro:hasPermission name='tradeLinkConfig_del'><a href='javascript:del(" + aData.id + ")'>删除</a></shiro:hasPermission></p>");

    };
    //生成列表信息
    $('#datatable-buttons').mydatatables();
});
    // 删除
    function del(id){
        $custom.alert.warning("确认要删除该条交易链路吗？",function(){
            var url = serverAddr+"/tradeLinkConfig/delTradeLinkConfig.asyn";
            $custom.request.ajax(url, {"ids":id}, function(result){
                if(result.state == '200'){
                    $custom.alert.success("删除成功！");
                    //删除成功，重新加载页面
                    $mytable.fn.reload();
                }else{
                    $custom.alert.error("删除失败！");
                }
            });


        });
    }
    // 新增
    $("#add-buttons").click(function(){
        $("#invoice-modal").modal({backdrop: 'static', keyboard: false});
    });

    //复制
    $("#copy-buttons").click(function(){
        var checkeds = $.find("input[name='item-checkbox']:checked");
        if (checkeds.length == 0) {
            $custom.alert.warningText("请选择需要复制的数据！");
            return;
        }
        var idsArray = new Array() ;
        if (checkeds.length >1) {
            $custom.alert.warningText("请选择1条数据！");
            return;
        }
        $(checkeds).each(function (i, m) {
            idsArray.push($(m).val());
        });
        var idsStr = idsArray.join(",");
        debugger
        edit(idsStr,'2');
    });

    //编辑
    function edit(id,type){
        $custom.request.ajax(serverAddr+"/tradeLinkConfig/toEditPage.asyn",{"id":id},function(data1){
            if(data1.state==200){
                $m18002.init(data1.data,type);
            }else{
                if(!!data1.expMessage){
                    $custom.alert.error(data1.expMessage);
                }else{
                    $custom.alert.error(data1.message);
                }
            }
        });
    }
    function dh_list(){
        $module.load.pageOrder("act=19001");
    }

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
    })

    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
</script>
