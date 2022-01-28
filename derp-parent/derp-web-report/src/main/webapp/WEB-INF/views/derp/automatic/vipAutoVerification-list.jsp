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
								<li class="breadcrumb-item"><a href="#">自动化校验</a></li>
								<li class="breadcrumb-item"><a href="#">唯品账单校验</a></li>
							</ol>
						</div>
					</div>
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
								    <span class="msg_title">年月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month" name="month" value="${month }">
                                    <span class="msg_title">PO号 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="poNo" name="poNo">
                                    <span class="msg_title">账单号 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="crawlerNo" name="crawlerNo" >
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								<div class="form_item h35">
								    <span class="msg_title">校验结果 :</span>
                                    <select name="verificationResult" id="verificationResult" class="input_msg">
                                    </select>
								</div>
							</div>
							<a href="javascript:" class="unfold">展开功能</a>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
	                         <shiro:hasPermission name="vip_auto_veri_export">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export" value="导出"/>
                             </shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>&nbsp;&nbsp;年月份</th>
									<th>&nbsp;&nbsp;&nbsp;&nbsp;账单号&nbsp;</th>
									<th>&nbsp;&nbsp;&nbsp;PO&nbsp;号&nbsp;&nbsp;</th>
									<th>账单销售总量</th>
									<th>账单红冲总量</th>
									<th>账单其他总量（调增）</th>
									<th>账单其他总量（调减）</th>
									<th>系统销售出库总量</th>
									<th>系统红冲总量</th>
									<th>系统库存调整（调增）</th>
									<th>系统库存调整（调减）</th>
									<th>校验结果</th>
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
$module.constant.getConstantListByNameURL.call($('select[name="verificationResult"]')[0],"vipAutoVeri_verificationResultList",null);
	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/vipAutoVeri/listVipAutoVerification.asyn?r='+Math.random(),
            columns:[
            /* 每一列的数据 */
                {data:'month'},{data:'crawlerNo'},{data:'poNo'},{data:'billSalesAccount'},
                {data:'billHcAccount'},{data:'billAdjustmentIncreaseAccount'},
                {data:'billAdjustmentDecreaseAccount'},{data:'systemSalesOutAccount'},{data:'systemHcAccount'},
                {data:'systemAdjustmentIncreaseAccount'},{data:'systemAdjustmentDecreaseAccount'},{data:'verificationResultLabel'}
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

    // 日期选择
    $(".date-input").datetimepicker({
         format: 'yyyy-mm',
            autoclose: true,
            todayBtn: true,
            startView: 'year',
            minView:'year',
            maxView:'decade',
            language:  'zh-CN',
    });
	
	// 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
	
	//汇总表导出
	 $("#export").on("click",function(){
        //根据筛选条件导出
        var url = serverAddr+"/vipAutoVeri/export.asyn?r="+Math.random() ;
        
        var poNo = $("#poNo").val() ;
        var crawlerNo = $("#crawlerNo").val() ;
        
        if( !poNo && !crawlerNo){
        	$custom.alert.error("请筛选条件导出");
        	
        	return ;
        }
        
        var params = $("#form1").serialize() ;
        $custom.alert.warning("确定导出选中对象？",function(){
        	$downLoad.downLoadByUrl(url + "&" +params);
        });
    });
	

</script>