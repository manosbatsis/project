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
                                    <li class="breadcrumb-item"><a href="#">事业部进销存汇总表</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form_box">
                         <form id="form1">
                            <div >
                                <div class="form_item h35">
                                     <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
	                                <input class="input_msg" type="hidden" name="depotIds" id="depotIds">
                                    <span class="msg_title"><i class="red">*</i>年月 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg date-input" name="ownMonth" value='<c:if test="${now != null }">${now }</c:if>'>
                                  	<span class="msg_title">商品名称 :</span>
                                    <input class="input_msg" type="text" name="productName">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="reload">查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                            <div >
                                <div class="form_item h35">      	                                                                  
	                                 <input class="input_msg" type="hidden" name="brandIds" id="brandIds">                                   
                                    <span class="msg_title">条码 :</span>
                                    <input class="input_msg" type="text" name="barcode">
                                   <span class="msg_title">标准品牌 :</span>
	                                <select name="brandId" class="input_msg" id="brandId" multiple data-title="请选择">
	                                   <option value="">请选择</option> 
	                                </select>
                                </div>
                            </div>
                              <div >
                                <div class="form_item h35">
                                    <span class="msg_title"><i class="red">*</i>仓库 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
		                                <select name="depotId" class="input_msg goods_select2" id="depotId">
		                                    <option value="">请选择</option>
		                                </select>
	                                </div>	 
	                                <span class="msg_title">货号 :</span>
                                    <input class="input_msg" type="text" name="goodsNo">                              
                                </div>
                            </div>
                             </form>
                        </div>
                       <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="buSummary_createTask">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="createTask" value="生成excel"/>
                                </shiro:hasPermission>

                                <shiro:hasPermission name="buSummary_buFlashInventorSummary">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashReport" value="刷新报表"/>
                                    <!-- <label for="syn">
                                        <input type="checkbox" id="syn" name="syn"/>同步数据
                                    </label>
                                    <input type="hidden"  id="selectTime" required="" > -->
                                </shiro:hasPermission>
                                <!-- none,block -->
                                <div for="flashForward" style="display: none;">
                                  <input type="checkbox" id="flashForward" name="flashForward"/>刷新已结转
                                </div>
                            </div>
                        </div>
                        <div class="row mt20">
                             <label id="labelTime" style="color: red;width: 300px;"></label> <label id="labelAccountState" style="color: red;width: 200px;"></label>
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                    <th style="vertical-align: middle;" rowspan="2">序号</th>
                                    <th style="vertical-align: middle;min-width: 100px" rowspan="2">仓库</th>
                                    <th style="vertical-align: middle;min-width: 100px" rowspan="2">事业部</th>
                                    <th style="vertical-align: middle;min-width: 100px" rowspan="2">货号</th>
                                    <th style="vertical-align: middle;min-width: 150px" rowspan="2">商品名称</th>
                                    <th style="vertical-align: middle;min-width: 100px" rowspan="2">品牌</th>
                                    <th style="vertical-align: middle;min-width: 80px" rowspan="2">条码</th>
                                    <th style="vertical-align: middle;" rowspan="2">8位码</th>
                                    <th style="vertical-align: middle;" rowspan="2">理货单位</th>
                                    <!-- <th style="vertical-align: middle;" rowspan="2">单价</br>HKD</th> -->
                                    <th style="white-space:nowrap;" colspan="8">数量</th>
                                    <th style="white-space:nowrap;">金额</th>
                                </tr>
                                <tr>
                                    <th>本月期</br>初库存</th>
                                    <th>本月正常品</br>期初库存</th>
                                    <th>本月残次品</br>期初库存</th>
                                    <th>本月入库</th>
                                    <th>本月出库</th>
                                    <th>来货残次</th>
                                    <th>出库残次</th>
                                    <th>好品损益</th>
                                    <th>坏品损益</th>
                                    <th>损益</th>
                                    <th>本月期</br>末库存</th>
                                    <th>本月正常品</br>期末库存</th>
                                    <th>本月残次品</br>期末库存</th>                                                                        
                                    <th>销售未确认</th>
                                    <th>采购未上架</th>
                                    <th>本期调拨在途</th>                                   
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
<script type="text/javascript">
$("button[type='reset']").click(function(){
     $(".goods_select2").each(function(){
        $(this).val("");
    }); 
     $("#depotId").select2({
		 language:"zh-CN",
		 placeholder: '请选择',
		 multiple: true,
		 closeOnSelect: true
	});
     
});
//加载仓库
$module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
//加载标准品牌
$module.brand.loadParentBrandSelect.call($('select[name="brandId"]')[0],null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
$(document).ready(function() {
    $("#depotId").select2({
		 language:"zh-CN",
		 placeholder: '',
		 multiple: true,
		 closeOnSelect: true
	});
    $(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        endDate: new Date(),
	        language:  'zh-CN',
   });
/*    $("#selectTime").datetimepicker({
       language:  'zh-CN',
       format: "yyyy-mm-dd",
       autoclose: 1,
       todayHighlight: 1,
       startView: 2,
       minView: 2
   }); */

    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url:serverAddr+'/buSummary/buInventorSummaryList.asyn?r='+Math.random(),
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
            {data:'depotName'},{data:'buName'},{data:'goodsNo'},{data:'goodsName'},{data:'brandName'},{data:'barcode'},{data:'factoryNo'},{data:'unitLabel'},{data:'monthBeginNum'},{data:'monthBeginNormalNum'},{data:'monthBeginDamagedNum'},
            { //操作
            	orderable: false,
            	width: "80px",
            	data: null,
            	render: function (data, type, row, meta) {
            	    return '<a href="javascript:details('+row.merchantId+','+row.depotId+','+row.buId+',\''+row.ownMonth+'\',\''+row.goodsNo+'\',\'1\',\''+row.unit+'\')">'+row.monthInstorageNum+'</a>';
            	}
            }, 
            { //操作
            	orderable: false,
            	width: "80px",
            	data: null,
            	render: function (data, type, row, meta) {
            	    return '<a href="javascript:details('+row.merchantId+','+row.depotId+','+row.buId+',\''+row.ownMonth+'\',\''+row.goodsNo+'\',\'0\',\''+row.unit+'\')">'+row.monthOutstorageNum+'</a>';
            	}
            },
            {data:'monthInBadNum'},{data:'monthOutBadNum'},{data:'profitlossGoodNum'},{data:'profitlossDamagedNum'},{data:'monthProfitlossNum'},{data:'monthEndNum'},{data:'monthEndNormalNum'},
            {data:'monthEndDamagedNum'},
            {data:'monthSaleUnconfirmedNum'},{data:'monthPurchaseNotshelfNum'},{data:'monthTransferNotshelfNum'},
     
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    	$("#labelTime").text("生成时间："+aData.createDate);

        if (!${userType == '1' || userType == '2'}) {
            $('td:eq(17)', nRow).css("display","none");
        }
    };
   
  //生成列表信息
  $('#datatable-buttons').mydatatables();
  if (!${userType == '1' || userType == '2'}) {
      $(".hiddenCol").css("display","none");
  }
});
$("#reload").click(function(){
	$("#labelTime").text("");
	var depotId = $('select[name="depotId"]').val();
	var ownMonth = $('input[name="ownMonth"]').val();
	var brandIds = $('select[name="brandId"]').val();
	var depotIds = $('select[name="depotId"]').val();
	if(ownMonth==null||ownMonth==""||ownMonth==undefined){
		$custom.alert.error("请输年月");
		return false;
	}
	$("#brandIds").val(brandIds);
	$("#depotIds").val(depotIds);
	$mytable.fn.reload();
	 //生成列表信息
    $('#datatable-buttons').mydatatables();    
	 //查询月结状态
    var urlp = serverAddr+'/summary/getMonthlyAccount.asyn';
	$custom.request.ajax(urlp,{"depotIds":depotIds.toString(),"settlementMonth":ownMonth},function(result){
		 //状态：1未转结 2 已转结
 		$("#labelAccountState").text("月结状态：未结转"); 
		 if(result.state==200&&result.data.state=='2'){
			 
			 $("#labelAccountState").text("月结状态：已结转");
		 }
		
	});
	 
});

