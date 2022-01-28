<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                                    <li class="breadcrumb-item"><a href="#">财务报表关账</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form_box">
                         <form id="form1">
                            <div class="form_con">
                                <div class="form_item h35">
	                                <span class="msg_title">归属月 :</span>
	                                <input type="text" required="" parsley-type="text" class="input_msg date-input" name="month" value='<c:if test="${now != null }">${now }</c:if>'>
                                    <span class="msg_title">帐表状态 :</span>
	                                <select name="status" class="input_msg" id="status">
	                                </select>
	                                <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="reload">查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                         </form>
                        </div>
                        <div class="row col-12 mt20 table-responsive">
                        <label id="labelTime" style="color: red;"></label>
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                	<th>序号</th>
									<th>公司</th>
									<th>事业部</th>
									<th>归属月份</th>
									<th>期初时间</th>
									<th>期末时间</th>
									<th>关账时间</th>
									<th>账表状态</th>
									<th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                               
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->

                </div>
                <!-- container -->
            </div>

    </div> <!-- content -->
</div>
<jsp:include page="/WEB-INF/views/modals/20000.jsp" />
<script type="text/javascript">

$(document).ready(function() {
	$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"financeInventorySummary_statusList",null);
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
	$(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
   	});

    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url:serverAddr+'/financeClose/financeSummaryList.asyn?r='+Math.random(),
        columns:[{  //序号
            data : null,
            bSortable : false,
            className : "",
            render : function(data, type, row, meta) {
                // 显示行号
                var startIndex = meta.settings._iDisplayStart;
                return startIndex + meta.row + 1;
               }	
            },						
            {data:'merchantName'},{data:'buName'},{data:'month'},{data:'month'},
            {data:'month'},{data:'closeDate'},{data:'statusLabel'},
            { //操作
                orderable: false,
                data: null,
                width:'70px',
                render: function (data, type, row, meta) {     
                	var str ="";
                	if (data.status=="029") {
                		str='<shiro:hasPermission name="financeClose_close"><a href="javascript:close(\'' + row.month +'\',\''+row.buId + '\')">关帐</a></shiro:hasPermission> '; 
					} 
                	if (data.status=="030") {                  		
                		str='<shiro:hasPermission name="financeClose_updateNotClose"><a href="javascript:updateNotClose(\'' + row.month +'\',\''+row.buId + '\')">反关账</a></shiro:hasPermission> ';
					}
                	str=str+'<shiro:hasPermission name="financeClose_detail"><a href="javascript:details(\'' + row.month +'\',\''+row.buId + '\')">详情</a></shiro:hasPermission> ';
                	var relCode=row.merchantId+","+row.buId+","+row.month;
                	var module="1";
                	str=str+'<shiro:hasPermission name="financeClose_detail"><a href="javascript:logDetails(\'' + relCode+'\',\''+module+ '\')">操作日志</a></shiro:hasPermission>';
                	return str;
                }
            }
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    	if (aData.month!=null) {
    		var date = aData.month;
    		date = date.replace(/-/g,'/'); 
    		var createDate = new Date(date);
   		    var year = createDate.getFullYear() + "";
   		    var month = (createDate.getMonth() + 1) + "";
   		 
   		    // 本月第一天日期
   		    var begin = year + "-" + month + "-01";
   		    // 本月最后一天日期    
   		    var lastDateOfCurrentMonth = new Date(year,month,0); 
   		    var end = year + "-" + month + "-" + lastDateOfCurrentMonth.getDate(); 

    		$('td:eq(3)', nRow).html(aData.month);
    		$('td:eq(4)', nRow).html(begin);
    		$('td:eq(5)', nRow).html(end);
		}

    };
   
	//生成列表信息
	$('#datatable-buttons').mydatatables();
  
	$("#reload").click(function(){
		$mytable.fn.reload();
		 //生成列表信息
	    $('#datatable-buttons').mydatatables();
	});
  
});

	//关帐
	function close(month,buId){
		if (!month) {
			$custom.alert.error('月份不能为空');
		}
		if (!buId) {
			$custom.alert.error('事业部不能为空');
		}
	    $custom.alert.warning("确定关帐吗？",function(){
	        $custom.request.ajax(serverAddr+"/financeClose/close.asyn",{"month":month,"buId":buId},function(data){
	            if(data.state==200){
	                if(data.data.code=="00"){
	                    $custom.alert.success(data.data.message);
	                    //删除成功，重新加载页面
	                    $mytable.fn.reload();
	                }else{
	                    $custom.alert.error(data.data.message);
	                }
	            }else{
	                $custom.alert.error(data.message);
	            }
	        });
	    });
	}
	
	//修改为未关账
	function updateNotClose(month,buId){
		if (!month) {
			$custom.alert.error('月份不能为空');
		}
		if (!buId) {
			$custom.alert.error('事业部不能为空');
		}
	    $custom.alert.warning("确定反关帐吗？",function(){
	        $custom.request.ajax(serverAddr+"/financeClose/updateNotClose.asyn",{"month":month,"buId":buId},function(data){
	            if(data.state==200){
	                if(data.data.code=="00"){
	                    $custom.alert.success(data.data.message);
	                    //删除成功，重新加载页面
	                    $mytable.fn.reload();
	                }else{
	                    $custom.alert.error(data.data.message);
	                }
	            }else{
	                $custom.alert.error(data.message);
	            }
	        });
	    });
	}
	
	//详情
	function details(month,buId){
		if (!month) {
			$custom.alert.error('月份不能为空');
		}
		if (!buId) {
			$custom.alert.error('事业部不能为空');
		}

        $module.revertList.serializeListForm() ;
        var obj = {name: "month", value: month};
        $module.revertList.params.push(obj) ;
        var obj1 = {name: "buId", value: buId};
        $module.revertList.params.push(obj1) ;
        $module.revertList.isMainList = true ;
        var param = "act=12014&month=" + month+"&buId=" + buId;
        $load.a($module.params.loadReportPageURL+param);
	}
	
	
</script>
