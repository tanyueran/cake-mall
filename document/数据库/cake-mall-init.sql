/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/10/28 14:41:00                          */
/*==============================================================*/


drop table if exists cake_news;

drop table if exists cake_order;

drop table if exists cake_product;

drop table if exists cake_product_categories;

drop table if exists cake_user;

drop table if exists cake_user_role;

/*==============================================================*/
/* Table: cake_news                                             */
/*==============================================================*/
create table cake_news
(
   id                   varchar(20) not null,
   sender_id            varchar(20) comment '发送方id',
   receiver_id          varchar(20) comment '接受方id',
   title                varchar(30) comment '消息简介',
   message              varchar(255) comment '消息内容',
   is_read              int comment '是否已读（0未读、1已读）',
   create_time          datetime default CURRENT_TIMESTAMP comment '消息发送时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '消息已读时间',
   primary key (id)
);

alter table cake_news comment '订单消息表';

/*==============================================================*/
/* Table: cake_order                                            */
/*==============================================================*/
create table cake_order
(
   id                   varchar(20) not null,
   cake_product_id      varchar(20) comment '蛋糕主键',
   create_user_id       varchar(20) comment '下单人主键',
   action_user_id       varchar(20) comment '接单拒单操作人主键',
   status               int comment '订单状态(
            0、已下单，未付款；
            5、未付款，订单取消；
            10、已付款，待发货；
            15、已拒单，订单取消；
            20、已接单，待配货；
            30、已配送，待收货；
            40、已收货，完成订单)下单后30分未付款，则取消订单；付款后30分钟未接单，则订单取消',
   create_time          datetime comment '订单创建时间',
   number               int comment '蛋糕个数',
   price                double comment '单价',
   total_price          double comment '总价',
   status5_time         datetime comment '未付款订单取消时间',
   status10_time        datetime comment '付款时间',
   status15_time        datetime comment '订单被拒时间',
   status20_time        datetime comment '接单时间',
   status30_time        datetime comment '发货时间',
   status40_time        datetime comment '订单完成时间',
   remark               varchar(255) comment '备注',
   primary key (id)
);

alter table cake_order comment '蛋糕订单表';

/*==============================================================*/
/* Table: cake_product                                          */
/*==============================================================*/
create table cake_product
(
   id                   varchar(20) not null,
   cake_product_categories_id varchar(20) comment '分类id',
   name                 varchar(255) not null comment '蛋糕名称',
   cake_imgs            varchar(2550) comment '蛋糕图片英文逗号分割，最多五个',
   detail               text comment '蛋糕详情',
   delete_status        int comment '删除状态(0未删除，1删除)',
   recommend_status     int comment '是否推荐状态（0不推荐，1推荐）',
   price                double comment '蛋糕价格',
   remark               varchar(255) comment '备注',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   Column_12            char(10),
   primary key (id)
);

alter table cake_product comment '蛋糕表';

/*==============================================================*/
/* Table: cake_product_categories                               */
/*==============================================================*/
create table cake_product_categories
(
   id                   varchar(20) not null,
   name                 varchar(255) comment '类型名称',
   code                 varchar(255) comment '类型编码',
   remark               varchar(255) comment '备注',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key AK_Key_2 (code)
);

alter table cake_product_categories comment '蛋糕类型表';

/*==============================================================*/
/* Table: cake_user                                             */
/*==============================================================*/
create table cake_user
(
   id                   varchar(20) not null,
   cake_user_role_id    varchar(20) comment '角色主键',
   nickname             varchar(255) comment '昵称',
   user_name            varchar(255) comment '用户姓名',
   head_img             varchar(255) comment '头像',
   user_code            varchar(255) not null comment '用户账号',
   user_pwd             varchar(255) not null comment '用户密码',
   money                double comment '账户金额',
   status               int comment '激活状态（0激活，1冻结）',
   remark               varchar(255) comment '备注',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key AK_Key_2 (user_code)
);

/*==============================================================*/
/* Table: cake_user_role                                        */
/*==============================================================*/
create table cake_user_role
(
   id                   varchar(20) not null,
   role_code            varchar(255) comment '角色code',
   role_name            varchar(255) comment '角色名称',
   remark               varchar(255) comment '备注',
   create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
   update_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
   primary key (id),
   unique key AK_Key_2 (role_code)
);

alter table cake_news add constraint FK_Reference_6 foreign key (receiver_id)
      references cake_user (id) on delete restrict on update restrict;

alter table cake_order add constraint FK_Reference_2 foreign key (cake_product_id)
      references cake_product (id) on delete restrict on update restrict;

alter table cake_order add constraint FK_Reference_3 foreign key (create_user_id)
      references cake_user (id) on delete restrict on update restrict;

alter table cake_order add constraint FK_Reference_4 foreign key (action_user_id)
      references cake_user (id) on delete restrict on update restrict;

alter table cake_product add constraint FK_Reference_1 foreign key (cake_product_categories_id)
      references cake_product_categories (id) on delete restrict on update restrict;

alter table cake_user add constraint FK_Reference_5 foreign key (cake_user_role_id)
      references cake_user_role (id) on delete restrict on update restrict;

