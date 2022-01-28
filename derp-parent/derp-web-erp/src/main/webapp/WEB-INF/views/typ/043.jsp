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
                       <li class="breadcrumb-item"><a href="#">库存结算详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">基本信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 结算单单号<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">结算状态 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">结算月份<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">结算时间<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">公司 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">期初时间 <span>：</span></span></label>
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
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">仓库名称 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">期末时间 <span>：</span></span></label>
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
                 <!--  title   基本信息  end -->
                  <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">商品信息</div>
                </div>
                <div class="row col-12 mt20">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>仓库名称</th>
                            <th>商品货号</th>
                            <th>商品名称</th>
                            <th>批次</th>
                            <th>生产日期</th>
                            <th>失效日期</th>
                            <th>计算库存量</th>
                            <th>仓库现存量</th>
                            <th>库存类型</th>
                            <th>期末结账库存</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                </div>
              </div>
                           <!-- 新增弹窗部分   start -->
             </div>
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
