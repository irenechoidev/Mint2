ALTER TABLE public.account
ADD created_at TIMESTAMP default now();

ALTER TABLE public.account
ADD updated_at TIMESTAMP default now();