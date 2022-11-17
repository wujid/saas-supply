<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="分类名称" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入分类名称" />
      </el-form-item>
      <el-form-item label="分类编码" prop="code">
        <el-input v-model.trim="searchForm.code" clearable placeholder="请输入分类编码" />
      </el-form-item>
      <el-form-item label="" class="btn-center">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button plain @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <my-table
      ref="myTable"
      stripe
      border
      toolbar
      :columns="columns"
      :search-form="searchForm"
      children-name="hasChildren"
      @pageRequest="pageRequest"
      @see="showSearch = !showSearch"
      @loadChildren="loadChildren"
    >
      <template slot="toolSlot">
        <div style="width:100%;">
          <el-button v-auth="'dict_category_add'" type="primary" size="mini" @click="addCategory">新建</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'dict_category_view'" type="primary" :underline="false" @click="categoryDetail(scope.row, 'view')">查看</el-link>
        <el-link v-auth="'dict_category_edit'" type="primary" :underline="false" @click="categoryDetail(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'dict_category_add_children'" type="primary" :underline="false" @click="addChildren(scope.row)">添加下级</el-link>
        <el-link v-auth="'dict_category_del'" type="primary" :underline="false" @click="delCategory(scope.row.id)">删除</el-link>
      </template>
    </my-table>
    <el-dialog class="dialog-creat" :title="detail.formTitle" :visible.sync="detail.formVisible" width="600px">
      <el-form ref="dictCategoryForm" :model="detail.form" :rules="detail.formRules">
        <el-form-item label="分类名称" label-width="150px" prop="name">
          <el-input v-model="detail.form.name" placeholder="请填写分类名称" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item label="分类编码" label-width="150px" prop="code">
          <el-input v-model="detail.form.code" placeholder="请填写分类编码" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item label="分类描述" label-width="150px" prop="description">
          <el-input v-model="detail.form.description" type="textarea" placeholder="请填写分类描述" style="width: 90%;" :disabled="detail.isDisabled" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="detail.operationType !== 'view'" type="primary" :loading="detail.formLoading" @click="saveCategory">确 定</el-button>
        <el-button @click="detail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {
  addDictCategory, delDictCategory,
  getChildrenByParentId,
  getDictCategoryPageByParams,
  updateDictCategory
} from '@/api/system'

export default {
  name: 'dict-category',
  data() {
    return {
      showSearch: true,
      searchForm: {
        code: null,
        name: null,
        parentId: 0
      },
      columns: [
        {
          title: '分类名称',
          key: 'name'
        },
        {
          title: '分类编码',
          key: 'code'
        },
        {
          title: '层级',
          key: 'level'
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ],
      detail: {
        operationType: 'add',
        formTitle: null,
        isDisabled: false,
        formVisible: false,
        dictCategory: null,
        form: {
          id: null,
          parentId: null,
          name: null,
          code: null,
          description: null
        },
        formRules: {
          code: [{ required: true, message: '请输入字典编码' }],
          name: [{ required: true, message: '请输入字典名称' }]
        }
      }
    }
  },
  async created() {
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve, reject) => {
        getDictCategoryPageByParams(params)
          .then((res) => {
            resolve({
              data: res.data.records,
              total: parseInt(res.data.total)
            })
          })
          .catch(() => {
            resolve({
              data: [],
              total: 0
            })
          })
      })
      callback(promise)
    },
    // 查询
    handleSearch() {
      this.$refs.myTable.handleSearch()
    },
    // 查询重置
    reset() {
      this.$refs.formSearch.resetFields()
      this.handleSearch()
    },
    // 查询子级
    loadChildren(tree, treeNode, callback) {
      const params = { parentId: tree.id }
      const promise = new Promise((resolve, reject) => {
        getChildrenByParentId(params)
          .then((res) => {
            resolve(res.data)
          })
          .catch(() => {
            resolve([])
          })
      })
      callback(promise)
    },
    // 新建
    addCategory() {
      this.detail.formTitle = '新建字典分类'
      this.detail.operationType = 'add'
      this.detail.isDisabled = false
      this.detail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictCategoryForm.resetFields()
      })
    },
    // 查看/编辑
    categoryDetail(row, type) {
      console.log('sds', row)
      if (type === 'view') {
        this.detail.formTitle = '查看字典分类信息'
        this.detail.operationType = 'view'
        this.detail.isDisabled = true
      } else {
        this.detail.formTitle = '编辑字典分类信息'
        this.detail.operationType = 'edit'
        this.detail.isDisabled = false
      }
      this.detail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictCategoryForm.resetFields()
        this.detail.form = {
          id: row.id,
          parentId: row.parentId,
          code: row.code,
          name: row.name,
          description: row.description
        }
      })
    },
    // 添加子级
    addChildren(row) {
      this.detail.formTitle = '新建字典分类'
      this.detail.operationType = 'add'
      this.detail.isDisabled = false
      this.detail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictCategoryForm.resetFields()
        this.detail.form.id = null
        this.detail.form.parentId = row.id
      })
    },
    // 删除
    delCategory(id) {
      this.$confirm('确认要删除该字典分类吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delDictCategory({ dictCategoryId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 新增/修改保存
    saveCategory() {
      this.$refs.dictCategoryForm.validate(async(valid) => {
        if (valid) {
          this.detail.formLoading = true
          try {
            if (this.detail.operationType === 'add') {
              await addDictCategory(this.detail.form)
            } else {
              await updateDictCategory(this.detail.form)
            }
            this.$message.success('提交成功！')
            this.detail.formVisible = false
            this.handleSearch()
          } finally {
            this.detail.formLoading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.table-page {
  width: 100%;
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  .el-link {
    margin-right: 10px;
  };
  .dialog-creat {
    .el-input {
      width: 90% !important;
    }
  }
}
::v-deep .tool-bar{
  margin: 0;
}
</style>
