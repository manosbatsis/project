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
								<li class="breadcrumb-item"><a href="#">销售目标达成率</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<ul class="nav nav-tabs">
						<li class="nav-item">
							<a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageReport('act=19001');">销售类型达成</a>
						</li>
						<li class="nav-item">
							<a href="#" data-toggle="tab" class="nav-link " onclick="$module.load.pageReport('act=19002');">电商平台达成</a>
						</li>
						<li class="nav-item">
							<a href="#" data-toggle="tab" class="nav-link " onclick="$module.load.pageReport('act=19003');">店铺计划达成</a>
						</li>
	           		</ul>
	           		<div class="tab-content">
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
                                    <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                    </select>
                                    <span class="msg_title">统计时间 :</span>
                                    <select id="time" class="input_msg" style="width: 60px;">
                                    	<option value="month">月份</option>
                                    	<option value="season">季度</option>
                                    	<option value="year">年份</option>
                                    </select>
                                    <input style="margin-left: -5px;" type="text" class="input_msg form_datetime date-input" name="month" id="month" style="font-size:13px;" value="${ month}" readonly>
                                    <input style="margin-left: -5px; display:none;" type="text" class="input_msg form_datetime date-input" name="season" id="season" style="font-size:13px;" readonly>
                                    <input style="margin-left: -5px; display:none;" type="text" class="input_msg form_datetime date-input" name="year" id="year" style="font-size:13px;" readonly>
                                    <span class="msg_title">商品名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsName" name="goodsName">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData() ;' >查询</button>
                                        <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick ='formReset() ;'>重置</button>
                                    </div>
								</div>
								
								<div class="form_item h35">
									<span class="msg_title">标准条码 :</span> 
									<input type="text" required="" parsley-type="text" class="input_msg" id="commbarcode" name="commbarcode">
								</div>
								<input type="hidden" name="type" value="1">
								<a href="javascript:" class="unfold">展开功能</a>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							 <shiro:hasPermission name="saleTargetAchievement_export">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportMain" value="导出"/>
                             </shiro:hasPermission>
							 <shiro:hasPermission name="saleTargetAchievement_flash">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashReport" value="刷新"/>
								 <!-- <label for="syn">
									 <input type="checkbox" id="syn" name="syn">同步数据
								 </label> -->
								 <input type="hidden"  id="selectTime" required="" >
							 </shiro:hasPermission>

							 <label id="labelTime" style="color: red;margin-left: 20px;"></label>
	                        <label id="labelStatus" style="color: red;"></label>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>事业部</th>
									<th>归属月份</th>
									<th>标准条码</th>
									<th>商品名称</th>
									<th>标准品牌</th>
									<th>To B销量 / 完成率</th>
									<th>To C销量 / 完成率</th>
									<th>总销量 / 总完成率</th>
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
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
	$(document).ready(function() {
		//渲染时间选择器
		renderMonth() ;
		renderSeasonDate() ;
		renderYear() ;
		
		$("#time").on('change', function(){
			let val = $(this).val() ;
			
			$(".date-input").hide() ;
			
			if(val == 'month'){
				$(".date-input").val('') ;
				$("#month").show() ;
			}else if(val == 'season'){
				$(".date-input").val('') ;
				$("#season").show() ;
			}else if(val == 'year'){
				$(".date-input").val('') ;
				$("#year").show() ;
			}
		});
		
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/saleTargetAchievement/listSaleTargetAchievement.asyn?r='+Math.random(),
            columns:[
            /* 每一列的数据 */
                {data:'buName'},{data:'month'},{data:'commbarcode'},{data:'goodsName'},
                {data:'brandParent'},
                { 
                	data: null,
                	render: function (data, type, row, meta) {
                		let html = "" ;
                		
                		html += row.toBNum ;
                		html += "<br>" ;
                		
                		let toBRate = parseFloat(row.toBRate) ;
                		toBRate = toBRate * 100; 
                		toBRate = toBRate.toFixed(2) ;
                		html += toBRate + "%" ;
                		
                		
                	    return html;
                	}
                },
                { 
                	data: null,
                	render: function (data, type, row, meta) {
                		
						let html = "" ;
                		
                		html += row.toCNum ;
                		html += "<br>" ;
                		
                		let toCRate = parseFloat(row.toCRate) ;
                		toCRate = toCRate * 100; 
                		toCRate = toCRate.toFixed(2) ;
                		html += toCRate + "%" ;
                		
                	    return html;
                	}
                },
                { 
                	data: null,
                	render: function (data, type, row, meta) {
                		
						let html = "" ;
                		
                		html += row.num ;
                		html += "<br>" ;
                		
                		let rate = parseFloat(row.rate) ;
                		rate = rate * 100; 
                		rate = rate.toFixed(2) ;
                		html += rate + "%" ;
                		
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
        
	})

	//导出
    $("#exportMain").on("click",function(){
       //根据筛选条件导出
       var url = serverAddr+"/saleTargetAchievement/exportSaleTargetAchievement.asyn?r="+Math.random() ;
       var params = $("#form1").serialize() ;
       $custom.alert.warning("确定导出筛选条件内容？",function(){
    	   $downLoad.downLoadByUrl(url + "&" +params) ;
       });
   });

	
	$("#flashReport").on("click",function(){
		
		var syn = false ; 
		if($("#syn").is(':checked')) {
			syn = true ;
		}
		
		 $custom.alert.warning("确定刷新",function(){
			 var params = $("#form1").serializeObject() ;
			 $custom.request.ajax(serverAddr+'/saleTargetAchievement/flashSaleTargetAchievement.asyn', params, function(result){
	             if(result.state==200&&result.data.code=='00'){
	            	 $custom.alert.success("正在刷新，请稍后");
	              }else{
	                  $custom.alert.error(result.data.message);
	              }
			});
		}) 	;
		
		
		
	})
	
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
	
	// 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
	
	/**
	* 月份初始化
	*/
	function renderMonth(){
		laydate.render({
	        elem: '#month', //指定元素
	        type: 'month',
	        format: 'yyyy-MM',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        trigger: 'click'
	    });
	}
	
	/**
	* 年份初始化
	*/
	function renderYear(){
		laydate.render({
	        elem: '#year', //指定元素
	        type: 'year',
	        format: 'yyyy',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        trigger: 'click'
	    });
	}
	
    /**
     * 季度初始化
     * @param ohd 季度input dom对象非jquery对象
     */
    function renderSeasonDate(){
    	var ele = $('#season');
    	laydate.render({
            elem: '#season',
            type: 'month',
            format: 'yyyy-第M季度',
            min: "1900-1-1",
            max: "2999-12-31",
            btns: ['confirm'],
            ready: function () {
                var hd = $("#layui-laydate" + ele.attr("lay-key"));
                if (hd.length > 0) {
                    hd.click(function () {
                        ren($(this));
                    });
                }
                ren(hd);
            },

            done: function (value) {
                var finaltime = '';
                if (value){
                    value = value.split('-');
                    var year = value[0];
                    var season = value[1];
                    if (season == '第1季度'){
                        var timeend = '03-31';
                        finaltime =  year + '-' + timeend;
                    }
                    if (season == '第2季度'){
                        var timeend = '06-30';
                        finaltime =  year + '-' + timeend
                    }
                    if (season == '第3季度'){
                        var timeend = '09-30';
                        finaltime = year + '-' + timeend
                    }
                    if (season == '第4季度'){
                        var timeend = '12-31';
                        finaltime = year + '-' + timeend
                    }
                }
                $('#season').val(finaltime);
            
            }

        });
        var ren = function (thiz) {
            var mls = thiz.find(".laydate-month-list");
            mls.each(function (i, e) {
                $(this).find("li").each(function (inx, ele) {
                    var cx = ele.innerHTML;
                    if (inx < 4) {
                        ele.innerHTML = cx.replace(/月/g, "季度").replace(/一/g, "第1").replace(/二/g, "第2").replace(/三/g, "第3").replace(/四/g, "第4");
                    } else {
                        ele.style.display = "none";
                    }
                });
            });
        }
    }
    
    function formReset(){
    	$(".date-input").hide() ;
    	$("#month").show() ;
    	$("#form1")[0].reset() ;
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
    
    function searchData() {
    	
		var time = $("#time").val() ;
    	
    	if( 'season' == time && !$("#season").val()){
    		$custom.alert.error("请选择季度");
    		
    		return ;
    	}
    	
    	if( 'year' == time && !$("#year").val()){
    		$custom.alert.error("请选择年份");
    		
    		return ;
    	}
    	
		$mytable.fn.reload();
	}
</script>