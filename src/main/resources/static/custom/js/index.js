/*
* 作者：ricardo
* 描述：登录页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/
var urlIsCurrentUserHasRole = "/users/isCurrentUserHasRole";
var hasRole = isCurrentUserHasRole(urlIsCurrentUserHasRole,"ADMIN");
setCookie("hasRole", hasRole, 7);

$(document).ready(function () {


    hiddenAll();
    $("[name='my-task-switch']").bootstrapSwitch({
        onText:"完成",
        offText:"未完成",
        onColor:"success",
        offColor:"warning",
        size:"small",
        onSwitchChange:function(event,state){
            if(state==true){
                //alert('已打开');
            }else{
               // alert('已关闭');
            }
        }
    });
});
