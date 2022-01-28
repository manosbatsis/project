<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
          <div class="row">
           <div class="col-12 ml10">
           <div class="col-12 row">
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">电商订单详情</a></li>
                    </ol>
                 </div>
               <div class="card-box">
               		<div id="step"></div>
               </div>  
              <div class="card-box table-responsive" >
                 <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">订单信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">单据状态 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div><span class="text-success">已入仓</span></div>
                            </div>
                        </div>
                    </div>
                </div>  
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">销售订单编号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">国检状态<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">物流企业名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div> 
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">外部交易单号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">海关状态<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">运单号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>   
                     <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">公司 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">电商平台<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">备注<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">出库仓库 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">进口模式<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">订单来源 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>跨境宝推送/导入</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">交易时间 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ml10 page-title col-12 borderb mt20">
	                    <div class="ml10">订购人信息</div>
	           </div>
	           <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">订购人 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">注册号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">电话号码 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
	           <div class="ml10 page-title col-12 borderb mt20">
	                    <div class="ml10">收件人信息</div>
	           </div>
	           <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">收件人 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">电话号码 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">收件地址 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
	           <div class="ml10 page-title col-12 borderb mt20">
	                    <div class="ml10">商品信息</div>
	           </div>
	           <div class="row col-12 mt20">
	           		<div class="card-box">
	           			<ul class="nav nav-tabs">
	           				<li class="nav-item">
	           					<a href="#itemList" data-toggle="tab" class="nav-link active">商品信息</a>
	           				</li>
	           				<li class="nav-item">
	           					<a href="#batchDefault" data-toggle="tab" class="nav-link">核销信息</a>
	           				</li>
	           			</ul>
	           			<div class="tab-content">
	           				<div class="tab-pane fade show active" id="itemList">
	           					<table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th style="white-space:nowrap;" class="tc">序号</th>
			                            <th style="white-space:nowrap;" class="tc">商品货号</th>
			                            <th style="white-space:nowrap;" class="tc">商品编号</th>
			                            <th style="white-space:nowrap;" class="tc">商品名称</th>
			                            <th style="white-space:nowrap;" class="tc">商品条形码</th>
			                            <th style="white-space:nowrap;" class="tc">计量单位</th>
			                            <th style="white-space:nowrap;" class="tc">销售数量</th>
			                            <th style="white-space:nowrap;" class="tc">销售单价</th>
			                            <th style="white-space:nowrap;" class="tc">销售总金额</th>
			                            <th style="white-space:nowrap;" class="tc">批次</th>
			                            <th style="white-space:nowrap;" class="tc">生产日期</th>
			                            <th style="white-space:nowrap;" class="tc">失效日期</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        </tbody>
			                    </table>
                            </div>
                            <div class="tab-pane fade" id="batchDefault">
                            </div>
	           			</div>
	           		</div>
               </div>             
             </div>
                  <!-- end row --> 
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script  type="text/javascript">
var $step = $("#step");
$step.step({
	index: 0,
	time: 500,
	title: ["制单时间", "审核时间", "申报时间", "放行时间", "发货时间"]
});
</script>