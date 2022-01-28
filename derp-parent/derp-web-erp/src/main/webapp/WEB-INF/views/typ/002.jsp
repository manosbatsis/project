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
                               <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">期初库存列表</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      <div class="form-row" >
                          <form id="form1" >
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-5 col-form-label "><div class="fr">仓库<span>：</span></div></label>
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
                          <div class="col-3">
                              <div class="row">
                                  <label class="col-6 col-form-label"><div class="fr">商品编号<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="barcode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3 ml15">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">商品名称<span>：</span></div></label>
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
                          <div class="form-group col-1">
                                <div class="row">
                                    <button type="button" class="btn ml15 btn-find waves-effect waves-light" onclick='$mytable.fn.reload();' >查询</button>
                                </div>
                            </div>
                            <div class="col-1">
                                <div class="row">
                                    <button type="reset" class="btn btn-light ml15 waves-effect waves-light">重置</button>
                                </div>
                            </div>
                            </form>
                      </div>
                      <div class="form-row">
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-5 col-form-label "><div class="fr">商品条码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3">
                              <div class="row">
                                  <label class="col-6 col-form-label"><div class="fr">商品类型<span>：</span></div></label>
                                  <div class="col-6 ml10">
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
                          <div class="col-3 ml15">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">数据来源<span>：</span></div></label>
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
                      <div class="form-row mt10">
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-5 col-form-label "><div class="fr">所属公司<span>：</span></div></label>
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
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <button type="button" class="btn btn-add waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            <button type="button" class="btn btn-add waves-effect waves-light btn-sm" id="add-buttons">编辑</button>
                            <button type="button" class="btn btn-add waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/merchandise/delMerchandise.asyn');">删除</button>
                            <button type="button" class="btn btn-add waves-effect waves-light btn-sm">批量导入</button>
                        </div>
                    </div>     
                      <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">序号</th>
                            <th style="white-space:nowrap;" class="tc">仓库</th>
                            <th style="white-space:nowrap;" class="tc">商品编码</th>
                            <th style="white-space:nowrap;" class="tc">商品名称</th>
                            <th style="white-space:nowrap;" class="tc">数量</th>
                            <th style="white-space:nowrap;" class="tc">计量单位</th>
                            <th style="white-space:nowrap;" class="tc">批次号</th>
                            <th style="white-space:nowrap;" class="tc">生产日期</th>
                            <th style="white-space:nowrap;" class="tc">有效期至</th>
                             <th style="white-space:nowrap;" class="tc">录入时间</th>
                            <th style="white-space:nowrap;" class="tc">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                    <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
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
             data: null,
             render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">';
             }
         },{  //序号
             data : null,  
             bSortable : false,  
             className : "text-right",  
             render : function(data, type, row, meta) {  
                 // 显示行号  
                 var startIndex = meta.settings._iDisplayStart;  
                 return startIndex + meta.row + 1;  
                     }  
         },
         {data:'code'},{data:'name'},{data:'unit'},{data:'filingPrice'},{data:'factoryCode'},{data:'barcode'},{data:'barcode'},{data:'barcode'},
         { //操作
             orderable: false,
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
