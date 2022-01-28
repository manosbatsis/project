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
                                    <li class="breadcrumb-item"><a href="#">累计汇总表</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form_box">
                         <form id="form1">
                            <div>
                                <div class="form_item h35">
	                                 <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">标准条码 :</span>
	                                <input class="input_msg" type="text" name="groupCommbarcode">
	                                <span class="msg_title"><i class="red">*</i>统计月份 :</span>
	                                <input type="text" required="" parsley-type="text" class="input_msg date-input" name="monthStart" value='${nowStart}'>	~                                
	                                <input type="text" required="" parsley-type="text" class="input_msg date-input" name="monthEnd" value='${nowEnd}'>
	                                                                
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="reload">查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                            <div>
                                <div class="form_item h35">                                                       
	                                 <input class="input_msg" type="hidden" name="brandIds" id="brandIds">
	                                <span class="msg_title">商品名称 :</span>
	                                <input class="input_msg" type="text" name="goodsName">
	                                 <span class="msg_title">品牌 :</span>
	                                <select name="brandId" class="input_msg " id="brandId" multiple data-title="请选择">
	                                   <option value="">请选择</option>
	                                </select>
	                                <span class="msg_title">分类 :</span>
                                    <select name="typeId" class="input_msg" id="typeId">
	                                   <option value="">全部</option>
	                                </select>
                                                
                                </div>
                            </div>
                             </form>
                        </div>
                       <div class="row col-md-12 mt20">
                            <div class="button-list">
								<shiro:hasPermission name="buFinanceAdd_export">
									<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export" value="导出"/>
								</shiro:hasPermission>																
								<label id="labelTime" style="color: red;margin-left: 20px;"></label>
								<label id="labelStatus" style="color: red;"></label>
                            </div>
                        </div>
                        <div class="row col-12 mt20 table-responsive">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                	<th>序号</th>
                                	<th style="min-width: 100px">统计月份</th>
                                	<th style="min-width: 100px">事业部</th>
									<th style="min-width: 100px">品牌</th>
									<th style="min-width: 200px">分类</th>
									<th style="min-width: 100px">标准条码</th>
									<th style="min-width: 200px">商品名称</th>
									<th>累计采购入库数量</th>
									<th>累计入库残损数量</th>
									<th>累计移库入</th>
									<th>累计采购结转数量</th>
									<th>累计采购结转金额</th>
									<th>累计销售已上架数量</th>
									<th>累计出库残损数量</th>
									<th>累计移库出量</th>
									<th>累计销售结转数量</th>
									<th>累计销售结转金额</th>
									<th>累计销毁数量</th>
									<th>累计盘盈盘数量</th>
									<th>累计盘盈亏数量</th>
									<th>累计损益结转数量</th>
									<th>累计损益结转金额</th>						
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
});
//加载仓库
$module.merchandiseCat.getMerchandiseCat.call($('select[name="typeId"]')[0],'1',null);
$module.brand.loadParentBrandSelect.call($('select[name="brandId"]')[0],null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, {});
$(document).ready(function() {
    $(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        startDate:'2020-01-01 00:00:00',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
   });

    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={ 
        url:serverAddr+'/buFinanceAdd/buFinanceAddSummaryList.asyn?r='+Math.random(),
        columns:[{  //序号
            data : null,
            bSortable : false,
            className : "",
            render : function(data, type, row, meta) {
                // 显示行号
                var startIndex = meta.settings._iDisplayStart;
                return startIndex + meta.row + 1;
               }
            },{data:'month'},					
            {data:'buName'},{data:'brandName'},{data:'typeName'},{data:'groupCommbarcode'},{data:'goodsName'},
            {data:'warehouseNum'},{data:'inDamagedNum'},{data:'moveInNum'},
            {data:'purchaseEndNum'},{data:'purchaseEndAmount'},
            {data:'saleShelfNum'},{data:'outDamagedNum'},{data:'moveOutNum'},{data:'saleEndNum'},
            {data:'saleEndAmount'},{data:'destroyNum'},{data:'profitNum'},{data:'lossNum'},
            {data:'lossOverflowNum'},{data:'lossOverflowAmount'},
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    	$("#labelTime").text("生成时间："+aData.createDate);
    };
   
  //生成列表信息
  $('#datatable-buttons').mydatatables();
  
 
	
	$("#reload").click(function(){
		$("#labelTime").text("");
		$("#labelStatus").text("");		
		var brandIds = $('select[name="brandId"]').val();		
		$("#brandIds").val(brandIds);
		var monthStart = $('input[name="monthStart"]').val();
		 if(monthStart==null||monthStart==""||monthStart==undefined){
			$custom.alert.error("请输入统计月份第一个月份");
			return false;
		 }
		 var monthEnd = $('input[name="monthEnd"]').val();
		 if(monthEnd==null||monthEnd==""||monthEnd==undefined){
			$custom.alert.error("请输入统计月份第二个月份");
			return false;
		 }
		$mytable.fn.reload();
		 //生成列表信息
	    $('#datatable-buttons').mydatatables();
	});
  
	// 导出
	$("#export").on("click",function(){
		debugger;
		var monthStart = $('input[name="monthStart"]').val();
		 if(monthStart==null||monthStart==""||monthStart==undefined){
			$custom.alert.error("请输入统计月份第一个月份");
			return false;
		 }
		 var monthEnd = $('input[name="monthEnd"]').val();
		 if(monthEnd==null||monthEnd==""||monthEnd==undefined){
			$custom.alert.error("请输入统计月份第二个月份");
			return false;
		 }
		 var buId = $('select[name="buId"]').val();
		 var groupCommbarcode = $('input[name="groupCommbarcode"]').val();
		 var brandIds = $('select[name="brandId"]').val();
		 var typeId = $('select[name="typeId"]').val();
		 var goodsName = $('input[name="goodsName"]').val();
		 
		 if (undefined==buId) {
			 buId='';
		 }
		 if (undefined==groupCommbarcode) {
			 groupCommbarcode='';
		 }
		 if (undefined==brandIds) {
			 brandIds='';
		 }
		 if (undefined==typeId) {
			 typeId='';
		 }
		 if (undefined==goodsName) {
			 goodsName='';
		 }

		 
	 	 //创建生成excel任务
	 	 var url = serverAddr+"/buFinanceAdd/export.asyn?monthStart="+monthStart+"&monthEnd="+monthEnd+
	 	"&buId="+buId+"&groupCommbarcode="+groupCommbarcode+"&brandIds="+brandIds+"&typeId="+typeId+"&goodsName="+goodsName;  	 
	 	 $downLoad.downLoadByUrl(url);
		
	});




});
</script>
