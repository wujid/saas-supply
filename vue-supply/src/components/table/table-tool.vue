<template>
  <div class="searchTool-page">
    <el-tooltip
      class="item"
      effect="dark"
      content="显示/隐藏查询"
      placement="bottom"
      :hide-after="1000"
    >
      <div
        class="enlarge"
        @click="handleShowSearch"
        @mouseover="enlargeHover = true"
        @mouseout="enlargeHover = false"
      >
        <img
          v-show="!enlargeHover"
          src="@/assets/images/magnifier.png"
          alt=""
        >
        <img
          v-show="enlargeHover"
          src="@/assets/images/magnifier-hover.png"
          alt=""
        >
      </div>
    </el-tooltip>
    <el-tooltip
      class="item"
      effect="dark"
      content="列设置"
      placement="bottom"
      :hide-after="1000"
    >
      <div
        class="search"
        @click="show = !show"
        @mouseover="searchHover = true"
        @mouseout="searchHover = false"
      >
        <img v-show="!searchHover" src="@/assets/images/search.png" alt="">
        <img
          v-show="searchHover"
          src="@/assets/images/search-hover.png"
          alt=""
        >

        <div v-show="show" class="list">
          <div v-for="(item, index) in newColumns" :key="index" class="list-item">
            <el-checkbox v-model="item.show" @change="checkboxChange($event,item)">{{ item.title }}</el-checkbox>
          </div>
        </div>
      </div>
    </el-tooltip>
    <el-tooltip
      class="item"
      effect="dark"
      content="刷新"
      placement="bottom"
      :hide-after="1000"
    >
      <div
        class="refresh"
        @click="update"
        @mouseover="refreshHover = true"
        @mouseout="refreshHover = false"
      >
        <img v-show="!refreshHover" src="@/assets/images/refresh.png" alt="">
        <img
          v-show="refreshHover"
          src="@/assets/images/refresh-hover.png"
          alt=""
        >
      </div>
    </el-tooltip>
    <div v-show="show" class="mantle" @click="show = false" />
  </div>
</template>

<script>
export default {
  props: {
    columns: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      show: false,
      enlargeHover: false,
      searchHover: false,
      refreshHover: false,
      newColumns: this.columns
    }
  },
  methods: {
    handleShowSearch() {
      this.$emit('see')
    },
    update() {
      this.$emit('update')
    },
    checkboxChange(e, item) {
      this.$emit('checkboxChange', item.key, e)
    }
  }
}
</script>

<style lang='scss' scoped>
.searchTool-page {
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding-left: 20px;
  box-sizing: border-box;
  position: relative;
  &::before {
    content: "";
    width: 1px;
    height: 14px;
    background-color: #e9e9e9;
    position: absolute;
    left: 10px;
    top: 9px;
  }
}
.refresh {
  cursor: pointer;
  display: flex;
  align-items: center;
  img {
    width: 19px;
    height: 19px;
  }
}
.enlarge {
  cursor: pointer;
  margin-right: 12px;
  display: flex;
  align-items: center;
  img {
    width: 20px;
    height: 20px;
  }
}
.search {
  cursor: pointer;
  position: relative;
  margin-right: 12px;
  display: flex;
  align-items: center;
  img {
    width: 20px;
    height: 20px;
  }
  .list {
    max-height: 300px;
    overflow-y: auto;
    background-color: #fff;
    position: absolute;
    top: 44px;
    right: 0;
    z-index: 50;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
    border: 1px solid #ebeef5;
    border-radius: 4px;
    padding: 10px 0;
    box-sizing: border-box;
    .list-item {
      line-height: 36px;
      padding: 0 20px;
    }
  }
}
.mantle {
  position: fixed;
  width: 100vw;
  height: 100vh;
  left: 0;
  bottom: 0;
  z-index: 10;
  opacity: 0;
}
</style>
