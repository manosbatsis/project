<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content  -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">模版管理</a></li>
                                <li class="breadcrumb-item "><a href="#">发票管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">模版类型 :</span>
                                    <select id="type" name="type" class="input_msg">
                                    </select>
                                    <span class="msg_title">客户 :</span>
                                    <input name="customerIds" id="customerIds" type="hidden">
                                    <select id="customerSelect" class="selectpicker show-tick form-control"  multiple data-live-search="true">
                                    </select>
                                    <span class="msg_title">状态 :</span>
                                    <select id="status" name="status" class="input_msg">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="tileTemp_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$module.load.pageOrder('act=16002');">新建模版</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th class="tc">模版类型</th>
                                <th class="tc">模版编码</th>
                                <th class="tc">模版名称</th>
                                <th class="tc">公司</th>
                                <th class="tc">适用客户</th>
                                <th class="tc">创建时间</th>
                                <th class="tc">状态</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/views/modals/15001.jsp" />
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"temp_typelist",null);
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"temp_statusList",null);
    getSearchCustomerSelect() ;

    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr + '/fileTemp/listFiletemp.asyn?r='+Math.random(),
            columns:[
                {data:'typeLabel'},
                {data:'code'},
                {data:'title'},
                {data:'merchantName'},
                {data:'customers'},
                {data:'createDate'},
                {data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	
                    	var html = "" ;
                    	
                    		html += '<shiro:hasPermission name="tileTemp_edit"><a href="javascript:edit('+row.id+')">编辑&nbsp;&nbsp;</a></shiro:hasPermission>' ;
                    		html += '<a href="javascript:view('+row.id+')">预览&nbsp;&nbsp;</a>' ;
                    		html += '<shiro:hasPermission name="tileTemp_choose"><a href="javascript:$m15001.init('+row.id +',\''+ row.cusType + '\',\''+row.status+'\''+')"><br>适用客户</a></shiro:hasPermission>' ;

                    	return html ;
                    	
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        $('#customerSelect').selectpicker('val',['noneSelectedText']);
    });

    function searchData(){
        var customerIds = [] ;
        $($("#customerSelect").find("option:selected").selectpicker('val')).each(function(i , m){
            var val = $(m).val() ;
            if (val != '') {
                customerIds.push(val);
            }
        });
        $("#customerIds").val(customerIds.join(","));
        $mytable.fn.reload();
    }

    //编辑
    function edit(id){
    	$module.load.pageOrder("act=16002&id=" + id);
    }
    //预览
    function view(id){
    	$module.load.pageOrder("act=16003&id=" + id);
    }

    //删除
    function changeStatus(id,status){
    	
        
    }

    /**
     *	下拉搜索
     */
    $("#customerSelect").prev().find(".bs-searchbox").find("input").keyup(
        function(){
            var customerName = $("#form1 .open input").val() ;
            getSearchCustomerSelect(customerName) ;
        }
    );

    /**
     * 获取适用客户下拉
     */
    function getSearchCustomerSelect(customerName){
        $custom.request.ajax($module.params.getCustomerByDTOURL,{"name":customerName},function(data) {
            if (data.state == 200) {
                var html = "" ;

                var customerList = data.data ;

                $(customerList).each(function(index , customer){
                    html += '<option value="'+customer.id+'" code="' + customer.code + '">' +customer.name+'</option>' ;

                }) ;

                $("#customerSelect").append(html) ;
                $('#customerSelect').selectpicker({width: '190px'});
                $('#customerSelect').selectpicker('refresh');

                $("#customerSelect").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                $("#customerSelect").prev().css({"z-index":"99"}) ;
                $("#customerSelect").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;

            } else {
                $custom.alert.error(data.message);
            }
        });
    }
</script>