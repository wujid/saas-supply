<template>
  <div class="edit-box">
    <div class="data-scope-form">
      <el-form ref="dataScopeForm" class="data-scope-info" label-width="150px" :model="form" :rules="rules">
        <div class="base-info clearfix">
          <el-row class="row-item">
            <el-col>
              <el-form-item label="用户名称：" prop="userName">
                <el-input v-model="form.userName" clearable style="width: 300px" class="form-width" placeholder="请选择权限用户" disabled />
                <el-button style="margin-left: 10px" :disabled="operationType !== 'add'" @click="userVisible = true">选择</el-button>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col>
              <el-form-item label="菜单名称：" prop="resourceId">
                <el-cascader
                  v-model="form.resourceId"
                  :options="menuData"
                  placeholder="请选择资源菜单"
                  :props="{ checkStrictly: false, emitPath: false, label: 'name', value: 'id', children: 'childrenList' }"
                  :disabled="operationType !== 'add'"
                  clearable
                  style="width: 300px"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col>
              <el-form-item label="权限类型：" prop="dataScopeType">
                <el-select v-model="form.dataScopeType" style="width: 300px" placeholder="全部" :disabled="operationType === 'view'" @change="changeDataScopeType">
                  <el-option
                    v-for="item in dataScopeTypeFilter"
                    :key="item.key"
                    :label="item.value"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col v-show="form.dataScopeType === 2 || form.dataScopeType === 6">
              <el-form-item label="权限角色：" prop="dataScopeRoleIds">
                <el-select v-model="form.dataScopeRoleIds" style="width: 300px" multiple clearable placeholder="请选择权限角色（可多选）" :disabled="operationType === 'view'" @change="changeDataScopeRole">
                  <el-option v-for="(item, index) in roleData" :key="index" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col v-show="tenantType === 2 && (form.dataScopeType === 3 || form.dataScopeType === 6)">
              <el-form-item label="权限组织机构：" prop="dataScopeOrgNames">
                <el-cascader
                  v-model="form.dataScopeOrgNames"
                  clearable
                  style="width: 300px"
                  :options="orgData"
                  placeholder="请选择权限组织机构"
                  :props="{ checkStrictly: false, emitPath: false, multiple: true, label: 'name', value: 'id', children: 'childrenList' }"
                  :disabled="operationType === 'view'"
                  @change="getOrgSelected"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col v-show="form.dataScopeType === 4 || form.dataScopeType === 6">
              <el-form-item label="权限用户：" prop="dataScopeUserNames">
                <el-input v-model="form.dataScopeUserNames" style="width: 300px" class="form-width" placeholder="请选择权限用户" disabled />
                <el-button style="margin-left: 10px" :disabled="operationType === 'view'" @click="chooseUserVisible = true">选择</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <user-select v-if="userVisible" :visible.sync="userVisible" @change="userChange" />
    <user-choose v-if="chooseUserVisible" :visible.sync="chooseUserVisible" @change="chooseUserChange" />
  </div>
</template>

<script>
import UserSelect from '@/components/system/user/user-select-dialog'
import UserChoose from '@/components/system/user/user-choose-dialog'
import { getOrgTree, getResourceTree, getRoleListByParams } from '@/api/system'

