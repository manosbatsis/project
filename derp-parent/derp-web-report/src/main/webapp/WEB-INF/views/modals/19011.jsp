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
</style>
<div>
    <!-- Signup modal content -->
    <div id="edit-modal"  class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -170px;">
        <div class="modal-dialog">
            <div class="modal-content" style="width: 800px;">
            	<div class="header" >
              <span class="header-title" >编辑项目</span>
          </div>
                <div class="modal-body" >
                    <form class="form-horizontal" id="invoice-from">
                        <!-- 自定义参数  -->  
                  <div class="form-group">
                      <div class="col-12">
                          <label >项目ID :&nbsp<span id="parentProjectCode2"></span></label>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                          <label>项目名称:&nbsp<span id="parentProjectName2"></span></label>
                      </div>
                  </div>
                        <div class="form-row mt20">
                            <table id="datatable-detail" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
                                    <th>小类ID</th>
                                    <th>小类名称</th>
                                    <th>备注</th>
                                </tr>
                                </thead>
                                <tbody id="data-tbody">
                                </tbody>
                            </table>
                        </div>
                 	<div class="form-group" style="float: right;">
                      <div class="col-12">
                          <button class="btn btn-info waves-effect waves-light fr btn_default" type="button" onclick='$m19011.save();'>确定</button>
                          <button class="btn btn-light waves-effect waves-light btn_default" type="button" onclick='$m19011.hide();'>取消</button>
                      </div>
                  </div>
                    </form>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
    var $m19011={
        init:function(parentProjectCode){
            $("#data-tbody").html("");
            $m19011.params.parentProjectCode2 = parentProjectCode ;
            $m19011.show(parentProjectCode);
        },
        show:function(parentProjectCode){
                $custom.request.ajax($m19011.params.detailUrl, {"parentProjectCode":parentProjectCode}, function(result){
                if(result.state == 200) {
                    var list = result.data;
                    if(list){
                        var html = "" ;
                        for (var i = 0; i < list.length; i++) {
                            var m = list[i];
                            $("#parentProjectCode2").html(m.parentProjectCode);
                            $("#parentProjectName2").html(m.parentProjectName);
                            var childProjectCode = m.childProjectCode;
                            var childProjectName = m.childProjectName;
                            var remark = m.remark;
                            html += '<tr>'
                                + '<td>' + $m19011.strFormat(childProjectCode) + '</td>'
                                + '<td>' + $m19011.strFormat(childProjectName) + '</td>'
                                + '<td><input type="text" name="remark" value="'+$m19011.strFormat(remark)+'"></td>'
                                + '</tr>';
                        }
                        $("#datatable-detail").append(html);
                    }
                }
            });
            $($m19011.params.modalId).modal("show");
        },
        strFormat:function (str) {
            if (!str) {
                return "";
            }
            return str;
        },
        hide:function(){
            $("#data-tbody").html("");
            $($m19011.params.modalId).modal("hide");
        },
        save:function(){
            var check = true;
            var remarkIsNull = true;
            //遍历备注
            $("#data-tbody").find("tr").each(function(){
                var remark = $(this).children().eq(2).find("input").val();
                if (!remark) {
                    check = false;
                } else {
                    remarkIsNull = false;
                }
            });
            if (remarkIsNull) {
                $custom.alert.error("至少填写一个备注");
                return;
            }
            //汇总信息
            var childProjectCodeList = new Array() ;// 小类ID
            var remarkList = new Array() ;  // 备注

            var trs = $("#data-tbody").find("tr") ;
            $(trs).each(function(index , tr){
                var childProjectCodeVal = $(tr).find("td").eq(0).text() ;
                var remarkVal = $(tr).find("input[name='remark']").val() ;
                if(remarkVal!=null && remarkVal!=""){
                    childProjectCodeList.push(childProjectCodeVal) ;
                    remarkList.push(remarkVal) ;
                }
            }) ;
            childProjectCodeList = childProjectCodeList.join(",") ;
            remarkList = remarkList.join(",") ;

            var jsonData = {"childProjectCodeList":childProjectCodeList,"remarkList":remarkList} ;
            $custom.request.ajaxReqrCheck($m19011.params.saveUrl, jsonData , function(data){
                console.log(data);
                if(data.state==200){
                    var str = data.data;
                    $custom.alert.success(str);
                    $("#data-tbody").html("");
                    $m19011.hide();
                    //重新加载页面
                    setTimeout(function () {
                        $load.a(pageUrl+"17001");
                    }, 1000);
                }else{
                    if(!!data.expMessage){
                        $custom.alert.error(data.expMessage);
                    }else{
                        $custom.alert.error(data.message);
                    }
                }
            } ,null , function(){});
        },
        params:{
            detailUrl:serverAddr + '/settlementConfig/getDetailById.asyn?r='+Math.random(),
            saveUrl:serverAddr + '/settlementConfig/modifySettlementConfig.asyn?r='+Math.random(),
            parentProjectCode2:"" ,
            modalId:'#edit-modal'
        }
    };
</script>