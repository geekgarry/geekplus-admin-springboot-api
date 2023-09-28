package ${basePackage}.webapp.${moduleName}.service.impl;

import ${basePackage}.webapp.${moduleName}.mapper.${modelNameUpperCamel}Mapper;
import ${basePackage}.webapp.${moduleName}.entity.${modelNameUpperCamel};
import ${basePackage}.webapp.${moduleName}.service.${modelNameUpperCamel}Service;
//import ${basePackage}.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import javax.annotation.Resource;


/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl implements ${modelNameUpperCamel}Service {
    @Resource
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

    /**
    * 增加
    * @param ${modelNameLowerCamel}
    * @return ${functionName}
    */
    public Integer insert${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel}){
        return ${modelNameLowerCamel}Mapper.insert${modelNameUpperCamel}(${modelNameLowerCamel});
    }

    /**
    * 批量增加
    * @param ${modelNameLowerCamel}List
    * @return ${functionName}
    */
    public Integer batchInsert${modelNameUpperCamel}List(List<${modelNameUpperCamel}> ${modelNameLowerCamel}List){
        return ${modelNameLowerCamel}Mapper.batchInsert${modelNameUpperCamel}List(${modelNameLowerCamel}List);
    }

    /**
    * 删除
    * @param ${pkColumn.smallColumnName}
    */
    public Integer delete${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName}){
        return ${modelNameLowerCamel}Mapper.delete${modelNameUpperCamel}ById(${pkColumn.smallColumnName});
    }

    /**
    * 批量删除
    */
    public Integer delete${modelNameUpperCamel}ByIds(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s){
        return ${modelNameLowerCamel}Mapper.delete${modelNameUpperCamel}ByIds(${pkColumn.smallColumnName}s);
    }

    /**
    * 修改
    * @param ${modelNameLowerCamel}
    */
    public Integer update${modelNameUpperCamel}(${modelNameUpperCamel} ${modelNameLowerCamel}){
        return ${modelNameLowerCamel}Mapper.update${modelNameUpperCamel}(${modelNameLowerCamel});
    }

    /**
    * 批量修改某几个字段
    * @param ${pkColumn.smallColumnName}s
    */
    public Integer batchUpdate${modelNameUpperCamel}List(${pkColumn.javaType}[] ${pkColumn.smallColumnName}s){
        return ${modelNameLowerCamel}Mapper.batchUpdate${modelNameUpperCamel}List(${pkColumn.smallColumnName}s);
    }

    /**
    * 查询全部
    */
    public List<${modelNameUpperCamel}> select${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel}){
        return ${modelNameLowerCamel}Mapper.select${modelNameUpperCamel}List(${modelNameLowerCamel});
    }

    /**
    * 查询全部,用于联合查询，在此基础做自己的定制改动
    */
    public List<${modelNameUpperCamel}> selectUnion${modelNameUpperCamel}List(${modelNameUpperCamel} ${modelNameLowerCamel}){
        return ${modelNameLowerCamel}Mapper.selectUnion${modelNameUpperCamel}List(${modelNameLowerCamel});
    }

    /**
    * 根据Id查询单条数据
    */
    public ${modelNameUpperCamel} select${modelNameUpperCamel}ById(${pkColumn.javaType} ${pkColumn.smallColumnName}){
        return ${modelNameLowerCamel}Mapper.select${modelNameUpperCamel}ById(${pkColumn.smallColumnName});
    }
}
