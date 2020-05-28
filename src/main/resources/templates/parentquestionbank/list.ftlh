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
<#--                            <th>主题库ID</th>-->
                            <th>题库类型</th>
                            <th>名称</th>
                            <th>公司</th>
                            <th>行业</th>
                            <th>岗位</th>
                            <th>子题库数</th>
                            <th>推荐</th>
                            <th>热度</th>
                            <th>发布时间</th>
                            <th colspan="3">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list parentQuestionBankDTOPage.content as parentQuestionBankDTO>
                            <tr>
<#--                                <td>${parentQuestionBankDTO.parentQbId}</td>-->
                                <td>${parentQuestionBankDTO.getParentQuestionBankTypeEnum().message}</td>
                                <td>${parentQuestionBankDTO.pqbTitle}</td>
                                <td>
                                    <#if parentQuestionBankDTO.pqbType == 0>
                                        ${parentQuestionBankDTO.company.companyName}
                                    </#if>
                                </td>
                                <td>${parentQuestionBankDTO.workField.fieldName}</td>
                                <td>${parentQuestionBankDTO.workPosition.positionName}(${parentQuestionBankDTO.getPositionPqbTypeName()})</td>
                                <td>${parentQuestionBankDTO.cqbNumber}</td>
                                <td>${parentQuestionBankDTO.isRecommended}</td>
                                <td>${parentQuestionBankDTO.pqbHeat}</td>
                                <td>${parentQuestionBankDTO.relaseTime}</td>

                                <td>
                                    <a href="/toseewritest/operator/questionbank/pqbindex?parentQbId=${parentQuestionBankDTO.parentQbId}" >修改</a>
                                </td>

                                <td>
                                    <#if parentQuestionBankDTO.isRelase == 0>
                                        <a href="/toseewritest/operator/questionbank/pqbrelase?parentQbId=${parentQuestionBankDTO.parentQbId}">发布</a>
                                    <#else>
                                        <a href="/toseewritest/operator/questionbank/pqbcancelrelase?parentQbId=${parentQuestionBankDTO.parentQbId}">取消发布</a>
                                    </#if>

                                </td>
                                <td>
                                    <a href="/toseewritest/operator/questionbank/cqblist?parentQbId=${parentQuestionBankDTO.parentQbId}">子题库</a>
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
                            <li><a href="/toseewritest/operator/questionbank/pqblist?page=${currentPage - 1}&size=${size}">上页</a></li>
                        </#if>
                        <#list 1..parentQuestionBankDTOPage.getTotalPages() as index>
                            <#if currentPage == index>
                                <li class="disabled"><a href="#">${index}</a></li>
                            <#else>
                                <li><a href="/toseewritest/operator/questionbank/pqblist?page=${index}&size=${size}">${index}</a></li>
                            </#if>
                        </#list>

                        <#if currentPage gte parentQuestionBankDTOPage.getTotalPages()>
                            <li class="disabled"><a href="#">下页</a></li>
                        <#else>
                            <li><a href="/toseewritest/operator/questionbank/pqblist?page=${currentPage + 1}&size=${size}">下页</a></li>
                        </#if>

                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