//详情
function details(merchantId,depotId,buId,ownMonth,goodsNo,operateType,unit) {
	$module.revertList.serializeListForm() ;
    $module.load.pageReport("act=120151&merchantId="+merchantId+"&depotId="+depotId+"&buId"+buId+"&ownMonth="+ownMonth+"&goodsNo="+goodsNo
    		                 +"&operateType="+operateType+"&unit="+unit);
} 


$("#flashReport").click(function(){
	var depotIds = $('select[name="depotId"]').val();
	var ownMonth = $('input[name="ownMonth"]').val();
	//var syn = $('input[name="syn"]').is(':checked');
	//var selectTime = $('#selectTime').val();
	var buId = $('select[name="buId"]').val();
	var flashForward = $('input[name="flashForward"]').is(':checked');
	if(depotIds==null||depotIds==""||depotIds==undefined||ownMonth==null||ownMonth==""||ownMonth==undefined){
		$custom.alert.error("请输入仓库和年月");
		return false;
	}
	
	var depotIdsStr=depotIds.toString();	
	$custom.alert.warning("确定刷新仓库本月报表?",function(){
		setTimeout(function () {
			$("#flashReport").attr('disabled',false);
		}, 1000);
		var urlp = serverAddr+'/buSummary/buFlashInventorSummary.asyn';
		$custom.request.ajax(urlp,{"ownMonth":ownMonth,"flashForward":flashForward,"depotIds":depotIdsStr,"buId":buId},function(result){
			if(result.state==200&&result.data.code=='00'){
				$custom.alert.success("正在刷新,请稍后再查询");
				$mytable.fn.reload();
			}else{
				$custom.alert.error(result.data.message);
			}
		});
	})
	
})

