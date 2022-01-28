<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

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
                                <li class="breadcrumb-item"><a href="#">账单管理</a></li>
                                <li class="breadcrumb-item"><a href="#">平台费用单</a></li>
                            </ol>
                        </div>
                    </div>
                    <ul class="nav nav-tabs">
						<shiro:hasPermission name="platformStatement_orderList">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link " onclick="$module.load.pageOrder('act=14012');">平台结算单</a>
							</li>
						</shiro:hasPermission> 
						<shiro:hasPermission name="platformCost_orderList"> 
								<li class="nav-item">
									<a href="#poList" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=14008');">平台费用单</a>
								</li>
				 		</shiro:hasPermission> 
		           	</ul>
		           	<div class="tab-content">
                    <form id="form1">
                        <div class="form_box">
                            <div>
                                <div class="form_item h35">
                                    <span class="msg_title">费用单号:</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="code" id="code">
                                    <span class="msg_title">平台单号:</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="billCode" id="billCode">
                                    <span class="msg_title">创建时间 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="createDateStr" id="createDateStr">

                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">费用项目 :</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="itemProjectName" id="itemProjectName">
                                    <span class="msg_title">费用类型 :</span>
                                    <select class="input_msg" name="costType" id="costType">
                                        <option value="">请选择</option>
                                    </select>
                                     <span class="msg_title">客户:</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">单据状态 :</span>
                                    <select class="input_msg" name="status" id="status">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                        <option value="">请选择</option>
                                    </select>

                                </div>

                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a> -->
                        </div>
                    </form>


                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable"
                                           class="group-checkable" /> <span></span>
                                </label></th>
                                <th>费用单号</th>
                                <th>客户</th>
                                <th>平台账单号</th>
                                <th>费用项目</th>
                                <th>事业部</th>
                                <th>数量</th>
                                <th>币种</th>
                                <th>金额</th>
                                <th>费用类型</th>
                                <th>创建日期</th>
                                <th>单据状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                </div>
                <!--======================Modal框===========================  -->
                <!-- end row -->

            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    //加载事业部
  	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"platformCostOrder_platformCostStatusList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="costType"]')[0],"platformCostOrder_costTypeList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
   
    $(document).ready(function() {
    	$(".date-input").datetimepicker({
   		 format: 'yyyy-mm-dd',
   	        autoclose: true,
   	        todayBtn: true,
   	        startView: 'year',
   	        minView:'month',
   	        maxView:'decade',
   	        language:  'zh-CN',
      });
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformCostOrder/listLatformCostOrder.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },           
                {data:'code'},{data:'customerName'},{data:'billCode'},{data:'itemProjectName'},
                {data:'buName'},{data:'num'},{data:'currency'},{data:'amount'},{data:'costTypeLabel'},
                {data:'createDate'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        html += '<a href="javascript:details(\''+row.id+'\')">详情</a>';
                        return html;
                    }
                },
            ],
            formid:'#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
           // $('td:eq(7)', nRow).html(""+aData.currency+"  "+aData.amount);

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        searchData() ;

        function searchData() {
            $mytable.fn.reload();
        }
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
    }) ;
    $('#datatable-buttons').on("change", ":checkbox", function() {
        $(this).parents('tr').toggleClass('success');
    }) ;



    function details(id){
        $module.revertList.serializeListForm() ;
        $module.revertList.isMainList = true;
        $module.load.pageOrder('act=14009&id='+id);
    }


</script>