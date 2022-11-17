<template>
  <div class="customer-add">
    <div class="group-tree">
      <div>
        <el-form ref="formSearch" size="mini" inline class="group-tree-form">
          <el-form-item label="" prop="name">
            <el-input
              v-model.trim="orgForm.searchForm.name"
              clearable
              placeholder="请输入机构名称查询"
              style="width:60%; margin-right: 10px"
              @keyup.enter.native="getOrgData"
            />
            <el-button v-auth="'org_add'" type="primary" size="mini" @click="addOrg(null)">创建机构</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-tree
        :data="orgData"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
        :props="{ label: 'name', children: 'childrenList' }"
        class="p-16"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span :class="{currentStyle: currentOrg.id === data.id}" @click="setAccountInfo(data)">{{ data.name }}</span>
          <span class="ml-16">
            <el-button v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="addOrg(data)">创建</el-button>
            <el-button v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="editOrg(data)">编辑</el-button>
            <el-button v-if="!data.isMain" v-auth="'org_add'" type="text" class="ml-16" size="mini" @click="delOrg(data.id)">删除</el-button>
          </span>
        </span>
      </el-tree>
    </div>
    <div class="table-page">
      <el-form v-show="userForm.showSearch" ref="formSearch" :model="userForm.searchForm" size="mini" class="formSearch ml-16" inline>
        <el-form-item label="姓名" prop="name">
          <el-input v-model.trim="userForm.searchForm.name" clearable placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="telephone">
          <el-input v-model.trim="userForm.searchForm.telephone" clearable placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input v-model.trim="userForm.searchForm.account" clearable placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="状态" prop="businessStatus">
          <el-select v-model="userForm.searchForm.businessStatus" placeholder="全部" clearable>
            <el-option
              v-for="item in [{label:'正常',value:1},{label:'冻结',value:2}]"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="" class="btn-center">
          <el-button type="primary" @click="userSearch">查询</el-button>
          <el-button plain @click="userReset">重置</el-button>
        </el-form-item>
      </el-form>
      <my-table
        ref="myTable"
        stripe
        border
        toolbar
        :columns="userForm.columns"
        :search-form="userForm.searchForm"
        @pageRequest="userPageRequest"
        @see="userForm.showSearch = !userForm.showSearch"
      >
        <template slot="toolSlot">
          <div style="width:100%;">
            <el-button v-auth="'org_user_add'" type="primary" size="mini" @click="addUser">新建人员</el-button>
          </div>
        </template>
        <template v-slot:operation="scope">
          <el-link v-auth="'org_user_view'" type="primary" :underline="false" @click="editUser(scope.row, 'view')">查看</el-link>
          <el-link v-auth="'org_user_edit'" type="primary" :underline="false" @click="editUser(scope.row, 'edit')">编辑</el-link>
          <el-link v-auth="'org_user_reset_password'" type="primary" :underline="false" @click="showResetPassword(scope.row)">重置密码</el-link>
          <span v-if="!scope.row.isManage">
            <el-link v-if="scope.row.businessStatus === 1" v-auth="'org_user_freeze'" type="primary" :underline="false" @click="freezeUser(scope.row.id)">冻结</el-link>
            <el-link v-else v-auth="'org_user_start'" type="primary" :underline="false" @click="activeUser(scope.row.id)">解冻</el-link>
            <el-link v-auth="'org_user_del'" type="primary" :underline="false" @click="delUser(scope.row.id)">删除</el-link>
          </span>
          <el-link v-auth="'org_user_data_scope'" type="primary" :underline="false" @click="dataScope(scope.row.id)">数据权限</el-link>
        </template>
      </my-table>
    </div>
    <el-dialog class="formDialog" :title="orgForm.formTitle" :visible.sync="orgForm.formVisible" width="600px">
      <el-form ref="orgForm" :model="orgForm.form" :rules="orgForm.formRules" label-width="120px">
        <el-form-item v-if="orgForm.form.parentName" label="上级名称">
          <el-input v-model="orgForm.form.parentName" disabled />
        </el-form-item>
        <el-form-item label="机构类型" prop="orgType">
          <el-select v-model="orgForm.form.orgType" placeholder="请选择机构类型" clearable :disabled="orgForm.isDisabled">
            <el-option
              v-for="item in [{label:'公司',value:'1'},{label:'部门',value:'2'}]"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="orgForm.form.code" placeholder="请输入编码" :disabled="orgForm.form.id !== null" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="orgForm.form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="orgForm.form.description" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item v-if="orgForm.form.orgType === '1'" label="地址">
          <el-input v-model="orgForm.form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="orgForm.form.sort" placeholder="请输入排序" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="orgForm.formLoading" @click="saveOrg">提交</el-button>
        <el-button @click="orgForm.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog class="formDialog" :title="userForm.formTitle" :visible.sync="userForm.formVisible" width="600px">
      <user-form ref="userForm" :key="userForm.formKey" :user-operation-type="userForm.userOperationType" :is-fill-org="true" />
      <div slot="footer" class="dialog-footer">
        <el-button v-if="userForm.userOperationType!=='view'" type="primary" :loading="userForm.formLoading" @click="saveUser">提 交</el-button>
        <el-button @click="userForm.formVisible = false">取 消</el-button>
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
import {
  activeUser,
  addOrg,
  addUser,
  delOrg, delUser, freezeUser,
  getOrgTree,
  getUserPage,
  resetPassword,
  updateOrg,
  updateUser
} from '@/api/system'
import UserForm from '@/components/system/user/user-form'
import { getFathersById } from '@/utils/common'
import { pPattern3 } from '@/utils/validate-form'

