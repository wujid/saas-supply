<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="角色编码" prop="code">
        <el-input v-model.trim="searchForm.code" clearable placeholder="请输入角色编码" />
      </el-form-item>
      <el-form-item label="角色名称" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="状态" prop="businessStatus">
        <el-select v-model="searchForm.businessStatus" placeholder="全部">
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
          <el-button v-auth="'role_add'" type="primary" size="mini" @click="addRole">新建角色</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'role_edit'" type="primary" :underline="false" @click="editRole(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'role_resource_auth'" type="primary" :disabled="scope.row.isSystem" :underline="false" @click="setResourceAuth(scope.row.id)">资源权限</el-link>
        <el-link v-auth="'role_user'" type="primary" :underline="false" @click="roleUser(scope.row)">角色用户</el-link>
        <span v-if="!scope.row.isMain">
          <el-link v-if="scope.row.businessStatus === 1" v-auth="'role_freeze'" type="primary" :underline="false" @click="freezeRole(scope.row.id)">冻结</el-link>
          <el-link v-else v-auth="'role_start'" type="primary" :underline="false" @click="activeRole(scope.row.id)">解冻</el-link>
          <el-link v-auth="'role_remove'" type="primary" :underline="false" @click="delRole(scope.row.id)">删除</el-link>
        </span>
      </template>
    </MyTable>
    <el-dialog :title="roleDetail.formTitle" :visible.sync="roleDetail.formVisible" width="600px">
      <el-form ref="roleForm" :model="roleDetail.form" :rules="roleDetail.formRules">
        <el-form-item label="角色编码" label-width="150px" prop="code">
          <el-input v-model="roleDetail.form.code" placeholder="请填写角色编码" style="width: 90%;" :disabled="roleDetail.isDisabled || roleDetail.roleOperationType === 'edit'" />
        </el-form-item>
        <el-form-item label="角色名称" label-width="150px" prop="name">
          <el-input v-model="roleDetail.form.name" placeholder="请填写角色名称" style="width: 90%;" :disabled="roleDetail.isDisabled" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDetail.formVisible = false">取 消</el-button>
        <el-button type="primary" :loading="roleDetail.formLoading" @click="saveRole">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog v-if="userInfo.formVisible" :title="userInfo.formTitle" :visible.sync="userInfo.formVisible" width="1200px">
      <role-user :role-id="userInfo.roleId" :is-main="userInfo.isMain" />
    </el-dialog>
  </div>
</template>

<script>
import { activeRole, addRole, delRole, freezeRole, getRolePageByParams, updateRole } from '@/api/system'
import RoleUser from '@/views/system/role/user'

export default {
  name: 'role',
  components: {
    RoleUser
  },
  data() {
    return {
      showSearch: true,
      searchForm: {
        code: null,
        name: null,
        businessStatus: null
      },
      columns: [
        {
          title: '角色编码',
          key: 'code'
        },
        {
          title: '角色名称',
          key: 'name'
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
      roleDetail: {
        formVisible: false,
        formTitle: '新建角色',
        formLoading: false,
        roleId: null,
        roleOperationType: 'add',
        isDisabled: false,
        form: {
          id: null,
          name: null,
          code: null
        },
        formRules: {
          code: [{ required: true, message: '请输入角色编码' }],
          name: [{ required: true, message: '请输入角色名称' }]
        }
      },
      userInfo: {
        formVisible: false,
        formTitle: '角色用户',
        formLoading: false,
        roleId: null,
        isMain: false
      }
    }
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve, reject) => {
        getRolePageByParams(params)
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
    // 冻结角色
    freezeRole(id) {
      this.$confirm('确认要冻结该角色吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await freezeRole({ roleId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 解冻角色
    activeRole(id) {
      this.$confirm('确认要解冻该角色吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await activeRole({ roleId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 删除角色
    delRole(id) {
      this.$confirm('确认要删除该角色吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delRole({ roleId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 新建角色
    addRole() {
      this.roleDetail.formTitle = '新建角色'
      this.roleDetail.roleOperationType = 'add'
      this.roleDetail.isDisabled = false
      this.roleDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.roleForm.resetFields()
      })
    },
    // 查看/编辑角色信息
    editRole(row, type) {
      if (type === 'view') {
        this.roleDetail.formTitle = '查看角色信息'
        this.roleDetail.roleOperationType = 'view'
        this.roleDetail.isDisabled = true
      } else {
        this.roleDetail.formTitle = '编辑角色信息'
        this.roleDetail.roleOperationType = 'edit'
      }
      this.roleDetail.formVisible = true
      this.$nextTick(() => {
        this.$refs.roleForm.resetFields()
        this.roleDetail.form = {
          id: row.id,
          code: row.code,
          name: row.name
        }
      })
    },
    // 保存/修改人员信息提交
    saveRole() {
      this.$refs.roleForm.validate(async(valid) => {
        if (valid) {
          this.roleDetail.formLoading = true
          try {
            if (this.roleDetail.roleOperationType === 'add') {
              await addRole(this.roleDetail.form)
            } else {
              await updateRole(this.roleDetail.form)
            }
            this.$message.success('提交成功！')
            this.roleDetail.formVisible = false
            this.handleSearch()
          } catch (e) {
            this.$message.error('提交失败！')
          } finally {
            this.roleDetail.formLoading = false
          }
        }
      })
    },
    // 资源权限
    setResourceAuth(id) {
      this.$router.push({ path: '/role-auth', query: { id: id }})
    },
    // 角色用户
    roleUser(row) {
      this.userInfo.roleId = row.id
      this.userInfo.isMain = row.isMain
      this.userInfo.formVisible = true
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

