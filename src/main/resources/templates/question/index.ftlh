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

                    <form role="form" method="post" action="/toseewritest/operator/questionbank/questionsave">
                        <div class="form-group">
                            <label>题目类型</label>
                            <select name="questionType" class="form-control">
                                <option>请选择题目类型</option>
                                <#list typeEnumList as typeEnum>
                                    <option value="${typeEnum.code}"
                                            <#if (questionDTO.questionType)?? && questionDTO.questionType == typeEnum.code>
                                                selected
                                            </#if>
                                    >${typeEnum.message}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>所属子题库</label>
                            <select name="childQbId" id="childQbId" class="form-control">
                                <option>请选择子题库</option>
                                <#list childQuestionBankList as childQuestionBank>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option value="${childQuestionBank.childQbId}"
                                            <#if (questionDTO.childQbId)?? && questionDTO.childQbId == childQuestionBank.childQbId>
                                                selected
                                            </#if>
                                    >${childQuestionBank.cqbTitle}
                                    </option>
                                </#list>
                            </select>
                        </div>



                        <div class="form-group">
                            <label>题干</label>
                            <input name="questionStem" type="text" class="form-control" value="${(questionDTO.questionStem)!''}"/>
                        </div>

                        <div class="form-group">
                            <label>答案</label>
                            <input name="answer" type="text" class="form-control" value="${(questionDTO.answer)!''}"/>
                        </div>

                        <div class="form-group">
                            <label>解析</label>
                            <input name="explanation" type="text" class="form-control" value="${(questionDTO.explanation)!''}"/>
                        </div>

                        <div class="form-group">
                            <label>选项</label>
                            <textarea name="questionOptions" class="form-control" rows="20">${(questionDTO.getOptionsJson())!''}</textarea>
                        </div>


                        <#--                        隐藏的字段 为了传给save-->
                        <input hidden type="text" name="questionId" value="${(questionDTO.questionId)!''}">
                        <button type="submit" class="btn btn-danger">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
