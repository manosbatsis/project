<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content -->

<div class="content">
    <div class="container-fluid mt80">
           <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">公司档案</a></li>
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchantShop/toPage.html');">店铺信息</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                	<span class="msg_title">开店公司 :</span>
                                    <input class="input_msg" type="text" name="merchantName" required="" >
                                    <span class="msg_title">店铺名称 :</span>
                                    <input class="input_msg" type="text" name="shopName" required="" >
                                    <span class="msg_title">店铺编码 :</span>
                                    <input class="input_msg" type="text" name="shopCode" required="" >
                                  
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">平台名称 :</span>
                                    <select class="input_msg" name="storePlatformCode" id="storePlatformCode">
                                    </select>
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" name="status" id="status">
                                    </select>
                                    <span class="msg_title">数据来源 :</span>
                                    <select class="input_msg" name="dataSourceCode" id="dataSourceCode">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>
                                 <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="merchantShop_toAddPage">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons">新增</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="merchantShop_delShop">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$custom.request.delChecked('/merchantShop/delShop.asyn');">删除</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="merchantShop_exportShop">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="exportShopInfo();">导出</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>开店公司</th>
                            <th>平台名称</th>
                            <th>仓库名称</th>
                            <th>店铺名称</th>
                            <th>店铺编码</th>
                            <th>专营母品牌</th>
                            <th>结算币种</th>
                            <th>状态</th>
                            <th>数据来源</th>
                            <th>运营类型</th>
                            <th>创建时间</th>
                            <th>操作员</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
                </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="storePlatformCode"]')[0],"storePlatformList",null);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"merchantShopRel_statusList",null);
$module.constant.getConstantListByNameURL.call($('select[name="dataSourceCode"]')[0],"dataSourceList",null);
//加载仓库的下拉数据
$module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/merchantShop/listShop.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },	
            {data:'merchantName'},{data:'storePlatformName'},{data:'depotName'},{data:'shopName'},{data:'shopCode'},{data:'superiorParentBrandName'}, {data:'currencyLabel'},{data:'statusLabel'},{data:'dataSourceName'},{data:'shopTypeName'},{data:'createDate'},{data:'operator'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                	var details = "";
                	details = '<shiro:hasPermission name="merchantShop_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                	return '<shiro:hasPermission name="merchantShop_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>   '+'   '+details;
                }
            },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //新增
        $("#add-buttons").click(function(){
        	$load.a("/merchantShop/toAddPage.html");
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
    });

    function searchData(){
        $mytable.fn.reload();
    }
    
    //编辑
    function edit(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/merchantShop/toEditPage.html?id="+id);
    }
    
    //详情
    function details(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/merchantShop/toDetailsPage.html?id="+id);
    }
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
    function exportShopInfo() {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = "/merchantShop/exportShop.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    }


</script>
</body>
</html>