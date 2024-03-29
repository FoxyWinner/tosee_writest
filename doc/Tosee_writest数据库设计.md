[TOC]

# 兔希刷题数据库设计

## 一、用户篇

### 用户表✅

```mysql
create table `user`(
  
  /* 用户ID */

  `user_id` varchar(32) not null ,
  
  `openid` varchar(64) not null comment '微信openid，可依次向微信拉取用户微信头像和昵称',
  
  `wechat_id` varchar(32)  comment '微信号，选填',

  /* 用户基本信息 */
  
  `user_name` varchar(64) comment '微信昵称，通过openid拉取',

  `gender` varchar(2) comment '性别，似乎可以从微信拉取',
  
  `profile_photo_url` varchar(64) comment '头像URL',  
  
   /* 教育背景 */
	`edu_degree` tinyint(3) comment '教育程度：1国内本科2海外本科3硕士研究生4博士研究生5其他',

  `university` varchar(64) comment '学校名称',
  
  `graduation_time` varchar(16) comment '毕业年月（可判断出应届非应届）',
  
   /* 求职背景 */

  `target_fields` varchar(64) comment '目标领域（互联网等）',

  `target_positons` varchar(64) comment '目标岗位（算法、前台、后台等）',

  /* 时间戳 */

  `create_time` timestamp not null default current_timestamp comment '创建时间',

  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`user_id`),
  UNIQUE KEY `uqe_open_id` (`openid`) 
);
```

### 管理员表✅

```mysql
create table `manager_info`(
  
  /* 用户ID */

  `manager_id` varchar(32) not null,
  
  `username` varchar(32) not null,
  
  `password` varchar(32) not null comment '待扩展字段',
  
  `openid` varchar(64) not null comment '微信openid',
  

  /* 时间戳 */

  `create_time` timestamp not null default current_timestamp comment '创建时间',

  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`manager_id`) 
);
```

### 



### 打卡表✅

一天一条记录比较好

打卡记录时先判断数据库中有没有该openid在今日的record 没有的话根据currentdate创建一条

打卡记录接口（做题数 正确数 实际做题时间 ） 打卡接口

打卡天数直接count就行 因为求的不是连续打卡天数

```mysql
create table `punch_clock`(
  
  `punch_id` varchar(32) not null ,
  
  `openid` varchar(64) not null comment '微信openid',
  
  `punch_date` date not null comment '该条记录对应的打卡日期，一个id和日期只对应一条记录',
  
	`punch_state` tinyint(3) not null comment '打卡状态0未满足打卡条件未打卡1满足打卡条件未打卡，2已打卡',
  
  /* 今日刷题数 今日正确率 今日学习时长*/
  
 	`complete_number` int not null default 0 comment '今日已完成题目数',
  
  `solve_number` int not null default 0 comment '今日已做对题目数，可计算今日正确率',
  
 	`exercise_time` int not null default 0 comment '今日练习时长，单位分钟',
  
  
  /* 时间戳 */

  `create_time` timestamp not null default current_timestamp comment '创建时间',

  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`punch_id`),
  UNIQUE KEY `uqe_open_id` (`open_id`) 
);
```

### 意见反馈表

```mysql
create table `feedback`(
  
  `feedback_id` varchar(32) not null ,
  
  `openid` varchar(64) not null comment '微信openid',
  
  `content` text not null comment '反馈内容',
  
  /* 时间戳 */

  `create_time` timestamp not null default current_timestamp comment '创建时间',

  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`feedback_id`)
);
```

## 二、题库篇

- 子题库和父题库的目标领域和岗位是固定的吗：

  专业知识和企业真题的目标领域和岗位固定；但行测题比较特殊，行测题属于全岗位（那就单独开一个目标领域和岗位为“全岗”）。

### 父题库表✅

