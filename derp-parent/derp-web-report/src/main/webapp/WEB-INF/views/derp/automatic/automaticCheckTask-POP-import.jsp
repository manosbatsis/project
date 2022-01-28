<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/automaticCheckTask/toPage.html');">自动检验任务列表</a></li>
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/automaticCheckTask/toAddPage.html');">新增核对任务</a></li>
                    </ol>
                    </div>
            </div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                        <span class="msg_title"><i class="red"  id="outDepotId_i">*</i>出仓仓库 :</span>
							<select name="outDepotId" class="input_msg">
						    <option value="">请选择</option>
						</select>
                        </div>
                        <div class="edit_item">
                         <span class="msg_title"><i class="red" id="checkDate_i">*</i>核对日期 :</span>
                                <input type="text" class="input_msg form_datetime date-input" name="checkStartDate" id="checkStartDate">
                                -
                                <input type="text" class="input_msg form_datetime date-input" name="checkEndDate" id="checkEndDate">
	                      </div>     
	                      <div class="edit_item"></div>
                    </div>
                    <div class="edit_box mt20">
                       <div class="edit_item">
                         <span class="msg_title"><i class="red"  id="storePlatformCode_i">*</i>平台名称 :</span>
                         <select class="input_msg" name="storePlatformCode" ></select>
                       </div>
                        <div class="edit_item">
                             <span class="msg_title"><i class="red"  id="shopCode_i">*</i>店铺 :</span>
                                 <select name="shopCode" class="input_msg goods_select2" >
                                     <option value="">请选择</option>
                                     <c:forEach items="${shopList }" var="shop">
                                         <option value="${shop.shopCode }">${shop.shopName}</option>
                                     </c:forEach>
	                          </select>
                    </div>    
	                      <div class="edit_item"></div>
                    </div>
                 <div class="edit_box mt20">
                        <div class="edit_item">
                        <span class="msg_title"><i class="red" id="file_i">*</i>文件 :</span>
                         		<button type="button" class="btn btn-gradient btn-file"
									id="btn-file">选择文件</button>
								<form enctype="multipart/form-data" id="form-upload">
									<input type="file" name="file" id="file" class="btn-hidden">
								</form>
                        </div>
                           <div class="edit_item"></div>    
	                    <div class="edit_item"></div>
                    </div>
                                        <!--  ================================导入信息显示部分  start ===============================================   -->
                        <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                   <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">开始核对</button>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                </div>
                            </div>
                        </div>
                    </div>
                   <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div style="width:730px;word-break: break-all; word-wrap: break-word;">
