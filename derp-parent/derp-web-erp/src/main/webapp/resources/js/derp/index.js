var token='';
$(function(){
    //全局应用名
    var baseURL = $('#baseURL').val();
    //首次加载的页面
    var firstURL=$('#firstPage').val();
    //默认加载工作台
    if(firstURL==null||firstURL==''||firstURL==undefined){
        firstURL="/list/menu.asyn?act=100&r="+Math.random();
    }
    //加载工作台
    $load.a(firstURL);
    //菜单加载
    $('ul.nav-second-level a').click(function() {
        $('#rightContent').html('');
        var imgObj = document.createElement("img");
        imgObj.setAttribute("src", baseURL+"/resources/assets/images/loading.svg");
        imgObj.setAttribute("class", "loadingSvg");
        $('#rightContent').append(imgObj);
        var url = $(this).attr("path");
        $load.a(url+"&r="+Math.random())
    });


});