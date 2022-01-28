<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" >
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    	<div>
	                       <ol class="breadcrumb">
	                           <li><i class="block"></i> 当前位置：</li>
	                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rateConfig/toPage.html');">汇率配置表</a></li>
	                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rateConfig/toAddPage.html?id=${detail.id }');">新增汇率配置</a></li>
	                    	</ol>
                    	</div>
            		</div>
            <!-- 基本信息开始 -->
               <div class="title_text">基本信息</div>
               <div class="info_box">
               
               <div class="edit_box">
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>原币：</label>
                       <select class="input_msg"  name="origCurrencyCode" id="origCurrencyCode">
                         </select>
                   </div>
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>本币：</label>
                       <select class="input_msg"  name="tgtCurrencyCode" id="tgtCurrencyCode"  >
                       </select>
                   </div>
               </div>
               
               <div class="edit_box">
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>数据来源：</label>
                       <select class="input_msg"  name="dataSource" id="dataSource"></select>
                   </div>
                   <div class="edit_item">
                   </div>
               </div>
                   <!-- 基本信息结束 -->
               </div>
               <div class="form-row m-t-50">
                   <div class="col-12">
                       <div class="row">
                           <div class="col-6">
                               <input type="button" class="btn btn-info waves-effect waves-light fr btn_default" onclick="save();" value="保 存" />
                           </div>
                           <div class="col-6">
                               <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="返回" />
                           </div>
                       </div>
                   </div>
               </div>

             </div>

                               <!--======================Modal框===========================  -->
                  <!-- end row -->
           </div>
           </div>
           </div>
          </div>
          </form>
        <!-- container -->
    </div>

</div> <!-- content -->
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="origCurrencyCode"]')[0],"currencyCodeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="tgtCurrencyCode"]')[0],"currencyCodeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="dataSource"]')[0],"exchangeRateConfig_dataSourceList",null);

$(function(){
		//重置
		$("#cancel-buttons").click(function(){
			$load.a("/rateConfig/toPage.html");
		});
		
	    $(".date-input").datetimepicker({
	        language:  'zh-CN',
	        format: "yyyy-mm-dd",
	        autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2
        });
	});

    //保存按钮
    function save() {
        var url = "/rateConfig/saveRateConfig.asyn";
        var form = $("#add-form").serializeArray();
        //必填项校验
        if($("#origCurrencyCode").val()==""){
            $custom.alert.error("原币不能为空！");
            return ;
        }
        if($("#tgtCurrencyCode").val()==""){
            $custom.alert.error("本币不能为空！");
            return ;
        }
        $custom.request.ajax(url, form, function(result){
            if(result.state == '200'){
                $custom.alert.success("新增成功！");
                setTimeout(function () {
                    $load.a("/rateConfig/toPage.html");
                }, 1000);
            }else if(result.message){
                $custom.alert.error(result.message);
            }else{
                $custom.alert.error("新增失败！");
            }
        });
    }
</script>
