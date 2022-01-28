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
                                <li class="breadcrumb-item"><a href="#">月结自核对</a></li>
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
                                    <span class="msg_title">仓库名称 :</span>
                                    <select name="depotId" id="depotId"  class="input_msg" >
	                                </select>
                                    <span class="msg_title">核对结果 :</span>
                                    <select class="input_msg" name="status"></select>
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
                            <shiro:hasPermission name="monthly_account_auto_veri_export">
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
                                <th>序号</th>
                                <th>仓库名称</th>
                                <th>报表月份</th>
                                <th>月结报表库存</th>
                                <th>事业部库存量</th>
                                <th>事业部业务进销存</th>
                                <th>仓库现存量</th>
                                <th>最近刷新时间</th>
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
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0], "businessFinanceAutomaticVerification_statusList", null);
    
  	//加载仓库下拉
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0], null, {"type":"a,b,c,d"});
    
    $(document).ready(function () {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr + '/monthlyAccountAutoVeri/listMonthlyAccountAutoVeri.asyn?r=' + Math.random(),
            columns: [
                /* 每一列的数据 */
                {  //序号
	            data : null,
	            bSortable : false,
	            className : "",
	            render : function(data, type, row, meta) {
	                // 显示行号
	                var startIndex = meta.settings._iDisplayStart;
	                return startIndex + meta.row + 1;
	               }
	            },
                {data: 'depotName'}, {data: 'month'}, 
                {data: null,
                	render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		html += '好品：' + row.monthlyAccountNormalNum + "<br>" ;
                		html += '坏品：' + row.monthlyAccountWornNum + "<br>" ;
                		html += '库存总量：' + row.monthlyAccountSurplusNum + "<br>" ;
                		
                        return html;
                    }
                },
                {data: null,
					render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		html += '好品：' + row.buInventoryNormalNum + "<br>" ;
                		html += '坏品：' + row.buInventoryWornNum + "<br>" ;
                		html += '库存总量：' + row.buInventorySurplusNum + "<br>" ;
                		
                        return html;
                    }
                }, 
                {data: null,
					render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		html += '好品：' + row.buInventorySummaryNormalNum + "<br>" ;
                		html += '坏品：' + row.buInventorySummaryWornNum + "<br>" ;
                		html += '库存总量：' + row.buInventorySummaryEndNum + "<br>" ;
                		
                        return html;
                    }	
                }, 
                {data: null,
					render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		html += '好品：' + row.inventoryRealTimeNormalQty + "<br>" ;
                		html += '坏品：' + row.inventoryRealTimeWornQty + "<br>" ;
                		html += '库存总量：' + row.inventoryRealTimeQty + "<br>" ;
                		
                        return html;
                    }	
                }, 
                {data: 'createDate'},
                {data: 'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	var html = '<shiro:hasPermission name="monthly_account_auto_veri_refresh"><a href="javascript:;" onclick="refresh(this, \''+row.depotId+'\',\'false\')">统计</a></shiro:hasPermission>' ;
                    	html += ' <shiro:hasPermission name="monthly_account_auto_veri_refresh"><a href="javascript:;" onclick="refresh(this, \''+row.depotId+'\',\'true\')">刷新</a></shiro:hasPermission>' ;
                        return html;
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
        var url = serverAddr + "/monthlyAccountAutoVeri/exportAutoVerification.asyn?r=" + Math.random();

        var params = $("#form1").serialize();
        $custom.alert.warning("确定导出选中对象？", function () {
        	$downLoad.downLoadByUrl(url + "&" + params);
        });
    });

    //刷新
    function refresh(obj, id, syn) {
    	
    	var msg = "" ;
    	var succMsg = "" ;
    	if('false' == syn ){
    		msg = "确定统计?" ;
    		succMsg = "统计中，请稍等" ;
    	}else if('true' == syn){
    		msg = "确定刷新?" ;
    		succMsg = "刷新中，请稍等" ;
    	}

        $custom.alert.warning(msg,function(){
            $(obj).css("pointer-events","none");
            $(obj).parents("tr").children('td').eq(2).text("");
            $(obj).parents("tr").children('td').eq(3).text("");
            $(obj).parents("tr").children('td').eq(4).text("");
            $(obj).parents("tr").children('td').eq(5).text("");
            $(obj).parents("tr").children('td').eq(6).text("");
            $(obj).parents("tr").children('td').eq(7).text("");
            
            var month = $("#month").val() ;
            $custom.request.ajax(serverAddr+'/monthlyAccountAutoVeri/flash.asyn',{ "depotId" : id, "month": month, "syn" : syn },function(result){
				 if(result.state==200&&result.data.code=='00'){
	                  $custom.alert.success("操作成功");
	              }else{
	                  $custom.alert.error(result.data.message);
	              }
			});
		}) 	
    }

</script>