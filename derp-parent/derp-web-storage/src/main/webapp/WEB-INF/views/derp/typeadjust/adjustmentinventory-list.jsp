<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<style>
    .date-input {
        font-size: 13px;
    }
</style>
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
                                <li class="breadcrumb-item"><a href="#">仓储管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">仓储库存调整</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">调整单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">入仓仓库 :</span>
                                    <select name="depotId" id="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">业务类别 :</span>
                                    <select name="model" class="input_msg">
                                    </select>
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                    <span class="msg_title">单据来源 :</span>
                                    <select name="source" class="input_msg">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">归属日期:</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="sourceStartTime" id="sourceStartTime">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="sourceEndTime" id="sourceEndTime">

                                </div>
                            </div>
                            <a href="javascript:;" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row mt20 col-12">
                        <div class="button-list">
                            <shiro:hasPermission name="adjustmentInventory_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="adjustmentInventory_deleteAdjustment">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="deleteObject();">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="adjustmentInventory_confirm">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="confirm();">确认</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="adjustmentInventory_export">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportAdjustmentInventory" value="导出"/>
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
                                <th>公司</th>
                                <th>库存调整单号</th>
                                <th>业务类别</th>
                                <th>来源单据号</th>
                                <th>仓库名称</th>
                                <th>调整时间</th>
                                <th>归属月份</th>
                                <th>归属日期</th>
                                <th>单据状态</th>
                                <th>单据来源</th>
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
<%--<jsp:include page="/WEB-INF/views/modals/11011.jsp" />--%>
<jsp:include page="/WEB-INF/views/modals/11012.jsp" />
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="model"]')[0],"adjustmentInventory_modelList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"adjustmentType_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="source"]')[0],"adjustmentInventory_sourceList",null);

    $(document).ready(function() {

        laydate.render({
            elem: '#sourceStartTime', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            }
        });

        laydate.render({
            elem: '#sourceEndTime', //指定元素
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

        //重置按钮
        $("button[type='reset']").click(function(){
            $(".goods_select2").each(function(){
                $(this).val("");
            });
            //重新加载select2
            $(".goods_select2").select2({
                language:"zh-CN",
                placeholder: '请选择',
                allowClear:true
            });
        });

        //加载仓库
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});

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
            url: serverAddr+'/adjustmentInventory/listAdjustment.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'merchantName'},{data:'code'},{data:'modelLabel'},{data:'sourceCode'},{data:'depotName'},{data:'adjustmentTime'},{data:'months'},{data:'sourceTime'},{data:'statusLabel'},
                {data:'sourceLabel'},
                { //操作
                    orderable: false,
                    width: "50px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = '<shiro:hasPermission name="adjustmentInventory_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        if(row.status === '020' && row.source === '01') {
                            caoZuoHtml += '<shiro:hasPermission name="adjustmentInventory_classifyAdjustmentInventory"></br><a href="javascript:$m11012.init('+row.id+','+row.depotId+')">分配事业部</a></shiro:hasPermission>';
                        }
                        return caoZuoHtml;
                    }
                },],
            formid:'#form1'
        }
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.source == '01'){
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }
            if (aData.status != '020'){
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

    function searchData(){
        $mytable.fn.reload();
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
    });
    //详情
    function details(id){
        $module.load.pageStorage("act=100022&id="+id);
    }

    function dh_list(){
        $module.load.pageStorage("act=10002");
    }
    
    //导入
    $("#import-buttons").click(function(){
    	$module.load.pageStorage("act=100021");
    });

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    })
    
    //删除
    function deleteObject(){
    	$custom.request.delChecked(serverAddr+'/adjustmentInventory/deleteAdjustment.asyn');
    }

    //导入单据确认
    function confirm() {
        //获取选中的id信息
        var ids = $mytable.fn.getCheckedRow();
        if (ids == null || ids == '' || ids == undefined) {
            $custom.alert.warningText("至少选择一条记录！");
            return;
        }
        var check = true;
        //校验归属日期并获取商品数量 检查可用库存量
        //获取商品数量 检查可用库存量
        var getUrl = serverAddr+"/adjustmentInventory/getAdjustmentSum.asyn";
        $custom.request.ajaxSync(getUrl,{"ids":ids},function(result){
            if(result.state == '200') {
                var mergeList = result.data.mergeList;
                if(mergeList) {
                    for(var k = 0; k < mergeList.length; k++){
                        var item = mergeList[k];
                        var depotId = item.depot_id;
                        var goodsId = item.goods_id;
                        var goodsNo = item.goods_no;
                        var depotcode = item.depot_code;
                        var type = item.is_damage;//是否坏品 0-好品 1-坏品
                        var isExpire = item.isExpire;//是否过期0-是 1-否
                        var batchNo = item.old_batch_no;//是否过期0-是 1-否
                        var adjustTotal = item.adjust_total;//调整量

                        //查询可用量
                        var tallyingUnit = null;
                        if(item.depot_type=="c"){
                            tallyingUnit = item.tallying_unit;
                        }
                        var availableNum =$module.inventory.queryAvailability(depotId,goodsId,depotcode,tallyingUnit,type,isExpire,batchNo);
                        if(availableNum ==-1){
                            check = false;
                            $custom.alert.warningText("货号:"+goodsNo+"未查到库存余量");
                            break;
                        }else if(adjustTotal>availableNum){
                            check = false;
                            if(batchNo!=null){
                                $custom.alert.warningText("库存不足货号:"+goodsNo+"批次:"+batchNo+",余量："+availableNum);
                            }else{
                                $custom.alert.warningText("库存不足货号："+goodsNo+",余量："+availableNum);
                            }
                            break;
                        }
                    }
                }
            } else {
                $custom.alert.warningText("确认入库失败！");
                return;
            }
            //推送入库确认
            if (check) {
                var url = serverAddr + "/adjustmentInventory/auditAdjustment.asyn";
                $custom.request.ajax(url, {"ids":ids}, function (data) {
                    if (data.state == 200) {
                        if (data.data.failure == 0) {
                            $custom.alert.success("确认入库成功！");
                        } else {
                            var message = data.data.failureMsg;
                            if (message) {
                                $custom.alert.error("成功："+ data.data.success + "\n" + "失败："+ data.data.failure + "\n" + message);
                            } else {
                                $custom.alert.error("确认入库失败！");
                            }
                        }
                        $mytable.fn.reload();
                    } else {
                        $custom.alert.error(data.message);
                    }
                });
            }
        });
    }

    //导出
    $('#exportAdjustmentInventory').click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = serverAddr+"/adjustmentInventory/exportAdjustmentInventory.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });
</script>
