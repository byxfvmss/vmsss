<#if (actionMessages?exists && actionMessages?size > 0)>
	<div id="msg"><strong>提示：</strong><#list actionMessages as message>${message}</#list></div>
</#if>
<#if (actionErrors?exists && actionErrors?size > 0)>
	<div id="msg"><strong>错误：</strong><#list actionErrors as error>${error}</#list></div>
</#if>