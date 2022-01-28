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
	                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toPage.html');">汇率管理</a></li>
	                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toAddPage.html?id=${detail.id }');">新增汇率管理</a></li>
	                    	</ol>
                    	</div>
            		</div>
            <!-- 基本信息开始 -->
               <div class="title_text">基本信息</div>
               <div class="info_box">
               
               <div class="edit_box">
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>兑换币种：</label>
                       <select class="edit_input"  name="origCurrencyCode" id="origCurrencyCode">
                         </select>
                   </div>
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>记账币种：</label>
                       <select class="edit_input"  name="tgtCurrencyCode" id="tgtCurrencyCode"  >
                       </select>
                   </div>
               </div>
               
               <div class="edit_box">
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>记账汇率：</label>
                       <input type="text" required="" parsley-type="text" class="edit_input"  name="rate" id="rate" onkeyup="value=value.replace(/[^\d^\.]/g,'')">
                   </div>
                   <div class="edit_item">
                       <label class="edit_label"><i class="red">*</i>生效时间：</label>
                       <input type="text" required="" parsley-type="text" class="edit_input form_datetime date-input" name="effectiveDateStr" id="effectiveDateStr">
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
                               <input type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons" value="重 置" />
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
$(function(){
		//重置
		$("#cancel-buttons").click(function(){
			$load.a("/rate/toAddPage.html");
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
        var url = "/rate/saveRate.asyn";
        var form = $("#add-form").serializeArray();
        //必填项校验
        if($("#origCurrencyCode").val()==""){
            $custom.alert.error("兑换币种不能为空！");
            return ;
        }
        if($("#tgtCurrencyCode").val()==""){
            $custom.alert.error("记账币种不能为空！");
            return ;
        }
        if($("#rate").val()==""){
            $custom.alert.error("记账汇率不能为空！");
            return ;
        }
        if($("#effectiveDateStr").val()==""){
            $custom.alert.error("生效时间不能为空！");
            return ;
        }

        $custom.request.ajax(url, form, function(result){
            if(result.state == '200'){
                $custom.alert.success("新增成功！");
                setTimeout(function () {
                    $load.a("/rate/toPage.html");
                }, 1000);
            }else if(result.message){
                $custom.alert.error(result.message);
            }else{
                $custom.alert.error("新增失败！");
            }
        });
    }
</script>
