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
                                <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">理货单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">理货单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">全部</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">理货单状态 :</span>
                                    <select class="input_msg" name="state">
                                    </select>
                                    <span class="msg_title">理货日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="tallyingStartDate" id="tallyingStartDate" style="font-size:13px;">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="tallyingEndDate" id="tallyingEndDate" style="font-size:13px;">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">预申报单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="orderCode">
                                	<span class="msg_title">合同号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="contractNo">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="tallying_confirm">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="confirm-buttons">确认</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="tallying_reject">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="reject-buttons">驳回</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="tallying_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>理货单号</th>
                                <th>预申报单号</th>
                                <th>仓库</th>
                                <th>事业部</th>
                                <th>合同号</th>
                                <th>理货时间</th>
                                <th>状态</th>
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
            <!-- container -->
        </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"tallyingOrder_stateList",null);
    $(document).ready(function() {

        //重置按钮
        $("button[type='reset']").click(function(){
        	
        });

        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
      	//事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
        
        laydate.render({
            elem: '#tallyingStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            trigger: 'click'
        });

        laydate.render({
            elem: '#tallyingEndDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            done: function(value){
                this.dateTime.hours = 23;
                this.dateTime.minutes = 59;
                this.dateTime.seconds = 59;
            }

        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/tallying/listTallyingOrder.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'orderCode'},{data:'depotName'},{data:'buName'},{data:'contractNo'},{data:'tallyingDate'},{data:'stateLabel'},
                { //操作
                    orderable: false,
                    width: "80px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="tallying_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                    }
                },],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //确认
    $("#confirm-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定确认选中对象？",function(){
            $custom.request.ajax(serverAddr+"/tallying/modifyOrderState.asyn",{"ids":ids,"state":'010'},function(data){
                if(data.state==200){
                    if(data.data=='状态必须为待确认'){
                        $custom.alert.error(data.data);
                    }else{
                        $custom.alert.success("确认成功");
                        //删除成功，重新加载页面
                        $mytable.fn.reload();
                    }
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });
    });
    //驳回
    $("#reject-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定驳回选中对象？",function(){
            $custom.request.ajax(serverAddr+"/tallying/modifyOrderState.asyn",{"ids":ids,"state":'004'},function(data){
                if(data.state==200){
                    if(data.data=='状态必须为待确认'){
                        $custom.alert.error(data.data);
                    }else{
                        $custom.alert.success("驳回成功");
                        //删除成功，重新加载页面
                        $mytable.fn.reload();
                    }
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });
    });
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

    //详情
    function details(id){
        $module.load.pageOrder("act=30031&id="+id);
    }

    function dh_list(){
        $module.load.pageOrder("act=3003");
    }
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
  //导出
    $("#export-buttons").click(function(){
        var url = serverAddr+'/tallying/exportTallyingOrder.asyn?r='+Math.random();
        var params = $("#form1").serialize() ;
        $custom.alert.warning("确定导出选中对象？",function(){
        	$downLoad.downLoadByUrl(url + "&" +params);
        });
    });
</script>
