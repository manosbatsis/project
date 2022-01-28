<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form">
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
                       <li class="breadcrumb-item"><a href="#">公司信息</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 主数据客户ID<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"></label>
                            <div class="col-5 ml10 line">
                            	<input type="radio" />是否启用
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"><i class="red">*</i>客户名称 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">客户简称<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">总署备案编码 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">卓志编码<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客商类型 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <select class="form-control">
                                	<option></option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">组织机构代码<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客户等级编码 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">企业性质<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <select class="form-control">
                                	<option></option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                 <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客商类型 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <select class="form-control">
                                	<option></option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                	<div class=col-12>
                		<div class="row">
                			 <label class="col-1 col-form-label " style="white-space:nowrap;"><span class="fr">经营范围 <span>：</span></span></label>
                			 <div class="col-11 ml10 line">
                            	<textarea rows="" cols="" class="form-control"></textarea>
                            </div>
                		</div>
                	</div>
                </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                <div class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">地址信息</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">法人代表 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">营业执照号<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">法人代表身份证 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">法人电话<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">公司电话 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">法人国籍<span>：</span></span></label>
                            <div class="col-5 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row col-12 mt20">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>操作</th>
                            <th>联系人</th>
                            <th>联系人电话</th>
                            <th>E-Mail</th>
                            <th>传真号码</th>
                            <th>企业详细地址</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="ml10 page-title col-12 borderb mt20">
                   <div class="ml10">其他配置</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">pop订单获取接口 <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                                <input type="text" required="" parsley-type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"></label>
                            <div class="col-5 ml10 line">
                                <input type="checkbox" />是否启用关联客户
                            </div>
                        </div>
                    </div>
                </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-1"></div>
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
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