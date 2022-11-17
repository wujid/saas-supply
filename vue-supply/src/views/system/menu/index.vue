<template>
  <div class="menu-manage p-16">
    <div class="menu-manage-left">
      <div class="tit pl-16 pr-16">菜单列表</div>
      <div class="text-right mt-16 mr-16">
        <el-button type="primary" size="mini" @click="addMenu(null)">创建根菜单</el-button>
      </div>
      <el-tree
        :data="menuData"
        node-key="id"
        :expand-on-click-node="false"
        :default-expanded-keys="expandedIds"
        :props="{ label: 'name', children: 'childrenList' }"
        class="p-16"
      >
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span :class="{currentStyle: currentMenu.id === data.id}" @click="setMenuInfo(data)">{{ data.name }}</span>
          <span class="ml-16">
            <el-button v-if="data.parentId === '0'" type="text" class="ml-16" size="mini" @click="addMenu(data)">创建子菜单</el-button>
            <el-button type="text" class="ml-16" size="mini" :disabled="disabledInProd" @click="editMenu(data)">编辑</el-button>
            <el-button
              v-if="!data.isFix"
              type="text"
              class="ml-16"
              size="mini"
              :disabled="disabledInProd"
              @click="delMenu(data.id)"
            >删除</el-button>
          </span>
        </span>
      </el-tree>
    </div>
    <div class="menu-manage-right">
      <div class="tit pl-16 pr-16">菜单详情</div>
      <div class="p-16 menu-detail">
        <el-form label-width="160px">
          <el-form-item label="菜单名称：">{{ currentMenu.data.name }}</el-form-item>
          <el-form-item label="菜单编码：">{{ currentMenu.data.code }}</el-form-item>
          <el-form-item label="菜单路径：">{{ currentMenu.data.path }}</el-form-item>
          <el-form-item label="菜单图标：">
            <i :class="currentMenu.data.icon + ' menu-icon'" />
          </el-form-item>
          <el-form-item label="菜单排序：">{{ currentMenu.data.sort }}</el-form-item>
          <el-form-item label="菜单状态：">{{ currentMenu.data.businessStatus === 1 ? '正常' : '冻结' }}</el-form-item>
          <el-form-item label="按钮：">
            <div>
              <el-button type="primary" size="mini" @click="addButton">创建</el-button>
            </div>
            <my-table
              ref="buttonTable"
              stripe
              border
              auto-height
              :pagination="false"
              :data="currentMenu.data.buttonResponseList"
              :columns="buttonForm.columns"
              class="m-0 p-0"
            >
              <template v-slot:operation="scope">
                <el-link type="primary" :underline="false" :disabled="disabledInProd" @click="editButton(scope.row)">编辑</el-link>
                <el-link type="primary" :underline="false" :disabled="disabledInProd" @click="delButton(scope.row.id)">删除</el-link>
              </template>
            </my-table>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <el-dialog class="formDialog" :title="menuForm.formTitle" :visible.sync="menuForm.formVisible" width="600px">
      <el-form ref="menuForm" :model="menuForm.form" :rules="menuForm.formRules" label-width="120px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuForm.form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单编码" prop="code">
          <el-input v-model="menuForm.form.code" placeholder="请输入菜单编码" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="menuForm.form.path" placeholder="请输入菜单路径" />
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-select v-model="menuForm.form.icon" popper-class="icon-select" placeholder="请选择菜单图标" clearable>
            <el-option v-for="(icon, index) in iconList" :key="index" :value="icon">
              <svg-icon :icon-class="icon" />
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="menuForm.form.sort" type="number" placeholder="请输入菜单排序值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="menuForm.formLoading" @click="saveMenu">确定提交</el-button>
        <el-button @click="menuForm.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog class="formDialog" :title="buttonForm.formTitle" :visible.sync="buttonForm.formVisible" width="600px">
      <el-form ref="buttonForm" :model="buttonForm.form" :rules="buttonForm.formRules" label-width="120px">
        <el-form-item label="按钮名称" prop="name">
          <el-input v-model="buttonForm.form.name" placeholder="请输入按钮名称" />
        </el-form-item>
        <el-form-item label="按钮编码" prop="code">
          <el-input v-model="buttonForm.form.code" placeholder="请输入按钮编码" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="buttonForm.form.sort" type="number" placeholder="请输入按钮排序值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="buttonForm.formLoading" @click="saveButton">确定提交</el-button>
        <el-button @click="buttonForm.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addResource, delResource, getResourceTree, updateResource } from '@/api/system'
