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

                    <form role="form" id="pqbForm" method="post" action="/toseewritest/operator/questionbank/pqbsave" data-toggle="validator">
                        <div class="form-group">
                            <label>题库类型</label>
                            <select name="pqbType" class="form-control" id="pqbType" onchange="pQBtypeChange()" data-error="题库类型必选" required>
                                <option value="">请选择题库类型</option>
                                <#list typeEnumList as typeEnum>
                                    <option value="${typeEnum.code}"
                                            <#if (parentQuestionBank.pqbType)?? && parentQuestionBank.pqbType == typeEnum.code>
                                                selected
                                            </#if>
                                    >${typeEnum.message}
                                    </option>
                                </#list>
                            </select>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" id="companyId-div">
                            <label>所属公司</label>
                            <select name="companyId" id="companyId" class="form-control">
                                <option value="">请选择公司</option>
                                <#list companyList as company>
                                    <option value="${company.companyId}"
                                            <#if (parentQuestionBank.companyId)?? && parentQuestionBank.companyId == company.companyId>
                                                selected
                                            </#if>
                                    >${company.companyName}
                                    </option>
                                </#list>
                            </select>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" id="fieldType-div">
                            <label>所属行业</label>
                            <select name="fieldType" id="fieldType" class="form-control" onchange="fieldChange()">
                                <option value="">请选择行业</option>
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
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" id="positionType-div">
                            <label>所属岗位</label>
                            <select name="positionType" id="positionType" class="form-control">
                                <option value="">请选择岗位</option>
                            </select>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group">
                            <label>题库名</label>
                            <input name="pqbTitle" type="text" class="form-control" value="${(parentQuestionBank.pqbTitle)!''}" data-error="题库名必填" required/>
                            <div class="help-block with-errors"></div>
                        </div>

                        <div class="form-group" >
                            <label>发布时间</label>
                            <#--                            这个要给人呈现个默认的-->
                            <input name="relaseTime" type="text" class="form-control" value="${(parentQuestionBank.relaseTime)!.now?string("yyyy/MM/dd")}"/>
                        </div>

                        <div class="form-group">
                            <label>热度</label>
                            <input hidden name="pqbHeat" type="text" class="form-control" value="${(parentQuestionBank.pqbHeat?c)!'0'}"/>
                        </div>

                        <div class="form-group">
                            <div class="checkbox">
                                <label><input name="isRecommended" type="checkbox"
                                            <#if parentQuestionBank?? && parentQuestionBank.isRecommended == 1>
                                                checked="checked"
                                            </#if>
                                    />推荐该主题库</label>
                            </div>
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

    // 在进入首页渲染完毕时先调用一次fieldChange（）
    $(document).ready(function(){
        var currentfieldType = $("#fieldType").val();
        if (currentfieldType != null) fieldChange();
    });
    function fieldChange()
    {
        $("#positionType").empty();

        var currentfieldType = $("#fieldType").val();
        var currentPqbType = $("#pqbType").val();
        // console.log("当前父题库类型",currentPqbType);
        var fieldType = $("#fieldType").val();
        var html = "";
        <#list workPositionList as workPosition>
            if( ${workPosition.fieldType} ==  fieldType )
                if (${workPosition.getPqbType()} == 3||  ${workPosition.getPqbType()} == currentPqbType)
                {
                    <#if (parentQuestionBank.positionType)?? && parentQuestionBank.positionType == workPosition.positionType>
                        html += "<option value='${workPosition.positionType}' selected> ${workPosition.positionName}(${workPosition.getPqbTypeName()}) </option>";
                    <#else >
                        html += "<option value='${workPosition.positionType}'> ${workPosition.positionName}(${workPosition.getPqbTypeName()}) </option>";
                    </#if>
                }

        </#list>

        $("#positionType").empty();
        $(html).appendTo("#positionType");
    }

    function pQBtypeChange()
    {
        var currentType = $("#pqbType").val();
        if (currentType != 0)
        {
            $("#companyId-div option:selected").val("");
            $("#companyId-div").hide();
            // $("#pqbForm").validator('update');
        }
        else
        {
            $("#companyId-div").show();
            // $("#pqbForm").validator('update');
        }

        // 行测
        if (currentType == 2)
        {
            // 如果是行测，那么行业和岗位值都要为0
            $("#fieldType option:selected").val("0");
            $("#positionType option:selected").val("0");
            $("#fieldType-div").hide();
            $("#positionType-div").hide();

            // $("#pqbForm").validator('update');
        }else
        {
            $("#fieldType-div").show();
            $("#positionType-div").show();

            var htmlFieldTypeDefault = "<option value=''> 请选择行业 </option>";
            var htmlPostitionTypeDefault = "<option value=''> 请选择岗位 </option>";


            $(htmlFieldTypeDefault).appendTo("#fieldType");
            $(htmlPostitionTypeDefault).appendTo("#positionType");

            // $("#pqbForm").validator('update');
        }

        fieldChange();

    }
</script>