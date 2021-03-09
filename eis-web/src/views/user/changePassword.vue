<template>
  <div class="app-container">
    <h2 align="center"> 修 改 密 码 </h2>
    <el-form ref="ruleForm" :model="form" :rules="rules" align="center" style="width: 500px" label-width="80px">
      <el-form-item label="原密码" prop="lastPassword">
        <el-input v-model="form.lastPassword" type="password" placeholder="请输入原密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input v-model="form.password" type="password" placeholder="请设置新密码" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="请确认新密码" />
      </el-form-item>
      <el-form-item>
        <el-button @click="reset()">重置</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { changePassword } from '@/api/user'
export default {
  name: 'ChangePassword',
  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else if (value.toString().length < 5 || value.toString().length > 15) {
        callback(new Error('密码长度为 5 到 15 个字符'))
      } else if (value.toString() === this.form.lastPassword) {
        callback(new Error('新密码不能与原密码相同！'))
      } else {
        callback()
      }
    }
    return {
      // 表单参数
      form: { lastPassword: '', password: '', confirmPassword: '' },
      // 表单校验
      rules: {
        lastPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请设置新密码', trigger: 'blur' },
          { min: 5, max: 15, message: '密码长度为 5 到 15 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.reset()
  },
  methods: {
    /** 提交按钮 */
    submitForm: function() {
      this.$refs['ruleForm'].validate(valid => {
        if (valid) {
          changePassword(this.form).then(res => {
            if (res.code === '100000') {
              if (res.data.msgFail !== undefined) {
                this.$message({ showClose: true, message: res.data.msgFail, type: 'error', duration: 5000 })
              } else {
                this.$message({ showClose: true, message: '修改密码成功, 3秒后将退出登录！！！', type: 'success' })
                setTimeout(() => {
                  this.logout()
                }, 3000)
              }
            }
          })
        }
      })
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      this.$router.push(`/login`)
    },
    reset() {
      this.form = {
        lastPassword: '',
        password: '',
        confirmPassword: ''
      }
    }
  }
}
</script>

<style scoped>
  .app-container {
    padding: 20px;
    position: absolute;
    top: 15%;
    left: 25%;
  }
</style>
