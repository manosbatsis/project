<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
								<li class="breadcrumb-item"><a href="#">销售退货入库单</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<%--<div class="form-row">
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label "><div class="fr">
												退运入库单号<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<input class="form-control" name="code" />
											</div>
										</div>
									</div>
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label"><div class="fr">
												退入仓库<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<select name="inDepotId" class="form-control">
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
											<label class="col-5 col-form-label"><div class="fr">
												客户<span>：</span>
											</div></label>
											<div class="col-7 ml10">
												<input type="text" class="form-control" name="customerName">
											</div>
										</div>
									</div>
									<div class="form-group col-2">
										<div class="row">
											<button type="button"
													class="btn ml15 btn-find waves-effect waves-light"
													onclick='$mytable.fn.reload();'>查询</button>
											<button type="reset" class="btn btn-light ml15 waves-effect waves-light">重置</button>
										</div>
									</div>
								</div>
								<div class="form-row">
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label "><div class="fr">
												单据状态<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<select class="form-control" name="status">
													<option value="">请选择</option>
													<option value="012">已入仓</option>
												</select>
											</div>
										</div>
									</div>
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label"><div class="fr">
												企业退运单号<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<input type="text" class="form-control" name="merchantReturnNo">
											</div>
										</div>
									</div>
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label"><div class="fr">
												销售退货编号<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<input type="text" class="form-control" name="orderCode">
											</div>
										</div>
									</div>
								</div>--%>
								<div class="form_item h35">
									<span class="msg_title">销退入库单号 :</span>
									<input class="input_msg" name="code" />
									<span class="msg_title">退入仓库 :</span>
									<select name="inDepotId" class="input_msg">
										<option value="">请选择</option>
										<c:forEach items="${depotBean }" var="depot">
											<option value="${depot.value }">${depot.label }</option>
										</c:forEach>
									</select>
									<span class="msg_title">客户 :</span>
									<input type="text" class="input_msg" name="customerName">
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">单据状态 :</span>
									<select class="input_msg" name="status">
									</select>
									<span class="msg_title">销退单号 :</span>
									<input type="text" class="input_msg" name="orderCode">
								    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId"></select>
								</div>
								<div class="form_item h35">
									<span class="msg_title">退货入库时间 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="returnInDateStartDate" id="returnInDateStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="returnInDateEndDate" id="returnInDateEndDate">
								</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-12 mt20">
						<div class="button-list">
							<shiro:hasPermission name="saleReturnIdepot_exportSaleReturnIdepot">
								<button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
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
								<th style="white-space: nowrap;" class="tc">销退入库单号</th>
								<th style="white-space: nowrap;" class="tc">合同号</th>
								<th style="white-space: nowrap;" class="tc">退入仓库</th>
								<th style="white-space: nowrap;" class="tc">退出仓库</th>
								<th style="white-space: nowrap;" class="tc">事业部</th>
								<th style="white-space: nowrap;" class="tc">退货入库数量</th>
								<th style="white-space: nowrap;" class="tc">销退单号</th>
								<th style="white-space: nowrap;" class="tc">单据状态</th>
								<th style="white-space: nowrap;" class="tc">退货入库时间</th>
								<th style="white-space: nowrap;" class="tc">操作</th>
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
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"saleReturnIdepot_statusList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null, null,null);
    $(document).ready(
        function() {
        	//加载仓库
            $module.depot.loadSelectData.call($('select[name="inDepotId"]')[0]);
            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url : serverAddr+'/saleReturnIdepot/listSaleReturnIdepot.asyn?r=' + Math.random(),
                columns : [
                    { //复选框单元格
                        className : "td-checkbox",
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return '<input type="checkbox" class="iCheck">';
                        }
                    },
                    {data : 'code'},{data : 'contractNo'},{data : 'inDepotName'},{data : 'outDepotName'},{data : 'buName'},{data : 'returnInNum'},{data : 'orderCode'},{data : 'statusLabel'},{data : 'returnInDate'},
                    { //操作
                        orderable : false,
                        data : null,
                        width: "120px",
                        render : function(data, type, row, meta) {
                            return'<shiro:hasPermission name="saleReturnIdepot_details"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>  <shiro:hasPermission name="saleReturnIdepot_toAttachment"><a href="javascript:toAttachment(\''+row.code+'\')">附件管理</a></shiro:hasPermission>';
                        }
                    }, ],
                formid : '#form1'
            };
            //每一行都进行 回调
            $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            };
            //生成列表信息
            $('#datatable-buttons').mydatatables();
        });
    
    
    laydate.render({
        elem: '#returnInDateStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
    
    laydate.render({
        elem: '#returnInDateEndDate', //指定元素
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
  
    function searchData() {
        $mytable.fn.reload();
    }
    //详情
    function details(id) {
        $module.load.pageOrder("act=50021&id=" + id);
    }
  //附件管理
    function toAttachment(code){
    	$module.load.pageOrder("act=200001&codeid=" + code);
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
    });
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
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
        
        $custom.request.ajax(serverAddr+"/saleReturnIdepot/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/saleReturnIdepot/exportSaleReturnIdepot.asyn";
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