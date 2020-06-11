<html>
<#include "../common/header.ftlh">
<body>
<div id="wrapper" class="toggled">
<#--    边栏sidebar-->
    <#include "../common/nav.ftlh">
<#--    主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>图标</th>
                            <th>文章名</th>
                            <th>标签</th>
                            <th>第二标签</th>
                            <th>发布时间</th>
                            <th>作者</th>
                            <th>浏览数</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list articlePage.content as article>
                            <tr>
                                <td><img height="100" width="100" src="${article.articleIcon}" alt=""></td>
                                <td>${article.articleTitle}</td>
                                <td>${article.articleTagName}</td>
                                <td>${article.secondTag}</td>
                                <td>${article.relaseTime}</td>
                                <td>${article.author}</td>
                                <td>${article.readerNumber}</td>
                                <td>
                                    <a class="btn btn-default" href="/toseewritest/operator/experience/articleindex?articleId=${article.articleId}" >修改</a>

                                <td>
                                    <a class="btn btn-danger" disabled href="/toseewritest/operator/experience/questiondelete?deletearticle?articleId=${article.articleId}">删除</a>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <#--        分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled"><a href="#">上页</a></li>
                        <#else>
                            <li><a href="/toseewritest/operator/experience/articlelist?page=${currentPage - 1}&size=${size}">上页</a></li>
                        </#if>
                        <#list 1..articlePage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/toseewritest/operator/experience/articlelist?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte articlePage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                            <li><a href="/toseewritest/operator/experience/articlelist?page=${currentPage + 1}&size=${size}">下页</a></li>

                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
