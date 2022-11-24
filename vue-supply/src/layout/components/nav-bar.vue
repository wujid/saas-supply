<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" />

    <div class="company-name">
      <span>欢迎你，{{ userInfo.name }}</span>
      <el-divider direction="vertical" />
      <el-dropdown @visible-change="visibleChange">
        <el-badge :value="msgInfo.count" :max="99" :hidden="parseInt(msgInfo.count) <= 0" style="cursor: pointer;">
          <span class="el-icon-bell" style="font-size: 16px;" />
        </el-badge>
        <el-dropdown-menu slot="dropdown" class="message-info">
          <el-tabs v-if="showMessageTabs" value="sysMessage">
            <el-tab-pane :label="`我的消息（${msgInfo.count}）`" name="sysMessage">
              <div class="message-info-box">
                <div v-if="msgInfo.count > 0">
                  <div class="message-content">
                    <div v-for="(item, index) in msgInfo.data" :key="index" @click="linkUrl(item.detailUrl)"><svg-icon icon-class="bell" />{{ item.title }}</div>
                  </div>
                  <el-button size="small" style="width: 100%;" @click="goMyMessage">查看更多</el-button>
                </div>
                <div v-else class="message-info-no">
                  <svg-icon icon-class="upset" /><span>暂无未读消息</span>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="imageUrl" class="user-avatar" alt="">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown" style="margin-top: -6px">
          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <router-link to="/user-info">
            <el-dropdown-item>
              个人信息
            </el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://github.com/PanJiaChen/vue-admin-template/">
            <el-dropdown-item>Github</el-dropdown-item>
          </a>
          <a target="_blank" href="https://panjiachen.github.io/vue-element-admin-site/#/">
            <el-dropdown-item>Docs</el-dropdown-item>
          </a>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapMutations, mapState } from 'vuex'
import Breadcrumb from '@/components/bread-crumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  data() {
    return {
      maleImg: require('@/assets/images/maleImg.png'),
      femaleImg: require('@/assets/images/femaleImg.png'),
      showMessageTabs: false
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user?.userInfo,
      tenantInfo: state => state.user?.tenantInfo,
      sidebar: state => state.app?.sidebar,
      msgInfo: state => state.user?.msgInfo
    }),
    // eslint-disable-next-line vue/return-in-computed-property
    imageUrl() {
      if (this.userInfo.avatarId) {
        return process.env.VUE_APP_BASE_API + '/file/attachment/downloadByParams?businessId=' + this.userInfo.avatarId
      } else {
        return null
      }
    }
  },
  created() {
    this.$store.dispatch('user/getMsgInfo')
  },
  methods: {
    ...mapMutations('user', ['SET_THEME']),
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      const tenantCode = this.tenantInfo.code
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?tenantCode=${tenantCode}&redirect=${this.$route.fullPath}`)
    },
    visibleChange(e) {
      this.showMessageTabs = !!e
    },
    linkUrl(url) {
      if (url && url.trim() !== '') {
        this.$router.push({ path: url })
      } else {
        this.$router.push({ path: '/message-content' })
      }
    },
    goMyMessage() {
      if (this.$route.path !== '/message-content') {
        this.$router.push({ path: '/message-content' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  .company-name{
    position: absolute;
    top: 50%;
    right: 100px;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    color: $color6Text;
    font-size: 12px;
    line-height: 32px;
    margin-top: 1px;
    .el-divider--vertical{
      margin: 0 12px;
    }
  }
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }
  .product-market{
    float: left;
    margin: 0 15px;
  }
  .breadcrumb-container {
    float: left;
  }
  .themePicker{
    position: absolute;
    top: 50%;
    right: 85px;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
  }
  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 40px;

      .avatar-wrapper {
        margin-top: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 35px;
          height: 35px;
          border-radius: 50%;
          border: 1px solid #f4f4f4;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -16px;
          top: 22px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
@import '@/styles/element-variables.scss';
.message-info{
  width: 300px !important;
  padding: 0;
  .el-tabs__nav-wrap{
    padding: 0 18px !important;
    .el-tabs__item{
      height: 40px;
      line-height: 35px;
    }
  }
  .message-info-box{
    padding: 0 20px 12px;
  }
  .message-content{
    margin-bottom: 10px;
    div{
      cursor: pointer;
      height: 30px;
      line-height: 30px;
      font-size: 12px;
      color: #666;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      &:hover{
        color: $--color-primary;
      }
      .svg-icon{
        margin-right: 6px;
        color: #999;
        font-size: 13px;
      }
    }
  }
  .message-info-no{
    text-align: center;
    line-height: 80px;
    font-size: 14px;
    color: #aaa;
    .svg-icon{
      margin: 0 6px 4px 0;
      color: #ccc;
      font-size: 22px;
      display: inline-block;
      vertical-align: middle;
    }
  }
  .el-button{
    color: #999 !important;
    &:hover, &:focus, &:active{
      color: $--color-primary !important;
    }
  }
}
.todo-link{
  &:hover{
    color: $--color-primary !important;
  }
}
</style>
