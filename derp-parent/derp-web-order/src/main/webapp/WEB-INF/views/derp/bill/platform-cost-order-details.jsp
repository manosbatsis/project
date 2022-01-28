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
                                <li class="breadcrumb-item"><a href="#">平台费用单详情</a></li>
                            </ol>
                        </div>
                    </div>
        
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <input type="hidden" parsley-type="text" class="input_msg"
                                           name="platformCostOrderId" id="platformCostOrderId" value="${detail.id }">
                                </div>
                            </div>                           
                        </div>
                    </form>
                    <div class="title_text">基本信息</div>
                    <div class="info_box">
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>费用单号：</span>                   
	                            <span>${detail.code }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>事业部：</span>                   
	                            <span>${detail.buName }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>账单号：</span>                   
	                            <span>${detail.billCode }</span>                
	                         </div>                                                   
	                      </div>     
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>客户：</span>                   
	                            <span>${detail.customerName }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>费用项目：</span>                   
	                            <span>${detail.itemProjectName }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>费用类型：</span>                   
	                            <span>${detail.costTypeLabel }</span>                
	                         </div>                                                   
	                      </div>   
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>结算币种：</span>                   
	                            <span>${detail.currency }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>单据状态：</span>                   
	                            <span>${detail.statusLabel }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>单据来源：</span>                   
	                            <span>${detail.sourceLabel }</span>                
	                         </div>                                                   
	                      </div>  
	                      
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>创建人：</span>                   
	                            <span>${detail.confirmName }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>创建时间：</span>                   
	                            <span>${detail.createDate }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>确认人：</span>                   
	                            <span>${detail.confirmName }</span>                
	                         </div>                                                   
	                      </div>         
	                      <div class="info_item">
	                         <div class="info_text">
	                         	<span>确认时间：</span>                   
	                            <span>${detail.confirmDate }</span>                
	                         </div> 
	                         <div class="info_text">
	                         	<span>转账单人：</span>                   
	                            <span>${detail.transferSlipName }</span>                
	                         </div>   
	                         <div class="info_text">
	                         	<span>转账单时间：</span>                   
	                            <span>${detail.transferSlipDate }</span>                
	                         </div>                                                   
	                      </div>                                      
                    </div>

                            
                    


                    <!--  ================================表格 ===============================================   -->
                    <div class="title_text">费用明细</div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped"
                               cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>商品货号</th>
                                <th>平台SKU条码</th>
                                <th>商品名称</th>
                                <th>PO号</th>
                                <th>数量</th>
                                <th>结算金额</th>                                
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
			$module.revertList.serializeListForm() ;
			$module.revertList.isMainList = true;
			$module.load.pageOrder('act=14008');
		});
    	

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformCostOrder/listLatformCostOrderItem.asyn?r='+Math.random(),
            columns:[  
                {data:'goodsNo'},{data:'skuNo'},{data:'goodsName'},{data:'poNo'},
                {data:'num'},{data:'amount'}
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