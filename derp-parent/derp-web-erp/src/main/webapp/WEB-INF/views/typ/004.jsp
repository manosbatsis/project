<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css" />

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt60">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
             <div class="row">
              <div class="card-box margin table-responsive" style="overflow-x:hidden;">
                 <div class="row">
              <!--  title   start  -->
              <div class="col-12">
                <div>
                 <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">客户编辑列表</a></li>
                    </ol>
                    </div>
                 </div>
                    <!--  title   end  -->
                  <div class="margin">
                      <div class="form-row">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">主数据客户ID<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsCode">
                                  </div>
                              </div>
                          </div>
                          <div class="col-2">
                              <div class="row">
                                   <div class="col-1  checkbox checkbox-info checkbox-circle mt10 ml30">
                                     <input id="checkbox8" type="checkbox" checked="">
                                  </div>
                                  <label class="col-5 col-form-label">客户</label>
                              </div>
                          </div>
                          <div class="col-2 ml15">
                                <div class="row">
                                   <div class="col-1  checkbox checkbox-info checkbox-circle mt10 ">
                                     <input id="checkbox8" type="checkbox" checked="">
                                  </div>
                                  <label class="col-10 col-form-label" style="white-space:nowrap;">既是客户又是供应商</label>
                              </div>
                          </div>
                          <!--<div class="col-md-3"></div>-->
                      </div>
                      <div class="form-row mt20">
                          <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><i class="red">*</i><div class="fr">客户名称<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">客户简介<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">营业执照号<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">组织机构代码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">英文名<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">英文简介<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                         <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">企业性质<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                     <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">注册地<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">客户类型<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                     <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                   </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">客户等级编码<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                          <div class="col-12 ml30">
                              <div class="row">
                                  <label  class="col-1 col-form-label " style="white-space:nowrap;"><div class="fr">经营范围<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <textarea class="form-control" rows="5" name="describe"></textarea>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="ml10 page-title col-12 borderb mt20">
                          <div class="ml10">联系方式</div>
                      </div>
                     <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">法人代表<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">法人国籍<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">法人代表身份证<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">法人电话<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                       <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">公司电话<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="ml10 page-title col-12 borderb mt20">
                          <div class="ml10">财务属性</div>
                      </div>
                         <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">付款方式<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                    <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                     </select>
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">折扣率<span>：</span></div></label>
                                  <div class="col-4 ml10">
                                      <div>
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                      </div>
                                  </div>
                                   <div class="col-1 mt20 ml10">
                                      <label>%</label>
                                      </div>
                              </div>
                          </div>
                      </div>
                       <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">账期<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">信用额度<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
                       <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">纳税识别度<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">开票类型<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                       </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="form-row mt20">
                             <div class="col-4">
                              <div class="row">
                                  <label  class="col-4 col-form-label " style="white-space:nowrap;"><div class="fr">开户银行<span>：</span></div></label>
                                  <div class="col-8 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-5">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">开户账号<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="name">
                                  </div>
                              </div>
                          </div>
                      </div>
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
<script type="text/javascript">
	$(function(){
		//保存按钮
		$("#save-buttons").click(function(){
			var url = "/merchandise/saveMerchandise.asyn";
			var form = $("#add-form").serializeArray();
			$custom.request.ajax(url, form, function(result){
				if(result.state == '200'){
					$custom.alert.success("添加成功！");
					setTimeout(function () {
						$load.a("/merchandise/toPage.html");
					}, 1000);
				}else{
					$custom.alert.error("添加失败！");
				}
			});
		});
		
		//取消按钮
		$("#cancel-buttons").click(function(){
			$load.a("/merchandise/toPage.html");
		});
		
	});
</script>
