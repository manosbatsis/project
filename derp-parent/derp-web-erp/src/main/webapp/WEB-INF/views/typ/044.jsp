<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css" />

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" >
              <div class="row">
                               <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">电商订单</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                      <div class="form-row" >
                          <form id="form1" >
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-6 col-form-label "><div class="fr">销售订单编号<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                  	<input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3">
                              <div class="row">
                                  <label class="col-6 col-form-label"><div class="fr">运  单  号<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3 ml15">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">仓库<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select  class="form-control" >
                                      		<option></option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                          <div class="form-group col-1">
                                <div class="row">
                                    <button type="button" class="btn ml15 btn-find waves-effect waves-light" onclick='$mytable.fn.reload();' >查询</button>
                                </div>
                            </div>
                            <div class="col-1">
                                <div class="row">
                                    <button type="reset" class="btn btn-light ml15 waves-effect waves-light">重置</button>
                                </div>
                            </div>
                            </form>
                      </div>
                      <div class="form-row filterBox">
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-6 col-form-label "><div class="fr">交易时间<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-6 col-form-label "><div class="fr">外部交易单号<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                      <input type="text" required="" parsley-type="text" class="form-control" name="goodsNo">
                                  </div>
                              </div>
                          </div>
                          <div class="col-3">
                              <div class="row">
                                  <label  class="col-6 col-form-label "><div class="fr">单据状态<span>：</span></div></label>
                                  <div class="col-6 ml10">
                                      <select  class="form-control" >
                                      		<option></option>
                                      </select>
                                  </div>
                              </div>
                          </div>
                      </div>
                      <div class="tc mt10">
                      	<a class="filter-toggle" href="javascript:void(0)">更多功能</a>
                      </div>
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <button type="button" class="btn btn-add waves-effect waves-light btn-sm">导出</button>
                        </div>
                    </div>     
                      <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10 table-responsive">
                    <table id="datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">
                            	<input type="checkbox" />全选
                            </th>
                            <th>商品名称</th>
                            <th>平台名称</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>买家</th>
                            <th>金额</th>
                            <th>发货仓</th>
                            <th>单据状态</th>
                            <th>运单号</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        	<tr>
                        		<td colspan="11" >
                        			<span><input type="checkbox" /></span>
                        			<span class="mr20">订单号：ORDERNO20180501</span>
                        			<span class="mr20">外部订单号：3035278163172</span>
                        			<span class="mr20">订单时间：2018-05-09 18:14:05</span>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td rowspan="2"></td>
                        		<td>
                        			<p class="nowrap">优思益Youthit蜂胶胶囊</p>
                        			<p class="nowrap">商品编码：OPG161014009166</p>
                        		</td>
                        		<td>
                        			<span class="nowrap">淘宝全球购</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">￥1267.00</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">2</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">小叮当</span>
                        		</td>
                        		<td>
                        			<p class="nowrap">优思益Youthit蜂胶胶囊</p>
                        			<p class="nowrap">商品编码：OPG161014009166</p>
                        		</td>
                        		<td>
                        			<span class="nowrap">南沙仓</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">待放行</span>
                        		</td>
                        		<td>
                        			<p class="nowrap">圆通快递</p>
                        			<p class="nowrap">981299876765</p>
                        		</td>
                        		<td>
                        			<a href="javascript:void(0)" class="nowrap">详情</a>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td colspan="11" >
                        			<span><input type="checkbox" /></span>
                        			<span class="mr20">订单号：ORDERNO20180501</span>
                        			<span class="mr20">外部订单号：3035278163172</span>
                        			<span class="mr20">订单时间：2018-05-09 18:14:05</span>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td rowspan="2"></td>
                        		<td rowspan="2" >
                        			<p class="nowrap">优思益Youthit蜂胶胶囊</p>
                        			<p class="nowrap">商品编码：OPG161014009166</p>
                        		</td>
                        		<td rowspan="2">
                        			<span class="nowrap">淘宝全球购</span>
                        		</td>
                        		<td rowspan="2">
                        			<span class="nowrap">￥1267.00</span>
                        		</td>
                        		<td rowspan="2">
                        			<span class="nowrap">2</span>
                        		</td>
                        		<td rowspan="4">
                        			<span class="nowrap">小叮当</span>
                        		</td>
                        		<td rowspan="4">
                        			<p class="nowrap">优思益Youthit蜂胶胶囊</p>
                        			<p class="nowrap">商品编码：OPG161014009166</p>
                        		</td>
                        		<td rowspan="4">
                        			<span class="nowrap">南沙仓</span>
                        		</td>
                        		<td rowspan="4">
                        			<span class="nowrap">待放行</span>
                        		</td>
                        		<td rowspan="4">
                        			<p class="nowrap">圆通快递</p>
                        			<p class="nowrap">981299876765</p>
                        		</td>
                        		<td rowspan="4">
                        			<a href="javascript:void(0)" class="nowrap">详情</a>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td></td>
                        		<td>
                        			<p class="nowrap">嘉宝Gerber 无花果蓝莓泡芙 42克</p>
                        			<p class="nowrap">商品编码：itemTBQQ0006</p>
                        		</td>
                        		<td>
                        			<span class="nowrap">淘宝全球购</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">￥1267.00</span>
                        		</td>
                        		<td>
                        			<span class="nowrap">2</span>
                        		</td>
                        	</tr>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                    <!--======================Modal框===========================  -->
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->

<script>
	var toggle = false;
	$('.filterBox').hide();
	$('.filter-toggle').click(function() {
		if (!toggle) {
			toggle = true;
			$('.filterBox').slideDown("fast")
		} else {
			toggle = false;
			$('.filterBox').slideUp("fast")
		}
	})
</script>
