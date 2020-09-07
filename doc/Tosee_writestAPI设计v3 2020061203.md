[TOC]

# API

测试用域名：[https://toseewritest.mynatapp.cc](https://toseewritest.mynatapp.cc/)

请求前缀：https://toseewritest.mynatapp.cc/toseewritest

Request示例：

https://toseewritest.mynatapp.cc/toseewritest/position/list

https://toseewritest.mynatapp.cc//toseewritest/wechat/auth?code=123456

仅当侯俊杰电脑打开内网穿透并运行Web程序时可用



## 打卡每日一练篇

### 查看打卡状态✅ 

```http
GET /writester/punch/getstate
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "punchState": 2,
        "completeNumber": 0,
        "correctRatio": 0,
        "exerciseTime": "13分钟",
        "insistDays": 1
    }
}
```

### 打卡✅ 

打卡的时候直接调用该接口，只要code为0就说明打卡成功了。

如果未达到打卡标准（punchState不为1），打卡不会成功

调用前请务必调用获取授权状态来查看用户是否授权。



```http
GET /writester/punch/punchclock
```

参数

```json
openid:on62f4kYUBt-AvuY11BcNNsCE4Ko
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "isFirstTimePunch": 0, // 若第一次打卡这个为1，若是重复打卡则这个为0
        "punchState": 2,
        "profilePhoto": "http://pic1.zhimg.com/50/v2-fce4f8a778fe3f24bca2cafc709b6847_hd.jpg",
      	"userName":"惊天动地阳光大男孩",
        "completeNumber": 0,
        "correctRatio": 0,
        "exerciseTime": "13分钟",
        "insistDays": 1
    }
}
或：

{
    "code": 2,
    "msg": "用户未达到打卡标准"
}
```

### 设置打卡状态✅

打卡状态不应该由前端决定，当后端拿到complete为1的record且当前状态为未打卡时，将自动改变打卡状态为待打卡。

<font color = "red">该接口只供前端对接调试阶段使用，不建议在生产环境中使用</font>

```http
GET /writester/punch/setstate
```

参数

```json
openid:on62f4kYUBt-AvuY11BcNNsCE4Ko
punchState: 1
```

返回

略 code为0即为成功

## 我的信息篇

### 登录✅

在调用wx.login()拿到code之后，以该code为参数向我们的服务器发出请求以换取openid

```http
GET /wechat/auth
```

参数

```
code: 2133123123
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
      "openid":"on62f4kYUBt-AvuY11BcNNsCE4Ko"
    }
}
```

### 保存用户信息✅

```http
POST /wechat/saveinfo
```

参数

```json
openid:on62f4kYUBt-AvuY11BcNNsCE4Ko
wechatId: houjunjie1015// 微信号选填
userName: 惊天动地阳光大男孩// 昵称
gender: 1 //性别1男2女3其他
profilePhotoUrl: http://pic1.zhimg.com/50/v2-fce4f8a778fe3f24bca2cafc709b6847_hd.jpg
eduDegree: 1//1国内本科2海外本科3硕士研究生4博士研究生5其他
university: 北京邮电大学// 大学名
graduationTime: 2020年9月//毕业时间
targetFields:[1,3] // 目标领域，数组
targetPositions:[1,3,4] // 目标岗位，数组
```

返回

```json
{
    "code": 0,
    "msg": "成功"
}
```

### 获取授权状态✅等待对接

只含有authorized字段

```http
GET /wechat/getauthstate
```

参数

```
openid:osL8-5WbE-o4K8zcurUf6HRRnf54
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "authorised": 1
    }
}
```

### 获取用户简要信息✅

```http
GET /wechat/getminiinfo
```

参数

```
openid:osL8-5WbE-o4K8zcurUf6HRRnf54
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "profilePhotoUrl": "http://pic1.zhimg.com/50/v2-fce4f8a778fe3f24bca2cafc709b6847_hd.jpg",
        "userName": "惊天动地阳光大男孩",
        "authorised": 1,
        "targetFields": [
            "互联网",
            "快消"
        ],
        "targetPositions": [
            "产品",
            "设计",
            "市场"
        ]
    }
}
```

### 获取用户信息✅

```http
GET /wechat/getinfo
```

参数

```
openid:osL8-5WbE-o4K8zcurUf6HRRnf54
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "wechatId": "houjunjie1015",
        "userName": "惊天动地阳光大男孩",
        "gender": "男",
        "profilePhotoUrl": "http://pic1.zhimg.com/50/v2-fce4f8a778fe3f24bca2cafc709b6847_hd.jpg",
        "authorised": 1,
        "eduDegree": "硕士研究生",
        "university": "北京邮电大学",
        "major": "计算机技术",
        "graduationTime": "2018/06",
        "targetFields": [
            {
                "fieldType": 1,
                "fieldName": "互联网"
            },
            {
                "fieldType": 3,
                "fieldName": "快消"
            }
        ],
        "targetPositions": [
            {
                "positionType": 1,
                "positionName": "产品"
            },
            {
                "positionType": 3,
                "positionName": "设计"
            },
            {
                "positionType": 4,
                "positionName": "市场"
            }
        ]
    }
}
```

以下这俩接口估计以后还得做二级联动，要废弃

### 行业列表✅

按照前端要求，data里统一为name和type

```http
GET /writester/position/fieldlist
```

参数

```
无
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "fieldType": 1,
            "fieldName": "互联网"
        },
        {
            "fieldType": 2,
            "fieldName": "地产"
        },
        {
            "fieldType": 3,
            "fieldName": "快消"
        },
        {
            "fieldType": 4,
            "fieldName": "金融"
        }
    ]
}
```

### 岗位列表✅

按照前端要求，data里统一为name和type

```http
GET /writester/position/positionlist
```

参数

```json
type: 2  //0企业真题，1专业知识，2为“我的”专用
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "positionType": 1,
            "positionName": "产品"
        },
        {
            "positionType": 2,
            "positionName": "运营"
        },
        {
            "positionType": 3,
            "positionName": "设计"
        },
        {
            "positionType": 4,
            "positionName": "市场"
        },
				...
    ]
}
```

### 意见反馈✅ 待对接

```http
POST /wechat/feedback
```

参数

```json
openid:osL8-5WbE-o4K8zcurUf6HRRnf54
content:阿巴巴
```

返回

```json
{
    "code": 0,
    "msg": "成功"
}
```

## 题库篇

### 首页搜索✅

```http
GET /writester/questionbank/searchqblist
```

参数

```json
openid: 'osL8-5Wd-xYNiASJVjv59tDB-JzA'
search: '运营'
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "childQbId": "15903194011801799949",
            "questionNumber": 7,
            "spentTime": 0,
            "simulationTime": 30,
            "complete": 0,
            "completeNumber": 0,
            "lastMode": 0,
            "collectState": 0,
            "title": "阿里文娱2019校招真题-问答题集锦",
            "heat": 1245,
            "correct": -1
        },
        {
            "childQbId": "15903198933031200349",
            "questionNumber": 1,
            "spentTime": 0,
            "simulationTime": 20,
            "complete": 0,
            "completeNumber": 0,
            "lastMode": 0,
            "collectState": 0,
            "title": "阿里巴巴运营岗2017校招真题-问答题集锦",
            "heat": 235,
            "correct": -1
        },
        {
            "childQbId": "15903420514021110055",
            "questionNumber": 6,
            "spentTime": 0,
            "simulationTime": 60,
            "complete": 0,
            "completeNumber": 0,
            "lastMode": 0,
            "collectState": 0,
            "title": "腾讯产品运营/策划2019校招真题",
            "heat": 666,
            "correct": -1
        },
        {
            "childQbId": "15903421065521039213",
            "questionNumber": 5,
            "spentTime": 0,
            "simulationTime": 50,
            "complete": 0,
            "completeNumber": 0,
            "lastMode": 0,
            "collectState": 0,
            "title": "腾讯游戏运营2019校招真题",
            "heat": 1316,
            "correct": -1
        }
    ]
}
```

### 热点题库✅

首页热点题库，选取数据库中热度最高的6套子题库

```http
GET /writester/questionbank/hotpointcqblist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "childQbId":"123457",
          	"questionNumber":30,
            "title": "主页热点题库1",
						"heat": 800,
          	"complete":0,//上次已完成的话是1
          	"completeNumber": 5, // 直接传数字，做了几道题
          	"correct": 80,
            "collectState": 0, //未收藏
          	"lastMode":0 ,// 上次没做过题
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
        {
            "childQbId":"123458",
          	"questionNumber":20,
            "title": "主页热点题库2",
						"heat": 800,
          	"complete":0,
          	"completeNumber": 80,
          	"correct": 80,
            "collectState": 1 ,//已收藏
          	"lastMode":1 ,// 上次模拟模式
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
      .. 一共六条
    ]
}
```

### 行业和职位列表✅

```http
GET /writester/position/list
```

参数

```json
type: 0  //0企业真题，1专业知识，2为“我的”专用
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "fieldType":"1",
            "fieldName": "互联网",
            "positions": [
                {
                    "positionType": "1",
                    "positonName": "产品经理",
                },
                {
                    "positionType": "2",
                    "positonName": "后台开发",
                }
            ]
        },
        {
            "fieldType":"2",
            "fieldName": "医疗",
            "positions": [
                {
                    "positionType": "3",
                    "positonName": "护士",
                },
                {
                    "positionType": "4",
                    "positonName": "行政",
                }
            ]
        }
    ]
}
```

Response_description:略

### 企业真题主题库列表✅

该请求返回一个父题库列表

```http
POST /writester/questionbank/enterpriseqblist
```

参数

```json
openid: osL8-5Wd-xYNiASJVjv59tDB-JzA
positionTypes:[2,3,4] // 岗位编号，最多传三个
collation: 1 // 0时间降序，1热度降序

//下面两个参数后期数据量大时做分页才用得到
page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "pqbId":"123457",
            "icon": "http://xxx.com",
						"title": "阿里巴巴后台真题一",
						"heat": 800,
            "cqbNumber":10
        },
        {
            "pqbId":"123458",
            "icon": "http://xxx.com",
						"title": "阿里巴巴后台真题二",
						"heat": 600,
            "cqbNumber":10
        }
    ]
}
```

Response_description:略



### 专项练习子题库列表✅

该请求根据用户发送的岗位编号，直接返回一个子题库列表

示例：https://toseewritest.mynatapp.cc/toseewritest/writester/questionbank/specialpractice?openid=osL8-5Wd-xYNiASJVjv59tDB-JzA&positionType=2

```http
GET /writester/questionbank/specialpractice
```

参数

```json
openid: osL8-5Wd-xYNiASJVjv59tDB-JzA
positionType: 3 // 岗位编号
collation: 1 // 0时间降序，1热度降序

//下面两个参数后期数据量大时做分页才用得到
page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "childQbId":"15881323384921812567",
          	"questionNumber":10,
            "title": "商业模式",
						"heat": 1000,
          	"complete":0,//上次已完成的话是1
          	"completeNumber": 0, // 直接传数字，上次做了几道题
          	"correct": -1,
            "collectState": 1, //未收藏
          	"lastMode":0,// 上次没做过题
          	"spentTime":0,
          	"simulationTime":80 //单位分钟
        },
        {
            "childQbId":"15881323384921812568",
          	"questionNumber":10,
            "title": "技术名词",
						"heat": 800,
          	"complete":0,//上次已完成的话是1
          	"completeNumber": 2,
          	"correct": -1,
            "collectState": 0 ,//已收藏
          	"lastMode":1 ,// 上次模拟模式
          	"spentTime":300,
          	"simulationTime":80 //单位分钟
        },
    ]
}
```

Response_description:略

### 行测练习主、子题库列表✅

在行测练习界面，我需要把主父题库列表一并发给你

示例：https://toseewritest.mynatapp.cc/toseewritest/writester/questionbank/administrativeqblist?openid=osL8-5Wd-xYNiASJVjv59tDB-JzA

```http
GET /writester/questionbank/administrativeqblist
```

参数

```json
openid: osL8-5Wd-xYNiASJVjv59tDB-JzA
collation: 1 // 0时间降序，1热度降序

//下面两个参数后期数据量大时做分页才用得到
page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

```json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "pqbId":"15881323384921812857",
						"title": "数量关系",
						"heat": 800,
            "cqbNumber":10,
          	"cqbList":[
            {
              "childQbId":"15881323384921812897",
              "questionNumber":10,
              "title": "数量关系子题库1",
              "heat": 1000,
              "complete":0,//上次已完成的话是1
              "completeNumber": 0, // 直接传数字，上次做了几道题
              "correct": -1,
              "collectState": 1, //未收藏
              "lastMode":0,// 上次没做过题
              "spentTime":0,
              "simulationTime":80 //单位分钟
        		},
            {
                "childQbId":"15881323384921812898",
                "questionNumber":10,
                "title": "数量关系子题库2",
                "heat": 800,
             		"complete":0,//上次已完成的话是1
                "completeNumber": 2,
                "correct": -1,
                "collectState": 0 ,
                "lastMode":1 ,// 上次模拟模式
                "spentTime":300,
                "simulationTime":80 //单位分钟
            },
          ]
        },
        {
            "pqbId":"15881323384921812858",
						"title": "常识判断",
						"heat": 600,
            "cqbNumber":10,
            "cqbList":[
              {
                "childQbId":"1588132338492181292",
                "questionNumber":10,
                "title": "常识判断子题库1",
                "heat": 1000,
                "complete":0,//上次已完成的话是1
                "completeNumber": 0, // 直接传数字，上次做了几道题
                "correct": -1,
                "collectState": 1, //未收藏
                "lastMode":0,// 上次没做过题
                "spentTime":0,
                "simulationTime":80 //单位分钟
              },
              {
                  "childQbId":"15881323384921819298",
                  "questionNumber":10,
                  "title": "常识判断子题库2",
                  "heat": 800,
                	"complete":0,//上次已完成的话是1
                  "completeNumber": 2,
                  "correct": -1,
                  "collectState": 0 ,
                  "lastMode":1 ,// 上次模拟模式
                  "spentTime":300,
                  "simulationTime":80 //单位分钟
              },
            ]
        }
    ]
}

