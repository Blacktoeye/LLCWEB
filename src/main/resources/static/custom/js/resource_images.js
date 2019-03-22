/*
* 作者：ricardo
* 描述：图片管理页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
**/
//获取图片分页数据
var urlGetPage="/image/page";
//获取图片
var urlGetImageById="/image/getImageById";
var urlDelete = "/image/delete";
var urlUpload="/image/save";
//加载遮罩
var $wrapper = $('#images-table');
$(function(){
    $('#lightBoxTest').Chocolat({
        container      : '#lightBox',
        imageSize     : 'cover',
    });
});
$(document).ready(function () {
    //images-table
    var $imagesTable = $('#images-table');
    var  _table = $imagesTable.dataTable(
        $.extend(true, {}, CONSTANT.DATA_TABLES.DEFAULT_OPTION,
            {
                ajax: function (data, callback, settings) {//ajax配置为function,手动调用异步查询
                    //手动控制遮罩
                    $wrapper.spinModal();
                    //封装请求参数
                    var param = documentManage.getQueryCondition(data);
                    $.ajax({
                        type: "GET",
                        url: urlGetPage,
                        cache: false,	//禁用缓存
                        data: param,	//传入已封装的参数
                        dataType: "json",
                        success: function (result) {
                            //异常判断与处理
                            if (result.errorCode) {
                                $.dialog.alert("查询失败。错误码：" + result.errorCode);
                                return;
                            }
                            //封装返回数据，这里仅演示了修改属性名
                            var returnData = {};
                            returnData.draw = result.draw;
                            returnData.recordsTotal = result.total;
                            returnData.recordsFiltered = result.total;//后台不实现过滤功能，每次查询均视作全部结果
                            returnData.data = result.pageData;
                            //关闭遮罩
                            $wrapper.spinModal(false);
                            //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                            //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                            callback(returnData);
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $.dialog.alert("查询失败");
                            $wrapper.spinModal(false);
                        }
                    });
                },
                //空值的column会引发dataTable 报warning ：DataTables warning: table id=document-table - Requested unknown parameter 'author' for row 3.
                columns: [
                    {
                        data: "id",
                        width: "80px"
                    },
                    {
                        data: "path",
                        width: "300px",
                        render: function (data, type, row, meta) {
                            return (data===null||data==undefined)?'<i class="fa fa-file-image-o"></i>图片不存在':'<a class="chocolat-image" target="_blank"' +
                                'href="/'+data+'"><img src="/'+data+'" class="table-img"/></a>';
                        }
                    },
                    {
                        data: "description",
                        width: "200px",
                        className: "ellipsis",	//文字过长时用省略号显示，CSS实现
                        render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
                    },
                    {
                        data: "createDate",
                        width: "80px",
                        render: function (data, type, row, meta) {
                            return dateToString(data);
                        }
                    },
                    {
                        data: "path",
                        render: function (data, type, row, meta) {
                            return (data===null||data==undefined)?'<span class="text-danger">数据错误！</span>':'<a href="/'+data+'">/'+data+'</a>';
                        }
                    },
                    {
                        className: "td-operation",
                        data: null,
                        width: "200px",
                        defaultContent: "",
                        orderable: false
                    }
                ],
                "createdRow": function (row, data, index) {

                    //不使用render，改用jquery文档操作呈现单元格
                    var $btnCopy = $('<button class="btn btn-info btn-copy" type="button" data-toggle="tooltip"data-placement="bottom" title="复制图片地址"> <i class="fa fa-copy"></i> </button>');
                    var $btnReview = $('<button class="btn btn-success btn-review" type="button" data-toggle="tooltip"data-placement="bottom" title="预览"> <i class="fa fa-file-image-o"></i> </button>');
                    var $btnEdit = $(' <button class="btn btn-warning btn-edit" type="button" data-toggle="tooltip"data-placement="bottom" title="编辑"> <i class="fa fa-edit"></i> </button>');
                    var $btnDelete = $('<button class="btn btn-danger  btn-delete" type="button" data-toggle="tooltip"data-placement="bottom" title="删除"> <i class="fa fa-trash-o"></i> </button>');

                    $('td', row).eq(5).append($btnCopy).append($btnReview).append($btnEdit).append($btnDelete);

                },
                "drawCallback": function (settings) {
                    //默认选中第一行
                    $("tbody tr", $imagesTable).eq(0).click();
                }
            })).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

    $imagesTable.on("click", ".btn-copy", function () {
        //复制图片url
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        copyUrls("/"+item.path);
    }).on("click", ".btn-review", function () {
        //预览图片
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        document.getElementById("img-preview").src="/"+item.path;
        document.getElementById("img-preview-a").href="/"+item.path;
    }).on("click", ".btn-edit", function () {
        //编辑图片信息
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        documentManage.editDocument(item);
        //显示图片上传页面
        $("#div-upload-image").slideToggle("fast");

    }).on("click", ".btn-delete", function () {
        //删除图片
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        documentManage.deleteDocument(item);
        _table.draw();
    });


    $("#btn-search").click(function () {
        documentManage.fuzzySearch=  true;
        _table.draw();
    });
    $("#btn-advanced-search").click(function () {
         documentManage.fuzzySearch=  false;
        _table.draw();
    });
    $("#btn-add-document").click(function () {
       //显示上传页面
        $("#div-input-id").hide();
        $("#input-id").val("");
        $("#input-description").val("");
        $("#input-group").val("");
        $("#div-upload-image").slideToggle("fast");
    });
    $("#input-upload-summit").click(function () {
        documentManage.uploadFile();
        _table.draw();
    });
});


var documentManage = {
    currentItem: null,
    fuzzySearch: true,
    getQueryCondition: function (data) {
        var param = {};
        //模糊查找标志，当有高级查找时，标记为false
        param.fuzzySearch = documentManage.fuzzySearch;
        param.fuzzy = $("#search-value").val();
        //高级查找封装参数(待完成)

        //组装分页参数
        param.startIndex = data.start;
        param.pageSize = data.length;
        param.draw = data.draw;
        return param;
    },
    uploadFile: function () {
        //手动控制遮罩
        $wrapper.spinModal();
        var formData = new FormData();
        formData.append("file",$("#btn-upload-image")[0].files[0]);
        formData.append("description",$("#input-description").val());
        formData.append("group",$("#input-group").val());
        formData.append("id",$("#input-id").val());

        $.ajax({
            url: urlUpload,
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false
        }).done(function(res) {
        }).fail(function(res) {});
        //关闭遮罩
        $wrapper.spinModal(false);
    },
    editDocument: function (item) {
        $("#div-input-id").hide();
        $("#input-id").val(item.id);
        //$("#btn-upload-image").val(item.originalName);
        $("#input-description").val(item.description);
        $("#input-group").val(item.model);
    },
    saveDocument: function (item) {

    },
    deleteDocument: function (item) {
        //$.dialog.tips("delete test");
        var message = "确定删除:"+item.originalName+"?";
        deleteFun(message,urlDelete,item.id);
    }
};

