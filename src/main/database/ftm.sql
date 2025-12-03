create schema ftm;

create table tournaments(
	id int primary key auto_increment,
    name_tour varchar(20) not null,
    formula enum('round', 'cup'),
    start_date date not null,
    end_date date not null
);
create table teams(
	id int primary key auto_increment,
    name_team varchar(20) not null    
);

create table players(
	id int primary key auto_increment,
    name_player varchar(20) not null,
    birthday date not null,
    shirt_num int not null,
    pos enum('ST','LW','RW','AM','CM','LM', 'RM', 'DM', 'LB', 'RB', 'CB', 'GK') not null
);

 create table users(
	id int primary key auto_increment,
    name_user varchar(12) not null,
    hashedpwd varchar(100) not null,
    role_user enum('admin', 'user') not null,
    team_id int,
    foreign key(team_id) references teams(id) 
);

-- matches and ranking--------------------
create table matches(
	id int primary key auto_increment,
    tour_id int not null,
    rounds int not null,
    home_id int not null,
    away_id int not null,
	home_result int,
    away_result int,
    dt date not null,
    isDone bool not null,
    foreign key(tour_id) references tournaments(id),
    foreign key(home_id) references teams(id),
    foreign key(away_id) references teams(id)
);

create table match_detail(
	id int primary key auto_increment,
    match_id int not null,
    player_id int not null,
    moment enum('goal', 'OG', 'pen', 'yellow', 'red', 'in', 'out', 'injured') not null,
    minutes int not null,
    foreign key (match_id) references matches(id) ON DELETE CASCADE,
	foreign key (player_id) references players(id)
);

create table ranking(
	tour_id int not null,
    team_id int not null,
    points int not null,
    played int not null,
    wins int not null,
    draws int not null,
    losses int not null,
    goals int not null,
    against_goal int not null,
    diff int not null,
    primary key (tour_id, team_id),
    foreign key (tour_id) references tournaments(id),
    foreign key (team_id) references teams(id)    
);
-- Extended ----------------

create table contract(
	id int primary key auto_increment,
    player_id int not null,
    team_id int not null,
    start_date date not null,
    end_date date not null,
    salary int not null,
    state enum('active', 'expired')  not null,
    foreign key (player_id) references players(id),
    foreign key (team_id) references teams(id)
);

create table transfer(
	id int primary key auto_increment,
    player_id int not null,
    from_team int,
    to_team int not null,
    dt date not null,
    fee int not null,
    salary_offer int not null,
    state ENUM('pending', 'accepted', 'rejected', 'cancelled') DEFAULT 'pending',
    
    foreign key (player_id) references players(id),
    foreign key (from_team) references teams(id),
    foreign key (to_team) references teams(id)
);