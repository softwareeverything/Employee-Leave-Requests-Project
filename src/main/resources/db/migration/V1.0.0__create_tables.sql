create table user
(
    id         bigint auto_increment primary key,
    email      varchar(255)                                         not null,
    password   varchar(255)                                         not null,
    name       varchar(255)                                         null,
    lastname   varchar(255)                                         null,
    role       ENUM ('ADMIN', 'USER')     DEFAULT 'USER',
    status     ENUM ('ACTIVE', 'PASSIVE') DEFAULT 'ACTIVE',
    started_at date                       default (current_date)    not null,
    created_by bigint                                               null,
    created_at timestamp                  default current_timestamp not null,
    CONSTRAINT UNIQUE KEY uk_email (email)
);

create table leave_request
(
    id            bigint auto_increment primary key,
    requester_id  bigint                                                           not null,
    approver_id   bigint                                                           null,
    description   varchar(600)                                                     null,
    start_date    date                                                             not null,
    end_date      date                                                             not null,
    days_off      int                                                              not null,
    approved_time timestamp                                                        null,
    status        ENUM ('WAITING', 'APPROVED', 'DENIED') DEFAULT 'WAITING',
    created_at    timestamp                              default current_timestamp not null,
    CONSTRAINT fk_request_to_user_requester foreign key (requester_id) references user (id),
    CONSTRAINT fk_request_to_user_approver foreign key (approver_id) references user (id)
);

create table holiday
(
    id            bigint auto_increment primary key,
    name          varchar(255)                        not null,
    day_of_month  int                                 not null,
    month_of_year int                                 not null,
    created_at    timestamp default current_timestamp not null,
    CONSTRAINT UNIQUE KEY uk_holidays_name (name)
);