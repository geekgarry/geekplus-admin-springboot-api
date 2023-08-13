package ${basePackage}.webapp.system.service;

import ${basePackage}.webapp.system.entity.${modelNameUpperCamel};
//import ${basePackage}.core.Service;
import java.util.List;


/**
 * ${title} ${functionName}
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Service {

    /**
    * 增加
    * @param ${modelNameLowerCamel}
    * @return ${functionName}
    */
    public Integer insert${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 批量增加
    * @param ${modelNameLowerCamel}List
    * @return ${functionName}
    */
    public Integer batchInsert${modelNameUpperCamel}List(List<${modelNameUpperCamel}> ${modelNameLowerCamel}List);

    /**
    * 删除
    * @param ${pkColumn.smallColumnName}
    */
    public Integer delete${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName});

    /**
    * 批量删除某几个字段
    */
    public Integer delete${modelNameUpperCamel}ByIds(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s);

    /**
    * 修改
    * @param ${modelNameLowerCamel}
    */
    public Integer update${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 批量修改
    * @param ${pkColumn.smallColumnName}s
    */
    public Integer batchUpdate${modelNameUpperCamel}List(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s);

    /**
    * 查询全部
    */
    public List<${modelNameUpperCamel}> select${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 查询全部，用作联合查询使用(在基础上修改即可)
    */
    public List<${modelNameUpperCamel}> selectUnion${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 根据Id查询单条数据
    */
    public ${modelNameUpperCamel} select${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName});
}
