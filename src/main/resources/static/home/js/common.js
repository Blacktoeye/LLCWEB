/**
 * Created by BlueFisher on 2016/10/30 0030.
 */

/****************************************日历插件*******************************************/
function calendar() {
    $("#dtBox").DateTimePicker(
        {
            dateFormat: "yyyy-MM-dd",
            dateTimeFormat: "yyyy-MM-dd HH:mm:ss",
            timeFormat: "HH:mm",
            shortDayNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            fullDayNames: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
            shortMonthNames: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
            fullMonthNames: ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
            titleContentDate: "设置日期",
            titleContentTime: "设置时间",
            titleContentDateTime: "设置日期和时间",
            buttonsToDisplay: ["HeaderCloseButton", "SetButton", "ClearButton"],
            setButtonContent: "确定",
            clearButtonContent: "取消"
        });
}

/****************************************高级搜索页面弹出与收起*******************************************/
function adSearch() {
    $('.Ad_search a').click(function () {
        $('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(200);
    });
    $('.remove').click(function () {
        $('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(200);
    });
}

/********************************json 的时间格式进行拼接***************************/
function converDate(date) {
    var year = new Date(date).getFullYear();
    var month = "0" +(new Date(date).getMonth() + 1);
    var day ="0" + new Date(date).getDate();
    return year + "-" + month.substring(month.length - 2) + "-" + day.substring(day.length - 2) ;
}

/********************************时间格式转换为Json的时间格式***************************/
function convertDateToJsonDate(strDate) {
    var splitted = strDate.split("-");
    var newDate = new Date(Date.UTC(splitted[0], splitted[1] - 1, splitted[2]));
    return newDate.getTime();
}

/****************************************填充编辑页面子方法*******************************************/
function fill(type, idValue) {
    var name = $("tbody.tableContent tr").eq(idValue).find('#' + type).text();
    $(".input_" + type).val(name);
    getInput(type);
}

/****************************************动态获取input的value值*******************************************/
function getInput(type) {
    $(".input_" + type).bind('input propertychange', function () {
        $(".input_" + type).val();
    });
}

/****************************************清除编辑页面子方法*******************************************/
function clear() {
    $("input").val("");
}

/****************************************取消事件*******************************************/
function cancel() {
    $(".content").removeClass("disappear");
    $(".content_edit").addClass("appear");
}

/****************************************删除事件*******************************************/
function del(obj,type) {
    var idValue = $(obj).attr("id");
    var idName = $("tbody.tableContent tr").eq(idValue).find('#id').text();

    $.getJSON('../admin/delete'+type+'?id=' + idName, function (obj) {
        if (obj.errorCode != "0x03") {
            var idName = $("tbody.tableContent tr").eq(idValue).remove();
            // window.location.reload();           //成功后刷新页面
            alert("删除成功！！！");
        }
        else
            alert("无权访问！！！");
    });
}

/****************************************上传图片*******************************************/
function upLoad() {
    var fd = new FormData();
    var values = $(".upload>input[name='name']").val();
    fd.append("pic", $("#upLoadFile").get(0).files[0]);
    fd.append("name", 'values');
    $.ajax({
        url: '/upload',
        type: 'POST',
        processData: false,
        contentType: false,
        //dataType:'JSON',
        data: fd,
        success: function (obj) {
            if(obj.status){
                // alert(obj.data);
                // window.location.reload();           //成功后刷新页面
                alert("上传成功！！！！");
                $(".input_pic_code").val(obj.data);
                $('#preview img').attr('src', '../getPic?id=' + obj.data);
            }
            else{
                alert("图片已经存在！！！！");
            }
            //window.location.reload();           //成功后刷新页面
        }
    });
}

/****************************************动态获取图片的value值*******************************************/
function getPicInput() {
    $(".upload>input[name='name']").attr('value', getName());
    $(".upload>input[name='name']").val(getName());
    $(".upload>input[name='name']").bind('input propertychange', function () {
        $(".upload>input[name='name']").attr("value", $(this).val());
    });
}
/****************************************获取图片的名字*******************************************/
function getName() {
    var file = $("#upLoadFile").val();
    var fileName = getFileName(file);
    //alert(file);          //********.jpg

    function getFileName(o) {
        var pos = o.lastIndexOf('\\');
        var pos1 = o.lastIndexOf('.');
        return o.substring(pos + 1, pos1);
    }

    return fileName;
}

/****************************************通过回车触发的更换图片事件*******************************************/
function picPreviewByEnter(event) {
    var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;       //处理兼容性问题
    $("input.input_pic_code").bind('input propertychange', function () {
        $("input.input_pic_code").attr("value", $(this).val());
    });
    if (keyCode == "13") {
        var picCode = $("input.input_pic_code").val();
        $('#preview img').attr('src', '../getPic?id=' + picCode);
    }
}

//根据id获取document信息
function getDocumentById(id,urlGetDocumentById) {
    var document =null;
    //设置同步
    $.ajax({
        type : "get",
        url : urlGetDocumentById,
        data :"id=" + id,
        async : false,
        success : function(data){
            document = data.data;
            if(data.result!==1){
                alert(data.message);
            }
        }
    });
    return document;
}

/*********************************************关于cookie********************************************/
//设置cookie
function setCookie(name, value, day) {
    var date = new Date();
    date.setDate(date.getDate() + day);
    document.cookie = name + '=' + value + ';expires=' + date;
};

//获取cookie
function getCookie(name) {
    var reg = RegExp(name + '=([^;]+)');
    var arr = document.cookie.match(reg);
    if (arr) {
        return arr[1];
    } else {
        return '';
    }
};

//删除cookie
function delCookie(name) {
    setCookie(name, null, -1);
};
//根据id获取信息(教授团队和人才)
function getPeopleByPosition(position,url){
    var document =null;
    //设置同步
    $.ajax({
        type : "get",
        url : url,
        data :"position=" + position,
        async : false,
        success : function(data){
            document = data.data;
            if(data.result!==1){
                alert(data.message);
            }
        }
    });
    return document;
}
//根据id获取信息(教授团队和人才)
function getPeopleById(id,url){
    var document =null;
    //设置同步
    $.ajax({
        type : "get",
        url : url,
        data :"id=" + id,
        async : false,
        success : function(data){
            document = data.data;
            if(data.result!==1){
                alert(data.message);
            }
        }
    });
    return document;
}

//加载人才培养页面内容
function initPeopleList(position,urlGetPeopleByPosition,demoUrl) {
    var peopleList = getPeopleByPosition(position,urlGetPeopleByPosition);
    console.log(position);
    console.log(urlGetPeopleByPosition);
    console.log(peopleList);
    var htmlText= '';
    var crumbsSpan = '';
    var crumbsCon = '';
    //左侧悬浮框高亮提示
    for(var i = 1; i < $("#resultType>li").length; i++ ){
        if($('#resultType').find('span').eq(i).text() == position){
            $('#resultType').find('a.ll_ref').eq(i-1).css('background', '#6ad').parent().siblings().find("a").css('background', '#f3f2f2');
            break;
        }
    }
    //中间指引链接
    $("#crumbs").find('li').eq(3).html("");
    crumbsSpan+='<span>&nbsp;/&nbsp;</span>';
    $("#crumbs").find('li').eq(3).append(crumbsSpan);
    $("#crumbs").find('li').eq(4).html("");
    crumbsCon+='<a href="#'+i+'">'+position+'</a>';
    $("#crumbs").find('li').eq(4).append(crumbsCon);
    //加载内容
    for (var i=0;i<peopleList.length;i++){
        var imgPath ='/custom/images/person-img.jpg';
        if(peopleList[i].portrait!==undefined&&peopleList[i].portrait!==null&&peopleList[i].portrait!==0)
            imgPath= '/image/getPath?id='+peopleList[i].portrait;
// /ResearchProject/professor_demo.html?id=
        htmlText +='<li>' +
            '<a href="'+demoUrl+'?id='+peopleList[i].id+'">' +
            '   <img src="'+imgPath+
            '" alt="'+peopleList[i].name+'" width="120"></a>' +
            '<a href="'+demoUrl+'?id='+peopleList[i].id+'" class="team_name"><h4>'+peopleList[i].name+'</h4></a>' +
            '</li>';
    }
    var root = $("ul.team_content");
    //清除内容
    root.empty();
    root.append(htmlText);
    console.log(htmlText);
}
//监听请求更改--顶部导航栏
function clickFn(index){
    delCookie("Team_introduction");
    initTeamList(index);
};
//导航栏二级
$(".public-nav li:has(ul)").hover(function () {
    $(this).find("a:first").css("background-color", "#00428f");
    $(this).find(".subnav").css("display", "block");

}, function () {
    $(this).find("a:first").css("background-color", "#035ba6");
    $(this).find(".subnav").css("display", "none");
});

//加载招聘页面的pdf
function loadActivity(id) {
    var options = {
        pdfOpenParams: {
            pagemode: "thumbs",
            navpanes: 0,
            toolbar: 0,
            statusbar: 0
        }
    };
    var urlGetActivitiesById="/activity/getActivitiesById";
    var activity = getFileById(id,urlGetActivitiesById);

    var pdffile = "/home/pdf-error.pdf";
    var urlGetDocumentById="/file/getFile";
    var file = getFileById(activity.introduction,urlGetDocumentById);
    var path = file.path;
    if(path==null||path==undefined||path.length<1)alert("未获取到简介！请检查数据！");
    else pdffile = path;
    //PDFObject.embed(pdffile, "#example1");

    var myPDF = PDFObject.embed(pdffile, "#example1",options);
    var el = document.querySelector("#div-title");
    el.setAttribute("class", (myPDF) ? "success" : "fail");
    el.innerHTML = (myPDF) ? "<h1>"+activity.title+"</h1>" : "文档加载失败！";
}
//根据id获取document信息
function getFileById(id,urlGetFileById) {
    var file =null;
    //设置同步
    $.ajax({
        type : "get",
        url : urlGetFileById,
        data :"id=" + id,
        async : false,
        success : function(data){
            file = data.data;
            if(data.result!==1){
                alert(data.message);
            }
        }
    });
    return file;
}


//设置cookie
function setCookie(name, value, day) {
    var date = new Date();
    date.setDate(date.getDate() + day);
    document.cookie = name + '=' + value + ';expires=' + date;
};

//获取cookie
function getCookie(name) {
    var reg = RegExp(name + '=([^;]+)');
    var arr = document.cookie.match(reg);
    if (arr) {
        return arr[1];
    } else {
        return '';
    }
};

//删除cookie
function delCookie(name) {
    setCookie(name, null, -1);
};

$(document).ready(function () {
    $("#btn-global-search").click(function () {
        var keyWord = $("#btn-global-search-input").val();
        window.open("/Globalsearch.html?keyWord="+keyWord);
        //alert(keyWord);
    });

    $("#btn-global-search-input").keypress(function (e) {
        if (e.which == 13) {
            var keyWord = $("#btn-global-search-input").val();
            window.open("/Globalsearch.html?keyWord="+keyWord);
        }
    });
});
