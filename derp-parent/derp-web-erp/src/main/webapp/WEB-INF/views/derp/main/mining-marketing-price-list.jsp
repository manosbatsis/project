<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/mining/toPage.html');">采销报价管理</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      
                     <form id="form1" >
                         <div class="form_box">
                             <div class="form_con">
                                 <div class="form_item h35">
                                     <span class="msg_title">供给客户 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="customerId">
                                     <span class="msg_title">商品货号 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="goodsNo">
                                     <span class="msg_title">条形码 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="barcode">
                                     <div class="fr">
                                         <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                         <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                     </div>
                                 </div>
                                 <div class="form_item h35">
                                     <span class="msg_title">商品名称 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="goodsName">
                                     <span class="msg_title">商品品牌 :</span>
                                     <input type="text" required="" parsley-type="text" class="input_msg" name="brandName">
                                 </div>
                             </div>
                             <a href="javascript:" class="unfold">展开功能</a>
                         </div>
                     </form>
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="mining_edit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="edit-buttons">编辑</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="mining_delDepot">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/mining/delDepot.asyn');">删除</button>
                            </shiro:hasPermission>


                        </div>
                    </div>     
                      <!--  ================================表格 ===============================================   -->
                    <div class="row col-md-12 mt20">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                        	<th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>供给客户</th>
                            <th>商品货号</th>
                            <th>商品名称</th>
                            <th>条形码</th>
                            <th>品牌</th>
                            <th>规格型号</th>
                            <th>供货报价币种</th>
                            <th>供货价</th>
                            <th>报价生效时间</th>
                            <th>报价失效时间</th>
                            <th>修改时间</th>
                            <th>备注</th>
                            <th>操作</th>
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
        <!-- container -->
    </div>
    </div>

</div> <!-- content -->
<script type="text/javascript">
$(document).ready(function() {
	//重置按钮
    $("button[type='reset']").click(function(){
    	$(".goods_select2").each(function(){
    		$(this).val("");
    	});
    	//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
    });
	
     //初始化
     $mytable.fn.init();
     //配置表格参数
     $mytable.params={
         url:'/mining/list.asyn?r='+Math.random(),
         columns:[{ //复选框单元格
             className: "td-checkbox",
             orderable: false,
             width: "30px",
             data: null,
             render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">';
             }
         },
         {data:'customerName'},
         {data:'goodsNo'},
         {data:'goodsName'},
         {data:'barcode'},
       	 {data:'brandName'},
       	 {data:'goodsSpec'},
       	 {data:'currency'},
         {data:'price'},
       	 {data:'effectDate'},
         {data:'invalidDate'},
         {data:'modifyDate'},
         {data:'remark'},
         { //操作
             orderable: false,
             width: "130px",
             data: null,
             render: function (data, type, row, meta) {
                 return '详情';
             }
         },
         ],
         formid:'#form1'
     };
     //生成列表信息
     $('#datatable-buttons').mydatatables();
});

	 function searchData(){
	     $mytable.fn.reload();
	 }
	
	 //新增
	 $("#add-buttons").click(function(){
	 	$load.a("/depot/toAddPage.html");
	 });
	 
	 //导入
	 $("#import-buttons").click(function(){
		$load.a("/depot/toImportPage.html");
	 });
	
	 //详情
	 function details(id){
	 	$load.a("/depot/toDetailsPage.html?id="+id);
	 }
 
	 //编辑
	 function edit(id){
	 	$load.a("/depot/toEditPage.html?id="+id);
	 }

	 /**
	  * 全选
	  */
	 $('#datatable-buttons').on("change", ":checkbox", function() {
	     // 列表复选框
	     if ($(this).is("[name='keeperUserGroup-checkable']")) {
	         // 全选
	         $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
             if($(this).is(':checked')){
                 $('#datatable-buttons tbody tr').addClass('success');
             }else{
                 $('#datatable-buttons tbody tr').removeClass('success');
             }
         }else{
	         // 一般复选
	         var checkbox = $("tbody :checkbox", '#datatable-buttons');
	         $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
             $(this).parents('tr').toggleClass('success');
         }
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
