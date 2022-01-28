<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .laydateStart,.laydateEnd{
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
                                <li class="breadcrumb-item"><a href="#">发货订单管理</a></li>
                            </ol>
                        </div>
                    </div>
                <ul class="nav nav-tabs">
						<shiro:hasPermission name="order_orderList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=4002');">发货订单管理</a>
							</li>
						</shiro:hasPermission> 
					<shiro:hasPermission name="order_orderReturnIdepotList"> 
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageOrder('act=4008');">退货订单管理</a>
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
                    <form id="form1" autocomplete="off">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">外部交易单号 :</span>
                                    <input type="text" class="input_msg" name="externalCode">
                                    <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformName" id="storePlatformName">
                                    </select>
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>

                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">订单编号 :</span>
                                    <input type="text" class="input_msg" name="code">
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status">
                                    </select>
                                    <span class="msg_title">交易时间 :</span>
                                    <input type="text" name="tradingStartDate" id="tradingStartDate" class="laydateStart">
                                    -
                                    <input type="text"  name="tradingEndDate" id="tradingEndDate" class="laydateEnd">
                                </div>
                                <div class="form_item h35">
                                    
                                    <span class="msg_title">订单来源 :</span>
                                    <select class="input_msg" name="orderSource">
                                    </select>
                                    <span class="msg_title">平台订单号 :</span>
                                    <input type="text" class="input_msg" name="exOrderId">
                                    <span class="msg_title">出库时间 :</span>
                                    <input type="text"  name="deliverStartDate" id="deliverStartDate" class="laydateStart">
                                    -
                                    <input type="text"   name="deliverEndDate" id="deliverEndDate" class="laydateEnd">
                                    
                                </div>
                                <div class="form_item h35">
		                              <span class="msg_title">店铺 :</span>
		                              <div style="min-width:136px;max-width: 635px;display: inline-block;border:1px solid #D3D3D3">
		                                  <select name="shopCode" class="input_msg goods_select2" id="shopCode">
		                                      <option value="">请选择</option>
		                                      <c:forEach items="${shopList }" var="shop">
		                                          <option value="${shop.shopCode }">${shop.shopName}</option>
		                                      </c:forEach>
				                                  </select>
                                      </div>
                                      <span class="msg_title">查询范围 :</span>
                                      <select class="input_msg" name="orderTimeRange" id="orderTimeRange">
                                          <option value="1">近12个月数据</option>
                                          <option value="2">12个月以前数据</option>
                                      </select>
                                  </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="order_exportOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importOrder">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order_checkConditionsOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="examineOrder">审核</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order_delOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delOrder">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows"  cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">
                                    <input type="checkbox" name="all"/>全选
                                </th>
                                <th style="white-space:nowrap;" class="tc" >订单号</th>
                                <th style="white-space:nowrap;" class="tc">平台名称</th>
                                <th style="white-space:nowrap;" class="tc">店铺名称</th>
                                <th style="white-space:nowrap;" class="tc">实付金额</th>
                                <th style="white-space:nowrap;" class="tc">订单时间</th>
                                <th style="white-space:nowrap;" class="tc">发货仓</th>
                                <th style="white-space:nowrap;" class="tc">发货时间</th>
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
$("button[type='reset']").click(function(){
    $(".goods_select2").each(function(){
       $(this).val("");
   }); 
    $("#shopCode").select2({
		 language:"zh-CN",
		 placeholder: '请选择',
		 multiple: false // 是否允许选择多个值
	});
});
$module.constant.getConstantListByNameURL.call($('select[name="orderSource"]')[0],"dataSourceList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"order_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="storePlatformName"]')[0],"storePlatformList",null);
    $(function(){
        $("#shopCode").select2({
   		 language:"zh-CN",
   		 placeholder: '请选择',
   		 multiple: false	// 是否允许选择多个值
   		});
		//初始化
		$mytable.fn.init();
		//配置表格参数
		$mytable.params = {
			url : serverAddr+'/order/listOrder.asyn?r=' + Math.random(),
			columns : [
					{ //复选框单元格
						className : "td-checkbox",
						orderable : false,
						data : null,
						render : function(data, type, row, meta) {
							return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.id+'">';
						}
					},
					{data : 'code'},{data : 'storePlatformNameLabel'},{data : 'shopName'},{data : 'payment'},{data : 'tradingDate'},{data : 'depotName'},{data : 'deliverDate'},
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
                    + "<p class='nowrap'>外部订单编号:"+(aData.externalCode==null?'':aData.externalCode)+"</p>");
                $('td:eq(2)', nRow).html("<p class='nowrap'>"+(aData.storePlatformNameLabel==null?'':aData.storePlatformNameLabel)+"</p>"
                		+"<p class='nowrap'>"+(aData.shopTypeCodeLabel==null?'':aData.shopTypeCodeLabel)+"</p>");
                $('td:eq(3)', nRow).html("<p class='nowrap'>"+(aData.shopName==null?'':aData.shopName)+"</p>");
                $('td:eq(4)', nRow).html("<p class='nowrap'>"+(aData.payment==null?'':aData.payment)+"</p>");
                $('td:eq(5)', nRow).html("<p class='nowrap'>"+(aData.tradingDate==null?'':aData.tradingDate)+"</p>");
                $('td:eq(6)', nRow).html("<p class='nowrap'>"+(aData.depotName==null?'':aData.depotName)+"</p>"
                		+"<p class='nowrap'>单据状态:"+(aData.statusLabel==null?'':aData.statusLabel)+"</p>");
                $('td:eq(7)', nRow).html("<p class='nowrap'>"+(aData.deliverDate==null?'':aData.deliverDate)+"</p>"); 
                $('td:eq(8)', nRow).html("<p class='nowrap'><shiro:hasPermission name='order_detail'><a href='javascript:details(" + aData.id + ")'>详情</a></shiro:hasPermission></p>"
                		+"<p class='nowrap'><a href='javascript:void(0);'>展开</a></p><p><input type='hidden' value='"+aData.id+"'/></p>"); 
                
		};
      //加载仓库的下拉数据
        $module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
		//生成列表信息
		$('#datatable-buttons').mydatatables();
	});
    
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
    // 点击展开功能显示某个电商订单的商品
    $('#datatable-buttons tbody').on( 'click', 'tr td:nth-child(9) p:nth-child(2)', function () {
    	var thisObj=$(this);
           if($(this).text() == '展开'){
              $(this).text('收起');
              $(this).css({"color":"#007bff","cursor":"pointer"});
              var orderId=$(this).next().children().val();
              var editItem="<tr id='goodsList'><td></td><td colspan='7'><table  width='100%'>"
              					 +"<tr ><th class='nowrap'>商品货号</th><th class='nowrap'>商品名称</th><th class='nowrap'>销售数量</th>"
              					 +"<th class='nowrap'>销售单价</th><th class='nowrap'>销售总金额</th><th class='nowrap'>商品优惠金额</th></tr>";   
              $custom.request.ajax(serverAddr+"/order/listItemDetailsById.html", {"id" : orderId}, function(data){
      			 for (var j= 0; j < data.data.itemList.length; j++) {
      			  	var result = data.data.itemList[j];
      				editItem+="<tr><td class='nowrap'>"+(result.goodsNo==null?'':result.goodsNo)+"</td><td class='nowrap'>"+(result.goodsName==null?'':result.goodsName)+"</td>"
      				+"<td class='nowrap'>"+result.num+"</td><td class='nowrap'>"+(result.originalPrice==null?0:result.originalPrice)+"</td>"
      				+"<td class='nowrap'>"+(result.originalDecTotal==null?0:result.originalDecTotal)+"</td><td class='nowrap'>"+(result.goodsDiscount==null?0:result.goodsDiscount)+"</td></tr>"; 
      			}
      			editItem+="<tr><td></<td><td></<td><td class='nowrap'>运费:"+data.data.wayFrtFee+"</td><td class='nowrap'>税费:"+data.data.taxes+"</td>"
  				+"<td class='nowrap'>优惠金额:"+(data.data.discount==null?0:data.data.discount)+"</td><td class='nowrap'>实付金额:"+data.data.payment+"</td></tr></table></td></tr>";
  				thisObj.parent("td").parent("tr").after(editItem);
         		}); 
          }else if($(this).text() == '收起'){
              $(this).text('展开');
              $(this).css({"color":"#007bff","cursor":"pointer"});
      		  thisObj.parent("td").parent("tr").next().html("");
          }
    	});
 
    $("input[name='all']").click(function(){
        //判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
        if($(this).is(":checked")){
            $("input[name='item-checkbox']").prop("checked",true);
        }else{
            $("input[name='item-checkbox']").prop("checked",false);
        }
    });
    //详情
    function details(id){
        $module.load.pageOrder("act=40021&id=" + id);
    }

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
        // 点击导出时，判断交易时间搜索条件是否填写，且日期宽度在31天内，若不满足提示“请限制导出时间为30天”，
        // 若满足条件，则导出，取消导出的1万行限制。若导出的数据超过10W行，提示“数据超过10W行，请缩小导出时间范围”。
        /*var tradingStartDateStr = $("#tradingStartDate").val();
        var tradingEndDateStr = $("#tradingEndDate").val();
        var deliverStartDateStr = $("#deliverStartDate").val();
        var deliverEndDateStr = $("#deliverEndDate").val();
        if (tradingStartDateStr && tradingEndDateStr) {
            var tradingEndDate = new Date(tradingEndDateStr);
            var tradingStartDate = new Date(tradingStartDateStr);
            var days = tradingEndDate.getTime() - tradingStartDate.getTime();
            var day = parseInt(days / (1000 * 60 * 60 * 24));
            if (day > 30) {
                $custom.alert.error("请限制导出时间为30天!");
                return;
            }
        } else if(deliverStartDateStr && deliverEndDateStr) {
            var deliverStartDate = new Date(deliverStartDateStr);
            var deliverEndDate = new Date(deliverEndDateStr);
            var days = deliverEndDate.getTime() - deliverStartDate.getTime();
            var day = parseInt(days / (1000 * 60 * 60 * 24));
            if (day > 30) {
                $custom.alert.error("请限制导出时间为30天!");
                return;
            }
        } else {
            $custom.alert.error("请限制导出时间为30天!");
            return;
        }*/

        $custom.request.ajax(serverAddr+"/order/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出10W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>150000){
        			$custom.alert.error("数据超过15W行，请缩小导出时间范围");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/order/exportOrder.asyn";
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
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    //导入
   	$("#importOrder").click(function(){
   		$module.load.pageOrder("act=40022");
   	});
   	
   	//删除
   	$("#delOrder").click(function(){
   		//alert($(this).text());
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
	   		
	   		$custom.request.ajax(serverAddr+"/order/delOrder.asyn", {"ids" : idsStr}, function(data){
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
   		$custom.alert.warning("确定出库？",function(){
	   		$(checkeds).each(function(i , m){
	   			ids.push($(m).val());
	   		}) ;
	   		
	   		var idsStr = ids.join(",") ; 
	   		
	   		//返回商品如果是美赞臣商品转成原货号
	   		$custom.request.ajax(serverAddr+"/order/checkConditionsOrder.asyn", {"ids" : idsStr}, function(data){
	   			if(data.state==200){
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
						//查询可用量
      	        	  	var availableNum = $module.inventory.queryAvailability(outDepotId,goodsId,outDepotCode,null,null,null,null);
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
					
					//执行出库
					$custom.request.ajax(serverAddr+"/order/examineOrder.asyn",{"ids":idsStr},function(data){
						if(data.state==200){
							$custom.alert.success("出库成功");
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
