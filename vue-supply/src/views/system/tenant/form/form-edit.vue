<template>
  <div class="edit-box">
    <div class="tenant-form">
      <el-form ref="tenantForm" class="tenant-info" label-width="150px" :model="form" :rules="rules">
        <div class="base-info clearfix">
          <div class="title-bar">租户信息</div>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="租户名称：" prop="tenant.name">
                <el-input v-model="form.tenant.name" class="form-width" placeholder="请输入租户名称" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="租户编码：" prop="tenant.code">
                <el-input v-model="form.tenant.code" class="form-width" placeholder="请输入租户编码" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="租户类型：" prop="tenant.type">
                <el-select v-model="form.tenant.type" @change="tenantTypeChange">
                  <el-option :value="1" label="个人" />
                  <el-option :value="2" label="机构" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="开始时间：">
                <el-date-picker
                  v-model="form.tenant.startTime"
                  class="form-width"
                  type="date"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="授权天数：" prop="tenant.days">
                <el-input-number v-model="form.tenant.days" :precision="0" class="form-width" size="medium" placeholder="请输入授权天数" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="结束时间：">
                <el-date-picker
                  v-model="form.tenant.endTime"
                  class="form-width"
                  type="date"
                  disabled
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
                <el-input v-model="form.user.account" class="form-width" placeholder="请输入管理员账号" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="密码：" prop="user.password">
                <el-input v-model="form.user.password" class="form-width" placeholder="请输入管理员密码" />
                <div style="font-size: 12px; color: #999; margin: -4px 0 -15px 0;">提示：填写新登录密码会覆盖原登录密码，不填写则原密码保持不变</div>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="名称：" prop="user.name">
                <el-input v-model="form.user.name" class="form-width" placeholder="请输入管理员名称" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-if="showOrg" class="org-info clearfix">
          <div class="title-bar">组织机构信息</div>
          <el-row class="row-item">
            <el-col :span="8">
              <el-form-item label="机构编码：" prop="org.code">
                <el-input v-model="form.org.code" class="form-width" placeholder="请输入机构名称" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="机构名称：" prop="org.name">
                <el-input v-model="form.org.name" class="form-width" placeholder="请输入机构名称" />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div class="role-info clearfix">
          <div class="title-bar">角色信息</div>
          <div class="tips">
            <el-button type="primary" size="small" @click="addRole">添 加</el-button>
          </div>
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
            <template v-slot:code="scope">
              <el-input v-model="scope.row.code" class="form-width" placeholder="请输入角色编码" />
            </template>
            <template v-slot:name="scope">
              <el-input v-model="scope.row.name" class="form-width" placeholder="请输入角色名称" />
            </template>
            <template v-slot:description="scope">
              <el-input v-model="scope.row.description" class="form-width" placeholder="请输入角色描述" />
            </template>
            <template v-slot:operation="scope">
              <el-link :disabled="scope.row.isSystem" type="danger" :underline="false" @click="handleRoleDel(scope.scope.$index)">移除</el-link>
            </template>
          </MyTable>
        </div>
        <div class="resource-info clearfix">
          <div class="title-bar">资源信息</div>
          <auth-tree v-for="(item, index) in resourceData.dataList" :key="index" :tree-data="item" @menuClick="menuClick" />
        </div>
      </el-form>
    </div>
    <div class="btn_box">
      <el-button type="primary" :loading="formLoading" @click="addTenantInfo">提交</el-button>
      <el-button @click="backForward">取消</el-button>
    </div>
  </div>
</template>

<script>
import { pPattern3 } from '@/utils/validate-form'
import { getTenantDetail, updateTenant } from '@/api/system'
import AuthTree from '@/components/auth/auth-tree'

