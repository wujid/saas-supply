<template>
  <div class="user-info p-16">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="display: inline-block; margin-top: 6px;">个人信息</span>
        <el-button style="float: right;" size="mini" type="primary" @click="edit()">编辑</el-button>
      </div>
      <el-form label-width="160px">
        <el-form-item label="头像：">
          <el-image
            style="width: 80px; height: 80px; border-radius: 50%;"
            :src="avatar"
            fit="fill"
          />
        </el-form-item>
        <el-form-item label="登录账号：">{{ userInfo.account }}</el-form-item>
        <el-form-item label="手机号码：">{{ userInfo.telephone }}</el-form-item>
        <el-form-item label="姓名：">{{ userInfo.name }}</el-form-item>
        <el-form-item label="性别：">{{ userInfo.sex === 1 ? '男' : '女' }}</el-form-item>
        <el-form-item label="邮箱：">{{ userInfo.email }}</el-form-item>
        <el-form-item label="是否管理员：">{{ userInfo.isManage ? '是' : '否' }}</el-form-item>
        <el-form-item label="账号角色：">{{ roleName }}</el-form-item>
        <el-form-item label="个人说明：">{{ userInfo.description }}</el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getInfo } from '@/api/system'

export default {
  name: 'user-info',
  data() {
    return {
      maleImg: require('@/assets/images/maleImg.png'),
      femaleImg: require('@/assets/images/femaleImg.png'),
      userInfo: {},
      roleName: '',
      avatar: null
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    async getUserInfo() {
      // this.userInfo = this.$store.state.user.userInfo
      // const roleList = this.$store.state.user.roleList
      const { data } = await getInfo()
      this.userInfo = data.userResponse
      const roleList = data.roleResponseList
      this.roleName = roleList.map(item => {
        return item.name
      }).join('、')
      this.getAvatar()
    },
    getAvatar() {
      this.avatar = require('@/assets/images/maleImg.png')
      if (!this.userInfo.avatarId || this.userInfo.avatarId === null) {
        if (this.userInfo.sex === 2) {
          this.avatar = require('@/assets/images/femaleImg.png')
        }
      } else {
        this.avatar = process.env.VUE_APP_BASE_API + '/file/attachment/downloadByParams?businessId=' + this.userInfo.avatarId
      }
    },
    edit() {
      if (process.env.NODE_ENV === 'production' && this.userInfo.isManage) {
        this.$message.success('演示环境管理员编辑功能禁用！')
        return
      }
      this.$router.push({ path: '/user-info-edit' })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info{
  .el-card{
    min-height: calc(100vh - 82px);
  }
  .el-form{
    margin: 30px 0 0 40px;
    .el-form-item{
      margin-bottom: 16px;
    }
  }
}
</style>