$("#createTask").on("click",function(){

	 var depotIds = $('select[name="depotId"]').val();
	 var ownMonth = $('input[name="ownMonth"]').val();	  
	 var select = document.getElementById("depotId");
	 	var depotNameAlls= [];
	 	var depotIdAlls =[];
	 	var depotNames=[];
	    for(i=0;i<select.length;i++){
	    	if (select[i].value==null||select[i].value==""||select[i].value==undefined) {	    		
			}else {
				depotIdAlls.push(select[i].value);
				if (select[i].label!='请选择') {
					depotNameAlls.push(select[i].label);	
				}
				if (select[i].selected==true&&select[i].label!='请选择') {
					depotNames.push(select[i].label);
				}
			}	    	
	    }
	 if(ownMonth==null||ownMonth==""||ownMonth==undefined){
		$custom.alert.error("请输入仓库和年月");
		return false;
	 }
	 var depotIdsStr=depotIds.toString();
	 var depotIdAllsStr =depotIdAlls.toString();
	 var depotNameAllsStr =depotNameAlls.toString();
	 var depotNamesStr=depotNames.toString();
	 var buId = $('select[name="buId"]').val();
  	 //检查文件是否已生成
  	 var checkUrl = serverAddr+'/buSummary/createTask.asyn';; 
  	 $custom.request.ajax(checkUrl,{"depotIds":depotIdsStr,"depotNames":depotNamesStr,"ownMonth":ownMonth,"depotIdAlls":depotIdAllsStr,"depotNameAlls":depotNameAllsStr,"buId":buId},function(result){
	     if(result.state==200&&result.data.code=='00'){
			  $custom.alert.success("请在报表任务列表查看进度");
		  }else{
			  $custom.alert.error(result.data.message);
		  }
     }); 
  	 
   
    
});

</script>
