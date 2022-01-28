<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>


<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<style>
    .date-input {
        font-size: 13px;
    }
</style>
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
                                <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:(0);">调拨理货单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                              
                                <div class="form_item h35">
                                    <span class="msg_title">调拨单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="orderCode">
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">理货单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">理货单状态 :</span>
                                    <select class="input_msg" name="state">
                                    </select>
                                    <span class="msg_title">合同号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="contractNo">

                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">理货日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="tallyingStartDate" id="tallyingStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="tallyingEndDate" id="tallyingEndDate">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="transferTallying_confirm">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="tallyConfirm('010');">确认</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transferTallying_reject">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="tallyConfirm('004');">驳回</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">序号</th>
                                <th style="white-space:nowrap;" class="tc">理货单号</th>
                                <th style="white-space:nowrap;" class="tc">调拨单号</th>
                                <th style="white-space:nowrap;" class="tc">仓库</th>
                                <th style="white-space:nowrap;" class="tc">事业部</th>
                                <th style="white-space:nowrap;" class="tc">合同号</th>
                                <th style="white-space:nowrap;" class="tc">理货时间</th>
                                <th style="white-space:nowrap;" class="tc">状态</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
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
    //加载仓库
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $(document).ready(function() {
        laydate.render({
            elem: '#tallyingStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
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
            url:serverAddr+'/transferTallying/listTallyingOrderTransfer.asyn?r='+Math.random(),
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
                        return '<shiro:hasPermission name="transferTallying_detail"><a href="javascript:details(' + row.id + ')">详情</a>&nbsp;&nbsp;</shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
           /* if(aData.state == '009'){
                $('td:eq(6)', nRow).html('待确认');
            }else if(aData.state == '010'){
                $('td:eq(6)', nRow).html('已确认');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.state == '004'){
                $('td:eq(6)', nRow).html('已驳回');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }*/
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    });

    //详情
    function details(id){
        $module.load.pageOrder("act=80021&id="+id);
    }

    //确认、驳回
    function tallyConfirm(state){
        //获取选中的id信息
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定提交选中对象？",function(){
            var url=serverAddr+"/transferTallying/tallyConfirmTransfer.asyn";
            $custom.request.ajax(url,{"ids":ids,"state":state},function(data){
                if(data.state==200){
                    if(data.data.failCode!=''&&data.data.failCode!=undefined){
                        $custom.alert.success('完成\n<font size="1">理货失败单号：\n'+data.data.failCode+'</font>');
                    }else{
                        $custom.alert.success('完成');
                    }
                    //删除成功，重新加载页面
                    $mytable.fn.reload();
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });
    }
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    })
</script>
