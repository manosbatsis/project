<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<div class="row">
    <div class="col-12">
        <div>
            <!-- Signup modal content -->
            <div id="signup-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content" style="width: 800px;text-align: center;">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h5 class="modal-title" id="myLargeModalLabel">核销</h5>
                        </div>
                        <div class="modal-body">
                            <form id="popup-goods-form">
                                <div class="form-row mt10 ml15">
                                    <div class="col-6">
                                        <div class="row">
                                            <div class="fr">应收账单号：
                                                <span id="code"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="row">
                                            <div class="fr">客户：
                                                <span id="customerName"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row mt10 ml15">
                                    <div class="col-6">
                                        <div class="row">
                                            <div class="fr">应收金额：
                                                <span id="currency"></span>&nbsp;&nbsp;<span id="receivablePrice"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="row">
                                            <div class="fr">发票号码：
                                                <span id="invoiceNo"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <div class="form-row mt20">
                                <table id="verify-table-detail" class="table table-striped" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th><i class="red">*</i>本期收款日期</th>
                                        <th><i class="red">*</i>本期核销金额</th>
                                        <th>收款流水号</th>
                                        <th>核销人</th>
                                        <th>核销时间</th>
                                    </tr>
                                    </thead>
                                    <tbody id="verify-tbody">
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-row mt20 attachDetail">
                                <div class='col-2'>
                                    <div class="info-box">
                                        <button type="button" class="btn btn-gradient btn-file"
                                                id="btn-file">附件上传</button>

                                        <form enctype="multipart/form-data" id="form-upload">
                                            <input type="file" name="file" id="file" class="btn-hidden file-import">
                                        </form>
                                    </div>
                                </div>

                            </div>
                            <div class="mt20 info_box" id="attachmentContent" >
                            </div>
                            <div class="form-row m-t-50">
                                <div class="col-12">
                                    <div class="row">
                                        <div class="col-4"></div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-info waves-effect waves-light fr btn-sm" onclick='$m13004.save()'>确认</button>
                                        </div>
                                        <div class="col-2">
                                            <button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick='$m13004.hide();'>取消</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal-dialog -->
            </div><!-- /.modal -->
        </div>
    </div><!-- end col -->
