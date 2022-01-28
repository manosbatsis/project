<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                       <li class="breadcrumb-item"><a href="#">库存管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:list();">月库存转结</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                       <%--         <div class="form-row h35" >
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">公司<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="merchantName">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr">仓库<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="depotId" class="form-control">
                                                    <option value="">请选择</option>
                                                    &lt;%&ndash;  <c:forEach items="${depotBean }" var="depot">
                                                         <option value="${depot.value }">${depot.label }</option>
                                                     </c:forEach> &ndash;%&gt;
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">结转月份<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control form_datetime date-input" name="settlementMonth">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-2">
                                        <div class="row">
                                            <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-5 col-form-label "><div class="fr">结转状态<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select name="state" class="form-control">
                                                    <option value="">请选择</option>
                                                    <option value="1">未结转</option>
                                                    <option value="2">已结转</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    <%--   <c:forEach items="${depotBean }" var="depot">
                                        <option value="${depot.value }">${depot.label }</option>
                                    </c:forEach> --%>
                                    </select>
                                    <span class="msg_title">结转月份 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input" name="settlementMonth" id="settlementMonth">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">结转状态 :</span>
                                    <select name="state" class="input_msg">
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:;" class="unfold">展开功能</a>
                        </div>
                   </form> 
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="monthlyAccount_addMonthlyAccount">
                                <input type="button" id="addMonthlyAccount" class="btn btn-find waves-effect waves-light btn-sm" value="新增">
                            </shiro:hasPermission>
                            <shiro:hasPermission name="monthlyAccount_exportMonthlyAccountMap">
                                <input type="button" id="exportInventoryBatch" class="btn btn-find waves-effect waves-light btn-sm" value="导出">
                            </shiro:hasPermission>
                            <shiro:hasPermission name="monthlyAccount_refreshMonthlyBill">
                                <input type="button"  id="refreshMonthlyBill"  class="btn btn-add waves-effect waves-light btn-sm" value="刷新月结账单">
                            </shiro:hasPermission>

                        </div>
                    </div>    
                     
                      <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                        	<th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
<!--                             <th>序号</th> -->
                            <th>公司</th>
                            <th>仓库名称</th>
                            <th>结转月份</th>
                            <th>期初时间</th>
                            <th>期末时间</th>
                            <th>结转时间</th>
                            <th>结转状态</th>
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
        <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
        <jsp:include page="/WEB-INF/views/modals/1000001.jsp" />
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"monthlyAccount_stateList",null);
$(document).ready(function() {
	
    //重置按钮
/* 	$("button[type='reset']").click(function(){
	    	$(".goods_select2").each(function(){
	    		$(this).val("");
	    	});
	    	//重新加载select2
			$(".goods_select2").select2({
				language:"zh-CN",
				placeholder: '请选择',
				allowClear:true
			});
	}); */
    var depotId = '${depotId}';
    var settlementMonth = '${settlementMonth}';
	$('#settlementMonth').val(settlementMonth);
	$(".date-input").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
    });
	
	//加载仓库下拉
	$module.depot.loadSelectData.call($("select[name='depotId']")[0],depotId);

	
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
   		url: serverAddr+'/monthlyAccount/listMonthlyAccount.asyn?r='+Math.random(),    
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" value='+data.id+' monthlyState='+data.state+'  name="iCheck"  class="iCheck">';
            }
        },
