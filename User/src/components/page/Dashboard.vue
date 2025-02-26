<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                      <img v-if="qq !== '0'" :src="`http://q1.qlogo.cn/g?b=qq&nk=${qq}&s=100`" class="user-avator" alt  />
                      <img v-else  src="../../assets/img/img.jpg" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{name}}</div>
                            <div>{{role}}</div>
                        </div>
                    </div>
                    <div class="user-info-list">
                        当前时间：
                        <span>{{ now }}</span>
                    </div>
                    <div class="user-info-list">
                        上次登录地点：
                        <span>河北</span>
                    </div>
                </el-card>
                <el-card shadow="hover" style="height:252px;">
                    <div slot="header" class="clearfix">
                        <span>我的项目进度</span>
                    </div>


                  <div v-for="res in project">
                    <div>{{ res.title }}</div>
                    <el-progress :percentage="res.state" color="#42b983"></el-progress>
                  </div>




                </el-card>
            </el-col>
            <el-col :span="16">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{ result.contactCount }}</div>
                                    <div>联系好友</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-lx-notice grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{result.waitCount}}</div>
                                    <div>待办进度</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                  <el-col :span="6">
                    <el-card shadow="hover" :body-style="{padding: '0px'}">
                      <div class="grid-content grid-con-4">
                        <i class="el-icon-lx-recharge grid-con-icon"></i>
                        <div class="grid-cont-right">
                          <div class="grid-num">{{result.money}}</div>
                          <div>账户余额</div>
                        </div>
                      </div>
                    </el-card>
                  </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-lx-goods grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{ result.projectCount }}</div>
                                    <div>项目数量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card shadow="hover" style="height:403px;">
                    <div slot="header" class="clearfix">
                        <span>待办进度</span>
                        <el-button style="float: right; padding: 3px 0" @click="toProject()" type="text">全部项目</el-button>
                    </div>
                    <el-table :show-header="false" :data="todoList" style="width:100%;">
                        <el-table-column width="40">
                          <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.state"></el-checkbox>
                          </template>
                        </el-table-column>
                        <el-table-column>
                            <template slot-scope="scope">
                                <div
                                    class="todo-item"
                                    :class="{'todo-item-del': scope.row.state}"
                                >{{scope.row.content}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column width="60">
                            <template>
                                <i class="el-icon-edit"></i>
                                <i class="el-icon-delete"></i>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>






    </div>
</template>

<script>
import Schart from 'vue-schart';
import bus from '../common/bus';
import axios from "axios";
export default {
    name: 'dashboard',
    data() {
        return {
          name: localStorage.getItem('ms_username'),
          todoList: [
            {
              title: '后台功能Api接口',
              status: false
            },
            {
              title: '用户的增删改查',
              status: false
            },
            {
              title: '美化UI',
              status: false
            }
          ],
          qq: localStorage.getItem('qq') || '0',
          result:{},
          project:[],
          now: new Date(new Date().getTime()).toLocaleTimeString(),//时间戳转换成时间
          //获取地址访问者的信息IP



        };
    },
  created() {
    this.getWeb();
    // 获取 URL 参数

    // 使用 window.location.search 获取查询参数
    const searchParams = new URLSearchParams(window.location.search);

    this.pid = searchParams.get('pid');
    this.sign = searchParams.get('sign');
    this.trade_no = searchParams.get('trade_no');
    this.out_trade_no = searchParams.get('out_trade_no');
    this.type = searchParams.get('type');
    this.money = searchParams.get('money');
    this.uname = searchParams.get('name');
    this.trade_status = searchParams.get('trade_status');
    this.sign_type = "MD5";

    // 检查是否存在 pid 和 sign 参数
    if (this.pid && this.sign) {
      axios // 使用正确的 axios 实例
          .get(this.Api + 'payment/notify', {
            params: {
              pid: this.pid,
              sign: this.sign,
              trade_no: this.trade_no,
              out_trade_no: this.out_trade_no,
              type: this.type,
              money: this.money,
              name: this.uname,
              trade_status: this.trade_status,
              sign_type: this.sign_type
            },
            headers: {
              token: localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('充值成功');
              window.location.href = '/#/dashboard';
            } else {
              this.$message.error('充值失败');
            }
          })
          .catch(error => {
            console.error('充值请求失败:', error); // 捕获并打印错误信息
            this.$message.error('充值请求失败，请稍后再试');
          });
    }
  },
    components: {
        Schart
    },
    computed: {
        role() {
          return (() => {
            switch (this.result.roleid) {
              case 0:
                return '自由工作者';
              case 1:
                return '雇主';
              case -2:
                return '待审核';
              default:
                return '游客';
            }
          })();

        }
    },
    // created() {
    //     this.handleListener();
    //     this.changeDate();
    // },
    // activated() {
    //     this.handleListener();
    // },
    // deactivated() {
    //     window.removeEventListener('resize', this.renderChart);
    //     bus.$off('collapse', this.handleBus);
    // },
    methods: {
        toProject() {
            this.$router.push('/myproject');
        },
        getWeb(){
          this.$axios
              .post(this.Api+'User/index', {}, {
                headers: {
                  'token':  localStorage.getItem('token')
                }
              })
              .then(res => {
                    if (res.data.code == 200) {
                      this.result=res.data.data;
                      this.todoList=res.data.data.projectrequireList;
                      this.project=res.data.data.projectList;
                    }
                  },
                  //判断如果报错就跳转到登录页
                  error => {
                    this.$message.error('请重新登录');
                    this.$router.push('/login');
                  }


              );
        }
        // handleListener() {
        //     bus.$on('collapse', this.handleBus);
        //     // 调用renderChart方法对图表进行重新渲染
        //     window.addEventListener('resize', this.renderChart);
        // },
        // handleBus(msg) {
        //     setTimeout(() => {
        //         this.renderChart();
        //     }, 200);
        // },
        // renderChart() {
        //     this.$refs.bar.renderChart();
        //     this.$refs.line.renderChart();
        // }
    }
};
</script>


<style scoped>
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}
.grid-con-4 .grid-con-icon {
  background: rgb(136, 196, 55);
}

.grid-con-4 .grid-num {
  color: rgb(136, 196, 55);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart {
    width: 100%;
    height: 300px;
}
</style>
