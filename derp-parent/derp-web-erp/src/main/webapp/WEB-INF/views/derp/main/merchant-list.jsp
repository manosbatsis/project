<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content  -->

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
                       <li class="breadcrumb-item "><a href="javascript:$load.a('/merchant/toPage.html');">公司列表</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">公司编码 :</span>
                                    <input class="input_msg" type="text" name="code" required="" >
                                    <span class="msg_title">公司简称 :</span>
                                    <input class="input_msg" type="text" name="name" required="" >
                                    <span class="msg_title">卓志编码 :</span>
                                    <input class="input_msg" type="text" name="topidealCode" required="" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="merchant_toAddPage">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons">新增</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="merchant_delMerchant">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$custom.request.delChecked('/merchant/delMerchant.asyn');">删除</button>
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
                            <th>公司编码</th>
                            <th>公司简称</th>
                            <th>卓志编码</th>
                            <th>联系电话</th>
                            <th style="max-width: 200px">接收清关资料邮箱</th>
                            <th>备注</th>
                            <th>创建时间</th>
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


    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/merchant/listMerchant.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'code'},{data:'name'},{data:'topidealCode'},{data:'tel'},{data:'email'},{data:'remark'},{data:'createDate'},
            { //操作
                orderable: false,
                width: "100px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<shiro:hasPermission name="merchant_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>'
                        +'  <shiro:hasPermission name="merchant_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                }
            },
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //新增
        $("#add-buttons").click(function(){
        	$load.a("/merchant/toAddPage.html");
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
    	
    	$load.a("/merchant/toEditPage.html?id="+id);
    }
    
    //详情
    function details(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/merchant/toDetailsPage.html?id="+id);
    }
    
</script>