```

### 子题库列表✅

示例URL：

https://toseewritest.mynatapp.cc/toseewritest/writester/questionbank/childqblist?openid=osL8-5Wd-xYNiASJVjv59tDB-JzA&pqbId=15881323384921812445&collation=1

点开企业真题中某个父题库后子题库列表界面

```
GET /writester/questionbank/childqblist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
pqbId: 15881323384921812445 //父题库id
collation: 1 // 0时间降序，1热度降序

//下面两个参数后期数据量大时做分页才用得到
page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
          //名称 热度 题目数量 已做百分比 正确率
            "childQbId":"123457",
          	"questionNumber":30,
            "title": "阿里巴巴2020校招游戏策划类",
          	"complete":0,//上次已完成的话是1
						"heat": 800,
          	"completeNumber": 5, // 直接传数字，做了几道题
          	"correct": 80,
            "collectState": 0, //未收藏
          	"lastMode":0 ,// 上次没做过题
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
        {
            "childQbId":"123457",
          	"questionNumber":20,
            "title": "阿里巴巴2020校招游戏策划类",
						"heat": 800,
          	"complete":0,//上次已完成的话是1
          	"completeNumber": 80,
          	"correct": 80,
            "collectState": 1 ,//已收藏
          	"lastMode":1 ,// 上次模拟模式
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
    ]
}
```

Response_description:略



### 题目列表✅

<font color = 'red'>练习模式or模拟模式的返回没有区别，这样的Response涵盖了两种模式所需的所有信息，客户端随便选用即可；</font>

当用户是中途退出再进入时，每个题目里多一个参数叫做userAnswer；

示例URL：

https://toseewritest.mynatapp.cc/toseewritest/writester/questionbank/questionslist?openid=osL8-5Wd-xYNiASJVjv59tDB-JzA&cqbId=15881323384921812123

```
GET /writester/questionbank/questionslist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
cqbId: 15881323384921812123 //子题库id
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": {
      [
        {
            "quesitonId": "123457",
          	"quesitonSeq": 1, //题号
        		"userAnswer":"A",// 用户中途退出时，这套题的选择
          	"questionType": 1, //题型,1单选题2多选题3问答题
            "questionStem": "（1）进行良好的时间管理，同样需要这些品质（2）把握好间隔和规律正是时间管理的内容（3）当鸡蛋越来越多， 情况越来越糟的时候，你要有能力控制局面（4）进行时间管理，就好像抛鸡蛋（5）要做好抛鸡蛋的动作，需要耐心、毅力、练习和计划（6）你要不停地把手中的一只鸡蛋换成另一只，还要保持所有的鸡蛋都不落到地上将以上 6 个句子重新排序，语序正确的是（ ）",
						"questionOptions": [
              {
                "optionName":"A",
                "optionValue":"（2）（4）（3）（6）（5）（1）"
              },
              {
                "optionName":"B",
                "optionValue":"（5）（1）（6）（4）（3）（2）"
              },
              {
                "optionName":"C",
                "optionValue":"（2）（4）（5）（6）（1）（3"
              },
              {
                "optionName":"D",
                "optionValue":"（4）（6）（5）（1）（3）（2）"
              }
            ] ,
          	"answer": "D",
          	"explanation": "首先观察四个选项的分布，根据少数服从多数原则，初定（2）为首句，而观察四个选项的尾句可看出（2）同样是多数项，因此需要根据首尾句的特点来判断（2）适合做句首还是句尾。观察（2）“正是”属于结论性的表述，不适合做首句，排除 AC 选项；对比（5）和（4），（4）将时间管理比喻为抛鸡蛋的话题，（5）则是对如何做好抛鸡蛋动作的具体描述，因此（4）应在（5）前。故初定选项为 D。将 D 项进行关联性验证，先由（4）引出时间管理像抛鸡蛋的话题，（6）对抛鸡蛋动作进行描述，（5）提出要做好抛鸡蛋这个动作需要具备三种品质，（1）指出良好的时间管理同样需要具备这三种品质，这是（3）中面对糟糕局面进行控制的能力，（2）进行总结。通过验证可知 D 正确，因此正确答案为 D 选项",
            "collectState": 0 //未收藏
          },
      
          {
            "quesitonId": "1234568",
          	"quesitonSeq": 2,
            "userAnswer":"B",// 用户中途退出时，这套题的选择
          	"questionType": 1, //题型,1单选题2多选题3问答题
            "questionStem": "1，2，6，30，210，（ ）",
						"questionOptions": [
              {
                "optionName":"A",
                "optionValue":"1890"
              },
              {
                "optionName":"B",
                "optionValue":"2310"
              },
              {
                "optionName":"C",
                "optionValue":"2520"
              },
              {
                "optionName":"D",
                "optionValue":"2730"
              }
            ],
            
          	"answer": "B",
          	"explanation": "本题规律为后一项除以前一项，得到新的质数数列 2、3、5、7、（），所以（）=11，答案为 210×11。因此，本题答案选择 B 选项。",
            "collectState": 1 //已收藏
          }
      ]
}
```

Response_description:略

### 错题解析or全部解析✅

示例：

https://toseewritest.mynatapp.cc/toseewritest/writester/questionbank/analysislist?openid=osL8-5Wd-xYNiASJVjv59tDB-JzA&cqbId=15881323384921812123&mistaken=1

```http
GET /writester/questionbank/analysislist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
cqbId: 15881323384921812123 //子题库id
mistaken:1 // 当mistaken为1的时候是错题解析，0的时候是全部解析
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
      {
            "quesitonId": "123457",
          	"quesitonSeq": 1, //题号
        		"userAnswer":"A",// 用户中途退出时，这套题的选择
          	"questionType": 1, //题型,1单选题2多选题3问答题
            "questionStem": "（1）进行良好的时间管理，同样需要这些品质（2）把握好间隔和规律正是时间管理的内容（3）当鸡蛋越来越多， 情况越来越糟的时候，你要有能力控制局面（4）进行时间管理，就好像抛鸡蛋（5）要做好抛鸡蛋的动作，需要耐心、毅力、练习和计划（6）你要不停地把手中的一只鸡蛋换成另一只，还要保持所有的鸡蛋都不落到地上将以上 6 个句子重新排序，语序正确的是（ ）",
						"questionOptions": [
              {
                "optionName":"A",
                "optionValue":"（2）（4）（3）（6）（5）（1）"
              },
              {
                "optionName":"B",
                "optionValue":"（5）（1）（6）（4）（3）（2）"
              },
              {
                "optionName":"C",
                "optionValue":"（2）（4）（5）（6）（1）（3"
              },
              {
                "optionName":"D",
                "optionValue":"（4）（6）（5）（1）（3）（2）"
              }
            ] ,
          	"answer": "D",
          	"explanation": "首先观察四个选项的分布，根据少数服从多数原则，初定（2）为首句，而观察四个选项的尾句可看出（2）同样是多数项，因此需要根据首尾句的特点来判断（2）适合做句首还是句尾。观察（2）“正是”属于结论性的表述，不适合做首句，排除 AC 选项；对比（5）和（4），（4）将时间管理比喻为抛鸡蛋的话题，（5）则是对如何做好抛鸡蛋动作的具体描述，因此（4）应在（5）前。故初定选项为 D。将 D 项进行关联性验证，先由（4）引出时间管理像抛鸡蛋的话题，（6）对抛鸡蛋动作进行描述，（5）提出要做好抛鸡蛋这个动作需要具备三种品质，（1）指出良好的时间管理同样需要具备这三种品质，这是（3）中面对糟糕局面进行控制的能力，（2）进行总结。通过验证可知 D 正确，因此正确答案为 D 选项",
            "collectState": 0 ,//未收藏
        		"isInMistake": 0,
          },
      
          {
            "quesitonId": "1234568",
          	"quesitonSeq": 2,
            "userAnswer":"B",// 用户中途退出时，这套题的选择
          	"questionType": 1, //题型,1单选题2多选题3问答题
            "questionStem": "1，2，6，30，210，（ ）",
						"questionOptions": [
              {
                "optionName":"A",
                "optionValue":"1890"
              },
              {
                "optionName":"B",
                "optionValue":"2310"
              },
              {
                "optionName":"C",
                "optionValue":"2520"
              },
              {
                "optionName":"D",
                "optionValue":"2730"
              }
            ],
            
          	"answer": "B",
          	"explanation": "本题规律为后一项除以前一项，得到新的质数数列 2、3、5、7、（），所以（）=11，答案为 210×11。因此，本题答案选择 B 选项。",
            "collectState": 1, //已收藏
            "isInMistake": 1
          }
        ]
}
```

### 中途退出子题库or完成题库✅ 新参数thisTimeSpent

示例URL：

```
POST /writester/questionbank/record
```

**参数**

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
cqbId: 15881323384921812123 //子题库id
complete: 0 // 0为未完成 1为已完成
completeNumber: 2 //已做完的题目个数
thisTimeSpent: 100// 这次实际消耗的时间
spentTime:2000 //以秒为单位
userAnswerList:["A","B","","D"...,"作答题答案"]//用户的作答
mode: 1 //1模拟模式 2练习模式

correct:30 //complete为1时，前端传回这个数据，中途退出时不传该字段
```

