<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
    }
    .header-title {
        padding: 15px;
        padding-right: 400px;
        font-weight: bolder;
        font-size: 18px;
    }
    .header-button {
        background-color: #fff;
        border: none;
    }
    .header-close {
    }
    .header-close::after {
        content: "\2716";
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="invoice-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              <span class="header-title" >新增交易链路</span>
          </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="invoice-from">
                            <!-- 自定义参数  -->
                      <div class="form-group">
                          <div class="col-12">
                              <label ><i class="red">*</i>交易链路名称&nbsp&nbsp : </label>
                              <input type="text" class="input_msg" id="linkName" name="linkName" style="width: 202px;"required reqMsg="请输入交易链路名称">
                          </div>
                      </div>
                      <div class="form-group">
                          <div class="col-12">
                              <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<i class="red">*</i>起点公司&nbsp&nbsp : </label>
                              <select class="input_msg" id="startPointMerchantId" name="startPointMerchantId"style="width: 202px;"required reqMsg="请选择起点公司"></select>
                              <label>&nbsp&nbsp&nbsp<i class="red">*</i>起点事业部: </label>
                              <select class="input_msg" name="startPointBuId" id="startPointBuId"style="width: 202px;" parsley-trigger="change" required reqMsg="请选择起点事业部">
                                  <option value=''>请选择</option>
                              </select>
                          </div>
                      </div>
                      <div class="form-group">
                        <div class="col-12">
                            <label ><i class="red">*</i>销售加价比例% : </label>
                            <input type="text" class="input_msg" id="startPointPremiumRate" name="startPointPremiumRate" style="width: 202px;">
                        	<label>&nbsp&nbsp&nbsp<i class="red">*</i>起点供应商: </label>
                            <select class="input_msg" name="startSupplierId" id="startSupplierId"style="width: 202px;" parsley-trigger="change" required reqMsg="请选择起点供应商">
                                <option value=''>请选择</option>
                            </select>
                        </div>
                      </div>
                      <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="oneType" name="oneType"  type="hidden">
                                <input class="input_msg" id="oneIdvaluetype" name="oneIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户1&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="oneCustomerId" name="oneCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red"  style="display:none;" id="one_i">*</i>客户1事业部: </label>
                                <select class="input_msg" name="oneBuId" id="oneBuId"style="width: 202px;" parsley-trigger="change">
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="onePremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="onePremiumRate" name="onePremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="twoType" name="twoType"  type="hidden">
                                <input class="input_msg" id="twoIdvaluetype" name="twoIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户2&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="twoCustomerId" name="twoCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="two_i">*</i>客户2事业部: </label>
                                <select class="input_msg" name="twoBuId" id="twoBuId"style="width: 202px;" parsley-trigger="change" >
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="twoPremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="twoPremiumRate" name="twoPremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="threeType" name="threeType"  type="hidden">
                                <input class="input_msg" id="threeIdvaluetype" name="threeIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户3&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="threeCustomerId" name="threeCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red"  style="display:none;" id="three_i">*</i>客户3事业部: </label>
                                <select class="input_msg" name="threeBuId" id="threeBuId"style="width: 202px;" parsley-trigger="change" >
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="threePremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="threePremiumRate" name="threePremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="fourType" name="fourType"  type="hidden">
                                <input class="input_msg" id="fourIdvaluetype" name="fourIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户4&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="fourCustomerId" name="fourCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="four_i">*</i>客户4事业部: </label>
                                <select class="input_msg" name="fourBuId" id="fourBuId"style="width: 202px;" parsley-trigger="change">
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                    </form>
                    <br/><br/><br/>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="tradeLinkCancel();">关闭</button>
                          <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="tradeLinkSave();">保存</button>
                      </div>
                  </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">

    //加载起点公司
    var merchantList = $module.merchantAll.listData();
    var merchantOption = '<option value="">请选择</option>';
    merchantList.forEach(function(o,i){
        merchantOption = merchantOption+'<option value="'+ o.value+'">'+o.label+'</option>';
    });
    $("#startPointMerchantId").append(merchantOption);
    // 起点公司改变
    $("#startPointMerchantId").change(function () {
        var startPointMerchantId = $("#startPointMerchantId").val();
        // 重新加载起点事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='startPointBuId']")[0],null, startPointMerchantId,null,null);
        // 重新加载客户1
        $module.customer.loadSelectDataByMerchantId.call($("select[name='oneCustomerId']")[0],null,1,startPointMerchantId,null);
        $module.supplier.loadSelectDataByMerchantId.call($("select[name='startSupplierId']")[0],null,startPointMerchantId);
    });
    // 客户1改变
    var oneCustomerInfo = null;//客户1信息
    var oneCustomerId = null;// 客户1Id
    $("#oneCustomerId").change(function () {
        oneCustomerId = $("#oneCustomerId").val();// 客户1
        if(oneCustomerId){
            $("#onePremiumRate_i").css("display","inline");
        }else{
            $("#onePremiumRate_i").css("display","none");
        }
        oneCustomerInfo = $module.customer.getCustomerById(oneCustomerId);//获取客户1信息
        $("#oneType").val(oneCustomerInfo.type);//客户类型(1内部,2外部)
        $("#oneIdvaluetype").val("1");//客户1存：1客户id,2公司id
        // 加载客户2
        if(oneCustomerInfo!=null && oneCustomerInfo.type=='2'){ //外部公司,拿供应商的关联公司
            $("#twoIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='twoCustomerId']")[0],null,oneCustomerInfo.mainId);
        }else{
            $("#twoIdvaluetype").val("1");//客户id
            $module.customer.loadSelectDataByMerchantId.call($("select[name='twoCustomerId']")[0],null,1,oneCustomerInfo.innerMerchantId,null);
        }
        var buOneCustomerId=oneCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#oneType").val()=="1"){
            buOneCustomerId = oneCustomerInfo.innerMerchantId;
            $("#one_i").css("display","inline");
        }else{
            $("#one_i").css("display","none");
        }
        // 重新加载客户1事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='oneBuId']")[0],null, buOneCustomerId,null,null);
    });
    // 客户2改变
    var twoCustomerId = null;// 客户2
    var twoCustomerInfo =null;
    $("#twoCustomerId").change(function () {
        twoCustomerId = $("#twoCustomerId").val();// 客户2
        if(twoCustomerId){
            $("#twoPremiumRate_i").css("display","inline");
        }else{
            $("#twoPremiumRate_i").css("display","none");
        }
        twoCustomerInfo = $module.customer.getCustomerById(twoCustomerId);//获取客户2信息
        $("#twoType").val(twoCustomerInfo!=null?twoCustomerInfo.type:"");//客户类型(1内部,2外部)
        // 加载客户3
        if(twoCustomerInfo!=null && twoCustomerInfo.type=='2'){ //外部公司,拿供应商的关联公司
            $("#threeIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='threeCustomerId']")[0],null,twoCustomerInfo.mainId);
        }else{
            if(twoCustomerInfo==null){ //若客户信息为空，则拿主数据id查询供应商的关联公司
                twoCustomerInfo = $module.supplier.getCustomerByMainMerchantId(oneCustomerInfo.mainId,twoCustomerId);//获取供应商信息
                $("#twoType").val("1");//客户类型(1内部,2外部)
            }
            $("#threeIdvaluetype").val("1");//客户id
            $module.customer.loadSelectDataByMerchantId.call($("select[name='threeCustomerId']")[0],null,1,twoCustomerInfo.innerMerchantId,null);
        }
        var buTwoCustomerId=twoCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#twoType").val()=="1"){
            buTwoCustomerId = twoCustomerInfo.innerMerchantId;
            $("#two_i").css("display","inline");
        }else{
            $("#two_i").css("display","none");
        }
        // 重新加载客户2事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='twoBuId']")[0],null, buTwoCustomerId,null,null);
    });
    // 客户3改变
    var threeCustomerId =null;// 客户3
    var threeCustomerInfo =null;
    $("#threeCustomerId").change(function () {
        threeCustomerId = $("#threeCustomerId").val();// 客户3
        if(threeCustomerId){
            $("#threePremiumRate_i").css("display","inline");
        }else{
            $("#threePremiumRate_i").css("display","none");
        }
        threeCustomerInfo = $module.customer.getCustomerById(threeCustomerId);//获取客户3信息
        $("#threeType").val(threeCustomerInfo!=null?threeCustomerInfo.type:"");//客户类型(1内部,2外部)
        // 加载客户4
        if(threeCustomerInfo!=null && threeCustomerInfo.type=='2'){ //外部公司,拿供应商的关联公司
            $("#fourIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='fourCustomerId']")[0],null,threeCustomerInfo.mainId);
        }else {
            if(threeCustomerInfo==null){ //若客户信息为空，则拿主数据id查询供应商的关联公司
                threeCustomerInfo = $module.supplier.getCustomerByMainMerchantId(twoCustomerInfo.mainId,threeCustomerId);//获取供应商信息
                $("#threeType").val("1");//客户类型(1内部,2外部)
            }
            $("#fourIdvaluetype").val("1");//客户id
            $module.customer.loadSelectDataByMerchantId.call($("select[name='fourCustomerId']")[0],null,1,threeCustomerInfo.innerMerchantId,null);
        }
        var buThreeCustomerId=threeCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#threeType").val()=="1"){
            buThreeCustomerId = threeCustomerInfo.innerMerchantId;
            $("#three_i").css("display","inline");
        }else{
            $("#three_i").css("display","none");
        }
        // 重新加载客户3事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='threeBuId']")[0],null, buThreeCustomerId,null,null);
    });
    // 客户4改变
    $("#fourCustomerId").change(function () {
        var fourCustomerId = $("#fourCustomerId").val();
        if(fourCustomerId){
            $("#fourPremiumRate_i").css("display","inline");
        }else{
            $("#fourPremiumRate_i").css("display","none");
        }
        var fourCustomerInfo = $module.customer.getCustomerById(fourCustomerId);//获取客户4信息
        $("#fourType").val(fourCustomerInfo!=null?fourCustomerInfo.type:"");//客户类型(1内部,2外部)
        if(fourCustomerInfo==null){
            $("#fourType").val("1");//客户类型(1内部,2外部)
        }
        var buFourCustomerId=fourCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#fourType").val()=="1"){
            buFourCustomerId =  fourCustomerInfo.innerMerchantId;
            $("#four_i").css("display","inline");
        }else{
            $("#four_i").css("display","none");
        }
        // 重新加载客户4事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='fourBuId']")[0],null,buFourCustomerId ,null,null);
    });
	// 保存
	function tradeLinkSave(){
        //必填项校验
        if(!$checker.verificationRequired()){
            return ;
        }
        var flag = true ;
		var linkName = $("#linkName").val();
		
		var startPointMerchantId = $("#startPointMerchantId").val();
		
		var startPointMerchantName = "" ;
		if(startPointMerchantId){
        	startPointMerchantName = $("#startPointMerchantId").find("option:selected").text();
		}
		
		var startPointBuId = $("#startPointBuId").val();
        var startPointBuName = "";
        
        if(startPointBuId){
        	startPointBuName = $("#startPointBuId").find("option:selected").text();
        }
        
        var startSupplierId = $("#startSupplierId").val();
        var startSupplierName = "" ;
        
        if(startSupplierId){
        	startSupplierName = $("#startSupplierId").find("option:selected").text();
        }
        
		var startPointPremiumRate = $("#startPointPremiumRate").val();
		
		var oneCustomerId = $("#oneCustomerId").val();
        var oneCustomerName = "" ;
        
        if(oneCustomerId){
        	oneCustomerName = $("#oneCustomerId").find("option:selected").text();
        }
        
		var oneBuId = $("#oneBuId").val();
        var oneBuName = "";
        
        if(oneBuId){
        	oneBuName = $("#oneBuId").find("option:selected").text();
        }
        
		var onePremiumRate = $("#onePremiumRate").val();
		
		var twoCustomerId = $("#twoCustomerId").val();
        var twoCustomerName = "";
        
        if(twoCustomerId){
        	twoCustomerName = $("#twoCustomerId").find("option:selected").text();
        }
        
		var twoBuId = $("#twoBuId").val();
        var twoBuName = "";
        
        if(twoBuId){
        	twoBuName = $("#twoBuId").find("option:selected").text();
        }
        
		var twoPremiumRate = $("#twoPremiumRate").val();
		
		var threeCustomerId = $("#threeCustomerId").val();
        var threeCustomerName = "";
        
        if(threeCustomerId){
        	threeCustomerName = $("#threeCustomerId").find("option:selected").text();
        }
        
		var threeBuId = $("#threeBuId").val();
        var threeBuName = "";
        
        if(threeBuId){
        	threeBuName = $("#threeBuId").find("option:selected").text();
        }
        
		var threePremiumRate = $("#threePremiumRate").val();
		
		var fourCustomerId = $("#fourCustomerId").val();
        var fourCustomerName = "";
        
        if(fourCustomerId){
        	fourCustomerName = $("#fourCustomerId").find("option:selected").text();
        }
        
		var fourBuId = $("#fourBuId").val();
        var fourBuName = "";
        
        if(fourBuId){
        	fourBuName = $("#fourBuId").find("option:selected").text();
        }
        
        var oneType = $("#oneType").val();
        var twoType = $("#twoType").val();
        var threeType = $("#threeType").val();
        var fourType = $("#fourType").val();
        var oneIdvaluetype = $("#oneIdvaluetype").val();
        var twoIdvaluetype = $("#twoIdvaluetype").val();
        var threeIdvaluetype = $("#threeIdvaluetype").val();
        var fourIdvaluetype = $("#fourIdvaluetype").val();

        if(oneCustomerId && oneType=="1" && !oneBuId){
            $custom.alert.error("客户1事业部不能为空");
            return;
        }
        if(twoCustomerId && twoType=="1" && !twoBuId){
            $custom.alert.error("客户2事业部不能为空");
            return;
        }
        if(threeCustomerId && threeType=="1" && !threeBuId){
            $custom.alert.error("客户3事业部不能为空");
            return;
        }
        if(fourCustomerId && fourType=="1" && !fourBuId){
            $custom.alert.error("客户4事业部不能为空");
            return;
        }
        if(oneCustomerId  && !onePremiumRate){
            $custom.alert.error("客户1销售加价比例不能为空");
            return;
        }
        if(twoCustomerId  && !twoPremiumRate){
            $custom.alert.error("客户2销售加价比例不能为空");
            return;
        }
        if(threeCustomerId  && !threePremiumRate){
            $custom.alert.error("客户3销售加价比例不能为空");
            return;
        }
        if(startPointPremiumRate && ! /^[0-9]+(.[0-9]+)?$/.test(startPointPremiumRate)){
            $custom.alert.error("请输入数值");
            flag = false ;
        }
        if(onePremiumRate && ! /^[0-9]+(.[0-9]+)?$/.test(onePremiumRate)){
            $custom.alert.error("请输入数值");
            flag = false ;
        }
        if(twoPremiumRate && ! /^[0-9]+(.[0-9]+)?$/.test(twoPremiumRate)){
            $custom.alert.error("请输入数值");
            flag = false ;
        }
        if(threePremiumRate && ! /^[0-9]+(.[0-9]+)?$/.test(threePremiumRate)){
            $custom.alert.error("请输入数值");
            flag = false ;
        }
		if(!flag){
			return ;
		}

        var json = {"linkName":linkName,"startPointMerchantId":startPointMerchantId, "startPointBuId":startPointBuId,
        		"startSupplierId":startSupplierId , "startSupplierName": startSupplierName,
            "startPointPremiumRate":startPointPremiumRate,"oneCustomerId":oneCustomerId,"oneBuId":oneBuId,
            "onePremiumRate":onePremiumRate ,  "twoCustomerId" : twoCustomerId,"twoBuId":twoBuId,"twoPremiumRate":twoPremiumRate,
            "threeCustomerId":threeCustomerId,"threeBuId":threeBuId,"threePremiumRate":threePremiumRate,"fourCustomerId":fourCustomerId,
            "fourBuId":fourBuId,"startPointMerchantName":startPointMerchantName,"startPointBuName":startPointBuName,
            "oneCustomerName":oneCustomerName,"oneBuName":oneBuName,"twoCustomerName":twoCustomerName,
            "twoBuName":twoBuName,"threeCustomerName":threeCustomerName,"threeBuName":threeBuName,
            "fourCustomerName":fourCustomerName,"fourBuName":fourBuName,"oneType":oneType,"twoType":twoType,
            "threeType":threeType,"fourType":fourType,"oneIdvaluetype":oneIdvaluetype,"twoIdvaluetype":twoIdvaluetype,
            "threeIdvaluetype":threeIdvaluetype,"fourIdvaluetype":fourIdvaluetype} ;
        var jsonStr= JSON.stringify(json);
		$custom.request.ajax(serverAddr+"/tradeLinkConfig/saveTradeLinkConfig.asyn", {"json":jsonStr} , function(data){
            if(data.state==200){
                $custom.alert.success("保存成功");
                modalHide() ;
                setTimeout("dh_list();" , 1000) ;
            }else{
                if(!!data.expMessage){
                    $custom.alert.error(data.expMessage);
                }else{
                    $custom.alert.error(data.message);
                }
            }
		});
	};

	function tradeLinkCancel(){
		$custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
			modalHide() ;
        });
	}

	function modalHide(){
		$('#invoice-modal').modal('hide');
		$("#linkName").val("");
		$("#startPointMerchantId").val("");
		$("#startPointBuId").val("");
		$("#startPointSupplierId").val("");
		$("#startPointPremiumRate").val("");
		$("#oneCustomerId").val("");
		$("#oneBuId").val("");
		$("#onePremiumRate").val("");
		$("#twoCustomerId").val("");
		$("#twoBuId").val("");
		$("#twoPremiumRate").val("");
		$("#threeCustomerId").val("");
		$("#threeBuId").val("");
		$("#threePremiumRate").val("");
		$("#fourCustomerId").val("");
		$("#fourBuId").val("");
	}

</script>