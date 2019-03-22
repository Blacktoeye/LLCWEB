/*
* 作者：ricardo
* 描述：登录页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/
$(document.body).css({
    "overflow-x": "hidden",
    "overflow-y": "hidden"
});
/*动态文字时钟*/
var click = setInterval(function () {
    var time = new Date();
    var hour = time.getHours().toString();
    if (time.getHours() < 10) hour = "0" + time.getHours();
    var min = time.getMinutes().toString();
    if (time.getMinutes() < 10) min = "0" + time.getMinutes();
    var sec = time.getSeconds().toString();
    if (time.getSeconds() < 10) sec = "0" + time.getSeconds();

    var timeStr = time.getFullYear() + '年' + (time.getMonth() + 1) + '月' + time.getDate() + '日 ' +
        hour + ':' + min + ':' + sec;
    $("#clock").html(timeStr);
}, 1000);

$(document).ready(function () {


});
//设想，用定时器关闭用户登录失败或注销时登录页面的提示信息
function timedMsg()
{
    var t=setTimeout("alert('5 seconds!')",5000)
}