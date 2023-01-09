#营销案例
营销案例是一个根据交易信息计算返现金额的案例。<br>
fiborule-test-market模块
* 1、判断交易时间是否在活动时间范围内；
* 2、判断当前客户是否报名该活动；
* 3、商户限制；
* 4、累计消费金额限制；
* 5、计算返现金额，并限制最大返现金额。

##组件开发
- - -
* ContextInitNode - 交易信息初始化节点，初始化参数
* DateJudgeNode - 时间判断节点，判断交易时间是否在活动时间范围内
* EnrollJudgeNode - 交易客户是否报名该活动判断
* MerchantJudgeNode - 商户判断，是否为对应的商户
* ConsumeAmountJudgeNode - 累计消费金额判断，可以放在redis中，判断累计金额是否达标，并累加金额
* CashBackNode - 根据返现百分比计算返现金额，并判断限额

##后台配置
- - -
```Java
fiborule.app=38
fiborule.server=192.168.1.38:18121
fiborule.scene-list[0].name=market
fiborule.scene-list[0].path=com.fibo.rule.test.market
```
##引擎流程图
- - -
![引擎流程图](marketDemoEngine.png)
##执行结果
- - -
参数-订单信息json
```json
{
  "bankTradeCode":"1000561650337995000",
  "cardNumber":"652352******7425",
  "cardProductName":"",
  "cardProductNumber":"",
  "customerNumber":"9888888888",
  "merchantCityCode":"330000",
  "merchantCode":"437034323252737",
  "merchantCountryCode":"103",
  "merchantName":"商户一",
  "tradeAmount":1000,
  "tradeDate":"2023-01-05",
  "tradePlaceCity":"330000",
  "tradePlaceProvince":"156"
}
```
执行结果-价格计算上下文json
```json
{
  "result": {
    "cashBack": {
      "cashBackAmount": 200,
      "cashbackPayType": 1
    },
    "hit": true
  },
  "tradeVo": {
    "bankTradeCode": "1000561650337995000",
    "cardNumber": "652352******7425",
    "cardProductName": "",
    "cardProductNumber": "",
    "customerNumber": "9888888888",
    "localTradeDate": 1672848000000,
    "merchantCityCode": "330000",
    "merchantCode": "437034323252737",
    "merchantCountryCode": "103",
    "merchantName": "商户一",
    "tradeAmount": 1000,
    "tradeDate": "2023-01-05",
    "tradePlaceCity": "330000",
    "tradePlaceProvince": "156"
  }
}
```