<span style="color:blue">一、天猫-出仓仓库为天运二期仓</span><br/>
【数据准备顺序】<br/>
1、订单管理列表页（查询发货时间和已出库状态的查询条件，导出当月的发货单据流水）；
2、【库存中心】-【出入库流水台账】导出当月发货出库的流水数据；
3、以出入库流水中的（外部流水号）对应订单管理的出库数据核对是否一致；并拿到每个订单号的实际发货时间填充回到订单管理订单流水中的发货时间。
4、以合并的源数据导入到系统POP流水校验进行校验（注：导入的数据表格必须要有<span class="red">“发货时间”、“交易订单号”、“商品数量”、“货品id”</span>四个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
<br/>
<span style="color:blue">二、天猫-出仓仓库为非天运二期仓</span><br/>
【数据准备顺序】<br/>
1、订单管理列表页（查询发货时间和已出库状态的查询条件，导出当月的发货单据流水）；
2、库存中心】-【出入库流水台账】导出当月发货出库的流水数据；
3、以出入库流水中的（外部流水号）对应订单管理的出库数据核对是否一致；并拿到每个订单号的实际发货时间填充回到订单管理订单流水中的发货时间。
4、以合并的源数据导入到系统POP流水校验进行校验（注：导入的数据表格必须要有<span class="red">“发货时间”、“交易订单号”、“商品数量”、“货品编码”四个必填字段</span>，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
<br/>
<span style="color:blue">三、京东</span><br/>
【数据准备顺序】<br/>
1、【订单管理】-【订单查询与跟踪】导出当月已出库、已完结的流水数据；
2、找蓝精灵提供查询商品对照关系的对接接口（【订单中心-订单货号拦截】），当前无对接接口可通过蓝精灵后台导出，或是找蓝精灵开发数据库里拉一份对照关系数据；并人工替换掉商品信息；
3、将导出的流水数据导入到系统中进行校验（注：导入的数据表格必须要有<span class="red">“订单号 ”、“商品ID ”、“订购数量”</span>三个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
<br/>
<span style="color:blue">四、出仓仓库为综合1仓的电商订单（拼多多、有赞、第e仓、小小包、澳新、斑马）</span><br/>
【数据准备顺序】<br/>
1、原有的流水链接（经分销库存流水明细中的‘业务单据号’）与经分销发货订单中的‘发货单号’关联匹配，以获取完整的数据（包含字段公司名称、仓库名称、商品OPG、商品货号、商品名称、交易类型、交易数量、单位、平台订单号、业务单据号、原始批次号、生产日期、失效日期、交易创建时间、发货时间）；
2、根据“公司”为维度区分数据，并导入系统做订单校验（注：导入数据表格必须要有<span class="red">“发货时间”、“平台订单号”、“商品货号”、“交易数量”、“原始批次号”</span>五个必填字段，导入数据模板仅保留必填字段即可，方可加快系统处理效率）。
【附链接】
<a target="_blank" href="http://192.168.0.103:8080/FineReport/ReportServer?reportlet=%5B5353%5D%5B5fd7%5D%5B5927%5D%5B6570%5D%5B636e%5D%2F%5B524d%5D%5B7aef%5D%5B62a5%5D%5B8868%5D%2F%5B7ecf%5D%5B5206%5D%5B9500%5D%5B5e93%5D%5B5b58%5D%5B6d41%5D%5B6c34%5D.cpt
">仓库收发明细</a>&nbsp&nbsp
<a target="_blank" href="http://192.168.0.103:8080/FineReport/ReportServer?reportlet=%5B5353%5D%5B5fd7%5D%5B5927%5D%5B6570%5D%5B636e%5D%2F%5B524d%5D%5B7aef%5D%5B62a5%5D%5B8868%5D%2F%5B7ecf%5D%5B5206%5D%5B9500%5D%5B5e93%5D%5B5b58%5D%5B6d41%5D%5B6c34%5D.cpt">Ofc电商订单对应关系</a>
<br/>
		</div>
                        </div>
                   </div>
                    <!--  ================================导入信息显示部分  end ===============================================   -->
              </div>
             </div>
                               <!--======================Modal框===========================  -->
    <div class="popupbg" style="display: none">
			<div class="popup_box">
				<img src="/resources/assets/images/uploading.gif" alt="">
				<p>正在上传中...</p>
			</div>
		</div>
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

<!-- content -->
<script type="text/javascript">
//点击上传文件
$("#btn-file").click(function(){
	var flag = "2";
	var outDepotId=$("[name='outDepotId']").val();
	var storePlatformCode = $("[name='storePlatformCode']").val();   	
	var shopCode=$("[name='shopCode']").val();
	var checkStartDate=$("[name='checkStartDate']").val();
	var checkEndDate=$("[name='checkEndDate']").val();
	//必填项校验
	if(!outDepotId){
		$custom.alert.error("出仓仓库不能为空！");
		return ;
	}
	$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":outDepotId}, function(result1){
		flag="1";
		if(result1.data.depotCode!='ERPWMS_360_0402'){			//当出仓仓库为综合1仓时,	平台名称/店铺非必填(禁用)
			//必填项校验
			if(!checkStartDate){
				$custom.alert.error("核对开始日期不能为空！");
				return ;
			}
			if(!checkEndDate){
				$custom.alert.error("核对结束日期不能为空！");
				return ;
			}
			if(!storePlatformCode){
				$custom.alert.error("平台不能为空！");
				return ;
			}
			if(!shopCode){
				$custom.alert.error("店铺不能为空！");
				return ;
			}
		}else{
			//必填项校验
			if(!checkStartDate){
				$custom.alert.error("核对开始日期不能为空！");
				return ;
			}else if(!checkEndDate){
				$custom.alert.error("核对结束日期不能为空！");
				return ;
			}else{
				flag="1";
			}
		}
		if(flag=="1"){

			var input = '<input type="file" name="file" id="file" class="btn-hidden file-import">';
			$("#form-upload").empty();
			$("#form-upload").append(input);			
			$("#file").click();
		}
	});
});
$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
//加载仓库的下拉数据
$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null, {});

