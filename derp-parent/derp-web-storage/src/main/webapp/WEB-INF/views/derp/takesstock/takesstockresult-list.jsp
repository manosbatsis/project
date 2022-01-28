<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp"/>
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <!-- <form id="add-form" action="/merchandise/saveMerchandise.asyn">  -->
        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">盘点结果列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <div class="form_box">
                        <form id="form1">
                            <div class="form_con">
                             
                                <div class="form_item h35">
                                    <span class="msg_title">盘点单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">仓库 :</span>
                                    <select class="input_msg" id="depotId" name="depotId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">业务场景 :</span>
                                    <select class="input_msg" name="model">
                                    </select>
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                    <span class="msg_title">服务类型 :</span>
                                    <select class="input_msg" name="serverType">
                                    </select>
                                </div>
                                <a href="javascript:" class="unfold">展开功能</a>
                            </div>
                        </form>
                    </div>
                    <div class="mt20">
                        <shiro:hasPermission name="takesstockresult_confirmTakesStock">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                    onclick="confirm(20);">确认
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="takesstockresult_confirmTakesStock">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                    onclick="confirm(10);">驳回
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="takesstockresult_importTakesStock">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default"
                                    id="import-buttons">导入
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="takesstockresult_export">
                            <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportTakesStockResult" value="导出"/>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                               width="100%">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>盘点单号</th>
                                <th>盘点指令单号</th>
                                <th>商家</th>
                                <th>仓库</th>
                                <th>服务类型</th>
                                <th>业务场景</th>
                                <th>单据日期</th>
                                <th>单据状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
            <!-- end row -->
        </div>
        <!-- container -->
    </div>
    <!--   </form>  -->

</div>
<!-- content -->
</div>
<jsp:include page="/WEB-INF/views/modals/9012.jsp" />
<script>
    $module.constant.getConstantListByNameURL.call($('select[name="serverType"]')[0],"takesStockResult_serverTypeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="model"]')[0],"takesStockResult_modelList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"takesStockResult_statusList",null);
    //加载仓库
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {});
    $(document).ready(function () {

	    //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/takesstockresult/listTakesStockResult.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data: 'code'},{data: 'takesStockCode'}, {data: 'merchantName'}, {data: 'depotName'}, {data: 'serverTypeLabel'}, {data: 'modelLabel'}, {data: 'sourceTime'}, {data: 'statusLabel'},
                { //操作
                    orderable: false,
                    width: "50px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = '<shiro:hasPermission name="takesstockresult_detail"><a href="javascript:detail(' + row.id + ')">详情</a></br></shiro:hasPermission>';
                        // 状态为待确认、已确认、入库中，显示“分配事业部”操作按钮(导入不用这按钮)
                        if(row.sourceType != '4'){
                            if(row.status == '024' || row.status == '025' || row.status == '022'){
                                caoZuoHtml += '<shiro:hasPermission name="takesstockresult_inDepotConfirm"><a href="javascript:updateBuName('+row.id + ',' + row.depotId +')">分配事业部</a></shiro:hasPermission>';
                            }
                        }
                        return caoZuoHtml;
                    }
                },
            ],
            formid: '#form1'
        }
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
            if (aData.status != '024') {
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });
    });


    //查询
    function searchData() {
        $mytable.fn.reload();
    }

    //详情
    function detail(id) {
        $module.load.pageStorage("act=90021&id=" + id);
    }

    //盘点确认/驳回
    function confirm(confirmType) {
        //获取选中的id信息
        var ids = $mytable.fn.getCheckedRow();
        if (ids == null || ids == '' || ids == undefined) {
            $custom.alert.warningText("至少选择一条记录！");
            return;
        }
        $custom.alert.warning("确定提交选中对象？", function () {
            var url = serverAddr + "/takesstockresult/confirmTakesStock.asyn";
            $custom.request.ajax(url, {"ids": ids, "confirmType": confirmType}, function (data) {
                if (data.state == 200) {
                    if (data.data.failCode != '' && data.data.failCode != undefined) {
                        $custom.alert.error('完成' + '<br><font size="2">提交失败单号：\n' + data.data.failCode + '</font>');
                    } else {
                        $custom.alert.success('完成');
                    }
                    $mytable.fn.reload();
                } else {
                    $custom.alert.error(data.message);
                }
            });
        });
    }

    //重置
    $("button[type='reset']").click(function () {
        $(".goods_select2").each(function () {
            $(this).val("");
        });
        //重新加载
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });
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

    //导入
    $("#import-buttons").click(function(){
        $module.load.pageStorage("act=90022");
    });

    // 分配事业部，展示所有商品
    function updateBuName(id, depotId) {
        $m9012.init(id,depotId);
    }

    //导出
    $('#exportTakesStockResult').click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = serverAddr+"/takesstockresult/exportTakesStockResult.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });
</script>