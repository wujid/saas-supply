<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="字典名称" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入字典名称" />
      </el-form-item>
      <el-form-item label="字典编码" prop="code">
        <el-input v-model.trim="searchForm.code" clearable placeholder="请输入字典编码" />
      </el-form-item>
      <el-form-item label="" class="btn-center">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button plain @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <MyTable
      ref="myTable"
      stripe
      border
      toolbar
      :columns="columns"
      :search-form="searchForm"
      @pageRequest="pageRequest"
      @see="showSearch = !showSearch"
    >
      <template slot="toolSlot">
        <div style="width:100%;">
          <el-button v-auth="'dict_add'" type="primary" size="mini" @click="addDict">新建</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'dict_view'" type="primary" :underline="false" @click="editDict(scope.row, 'view')">查看</el-link>
        <el-link v-auth="'dict_edit'" type="primary" :underline="false" @click="editDict(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'dict_config'" type="primary" :underline="false" @click="dictConfig(scope.row.id)">配置</el-link>
        <el-link v-auth="'dict_remove'" type="primary" :underline="false" @click="delDict(scope.row.id)">删除</el-link>
      </template>
    </MyTable>
    <el-dialog :title="dictDetail.formTitle" :visible.sync="dictDetail.formVisible" width="600px">
      <el-form ref="dictForm" :model="dictDetail.form" :rules="dictDetail.formRules">
        <el-form-item label="字典名称" label-width="150px" prop="name">
          <el-input v-model="dictDetail.form.name" placeholder="请填写字典名称" style="width: 90%;" :disabled="dictDetail.isDisabled" />
        </el-form-item>
        <el-form-item label="字典编码" label-width="150px" prop="code">
          <el-input v-model="dictDetail.form.code" placeholder="请填写字典编码" style="width: 90%;" :disabled="dictDetail.isDisabled" />
        </el-form-item>
        <el-form-item label="字典描述" label-width="150px" prop="description">
          <el-input v-model="dictDetail.form.description" placeholder="请填写字典描述" style="width: 90%;" :disabled="dictDetail.isDisabled" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dictDetail.dictOperationType !== 'view'" type="primary" :loading="dictDetail.formLoading" @click="saveDict">确 定</el-button>
        <el-button @click="dictDetail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog v-if="dictItemDetail.formVisible" :title="dictItemDetail.formTitle" :visible.sync="dictItemDetail.formVisible" width="1000px">
      <dict-item :dict-id="dictItemDetail.dictId" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="dictItemDetail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addDict, delDict, getDictPageByParams, updateDict } from '@/api/system'
import DictItem from '@/views/system/dict/dict-item'

export default {
  name: 'dict-manage',
  components: {
    DictItem
  },
  data() {
    return {
      showSearch: true,
      searchForm: {
        code: null,
        name: null
      },
      columns: [
        {
          title: '字典名称',
          key: 'name'
        },
        {
          title: '字典编码',
          key: 'code'
        },
        {
          title: '字典描述',
          key: 'description'
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ],
      dictDetail: {
        dictOperationType: 'add',
        isDisabled: false,
        formVisible: false,
        dictId: null,
        form: {
          id: null,
          name: null,
          code: null,
          description: null
        },
        formRules: {
          code: [{ required: true, message: '请输入字典编码' }],
          name: [{ required: true, message: '请输入字典名称' }]
        }
      },
      dictItemDetail: {
        formVisible: false,
        formTitle: '配置字典',
        formLoading: false,
        dictId: null
      }
    }
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve) => {
        getDictPageByParams(params)
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
    // 新增字典
    addDict() {
      this.dictDetail.formTitle = '新建字典'
      this.dictDetail.dictOperationType = 'add'
      this.dictDetail.isDisabled = false
      this.dictDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictForm.resetFields()
      })
    },
    // 查看/编辑字典
    editDict(row, type) {
      if (type === 'view') {
        this.dictDetail.formTitle = '查看字典信息'
        this.dictDetail.dictOperationType = 'view'
        this.dictDetail.isDisabled = true
      } else {
        this.dictDetail.formTitle = '编辑字典信息'
        this.dictDetail.dictOperationType = 'edit'
        this.dictDetail.isDisabled = false
      }
      this.dictDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictForm.resetFields()
        this.dictDetail.form = {
          id: row.id,
          code: row.code,
          name: row.name,
          description: row.description
        }
      })
    },
    // 字典配置
    dictConfig(id) {
      this.dictItemDetail.dictId = id
      this.dictItemDetail.formVisible = true
    },
    // 字典删除
    delDict(id) {
      this.$confirm('确认要删除该字典吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delDict({ dictId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 字典新增/修改
    saveDict() {
      this.$refs.dictForm.validate(async(valid) => {
        if (valid) {
          this.dictDetail.formLoading = true
          try {
            if (this.dictDetail.dictOperationType === 'add') {
              await addDict(this.dictDetail.form)
            } else {
              await updateDict(this.dictDetail.form)
            }
            this.$message.success('提交成功！')
            this.handleSearch()
            this.dictDetail.formVisible = false
          } finally {
            this.dictDetail.formLoading = false
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
}
</style>
