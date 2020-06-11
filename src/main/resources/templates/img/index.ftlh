<html>
<#include "../common/header.ftlh">
<#--bootstrap fileinput css-->
<link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />

<body>
<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftlh">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                            <form role="form" id="imgForm" >
                                <div class="form-group">
                                    <label>图床： 将图片上传我们的图片服务器并转为URL</label>
                                    <input class="form-control" id="imgUrl" name="s" type="text" readonly/>
                                    <div class="help-block with-errors"></div>
                                    <div class="file-loading">
                                        <input id="imgInput" type="file">
                                        <p class="help-block">支持jpg、jpeg、png、gif格式，大小不超过1M</p>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <p class="help-block">支持jpg、jpeg、png、gif格式，大小不超过1M</p>
                                    <p class="help-block">这个页面暂时只用于前端打卡图片的存储</p>
                                </div>
                            </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.8/js/fileinput.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap-fileinput/4.4.8/js/locales/zh.min.js"></script>
<script>

    // $.fn.validator.Constructor.INPUT_SELECTOR = ' [type="submit"], [type="reset"], button)'
    $(function () {
        var initialPreview = [];

        $("#imgInput").fileinput({
            uploadUrl: '/toseewritest/image/upload',
            dropZoneEnabled: false,
            language: 'zh',
            browseClass: "btn btn-primary btn-block",
            showCaption: false,
            showRemove: false,
            showUpload: false,
            allowedFileExtensions: [ 'jpg', 'jpeg', 'png', 'gif' ],
            maxFileSize: 1024,
            autoReplace: true,
            overwriteInitial: true,
            maxFileCount: 1,
            initialPreview: initialPreview,
        });
    });
    //上传完成设置表单内容
    $('#imgInput').on('fileuploaded', function(event, data, previewId, index) {
        if (data.response.code != 0) {
            alert(data.response.msg)
            return
        }
        $('#imgUrl').val(data.response.data.fileName)
    });
</script>