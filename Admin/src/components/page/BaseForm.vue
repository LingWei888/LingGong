<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-calendar"></i> 系统管理
                </el-breadcrumb-item>
                <el-breadcrumb-item>基本信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="form-box">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="标题">
                        <el-input v-model="form.title"></el-input>
                    </el-form-item>
                    <el-form-item label="网站名">
                      <el-input v-model="form.sitename"></el-input>
                    </el-form-item>
                    <el-form-item label="关键词">
                      <el-input type="textarea" rows="5" v-model="form.keywords"></el-input>
                    </el-form-item>
                    <el-form-item label="网站描述">
                      <el-input type="textarea" rows="5" v-model="form.description"></el-input>
                    </el-form-item>



                    <el-form-item label="公告">
                      <el-input type="textarea" rows="5" v-model="form.gonggao"></el-input>
                    </el-form-item>




                    <el-form-item>
                        <el-button type="primary" @click="onSubmit">修改信息</el-button>
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
                region: '',
                date1: '',
                date2: '',
                delivery: true,
                type: ['步步高'],
                resource: '小天才',
                desc: '',
                options: []
            }
        };
    },
    created() {
      this.getData();
    },
    methods: {
      getData() {
        this.$axios
            .post(this.Api+'Admin/cxConfig', {}, {
              headers: {
                'token':  localStorage.getItem('token')
              }
            })
            .then(res => {
                  if (res.data.code == 200) {
                    this.form=res.data.data;
                  }else{
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
            this.$axios
                .post(this.Api+'Admin/setConfig', this.form, {
                  headers: {
                    'token':  localStorage.getItem('token')
                  }
                })
                .then(res => {
                      if (res.data.code == 200) {
                        this.$message.success('修改成功');
                      }
                    },
                    //判断如果报错就跳转到登录页
                    error => {
                      this.$message.error('请重新登录');
                      localStorage.removeItem('token');
                      this.$router.push('/login');
                    }
                )
        }
    }
};
</script>