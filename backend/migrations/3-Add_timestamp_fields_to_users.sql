ALTER TABLE public.users
ADD created_at TIMESTAMP default now();

ALTER TABLE public.users
ADD updated_at TIMESTAMP default now();