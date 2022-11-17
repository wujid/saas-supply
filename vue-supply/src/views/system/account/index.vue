<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="姓名" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="联系电话" prop="telephone">
        <el-input v-model.trim="searchForm.telephone" clearable placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item label="账号" prop="account">
        <el-input v-model.trim="searchForm.account" clearable placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model.trim="searchForm.email" clearable placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="状态" prop="businessStatus">
        <el-select v-model="searchForm.businessStatus" placeholder="全部" clearable>
          <el-option
            v-for="item in [{label:'正常',value:1},{label:'冻结',value:2}]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
      @pageRequest="pageRequest"
      @see="showSearch = !showSearch"
    >
      <template slot="toolSlot">
        <div style="width:100%;">
          <el-button v-auth="'account_add_user'" type="primary" size="mini" @click="addUser">新建人员</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'account_view'" type="primary" :underline="false" @click="editUser(scope.row, 'view')">查看</el-link>
        <el-link v-auth="'account_edit'" type="primary" :underline="false" @click="editUser(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'account_reset_password'" type="primary" :underline="false" @click="showResetPassword(scope.row)">重置密码</el-link>
        <span v-if="!scope.row.isManage">
          <el-link v-if="scope.row.businessStatus === 1" v-auth="'account_freeze'" type="primary" :underline="false" @click="freezeUser(scope.row.id)">冻结</el-link>
          <el-link v-else v-auth="'account_start'" type="primary" :underline="false" @click="activeUser(scope.row.id)">解冻</el-link>
          <el-link v-auth="'account_remove'" type="primary" :underline="false" @click="delUser(scope.row.id)">删除</el-link>
        </span>
        <el-link v-auth="'account_remove'" type="primary" :underline="false" @click="dataScope(scope.row.id)">数据权限</el-link>
      </template>
    </my-table>
    <el-dialog class="formDialog" :title="userDetail.formTitle" :visible.sync="userDetail.formVisible" width="600px">
      <user-form ref="userForm" :user-operation-type="userDetail.userOperationType" />
      <div slot="footer" class="dialog-footer">
        <el-button v-if="userDetail.userOperationType!=='view'" type="primary" :loading="userDetail.formLoading" @click="saveUser">提 交</el-button>
        <el-button @click="userDetail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog class="formDialog" :title="resetPassword.formTitle" :visible.sync="resetPassword.formVisible" width="600px">
      <el-form ref="resetPasswordForm" :model="resetPassword.form" :rules="resetPassword.formRules" label-width="120px">
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetPassword.form.password" show-password placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPassword.form.confirmPassword" show-password placeholder="请输入确认密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="resetPassword.formLoading" @click="resetPasswordFormSave">提 交</el-button>
        <el-button @click="resetPassword.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { activeUser, addUser, delUser, freezeUser, getUserPage, resetPassword, updateUser } from '@/api/system'
import { pPattern3 } from '@/utils/validate-form'
import UserForm from '@/components/system/user/user-form'

export default {
  name: 'account-manage',
  components: {
    UserForm
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (!pPattern3.pattern.test(value)) {
        return callback(new Error(pPattern3.message))
      }
      callback()
    }
    const confirmPassword = (rule, value, callback) => {
      if (value !== this.resetPassword.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
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
      ],
      resetPassword: {
        formTitle: '重置密码',
        formVisible: false,
        form: {
          userId: null,
          password: null,
          confirmPassword: null
        },
        formRules: {
          password: [{ required: true, message: '请输入密码' }, { validator: validatePassword }],
          confirmPassword: [{ required: true, message: '请输入确认密码' }, { validator: confirmPassword }]
        },
        formLoading: false
      },
      userDetail: {
        formVisible: false,
        formTitle: '新建人员',
        formLoading: false,
        userId: null,
        userOperationType: 'add'
      }
    }
  },
  created() {
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      params.isUseDataScope = true
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
    // 重置密码
    showResetPassword(row) {
      if (process.env.NODE_ENV === 'production' && row.isManage) {
        this.$message.success('演示环境管理员功能禁用！')
        return
      }
      this.resetPassword.formVisible = true
      this.$nextTick(() => {
        this.$refs.resetPasswordForm.resetFields()
        this.resetPassword.form.userId = row.id
      })
    },
    // 重置密码提交
    resetPasswordFormSave() {
      this.$refs.resetPasswordForm.validate(async(valid) => {
        if (valid) {
          this.resetPassword.formLoading = true
          try {
            await resetPassword(this.resetPassword.form)
            this.$message.success('重置成功！')
          } catch (e) {
            this.$message.success('重置失败！')
          } finally {
            this.resetPassword.formLoading = false
            this.resetPassword.formVisible = false
          }
        }
      })
    },
    // 账户解冻
    activeUser(id) {
      this.$confirm('确认要解冻该账号吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await activeUser({ userId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 账户冻结
    freezeUser(id) {
      this.$confirm('确认要冻结该账号吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await freezeUser({ userId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 账号删除
    delUser(id) {
      this.$confirm('确认要删除该账号吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delUser({ userId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 数据权限
    dataScope(id) {
      this.$router.push({ path: '/data-scope', query: { userId: id }})
    },
    // 新建人员信息
    addUser() {
      this.userDetail.formTitle = '创建人员信息'
      this.userDetail.userOperationType = 'add'
      this.userDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.userForm.$refs.userForm.resetFields()
        this.$refs.userForm.userForm.password = null
        this.$refs.userForm.userForm.roleIds = []
      })
    },
    // 查看/编辑人员信息
    editUser(row, type) {
      if (type === 'view') {
        this.userDetail.formTitle = '查看人员信息'
        this.userDetail.userOperationType = 'view'
      } else {
        if (process.env.NODE_ENV === 'production' && row.isManage) {
          this.$message.success('演示环境管理员功能禁用！')
          return
        }
        this.userDetail.formTitle = '编辑人员信息'
        this.userDetail.userOperationType = 'edit'
      }
      this.userDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.userForm.$refs.userForm.resetFields()
        this.$refs.userForm.userForm = {
          id: row.id,
          account: row.account,
          name: row.name,
          telephone: row.telephone,
          password: row.password,
          sex: row.sex,
          email: row.email,
          tenantId: row.tenantId,
          roleIds: row.roleIds
        }
      })
    },
    // 保存/修改人员信息提交
    saveUser() {
      this.$refs.userForm.$refs.userForm.validate(async(valid) => {
        if (valid) {
          this.userDetail.formLoading = true
          try {
            if (this.userDetail.userOperationType === 'add') {
              await addUser(this.$refs.userForm.userForm)
            } else {
              await updateUser(this.$refs.userForm.userForm)
            }
            this.$message.success('提交成功！')
            this.userDetail.formVisible = false
            this.handleSearch()
          } finally {
            this.userDetail.formLoading = false
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
::v-deep .tool-bar{
  margin: 0;
}
</style>

