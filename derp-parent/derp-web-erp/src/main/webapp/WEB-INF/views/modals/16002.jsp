<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="popup-signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择商家</h5>
                </div>
                <div class="modal-body"> 
                      <div class="form-row mt20">
                   			<table id="popup-datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span></span>
			                                </label>
		                           		 </th>
			                            <th>公司编码</th>
			                            <th>公司名称</th>

			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        </tbody>
			                        
			              </table>
			                    <div style="display: block;float: right;width: 100%;">
				                    <div class="page_txt" style="display: inline-block;float: left;line-height: 38px;"></div>
				                    <div class="pageUtils" style="display: inline-block;float: right;"></div>
				                </div>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m16002.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m16002.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

var $m16002={
		
        init:function(){
            this.show();
        },
        show:function(){

        	
        	$("#data-tbody").empty();
        	$custom.request.ajax($m16002.params.url, {}, function(result){
        		if(result.state == 200){
        			
        			var list = result.data ;
        			if(list){
							
							var html = "" ;
							
							$(list).each(function(i, m){
								var name = m.name ;
								var code = m.code ;
								var id = m.id ;
								
								var checked = "" ;
								var disabled = "" ;

								
								html += '<tr><td class=" td-checkbox"><input type="checkbox" value="'+id+'" class="iCheck" '+ checked + ' ' + disabled + '></td>' +
									'<td>'+code+'</td>' + '<td>'+name+'</td>' + '</tr>' ;
							}) ;
							
							$("#popup-datatable-buttons").append(html) ;
						}
        		}
        	});
            $($m16002.params.modalId).modal("show");
        },
        hide:function(){
            $($m16002.params.modalId).modal("hide");
        },
        save:function(){
        	
        	var checked = $(".td-checkbox").find("input[type=checkbox]:checked") ;
        	
        	var rows = new Array ();
        	
        	$(checked).each(function(i,m){
        		var id = $(m).val() ;
        		
        		rows.push(id);
        	});        	
        	rows = rows.join(",") ;
        	$custom.request.ajax($m16002.params.getMerchantListUrl, {"merchantIds": rows}, function(res){
        		
        		var list=res.data.result;
        		var settlementTypeList=res.data.settlementTypeList;
        		var accountPeriodList=res.data.accountPeriodList;
        		var tariffRateConfigList=res.data.tariffRateConfigList;
        		
        		if(res.state == 200){	
        			var html2 = "";
        			$(list).each(function(index,event){
        				var registeredType=event.registeredType;// 注册地类型 1、境内；2、境外    境外优先配置税率为”0  境内优先配置税率为0.13
        				
        				var tariffRateConfigHtml='<td><select name="tariffRateConfigId" style="width: 70px ;height:25px;" ><option value="">请选择</option>';
        				$(tariffRateConfigList).each(function(index,tariffRateConfig){
        					var rate= tariffRateConfig.rate;
        					var selectedStr="";
        					console.log("1"==registeredType);
        					console.log(0==rate);
        					console.log("2"==registeredType);
        					console.log(0.13==rate);
        					console.log("2"==registeredType&&0.13==rate);
        					if ("1"==registeredType && 0==rate) {
        						selectedStr="selected";
    						}
        					if ("2"==registeredType&&0.13==rate) {
        						selectedStr="selected";
    						}
        					tariffRateConfigHtml=tariffRateConfigHtml+
  						  '<option value="'+tariffRateConfig.id+'" '+selectedStr+'>'+tariffRateConfig.rate+'</option>';
        				});
        				tariffRateConfigHtml=tariffRateConfigHtml+'</select></td>';
        				
        				
        				html2+="<tr>"+
    	            	"<td><input type='checkbox'   class='iCheck' name='item-checkbox'><input type='hidden' name='merchantId' value='"+event.id+"'/></td>"+
                    	"<td>"+(event.code==null?'':event.code)+"<input type='hidden' name='merchantCode' value='"+(event.code==null?'':event.code)+"'/></td>"+
                        "<td>"+(event.name==null?'':event.name)+"<input type='hidden' name='merchantName' value='"+(event.name==null?'':event.name)+"'/></td>"+
                        '<td><input type="checkbox"  checkbox" checked="checked" name="purchasePriceManage"/>启用</td>'+    
                        tariffRateConfigHtml+
                        "</tr>";
					}) ;
        			$("#table-list-group").append(html2); 
        			//$module.constant.getConstantListByNameURL.call($('select[name="settlementType"]')[0],"customerMerchantRel_settlementTypeList",null);
                    //$module.constant.getConstantListByNameURL.call($('select[name="accountPeriod"]')[0],"customerInfo_accountPeriodList",null);
                    
        			$($m16002.params.modalId).modal("hide");
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        	
        },
        params:{
            url:'/supplier/getMerchantInfo.asyn?r='+Math.random(),
            getMerchantListUrl:'/merchant/getMerchantList.asyn?r='+Math.random(),
            modalId:'#popup-signup-modal'
        }
    };
    
    /**
     * 全选
     */
    $('#popup-datatable-buttons').on("change", ":checkbox", function() {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#popup-datatable-buttons').prop("checked",$(this).prop("checked"));
        }else{
            // 一般复选
            var checkbox = $("tbody :checkbox", '#popup-datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#popup-datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });


</script>