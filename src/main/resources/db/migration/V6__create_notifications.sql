CREATE TABLE IF NOT EXISTS public.notifications (
                                                    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
    type        VARCHAR(100) NOT NULL,
    title       VARCHAR(500) NOT NULL,
    message     TEXT NOT NULL,
    is_read     BOOLEAN NOT NULL DEFAULT false,
    read_at     TIMESTAMP WITH TIME ZONE,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_notifications_user ON public.notifications(user_id, is_read);
