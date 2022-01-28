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
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">新增采购订单</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">基本信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i> 供应商 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
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
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">*</i> 付款主体<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <select class="form-control">
                                                    <option>键太</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                 </select>
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">业务模式 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
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
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">预计付款时间<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div> 
                           <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i> 采购币别 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <select class="form-control">
                                                    <option>人民币</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                 </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">运输方式<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                 </select>
                            </div>
                        </div>
                    </div>
                </div> 
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">合同号 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                 <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">预计到港时间<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">PO号 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                 <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">销售渠道<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">交货地点 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                 <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">采购计划编号<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                               <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                 </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备注 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                              <textarea class="form-control" rows="4" name="describe"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr"><i class="red">*</i> 入仓仓库<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                               <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                 </select>
                            </div>
                        </div>
                    </div>
                </div>
                 <!--  title   基本信息  start -->
                 <!--   财务相关  start -->
                  <div id="finance" class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">财务相关</div>
                  </div>
                 <div id="finance-list" style="display:none">
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">资金成本预提 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">异常货款调整<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">预计回款金额<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">破损预提 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">其他费用<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">预计单品销价<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">汇损预提 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">毛利率<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                </div>
                 <!--   财务相关  end -->
                  <!--   款项信息  start -->
                  <div id="term" class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">款项信息</div>
                  </div>
                 <div id="term-list" style="display:none">
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">卓普信付供应商 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">货款到日期<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">资金占用时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">卓志付保证金 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                               <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">实际回款时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">资金线上超时<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                              <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                 </div>
                 <!--   款项信息  end -->
                   <!--   商品信息  start -->
                <div class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">商品信息</div>
                </div>
                <div class="row col-12 mt20">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>商品条码</th>
                            <th>商品名称</th>
                            <th>商品货号</th>
                            <th>计量单位</th>
                            <th>采购数量</th>
                            <th>采购单价</th>
                            <th>采购总金额</th>
                            <th>预计汇率</th>
                            <th>预计折算人民币</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
               <div class="form-row mt20 ml15">
                   <div class="col-4">
                        <div class="row">
                             <button type="button" class="btn btn-add waves-effect waves-light fr" onclick='$("#modify-model").modal("show")'>新增</button>
                        </div>
                    </div>
                </div>
                 <!--   商品信息  end -->
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
                           <!-- 新增弹窗部分   start -->
         <div class="row">
         <div class="col-12">
             <div>
            <!-- Signup modal content -->
            <div id="modify-model"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;position:fixed;top:130px;bottom:auto;left:-350px;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width:1000px;">
                        <div class="modal-body">
                          <div class="ml10 page-title col-12 borderb mt20">
                           <div class="ml10">商品信息</div>
                           </div>
                     <div class="row col-12 mt20">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>商品条码</th>
                            <th>商品名称</th>
                            <th>商品货号</th>
                            <th>计量单位</th>
                            <th>采购数量</th>
                            <th>采购单价</th>
                            <th>采购总金额</th>
                            <th>预计汇率</th>
                            <th>预计折算人民币</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                        </table>
                   </div>
                   <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-button">保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-button">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
              </div><!-- /.modal -->
           </div>
          </div><!-- end col -->
      </div>
                 
                 
                 <!-- 新增弹窗部分 end-->
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
	 
	/* 
	    function resetPwd(){
	        var url='/user/modifyPwd.asyn?r='+Math.random();
	        var dataJson=$('#resetPwdForm').serializeArray();
	        $custom.request.ajax(url,dataJson,function(data){
	            if(data.state==200){
	                $custom.alert.success(data.message);
	                $("#modify-model").modal("hide");
	            }else{
	                $custom.alert.error(data.message);
	            }

	        });
	    } */

	 
	 
	 
	 
	 
	 
	 
	 
</script>
