<template>
  <div class="table-page">
    <el-form v-show="showSearch" ref="formSearch" :model="searchForm" size="mini" class="formSearch" inline>
      <el-form-item label="消息类型" prop="msgType">
        <el-select v-model="searchForm.msgType" placeholder="全部" clearable>
          <el-option
            v-for="item in msgTypes"
            :key="item.value"
            :label="item.value"
            :value="item.key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="businessStatus">
        <el-select v-model="searchForm.businessStatus" placeholder="全部" clearable>
          <el-option
            v-for="item in businessStatus"
            :key="item.value"
            :label="item.value"
            :value="item.key"
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
          <el-button v-auth="'message_content_read_all'" type="primary" size="mini" @click="readAll">全部已读</el-button>
        </div>
      </template>
      <template v-slot:operation="scope">
        <el-link v-auth="'message_content_view'" type="primary" :underline="false" @click="viewContent(scope.row)">查看</el-link>
        <el-link v-show="scope.row.businessStatus === 401" v-auth="'message_content_read'" type="primary" :underline="false" @click="updateReader(scope.row)">已读</el-link>
        <el-link v-auth="'message_content_del'" type="primary" :underline="false" @click="delContent(scope.row)">删除</el-link>
      </template>
    </my-table>
    <el-dialog title="查看消息" :visible.sync="showContent" width="600px">
      <div class="message-title">{{ activeData.title }}</div>
      <div class="message-other">
        <span>发布时间：{{ activeData.createTime }}</span>
      </div>
      <div class="message-info-box" v-html="activeData.content" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="showContent = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { delContentById, getContentPage, updateAllReader, updateReaderById } from '@/api/message'
import moment from 'moment/moment'

const msgTypes = [
  { key: 1, value: '个人' },
  { key: 2, value: '公告' }
]
const businessStatus = [
  { key: 6, value: '未读' },
  { key: 7, value: '已读' }
]
export default {
  name: 'message-content',
  data() {
    return {
      msgTypes,
      businessStatus,
      showSearch: true,
      searchForm: {
        msgType: null,
        businessStatus: null,
        notifyType: 1
      },
      columns: [
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
          title: '消息标题',
          key: 'title'
        },
        {
          title: '业务状态',
          key: 'businessStatus',
          formatter(row) {
            const result = businessStatus.find(function(item) {
              return item.key === row.businessStatus
            })
            return result.value
          }
        },
        {
          title: '时间',
          key: 'createTime',
          formatter(row) {
            return moment(row.createTime).format('YYYY-MM-DD HH:mm:ss')
          }
        },
        {
          title: '操作',
          key: 'operation',
          className: 'text-left',
          rowSlot: true
        }
      ],
      showContent: false,
      activeData: {}
    }
  },
  async created() {
  },
  methods: {
    pageRequest(query, callback) {
      const params = query
      const promise = new Promise((resolve, reject) => {
        getContentPage(params)
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
    // 查看消息
    async viewContent(row) {
      this.showContent = true
      this.activeData = row
      if (row.businessStatus === 6) {
        await this.updateReader(row)
      }
    },
    // 全部已读
    async readAll() {
      this.$confirm('确认要全部消息标记为已读吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await updateAllReader()
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
    },
    // 修改消息已读
    async updateReader(row) {
      await updateReaderById({ contentId: row.id })
      this.handleSearch()
    },
    // 删除消息
    delContent(row) {
      this.$confirm('确认要删除该消息吗？', '提示', {
        type: 'warning'
      }).then(async _ => {
        await delContentById({ contentId: row.id })
        this.$message.success('操作成功！')
        this.handleSearch()
      }).catch(_ => {})
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
