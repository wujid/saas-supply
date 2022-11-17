<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>编辑个人信息</span>
      </div>
      <el-form ref="userInfoForm" :model="form" :rules="rules" label-width="160px">
        <el-form-item label="头像：" prop="avatar">
          <single-upload v-model="form.attachmentId" :business-id="form.avatarId" :size="5" />
        </el-form-item>
        <el-form-item label="手机号码：" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请填写手机号码" />
        </el-form-item>
        <el-form-item label="姓名：" prop="name">
          <el-input v-model="form.name" placeholder="请填写姓名" />
        </el-form-item>
        <el-form-item label="性别：" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱：" prop="email">
          <el-input v-model="form.email" placeholder="请填写邮箱" />
        </el-form-item>
        <el-form-item label="登录账号：">{{ userInfo.account }}</el-form-item>
        <el-form-item label="登录密码：" prop="password">
          <el-input v-model="form.password" placeholder="请填写新登录密码" show-password />
          <div style="font-size: 12px; color: #999; margin: -4px 0 -15px 0;">提示：填写新登录密码会覆盖原登录密码，不填写则原密码保持不变</div>
        </el-form-item>
        <el-form-item label="账号角色：">{{ roleName }}</el-form-item>
        <el-form-item label="个人说明：">
          <el-input v-model="form.description" type="textarea" placeholder="请填写个人说明" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="showLoading" @click="onSubmit()">确认修改</el-button>
          <el-button @click="goBack()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { isMobile, email, pPattern3 } from '@/utils/validate-form'
import singleUpload from '@/components/single-upload'
import { updateUser } from '@/api/system'

export default {
  name: 'edit-user-info',
  components: {
    singleUpload
  },
  data() {
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
    const checkPassword = (rule, value, callback) => {
      if (value && value !== '') {
        if (!pPattern3.pattern.test(value)) {
          return callback(new Error(pPattern3.message))
        }
      }
      callback()
    }
    return {
      baseImgUrl: window.webConfig.gateway,
      maleImg: require('@/assets/images/maleImg.png'),
      femaleImg: require('@/assets/images/femaleImg.png'),
      userInfo: {},
      roleName: '',
      form: {
        id: '',
        name: '',
        account: '',
        avatarId: null,
        telephone: '',
        sex: 1,
        email: '',
        password: null,
        description: '',
        attachmentId: null
      },
      showLoading: false,
      rules: {
        telephone: [
          { required: true, message: '请填写手机号码' }, { validator: checkPhone }
        ],
        name: [
          { required: true, message: '请填写姓名' }
        ],
        sex: [
          { required: true, message: '请选择性别' }
        ],
        email: [
          { required: true, message: '请填写邮箱' }, { validator: checkEmail }
        ],
        password: [
          { validator: checkPassword }
        ]
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.userInfo = this.$store.state.user.userInfo
      const roleList = this.$store.state.user.roleList
      this.roleName = roleList.map(item => {
        return item.name
      }).join('、')
      this.form = {
        id: this.userInfo.id,
        name: this.userInfo.name,
        account: this.userInfo.account,
        avatarId: this.userInfo.avatarId,
        telephone: this.userInfo.telephone,
        sex: this.userInfo.sex,
        email: this.userInfo.email,
        description: this.userInfo.description,
        attachmentId: null
      }
    },
    onSubmit() {
      this.$refs.userInfoForm.validate(async(valid) => {
        if (valid) {
          try {
            this.showLoading = true
            await updateUser(this.form)
            this.$message.success('提交成功！')
            this.goBack()
          } finally {
            this.showLoading = false
          }
        }
      })
    },
    goBack() {
      this.$router.push({ path: '/user-info' })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-info-edit{
  .el-card{
    min-height: calc(100vh - 82px);
  }
  .el-form{
    margin: 30px 0 0 40px;
    .el-form-item{
      .el-input{
        width: 380px;
      }
    }
  }
}
</style>
