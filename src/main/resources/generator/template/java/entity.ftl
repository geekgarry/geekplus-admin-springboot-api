package ${basePackage}.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

<#list importList as import>
import ${import};
</#list>
<#--<#list allColumn as item>-->
<#--<#switch item.javaType>-->
<#--<#case 'Date'>-->
<#--import java.util.Date;-->
<#--<#break>-->
<#--<#case 'BigDecimal'>-->
<#--import java.math.BigDecimal;-->
<#--<#break>-->
<#--<#case 'BigInteger'>-->
<#--import java.math.BigInteger;-->
<#--<#break>-->
<#--</#switch>-->
<#--<#if item.javaType == 'Date'>-->
<#--import java.util.Date;-->
<#--</#if>-->
<#--<#if item.javaType == 'BigDecimal'>-->
<#--import java.math.BigDecimal;-->
<#--</#if>-->
<#--<#if item.javaType == 'BigInteger'>-->
<#--import java.math.BigInteger;-->
<#--</#if>-->
<#--</#list>-->

/**
 * 功能：${functionName} 对象:${tableName}
 *
 * @author ${author}
 * @date ${date}
 */
public class ${modelNameUpperCamel} extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    <#list allColumn as column>

    /**
     * ${functionName} ${title}
     */
    private ${column.javaType} ${column.smallColumnName};
    </#list>

    <#list allColumn as column>
	/**
	 *获取${column.columnComment}
	 */
	public ${column.javaType} get${column.bigColumnName}(){
		return ${column.smallColumnName};
	}

	/**
	 *设置${column.columnComment}
	 */
	public void set${column.bigColumnName}(${column.javaType} ${column.smallColumnName}){
		this.${column.smallColumnName} = ${column.smallColumnName};
	}
	</#list>

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
    <#list allColumn as column>
            .append("${column.smallColumnName}", get${column.bigColumnName}())
    </#list>
            .toString();
    }
}
