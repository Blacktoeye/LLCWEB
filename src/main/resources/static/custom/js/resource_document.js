/*
* 作者：ricardo
* 描述：用户管理页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/
//获取文档分页数据
var urlGetPage="/document/page";
//获取document对应的内容信息
var urlGetDocumentById="/document/getDocumentById";

//添加document 传入的id<=0
//更改document信息
var urlEditDocumentById="/admin/edit.html";

//加载遮罩
var $wrapper = $('#document-table');
$(document).ready(function () {

    //document-table
    var $documentTable = $('#document-table');
    var  _table = $documentTable.dataTable(
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
                        data: "author",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            return data===null?"":data;
                        }
                    },
                    {

                        data: "title",
                        //width: "300px",
                        className: "ellipsis",	//文字过长时用省略号显示，CSS实现
                        render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
                    },
                    {

                        data: "infor",
                        width: "200px",
                        className: "ellipsis",	//文字过长时用省略号显示，CSS实现
                        render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS//会显示省略号的列，需要用title属性实现划过时显示全部文本的效果
                    },
                    {

                        data: "model",
                        width: "80px",
                        render: function (data, type, row, meta) {
                            return data===null?"":drawModel(data);
                        }
                    },
                    {
                        data: "createDate",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            return dateToString(data);
                        }
                    },
                    {
                        data: "modifyDate",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            return dateToString(data);
                        }

                    },
                    {
                        className: "td-operation",
                        data: null,
                        width: "100px",
                        defaultContent: "",
                        orderable: false
                    }
                ],
                "createdRow": function (row, data, index) {
                    //行渲染回调,在这里可以对该行dom元素进行任何操作
                    // //给当前行加样式
                    // if (data.isTaoliao) {
                    //     $(row).addClass("text-info");
                    // }
                    // //给当前行某列加样式
                     $('td', row).eq(5).addClass("text-info");
                     $('td', row).eq(6).addClass("text-warning");

                    //不使用render，改用jquery文档操作呈现单元格
                    var $btnEdit = $(' <button class="btn btn-warning btn-edit" type="button" data-toggle="tooltip"data-placement="bottom" title="编辑"> <i class="fa fa-edit"></i> </button>');
                    var $btnDelete = $('<button class="btn btn-danger  btn-delete" type="button" data-toggle="tooltip"data-placement="bottom" title="删除"> <i class="fa fa-trash-o"></i> </button>');

                    $('td', row).eq(7).append($btnEdit).append($btnDelete);

                },
                "drawCallback": function (settings) {
                    //默认选中第一行
                    $("tbody tr", $documentTable).eq(0).click();
                }
            })).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

    //行点击事件
    $("tbody", $documentTable).on("click", "tr", function (event) {
        $(this).addClass("active").siblings().removeClass("active");
        // $("table tr").css('background-color','white');
        // $(this).css('background-color','blue');
        //获取该行对应的数据
        var item = _table.row($(this).closest('tr')).data();
        documentManage.currentItem = item;
        documentManage.showDocument(item);
    });
    $documentTable.on("click", ".btn-edit", function () {
        //编辑按钮
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");

        documentManage.editDocument(item);
    }).on("click", ".btn-delete", function () {
        //删除按钮
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        documentManage.deleteDocument(item);
    });


    $("#btn-search").click(function () {
        documentManage.fuzzySearch=  true;
        _table.draw();
    });
    $("#btn-advanced-search").click(function () {
         documentManage.fuzzySearch=  false;
        //_table.draw();
    });
    $("#btn-add-document").click(function () {
        //转跳到文档编辑界面 ,新增文档传入id为0
        window.open(urlEditDocumentById+"?id=0");
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
    showDocument: function (item) {
        //$("#view-content").html("show content test<span style='color: #e12fab;'>span test"+item.id+"</span>");
        if(item!=null||item !=undefined){
            var document = getDocumentById(item.id,urlGetDocumentById);
            if(document!==null) $("#view-content").html(document.content);
            else $("#view-content").html("未找到文档内容！");
        }
        else $("#view-content").html("");
    },
    editDocument: function (item) {
        //$.dialog.tips("edit test");
        //转跳到文档编辑界面 ,新增文档传入id为0
        window.open(urlEditDocumentById+"?id="+item.id);
    },

    deleteDocument: function (item) {
        $.dialog.tips("delete test");
    }
};


