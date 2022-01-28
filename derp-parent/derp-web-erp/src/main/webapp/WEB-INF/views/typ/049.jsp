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
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">编辑资质</a></li>
                    </ol>
                 </div>
              <div class="card-box table-responsive" >
				  <div class="title_text">基础信息</div>
	           <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">所属公司<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
	           <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">客户名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">客户简称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">营业执照号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">组织机构代码<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div></div>
                            </div>
                        </div>
                    </div>
                </div>

				  <div class="title_text">资质信息</div>
	           <div class="row col-12 mt20">
	           	<table class="table table-bordered" cellspacing="0" width="100%">
		                        <thead>
		                        <tr>
		                            <th>资料名称</th>
		                            <th>资料图片</th>
		                            <th>修改人</th>
		                            <th>修改时间</th>
		                        </tr>
		                        </thead>
		                        <tbody>
		                        	<tr>
		                        		<td>营业执照副本</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>组织机构代码副本</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>注册登记证</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>供货记录</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>证明信息</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>品牌授权</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>银行流水</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>企业备案表</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        	<tr>
		                        		<td>法人身份证</td>
		                        		<td>XXX/XXX/XXXX.jpg</td>
		                        		<td></td>
		                        		<td></td>
		                        	</tr>
		                        </tbody>
		                    </table>
               </div>             
             </div>
                  <!-- end row --> 
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
