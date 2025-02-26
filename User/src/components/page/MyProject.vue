<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 基础表格
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                >批量删除</el-button>
                <el-select v-model="query.address" placeholder="项目名" class="handle-select mr10">
                    <el-option key="1" label="项目名" value="项目名"></el-option>
                </el-select>
                <el-input v-model="query.title" placeholder="项目名" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="primary" v-if="roleid==1" icon="el-icon-circle-plus-outline" @click="handleadd">发布项目</el-button>
            </div>
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
                <el-table-column prop="title" label="项目名称"></el-table-column>
                <el-table-column label="项目预算">
                    <template slot-scope="scope">￥{{scope.row.money}}</template>
                </el-table-column>


                <el-table-column label="状态" align="center">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.state==0?'success':(scope.row.state==='-1'?'danger':'')"
                        >{{scope.row.state==0?'雇佣中':(scope.row.state==-1?'未开放':(scope.row.state==9?'已完成':'进行中'))}}</el-tag>
                    </template>
                </el-table-column>

              <el-table-column prop="addtime" label="项目发布时间">
                <template slot-scope="scope">{{ formatDate(scope.row.addtime) }}</template>
              </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row)"
                            v-if="scope.row.state==1||roleid==1||scope.row.state==9"
                        >项目管理</el-button>
                        <el-button
                            type="text"
                            icon="el-icon-delete"
                            class="red"
                            @click="handleDelete(scope.$index, scope.row)"
                            v-if="scope.row.state==0 && roleid==0"
                        >取消申请</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="query.pageIndex"
                    :page-size="query.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->

    </div>
</template>

<script>
import { fetchData } from '../../api/index';
export default {
    name: 'myproject',
    data() {
        return {
            query: {
                address: '',
                name: '',
                pageIndex: 1,
                pageSize: 10
            },
            tableData: [],
            multipleSelection: [],
            delList: [],
            editVisible: false,
            pageTotal: 0,
            form: {},
            idx: -1,
            id: -1,
            roleid: localStorage.getItem('roleid')
        };
    },
    created() {
      this.getData();
    },
    methods: {
      // 获取 easy-mock 的模拟数据
      getData() {
        //console.log(localStorage.getItem('token'));
        this.$axios
            .post(this.Api+'User/myProject', {
              'pageNum': this.query.pageIndex,
              'pageSize': this.query.pageSize,
              'keyword': this.query.title

            }, {
              headers: {
                'token':  localStorage.getItem('token')
              }
            })
            .then(res => {
                  if (res.data.code == 200) {
                    this.tableData=res.data.data.records;
                    this.pageTotal=res.data.data.total;


                  }
                },
                //判断如果报错就跳转到登录页
                error => {
                  this.$message.error('请重新登录');
                  this.$router.push('/login');
                }


            );


        /*fetchData(this.query).then(res => {
          console.log(res);
          this.tableData = res.list;
          this.pageTotal = res.pageTotal || 50;
        });*/
      },
      handleadd() {
        this.$router.push('/addproject');
      },
      // 触发搜索按钮
      handleSearch() {
        this.$set(this.query, 'pageIndex', 1);
        this.$set(this.query, 'title', this.query.title);

        this.getData();
      },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                  this.$axios
                      .post(this.Api+'User/outProject', {
                        'id': row.id
                      }, {
                        headers: {
                          'token':  localStorage.getItem('token')
                        }
                      })
                      .then(res => {
                        if (res.data.code == 200) {
                          this.$message.success('删除成功');
                          this.tableData.splice(index, 1);

                        }else{
                          this.$message.error('删除失败');
                        }
                      })

                })
                .catch(() => {});
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                str += this.multipleSelection[i].name + ' ';
            }
            this.$message.error(`删除了${str}`);
            this.multipleSelection = [];
        },
        // 编辑操作
        handleEdit(index, row) {
            this.idx = index;
            this.form = row;
            if (this.roleid == 0) {
              this.$router.push('/projectrequire?id='+ row.id);
            } else {
              this.$router.push('/agreelist?id=' + row.id);
            }
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

        // 分页导航
        handlePageChange(val) {
            this.$set(this.query, 'pageIndex', val);
            this.getData();
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
