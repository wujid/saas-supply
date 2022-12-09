<template>
  <div class="login-container">
    <div class="login-wapper">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
        <div class="login-logo">
          <h3 class="title">Login Form</h3>
        </div>
        <el-form-item v-if="showTenant" prop="tenantCode">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="tenantCode"
            v-model="loginForm.tenantCode"
            placeholder="唯一码"
            name="tenantCode"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="userName">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="loginForm.userName"
            placeholder="账号"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="off"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="off"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
        <el-row>
          <el-col :span="16">
            <el-form-item prop="captcha">
              <el-input ref="captcha" v-model="loginForm.captcha" placeholder="验证码" name="captcha" type="text" tabindex="1" @keyup.enter.native="handleLogin" />
            </el-form-item>
          </el-col>
          <el-col :span="8" class="captcha">
            <el-image :src="captchaUrl" @click="generateCaptcha" />
          </el-col>
        </el-row>
        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登 录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>

import { generateCaptcha } from '@/api/system'
import { nanoid } from 'nanoid'
import { getTenantCode } from '@/utils/auth'

export default {
  name: 'login',
  components: {
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码！'))
        return
      }
      if (value.length < 6) {
        callback(new Error('密码不能少于6个字符！'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        userName: 'admin',
        password: 'qq123456',
        tenantCode: 'JXSM',
        key: null,
        captcha: null
      },
      loginRules: {
        userName: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        tenantCode: [{ required: true, message: '请输入编码' }],
        captcha: [{ required: true, message: '请输入验证码' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: '',
      showTenant: true,
      captchaUrl: null
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        // 判断路由是否存在租户编码,如果存在则使用该编码,否则看缓存中是否存在
        this.redirect = route.query && route.query.redirect
        if (route.query && route.query.tenantCode) {
          this.loginForm.tenantCode = route.query.tenantCode
          this.showTenant = false
          return
        }
        const tenantCode = getTenantCode()
        if (tenantCode) {
          this.loginForm.tenantCode = tenantCode
          this.showTenant = false
          return
        }
        this.loginForm.tenantCode = 'JXSM'
        this.loginForm.userName = 'admin'
        this.loginForm.password = 'qq123456'
        this.showTenant = true
      },
      immediate: true
    }
  },
  async created() {
    await this.generateCaptcha()
    if (this.$websocket.getSocket()) {
      this.$websocket.close()
    }
  },
  methods: {
    async generateCaptcha() {
      this.loginForm.key = nanoid(5)
      const imageData = await generateCaptcha({ key: this.loginForm.key })
      const blob = new Blob([imageData])
      this.captchaUrl = window.URL.createObjectURL(blob)
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.generateCaptcha()
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-wapper .el-input input {
    color: $cursor;
  }
}

.login-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
  background: url('~@/assets/images/back-ground.jpg') center center no-repeat;
  .login-wapper{
    .login-form {
      position: fixed;
      width: 407px;
      height: 567px;
      padding: 60px;
      top: 50%;
      left: 50%;
      margin: -283px 0 0 193px;
      background: url('~@/assets/images/login-bg2.png') center center no-repeat;
      overflow: hidden;
    }
    .login-logo{
      text-align: center;
      margin: 15px 0;
    }
    .title {
      font-size: 16px;
      color: #F1F5F8;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: normal;
    }
    .el-form-item {
      background: rgba(0, 0, 0, 0.1);
      border-radius: 8px;
      color: #454545;
      .el-form-item__error{
        color: #f54343;
      }
    }
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
      input {
        background: transparent!important;
        border: 0;
        -webkit-appearance: none;
        border-radius: 0;
        padding: 12px 5px 12px 15px;
        color: #fff;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0 1000px #2d3a4b inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: #fff;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: #fff;
      cursor: pointer;
      user-select: none;
    }
    .forget-btn{
      text-align: right;
      margin: -20px 0 15px 0;
      .el-button{
        color: #C3E2FF;
      }
    }
    .login-btn{
      background: #1890FF;
      border-color: #1890FF;
      box-shadow: 0 0 10px rgba(24,144,255,.8);
      border-radius: 9999px;
      height: 45px;
      margin-top: 10px;
      &:hover{
        border-color: #1890FF;
        opacity: .9;
      }
    }
  }
  .formDialog{
    .el-input{
      width: 90%;
    }
  }
  .captcha{
    .el-image {
      position: relative;
      display: inline-block;
      overflow: hidden;
      height: 47px;
      margin-left: 5px;
      width: 94px;
      border-radius: 10px;
    }
  }
  .footer{
    background: rgba(0, 0, 0, 0.36);
    padding: 4px 0 1px;
    position: absolute;
    width: 100%;
    bottom: 0;
    left: 0;
    text-align: center;
    font-size: 12px;
    color: #fff;
    opacity: .8;
    p{
      margin: 10px 0;
    }
    a{
      color: #fff;
      display: inline-block;
      margin: 0 10px;
      &:hover{
        text-decoration: underline;
      }
    }
    span{
      display: inline-block;
      margin: 0 10px;
    }
  }
}
</style>
