<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
								<li class="breadcrumb-item"><a href="#">销售出库差异分析表</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
						<%--		<div class="form-row h35">
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label "><div class="fr">
												销售订单编号<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<input class="form-control" name="orderCode" />
											</div>
										</div>
									</div>
									<div class="col-3">
										<div class="row">
											<label class="col-6 col-form-label"><div class="fr">
												出库单号<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<input class="form-control" name="saleOutDepotCode" />
											</div>
										</div>
									</div>
									<div class="col-4">
										<div class="row">
											<label class="col-5 col-form-label"><div class="fr">
												订单完结时间<span>：</span>
											</div></label>
											<div class="col-7 ml10">
												<input type="text" class="form-control form_datetime date-input" name="endDateStr">
											</div>
										</div>
									</div>
									<div class="form-group col-2">
										<div class="row">
											<button type="button"
													class="btn ml15 btn-find waves-effect waves-light btn_default"
													onclick='$mytable.fn.reload();'>查询</button>
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
											<label class="col-6 col-form-label "><div class="fr">
												是否完结<span>：</span>
											</div></label>
											<div class="col-6 ml10">
												<select class="form-control" name="state">
													<option value="1">已完结</option>
													<option value="0">未完结</option>
												</select>
											</div>
										</div>
									</div>
								</div>--%>
								<div class="form_item h35">
									<span class="msg_title">销售订单编号 :</span>
									<input class="input_msg" name="orderCode" />
									<span class="msg_title">出库单号 :</span>
									<input class="input_msg" name="saleOutDepotCode" />
									<span class="msg_title">订单完结时间 :</span>
									<input type="text" class="input_msg form_datetime date-input" name="endDateStr">
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
								<div class="form_item h35">
									<span class="msg_title">是否完结 :</span>
									<select class="input_msg" name="isEnd">
									</select>
									<!-- <span class="msg_title">品牌:</span>
									<select class="input_msg" name="brandId" id="brandId">
                                     	<option value="">请选择</option>
                                    </select> -->
                                   <!--  根据品牌查询换成了根据货号查询 -->
                                    <span class="msg_title">货号:</span>
                                    <input class="input_msg" name="goodsNo" />                                   
                                    <span class="msg_title">销售类型 :</span>
									<select class="input_msg" name="saleType">
									</select>
								</div>
									<div class="form_item h35">
									   <span class="msg_title">事业部 :</span>
                                       <select class="input_msg" name="buId" ></select>
									</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-12 mt20">
						<div class="button-list" style="display: none">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm">导出</button>
						</div>
					</div>
					<!--  ================================表格 ===============================================   -->
					<div class="mt10 table-responsive">
						<table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
							<thead>
							<tr>
								<th>
									<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
										<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
										<span></span>
									</label>
								</th>
								<th>销售订单编号</th>
								<th>事业部</th>
								<th>商品货号</th>
								<th style="min-width: 220px;">商品名称</th>
								<th>出库清单编号</th>
								<th>销售数量</th>
								<th>出库数量</th>
								<th>结余</th>
								<th>完结出库时间</th>
								<th>海外仓理货单位</th>
								<!-- 									<th style="white-space: nowrap;" class="tc">操作</th> -->
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
/* $module.brand.loadSelectData.call($("select[name='brandId']")[0]); */
$module.constant.getConstantListByNameURL.call($('select[name="isEnd"]')[0],"saleAnalysis_isEndList",null);
$module.constant.getConstantListByNameURL.call($('select[name="saleType"]')[0],"saleAnalysis_saleTypeList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null, null,null);
    $(document).ready(
        function() {
            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url : serverAddr+'/saleAnalysis/listSaleAnalysis.asyn?r=' + Math.random(),
                columns : [
                    { //复选框单元格
                        className : "td-checkbox",
                        orderable : false,
                        data : null,
                        render : function(data, type, row, meta) {
                            return '<input type="checkbox" class="iCheck">';
                        }
                    },
                    {data : 'orderCode'},{data : 'buName'},{data : 'goodsNo'},{data : 'goodsName'},{data : 'saleOutDepotCode'},{data : 'saleNum'},{data : 'outDepotNum'},{data : 'surplus'},{data : 'endDate'},{data : 'tallyingUnitLabel'},
// 						{ //操作
// 							orderable : false,
// 							data : null,
// 							render : function(data, type, row, meta) {
// 								return '<a href="javascript:details(' + row.id + ')">详情</a>';
// 							}
// 						},
                ],
                formid : '#form1'
            };
            $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
                if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 30){
                    $('td:eq(4)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 30) + "...</span>");
                }
            }
                //生成列表信息
            $('#datatable-buttons').mydatatables();
        });

    function searchData() {
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
	$(function () {
		$("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
</script>