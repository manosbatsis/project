<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

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
								<li class="breadcrumb-item"><a href="#">销售管理</a></li>
								<li class="breadcrumb-item"><a href="#">商品销售目标</a></li>
							</ol>
						</div>
					</div>
					<ul class="nav nav-tabs">
						<shiro:hasPermission name="sale_targetList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link  active"  onclick="$module.load.pageReport('act=18001');">商品销售目标</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="sale_amount_targetList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link"  onclick="$module.load.pageReport('act=18004');">月度销售额目标</a>
							</li>
						</shiro:hasPermission>
					</ul>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                    </select>
                                    <span class="msg_title">计划月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month" name="month" >
                                    <span class="msg_title">归属年份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="year" name="year" >
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								<div class="form_item h35">
                                    <span class="msg_title">计划维度 :</span>
                                    <select name="type" id="type" class="input_msg">
                                    </select>
                                </div>
								<a href="javascript:" class="unfold">展开功能</a>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							 <shiro:hasPermission name="sale_target_export">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportBtn" value="导出"/>
							 </shiro:hasPermission>
							 <shiro:hasPermission name="sale_target_import">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importBtn" value="导入"/>
							 </shiro:hasPermission>
							 <shiro:hasPermission name="sale_target_del">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delBtn" value="删除"/>
							 </shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0" width="100%" >
                            <thead>
								<tr>
									<th>
										<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
											<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" id="checkAll"/> <span></span>
										</label>
									</th>
									<th>事业部</th>
									<th>销售计划月份</th>
									<th>归属年份</th>
									<th>计划维度</th>
									<th>创建人</th>
									<th>创建时间</th>
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
		</div>
	</div>
</div>
<script type="text/javascript">
	$module.constant.getConstantListByNameURL.call($('select[name="type"]')[0],"sale_target_typeList",null);
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/saleTarget/listSaleTarget.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
                {data:'buName'},{data:'month'},{data:'year'},{data:'typeLabel'},
                {data:'creator'},{data:'createDate'},
                { //操作
                	orderable: false,
                	width: "80px",
                	data: null,
                	render: function (data, type, row, meta) {
                		var html = '<a href="javascript:details(\''+row.month+'\',\''+row.buId+'\',\''+row.type+'\')">详情</a>' ;
                	    return html;
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
        
	})

	/**
     * 全选
     */
    $("input[name='keeperUserGroup-checkable']").on("change", function(){
        if($(this).is(':checked')){
            $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
            $('#datatable-buttons tbody tr').addClass('success');
        }else{
            $(":checkbox", '#datatable-buttons').prop("checked",false);
            $('#datatable-buttons tbody tr').removeClass('success');
        }
    })
	$('#datatable-buttons').on("change", ":checkbox", function() {
	    $(this).parents('tr').toggleClass('success');
	})
    
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });

	// 日期选择
	$("#month").datetimepicker({
		 format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
   	});
	
	// 日期选择
	$("#year").datetimepicker({
		 format: 'yyyy',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'decade',
	        minView:'decade',
	        maxView:'decade',
	        language:  'zh-CN',
   	});
	
	/**
	*	导入按钮
	*/
	$("#importBtn").on("click",function(){
		$module.load.pageReport("act=18002");
	});
	
	/**
	*	导出按钮
	*/
	$("#exportBtn").on("click",function(){
		var params = getCheckedRows() ;
		var url = serverAddr+"/saleTarget/exportSaleTarget.asyn?params=" + params;
		
		$downLoad.downLoadByUrl(url) ;
	});
	
	/**
	*	删除按钮
	*/
	$("#delBtn").on("click",function(){
		
		var params = getCheckedRows() ;
		if(params.length == 0){
			$custom.alert.error("请选择一项");
			return ;
		}
		
		$custom.alert.warning("确定删除所选项?",function(){
			 $custom.request.ajax(serverAddr+'/saleTarget/delSaleTarget.asyn',{ "params" : params  },function(result){
				 if(result.state==200){
	                  $custom.alert.success("操作成功");
	                  setTimeout('$module.load.pageReport("act=18001");', 1500) ;
	              }else{
	                  $custom.alert.error(result.data.message);
	              }
			});
		}) ;
	});
	
	function details(month, buId, type){
        $module.load.pageReport("act=18003&month=" + month +"&buId=" + buId + "&type=" + type);
	}
	
	function getCheckedRows(){
		var params = [] ;
		
		var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                var str = row.buId + "_" + row.month + "_" + row.type ;
                params.push(str);
            }
        }
        return params.join(",") ;
	}

</script>