const dataScopeTypeList = [
  { key: 1, value: '所有数据权限' },
  { key: 2, value: '角色数据权限' },
  { key: 4, value: '用户数据权限' },
  { key: 5, value: '个人数据权限' },
  { key: 6, value: '自定义数据权限' }
]
export default {
  name: 'data-scope-info',
  components: {
    UserChoose,
    UserSelect
  },
  props: {
    dataScopeOperationType: {
      type: String,
      default: () => ''
    }
  },
  data() {
    const validateRole = (rule, value, callback) => {
      if (this.form.dataScopeType === 2 && (!value || value.length === 0)) {
        return callback(new Error('请选择权限角色'))
      }
      callback()
    }
    const validateUser = (rule, value, callback) => {
      if (this.form.dataScopeType === 4 && !value) {
        return callback(new Error('请选择权限用户'))
      }
      callback()
    }
    const validateOrg = (rule, value, callback) => {
      if (this.form.dataScopeType === 3 && !value) {
        return callback(new Error('请选择权限组织机构'))
      }
      callback()
    }
    return {
      dataScopeTypeList,
      tenantType: this.$store.state.user.tenantInfo.type,
      userVisible: false,
      chooseUserVisible: false,
      menuData: [],
      orgData: [],
      roleData: [],
      operationType: '',
      form: {
        id: null,
        userId: null,
        userName: null,
        resourceId: null,
        resourceName: null,
        dataScopeType: null,
        dataScopeUsers: [],
        dataScopeOrgs: [],
        dataScopeRoles: [],
        dataScopeUserNames: null,
        dataScopeOrgNames: [],
        dataScopeRoleIds: []
      },
      rules: {
        'userName': [{ required: true, message: '请选择权限用户' }],
        'resourceId': [{ required: true, message: '请选择资源菜单' }],
        'dataScopeType': [{ required: true, message: '请选择权限类型' }],
        'dataScopeOrgNames': [{ validator: validateOrg }],
        'dataScopeUserNames': [{ validator: validateUser }],
        'dataScopeRoleIds': [{ validator: validateRole }]
      }
    }
  },
  computed: {
    dataScopeTypeFilter() {
      const list = this.dataScopeTypeList
      if (this.tenantType === 2) {
        const isHave = list.some(item => {
          return item.key === 3
        })
        if (!isHave) {
          list.push({ key: 3, value: '组织机构数据权限' })
        }
      }
      return list
    }
  },
  watch: {
    dataScopeOperationType: {
      handler: function(val) {
        this.operationType = val
      },
      deep: true,
      immediate: true
    }
  },
  async created() {
    await this.getMenuData()
    await this.getOrgData()
    await this.getRoleData()
  },
  methods: {
    // 获取资源菜单树形列表
    async getMenuData() {
      const { data } = await getResourceTree({ type: 1 })
      this.menuData = data
      this.dataTrim(this.menuData)
    },
    // 获取组织机构树形列表
    async getOrgData() {
      const { data } = await getOrgTree()
      this.orgData = data
      this.dataTrim(this.orgData)
    },
    // 获取角色列表
    async getRoleData() {
      const { data } = await getRoleListByParams()
      this.roleData = data
    },
    // 当前权限设置选中的用户
    userChange(params) {
      if (params.isManage) {
        this.$message.warning('管理员不允许权限设置')
        return
      }
      this.form.userId = params.id
      this.form.userName = params.name
    },
    // 权限类型改变时显示不同的权限域
    changeDataScopeType() {
      this.form.dataScopeUserNames = null
      this.form.dataScopeOrgNames = null
      this.form.dataScopeUsers = []
      this.form.dataScopeOrgs = []
    },
    // 权限选中的用户集
    chooseUserChange(list) {
      this.form.dataScopeUserNames = list.map(item => {
        return item.name
      }).join(',')
      this.form.dataScopeUsers = list.map(item => {
        return { dataScopeRange: 4, dataScopeId: item.id }
      })
    },
    // 权限选中的组织机构集
    getOrgSelected(list) {
      this.form.dataScopeOrgs = list.map(item => {
        return { dataScopeRange: 3, dataScopeId: item }
      })
    },
    // 权限选中的角色集
    changeDataScopeRole(list) {
      this.form.dataScopeRoles = list.map(item => {
        return { dataScopeRange: 2, dataScopeId: item }
      })
    },
    // 递归将空子级改成undefined
    dataTrim(childrenList) {
      childrenList.forEach(children => {
        if (children.childrenList.length === 0) {
          children.childrenList = undefined
        } else {
          this.dataTrim(children.childrenList)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-box {
  background-color: #fff;
  padding: 10px 5px;
  .data-scope-form {
    margin: 5px;
    padding: 20px;
    border: 1px solid #f6f6f6;
  }
  .data-scope-info {
    width: 100%;
    .title-bar {
      font-size: 14px;
      border-bottom: 1px solid #e1e1e1;
      padding-bottom: 5px;
      margin-bottom: 40px;
    }
    .row-item {
      width: 100%;
      float: right;
    }
    .selectLong {
      width: 10%;
      margin-left: 10px;
    }
    .credentialTypeBox {
      display: inline-block;
      max-width: 270px;
    }
    .credentialNumberBox {
      display: inline-block;
      width: calc(100% - 290px);
      max-width: 185px;
    }
    .credentialType {
      width: 120px;
    }
    .credentialNumber {
      // width: 220px;
      margin-left: 10px;
    }
  }
  .btn_box {
    padding: 50px 0 20px;
    text-align: center;
  }
}
</style>