后端会根据根据你的complete字段判断你的完成度信息，不用区分**中途退出**和**完成**两个API；

**返回**

``` json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "surpassRatio": 100
    }
}
```

### 获取作答报告✅

示例URL：

```
POST /writester/questionbank/report
```

**参数**

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
cqbId: 15881323384921812123 //子题库id
```

**返回**

``` json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "childQbTitle": "腾讯2019市场职能秋招",
        "correctRatio": 30,
        "spentTime": 300,
        "completeNumber": 3,
        "questionNumber": 6,
        "surpassRatio": 0,
        "answerList": [
            "D",
            "B",
            "B",
            "A",
            "我是TEST问答题的答案",
            "AB"
        ],
        "userAnswerList": [
            "A",
            "B",
            "B"
        ],
        "lastMode": 1
    }
}
```

## 收藏篇

### 收藏✅

点开企业真题中某个父题库后子题库列表界面

```
POST /writester/collect/add
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
type: 2 //收藏类型, 1子题库2题目3文章
targetId: 15882518102231236476
```

返回

``` json
{
    "code": 0,
    "msg": "成功"
}
```

Response_description:略



### 取消收藏✅

```
POST /writester/collect/cancel
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko //用户的openid
type: 2 //收藏类型, 1子题库2题目3文章
targetId: 15882518102231236476
```

返回

``` json
{
    "code": 0,
    "msg": "成功"
}
```

Response_description:略





## 复习篇

### 首页推荐列表✅

我先暂时把编辑推荐位的子题库列表发给你；

按理说处于这个推荐列表中的题库应该满足：

- 用户做过
- 推荐位

但现在是只显示运营推荐子题库列表（这个推荐是全局的，而不是根据openid客制化的）

```http
GET /writester/review/recommendlist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko

//下面两个参数后期数据量大时做分页才用得到
page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "childQbId":"123457",
          	"questionNumber":30,
            "title": "复习推荐题库1",
						"heat": 800,
          	"complete":0,//上次已完成的话是1
          	"completeNumber": 5, // 直接传数字，做了几道题
          	"correct": 80,
            "collectState": 0, //未收藏
          	"lastMode":0 ,// 上次没做过题
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
        {
            "childQbId":"123458",
          	"questionNumber":20,
            "title": "复习推荐题库2",
						"heat": 800,
          	"complete":0,
          	"completeNumber": 80,
          	"correct": 80,
            "collectState": 1 ,//已收藏
          	"lastMode":1 ,// 上次模拟模式
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
    ]
}
```

### 收藏夹列表✅

```http
GET /writester/review/collectlist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
type: 2 //收藏类型, 1子题库2题目3文章

page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

当type为1时，返回收藏子题库列表（要求从这个界面直接可以进入练习的）：

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "childQbId":"123457",
          	"questionNumber":30,
            "title": "收藏子题库1",
						"heat": 800,
          	"completeNumber": 5, // 直接传数字，做了几道题
          	"correct": 80,
            "collectState": 0, //未收藏
          	"lastMode":0 ,// 上次没做过题
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
        {
            "childQbId":"123457",
          	"questionNumber":20,
            "title": "收藏子题库2",
						"heat": 800,
          	"completeNumber": 80,
          	"correct": 80,
            "collectState": 1 ,//已收藏
          	"lastMode":1 ,// 上次模拟模式
          	"spentTime":30,
          	"simulationTime":80 //单位分钟
        },
    ]
}
```

当type为2时，返回收藏题目列表（仍以子题库列表形式呈现）：

（和错题本一样的虚拟子题库设计）

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "collectBookId":"123457",
          	"collectNumber":6, //本子题库中你收藏过的题目数量
            "title": "收藏题目虚拟子题库1",
          	"updateTime": "2022/4/23" // 子题库中最新收藏的题目的收藏时间
        },
        {
					...
        },
    ]
}
```

