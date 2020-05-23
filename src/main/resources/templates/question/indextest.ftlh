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

                    <form role="form" method="post" action="/toseewritest/operator/questionbank/questionsave" id="questionForm" data-toggle="validator">
                        <div class="form-group">
                            <label>所属子题库</label>
                            <select name="childQbId" id="childQbId" class="form-control" data-error="子题库必选">
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
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>题目类型</label>
                            <select name="questionType" id="questionType" class="form-control" data-error="题目类型必选" onchange="questionTypeChange()">
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
                            <div class="help-block with-errors"></div>
                        </div>


                        <div class="form-group">
                            <label>题干</label>
                            <input name="questionStem" type="text" class="form-control" data-error="题干必填" required value="${(questionDTO.questionStem)!''}"/>
<#--                            <span class="glyphicon glyphicon-ok form-control-feedback"></span>-->
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>答案</label>
                            <input name="answer" type="text" class="form-control" data-error="答案必填" required value="${(questionDTO.answer)!''}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>解析</label>
                            <input name="explanation" type="text" class="form-control" data-error="解析必填" required value="${(questionDTO.explanation)!''}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" id="questionOptionsDiv">
                            <textarea name="questionOptions" hidden id="questionOptions" class="form-control" rows="20" data-error="选项必填" required>${(questionDTO.getOptionsJson())!''}</textarea>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>选项</label>
                        </div>

<#--                        freemarkerjson解析-->
                        <#if questionDTO??>
                            <#assign optionsJson="${questionDTO.getOptionsJson()}"?eval />
                            <#list optionsJson as optionJson>
                            <div class="form-group">
                                    <div class="input-group" >
                                        <span class="input-group-addon">${optionJson.optionName}</span>
                                        <input type="text" class="form-control"  value="${optionJson.optionValue}">
                                    </div>
                            </div>
                            </#list>
                        </#if>





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

<script type="text/javascript">
    var defaultOptionStr = '[\n' +
        '  {\n' +
        '    "optionName": "A",\n' +
        '    "optionValue": ""\n' +
        '  },\n' +
        '  {\n' +
        '    "optionName": "B",\n' +
        '    "optionValue": ""\n' +
        '  },\n' +
        '  {\n' +
        '    "optionName": "C",\n' +
        '    "optionValue": ""\n' +
        '  },\n' +
        '  {\n' +
        '    "optionName": "D",\n' +
        '    "optionValue": ""\n' +
        '  }\n' +
        ']';

    <#if !questionDTO??>
        $("#questionOptions").val(defaultOptionStr);
        $("#options-help-block").hide();
    </#if>

    <#if  questionDTO?? && questionDTO.questionType == 3>
        // 隐藏
        $("#questionOptionsDiv").hide();
        // 更新验证器，让其不验证选项
        $("#questionForm").validator('update');
    </#if >
    function questionTypeChange()
    {
        var questionType = $("#questionType").val();

        if(questionType == 3)
        {
            // 隐藏选项栏
            $("#questionOptionsDiv").hide();
            $("#questionForm").validator('update');
        }
        else
        {
            $("#questionOptionsDiv").show();
            $("#questionForm").validator('update');
        }
    }
</script>