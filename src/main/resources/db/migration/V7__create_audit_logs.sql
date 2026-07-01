CREATE TABLE IF NOT EXISTS public.audit_logs (
                                                 id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID REFERENCES public.users(id),
    action      VARCHAR(100) NOT NULL,
    resource    VARCHAR(100) NOT NULL,
    resource_id UUID,
    old_values  TEXT,
    new_values  TEXT,
    ip_address  VARCHAR(45),
    user_agent  VARCHAR(500),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE INDEX idx_audit_user     ON public.audit_logs(user_id);
CREATE INDEX idx_audit_resource ON public.audit_logs(resource, resource_id);