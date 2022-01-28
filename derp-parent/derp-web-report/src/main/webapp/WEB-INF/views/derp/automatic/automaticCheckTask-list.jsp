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
								<li class="breadcrumb-item"><a href="#">自动化校验</a></li>
								<li class="breadcrumb-item"><a href="#">自动校验任务列表</a></li>
							</ol>
						</div>
					</div>
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
								    <span class="msg_title">任务类型 :</span>
  									<select name="taskType" class="input_msg" ></select>
  									<span class="msg_title">创建人 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="createName" name="createName" >
                                    <span class="msg_title">核对日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="checkStartDate" id="checkStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="checkEndDate" id="checkEndDate">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								<div class="form_item h35">
								    <span class="msg_title">核对结果 :</span>
  									<select class="input_msg" name="checkResult"></select>
  									<span class="msg_title">处理状态 :</span>
  									<select class="input_msg" name="state"></select>
  									<span class="msg_title">创建日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createDate" id="createDate">
								</div>
								<div class="form_item h35">
								<span class="msg_title">出仓仓库 :</span>
									<select name="outDepotId" class="input_msg">
										<option value="">请选择</option>
									</select>
  								   <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformCode" ></select>
		                             <span class="msg_title">店铺 :</span>
		                              <div style="min-width:136px;max-width: 635px;display: inline-block;border:1px solid #D3D3D3">
		                                  <select name="shopCode" class="input_msg goods_select2" >
		                                      <option value="">请选择</option>
		                                      <c:forEach items="${shopList }" var="shop">
		                                          <option value="${shop.shopCode }">${shop.shopName}</option>
		                                      </c:forEach>
				                          </select>
                                  </div>
								</div>
								<div class="form_item h35">
								   <span class="msg_title">任务编码 :</span>
                                   <input type="text" parsley-type="text" class="input_msg" id="taskCode" name="taskCode" >
								</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
	                         <shiro:hasPermission name="POPCheckTask_import">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="POPCheckTask" value="创建POP核对任务"/>
                             </shiro:hasPermission>
                             <shiro:hasPermission name="POPAmountCheckTask_import">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="POPAmountCheckTask" value="创建POP金额核对任务"/>
                             </shiro:hasPermission>
                             <shiro:hasPermission name="depotCheckTask_import">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="depotCheckTask" value="创建仓库核对任务"/>
                             </shiro:hasPermission>
                             <shiro:hasPermission name="POPCheckTask_import">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="markButton" value="标记"/>
                             </shiro:hasPermission>
                             <shiro:hasPermission name="POPCheckTask_del">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="del();" value="删除"/>
                             </shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div  class="row mt20">
                             <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>任务编码</th>
                            <th>任务类型</th>
                            <th>核对日期</th>
                            <th>创建人</th>
                            <th>创建时间</th>
                            <th>核对结果</th>
                            <th>处理状态</th>
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
				  		<jsp:include page="/WEB-INF/views/modals/17013.jsp" />
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
            url:serverAddr+'/automaticCheckTask/listAutomaticCheckTask.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'taskCode'},{data:'taskTypeLabel'},{data:'checkEndDate'},{data:'createName'},
            {data:'createDate'},{data:'checkResultLabel'},{data:'stateLabel'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                    return "";
                }
            },
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	  $('td:eq(1)', nRow).html(aData.taskCode==null?'':aData.taskCode);
        	  $('td:eq(2)', nRow).html(aData.taskTypeLabel==null?'':aData.taskTypeLabel);
        	  $('td:eq(4)', nRow).html(aData.createName==null?'':aData.createName);
        	  $('td:eq(5)', nRow).html(aData.createDate==null?'':aData.createDate);
        	  $('td:eq(6)', nRow).html(aData.checkResultLabel==null?'':aData.checkResultLabel);
        	  $('td:eq(7)', nRow).html(aData.stateLabel==null?'':aData.stateLabel);
        	  
        	  if((aData.checkStartDate) && (aData.checkEndDate) ){
        		  var checkStart=new Date(aData.checkStartDate);
        		  var checkEnd=new Date(aData.checkEndDate);
        		  
        		  var checkStartDate =checkStart.getFullYear() + '-' + (checkStart.getMonth() + 1) + '-' + checkStart.getDate() 
        		  + ' ' + checkStart.getHours() + ':' + checkStart.getMinutes() + ':' + checkStart.getSeconds();
        		  var checkEndDate =checkEnd.getFullYear() + '-' + (checkEnd.getMonth() + 1) + '-' + checkEnd.getDate() 
        		  + ' ' + checkEnd.getHours() + ':' + checkEnd.getMinutes() + ':' + checkEnd.getSeconds();
        	  	$('td:eq(3)', nRow).html(checkStartDate+"<span>&nbsp&nbsp-&nbsp&nbsp</span>"+checkEndDate);
        	  }
        	  
        	  if(aData.state=="3"){
        		  $('td:eq(8)', nRow).html("<shiro:hasPermission name='automaticCheckTask_export'><a href='javascript:download("+aData.id+");'>导出</a>&nbsp&nbsp&nbsp</shiro:hasPermission>"
                    		+"<a href='javascript:void(0);'>展开</a><input type='hidden' value='"+aData.id+"'/>");
        	  }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        searchData() ;
        
        function searchData() {
			$mytable.fn.reload();
		}
      //加载仓库的下拉数据
	  $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null, {});
	  $module.constant.getConstantListByNameURL.call($('select[name="checkResult"]')[0],"automaticCheckTask_checkResultList",null);
	  $module.constant.getConstantListByNameURL.call($('select[name="taskType"]')[0],"automaticCheckTask_taskTypeList",null);
	  $module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"automaticCheckTask_stateList",null);
	  $module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
	})

    // 日期选择
    $(".date-input").datetimepicker({
         language:  'zh-CN',
         format: "yyyy-mm-dd hh:ii:ss",
         autoclose: 1,
         todayHighlight: 1,
         startView: 2,
         minView: 2
    });
	   // 点击展开功能显示某个核对任务
    $('#datatable-buttons tbody').on( 'click', 'tr td:nth-child(9) a:nth-child(2)', function () {
    	var thisObj=$(this);
           if($(this).text() == '展开'){
              $(this).text('收起');
              $(this).css({"color":"#007bff","cursor":"pointer"});
              var id=$(this).next().val();
              var editItem="<tr id='goodsList'><td></td><td colspan='8'><table  width='100%'>"
					 +"<tr ><th class='nowrap'>数据源</th><th class='nowrap'>核对仓库</th><th class='nowrap'>平台</th>"
					 +"<th class='nowrap'>店铺</th><th class='nowrap'>备注</th></tr>";   
              $custom.request.ajax(serverAddr+"/automaticCheckTask/listTaskDetailsById.html", {"id" : id}, function(data){
      			  	var result = data.data;
      				editItem+="<tr><td class='nowrap'>"+(result.dataSourceLabel==null?'':result.dataSourceLabel)+"</td><td class='nowrap'>"+(result.outDepotName==null?'':result.outDepotName)+"</td>"
      				+"<td class='nowrap'>"+(result.storePlatformLabel==null?'':result.storePlatformLabel)+"</td><td class='nowrap'>"+(result.shopName==null?'':result.shopName)+"</td>"
      				+"<td class='nowrap'>"+(result.remark==null?'':result.remark)+"</td></tr>"; 
  					thisObj.parent("td").parent("tr").after(editItem);
         		}); 
          }else if($(this).text() == '收起'){
              $(this).text('展开');
              $(this).css({"color":"#007bff","cursor":"pointer"});
      		  thisObj.parent("td").parent("tr").next().html("");
          }
    	});
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    $("#depotCheckTask").click(function(){
    	$module.load.pageReport("act=140032");
    }) ;

    //创建POP核对任务
   	$("#POPCheckTask").click(function(){
   		$module.load.pageReport("act=14004");
   	});
    
    //创建POP金额核对任务
   	$("#POPAmountCheckTask").click(function(){
   		$module.load.pageReport("act=14005");
   	});

    //删除
    function del(){
    	$custom.request.delChecked(serverAddr+'/automaticCheckTask/delAutomaticCheckTask.asyn');
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
    //标记
   	$("#markButton").click(function(){
   		var flag=0;
   	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("请选择一条记录！");
			return ;
		}
		var idList=ids.split(",");
		for (var k = 0; k < idList.length; k++) {
			 $custom.request.ajax(serverAddr+"/automaticCheckTask/listTaskDetailsById.html", {"id" : idList[k]}, function(data){
				  	var result = data.data;
				  	// 状态为已完成并且没有被标记过
					if(result.state=="3" && result.isMark=="0"){
						 flag++;
						if(idList.length==flag){
							 $m17013.init(ids);
						}
					}else if(result.state!="3"){
						$custom.alert.warningText("任务编码为:"+result.taskCode+"处理状态不是已完成");
						return ;
					}else if(result.isMark!="0"){
						$custom.alert.warningText("任务编码为:"+result.taskCode+"已标记过");
						return ;
					}
	  		}); 
		}
   	});
	   $("[name='storePlatformCode']").change(function() {
			var storePlatformCode = $("[name='storePlatformCode']").val();   	
			 $custom.request.ajax(serverAddr+"/automaticCheckTask/changeShopCodeLabel.asyn",{'storePlatformCode':storePlatformCode,'outDepotId':null},function(data){
		            var dataRole = data.data.shopList;  
		            $("[name='shopCode']").empty(); 
		            var html = "<option value=''>请选择</option>";
		             for(var i=0;i<dataRole.length;i++){
		                html += "<option value='"+dataRole[i].shopCode
		                +"'>"+dataRole[i].shopName+"</option>";
		            } 
		            $("[name='shopCode']").append(html);
		        });
});
	 // 导出
	   function download(id){
	   	 var url = serverAddr+"/automaticCheckTask/downLoad.asyn?id="+id;
	   	 $downLoad.downLoadByUrl(url);
	   }
</script>