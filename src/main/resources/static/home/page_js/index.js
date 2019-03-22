/*
* 作者：ricardo
* 描述：首页js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/

$(document).ready(function () {

    //公告通知
    var infor = getActivities("/activity/getActivities","activityType=会议纪要&count=6");
    fillTheInfo("#con1", infor,"/admin/activity/");

    //科学研究
    var projects = getTopMes("/project/getLatest",7);
    fillTheModel("#ul-project", projects,"/project_brief.html?id=");
    //学术交流
    var academicData = getActivities("/activity/getActivities","activityType=开放会议&count=6");
    fillTheModel("#ul-academic-exchange", academicData,"/achievement/activity.html?id=");

    //论文
    var paperMes = getTopMes("/paper/getLatest",5);
    fillTheProduct("#div-first-paper", "#ul-paper", paperMes,"/achievement/scientific_achievements1.html?id=","#bc0e05");
    //专利
    paperMes = getTopMes("/patent/getLatest",5);
    fillTheProduct("#div-first-patent", "#ul-patent", paperMes,"/achievement/scientific_achievements2.html?id=","#fd7e02");
    //软著
    paperMes = getTopMes("/software/getLatest",5);
    fillTheProduct("#div-first-software", "#ul-software", paperMes,"/achievement/scientific_achievements3.html?id=","#f8b551");

    //中心动态
    var dynamicMes = getActivities("/activity/getActivities","activityType=会议纪要&count=6");
    fillTheModel("#ul-activity", dynamicMes,"#");
    //文体活动
    dynamicMes = getActivities("/activity/getActivities","activityType=团队建设&count=6");
    fillTheModel("#ul-activity2", dynamicMes,"/achievement/activity.html?id=");

    //开放课题
    var openPro = getTopMes("/project/getLatest",6);
    fillTheModel("#ul-open-project", openPro,"/project_brief.html?id=");


});
$(function(){

});
//getActivities

//activity消息
function getActivities(url,param){
    var message =null;
    //设置同步
    $.ajax({
        type : "get",
        url : url,
        data : param,
        async : false,
        success : function(data){
            if(data.result!=1){
                //alert(data.message);
            }
            else{message = data.data;}
        }
    });
    return message;
}

//获取首页推送消息
function getTopMes(url,num){
    var message =null;
    //设置同步
    $.ajax({
        type : "get",
        url : url,
        data : "count="+num,
        async : false,
        success : function(data){
            if(data.result!=1){
                //alert(data.message);
            }
            else{
                message = data.data;
            }
        }
    });
    return message;
}
//更新推送消息
function pushMes(id,message){
    var num;
    ($(id).find('.item1').find('a').length > message.length)?num = message.length:num = $(id).find('.item1').find('a').length;
    $(id).find('.item1').find('a').html("");
    if(message.length == 0)     return ;
    for(var i = 0 ; i < num; i++){
        $(id).find('.item1').find('a').eq(i).html(message[i].title);
    }
    if(num ==  message.length){
        for(var j = num; j <= $(id).find('.item1').find('a').length+1; j++){
            $(id).find('.item1').eq(j).html("");
        }
    }
}

//填充滚动通知栏数据
function fillTheInfo(id, data,pre_url) {
    var father = $(id);
    father.empty();
    for(var i = 0 ; i < data.length; i++){
        var str = "<li>\n" +
            "       <a href=\""+pre_url+data[i].id+"\">"+data[i].title+"</a>\n" +
            "     </li>";
        father.append(str);
    }
}
//根据传入的首页模块id以及数据，填充模块内容，根据pre_url设置跳转链接
function fillTheModel(id, data,pre_url) {
    var father = $(id);
    father.empty();
    for(var i = 0 ; i < data.length; i++){
        var date = new Date(data[i].date);
        var str = "<li class=\"item item1\">" +
            "        <i></i>" +
            "        <a target='_blank' href=\""+pre_url+data[i].id+"\">"+data[i].title+"</a>" +
            "        <span class=\"item-time\">"+date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+"</span>\n" +
            "        </li>";
        father.append(str);
    }
}
//根据传入的首页模块id以及数据，填充模块内容，根据pre_url设置跳转链接 //论文专利软著模块
function fillTheProduct(first_id, id, data,pre_url,backgroundColor) {
    if(data.length<1)return;
    //填充第一篇
    var first = $(first_id);
    first.empty();
    var date = new Date(data[0].date);
    //console.log(date);
    var firstTitle = data[0].title;
    var lastTitle=firstTitle;
    for(var j=firstTitle.length;j<45;j++){
        lastTitle+="&nbsp;";
    }

    var str1 = " <div style='text-align:center;background-color: "+backgroundColor+";' class=\"lately-date fl\">\n" +
        "                    <p style='padding-top: 3px;' class=\"date-day\">"+date.getDate()+"</p>\n" +
        "                    <span  class=\"date-other\">"+date.getFullYear()+"-"+(date.getMonth()+1)+"</span>\n" +
        "                </div>\n" +
        "                <div class=\"lately-cont item1\">\n" +
        "                    <p class=\"lately-title\"><a  target='_blank' href=\""+pre_url+data[0].id+"\">"+lastTitle+"</a></p>\n" +
        //"                    <span class=\"lately-des\">"+data[0].authorList+"</span>\n" +
        "                </div>";
    first.append(str1);

    //填充剩下的
    var father = $(id);
    father.empty();
    for(var i = 1 ; i < data.length; i++){
        date = new Date(data[i].date);
        var str = "<li class=\"item item1\">" +
            "        <i></i>" +
            "        <a  target='_blank' href=\""+pre_url+data[i].id+"\">"+data[i].title+"</a>" +
            "        <span class=\"item-time\">"+date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+"</span>\n" +
            "        </li>";
        father.append(str);
    }
}