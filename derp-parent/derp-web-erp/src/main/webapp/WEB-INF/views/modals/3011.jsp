<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg modal_w1000 " tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择公司</h5>
                </div>
                <div class="modal-body">
                	<div class="form-row">
                      </div>
                      <div class="form-row mt20">
                   			<table id="datatable-merchant" class="table table-striped" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                            </th>
			                            <th>公司编码</th>
			                            <th>公司名称</th>
			                            <th>账期</th>
			                            <th>销售模式</th>
			                        </tr>
			                        </thead>
			                        <tbody id="data-tbody">
			                        </tbody>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m3011.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m3011.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">

    var $m3011={
        init:function(id){
        	$m3011.params.customerId = id ;
            this.show(id);
        },
        show:function(id){
        	
        	var curentMerchantId = $("#curentMerchantId").val() ;
        	
        	$("#data-tbody").empty();
        	$custom.request.ajax($m3011.params.relUrl, {"id":id}, function(result){
        		if(result.state == 200){
        			var relList = result.data.relList ;
            		var businessModelList=result.data.businessModelList;
            		var accountPeriodList=result.data.accountPeriodList;
        			
            		
            		
       				$custom.request.ajax($m3011.params.url, {"id":id}, function(res){
       					if(res.state == 200){
       						var list = res.data ;
       						debugger;
       						if(list){
       							
       							var html = "" ;
       							
       							$(list).each(function(i, m){
       								var name = m.name ;
       								var code = m.code ;
       								var id = m.id ;
       								
       								var checked = "" ;
       								var disabled = "" ;
       								
       								var businessModel="";// 客户关系表销售类型
       								var accountPeriod="";//客户关系表账期
       								
       								if(relList){
       									$(relList).each(function(j, rel){
       										merchantId = rel.merchantId ;
       										
       										if(id == merchantId){
       											checked = "checked" ;
       											businessModel=rel.businessModel;
       											accountPeriod=rel.accountPeriod;
       											return false ;
       										}
       										
       									}) ;
       								}
       								
       								var businessModelHtml='<td><select name="businessModel"  ><option value="">请选择</option>';
       								if(businessModelList){
       									$(businessModelList).each(function(j, businessModelObj){
       										var selected1="";
       			        	        		debugger;
       			        					if (businessModelObj.key==businessModel) {
       			        						selected1="selected";
       			    						}
       			        					businessModelHtml=businessModelHtml+
       	        						  '<option value="'+businessModelObj.key+'"'+selected1+'>'+businessModelObj.value+'</option>';

       									}) ;
       									businessModelHtml=businessModelHtml+'</select></td>';
       								}
       								
       								var accountPeriodHtml='<td><select name="accountPeriod"  ><option value="">请选择</option>';
       								if(accountPeriodList){
       									$(accountPeriodList).each(function(j, accountPeriodObj){
       										var selected2="";
       			        	        		debugger;
       			        					if (accountPeriodObj.key==accountPeriod) {
       			        						selected2="selected";
       			    						}
       			        					accountPeriodHtml=accountPeriodHtml+
       	        						  '<option value="'+accountPeriodObj.key+'"'+selected2+'>'+accountPeriodObj.value+'</option>';

       									}) ;
       									accountPeriodHtml=accountPeriodHtml+'</select></td>';
       								}
       								
       								
       								
       						
       								
       								html += '<tr><td class=" td-checkbox"><input type="checkbox" name="checkedBox" value="'+id+'" class="iCheck" '+ checked + ' ' + disabled + '></td>' +
       									'<td>'+code+
       									'<input type="hidden" name="merchantId" value='+id+'>'+
       									'</td>' + 
       									'<td>'+name+'</td>' +
       									accountPeriodHtml+
       									businessModelHtml+
       									'</tr>' ;
       							}) ;
       							
       							$("#datatable-merchant").append(html) ;
       						}
       					}
       				});
        			
        			
        		}
        	});
            $($m3011.params.modalId).modal("show");
        },
        hide:function(){
            $($m3011.params.modalId).modal("hide");
        },
        save:function(){
        	var obj = {};
        	
        	var itemList = new Array();
            debugger;
            $('#datatable-merchant tbody tr').each(function(i){// 遍历 tr
            	debugger;
            		
            	if ($(this).find('input[name="checkedBox"]').is(':checked')==true) { 
            		var item ={};
            		var merchantId = $(this).find('input[name="merchantId"]').val();//商家id
    				item['merchantId']=merchantId;
    				var businessModel = $(this).find('select[name="businessModel"]').val();
    				item['businessModel']=businessModel;
    				var accountPeriod = $(this).find('select[name="accountPeriod"]').val();
    				item['accountPeriod']=accountPeriod;

    				itemList.push(item);
				} 
						
				
			});
            var customerId= $m3011.params.customerId;
            var json = {};
            json['customerId']=customerId;
            json['itemList']=itemList;
			var jsonObject= JSON.stringify(json); 
			obj['json']=jsonObject;
			
        	
        	
        	$custom.request.ajax($m3011.params.saveRelUrl, obj, function(res){
        		if(res.state == 200){
        			$custom.alert.success("操作成功");       			
        			$m3011.hide() ;
        			
        		}else{
        			$custom.alert.error	(res.expMessage);
        		}
        	});
        },
        params:{
            url:'/supplier/getMerchantInfo.asyn?r='+Math.random(),
            relUrl:'/supplier/getMerchantRel.asyn?r='+Math.random(),
            saveRelUrl:'/customer/saveMerchantRelJSon.asyn?r='+Math.random(),
           	customerId:"" ,
            modalId:'#signup-modal'
        }
    };
    
    $("input[name='keeperMerchantGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-merchant').prop("checked",$(this).prop("checked"));
            $('#datatable-merchant tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-merchant').prop("checked",false);
            $('#datatable-merchant tbody tr').removeClass('success');
        }
    })
    $('#datatable-merchant').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })
    
</script>