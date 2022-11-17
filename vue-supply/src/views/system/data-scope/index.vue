<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="菜单名称" prop="resourceId">
        <el-cascader
          v-model="searchForm.resourceId"
          :options="menuData"
          placeholder="请选择资源菜单"
          :props="{ checkStrictly: false, emitPath: false, label: 'name', value: 'id', children: 'childrenList' }"
          clearable
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userId">
        <el-select
          v-model="searchForm.userId"
          placeholder="请输入用户名称"
          value-key="id"
          filterable
          remote
          clearable
          reserve-keyword
          :remote-method="userRemote"
        >
          <el-option
            v-for="item in userData"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="权限类型" prop="dataScopeType">
        <el-select v-model="searchForm.dataScopeType" placeholder="全部" clearable>
          <el-option
            v-for="item in dataScopeTypeFilter"
            :key="item.key"
            :label="item.value"
            :value="item.key"
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
          <el-button v-auth="'data_scope_create'" type="primary" size="mini" @click="addDataScope">新建</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'data_scope_view'" type="primary" :underline="false" @click="editDataScope(scope.row, 'view')">查看</el-link>
        <el-link v-auth="'data_scope_edit'" type="primary" :underline="false" @click="editDataScope(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'data_scope_remove'" type="primary" :underline="false" @click="delDataScope(scope.row.id)">删除</el-link>
      </template>
    </MyTable>
    <el-dialog class="formDialog" :title="dataScopeInfo.formTitle" :visible.sync="dataScopeInfo.formVisible" width="1500px">
      <data-scope-info ref="dataScopeForm" :data-scope-operation-type="dataScopeInfo.operationType" />
      <div slot="footer" class="dialog-footer">
        <el-button v-if="dataScopeInfo.operationType !== 'view'" type="primary" :loading="dataScopeInfo.formLoading" @click="saveDataScope">确 定</el-button>
        <el-button @click="dataScopeInfo.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addDataScope, delDataScopeById,
  getDataScopePageByParams,
  getDataScopeRangeListByParams, getResourceTree, getUserListByParams,
  updateDataScope
} from '@/api/system'
import DataScopeInfo from '@/views/system/data-scope/form'

