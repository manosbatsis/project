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
                                <li class="breadcrumb-item"><a href="#">收款核销跟踪</a></li>
                            </ol>
                        </div>
                    </div>
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">应收账单号:</span>
                                    <input type="text" parsley-type="text" class="input_msg"
                                           name="receiveCode" id="receiveCode">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerId" id="customerId">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">账单月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input"
                                           name="billMonth" id="billMonth">

                                    <div class="fr">
                                        <button type="button"
                                                class="btn ml15 btn-find waves-effect waves-light btn_default"
                                                onclick='$mytable.fn.reload();'>查询</button>
                                        <button type="reset"
                                                class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">账期逾期 :</span>
                                    <select class="input_msg" name="overdueStatus" id="overdueStatus">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                </div>

                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a> -->
                        </div>
                    </form>
                     <div class="row col-md-12 mt20">
                            <div class="button-list">
								<shiro:hasPermission name="receiveBillVerification_export">
									<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-button" value="导出"/>
								</shiro:hasPermission>
								<shiro:hasPermission name="receiveBillVerification_flash">
									<input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flash" value="刷新"/>								
								</shiro:hasPermission>
								<label id="labelTime" style="color: red;margin-left: 20px;"></label>
								<label id="labelStatus" style="color: red;"></label>
                            </div>
                        </div>

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
                                <th>应收账单号</th>
                                <th>事业部</th>
                                <th>发票编号</th>
                                <th>是否开发票</th>
                                <th>账单月份</th>
                                <th>客户</th>
                                <th>应收金额</th>
                                <th>未核金额</th>
                                <th>结算币种</th>
                                <th>账单日期</th>
                                <th>开票日期</th>
                                <th>客户账期</th>
                                <th>账期逾期天数</th>
                                <th>创建时间</th>
                                <th>核销详情</th>
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
                <jsp:include page="/WEB-INF/views/modals/14003.jsp" />
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $module.constant.getConstantListByNameURL.call($('select[name="overdueStatus"]')[0],"receiveBillVerification_overdueStatusList",null);
    //加载客户的下拉数据
    $module.customer.loadSelectData.call($('select[name="customerId"]')[0]);
   
    $(document).ready(function() {
        var receiveCode = "${receiveCode}";
        $("#receiveCode").val(receiveCode);
    	$(".date-input").datetimepicker({
   		 format: 'yyyy-mm',
   	        autoclose: true,
   	        todayBtn: true,
   	        startView: 'year',
   	        minView:'year',
   	        maxView:'decade',
   	        language:  'zh-CN',
      });
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/receiveBillVerification/listReceiveBillVerification.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            }, 
                {data:'receiveCode'},{data:'buName'},{data:'invoiceNo'},{data:'invoiceStatusLabel'},{data:'billDate'},{data:'customerName'},{data:'receivePrice'},
                {data:'uncollectedPrice'},{data:'currencyLabel'},{data:'billDate'},{data:'invoiceDate'},
                {data:'accountPeriodLabel'},{data:'accountOverdueDays'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var html = "";
                        html += '<a href="javascript:notes(\''+row.receiveCode+'\')">备注</a>&nbsp;&nbsp;' ;
                        html += '<a href="javascript:details(\''+row.receiveId+'\')">详情</a>';
                        return html;
                    }
                },
            ],
            formid:'#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
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

    // 导出
    $("#export-button").click(function(){
    	var receiveCode = $("#receiveCode").val();
    	var customerId = $("#customerId").val();
    	var billMonth = $("#billMonth").val();
    	var overdueStatus = $("#overdueStatus").val();
    	var buId = $("#buId").val();
    	url = serverAddr+"/receiveBillVerification/export.asyn?receiveCode="+receiveCode+
    			"&customerId="+customerId+"&billMonth="+billMonth+
    			"&overdueStatus="+overdueStatus+
    			"&buId="+buId;
    	$custom.alert.warning("确定导出对象？",function(){
        	window.open(url);
        });
  	
    });
    // 刷新
    $("#flash").click(function(){
    	var url = serverAddr+"/receiveBillVerification/flash.asyn" ;
    	var receiveCodes=[];
        var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
        for(var i = 0; i < nTrs.length; i++){
            if($(nTrs[i]).find("input[type=checkbox]").prop('checked')){
                var row = $mytable.tableObj.fnGetData(nTrs[i]);
                receiveCodes.push(row.receiveCode);
            }
        }
        receiveCodes=receiveCodes.join(",");
        if(receiveCodes==null||receiveCodes==''||receiveCodes==undefined){
            $custom.alert.warningText("至少选择一条记录！");
            return false;
        }

    	$custom.request.ajax(url, {"receiveCodes" : receiveCodes }, function(data){
    		if(data.state == '200'){
    			$custom.alert.success("刷新成功");
    		}else {
    			$custom.alert.error(result.message);
			}
    	});

  	
    });

    function details(receiveId){
        $module.load.pageOrder('act=14002&id='+receiveId);
    }
    function notes(receiveCode){
   	  var url = serverAddr+"/receiveBillVerification/toNotesPage.asyn" ;
   	  $('#verification-modal').modal('show'); 
   		$('#notes').val('');
   		// 获取编辑详情
   	  $custom.request.ajax(url, {"receiveCode" : receiveCode }, function(data){
   				if(data.state == '200'){					
   					$("#itemTbody").empty() ;
   					var items = data.data;	
   					var html = "";  					
   					$(items).each(function( index , item){
   						html += '<tr>' + 
							'<td>'+item.notesName+'</td>'+
							'<td>'+item.createDate+'</td>' +
							'<td>'+item.notes+'</td>' + 
							'</tr>' ; 						
   					}) ;
   					
   					$("#itemTbody").append(html) ;
   					
   				}else{
   					$custom.alert.error(result.message);
   				}
   			});  
   	  $('#receiveCode').val(receiveCode);
    	
    }


</script>