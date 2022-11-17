<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="模板编码" prop="code">
        <el-input v-model.trim="searchForm.code" clearable placeholder="请输入模板编码" />
      </el-form-item>
      <el-form-item label="消息类型" prop="msgType">
        <el-select v-model="searchForm.businessStatus" placeholder="全部">
          <el-option
            v-for="item in msgTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="" class="btn-center">
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button plain @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <my-table
      ref="myTable"
      stripe
      border
      toolbar
      :columns="columns"
      :search-form="searchForm"
      @pageRequest="pageRequest"
      @see="showSearch = !showSearch"
    >
      <template slot="toolSlot">
        <div style="width:100%;">
          <el-button v-auth="'message_template_create'" type="primary" size="mini" @click="addTemplate">新建</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'message_template_view'" type="primary" :underline="false" @click="editTemplate(scope.row, 'view')">查看</el-link>
        <el-link v-auth="'message_template_edit'" type="primary" :underline="false" @click="editTemplate(scope.row, 'edit')">编辑</el-link>
        <el-link v-auth="'message_template_del'" type="primary" :underline="false" @click="delTemplate(scope.row.id)">删除</el-link>
        <el-link v-auth="'message_template_test'" type="primary" :underline="false" @click="messageTest(scope.row)">测试</el-link>
      </template>
    </my-table>
    <el-dialog :title="detail.formTitle" :visible.sync="detail.formVisible" width="800px">
      <el-form ref="templateForm" :model="detail.form" :rules="detail.formRules">
        <el-form-item label="模板编码" label-width="150px" prop="code">
          <el-input v-model="detail.form.code" placeholder="请输入模板编码" :disabled="detail.isDisabled || detail.operationType === 'edit'" />
        </el-form-item>
        <el-form-item label="模板标题" label-width="150px" prop="title">
          <el-input v-model="detail.form.title" placeholder="请输入模板标题" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item label="详情URL" label-width="150px" prop="detailUrl">
          <el-input v-model="detail.form.detailUrl" placeholder="请输入详情URL" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item label="消息类型" label-width="150px" prop="msgType">
          <el-select v-model="detail.form.msgType" placeholder="请选择消息类型" :disabled="detail.isDisabled" @change="msgTypeChange">
            <el-option
              v-for="item in msgTypes"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-show="detail.showPublishMsgNotifyType" label="消息公告至" label-width="150px" prop="publishMsgNotifyType">
          <el-select v-model="detail.form.publishMsgNotifyType" placeholder="请选择" :disabled="detail.isDisabled" @change="publishMsgTypeChange">
            <el-option
              v-for="item in publishMsgNotifyTypes"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-show="detail.showTenant" label="请选择租户" label-width="150px" prop="tenantIds">
          <el-select v-model="detail.form.tenantIds" multiple clearable placeholder="请选择租户(可多选)" :disabled="detail.isDisabled">
            <el-option
              v-for="item in tenants"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="消息通知类型" label-width="150px" prop="notifyTypes">
          <el-checkbox-group v-model="detail.form.notifyTypes" :disabled="detail.isDisabled" @change="handleNotifyType">
            <el-checkbox v-for="(item, index) in notifyTypes" :key="index" :label="item.key">{{ item.value }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item v-show="detail.showNotifyTemplateSystem" label="系统消息通知内容" label-width="150px" prop="notifyTemplateSystem">
          <EditorElem :catch-data="systemCatchData" :content="detail.form.notifyTemplateSystem" style="width: 90%;" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item v-show="detail.showNotifyTemplateEmail" label="邮件消息通知内容" label-width="150px" prop="notifyTemplateEmail">
          <EditorElem :catch-data="emailCatchData" :content="detail.form.notifyTemplateEmail" style="width: 90%;" :disabled="detail.isDisabled" />
        </el-form-item>
        <el-form-item v-show="detail.showNotifyTemplateSms" label="短信消息通知内容" label-width="150px" prop="notifyTemplateEmail">
          <el-input v-model="detail.form.notifyTemplateSms" type="textarea" placeholder="请输入短信消息通知内容" :disabled="detail.isDisabled" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="detail.operationType !== 'view'" type="primary" :loading="detail.formLoading" @click="saveTemplate">提 交</el-button>
        <el-button @click="detail.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="testMessage.formTitle" :visible.sync="testMessage.formVisible" width="800px">
      <el-form ref="testMessageForm" :model="testMessage.form">
        <el-form-item label="数据" label-width="150px" prop="paramsMap">
          <b-code-editor
            ref="editor"
            v-model="testMessage.form.jsonStr"
            :auto-format="true"
            :smart-indent="true"
            theme="dracula"
            :indent-unit="4"
            :line-wrap="true"
          />
          <div style="font-size: 12px; color: #999; margin: -4px 0 -15px 0;">
            提示：通用参数key值为0，指定接收人请使用对应人员ID,如需格式化请<el-link style="font-size: 12px;" :underline="true" @click="$refs['editor'].formatCode()">点击</el-link>
          </div>
        </el-form-item>
        <el-form-item v-show="testMessage.showUser" label="接收人" label-width="150px" prop="receiverUserIds">
          <el-select v-model="testMessage.form.receiverUserIds" multiple clearable placeholder="请选择接收人(可多选)">
            <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.name + '-' + item.id"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="testMessage.formLoading" @click="sendMessageTo">提 交</el-button>
        <el-button @click="testMessage.formVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addTemplate,
  delTemplate,
  getTemplateInfoById,
  getTemplatePageByParams,
  sendMessage,
  updateTemplate
} from '@/api/message'
import EditorElem from '@/components/editor-item'
import { getTenantList, getUserListByParams } from '@/api/system'
const msgTypes = [
  { key: 1, value: '个人' },
  { key: 2, value: '公告' }
]
const publishMsgNotifyTypes = [
  { key: 1, value: '系统所有成员' },
  { key: 2, value: '租户' }
]
const notifyTypes = [
  { key: 1, value: '系统' },
  { key: 2, value: '邮件' },
  { key: 3, value: '短信' }
]
export default {
  name: 'message-template',
  components: {
    EditorElem
  },
  data() {
    const validatePublishMsgNotifyType = (rule, value, callback) => {
      if (!value && this.detail.form.msgType === 2) {
        callback(new Error('请选择公告消息公告范围！'))
        return
      }
      callback()
    }
    const validateTenants = (rule, value, callback) => {
      if (this.detail.form.publishMsgNotifyType === 2 && (!value || value.length === 0)) {
        callback(new Error('请选择租户！'))
        return
      }
      callback()
    }
    const validateNotifyTemplateSystem = (rule, value, callback) => {
      const isTrue = this.detail.form.notifyTypes.some(item => {
        return item === 1
      })
      if (isTrue && !value) {
        callback(new Error('请输入系统消息通知内容！'))
        return
      }
      callback()
    }
    const validateNotifyTemplateEmail = (rule, value, callback) => {
      const isTrue = this.detail.form.notifyTypes.some(item => {
        return item === 2
      })
      if (isTrue && !value) {
        callback(new Error('请输入邮件消息通知内容！'))
        return
      }
      callback()
    }
    const validateNotifyTemplateSms = (rule, value, callback) => {
      const isTrue = this.detail.form.notifyTypes.some(item => {
        return item === 3
      })
      if (isTrue && !value) {
        callback(new Error('请输入短信消息通知内容！'))
        return
      }
      callback()
    }
    const validateReceiverUser = (rule, value, callback) => {
      if (this.testMessage.showUser && !value) {
        callback(new Error('请选择消息接收人！'))
        return
      }
      callback()
    }
    return {
      msgTypes,
      publishMsgNotifyTypes,
      notifyTypes,
      tenants: [],
      users: [],
      showSearch: true,
      searchForm: {
        code: null,
        msgType: null
      },
      columns: [
        {
          title: '模板编码',
          key: 'code'
        },
        {
          title: '模板标题',
          key: 'title'
        },
        {
          title: '消息类型',
          key: 'msgType',
          formatter(row) {
            const result = msgTypes.find(function(item) {
              return item.key === row.msgType
            })
            return result.value
          }
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ],
      detail: {
        formLoading: false,
        operationType: 'add',
        isDisabled: false,
        formVisible: false,
        templateId: null,
        form: {
          id: null,
          code: null,
          title: null,
          detailUrl: null,
          msgType: null,
          publishMsgNotifyType: null,
          notifyTypes: [],
          notifyTemplateSystem: null,
          notifyTemplateEmail: null,
          notifyTemplateSms: null,
          tenantIds: []
        },
        showPublishMsgNotifyType: false,
        showTenant: false,
        showNotifyTemplateSystem: false,
        showNotifyTemplateEmail: false,
        showNotifyTemplateSms: false,
        formRules: {
          code: [{ required: true, message: '请输入模板编码' }],
          title: [{ required: true, message: '请输入模板标题' }],
          msgType: [{ required: true, message: '请输入消息类型' }],
          publishMsgNotifyType: [{ required: true, trigger: 'blur', validator: validatePublishMsgNotifyType }],
          tenantIds: [{ required: true, trigger: 'blur', validator: validateTenants }],
          notifyTypes: [{ required: true, message: '请选择消息通知类型' }],
          notifyTemplateSystem: [{ required: true, trigger: 'blur', validator: validateNotifyTemplateSystem }],
          notifyTemplateEmail: [{ required: true, trigger: 'blur', validator: validateNotifyTemplateEmail }],
          notifyTemplateSms: [{ required: true, trigger: 'blur', validator: validateNotifyTemplateSms }]
        }
      },
      testMessage: {
        isDisabled: false,
        formVisible: false,
        templateCode: null,
        form: {
          jsonStr: `{
                      "0": {}
                    }`,
          receiverUserIds: []
        },
        formRules: {
          receiverUserIds: [{ required: true, trigger: 'blur', validator: validateReceiverUser }]
        },
        showUser: false
      }
    }
  },
  async created() {
    await this.getTenantData()
    await this.getAllUser()
  },
  methods: {
    async getTenantData() {
      const { data } = await getTenantList()
      this.tenants = data
    },
    async getAllUser() {
      const { data } = await getUserListByParams()
      this.users = data
    },
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve, reject) => {
        getTemplatePageByParams(params)
          .then((res) => {
            resolve({
              data: res.data.records,
              total: parseInt(res.data.total)
            })
          })
          .catch(() => {
            resolve({
              data: [],
              total: 0
            })
          })
      })
      callback(promise)
    },
    // 查询
    handleSearch() {
      this.$refs.myTable.handleSearch()
    },
    // 查询重置
    reset() {
      this.$refs.formSearch.resetFields()
      this.handleSearch()
    },
    // 新增模板
    addTemplate() {
      this.detail.formTitle = '新建模板'
      this.detail.operationType = 'add'
      this.detail.isDisabled = false
      this.detail.formVisible = true
      this.detail.showPublishMsgNotifyType = false
      this.detail.showTenant = false
      this.detail.showNotifyTemplateSystem = false
      this.detail.showNotifyTemplateEmail = false
      this.detail.showNotifyTemplateSms = false
      this.$nextTick(() => {
        this.$refs.templateForm.resetFields()
        this.detail.form.id = null
        this.detail.form.notifyTypes = []
      })
    },
    // 修改模板
    async editTemplate(row, type) {
      this.detail.showPublishMsgNotifyType = false
      this.detail.showTenant = false
      this.detail.showNotifyTemplateSystem = false
      this.detail.showNotifyTemplateEmail = false
      this.detail.showNotifyTemplateSms = false
      if (type === 'view') {
        this.detail.formTitle = '查看模板'
        this.detail.operationType = 'view'
        this.detail.isDisabled = true
      } else {
        this.detail.formTitle = '编辑模板'
        this.detail.operationType = 'edit'
        this.detail.isDisabled = false
      }
      const { data } = await getTemplateInfoById({ templateId: row.id })
      this.detail.formVisible = true
      this.$nextTick(() => {
        this.$refs.templateForm.resetFields()
        this.detail.form = {
          id: data.id,
          code: data.code,
          title: data.title,
          detailUrl: data.detailUrl,
          msgType: data.msgType,
          publishMsgNotifyType: data.publishMsgNotifyType,
          notifyTypes: data.notifyTypes,
          notifyTemplateSystem: data.notifyTemplateSystem,
          notifyTemplateEmail: data.notifyTemplateEmail,
          notifyTemplateSms: data.notifyTemplateSms,
          tenantIds: data.tenantIds
        }
        this.msgTypeChange()
        this.publishMsgTypeChange()
        this.handleNotifyType(this.detail.form.notifyTypes)
      })
    },
    // 删除模板
    delTemplate(id) {
      this.$confirm('确认要删除该模板吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delTemplate({ templateId: id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 消息类型更改
    msgTypeChange() {
      // 消息类型为个人时
      if (this.detail.form.msgType === 1) {
        this.detail.showPublishMsgNotifyType = false
        this.detail.form.publishMsgNotifyType = null
        this.detail.showTenant = false
        this.detail.form.tenantIds = null
      }
      // 消息类型为公告时
      if (this.detail.form.msgType === 2) {
        this.detail.showPublishMsgNotifyType = true
      }
    },
    // 公告消息类型更改
    publishMsgTypeChange() {
      // 公告至系统内所有成员
      if (this.detail.form.publishMsgNotifyType === 1) {
        this.detail.form.tenantIds = null
        this.detail.showTenant = false
      }
      // 公告至指定租户内的所有成员
      if (this.detail.form.publishMsgNotifyType === 2) {
        this.detail.showTenant = true
      }
    },
    // 消息通知方式更改
    handleNotifyType(value) {
      // 系统内通知
      const notifySystem = value.some(item => {
        return item === 1
      })
      if (notifySystem) {
        this.detail.showNotifyTemplateSystem = true
      } else {
        this.detail.showNotifyTemplateSystem = false
        this.detail.form.notifyTemplateSystem = null
      }
      // 邮件通知
      const notifyEmail = value.some(item => {
        return item === 2
      })
      if (notifyEmail) {
        this.detail.showNotifyTemplateEmail = true
      } else {
        this.detail.showNotifyTemplateEmail = false
        this.detail.form.notifyTemplateEmail = null
      }
      // 邮件通知
      const notifySms = value.some(item => {
        return item === 3
      })
      if (notifySms) {
        this.detail.showNotifyTemplateSms = true
      } else {
        this.detail.showNotifyTemplateSms = false
        this.detail.form.notifyTemplateSms = null
      }
    },
    systemCatchData(json) {
      this.detail.form.notifyTemplateSystem = json
    },
    emailCatchData(json) {
      this.detail.form.notifyTemplateEmail = json
    },
    // 消息模板保存/修改
    saveTemplate() {
      this.$refs.templateForm.validate(async(valid) => {
        if (valid) {
          this.detail.formLoading = true
          try {
            const params = JSON.parse(JSON.stringify(this.detail.form))
            if (this.detail.form.notifyTemplateSystem) {
              params.notifyTemplateSystem = params.notifyTemplateSystem.replace('target="_blank"', '')
            }
            if (this.detail.operationType === 'add') {
              await addTemplate(params)
            } else {
              await updateTemplate(params)
            }
            this.detail.formVisible = false
            this.$message.success('提交成功！')
            this.handleSearch()
          } catch (e) {
            this.$message.error('提交失败！')
          } finally {
            this.detail.formLoading = false
          }
        }
      })
    },
    // 消息测试
    messageTest(row) {
      this.testMessage.showUser = row.msgType === 1
      this.testMessage.templateCode = row.code
      this.testMessage.formVisible = true
      this.$nextTick(() => {
        this.$refs.testMessageForm.resetFields()
        this.testMessage.form.jsonStr = `{
                      "0": {}
                    }`
      })
    },
    // 发送消息
    sendMessageTo() {
      this.$refs.testMessageForm.validate(async(valid) => {
        if (valid) {
          this.testMessage.formLoading = true
          try {
            let paramsMap = {}
            if (this.testMessage.form.jsonStr) {
              paramsMap = JSON.parse(this.testMessage.form.jsonStr)
            }
            const params = {
              templateCode: this.testMessage.templateCode,
              paramsMap: paramsMap,
              receiverUserIds: this.testMessage.form.receiverUserIds
            }
            await sendMessage(params)
            this.testMessage.formVisible = false
            this.$message.success('发送成功！')
            this.handleSearch()
          } finally {
            this.testMessage.formLoading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.table-page {
  width: 100%;
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  .el-link {
    margin-right: 10px;
  };
}
::v-deep .tool-bar{
  margin: 0;
}
</style>
