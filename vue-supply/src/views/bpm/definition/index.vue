<template>
  <div class="customer-add">
    <div class="group-tree">
      <div>
        <el-form ref="formSearch" size="mini" inline class="group-tree-form">
          <el-form-item label="" prop="name">
            <el-input
              v-model.trim="categoryForm.searchForm.name"
              clearable
              placeholder="请输入分类名称查询"
              style="width:60%; margin-right: 10px"
              @keyup.enter.native="getCategoryData"
            />
            <el-button v-auth="'org_add'" type="primary" size="mini" @click="addCategory(null)">创建分类</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-tree
        :data="categoryData"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        :props="{ label: 'name', children: 'childrenList' }"
        class="p-16"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span :class="{currentStyle: selectedCategory.id === data.id}" @click="getDefinitionInfo(data)">{{ data.name }}</span>
          <span class="ml-16">
            <el-button v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="addCategory(data)">创建</el-button>
            <el-button v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="editCategory(data)">编辑</el-button>
            <el-button v-if="!data.isMain" v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="delCategory(data.id)">删除</el-button>
          </span>
        </span>
      </el-tree>
    </div>
    <el-dialog class="formDialog" :title="categoryForm.formTitle" :visible.sync="categoryForm.formVisible" width="600px">
      <el-form ref="categoryForm" :model="categoryForm.form" :rules="categoryForm.formRules" label-width="120px">
        <el-form-item v-if="categoryForm.form.parentName" label="上级名称">
          <el-input v-model="categoryForm.form.parentName" disabled />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="categoryForm.form.code" placeholder="请输入编码" :disabled="categoryForm.form.id !== null" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="categoryForm.form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="categoryForm.form.sort" placeholder="请输入排序" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="categoryForm.formLoading" @click="saveCategory">提交</el-button>
        <el-button @click="categoryForm.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addCategory, getCategoryTreeByParams, updateCategory } from '@/api/bpm'

export default {
  name: 'bpm-definition',
  data() {
    return {
      categoryData: [],
      selectedCategory: {
        id: null,
        name: null,
        data: {}
      },
      categoryForm: {
        formTitle: null,
        formVisible: false,
        formLoading: false,
        searchForm: {
          name: null
        },
        isDisabled: false,
        form: {
          id: null,
          parentName: null,
          parentId: null,
          code: null,
          name: null,
          sort: null
        },
        formRules: {
          name: { required: true, message: '请输入名称' },
          code: { required: true, message: '请输入编码' },
          sort: { required: true, message: '请输入排序' }
        }
      }
    }
  },
  created() {
    this.getCategoryData()
  },
  methods: {
    async getCategoryData() {
      this.selectedCategory = {
        id: null,
        name: null,
        data: null
      }
      const { data } = await getCategoryTreeByParams({ name: this.categoryForm.searchForm.name })
      this.categoryData = data
      if (this.categoryData) {
        await this.getDefinitionInfo(this.categoryData[0])
      }
    },
    async getDefinitionInfo(data) {
    },
    addCategory(data) {
      this.categoryForm.formTitle = '新建流程分类'
      this.categoryForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.categoryForm.resetFields()
        if (data) {
          this.categoryForm.form.parentName = data.name
          this.categoryForm.form.parentId = data.id
        } else {
          this.categoryForm.form.parentName = null
          this.categoryForm.isDisabled = true
        }
        this.categoryForm.form.id = null
        this.categoryForm.form.sort = null
      })
    },
    editCategory(data) {
    },
    delCategory(id) {
    },
    // 保存流程分类
    saveCategory() {
      this.$refs.categoryForm.validate(async(valid) => {
        if (valid) {
          this.categoryForm.formLoading = true
          try {
            if (this.categoryForm.form.id === null) {
              await addCategory(this.categoryForm.form)
            } else {
              await updateCategory(this.categoryForm.form)
            }
            await this.getCategoryData()
            this.$message.success('提交成功！')
            this.categoryForm.formVisible = false
          } catch (e) {
            this.$message.success('提交失败！')
          } finally {
            this.categoryForm.formLoading = false
          }
        }
      })
    }
  }
}
</script>

<style lang='scss' scoped>
.group-tree {
  float: left;
  width: 320px;
  height: calc(100vh - 50px);
  background-color: #fff;
  overflow: auto;
  &::-webkit-scrollbar{
    width: 10px;
    height: 10px;
  }
  &::-webkit-scrollbar-thumb {
    background: #d1d1d1;
  }
  &::-webkit-scrollbar-track {
    background: #f2f2f2;
  }
  ::v-deep .el-tree {
    height: 100%;
  }
  ::v-deep .el-tree-node {
    min-width: 100% !important;
    display: block !important;
    float: left;
  }
  ::v-deep .currentStyle{
    font-weight: bold;
    color: #333;
  }
  .custom-tree-node span {
    font-size: 14px;
  }
}
.table-page {
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  .el-link {
    margin-right: 10px;
  };
}
.formDialog{
  .el-input{
    width: 90%;
  }
}
::v-deep .group-tree-form{
  margin: 20px 0 0;
  .el-form-item{
    margin: 0;
    width: 100%;
  }
  .el-form-item__content{
    width: 100%!important;
    padding-left: 15px;
    box-sizing: border-box;
  }
}
</style>

