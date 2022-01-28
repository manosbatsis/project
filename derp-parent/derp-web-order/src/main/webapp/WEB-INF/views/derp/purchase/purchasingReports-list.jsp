<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
#datatable-buttons tbody tr td{
      text-align: center;
}

</style>
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
                                <li class="breadcrumb-item"><a href="#">补货动销报表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                   <span class="msg_title">报表日期 :</span>
				                   <input type="text" class="input_msg layui-input" id="reportDate" name="reportDate">				                    
                                    <span class="msg_title">仓库 :</span>
                                    <select name="warehouse" class="input_msg" id="warehouse">
                                        <option value="">请选择</option>
                                        <option value="上海外高桥">上海外高桥仓库</option>
                                        <option value="天津保税">天津保税仓库</option>
                                        <option value="郑州保税">郑州保税仓库</option>
                                        <option value="南沙保税送中心">南沙保税送中心</option>
                                    </select>
                                    <span class="msg_title">商品编码 :</span>
                                    <input type="text" class="input_msg" name="wareId">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">平台 :</span>
                                    <select class="input_msg" name="platform" id="platform">
                                     	<option value="">请选择</option>
                                        <option value="京东">京东</option>
                                        <option value="唯品">唯品</option>
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="purchasingReports_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
                            </shiro:hasPermission>
                            <%-- <shiro:hasPermission name="purchasingReports_refresh">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="refreshReport">刷新报表</button>
                            </shiro:hasPermission> --%>
                           
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
	                            <tr>
	                            	<th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
	                                <th>公司</th>
	                                <th>平台</th>
	                                <th style="text-align: center;min-width: 100px">仓库</th>
	                                <th style="text-align: center;min-width: 80px">报表日期</th>
	                                <th style="text-align: center;min-width: 100px">商品编码</th>
	                                <th style="text-align: center;min-width: 100px">条形码</th>
	                                <th style="text-align: center;min-width: 400px">商品名称</th>
	                                <th>在途数量</th>
	                                <th>库存</th>
	                                <th style="text-align: center;">90天日均销量</th>
	                                <th style="text-align: center;">预计库存清空天数</th>
	                                <th style="text-align: center;">建议采购数量</th>
	                                <th style="text-align: center;">补货后预计库存<br>清空天数</th>
	                                <th style="text-align: center;">是否触发补货<br>预警</th>
	                                <th>箱规</th>
	                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/purchasingReports/listPurchasingReports.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
		            className: "td-checkbox",
		            orderable: false,
		            width: "30px",
		            data: null,
		            render: function (data, type, row, meta) {
		                 return '<input type="checkbox" class="iCheck">';
		            }
            	},
            	{data:'merchantName'},{data:'platform'},{data:'warehouse'},{data:'runDate'},{data:'wareId'},{data:'upc'},{data:'name'},
            	{data:'transitStock'},{data:'stock'},{data:'avg90dayNum'},{data:'stockSellDays'},{data:'purchaseNum'},{data:'supplySellDays'},
            	{data:'supplyWarning'},{data:'cartonSize'}
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){			
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    laydate.render({
        elem: '#reportDate',
        trigger: 'click',//可以解决多个实例闪退问题
        range: false, //或 range: '~' 来自定义分割字符
        type:'date'
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
    })
    //导出
    $("#export").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/purchasingReports/getOrderCount.asyn", form, function(result){//判断导出的数量是否超出1W
        	if(result.state==200){
        		var total = result.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据 
        		var param = $("#form1").serialize();
        		var url = serverAddr+"/purchasingReports/exportPurchasingReports.asyn?"+param;
            	$downLoad.downLoadByUrl(url);
        	}else{
            	if(!!result.expMessage){
            		$custom.alert.error(result.expMessage);
            	}else{
            		$custom.alert.error(result.message);

            	}
            }
        });
    });
    
</script>
