<template>
  <div>
    <section class="main">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-rank"></i> 项目管理</el-breadcrumb-item>
                <el-breadcrumb-item>项目需求</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="plugins-tips">
                <div>项目标题：{{title}}</div>
                <div>项目内容：{{content}}</div>
                <div>项目结束限制时间：{{formatDate(endtime)}}</div>
              <el-button
                  type="primary"
                  icon="el-icon-star-on"
                  :disabled="!canRateProject"
                  @click="rateHandler"
              >评分</el-button>
            </div>
            <div class="drag-box">
                <div class="drag-box-item">
                    <div class="item-title">未完成</div>
                    <draggable v-model="todo" @remove="removeHandle" :options="dragOptions">
                        <transition-group tag="div" id="todo" class="item-ul">
                            <div v-for="item in todo" class="drag-list" :data-abs="item.id" :key="item.id">
                                {{item.content}}
                            </div>
                        </transition-group>
                    </draggable>
                </div>
                <div class="drag-box-item">
                    <div class="item-title">已实现</div>
                    <draggable v-model="doing" @remove="removeHandle" :options="dragOptions">
                        <transition-group tag="div" id="doing" class="item-ul">
                            <div v-for="item in doing" class="drag-list" :data-abs="item.id" :key="item.id">
                                {{item.content}}
                            </div>
                        </transition-group>
                    </draggable>
                </div>
                <div class="drag-box-item">
                    <div class="item-title">已完成</div>
<!--                    <draggable v-model="done" @remove="removeHandle" :options="dragOptions">-->
                        <transition-group tag="div" id="done" class="item-ul">
                            <div v-for="item in done" class="drag-list" :data-abs="item.id" :key="item.id">
                                {{item.content}}
                            </div>
                        </transition-group>
