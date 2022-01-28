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
								<li class="breadcrumb-item"><a href="#">云集账单校验</a></li>
							</ol>
						</div>
					</div>
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
								    <span class="msg_title">账单月份 :</span>
                                    <input type="text" parsley-type="text" class="input_msg date-input" id="month" name="month" value="${month }">
                                    <span class="msg_title">结算单号 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="settleId" name="settleId">
                                    <span class="msg_title">只看差异 :</span>
                                    <input type="checkbox" parsley-type="text" id="checkDifference" name="checkDifference" value="1">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
								<div class="form_item h35">
                                    <span class="msg_title">云集商品编码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="skuNo" name="skuNo" >
                                </div>
                                <a href="javascript:" class="unfold">展开功能</a>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
	                         <shiro:hasPermission name="yunji_auto_veri_export">
                                 <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export" value="导出"/>
                             </shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>&nbsp;&nbsp;账单月份</th>
									<th>&nbsp;&nbsp;&nbsp;&nbsp;结算单号&nbsp;</th>
									<th>&nbsp;&nbsp;结算日期</th>
									<th>云集商品编码</th>
									<th>平台发货量</th>
									<th>平台退货量</th>
									<th>&nbsp;&nbsp;商品货号&nbsp;&nbsp;</th>
									<th>系统出库量</th>
									<th>系统入库量</th>
									<th>&nbsp;&nbsp;差异&nbsp;&nbsp;</th>
									<th>&nbsp;&nbsp;原因&nbsp;&nbsp;</th>
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

	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/yunjiAutoVeri/listYunjiAutoVerification.asyn?r='+Math.random(),
            columns:[
            /* 每一列的数据 */
                {data:'month'},{data:'settleId'},{data:'settleDate'},{data:'skuNo'},
                {data:'platformDeliveryAccount'},{data:'platformReturnAccount'},
                {
                	data:'goodsNo',
              		render: function (data, type, row, meta) {
                          
              		    var html = "" ;
              		    
              		    if(row.goodsNo){
              		    	var goodsNos = row.goodsNo ;
              		    	goodsNos = goodsNos.split(",") ;
              		    	
              		    	$(goodsNos).each(function(index, goodsNo){
              		    		html += goodsNo + "<br/>" ;
              		    	}) ;
              		      
              		    }
              		    
              		    return html ;
                          
                      }
                },
                {data:'systemDeliveryAccount'},{data:'systemReturnAccount'},
                {
                	data: null,
                    render: function (data, type, row, meta) {
                    	
                    	var deliveryDifferent = row.deliveryDifferent ;
                    	var returnDifferent = row.returnDifferent ;
                    	
                    	var html = "" ;
                    	
                    	html += "出库：" + deliveryDifferent + "<br/>" + "入库：" + returnDifferent ;
                    	
                        return html;
                    }
                },
                {data:'result'}
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
        var jsonData=null;
        var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        var url = serverAddr+"/yunjiAutoVeri/getExportCount.asyn?r="+Math.random() ;
        
        $custom.request.ajax(url, form, function(data){//判断导出的数量是否超出1W
            if(data.state==200){
                var total = data.data;//总数
                if(total>10000){
                    $custom.alert.error("导出的数量超过1万条，请填写筛选条件再导出");
                    return;
                }
                if(total==0){
                    swal({title: '导出数据为空！',type: 'warning',confirmButtonColor: '#4fa7f3'});
                    return;
                }
                
              //导出数据
                var url = serverAddr+"/yunjiAutoVeri/export.asyn";
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
	

</script>