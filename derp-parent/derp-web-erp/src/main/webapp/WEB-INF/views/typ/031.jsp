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
           <div class="col-12 ml10">
           <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">销售出库详情</a></li>
                    </ol>
                 </div>
              <div class="card-box table-responsive" >
                 <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">单据状态：<span class="text-success">已入仓</span></div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">出库清单编号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">销售订单编号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>  
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">公司 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">海关放行时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div> 
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">出库仓库 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">交运时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>   
                     <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">物流企业名称 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">进口类型<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">运单号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">总运单号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">单据类型 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ml10 page-title col-12 borderb mt20">
	                    <div class="ml10">出库商品明细</div>
	           </div>
	           <div class="row col-12 mt20">
	           		<div class="card-box">
	           			<ul class="nav nav-tabs">
	           				<li class="nav-item">
	           					<a href="#itemList" data-toggle="tab" class="nav-link active">商品信息</a>
	           				</li>
	           				<li class="nav-item">
	           					<a href="#batchDefault" data-toggle="tab" class="nav-link">核销信息</a>
	           				</li>
	           			</ul>
	           			<div class="tab-content">
	           				<div class="tab-pane fade show active" id="itemList">
	           					<table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th style="white-space:nowrap;" class="tc">序号</th>
			                            <th style="white-space:nowrap;" class="tc">商品货号</th>
			                            <th style="white-space:nowrap;" class="tc">商品编号</th>
			                            <th style="white-space:nowrap;" class="tc">商品名称</th>
			                            <th style="white-space:nowrap;" class="tc">商品条形码</th>
			                            <th style="white-space:nowrap;" class="tc">计量单位</th>
			                            <th style="white-space:nowrap;" class="tc">销售数量</th>
			                            <th style="white-space:nowrap;" class="tc">销售单价</th>
			                            <th style="white-space:nowrap;" class="tc">销售总金额</th>
			                            <th style="white-space:nowrap;" class="tc">批次</th>
			                            <th style="white-space:nowrap;" class="tc">生产日期</th>
			                            <th style="white-space:nowrap;" class="tc">失效日期</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        </tbody>
			                    </table>
                            </div>
                            <div class="tab-pane fade" id="batchDefault">
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
		
		//财务相关收起，展开
		$("#finance").click(function(){
			$("#finance-list").toggle();
		});
		
		//款项信息收起，展开
		$("#term").click(function(){
			$("#term-list").toggle();
		});
		
		//保存按钮
		$("#save-button").click(function(){
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
		
		//弹框取消按钮
		$("#cancel-button").click(function(){
			console.log(32753751267);
			/* $load.a("/merchandise/toPage.html"); */
			/* $("#modify-model").hide(); */
			$(".fade").removeClass("show");
		});
		
	});
	
	//table
 $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/customer/listCustomer.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* {  //序号
                data : null,  
                bSortable : false,  
                className : "text-right",  
                width : "30px",  
                render : function(data, type, row, meta) {  
                    // 显示行号  
                    var startIndex = meta.settings._iDisplayStart;  
                    return startIndex + meta.row + 1;  
                }  
            }, */
            {data:'code'},{data:'name'},{data:'orgCode'},{data:'cusType'},{data:'legalTel'},{data:'orgCode'},{data:'cusType'},{data:'legalTel'},{data:'legalTel'},
           /*  { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<a href="javascript:edit('+row.id+')">编辑</a>';
                }
            }, */
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if (aData.cusType == '1'){
                $('td:eq(4)', nRow).html('客户');
            }else if (aData.cusType == '2'){
                $('td:eq(4)', nRow).html('供应商');
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //新增
        $("#add-buttons").click(function(){
        	$load.a("/customer/toAddPage.html");
        });
        
        //批量导入
        $("#import-buttons").click(function(){
        	$load.a("/customer/toImportPage.html");	
        });
        
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
    });

    function searchData(){
        $mytable.fn.reload();
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
