<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
 
        <div class="row">
                     <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">批量导入</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <!--  ================================上传文件部分 start  ===============================================   -->
                    <form id="form1" >
                        <div class="form-row  col-12">
                            <div class="col-4">
                                <div class="row col-12">
                                    <label  class="col-4 col-form-label"  style="white-space:nowrap;">上传文件<span>：</span></label>
                                    <div class="col-7 mll">
                                      <div class="fileupload fileupload-new" data-provides="fileupload">
                                                    <button type="button" class="btn btn-gradient btn-file">
                                                        <span class="fileupload-new"><i class="fa fa-paper-clip"></i>选择文件</span>
                                                        <span class="fileupload-exists"><i class="fa fa-undo"></i>重新上传</span>
                                                        <input type="file" class="btn-secondary">
                                                    </button>
                                                    <span class="fileupload-preview" style="margin-left:5px;"></span>
                                                    <a href="#" class="close fileupload-exists" data-dismiss="fileupload" style="float: none; margin-left:5px;"></a>
                                          </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4" style="margin-top:10px;">
                            <div class="checkbox">
                              <input id="checkbox1" type="checkbox">
                                    <label for="checkbox1">
                                      同一编号信息保留原有信息
                                    <label>                                  
                            </div>
                            </div>
                           <div class="col-4" style="margin-top:10px;">
                              <a href="#"> 模板下载</a>
                           </div>
                        </div>
                    </form>
                    <!--  ================================上传文件部分  end===============================================   -->
                    <!--  ================================导入信息显示部分  start ===============================================   -->
                    <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                           <h5 class="black">导入详情:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">新增数据<span>：</span></div>
                              <div class="col-2 ml15">100条</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">错误数据<span>：</span></div>
                              <div class="col-2 ml15"><span class="red">100</span>条</div>
                        </div>
                   </div>
                   <div class="col-9 mt10 p-20 ml30"  id="border">
                       <div class="col-12 row mt10">
                          <h5 class="black"> 填写Excle模板小贴士:</h5>
                       </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">1.无法对同一商品编号的商品做多次导入；</div>
                        </div>
                        <div class="col-12 row mt10">
                              <div class="col-1"  style="white-space:nowrap;">2.按照格式进行填写数据;</div>
                        </div>
                   </div>
                    <!--  ================================导入信息显示部分  end ===============================================   -->
            </div>
            </div>
        </div>

    



        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>



<script type="text/javascript">

</script>