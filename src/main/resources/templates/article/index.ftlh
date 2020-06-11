<html>
<#include "../common/header.ftlh">
<#--bootstrap fileinput css-->
<link href="https://cdn.bootcss.com/bootstrap-fileinput/4.4.7/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<#--layui css-->
<link rel="stylesheet" href="/toseewritest/layui/css/layui.css"  media="all">

<body>
<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftlh">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">

                    <form role="form" method="post" action="/toseewritest/operator/experience/articlesave" id="articleForm" data-toggle="validator">
                        <div class="form-group">
                            <label>文章名</label>
                            <input name="articleTitle" type="text" class="form-control" data-error="文章名必填" required value="${(experienceArticle.articleTitle)!''}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>图片</label>
                            <input id="articleIcon" name="articleIcon" type="text" hidden="hidden" value="${(experienceArticle.articleIcon)!''}" data-validate="true" data-error="图片必上传" required />
                            <div class="help-block with-errors"></div>
                            <div class="file-loading">
                                <input id="articleIconInput" type="file">
                                <p class="help-block">支持jpg、jpeg、png、gif格式，大小不超过1M</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>标签</label>
                            <select name="articleTagId" id="articleTag" class="form-control" data-error="标签必选" required>
                                <option value="">请选择标签</option>
                                <#list articleTagList as articleTag>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option value="${articleTag.tagId}"
                                            <#if ((experienceArticle.articleTagId)?? && experienceArticle.articleTagId == articleTag.tagId)>
                                                selected
                                            </#if>
                                    >${articleTag.tagName}
                                    </option>
                                </#list>
                            </select>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>第二标签</label>
                            <input name="secondTag" type="text" class="form-control"  value="${(experienceArticle.secondTag)!''} "/>
                            <div class="help-block with-errors"></div>
                        </div>



                        <div class="form-group">
                            <label>作者</label>
                            <input name="author" type="text" class="form-control"  value="${(experienceArticle.author)!''}" data-error="作者必填" required/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>浏览数</label>
                            <input name="readerNumber" type="text" class="form-control"  value="${(experienceArticle.readerNumber?c)!'0'}" data-error="浏览数必填" required/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>内容</label>
                            <p class="help-block" id="options-help-block">
                                内容可以暂时为空，但不建议输入内容为空的文章。
                            </p>
                            <textarea name="content" id="content" class="form-control" rows="20" onchange="">${(experienceArticle.content)!''}</textarea>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>发布时间</label>
                            <#--                            这个要给人呈现个默认的-->
                            <input name="relaseTime" type="text" class="form-control" data-error="发布时间必填" required value="${(childQuestionBank.relaseTime)!.now?string("yyyy-MM-dd")}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <div class="checkbox">
                                <label><input name="isRecommended" type="checkbox"
                                            <#if experienceArticle?? && experienceArticle.isRecommended == 1>
                                                checked="checked"
                                            </#if>
                                    />推荐该文章</label>
                            </div>
                        </div>

                        <#--                        隐藏的字段 为了传给save-->
                        <input hidden type="text" name="articleId" value="${(experienceArticle.articleId)!''}">
                        <button type="submit" class="btn btn-danger">提交</button>

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
        if ('${(experienceArticle.articleIcon)!""}' != '') {
            initialPreview = "<img class='kv-preview-data file-preview-image' src='${(experienceArticle.articleIcon)!""}'>"
        }

        $("#articleIconInput").fileinput({
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
    $('#articleIconInput').on('fileuploaded', function(event, data, previewId, index) {
        if (data.response.code != 0) {
            alert(data.response.msg)
            return
        }
        $('#articleIcon').val(data.response.data.fileName)
        $("#articleForm").validator('update');
    });
</script>
<script src="/toseewritest/layui/layui.js" charset="utf-8"></script>
<script>
    layui.use('layedit', function()
    {
        var layedit = layui.layedit
            ,$ = layui.jquery;

        layedit.set({
            uploadImage: {
                url: '/toseewritest/image/richtextupload' //接口url
                ,type: 'post'
            }
        });

        var index = layedit.build('content', {
            tool: [  'strong','italic','underline','del','|','left','center','right','link','unlink','image']
        });


        //编辑器外部操作
        // var active = {
        //     content: function(){
        //         alert(layedit.getContent(index)); //获取编辑器内容
        //     }
        //     ,text: function(){
        //         alert(layedit.getText(index)); //获取编辑器纯文本内容
        //     }
        //     ,selection: function(){
        //         alert(layedit.getSelection(index));
        //     }
        // };

        // $('.site-demo-layedit').on('click', function(){
        //     var type = $(this).data('type');
        //     active[type] ? active[type].call(this) : '';
        // });

    });
</script>