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
                                <li class="breadcrumb-item"><a href="#">平台结算单详情</a></li>
                            </ol>
                        </div>
                    </div>
        
                    <form id="form1">
                        <input type="hidden" parsley-type="text" class="input_msg"
                               name="platformStatementOrderId" id="platformStatementOrderId" value="${detail.id }">
                    </form>
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>平台结算单号：</span>                   
	                            <span>${detail.billCode }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>账单月份：</span>                   
	                            <span>${detail.month }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>客户：</span>                   
	                            <span>${detail.customerName }</span>                
	                         </div>                                                   
	                      </div>     
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>账单金额：</span>                   
	                            <span>${detail.billAmount }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>结算币种：</span>                   
	                            <span>${detail.currency }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>是否已开票：</span>                   
	                            <span>${detail.isInvoiceLabel }</span>                
	                         </div>                                                   
	                      </div>   
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>开票人：</span>                   
	                            <span>${detail.invoiceDrawer }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>开票日期：</span>                   
	                            <span>${detail.invoiceDate }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>账单日期：</span>                   
	                            <span><fmt:formatDate value="${detail.billDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
	                         </div>                                                   
	                      </div>  
	                      
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="title_text">账单信息</div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>类型</th>
                                <th>PO号</th>
                                <th>商品条码</th>
                                <th>商品名称</th>
                                <th>标准品牌</th>
                                <th>结算数量</th>
                                <th>结算金额（原币）</th>                                
                                <th>结算金额（RMB）</th>                                
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
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
                                  </div>
                              </div>
                          </div>
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
    	
    	//取消按钮
		$("#cancel-buttons").click(function(){          
			$module.load.pageOrder('act=14012');
		});
    	

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformStatementOrder/listPlatformStatementItem.asyn?r='+Math.random(),
            columns:[  
                {data:'typeLabel'},{data:'poNo'},{data:'barcode'},{data:'goodsName'},
                {data:'brandParent'},{data:'settlementNum'},{data:'settlementAmount'},{data:'settlementAmountRmb'}
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



</script>