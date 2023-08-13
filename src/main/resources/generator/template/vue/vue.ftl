<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <#if allColumn?exists>
        <#list allColumn as column>
        <#if (column.columnType=='varchar'||column.columnType=='char') && column.isPk!='1' >
            <el-form-item label="${column.columnComment}" prop="${column.smallColumnName}">
                <el-input
                    v-model="queryParams.${column.smallColumnName}"
                    placeholder="请输入${column.columnComment}"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
        <#elseif (column.columnType=='int'||column.columnType=='tinyint'||column.columnType=='smallint'||column.columnType=='bigint') && column.isPk!='1' >
            <el-form-item label="${column.columnComment}" prop="${column.smallColumnName}">
                <el-input
                    v-model="queryParams.${column.smallColumnName}"
                    placeholder="请输入${column.columnComment}"
                    clearable
                    size="small"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
        </#if>
        </#list>
        </#if>
            <el-form-item>
                <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleAdd"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="success"
                    icon="el-icon-edit"
                    size="mini"
                    :disabled="single"
                    @click="handleUpdate"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="danger"
                    icon="el-icon-delete"
                    size="mini"
                    :disabled="multiple"
                    @click="handleDelete"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    type="warning"
                    icon="el-icon-download"
                    size="mini"
                    @click="handleExport"
                >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <#if allColumn?exists>
            <#list allColumn as column>
            <#if column.javaType == 'Date'>
            <el-table-column label="${column.columnComment}" align="center" prop="${column.smallColumnName}" width="100" show-overflow-tooltip >
                <template slot-scope="scope">
                    <span>{{ dateFormat(scope.row.${column.smallColumnName}) }}</span>
                </template>
            </el-table-column>
            <#else >
            <el-table-column label="${column.columnComment}" align="center" prop="${column.smallColumnName}" />
            </#if>
            </#list>
            </#if>
            <el-table-column label="操作" align="center" fixed="right" width="120" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleUpdate(scope.row)"
                    >修改</el-button>
                    <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                    >删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
        />

        <!-- 添加或修改数据对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-row>
                    <#if allColumn?exists>
                    <#list allColumn as column>
                    <#if (column.columnType=='varchar'||column.columnType=='char') && column.isPk!='1' >
                    <el-col :span="12">
                        <el-form-item label="${column.columnComment}" prop="${column.smallColumnName}">
                            <el-input v-model="form.${column.smallColumnName}" placeholder="请输入${column.columnComment}" />
                        </el-form-item>
                    </el-col>
                    <#elseif (column.columnType=='int'||column.columnType=='tinyint'||column.columnType=='smallint'||column.columnType=='bigint') && column.isPk!='1' >
                    <el-col :span="12">
                        <el-form-item label="${column.columnComment}" prop="${column.smallColumnName}">
                            <el-select v-model="form.${column.smallColumnName}" placeholder="请选择">
                                <el-option label="Label1" value="1"></el-option>
                                <el-option label="Label2" value="2"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <#elseif (column.columnType=='text'||column.columnType=='tinytext'||column.columnType=='bigtext'||column.columnType=='longtext') && column.isPk!='1' >
                    <el-col :span="24">
                        <el-form-item label="${column.columnComment}" prop="${column.smallColumnName}">
                            <!--<editor v-model="form.${column.smallColumnName}" :min-height="186"/>-->
                            <el-input type="textarea" :rows="3" v-model="form.${column.smallColumnName}" placeholder="请输入内容"></el-input>
                        </el-form-item>
                    </el-col>
                    </#if>
                    </#list>
                    </#if>
                    <!-- <el-col :span="24">
                      <el-form-item label="">
                        <el-radio-group v-model="">
                          <el-radio :key="" :label="" >radio1</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </el-col> -->
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { list${className}, get${className}, del${className}, add${className}, update${className}, export${className} } from "@/api/${moduleName}/${jsMethodName}";

export default {
    name: "${componentName}",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 数据列表
            list: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 类型数据字典
            statusOptions: [],
            // 状态数据字典
            typeOptions: [],
            // 查询参数
            queryParams: {
            pageNum: 1,
            pageSize: 10,
            <#if allColumn?exists>
            <#list allColumn as column>
            ${column.smallColumnName}: undefined,
            </#list>
            </#if>
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
            <#if allColumn?exists>
            <#list allColumn as column>
            <#if column.javaType == 'String'>
                ${column.smallColumnName}: [
                { required: true, message: "${column.columnComment}不能为空", trigger: "blur" }
                ],
            <#elseif column.javaType == 'Integer'>
                ${column.smallColumnName}: [
                { required: true, message: "${column.columnComment}不能为空", trigger: "change" }
                ],
            </#if>
            </#list>
            </#if>
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 分页查询数据列表 */
        getList() {
            this.loading = true;
            list${className}(this.queryParams).then(response => {
                this.list = response.rows;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
            <#if allColumn?exists>
                <#list allColumn as column>
                ${column.smallColumnName}: undefined,
                </#list>
            </#if>
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.${pkColumn.smallColumnName})
            this.single = selection.length!=1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加数据信息";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.${pkColumn.smallColumnName} || this.ids[0]
            get${className}({ ${pkColumn.smallColumnName}: id }).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改数据信息";
            });
        },
        /** 提交按钮 */
        submitForm: function() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.${pkColumn.smallColumnName} != undefined) {
                        update${className}(this.form).then(response => {
                            this.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        add${className}(this.form).then(response => {
                            this.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.${pkColumn.smallColumnName} || this.ids
            this.$confirm('是否确认删除列表编号为"' + ids + '"的数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return del${className}(ids);
                }).then(() => {
                    this.getList();
                    this.msgSuccess("删除成功");
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            const queryParams = this.queryParams;
            this.$confirm('是否确认导出所有列表数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(function() {
                return 'export'+${className}(queryParams);
            }).then(response => {
                this.download(response.msg);
            })
        }
    }
};
</script>
