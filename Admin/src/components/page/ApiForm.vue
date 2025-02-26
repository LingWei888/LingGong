<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 系统管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>Api接口管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="form-box">
        <el-form ref="form" :model="form" label-width="80px">


          <el-form-item label="实名Api">
            <el-input v-model="form.api"></el-input>
          </el-form-item>
          <el-form-item label="实名Key">
            <el-input v-model="form.appcode"></el-input>
          </el-form-item>
          <el-form-item label="邮箱Smtp">
            <el-input v-model="form.smtp"></el-input>
          </el-form-item>
          <el-form-item label="邮箱port">
            <el-input v-model="form.port"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.mail"></el-input>
          </el-form-item>
          <el-form-item label="邮箱key">
            <el-input v-model="form.key"></el-input>
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
  name: 'apiform',
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
          .post(this.Api+'Admin/cxApi', {}, {
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
          .post(this.Api+'Admin/setApi', this.form, {
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