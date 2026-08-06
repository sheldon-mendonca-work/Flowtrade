SELECT 'CREATE DATABASE flowtrade' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'flowtrade')\gexec