```mysql
/* 一个父题库包含了若干个子题库 */
create table `parent_question_bank`(
  
  /* ID */
  `parent_qb_id` varchar(32) not null comment '父题库ID',
  
  /* 子题库信息 */
  `pqb_type`  tinyint(3) COMMENT '题库类型,0企业1专业2行测',
  
  `pqb_title` varchar(64) comment '父题库名',
  
  `company_id` int comment '当题库类型为企业时，该字段标识该题库所属公司，与题库ICON关联',
  
  `target_field_type` int comment '目标领域（互联网等）编号',

  `target_positon_type` int comment '目标岗位（算法、前台、后台等）编号',
  
  `cqb_number` int not null comment '包含的子题库数量',
  
  /* 排序属性 */
  `is_recommended` tinyint(3) default 0 comment '推荐等级,推荐的话为1，用户按时间排序时的推荐位',
  
  `pqb_heat` int not null comment '父题库热度，是根据所包含的所有子题库的热度之和计算的',
  
  `relase_time` varchar(32) comment '发布时间精确到日，例20181108，是所含子题库中最新子题库的发布时间',
  
  /* 时间戳 */
  
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`parent_qb_id`)
);
```

### 子题库表✅

```mysql
create table `child_question_bank`(
  
  /* ID */
  `child_qb_id` varchar(32) not null,
  
  `parent_qb_id` varchar(32) not null comment '所属父题库ID',
  
  /* 子题库信息 */
  `cqb_title` varchar(64) comment '子题库名',
  
  `question_number` int not null comment '题目数量',
  
  `simulation_time` int comment '模拟模式所需时间，单位min',
  
  /* 排序属性 */
  `cqb_heat` int not null comment '题库热度，做过此题库的用户数',
  
  `is_recommended` tinyint(3) default 0 comment '推荐等级,推荐的话为1，用户按时间排序时的推荐位',
  
  `relase_time` varchar(32) comment '发布时间精确到日，例20181108',
  
  /* 时间戳 */
  
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`child_qb_id`)

);
```



### 题目表✅

```mysql
create table `question`(
  /* ID */
  
  `question_id` varchar(32) not null,
  
  `child_qb_id` varchar(32) not null comment '所属子题库ID',
  
  /* 题目信息 */
 
  `question_type` tinyint(3) COMMENT '题型,1单选题2多选题3问答题',
  
  /* 题目内容 */
  
  `question_stem` text comment '题目题干，HTML富文本',
  
  `answer` varchar(256) not null comment '答案，HTML富文本',
  
  `explanation` text not null comment '解析，HTML富文本',
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`question_id`)
);
```

### 选项表✅

```mysql
create table `quesiton_option`(
  /* ID */
  
  `option_id` varchar(32) not null,
  
  `question_id` varchar(32) not null comment '所属题目ID',
  
  /* 选项信息 */
 
  `option_name` varchar(16) not null COMMENT '选项名，ABCD？',
  
  `option_value` varchar(128) not null COMMENT '选项值',
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`option_id`)
);
```

### 公司表✅

```mysql
create table `company`(
  
  /* ID */
  
  `company_id` int not null auto_increment,

   /* 公司信息 */
  
  `field_type` int comment '所属行业',
  
  `company_name` varchar(32) comment '企业名称',
  
  `company_icon` varchar(128) comment '企业logo的URL',
  
  /* 时间戳 */
  
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`company_id`)

);
```



## 三、意向篇

行业与职位是一对多的关系，我们在职位表中维持一个外键：行业ID

### 行业表✅

```mysql
create table `work_field`(
  
  /* ID */
  `field_id` int not null auto_increment,
  
  `field_type` int not null comment '行业编号',

   /* 行业信息 */
  `field_name` varchar(32) not null comment '行业名称',
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`field_id`),
  UNIQUE KEY `uqe_field_type` (`field_type`)
);
```



### 职位表✅

```mysql
create table `work_position`(
  
  /* ID */
  
  `position_id` int not null auto_increment,
  
  `field_type` int not null comment '所属行业的编号',
  
  `position_type` int not null comment '职位编号',
  
  /* 职位信息*/
  
  `position_name` varchar(32) not null comment '职位名称',
  
  /* 时间戳 */
  
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
  primary key (`position_id`),
  UNIQUE KEY `uqe_position_type` (`position_type`)

);
```



## 四、笔经篇

### 笔经干货✅

```mysql
create table `experience_article`(
  /* ID */
  `article_id` varchar(32) not null,
  
  /* 推荐 */
  `is_recommended` tinyint(3) default 0 not null comment '是否推荐',
  
  /* 文章信息 */
  `article_type` tinyint(3) default 0 COMMENT '文章类别,0普通，预留字段',
  
  `article_tag_id` int comment '文章标签id',
  
  `article_icon` varchar(128) comment '文章iconurl',
  
  `reader_number` int default 0 not null comment '多少人读过，“热点”时的排序依据',
  
  `author` varchar(32) comment '作者名',
  
  `relase_time` varchar(64) comment '发布时间',
  
  /* 文章内容 */
  
  `content` text comment '文章内容，HTML富文本存储',
 
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`article_id`)
);
```

