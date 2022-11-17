<template>
  <div class="user-add">
    <el-form ref="userForm" :model="userForm" :rules="userFormRules" label-width="120px">
      <el-form-item label="账号" prop="account">
        <el-input v-model="userForm.account" placeholder="请输入账号" :disabled="isDisabled" />
      </el-form-item>
      <el-form-item v-if="operationType==='add'" label="密码" prop="password">
        <el-input v-model="userForm.password" placeholder="请输入密码" :disabled="isDisabled" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="userForm.name" placeholder="请输入员工姓名" :disabled="isDisabled" />
      </el-form-item>
      <el-form-item label="手机号码" prop="telephone">
        <el-input v-model="userForm.telephone" placeholder="请输入手机号码" :disabled="isDisabled" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-radio-group v-model="userForm.sex" :disabled="isDisabled">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="userForm.email" placeholder="请输入邮箱" :disabled="isDisabled" />
      </el-form-item>
      <el-form-item v-if="isFillOrg" label="部门" prop="deptIds">
        <el-cascader
          v-model="userForm.deptIds"
          :disabled="isDisabled"
          placeholder="请选择部门"
          :options="orgData"
          :props="{ checkStrictly: true, label: 'name', value: 'id', children: 'childrenList'}"
          clearable
          @change="handleDeptChange"
        />
      </el-form-item>
      <el-form-item label="角色" prop="roleIds">
        <el-select v-model="userForm.roleIds" multiple clearable placeholder="请选择角色（可多选）" :disabled="isDisabled">
          <el-option v-for="(item, index) in roleList" :key="index" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getOrgTree, getRoleListByParams } from '@/api/system'
import { email, isMobile, pPattern3 } from '@/utils/validate-form'

export default {
  name: 'user-form',
  props: {
    userOperationType: {
      type: String,
      default: () => ''
    },
    isFillOrg: {
      type: Boolean,
      default: () => false
    }
  },
  data() {
    const checkPassword = (rule, value, callback) => {
      if (!pPattern3.pattern.test(value)) {
        return callback(new Error(pPattern3.message))
      }
      callback()
    }
    const checkPhone = (rule, value, callback) => {
      if (!isMobile.pattern.test(value)) {
        return callback(new Error(isMobile.message))
      }
      callback()
    }
    const checkEmail = (rule, value, callback) => {
      if (!email.pattern.test(value)) {
        return callback(new Error(email.message))
      }
      callback()
    }
    return {
      isDisabled: false,
      operationType: 'add',
      roleList: [],
      orgData: [],
      userForm: {
        id: null,
        orgId: null,
        deptIds: [],
        departId: null,
        account: null,
        password: null,
        telephone: null,
        name: null,
        sex: null,
        email: null,
        tenantId: null,
        roleIds: []
      },
      userFormRules: {
        orgId: [{ required: true, message: '请选择机构' }],
        deptIds: [{ required: true, message: '请选择部门' }],
        account: [{ required: true, message: '请输入登录账户' }],
        password: [{ required: true, message: '请输入登录密码' }, { validator: checkPassword }],
        telephone: [{ required: true, message: '请输入手机号码' }, { validator: checkPhone }],
        email: [{ required: true, message: '请输入邮箱' }, { validator: checkEmail }],
        name: [{ required: true, message: '请输入员工姓名' }],
        sex: [{ required: true, message: '请选择员工性别' }],
        roleIds: [{ required: true, message: '请选择员工角色' }]
      }
    }
  },
  watch: {
    userOperationType: {
      handler: function(val) {
        this.operationType = val
        this.isDisabled = this.operationType === 'view'
      },
      deep: true,
      immediate: true
    }
  },
  async created() {
    await this.getRoleList()
    await this.getOrgData()
  },
  methods: {
    async getRoleList() {
      const { data } = await getRoleListByParams()
      this.roleList = data
    },
    async getOrgData() {
      const { data } = await getOrgTree()
      this.orgData = data
      this.disabledTop(this.orgData)
      this.dropEmptyChildren(this.orgData)
    },
    // 禁用顶级
    disabledTop(data) {
      if (data && data.length > 0) {
        data.forEach(item => {
          this.$set(item, 'disabled', true)
        })
      }
    },
    // 去除空子级
    dropEmptyChildren(data) {
      if (data && data.length > 0) {
        data.forEach(item => {
          if (item.childrenList) {
            if (item.childrenList.length > 0) {
              this.dropEmptyChildren(item.childrenList)
            } else {
              delete item.childrenList
            }
          }
        })
      }
    },
    handleDeptChange(value) {
      if (!value) {
        this.userForm.orgId = null
        this.userForm.departId = null
        return
      }
      if (value.length === 1) {
        this.userForm.orgId = value[0]
        this.userForm.departId = null
        return
      }
      this.userForm.orgId = value[0]
      const index = value.length - 1
      this.userForm.departId = value[index]
    }
  }
}
</script>

<style lang='scss' scoped>
.el-input, .el-cascader, .el-select{
  width: 90%;
}
::v-deep .is-disabled{
  .el-input__inner{
    background: #fff!important;
    color: #666!important;
  }
  .el-radio__label{
    color: #666!important;
  }
  .is-checked{
    .el-radio__inner{
      background-color: #409EFF!important;
      border-color: #409EFF!important;
    }
    .el-radio__inner::after{
      background-color: #fff!important;
    }
  }
  .el-radio__inner{
    background-color: #E4E7ED!important;
    border-color: #E4E7ED!important;
  }
  .el-radio__inner::after{
    background-color: #fff!important;
  }
}
</style>
