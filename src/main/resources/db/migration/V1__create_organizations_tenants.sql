CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS public.organizations (
                                                    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(100) NOT NULL UNIQUE,
    email           VARCHAR(255) NOT NULL UNIQUE,
    is_active       BOOLEAN NOT NULL DEFAULT true,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS public.tenants (
                                              id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES public.organizations(id) ON DELETE CASCADE,
    schema_name     VARCHAR(63) NOT NULL UNIQUE,
    status          VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    plan            VARCHAR(50) NOT NULL DEFAULT 'STARTER',
    max_users       INTEGER NOT NULL DEFAULT 10,
    max_agents      INTEGER NOT NULL DEFAULT 5,
    max_tickets_pm  INTEGER NOT NULL DEFAULT 1000,
    trial_ends_at   TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS public.users (
                                            id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    phone           VARCHAR(50),
    avatar_url      VARCHAR(500),
    is_active       BOOLEAN NOT NULL DEFAULT true,
    is_email_verified BOOLEAN NOT NULL DEFAULT false,
    last_login_at   TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS public.tickets (
                                              id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject         VARCHAR(500) NOT NULL,
    description     TEXT NOT NULL,
    status          VARCHAR(30)  NOT NULL DEFAULT 'OPEN',
    priority        VARCHAR(20)  NOT NULL DEFAULT 'MEDIUM',
    category        VARCHAR(100),
    customer_id     UUID NOT NULL,
    assigned_to     UUID,
    team_id         UUID,
    source          VARCHAR(50)  NOT NULL DEFAULT 'WEB',
    due_date        TIMESTAMP WITH TIME ZONE,
    resolved_at     TIMESTAMP WITH TIME ZONE,
    closed_at       TIMESTAMP WITH TIME ZONE,
    first_response_at TIMESTAMP WITH TIME ZONE,
                                    sentiment_score NUMERIC(3,2),
    ai_summary      TEXT,
    sla_breached    BOOLEAN NOT NULL DEFAULT false,
    created_by      UUID,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_tickets_customer  ON public.tickets(customer_id);
CREATE INDEX idx_tickets_status    ON public.tickets(status);
CREATE INDEX idx_tickets_priority  ON public.tickets(priority);