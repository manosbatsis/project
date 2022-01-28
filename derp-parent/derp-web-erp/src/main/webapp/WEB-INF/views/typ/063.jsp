<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css"/>

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
                           <li class="breadcrumb-item"><a href="#">xxxx</a></li>
                           <li class="breadcrumb-item"><a href="#">批量导入</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      <div class="title_text">批量导入</div>
                    <div class="mt20">
                        <button type="button" class="btn select_file" id="btn-file">选择文件<input type="file" name="file" class="upload_import"></button>
                        <button type="button" class="btn upload_file">上传文件<input type="file" name="file" class="upload_import"></button>
                        <button type="button" class="btn template_download">模板下载</button>
                    </div>
                        <p class="mt20">新增数据：<span>xx</span>条</p>
                    <div class="batch_import">
                        <table>
                            <thead>
                            <tr>
                                <td>序号</td>
                                <td>仓库名称</td>
                                <td>商品货号</td>
                                <td>商品名称</td>
                                <td>商品条码</td>
                                <td>库存类型</td>
                                <td>库存数量</td>
                                <td>批次号</td>
                                <td>生产日期</td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><input type="checkbox"></td>
                                <td>1347766554</td>
                                <td>1347766554</td>
                                <td>1347766554</td>
                                <td>好品/坏品</td>
                                <td>1000</td>
                                <td>12334445</td>
                                <td>8877655677</td>
                                <td>2018年1月1日</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-6">
                                      <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" id="save-buttons">确认并保存</button>
                                  </div>
                                  <div class="col-6">
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
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

