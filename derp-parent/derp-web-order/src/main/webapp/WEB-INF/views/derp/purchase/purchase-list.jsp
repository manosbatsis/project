<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">采购档案</a></li>
                                <li class="breadcrumb-item"><a href="javascript:dh_list();">采购订单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">采购订单编号 :</span>
                                    <input class="input_msg" type="text" name="code" id="code" required="" >
                                    <span class="msg_title">入库仓库 :</span>
                                    <select name="depotId" class="input_msg" id="depotId">
                                        <option value="">全部</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">供应商 :</span>
                                    <select name="supplierId" class="input_msg" id="supplierId">
                                    	<option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData(); ' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                    </select>
                                    <span class="msg_title">PO号 :</span>
                                    <input class="input_msg" type="text" name="poNos" id="poNos" required="" >
                                    <span class="msg_title">订单状态 :</span>
                                    <select class="input_msg" name="status" id="status">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">入库单编码 :</span>
                                    <input class="input_msg" type="text" name="warehouseCode" id="warehouseCode" required="warehouseCode" >
                                    <span class="msg_title">录单日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createStartDate" id="createStartDate" style="font-size:13px;">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createEndDate" id="createEndDate" style="font-size:13px;">
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">业务模式 :</span>
                                    <select class="input_msg" name="businessModel" id="businessModel">
                                    </select>
                                    <span class="msg_title">是否完结 :</span>
                                    <select class="input_msg" name="isEnd" id="isEnd">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">金额调整状态 :</span>
									<select name="amountAdjustmentStatus" class="input_msg"> </select>
									<span class="msg_title">金额确认状态 :</span>
									<select name="amountConfirmStatus" class="input_msg"></select>
                                </div>
                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a> -->
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt-5">
                        <div class="button-list">
                            <shiro:hasPermission name="purchase_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_endPurchaseOrderCheck">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm " id="end-buttons">完结入库</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_delPurchaseOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="deleteObject();">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_generateDeclareOrder">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="generate-buttons">生成预申报单</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_inWarehouse">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="in-warehouse-buttons">中转仓入库</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_trade_link">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="trade-link-buttons">创建交易链路</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_sd_create">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="sd-create-buttons">创建采购SD单</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="purchase_exportPurchase">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-button">导出</button>
                            </shiro:hasPermission>
							<shiro:hasPermission name="purchase_copy">
                            	<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="copy-buttons" >复制</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purchase_to_sale">
                            	<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="to-sale-buttons" >转销售订单</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="purchase_to_financing">
                            	<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="to-financing-buttons" >生成融资代采</button>
							</shiro:hasPermission>
                        </div>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>采购订单编号</th>
                                <th>入仓仓库</th>
                                <th>事业部</th>
                                <th>供应商</th>
                                <th>po号</th>
                                <th>订单状态</th>
                                <th>数量/总金额</th>
                                <th>生成预申报单</th>
                                <th>金额调整</th>
                                <th>付款状态</th>
                                <th>业务模式</th>
                                <th>融资申请号</th>
                                <th>录单日期</th>
                                <th style="width:200px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<jsp:include page="/WEB-INF/views/modals/2010.jsp" />
