<template>
  <div class="auth-info">
    <div class="auth-info-item">
      <div class="auth-info-tit">
        <el-checkbox v-model="localTreeData.isChecked" :disabled="localTreeData.disable" :indeterminate="localTreeData.indeterminate" @change="menuClick(localTreeData)">{{ localTreeData.name }}</el-checkbox>
      </div>
      <div v-if="localTreeData.buttonResponseList && localTreeData.buttonResponseList.length > 0" class="auth-info-btns">
        <el-checkbox v-for="btnItem in localTreeData.buttonResponseList" :key="btnItem.id" v-model="btnItem.isChecked" :disabled="btnItem.disable">{{ btnItem.name }}</el-checkbox>
      </div>
    </div>
    <div v-if="localTreeData.childrenList && localTreeData.childrenList.length > 0" class="auth-info-two">
      <auth-tree v-for="(item, index) in localTreeData.childrenList" :key="index" :tree-data="item" class="auth-info-child" @menuClick="menuClick" />
    </div>
  </div>
</template>

<script>

export default {
  name: 'auth-tree',
  components: {

  },
  props: {
    treeData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      localTreeData: this.treeData
    }
  },
  watch: {
    treeData: {
      handler: function(val) {
        this.localTreeData = val
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.localTreeData = this.treeData
  },
  created() {

  },
  methods: {
    menuClick(item) {
      this.$emit('menuClick', item)
    }
  }
}
</script>

<style lang="scss" scoped>
  .auth-info-item{
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #f0f2f5;
    .auth-info-btns{
      margin-left: 40px;
    }
  }
  .auth-info-tit{
    line-height: 40px;
    width: 250px;
    padding: 0 16px;
  }
  .auth-info-btns{
    flex: 1;
    border-left: 1px solid #f0f2f5;
    padding: 0 16px;
    line-height: 40px;
  }
  .auth-info-two{
    .auth-info-tit{
      padding-left: 40px;
    }
  }
  .auth-info-child > .auth-info-two {
    .auth-info-tit{
      padding-left: 80px;
    }
    .auth-info-child > .auth-info-two {
      .auth-info-tit{
        padding-left: 120px;
      }
    }
  }
  ::v-deep .el-checkbox__input.is-checked+.el-checkbox__label{
    color: #666;
  }
</style>