//         {  //序号
//             data : null,  
//             bSortable : false,  
//             className : "text-right",  
//             width : "30px",  
//             render : function(data, type, row, meta) {  
//                 // 显示行号  
//                 var startIndex = meta.settings._iDisplayStart;  
//                 return startIndex + meta.row + 1;  
//                     }  
//         },
        {data:'merchantName'},{data:'depotName'},{data:'settlementMonth'},{data:'firstDate'},{data:'endDate'},{data:'settlementDate'},
        {data:'stateLabel'},
        { //操作
            orderable: false,
            width: "130px",
            data: null,
            render: function (data, type, row, meta) {
            	var edit = "";
            	if(row.state=='1'){
            		if(row.depotType =='1'){
                		edit+= '<shiro:hasPermission name="monthlyAccount_confirmMonthlySurplusNum"><a href="javascript:surplusNumButton('+row.id+',\''+row.state+'\',\''+row.orderNo+'\',\''+row.settlementMonth+'\')">按计算库存量结转</a><br></shiro:hasPermission>';
                		//edit+='<shiro:hasPermission name="monthlyAccount_confirmMonthlyAvailableNum"><a href="javascript:availableNumButton('+row.id+',\''+row.state+'\',\''+row.orderNo+'\',\''+row.settlementMonth+'\')">按仓库现存量结转</a><br></shiro:hasPermission>';
                	}else if(row.depotType =='2'){
                		edit += '<shiro:hasPermission name="monthlyAccount_confirmMonthlySurplusNum"><a href="javascript:surplusNumButton('+row.id+',\''+row.state+'\',\''+row.orderNo+'\',\''+row.settlementMonth+'\')">按计算库存量结转</a><br></shiro:hasPermission>';
                	}
            	}else {
            		edit += '<shiro:hasPermission name="monthlyAccount_updateNotSettlement"><a href="javascript:updateNotSettlement('+row.id+')">修改为未转结</a> <br></shiro:hasPermission>';
				}
                return edit += '<shiro:hasPermission name="monthlyAccount_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
            }
        },  
        
        ],
        formid:'#form1'
    }
    //生成列表信息
    $('#datatable-buttons').mydatatables();

} );

//如果存在 现存量和库存量相等直接结转 如果不等提示 是否强制结转
function surplusNumButton (id,state,orderNo,settlementMonth){
	if(state=='2'){
		$custom.alert.error("该月份已结转");
		return false;
	}
	//debugger 不生成库存调整单了后期可以 
	//var  date= $module.order.getAdjustmentStatus(orderNo,settlementMonth);
	/* if(date!=null){
	     $custom.alert.error("所选数据已生成库存调整单，只能按仓库现存量结转！");
	}else{			
		
			
	} */
	$custom.request.ajax(serverAddr+"/monthlyAccountItem/checkMonthlySurplusNum.asyn",{"id":id},function(data){
				if(data.state==200){
					if(data.data.code=='00'){//校验无问题 
						var message="校验无问题是否进行结转";
						confirmMonthlySurplusNum(id,state,message);
					}else if(data.data.code=='02'){//存在 库存余量和现存量不等的货号 是否强制结转
						var message=data.data.message;
						confirmMonthlySurplusNum(id,state,message);
					}else{
						$custom.alert.error(data.data.message);
					}
				}else{
					$custom.alert.error(data.message);
				}
			});
		

}

//确认按计算库存量结转
function confirmMonthlySurplusNum (id,state,message){
	if(state=='2'){
		$custom.alert.error("该月份已结转");
		return false;
	}
	$custom.alert.warning(message,function(){
		$custom.request.ajax(serverAddr+"/monthlyAccountItem/confirmMonthlySurplusNum.asyn",{"id":id},function(data){
			if(data.state==200){
				if(data.data.code=='00'){
					$custom.alert.success("结转成功");
					//审核成功，重新加载页面
					//$mytable.fn.reload();
					$module.revertList.serializeListForm() ;
					$module.revertList.isMainList = true ;
					list() ;
				}else{
					$custom.alert.error(data.data.message);
				}
			}else{
				$custom.alert.error(data.message);
			}
		});
	});

}
	

//按仓库现存量结转
function availableNumButton (id,state,orderNo,settlementMonth){
	if(state=='2'){
		$custom.alert.error("该月份已结转");
		return false;
	}else{
		var  date= $module.order.getAdjustmentStatus(orderNo,settlementMonth);
		if(date!=null){
			if(date.status!=null&&date.status=='019'){//已调整
				$custom.alert.warning("是否进行月结",function(){
					//检查是否存在差异
					$custom.request.ajax(serverAddr+"/monthlyAccountItem/isDifference.asyn",{"id":id},function(data1){
						if(data1.state==200){
							if(data1.data.code=='00'){
								if(data1.data.status == 'false'){//不存在差异
									//月结
									$custom.request.ajax(serverAddr+"/monthlyAccountItem/confirmMonthlyAvailableNum.asyn",{"id":id},function(data){
										if(data.state==200){
											if(data.data.code=='00'){
												$custom.alert.success("月结成功");
												//审核成功，重新加载页面
												//$mytable.fn.reload();
												$module.revertList.serializeListForm() ;
    											$module.revertList.isMainList = true ;
												list() ;
											}else{
												$custom.alert.error(data.data.message);
											}
										}else{
											$custom.alert.error(data.message);
										}
									});
								}else{//存在差异
									$custom.alert.error("所选数据存在差异，请刷新后再结转！");
								}
							}else{
								$custom.alert.error(data1.data.message);
							}
						}else{
							$custom.alert.error(data1.message);
						}
					});
				});
			}else{//未调整
				$custom.alert.error("所选数据已生成库存调整单未审核，请审核后再结转！");
			}
		}else{
			$custom.alert.warning("是否生成库存调整单",function(){
				$custom.request.ajax(serverAddr+"/monthlyAccountItem/createMonthlyAvailableNum.asyn",{"id":id},function(data){
					if(data.state==200){
						if(data.data.code=='00'){
							$custom.alert.success("已生成库存调整单");
							//审核成功，重新加载页面
							//$mytable.fn.reload();
							$module.revertList.serializeListForm() ;
    						$module.revertList.isMainList = true ;
							list() ;
						}else{
							$custom.alert.error(data.data.message);
						}
					}else{
						$custom.alert.error(data.message);
					}
				});
			});
		}
	}
	
}	
	
