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
                <el-form-item prop="confirmPassword">
                    <el-input
                        type="password"
                        placeholder="确认密码"
                        v-model="param.confirmPassword"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="mail">
                  <el-input v-model="param.mail" placeholder="邮箱">
                    <el-button slot="prepend" icon="el-icon-lx-mail"></el-button>
                  </el-input>
                </el-form-item>
                <el-form-item prop="code">
                  <el-input v-model="param.code" placeholder="验证码">
                    <el-button slot="prepend" :disabled="isCounting" @click="sendCode">{{ countdownText }}</el-button>
                  </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="submitForm()">注册</el-button>
                </div>
                <p class="login-tips">Tips : 已有账号？去<router-link to="/login">登录</router-link></p>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
    data: function() {
        return {
            param: {
                username: '',
                password: '',
                confirmPassword: '', // 新增
                mail: '',
                code: ''
            },
            rules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                confirmPassword: [ // 新增
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: this.validateConfirmPassword, trigger: 'blur' }
                ],
                mail: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
                code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
            },
            isCounting: false,
            countdown: 60,
            countdownText: '点击发送'
        };
    },
    methods: {
        submitForm() {
            this.$refs.login.validate(valid => {
                if (valid) {
                    this.$axios
                    .post(this.Api+'User/reg', {
                        username: this.param.username,
                        password: this.param.password,
                        email: this.param.mail,
                        code: this.param.code
                    })
                    .then(res => {
                        if (res.data.code == 200) {
                            this.$message.success('注册成功');
                            this.$router.push('/login');
                        } else {
                            this.$message.error(res.data.msg);
                        }
                    }, error => {
                        this.$message.error("请重新获取验证码");
                    })

                } else {
                    this.$message.error('请输入完整信息');
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        sendCode() {
            if (this.isCounting) return;

            // 验证邮箱格式
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!emailPattern.test(this.param.mail)) {
                this.$message.error('请输入正确的邮箱地址');
                return;
            }

            // 验证账号和密码是否填写完整
            if (!this.param.username || !this.param.password) {
                this.$message.error('请输入账号和密码');
                return;
            }

            // 模拟发送验证码接口调用
            this.$axios
            .post(this.Api+'User/regcode', {
                username: this.param.username,
                password: this.param.password,
                email: this.param.mail
            })
            .then(res => {
                if (res.data.code == 200) {
                    this.$message.success('验证码已发送，请查收邮箱');
                    this.startCountdown();
                } else {
                    this.$message.error(res.data.msg);
                }
            })

        },
        startCountdown() {
            this.isCounting = true;
            this.countdownText = `${this.countdown}秒后重新发送`;
            const interval = setInterval(() => {
                this.countdown--;
                this.countdownText = `${this.countdown}秒后重新发送`;
                if (this.countdown <= 0) {
                    clearInterval(interval);
                    this.isCounting = false;
                    this.countdown = 60;
                    this.countdownText = '点击发送';
                }
            }, 1000);
        },
        validateConfirmPassword(rule, value, callback) { // 新增
            if (value !== this.param.password) {
                callback(new Error('两次输入的密码不一致'));
            } else {
                callback();
            }
        }
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