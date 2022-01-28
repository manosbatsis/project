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
								<li class="breadcrumb-item"><a href="#">唯品PO账单核销报表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<ul class="nav nav-tabs">
						<shiro:hasPermission name="vipPoBillVerification_list">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageReport('act=12010');">PO商品核销表</a>
							</li>
						</shiro:hasPermission>

						<shiro:hasPermission name="vipPoBillVerification_endList">
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageReport('act=12012');">PO完结</a>
							</li>
						</shiro:hasPermission>
	           		</ul>
	           		<div class="tab-content">
					<form id="form1">
						<div class="form_box">
							<div >
								<div class="form_item h35">
									<input type="hidden" id="poTemp" value="${poNo}">
                                    <span class="msg_title">PO号 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="po" name="po">
                                    <span class="msg_title">标准条码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="commbarcode" name="commbarcode" >
                                    <span class="msg_title">天数:</span>
                                    <select name="days" class="input_msg">
                                    	<option value="">全部</option>
                                        <option value="0">30天以内</option>
                                        <option value="30">30~60天</option>
                                        <option value="60">60~90天</option>
                                        <option value="90">90天及以上</option>
                                        <option value="120">120天及以上</option>
                                        <option value="180">180天及以上</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
							</div>
							<div>
								<div class="form_item h35">
									<span class="msg_title">事业部:</span>
									<select class="input_msg" name="buId">
									</select>
									<span class="msg_title">客户:</span>
									<select class="input_msg" name="customerId">
									</select>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
	                         <shiro:hasPermission name="vipPoBillVerification_exportMain">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportMain" value="汇总表导出"/>
                             </shiro:hasPermission>
	                     
							 <shiro:hasPermission name="vipPoBillVerification_exportDatails">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportDatails" value="明细表导出"/>
							 </shiro:hasPermission>

							 <shiro:hasPermission name="vipPoBillVerification_flashVipPoBillVeris">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashReport" value="刷新报表"/>
								 <!-- <label for="syn">
									 <input type="checkbox" id="syn" name="syn">同步数据
								 </label>
								 <input type="hidden"  id="selectTime" required="" > -->
							 </shiro:hasPermission>
							 <label id="dataTime" style="color: red;margin-left: 20px;"></label>
							<label id="labelTime" style="color: red;margin-left: 20px;"></label>
	                        <label id="labelStatus" style="color: red;"></label>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>
										<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" id="checkAll"/> <span></span>
										</label>
									</th>
									<th>公司</th>
									<th>仓库</th>
									<th>事业部</th>
									<th>PO号</th>
									<th>客户名称</th>
									<th>po时间</th>
									<th>标准条码</th>
									<th>母品牌</th>
									<th>销售单价</th>
									<th>库存量</th>
									<th>库存金额</th>
									<th>上架总量</th>
									<th>上架总金额</th>
									<th>上架残损量</th>
									<th>首次上架时间</th>
									<th>账单总量</th>
									<th>最近账单时间</th>
									<th>销售出库总量</th>
									<th>国检抽样</th>
									<th>销售退数量</th>
									<th>调拨入库</th>
                                    <th>调拨出库</th>
									<th>唯品红冲</th>
									<th>盘盈数量</th>
									<th>盘亏数量</th>
									<th>报废数量</th>
									<th>天数</th>
									<th>操作</th>
								</tr>
							</thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    
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
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
	//加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
	$(document).ready(function() {
		
		var poTemp = $("#poTemp").val() ;
		
		if(poTemp){
			$("#po").val(poTemp) ;
		}
		
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/vipPoBillVerification/listVipPoBillVeriList.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
                {data:'merchantName'},{data:'depotName'},{data:'buName'}, 
                {data:'po'},{data:'customerName'},{data:'poDate'},{data:'commbarcode'},{data:'superiorParentBrand'},
                {
                	data: null,
                	render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		if(row.currency){
                			html += row.currency + " " ;
                		}
                		
                		if(row.salePrice){
                			html += row.salePrice ;
                		}
                		
                	    return html;
                	}
                },{data:'unsettledAccount'},{
                	
               		data: null,
                   	render: function (data, type, row, meta) {
                   		
                   		var html = "" ;
                   		
                   		if(row.currency){
                   			html += row.currency + " " ;
                   		}
                   		
                   		if(row.salePrice){
                   			html += row.inventoryAmount ;
                   		}
                   		
                   	    return html;
                   	}
                },
                {data:'shelfTotalAccount'},{data:'shelfTotalAmount'},{data:'shelfDamagedAccount'},{data:'firstShelfDate'},{data:'billTotalAccount'},
                {data:'billRecentDate'},{data:'outDepotTotalAccont'},{data:'nationalInspectionSampleAccount'},
                {data:'saleReturnAccount'},{data:'transferInAccount'},{data:'transferOutAccount'},
                {data:'vipHcAccount'},{data:'inventorySurplusAccount'},{data:'inventoryDeficientAccount'},{data:'scrapAccount'},
                {data:'days'},
                { //操作
                	orderable: false,
                	'sWidth': "80px",
                	data: null,
                	render: function (data, type, row, meta) {
                	    return '<shiro:hasPermission name="vipPoBillVerification_detail"><a href="javascript:details(\''+row.po+'\',\''+row.commbarcode+'\',\''+ row.depotId +'\')">详情</a></shiro:hasPermission>';
                	}
                }
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	var text = $("#labelTime").text() ;
        	
        	if(!text && aData.modifyDate){
        		var labelTime = compareTime(text , aData.modifyDate ) ;
        		
        		if(labelTime != ""){
	        		$("#labelTime").text("生成时间："+ labelTime);
        		}
        		
        	}
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
        getDataTime() ;
	})

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

	//汇总表导出
	 $("#exportMain").on("click",function(){
        //根据筛选条件导出
        var url = serverAddr+"/vipPoBillVerification/exportMain.asyn?r="+Math.random() ;
        var params = $("#form1").serialize() ;
        $custom.alert.warning("确定导出选中对象？",function(){
            $downLoad.downLoadByUrl(url + "&" +params) ;
        });
    });
	
	//明细表导出
     $("#exportDatails").on("click",function(){
    	 var url = serverAddr+"/vipPoBillVerification/exportDetails.asyn?r="+Math.random(); 
    	 var params = $("#form1").serializeObject() ;
    	 $custom.request.ajax(url,params,function(result){
             if(result.state==200&&result.data.code=='00'){
                  $custom.alert.success("请在报表任务列表查看进度");
              }else{
                  $custom.alert.error(result.data.message);
              }
         });
    });
	
	$("#flashReport").on("click",function(){
		var po = $("#po").val();
		
		/* var syn = false ; 
		if($("#syn").is(':checked')) {
			syn = true ;
		} */
		
		if(po == null || po == ''){
			$custom.alert.warning("确定刷新?",function(){
				 $custom.request.ajax(serverAddr+'/vipPoBillVerification/flashVipPoBillVeris.asyn',{   },function(result){
					console.log("log:"+result.state);
					if(result.state==200&&result.data.code=='00'){
		                 $custom.alert.success("正在刷新，稍后点击【查询】，查询结果");
		             }else{
		                 $custom.alert.error(result.data.message);
		             }
				});
			}) 	
		}else{
			 $custom.alert.warning("确定刷新该PO号?",function(){
				 $custom.request.ajax(serverAddr+'/vipPoBillVerification/flashVipPoBillVeris.asyn',{"po":po , "syn" : syn },function(result){
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
	
	function details(po_no , commbarcode, depotId){
		$module.revertList.serializeListForm() ;
        $module.load.pageReport("act=12011&poNo=" + po_no +"&commbarcode=" + commbarcode +"&depotId=" + depotId);
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
	
	$.prototype.serializeObject = function () {
        var a,o,h,i,e;
        a = this.serializeArray();
        o={};
        h=o.hasOwnProperty;
        for(i=0;i<a.length;i++){
            e=a[i];
            if(!h.call(o,e.name)){
                o[e.name]=e.value;
            }
        }
        return o;
     }
	
	
	//获取数据截止时间
	function getDataTime(){
		var url = serverAddr+"/vipPoBillVerification/getDataTime.asyn?r="+Math.random(); 
		$custom.request.ajax(url,{},function(result){
            var billDate = result.data.billDate ;
            if(billDate){
            	$("#dataTime").text("数据统计截止时间：" + billDate) ;
            }
            
        });
	}

</script>