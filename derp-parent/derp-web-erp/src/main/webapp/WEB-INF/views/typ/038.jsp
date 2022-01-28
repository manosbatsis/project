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
                       <li class="breadcrumb-item"><a href="#">配置信息详情</a></li>
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
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 使用对象<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 名称<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr"> 创建人<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>   
                  <div class="form-row mt20 ml15">
                    <div class="col-4">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">KEY <span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">VALUE<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="row">
                            <label  class="col-4 col-form-label" style="white-space:nowrap;"><span class="fr">创建时间<span>：</span></span></label>
                            <div class="col-8 ml10 line">
                            </div>
                        </div>
                    </div>
                </div>
                 <!--  title   基本信息  start -->
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