//上传文件到后端
$("#save-buttons").on("click",function(){
	var outDepotId=$("[name='outDepotId']").val();
	var storePlatformCode = $("[name='storePlatformCode']").val();   	
	var shopCode=$("[name='shopCode']").val();
	var checkStartDate=$("[name='checkStartDate']").val();
	var checkEndDate=$("[name='checkEndDate']").val();

	
	//判断是否为excel格式
	var file = $.find('#file')[0].files[0];
	
	if(!file){
		$custom.alert.error("请上传文件");
        return ;
	}
	
	var suffix = file.name.split(".")[1];
	if(suffix=="xlsx" || suffix=="xls"){
		$(".popupbg").show();
		var formData = new FormData(); 
		formData.append('storePlatformCode', storePlatformCode) ;
		formData.append('shopCode', shopCode) ;
		formData.append('outDepotId', outDepotId) ;
		formData.append('checkStartDate', checkStartDate) ;
		formData.append('checkEndDate', checkEndDate) ;
		formData.append('taskType', "1") ;
		formData.append('file', file) ;
		$custom.request.ajaxUpload(serverAddr+"/automaticCheckTask/saveCheckResult.asyn", formData, function(result){
			$(".popupbg").hide();
			if(result.state == 200){
				$custom.alert.success("已创建任务:"+result.data+",请在列表查看进度");
				setTimeout('$("#cancel-buttons").click();', 1000) ;
			}else{
                $custom.alert.error(result.data.message);
            }
		});
	}else{
		$custom.alert.error("请上传正确的excel格式文件");
	} 
});

	$(function(){
		//保存按钮
		   $("[name='outDepotId']").change(function() {
		    	var outDepotId = $("[name='outDepotId']").val();   	
		    	var storePlatformCode = $("[name='storePlatformCode']").val();   	
		    	$custom.request.ajax("/depot/getDepotDetails.asyn", {"id":outDepotId}, function(result1){
					if(result1.data.depotCode=='ERPWMS_360_0402'){			//当出仓仓库为综合1仓时,	平台名称/店铺非必填(禁用)
						$("#storePlatformCode_i").css("display","none");
						$("#shopCode_i").css("display","none");
						$("[name='storePlatformCode']").attr("disabled",true);
						$("[name='shopCode']").attr("disabled",true);
					}else{
						$("#storePlatformCode_i").css("display","inline-block");
						$("#shopCode_i").css("display","inline-block");
						$("[name='storePlatformCode']").attr("disabled",false);
						$("[name='shopCode']").attr("disabled",false);

						$custom.request.ajax(serverAddr+"/automaticCheckTask/changeShopCodeLabel.asyn",{'storePlatformCode':storePlatformCode,'outDepotId':outDepotId},function(data){
					            var dataRole = data.data.shopList;  
					            $("[name='storePlatformCode']").empty(); 
					            var html = "<option value=''>请选择</option>";
					           for(var i=0;i<dataRole.length;i++){
					        	   html += "<option value='"+dataRole[i].storePlatformCode
					                +"'>"+dataRole[i].storePlatformName+"</option>";
						       } 
					            var optionText = null;
					            $("[name='storePlatformCode']").append(html);
					            $("[name='storePlatformCode'] option").each(function () { 
					                   var text = $(this).val()     
					                   if( text == optionText ){
					                	   $(this).remove();
					                   }else{
					                	   optionText=text;  
					                   } 
					               });
					            $("[name='shopCode']").empty(); 
					            var shopCodeHtml = "<option value=''>请选择</option>";
					           for(var i=0;i<dataRole.length;i++){
					        	   shopCodeHtml += "<option value='"+dataRole[i].shopCode
					                +"'>"+dataRole[i].shopName+"</option>";
					            } 
					            $("[name='shopCode']").append(shopCodeHtml);
					        });
					}
				});
		    });
		//返回
		$("#cancel-buttons").click(function(){
			$module.revertList.isMainList = true;
		    $module.load.pageReport("act=14003");
		});
	});
	
	  // 日期选择
    $("[name='checkStartDate']").datetimepicker({
        language:  'zh-CN',
        format: 'yyyy-mm-dd 00:00:00',
        minView: 2,
    });
    $("[name='checkEndDate']").datetimepicker({
        language:  'zh-CN',
        format: 'yyyy-mm-dd 23:59:59',
        minView: 2,
    });

 
    	
	
</script>
