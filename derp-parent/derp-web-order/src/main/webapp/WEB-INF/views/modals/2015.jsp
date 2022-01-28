<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="invoice-modal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;position:fixed;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 1000px;text-align: center;margin-left: -200px;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">选择交易链路</h5>
                        </div>
                        <div class="modal-body">
                            <div class="form-row mt20">
                                <table id="fileTemp-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th style="text-align: center;">
                                            <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                <input type="checkbox" name="keeperMerchantGroup-checkable"
                                                       class="group-checkable"/>
                                            </label>
                                        </th>
                                        <th style="text-align: center;">链路名称</th>
                                        <th style="text-align: center;">起点公司</th>
                                        <th style="text-align: center;">客户1</th>
                                        <th style="text-align: center;">客户2</th>
                                        <th style="text-align: center;">客户3</th>
                                        <th style="text-align: center;">客户4</th>
                                    </tr>
                                    </thead>
                                    <tbody id="model-tbody" style="height: 300px; overflow-y:scroll ;"></tbody>
                                </table>
                            </div>
                        </div>

                        <div class="form-row ml20" style="padding-bottom: 20px;">
                            <div class="col-12">
                                <div class="row">
                                    <div class="col-4"></div>
                                    <div class="col-2">
                                        <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm"
                                                onclick='$m2015.save()'>确认
                                        </button>
                                    </div>
                                    <div class="col-2">
                                        <button type="button"
                                                class="btn btn-light waves-effect waves-light btn_default btn-sm"
                                                onclick='$m2015.hide();'>取消
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </div>
</div>
<!-- end col -->
</div>
<script type="text/javascript">


    var $m2015 = {
        init: function () {
        	
        	var purchaseOrderId = $("#purchaseOrderId").val() ;
        	
            $custom.request.ajax(serverAddr + '/purchase/getTradeLinkList.asyn', {"purchaseOrderId": purchaseOrderId}, function (result) {
                if (result.state == 200) {
                    var list = result.data;
                    if (list) {
                        $($m2015.params.modalId).modal({backdrop: 'static', keyboard: false});
                        var html = "";
                        $(list).each(function (i, m) {
                            var id = m.id;
                            var linkName = m.linkName;
                            
                            var starter = '' ;
                            if(m.startPointMerchantName){
                            	starter += '<input type="hidden" name="startPointMerchantName" value="'+m.startPointMerchantName+'">' ;
                            	starter += '<input type="hidden" name="startPointMerchantId" value="'+m.startPointMerchantId+'">' ;
                            	starter += m.startPointMerchantName + '<br/>';
                            }
                            
                            if(m.startPointBuName){
                            	starter += '<input type="hidden" name="startPointBuName" value="'+m.startPointBuName+'">' ;
                            	starter += '<input type="hidden" name="startPointBuId" value="'+m.startPointBuId+'">' ;
                            	starter += m.startPointBuName + "<br/>" ;
                            }
                            
                            if(m.startPointPremiumRate){
                            	starter += '<input type="hidden" name="startPointPremiumRate" value="'+m.startPointPremiumRate+'">' ;
                            	starter += m.startPointPremiumRate + "%" ;
                            }
                            
                            var one = "" ;
                            if(m.oneCustomerName){
                            	one += '<input type="hidden" name="oneCustomerName" value="'+m.oneCustomerName+'">' ;
                            	one += '<input type="hidden" name="oneCustomerId" value="'+m.oneCustomerId+'">' ;
                            	one += m.oneCustomerName + "<br/>";
                            }
                            
                            if(m.oneBuName){
                            	one += '<input type="hidden" name="oneBuId" value="'+m.oneBuId+'">' ;
                            	one += '<input type="hidden" name="oneBuName" value="'+m.oneBuName+'">' ;
                            	one += m.oneBuName + "<br/>" ;
                            }
                            
                            if(m.onePremiumRate){
                            	one += '<input type="hidden" name="onePremiumRate" value="'+m.onePremiumRate+'">' ;
                            	one += m.onePremiumRate + "%";
                            }
                            
                            if(m.oneType){
                            	one += '<input type="hidden" name="oneType" value="'+m.oneType+'">' ;
                            }
                            
                            if(m.oneIdvaluetype){
                            	one += '<input type="hidden" name="oneIdValueType" value="'+m.oneIdvaluetype+'">' ;
                            }
                            
                            var two = "" ;
                            if(m.twoCustomerName){
                            	two += '<input type="hidden" name="twoCustomerName" value="'+m.twoCustomerName+'">' ;
                            	two += '<input type="hidden" name="twoCustomerId" value="'+m.twoCustomerId+'">' ;
                            	two += m.twoCustomerName + "<br/>";
                            }
                            
                            if(m.twoBuName){
                            	two += '<input type="hidden" name="twoBuId" value="'+m.twoBuId+'">' ;
                            	two += '<input type="hidden" name="twoBuName" value="'+m.twoBuName+'">' ;
                            	two += m.twoBuName + "<br/>" ;
                            }
                            
                            if(m.twoPremiumRate){
                            	two += '<input type="hidden" name="twoPremiumRate" value="'+m.twoPremiumRate+'">' ;
                            	two += m.twoPremiumRate + "%";
                            }
                            
                            if(m.twoType){
                            	two += '<input type="hidden" name="twoType" value="'+m.twoType+'">' ;
                            }
                            
                            if(m.twoIdvaluetype){
                            	two += '<input type="hidden" name="twoIdValueType" value="'+m.twoIdvaluetype+'">' ;
                            }
                            
                            
                            var three = "" ;
                            if(m.threeCustomerName){
                            	three += '<input type="hidden" name="threeCustomerName" value="'+m.threeCustomerName+'">' ;
                            	three += '<input type="hidden" name="threeCustomerId" value="'+m.threeCustomerId+'">' ;
                            	three += m.threeCustomerName + "<br/>";
                            }
                            
                            if(m.threeBuName){
                            	three += '<input type="hidden" name="threeBuName" value="'+m.threeBuName+'">' ;
                            	three += '<input type="hidden" name="threeBuId" value="'+m.threeBuId+'">' ;
                            	three += m.threeBuName + "<br/>" ;
                            }
                            
                            if(m.threePremiumRate){
                            	three += '<input type="hidden" name="threePremiumRate" value="'+m.threePremiumRate+'">' ;
                            	three += m.threePremiumRate + "%";
                            }
                            
                            if(m.threeType){
                            	three += '<input type="hidden" name="threeType" value="'+m.threeType+'">' ;
                            }
                            
                            if(m.threeIdvaluetype){
                            	three += '<input type="hidden" name="threeIdValueType" value="'+m.threeIdvaluetype+'">' ;
                            }
                            
                            var four = "" ;
                            if(m.fourCustomerName){
                            	four += '<input type="hidden" name="fourCustomerName" value="'+m.fourCustomerName+'">' ;
                            	four += '<input type="hidden" name="fourCustomerId" value="'+m.fourCustomerId+'">' ;
                            	four += m.fourCustomerName + "<br/>";
                            }
                            
                            if(m.fourBuName){
                            	four += '<input type="hidden" name="fourBuName" value="'+m.fourBuName+'">' ;
                            	four += '<input type="hidden" name="fourBuId" value="'+m.fourBuId+'">' ;
                            	four += m.fourBuName + "<br/>" ;
                            }
                            
                            if(m.fourPremiumRate){
                            	four += '<input type="hidden" name="fourPremiumRate" value="'+m.fourPremiumRate+'">' ;
                            	four += m.fourPremiumRate + "%";
                            }
                            
                            if(m.fourType){
                            	four += '<input type="hidden" name="fourType" value="'+m.fourType+'">' ;
                            }
                            
                            if(m.fourIdvaluetype){
                            	four += '<input type="hidden" name="fourIdValueType" value="'+m.fourIdvaluetype+'">' ;
                            }
                            
                            html += '<tr>'
                                + '<td class="temp-checkbox"><input type="checkbox" value="' + id + '" class="iCheck" ' + '></td>'
                                + '<td>' + linkName + '</td>'
                                + '<td>' + starter + '</td>'
                                + '<td>' + one + '</td>'
                                + '<td>' + two + '</td>'
                                + '<td>' + three + '</td>'
                                + '<td>' + four + '</td>'
                                + '</tr>';

                        });
                        $("#model-tbody").html(html);
                    }
                } else {
                    var message = res.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("未查到链路配置");
                    }
                }
            });
        },
        hide: function () {
            $($m2015.params.modalId).modal("hide");
        },
        save: function () {
            var checked = $(".temp-checkbox").find("input[type=checkbox]:checked");
            if (checked.length == 0 || checked.length > 1) {
                $custom.alert.warningText("请选择一个配置！");
                return;
            }
            
            $(".divCustomer1").hide() ;
            $(".divCustomer2").hide() ;
            $(".divCustomer3").hide() ;
            $(".divCustomer4").hide() ;
            $(".divCustomer5").hide() ;
            
            var trOrg = $(".temp-checkbox").find("input[type=checkbox]:checked").parent().parent() ;
            
            var id = $(".temp-checkbox").find("input[type=checkbox]:checked").val();
            $($m2015.params.modalId).modal("hide");
            $('.modal-backdrop').remove();//去掉遮罩层
            
            $("#tradeLinkId").val(id) ;
            
            var startPointMerchantId = null ;
            if($(trOrg).find("input[name='startPointMerchantName']").val()){
            	$(".divCustomer1").show() ;
            	var startPointMerchantName = $(trOrg).find("input[name='startPointMerchantName']").val() ;
            	startPointMerchantId = $(trOrg).find("input[name='startPointMerchantId']").val() ;
            	$("#startPointMerchantName").val(startPointMerchantName) ;
            	
            	var startBuName = $(trOrg).find("input[name='startPointBuName']").val() ;
            	$("#startPointBuName").val(startBuName) ;
            	
            	var startPointPremiumRate = $(trOrg).find("input[name='startPointPremiumRate']").val() ;
            	$("#startPointPremiumRate").val(startPointPremiumRate) ;
            	
            	$("#startPointPoNo").attr("required", "true") ;
            	$("#startPointPoNo").attr("reqMsg", "起点公司PO号不能为空") ;
            	
            	var depotType = "a,d" ;
            	if('3' == businessModel){
        			depotType = 'd' ;
        		}
            	
            	$module.depot.getSelectBeanByMerchantRel.call($("#startPointDepotId"), null, {"type": depotType, "merchantId": startPointMerchantId});
            }
            
            var oneCustomerId = null ;
            var oneCustomerJson = null ;
            var oneIdValueType = null ;
            if($(trOrg).find("input[name='oneCustomerName']").val()){
            	$(".divCustomer2").show() ;
            	oneCustomerId = $(trOrg).find("input[name='oneCustomerId']").val() ;
            	var oneCustomerName = $(trOrg).find("input[name='oneCustomerName']").val() ;
            	$("#oneCustomerName").val(oneCustomerName) ;
            	
            	var oneBuName = $(trOrg).find("input[name='oneBuName']").val() ;
            	$("#oneBuName").val(oneBuName) ;
            	
            	var onePremiumRate = $(trOrg).find("input[name='onePremiumRate']").val() ;
            	$("#onePremiumRate").val(onePremiumRate) ;
            	
            	var oneType = $(trOrg).find("input[name='oneType']").val() ;
            	oneIdValueType = $(trOrg).find("input[name='oneIdValueType']").val() ;
            	
            	/**
                 *	1、若对应该级客户是内部公司，必填

                 *1）、若该级不是末级，则取该公司关联的中转仓

                 *2）、若该级是末级、则取该公司关联的中转仓、保税仓

                 *2、若对应该级客户是不是内部公司，非必填，取上级客户关联的备查库
                 */
                 $("#onePoNo").attr("readOnly", "true") ;
            	 $("#poNo2I").hide() ;
                 if(oneType == '1'){
                 	$("#oneDepotId").attr("required", "true") ;
                 	$("#oneDepotId").attr("reqMsg", "客户1仓库不能为空") ;
                 	$("#onePoNo").attr("required", "true") ;
	            	$("#onePoNo").attr("reqMsg", "客户1PO号不能为空") ;
	            	$("#onePoNo").removeAttr("readonly") ;
	            	$("#poNo2I").show() ;
	            	$("#oneBuNameI").show() ;
                 	
                 	var tempMerchantId = oneCustomerId ;
                 	if(oneIdValueType == '1'){
	                 	oneCustomerJson = $module.customer.getCustomerById(oneCustomerId) ;
	                 	tempMerchantId = oneCustomerJson.innerMerchantId ;
                 	}
                 	
                 	var depotType = "a,d" ;
                	if('3' == businessModel){
            			depotType = 'd' ;
            		}
                 	
               		$module.depot.getSelectBeanByMerchantRel.call($("#oneDepotId"), null,{"type": depotType, "merchantId": tempMerchantId});
                 }else{
                	$("#oneBuNameI").hide() ;
                 	$module.depot.getSelectBeanByMerchantRel.call($("#oneDepotId"), null,{"type":"b", "merchantId": startPointMerchantId});
                 }
            }
            
            var twoCustomerId = null ;
            var twoCustomerJson = null ;
            var twoIdValueType = null ;
            if($(trOrg).find("input[name='twoCustomerName']").val()){
            	$(".divCustomer3").show() ;
            	var twoCustomerName = $(trOrg).find("input[name='twoCustomerName']").val() ;
            	twoCustomerId = $(trOrg).find("input[name='twoCustomerId']").val() ;
            	$("#twoCustomerName").val(twoCustomerName) ;
            	
            	var twoBuName = $(trOrg).find("input[name='twoBuName']").val() ;
            	$("#twoBuName").val(twoBuName) ;
            	
            	var twoPremiumRate = $(trOrg).find("input[name='twoPremiumRate']").val() ;
            	$("#twoPremiumRate").val(twoPremiumRate) ;
            	
            	var twoType = $(trOrg).find("input[name='twoType']").val() ;
            	twoIdValueType = $(trOrg).find("input[name='twoIdValueType']").val() ;
            	
            	/**
                 *	1、若对应该级客户是内部公司，必填

                 *1）、若该级不是末级，则取该公司关联的中转仓

                 *2）、若该级是末级、则取该公司关联的中转仓、保税仓

                 *2、若对应该级客户是不是内部公司，非必填，取上级客户关联的备查库
                 */
                 $("#twoPoNo").attr("readOnly", "true") ;
            	 $("#poNo3I").hide() ;
                 if(twoType == '1'){
                 	$("#twoDepotId").attr("required", "true") ;
                 	$("#twoDepotId").attr("reqMsg", "客户2仓库不能为空") ;
                 	$("#twoPoNo").attr("required", "true") ;
                	$("#twoPoNo").attr("reqMsg", "客户2PO号不能为空") ;
                	$("#twoPoNo").removeAttr("readonly") ;
	            	$("#poNo3I").show() ;
	            	$("#twoBuNameI").show() ;
                 	
                 	var tempMerchantId = twoCustomerId ;
                 	if(twoIdValueType == '1'){
                 		twoCustomerJson = $module.customer.getCustomerById(twoCustomerId) ;
	                 	tempMerchantId = twoCustomerJson.innerMerchantId ;
                 	}
                 	
                 	var depotType = "a,d" ;
                	if('3' == businessModel){
            			depotType = 'd' ;
            		}
                 	
               		$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), null,{"type": depotType, "merchantId": tempMerchantId});
                 }else{
                	 $("#twoBuNameI").hide() ;
                	 
                	 if(oneCustomerJson){
	                 	$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), null,{"type":"b", "merchantId": oneCustomerJson.innerMerchantId});
                	 }else{
                		$module.depot.getSelectBeanByMerchantRel.call($("#twoDepotId"), null,{"type":"b", "merchantId": oneCustomerId});
                	 }
                 }
            }
            
	        var threeCustomerId = null ;
	        var threeCustomerJson = null ;
	        var threeIdValueType = null ;
            if($(trOrg).find("input[name='threeCustomerName']").val()){
            	$(".divCustomer4").show() ;
            	var threeCustomerName = $(trOrg).find("input[name='threeCustomerName']").val() ;
            	threeCustomerId = $(trOrg).find("input[name='threeCustomerId']").val() ;
            	$("#threeCustomerName").val(threeCustomerName) ;
            	
            	var threeBuName = $(trOrg).find("input[name='threeBuName']").val() ;
            	$("#threeBuName").val(threeBuName) ;
            	
            	var threePremiumRate = $(trOrg).find("input[name='threePremiumRate']").val() ;
            	$("#threePremiumRate").val(threePremiumRate) ;
            	
            	var threeType = $(trOrg).find("input[name='threeType']").val() ;
            	threeIdValueType = $(trOrg).find("input[name='threeIdValueType']").val() ;
            	
            	/**
                 *	1、若对应该级客户是内部公司，必填

                 *1）、若该级不是末级，则取该公司关联的中转仓

                 *2）、若该级是末级、则取该公司关联的中转仓、保税仓

                 *2、若对应该级客户是不是内部公司，非必填，取上级客户关联的备查库
                 */
                 $("#threePoNo").attr("readOnly", "true") ;
            	 $("#poNo4I").hide() ;
                 if(threeType == '1'){
                 	$("#threeDepotId").attr("required", "true") ;
                 	$("#threeDepotId").attr("reqMsg", "客户3仓库不能为空") ;
                 	$("#threePoNo").attr("required", "true") ;
                	$("#threePoNo").attr("reqMsg", "客户3PO号不能为空") ;
                	$("#threePoNo").removeAttr("readonly") ;
	            	$("#poNo4I").show() ;
	            	$("#threeBuNameI").show() ;
                 	
                 	var tempMerchantId = threeCustomerId ;
                 	if(threeIdValueType == '1'){
                 		threeCustomerJson = $module.customer.getCustomerById(threeCustomerId) ;
	                 	tempMerchantId = threeCustomerJson.innerMerchantId ;
                 	}
                 	
                 	var depotType = "a,d" ;
                	if('3' == businessModel){
            			depotType = 'd' ;
            		}
                 	
               		$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), null, {"type": depotType, "merchantId": tempMerchantId});
                 }else{
                	 $("#threeBuNameI").hide() ;
                	 
                	 if(twoCustomerJson){
	                 	$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), null, {"type":"b", "merchantId": twoCustomerJson.innerMerchantId});
                	 }else{
                		$module.depot.getSelectBeanByMerchantRel.call($("#threeDepotId"), null, {"type":"b", "merchantId": twoCustomerId});
                	 }
                 }
            }
            
		    var fourCustomerId = null ;
		    var fourCustomerJson = null ;
		    var fourIdValueType = null ;
		    $("#poNo5I").hide() ;
            if($(trOrg).find("input[name='fourCustomerName']").val()){
            	$(".divCustomer5").show() ;
            	var fourCustomerName = $(trOrg).find("input[name='fourCustomerName']").val() ;
            	fourCustomerId = $(trOrg).find("input[name='fourCustomerId']").val() ;
            	$("#fourCustomerName").val(fourCustomerName) ;
            	
            	var fourBuName = $(trOrg).find("input[name='fourBuName']").val() ;
            	$("#fourBuName").val(fourBuName) ;
            	
            	var fourPremiumRate = $(trOrg).find("input[name='fourPremiumRate']").val() ;
            	$("#fourPremiumRate").val(fourPremiumRate) ;
            	
            	var fourType = $(trOrg).find("input[name='fourType']").val() ;
            	fourIdValueType = $(trOrg).find("input[name='fourIdValueType']").val() ;
            	
            	/**
                 *	1、若对应该级客户是内部公司，必填

                 *1）、若该级不是末级，则取该公司关联的中转仓

                 *2）、若该级是末级、则取该公司关联的中转仓、保税仓

                 *2、若对应该级客户是不是内部公司，非必填，取上级客户关联的备查库
                 */
                 if(fourType == '1'){
                	 
                	 $("#fourPoNo").removeAttr("readonly") ;
                	 $("#fourPoNo").show() ;
                	 $("#fourPoNo").attr("required", "true") ;
                 	$("#fourPoNo").attr("reqMsg", "客户4PO号不能为空") ;
                 	$("#fourDepotId").attr("required", "true") ;
                 	$("#fourDepotId").attr("reqMsg", "客户4仓库不能为空") ;
                 	$("#fourBuNameI").show() ;
                 	
                 	var tempMerchantId = fourCustomerId ;
                 	if(fourIdValueType == '1'){
                 		fourCustomerJson = $module.customer.getCustomerById(fourCustomerId) ;
	                 	tempMerchantId = fourCustomerJson.innerMerchantId ;
                 	}
                 	
                 	var depotType = "a,d" ;
                	if('3' == businessModel){
            			depotType = 'd' ;
            		}
                 	
                 	fourCustomerJson = $module.customer.getCustomerById(fourCustomerId)
               		$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), null,{"type": depotType, "merchantId": tempMerchantId});
                 }else{
                	 
                	 $("#fourPoNo").attr("readonly", true) ;
                	 $("#fourBuNameI").hide() ;
                	 
                	 if(threeCustomerJson){
	                 	$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), null,{"type":"b", "merchantId": threeCustomerJson.innerMerchantId});
                	 }else{
                		$module.depot.getSelectBeanByMerchantRel.call($("#fourDepotId"), null,{"type":"b", "merchantId": threeCustomerId});
                	 }
                 }
            }
        },
        params: {
            modalId: '#invoice-modal',
            url: null,
            customerId: null
        }
    };

</script>