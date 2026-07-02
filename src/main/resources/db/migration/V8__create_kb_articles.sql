CREATE TABLE IF NOT EXISTS public.kb_articles (
                                                  id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title           VARCHAR(500) NOT NULL,
    content         TEXT NOT NULL,
    excerpt         TEXT,
    status          VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    author_id       UUID NOT NULL REFERENCES public.users(id),
    views           INTEGER NOT NULL DEFAULT 0,
    helpful_votes   INTEGER NOT NULL DEFAULT 0,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_kb_articles_status ON public.kb_articles(status);