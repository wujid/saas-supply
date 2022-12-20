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
    <div class="table-page">
      <el-form v-show="defForm.showSearch" ref="formSearch" :model="defForm.searchForm" size="mini" class="formSearch ml-16" inline>
        <el-form-item label="流程名称" prop="processName">
          <el-input v-model.trim="defForm.searchForm.processName" clearable placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="状态" prop="businessStatus">
          <el-select v-model="defForm.searchForm.businessStatus" placeholder="全部" clearable>
            <el-option
              v-for="item in [{label:'激活',value:703},{label:'挂起',value:704}]"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="" class="btn-center">
          <el-button type="primary" @click="defSearch">查询</el-button>
          <el-button plain @click="defReset">重置</el-button>
        </el-form-item>
      </el-form>
      <my-table
        ref="myTable"
        stripe
        border
        toolbar
        :columns="defForm.columns"
        :search-form="defForm.searchForm"
        @pageRequest="defPageRequest"
        @see="defForm.showSearch = !defForm.showSearch"
      >
        <template slot="toolSlot">
          <div style="width:100%;">
            <el-button v-auth="'org_user_add'" type="primary" size="mini" @click="addBpm">新建流程</el-button>
          </div>
        </template>
        <template v-slot:operation="scope">
          <el-link v-auth="'org_user_view'" type="primary" :underline="false" @click="defHistory(scope.row, 'view')">历史版本</el-link>
        </template>
      </my-table>
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
import { addCategory, delCategory, getCategoryTreeByParams, getProcessDefinitionPage, updateCategory } from '@/api/bpm'

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
      },
      defForm: {
        showSearch: true,
        searchForm: {
          processName: null,
          categoryId: null,
          businessStatus: null
        },
        columns: [
          {
            title: '流程名称',
            key: 'processName'
          },
          {
            title: '流程备注',
            key: 'description'
          },
          {
            title: '流程图片',
            key: 'diagramName'
          },
          {
            title: '流程xml',
            key: 'xmlName'
          },
          {
            title: '版本号',
            key: 'version',
            formatter(row) {
              return 'v.' + row.version
            }
          },
          {
            title: '流程状态',
            key: 'businessStatus',
            formatter(row) {
              if (row.businessStatus === 703) {
                return '激活'
              }
              return '挂起'
            }
          },
          {
            title: '操作',
            width: 400,
            key: 'operation',
            className: 'text-left',
            rowSlot: true
          }
        ]
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
    // 获取流程定义信息
    async getDefinitionInfo(data) {
      this.selectedCategory.id = data.id
      this.selectedCategory.name = data.name
      this.selectedCategory.data = data
      this.defSearch()
    },
    // 新建流程分类
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
    // 修改流程分类
    editCategory(data) {
      this.categoryForm.formTitle = '修改流程分类'
      this.categoryForm.formVisible = true
      this.categoryForm.isDisabled = true
      this.$nextTick(() => {
        this.categoryForm.form = {
          id: data.id,
          parentName: data.parentName,
          parentId: data.parentId,
          code: data.code,
          name: data.name,
          sort: data.sort
        }
      })
    },
    // 删除流程分类
    delCategory(id) {
      this.$confirm('确认要删除吗？', '提示', {
        type: 'error'
      }).then(async _ => {
        await delCategory({ categoryId: id })
        await this.getCategoryData()
        this.$message.success('删除成功！')
      }).catch(_ => {})
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
    },
    defPageRequest(query, callback) {
      const params = query
      params.categoryId = this.selectedCategory.id
      const promise = new Promise((resolve) => {
        getProcessDefinitionPage(params)
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
    defSearch() {
      this.$refs.myTable.handleSearch()
    },
    defReset() {
      this.$refs.formSearch.resetFields()
      this.defSearch()
    },
    // 新建流程
    addBpm() {
    },
    // 流程历史版本信息
    defHistory() {
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

