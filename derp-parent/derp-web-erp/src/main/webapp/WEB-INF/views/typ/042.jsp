<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">调拨理货单详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">理货单详情</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 理货单据状态<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            	<span class="text-success">待确认</span>
                            	<button type="button" class="btn btn-info waves-effect waves-light mr20" >确认</button>
                            	<button type="button" class="btn btn-info waves-effect waves-light" >驳回</button>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 调拨订单号<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">理货单号 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">预申报单号<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">确认人<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">合同号 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">仓库名称 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">确认时间 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">理货时间 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">托板数量 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">创建人 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">备注 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">创建时间 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                 <!--  title   基本信息  end -->
                  <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">理货商品详情</div>
                </div>
                <div class="row col-12 mt20">
	           		<div class="card-box">
	           			<ul class="nav nav-tabs">
	           				<li class="nav-item">
	           					<a href="#itemList" data-toggle="tab" class="nav-link active">商品列表</a>
	           				</li>
	           				<li class="nav-item">
	           					<a href="#batchDefault" data-toggle="tab" class="nav-link">批次明细</a>
	           				</li>
	           			</ul>
	           			<div class="tab-content">
	           				<div class="tab-pane fade show active" id="itemList">
	           					<table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th style="white-space:nowrap;" class="tc">序号</th>
			                            <th style="white-space:nowrap;" class="tc">商品货号</th>
			                            <th style="white-space:nowrap;" class="tc">商品名称</th>
			                            <th style="white-space:nowrap;" class="tc">商品条形码</th>
			                            <th style="white-space:nowrap;" class="tc">申报数量</th>
			                            <th style="white-space:nowrap;" class="tc">理货数量</th>
			                            <th style="white-space:nowrap;" class="tc">缺少数量</th>
			                            <th style="white-space:nowrap;" class="tc">多货数量</th>
			                            <th style="white-space:nowrap;" class="tc">可售数量</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        </tbody>
			                    </table>
                            </div>
                            <div class="tab-pane fade" id="batchDefault">
	           					<table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th style="white-space:nowrap;" class="tc">序号</th>
			                            <th style="white-space:nowrap;" class="tc">商品货号</th>
			                            <th style="white-space:nowrap;" class="tc">商品名称</th>
			                            <th style="white-space:nowrap;" class="tc">批次号</th>
			                            <th style="white-space:nowrap;" class="tc">生产日期</th>
			                            <th style="white-space:nowrap;" class="tc">效期至</th>
			                            <th style="white-space:nowrap;" class="tc">损货数量</th>
			                            <th style="white-space:nowrap;" class="tc">过期数量</th>
			                            <th style="white-space:nowrap;" class="tc">到期日期</th>
			                            <th style="white-space:nowrap;" class="tc">子合同号</th>
			                            <th style="white-space:nowrap;" class="tc">重量</th>
			                            <th style="white-space:nowrap;" class="tc">体积</th>
			                            <th style="white-space:nowrap;" class="tc">长</th>
			                            <th style="white-space:nowrap;" class="tc">宽</th>
			                            <th style="white-space:nowrap;" class="tc">高</th>
			                        </tr>
			                        </thead>
			                        <tbody>
			                        </tbody>
			                    </table>
                            </div>
	           			</div>
	           		</div>
               </div>  
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
