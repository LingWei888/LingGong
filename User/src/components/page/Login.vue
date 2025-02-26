<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">用户管理中心</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="账号">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        type="password"
                        placeholder="密码"
                        v-model="param.password"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">登录</el-button>
                </div>
                <p class="login-tips">Tips : 没有账号？<router-link to="/reg">立即注册</router-link></p>
            </el-form>
        </div>






    </div>
</template>

<script>
export default {
    data: function() {
        return {
            param: {

            },
            rules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            },
        };
    },
    methods: {
        submitForm() {
            this.$refs.login.validate(valid => {
                if (valid) {

                  this.$axios
                      .post(this.Api+'User/login', {
                        user: this.param.username,
                        pwd: this.param.password,
                      })
                      .then(res => {
                        if (res.data.code == 200) {
                          //设置token
                          localStorage.setItem('token', res.data.data.token);
                          localStorage.setItem('uid', res.data.data.userId);
                          localStorage.setItem('roleid', res.data.data.roleId);
                          localStorage.setItem('qq', res.data.data.qq);
                          this.$message.success('登录成功');
                          localStorage.setItem('ms_username', this.param.username);

                          this.$router.push('/');
                        } else {
                          this.$message.error(res.data.msg);
                        }
                      });


                } else {
                    this.$message.error('请输入账号和密码');
                    console.log('error submit!!');
                    return false;
                }
            });
        },
    },
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/login-bg.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>