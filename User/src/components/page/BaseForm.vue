<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-calendar"></i> 基础设置
                </el-breadcrumb-item>
                <el-breadcrumb-item>基本信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="姓名">
                        <el-input v-model="form.name" :disabled="isDisabled"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号">
                        <el-input v-model="form.card" :disabled="isDisabled"></el-input>
                    </el-form-item>
                    <el-form-item v-if="roleid === '-1'" label="职业">
                        <el-select v-model="form.occupation" placeholder="请选择职业" @change="handleOccupationChange">
                            <el-option label="自由工作者" value="0"></el-option>
                            <el-option label="雇主" value="1"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="roleid !== '-1'" label="个人标签">
                        <el-input v-model="form.tips" ></el-input>
                    </el-form-item>
                    <div v-if="roleid === '-1'&& form.occupation === '1'">雇主务必要缴纳保证金，保证金为1000元以上，请务必支付(备注自己的账号)。</div><br v-if="roleid === '-1'&& form.occupation === '1'">
                    <el-form-item v-if="roleid === '-1' && form.occupation === '1'||roleid==='1'||roleid === '-2'" label="保证金">
                        <el-input v-model="form.bzmoney" :disabled="roleid !== '-1'"></el-input>
                        <img v-if="!isDisabled && form.occupation === '1'" src="../../assets/img/qrcode.png" alt="QR Code" style="width: 150px; height: 200px;">
                    </el-form-item>
                    <el-form-item v-if="roleid !== '-1'" label="邮箱">
                        <el-input v-model="form.mail" ></el-input>
                    </el-form-item>
                    <el-form-item v-if="roleid !== '-1'" label="QQ">
                        <el-input v-model="form.qq" ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">表单提交</el-button>
                        <el-button>取消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'baseform',
    data() {
        return {
            options: [
                {
                    value: 'guangdong',
                    label: '广东省',
                    children: [
                        {
                            value: 'guangzhou',
                            label: '广州市',
                            children: [
                                {
                                    value: 'tianhe',
                                    label: '天河区'
                                },
                                {
                                    value: 'haizhu',
                                    label: '海珠区'
                                }
                            ]
                        },
                        {
                            value: 'dongguan',
                            label: '东莞市',
                            children: [
                                {
                                    value: 'changan',
                                    label: '长安镇'
                                },
                                {
                                    value: 'humen',
                                    label: '虎门镇'
                                }
                            ]
                        }
                    ]
                },
                {
                    value: 'hunan',
                    label: '湖南省',
                    children: [
                        {
                            value: 'changsha',
                            label: '长沙市',
                            children: [
                                {
                                    value: 'yuelu',
                                    label: '岳麓区'
                                }
                            ]
                        }
                    ]
                }
            ],
            form: {
                name: '',
                card: '',
                tips: '',
                bzmoney: '',
                mail: '',
                qq: '',
                occupation: '' // 新增 occupation 字段
            },
            isDisabled: true,
            roleid: localStorage.getItem('roleid')
        };
    },
    created() {
        this.getData();
        this.checkRole();
    },
    methods: {
        getData() {
            this.$axios
                .post(this.Api + 'User/cxInfo', {}, {
                    headers: {
                        'token': localStorage.getItem('token')
                    }
                })
                .then(res => {
                    if (res.data.code == 200) {
                        this.form = res.data.data;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                },
                //判断如果报错就跳转到登录页
                error => {
                    this.$message.error('请重新登录');
                    this.$router.push('/login');
                }
            );
        },
        onSubmit() {
            const roleid = localStorage.getItem('roleid');
            const apiEndpoint = roleid === '-1' ? 'User/CheckInfo' : 'User/infoEdit';
            this.$axios
                .post(this.Api + apiEndpoint,
                    {
                        name: this.form.name,
                        id_card: this.form.card,
                        role_id: this.form.occupation,
                        money: this.form.bzmoney,
                        tips: this.form.tips,
                        mail: this.form.mail,
                        qq: this.form.qq
                    }
                    , {
                    headers: {
                        'token': localStorage.getItem('token')
                    }
                })
                .then(res => {
                    if (res.data.code == 200) {
                        this.$message.success('修改成功');
                        if(roleid === '-1'){
                          localStorage.setItem('roleid', this.form.occupation);
                        }
                    }else if(res.data.code == 201){
                        this.$message.success(res.data.msg);
                        localStorage.setItem('roleid', "-2");
                    }else{
                        this.$message.error(res.data.msg);
                    }
                },
                //判断如果报错就跳转到登录页
                error => {
                    this.$message.error('请重新登录');
                    localStorage.removeItem('token');
                    this.$router.push('/login');
                }
            )
        },
        handleOccupationChange(value) {
            this.form.occupation = value; // 直接将value赋值给form.occupation
            console.log('Occupation changed to ' + value);
        },
        checkRole() {
            const roleid = localStorage.getItem('roleid');
            this.isDisabled = roleid !== '-1';
        }
    }
};
</script>