export default {
  name: 'org-manage',
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
      orgData: [],
      currentOrg: {
        id: null,
        name: null,
        data: {}
      },
      orgForm: {
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
          orgType: null,
          parentId: null,
          code: null,
          name: null,
          address: null,
          description: null,
          sort: null
        },
        formRules: {
          name: { required: true, message: '请输入名称' },
          orgType: { required: true, message: '请输入机构类型' },
          code: { required: true, message: '请输入编码' },
          sort: { required: true, message: '请输入排序' }
        }
      },
      userForm: {
        showSearch: true,
        searchForm: {
          name: null,
          account: null,
          email: null,
          telephone: null,
          businessStatus: null,
          orgId: null,
          deptId: null,
          deptIds: null
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
            title: '部门',
            key: 'departName'
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
            width: 400,
            key: 'operation',
            className: 'text-left',
            rowSlot: true
          }
        ],
        formKey: 0,
        formVisible: false,
        formTitle: '新建人员',
        formLoading: false,
        userId: null,
        userOperationType: 'add'
      },
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
      }
    }
  },
  created() {
    if (process.env.NODE_ENV === 'production') {
      this.$message.success('演示环境部分功能禁用！')
    }
    this.getOrgData()
  },
  methods: {
    // 获取组织机构树形列表
    async getOrgData() {
      this.currentOrg = {
        id: null,
        name: null,
        data: null
      }
      const { data } = await getOrgTree({ name: this.orgForm.searchForm.name })
      this.orgData = data
      this.setAccountInfo(this.orgData[0])
    },
    // 组织机构选中查询账号信息
    setAccountInfo(data) {
      this.currentOrg.id = data.id
      this.currentOrg.name = data.name
      this.currentOrg.data = data
      if (data.parentId === '0') {
        // 查询公司
        this.userForm.searchForm.orgId = data.id
        this.userForm.searchForm.deptId = null
        this.userForm.searchForm.deptIds = null
      } else {
        // 查询部门--获取该部门下的所有子级部门ID集
        this.userForm.searchForm.orgId = null
        const deptIds = [data.id]
        this.getChildrenIds(deptIds, data.childrenList)
        this.userForm.searchForm.deptIds = deptIds.join(',')
      }
      this.userSearch()
    },
    // 获取当前及所有子级ID集
    getChildrenIds(ids, nodes) {
      if (nodes && nodes.length > 0) {
        nodes.forEach(node => {
          ids.push(node.id)
          this.getChildrenIds(ids, node.childrenList)
        })
      }
    },
    // 新增组织机构
    addOrg(data) {
      this.orgForm.formTitle = '新建组织机构'
      this.orgForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.orgForm.resetFields()
        if (data) {
          this.orgForm.form.parentName = data.name
          this.orgForm.form.parentId = data.id
          if (data.orgType === '1') {
            this.orgForm.form.orgType = null
            this.orgForm.isDisabled = false
          } else {
            this.orgForm.form.orgType = '2'
            this.orgForm.isDisabled = true
          }
        } else {
          this.orgForm.form.parentName = null
          this.orgForm.form.orgType = '1'
          this.orgForm.isDisabled = true
        }
        this.orgForm.form.id = null
      })
    },
    // 编辑组织机构
    editOrg(data) {
      this.orgForm.formTitle = '编辑组织机构'
      this.orgForm.formVisible = true
      this.orgForm.isDisabled = true
      this.$nextTick(() => {
        // this.$refs.orgForm.resetFields()
        this.orgForm.form = {
          id: data.id,
          parentName: data.parentName,
          parentId: data.parentId,
          orgType: data.orgType,
          code: data.code,
          name: data.name,
          address: data.address,
          description: data.description,
          sort: data.sort
        }
      })
    },
    // 保存组织机构
    saveOrg() {
      this.$refs.orgForm.validate(async(valid) => {
        if (valid) {
          this.orgForm.formLoading = true
          try {
            if (this.orgForm.form.id === null) {
              addOrg(this.orgForm.form)
            } else {
              updateOrg(this.orgForm.form)
            }
            await this.getOrgData()
            this.$message.success('提交成功！')
            this.orgForm.formVisible = false
          } catch (e) {
            this.$message.success('提交失败！')
          } finally {
            this.orgForm.formLoading = false
          }
        }
      })
    },
    // 删除组织机构
    delOrg(id) {
      this.$confirm('确认要删除吗？', '提示', {
        type: 'error'
      }).then(async _ => {
        await delOrg({ orgId: id })
        await this.getOrgData()
        this.$message.success('删除成功！')
      }).catch(_ => {})
    },
    // 用户信息查询请求
    userPageRequest(query, callback) {
      const params = query
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
    // 用户查询
    userSearch() {
      this.$refs.myTable.handleSearch()
    },
    // 用户查询重置
    userReset() {
      this.$refs.formSearch.resetFields()
      this.userSearch()
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
            this.userSearch()
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
        this.userSearch()
      }).catch(_ => {})
    },
    // 账户冻结
    freezeUser(id) {
      this.$confirm('确认要冻结该账号吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await freezeUser({ userId: id })
        this.$message.success('操作成功！')
        this.userSearch()
      }).catch(_ => {})
    },
    // 账号删除
    delUser(id) {
      this.$confirm('确认要删除该账号吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delUser({ userId: id })
        this.$message.success('操作成功！')
        this.userSearch()
      }).catch(_ => {})
    },
    // 数据权限
    dataScope(id) {
      this.$router.push({ path: '/data-scope', query: { userId: id }})
    },
    // 新建人员信息
    addUser() {
      this.userForm.formKey += 1
      this.userForm.formTitle = '创建人员信息'
      this.userForm.userOperationType = 'add'
      this.userForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.userForm.$refs.userForm.resetFields()
        this.$refs.userForm.userForm.password = null
        this.$refs.userForm.userForm.roleIds = []
        this.$refs.userForm.userForm.deptIds = []
      })
    },
    // 查看/编辑人员信息
    editUser(row, type) {
      if (type === 'view') {
        this.userForm.formTitle = '查看人员信息'
        this.userForm.userOperationType = 'view'
      } else {
        if (process.env.NODE_ENV === 'production' && row.isManage) {
          this.$message.success('演示环境管理员功能禁用！')
          return
        }
        this.userForm.formTitle = '编辑人员信息'
        this.userForm.userOperationType = 'edit'
      }
      this.userForm.formVisible = true
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
          roleIds: row.roleIds,
          orgId: row.orgId,
          departId: row.departId,
          deptIds: getFathersById(row.departId, this.orgData, 'id', 'childrenList')
        }
      })
    },
    // 保存/修改人员信息提交
    saveUser() {
      this.$refs.userForm.$refs.userForm.validate(async(valid) => {
        if (valid) {
          this.userForm.formLoading = true
          try {
            if (this.userForm.userOperationType === 'add') {
              await addUser(this.$refs.userForm.userForm)
            } else {
              await updateUser(this.$refs.userForm.userForm)
            }
            this.$message.success('提交成功！')
            this.userForm.formVisible = false
            this.userSearch()
          } finally {
            this.userForm.formLoading = false
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

