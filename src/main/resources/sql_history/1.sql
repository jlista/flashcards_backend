CREATE TABLE app_user(
	user_id BIGSERIAL PRIMARY KEY,
	username VARCHAR(255) NOT NULL,
	Created TIMESTAMPTZ NOT NULL default current_timestamp
);

CREATE TABLE card(
	card_id BIGSERIAL PRIMARY KEY,
	clue VARCHAR(255) NOT NULL,
	answer VARCHAR(511) NOT NULL,
	created TIMESTAMPTZ NOT NULL default current_timestamp,
	deck_id BIGINT references deck(deck_id) NOT NULL,
	owned_by BIGINT references app_user(user_id) NOT NULL
);

CREATE TABLE deck(
	deck_id BIGSERIAL PRIMARY KEY,
	deck_name VARCHAR(255) NOT NULL,
	deck_desc VARCHAR(511),
	created TIMESTAMPTZ NOT NULL  default current_timestamp,
	owned_by BIGINT references app_user(user_id) NOT NULL
)

CREATE TABLE user_deck(
	user_deck_id BIGSERIAL PRIMARY KEY,
	deck_id BIGINT references deck(deck_id) NOT NULL,
	owned_by BIGINT references app_user(user_id) NOT NULL,
	created TIMESTAMPTZ NOT NULL default current_timestamp
)

CREATE TABLE deck_card(
	user_deck_id BIGINT references user_deck(user_deck_id) NOT NULL,
	card_id BIGINT references card(card_id) NOT NULL,
	mastery_level int NOT NULL,
	streak int NOT NULL,
	last_correct TIMESTAMPTZ,
	is_flagged bool NOT NULL,
	created TIMESTAMPTZ NOT NULL default current_timestamp,
	PRIMARY KEY (user_deck_id, card_id)
)


insert into app_user(username)
VALUES ('admin');
select * from app_user;

insert into deck (deck_name, deck_desc, owned_by)
VALUES ('test name', 'test description', 1);
select * from deck;

insert into user_deck (deck_id, owned_by)
VALUES (1,1);
select * from user_deck;

insert into card (clue, answer, owned_by, deck_id)
VALUES('Foo', 'Bar', 1, 1);
select * from card;

insert into deck_card(card_id, user_deck_id, mastery_level, streak, is_flagged)
VALUES(1,2,0,0,false);
select * from deck_card;