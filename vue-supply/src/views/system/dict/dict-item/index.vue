<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="名称" prop="label">
        <el-input v-model.trim="searchForm.label" clearable placeholder="请输入名称" />
      </el-form-item>
      <el-form-item label="值" prop="value">
        <el-input v-model.trim="searchForm.value" clearable placeholder="请输入值" />
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
          <el-button type="primary" size="mini" @click="addDictItem">新建</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link type="primary" :underline="false" @click="editDictItem(scope.row, 'view')">查看</el-link>
        <el-link type="primary" :underline="false" @click="editDictItem(scope.row, 'edit')">编辑</el-link>
        <el-link type="primary" :underline="false" @click="delDictItem(scope.row.id)">删除</el-link>
      </template>
    </MyTable>
    <el-dialog :title="dictItemDetail.formTitle" :visible.sync="dictItemDetail.formVisible" width="600px" append-to-body>
      <el-form ref="dictItemForm" :model="dictItemDetail.form" :rules="dictItemDetail.formRules">
        <el-form-item label="名称" label-width="150px" prop="label">
          <el-input v-model="dictItemDetail.form.label" placeholder="请填写字典名称" style="width: 90%;" :disabled="dictItemDetail.isDisabled" />
        </el-form-item>
        <el-form-item label="值" label-width="150px" prop="value">
          <el-input v-model="dictItemDetail.form.value" placeholder="请填写值" style="width: 90%;" :disabled="dictItemDetail.isDisabled" />
        </el-form-item>
        <el-form-item label="排序" label-width="150px" prop="sort">
          <el-input-number v-model="dictItemDetail.form.sort" :step="1" :min="1" :precision="0" placeholder="请填写排序" style="width: 90%;" :disabled="dictItemDetail.isDisabled" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dictItemDetail.dictItemOperationType !== 'view'" type="primary" :loading="dictItemDetail.formLoading" @click="saveDictItem">确 定</el-button>
        <el-button @click="dictItemDetail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addDictItem, delDictItem, getDictItemPageByParams, updateDictItem } from '@/api/system'

export default {
  name: 'dict-item',
  props: {
    dictId: {
      type: String,
      default: () => null
    }
  },
  data() {
    return {
      showSearch: true,
      searchForm: {
        label: null,
        value: null,
        dictId: this.dictId
      },
      columns: [
        {
          title: '名称',
          key: 'label'
        },
        {
          title: '值',
          key: 'value'
        },
        {
          title: '排序',
          key: 'sort'
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ],
      dictItemDetail: {
        dictItemOperationType: 'add',
        isDisabled: false,
        formVisible: false,
        form: {
          id: null,
          dictId: this.dictId,
          label: null,
          value: null,
          sort: null
        },
        formRules: {
          label: [{ required: true, message: '请输入名称' }],
          value: [{ required: true, message: '请输入值' }]
        }
      }
    }
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve) => {
        getDictItemPageByParams(params)
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
    // 新建
    addDictItem() {
      this.dictItemDetail.formTitle = '新建'
      this.dictItemDetail.dictItemOperationType = 'add'
      this.dictItemDetail.isDisabled = false
      this.dictItemDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictItemForm.resetFields()
      })
    },
    // 查看/编辑
    editDictItem(row, type) {
      if (type === 'view') {
        this.dictItemDetail.formTitle = '查看字典信息'
        this.dictItemDetail.dictItemOperationType = 'view'
        this.dictItemDetail.isDisabled = true
      } else {
        this.dictItemDetail.formTitle = '编辑字典信息'
        this.dictItemDetail.dictItemOperationType = 'edit'
      }
      this.dictItemDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.dictItemForm.resetFields()
        this.dictItemDetail.form = {
          id: row.id,
          label: row.label,
          value: row.value,
          sort: row.sort
        }
      })
    },
    // 删除
    delDictItem(id) {
      this.$confirm('确认要删除该字典数据吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delDictItem({ dictItemId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 保存/修改
    saveDictItem() {
      this.$refs.dictItemForm.validate(async(valid) => {
        if (valid) {
          this.dictItemDetail.formLoading = true
          try {
            if (this.dictItemDetail.dictItemOperationType === 'add') {
              await addDictItem(this.dictItemDetail.form)
            } else {
              await updateDictItem(this.dictItemDetail.form)
            }
            this.$message.success('提交成功！')
            this.dictItemDetail.formVisible = false
            this.handleSearch()
          } catch (e) {
            this.$message.error('提交失败！')
          } finally {
            this.dictItemDetail.formLoading = false
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
