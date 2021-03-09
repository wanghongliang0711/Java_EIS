<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  body .el-table th.gutter{
    display: table-cell!important;
  }
  .el-table__fixed {
    height: auto !important;
    bottom: 18px;
  }
  .el-table__fixed-right {
    height: auto !important;
    bottom: 18px;
  }
</style>
