<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css" />

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
             <div class="row">
              <div class="card-box margin table-responsive" style="overflow-x:hidden;">
                 <div class="row">
              <!--  title   start  -->
              <div class="col-12">
                 <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">新增销售订单</a></li>
                    </ol>
                 </div>
                    <!--  title   end  -->
                  <div class="margin">
                      <div class="form-row mt20">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i> 客户名称<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label" ><div class="fr"> <i class="red">*</i> 出仓仓库<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr"><i class="red">*</i> 交货日期<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr"><i class="red">*</i> 销售计划编号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">合同号<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">销售类别<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                     <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">销售员<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                     <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                      </div>                    
                      <div class="form-row mt20">
                          <div class="col-12 ml30">
                              <div class="row">
                                  <label  class="col-1 col-form-label " style="white-space:nowrap;"><div class="fr">备注<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <textarea class="form-control" rows="5" name="describe"></textarea>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="ml10 page-title col-12 borderb mt20">
                          <div class="ml10">商品信息</div>
                      </div>
                       <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th class="tc">序号</th>
                            <th class="tc">操作</th>
                            <th class="tc">子合同编号</th>
                            <th class="tc">商品编号</th>
                            <th class="tc">商品名称</th>
                            <th class="tc">商品条形码</th>
                            <th class="tc">计量单位</th>
                            <th class="tc">存货数量</th>
                            <th class="tc">销售数量</th>
                            <th class="tc">销售单价</th>
                            <th class="tc">销售总金额</th>
                             <th class="tc">批次号</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                      <div class="ml10 page-title col-12 borderb mt20">
                          <div class="ml10">付款信息</div>
                      </div>
                         <div class="form-row mt20">
                            <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">收款后金额<span>：</span></div></label>
                                  <div class="col-4 ml10">
                                      <div>
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                      </div>
                                  </div>
                                   <div class="col-1 mt20 ml10">
                                      <label>%</label>
                                      </div>
                              </div>
                          </div>
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " ><div class="fr">收款优惠<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                     <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                       <div class="form-row mt20">
                             <div class="col-5">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">本次支付（定金）<span>：</span></div></label>
                                  <div class="col-4 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-4">
                              <div class="row">
                                  <label class="col-4 col-form-label"><div class="fr">结算账户<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                     </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
                  <!-- end row -->
          </form>
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
	         columns:[{  //序号
	             data : null,  
	             bSortable : false,  
	             className : "text-right",  
	             width : "30px",  
	             render : function(data, type, row, meta) {  
	                 // 显示行号  
	                 var startIndex = meta.settings._iDisplayStart;  
	                 return startIndex + meta.row + 1;  
	                     }  
	         }, { //操作
	             orderable: false,
	             width: "130px",
	             data: null,
	             render: function (data, type, row, meta) {
	             	var edit = "";
	             	if(row.source !='1'){
	             		edit = '<a href="javascript:edit('+row.id+')">编辑</a>'
	             	}
	                 return edit + '<a style="margin-right:15px; font-size:20px;">+</a> <a  style="margin-right:15px;font-size:20px;">-</a>  <a href="javascript:details('+row.id+')">修改</a>';
	             }
	         },
	         {data:'code'},{data:'name'},{data:'unit'},{data:'filingPrice'},{data:'factoryCode'},{data:'barcode'},{data:'factoryCode'},{data:'factoryCode'},{data:'factoryCode'},{data:'factoryCode'},
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