const dataScopeTypeList = [
  { key: 1, value: '所有数据权限' },
  { key: 2, value: '角色数据权限' },
  { key: 4, value: '用户数据权限' },
  { key: 5, value: '个人数据权限' },
  { key: 6, value: '自定义数据权限' }
]
export default {
  name: 'data-scope',
  components: {
    DataScopeInfo
  },
  data() {
    return {
      dataScopeTypeList,
      tenantType: this.$store.state.user.tenantInfo.type,
      showSearch: true,
      menuData: [],
      userData: [],
      searchForm: {
        userId: null,
        resourceId: null,
        dataScopeType: null
      },
      dataScopeInfo: {
        formTitle: '新建数据权限',
        formVisible: false,
        operationType: 'add',
        formLoading: false
      },
      columns: [
        {
          title: '用户名称',
          key: 'userName'
        },
        {
          title: '菜单名称',
          key: 'resourceName'
        },
        {
          title: '数据权限类型',
          key: 'dataScopeType',
          formatter(row) {
            const result = dataScopeTypeList.find(function(item) {
              return item.key === row.dataScopeType
            })
            return result.value
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
  async created() {
    await this.getMenuData()
  },
  methods: {
    // 获取资源菜单树形列表
    async getMenuData() {
      const { data } = await getResourceTree({ type: 1 })
      this.menuData = data
      this.dataTrim(this.menuData)
    },
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve) => {
        getDataScopePageByParams(params)
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
    // 新增数据权限
    addDataScope() {
      this.dataScopeInfo.formTitle = '新建数据权限'
      this.dataScopeInfo.formVisible = true
      this.dataScopeInfo.operationType = 'add'
      this.$nextTick(() => {
        this.$refs.dataScopeForm.$refs.dataScopeForm.resetFields()
        this.$refs.dataScopeForm.form = {
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
        }
      })
    },
    // 查看/编辑人员信息
    async editDataScope(row, type) {
      if (type === 'view') {
        this.dataScopeInfo.formTitle = '查看数据权限'
        this.dataScopeInfo.operationType = 'view'
      } else {
        this.dataScopeInfo.formTitle = '编辑数据权限'
        this.dataScopeInfo.operationType = 'edit'
      }
      this.dataScopeInfo.formVisible = true
      // 数据整合
      const { data } = await getDataScopeRangeListByParams({ dataScopeTypeId: row.id })

      const dataScopeRoles = data.filter(item => {
        return item.dataScopeRange === 2
      })

      const dataScopeOrgs = data.filter(item => {
        return item.dataScopeRange === 3
      })

      const dataScopeUsers = data.filter(item => {
        return item.dataScopeRange === 4
      })

      const dataScopeOrgIds = data.filter(item => {
        return item.dataScopeRange === 2
      }).map(item => {
        return item.dataScopeId
      })

      const dataScopeOrgNames = data.filter(item => {
        return item.dataScopeRange === 3
      }).map(item => {
        return item.dataScopeId
      })
      const dataScopeUserNames = data.filter(item => {
        return item.dataScopeRange === 4
      }).map(item => {
        return item.name
      }).join(',')
      this.$nextTick(() => {
        this.$refs.dataScopeForm.$refs.dataScopeForm.resetFields()
        this.$refs.dataScopeForm.form = {
          id: row.id,
          userId: row.userId,
          userName: row.userName,
          resourceId: row.resourceId,
          resourceName: row.resourceName,
          dataScopeType: row.dataScopeType,
          dataScopeUsers: dataScopeUsers,
          dataScopeOrgs: dataScopeOrgs,
          dataScopeRoles: dataScopeRoles,
          dataScopeUserNames: dataScopeUserNames,
          dataScopeOrgNames: dataScopeOrgNames,
          dataScopeRoleIds: dataScopeOrgIds
        }
      })
    },
    // 保存数据权限
    saveDataScope() {
      this.$refs.dataScopeForm.$refs.dataScopeForm.validate(async(valid) => {
        if (valid) {
          const form = this.$refs.dataScopeForm.form
          // 租户类型为个人
          if (this.tenantType === 1) {
            if (form.dataScopeType === 6 && form.dataScopeRoles.length === 0 && form.dataScopeUsers.length === 0) {
              this.$message.warning('权限角色和权限用户需至少填写其一')
              return
            }
          }
          // 租户类型为组织机构
          if (this.tenantType === 2) {
            if (form.dataScopeType === 6 && form.dataScopeRoles.length === 0 && form.dataScopeUsers.length === 0 && form.dataScopeOrgs.length === 0) {
              this.$message.warning('权限角色,权限组织机构和权限用户需至少填写其一')
              return
            }
          }
          this.dataScopeInfo.formLoading = true
          const dataScopeRangeList = form.dataScopeRoles.concat(form.dataScopeUsers).concat(form.dataScopeOrgs)
          const saveForm = {
            id: form.id,
            userId: form.userId,
            resourceId: form.resourceId,
            dataScopeType: form.dataScopeType,
            dataScopeRangeList: dataScopeRangeList
          }
          try {
            if (this.dataScopeInfo.operationType === 'add') {
              await addDataScope(saveForm)
            } else {
              await updateDataScope(saveForm)
            }
            this.$message.success('提交成功！')
            this.dataScopeInfo.formVisible = false
          } catch (e) {
            this.$message.error('提交失败！')
          } finally {
            this.dataScopeInfo.formLoading = false
            this.handleSearch()
          }
        }
      })
    },
    // 删除
    delDataScope(id) {
      this.$confirm('确认要删除该配置项吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delDataScopeById({ dataScopeTypeId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
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
    },
    // 查询用户信息
    async getUsers(name) {
      const { data } = await getUserListByParams({ likeName: name })
      this.userData = data
    },
    userRemote(query) {
      if (query !== '') {
        setTimeout(() => {
          this.getUsers(query)
        }, 200)
      }
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

