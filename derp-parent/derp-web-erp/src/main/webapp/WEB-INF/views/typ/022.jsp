<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
        <div class="row">
            <div class="card-box margin table-responsive" id="border">
                 <!--  title   start  -->
              <div class="col-12 ml10">
                 <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">预申报单详情</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                    <!--  title   采购订单明细  start -->
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">采购订单明细</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">预申报单号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>DECLARATION201805170001</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">订单状态<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>已完结</div>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>供应商 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">头程提单号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">装货港<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">业务模式 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">发票号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">卸货港<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">合同号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">销售渠道<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">创建人<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">PO  号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">运输方式<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">创建时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>入仓仓库 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">预计到港时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">审核人<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">收货地点 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">箱      数<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">审核时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                 <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备   注 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>  
                 <!--  title   采购订单明细  end -->
                 <!-- 商品信息 start -->
                <div class="form-row mt20 ml15">
                	<div class="col-12">
                		<div class="card-box">
                			<h4 class="header-title m-t-0">商品信息</h4>
                			<div class="table-responsive">
                				<table class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                        	<th><input type="checkbox" /></th>
			                        	<th>序号</th>
			                        	<th>采购订单号</th>
			                        	<th>合同号</th>
			                        	<th>商品条码</th>
			                        	<th>商品名称</th>
			                        	<th>商品货号</th>
			                        	<th>采购数量</th>
			                        	<th>采购单价</th>
			                        	<th><div>采购总金额</div><div>（RMB）</div></th>
			                            <th>品牌类型</th>
			                            <th>毛重</th>
			                            <th>净重</th>
			                            <th>箱号</th>
			                            <th>生产批次号</th>
			                            <th>成分占比</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        </tbody>
			                    </table>
                			</div>
                		</div>
                    </div>
                </div>
                 <!-- 商品信息 end -->
            <!-- end row -->
            </div>
            <!-- container -->
        </div>

    </div> <!-- content -->
<script type="text/javascript">
	$(function(){
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/merchandise/saveMerchandise.asyn";
			var form = $("#add-form").serializeArray();
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("添加成功！");
					setTimeout(function () {
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error("添加失败！");
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/merchandise/toPage.html");
		});
		
	});
	
	//table
	$(document).ready(function() {
	    // $custom.alert.input();
	     //初始化
	     $mytable.fn.init();
	     //配置表格参数
	     $mytable.params={
	         url:'/merchandise/listMerchandise.asyn?r='+Math.random(),
	         columns:[{ //复选框单元格
	             className: "td-checkbox",
	             orderable: false,
	             width: "30px",
	             data: null,
	             render: function (data, type, row, meta) {
	                 return '<input type="checkbox" class="iCheck">';
	             }
	         },{  //序号
	             data : null,  
	             bSortable : false,  
	             className : "text-right",  
	             width : "30px",  
	             render : function(data, type, row, meta) {  
	                 // 显示行号  
	                 var startIndex = meta.settings._iDisplayStart;  
	                 return startIndex + meta.row + 1;  
	                     }  
	         },
	         {data:'code'},{data:'name'},{data:'unit'},{data:'filingPrice'},{data:'factoryCode'},{data:'barcode'},
	         { //操作
	             orderable: false,
	             width: "130px",
	             data: null,
	             render: function (data, type, row, meta) {
	             	var edit = "";
	             	if(row.source !='1'){
	             		edit = '<a href="javascript:edit('+row.id+')">编辑</a>'
	             	}
	                 return edit + '  <a href="javascript:details('+row.id+')">详情</a>';
	             }
	         },
	         ],
	         formid:'#form1'
	     };
	     //生成列表信息
	     $('#datatable-buttons').mydatatables();

	 } );

	 function searchData(){
	     $mytable.fn.reload();
	 }

	 //新增
	 $("#add-buttons").click(function(){
	 	$load.a("/merchandise/toAddPage.html");
	 });

	 //详情
	 function details(id){
	 	$load.a("/merchandise/toDetailsPage.html?id="+id);
	 }
	 
	 //编辑
	 function edit(id){
	 	$load.a("/merchandise/toEditPage.html?id="+id);
	 }

	 /**
	  * 全选
	  */
	 $('#datatable-buttons').on("change", ":checkbox", function() {
	     // 列表复选框
	     if ($(this).is("[name='keeperUserGroup-checkable']")) {
	         // 全选
	         $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
	     }else{
	         // 一般复选
	         var checkbox = $("tbody :checkbox", '#datatable-buttons');
	         $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
	     }
	 });
</script>
