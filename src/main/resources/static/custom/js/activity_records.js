/*
* 作者：ricardo
* 描述：专利管理页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/
//获取专利分页数据
var urlGetPage="/activity/page";
//更改
var urlSave="/activity/save";
//删除
var urlDelete="/activity/delete";
//获取document对应的内容信息
var urlGetDocumentById="/document/getDocumentById";


var urlGetAllDocuments="/document/getAll";
//加载遮罩
var $wrapper = $('#paper-table');

$(document).ready(function () {

    //paper-table
    var $paperTable = $('#paper-table');
    var  _table = $paperTable.dataTable(
        $.extend(true, {}, CONSTANT.DATA_TABLES.DEFAULT_OPTION,
            {
                ajax: function (data, callback, settings) {//ajax配置为function,手动调用异步查询
                    //手动控制遮罩
                    $wrapper.spinModal();
                    //封装请求参数
                    var param = usersManage.getQueryCondition(data);
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
                            callback(returnData);
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            $.dialog.alert("查询失败");
                            $wrapper.spinModal(false);
                        }
                    });
                },
                columns: [
                    {
                        data: "title"
                    },
                    {
                        data: "author",
                        width: "100px"
                    },
                    {
                        data: "peopleList",
                        width: "200px",
                        className: "ellipsis",
                        render: CONSTANT.DATA_TABLES.RENDER.ELLIPSIS
                    },
                    {
                        data: "activityType",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            var content;
                            if(data==='活动')content ='<span class="text-info">'+data+'</span>';
                            else if(data==='会议')content ='<span class="text-success">'+data+'</span>';
                            else if(data==='通知')content ='<span class="text-warning">'+data+'</span>';
                            else content ='<span class="text-danger">'+data+'</span>';
                            return content;
                        }
                    },
                    {
                        data: "startDate",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            return dateToString(data);
                        }
                    },
                    {
                        data: "endDate",
                        width: "100px",
                        render: function (data, type, row, meta) {
                            return dateToString(data);
                        }
                    },
                    {
                        data: "model",
                        width: "100px"
                    },
                    {
                        data: "isPublish",
                        width: "80px",
                        render: function (data, type, row, meta) {
                            return (data===1)?'<span class="text-success">是</span>':'<span class="text-danger">否</span>';
                        }
                    },
                    {
                        className: "td-operation",
                        data: null,
                        width: "120px",
                        defaultContent: "",
                        orderable: false
                    }
                ],
                "createdRow": function (row, data, index) {
                    //不使用render，改用jquery文档操作呈现单元格
                    var $btnEdit = $(' <button class="btn btn-warning btn-edit" type="button" data-toggle="tooltip"data-placement="bottom" title="编辑"> <i class="fa fa-edit"></i> </button>');
                    var $btnDelete = $('<button class="btn btn-danger  btn-delete" type="button" data-toggle="tooltip"data-placement="bottom" title="删除"> <i class="fa fa-trash-o"></i> </button>');

                    $('td', row).eq(8).append($btnEdit).append($btnDelete);
                },
                "drawCallback": function (settings) {
                    //默认选中第一行
                    $("tbody tr", $paperTable).eq(0).click();
                }
            })).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象

    //行点击事件
    $("tbody", $paperTable).on("click", "tr", function (event) {
        $(this).addClass("active").siblings().removeClass("active");//有空再试试渲染选中行
        //$(this).addClass("active").siblings("tr").removeClass("active");
        // $("table tr").css('background-color','white');
        // $(this).css('background-color','blue');
        //获取该行对应的数据
        var item = _table.row($(this).closest('tr')).data();
        usersManage.currentItem = item;
        usersManage.show(item);
    });
    $paperTable.on("click", ".btn-edit", function () {
        //编辑按钮
        var item = _table.row($(this).closest('tr')).data();
        usersManage.edit(item);
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        //切换panel
        $("#add-panel").show().siblings(".panel").hide();
    }).on("click", ".btn-delete", function () {
        //删除按钮
        var item = _table.row($(this).closest('tr')).data();
        $(this).closest('tr').addClass("active").siblings().removeClass("active");
        usersManage.delete(item);
    });


    $("#btn-advanced-search").click(function () {
        _table.draw();
    });

    $("#btn-add-user").click(function () {
        usersManage.add();
        $("#add-panel").slideToggle("fast");
        $("#add-panel").show().siblings(".panel").hide();
    });
    $("#btn-add-save").click(function () {
        usersManage.save();
        _table.draw();
    });
    $("#btn-add-cancel").click(function () {
        $("#add-panel").slideToggle("fast");
        //$("#add-panel").show().siblings(".panel").show();
    });

    $("#btn-refresh-selector").click(function () {
        usersManage.initCompenets();
    });
    $("#btn-edit-document").click(function () {
        var id = $("#add-introduction").val();
        if(id==null||id==undefined||id.length==0)
            id = '0';
        window.open('/admin/edit.html?id='+id);
    });

    usersManage.initCompenets();
});


var usersManage = {
    currentItem: null,
    fuzzySearch: true,
    //渲染页面控件
    initCompenets:function(){

        //渲染document选择框
        var documentList = getAllDocuments(urlGetAllDocuments);
        var selector = $("#add-introduction");
        selector.empty();
        var options = "";
        for (var i=0; i<documentList.length;i++){
            //这里的是否空闲渲染存在问题
            options+= "<option"+
                " value='"+documentList[i].id+ "'>"+documentList[i].title
                +"</option>";
        }
        selector.append(options);
        selector.selectpicker('refresh');
    },
    getQueryCondition: function (data) {
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
    show: function (item) {
        var document = getDocumentById(item.introduction,urlGetDocumentById);
        var introduction;
        if(document===null||document==undefined||document.content==null||document.content==undefined)
            introduction=item.title+"<span style='color: red;'>该项目没有简介！</span>";
        else introduction= document.content;
        $('#document-preview').html(introduction);
    },
    add:function () {
        $("#add-panel-title").text("添加活动");
        $("#add-id").val(0);
        $("#add-introduction").val();

        $("#add-people_list").val();
        $("#add-title").val();
        $("#add-group").val();
        $("#add-is_publish").val();
        $("#add-activity_type").val();
        $("#add-start_date").val();
        $("#add-end_date").val();
    },
    edit: function (item) {
        $("#add-panel-title").text("编辑活动");
        $("#add-id").val(item.id);
        $("#add-introduction").val(item.introduction);

        $("#add-people_list").val(item.peopleList);
        $("#add-title").val(item.title);
        $("#add-group").val(item.model);
        $("#add-is_publish").val(item.isPublish);
        $("#add-activity_type").val(item.activityType);
        $("#add-start_date").val(dateToString(item.startDate));
        $("#add-end_date").val(dateToString(item.endDate));
        $("#add-introduction").selectpicker('refresh');
        $("#add-group").selectpicker('refresh');
        $("#add-is_publish").selectpicker('refresh');
        //更多
    },
    save: function () {
        var activity={};
        activity.id=$("#add-id").val();
        activity.peopleList=$("#add-people_list").val();
        activity.title=$("#add-title").val();
        activity.model=$("#add-group").val();
        activity.isPublish=$("#add-is_publish").val();
        activity.activityType=$("#add-activity_type").val();

        activity.startDate=$("#add-start_date").val();
        activity.endDate= $("#add-end_date").val();

        activity.introduction=$("#add-introduction").val();
        saveFun(urlSave,activity)
    },
    delete: function (item) {
        var message ="确定删除项目：“"+item.title+"”的信息?";
        deleteFun(message,urlDelete,item.id);
    }
};

