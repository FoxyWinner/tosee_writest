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
                    <form role="form" method="post" action="/toseewritest/operator/questionbank/cqbsave">
                        <div class="form-group">
                            <label>所属主题库</label>
                            <select name="parentQbId"  class="form-control">
                                <option>请选择主题库</option>
                                <#list parentQuestionBankList as parentQuestionBank >
                                    <option value="${parentQuestionBank.parentQbId}"
                                            <#if (childQuestionBank.parentQbId)?? && childQuestionBank.parentQbId == parentQuestionBank.parentQbId>
                                                selected
                                            </#if>
                                    >${parentQuestionBank.pqbTitle}
                                    </option>
                                </#list>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>子题库名</label>
                            <input name="cqbTitle" type="text" class="form-control" value="${(childQuestionBank.cqbTitle)!''}"/>
                        </div>

                        <div class="form-group">
                            <label>模拟时长</label>
                            <input name="simulationTime" type="text" class="form-control" value="${(childQuestionBank.simulationTime)!''}"/>
                        </div>

                        <div class="form-group">
                            <label>发布时间</label>
<#--                            这个要给人呈现个默认的-->
                            <input name="relaseTime" type="text" class="form-control" value="${(childQuestionBank.relaseTime)!.now?string("yyyy/MM/dd")}"/>
                        </div>

                        <div class="form-group">
                            <label>热度</label>
                            <input name="cqbHeat" type="text" class="form-control" value="${(childQuestionBank.cqbHeat)!''}"/>
                        </div>

                        <div class="form-group">
                            <div class="checkbox">
                                <label><input name="isRecommended" type="checkbox"
                                    <#if childQuestionBank?? && childQuestionBank.isRecommended == 1>
                                        checked="checked"
                                    </#if>
                                    />推荐该子题库</label>
                            </div>
                        </div>

                        <#--                        隐藏的字段 为了传给save-->
                        <input hidden type="text" name="childQbId" value="${(childQuestionBank.childQbId)!''}">
                        <button type="submit" class="btn btn-danger">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>

<#--<script type="text/javascript">-->
<#--    var currentfieldType = $("#fieldType").val();-->
<#--    // 在进入首页渲染完毕时先调用一次fieldChange（）-->
<#--    if (currentfieldType != null) fieldChange();-->
<#--    function fieldChange()-->
<#--    {-->
<#--        var fieldType = $("#fieldType").val();-->
<#--        var html = "";-->
<#--        <#list workPositionList as workPosition>-->
<#--            if(${workPosition.fieldType} ==  fieldType)-->
<#--            {-->
<#--                <#if (parentQuestionBank.positionType)?? && parentQuestionBank.positionType == workPosition.positionType>-->
<#--                    html += "<option value='${workPosition.positionType}' selected> ${workPosition.positionName} </option>";-->
<#--                <#else >-->
<#--                    html += "<option value='${workPosition.positionType}'> ${workPosition.positionName} </option>";-->
<#--                </#if>-->
<#--            }-->
<#--        </#list>-->

<#--        $("#positionType").empty();-->
<#--        $(html).appendTo("#positionType");-->
<#--    }-->
<#--</script>-->