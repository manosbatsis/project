<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                                <li class="breadcrumb-item"><a href="#">报表管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:list();">存货跌价准备</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div>
                                <div class="form_item h35">
                                	<span class="msg_title">报表月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="reportMonth" name="reportMonth" value="${month }" readonly> 
                                    <span class="msg_title">仓库 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
	                                    <select name="depotId" id="depotId"  class="selectpicker show-tick form-control" multiple data-title="请选择">
	                                    </select>
	                                    <input class="input_msg" type="hidden" name="depotIds" id="depotIds">
                                    </div>
                                    <span class="msg_title">商品货号:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData()' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35" style="min-width:136px;max-width: 635px;display: inline-block;">
                                    <span class="msg_title">计提比例 :</span>
                                    <select name="depreciationReserveProportion" id="depreciationReserveProportion"  class="input_msg">
                                        <option value="">全部</option>
                                        <option value="1">100%</option>
                                        <option value="0.95">95%</option>
                                        <option value="0.7">70%</option>
                                        <option value="0.3">30%</option>
                                        <option value="0">0%</option>
                                    </select>
                                    <span class="msg_title">效期 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
                                    <select name="effectiveInterval" id="effectiveInterval" class="selectpicker show-tick form-control" multiple data-title="请选择">
                                    </select>
                                    <input class="input_msg" type="hidden" name="effectiveIntervals" id="effectiveIntervals">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="inventoryFallingPriceReserves_exportReserves">
                                <input type="button" id="exportReport" class="btn btn-find waves-effect waves-light btn-sm" value="导出"/>
                            </shiro:hasPermission>

                            <shiro:hasPermission name="inventoryFallingPriceReserves_flashReserves">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashReport" value="刷新报表"/>
                            </shiro:hasPermission>

							<label id="labelTime" style="color: red;margin-left: 20px;"></label>
	                        <label id="labelStatus" style="color: red;"></label>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>公司名称</th>
                                <th>仓库名称</th>
                                <th>报表月份</th>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>生产日期</th>
                                <th>失效日期</th>
                                <th>批次号</th>
                                <th>总数量</th>
                                <th>库存类型</th>
                                <th>剩余失效(天)</th>
                                <th>总效期(天)</th>
                                <th>剩余效期占比</th>
                                <th>效期区间</th>
                                <th>跌价准备比例</th>
                                <th>单价</th>
                                <th>计提总额</th>
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
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="effectiveInterval"]')[0],"fallingPrice_effectiveIntervalList",null);
    //加载仓库下拉
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0], null, {"type":"a,c","isInvertoryFallingPrice":"1"});
    $('.selectpicker').selectpicker({width: '250px'});
    $('.selectpicker').selectpicker('refresh');
    
    $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
    $(".selectpicker").prev().css({"z-index":"99"});
    $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
    
    $(document).ready(function() {

        //重置按钮
        $("button[type='reset']").click(function(){
        	$('.selectpicker').selectpicker({width: '250px'});
        	$('.selectpicker').selectpicker('val','');
        	$('.selectpicker').selectpicker('refresh');
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


        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/inventoryFallingPriceReserves/listInventoryFallingPriceReserves.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" value='+data.id+'  name="iCheck" class="iCheck">';
                }
            },
                {data:'merchantName'},{data:'depotName'},
                {data:'reportMonth'},
                {data:'goodsNo'},{data:'goodsName'},
                {data:'productionDate',
                    'render': function (data, type, row, meta) {
                        if(data!=null&&data!=''){
                            return data.substring(0,10) ;
                        }else{
                            return '';
                        }
                    }
                },
                {data:'overdueDate',
                        'render': function (data, type, row, meta) {
                            if(data!=null&&data!=''){
                                return data.substring(0,10);
                            }else{
                                return '';
                            }
                        }
                },{data:'batchNo'},{data:'surplusNum'},
                {data:'inverntoryTypeLabel'},
                {data:'surplusDays'},{data:'totalDays'},
                {data:'surplusProportion',
                    'render': function (data, type, row, meta) {
                        if( data != '0' ) {
                        	
                        	data = parseFloat(data) * 100 ;
                        	data = data.toFixed(2) ;
                        	
                            return data += "%";
                        }else{
                        	return "";
                        }
                    }
                },
                {data:'effectiveIntervalLabel'},{data:'depreciationReserveProportion',
                	'render': function (data, type, row, meta) {
                        if( data != '0' ) {
                        	
                        	data = parseFloat(data) * 100 ;
                        	data = data.toFixed(2) ;
                        	
                            return data += "%";
                        }else{
                        	return 0 ;
                        }
                    }	
                },{data:'settlementPrice'},{data:'totalProvision'}
                
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.goodsName != null && aData.goodsName.length>25){
                $('td:eq(5)', nRow).html(aData.goodsName.substring(0, 25)+"...");
                $('td:eq(5)', nRow).attr("title",aData.goodsName);
            }
            
			var text = $("#labelTime").text() ;
        	
        	if(!text && aData.createDate){
        		var labelTime = compareTime(text , aData.createDate ) ;
        		
        		if(labelTime != ""){
	        		$("#labelTime").text("生成时间："+ labelTime);
        		}
        		
        	}
        }
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        //searchData() ;
    } );

    function list(){
    	$module.load.pageReport("act=12013");
    }


    function searchData(){
    	
    	$("#labelTime").text("");
    	
    	var effectiveIntervals = $('select[name="effectiveInterval"]').val();
    	if(effectiveIntervals){
	    	effectiveIntervals = effectiveIntervals.join(",") ;
	    	$("#effectiveIntervals").val(effectiveIntervals);
    	}
    	
    	var depotIds = $('select[name="depotId"]').val();
    	if(depotIds){
	    	depotIds = depotIds.join(",") ;
	        $("#depotIds").val(depotIds);
    	}
        $mytable.fn.reload();
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
    })
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
      //导出
    $("#exportReport").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        $custom.request.ajax(serverAddr+'/inventoryFallingPriceReserves/getCount.asyn', form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>50000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		if(total==0){
        			swal({title: '导出数据为空！',type: 'warning',confirmButtonColor: '#4fa7f3'});
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/inventoryFallingPriceReserves/exportInventoryFallingPriceReserves.asyn";
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
      
    $("#flashReport").on("click",function(){

            $custom.alert.warning("确定刷新该月份?",function(){
                $custom.request.ajax(serverAddr+'/inventoryFallingPriceReserves/flashInventoryFallingPriceReserves.asyn',{"reportMonth":$("#reportMonth").val() },function(result){
                    if(result.state==200&&result.data.code=='00'){
                        $custom.alert.success("正在刷新，稍后点击【查询】，查询结果");
                    }else{
                        $custom.alert.error(result.data.message);
                    }
                });
            })

    })
</script>
