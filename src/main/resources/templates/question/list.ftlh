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
                <#--        列表-->
                <div class="col-md-12 column">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <th>题目ID</th>
                            <th>所属子题库</th>
                            <th>题目类型</th>
                            <th>序号</th>
                            <th>题干</th>
                            <th>选项</th>
                            <th>答案</th>
                            <th>解析</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list questionDTOPage.content as questionDTO>
                            <tr>
                                <td>${questionDTO.questionId}</td>
                                <td>${questionDTO.childQbId}</td>
                                <td>${questionDTO.getQuestionTypeEnum().message}</td>
                                <td>${questionDTO.questionSeq}</td>
                                <td>${questionDTO.questionStem}</td>
                                <td>
                                    <#if questionDTO.questionOptions??>
                                        <#list questionDTO.questionOptions as questionOption>
                                            <span class="label label-primary">${questionOption.optionName}</span> : ${questionOption.optionValue} ; <br>
                                        </#list>
                                    </#if>
                                </td>
                                <td>${questionDTO.answer}</td>
                                <td>${questionDTO.explanation}</td>
i
                                <td>
                                    <a href="/toseewritest/operator/questionbank/questionindex?questionId=${questionDTO.questionId}" >修改</a>
                                </td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/questiondelete?questionId=${questionDTO.questionId}">删除</a>
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
                            <li><a href="/tosee_writest/operator/questionbank/questionlist?page=${currentPage - 1}&size=${size}">上页</a></li>
                        </#if>
                        <#list 1..questionDTOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/tosee_writest/operator/questionbank/questionlist?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte questionDTOPage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                            <li><a href="/tosee_writest/operator/questionbank/questionlist?page=${currentPage + 1}&size=${size}">下页</a></li>
                        </#if>

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>
