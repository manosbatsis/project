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
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:$load.a('/transfer/toPage.html');">调拨订单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                               
                                <div class="form_item h35">
                                    <span class="msg_title">调拨订单号 :</span>                                         
                                    <input class="input_msg" type="text" name="code" maxlength="30">
                                    <span class="msg_title">调入仓库 :</span>
                                    <select name="inDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">调出仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">合同号 :</span>
                                    <input class="input_msg" type="text" name="contractNo" maxlength="50" >
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                    <span class="msg_title">LBX单号 :</span>                                         
                                    <input class="input_msg" type="text" name="lbxNo" maxlength="30">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" class="input_msg">
                                    </select>
                                    <span class="msg_title">创建时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateStart" id="createDateStart">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createDateEnd" id="createDateEnd">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>

                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="transfer_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transfer_delTransferOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del()">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transfer_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transfer_export">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportTransferOrder" value="导出"/>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transfer_copy">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="copy-buttons" value="复制"/>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="transfer_exportCustoms">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-customs-button">导出清关资料</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">序号</th>
                                <th style="white-space:nowrap;" class="tc">调拨单号</th>
                                <th style="white-space:nowrap;" class="tc">合同号</th>
                                <th style="white-space:nowrap;" class="tc">调出仓库</th>
                                <th style="white-space:nowrap;" class="tc">调入仓库</th>
                                <th style="white-space:nowrap;" class="tc">事业部</th>
                                <th style="white-space:nowrap;" class="tc">单据状态</th>
                                <th style="white-space:nowrap;" class="tc">LBX单号</th>
                                <th style="white-space:nowrap;" class="tc">创建时间</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!-- end row -->
        <!-- 弹窗开始 -->
        <jsp:include page="/WEB-INF/views/modals/3012.jsp"/>
        <jsp:include page="/WEB-INF/views/modals/9037.jsp" />
        <!-- 弹窗结束 -->
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"transferOrder_statusList",null);

    $("button[type='reset']").click(function(){
        $(".goods_select2").each(function(){
            $(this).val("");
        });
        //重新加载
        $(".goods_select2").select2({
            language:"zh-CN",
            placeholder: '请选择',
            allowClear:true
        });
    });

    //加载仓库
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null, {});
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {});
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $(document).ready(function() {


        laydate.render({
            elem: '#createDateStart', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            }
        });

        laydate.render({
            elem: '#createDateEnd', //指定元素
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

        //重新加载select2
        $(".goods_select2").select2({
            language:"zh-CN",
            placeholder: '请选择',
            allowClear:true
        });

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/transfer/listTransferOrder.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'contractNo'},{data:'outDepotName'},{data:'inDepotName'},{data:'buName'},{data:'statusLabel'},{data:'lbxNo'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "125px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = '<shiro:hasPermission name="transfer_detail"><a href="javascript:detail('+row.id+')">详情</a></shiro:hasPermission>';
                        if(row.status == '013'){
                            caoZuoHtml += '<shiro:hasPermission name="transfer_edit">&nbsp;<a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>';
                        }
                        if (row.outDepotBatchValidation != '1' && row.outDepotIsInDependOut != '1' &&(row.status == '014' || row.status == '024')) {
                            caoZuoHtml += '<shiro:hasPermission name="transfer_out">&nbsp;<a href="javascript:transferOut('+row.id+')">打托出库</a></shiro:hasPermission>';
                        }
                        if (row.inDepotBatchValidation != '1' && row.inDepotIsOutDependIn != '1' && row.status == '023') {
                            caoZuoHtml += '<shiro:hasPermission name="transfer_in">&nbsp;<a href="javascript:transferIn('+row.id+')">入库上架</a></shiro:hasPermission>';
                        }
                        if (row.status == '014' || row.status == '024' || row.status == '023') {
                            caoZuoHtml += '<shiro:hasPermission name="transfer_attachment">&nbsp;<a href="javascript:toAttachment(\''+row.code+'\')">附件</a></shiro:hasPermission>';
                        }
                        if (row.status == '007') {
                            caoZuoHtml += '<shiro:hasPermission name="transfer_attachment">&nbsp;<a href="javascript:toAttachment(\''+row.code+'\')">附件</a></shiro:hasPermission>';
                        }
                        return caoZuoHtml;
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            /*if(aData.status == '006'){
                $('td:eq(5)', nRow).html('已删除');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.status == '014'){
                $('td:eq(5)', nRow).html('已提交');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.status == '023'){
                $('td:eq(5)', nRow).html('调拨已出库');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.status == '024'){
                $('td:eq(5)', nRow).html('调拨已入库');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.status == '007'){
                $('td:eq(5)', nRow).html('已完结');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else{
                $('td:eq(5)', nRow).html('待提交');
            }*/


        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function(){
        $load.a(pageUrl+'80014&pageSource=2');
    });

    //编辑
    function edit(id){
        //$load.a(pageUrl+"80012&id="+id);
        $module.load.pageOrder("act=80012&id="+id);
    }

    //导入
    $("#import-buttons").click(function(){
        $module.load.pageOrder("act=80015");
    });


    //详情
    function detail(id){
        //$module.load.pageOrder("atc=80011&id="+id);
        $module.load.pageOrder("act=80011&id="+id);
    }
    //删除
    function del(){
        $custom.request.delChecked(serverAddr+'/transfer/delTransferOrder.asyn');
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
    
    function showModel(id,lbxno){
    	$("#newLbxNo").val(lbxno);
    	$("#transOrderId").val(id);
    	$("#lbxno-signup-modal").modal("show");
    }

    $('#exportTransferOrder').click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            var countUrl = serverAddr+"/transfer/exportTransferCount.asyn";
            $custom.request.ajax(countUrl,$("#form1").serializeArray(),function(result){
                if(result.data.code!='00'){
                    $custom.alert.warningText(result.data.message);
                }else{
                    //导出
                    var url = serverAddr+"/transfer/exportTransferOrder.asyn?"+param;
                    $downLoad.downLoadByUrl(url);
                }
            });
        });
    });

    //出库报告
    function transferOut(id) {
        var validUrl = serverAddr+"/transfer/isExistTransferOutOrInOrder.asyn"
        $custom.request.ajax(validUrl,{"id":id, "orderType":0},function(result){
            if(result.data.code!='00'){
                $custom.alert.warningText(result.data.message);
            }else{
                $module.load.pageOrder("act=80017&id="+id);
            }
        });
    }

    //入库上架
    function transferIn(id) {
        var validUrl = serverAddr+"/transfer/isExistTransferOutOrInOrder.asyn"
        $custom.request.ajax(validUrl,{"id":id, "orderType":1},function(result){
            if(result.data.code!='00'){
                $custom.alert.warningText(result.data.message);
            }else{
                $module.load.pageOrder("act=80016&id="+id);
            }
        });
    }

    //附件管理
    function toAttachment(code){
        $module.load.pageOrder("act=200001&codeid=" + code);
    }

    //复制
    $("#copy-buttons").click(function () {
        //获取选中的id信息
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        if (ids.indexOf(",") != -1) {
            $custom.alert.warningText("只能选择一条记录！");
            return ;
        }
        $load.a(pageUrl+'80014&pageSource=2&id='+ids);
    })

    //导出清关资料
    $("#export-customs-button").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        if(ids.indexOf(",")!=-1){
            $custom.alert.warningText("目前只支持单条数据导出！");
            return ;
        }

        var isSameArea = "";//是否同关区
        var outDepotId = "";//出仓id
        var inDepotId = "";//入仓 id
        var outCustomsId = "";//出仓关区
        var inCustomsId = "";//入仓同关区
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                isSameArea = row.isSameArea;//是否同关区
                outDepotId = row.outDepotId;//出仓id
                inDepotId = row.inDepotId;//入仓 id
                outCustomsId = row.outCustomsId;//出仓关区
                inCustomsId = row.inCustomsId;//入仓同关区
                break;
            }
        }
        $m9037.init(isSameArea,outDepotId,inDepotId,outCustomsId,inCustomsId,
            serverAddr+"/transfer/exportCustomsDeclare.asyn", ids);

    });
</script>