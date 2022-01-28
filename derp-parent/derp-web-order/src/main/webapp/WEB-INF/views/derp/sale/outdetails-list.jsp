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
                                <li class="breadcrumb-item"><a href="#">销售出库明细</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                           <%--     <div class="form-row h35" >
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-6 col-form-label "><div class="fr">销售订单编号<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input class="form-control" name="saleOrderCode"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-6 col-form-label"><div class="fr">出仓仓库<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <select name="outDepotId" class="form-control">
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${depotBean }" var="depot">
                                                        <option value="${depot.value }">${depot.label }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">销售类型<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select  class="form-control" name="saleType">
                                                    <option value="1">购销</option>
                                                    <option value="2">代销</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-2">
                                        <div class="row">
                                            <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                    <div class="col-1">
                                        <div class="row">
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-6 col-form-label "><div class="fr">po 号<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="poNo">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-6 col-form-label "><div class="fr">出库清单号<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="code">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3 ml15">
                                        <div class="row">
                                            <label class="col-5 col-form-label"><div class="fr">单据状态<span>：</span></div></label>
                                            <div class="col-7 ml10">
                                                <select  class="form-control" name="status">
                                                    <option value="">请选择</option>
                                                    <option value="017">待出库</option>
                                                    <option value="018">已出库</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row h35">
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-6 col-form-label "><div class="fr">商      家<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" required="" parsley-type="text" class="form-control" name="merchantName">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label  class="col-6 col-form-label "><div class="fr">发货时间<span>：</span></div></label>
                                            <div class="col-6 ml10">
                                                <input type="text" class="form-control form_datetime date-input" name="deliverDateStr">
                                            </div>
                                        </div>
                                    </div>
                                </div>--%>
                                <div class="form_item h35">
                                    <span class="msg_title">销售订单编号 :</span>
                                    <input class="input_msg" name="saleOrderCode"/>
                                    <span class="msg_title">出库清单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">唯品账单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="vipsBillNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">LBX单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="lbxNo">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
		                                <option value="">请选择</option>
		                            </select>
                                    <span class="msg_title">出仓仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${depotBean }" var="depot">
                                            <option value="${depot.value }">${depot.label }</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">销售类型 :</span>
                                    <select  class="input_msg" name="saleType"></select>
                                     <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" ></select>
                                    <span class="msg_title" style="width: 150px">商品名称/货号/条码 :</span>
                                    <input class="input_msg" name="goodsStr"/>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="saleOutDetails_exportSaleOutDepotDetails">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" min-width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>销售出库单号</th>
                                <th style="min-width: 150px">销售订单号</th>
                                <th>出库仓名称</th>
                                <th>事业部</th>
                                <th style="width: 150px">唯品账单号</th>
                                <th>LBX单号</th>
                                <th style="min-width: 100px">PO号</th>
                                <th style="min-width: 150px">客户名称</th>
                                <th>销售类型</th>
                                <th>商品货号</th>
								<th style="width: 200px">商品名称</th>
								<th>出库数量</th>
								<th>销售数量</th>
								<th>海外仓理货单位</th>
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
    $(document).ready(function() {
    	//加载事业部
    	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null, null,null);
    	$module.constant.getConstantListByNameURL.call($('select[name="saleType"]')[0],"saleOutDepot_saleTypeList",null);
    	$module.depot.loadSelectData.call($('select[name="outDepotId"]')[0]);
    	//加载客户的下拉数据
    	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
        // $custom.alert.input();
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/saleOutDetails/listSaleOutDepotDetails.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'saleOrderCode'},{data:'outDepotName'},{data:'buName'},{data:'vipsBillNo'},{data:'lbxNo'},{data:'poNo'},{data:'customerName'},{data:'saleTypeLabel'},{data:'goodsNo'},{data:'goodsName',winth:'200px'},{data:'transferNum'},{data:'saleNum'},{data:'tallyingUnitLabel'}
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            var transferNum =aData.transferNum==null?0:aData.transferNum;
            var wornNum =aData.wornNum==null?0:aData.wornNum;
            var totalOutNum = transferNum+wornNum;
        	$('td:eq(12)', nRow).html("<span>" +totalOutNum + "</span>");
        	
        	
            if(aData.customerName != null && aData.customerName != undefined && aData.customerName.length > 20){
                $('td:eq(8)', nRow).html("<span title='" + aData.customerName + "'>" + aData.customerName.substring(0, 20) + "...</span>");
            }
            if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 30){
                $('td:eq(11)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 30) + "...</span>");
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    } );

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

    $(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2
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
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/saleOutDetails/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/saleOutDetails/exportSaleOutDepotDetails.asyn";
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
</script>
