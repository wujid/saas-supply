<template>
  <el-dialog :title="title" :visible.sync="visible" :width="width" :before-close="handleClose" append-to-body>
    <el-form ref="formSearch" :model="searchForm" size="mini" class="formSearch m-0 pl-0" inline>
      <el-form-item label="账号" prop="account">
        <el-input v-model.trim="searchForm.account" clearable placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="">
        <el-button plain @click="reset">重置</el-button>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </el-form>
    <my-table
      ref="myTable"
      stripe
      border
      selection
      auto-height
      :page-choose="true"
      :select-tips="false"
      :columns="columns"
      :search-form="searchForm"
      @pageRequest="pageRequest"
      @selection-change="selectionChange"
    />
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleOk">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getUserPage } from '@/api/system'

export default {
  name: 'user-choose',
  props: {
    visible: {
      type: Boolean,
      default: () => false
    },
    title: {
      type: String,
      default: () => '选择用户'
    },
    width: {
      type: String,
      default: () => '50%'
    }
  },
  data() {
    return {
      searchForm: {
        name: null,
        account: null
      },
      columns: [
        {
          title: '',
          key: 'radio',
          width: 55,
          rowSlot: true
        },
        {
          title: '账号',
          key: 'account'
        },
        {
          title: '姓名',
          key: 'name'
        },
        {
          title: '联系电话',
          key: 'telephone'
        }
      ],
      selectList: []
    }
  },
  created() {
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve, reject) => {
        getUserPage(params)
          .then((res) => {
            resolve({
              data: res.data.records,
              total: parseInt(res.data.total)
            })
            this.tableData = res.data.records
          })
          .catch(() => {
            resolve({
              data: [],
              total: 0
            })
            this.tableData = []
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
    // 关闭
    handleClose() {
      this.$emit('update:visible', false)
    },
    selectionChange(e) {
      this.selectList = e
    },
    handleOk() {
      if (this.selectList.length) {
        this.$emit('change', this.selectList)
        this.handleClose()
      } else {
        this.$message.warning('请选择用户')
      }
    }
  }
}
</script>
