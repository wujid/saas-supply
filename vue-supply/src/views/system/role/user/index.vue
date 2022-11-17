<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="姓名" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="账号" prop="account">
        <el-input v-model.trim="searchForm.account" clearable placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="">
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
      @pageRequest="pageRequest"
      @see="showSearch = !showSearch"
    >
      <template slot="toolSlot">
        <div style="width:100%;">
          <el-button type="primary" size="mini" @click="addUserRole.userVisible = true">添加</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-if="!(isMain && scope.row.isManage)" type="primary" :underline="false" @click="delUserRole(scope.row.id)">取消授权</el-link>
      </template>
    </my-table>
    <user-select v-if="addUserRole.userVisible" :visible.sync="addUserRole.userVisible" @change="userRoleChange" />
  </div>
</template>

<script>
import UserSelect from '@/components/system/user/user-select-dialog'
import { addUserRole, delUserAuth, getUserPage } from '@/api/system'

export default {
  name: 'role-user',
  components: {
    UserSelect
  },
  props: {
    roleId: {
      type: String,
      default: () => null
    },
    isMain: {
      type: Boolean,
      default: () => false
    }
  },
  data() {
    return {
      showSearch: true,
      searchForm: {
        name: null,
        account: null,
        telephone: null,
        sex: null,
        email: null,
        businessStatus: null
      },
      addUserRole: {
        userVisible: false,
        title: '选择用户'
      },
      columns: [
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
        },
        {
          title: '状态',
          key: 'businessStatus',
          formatter(row, column, cellValue, index) {
            if (row.businessStatus === 1) {
              return '正常'
            }
            return '冻结'
          }
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ]
    }
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      params.roleId = this.roleId
      const promise = new Promise((resolve, reject) => {
        getUserPage(params)
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
    // 取消授权
    delUserRole(id) {
      this.$confirm('确认要取消该用户授权吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delUserAuth({ userId: id, roleId: this.roleId })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 新增权限用户选择用户回调
    async userRoleChange(params) {
      await addUserRole({ userId: params.id, roleId: this.roleId })
      this.$message.success('提交成功！')
      this.handleSearch()
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
::v-deep .tool-bar{
  margin: 0;
}
</style>
