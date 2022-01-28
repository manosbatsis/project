<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
              <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">库存档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:list();">库存期初列表</a></li>
                       <li class="breadcrumb-item "><a href="javascript:add();">新增库存期初</a></li>
                    </ol>
                    </div>
            </div>
               <!--  ================================上传文件部分 start  ===============================================   -->
                        <div class="form-row  col-12">
                            <div class="col-4">
                                <div class="row col-12">
                                    <label  class="col-4 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-7 mll">
                                      <button type="button" class="btn btn-gradient btn-file" id="btn-file">选择文件</button>
                                    	<form enctype="multipart/form-data" id="form-upload">
                                    		<input type="file" name="file" id="file" class="btn-hidden">
                                    	</form>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4" style="margin-top:10px;">
                            </div>
                           <div class="col-4" style="margin-top:10px;">
                              <a href="javascript:download();"> 模板下载</a>
                           </div>
                        </div>
                    <!--  ================================上传文件部分  end===============================================   -->
                    <!--  ================================导入信息显示部分  start ===============================================   -->
                    <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                           <h5 class="black">导入详情:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">新增数据<span>：</span></div>
                              <div class="col-2 ml15"><span id="successCount">0</span>条</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">错误数据<span>：</span></div>
                              <div class="col-2 ml15"><span id= "failureCount" class="red">0</span>条</div>
                        </div>
                   </div>
                   <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">1.按照格式进行填写数据;</div>
                        </div>
                   </div>
                    <!--  ================================导入信息显示部分  end ===============================================   -->
                           <!--  ================================表格 ===============================================   -->
                    <form id="form1" >
                    	<input type="hidden" name="state" value="1"/>
                    </form>
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>商品货号</th>
                            <th>商品名称</th>
                            <th>数量</th>
                            <th>计量单位</th>
                            <th>批次号</th>
                            <th>生产日期</th>
                            <th>有效期至</th>
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
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">确认并保存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取消</button>
                                  </div>
                              </div>
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
		//取消按钮
		$("#cancel-buttons").click(function(){
			$custom.request.ajax(serverAddr+"/initInventory/delUnconfirmedInitInventory.asyn", null, function(result){
				$module.load.pageInventory("bls=6001");
			});
		});
		//确认按钮
		$("#save-buttons").click(function(){
			$custom.request.ajax(serverAddr+"/initInventory/confirmInitInventory.asyn", null, function(result){
				$custom.alert.success("确认成功！");
				$module.load.pageInventory("bls=6001");
			});
		});
		
	});
	
	
	//点击上传文件
	$("#btn-file").click(function(){
		$("#file").click();
	});

	//上传文件到后端
	$("#file").change(function(){
		//判断是否为excel格式
		var data = $(this).val().split(".");
		var suffix = data[data.length-1];
		if(suffix=="xlsx" || suffix=="xls"){
			var formData = new FormData($("#form-upload")[0]); 
			$custom.request.ajaxUpload(serverAddr+"/initInventory/importOtherInitInventory.asyn", formData, function(result){
				if(result.state == '200'){
					$custom.alert.success("导入成功！");
					//table
					//初始化
				     $mytable.fn.init();
				     //配置表格参数
				     $mytable.params={
				         url:serverAddr+'/initInventory/listInitInventory.asyn?r='+Math.random(),
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
				         },
				         {data:'goodsNo'},{data:'goodsName'},{data:'unitName'},{data:'surplusNum'},{data:'batchNo'},{data:'productionDate'},{data:'overdueDate'},
				         ],
				         formid:'#form1'
				     };
				     //生成列表信息
				     $('#datatable-buttons').mydatatables();
				}else{
					$custom.alert.error("导入失败！");
				}
				$("#successCount").text(result.data["success"]);//新增条数
				$("#failureCount").text(result.data["failure"]);//错误条数
			});
		}else{
			$custom.alert.error("请上传正确的excel格式文件");
		}
	});
	
	function list(){
		$module.load.pageInventory("bls=6001");
	}
	
	function add(){
		$module.load.pageInventory("bls=6009");
	}
	
	//模板下载
	function download(){
		this.location.href="/excelTemplate/download.asyn?ids=005";
	}
</script>
