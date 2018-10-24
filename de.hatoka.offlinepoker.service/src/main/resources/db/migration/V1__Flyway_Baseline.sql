-- cashgame_competitor definition

-- Drop table

-- DROP TABLE cashgame_competitor;

CREATE TABLE cashgame_competitor (
	competitor_id bigint IDENTITY(1,1) NOT NULL,
	cashgame_id bigint NOT NULL,
	invest_amount numeric(19,2) NULL,
	invest_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	payout_amount numeric(19,2) NULL,
	payout_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	player_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	[position] int NULL,
	state int NOT NULL,
	CONSTRAINT PK_cashgame_competitor PRIMARY KEY (competitor_id),
	CONSTRAINT AK_cashgame_competitor UNIQUE (cashgame_id,player_ref)
);
CREATE NONCLUSTERED INDEX IDX_cashgame_competitor ON cashgame_competitor (cashgame_id);

-- cashgames definition

-- Drop table

-- DROP TABLE cashgames;

CREATE TABLE cashgames (
	cashgame_id bigint IDENTITY(1,1) NOT NULL,
	buy_in_amount numeric(19,2) NULL,
	buy_in_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	group_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	local_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	owner_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	date_start datetime2(7) NOT NULL,
	CONSTRAINT PK_cashgames PRIMARY KEY (cashgame_id),
	CONSTRAINT AK_cashgames UNIQUE (owner_ref,local_ref)
);
CREATE NONCLUSTERED INDEX IDX_cashgames ON cashgames (owner_ref);


-- group_admin definition

-- Drop table

-- DROP TABLE group_admin;

CREATE TABLE group_admin (
	groupadmin_id bigint IDENTITY(1,1) NOT NULL,
	group_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	user_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	CONSTRAINT PK_group_admin PRIMARY KEY (groupadmin_id),
	CONSTRAINT AK_group_admin UNIQUE (group_ref,user_ref)
);
CREATE NONCLUSTERED INDEX IDX_group_admin_user ON group_admin (user_ref);
CREATE NONCLUSTERED INDEX IDX_group_admin_group ON group_admin (group_ref);


-- group_member definition

-- Drop table

-- DROP TABLE group_member;

CREATE TABLE group_member (
	groupmember_id bigint IDENTITY(1,1) NOT NULL,
	group_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	player_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	CONSTRAINT PK_group_member PRIMARY KEY (groupmember_id),
	CONSTRAINT AK_group_member UNIQUE (group_ref,player_ref)
);
CREATE NONCLUSTERED INDEX IDX_group_member_player ON group_member (player_ref);
CREATE NONCLUSTERED INDEX IDX_group_member_group ON group_member (group_ref);


-- groups definition

-- Drop table

-- DROP TABLE groups;

CREATE TABLE groups (
	internal_id bigint IDENTITY(1,1) NOT NULL,
	group_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	name varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	CONSTRAINT PK_groups PRIMARY KEY (internal_id),
	CONSTRAINT AK_groups UNIQUE (group_ref)
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_groups ON groups (group_ref);


-- player_history definition

-- Drop table

-- DROP TABLE player_history;

CREATE TABLE player_history (
	history_id bigint IDENTITY(1,1) NOT NULL,
	amount numeric(19,2) NULL,
	amount_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	[date] datetime2(7) NOT NULL,
	game_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	player_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	[type] int NOT NULL,
	CONSTRAINT PK_player_history PRIMARY KEY (history_id)
);
CREATE NONCLUSTERED INDEX IDX_player_history_game ON player_history (game_ref);
CREATE NONCLUSTERED INDEX IDX_player_history_player ON player_history (player_ref);


-- players definition

-- Drop table

-- DROP TABLE players;

CREATE TABLE players (
	player_id bigint IDENTITY(1,1) NOT NULL,
	context_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	email varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	date_first datetime2(7) NULL,
	name varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	local_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	CONSTRAINT PK_players PRIMARY KEY (player_id),
	CONSTRAINT AK_players UNIQUE (context_ref,local_ref)
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_players ON players (context_ref, local_ref);


-- tournament_blindlevel definition

-- Drop table

-- DROP TABLE tournament_blindlevel;

CREATE TABLE tournament_blindlevel (
	blindlevel_id bigint IDENTITY(1,1) NOT NULL,
	ante int NULL,
	blind_big int NULL,
	duration int NULL,
	pause bit NULL,
	rebuy_allowed bit NOT NULL,
	[position] int NULL,
	blind_small int NULL,
	date_start datetime2(7) NULL,
	tournament_id bigint NOT NULL,
	CONSTRAINT PK_tournament_blindlevel PRIMARY KEY (blindlevel_id),
	CONSTRAINT AK_tournament_blindlevel UNIQUE (tournament_id,[position])
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_tournament_blindlevel ON tournament_blindlevel (tournament_id, [position]);


-- tournament_competitor definition

-- Drop table

-- DROP TABLE tournament_competitor;

CREATE TABLE tournament_competitor (
	competitor_id bigint IDENTITY(1,1) NOT NULL,
	invest_amount numeric(19,2) NULL,
	invest_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	payout_amount numeric(19,2) NULL,
	payout_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	player_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	[position] int NULL,
	state int NOT NULL,
	table_no int NOT NULL,
	table_seat int NOT NULL,
	tournament_id bigint NOT NULL,
	CONSTRAINT PK_tournament_competitor PRIMARY KEY (competitor_id),
	CONSTRAINT AK_tournament_competitor UNIQUE (tournament_id,player_ref)
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_tournament_competitor ON tournament_competitor (tournament_id,player_ref);


-- tournament_rank definition

-- Drop table

-- DROP TABLE tournament_rank;

CREATE TABLE tournament_rank (
	rank_id bigint IDENTITY(1,1) NOT NULL,
	amount numeric(10,4) NULL,
	position_first int NOT NULL,
	position_last int NOT NULL,
	percentage numeric(10,4) NULL,
	tournament_id bigint NOT NULL,
	CONSTRAINT PK_tournament_rank PRIMARY KEY (rank_id)
);
CREATE NONCLUSTERED INDEX IDX_tournament_rank ON tournament_rank (tournament_id);


-- tournaments definition

-- Drop table

-- DROP TABLE tournaments;

CREATE TABLE tournaments (
	tournament_id bigint IDENTITY(1,1) NOT NULL,
	owner_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	local_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	group_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	buy_in_amount numeric(19,2) NULL,
	buy_in_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	current_round int NOT NULL,
	initial_stacksize int NOT NULL,
	max_player_per_table int NOT NULL,
	name varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	re_buy_amount numeric(19,2) NULL,
	re_buy_cur varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	date_start datetime2(7) NOT NULL,
	date_end datetime2(7) NULL,
	CONSTRAINT PK_tournaments PRIMARY KEY (tournament_id),
	CONSTRAINT AK_tournaments UNIQUE (owner_ref,local_ref)
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_tournaments ON tournaments (owner_ref, local_ref);


-- users definition

-- Drop table

-- DROP TABLE users;

CREATE TABLE users (
	user_id bigint IDENTITY(1,1) NOT NULL,
	user_ref varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NOT NULL,
	active bit NOT NULL,
	email varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	name_first varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	name_last varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	name_nick varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	locale varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	time_zone varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8 NULL,
	CONSTRAINT PK_users PRIMARY KEY (user_id),
	CONSTRAINT AK_users UNIQUE (user_ref)
);
CREATE UNIQUE NONCLUSTERED INDEX IDX_users ON users (user_ref);
