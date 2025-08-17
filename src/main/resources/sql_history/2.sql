ALTER TABLE deck add column is_public boolean default false;

CREATE TABLE daily_stats (
	user_id BIGINT references app_user(user_id) NOT NULL,
	date_stamp DATE NOT NULL, 
	num_correct INT default 0,
	num_incorrect INT default 0,
	PRIMARY KEY (user_id, date_stamp)
);