function list(){
	$module.load.pageInventory("bls=6005");
}


//详情
function details(id){
	$module.load.pageInventory("bls=6011&id="+id);
}

 function searchData(){
     $mytable.fn.reload();
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
// 点击展开功能
$(".unfold").click(function () {
    $(".form_con").toggleClass('hauto');
    if($(this).text() == '展开功能'){
        $(this).text('收起功能');
    }else{
        $(this).text('展开功能');
    }
})
$(function () {
    $("#datatable-buttons_wrapper").removeClass('container-fluid');
})

     //导出
    $("#exportInventoryBatch").on("click",function(){
        //获取选中的id信息
      var ids="";
        $("input[name='iCheck']:checked").each(function() { // 遍历选中的checkbox
            ids+=$(this).val()+",";
        });
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("请选择一条记录！");
            return ;
        }
        ids=ids.substring(0,ids.length-1); 
        var str = ids.split(',');
        if(str.length>1){
        	 $custom.alert.warningText("只能选择一条记录导出！");
             return ;
        }

        $custom.alert.warning("确定导出勾选的数据？",function(){
           var url=serverAddr+"/monthlyAccount/exportMonthlyAccountMap.asyn?id="+ids;
           $downLoad.downLoadByUrl(url);
        });
    });


//刷新月结账单数据
$("#refreshMonthlyBill").on("click",function(){
    //获取选中的id信息
  var ids="";
   var state="";
    $("input[name='iCheck']:checked").each(function() { // 遍历选中的checkbox
        ids+=$(this).val()+",";
        state+=$(this).attr("monthlyState")+",";
    });
    if(ids==null||ids==''||ids==undefined){
        $custom.alert.warningText("请选择需要刷新的记录！");
        return ;
    }
    ids=ids.substring(0,ids.length-1); 
    state=state.substring(0,state.length-1); 
    if(state.indexOf("2") != -1){
     	$custom.alert.error("已结转的数据不能重新生成月结账单");
     	return ;
    }
    $custom.alert.warning("确定刷新勾选的月结数据？",function(){
      	$custom.request.ajax(serverAddr+"/monthlyAccount/refreshMonthlyBill.asyn",{"ids":ids},function(data){
	    	if(data.state==200){
	    	//	debugger
	    		var status= data.data.status;
	    		if(status==1){
	 			   $custom.alert.success("月结账单刷新成功");
	    		}else{
	    			var errorMessage=data.data.errorMessage;
	    			$custom.alert.error(errorMessage);
	    		}
		      	//重新加载页面
		     	$mytable.fn.reload();
		    }else{
		    	$custom.alert.error(data.message);
		    }
	  });
    });
});
//新增
$("#addMonthlyAccount").click(function(){	
	$('#addMonthly-modal').modal('show');
});

//修改为未转结
function updateNotSettlement(id){
	$custom.alert.warning("确定修改为未转结吗？",function(){
		$custom.request.ajax(serverAddr+"/monthlyAccount/updateNotSettlement.asyn",{"id":id},function(data){
			
				if(data.state==200){
		    		if(data.data.status=='00'){
		 			   $custom.alert.success("修改成功");
				      	//重新加载页面
				     	$mytable.fn.reload();
		    		}else{
		    			$custom.alert.error(data.data.errorMessage);
		    		}
			    }else{
			    	$custom.alert.error(data.message);
			    }
			
		})
		
	})
}

</script>
