<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .date-input {
        font-size: 13px;
    }
</style>
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
                                <li class="breadcrumb-item"><a href="#">销售出库清单</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">销售订单编号 :</span>
                                    <input class="input_msg" name="saleOrderCode"/>
                                    <span class="msg_title">出仓仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">销售类型 :</span>
                                    <select  class="input_msg" name="saleType">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">PO号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="poNo">
                                    <span class="msg_title">出库清单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">单据状态 :</span>
                                    <select  class="input_msg" name="status">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
		                             <span class="msg_title">事业部 :</span>
                                     <select class="input_msg" name="buId" id="buId"></select>
                                    <span class="msg_title">发货时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="deliverStartDate" id="deliverStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="deliverEndDate" id="deliverEndDate">
                                </div>
                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a>  -->
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="saleOut_exportSaleOutDepot">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="saleOut_importSaleOut">
                                <div class="btn-group">
                                    <button type="button"
                                            class="btn btn-find dropdown-toggle waves-effect waves-light btn-sm btn_default"
                                            data-toggle="dropdown" aria-expanded="false">
                                        导入 <span class="caret"></span>
                                    </button>
                                    <div class="dropdown-menu" aria-labelledby="btnGroupDrop1"
                                         x-placement="bottom-start"
                                         style="position: absolute; transform: translate3d(0px, 36px, 0px); top: 0px; left: 0px; will-change: transform;">
                                        <a class="dropdown-item" href="javascript:$module.load.pageOrder('act=40032');">出库单导入</a>
                                    </div>
                                </div>
                            </shiro:hasPermission>
							<%--<shiro:hasPermission name="saleOut_importSaleOut">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importOrder">导入</button>
                            </shiro:hasPermission>--%>
                            <shiro:hasPermission name="saleOut_auditSaleOut">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="audit">审核</button>
                            </shiro:hasPermission>
							<shiro:hasPermission name="saleOut_delSaleOut">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delOrder">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>出库清单编号</th>
                                <th>PO号</th>
                                <th>出仓仓库</th>
                                <th>事业部</th>
                                <th>公司</th>
                                <th>客户</th>
                                <th>销售类型</th>
                                <th>销售订单编号</th>
                                <th>发货时间</th>
                                <th>单据状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="saleType"]')[0],"saleOutDepot_saleTypeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"saleOutDepot_statusList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, null,null);
function showHideCode(obj){
    $(obj).prev().toggle();
		//被隐藏了
	if($(obj).prev().is(":hidden")){
		$(obj).prev().prev().show();
		}else{
			$(obj).prev().prev().hide();
		} 
   }

 
    $(document).ready(function() {
    	$('[type="reset"]').click();
    	$module.depot.loadSelectData.call($('select[name="outDepotId"]')[0]);
    	//加载客户的下拉数据
    	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
        // $custom.alert.input();
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/saleOut/listSaleOutDepot.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'poNo'},{data:'outDepotName'},{data:'buName'},{data:'merchantName'},{data:'customerName'},{data:'saleTypeLabel'},{data:'saleOrderCode'},{data:'deliverDate'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width:70,
                    data: null,
                    render: function (data, type, row, meta) {
                    	var details='<shiro:hasPermission name="saleOut_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        return details;
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	// 销售类型为采购执行的上架按钮先屏蔽掉
            if (aData.status == '017'){
                $('td:eq(10)', nRow).html('待出库');
            }else if (aData.status == '018'){
                $('td:eq(10)', nRow).html('已出库');
            }else if (aData.saleType != '3' && aData.status == '027'){
                $('td:eq(10)', nRow).html('出库中');
            }
            if(aData.poNo!=null){	
           	 var itemList = new Array();
           	 if(aData.poNo.indexOf("&")!=-1){
           	 itemList.push(aData.poNo.split("&"));
           	 for (var i = 0; i < itemList.length; i++) {
           		// 如果只有一个PO号
           		if(itemList[i].length==1){
  	        			 $('td:eq(2)', nRow).html(aData.poNo);
  	        		}else{	// 如果只有多个PO号
  	        			// 显示poNo
  	        			$('td:eq(2)', nRow).html("<div name='partPoNo'>"+aData.poNo.split("&")[0]+"</div><div name = 'showdiv' style='display:none;'>"+aData.poNo+"</div><a href='###' onclick='showHideCode(this)''>查看更多</a>");
  	        		}
				}
         	}
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
		
    } );

    function searchData(){
        $mytable.fn.reload();
    }
    //导入
   	$("#importOrder").click(function(){
   		$module.load.pageOrder("act=40032");
   	});
   	//删除
   	$("#delOrder").click(function(){
   	 var ids=$mytable.fn.getCheckedRow();
     if(ids==null||ids==''||ids==undefined){
         $custom.alert.warningText("至少选择一条记录！");
         return ;
     }
   		
   		$custom.alert.warning("确定删除数据？",function(){
	   		
	   		$custom.request.ajax(serverAddr+"/saleOut/delSaleOut.asyn", {"ids" : ids}, function(data){
	   			if(data.state==200){
	   				$custom.alert.success("删除成功");
	   				//重新加载页面
                    $mytable.fn.reload();
	   			}else{
	   				if(!!data.expMessage){
	            		$custom.alert.error(data.expMessage);
	            	}else{
	            		$custom.alert.error(data.message);
	            	}
	   			}
	   		});
   		});
   	});
	//审核并提交
	$("#audit").on("click",function(){
		var url = serverAddr+"/saleOut/auditSaleOutDepot.asyn";
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("确定审核并提交？",function(){
				//执行审核
				$custom.request.ajax(url,{"ids":ids},function(data){
					if(data.state==200){
                        var str = '';
					    if (data.data.failure == 0) {
                            str = "成功："+data.data.success+"条";
                            $custom.alert.success(str);
                        } else {
                            str = "成功："+data.data.success+"条\n失败："+data.data.failure+"条\n";
                            if(!!data.data.failureMsg){
                                str = str + data.data.failureMsg;
                            }
                            $custom.alert.error(str);
                        }
						//重新加载页面
						$mytable.fn.reload();
					}else{
						if(!!data.expMessage){
							$custom.alert.error(data.expMessage);
						}else{
							$custom.alert.error(data.message);
						}
					}
				});
		});
	});

    //详情
    function details(id){
        $module.load.pageOrder("act=40031&id=" + id);
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
  	
		// 时间插件
	    laydate.render({
	        elem: '#deliverStartDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        }
	    });
	    laydate.render({
	        elem: '#deliverEndDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        done: function(value){
	            this.dateTime.hours = 23;
	            this.dateTime.minutes = 59;
	            this.dateTime.seconds = 59;
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
    
    //导出
    $("#exportOrder").on("click",function(){
    	var jsonData=null;
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
	    	var jsonArray=$("#form1").serializeArray();
	        $.each(jsonArray, function(i, field){
	            if(field.value!=null&&field.value!=''&&field.value!=undefined){
	                jsonData=$json.setJson(jsonData,field.name,field.value);
	            }
	        });
		}
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/saleOut/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/saleOut/exportSaleOutDepot.asyn";
        		var i =0;
            	if(!!form){
           		    for(var key in form){
           		    	if(i==0){
               	    		url +="?";
               	    	}else{
               	    		url +="&";
               	    	}
           		    	url +=key+"="+form[key];
           		    	i++;
           		    }
            	}
            	$downLoad.downLoadByUrl(url); 
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
        });
    });
    function dh_list(){
        $module.load.pageOrder("act=4003");
    }
  	
</script>
