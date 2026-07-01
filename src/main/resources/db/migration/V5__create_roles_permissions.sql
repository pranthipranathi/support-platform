CREATE TABLE IF NOT EXISTS public.roles (
                                            id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name         VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(255) NOT NULL,
    description  TEXT,
    is_system    BOOLEAN NOT NULL DEFAULT false,
    is_active    BOOLEAN NOT NULL DEFAULT true,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS public.permissions (
                                                  id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    resource    VARCHAR(100) NOT NULL,
    action      VARCHAR(50)  NOT NULL,
    description TEXT,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS public.role_permissions (
                                                       role_id       UUID NOT NULL REFERENCES public.roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES public.permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
    );

CREATE TABLE IF NOT EXISTS public.user_roles (
                                                 user_id UUID NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES public.roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
    );

-- Seed default roles
INSERT INTO public.roles (name, display_name, description, is_system, is_active) VALUES
                                                                                     ('SUPER_ADMIN', 'Super Admin', 'Full system access', true, true),
                                                                                     ('ADMIN',       'Admin',       'Tenant admin access', true, true),
                                                                                     ('AGENT',       'Agent',       'Support agent access', true, true),
                                                                                     ('CUSTOMER',    'Customer',    'Customer access', true, true)
    ON CONFLICT (name) DO NOTHING;

-- Seed default permissions
INSERT INTO public.permissions (name, resource, action, description) VALUES
                                                                         ('tickets:create',    'tickets',    'create', 'Create tickets'),
                                                                         ('tickets:read',      'tickets',    'read',   'View tickets'),
                                                                         ('tickets:update',    'tickets',    'update', 'Update tickets'),
                                                                         ('tickets:delete',    'tickets',    'delete', 'Delete tickets'),
                                                                         ('customers:create',  'customers',  'create', 'Create customers'),
                                                                         ('customers:read',    'customers',  'read',   'View customers'),
                                                                         ('customers:update',  'customers',  'update', 'Update customers'),
                                                                         ('customers:delete',  'customers',  'delete', 'Delete customers'),
                                                                         ('agents:create',     'agents',     'create', 'Create agents'),
                                                                         ('agents:read',       'agents',     'read',   'View agents'),
                                                                         ('users:read',        'users',      'read',   'View users'),
                                                                         ('users:update',      'users',      'update', 'Update users'),
                                                                         ('roles:read',        'roles',      'read',   'View roles'),
                                                                         ('roles:update',      'roles',      'update', 'Update roles')
    ON CONFLICT (name) DO NOTHING;