### 文章标签✅

```mysql
create table `article_tag`(
  /* ID */
  `tag_id`  int not null auto_increment,
  
  /* 标签信息 */
  `tag_name` varchar(64) not null comment '标签名',
  
  `tag_heat` int not null comment '标签热度',
 
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`tag_id`)
);
```





## 五、半论坛



## 六、复习篇

### 错题本✅

该表应该看做一个含有用户信息的虚拟子题库

```mysql
create table `mistake_book`(
  /* ID */
  `mistake_book_id` varchar(32) not null,
  
  /* 错题信息 */
  `openid` varchar(32) not null,
  
  `cqb_id` varchar(32) not null comment '对应的真正子题库',
  
  `mistake_number` int not null comment '错题数量',
  
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`mistake_book_id`)
);
```

### 错题✅

```mysql
create table `mistake`(
  /* ID */
  `mistake_id` varchar(32) not null,
  
  /* 错题信息 */
  `openid` varchar(32) not null,
  
  `mistake_book_id` varchar(32) not null comment '所属的错题本id',
  
  `question_id` varchar(32) not null comment '指向的真正题目',
  
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`mistake_id`)
);
```

### 收藏夹✅

这个收藏夹的优先级应该是最高吧

```mysql
create table `favorite`(
  /* ID */
  `favorite_id` varchar(32) not null,
  
  /* 点赞信息 */
  `openid` varchar(32) not null,
  
  `favorite_type` tinyint(3) not null comment '1子题库2题目3文章',
  
  `target_id` varchar(32) not null comment '点赞目标ID',

  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`favorite_id`)
);
```

### 收藏题目册✅

该表应该看做一个含有用户信息的虚拟子题库

```mysql
create table `collect_book`(
  /* ID */
  `collect_book_id` varchar(32) not null,
  
  /* 题目信息 */
  `openid` varchar(32) not null,
  
  `cqb_id` varchar(32) not null comment '对应的真正子题库',
  
  `collect_number` int not null comment '错题数量',
  
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`collect_book_id`)
);
```

### 收藏题✅

```mysql
create table `collect_question`(
  /* ID */
  `collect_question_id` varchar(32) not null,
  
  /* 收藏题信息 */
  `openid` varchar(32) not null,
  
  `collect_book_id` varchar(32) not null comment '所属的收藏题目册id',
  
  `question_id` varchar(32) not null comment '指向的真正题目',
  
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`collect_question_id`)
);
```

### 练习记录✅

需要支撑两项功能：

中途退出时记录用户对这套题的进度；

用户完成作答时保留用户的作答报告。

```mysql
create table `practice_record`(
  /* ID */
  `record_id` varchar(32) not null,
  
  /* 记录信息 */
  `openid` varchar(32) not null comment '该记录对应的用户',
  
  `child_qb_id` varchar(32) not null comment '该记录对应的子题库',
  
  `complete_number`  int COMMENT '这套子题库已完成的题目数量',

 	`user_answer_list` text not null comment '用户答案数组[]',
  
  `spent_time` int comment '提交这次练习记录时已花费的时间',
  
  `complete` tinyint(3) not null comment '0未完成1已完成',
  
  `correct_ratio` int comment '正确率',
  
  `last_mode` tinyint(3) not null comment '0没做过1模拟模式2练习模式',
  
  `surpass_ratio` int comment '超越率，complete为1时才有效',
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`record_id`)
);
```

## 七、出校篇

### 出校记录

```mysql
create table `pass_record`(
  /* ID */
  `record_id` varchar(32) not null,
  
  /* 记录信息 */
  `name` varchar(32) not null comment '姓名',
  
  `institute` varchar(128) not null comment '学院名',
  
  `state`  varchar(32) not null comment '状态',

 	`student_number` varchar(32) not null comment '学号',
  
  `time` varchar(64) not null comment '时间',
  
  
  /* 时间戳 */
  `create_time` timestamp not null default current_timestamp comment '创建时间',
  `update_time`  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
  
	primary key (`record_id`)
);
```

## 