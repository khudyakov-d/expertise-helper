create sequence hibernate_sequence start 1 increment 1;

create table action
(
    id   int8 not null,
    name varchar(255),
    spel varchar(255),
    primary key (id)
);

create table application
(
    id           uuid not null,
    location     varchar(255),
    organization varchar(255),
    pages_count  int4 not null,
    topic        varchar(255),
    topic_number varchar(255),
    project_id   uuid,
    primary key (id)
);
create table deferred_events
(
    jpa_repository_state_id int8 not null,
    deferred_events         varchar(255)
);
create table expert
(
    id               uuid not null,
    birth_date       timestamp,
    degree           int4 not null,
    email            varchar(255),
    name             varchar(255),
    organization     varchar(255),
    personal_phone   varchar(255),
    post             varchar(255),
    science_category int4 not null,
    work_phone       varchar(255),
    user_id          uuid,
    primary key (id)
);
create table expert_invitations
(
    expert_id      uuid not null,
    invitations_id uuid not null
);;
create table expert_contract
(
    contract_date       timestamp,
    contract_number     varchar(255),
    expert_id  uuid not null,
    project_id uuid not null,
    primary key (expert_id, project_id)
);
create table guard
(
    id   int8 not null,
    name varchar(255),
    spel varchar(255),
    primary key (id)
);
create table invitation
(
    id              uuid      not null,
    conclusion_path varchar(255),
    deadline_date   timestamp not null,
    status          int4      not null,
    application_id  uuid,
    expert_id       uuid,
    primary key (id)
);
create table project
(
    id                      uuid      not null,
    act_path                varchar(255),
    base_rate               float8    not null,
    conclusion_path         varchar(255),
    contract_path           varchar(255),
    creation_date           timestamp not null,
    project_type            int4      not null,
    required_number_experts int4      not null,
    title                   varchar(255),
    user_id                 uuid,
    primary key (id)
);
create table state
(
    id                int8 not null,
    initial_state     boolean,
    kind              int4,
    machine_id        varchar(255),
    region            varchar(255),
    state             varchar(255),
    submachine_id     varchar(255),
    initial_action_id int8,
    parent_state_id   int8,
    primary key (id)
);
create table state_entry_actions
(
    jpa_repository_state_id int8 not null,
    entry_actions_id        int8 not null,
    primary key (jpa_repository_state_id, entry_actions_id)
);
create table state_exit_actions
(
    jpa_repository_state_id int8 not null,
    exit_actions_id         int8 not null,
    primary key (jpa_repository_state_id, exit_actions_id)
);
create table state_machine
(
    machine_id            varchar(255) not null,
    state                 varchar(255),
    state_machine_context oid,
    primary key (machine_id)
);
create table state_state_actions
(
    jpa_repository_state_id int8 not null,
    state_actions_id        int8 not null,
    primary key (jpa_repository_state_id, state_actions_id)
);
create table transition
(
    id         int8 not null,
    event      varchar(255),
    kind       int4,
    machine_id varchar(255),
    guard_id   int8,
    source_id  int8,
    target_id  int8,
    primary key (id)
);
create table transition_actions
(
    jpa_repository_transition_id int8 not null,
    actions_id                   int8 not null,
    primary key (jpa_repository_transition_id, actions_id)
);
create table usr
(
    id            uuid not null,
    email         varchar(255),
    mail_password varchar(255),
    name          varchar(255),
    primary key (id)
);

alter table expert_invitations
    add constraint UK_eshxton8cvqvi9i19sxbqxprq unique (invitations_id);
alter table application
    add constraint FKrxh04lcvhpj4owpuk43oa0njh foreign key (project_id) references project;
alter table deferred_events
    add constraint fk_state_deferred_events foreign key (jpa_repository_state_id) references state;
alter table expert
    add constraint FKkl1r4r84o1kujifabifulitc6 foreign key (user_id) references usr;
alter table expert_invitations
    add constraint FKs8lbamfvbwdjcamtnj8mcea4s foreign key (invitations_id) references invitation;
alter table expert_invitations
    add constraint FKllwle2mmsuahu7uadt5d1maah foreign key (expert_id) references expert;
alter table expert_contract
    add constraint FKeg8ks7solg9vlutlmwodkah5n foreign key (expert_id) references expert;
alter table expert_contract
    add constraint FKojplwjgjdnnpbh97kaif9y6n4 foreign key (project_id) references project;
alter table invitation
    add constraint FK6nt0edxroetnpr4o7iubrqp8k foreign key (application_id) references application;
alter table invitation
    add constraint FKhbuulcrxkhstrlqv2ir73wpj7 foreign key (expert_id) references expert;
alter table project
    add constraint FKbfnwrq5lwqynoyfy2p5vfkr77 foreign key (user_id) references usr;
alter table state
    add constraint fk_state_initial_action foreign key (initial_action_id) references action;
alter table state
    add constraint fk_state_parent_state foreign key (parent_state_id) references state;
alter table state_entry_actions
    add constraint fk_state_entry_actions_a foreign key (entry_actions_id) references action;
alter table state_entry_actions
    add constraint fk_state_entry_actions_s foreign key (jpa_repository_state_id) references state;
alter table state_exit_actions
    add constraint fk_state_exit_actions_a foreign key (exit_actions_id) references action;
alter table state_exit_actions
    add constraint fk_state_exit_actions_s foreign key (jpa_repository_state_id) references state;
alter table state_state_actions
    add constraint fk_state_state_actions_a foreign key (state_actions_id) references action;
alter table state_state_actions
    add constraint fk_state_state_actions_s foreign key (jpa_repository_state_id) references state;
alter table transition
    add constraint fk_transition_guard foreign key (guard_id) references guard;
alter table transition
    add constraint fk_transition_source foreign key (source_id) references state;
alter table transition
    add constraint fk_transition_target foreign key (target_id) references state;
alter table transition_actions
    add constraint fk_transition_actions_a foreign key (actions_id) references action;
alter table transition_actions
    add constraint fk_transition_actions_t foreign key (jpa_repository_transition_id) references transition;