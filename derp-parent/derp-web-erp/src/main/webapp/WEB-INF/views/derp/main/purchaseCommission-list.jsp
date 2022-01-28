<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>


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
								<li class="breadcrumb-item"><a href="#">客户档案</a></li>
								<li class="breadcrumb-item"><a
									href="javascript:$load.a('/purchaseCommission/toPage.html');">加价比例配置</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">客户名称 :</span> 
									<select class="input_msg" name="customerId" id="customerId">
										<option value="">请选择</option>
									</select> 
									<span class="msg_title">供应商 :</span> 
									<select name="supplierId" class="input_msg" id="supplierId">
										<option value="">请选择</option>
										<c:forEach items="${supplierBean }" var="supplier">
											<option value="${supplier.value }">${supplier.label }</option>
										</c:forEach>
									</select>
							 		<span class="msg_title">配置类型 :</span>
									<select class="input_msg" name="configType" id="configType"></select>
									<div class="fr">
										<button type="button"
											class="btn btn-find waves-effect waves-light btn_default"
											onclick='searchData();'>查询</button>
										<button type="reset"
											class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-12 mt20">
						<div class="button-list">
							<shiro:hasPermission name="purchaseCommission_add">
								<button type="button"
										class="btn btn-find waves-effect waves-light btn-sm"
										id="add-buttons">新增</button>
							</shiro:hasPermission>

						</div>
					</div>
					<!--  ================================表格 ===============================================   -->
					<div class="row mt10">
						<table id="datatable-buttons" class="table table-striped"
							cellspacing="0" width="100%">
							<thead>
								<tr>
									<th><label
										class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable"
											class="group-checkable" /> <span></span>
									</label></th>
									<th>客户名称</th>
									<th>供应商名称</th>
									<th>配置类型</th>
									<th>销售价回扣(%)</th>
									<th>采购价佣金(%)</th>
									<th>编辑时间</th>
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
			<!-- container -->
		</div>
	</div>
</div>
<!-- content -->
<jsp:include page="/WEB-INF/views/modals/3041.jsp" />
<script type="text/javascript">
	 //加载客户的下拉数据
	$module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
	//加载配置类型的下拉数据
	$module.constant.getConstantListByNameURL.call($('select[name="configType"]')[0],"purchaseCommission_configTypeList",null);

        $(document).ready(function () {

            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url: '/purchaseCommission/listPurchaseCommission.asyn?r='+Math.random(),
                columns: [{ //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                {data: 'customerName'}, {data: 'supplierName'}, {data: 'configTypeLabel'}, {data: 'saleRebate'}, {data: 'purchaseCommission'}, {data: 'modifyDate'},
                { //操作
                    orderable: false,
                    data: null,
                    width:'70px',
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="purchaseCommission_getPurchaseCommission"><a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission>  <shiro:hasPermission name="purchaseCommission_delPurchaseCommission"><a href="javascript:del(' + row.id + ')">删除</a></shiro:hasPermission>';
                    }
                },
                ],
                formid: '#form1'
            };
            
          //每一行都进行 回调
            $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
                if (aData.saleRebate) {
                	
                    $('td:eq(4)', nRow).html(accMul(aData.saleRebate,100));
                }else{
                	$('td:eq(4)', nRow).html("0");
                } 
                if (aData.purchaseCommission ) {
                	
                    $('td:eq(5)', nRow).html(accMul(aData.purchaseCommission,100));
                }else{
                	$('td:eq(5)', nRow).html("0");
                }  
            };
            
            //生成列表信息
            $('#datatable-buttons').mydatatables();
        });

        function searchData() {
            $mytable.fn.reload();
        }
	   	 //新增
	   	 $("#add-buttons").click(function(){
	   	 	$('#comission-modal').modal('show');
	   	 });

        //编辑
        function edit(id) {
        	var url = "/purchaseCommission/getPurchaseCommission.asyn" ;
        	$custom.request.ajaxSync(url , {"id" : id } , function(data){
        		if(data.state == '200'){
    				if(data.data){	
    					$("#modelId").val(data.data.id);
						$("#modelConfigType").val(data.data.configType);
						$("#modelCustomerId").val(data.data.customerId);
    					$("#modelSupplierId").val(data.data.supplierId);
    					$("#modelMerchantId").val(data.data.merchantId);
    					$("#saleRebate").val(accMul(data.data.saleRebate,100));
    					$("#purchaseCommission").val(accMul(data.data.purchaseCommission,100));
    					$('#comission-modal').modal('show');	
    					 
						return false ;

    				}
    			}
        		
    			$custom.alert.error(data.message);
    			
        	}) ;
        }
        
        //删除
        function del(id){
        	
        	var url = "/purchaseCommission/delPurchaseCommission.asyn" ;
        	
        	$custom.request.del(id,url) ; 
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
        });
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    });
    
    //相乘解决精度问题
    function accMul(arg1,arg2){ 
        var m=0,s1=arg1.toString(),s2=arg2.toString(); 
        try{m+=s1.split(".")[1].length}catch(e){} 
        try{m+=s2.split(".")[1].length}catch(e){} 
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
    } 
        
	
    </script>