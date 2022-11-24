<template>
  <div class="app-container">
    <iframe id="mainIframe" style="width: 100%; height: 100%" class="mainIframe" :src="getUrl" />
  </div>
</template>

<script>
export default {
  name: 'job-task',
  data() {
    return {
    }
  },
  computed: {
    getUrl() {
      return process.env.VUE_APP_BASE_API + '/job-client/job'
    }
  },
  methods: {
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
}
</style>

