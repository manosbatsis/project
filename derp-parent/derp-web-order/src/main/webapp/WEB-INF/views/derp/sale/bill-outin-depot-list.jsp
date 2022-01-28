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
                                <li class="breadcrumb-item"><a href="#">账单出库单</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">出仓仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                    </select>
                                    <span class="msg_title">账单号:</span>
                                    <input class="input_msg" name="billCode"/>
                                    <span class="msg_title">单据状态 :</span>
                                    <select  class="input_msg" name="state">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">账单出库单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">处理类型 :</span>
                                    <select name="processingType" class="input_msg">
                                    </select>
                                    <span class="msg_title">调整类型 :</span>
                                    <select name="operateType" class="input_msg">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" class="input_msg">
                                    </select>
                                    <span class="msg_title">账单来源 :</span>
                                    <select name="billSource" class="input_msg">
                                    </select>
                                    <span class="msg_title">客户 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="customerName">
                                </div>
                                 <div class="form_item h35">
                                    <span class="msg_title">账单出库时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="deliverStartDate" id="deliverStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="deliverEndDate" id="deliverEndDate">
                                </div>
                            </div>
                            
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="billOutindepot_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="billOutindepot_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importOrder">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="billOutindepot_audit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="auditOrder">审核</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="billOutindepot_delete">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="deleteOrder">删除</button>
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
                                <th>账单出库单号</th>
                                <th>客户</th>
                                <th>仓库</th>
                                <th>事业部</th>
                                <th>平台账单号</th>
                                <th>处理类型</th>
                                <th>账单出库时间</th>
                                <th>账单总量</th>
                                <th>账单金额</th>
                                <th>币种</th>
                                <th>库存调整类型</th>
                                <th>单据状态</th>
                                <th>账单来源</th>
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
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"billOutinDepot_stateList",null);
$module.constant.getConstantListByNameURL.call($('select[name="processingType"]')[0],"processingTypeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="operateType"]')[0],"billOutinDepot_operateTypeList",null);
$module.constant.getConstantListByNameURL.call($('select[name="billSource"]')[0],"billOutinDepot_billSourceList",null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($('select[name="buId"]')[0],null,null,null,null);
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
    	$module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0], "", {});
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/billOutinDepot/listBillOutinDepot.asyn?r='+Math.random(),
            columns:[
            	{ //复选框单元格
                    orderable: false,
                    width: "30px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data:'code'},{data:'customerName'},{data:'depotName'},{data:'buName'},{data:'billCode'},{data:'processingTypeLabel'},{data:'deliverDate'},{data:'totalNum'},{data:'totalAmount'},
                {data:'currencyLabel'}, {data:'operateTypeLabel'},{data:'stateLabel'},{data:'billSourceLabel'},
                { //操作
                    orderable: false,
                    width:70,
                    data: null,
                    render: function (data, type, row, meta) {
                    	return '<shiro:hasPermission name="billOutindepot_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
		
    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //详情
    function details(id){
        $module.load.pageOrder("act=40111&id=" + id);
    }
  	
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
        
        $custom.request.ajax(serverAddr+"/billOutinDepot/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/billOutinDepot/exportBillOutinDepot.asyn";
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
        $module.load.pageOrder("act=4011");
    }
    
  //导入
    $("#importOrder").on("click",function(){
    	$module.load.pageOrder("act=40112");
    });
  
  //删除
    $("#deleteOrder").on("click",function(){
    	$custom.request.delChecked(serverAddr+'/billOutinDepot/delBillOutinDepot.asyn');
    });
  
  //审核并提交
	$("#auditOrder").on("click",function(){
		var url = serverAddr+"/billOutinDepot/auditBillOutinDepot.asyn";
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("确定审核并提交？",function(){
			//获取选中订单的所有商品和对应数量（相同商品数量叠加）
			$custom.request.ajax(serverAddr+"/billOutinDepot/getOrderGoodsInfo.asyn",{"ids":ids},function(data){
				var flag = true;
				var map = data.data;
				
				for(var key in map){
					var keys = key.split('-');
					var outDepotId = keys[1];
					var goodsId = keys[0];
					var outDepotCode = keys[2];
					var goodsNo = keys[3];
					var unit=keys[4];
					var num = map[key];
					var tallyingUnit='';
					if ('HK01'==outDepotCode) {
						if ('00'==unit) {
							tallyingUnit='0';
						}else if ('01'==unit) {
							tallyingUnit='1';
						}else if ('02'==unit) {
							tallyingUnit='2';
						}
					}
					
					//查询可用量
  	        	  	var availableNum =$module.inventory.queryAvailability(outDepotId,goodsId,outDepotCode,tallyingUnit,null,null,null);
  	        		//判断库存量是否足够
  	        	  if(availableNum ==-1){
  	        		  flag = false;
	        		  $custom.alert.warningText("商品货号："+goodsNo+",未查到库存可用量");
				      break;
	        	  }else if(num>availableNum){
	        		  flag = false;
					  $custom.alert.warningText("库存不足，商品货号："+goodsNo+",可用量："+availableNum);
					  break;
				  }
				}
				if(!flag){
					return;
				}
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
	});
  
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
    });
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    });

</script>
