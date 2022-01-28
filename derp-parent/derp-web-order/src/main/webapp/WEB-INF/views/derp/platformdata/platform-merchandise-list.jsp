<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .date-input {
        font-size: 13px;
    }
</style>
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">平台商品管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">商品编码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="wareId">
                                    <span class="msg_title">品牌 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="brand">
                                    <span class="msg_title">条码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="upc">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">平台 :</span>
                                    <select class="input_msg" name="crawlerType" id="crawlerType"></select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="platformmerchandise_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platformmerchandise_cartonsizeimport">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="cartonsizeImport">箱规导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="platformmerchandise_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="platformmerchandiseImport">商品导入</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>平台</th>
                                <th>归属账号</th>
                                <th>商品编码</th>
                                <th style="width: 300px;">商品名称</th>
                                <th>条形码</th>
                                <th>品牌</th>
                                <th>单位</th>
                                <th>箱规</th>
                                <th>创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">

    $module.constant.getConstantListByNameURL.call($('select[name="crawlerType"]')[0],"crawler_typeList",null);
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/platformMerchandise/listByPage.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
            {data:'crawlerTypeLabel'},{data:'userCode'},{data:'wareId'},{data:'name'},{data:'upc'},{data:'brand'},{data:'unit'},{data:'cartonSize'},{data:'createDate'}
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    function searchData(){
        $mytable.fn.reload();
    }
    //导入
   	$("#platformmerchandiseImport").click(function(){
   		$module.load.pageOrder("act=210002");
   	});
    //箱规导入
    $("#cartonsizeImport").click(function(){
        $module.load.pageOrder("act=210003");
    });

    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    //导出
    $("#export").on("click",function(){
            var param = $("#form1").serialize();
            $custom.alert.warning("确定导出？",function(){
                var url = serverAddr+"/platformMerchandise/export.asyn?"+param;
                $downLoad.downLoadByUrl(url);
            });
        });

</script>
