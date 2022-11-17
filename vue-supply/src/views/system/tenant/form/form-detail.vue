<template>
  <div class="edit-box">
    <div class="tenant-form">
      <el-form ref="tenantForm" class="tenant-info" label-width="150px" :model="form" disabled>
        <div class="base-info clearfix">
          <div class="title-bar">租户信息</div>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="租户名称：" prop="tenant.name">
                <el-input v-model="form.tenant.name" class="form-width" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="租户编码：" prop="tenant.code">
                <el-input v-model="form.tenant.code" class="form-width" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="租户类型：" prop="tenant.type">
                <el-select v-model="form.tenant.type">
                  <el-option :value="1" label="个人" />
                  <el-option :value="2" label="机构" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="开始时间：" prop="tenant.startTime">
                <el-date-picker
                  v-model="form.tenant.startTime"
                  class="form-width"
                  type="date"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="授权天数：" prop="tenant.days">
                <el-input-number v-model="form.tenant.days" :precision="0" class="form-width" size="medium" placeholder="请输入授权天数" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="结束时间：" prop="tenant.endTime">
                <el-date-picker
                  v-model="form.tenant.endTime"
                  class="form-width"
                  type="date"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div class="user-info clearfix">
          <div class="title-bar">管理员信息</div>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="账号：" prop="user.account">
                <el-input v-model="form.user.account" class="form-width" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="密码：" prop="user.password">
                <el-input v-model="form.user.password" class="form-width" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="名称：" prop="user.name">
                <el-input v-model="form.user.name" class="form-width" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-if="showOrg" class="org-info clearfix">
          <div class="title-bar">组织机构信息</div>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="机构编码：" prop="org.code">
                <el-input v-model="form.org.code" class="form-width" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="机构名称：" prop="org.name">
                <el-input v-model="form.org.name" class="form-width" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div class="role-info clearfix">
          <div class="title-bar">角色信息</div>
          <MyTable
            stripe
            border
            auto-height
            :select-tips="false"
            :pagination="false"
            :data="form.roleList"
            :columns="roleColumns"
            class="m-0 p-0"
          >
            <template v-slot:name="scope">
              <el-input v-model="scope.row.code" class="form-width" />
            </template>
            <template v-slot:name="scope">
              <el-input v-model="scope.row.name" class="form-width" />
            </template>
            <template v-slot:description="scope">
              <el-input v-model="scope.row.description" class="form-width" />
            </template>
          </MyTable>
        </div>
        <div class="resource-info clearfix">
          <div class="title-bar">资源信息</div>
          <auth-tree v-for="(item, index) in resourceData.dataList" :key="index" :tree-data="item" />
        </div>
      </el-form>
    </div>
    <div class="btn_box">
      <el-button @click="backForward">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getTenantDetail } from '@/api/system'
import AuthTree from '@/components/auth/auth-tree'

export default {
  name: 'tenant-detail',
  components: {
    AuthTree
  },
  data() {
    return {
      tenantId: null,
      form: {
        tenant: {
          code: null,
          name: null,
          type: null,
          startTime: null,
          days: null,
          endTime: null
        },
        user: {
          account: null,
          password: null,
          name: null
        },
        org: {
          code: null,
          name: null
        },
        roleList: []
      },
      showOrg: false,
      formLoading: false,
      resourceData: {
        dataList: []
      },
      roleColumns: [
        {
          title: '序号',
          key: 'index'
        },
        {
          title: '系统角色',
          key: 'isSystem',
          width: 100,
          formatter(row) {
            if (row.isSystem) {
              return '是'
            }
            return '否'
          }
        },
        {
          title: '角色编码',
          key: 'code',
          rowSlot: true
        },
        {
          title: '角色名称',
          key: 'name',
          rowSlot: true
        },
        {
          title: '角色描述',
          key: 'description',
          rowSlot: true
        }
      ]
    }
  },
  async created() {
    this.tenantId = this.$route.query.id
    const { data } = await getTenantDetail({ tenantId: this.tenantId })
    this.form.tenant = {
      code: data.tenant.code,
      name: data.tenant.name,
      type: data.tenant.type,
      startTime: data.tenant.startTime,
      days: data.tenant.days,
      endTime: data.tenant.endTime
    }
    this.form.user = {
      account: data.user.account,
      name: data.user.name
    }
    this.form.org = {
      code: data.org?.code,
      name: data.org?.name
    }
    this.showOrg = this.form.tenant.type !== 1
    this.form.roleList = data.roleList
    this.resourceData.dataList = data.menuResponseList
    this.changeData(this.resourceData.dataList)
  },
  methods: {
    // 菜单数据组装
    changeData(data) {
      data.forEach((item) => {
        // 如果是固定菜单则禁止操作
        if (item.isFix) {
          item.disable = true
          item.isChecked = true
        }
        // 按钮固定按钮则禁止操作
        if (item.buttonResponseList?.length > 0) {
          item.buttonResponseList.forEach(btnItem => {
            btnItem.disable = !item.isChecked
          })
        }
        // 子菜单递归处理
        if (item.childrenList?.length > 0) {
          this.changeStatus(item)
          this.changeData(item.childrenList)
        }
      })
    },
    // 菜单是否选中
    changeStatus(item) {
      this.$set(item, 'indeterminate', false)
      if (item.childrenList.every(subItem => { return subItem.isChecked === false })) {
        item.isChecked = false
      } else {
        item.isChecked = true
      }
      item.indeterminate = item.childrenList.some(subItem => subItem.isChecked) && !item.childrenList.every(subItem => subItem.isChecked)

      item.childrenList.forEach(childItem => {
        if (childItem.childrenList?.length > 0) {
          this.changeStatus(childItem)
        }
      })
    },
    // 返回
    backForward() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-box {
  background-color: #fff;
  padding: 10px 5px;
  .tenant-form {
    margin: 5px;
    padding: 20px;
    border: 1px solid #f6f6f6;
  }
  .tenant-info {
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
