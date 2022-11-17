<template>
  <div class="upload-container">
    <el-upload
      class="image-uploader"
      :multiple="false"
      :show-file-list="false"
      :disabled="loading"
      :accept="fileType"
      :before-upload="beforeUpload"
      :on-progress="handleImageProgress"
      :on-success="handleImageSuccess"
      :on-error="handleImageError"
      :file-list="fileList"
      :action="fileApi"
    >
      <i :class="loading ? 'el-icon-loading' : 'el-icon-upload'" />
      <div class="el-upload__text">{{ loading?'上传中':'点击上传' }}</div>
    </el-upload>
    <div v-if="imageUrl != null" class="image-preview">
      <div class="image-preview-wrapper">
        <img :src="imageUrl">
        <div class="file-name">
          <span>{{ imageUrl.split('/').pop() }}</span>
        </div>
        <div class="image-preview-action">
          <i class="el-icon-delete" @click="rmImage" />
          <i class="el-icon-zoom-in" @click="handleImagePreview" />
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="visible" width="500px" style="text-align:center;" append-to-body>
      <img v-if="imageUrl != null" :src="imageUrl" style="max-width: 100%">
    </el-dialog>
  </div>
</template>

<script>

import { delUnRelationAttachmentById, getAttachmentByParams } from '@/api/file'

export default {
  name: 'single-image-upload',
  props: {
    value: {
      type: String,
      default: null
    },
    fileType: {
      type: String,
      default: 'image/*'
    },
    size: {
      type: Number,
      default: 5
    },
    businessId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      visible: false,
      isFile: true,
      loading: false,
      fileApi: ((process.env.NODE_ENV === 'production') ? window.webConfig.gateway : process.env.VUE_APP_BASE_API) + '/file/attachment/upload',
      imageName: null,
      fileList: [],
      busId: this.businessId
    }
  },
  computed: {
    imageUrl() {
      if (this.value) {
        return window.webConfig.gateway + '/file/attachment/downloadByParams?attachmentId=' + this.value
      }
      if (this.busId) {
        return window.webConfig.gateway + '/file/attachment/downloadByParams?businessId=' + this.busId
      }
      return null
    }
  },
  watch: {
    businessId: {
      handler: function(val) {
        if (val) {
          this.busId = val
          this.getFileInfo()
        } else {
          this.busId = null
          this.rmImage()
        }
      },
      deep: true,
      immediate: true
    }
  },
  created() {

  },
  methods: {
    async getFileInfo() {
      const { data } = await getAttachmentByParams({ businessId: this.busId })
      if (data !== null) {
        this.imageName = data.name
        this.emitInput(data.id)
      }
    },
    async rmImage() {
      await delUnRelationAttachmentById({ attachmentId: this.value })
      this.emitInput(null)
      this.busId = null
      this.fileList = []
    },
    emitInput(val) {
      this.$emit('input', val)
    },
    handleImageSuccess(response) {
      this.loading = false
      if (response.code !== 200) {
        this.$message({ message: response.message, type: 'error' })
        return
      }
      this.imageName = response.data.name
      this.emitInput(response.data.id)
      this.$emit('uploadEnd')
    },
    handleImageError(_err, file, fileList) {
      this.loading = false
      this.$emit('uploadEnd')
    },
    handleImageProgress(event, file, fileList) {
      this.loading = true
      this.$emit('uploading')
    },
    handleImagePreview() {
      this.visible = true
    },
    beforeUpload(file) {
      if (file?.size > 1024 * 1024 * this.size) {
        this.$nextTick(() => {
          this.$message({
            message: `上传的文件最大为${this.size}M！`,
            type: 'error'
          })
        })
        return false
      }

      const imageArr = ['.png', '.jpeg', '.jpg', '.bmp']
      let fileTypeArr = this.fileType.split(',')
      if (this.fileType.indexOf('image/*') !== -1) {
        fileTypeArr = fileTypeArr.concat(imageArr)
      }
      const activeFileType = '.' + (file ? file.name.split('.')[1] : undefined)
      if (activeFileType && !fileTypeArr.includes(activeFileType)) {
        this.$nextTick(() => {
          this.$message({
            message: `请上传${this.fileType}类型的文件！`,
            type: 'error'
          })
        })
        return false
      }
      return file
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  $previewWH:100px;
  .upload-container {
    display: inline-block;
    width: $previewWH;
    height: $previewWH;
    border: 1px solid #ddd;
    position: relative;
    @include clearfix;
    .image-uploader {
      width: 100%;
      height: 100%;
      i{
        font-size: 34px;
        color: #aaa;
        margin-top: 19px;
      }
      .el-icon-loading{
        font-size: 30px;
        margin-top: 24px;
      }
      .el-upload__text{
        color: #888;
        margin-top: -18px;
      }
    }
    .image-preview {
      width: 100%;
      height: 100%;
      position: absolute;
      left: 0;
      top: 0;
      background: #fff;
      .image-preview-wrapper {
        position: relative;
        width: 100%;
        height: 100%;
        line-height: $previewWH;
        text-align: center;
        vertical-align: middle;
        overflow: hidden;
        .file-name{
          height: 100%;
          word-break: break-all;
          padding: 0 10px;
          color: rgb(53, 183, 235);
          span{
            display: inline-block;
            line-height: 20px;
            vertical-align: middle;
          }
        }
        img {
          max-width: 100%;
          max-height: 100%;
          vertical-align: middle;
          margin-bottom: 5px;
        }
      }
      .image-preview-action {
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        top: 0;
        cursor: default;
        text-align: center;
        color: #fff;
        opacity: 0;
        font-size: 16px;
        background-color: rgba(0, 0, 0, .5);
        transition: opacity .3s;
        text-align: center;
        line-height: $previewWH;
        i {
          display: inline-block;
          margin: 0 8px;
          font-size: 24px;
          cursor: pointer;
        }
      }
      &:hover {
        .image-preview-action {
            opacity: 1;
        }
      }
    }
  }
</style>
