<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />

<!-- Start content-->
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
                       <li class="breadcrumb-item"><a href="#">协议单价新增</a></li>
                    </ol>
                    </div>
                   </div>
                    <!--  title   end  -->               
                          <!--  title   基本信息   start -->
                    <div class="title_text">基本信息</div>
                    <div class="edit_box mt20">
                        <div class="edit_item">
                            <input type="hidden" value="${merchantId}" name="merchantId" id="merchantId"/>
                            <input type="hidden" value="${merchantName}" name = "merchantName" id = "merchantName">
                            <label class="edit_label"><i class="red">*</i>移入事业部：</label>
                            <select  class="edit_input" name="inBuId" id="inBuId" required reqMsg = "移入事业部不能为空"></select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>移出事业部：</label>
                            <select  class="edit_input" name="outBuId" id="outBuId" required reqMsg = "移出事业部不能为空"></select>
                        </div>
                        <div class="edit_item">
                            <label class="edit_label"><i class="red">*</i>协议币种：</label>
                            <select class="edit_input" name="currency" id="currency" parsley-trigger="change" required reqMsg="协议币种不能为空">
                            </select>
                        </div>
                    </div>
                 <!--  title   基本信息  start -->
                   <!--   商品信息  start -->
                    <div class="col-12 ml10">
                        <div class="row">
                         <div class="col-10">
                             <div class="title_text">商品信息</div>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(7, null, null);">选择商品</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                    </div>                       
                    <div class="form-row mt20 ml15">
                   <table id="table-list" class="table table-striped">
                   <thead>
                      <tr>
                         <th><input id="checkbox11" type="checkbox" name="all"></th>
                          <th>商品货号</th>
                          <th>商品名称</th>
                          <th>条形码</th>
                          <th>品牌</th>
                          <th>协议单价</th>
                      </tr>
                      </thead>
                      <tbody></tbody>
                  </table>                              
                   </div>
                 <!--   商品信息  end -->
                      <div class="form-row m-t-50">
                          <div class="col-12">
                              <div class="row">
                                  <div class="col-4">
                                  </div>
                                  <div class="col-4">
                                      <shiro:hasPermission name="agreementCurrencyConfig_saveConfig">
                                          <input type="button" class="btn btn-info waves-effect waves-light btn_default" id="save-buttons" value="保存"/>
                                      </shiro:hasPermission>
                                      <button type="button" class="btn btn-light waves-effect waves-light btn_default" id="cancel-buttons">取 消</button>
                                  </div>
                                  <div class="col-4">
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
              </div>
             </div>
             </div>
              <!-- end row -->
              <!-- 弹窗开始 -->
              <jsp:include page="/WEB-INF/views/modals/9081.jsp" />
              <!-- 弹窗结束 -->
              <!-- end row -->
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='inBuId']")[0],null,null, null,null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='outBuId']")[0],null,null, null,null);
$module.constant.getConstantListByNameURL.call($('select[name="currency"]')[0],"currencyCodeList",null);

 $(document).ready(function() {
        $(function(){
            //取消按钮
            $("#cancel-buttons").click(function(){
                $module.load.pageOrder('act=15001');
            });
        });
		//提交按钮
		$("#save-buttons").click(function(){
            //必填项校验
            if(!$checker.verificationRequired()){
                return ;
            }
            if($("#unNeedIds").val()==""){
                $custom.alert.error("至少选择一个商品！");
                return ;
            }
            if($("#inBuId").val()==$("#outBuId").val()){
                $custom.alert.error("移入/移出事业部不能选择一样！");
                return ;
            }
			$custom.alert.warning("确定保存该配置？",function(){
			var url = serverAddr+"/agreementCurrencyConfig/addAgreementCurrencyConfig.asyn";
			var form = {
			};
			var json = {};
            var merchantId = $("#merchantId").val();
            var merchantName = $("#merchantName").val();
            var inBuId = $("#inBuId").val();
            var inBuName = $("#inBuId").find("option:selected").text();
			var outBuId = $("#outBuId").val();
            var outBuName = $("#outBuId").find("option:selected").text();
            var currency = $("#currency").val();
			var itemList = [];
			var flag = true;
			$('#table-list tbody tr').each(function(i){// 遍历 tr
				var item ={};
                var goodsId = $(this).find('input[name="goodsId"]').val();
                item['goodsId']=goodsId;
                var goodsCode = $(this).find('input[name="goodsCode"]').val();
                item['goodsCode']=goodsCode;
                var goodsNo = $(this).find('input[name="goodsNo"]').val();
                item['goodsNo']=goodsNo;
                var goodsName = $(this).find('input[name="goodsName"]').val();
                item['goodsName']=goodsName;
                var barcode = $(this).find('input[name="barcode"]').val();
                item['barcode']=barcode;
                var price = $(this).find('input[name="price"]').val();
                item['price']=price;
                var brandName = $(this).find('input[name="brandName"]').val();
                item['brandName']=brandName;
				if(!price || isNaN(price) ||price<=0){
					$custom.alert.error("协议单价必须是大于0的数");
                    flag = false;
		    		return;
				}
				itemList.push(item);
			});
			if(!flag){
				return;
			}
			if(!itemList || itemList.length==0){
				$custom.alert.error("至少选择一个商品");
				return;
			}
        	if(!flag){
				return false;
			}
            json['merchantId']=merchantId;
            json['merchantName']=merchantName;
            json['inBuId']=inBuId;
            json['inBuName']=inBuName;
            json['outBuId']=outBuId;
            json['outBuName']=outBuName;
            json['currency']=currency;
            json['itemList']=itemList;

			var jsonStr= JSON.stringify(json);			
			form['json']=jsonStr;

			$custom.request.ajax(url, form, function(data){
				if(data.state==200){
					var str = data.data;
					$custom.alert.success(str);
					//重新加载页面
					setTimeout(function () {
						$load.a(pageUrl+"15001");
					}, 1000);
				}else{
					if(!!data.expMessage){
						$custom.alert.error(data.expMessage);
					}else{
						$custom.alert.error(data.message);
					}
				}
			});
		});
		});
		//删除选中行
	 	$("#delete-list-button").click(function() {
	 		var goodsId = [];
	        $("input[name='item-checkbox']:checked").each(function() { // 遍历选中的checkbox
	            n = $(this).parents("tr").index()+1;  // 获取checkbox所在行的顺序
	            goodsId.push($(this).next().val());
	            $("#table-list").find("tr:eq("+n+")").remove();
	        });
	        var unNeedIds = $("#unNeedIds").val();
	        var temp = unNeedIds.split(",");
	        var newUnNeedIds = delArrElement(goodsId,temp);
	        $("#unNeedIds").val(newUnNeedIds);
	        $("input[name='all']").prop("checked",false);
	    });
		function delArrElement(goodsId,temp){
			for (var i = 0; i < goodsId.length; i++) {
	        	for (var j = 0; j < temp.length; j++) {
					if(goodsId[i] == temp[j]){
						temp.splice(j,1);
						delArrElement(goodsId,temp);
					}
				}
			}
			return temp;
		}
	 	$("input[name='all']").click(function(){
			//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
			if($(this).is(":checked")){
				$("input[name='item-checkbox']").prop("checked",true);
			}else{
				$("input[name='item-checkbox']").prop("checked",false);
			}
		});
     //监听协议单价的离开事件
     $("#table-list").on("blur",'.goods-price',function(){
         var that = $(this);
         //获取当前价格
         var price= that.val();
         //判断是否数字
         if(isNaN(price)){
             $custom.alert.error("协议单价请输入数字");
             return ;
         }
         if(price<=0){
             $custom.alert.error("协议单价请输入大于0的数");
             return ;
         }
     });
 });
</script>