当type为3时，返回收藏文章列表：

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "articleId": "15898988952791222589",
            "isRecommended": 1,
            "author": "李童",
            "secondTag": "第二标签",
            "iconUrl": "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1109618770,2595587827&fm=11&gp=0.jpg",
            "title": "李童Google产品心得",
            "readNum": 212
        }	,
      ...
    ]
}
```



### 收藏夹题目练习✅

```http
GET /writester/review/docollectquestion
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
collectBookId:"123457"
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
			{
        {
            "collectQuestionId": "15895989496241410320",
            "collectQuestionSeq": 1,
            "questionId": "15881323384921812124",
            "questionType": 3,
            "questionStem": "TEST，问答题",
            "answer": "我是TEST问答题的答案",
            "explanation": "问答题测试用例",
            "collectState": 1
        },
        {
            "collectQuestionId": "15895993468541748387",
            "collectQuestionSeq": 2,
            "questionId": "15882183451931833108",
            "questionType": 1,
            "questionStem": "“80 后”这个词，最早于 2001 年出现在网络论坛中，指的是一批活跃于网络论坛的出生于 20 世纪 80 年代的诗人。2003 年开始，它更多指的是一批被商业运作出名的生于 1980 年以后的写手。2004 年底，随着“80 后作家”的批量涌现，这个词逐渐被用来指称整个 20 世纪 80 年代出生的年轻人群体。最适合做这段文字标题的是（ ）。",
            "answer": "B",
            "explanation": "文段脉络清晰，首先介绍了“80 后”这个词在 2001 年是何意，接着以时间为维度说明“80 后”在 2003 年、2004 年的词意变化，可以看出，文段主要阐述的是“80 后”的由来。因此，本题选择 B 选项",
            "collectState": 1,
            "questionOptions": [
                {
                    "optionName": "A",
                    "optionValue": "“充满希望”的一代"
                },
                {
                    "optionName": "B",
                    "optionValue": "“80 后”的由来"
                },
                {
                    "optionName": "C",
                    "optionValue": "用新视角理性看待“80 后”"
                },
                {
                    "optionName": "D",
                    "optionValue": "“80 后”引起社会的广泛关注"
                }
            ]
        }
    ]
}
```

### 错题本列表✅

```http
GET /writester/review/mistakebooklist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko

page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "mistakeBookId":"123457",
          	"mistakeNumber":6, //本子题库中你的错题数量
            "title": "错题本子题库1",
          	"updateTime": "2022/4/23", // 该错题本中最新做错的错题做错时间
        },
        {
					...
        },
    ]
}
```

Response_description:略

### 错题练习✅

mistakeId就像一个指针一样指向question

```http
GET /writester/review/domistake
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
mistakeBookId:"123457"
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
			{
            "mistakeId": "123457",
          	"mistakeSeq": 1, //错题题号
        		"questionId": "2131321", // 题目id供删除错题使用
          	"questionType": 1, //题型,1单选题2多选题3问答题
            "questionStem": "（1）进行良好的时间管理，同样需要这些品质（2）把握好间隔和规律正是时间管理的内容（3）当鸡蛋越来越多， 情况越来越糟的时候，你要有能力控制局面（4）进行时间管理，就好像抛鸡蛋（5）要做好抛鸡蛋的动作，需要耐心、毅力、练习和计划（6）你要不停地把手中的一只鸡蛋换成另一只，还要保持所有的鸡蛋都不落到地上将以上 6 个句子重新排序，语序正确的是（ ）",
						"questionOptions": [
              {
                "optionName":"A",
                "optionValue":"（2）（4）（3）（6）（5）（1）"
              },
              {
                "optionName":"B",
                "optionValue":"（5）（1）（6）（4）（3）（2）"
              },
              {
                "optionName":"C",
                "optionValue":"（2）（4）（5）（6）（1）（3"
              },
              {
                "optionName":"D",
                "optionValue":"（4）（6）（5）（1）（3）（2）"
              }
            ] ,
          	"answer": "D",
          	"explanation": "首先观察四个选项的分布，根据少数服从多数原则，初定（2）为首句，而观察四个选项的尾句可看出（2）同样是多数项，因此需要根据首尾句的特点来判断（2）适合做句首还是句尾。观察（2）“正是”属于结论性的表述，不适合做首句，排除 AC 选项；对比（5）和（4），（4）将时间管理比喻为抛鸡蛋的话题，（5）则是对如何做好抛鸡蛋动作的具体描述，因此（4）应在（5）前。故初定选项为 D。将 D 项进行关联性验证，先由（4）引出时间管理像抛鸡蛋的话题，（6）对抛鸡蛋动作进行描述，（5）提出要做好抛鸡蛋这个动作需要具备三种品质，（1）指出良好的时间管理同样需要具备这三种品质，这是（3）中面对糟糕局面进行控制的能力，（2）进行总结。通过验证可知 D 正确，因此正确答案为 D 选项",
            "collectState": 0 //未收藏
          },
      …………
    ]
}
```

### 错题删除✅

```http
POST /writester/review/deletemistake
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
questionId:"15887760531851311111"
```

返回

``` json
{
    "code": 0,
    "msg": "成功"
}
```



### 练习记录✅

```http
GET /writester/review/recordlist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko

