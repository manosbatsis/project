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
								<li class="breadcrumb-item"><a href="#">月度销售额目标</a></li>
							</ol>
						</div>
					</div>
					<ul class="nav nav-tabs">
						<shiro:hasPermission name="sale_targetList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link"  onclick="$module.load.pageReport('act=18001');">商品销售目标</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="sale_amount_targetList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link active"  onclick="$module.load.pageReport('act=18004');">月度销售额目标</a>
							</li>
						</shiro:hasPermission>
					</ul>
					<!--  title   end  -->
					<form id="form1">
						<div class="form_box">
							<div class="">
								<div class="form_item h35">
									<span class="msg_title">部门 :</span>
									<select class="input_msg" name="departmentId" id="departmentId">
										<option value="">请选择</option>
									</select>
									<input type="hidden" name="departmentIds"/>
									<span class="msg_title">母品牌 :</span>
									<select class="input_msg" name="brandSuperiorId" id="brandSuperiorId">
										<option value="">请选择</option>
									</select>
									<input type="hidden" name="brandSuperiorIds"/>
									<span class="msg_title">项目组:</span>
                                    <select name="buId" class="input_msg" id="buId">
										<option value="">请选择</option>
                                    </select>
									<input type="hidden" name="buIds"/>
                                    <span class="msg_title">计划月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month" name="month" />
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							 <shiro:hasPermission name="sale_amountTarget_export">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportBtn" onclick="javascript:exportAmountList()" value="导出"/>
							 </shiro:hasPermission>
							 <shiro:hasPermission name="sale_amountTarget_import">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importBtn" onclick="javascript:importAmountList()" value="导入"/>
							 </shiro:hasPermission>
							 <shiro:hasPermission name="sale_amountTarget_del">
								 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delBtn" onclick="javascript:delAmount()" value="删除"/>
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
									<th>部门</th>
									<th>项目组</th>
									<th>母品牌</th>
									<th>销售计划月份</th>
									<th>计划金额</th>
									<th>创建人</th>
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
				<jsp:include page="/WEB-INF/views/modals/20000.jsp" />
                <!-- end row -->
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		showDepartment();
		showBrandSuperior();
		showBusiness();
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/saleAmountTarget/listSaleAmountTarget.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            /* 每一列的数据 */
			{data:'departmentName'},{data:'buName'},{data:'parentBrandName'},{data:'month'},{data: 'totalPlanAmount'},{data:'creator'},
			{ //操作
				orderable: false,
				width:70,
				data: null,
				render: function (data, type, row, meta) {
					var relCode=row.buId+"-"+row.month+"-"+row.parentBrandId;
					var module="2";
					return '<shiro:hasPermission name="saleAmountTarget_log"><a href="javascript:logDetails(\'' + relCode+'\',\''+module+ '\')">操作日志</a></shiro:hasPermission>';
				}
			}],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	var amount = aData.totalPlanAmount == null ? 0 :aData.totalPlanAmount;
			$('td:eq(5)', nRow).html(aData.currency+"&nbsp" + amount);
			$('td:eq(6)', nRow).html(aData.creator+"<br>"+(aData.createDate==null?'':aData.createDate));

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

		// var departmentIds = $('input[name="departmentIdsStr"]').val();
		// var departmentIdArr = departmentIds.split(",");
		// $('select[name="departmentIds"]').selectpicker('val', departmentIdArr);
		//
		// var brandSuperiorIds = $('input[name="brandSuperiorIdsStr"]').val();
		// var brandSuperiorIdArr = brandSuperiorIds.split(",");
		// $('select[name="brandSuperiorIds"]').selectpicker('val', brandSuperiorIdArr);
		//
		// var buIds = $('input[name="buIdsStr"]').val();
		// var buIdArr = buIds.split(",");
		// $('select[name="buIds"]').selectpicker('val', buIdArr);
	})
	function searchData() {
		$mytable.fn.reload();
	}

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

	//导出
	function exportAmountList(){
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		var jsonData=null;
		if(ids){
			jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
			var jsonArray=$("#form1").serializeArray();
			$.each(jsonArray, function(i, field){
				if(field.value!=null&&field.value!=''&&field.value!=undefined){
					jsonData=$json.setJson(jsonData,field.name,field.value);
				}
			});
		}
		var form = JSON.parse(jsonData);

		$custom.request.ajax(serverAddr+"/saleAmountTarget/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
			if(data.state==200){
				var total = data.data.total;//总数
				if(total>10000){
					$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
					return;
				}
				//导出数据
				var url = serverAddr+"/saleAmountTarget/exportAmountList.asyn";

				var i =0;
				if(!!form){
					for(var key in form){
						if(i==0){
							url +="?";
						}else{
							url +="&";
						}
						url +=key+"="+form[key];
						i++;
					}
				}
				$downLoad.downLoadByUrl(url);
			}else{
				if(!!data.expMessage){
					$custom.alert.error(data.expMessage);

				}else{
					$custom.alert.error(data.message);

				}
			}
		});
	}

	//导入
	function importAmountList(){
		$module.load.pageReport("act=18005");
	}

	function delAmount(){
        var params = getCheckedRows() ;
        if(params.length == 0){
            $custom.alert.error("请选择一项");
            return ;
        }

        $custom.alert.warning("确定删除所选项?",function(){
            $custom.request.ajax(serverAddr+'/saleAmountTarget/delAmountTarget.asyn',{ "params" : params  },function(result){
                if(result.state==200){
                    $custom.alert.success("操作成功");
                    setTimeout('$module.load.pageReport("act=18004");', 1500) ;
                }else{
                    $custom.alert.error(result.data.message);
                }
            });
        }) ;
    }

    function getCheckedRows(){
        var params = [] ;

        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                var str = row.id;
                params.push(str);
            }
        }
        return params.join(",") ;
    }
	function showDepartment(){
		var selectObj=$('select[name="departmentId"]');
		var jsonData = null;
		var url = "/webapi/system/businessUnit/getDepartSelectBeanByUserId.asyn" ;

		$.ajax({
			url: url,
			type: "POST",
			dataType: "JSON",
			contentType:'application/x-www-form-urlencoded',
			data: {token: token },
			async: false,
			success: function (result) {
				jsonData = result.data;
			}
		});
		if(jsonData){
			loadMultiSelect(selectObj, jsonData);
		}
	}
	function showBrandSuperior(){
		var selectObj=$('select[name="brandSuperiorId"]');
		var jsonData = null;
		var url = "/webapi/system/brandSuperior/getSelectBean.asyn" ;
		$.ajax({
			url: url,
			type: "POST",
			dataType: "JSON",
			contentType:'application/x-www-form-urlencoded',
			data: {token: token },
			async: false,
			success: function (result) {
				jsonData = result.data;
			}
		});
		if(jsonData){
			loadMultiSelect(selectObj, jsonData);
		}
	}
	function showBusiness(){
		var selectObj=$('select[name="buId"]');
		var jsonData = null;
		var url = "/webapi/system/businessUnit/getSelectBeanByUserId.asyn" ;
		$.ajax({
			url: url,
			type: "POST",
			dataType: "JSON",
			contentType:'application/x-www-form-urlencoded',
			data: {token: token },
			async: false,
			success: function (result) {
				jsonData = result.data;
			}
		});
		if(jsonData){
			loadMultiSelect(selectObj, jsonData);
		}
	}
    function loadMultiSelect(selectObj , jsonData){
		selectObj.empty();

		width = "136px" ;
		$(selectObj).removeClass("input_msg") ;

		$(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;

		$(selectObj).addClass("selectpicker").addClass("show-tick") ;
		$(selectObj).attr("data-live-search", "true") ;
		$(selectObj).attr("multiple","multiple") ;

		jsonData.forEach(function(o,i){
			selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
		});

		$(selectObj).selectpicker({width: width});
		$(selectObj).selectpicker({noneSelectedText : '请选择'});
		$(selectObj).selectpicker('refresh');

		$(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
		$(".selectpicker").prev().css({"z-index":"99"});
		$(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
	}
	//选了部门，那么项目组（事业部）就要联动带出
	$('select[name="departmentId"]').change(function(){
		var departmentIds = $('select[name="departmentId"]').val();
		var buIdArr = new Array();
		if(departmentIds && departmentIds.length > 0){
			var url = "/webapi/system/businessUnit/listDTO.asyn" ;
			$.ajax({
				url: url,
				type: "POST",
				dataType: "JSON",
				contentType:'application/x-www-form-urlencoded',
				data: {token: token, departmentIds: departmentIds.join(",") },
				async: false,
				success: function (result) {
					$(result.data).each(function(index, item){
						buIdArr.push(item.id);
					})
				}
				
			});
		}
		$('select[name="buId"]').selectpicker('val', buIdArr);
		$('input[name="buIds"]').val(buIdArr);
		$('input[name="departmentIds"]').val(departmentIds);
		
	});

	$('select[name="brandSuperiorId"]').change(function(){
		var brandSuperiorIds = $('select[name="brandSuperiorId"]').val();
		$('input[name="brandSuperiorIds"]').val(brandSuperiorIds);
	});

	$('select[name="buId"]').change(function(){
		var buIds = $('select[name="buId"]').val();
		$('input[name="buIds"]').val(buIds);
	});

</script>