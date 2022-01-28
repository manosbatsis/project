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
                        <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/product/toPage.html');">货品列表</a></li>
                    </ol>
                    </div>
            </div>
                   <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                               <%-- <div class="form-row ml15 h35">
                                    <div class="form-group col-md-3">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;">货品名称<span>：</span></label>
                                            <div class="col-7 ml10">
                                                <input class="form-control" type="text" name="name" required="" >
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;">商品条码<span>：</span></label>
                                            <div class="col-7 ml10">
                                                <input class="form-control" type="text" name="barcode" required="" >
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;">商品品牌<span>：</span></label>
                                            <div class="col-7 ml10">
                                                <select name="brandId" class="form-control goods_select2">
                                                    <option value="">请选择</option>
                                                    <c:forEach items="${brandBean }" var="brand">
                                                        <option value="${brand.value }">${brand.label }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <div class="row">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                            <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                        </div>
                                    </div>
                                </div>--%>
           <div class="form_item h35">
                                    <span class="msg_title">货品名称 :</span>
                                    <input class="input_msg" type="text" name="name" required="" >
                                    <span class="msg_title">商品条码 :</span>
                                    <input class="input_msg" type="text" name="barcode" required="" >
                                    <span class="msg_title">商品品牌 :</span>
                                    <select name="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${brandBean }" var="brand">
                                            <option value="${brand.value }">${brand.label }</option>
                                        </c:forEach>
                                    </select>
                                     <div class="fr">
			                                    <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
			                                    <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                       				</div>
                                </div>
                                <div class="form_item h35">
                                	<span class="msg_title">二级类目 :</span>
                                    <select name="productTypeId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${productTypeBean }" var="productType">
                                            <option value="${productType.value }">${productType.label }</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="product_modifyBatchProduct">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$m8013.init(this);" value="批量修改品类"/>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!-- 过滤条件查询  end  -->
                    <div class="row mt20">
                    <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>货品名称</th>
                            <th>计量单位</th>
                            <th>二级类目</th>
                            <th>条形码</th>
                            <th>品牌</th>
                            <th>原产国</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <jsp:include page="/WEB-INF/views/modals/8013.jsp" />
       </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->
<footer class="footer text-right">
</footer>



<script type="text/javascript">

    $(document).ready(function() {
    	
    	//重置按钮
        $("button[type='reset']").click(function(){
        	$(".goods_select2").each(function(){
        		$(this).val("");
        	});
        	//重新加载select2
    		$(".goods_select2").select2({
    			language:"zh-CN",
    			placeholder: '请选择',
    			allowClear:true
    		});
        });
    	
    	//重新加载select2
		$(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
    	
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/product/listProduct.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data:'productName'},{data:'unitName'},{data:'goodsClassifyName'},{data:'barcode'},{data:'brandName'},{data:'countyName'},
            { //操作
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                	return '<shiro:hasPermission name="product_toEditPage"><a href="javascript:edit('+row.id+')">编辑</a></shiro:hasPermission>'
                        + '  <shiro:hasPermission name="product_toDetailsPage"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>  '
                }
            },
            ],
            formid:'#form1'
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    } );

    function searchData(){
        $mytable.fn.reload();
    }

    //详情
    function details(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/product/toDetailsPage.html?id="+id);
    }
    //编辑
    function edit(id){
    	$module.revertList.serializeListForm() ;
    	$module.revertList.isMainList = true ; 
    	
    	$load.a("/product/toEditPage.html?id="+id);
    }
    
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
    });
</script>