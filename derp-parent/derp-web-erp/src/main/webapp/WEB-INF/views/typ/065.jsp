<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css"/>
<style>
    .input-group input{width: 100%;}
</style>
<div class="content">
    <div class="container-fluid mt80" style="min-height: 500px">
        <h3>百度搜索</h3>
        <p>支持逗号分隔多关键字</p>
        <div class="row">
            <div class="col-lg-6">
                <div class="input-group" style="width: 300px;">
                    <input type="text" id="baidu">
                    <div class="input-group-btn">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-right" role="menu">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src='<c:url value="/resources/plugins/bootstrap-suggest/bootstrap-suggest.js" />' type="text/javascript"></script>
<script>
    /**
     * 百度搜索 API 测试
     */
    $("#baidu").bsSuggest({
        emptyTip: '未检索到匹配的数据',
        allowNoKeyword: false,   //是否允许无关键字时请求数据。为 false 则无输入时不执行过滤请求
        multiWord: true,         //以分隔符号分割的多关键字支持
        separator: ",",          //多关键字支持时的分隔符，默认为空格
        getDataMethod: "url",    //获取数据的方式，总是从 URL 获取
        url: 'http://unionsug.baidu.com/su?p=3&wd=', //优先从url ajax 请求 json 帮助数据，注意最后一个参数为关键字请求参数
        jsonp: 'cb',                        //如果从 url 获取数据，并且需要跨域，则该参数必须设置
        fnProcessData: function (json) {    // url 获取数据时，对数据的处理，作为 fnGetData 的回调函数
            var index, len, data = {value: []};
            if (!json || !json.s || json.s.length === 0) {
                return false;
            }

            len = json.s.length;

            for (index = 0; index < len; index++) {
                data.value.push({
                    word: json.s[index]
                });
            }
            data.defaults = 'baidu';

            //字符串转化为 js 对象
            return data;
        }
    }).on('onDataRequestSuccess', function (e, result) {
        console.log('onDataRequestSuccess: ', result);
    }).on('onSetSelectValue', function (e, keyword, data) {
        console.log('onSetSelectValue: ', keyword, data);
    }).on('onUnsetSelectValue', function () {
        console.log("onUnsetSelectValue");
    });
</script>

