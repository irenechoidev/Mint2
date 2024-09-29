CREATE TABLE public.account_transaction(
    id varchar(36) PRIMARY KEY,
    account_id varchar(255) NOT NULL,
    amount DECIMAL NOT NULL,
    created_at TIMESTAMP default now(),
    updated_at TIMESTAMP default now(),
    FOREIGN KEY(account_id) REFERENCES account(id)
);

CREATE INDEX idx_account_transaction_account_id ON account_transaction(account_id);