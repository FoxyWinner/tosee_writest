<html>
<#include "../common/header.ftlh">
<style>
    input[readonly],.form-control[readonly]{background-color: #ffffff;}
</style>
<body>
<div id="wrapper" class="toggled">
<#--    边栏sidebar-->
    <#include "../common/nav.ftlh">
<#--    主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">


<#--                    <th>所属子题库</th>-->
<#--                    <th>题目类型</th>-->
<#--                    <th>序号</th>-->
<#--                    <th>题干</th>-->
<#--                    <th>选项</th>-->
<#--                    <th>答案</th>-->
<#--                    <th>解析</th>-->
<#--                    <th colspan="2">操作</th>-->



<#--                    <table class="table table-bordered table-hover table-condensed">-->
<#--                        <thead>-->
<#--                        <tr>-->
<#--&lt;#&ndash;                            <th>题目ID</th>&ndash;&gt;-->
<#--                            <th>所属子题库</th>-->
<#--                            <th>题目类型</th>-->
<#--                            <th>序号</th>-->
<#--                            <th>题干</th>-->
<#--                            <th>选项</th>-->
<#--                            <th>答案</th>-->
<#--                            <th>解析</th>-->
<#--                            <th colspan="2">操作</th>-->
<#--                        </tr>-->
<#--                        </thead>-->
<#--                        <tbody>-->
                        <#list questionDTOPage.content as questionDTO>
                            <div class="list-group">
                                <a href="/toseewritest/operator/questionbank/questionlist?childQbId=${questionDTO.childQbId}" class="list-group-item active">${questionDTO.childQbTitle}
                                </a>
                                <div class="list-group-item">
                                    <span class="label label-info">${questionDTO.getQuestionTypeEnum().message}</span>
                                    <span class="badge">第${questionDTO.questionSeq}题</span>
                                </div>
                                <div class="list-group-item">
                                    <h5 class="list-group-item-heading">
                                        题干：
                                            ${questionDTO.questionStem?no_esc}
                                    </h5>

                                    <p class="list-group-item-text">
                                        <br>
                                        <#if questionDTO.questionOptions??>
                                            <#assign optionsJson="${questionDTO.getOptionsJson()}"?eval />
                                            <#list optionsJson as optionJson>
                                            <div class="form-group">
                                                <div class="input-group" >
                                                    <span class="input-group-addon">${optionJson.optionName}</span>
                                                    <input type="text" class="form-control"  value="${optionJson.optionValue}" readonly>
                                                </div>
                                            </div>
                                            </#list>
                                        </#if>

                                    </p>
                                </div>
                                <div class="list-group-item">
                                    <span class="badge">答案</span> ${questionDTO.answer}
                                </div>
                                <div class="list-group-item"> <span class="badge">解析</span> ${questionDTO.explanation?no_esc}</div>

                                <div class="list-group-item">
                                    <a class="btn btn-default" href="/toseewritest/operator/questionbank/questionindex?questionId=${questionDTO.questionId}" >修改</a>
                                    <a class="btn btn-danger" href="/toseewritest/operator/questionbank/questiondelete?questionId=${questionDTO.questionId}">删除</a>
                                </div>

                            </div>

                        <#--                            <tr>-->
<#--&lt;#&ndash;                                <td>${questionDTO.questionId}</td>&ndash;&gt;-->
<#--                                <td>${questionDTO.childQbTitle}</td>-->
<#--                                <td>${questionDTO.getQuestionTypeEnum().message}</td>-->
<#--                                <td>${questionDTO.questionSeq}</td>-->
<#--                                <td>${questionDTO.questionStem}</td>-->
<#--                                <td>-->
<#--                                    <#if questionDTO.questionOptions??>-->
<#--                                        <#list questionDTO.questionOptions as questionOption>-->
<#--                                            <span class="label label-primary">${questionOption.optionName}</span> : ${questionOption.optionValue} ; <br>-->
<#--                                        </#list>-->
<#--                                    </#if>-->
<#--                                </td>-->
<#--                                <td>${questionDTO.answer}</td>-->
<#--                                <td>${questionDTO.explanation}</td>-->
<#--                                <td>-->
<#--                                    <a href="/toseewritest/operator/questionbank/questionindex?questionId=${questionDTO.questionId}" >修改</a>-->
<#--                                </td>-->
<#--&lt;#&ndash;                                <td>&ndash;&gt;-->
<#--&lt;#&ndash;                                    <a href="/toseewritest/operator/questionbank/questionindextest?questionId=${questionDTO.questionId}" >修改（测试用）</a>&ndash;&gt;-->
<#--&lt;#&ndash;                                </td>&ndash;&gt;-->

<#--                                <td>-->
<#--                                    <a href="/toseewritest/operator/questionbank/questiondelete?questionId=${questionDTO.questionId}">删除</a>-->
<#--                                </td>-->
<#--                            </tr>-->
                            <br>
                        </#list>
<#--                        </tbody>-->
<#--                    </table>-->
                </div>
                <#--        分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                        <#if currentPage lte 1>
                            <li class="disabled"><a href="#">上页</a></li>
                        <#else>
                            <#if currentchildQbId?? >
                                <li><a href="/toseewritest/operator/questionbank/questionlist?childQbId=${currentchildQbId}&page=${currentPage - 1}&size=${size}">上页</a></li>
                            <#else>
                                <li><a href="/toseewritest/operator/questionbank/questionlist?page=${currentPage - 1}&size=${size}">上页</a></li>
                            </#if>
                        </#if>
                        <#list 1..questionDTOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <#if currentchildQbId?? >
                                    <li><a href="/toseewritest/operator/questionbank/questionlist?childQbId=${currentchildQbId}&page=${index}&size=${size}">${index}</a></li>
                                <#else>
                                    <li><a href="/toseewritest/operator/questionbank/questionlist?page=${index}&size=${size}">${index}</a></li>
                                </#if>
                            </#if>
                        </#list>

                        <#if currentPage gte questionDTOPage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                                <#if currentchildQbId?? >
                                    <li><a href="/toseewritest/operator/questionbank/questionlist?childQbId=${currentchildQbId}&page=${currentPage + 1}&size=${size}">下页</a></li>
                                <#else>
                                    <li><a href="/toseewritest/operator/questionbank/questionlist?page=${currentPage + 1}&size=${size}">下页</a></li>
                                </#if>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
