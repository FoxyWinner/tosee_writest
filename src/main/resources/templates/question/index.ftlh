<html>
<#include "../common/header.ftlh">
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

                    <form role="form" method="post" action="/toseewritest/operator/questionbank/questionsave" id="questionForm" data-toggle="validator">
                        <div class="form-group">
                            <label>所属子题库</label>
                            <select name="childQbId" id="childQbId" class="form-control" data-error="子题库必选" required>
                                <option value="">请选择子题库</option>
                                <#list childQuestionBankList as childQuestionBank>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option value="${childQuestionBank.childQbId}"
                                            <#if ((questionDTO.childQbId)?? && questionDTO.childQbId == childQuestionBank.childQbId) ||
                                            (lastChildQbId?? && lastChildQbId == childQuestionBank.childQbId)>
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
                            <select name="questionType" id="questionType" class="form-control" data-error="题目类型必选" required onchange="questionTypeChange()">
                                <option value="">请选择题目类型</option>
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
                            <p class="help-block" id="options-help-block">
                                题干可以暂时为空，但不建议输入题干为空的题目。
                            </p>
                            <textarea name="questionStem" id="content" class="form-control" rows="20" onchange="">${(questionDTO.questionStem)!''}</textarea>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>答案</label>
                            <input name="answer" type="text" class="form-control" data-error="答案必填" required value="${(questionDTO.answer)!''}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>解析</label>
                            <input name="explanation" type="text" class="form-control" data-error="答案必填" required value="${(questionDTO.explanation)!''}"/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" id="questionOptionsDiv">
                            <label>选项</label>
<#--                            实际上要填这个questionOptions-->
                            <input readonly name="questionOptions" id="questionOptions" class="form-control" rows="20"  value="${(questionDTO.getOptionsJson())!''}"/>
                            <br>

                            <#if questionDTO?? && questionDTO.questionOptions??>
                                <#assign optionsJson="${questionDTO.getOptionsJson()}"?eval />
                                <#list optionsJson as optionJson>
                                    <div class="form-group">
                                        <div class="input-group" >
                                            <span class="input-group-addon">${optionJson.optionName}</span>
                                            <input id="optionInput${optionJson_index}" type="text" class="form-control"  oninput="onOptionInput(${optionJson_index})" value="${optionJson.optionValue}">
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <#list 0..3 as i>
                                    <div class="form-group">
                                        <div class="input-group" >
<#--                                            span由JS填充-->
                                            <span class="input-group-addon" id="optionName${i}">
                                            </span>
                                            <input id="optionInput${i}" type="text" class="form-control"  oninput="onOptionInput(${i})">
                                        </div>
                                    </div>
                                </#list>
                            </#if>
                            <div class="help-block with-errors"></div>
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
<#--题干富文本编辑器-->
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


    });
</script>

<#--选项-->
<script type="text/javascript">
    // 初始化

    // 若为新增
    <#if !questionDTO??>
        // 默认JSON
        var defaultOptionStr = "[{\"optionName\":\"A\",\"optionValue\":\"\"},{\"optionName\":\"B\",\"optionValue\":\"\"},{\"optionName\":\"C\",\"optionValue\":\"\"},{\"optionName\":\"D\",\"optionValue\":\"\"}]";
        // input赋值
        $("#questionOptions").val(defaultOptionStr);
        // 选项名显示
        var jsonObjs = JSON.parse(defaultOptionStr);
        for (var i in jsonObjs)
        {
            $("#optionName"+i).html(jsonObjs[i].optionName);
        }
    </#if>

    // 若为更改且为问答题
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

    // 选项值变动触发函数
    function onOptionInput(index) {
        // 定位选名 插入json obj
        var jsonStr = $("#questionOptions").val();
        var jsonObj = JSON.parse(jsonStr);
        jsonObj[index].optionValue = $("#optionInput"+index).val();
        $("#questionOptions").val(JSON.stringify(jsonObj));
    }
</script>