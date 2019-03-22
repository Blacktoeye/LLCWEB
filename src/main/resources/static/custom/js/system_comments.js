/*
 * 作者：ricardo
 * 描述：专利管理页面js
 * 编写结构说明：
 *     接口URL-->
 *     页面加载-->
 *     事件监听-->
 *
 **/
//获取留言分页数据
var urlGetPage = "";
//更改
var urlSave = "";
//删除
var urlDelete = "";
var urlGetAllDocuments = "/document/getAll";
//加载遮罩
var $wrapper = $('#comment-table');

$(document).ready(function() {

    //comment-table
    var $commentTable = $('#comment-table');
    var _table = $commentTable.dataTable(
        $.extend(true, {}, CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
            ajax: function(data, callback, settings) { //ajax配置为function,手动调用异步查询
                //手动控制遮罩
                $wrapper.spinModal();
                //封装请求参数
                var param = usersManage.getQueryCondition(data);
                $.ajax({
                    type: "GET",
                    url: urlGetPage,
                    cache: false, //禁用缓存
                    data: param, //传入已封装的参数
                    dataType: "json",
                    success: function(result) {
                        //异常判断与处理
                        if (result.errorCode) {
                            $.dialog.alert("查询失败。错误码：" + result.errorCode);
                            return;
                        }
                        //封装返回数据，这里仅演示了修改属性名
                        var returnData = {};
                        returnData.draw = result.draw;
                        returnData.recordsTotal = result.total;
                        returnData.recordsFiltered = result.total; //后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.pageData;
                        //关闭遮罩
                        $wrapper.spinModal(false);
                        callback(returnData);
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        $.dialog.alert("查询失败");
                        $wrapper.spinModal(false);
                    }
                });
            },
            columns: [{
                    data: "id",
                    width: "45px"
                },
                {
                    data: "name",
                    width: "80px"
                },
                {
                    data: "content",
                    width: "150px",
                    className: "ellipsis",
                    render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS
                },
                {
                    data: "contact",
                    width: "80px"
                }, {
                    data: "date",
                    width: "120px",
                    render: function(data, type, row, meta) {
                        return dateToString(data);
                    }
                },
                {
                    data: "is_view",
                    width: "60px",
                    render: function(data, type, row, meta) {
                        if (data == 1) {
                            return '<span class="label label-sm label-success">是</span>';
                        } else {
                            return '<span class="label label-sm label-warning">否</span>';
                        }
                    }
                },

                {
                    className: "td-operation",
                    data: null,
                    width: "80px",
                    defaultContent: "",
                    orderable: false
                }
            ],
            "createdRow": function(row, data, index) {
                //不使用render，改用jquery文档操作呈现单元格                   
                var $btnDelete = $('<button class="btn btn-danger  btn-delete" type="button" data-toggle="tooltip"data-placement="bottom" title="删除"> <i class="fa fa-trash-o"></i> </button>');

                $('td', row).eq(6).append($btnEdit).append($btnDelete);
            },

        })).api(); //此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

    //行点击事件
    $("tbody", $commentTable).on("click", "tr", function(event) {
        $(this).addClass("active").siblings().removeClass("active"); //有空再试试渲染选中行
        //$(this).addClass("active").siblings("tr").removeClass("active");
        // $("table tr").css('background-color','white');
        // $(this).css('background-color','blue');
        //获取该行对应的数据
        var item = _table.row($(this).closest('tr')).data();
        usersManage.currentItem = item;
        usersManage.show(item);
    });
    $commentTable.on("click", ".btn-delete", function() {
        //删除按钮
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        usersManage.delete(item);
    });
    $("#btn-advanced-search").click(function() {
        _table.draw();
    });
    刷新按钮
    $("#btn-refresh-selector").click(function() {
        usersManage.initCompenets();
    });
    usersManage.initCompenets();

});


var usersManage = {
    currentItem: null,
    fuzzySearch: true,
    //渲染页面控件
    initCompenets: function() {

        //渲染document选择框
        var documentList = getAllDocuments(urlGetAllDocuments);
        var selector = $("#add-introduction");
        selector.empty();
        var options = "";
        for (var i = 0; i < documentList.length; i++) {
            //这里的是否空闲渲染存在问题
            options += "<option" +
                " value='" + documentList[i].id + "'>" + documentList[i].title +
                "</option>";
        }
        selector.append(options);
        selector.selectpicker('refresh');
    },
    getQueryCondition: function(data) {
        var param = {};
        param.username = $("#search-name").val();
        //模糊查找标志，当有高级查找时，标记为false
        param.fuzzySearch = usersManage.fuzzySearch;
        param.fuzzy = $("#search-name").val();
        //组装分页参数
        param.startIndex = data.start;
        param.pageSize = data.length;
        param.draw = data.draw;
        return param;
    },
    show: function(item) {
        $("#view-name").text(item.name);
        $("#view-content").text(item.content);
        $("#view-concact").text(item.concact);
        $("#view-date").text(dateToString(item.date));
    },
    delete: function(item) {
        var message = "确定删除此留言?";
        deleteFun(message, urlDelete, item.id);
    }
};