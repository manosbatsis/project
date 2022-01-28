(function($) {
    $.fn.fileTempModal = function(options) {
        var defaults = {
            url: '标题',
            title: '选择发票模板',
            tbody:'',
            showCloseButton: true,
            otherButtons: [],
            otherButtonStyles: [],
            bootstrapModalOption: {},
            dialogShow: function() {},
            dialogShown: function() {},
            dialogHide: function() {},
            dialogHidden: function() {},
            clickButton: function(sender, modal, index) {}
        };
        options = $.extend(defaults, options);
        var modalID = '';

        function getModalID() {
            var date = new Date();
            return 'mdl' + date.valueOf();
        }

        function searchData(tmpHtml, obj, modalID, options) {
            var title = $("#title").val();
            $("#data-tbody").empty();
            $custom.request.ajax( $module.params.listFileTempInfoURL, {"title":title}, function(result){
                if(result.state == 200) {
                    var list = result.data;
                    debugger
                    if(list){
                        var html = "" ;
                        $(list).each(function(i, m) {
                            var id = m.id;
                            var title = m.title;
                            var code = m.code;
                            var customers = m.customers;
                            html += '<tr>'
                                 + '<td class="td-checkbox"><input type="checkbox" value="' + id + '" class="iCheck" ' + '></td>'
                                 + '<td>' + title + '</td>'
                                 + '<td>' + code + '</td>'
                                 + '<td>' + customers + '</td>'
                                 + '</tr>';

                        });
                        //替换模板标记
                        tmpHtml = tmpHtml.replace(/{ID}/g,modalID ).replace(/{title}/g, options.title).replace(/{tbody}/g, html);
                        obj.append(tmpHtml);
                        $('#confirmBtn').click(function() {
                            alert(1)
                            // searchData(tmpHtml, obj, modalID, options);
                        });
                        $('#'+modalID).modal({backdrop: 'static', keyboard: false});
                    }
                }
            });
        }

        $.fn.extend({
            closeDialog: function(modal) {
                var modalObj = modal;
                modalObj.modal('hide');
            }
        });

        return this.each(function() {
            var obj = $(this);
            modalID = getModalID();
            var tmpHtml = '<div class="modal fade" id="{ID}" role="dialog" aria-hidden="true">' +
                '<div class="modal-dialog"><div class="modal-content">' +
                '<div class="modal-header"><button type="button" class="close" data-dismiss="modal">' +
                '<span aria-hidden="true">&times;</span></button><h6 class="modal-title">{title}</h6></div>' +
                '<div class="modal-body">' +
                '<form id="popup-goods-form">' +
                '<div class="form-row mt10">' +
                '<div class="col-8">' +
                '<div class="row">' +
                '<label class="col-4 col-form-label"><div class="fr">发票名称<span>：</span></div></label>' +
                '<div class="col-8"><input name="title" type="text" class="form-control"/> </div>' +
                '</div>' +
                '</div>' +
                '<div class="col-2">' +
                '</div>' +
                '<div class="col-2">' +
                '<button type="button" class="btn btn-info waves-effect waves-light fr btn-sm" id="confirmBtn">确定</button>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '<div class="form-row mt20">' +
                '<table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">' +
                '<thead>' +
                '<tr><th style="text-align: center;">' +
                '<label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">' +
                '<input type="checkbox" name="keeperMerchantGroup-checkable" class="group-checkable" />' +
                '</label>' +
                '</th>' +
                '<th>发票模板名称</th>' +
                '<th>发票编码</th>' +
                '<th>适用客户</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody id="data-tbody">{tbody}</tbody>' +
                '</table>' +
                '</div>' +
                '</div>' +
                '<div class="modal-footer">'+
                '<div class="form-row mt20">' +
                '<div class="col-12">' +
                '<div class="row">' +
                '<div class="col-4"></div>' +
                '<div class="col-2">' +
                '<button type="button" class="btn btn-info waves-effect waves-light fr btn-sm" onclick=\'$m13004.save()\'>确认</button>' +
                '</div>' +
                '<div class="col-2">' +
                '<button type="button" class="btn btn-light waves-effect waves-light btn_default btn-sm" onclick=\'$m13004.hide();\'>取消</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div></div></div></div>';
            var buttonHtml = '<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>';
            if (!options.showCloseButton && options.otherButtons.length > 0) {
                buttonHtml = '';
            }
            //生成按钮
            var btnClass = 'cls-' + modalID;
            for (var i = 0; i < options.otherButtons.length; i++) {
                buttonHtml += '<button buttonIndex="' + i + '" class="' + btnClass + ' btn ' + options.otherButtonStyles[i] + '">' + options.otherButtons[i] + '</button>';
            }

            //绑定按钮事件,不包括关闭按钮

            searchData(tmpHtml, obj, modalID, options);


            /*//绑定本身的事件
            modalObj.on('show.bs.modal', function() {
                options.dialogShow();
            });
            modalObj.on('shown.bs.modal', function() {
                options.dialogShown();
            });
            modalObj.on('hide.bs.modal', function() {
                options.dialogHide();
            });
            modalObj.on('hidden.bs.modal', function() {
                options.dialogHidden();
                modalObj.remove();
            });
            modalObj.modal(options.bootstrapModalOption);*/
        });

    };

    $.extend({
        fileTempModal: function(options) {
            $("body").fileTempModal(options);
        }
    });

})(jQuery);