import _ from 'lodash'

export default {
  name: 'menu-manage',
  data() {
    return {
      menuData: [],
      iconList: [],
      expandedIds: [],
      currentMenu: {
        id: null,
        data: {}
      },
      menuForm: {
        formTitle: '',
        formVisible: false,
        formLoading: false,
        form: {
          id: null,
          parentId: null,
          code: null,
          name: null,
          type: null,
          icon: null,
          path: null,
          sort: null
        },
        formRules: {
          name: { required: true, message: '请输入菜单名称' },
          code: { required: true, message: '请输入菜单编码' },
          path: {},
          sort: { required: true, message: '请输入菜单排序' }
        }
      },
      buttonForm: {
        columns: [
          { title: '按钮名称', key: 'name' },
          { title: '按钮编码', key: 'code' },
          { title: '按钮排序', key: 'sort' },
          { title: '操作', key: 'operation', rowSlot: true }
        ],
        formTitle: '',
        formVisible: false,
        formLoading: false,
        form: {
          id: null,
          parentId: null,
          code: null,
          name: null,
          type: null,
          icon: null,
          path: null,
          sort: null
        },
        formRules: {
          name: { required: true, message: '请输入按钮名称' },
          code: { required: true, message: '请输入按钮编码' },
          sort: { required: true, message: '请输入按钮排序' }
        }
      }
    }
  },
  computed: {
    disabledInProd() {
      return process.env.NODE_ENV === 'production'
    }
  },
  created() {
    if (process.env.NODE_ENV === 'production') {
      this.$message.success('演示环境部分功能禁用！')
    }
    this.getMenuData()
    this.iconList = JSON.parse(localStorage.getItem('IconList'))
  },
  methods: {
    // 获取资源菜单树形列表
    async getMenuData() {
      const { data } = await getResourceTree()
      this.menuData = data
      this.setMenuInfo(this.getCurrentMenuInfo(data))
    },
    getCurrentMenuInfo(data) {
      const list = _.cloneDeep(data)
      let menuInfo = {}
      for (let i = 0; i < list.length; i++) {
        if (list[i].id === this.currentMenu.id) {
          menuInfo = list[i]
          return menuInfo
        } else {
          if (list[i].childrenList?.length > 0) {
            menuInfo = this.getCurrentMenuInfo(list[i].childrenList)
            if (menuInfo && menuInfo !== {}) {
              return menuInfo
            }
          }
        }
      }
    },
    // 设置当前选中的菜单
    setMenuInfo(data) {
      if (data) {
        this.currentMenu.id = data.id
        this.currentMenu.data = data
      }
    },
    // 创建子菜单
    addMenu(data) {
      this.menuForm.formTitle = '创建菜单'
      this.menuForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.menuForm.resetFields()
        if (data) {
          this.menuForm.form.parentId = data.id
          this.menuForm.formRules.path = { required: true, message: '请输入菜单路径' }
        } else {
          this.menuForm.form.parentId = 0
          this.menuForm.form.id = null
          this.menuForm.formRules.path = {}
        }
        this.menuForm.form.type = 1
      })
    },
    // 编辑菜单
    editMenu(data) {
      this.menuForm.formTitle = '编辑菜单'
      this.menuForm.formVisible = true
      if (data.parentId !== '0') {
        this.menuForm.formRules.path = { required: true, message: '请输入菜单路径' }
      } else {
        this.menuForm.formRules.path = {}
      }
      this.$nextTick(() => {
        this.$refs.menuForm.resetFields()
        this.menuForm.form = {
          id: data.id,
          parentId: data.parentId,
          code: data.code,
          name: data.name,
          type: data.type,
          icon: data.icon,
          path: data.path,
          sort: data.sort
        }
      })
    },
    // 保存/修改菜单
    saveMenu() {
      this.$refs.menuForm.validate(async(valid) => {
        if (valid) {
          this.menuForm.formLoading = true
          try {
            if (this.menuForm.form.id === null) {
              await addResource(this.menuForm.form)
            } else {
              await updateResource(this.menuForm.form)
            }
            this.$message.success('提交成功！')
            this.menuForm.formVisible = false
            await this.getMenuData()
          } finally {
            this.menuForm.formLoading = false
          }
        }
      })
    },
    // 删除菜单
    delMenu(id) {
      this.$confirm('确认要删除该菜单吗？', '提示', {
        type: 'error'
      }).then(async _ => {
        await delResource({ resourceId: id })
        await this.getMenuData()
        this.$message.success('删除成功！')
      }).catch(_ => {})
    },
    // 添加按钮
    addButton() {
      this.buttonForm.formTitle = '创建按钮'
      this.buttonForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.buttonForm.resetFields()
        this.buttonForm.form.parentId = this.currentMenu.id
        this.buttonForm.form.id = null
        this.buttonForm.form.type = 2
      })
    },
    // 修改按钮
    editButton(row) {
      this.buttonForm.formTitle = '修改按钮'
      this.buttonForm.formVisible = true
      this.$nextTick(() => {
        this.$refs.buttonForm.resetFields()
        this.buttonForm.form = {
          parentId: row.parentId,
          id: row.id,
          code: row.code,
          name: row.name,
          type: row.type,
          icon: row.icon,
          path: row.path,
          sort: row.sort
        }
      })
    },
    // 保存/修改按钮
    saveButton() {
      this.$refs.buttonForm.validate(async(valid) => {
        if (valid) {
          this.buttonForm.formLoading = true
          try {
            if (this.buttonForm.form.id === null) {
              await addResource(this.buttonForm.form)
            } else {
              await updateResource(this.buttonForm.form)
            }
            await this.getMenuData()
            this.$message.success('提交成功！')
            this.buttonForm.formVisible = false
          } finally {
            this.buttonForm.formLoading = false
          }
        }
      })
    },
    // 删除按钮
    delButton(id) {
      this.$confirm('确认要删除该按钮吗？', '提示', {
        type: 'error'
      }).then(async _ => {
        await delResource({ resourceId: id })
        await this.getMenuData()
        this.$message.success('删除成功！')
      }).catch(_ => {})
    }
  }
}
</script>

