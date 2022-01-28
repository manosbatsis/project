<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
          <div class="row">
            <div class="col-12">
                <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="javascript:$load.a('/contrast/toPage.html');">爬虫商品对照表</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                               
                                <div class="form_item h35">
                                    <span class="msg_title">平台名称 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="platformName">
                                    <span class="msg_title">爬虫商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="crawlerGoodsNo">
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                  <div class="form_item h35">
                                    <span class="msg_title">公司简称 :</span>
                                    <select class="input_msg" id="merchantId" name="merchantId">
                                    </select>
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" id="status" name="status">
		                            </select>
                                </div>
                                 <a href="javascript:;" class="unfold">展开功能</a>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="contrast_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="contrast_export">
                                    <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons" value="导出"/>
                                </shiro:hasPermission>
                              <!--   <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/reptile/delReptile.asyn');">删除</button> -->
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            <th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
                                    <th style="white-space:nowrap;">平台名称</th>
                                    <th style="white-space:nowrap;">用户名</th>
                                    <th style="white-space:nowrap;">爬虫商品货号</th>
                                    <th style="white-space:nowrap;">爬虫商品名称</th>
                                    <th style="white-space:nowrap;">公司简称</th>
                                    <th style="white-space:nowrap;">事业部</th>
                                    <%--<th style="white-space:nowrap;">商品货号</th>--%>
                                    <%--<th style="white-space:nowrap;">商品名称</th>--%>
                                    <th style="white-space:nowrap;">状态</th>
                                    <th style="white-space:nowrap;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
			</div>
		</div>
	</div>
</div>
                <!-- container -->
<script type="text/javascript">
//加载状态
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"merchandiseContrast_statusList",null);
$module.merchantAll.loadSelectData.call($('select[name="merchantId"]')[0],null,null);
$(document).ready(function() {
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url : serverAddr+'/contrast/contrastList.asyn?r=' + Math.random(),
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" class="iCheck">';
            }
        },
        {data : 'platformName'},{data : 'platformUsername'},{data : 'crawlerGoodsNo'},{data : 'crawlerGoodsName'},{data : 'merchantName'},{data : 'buName'},/*{data : 'goodsNo'},{data : 'goodsName'},*/{data : 'statusLabel'},
            { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {
                	var details = "";
                    details = '<shiro:hasPermission name="contrast_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                    return '<shiro:hasPermission name="contrast_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>  '+'   '+details;

                }
            },
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    };
        
    //生成列表信息
    $('#datatable-buttons').mydatatables();
   
});

function searchData(){
    $mytable.fn.reload();
}

//详情
function details(id){
	$module.load.pageOrder("act=110011&id="+id);
}

//编辑
function edit(id){
	$module.load.pageOrder("act=110013&id="+id);
}
//新增
$("#add-buttons").click(function(){
	$module.load.pageOrder("act=110012");
});

/**
 * 全选
 */
$("input[name='keeperUserGroup-checkable']").on("change", function(){
    if($(this).is(':checked')){
        $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
        $('#datatable-buttons tbody tr').addClass('success');
    }else{
        $(":checkbox", '#datatable-buttons').prop("checked",false);
        $('#datatable-buttons tbody tr').removeClass('success');
    }
})
$('#datatable-buttons').on("change", ":checkbox", function() {
    $(this).parents('tr').toggleClass('success');
})

// 点击展开功能
$(".unfold").click(function () {
    $(".form_con").toggleClass('hauto');
    if($(this).text() == '展开功能'){
        $(this).text('收起功能');
    }else{
        $(this).text('展开功能');
    }
})

    $("#export-buttons").click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = serverAddr+"/contrast/exportContrast.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });
</script>
