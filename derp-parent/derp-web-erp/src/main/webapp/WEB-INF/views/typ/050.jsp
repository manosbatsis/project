<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
 
        <div class="row">
                     <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">导入库存期初</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <!--  ================================上传文件部分 start  ===============================================   -->
                    <form id="form1" >
                        <div class="form-row  col-12">
                            <div class="col-4">
                                <div class="row col-12">
                                    <label  class="col-4 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-7 mll">
                                      <div class="fileupload fileupload-new" data-provides="fileupload">
                                                    <button type="button" class="btn btn-gradient btn-file">
                                                        <span class="fileupload-new"><i class="fa fa-paper-clip"></i>选择文件</span>
                                                        <span class="fileupload-exists"><i class="fa fa-undo"></i>重新上传</span>
                                                        <input type="file" class="btn-secondary">
                                                    </button>
                                                    <span class="fileupload-preview" style="margin-left:5px;"></span>
                                                    <a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none; margin-left:5px;"></a>
                                          </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4" style="margin-top:10px;">
                    <!--         <div class="checkbox">
                              <input id="checkbox1" type="checkbox">
                                    <label for="checkbox1">
                                      同一编号信息保留原有信息
                                    <label>                                  
                            </div> -->
                            </div>
                           <div class="col-4" style="margin-top:10px;">
                              <a href="#"> 模板下载</a>
                           </div>
                        </div>
                    </form>
                    <!--  ================================上传文件部分  end===============================================   -->
                    <!--  ================================导入信息显示部分  start ===============================================   -->
                    <div class="col-12 mt10 p-20" id="border">
                       <div class="col-12 row mt10">
                           <h5 class="black">导入详情:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">新增数据<span>：</span></div>
                              <div class="col-2 ml15">100条</div>
                        </div>
                            <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-responsive" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">序号</th>
                            <th style="white-space:nowrap;" class="tc">仓库名称</th>
                            <th style="white-space:nowrap;" class="tc">商品货号</th>
                            <th style="white-space:nowrap;" class="tc">商品名称</th>
                            <th style="white-space:nowrap;" class="tc">商品条码</th>
                            <th style="white-space:nowrap;" class="tc">库存类型</th>
                            <th style="white-space:nowrap;" class="tc">库存状态</th>
                            <th style="white-space:nowrap;" class="tc">批次号</th>
                            <th style="white-space:nowrap;" class="tc">库存数量</th>
                             <th style="white-space:nowrap;" class="tc">冻结数量</th>
                            <th style="white-space:nowrap;" class="tc">锁定数量</th>
                             <th style="white-space:nowrap;" class="tc">冻结数量</th>
                            <th style="white-space:nowrap;" class="tc">可用数量</th>
                            <th style="white-space:nowrap;" class="tc">生产日期</th>
                            <th style="white-space:nowrap;" class="tc">效期至</th>
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
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">确认并保 存</button>
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
            </div>
        </div>

    



        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>



<script type="text/javascript">
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
             className : "text-center",  
             render : function(data, type, row, meta) {  
                 // 显示行号  
                 var startIndex = meta.settings._iDisplayStart;  
                 return startIndex + meta.row + 1;  
                     }  
         },
         {data:'code'},{data:'unit'},{data:'unit'},{data:'unit'},{data:'unit'},{data:'unit'},{data:'filingPrice'},{data:'factoryCode'},{data:'filingPrice'},{data:'filingPrice'},{data:'filingPrice'},{data:'filingPrice'},{data:'filingPrice'},{data:'filingPrice'}
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
	
</script>