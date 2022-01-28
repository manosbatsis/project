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
    <div id="signup-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              <span class="header-title" >编辑交易链路</span>
          </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="edit-from">
                            <!-- 自定义参数  -->
                      <div class="form-group">
                          <div class="col-12">
                              <input class="input_msg" id="id" name="id"  type="hidden" >
                              <label ><i class="red">*</i>交易链路名称&nbsp&nbsp : </label>
                              <input type="text" class="input_msg" id="editLinkName" name="editLinkName" style="width: 202px;">
                          </div>
                      </div>
                      <div class="form-group">
                          <div class="col-12">
                              <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<i class="red">*</i>起点公司&nbsp&nbsp : </label>
                              <select class="input_msg" id="editStartPointMerchantId" name="editStartPointMerchantId"style="width: 202px;"><option value=''>请选择</option></select>
                              <label>&nbsp&nbsp&nbsp<i class="red">*</i>起点事业部: </label>
                              <select class="input_msg" name="editStartPointBuId" id="editStartPointBuId"style="width: 202px;" parsley-trigger="change">
                                  <option value=''>请选择</option>
                              </select>
                          </div>
                      </div>
                      <div class="form-group">
                        <div class="col-12">
                            <label ><i class="red">*</i>销售加价比例% : </label>
                            <input type="text" class="input_msg" id="editStartPointPremiumRate" name="editStartPointPremiumRate" style="width: 202px;">
                        	<label>&nbsp&nbsp&nbsp<i class="red">*</i>起点供应商: </label>
                            <select class="input_msg" name="editStartPointSupplierId" id="editStartPointSupplierId"style="width: 202px;" parsley-trigger="change" >
                                <option value=''>请选择</option>
                            </select>
                        </div>
                      </div>
                      <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="editOneType" name="editOneType"  type="hidden">
                                <input class="input_msg" id="editOneIdvaluetype" name="editOneIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户1&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="editOneCustomerId" name="editOneCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="editOne_i">*</i>客户1事业部: </label>
                                <select class="input_msg" name="editOneBuId" id="editOneBuId"style="width: 202px;" parsley-trigger="change">
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="editOnePremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="editOnePremiumRate" name="editOnePremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="editTwoType" name="editTwoType"  type="hidden">
                                <input class="input_msg" id="editTwoIdvaluetype" name="editTwoIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户2&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="editTwoCustomerId" name="editTwoCustomerId"style="width: 202px;" >
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="editTwo_i">*</i>客户2事业部: </label>
                                <select class="input_msg" name="editTwoBuId" id="editTwoBuId"style="width: 202px;" parsley-trigger="change" >
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="editTwoPremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="editTwoPremiumRate" name="editTwoPremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="editThreeType" name="threeType"  type="hidden">
                                <input class="input_msg" id="editThreeIdvaluetype" name="editThreeIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户3&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="editThreeCustomerId" name="editThreeCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="editThree_i">*</i>客户3事业部: </label>
                                <select class="input_msg" name="editThreeBuId" id="editThreeBuId"style="width: 202px;" parsley-trigger="change" >
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-12">
                                <label ><i class="red" style="display:none;" id="editThreePremiumRate_i">*</i>销售加价比例% : </label>
                                <input type="text" class="input_msg" id="editThreePremiumRate" name="editThreePremiumRate" style="width: 202px;">
                            </div>
                        </div>
                        <div style="color: lightgrey">------------------------------------------------------------------------------------------------------------</div>
                        <div class="form-group">
                            <div class="col-12">
                                <input class="input_msg" id="editFourType" name="editFourType"  type="hidden">
                                <input class="input_msg" id="editFourIdvaluetype" name="editFourIdvaluetype"  type="hidden">
                                <label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp客户4&nbsp&nbsp : &nbsp&nbsp&nbsp&nbsp&nbsp</label>
                                <select class="input_msg" id="editFourCustomerId" name="editFourCustomerId"style="width: 202px;">
                                    <option value=''>请选择</option>
                                </select>
                                <label>&nbsp&nbsp&nbsp<i class="red" style="display:none;" id="four_i">*</i>客户4事业部: </label>
                                <select class="input_msg" name="editFourBuId" id="editFourBuId"style="width: 202px;" parsley-trigger="change">
                                    <option value=''>请选择</option>
                                </select>
                            </div>
                        </div>
                    </form>
                    <br/><br/><br/>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick="editTradeLinkCancel();">关闭</button>
                          <button class="btn btn-info waves-effect waves-light fr btn_default delayedBtn" type="button" onclick="tradeLinkEdit();">保存</button>
                      </div>
                  </div>
                    <input type="hidden" value="" id="type">
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m18002={
        init:function(data,type){
        	$("#type").val(type);
        	$('#edit-from')[0].reset() ;
        	$('#edit-from').find("select").val("") ;
        	$('#edit-from').find("select").html("<option value=''>请选择</option>") ;
        	
            $("#id").val(data.id);
            $("#editLinkName").val(data.linkName);

            //加载起点公司
            var merchantList = $module.merchantAll.listData();
            var merchantOption = '<option value="">请选择</option>';
            merchantList.forEach(function(o,i){
                if (data.startPointMerchantId == o.value) {
                    merchantOption = merchantOption+'<option value="'+ o.value+'" selected>'+o.label+'</option>';
                }else{
                    merchantOption = merchantOption+'<option value="'+ o.value+'">'+o.label+'</option>';
                }
            });
            
            $("#editStartPointMerchantId").empty();
            $("#editStartPointMerchantId").append(merchantOption);
            //加载起点事业部
            $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editStartPointBuId']")[0],data.startPointBuId, $("#editStartPointMerchantId").val());
            
            $module.supplier.loadSelectDataByMerchantId.call($("select[name='editStartPointSupplierId']")[0], data.startSupplierId, $("#editStartPointMerchantId").val());
            
            $("#editStartPointPremiumRate").val(data.startPointPremiumRate);

            $("#editOneType").val(data.oneType);
            $("#editOneIdvaluetype").val(data.oneIdvaluetype);

            var oneCustomerInfo = $module.customer.getCustomerById(data.oneCustomerId);//获取客户1信息
            var buOneCusId=data.oneCustomerId;
            if($("#editOneIdvaluetype").val()=="2"){ //公司id
                $module.supplier.loadSelectDataByMainId.call($("select[name='editOneCustomerId']")[0],data.oneCustomerId,$("#editStartPointMerchantId").val());
            }else{
                if(data.oneType=="1") {// 内部
                    buOneCusId = oneCustomerInfo.innerMerchantId;
	                $module.customer.loadSelectDataByMerchantId.call($("select[name='editTwoCustomerId']")[0], data.twoCustomerId, 1, buOneCusId);
                }
                $module.customer.loadSelectDataByMerchantId.call($("select[name='editOneCustomerId']")[0],data.oneCustomerId,1,$("#editStartPointMerchantId").val(),null);
                
            }
            $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editOneBuId']")[0],data.oneBuId, buOneCusId,null,null);


            $("#editOnePremiumRate").val(data.onePremiumRate);
            $("#editTwoType").val(data.twoType);
            $("#editTwoIdvaluetype").val(data.twoIdvaluetype);

            var twoCustomerInfo = $module.customer.getCustomerById(data.twoCustomerId);//获取客户2信息
            var buTwoCusId=data.twoCustomerId;
            if(buTwoCusId){
                if($("#editTwoIdvaluetype").val()=="2"){ //公司id
                    $module.supplier.loadSelectDataByMainId.call($("select[name='editTwoCustomerId']")[0],data.twoCustomerId,oneCustomerInfo.mainId);
                }else{
                    if(data.twoType=="1"){// 内部
                        buTwoCusId = twoCustomerInfo.innerMerchantId;
	                    $module.customer.loadSelectDataByMerchantId.call($("select[name='editThreeCustomerId']")[0], data.threeCustomerId, 1, buTwoCusId);
                    }
                    $module.customer.loadSelectDataByMerchantId.call($("select[name='editTwoCustomerId']")[0],data.twoCustomerId,1,null,null);
                    
                }
                $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editTwoBuId']")[0],data.twoBuId, buTwoCusId,null,null);
            }

            $("#editTwoPremiumRate").val(data.twoPremiumRate);
            $("#editThreeType").val(data.threeType);
            $("#editThreeIdvaluetype").val(data.threeIdvaluetype);

            var threeCustomerInfo = $module.customer.getCustomerById(data.threeCustomerId);//获取客户3信息
            var buThreeCusId= data.threeCustomerId;
            if(buThreeCusId){
                if($("#editThreeIdvaluetype").val()=="2"){ //公司id
                    $module.supplier.loadSelectDataByMainId.call($("select[name='editThreeCustomerId']")[0],data.threeCustomerId,twoCustomerInfo.mainId);
                }else{
                    if(data.threeType=="1") {// 内部
                        buThreeCusId = threeCustomerInfo.innerMerchantId;
                        $module.customer.loadSelectDataByMerchantId.call($("select[name='editFourCustomerId']")[0], data.fourCustomerId, 1, buThreeCusId);
                    }
                    $module.customer.loadSelectDataByMerchantId.call($("select[name='editThreeCustomerId']")[0],data.threeCustomerId,1,null,null);
                }
                $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editThreeBuId']")[0],data.threeBuId, buThreeCusId,null,null);
            }

            $("#editThreePremiumRate").val(data.threePremiumRate);
            $("#editFourType").val(data.fourType);
            $("#editFourIdvaluetype").val(data.fourIdvaluetype);

            var fourCustomerInfo = $module.customer.getCustomerById(data.fourCustomerId);//获取客户4信息
            var buFourCusId=data.fourCustomerId;
            if(buFourCusId){
                if($("#editFourIdvaluetype").val()=="2"){ //公司id
                    $module.supplier.loadSelectDataByMainId.call($("select[name='editFourCustomerId']")[0],data.fourCustomerId,threeCustomerInfo.mainId);
                }else{
                    if(data.fourType=="1") {// 内部
                        buFourCusId = fourCustomerInfo.innerMerchantId;
                    }
                    $module.customer.loadSelectDataByMerchantId.call($("select[name='editFourCustomerId']")[0],data.fourCustomerId,1,null,null);
                }
                $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editFourBuId']")[0],data.fourBuId,buFourCusId ,null,null);
            }

            if(data.oneType=="1"){
                $("#editOne_i").css("display","inline");
            }

            if(data.twoType=="1"){
                $("#editTwo_i").css("display","inline");
            }
            if(data.threeType=="1"){
                $("#editThree_i").css("display","inline");
            }
            if(data.fourType=="1"){
                $("#editFour_i").css("display","inline");
            }
            $("#signup-modal").modal({backdrop: 'static', keyboard: false});
        },
        params:{
            modalId:'#signup-modal'
        }
    };

    // 起点公司改变
    $("#editStartPointMerchantId").change(function () {
        var startPointMerchantId = $("#editStartPointMerchantId").val();
        // 重新加载起点事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editStartPointBuId']")[0],null, startPointMerchantId,null,null);
        // 重新加载客户1
        $module.customer.loadSelectDataByMerchantId.call($("select[name='editOneCustomerId']")[0],null,1,startPointMerchantId,null);
        
        $module.supplier.loadSelectDataByMerchantId.call($("select[name='editStartPointSupplierId']")[0],null,startPointMerchantId);
    });
    // 客户1改变
    var oneCustomerInfo = null;//客户1信息
    var oneCustomerId = null;// 客户1Id
    $("#editOneCustomerId").change(function () {
        oneCustomerId = $("#editOneCustomerId").val();// 客户1
        if(oneCustomerId){
            $("#editOnePremiumRate_i").css("display","inline");
        }else{
            $("#editOnePremiumRate_i").css("display","none");
        }
        oneCustomerInfo = $module.customer.getCustomerById(oneCustomerId);//获取客户1信息
        $("#editOneType").val(oneCustomerInfo.type);//客户类型(1内部,2外部)
        $("#editOneIdvaluetype").val("1");//客户1存：1客户id,2公司id
        // 加载客户2
        if(oneCustomerInfo!=null && oneCustomerInfo.type=='2'){ //外部公司,拿供应商
        	$("#editTwoIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='editTwoCustomerId']")[0],null,oneCustomerInfo.mainId);
        }else{
        	$("#editTwoIdvaluetype").val("1");//公司id
            $module.customer.loadSelectDataByMerchantId.call($("select[name='editTwoCustomerId']")[0],null,1,oneCustomerInfo.innerMerchantId,null);
        }
        var buOneCustomerId=oneCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#editOneType").val()=="1"){
            buOneCustomerId = oneCustomerInfo.innerMerchantId;
            $("#editOne_i").css("display","inline");
        }else{
            $("#editOne_i").css("display","none");
        }
        // 重新加载客户1事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editOneBuId']")[0],null, buOneCustomerId,null,null);
    });
    // 客户2改变
    var twoCustomerId = null;// 客户2
    var twoCustomerInfo =null;
    $("#editTwoCustomerId").change(function () {
        twoCustomerId = $("#editTwoCustomerId").val();// 客户2
        if(twoCustomerId){
            $("#editTwoPremiumRate_i").css("display","inline");
        }else{
            $("#editTwoPremiumRate_i").css("display","none");
        }
        twoCustomerInfo = $module.customer.getCustomerById(twoCustomerId);//获取客户2信息
        // 加载客户3
        if(twoCustomerInfo!=null && twoCustomerInfo.type=='2'){ //外部公司,拿供应商
            $("#editTwoType").val(twoCustomerInfo.type);//客户类型(1内部,2外部)
            $("#editThreeIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='editThreeCustomerId']")[0],null,twoCustomerInfo.mainId);
        }else{
            $("#editTwoType").val("1");//客户类型(1内部,2外部)
            $("#editThreeIdvaluetype").val("1");//客户id
            if(twoCustomerInfo==null){ //若客户信息为空，则拿主数据id查询供应商
                twoCustomerInfo = $module.supplier.getCustomerByMainMerchantId(oneCustomerInfo.mainId,twoCustomerId);//获取供应商信息
                $("#editThreeIdvaluetype").val("2");//公司id
            }
            $module.customer.loadSelectDataByMerchantId.call($("select[name='editThreeCustomerId']")[0],null,1,twoCustomerInfo.innerMerchantId,null);
        }
        var buTwoCustomerId=twoCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#editTwoType").val()=="1"){
            buTwoCustomerId = twoCustomerInfo.innerMerchantId;
            $("#editTwo_i").css("display","inline");
        }else{
            $("#editTwo_i").css("display","none");
        }
        // 重新加载客户2事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editTwoBuId']")[0],null, buTwoCustomerId,null,null);
    });
    // 客户3改变
    var threeCustomerId =null;// 客户3
    var threeCustomerInfo =null;
    $("#editThreeCustomerId").change(function () {
        threeCustomerId = $("#editThreeCustomerId").val();// 客户3
        if(threeCustomerId){
            $("#editThreePremiumRate_i").css("display","inline");
        }else{
            $("#editThreePremiumRate_i").css("display","none");
        }
        threeCustomerInfo = $module.customer.getCustomerById(threeCustomerId);//获取客户3信息
        // 加载客户4
        if(threeCustomerInfo!=null && threeCustomerInfo.type=='2'){ //外部公司,拿供应商
            $("#editThreeType").val(threeCustomerInfo.type);//客户类型(1内部,2外部)
            $("#editFourIdvaluetype").val("2");//公司id
            $module.supplier.loadSelectDataByMainId.call($("select[name='editFourCustomerId']")[0],null,threeCustomerInfo.mainId);
        }else {
            $("#editThreeType").val("1");//客户类型(1内部,2外部)
            $("#editFourIdvaluetype").val("1");//客户id
            if(threeCustomerInfo==null){ //若客户信息为空，则拿主数据id查询供应商
                threeCustomerInfo = $module.supplier.getCustomerByMainMerchantId(twoCustomerInfo.mainId,threeCustomerId);//获取供应商信息
                $("#editFourIdvaluetype").val("2");//公司id
            }
            $module.customer.loadSelectDataByMerchantId.call($("select[name='editFourCustomerId']")[0],null,1,threeCustomerInfo.innerMerchantId,null);
        }
        var buThreeCustomerId=threeCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#editThreeType").val()=="1"){
            buThreeCustomerId = threeCustomerInfo.innerMerchantId;
            $("#editThree_i").css("display","inline");
        }else{
            $("#editThree_i").css("display","none");
        }
        // 重新加载客户3事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editThreeBuId']")[0],null, buThreeCustomerId,null,null);
    });
    // 客户4改变
    $("#editFourCustomerId").change(function () {
        var fourCustomerId = $("#editFourCustomerId").val();
        if(fourCustomerId){
            $("#editFourPremiumRate_i").css("display","inline");
        }else{
            $("#editFourPremiumRate_i").css("display","none");
        }
        var fourCustomerInfo = $module.customer.getCustomerById(fourCustomerId);//获取客户4信息
        if(fourCustomerInfo!=null && fourCustomerInfo.type=='2') {
            $("#editFourType").val(threeCustomerInfo.type);//客户类型(1内部,2外部)
        }else{
            $("#editFourType").val("1");//客户类型(1内部,2外部)
            if(fourCustomerInfo==null){
                $("#editFourIdvaluetype").val("2");//公司id
            }
        }
        var buFourCustomerId=fourCustomerId;
        // 该级客户是内部公司,则事业部必填
        if($("#editFourType").val()=="1"){
            buFourCustomerId =  fourCustomerInfo.innerMerchantId;
            $("#editFour_i").css("display","inline");
        }else{
            $("#editFour_i").css("display","none");
        }
        // 重新加载客户4事业部
        $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='editFourBuId']")[0],null,buFourCustomerId ,null,null);
    });


	// 保存编辑
	function tradeLinkEdit(){
        var flag = true ;
        var id = $("#id").val();
		var linkName = $("#editLinkName").val();
		
		var startPointMerchantId = $("#editStartPointMerchantId").val();
        var startPointMerchantName = "";
        
        if(startPointMerchantId){
        	startPointMerchantName = $("#editStartPointMerchantId").find("option:selected").text();
        }
        
		var startPointBuId = $("#editStartPointBuId").val();
        var startPointBuName = "";
        
        if(startPointBuId){
        	startPointBuName = $("#editStartPointBuId").find("option:selected").text();
        }
        
        var startSupplierId = $("#editStartPointSupplierId").val();
        var startSupplierName = "";
        
        if(startSupplierId){
        	startSupplierName = $("#editStartPointSupplierId").find("option:selected").text();
        }
        
		var startPointPremiumRate = $("#editStartPointPremiumRate").val();
		
		var oneCustomerId = $("#editOneCustomerId").val();
        var oneCustomerName = "";
        
        if(oneCustomerId){
        	oneCustomerName = $("#editOneCustomerId").find("option:selected").text();
        }
        
		var oneBuId = $("#editOneBuId").val();
        var oneBuName = "";
        
        if(oneBuId){
        	oneBuName = $("#editOneBuId").find("option:selected").text();
        }
        
		var onePremiumRate = $("#editOnePremiumRate").val();
		
		var twoCustomerId = $("#editTwoCustomerId").val();
        var twoCustomerName = "";
        
        if(twoCustomerId){
        	twoCustomerName = $("#editTwoCustomerId").find("option:selected").text();
        }
        
		var twoBuId = $("#editTwoBuId").val();
        var twoBuName = "" ;
        
        if(twoBuId){
        	twoBuName = $("#editTwoBuId").find("option:selected").text();
        }
        
		var twoPremiumRate = $("#editTwoPremiumRate").val();
		
		var threeCustomerId = $("#editThreeCustomerId").val();
        var threeCustomerName = "";
        
        if(threeCustomerId){
        	threeCustomerName = $("#editThreeCustomerId").find("option:selected").text();
        }
        
		var threeBuId = $("#editThreeBuId").val();
        var threeBuName = "";
        
        if(threeBuId){
        	threeBuName = $("#editThreeBuId").find("option:selected").text();
        }
        
		var threePremiumRate = $("#editThreePremiumRate").val();
		
		var fourCustomerId = $("#editFourCustomerId").val();
        var fourCustomerName = "";
        
        if(fourCustomerId){
        	fourCustomerName = $("#editFourCustomerId").find("option:selected").text();
        }
        
		var fourBuId = $("#editFourBuId").val();
        var fourBuName = "";
        
        if(fourBuId){
        	fourBuName = $("#editFourBuId").find("option:selected").text();
        }

        var oneType = $("#editOneType").val();
        var twoType = $("#editTwoType").val();
        var threeType = $("#editThreeType").val();
        var fourType = $("#editFourType").val();
        var oneIdvaluetype = $("#editOneIdvaluetype").val();
        var twoIdvaluetype = $("#editTwoIdvaluetype").val();
        var threeIdvaluetype = $("#editThreeIdvaluetype").val();
        var fourIdvaluetype = $("#editFourIdvaluetype").val();
        //必填项校验
        if(!linkName){
            $custom.alert.error("链路名称不能为空");
            return;
        }
        
        if(!startPointMerchantId){
            $custom.alert.error("起点公司不能为空");
            return;
        }
        
        if(!startPointBuId){
            $custom.alert.error("起点事业部不能为空");
            return;
        }

        if(!startSupplierId){
            $custom.alert.error("起点供应商不能为空");
            return;
        }
        
        if(!startPointPremiumRate){
            $custom.alert.error("销售加价比例不能为空");
            return;
        }
        
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
        var type=$("#type").val();
        var url="";
        if(type=="1"){
            url="/tradeLinkConfig/modifyTradeLinkConfig.asyn";
        }else{
            url="/tradeLinkConfig/saveTradeLinkConfig.asyn";
            id="";
        }
        var json = {"id":id,"linkName":linkName,"startPointMerchantId":startPointMerchantId, "startPointBuId":startPointBuId,
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

        debugger
        console.log('type===='+type);
        console.log('url===='+url);
        debugger
		$custom.request.ajax(serverAddr+url,  {"json":jsonStr} , function(data){
            if(data.state==200){
                $custom.alert.success("修改成功");
                editModalHide() ;
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

	function editTradeLinkCancel(){
		$custom.cancelReqrCheck("你将关闭该界面，数据不会被保存，是否继续关闭？",function(){
            editModalHide() ;
        });
	}

	function editModalHide(){
		$('#signup-modal').modal('hide');
		$("#linkName").val("");
		$("#startPointMerchantId").val("");
		$("#startPointBuId").val("");
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