<!--                    </draggable>-->
                </div>
            </div>
        </div>
    </section>

    <el-dialog title="评分" :visible.sync="rateVisible" width="30%">
      <el-rate v-model="rating" show-text></el-rate>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rateVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRating">提交评分</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
    import draggable from 'vuedraggable'
    export default {
        name: 'draglist',
        data() {
            return {
                rateVisible: false,
                rating: 0,
                dragOptions:{
                    animation: 120,
                    scroll: true,
                    group: 'sortlist',
                    ghostClass: 'ghost-style'
                },
                todo: [
                    {
                        id: '1',
                        content: '需求1'
                    },
                ],
                doing: [
                ],
                done:[
                ],
              title: '项目标题',
              content: '项目需求',
              endtime: '2025-01-01',
              projectDetails: {} // 添加 projectDetails 对象
            }
        },
        components:{
            draggable
        },

        created() {
          // 从URL中提取项目ID

          const hash = window.location.hash;

          // 去掉开头的 '#'
          const hashWithoutHash = hash.substring(1);

          // 创建 URL 对象
          const url = new URL(hashWithoutHash, this.Api);

          // 使用 URLSearchParams 解析查询参数
          const searchParams = new URLSearchParams(url.search);

          // 获取 id 参数
          this.projectId = searchParams.get('id');

          this.getProjectDetails();
        },
        methods: {
            getProjectDetails(){

                const hash = window.location.hash;

                // 去掉开头的 '#'
                const hashWithoutHash = hash.substring(1);

                // 创建 URL 对象
                const url = new URL(hashWithoutHash, this.Api);

                // 使用 URLSearchParams 解析查询参数
                const searchParams = new URLSearchParams(url.search);

                // 获取 id 参数
                this.projectId = searchParams.get('id');

                this.$axios
                    .post(this.Api + 'User/projectRequire', {
                        'id': this.projectId
                    }, {
                        headers: {
                            'token': localStorage.getItem('token')
                        }
                    })
                    .then(res => {
                        if (res.data.code == 200) {
                            this.todo = res.data.data.todo;
                            this.doing = res.data.data.doing;
                            this.done = res.data.data.done;
                            this.title = res.data.data.title;
                            this.content = res.data.data.content;
                            this.endtime = res.data.data.endtime;
                            this.projectDetails = res.data.data; // 将项目详情赋值给 projectDetails
                        }
                    }, error => {
                          this.$message.error('请重新登录');
                          this.$router.push('/login');
                        }
                    );
            },
            formatDate(timestamp) {
              //将timestamp 转换为 int类型
              timestamp = parseInt(timestamp);
              // 检查 timestamp 是否为有效数字
              if (typeof timestamp === 'number' && !isNaN(timestamp)) {
                // 如果时间戳是以秒为单位的，乘以 1000 转换为毫秒
                if (timestamp < 1e10) { // 假设时间戳小于 10 亿的是以秒为单位的
                  timestamp *= 1000;
                }
                const date = new Date(timestamp);
                return date.toLocaleString(); // 或者使用自定义格式化方法
              } else {
                // 返回默认值或空字符串
                return "Invalid Date";
              }
            },
          rateHandler() {
            if (this.canRateProject()) {
              this.rateVisible = true;
            } else {
              this.$message.error('项目尚未完成，无法评分');
            }
          },
          canRateProject() {
            return this.projectDetails.state === 9; // 确保 projectDetails 已经被正确赋值
          },
          submitRating() {
            this.$axios
                .post(this.Api + 'User/rateHandler', {
                  'id': this.projectId, // 假设你有一个 projectId
                  'rating': this.rating
                }, {
                  headers: {
                    'token': localStorage.getItem('token')
                  }
                })
                .then(res => {
                  if (res.data.code == 200) {
                    this.$message.success('评分成功');
                    this.rateVisible = false;
                  } else {
                    this.$message.error(res.data.msg);
                  }
                })
                .catch(error => {
                  this.$message.error('评分失败');
                });
          },
            removeHandle(event) {
                //console.log('Event item:', event.item); // 添加调试信息
                let status;
                if (event.to.id === 'todo') {
                    status = 0;
                } else if (event.to.id === 'doing') {
                    status = 1;
                } else if (event.to.id === 'done') {
                    status = 2;
                }

                // 确保 event.item 包含正确的 id 属性
                //const itemId = event.item.id;
                const itemId = event.item.getAttribute('data-abs'); // 获取 abs 属性

                if (itemId) { // 添加检查，确保 itemId 存在
                    this.$axios
                        .post(this.Api + 'User/editRequire', {
                            'id': itemId,
                            'state': status,
                            'proid': this.projectId,
                        }, {
                            headers: {
                                'token': localStorage.getItem('token')
                            }
                        })
                        .then(res => {
                            if (res.data.code == 200) {
                                this.$message.success(`更新成功！`);
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        });
                } else {
                    console.error('Item ID is missing:', event.item); // 添加错误信息
                }
            }
        }
    }

</script>

<style scoped>
    .drag-box{
        display: flex;
        user-select: none;
    }
    .drag-box-item {
        flex: 1;
        max-width: 330px;
        min-width: 300px;
        background-color: #eff1f5;
        margin-right: 16px;
        border-radius: 6px;
        border: 1px #e1e4e8 solid;
    }
    .item-title{
        padding: 8px 8px 8px 12px;
        font-size: 14px;
        line-height: 1.5;
        color: #24292e;
        font-weight: 600;
    }
    .item-ul{
        padding: 0 8px 8px;
        height: 500px;
        overflow-y: scroll;
    }
    .item-ul::-webkit-scrollbar{
        width: 0;
    }
    .drag-list {
        border: 1px #e1e4e8 solid;
        padding: 10px;
        margin: 5px 0 10px;
        list-style: none;
        background-color: #fff;
        border-radius: 6px;
        cursor: pointer;
        -webkit-transition: border .3s ease-in;
        transition: border .3s ease-in;
    }
    .drag-list:hover {
        border: 1px solid #20a0ff;
    }
    .drag-title {
        font-weight: 400;
        line-height: 25px;
        margin: 10px 0;
        font-size: 22px;
        color: #1f2f3d;
    }
    .ghost-style{
        display: block;
        color: transparent;
        border-style: dashed
    }
</style>