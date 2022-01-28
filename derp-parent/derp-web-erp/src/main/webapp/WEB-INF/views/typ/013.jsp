<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
            <div class="row">
                <div class="col-12">
                    <div class="card-box table-responsive">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">新增采购订单</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <!--  title   基本信息   start -->
                                <div class="title_text">基本信息</div>
                                <div class="form-row mt20 ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr"><i class="red">*</i> 供应商 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr"><i class="red">*</i> 付款主体<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <select class="form-control">
                                                    <option>键太</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr">业务模式 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <select class="form-control">
                                                    <option>1</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr">预计付款时间<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr"><i class="red">*</i> 采购币别 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <select class="form-control">
                                                    <option>人民币</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr">运输方式<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr">合同号 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr">预计到港时间<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr">PO号 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr">销售渠道<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr">交货地点 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <input type="text" required="" parsley-type="text" class="form-control">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr">采购计划编号<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row ml15">
                                    <div class="col-4">
                                        <div class="row">
                                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                    class="fr">备注 <span>：</span></span></label>
                                            <div class="col-8 ml10 line">
                                                <textarea class="form-control" rows="4" name="describe"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-7">
                                        <div class="row">
                                            <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                    class="fr"><i class="red">*</i> 入仓仓库<span>：</span></span></label>
                                            <div class="col-5 ml10 line">
                                                <select class="form-control">
                                                    <option>请选择</option>
                                                    <option>2</option>
                                                    <option>3</option>
                                                    <option>4</option>
                                                    <option>5</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--  title   基本信息  start -->
                                <!--   财务相关  start -->
                                <div class="title_text">财务相关</div>
                                <div id="finance-list" style="display:none">
                                    <div class="form-row mt20 ml15">
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                        class="fr">资金成本预提 <span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">异常货款调整<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">预计回款金额<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row ml15">
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                        class="fr">破损预提 <span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">其他费用<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">预计单品销价<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row ml15">
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                        class="fr">汇损预提 <span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">毛利率<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--   财务相关  end -->
                                <!--   款项信息  start -->
                                <div class="title_text">款项信息</div>
                                <div id="term-list" style="display:none">
                                    <div class="form-row mt20 ml15">
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                        class="fr">卓普信付供应商 <span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">货款到日期<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">资金占用时间<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row ml15">
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label " style="white-space:nowrap;"><span
                                                        class="fr">卓志付保证金 <span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">实际回款时间<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-4">
                                            <div class="row">
                                                <label class="col-4 col-form-label" style="white-space:nowrap;"><span
                                                        class="fr">资金线上超时<span>：</span></span></label>
                                                <div class="col-7 ml10 line">
                                                    <input type="text" required="" parsley-type="text"
                                                           class="form-control">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!--   款项信息  end -->
                                <!--   商品信息  start -->
                                <div class="col-12 ml10">
                                    <div class="row">
                                        <div class="col-10">
                                            <div class="title_text">商品信息</div>
                                        </div>
                                        <div class="col-1 mt10">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                    id="add-list">新 增
                                            </button>
                                        </div>
                                        <div class="col-1 mt10">
                                            <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                    id="delete-list">删 除
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row mt20 ml15">
                                    <table id="table-list" class="table table-striped">
                                        <tr>
                                            <th style="width:50px;text-align:center"><input id="checkbox11"
                                                                                            type="checkbox" name="all">
                                            </th>
                                            <th style="width:100px;">子PO编号</th>
                                            <th style="width:300px;">商品编号</th>
                                            <th style="width:100px;">商品条码</th>
                                            <th style="width:100px;">商品货号</th>
                                            <th style="width:100px;">计量单位</th>
                                            <th style="width:100px;">采购数量</th>
                                            <th style="width:100px;">采购单价</th>
                                            <th style="width:100px;">采购总金额</th>
                                            <th style="width:100px;">预计汇率</th>
                                            <th style="width:100px;">预计折算人民币</th>
                                        </tr>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button" class="btn btn-info waves-effect waves-light fr"
                                                        id="save-buttons">保 存
                                                </button>
                                            </div>
                                            <div class="col-6">
                                                <button type="button" class="btn btn-light waves-effect waves-light"
                                                        id="cancel-buttons">取 消
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
        </form>
    </div>
    <!-- container -->
</div>

</div> <!-- content -->
<script type="text/javascript">

    $(function () {


        //删除选中行
        $("#delete-list").click(function () {
            $("input[name='test']:checked").each(function () { // 遍历选中的checkbox
                n = $(this).parents("tr").index();  // 获取checkbox所在行的顺序
                $("#table-list").find("tr:eq(" + n + ")").remove();
            });
        });

        //判断是否全选
        $("input[name='all']").click(function () {

            //判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型

            if ($(this).is(":checked")) {

                $("input[name='test']").prop("checked", true);

            } else {

                $("input[name='test']").prop("checked", false);

            }

        });


        //保存按钮
        $("#save-buttons").click(function () {
            var url = "/merchandise/saveMerchandise.asyn";
            var form = $("#add-form").serializeArray();
            $custom.request.ajax(url, form, function (result) {
                if (result.state == '200') {
                    $custom.alert.success("添加成功！");
                    setTimeout(function () {
                        $load.a("/merchandise/toPage.html");
                    }, 1000);
                } else {
                    $custom.alert.error("添加失败！");
                }
            });
        });

        //取消按钮
        $("#cancel-buttons").click(function () {
            $load.a("/merchandise/toPage.html");
        });

        //财务相关收起，展开
        $("#finance").click(function () {
            $("#finance-list").toggle();
        });

        //款项信息收起，展开
        $("#term").click(function () {
            $("#term-list").toggle();
        });

        //新增按钮
        $("#add-list").click(function () {
            var listArr = "<tr><td style='width:50px;text-align:center'><input id='checkbox11' type='checkbox' name='test'></td><td>January</td><td><select class='form-control modal_input goods_select2' id='isUnion'><option value=''></option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option></select></td><td>January</td><td>January</td><td>January</td><td>January</td><td><input type='text' name='name' /></td><td>January</td><td>January</td><td>January</td></tr>";
            $("#table-list").append(listArr);
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            })
        })

    });

</script>
