<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

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
								<li class="breadcrumb-item"><a href="#">报表管理</a></li>
								<li class="breadcrumb-item"><a href="#">商品在库天数</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                    </select>
                                    <span class="msg_title">报表月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month" name="month" readonly value="${month }">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							 <shiro:hasPermission name="inWarehouseDays_flashInWarehouseDays">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashReport" value="刷新报表"/>
								 <!-- <label for="syn">
									 <input type="checkbox" id="syn" name="syn">同步数据
								 </label>
								 <input type="hidden"  id="selectTime" required="" > -->
							 </shiro:hasPermission>

							<label id="labelTime" style="color: red;margin-left: 20px;"></label>
	                        <label id="labelStatus" style="color: red;"></label>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0" width="100%" >
                            <thead>
								<tr>
									<th>
										<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" id="checkAll"/> <span></span>
										</label>
									</th>
									<th>事业部</th>
									<th>库存总量</th>
									<th>库存总金额</th>
									<th>统计币种</th>
									<th>统计截止日期</th>
									<th>报表月份</th>
									<th>刷新时间</th>
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
	</div>
</div>
<script type="text/javascript">

	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);

	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/inWarehouseDays/listInWarehouseDays.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
                {data:'buName'},{data:'totalNum'},{data:'totalAmount'},{data:'currency'},
                {
                	data:null,
               		render: function (data, type, row, meta) {
               			return formatDateTime(row.statisticsDate) ;
                   	}
                },
                {data:'month'},{data:'createDate'},
                { //操作
                	orderable: false,
                	width: "80px",
                	data: null,
                	render: function (data, type, row, meta) {
                		var html = '<a href="javascript:details(\''+row.month+'\',\''+row.buId+'\')">详情</a>' ;
                		html += ' <shiro:hasPermission name="inWarehouseDays_exportInWarehouseDetails"><a href="javascript:exportDetails(\''+row.month+'\',\''+row.buId+'\')">导出</a></shiro:hasPermission>' ;
                	    return html;
                	}
                }
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	var text = $("#labelTime").text() ;
        	
        	if(!text && aData.createDate){
				var labelTime = compareTime(text , aData.createDate ) ;
        		
        		if(labelTime != ""){
	        		$("#labelTime").text("生成时间："+ labelTime);
        		}
        	}
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
	});
	
	function searchData() {
    	$("#labelTime").text("");
		$mytable.fn.reload();
	}

	//全选
	$("#checkAll").on("click",function(){
		var flag = $("#checkAll").is(":checked");
		var checks = $("tbody tr td input[class='iCheck']");
		checks.each(function(){
			$(this).attr("checked", flag);
		});
	});

	// 日期选择
	$(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
   	});
	
	$("#flashReport").on("click",function(){
		var sDate = $("#month").val();
		
		/* var syn = false ; 
		if($("#syn").is(':checked')) {
			syn = true ;
		} */
		
		if(sDate == null || sDate == ''){
			$custom.alert.warning("确定刷新?",function(){
				 $custom.request.ajax(serverAddr+'/inWarehouseDays/flashInWarehouseDays.asyn',{   },function(result){
					console.log("log:"+result.state);
					if(result.state==200&&result.data.code=='00'){
		                 $custom.alert.success("正在刷新，稍后点击【查询】，查询结果");
		             }else{
		                 $custom.alert.error(result.data.message);
		             }
				});
			}) 	
		}else{
			 $custom.alert.warning("确定刷新该月份?",function(){
				 $custom.request.ajax(serverAddr+'/inWarehouseDays/flashInWarehouseDays.asyn',{"month":$("#month").val()},function(result){
 					console.log("log:"+result.state);
 					if(result.state==200&&result.data.code=='00'){
		                 $custom.alert.success("正在刷新，稍后点击【查询】，查询结果");
		             }else{
		                 $custom.alert.error(result.data.message);
		             }
				});
			}) 	
		}
		
		
		
	})
	
	function details(month , buId){
        $module.load.pageReport("act=12009&month=" + month +"&buId=" + buId);
	}
	
	function exportDetails(month , buId){
		var url = serverAddr+"/inWarehouseDays/exportInWarehouseDetails.asyn?month=" + month +"&buId=" + buId;
	   	 $custom.request.ajax(url,{},function(result){
            if(result.state==200&&result.data.code=='00'){
                 $custom.alert.success("请在报表任务列表查看进度");
             }else{
                 $custom.alert.error(result.data.message);
             }
        });
	}
	
	function compareTime(startDate, endDate) {   
		 if (startDate.length > 0 && endDate.length > 0) {   
		    var startDateTemp = startDate.split(" ");   
		    var endDateTemp = endDate.split(" ");   

		    var arrStartDate = startDateTemp[0].split("-");   
		    var arrEndDate = endDateTemp[0].split("-");   

		    var arrStartTime = startDateTemp[1].split(":");   
		    var arrEndTime = endDateTemp[1].split(":");   

			var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);   
			var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);   
	
			if (allStartDate.getTime() >= allEndDate.getTime()) {   
			    return startDate;   
			} else {   
			    return endDate;   
		    }   
		} else {   
		    return endDate;   
		}   
	}    
	
	function formatDateTime(inputTime) {
	    var date = new Date(inputTime);
	    var y = date.getFullYear();
	    var m = date.getMonth() + 1;
	    m = m < 10 ? ('0' + m) : m;
	    var d = date.getDate();
	    d = d < 10 ? ('0' + d) : d;
	    var h = date.getHours();
	    h = h < 10 ? ('0' + h) : h;
	    var minute = date.getMinutes();
	    var second = date.getSeconds();
	    minute = minute < 10 ? ('0' + minute) : minute;
	    second = second < 10 ? ('0' + second) : second;
	    return y + '-' + m + '-' + d ;
	};

</script>