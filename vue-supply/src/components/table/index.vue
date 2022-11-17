<template>
  <div class="table-container">
    <!-- 自定义插槽 -->
    <slot name="comSlot" />
    <!-- 表头 -->
    <div class="tool-bar" :class="{ maginBottom10: toolbar || Object.prototype.hasOwnProperty.call($slots, 'toolSlot')}">
      <!-- 表头插槽 -->
      <slot name="toolSlot" />
      <!-- 表头工具 -->
      <table-tool
        v-if="toolbar"
        :columns="newColumns"
        @update="update"
        @see="see"
        @checkboxChange="checkboxChange"
      />
    </div>
    <!-- 多选提示框 -->
    <div v-if="selectNum&&selectTips" class="tips">
      <div class="tips-left">
        <img src="@/assets/images/table-tips.png" alt="">
        <p>
          已选中 <span>{{ selectNum }}</span> 项
        </p>
      </div>
      <div class="tips-right" @click="handleClear">清空</div>
    </div>
    <!-- 自定义插槽 -->
    <slot name="comSubSlot" />
    <!-- 表格 -->
    <div id="table-center" :class="{ 'position-relative': positionStatus }">
      <el-table
        ref="myTable"
        :key="tableKay"
        v-loading="loading"
        :class="{ 'position-absolute': positionStatus }"
        :data="tableData"
        size="small"
        style="width: 99.9%"
        v-bind="$attrs"
        :max-height="$attrs['max-height']||tableHeight"
        :cell-style="cellStyle"
        :header-cell-style="headerCellStyle"
        :span-method="spanMethod"
        :row-key="getRowKey"
        :load="loadChildren"
        lazy
        :tree-props="{children: 'children', hasChildren: childrenName}"
        v-on="$listeners"
        @selection-change="selectionChange"
        @select="userSelect"
        @select-all="userSelectAll"
      >
        <el-table-column
          v-if="Object.prototype.hasOwnProperty.call($attrs, 'selection') && pageChoose"
          type="selection"
          :reserve-selection="true"
          :selectable="selectable"
          width="55"
        />
        <el-table-column
          v-if="Object.prototype.hasOwnProperty.call($attrs, 'selection') && !pageChoose"
          type="selection"
          :selectable="selectable"
          width="55"
        />

        <template v-for="item in columnFilter(newColumns)">
          <!-- 序号 -->
          <el-table-column
            v-if="item.key === 'index'"
            :key="item.key"
            v-bind="item"
            :prop="item.key"
            :label="item.title"
            :width="item.width ? item.width : '55'"
          >
            <template slot-scope="scope">
              <span> {{ serialNumber(item, scope) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-else-if="item.type" :key="item.key" v-bind="item" :prop="item.key" :label="item.title" />
          <el-table-column v-else-if="item.formatter&&item.headerSlot" :key="item.key" v-bind="item" :prop="item.key" :label="item.title">
            <template slot="header" slot-scope="scope">
              <slot :name="item.key + 'Header'" :scope="scope" :row="scope.row" />
            </template>
          </el-table-column>
          <el-table-column v-else-if="item.formatter" :key="item.key" v-bind="item" :prop="item.key" :label="item.title" />
          <el-table-column
            v-else
            :key="item.key"
            v-bind="item"
            :prop="item.key"
            :label="item.title"
          >
            <template slot="header" slot-scope="scope">
              <slot
                v-if="item.headerSlot"
                :name="item.key + 'Header'"
                :scope="scope"
                :row="scope.row"
              />
              <span v-else>{{ scope.column.label }}</span>
            </template>
            <template slot-scope="scope">
              <slot v-if="item.rowSlot" :name="item.key" :scope="scope" :row="scope.row" />
              <span v-else-if="Object.prototype.hasOwnProperty.call(item, 'dict')" v-html="dictHtml(item, scope)" />
              <span v-else>{{ scope.row[item.key] }}</span>
            </template>
          </el-table-column>
        </template>
      </el-table>
      <!-- 自定义插槽 -->
      <slot name="comTableSlot" />
    </div>
    <!-- 页尾 -->
    <div class="pagination-center">
      <div>
        <!-- 页尾插槽 -->
        <slot name="footerSlot" />
      </div>
      <!-- 分页 -->
      <el-pagination
        v-if="pagination"
        background
        :page-size="limit"
        :current-page.sync="page"
        :layout="paginationLayout"
        :total="total"
        class="pagination"
        @current-change="currentChange"
        @size-change="sizeChange"
        @prev-click="prevClick"
        @next-click="nextClick"
      >
        <span><em class="pagination-currentNum">{{ page }}</em>/{{ Math.ceil(total / limit) }}</span>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import TableTool from './table-tool'
export default {
  name: 'my-table',
  components: {
    TableTool
  },
  props: {
    // 列
    columns: {
      type: Array,
      default: () => []
    },
    // 筛选条件
    searchForm: {
      type: Object,
      default: () => {}
    },
    // 表头工具栏
    toolbar: {
      type: Boolean,
      default: false
    },
    // 调页最大数量
    toPageLimitMax: {
      type: Number,
      default: 10000
    },
    // 主动请求
    autoRequest: {
      type: Boolean,
      default: true
    },
    autoHeight: {
      type: Boolean,
      default: false
    },
    // 分页组件
    pagination: {
      type: Boolean,
      default: true
    },
    // 选择提示
    selectTips: {
      type: Boolean,
      default: true
    },
    // 自定义参数集合（方便扩展）
    tableProps: {
      type: Object,
      default: () => {}
    },
    // 是否支持翻页多选
    pageChoose: {
      type: Boolean,
      default: false
    },
    // 已选择的数据（用于翻页多选回显）目前只支持含有唯一code值的数据
    choosedData: {
      type: Array,
      default: () => []
    },
    tableKay: {
      type: Number,
      default: 1
    },
    // 是否有子级
    childrenName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      newColumns: [],
      loading: false,
      page: 1,
      limit: 10,
      total: 1,
      selectNum: 0,
      tableData: [],
      tableHeight: null,
      paginationLayout: 'total, sizes, prev, pager, next, jumper',
      excessQuantity: true, // 调页是否超出最大数量限制
      merge: [],
      pos: null,
      newChoosedData: [],
      positionStatus: false

    }
  },
  computed: {
    // 单元格样式
    cellStyle() {
      return {
        'text-align': 'center',
        'font-size': '12px',
        'color': '#666'
      }
    },
    // 表格头部样式
    headerCellStyle() {
      return {
        'text-align': 'center',
        'background-color': '#f2f2f2',
        'color': '#666',
        'font-weight': 'normal',
        'font-size': '12px'
      }
    }
  },
  watch: {
    columns: {
      handler(val) {
        if (val) {
          this.newColumns = this.columns.map((c) => {
            return {
              ...c,
              'show-overflow-tooltip': c.showOverflowTooltip ? c.showOverflowTooltip : true,
              show: true
            }
          })
        }
      },
      immediate: true
    },
    $attrs: {
      handler(val) {
        if (val.data) {
          if (val.data.length) {
            this.tableData = []
            val.data.forEach(v => {
              this.tableData.push(v)
            })
            this.initMergeRow(val.data)
          } else {
            this.tableData = []
          }
        }
      },
      deep: true,
      immediate: true
    },
    tableData: {
      handler(val) {
        if (val) {
          if (val?.length > 0 && this.choosedData?.length > 0) {
            this.$nextTick(() => {
              this.tableData.forEach((vl, i) => {
                if (this.newChoosedData.some(item => { return item.code === vl.code })) {
                  this.$refs.myTable.toggleRowSelection(vl, true)
                } else {
                  this.$refs.myTable.toggleRowSelection(vl, false)
                }
              })
            })
          }
        }
      },
      immediate: true
    }
  },
  created() {
    if (!Object.prototype.hasOwnProperty.call(this.$attrs, 'data') && this.autoRequest) {
      this.pageRequest()
    }
    this.$nextTick(() => {
      this.getTableHeight()
      // 监听窗口变化
      window.addEventListener('resize', this.getTableHeight, true)
    })
  },
  mounted() {
    this.$nextTick(() => {
      this.newChoosedData = [...this.choosedData]
    })
  },
  beforeDestroy() {
    // 解绑监听
    window.removeEventListener('resize', this.getTableHeight, true)
  },
  methods: {
    selectable(row, index) {
      if (row.disableChoose) {
        return false
      } else {
        return true
      }
    },
    getRowKey(row) {
      if (!this.$attrs.data && (row.id || row.code)) {
        return row.id ? row.id : row.code
      }
    },
    // 数据请求
    pageRequest() {
      this.loading = true
      const query = {
        pageIndex: this.page,
        pageSize: this.limit
      }
      for (const key in this.searchForm) {
        const type = Object.prototype.toString.call(this.searchForm[key])
        if (type === '[object String]' && this.searchForm[key] !== '') {
          query[key] = this.searchForm[key]
        }
        if (type === '[object Number]') {
          query[key] = this.searchForm[key]
        }
        if (
          type === '[object Object]' &&
          Object.keys(this.searchForm[key]).length
        ) {
          query[key] = this.searchForm[key]
        }
        if (type === '[object Array]' && this.searchForm[key].length) {
          query[key] = this.searchForm[key]
        }
      }
      this.$emit('pageRequest', query, (callback) => {
        callback
          .then((res) => {
            const { data, total } = res
            this.tableData = data
            this.total = Number(total)
            this.page = query.pageIndex
            this.limit = query.pageSize
            if (this.tableProps && Object.prototype.hasOwnProperty.call(this.tableProps, 'mergeRow')) {
              this.initMergeRow(data)
            }
          })
          .finally(() => {
            this.loading = false
            // 清空多选
            if (this.selectNum && !this.pageChoose) {
              this.handleClear()
            }
          })
      })
    },
    // 初始化合并行
    initMergeRow(data) {
      this.pos = null
      this.merge = new Map()
      this.getSpanArr(data)
    },
    // 判断合并关键字
    getSpanArr(data) {
      if (this.tableProps && Object.prototype.hasOwnProperty.call(this.tableProps, 'mergeRow')) {
        for (let i = 0, l = this.tableProps.mergeRow.length; i < l; i++) {
          const arr = []
          for (let j = 0; j < data.length; j++) {
            if (j === 0) {
              arr.push(1)
              this.pos = 0
            } else {
              if (data[j][this.tableProps.mergeRow[i]] === data[j - 1][this.tableProps.mergeRow[i]]) {
                arr[this.pos] += 1
                arr.push(0)
              } else {
                arr.push(1)
                this.pos = j
              }
            }
          }
          this.merge.set(this.tableProps.mergeRow[i], arr)
        }
      }
    },
    // 合并行
    spanMethod({ row, column, rowIndex, columnIndex }) {
      let _row = null
      let _col = null
      try {
        this.merge.forEach((c, key) => {
          if (column.property === key) {
            _row = c[rowIndex]
            _col = _row > 0 ? 1 : 0
            throw new Error()
          }
        })
      } catch (error) {
        return {
          rowspan: _row,
          colspan: _col
        }
      }
    },
    // 主动查询
    handleSearch({ page } = {}) {
      if (page) {
        this.page = page
      } else {
        this.page = 1
      }
      this.pageRequest()
    },
    // 获取展示列
    columnFilter(columns) {
      return columns.filter((c) => c.show)
    },
    // 单元格 dict 转换
    dictHtml(column, scope) {
      for (let i = 0; i < column.dict.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (scope.row[column.key] == column.dict[i].value) {
          if (column.dict[i].color && column.dict[i].color.includes('#')) {
            return `<span style="color:${column.dict[i].color}">${column.dict[i].label}</span>`
          }
          return `<span class="${column.dict[i].color}">${column.dict[i].label}</span>`
        }
      }
    },
    // 序号
    serialNumber(column, scope) {
      const index = scope.$index
      return index + 1 + (this.page - 1) * this.limit
    },
    // 获取table容器高度
    getTableHeight() {
      if (this.autoHeight) return
      if (Object.prototype.hasOwnProperty.call(this.$attrs, 'height')) return
      if (Object.prototype.hasOwnProperty.call(this.$attrs, 'max-height')) return
      this.positionStatus = true
      setTimeout(() => {
        const box = document.getElementById('table-center')
        const centerHeight = window.getComputedStyle(box).height
        this.tableHeight = parseInt(centerHeight) + 'px'
        this.positionStatus = false
        this.$refs.myTable.doLayout()
      })
    },
    // 翻页
    currentChange(page) {
      if (
        this.excessQuantity &&
        this.toPageLimitMax &&
        this.limit * page > this.toPageLimitMax
      ) {
        this.$message.warning(
          '列表仅在 10000 条内支持调页查看！请使用上一页，下一页进行翻页。'
        )
        const maxPage = this.toPageLimitMax / this.limit
        this.page = maxPage
        this.paginationLayout = 'total, sizes, prev,slot, next'
      } else {
        this.page = page
        if (this.toPageLimitMax && this.limit * page > this.toPageLimitMax) {
          this.paginationLayout = 'total, sizes, prev,slot, next'
        } else {
          this.paginationLayout = 'total, sizes, prev, pager, next, jumper'
        }
      }
      this.excessQuantity = true
      this.pageRequest()
    },
    // 改变每页数量
    sizeChange(limit) {
      this.limit = limit
      this.page = 1
      this.paginationLayout = 'total, sizes, prev, pager, next, jumper'
      this.pageRequest()
    },
    // 上一页
    prevClick() {
      this.excessQuantity = false
    },
    // 下一页
    nextClick() {
      this.excessQuantity = false
    },
    // 列设置
    checkboxChange(key, show) {
      this.newColumns.forEach((c) => {
        if (c.key === key) {
          c.show = show
        }
      })
    },
    // 多选
    selectionChange(e) {
      if (this.selectNum && !e.length) {
        this.getTableHeight()
      }
      if (!this.selectNum && e.length) {
        this.getTableHeight()
      }
      this.selectNum = e.length
    },
    // 用户手动触发选择
    userSelect(e, row) {
      const tmp = [...this.newChoosedData]
      let i = 0
      if (this.newChoosedData.some((item, index) => {
        i = index
        return row.code === item.code
      })) {
        tmp.splice(i, 1)
      } else {
        tmp.push(row)
      }
      this.newChoosedData = tmp
    },
    // 用户手动触发全选
    userSelectAll(e) {
      let tmp = [...this.newChoosedData]
      this.tableData.forEach((row) => {
        if (e.length === 0 || !this.tableData.some((item) => { return item.code === e[0].code })) {
          tmp = tmp.filter(item => { return item.code !== row.code })
        } else {
          if (!this.newChoosedData.some((item, index) => { return row.code === item.code })) {
            tmp.push(row)
          }
        }
      })
      this.newChoosedData = tmp
    },
    // 清空多选
    handleClear() {
      this.$refs.myTable.clearSelection()
    },
    // 控制搜索栏显隐
    see() {
      this.getTableHeight()
      this.$emit('see')
    },
    update() {
      this.handleSearch({ page: this.page })
    },
    loadChildren(tree, treeNode, resolve) {
      this.$emit('loadChildren', tree, treeNode, (callback) => {
        callback
          .then((res) => {
            resolve(res)
          })
          .finally(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>

<style lang='scss' >
.table-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  border: solid 1px #fff;
  padding: 16px;
  margin: 16px;
  box-sizing: border-box;
  .position-relative {
    position: relative;
  }
  .position-absolute {
    position: absolute;
  }
  #table-center {
    flex: 1;
  }
  .maginBottom10{
    margin-bottom: 10px;
  }
  .tips {
    height: 30px;
    background: #e6f7ff;
    border: 1px solid #bae7ff;
    box-shadow: 0px 3px 6px rgba(153, 153, 153, 0.11);
    border-radius: 2px;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 30px 0 18px;
    box-sizing: border-box;
    .tips-left {
      height: 100%;
      display: flex;
      align-items: center;
      img {
        width: 14px;
        height: 14px;
        margin-right: 23px;
      }
      p {
        font-size: 14px;
        font-family: "Regular";
        color: rgba(0, 0, 0, 0.65);
      }
      span {
        color: #5191fd;
      }
    }
    .tips-right {
      font-size: 14px;
      font-family: "Regular";
      color: #5191fd;
      cursor: pointer;
    }
  }
  .tool-bar {
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
  .table-solt {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    color: #5191fd;
  }
  .pagination-center {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 10px;

    .pagination-currentNum {
      color:#5191fd;
    }
    .pagination {
      text-align: center;
      width: 100%;
    }
  }

  .el-table--border th.el-table__cell.gutter:last-of-type {
    background-color: #f1f4f9 !important;
  }
  .el-table td.el-table__cell div {
    .el-link{
      margin: 0 5px;
    }
  }
  .el-pagination {
    font-weight: normal;
  }
}
.el-tooltip__popper {
  max-width: 50%;
}
</style>