<jsp:include page="/WEB-INF/views/modals/2011.jsp" />
<jsp:include page="/WEB-INF/views/modals/2014.jsp" />
<jsp:include page="/WEB-INF/views/modals/16001.jsp" />
<jsp:include page="/WEB-INF/views/modals/4001.jsp" />
<jsp:include page="/WEB-INF/views/modals/4003.jsp" />
<jsp:include page="/WEB-INF/views/modals/4004.jsp" />
<jsp:include page="/WEB-INF/views/modals/4006.jsp" />
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="isEnd"]')[0],"purchaseOrder_isEndList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"purchaseOrder_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="businessModel"]')[0],"purchaseOrder_businessModelList",null);
$module.constant.getConstantListByNameURL.call($('select[name="amountAdjustmentStatus"]')[0],"purchaseOrder_amountAdjustmentStatusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="amountConfirmStatus"]')[0],"purchaseOrder_amountConfirmStatusList",null);
    $(document).ready(function() {

        //重置按钮
        $("button[type='reset']").click(function(){
        });
        
        laydate.render({
            elem: '#createStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            trigger: 'click'
        });

        laydate.render({
            elem: '#createEndDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
            done: function(value){
                this.dateTime.hours = 23;
                this.dateTime.minutes = 59;
                this.dateTime.seconds = 59;
            },
            trigger: 'click'

        });
		
        //根据入库仓库加载
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0], null, null);
        //根据供应商加载
        $module.supplier.loadSelectData.call($("select[name='supplierId']")[0],"");
        //事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);

        //初始化
        $mytable.fn.init();
        //配置表格参数
        		
        $mytable.params={
            url: serverAddr+'/purchase/listPurchaseOrder.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'depotName'},{data:'buName'},{data:'supplierName'},{data:'poNo'},
                {data:'statusLabel'},
                {data:'',
                	render: function (data, type, row, meta) {
                		
                		var html = "" ;
                		
                		if(row.totalNum){
                			html += row.totalNum + "<br/>" ;
                		}
                		
                		if(row.currency){
                			html += row.currency + " " ;
                		}
                		
                		if(row.goodsAmount){
                			html += row.goodsAmount ;
                		}
                		
                		return html 
                	}
                },{data:'isGenerateLabel'},{
                	data:null ,
                	render:function(data, type, row, meta){
                		var html = row.amountAdjustmentStatusLabel ;
                		
                		if(row.status != '001' && row.isAmountAdjustmentAble == '1'){
                			html += '<br><shiro:hasPermission name="purchase_changeAmount"><a href="#" onclick="changeAmount('+row.id+')">修改</a></shiro:hasPermission>' ;
                		}
                		
               			html += '<br>' ;
               			
                		html += row.amountConfirmStatusLabel;
						if(row.status != '001' && row.isAmountConfirmAble == '1'){
                			html += '<br><shiro:hasPermission name="purchase_confirmAmount"><a href="#" onclick="confirmAmount('+row.id+')">确认</a></shiro:hasPermission>' ;
                		}
                		
                		return html ;
                	}
                },
                {data:'billStatusLabel'},{data:'businessModelLabel'},{data:'financingNo'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "80px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        if(row.status == '001'){
                        	html += ' <shiro:hasPermission name="purchase_edit"><a href="javascript:edit('+row.id+')">编辑</a> </shiro:hasPermission></br>';
                        }
                        
                        if(row.status == '002'){
                        	html += ' <shiro:hasPermission name="purchase_audit"><a href="javascript:edit('+row.id+')">审核</a> </shiro:hasPermission></br>';
                        }
                        
                        html += ' <shiro:hasPermission name="purchase_detail"><a href="javascript:details('+row.id+')">详情</a><br/></shiro:hasPermission>';
                        html += ' <shiro:hasPermission name="purchase_view_contract"><a href="javascript:viewContract('+row.id+','+row.supplierId+')">查看合同</a><br/></shiro:hasPermission>';
                        
                        if(row.status == '007'||row.status == '003' ){                        	
                        	if(row.drawInvoiceDate==null){                     		
                        		html += ' <shiro:hasPermission name="purchase_receiveInvoice"><a href="javascript:receiveInvoice('+row.id+',\'' + row.code + '\')">收到发票</a><br/></shiro:hasPermission> ';
                        	}                       	
                        	if(row.payDate==null){
                        		html += ' <shiro:hasPermission name="purchase_alreadyPay"><a href="javascript:alreadyPay('+row.id+',\'' + row.code + '\')">录入付款</a><br/></shiro:hasPermission>';
                        	}
                        }
                        
                        if(row.isInDepotAble == '1'){
                        	html += ' <shiro:hasPermission name="purchase_in"><a href="javascript:purchaseIn('+row.id+')">上架入库</a><br/></shiro:hasPermission>';
                        }
                        
                        html += '<shiro:hasPermission name="purchase_toAttachment"><a href="javascript:toAttachment(\''+row.code+'\')">附件管理</a><br/></shiro:hasPermission>' ;
                        
                        return html;
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
        
        //过滤下拉框已删除选项
        $('#status option[value="006"]').remove() ;
        
    });

    function searchData(){
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function(){
        $load.a(pageUrl+"30011&pageSource=2");
    });

    //详情
    function details(id){
        $module.load.pageOrder("act=30013&id="+id);
    }

    //编辑
    function edit(id){
        $module.load.pageOrder("act=30012&id="+id);
    }
    
  	//查看合同
    function viewContract(id, customerId){
        var url = $module.params.loadOrderPageURL+"act=30016&id="+id ;
  		$m16001.init(url, customerId) ;
    }
    
    //收到发票
    function receiveInvoice(id, code){	
	  	
	  var url = serverAddr+"/purchase/getPurchaseItem.asyn" ;
	  //获取采购订单商品
	  $custom.request.ajax(url, {"id" : id }, function(result){
				if(result.state == '200'){
					
					$("#itemTbody").empty() ;
					var items = result.data ;
					
					var html = "" ;
					$(items).each(function( index , item){
						
						var amount = item.amount ; 
						amount = amount.toFixed(2) ;
						var price = item.price ;
						price = price.toFixed(8) ;
						
						html += '<tr>' + 
							'<td>'+item.goodsNo+'</td>'+
							'<td>'+item.goodsName+'</td>' +
							'<td>'+item.num+'</td>' + 
							'<td>'+price+'</td>' + 
							'<td>'+amount+'</td>' +
							'<td><input type="text" name="amount" value="'+amount+'" onblur="changePrice(this)"></td>' +
							'</tr>' ;
							
					}) ;
					
					$("#itemTbody").append(html) ;
					
				}else{
					$custom.alert.error(result.message);
				}
			});
			  
    	$('#id').val(id);   
    	$('#code_invoice').val(code);
    	$('#invoice-modal').modal('show');
		$module.revertList.serializeListForm() ;
		$module.revertList.isMainList = true ;    	
    	
    }
  //已经付款
    function alreadyPay(id, code){
    	$('#pay_id').val(id);
    	$('#code_pay').val(code);
    	$('#pay-modal').modal('show');
    	$module.revertList.serializeListForm() ;
	    $module.revertList.isMainList = true ; 
    }

    //完结入库
    $("#end-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.request.ajax(serverAddr+"/purchase/endPurchaseOrderCheck.asyn",{"ids":ids},function(data1){
        	if(data1.data == '状态必须为已审核'){
        		$custom.alert.error(data1.data);
        	}else{
        		// 勾稽比例为0时只提示消息即可
        		var resultData = data1.data;
        		var resultLength = resultData.length;
        		var resultSub = resultData.substring(resultLength-5,resultLength);

        		if(resultSub=="占0.0%"){
        			$custom.alert.error(resultData);
        		}else{
        			$custom.alert.warning(data1.data,function(){
    	                $custom.request.ajax(serverAddr+"/purchase/endPurchaseOrder.asyn",{"ids":ids},function(data2){
    	                    if(data2.state==200){
    	                        $custom.alert.success("完结入库成功");
    	                        //完结入库成功，重新加载页面
    	                        $mytable.fn.reload();
    	                    }else{
    	                        $custom.alert.error(data2.message);
    	                    }
    	                });
    	            });
        		}
        	}
        });
    });

    /*
    * 生成预申报单
    */
    $("#generate-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $custom.alert.warning("确定生成选中对象？",function(){
            $custom.request.ajax(serverAddr+"/purchase/generateDeclareOrder.asyn",{"ids":ids},function(data){
                if(data.state==200){
                    if(data.data.indexOf('生成预申报成功')!=-1){
                        $custom.alert.success("生成预申报成功");
                        var obj = data.data.split(",");
                        //成功，重新加载页面
                        setTimeout(function () {
                            // $load.a(pageUrl+"30021&id="+obj[1])
                            // 跳转新系统(与申报单编辑页)
                            var hostname = window.location.hostname
                            var array = hostname.split('.') || []
                            var flag = true
                            for (var i = 0; i < array.length; i++) {
                                if (isNaN(array[i])) {
                                    flag = false
                                    break
                                }
                            }
                            if (flag) { // ip
                                console.log('测试环境')
                                var ip = '10.10.102.208:89'
                                $load.a('http://'+ ip +'/purchase/predeclarationEdit?from=oldsystem&other=gh&id=' + obj[1])
                            } else { // 域名 正式环境
                                console.log('正式环境')
                                $load.a('http://depx.topideal.mobi/purchase/predeclarationList?from=oldsystem&other=gh' + obj[1])
                            }
                        }, 1000);
                    }else{
                        $custom.alert.error(data.data);
                    }
                }else{
                    $custom.alert.error(data.message);
                }
            });
        });
    });

    // 点击中转仓入库，弹框选择入库时间
    $("#in-warehouse-buttons").click(function(){
        var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
        $("#inWarehouseIds").val(ids);
		$('#in-warehouse-modal').modal('show');
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
    })
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    })

    function dh_list(){
        $module.load.pageOrder("act=3001");
    }

    //批量导入
    $("#import-buttons").click(function(){
        $module.load.pageOrder("act=30014");
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
    
    //删除
    function deleteObject(){
    	$custom.request.delChecked(serverAddr+'/purchase/delPurchaseOrder.asyn');
    }
    
    //导出
    $("#export-button").click(function(){
    	
    	var url = "" ;
    	
    	var ids=$mytable.fn.getCheckedRow();
    	if(ids){
    		url = serverAddr+"/purchase/exportPurchase.asyn?ids="+ids ;
    	}else{
    		var code = $("#code").val();
        	var depotId = $("#depotId").val();
        	var supplierId = $("#supplierId").val();
        	var isEnd = $("#isEnd").val();
        	var poNo = $("#poNos").val();
        	var status = $("#status").val();
        	var buId = $("#buId").val();
        	var createStartDate = $("#createStartDate").val();
        	var createEndDate = $("#createEndDate").val();
        	var warehouseCode = $("#warehouseCode").val();
        	
        	url = serverAddr+"/purchase/exportPurchase.asyn?code="+code+"&warehouseCode="
    		+warehouseCode+"&isEnd="+isEnd+"&depotId="+depotId+"&supplierId="+supplierId+"&buId=" + buId
    		+"&poNos="+poNo+"&status="+status+"&createStartDate="+createStartDate+"&createEndDate="+createEndDate ;
        	
    	}
    	
    	$custom.alert.warning("确定导出选中对象？",function(){
    		$downLoad.downLoadByUrl(url) ;
        });
    	
    });
    
    function changePrice(org){
		var tr = $(org).parent().parent() ;
		
		var amount = tr.find("input[name='amount']").val() ;
		
		if(!amount || ! /^[0-9]+(.[0-9]+)?$/.test(amount)){
			$custom.alert.error("请输入数值");
			return ;
		}
		
		amount = parseFloat(amount) ;
		
		if(amount < 0) {
			$custom.alert.error("请输入大于0的数值");
			return ;
		}
		
	} 
    
  	//复制
    $("#copy-buttons").click(function(){
    	var ids=$mytable.fn.getCheckedRow(); 
    	
    	if(!ids || ids.indexOf(",") > -1){
    		$custom.alert.error("请选择一项复制");
    		
    		return ;
    	}
    	
    	$load.a(pageUrl+"30011&type=purchaseCopy&ids="+ids);
    }) ;
  	
  //附件管理
    function toAttachment(code){
    	$module.load.pageOrder("act=200001&codeid=" + code);
    }
  
    //创建交易链路
  	$("#trade-link-buttons").click(function(){
  		var ids=$mytable.fn.getCheckedRow(); 
  		if(!ids || ids.indexOf(",") > -1){
    		$custom.alert.error("请选择一条数据");
    		
    		return ;
    	}
  		
  		$custom.request.ajax(serverAddr+"/purchase/toSaleStepBeforeCheck.asyn",{"id":ids},function(data){
            if(data.state==200){
            	
            	if(data.data == true){
            		$module.load.pageOrder("act=30017&id=" + ids) ;
            	}else{
            		$custom.alert.error("采购订单状态必须为【已审核】【 已完结】");
            	}
            	
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
        });
  		
  		
  		
  	}) ;
    
  	//创建采购SD单
  	$("#sd-create-buttons").click(function(){
  		var ids=$mytable.fn.getCheckedRow(); 
  		if(!ids || ids.indexOf(",") > -1){
    		$custom.alert.error("请选择一条数据");
    		
    		return ;
    	}
  		
  		$custom.request.ajax(serverAddr+"/purchase/createSdOrderCheck.asyn",{"id":ids},function(data){
            if(data.state==200){
            	$m4003.init(ids) ;
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
        });
  		
  	}) ;
  	
  	//转销售
  	$("#to-sale-buttons").click(function(){
  		var url = serverAddr+"/sale/createPurchase.asyn";
  		
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(!ids || ids.indexOf(",") > -1){
			$custom.alert.warningText("请选择一条记录！");
			return ;
		}
		
		$m4004.init(ids) ;
  	}) ;
  	
  //转代采
  	$("#to-financing-buttons").click(function(){
  		
  		var ids=$mytable.fn.getCheckedRow();
        if(ids==null||ids==''||ids==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return ;
        }
		
        $custom.request.ajax(serverAddr+"/purchase/createFinancingOrderCheck.asyn",{"ids":ids},function(data){
            if(data.state==200){
            	$module.load.pageOrder("act=300102&ids=" + ids);
            }else{
            	if(data.expMessage != null){
					$custom.alert.error(data.expMessage);
				}else{
					$custom.alert.error(data.message);
				}
            }
        });
        
  	}) ;
    
    //金额调整
    function changeAmount(id){
    	$m4001.init(id) ;
    }
    
  	//金额调整确认
    function confirmAmount(id){
    	$m4006.init(id) ;
    }
    
    //打托入库
    function purchaseIn(id){
    	$module.load.pageOrder("act=300101&id=" + id);
    }
</script>