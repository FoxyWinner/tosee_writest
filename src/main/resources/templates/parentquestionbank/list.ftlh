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
                            <th>主题库ID</th>
                            <th>题库类型</th>
                            <th>名称</th>
                            <th>公司</th>
                            <th>行业</th>
                            <th>岗位</th>
                            <th>子题库数</th>
                            <th>推荐</th>
                            <th>热度</th>
                            <th>发布时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list parentQuestionBankDTOPage.content as parentQuestionBankDTO>
                            <tr>
                                <td>${parentQuestionBankDTO.parentQbId}</td>
                                <td>${parentQuestionBankDTO.getParentQuestionBankTypeEnum().message}</td>
                                <td>${parentQuestionBankDTO.pqbTitle}</td>
                                <td>
                                    <#if parentQuestionBankDTO.pqbType == 0>
                                        ${parentQuestionBankDTO.company.companyName}
                                    </#if>
                                </td>
                                <td>${parentQuestionBankDTO.workField.fieldName}</td>
                                <td>${parentQuestionBankDTO.workPosition.positionName}</td>
                                <td>${parentQuestionBankDTO.cqbNumber}</td>
                                <td>${parentQuestionBankDTO.isRecommended}</td>
                                <td>${parentQuestionBankDTO.pqbHeat}</td>
                                <td>${parentQuestionBankDTO.relaseTime}</td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/pqbindex?parentQbId=${parentQuestionBankDTO.parentQbId}" >修改</a>
                                </td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/pqbrecommend?parentQbId=${parentQuestionBankDTO.parentQbId}">推荐</a>
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
                            <li><a href="/tosee_writest/operator/questionbank/list?page=${currentPage - 1}&size=${size}">上页</a></li>
                        </#if>
                        <#list 1..parentQuestionBankDTOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/tosee_writest/operator/questionbank/list?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte parentQuestionBankDTOPage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                            <li><a href="/tosee_writest/operator/questionbank/list?page=${currentPage + 1}&size=${size}">下页</a></li>
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
