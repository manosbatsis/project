<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />

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
                                <li class="breadcrumb-item"><a href="#">标准成本单价管理</a></li>
                                <li class="breadcrumb-item"><a href="#">标准成本单价列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <ul class="nav nav-tabs">
                        <li class="nav-item">
                            <a href="#" data-toggle="tab" class="nav-link " onclick="$module.load.pageReport('act=13001');">单价管理</a>
                        </li>
                        <li class="nav-item">
                            <a href="#poList" data-toggle="tab" class="nav-link active" onclick="$module.load.pageReport('act=13006');">待审核单价</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                     <span class="msg_title">条形码 :</span>
                                    	<input type="text" required="" parsley-type="text" class="input_msg" id="barcode"name="barcode">
                                    <span class="msg_title">生效月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="effectiveDate" name="effectiveDate" >
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" id="buId" class="input_msg">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="settlementPrice_examine">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="examine">审核</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>所属公司</th>
                            <th>事业部</th>
                            <th>商品名称</th>
                            <th>条形码</th>
                            <th>生效年月</th>
                            <th>结算币种</th>
                            <th>调整前单价</th>
                            <th>调整后单价</th>
                            <th>调价原因</th>
                            <th>提交人</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    </div>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
    </div> <!-- content -->
    <jsp:include page="/WEB-INF/views/modals/13006.jsp" />
<script type="text/javascript">
	
	//事业部
	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
	
    //日期选择
	$(".date-input").datetimepicker({
	     format: 'yyyy-mm',
	        autoclose: true,
	        todayBtn: true,
	        startView: 'year',
	        minView:'year',
	        maxView:'decade',
	        language:  'zh-CN',
	});
    
	$(document).ready(function () {
        //重置按钮
        $("button[type='reset']").click(function () {
            $(".goods_select2").each(function () {
                $(this).val("");
            });
            //重新加载select2
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            });
        });
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr+'/settlementPrice/listExamineList.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data: 'merchantName'}, {data: 'buName'},{data: 'goodsName'}, {data: 'barcode'}, {data: 'effectiveDate'}, {data: 'currencyLabel'}, {data: 'historyPrice'},
            {data: 'price'}, {data: 'adjustPriceResult'},{data: 'modifier'}
            
            
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 20){
                $('td:eq(3)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 20) + "...</span>");               
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    });

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
    
  //提交审核
  $("#examine").on("click" , function(){
	  var ids=$mytable.fn.getCheckedRow();
	  
	  if(ids==null||ids==''||ids==undefined){
          $custom.alert.warningText("至少选择一条记录！");
          return ;
      }
	  
	  $m13006.show() ;
  });

	
</script>