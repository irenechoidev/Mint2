CREATE TABLE public.users(
    id varchar(36) PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) not null
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

CREATE TABLE public.account(
    id varchar(36) PRIMARY KEY,
    user_id varchar(36) NOT NULL,
    balance DECIMAL NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE INDEX idx_account_user_id ON account(user_id);