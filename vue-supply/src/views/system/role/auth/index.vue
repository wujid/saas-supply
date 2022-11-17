<template>
  <div class="app-container">
    <div class="info-box p-16">
      <div class="auth-top">
        <div class="tit">当前配置角色：<span>{{ name }}</span></div>
        <div>
          <el-button type="primary" size="small" @click="saveResourceAuth">保存</el-button>
          <el-button plain size="small" @click="cancel">取消</el-button>
        </div>
      </div>
      <div class="auth-info-box">
        <auth-tree v-for="(item, index) in dataList" :key="index" :tree-data="item" @menuClick="menuClick" />
      </div>
    </div>
  </div>
</template>

<script>
import { getAuthByRoleId, setResourceAuth } from '@/api/system'
import store from '@/store'
import AuthTree from '@/components/auth/auth-tree'

export default {
  name: 'resource-auth',
  components: {
    AuthTree
  },
  data() {
    return {
      id: null,
      name: null,
      dataList: []
    }
  },
  async beforeMount() {
    if (this.$route.query.id && typeof this.$route.query.id !== 'undefined') {
      this.id = this.$route.query.id
    }
    const { data } = await getAuthByRoleId({ roleId: this.id })
    this.name = data.name
    this.dataList = data.menuResponseList
    this.changeData(this.dataList)
  },
  methods: {
    // 菜单数据组装
    changeData(data) {
      data.forEach((item, index) => {
        // 如果是固定菜单则禁止操作
        if (item.isFix) {
          item.disable = true
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
      this.changeData(this.dataList)
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
    async saveResourceAuth() {
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
      getAuthMenu(this.dataList)
      const obj = {
        id: this.id,
        resourceIdList: resourceIdList
      }
      await setResourceAuth(obj)
      await store.dispatch('user/getInfo')
      this.$message.success('操作成功！')
      this.cancel()
    },
    cancel() {
      this.$router.push({ path: '/role' })
    }
  }
}
</script>

<style lang="scss" scoped>
.info-box{
  min-height: calc(100vh - 82px);
  background: #fff;
}
.auth-top{
  display: flex;
  line-height: 32px;
  justify-content: space-between;
  margin-bottom: 16px;
  .tit{
    color: #666;
    span{
      color: #409EFF;
    }
  }
}
.auth-info-box{
  border: 1px solid #f0f2f5;
  border-bottom: 0;
  border-radius: 5px;
  position: relative;
}
</style>

