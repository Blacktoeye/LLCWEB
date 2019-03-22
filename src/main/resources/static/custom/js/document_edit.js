/*
* 作者：ricardo
* 描述：文档编辑页面js
* 编写结构说明：
*     接口URL-->
*     页面加载-->
*     事件监听-->
*
**/

// //获取document对应的内容信息
// var urlGetDocumentById="/document/getDocumentById";
//保存document, 修改保存和新建保存， 新建保存时，因为文档id，没有，所以传到后台的id要<=0 !
var urlSave = "/document/save";

$(document).ready(function () {
    $('#summernote-div').summernote({
        lang: 'zh-CN',
        height: 500,                 // set editor height
        width: 800,
        //minHeight: 100,             // set minimum height of editor
        maxHeight: 800,             // set maximum height of editor
        maxWidth: 805,
        focus: true                  // set focus to editable area after initializing summernote
    });
    $('#summernote-div').summernote('code', content);
    //预览
    $("#btn-preview-document").click(function () {
        $('#document-preview').empty();
        var markupStr = $('#summernote-div').summernote('code');
        //console.log(markupStr);
        $('#document-preview').append(markupStr);
    });
    //清除编辑器内容
    $("#btn-clear-document").click(function () {
        $('#summernote-div').summernote('code', "");
    });
    //保存文档，
    $("#btn-save-document").click(function () {
        //检查参数
        //保存
        documentManage.save();
    });
    //保存文档，
    $("#btn-new-document").click(function () {
        window.open('/admin/edit.html');
    });
});

var documentManage = {
    save: function () {
        var document = {};
        document.id= $("#edit-id").val();
        document.content= $('#summernote-div').summernote('code');
        document.title= $("#edit-title").val();
        document.infor= $("#edit-infor").val();
        document.group= $("#edit-group").val();

        saveDocument(urlSave,document);
    },
    editDocument: function (item) {
        $.dialog.tips("edit test");
    },

    deleteDocument: function (item) {
        $.dialog.tips("delete test");
    }
};

//保存通用函数
function saveDocument(urlSave, myData) {
    $.ajax({
        type: "post",
        url: urlSave,
        data: myData,
        async: false,
        success: function (data) {
            $.dialog.tips(data.message);
            if(data.result===1)window.location.href='/admin/edit.html?id='+data.data.id;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $.dialog.alert("保存失败！");
        }
    });
}