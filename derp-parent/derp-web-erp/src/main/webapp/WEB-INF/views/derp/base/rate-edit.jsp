<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        
          <form id="add-form"  action="" name="form1">
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
                           <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toEditPage.html?id=${detail.id }');">编辑汇率管理</a></li>
                    </ol>
                    </div>
            </div>
            <div class="title_text">基本信息</div>
                    <div class="info_box">
                        <div class="form-row mt20">
                            <div class="col-4">
                                <div class="row">
                                    <input type="hidden" name="id" value="${detail.id}">
                                    <label class="col-3 col-form-label "
                                           style="white-space: nowrap;"><span class=""><i
                                            class="red">*</i> 兑换币种<span>：</span></span></label>
                                    <div class="col-9 ml10 line">
                                        <select class="form-control" name="origCurrencyCode" id="origCurrencyCode" onchange="ChangeNameSelect(this.selectedIndex)">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-1" style="text-align: center;">
                                <img alt="" src="/resources/assets/images/u5617.png">
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-3 col-form-label "
                                           style="white-space: nowrap;"><span class=""><i
                                            class="red">*</i> 记账币种<span>：</span></span></label>
                                    <div class="col-9 ml10 line">
                                        <select class="form-control" name="tgtCurrencyCode" id="tgtCurrencyCode">
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-row mt20">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-3 col-form-label "
                                           style="white-space: nowrap;"><span class=""><i
                                            class="red">*</i> 记账汇率<span>：</span></span></label>
                                    <div class="col-9 ml10 line">
                                        <input type="text" required="" parsley-type="text" class="form-control"  name="rate" value="${detail.rate }" onkeyup="value=value.replace(/[^\d^\.]/g,'')">
                                    </div>
                                </div>
                            </div>
                            <div class="col-1">
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-3 col-form-label "
                                           style="white-space: nowrap;"><span class=""><i
                                            class="red">*</i> 生效时间<span>：</span></span></label>
                                    <div class="col-9 ml10 line">
                                        <input type="text" required="" parsley-type="text" class="form-control form_datetime date-input" name="effectiveDateStr" value="${effectiveDate}">
                                    </div>
                                </div>
                            </div>
                        </div>
                  </div>
                        <!-- 基本信息结束 -->
                        <!-- 折算方式开始 -->
                  <div class="form-row m-t-50">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-6">
                                    <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">保 存</button>
                                </div>
                                <div class="col-6">
                                    <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                </div>
                            </div>
                        </div>
                    </div>
              </div>

             </div>

                               <!--======================Modal框===========================  -->
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>
<script src='/resources/js/system/base.js' type="text/javascript"></script>
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="origCurrencyCode"]')[0],"currencyCodeList",'${detail.origCurrencyCode}');
    $module.constant.getConstantListByNameURL.call($('select[name="tgtCurrencyCode"]')[0],"currencyCodeList",'${detail.tgtCurrencyCode}');
$(function(){		

	//保存按钮
	$("#save-buttons").click(function(){
		var url = "/rate/modifyRate.asyn";	
		var form = $("#add-form").serializeArray();
		$custom.request.ajax(url, form, function(result){	
			if(result.state == '200'){
				$custom.alert.success("编辑成功！");
				setTimeout(function () {
					$load.a("/rate/toPage.html");
				}, 1000);
			}else if(result.message){
				$custom.alert.error(result.message);
			}else{
				$custom.alert.error("编辑失败！");
			}
		});
	});
	
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/rate/toPage.html");
		});
		
	    $(".date-input").datetimepicker({
	        language:  'zh-CN',
	        format: "yyyy-mm",
	        autoclose: 1,
			todayHighlight: 1,
			startView: 3,
			minView: 3
        });
	});
</script>
