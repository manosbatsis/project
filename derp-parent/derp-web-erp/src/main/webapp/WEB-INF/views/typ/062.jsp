<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
          <div class="row">
           <div class="col-12">
              <div class="card-box" >
                                         <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                           <li class="breadcrumb-item"><a href="#">爬虫配置</a></li>
                           <li class="breadcrumb-item"><a href="#">新增爬虫配置</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">使用平台<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">爬取账单时点<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                        <div class="form-row mt10">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">用户名<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">密码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt10">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">关联公司<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">关联客户<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="email" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr" id="save-buttons">保 存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">取 消</button>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
                               <!--======================Modal框===========================  -->
                  <!-- end row -->
           </div>
          </div>
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->