<style lang="scss" scoped>
@mixin scrollbar {
  &::-webkit-scrollbar{
    width: 10px;
    height: 10px;
  }
  &::-webkit-scrollbar-thumb {
    background: #d1d1d1;
  }
  &::-webkit-scrollbar-track {
    background: #f2f2f2;
  }
}

.menu-manage {
  display: flex;
  .menu-manage-left{
    height: calc(100vh - 82px);
    background: #fff;
    width: 350px;
    float: left;
    margin-right: 16px;
    overflow: auto;
    @include scrollbar;
    ::v-deep .el-tree-node {
      min-width: 100% !important;
      display: block !important;
      float: left;
    }
    ::v-deep .currentStyle{
      font-weight: bold;
      color: #333;
    }
    .custom-tree-node span {
      font-size: 14px;
    }
  }
  .menu-manage-right{
    flex: 1;
    height: calc(100vh - 82px);
    background: #fff;
    border-left: 1px solid #eee;
    overflow: auto;
    @include scrollbar;
    .el-link {
      margin-right: 10px;
    };
  }
  .tit{
    border-bottom: 1px solid #eee;
    line-height: 40px;
    font-size: 15px;
    color: #333;
  }
  .menu-detail{
    .el-form-item{
      margin-bottom: 12px;
    }
  }
  ::v-deep .el-table__cell{
    padding: 0 0 !important;
  }
  ::v-deep .el-table__row{
    .el-table__cell{
      padding: 6px 0 !important;
    }
  }
  .formDialog{
    .el-input,.el-select{
      width: 90%;
    }
  }
  .menu-icon{
    font-size: 20px;
    position: relative;
    top: 2px;
  }
}
</style>

<style lang="scss">
.icon-select{
  .el-scrollbar{
    width: 430px !important;
  }
  .el-select-dropdown__item{
    width: 60px !important;
    font-size: 20px;
    float: left;
  }
}

</style>