</div>
<script type="text/javascript">



    var $m13004={

        init:function(id, code, receivablePrice, invoiceNo, customerName, currency){
            $derpTimer.lessThanNowDateTimer("#receiveDate", "yyyy-MM-dd") ;
            //初始化plupload
            $m13004.params.uploader = new plupload.Uploader({ //实例化一个plupload上传对象
                browse_button : 'btn-file',
                url : serverAddr + '/attachment/uploadFiles.asyn?token='+token + "&code=" + code,
                max_file_size: '20MB',//限制为25MB
                multi_selection:true,
                filters: {mime_types :[{ title : "图片文件", extensions : "jpg,jpeg,png,bmp,JPG,JPEG,PNG,BMP" },
                        { title : "RAR压缩文件", extensions : "zip,rar,ZIP,RAR" },
                        { title : "文档文件", extensions : "docx,xlsx,doc,xls,txt,pdf,eml,DOCX,XLSX,DOC,XLS,TXT,PDF,EML" }]},//文件限制
                init : {
                    PostInit: function (a) {
                    },
                    FilesAdded: function (uder, files) {
                        var html = '' ;
                        //单个文件进度显示
                        $(files).each(function(index , file){
                            html += '<div style="text-align: left;" id="item'+ file.id +'">' +
                                '<span>' + file.name +
                                '</span>' +
                                '<span>' +
                                '<a href="javascript:removeFile(\''+ file.id +'\')" id="a'+file.id+'" style="padding-left:8px;">取消</a>' +
                                '</span>' +
                                '</div>';
                        }) ;

                        $("#attachmentContent").append(html) ;
                    },
                    UploadProgress: function (uder, file) {
                    },
                    UploadFile: function (uder , file) {
                        $("#error" + file.id).remove();
                        $("#a" + file.id).attr("href","#") ;
                        $("#a" + file.id).text("上传中...") ;
                    },
                    FileUploaded: function (uder, file, resObject) {

                        var result = resObject.response;
                        result = JSON.parse(result) ;
                        if( result.state == 200 ){
                            if(result.data && result.data.code == 200){
                                removeFile(file.id) ;
                                // $attachTable.fn.reload();

                                return ;
                            }else{
                                $custom.alert.error(file.name + "上传失败");
                            }
                        }else{
                            $custom.alert.error(result.message);
                        }

                        uploadErrorDisplay(file.id) ;

                    },
                    Error: function (uder, res) {

                        if(res.code == -600){
                            $custom.alert.error(res.file.name + "超过20M");
                            return ;
                        }

                        uploadErrorDisplay(res.file.id) ;
                        $custom.alert.error(res.file.name + "上传失败");
                    }
                }

            });
            $m13004.params.uploader.init(); //初始化

            $m13004.params.id = id;
            $m13004.params.receivablePrice = receivablePrice;
            $m13004.params.currency = currency;
            $("#code").html(code);
            if (invoiceNo) {
                $("#invoiceNo").html(invoiceNo);
            }
            $("#customerName").html(customerName);
            $("#receivablePrice").html(receivablePrice);
            $("#currency").html(currency);
            var totalPrice = 0;
            $("#verify-tbody").empty();
            $("#attachmentContent").html('');
            //查询核销记录
            $custom.request.ajax($m13004.params.verifyItemUrl, {"id":id}, function(result){
                if(result.state == 200) {
                    var list = result.data;
                    if(list){
                        var html = "" ;
                        $(list).each(function(i, m) {
                            var receiveDate = m.receiveDate == null ? "" : m.receiveDate;
                            var receiceNo = m.receiceNo == null ? "" : m.receiceNo;
                            var price = m.price;
                            var verifier = m.verifier == null ? "" : m.verifier;
                            var verifyDate = m.verifyDate;
                            totalPrice += price;
                            html += '<tr>'
                                + '<td>' + receiveDate + '</td>'
                                + '<td>' + price + '</td>'
                                + '<td>' + receiceNo + '</td>'
                                + '<td>' + verifier + '</td>'
                                + '<td>' + verifyDate + '</td>'
                                + '</tr>';

                        });
                        $m13004.params.existPrice = totalPrice;
                        if (Math.abs(totalPrice) < Math.abs(receivablePrice)) {
                            html += '<tr>'
                                + '<td><input type="text" parsley-type="text" class="input_msg date-input" name="receiveDate" id="receiveDate"></td>'
                                + '<td><input class="form-control" id="price" ></td>'
                                + '<td><input class="form-control" id="receiceNo" ></td>'
                                + '<td></td>'
                                + '<td></td>'
                                + '<td></td>'
                                + '</tr>';
                        }
                        $("#verify-table-detail").append(html);
                        laydate.render({
                            elem: '#receiveDate', //指定元素
                        });
                    }
                }
            });
            $($m13004.params.modalId).modal({backdrop: 'static', keyboard: false});
        },
        hide:function(){
            $($m13004.params.modalId).modal("hide");
        },
        save:function () {
            var receiveDateStr = $("#receiveDate").val();
            var price = $("#price").val();
            var receiceNo = $("#receiceNo").val();
            if (!receiveDateStr) {
                $custom.alert.error("本期收款日期不能为空！");
                return false;
            }
            if (!price) {
                $custom.alert.error("本期核销金额不能为空！");
                return false;
            }

            if ((Math.abs(Number(price) + Number($m13004.params.existPrice))).toFixed(2)> Math.abs($m13004.params.receivablePrice)) {
                $custom.alert.error("核销总金额不得大于应收金额！");
                return false;
            }
            var receiveDate = new Date(receiveDateStr);
            var today = new Date();
            if (receiveDate - today > 0) {
                $custom.alert.error("核销日期不得晚于当前日期！");
                return false;
            }
            //上传文件
            var files = $m13004.params.uploader.files ;
            for(var index = 0 ; index < files.length ; index ++){
                if(files[index].status == 1){
                    continue ;
                }

                files[index].percent = 0 ;
                files[index].loaded = 0 ;
                files[index].status = 1 ;
            }
            $m13004.params.uploader.start() ;
            var json = {"billId":$m13004.params.id, "receiceNo":receiceNo,"receiveDateStr":receiveDateStr, "price":price}
            $custom.request.ajax($m13004.params.saveUrl, json , function(res){
                if (res.state == '200' && res.data.code == '00') {

                    $custom.alert.success("保存成功！");
                    $m13004.hide();
                    setTimeout(function () {
                        $module.revertList.serializeListForm() ;
                        $module.revertList.isMainList = true;
                        $module.load.pageOrder('act=14001');
                    }, 1000);

                } else {
                    var message = res.data.message;
                    if (message != null && message != '' && message != undefined) {
                        $custom.alert.error(message);
                    } else {
                        $custom.alert.error("保存失败！");
                    }
                    $m13004.hide();
                }
            });


        },
        params:{
            verifyItemUrl:serverAddr + '/receiveBill/getVerifyItems.asyn?r='+Math.random(),
            saveUrl:serverAddr + '/receiveBill/saveReceiveBillVerify.asyn?r='+Math.random(),
            modalId:'#signup-modal',
            id:null,
            existPrice:0,
            receivablePrice:0,
            currency:null,
            uploader:null
        }
    };

    function uploadErrorDisplay(id){
        $("#bar" + id).removeClass("progress-bar-success") ;
        $("#bar" + id).addClass("progress-bar-danger") ;
        $("#a" + id).attr("href",'javascript:removeFile(\''+ id +'\')') ;
        $("#a" + id).html("取消") ;
        $("#a" + id).before('<a href="#" id="error'+id+'" style="padding-left:8px;color:red;">上传失败</a>') ;
    }

    //点击取消触发事件
    function removeFile(fileId){
        // $("#item" + fileId).next().remove() ;
        $("#item" + fileId).remove() ;

        $m13004.params.uploader.removeFile(fileId);
    }
</script>