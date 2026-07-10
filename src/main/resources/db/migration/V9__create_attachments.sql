CREATE TABLE IF NOT EXISTS public.attachments (
                                                  id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticket_id       UUID REFERENCES public.tickets(id) ON DELETE CASCADE,
    uploaded_by     UUID NOT NULL REFERENCES public.users(id),
    file_name       VARCHAR(500) NOT NULL,
    file_size       BIGINT NOT NULL,
    mime_type       VARCHAR(255) NOT NULL,
    storage_path    VARCHAR(1000) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_attachments_ticket ON public.attachments(ticket_id);