<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="名称" prop="name">
        <el-input v-model.trim="searchForm.name" clearable placeholder="请输入租户名称" />
      </el-form-item>
      <el-form-item label="编码" prop="code">
        <el-input v-model.trim="searchForm.code" clearable placeholder="请输入租户编码" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="searchForm.type" placeholder="全部" clearable>
          <el-option
            v-for="item in [{label:'个人',value:1},{label:'组织',value:2}]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          v-model="searchForm.endTime"
          class="form-width3"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
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
          <el-button type="primary" size="mini" @click="addTenant">新建租户</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'tenant_view'" type="primary" :underline="false" @click="viewTenant(scope.row.id)">查看</el-link>
        <el-link v-auth="'tenant_edit'" type="primary" :underline="false" @click="editTenant(scope.row)">编辑</el-link>
        <el-link v-if="scope.row.businessStatus === 1" v-auth="'tenant_freeze'" type="primary" :underline="false" @click="freezeTenant(scope.row)">冻结</el-link>
        <el-link v-if="scope.row.businessStatus === 2" v-auth="'tenant_start'" type="primary" :underline="false" @click="activeTenant(scope.row)">解冻</el-link>
        <el-link v-if="scope.row.businessStatus !== 2" v-auth="'tenant_renewal'" type="primary" :underline="false" @click="renewalDays(scope.row)">续期</el-link>
        <el-link v-auth="'tenant_view'" type="primary" :underline="false" @click="delTenant(scope.row)">删除</el-link>
      </template>
    </my-table>
    <el-dialog class="formDialog" :title="renewalDaysForm.formTitle" :visible.sync="renewalDaysForm.formVisible" width="600px">
      <el-form ref="renewalForm" :model="renewalDaysForm.form" label-width="120px">
        <el-form-item label="天数" prop="days">
          <el-input-number v-model="renewalDaysForm.form.days" :step="1" :precision="0" size="medium" :min="1" show-password placeholder="请输入续期" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="renewalDaysForm.formLoading" @click="renewalDaysFormSave">确定提交</el-button>
        <el-button @click="renewalDaysForm.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { activeTenant, delTenant, freezeTenant, getTenantPage, renewalDays } from '@/api/system'
import moment from 'moment'

export default {
  name: 'tenant-manage',
  data() {
    return {
      showSearch: true,
      searchForm: {
        code: null,
        name: null,
        type: null,
        businessStatus: null,
        endTime: []
      },
      columns: [
        {
          title: '名称',
          key: 'name'
        },
        {
          title: '编码',
          key: 'code'
        },
        {
          title: '类型',
          key: 'type',
          formatter(row) {
            if (row.type === 1) {
              return '个人'
            }
            return '组织'
          }
        },
        {
          title: '开始时间',
          key: 'startTime',
          formatter(row) {
            return moment(row.startTime).format('YYYY-MM-DD')
          }
        },
        {
          title: '授权天数',
          key: 'days'
        },
        {
          title: '结束时间',
          key: 'endTime',
          formatter(row) {
            if (!row.endTime) {
              return null
            }
            return moment(row.endTime).format('YYYY-MM-DD')
          }
        },
        {
          title: '状态',
          key: 'businessStatus',
          formatter(row) {
            if (row.businessStatus === 1) {
              return '正常'
            }
            if (row.businessStatus === 2) {
              return '冻结'
            }
            return '已过期'
          }
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true,
          width: 300
        }
      ],
      renewalDaysForm: {
        formTitle: '续期',
        formVisible: false,
        formLoading: false,
        form: {
          tenantId: null,
          days: null
        }
      }
    }
  },
  created() {
    if (process.env.NODE_ENV === 'production') {
      this.$message.success('演示环境部分租户功能禁用！')
    }
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      if (params.endTime?.length) {
        params.endTimeStart = moment(params.endTime[0]).format('YYYY-MM-DD')
        params.endTimeEnd = moment(params.endTime[1]).format('YYYY-MM-DD')
        delete params.endTime
      }
      const promise = new Promise((resolve) => {
        getTenantPage(params)
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
    addTenant() {
      this.$router.push({ path: '/tenant-info-add' })
    },
    // 查看
    viewTenant(id) {
      this.$router.push({ path: '/tenant-info-detail', query: { id: id }})
    },
    // 编辑
    editTenant(row) {
      if (process.env.NODE_ENV === 'production' && (row.code === 'supply' || row.code === 'JXSM')) {
        this.$message.success('演示环对租户对supply和JXSM部分功能禁用！')
        return
      }
      this.$router.push({ path: '/tenant-info-edit', query: { id: row.id }})
    },
    // 冻结
    freezeTenant(row) {
      if (process.env.NODE_ENV === 'production' && (row.code === 'supply' || row.code === 'JXSM')) {
        this.$message.success('演示环对租户对supply和JXSM部分功能禁用！')
        return
      }
      this.$confirm('确认要冻结该租户吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await freezeTenant({ tenantId: row.id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 解冻
    activeTenant(row) {
      if (process.env.NODE_ENV === 'production' && (row.code === 'supply' || row.code === 'JXSM')) {
        this.$message.success('演示环对租户对supply和JXSM部分功能禁用！')
        return
      }
      this.$confirm('确认要解冻该租户吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await activeTenant({ tenantId: row.id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 续期
    renewalDays(row) {
      this.renewalDaysForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.renewalForm.resetFields()
        this.renewalDaysForm.form.tenantId = row.id
      })
    },
    // 续期提交
    renewalDaysFormSave() {
      this.$refs.renewalForm.validate(async(valid) => {
        if (valid) {
          this.renewalDaysForm.formLoading = true
          try {
            await renewalDays(this.renewalDaysForm.form)
            this.$message.success('续期成功！')
            this.handleSearch()
          } catch (e) {
            this.$message.success('续期失败！')
          } finally {
            this.renewalDaysForm.formLoading = false
            this.renewalDaysForm.formVisible = false
          }
        }
      })
    },
    // 删除
    delTenant(row) {
      if (process.env.NODE_ENV === 'production' && (row.code === 'supply' || row.code === 'JXSM')) {
        this.$message.success('演示环对租户对supply和JXSM部分功能禁用！')
        return
      }
      this.$confirm('确认要删除该租户吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delTenant({ userId: row.id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
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
