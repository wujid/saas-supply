<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
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
        <el-col :span="8">
          <el-image :src="captchaUrl" @click="generateCaptcha" />
        </el-col>
      </el-row>
      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">Login</el-button>
    </el-form>
  </div>
</template>

<script>

import { generateCaptcha } from '@/api/system'
import { nanoid } from 'nanoid'
import { getTenantCode } from '@/utils/auth'

export default {
  name: 'login',
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
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
