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

                    <form role="form" method="post" action="/toseewritest/operator/questionbank/pqbsave">
                        <div class="form-group">
                            <label>题库类型</label>
                            <select name="pqbType" class="form-control">
                                <#list typeEnumList as typeEnum>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option>请选择题库类型</option>
                                    <option value="${typeEnum.code}"
                                            <#if (parentQuestionBank.pqbType)?? && parentQuestionBank.pqbType == typeEnum.code>
                                                selected
                                            </#if>
                                    >${typeEnum.message}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>所属公司</label>
                            <select name="companyId" class="form-control">
                                <#list companyList as company>
                                    <option>请选择公司</option>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option value="${company.companyId}"
                                            <#if (parentQuestionBank.companyId)?? && parentQuestionBank.companyId == company.companyId>
                                                selected
                                            </#if>
                                    >${company.companyName}
                                    </option>
                                </#list>
                            </select>
                        </div>

<#--                        todo 行测时不用选，默认为0就行-->
                        <div class="form-group">
                            <label>所属行业</label>
                            <select name="fieldType" id="fieldType" class="form-control" onchange="fieldChange()">
                                <option>请选择岗位</option>
                                <#list workFieldList as workField>
                                <#--                                    两个问号是"如果存在"的意思-->
                                    <option value="${workField.fieldType}"
                                            <#if (parentQuestionBank.fieldType)?? && parentQuestionBank.fieldType == workField.fieldType>
                                                selected
                                            </#if>
                                    >${workField.fieldName}
                                    </option>
                                </#list>
                            </select>
                        </div>

<#--                        todo 岗位只有选择过行业且不为行测题库时显示-->
                        <div class="form-group">
                            <label>所属岗位</label>
                            <select name="positionType" id="positionType" class="form-control">
                                <option>请选择岗位</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>题库名</label>
                            <input name="pqbTitle" type="text" class="form-control" value="${(parentQuestionBank.pqbTitle)!''}"/>
                        </div>

                        <#--                        隐藏的字段 为了传给save-->
                        <input hidden type="text" name="parentQbId" value="${(parentQuestionBank.parentQbId)!''}">
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
    var currentfieldType = $("#fieldType").val();
    // 在进入首页渲染完毕时先调用一次fieldChange（）
    if (currentfieldType != null) fieldChange();
    function fieldChange()
    {
        var fieldType = $("#fieldType").val();
        var html = "";
        <#list workPositionList as workPosition>
            if(${workPosition.fieldType} ==  fieldType)
            {
                <#if (parentQuestionBank.positionType)?? && parentQuestionBank.positionType == workPosition.positionType>
                    html += "<option value='${workPosition.positionType}' selected> ${workPosition.positionName} </option>";
                <#else >
                    html += "<option value='${workPosition.positionType}'> ${workPosition.positionName} </option>";
                </#if>
            }
        </#list>

        $("#positionType").empty();
        $(html).appendTo("#positionType");
    }
</script>