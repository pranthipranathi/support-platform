CREATE TABLE IF NOT EXISTS public.comments (
                                               id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticket_id   UUID NOT NULL REFERENCES public.tickets(id) ON DELETE CASCADE,
    author_id   UUID NOT NULL REFERENCES public.users(id),
    content     TEXT NOT NULL,
    is_internal BOOLEAN NOT NULL DEFAULT false,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_comments_ticket ON public.comments(ticket_id);