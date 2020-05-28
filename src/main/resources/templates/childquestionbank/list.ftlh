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
                            <th>所属父题库</th>
<#--                            <th>子题库ID</th>-->
                            <th>子题库名</th>
                            <th>题目数</th>
                            <th>模拟时长</th>
                            <th>热度</th>
                            <th>发布时间</th>
                            <th colspan="3">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list childQuestionBankPage.content as childQuestionBank>
                            <tr>
                                <td>${childQuestionBank.parentQbTitle}</td>
<#--                                <td>${childQuestionBank.childQbId}</td>-->
                                <td>${childQuestionBank.cqbTitle}</td>
                                <td>${childQuestionBank.questionNumber}</td>
                                <td>${childQuestionBank.simulationTime}分钟</td>
                                <td>${childQuestionBank.cqbHeat}</td>
                                <td>${childQuestionBank.relaseTime}</td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/cqbindex?childQbId=${childQuestionBank.childQbId}" >修改</a>
                                </td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/cqbrecommend?childQbId=${childQuestionBank.childQbId}">推荐</a>
                                </td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/questionlist?childQbId=${childQuestionBank.childQbId}">题目</a>
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
                            <li><a href="/toseewritest/operator/questionbank/cqblist?page=${currentPage - 1}&size=${size}">上页</a></li>
                        </#if>
                        <#list 1..childQuestionBankPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/toseewritest/operator/questionbank/cqblist?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte childQuestionBankPage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                            <li><a href="/toseewritest/operator/questionbank/cqblist?page=${currentPage + 1}&size=${size}">下页</a></li>
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
