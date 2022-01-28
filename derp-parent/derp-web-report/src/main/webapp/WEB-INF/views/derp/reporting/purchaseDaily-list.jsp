<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page isELIgnored="false" %>
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
								<li class="breadcrumb-item"><a href="#">云集采销日报</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">品类 :</span>
									<select name="productTypeId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${listMerchandiseCat}" var="selectMerchandiseCat">
                                            <option value="${selectMerchandiseCat.value }">${selectMerchandiseCat.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">品牌名称 :</span>
                                    <select name="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${listBrand}" var="selectBrand">
                                            <%-- <option value="${selectBrand.value }">${selectBrand.label }</option> --%>
                                            <option value="${selectBrand.id }">${selectBrand.name }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">统计日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="sDate" id="sDate" readonly="readonly" style="background-color: #FFFFFF;">
                                    <!-- <span class="msg_title">商品货号 :</span>
                                    <input type="text" name="goodsNo" class="input_msg"> -->
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								
							</div>
							<a href="javascript:;" class="unfold">展开功能</a>
						</div>
					</form>
					
					<div class=" row col-12 mt-5">
						<shiro:hasPermission name="yunJiPurchaseDaily_exportYunJiPurchaseDaily">
							<div class="button-list">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
							</div>
						</shiro:hasPermission>

						<shiro:hasPermission name="yunJiPurchaseDaily_mqUpdateByDay">
							<div class="button-list">
								<!-- &nbsp;<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="mqTest" >重新生成该日数据</button> -->
								&nbsp;<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="mqTest" >刷新该日数据</button>
							</div>
						</shiro:hasPermission>

                        <!-- <div class="button-list">
                        	&nbsp;&nbsp;<input type="text" class="input_msg form_datetime date-input" name="testDate" id="testDate" readonly="readonly" style="background-color: #FFFFFF;"  />
                        </div> -->

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
									<th>品类</th>
									<th>品牌</th>
									<th>当日销售量</th>
									<th>当日销售额(RMB)</th>
									<th>当月销售量</th>
									<th>当月日均销售量</th>
									<th>当月销售额(RMB)</th>
									<th>年度销售量</th>
									<th>年度销售额(RMB)</th>
									<th>库存</th>
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

	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/yunJiPurchaseDaily/listYunJiPurchaseDaily.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
                {data:'productTypeName'},{data:'brandName'},
                {data:'daySaleCount'},{data:'daySaleAmount'},{data:'monthSaleCount'},{data:'monthAvgSaleCount'},{data:'monthSaleAmount'},
                {data:'yearSaleCount'},{data:'yearSaleAmount'},{data:'inventory'}
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.inventory == null){
                $('td:eq(10)', nRow).html('-');
            }
        };
        
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        function searchData() {
			$mytable.fn.reload();
		}
        
        //获得昨天时间
        var yesterday = new Date(new Date() - 24*60*60*1000);
        var y = yesterday.getFullYear();
        var m = yesterday.getMonth()+1;//获取当前月份的日期
        var d = yesterday.getDate();
        var day = y + "-" + (m<10?'0'+m:m) + "-" + d;
        $("#sDate").val(day);
	})

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
		language : 'zh-CN',
		format : "yyyy-mm-dd",
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		minView : 2,
		endDate : new Date(new Date() - 24*60*60*1000)
	});
	
	// 点击展开功能
	$(".unfold").click(function() {
		$(".form_con").toggleClass('hauto');
		if ($(this).text() == '展开功能') {
			$(this).text('收起功能');
		} else {
			$(this).text('展开功能');
		}
	})

	 //导出
     $("#exportOrder").on("click",function(){
    	var s = $('input[name="sDate"]').val();
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        $custom.request.ajax(serverAddr+'/yunJiPurchaseDaily/getOrderCount.asyn', form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		if(total==0){
        			swal({title: '导出数据为空！',type: 'warning',confirmButtonColor: '#4fa7f3'});
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/yunJiPurchaseDaily/exportYunJiPurchaseDaily.asyn";
        		var i =0;
            	if(!!form){
           		    for(var key in form){
           		    	if(i==0){
               	    		url +="?";
               	    	}else{
               	    		url +="&";
               	    	}
           		    	url +=key+"="+form[key];
           		    	i++;
           		    }
            	}
            	//window.open(url); 
            	$downLoad.downLoadByUrl(url)
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error("未知异常");
            		console.log(data.expMessage);
            	}else{
            		$custom.alert.error("未知异常");
            		console.log(data.message);

            	}
            }
        });
    });
	
	$("#mqTest").on("click",function(){
		var sDate = $("#sDate").val();
		if(sDate == null || sDate == ''){
			swal({title: '刷新时间未选择',type: 'warning',confirmButtonColor: '#4fa7f3'});
		}else{
			 $custom.alert.warning("确定刷新该日期?",function(){
				 $custom.request.ajax(serverAddr+'/yunJiPurchaseDaily/mqUpdateByDay.asyn',{"sDate":$("#sDate").val()},function(result){
 					console.log("log:"+result.state);
				});
			}) 	
		}
		
		
		
	})
</script>