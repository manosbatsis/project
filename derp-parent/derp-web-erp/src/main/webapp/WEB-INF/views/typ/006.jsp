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
            <div class="margin w100">
                <button type="submit" class="btn btn-info waves-effect waves-light fr">保 存</button>
            </div>
            <div class="card-box margin table-responsive" id="border">
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">基础子资料</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">商品编码 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">商品条形码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">保质天数<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>1095天</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label"><span class="fr">规格<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>6片/盒</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">商品货号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>536987297692</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">计量单位<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>千克</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">原产国<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>美国</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label"><span class="fr">品牌<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>一叶子</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">商品名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>我的美丽日记 弹力导入双膜组6片</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">商品分类<span>：</span></span></label>
                            <div class="col-7 ml10 line">
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
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label"><span class="fr">毛重<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>0.35</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-4 col-form-label"><span class="fr">净重<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>0.23</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " ><span class="fr">唯一标识(OPG)<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085550</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">工厂编码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control" id="inputEmail3">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end row -->
            <div class="card-box margin table-responsive" id="border">
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">商品申报信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">货主名称 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>健太</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备案价格<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>￥12.45</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row ml15">
                    <div class="col-3">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">HS code<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>430988989</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
