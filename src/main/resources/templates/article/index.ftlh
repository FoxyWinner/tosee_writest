<html>
<#include "../common/header.ftlh">

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
                            <img height="100" width="100" src="${(experienceArticle.articleIcon)!''}" alt="">
                            <input name="articleIcon" type="text" class="form-control" value="${(experienceArticle.articleIcon)!''}" data-error="URL必填" required/>
                            <div class="help-block with-errors"></div>
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

                        <div class="form-group" id="questionOptionsDiv">
                            <label>内容</label>
                            <textarea name="content" id="content" class="form-control" rows="20" data-error="内容必填" required>${(experienceArticle.content)!''}</textarea>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>发布时间</label>
                            <#--                            这个要给人呈现个默认的-->
                            <input name="relaseTime" type="text" class="form-control" value="${(childQuestionBank.relaseTime)!.now?string("yyyy-MM-dd")}" data-error="发布时间必填且符合格式" pattern="yyyy-MM-dd"/>
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

<script type="text/javascript">
</script>