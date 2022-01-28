<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp"/>

<!DOCTYPE html>
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
                                <li class="breadcrumb-item"><a href="#">自动化校验</a></li>
                                <li class="breadcrumb-item"><a href="#">业财自核对</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">年月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month"
                                           name="month" value="${month }">
                                    <span class="msg_title">核对结果 :</span>
                                    <select class="input_msg" name="status"></select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询
                                        </button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">
                                            重置
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="businessFinance_auto_veri_export">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export"
                                       value="导出"/>
                            </shiro:hasPermission>
                        </div>
                    </div>

                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"
                               width="100%">
                            <thead>
                            <tr>
                                <th>报表月份</th>
                                <th>事业部</th>
                                <th>事业部业务进销存</th>
                                <th>事业部财务进销存</th>
                                <th>最新更新时间</th>
                                <th>校验结果</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0], "businessFinanceAutomaticVerification_statusList", null);
    $(document).ready(function () {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/businessFinanceAutoVeri/listBusinessFinanceAutoVerification.asyn?r=' + Math.random(),
            columns: [
                /* 每一列的数据 */
                {data: 'month'},{data: 'buName'},{data: 'buInventorySummaryEndNum'},
                {data: 'buFinanceSummaryEndNum'}, {data: 'createDate'},
                {data: 'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var caoZuoHtml = "";
                        caoZuoHtml += '<shiro:hasPermission name="businessFinance_auto_veri_refresh"><a href="javascript:void(0)" onclick="refresh(this,\'' + row.month + '\',\'true\',\'' + row.buId + '\')">刷新</a></shiro:hasPermission>';
                        caoZuoHtml += '<shiro:hasPermission name="businessFinance_auto_veri_countFresh">&nbsp;<a href="javascript:void(0)" onclick="refresh(this,\'' + row.month + '\',\'false\',\'' + row.buId + '\')">统计</a></shiro:hasPermission>';
                        return caoZuoHtml;
                    }
                },
            ],
            formid: '#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        searchData();

        function searchData() {
            $mytable.fn.reload();
        }

    })

    // 日期选择
    $(".date-input").datetimepicker({
        format: 'yyyy-mm',
        autoclose: true,
        todayBtn: true,
        startView: 'year',
        minView: 'year',
        maxView: 'decade',
        language: 'zh-CN',
    });

    //汇总表导出
    $("#export").on("click", function () {
        //根据筛选条件导出
        var url = serverAddr + "/businessFinanceAutoVeri/exportAutoVerification.asyn?r=" + Math.random();

        var params = $("#form1").serialize();
        $custom.alert.warning("确定导出选中对象？", function () {
            $downLoad.downLoadByUrl(url + "&" + params);
        });
    });

    //刷新
    function refresh(obj, month, refresh, buId) {
        var msg = "" ;
        var succMsg = "" ;
        if('false' == refresh ){
            msg = "确定统计?" ;
            succMsg = "统计中，请稍等" ;
        }else if('true' == refresh){
            msg = "确定刷新?" ;
            succMsg = "刷新中，请稍等" ;
        }
        $custom.alert.warning(msg,function(){
            var url = serverAddr + "/businessFinanceAutoVeri/refreshAutoVerification.asyn?r=" + Math.random();
            $custom.request.ajax(url,{"month":month, "buId":buId, "refresh":refresh},function(result){
                if(result.state==200&&result.data.code=='00'){
                    $(obj).css("pointer-events","none");
                    $(obj).parents("tr").children('td').eq(1).text("");
                    $(obj).parents("tr").children('td').eq(2).text("");
                    $(obj).parents("tr").children('td').eq(3).text("");
                    $(obj).parents("tr").children('td').eq(4).text("");
                    $(obj).parents("tr").children('td').eq(5).text("");
                    $(obj).parents("tr").children('td').eq(6).text("");
                    $custom.alert.success(succMsg);
                }else{
                    $custom.alert.error(result.data.message);
                }
            });
        })
    }

</script>