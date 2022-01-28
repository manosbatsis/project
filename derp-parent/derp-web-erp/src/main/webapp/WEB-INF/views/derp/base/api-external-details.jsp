<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                                          <!--  title   start  -->
              <div class="col-12">
                <div>
                   <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">接口管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/apiExternal/toPage.html');">接口管理列表</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/apiExternal/toDetailsPage.html');">外部接口配置详情</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                <form id="add-form">
                <div class="info_box">
                	<div class="info_item">
                            <div class="info_text">
                            	<span>使用对象：</span>
                            	<span>${detail.platformName }</span>
                            </div>
                            <div class="info_text">
                            	<span>公司名称：</span>
                            	<span>${detail.merchantName }</span>
                            </div>
                    </div>
                    <div class="info_item">
                            <div class="info_text">
                            	<span>app_key：</span>
                            	<span>${detail.appKey }</span>
                            </div>
                            <div class="info_text">
                            	<span>秘钥：</span>
                            	<span>${detail.appSecret }</span>
                            </div>
                    </div>
                    <div class="info_item">
                            <div class="info_text">
                            	<span>备注：</span>
                            	<span>${detail.remark }</span>
                            </div>
                            <div class="info_text">
                            </div>
                    </div>
                </div>
                </form>
                 <!--  title   基本信息  start -->
                      <div class="form-row">
                    <div class="col-3">
                        <div class="form-row m-t-50">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返 回</button>
                                  </div>
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
        <!-- container -->
    </div>
</div>
</div> <!-- content -->


<script type="text/javascript">
		
		//返回
		$("#cancel-buttons").click(function(){
			$load.a("/apiExternal/toPage.html");
		});

</script>