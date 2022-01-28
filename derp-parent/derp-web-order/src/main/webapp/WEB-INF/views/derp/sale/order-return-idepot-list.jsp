<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .laydateStart{
        font-size: 13px;
    }
   .laydateEnd{
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
                                <li class="breadcrumb-item"><a href="#">电商订单</a></li>
                                <li class="breadcrumb-item"><a href="#">退货订单管理</a></li>
                            </ol>
                        </div>
                    </div>
                <ul class="nav nav-tabs">
						<shiro:hasPermission name="order_orderList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link"  onclick="$module.load.pageOrder('act=4002');">发货订单管理</a>
							</li>
						</shiro:hasPermission> 
					<shiro:hasPermission name="order_orderReturnIdepotList"> 
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link active"  onclick="$module.load.pageOrder('act=4008');">退货订单管理</a>
							</li>
			 		</shiro:hasPermission> 
					<shiro:hasPermission name="order_businessUnitRecordList"> 
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageOrder('act=40023');">事业部补录列表</a>
							</li>
			 		</shiro:hasPermission> 
	           	</ul>
	           	<div class="tab-content">
                    <!--  title   end  -->
                  <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">来源交易单号 :</span>
                                    <input type="text" class="input_msg" name="externalCode">
                                    <span class="msg_title">入库单号 :</span>
                                    <input type="text" class="input_msg" name=inDepotCode>
                                    <span class="msg_title">退入仓名称 :</span>
                                    <select name="returnInDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>

                                    </div>
                                </div>
                                <div class="form_item h35">
                                   <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformCode" id="storePlatformCode"></select>
                                    <span class="msg_title">订单编号 :</span>
                                    <input type="text" class="input_msg" name="code">
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">退货单号 :</span>
                                    <input type="text" class="input_msg" name="returnCode" id="returnCode">
                                   <span class="msg_title">订单来源 :</span>
                                    <select class="input_msg" name="orderReturnSource"></select>
                                  	<span class="msg_title">退货时间 :</span>
                                    <input type="text" class="laydateStart" name="returnInCreateStartDate" id="returnInCreateStartDate">
                                    —
                                    <input type="text" class="laydateEnd" name="returnInCreateEndDate" id="returnInCreateEndDate">
                                </div>
                                  <div class="form_item h35">
                                   <span class="msg_title">入库时间 :</span>
                                    <input type="text" class="laydateStart" name="returnInStartDate" id="returnInStartDate">
                                    —
                                    <input type="text" class="laydateEnd" name="returnInEndDate" id="returnInEndDate">
                                    </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="order_return_exportOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
           				    <shiro:hasPermission name="order_return_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importOrder">导入</button>
                 			</shiro:hasPermission>
                            <shiro:hasPermission name="order_return_checkConditionsOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="examineOrder">审核</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order_return_delOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delOrder">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">
                                    <input type="checkbox" name="all"/>全选
                                </th>
                                <th style="white-space:nowrap;" class="tc">订单号</th>
                                <th style="white-space:nowrap;" class="tc">平台名称</th>
                                <th style="white-space:nowrap;" class="tc">店铺名称</th>
                                <th style="white-space:nowrap;" class="tc">退款金额</th>
                                <th style="white-space:nowrap;" class="tc">退货时间</th>
                                <th style="white-space:nowrap;" class="tc">入库仓库</th>
                                <th style="white-space:nowrap;" class="tc">入库时间</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
               </div>
                </div>
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"order_returnStatusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="orderReturnSource"]')[0],"order_returnSourceList",null);
$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
    $(function(){
		//初始化
		$mytable.fn.init();
		//配置表格参数
		$mytable.params = {
			url : serverAddr+'/orderReturnIdepot/listOrder.asyn?r=' + Math.random(),
			columns : [
					{ //复选框单元格
						className : "td-checkbox",
						orderable : false,
						data : null,
						render : function(data, type, row, meta) {
							return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.id+'">';
						}
					},
					{data : 'code'},{data : 'storePlatformCodeLabel'},{data : 'shopName'},{data : 'amount'},{data : 'returnInCreateDate'},{data : 'returnInDepotName'},{data : 'returnInDate'},
					{ //操作
						orderable : false,
						data : null,
						render : function(data, type, row, meta) {
							return "";
						}
					}, ],
			formid : '#form1'
		};
		//每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
                $('td:eq(1)', nRow).html("<p class='nowrap'>订单编号:"+(aData.code==null?'':aData.code)+"</p>"
                		+"<p class='nowrap'>来源交易单号:"+(aData.externalCode==null?'':aData.externalCode)+"</p>");
                 $('td:eq(2)', nRow).html("<p class='nowrap'>"+(aData.storePlatformCodeLabel==null?'':aData.storePlatformCodeLabel)+"</p>"
                		 +"<p class='nowrap'>"+(aData.shopTypeCodeLabel==null?'':aData.shopTypeCodeLabel)+"</p>");
                $('td:eq(3)', nRow).html("<p class='nowrap'>"+(aData.shopName==null?'':aData.shopName)+"</p>");
                $('td:eq(4)', nRow).html("<p class='nowrap'>"+(aData.amount==null?'':aData.amount)+"</p>");
                $('td:eq(5)', nRow).html("<p class='nowrap'>"+(aData.returnInCreateDate==null?'':aData.returnInCreateDate)+"</p>");
                $('td:eq(6)', nRow).html("<p class='nowrap'>"+(aData.returnInDepotName==null?'':aData.returnInDepotName)+"</p>"
                		+"<p class='nowrap'>单据状态:"+(aData.statusLabel==null?'':aData.statusLabel)+"</p>");
                $('td:eq(7)', nRow).html("<p class='nowrap'>"+(aData.returnInDate==null?'':aData.returnInDate)+"</p>"); 
                $('td:eq(8)', nRow).html("<p class='nowrap'><a href='javascript:details(" + aData.id + ")''>详情</a></p>"
                		+"<p class='nowrap'><a href='javascript:void(0);'>展开</a></p><p><input type='hidden' value='"+aData.id+"'/></p>"); 
        };
    	//加载仓库的下拉数据
		$module.depot.loadSelectData.call($('select[name="returnInDepotId"]')[0]);
		//生成列表信息
		$('#datatable-buttons').mydatatables();
	});
    // 点击展开功能显示某个电商订单的商品
    $('#datatable-buttons tbody').on( 'click', 'tr td:nth-child(9) p:nth-child(2)', function () {
    	   var thisObj=$(this);
           if($(this).text() == '展开'){ 	
              $(this).text('收起');
              $(this).css({"color":"#007bff","cursor":"pointer"});
              var orderId=$(this).next().children().val();
              var editItem="<tr id='goodsList'><td></td><td colspan='7'><table  width='100%'><thead>"
              					 +"<tr><th class='nowrap'>商品货号</th><th class='nowrap'>商品名称</th><th class='nowrap'>正常品数量</th>"
              					 +"<th class='nowrap'>残次品数量</th><th class='nowrap'>退货总数量</th><th class='nowrap'>销售单价</th><th class='nowrap'>销售金额</th></tr>"; 
              $custom.request.ajax(serverAddr+"/orderReturnIdepot/listItemDetailsById.html", {"id" : orderId}, function(data){
      			 for (var j= 0; j < data.data.itemList.length; j++) {
      			  	var result = data.data.itemList[j];
      			  	var returnNumInt =result.returnNum==null?0:result.returnNum;
      			  	var badGoodsNumInt =result.badGoodsNum==null?0:result.badGoodsNum;
      			  	var returnTotalNum = returnNumInt+badGoodsNumInt;
      				editItem+="<tr><td class='nowrap'>"+result.inGoodsNo+"</td><td class='nowrap'>"+result.inGoodsName+"</td>"
      				+"<td class='nowrap'>"+returnNumInt+"</td><td class='nowrap'>"+badGoodsNumInt+"</td>"
      				+"<td class='nowrap'>"+returnTotalNum+"</td><td class='nowrap'>"+(result.price==null?0:result.price)+"</td>"
      				+"<td class='nowrap'>"+(result.decTotal==null?0:result.decTotal)+"</td></tr>"; 
      			}
      			editItem+="<tr><td class='nowrap' colspan='7'  align='right'>实际退款总金额:"+data.data.amount+"</td></tr></table></td></tr>"; 
      			thisObj.parent("td").parent("tr").after(editItem);
         		});
          }else if($(this).text() == '收起'){
              $(this).text('展开');
              $(this).css({"color":"#007bff","cursor":"pointer"});
              thisObj.parent("td").parent("tr").next().html("");
          }
    	} );
    
    $(".laydateStart").each(function(){
    	   laydate.render({
   	        elem: this, //指定元素
   	        type: 'datetime',
   	        format: 'yyyy-MM-dd HH:mm:ss',
   	        ready: function () {
   	            $('.laydate-btns-time').remove();
   	        }
   	    });
    });
    $(".laydateEnd").each(function(){
    	 laydate.render({
 	        elem: this, //指定元素
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
    });
    $("input[name='all']").click(function(){
        //判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
        if($("input[name='all']").is(":checked")){
            $("input[name='item-checkbox']").prop("checked",true);
        }else{
            $("input[name='item-checkbox']").prop("checked",false);
        }
    });
    //详情
    function details(id){
        $module.load.pageOrder("act=40081&id=" + id);
    }
    //导出
    $("#exportOrder").on("click",function(){
    	//获取选中的id信息
    	var jsonData=null;
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
        $custom.request.ajax(serverAddr+"/orderReturnIdepot/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出10W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>100000){
        			$custom.alert.error("数据超过10W行，请缩小导出时间范围");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/orderReturnIdepot/exportOrder.asyn";
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

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(".unfold").text() == '展开功能'){
            $(".unfold").text('收起功能');
        }else{
            $(".unfold").text('展开功能');
        }
    });
    
    //导入
   	$("#importOrder").click(function(){
   		$module.load.pageOrder("act=40082");
   	});
   	
   	//删除
   	$("#delOrder").click(function(){
   		var checkeds = $.find("input[name='item-checkbox']:checked") ;
   		var ids = new Array() ;
   		
   		if(checkeds.length == 0){
   			$custom.alert.warningText("至少选择一条记录！");
   			return ;
   		}
   		$custom.alert.warning("确定删除数据？",function(){
	   		$(checkeds).each(function(i , m){
	   			ids.push($(m).val());
	   		}) ;
	   		
	   		var idsStr = ids.join(",") ; 
	   		
	   		$custom.request.ajax(serverAddr+"/orderReturnIdepot/delOrder.asyn", {"ids" : idsStr}, function(data){
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
   	
   	//审核
   	$("#examineOrder").click(function(){
   		var checkeds = $.find("input[name='item-checkbox']:checked") ;
   		var ids = new Array() ;	
   		if(checkeds.length == 0){
   			$custom.alert.warningText("至少选择一条记录！");
   			return ;
   		} 		
   		$custom.alert.warning("确定审核？",function(){
	   		$(checkeds).each(function(i , m){
	   			ids.push($(m).val());
	   		}) ;
	   		var idsStr = ids.join(",") ; 		
	   		$custom.request.ajax(serverAddr+"/orderReturnIdepot/checkConditionsOrder.asyn", {"ids" : idsStr}, function(data){
	   			if(data.state==200){
					$custom.request.ajax(serverAddr+"/orderReturnIdepot/examineOrder.asyn",{"ids":idsStr},function(data){
						if(data.state==200){
							$custom.alert.success("审核成功");
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
    
</script>