<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="customs-modal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="custom-width-modalLabel" aria-hidden="true"
                 style="display: none;position:fixed;top:30px;bottom:auto;height:100%;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 600px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">选择清关单证导出模板</h5>
                        </div>
                        <div class="modal-body">
                        	<div id="inTempalteDiv">
	                            <form id="in-popup-goods-form">
	                                <div class="edit_box mt20">
										查询该入库仓库关联采购清关单证模板有多个，请选择导出模板：
									</div>
	                            </form>
	                            <div class="form-row mt20">
	                                <table id="inTemp-detail" class="table table-striped" cellspacing="0" width="100%">
	                                    <thead>
	                                    <tr>
	                                        <th style="text-align: center;">
                                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                    <input type="checkbox" name="keeperMerchantGroup-checkable"
                                                           class="group-checkable"/>
                                                </label>
                                            </th>
	                                        <th style="text-align: center;">清关模板编码</th>
	                                        <th style="text-align: center;">清关模板名称</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody id="model-tbody1"></tbody>
	                                </table>
	                            </div>
                            </div>
                            <div id="outTempalteDiv">
	                            <form id="out-popup-goods-form">
	                                <div class="edit_box mt20">
										查询该出库仓库关联采购清关单证模板有多个，请选择导出模板：
									</div>
	                            </form>
	                            <div class="form-row mt20">
	                                <table id="outTemp-detail" class="table table-striped" cellspacing="0" width="100%">
	                                    <thead>
	                                    <tr>
	                                        <th style="text-align: center;"></th>
	                                        <th style="text-align: center;">清关模板编码</th>
	                                        <th style="text-align: center;">清关模板名称</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody id="model-tbody2"></tbody>
	                                </table>
	                            </div>
                            </div>
                        </div>
                        <div class="form-row ml20" style="padding-bottom: 20px;">
                            <div class="col-12">
                                <div class="row">
                                    <div class="col-4"></div>
                                    <div class="col-2">
                                        <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm"
                                                onclick='$m9037.save()'>确认
                                        </button>
                                    </div>
                                    <div class="col-2">
                                        <button type="button"
                                                class="btn btn-light waves-effect waves-light btn_default btn-sm"
                                                onclick='$m9037.hide();'>取消
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </div>
</div>
<!-- end col -->
</div>
<script type="text/javascript">
    var $m9037 = {
    	//isSameArea - 是否同关区,	outDepotId - 出仓id,	 inDepotId - 入仓id,	outCustomsId - 出仓关区, inCustomsId - 入仓同关区
        init: function (isSameArea,outDepotId,inDepotId,outCustomsId,inCustomsId,downloadUrl, orderId) {
            $m9037.params.downloadUrl = downloadUrl;
            $m9037.params.orderId = orderId;

        	var data = {};
            if(isSameArea){//是否同关区
	            data["isSameArea"]=isSameArea;            	
            }
            if(outDepotId){//出仓id
	            data["outDepotId"]=outDepotId;            	
            }
            if(inDepotId){//入仓id
	            data["inDepotId"]=inDepotId;            	
            }
            if(outCustomsId){//出仓关区
	            data["outCustomsId"]=outCustomsId;            	
            }
            if(inCustomsId){//入仓同关区
	            data["inCustomsId"]=inCustomsId;            	
            }
            var form = {};
            var json = JSON.stringify(data);
            form['json'] = json;
            $("#model-tbody1").empty();
            $("#model-tbody2").empty();
            $custom.request.ajax(serverAddr + '/customsFileConfig/getExportTemplate.asyn', form, function (result) {
                if (result.state == 200) {
                    var list = result.data;
                    if (list) {
                        if(list.code == "01"){                           
                            $custom.alert.error("出仓/入仓仓库不存在配置的清关资料模板");
                            return ;
                        }
                        //出仓、入仓只有一个模板，直接下载
                        if(list.code == "00"){
                        	var json = {};
                        	json.id = $m9037.params.orderId;
                        	if(list.inList && list.inList.length > 0){
                            	json.inFileTempIds = list.inList[0].fileTempId+"";
                        	}else{
                        		json.inFileTempIds = "";
                        	}
                        	if(list.outList && list.outList.length > 0){
                        		json.outFileTempIds = list.outList[0].fileTempId+"";
                        	}else{
                        		json.outFileTempIds = "";
                        	}

                            var jsonStr = JSON.stringify(json);
                            var form = $("<form></form>").attr("action", $m9037.params.downloadUrl).attr("method", "post").attr("target","_blank");
                            form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
                            form.appendTo('body').submit().remove();
                            return ;
                        }
                        $($m9037.params.modalId).modal({backdrop: 'static', keyboard: false});
                        var inhtml = "";
                        var inList = list.inList;
                        if(inList && inList.length > 0){
                            $(inList).each(function (i, m) {
                            	 var fileTempId = m.fileTempId;
                                 var fileTempCode = m.fileTempCode;
                                 var fileTempName = m.fileTempName;
                                 inhtml += '<tr>'
                                     + '<td class="temp-checkbox"><input type="checkbox" value="' + fileTempId + '" name="inFileTempId"></td>'
                                     + '<td>' + fileTempCode + '</td>'
                                     + '<td>' + fileTempName + '</td>'
                                     + '</tr>';

                            });
                        	$("#inTempalteDiv").css("display","block");
                        	$("#inTemp-detail").append(inhtml);
                        }else{
                        	$("#inTempalteDiv").css("display","none");
                        }
                       
                        var outhtml = "";
                        var outList = list.outList;
                        if(outList && outList.length > 0){
                        	$(outList).each(function (i, m) {
                           	 var fileTempId = m.fileTempId;
                                var fileTempCode = m.fileTempCode;
                                var fileTempName = m.fileTempName;
                                outhtml += '<tr>'
                                    + '<td class="temp-checkbox"><input type="checkbox" value="' + fileTempId + '" name="outFileTempId"></td>'
                                    + '<td>' + fileTempCode + '</td>'
                                    + '<td>' + fileTempName + '</td>'
                                    + '</tr>';

                           });
                        	$("#outTempalteDiv").css("display","block");
	                        $("#outTemp-detail").append(outhtml);
                        } else{
                        	$("#outTempalteDiv").css("display","none");
                        }                       
                        
                    }
                } else {
                    var message = result.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("没有关联模板！");
                    }
                }
            }); 
        },
        hide: function () {
        	$("#model-tbody").empty();
            $($m9037.params.modalId).modal("hide");
        },
        save: function () {
            var inFileTempIdArr = [];
            var outFileTempIdArr = [];
            $("input[name='outFileTempId']").each(function() {
                if ($(this).prop("checked")) {
                    outFileTempIdArr.push($(this).attr('value'))
                }

            });

            $("input[name='inFileTempId']").each(function() {
                if ($(this).prop("checked")) {
                    inFileTempIdArr.push($(this).attr('value'))
                }
            });

            if (inFileTempIdArr.length == 0 && outFileTempIdArr.length == 0) {
                $custom.alert.warningText("请至少选择一个模板！");
                return;
            }

            var inFileTempIds = inFileTempIdArr.join(",");
            var outFileTempIds = outFileTempIdArr.join(",");

            var json = {};
            json.id = $m9037.params.orderId;
            json.inFileTempIds = inFileTempIds;
            json.outFileTempIds = outFileTempIds;
            var jsonStr = JSON.stringify(json);

            $($m9037.params.modalId).modal("hide");
            $('.modal-backdrop').remove();//去掉遮罩层

            var form = $("<form></form>").attr("action", $m9037.params.downloadUrl).attr("method", "post").attr("target","_blank");
            form.append($("<input></input>").attr("type", "hidden").attr("name", "json").attr("value", jsonStr));
            form.appendTo('body').submit().remove();
        },
        params: {
            modalId: '#customs-modal',
            downloadUrl:"",
            orderId:""
        }
    };

</script>