export default {
  name: 'tenant-edit',
  components: {
    AuthTree
  },
  data() {
    const checkPassword = (rule, value, callback) => {
      if (value && value !== '') {
        if (!pPattern3.pattern.test(value)) {
          return callback(new Error(pPattern3.message))
        }
      }
      callback()
    }
    return {
      form: {
        tenant: {
          id: null,
          code: null,
          name: null,
          type: null,
          startTime: null,
          days: null,
          endTime: null
        },
        user: {
          id: null,
          account: null,
          password: null,
          name: null
        },
        org: {
          id: null,
          code: null,
          name: null
        },
        resourceIdList: [],
        roleList: []
      },
      rules: {
        'tenant.name': [{ required: true, message: '请输入租户名称' }],
        'tenant.code': [{ required: true, message: '请输入租户编码' }],
        'tenant.type': [{ required: true, message: '请选择租户类型' }],
        'tenant.days': [{ required: true, message: '请输入授权天数' }],
        'user.account': [{ required: true, message: '请输入管理员账号' }],
        'user.password': [{ validator: checkPassword }],
        'user.name': [{ required: true, message: '请输入管理员名称' }],
        'org.code': [{ required: true, message: '请输入机构编码' }],
        'org.name': [{ required: true, message: '请输入机构名称' }]
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
        },
        {
          title: '操作',
          key: 'operation',
          rowSlot: true
        }
      ]
    }
  },
  async created() {
    this.tenantId = this.$route.query.id
    const { data } = await getTenantDetail({ tenantId: this.tenantId })
    this.form.tenant = {
      id: data.tenant.id,
      code: data.tenant.code,
      name: data.tenant.name,
      type: data.tenant.type,
      startTime: data.tenant.startTime,
      days: data.tenant.days,
      endTime: data.tenant.endTime
    }
    this.form.user = {
      id: data.user.id,
      account: data.user.account,
      name: data.user.name
    }
    this.form.org = {
      id: data.org?.id,
      code: data.org?.code,
      name: data.org?.name
    }
    this.showOrg = this.form.tenant.type !== 1
    this.form.roleList = data.roleList
    this.resourceData.dataList = data.menuResponseList
    this.changeData(this.resourceData.dataList)
  },
  methods: {
    // 租户类型改变时
    tenantTypeChange() {
      this.showOrg = this.form.tenant.type !== 1
      this.form.org.name = null
    },
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
    // 菜单勾选/反选
    menuClick(item) {
      let isTrue = false
      if (item.isChecked) {
        isTrue = true
      }
      this.btnChange(item, isTrue)
      this.childMenuChange(item, isTrue)

      if (item.childrenList?.length > 0) {
        item.childrenList.forEach(subItem => {
          this.menuClick(subItem)
        })
      }
      this.changeData(this.resourceData.dataList)
    },
    btnChange(item, value) {
      if (item.buttonResponseList?.length > 0) {
        item.buttonResponseList.forEach(btnItem => {
          btnItem.isChecked = value
          btnItem.disable = !value
        })
      }
    },
    childMenuChange(item, value) {
      if (item.childrenList?.length > 0) {
        item.childrenList.forEach(subItem => {
          subItem.isChecked = value
          this.btnChange(subItem, value)
          this.childMenuChange(subItem, value)
        })
      }
    },
    // 获取选中的资源ID集
    getResourceIdList() {
      const resourceIdList = []
      function getAuthMenu(data) {
        data.forEach(item => {
          if (item.buttonResponseList?.length > 0) {
            item.buttonResponseList.forEach(btnItem => {
              if (btnItem.isChecked) {
                resourceIdList.push(btnItem.id)
              }
            })
          }
          if (item.isChecked) {
            resourceIdList.push(item.id)
          }
          if (item.childrenList?.length > 0) {
            getAuthMenu(item.childrenList)
          }
        })
      }
      getAuthMenu(this.resourceData.dataList)
      return resourceIdList
    },
    addTenantInfo() {
      this.$refs.tenantForm.validate(async(valid) => {
        if (valid && this.validateRoles()) {
          this.formLoading = true
          try {
            this.form.resourceIdList = this.getResourceIdList()
            await updateTenant(this.form)
            this.$message.success('提交成功！')
            this.backForward()
          } catch (e) {
            this.$message.error('提交失败！')
          } finally {
            this.formLoading = false
          }
        }
      })
    },
    // 返回
    backForward() {
      this.$router.back()
    },
    // 删除角色
    handleRoleDel(index) {
      this.form.roleList.splice(index, 1)
    },
    // 添加角色
    addRole() {
      const role = { id: null, code: '', name: '', description: '', isSystem: false, status: 0 }
      this.form.roleList.push(role)
    },
    validateRoles() {
      let isTrue = true
      for (let i = 0; i < this.form.roleList.length; i++) {
        const no = i + 1
        const item = this.form.roleList[i]
        if (!item.code) {
          this.$message.warning('角色信息第' + no + '行请填写角色编号')
          isTrue = false
          break
        }
        if (!item.name) {
          this.$message.warning('角色信息第' + no + '行请填写角色名称')
          isTrue = false
          break
        }
        for (let j = i + 1; j < this.form.roleList.length; j++) {
          if (item.code === this.form.roleList[j].code) {
            this.$message.warning('角色信息第' + no + '行存在角色编号重复')
            isTrue = false
            break
          }
        }
      }
      return isTrue
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
