create table tasks (
    id bigint not null auto_increment,
    title varchar(64) NOT NULL,
    description varchar(1000),
    type enum('high-priority', 'necessary', 'optional') NOT NULL,
    start_date datetime NOT NULL,
    end_date datetime NOT NULL,
    close_date datetime,
    category_id bigint,
    all_day boolean NOT NULL,
    primary key (id)
);

create table categories (
    id bigint not null auto_increment,
    title varchar(64) not null,
    colour varchar(6) not null,
    primary key (id)
);

alter table tasks
add foreign key (category_id) references categories(id);