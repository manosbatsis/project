<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
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
								<li class="breadcrumb-item"><a href="#">商品管理</a></li>
								<li class="breadcrumb-item"><a href="#">标准条码管理</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<ul class="nav nav-tabs">
						<shiro:hasPermission name="commbarcode_list">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active" onclick="$load.a('/list/menu.asyn?act=2001');">标准条码管理</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="groupCommbarcode_list">
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link" onclick="$load.a('/list/menu.asyn?act=2002');">组码管理</a>
							</li>
						</shiro:hasPermission>
	           		</ul>
	           		<div class="tab-content">
					<form id="form1">
						<div class="form_box">
							<div>
								<div class="form_item h35">
                                    <span class="msg_title">标准条码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="commbarcode" name="commbarcode" >
                                    <span class="msg_title">标准品牌 :</span>
                                    <select id="commBrandParentId" name="commBrandParentId" class="selectpicker show-tick form-control" data-live-search="true">
                                    </select>
                                    <span class="msg_title">条形码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="barcode" name="barcode" >
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick="formReset()">重置</button>
                                    </div>
								</div>
								<div class="form_item h35">
								    <span class="msg_title">商品名称 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="goodsName" name="goodsName" >
									<span class="msg_title">商品货号 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="goodsNo" name="goodsNo" >
                                    <span class="msg_title">维护状态 :</span>
                                    <select name="maintainStatus" class="input_msg">
                                    </select>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							<shiro:hasPermission name="commbarcode_import">
	                        	<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importCommbarcode" value="导入"/>
							</shiro:hasPermission>
							<shiro:hasPermission name="commbarcode_export">
	                        	<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportCommbarcode" value="导出"/>
							</shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>
										<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" id="checkAll"/> <span></span>
										</label>
									</th>
									<th>标准条码</th>
									<th>标准品牌</th>
									<th>二级分类</th>
									<th>商品名称</th>
									<th>维护状态</th>									
									<th>母品牌</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    
                    </div>
                    <!--  ================================表格  end ===============================================   -->
				</div>
				<!--======================Modal框===========================  -->
                <jsp:include page="/WEB-INF/views/modals/11001.jsp" />
                <!-- end row -->
                
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="maintainStatus"]')[0],"commbarcode_maintainStatusList",null);
	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/commbarcode/listCommbarcodes.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
                {data:'commbarcode'},{data:'commBrandParentName'},{data:'commTypeName'},
                {data:'commGoodsName'},{data:'maintainStatusLabel'},{data:'brandSuperiorName'},{data:'createDate'},
                { //操作
                	orderable: false,
                	'sWidth': "80px",
                	data: null,
                	render: function (data, type, row, meta) {
                	    return '<shiro:hasPermission name="commbarcode_edit"><a href="javascript:$m11001.init(\''+row.id+'\')">编辑</a></shiro:hasPermission> <shiro:hasPermission name="commbarcode_detail"><a href="javascript:details(\''+row.id+'\')">详情</a></shiro:hasPermission>';
                	}
                }
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
        function searchData() {
        	
			$mytable.fn.reload();
		}
        
        getBrandParentSelect() ;
        
	})

	//全选
	$("#checkAll").on("click",function(){
		var flag = $("#checkAll").is(":checked");
		var checks = $("tbody tr td input[class='iCheck']");
		checks.each(function(){
			$(this).attr("checked", flag);
		});
	});
	
	$("#importCommbarcode").on("click" , function(){
		$load.a('/list/menu.asyn?act=20011');
	});

	//导出
     $("#exportCommbarcode").on("click",function(){
    	//根据筛选条件导出
    	var ids=$mytable.fn.getCheckedRow();
        $custom.request.ajax('/commbarcode/getExportCount.asyn', {ids : ids}, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>5000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		if(total==0){
        			swal({title: '导出数据为空！',type: 'warning',confirmButtonColor: '#4fa7f3'});
        			return;
        		}
        		//导出数据
            	var url = "/commbarcode/exportCommbarcode.asyn?ids="+ ids;
            	$downLoad.downLoadByUrl(url)
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error("未知异常");
            		console.log(data.expMessage);
            	}else{
            		$custom.alert.error("未知异常");
            		console.log(data.message);

            	}
            }
        });
    });
	
	function details(id){
		$module.revertList.serializeListForm() ;
		$load.a('/list/menu.asyn?act=20012&id='+id);
	}
	
	//点击展开功能
	$(".unfold").click(function () {
	    $(".form_con").toggleClass('hauto');
	    if($(this).text() == '展开功能'){
	        $(this).text('收起功能');
	    }else{
	        $(this).text('展开功能');
	    }
	});
	
	/**
	    *   下拉搜索
	    */
	    $("#commBrandParentId").prev().find(".bs-searchbox").find("input").keyup(
	        function(){
	            var brandParent = $("#form1 .open input").val() ;
	            getBrandParentSelect(brandParent) ;
	        }
	    );
	    
	    /**
	    * 获取标准品牌下拉
	    */
	    function getBrandParentSelect(brandParent){
	        var url = "/commbarcode/getBrandParent.asyn" ;
	        $custom.request.ajax(url,{"brandParent":brandParent},function(data) {
	            if (data.state == 200) {
	                
	                var html = "" ;
	                
	                var brandParentList = data.data ;
	                
	                if(!brandParent){
	                	html += '<option value="">请选择</option>' ;
	                }
	                
	                $(brandParentList).each(function(index , brandParent){
	                    html += '<option value="'+brandParent.id+'">'+brandParent.name+'</option>' ;
	                
	                }) ;
	                
	                $("#commBrandParentId").html(html) ;
	                $('#commBrandParentId').selectpicker({width: '136px'});
	                $('#commBrandParentId').selectpicker('refresh');
	                
	                $("#commBrandParentId").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
	                $("#commBrandParentId").prev().css({"z-index":"99"}) ;
	                $("#commBrandParentId").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
	                
	            } else {
	                $custom.alert.error(data.message);
	            }
	        });
	    }
	    
	    function formReset(){
	    	getBrandParentSelect() ;
	    	
	    	$("#form1")[0].reset() ;
	    }

</script>