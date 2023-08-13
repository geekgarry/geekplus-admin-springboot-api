package ${basePackage}.webapp.system.mapper;

import ${basePackage}.webapp.system.entity.${modelNameUpperCamel};
import java.util.List;

/**
 * ${title} ${functionName}
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Mapper {

    /**
    * 增加
    * @param ${modelNameLowerCamel}
    * @return ${functionName}
    */
    Integer insert${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 批量增加
    * @param ${modelNameLowerCamel}List
    * @return
    */
    public int batchInsert${modelNameUpperCamel}List(List<${modelNameUpperCamel}> ${modelNameLowerCamel}List);

    /**
    * 删除
    * @param ${pkColumn.smallColumnName}
    */
    Integer delete${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName});

    /**
    * 批量删除
    */
    Integer delete${modelNameUpperCamel}ByIds(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s);

    /**
    * 修改
    * @param ${modelNameLowerCamel}
    */
    Integer update${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 批量修改魔偶几个字段
    * @param ${pkColumn.smallColumnName}s
    */
    Integer batchUpdate${modelNameUpperCamel}List(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s);

    /**
    * 查询全部
    */
    List<${modelNameUpperCamel}> select${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 查询全部,联合查询使用
    */
    List<${modelNameUpperCamel}> selectUnion${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel});

    /**
    * 根据Id查询单条数据
    */
    ${modelNameUpperCamel} select${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName});
}
