<template>
  <div class="recharge-container">
    <div class="card">
      <h2 class="title">在线充值</h2>

      <!-- 金额输入 -->
      <div class="amount-section">
        <div class="input-group">
          <span class="prefix">¥</span>
          <el-input
              v-model="amount"
              type="number"
              placeholder="请输入充值金额"
              min="1"
              @input="validateAmount"
          />
        </div>
        <div class="quick-amount">
          <el-button
              v-for="num in quickAmounts"
              :key="num"
              @click="amount = num"
          >
            ¥{{ num }}
          </el-button>
        </div>
      </div>

      <!-- 支付方式 -->
      <div class="payment-methods">
        <div
            v-for="method in paymentMethods"
            :key="method.id"
            class="method-card"
            :class="{ active: selectedMethod === method.id }"
            @click="selectedMethod = method.id"
        >
          <div class="method-icon">
            <i :class="method.icon" />
          </div>
          <span class="method-name">{{ method.name }}</span>
        </div>
      </div>

      <!-- 提交按钮 -->
      <el-button
          type="success"
          class="submit-btn"
          :disabled="!isValid"
          @click="handleSubmit"
      >
        立即充值（¥{{ amount }}）
      </el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      amount: '',
      selectedMethod: null,
      quickAmounts: [10, 50, 100, 200, 500],
      paymentMethods: [
        { id: 'qqpay', name: 'QQ支付', icon: 'el-icon-coin' },
        { id: 'alipay', name: '支付宝', icon: 'el-icon-stopwatch' },
        { id: 'wxpay', name: '微信支付', icon: 'el-icon-chat-dot-round' }
      ]
    };
  },
  computed: {
    isValid() {
      return this.amount > 0 && this.selectedMethod;
    }
  },
  methods: {
    validateAmount() {
      if (this.amount < 0) {
        this.amount = 0;
      }
    },
    handleSubmit() {
      axios
          .post(this.Api + 'User/payment', {
            money: this.amount,
            type: this.selectedMethod,
            user: localStorage.getItem('ms_username')
          }, {
            headers: {
              token: localStorage.getItem('token')
            }
          })
          .then(res => {
            if (res.data.code === 200) {
              this.$message.success('获取成功，跳转支付Ing');
              //直接跳转到res.data.data
              window.location.href = res.data.data;

            } else {
              this.$message.error('获取失败');
            }
          });
    }
  },
  created() {
    // 获取 URL 参数

    const hash = window.location.hash;

    // 去掉开头的 '#'
    const hashWithoutHash = hash.substring(1);

    // 创建 URL 对象
    const url = new URL(hashWithoutHash, this.Api);

    // 使用 URLSearchParams 解析查询参数
    const searchParams = new URLSearchParams(url.search);

    console.log('searchParams:', searchParams); // 打印 searchParams 以进行调试

    this.pid = searchParams.get('pid');
    this.sign = searchParams.get('sign');
    this.trade_no = searchParams.get('trade_no');
    this.out_trade_no = searchParams.get('out_trade_no');
    this.type= searchParams.get('type');
    this.ucmoney= searchParams.get('money');
    this.ucname = searchParams.get('name');
    this.trade_status = searchParams.get('trade_status');
    this.sign_type= "MD5";

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
                  money: this.ucmoney,
                  name: this.ucname,
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
  }
};
</script>

<style scoped>
html, body {
  height: 100%;
  margin: 0;
  overflow: hidden; /* 禁止上下滑动 */
}

.recharge-container {
  min-height: 10vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  box-sizing: border-box; /* 确保内边距包含在高度计算中 */
}

.card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 2.5rem;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.title {
  text-align: center;
  color: #2d3748;
  margin-bottom: 2rem;
  font-family: 'Roboto', sans-serif;
}

.amount-section {
  margin-bottom: 2rem;
}

.amount-section .input-group {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.amount-section .input-group .prefix {
  font-size: 1.5rem;
  margin-right: 1rem;
  color: #4a5568;
}

.amount-section .input-group .el-input .el-input__inner {
  height: 50px;
  font-size: 1.5rem;
  padding-left: 1rem;
  border-radius: 8px;
}

.amount-section .quick-amount {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
  gap: 1rem;
}

.amount-section .quick-amount .el-button {
  padding: 0.8rem;
  transition: all 0.3s ease;
}

.amount-section .quick-amount .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.payment-methods {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.payment-methods .method-card {
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.payment-methods .method-card:hover {
  transform: translateY(-3px);
  border-color: #48bb78;
}

.payment-methods .method-card.active {
  border-color: #48bb78;
  background: rgba(72, 187, 120, 0.05);
}

.payment-methods .method-card .method-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.payment-methods .method-card .method-icon .icon-qq {
  color: #00a1d6;
}

.payment-methods .method-card .method-icon .icon-alipay {
  color: #009fe8;
}

.payment-methods .method-card .method-icon .icon-wechatpay {
  color: #07c160;
}

.payment-methods .method-card .method-name {
  font-weight: 500;
  color: #2d3748;
}

.submit-btn {
  width: 100%;
  height: 50px;
  font-size: 1.1rem;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(72, 187, 120, 0.4);
}

.submit-btn:disabled {
  opacity: 0.7;
  transform: none;
  box-shadow: none;
}
</style>