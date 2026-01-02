-- V5: Add User Avatar
-- Add avatar_url column to users table

ALTER TABLE users ADD COLUMN avatar_url VARCHAR(500);

-- Add comment
COMMENT ON COLUMN users.avatar_url IS 'URL to user profile avatar image';
