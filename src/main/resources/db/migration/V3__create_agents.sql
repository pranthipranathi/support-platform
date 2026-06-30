CREATE TABLE IF NOT EXISTS public.agents (
                                             id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL UNIQUE REFERENCES public.users(id),
    department      VARCHAR(100),
    max_tickets     INTEGER NOT NULL DEFAULT 20,
    is_available    BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_agents_user ON public.agents(user_id);