page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "recordId":"15887760531851368911",// 练习记录ID
          	"childQbId":"15881323384921812123",// 子题库ID
          	"questionNumber":6, //子题库题目数量
            "title": "练习记录子题库1",
          	"updateTime": "2022/4/23", // 这条记录的更新时间
          	"complete":1,
          	"completeNumber":6, //完成数
          	"correct":80,
          	"lastMode":0, // 已完成的话lastMode一律传0
            "spentTime":30,
          	"simulationTime":80 //单位分钟
        },
        {
					...
        },
    ]
}
```

Response_description:略

### 练习记录删除✅

```http
POST /writester/review/recorddelete
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
recordId:"15887760531851368911"
```

返回

``` json
{
    "code": 0,
    "msg": "成功"
}
```



## 笔经干货篇

### 获取标签列表✅

用于标签筛选

```http
GET /writester/experience/taglist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "tagId":"1",
          	"tagName":"互联网"
        },
        {
						"tagId":"2",
          	"tagName":"行政"
        },
    ]
}
```

### 获取文章列表✅

```http
GET /writester/experience/articlelist
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
tagId:1  // 可不传，显示推荐列表
search:"搜索关键字" // 可为空，可不传

page: 0 //从第0页开始，该字段可不传，服务器端默认为第0页
size: 10 //每页所含条目大小，该字段可不传，服务器端默认size为10
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "articleId": "15881323384921812392",
            "isRecommended": 1,
            "author": "逍遥子",
            "tag": "互联网",
            "iconUrl": "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3211174755,200170773&fm=26&gp=0.jpg",
            "title": "阿里巴巴笔经干货",
            "readNum": 5000
        },
        {
            "articleId": "15887356685231820333",
            "isRecommended": 0,
            "author": "马云",
            "tag": "互联网",
            "iconUrl": "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3211174755,200170773&fm=26&gp=0.jpg",
            "title": "阿里巴巴笔经干货2",
            "readNum": 5000
        }
    ]
}
```

### 获取文章内容✅

```http
GET /writester/experience/articlecontent
```

参数

```json
openid: on62f4kYUBt-AvuY11BcNNsCE4Ko
articleId: 123457
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": {
        "articleId": "15881323384921812392",
        "isRecommended": 1,
        "author": "逍遥子",
        "collectState": 0,
        "content": "阿巴阿巴阿巴巴",
        "relaseTime": "2020-05-24",
        "title": "阿里巴巴笔经干货",
        "tag": "互联网",
        "readNum": 5000
    }
}
```

## 出校篇

### 表单提交

```http
POST /writester/pass/commitinfo
```

参数

```json
name: "王伟"
institute: "计算机学院（国家示范软件学院）"
state: "允许入校"
studentNumber: "2018211457"
time: "202009051200"
```

返回

``` json
{
    "code": 0,
    "msg": "成功",
    "data": {
    }
}
```
