<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                      <div class="form-row mt20">
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-5 col-form-label "><div class="fr">商品编号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsCode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-4 col-form-label"  style="white-space:nowrap;"><div class="fr">商品名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="form-group col-2">
                                <div class="row">
                                    <button type="button" class="btn btn-find waves-effect waves-light ml30" onclick='$mytable.fn.reload();' >查询</button>
                                </div>
                            </div>
                            <div class="col-1">
                                <div class="row">
                                    <button type="reset" class="btn btn-light waves-effect waves-light">重置</button>
                                </div>
                            </div>
                      </div>
                       <div class="form-row mt20">
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-5 col-form-label "><div class="fr">商品品牌<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsCode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-4 col-form-label"  style="white-space:nowrap;"><div class="fr">商品条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                      </div>                
                           <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">商品编码</th>
                            <th style="white-space:nowrap;" class="tc">商品名称</th>
                            <th style="white-space:nowrap;" class="tc">品牌</th>
                            <th style="white-space:nowrap;" class="tc">计量单位</th>
                            <th style="white-space:nowrap;" class="tc">商品条码</th>
                            <th style="white-space:nowrap;" class="tc">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">确定</button>
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
