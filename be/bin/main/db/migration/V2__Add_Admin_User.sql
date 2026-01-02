-- V2: Thêm tài khoản Admin hệ thống
-- Chỉ chạy nếu chưa có user 'admin'

-- Insert admin user nếu chưa tồn tại
-- Password: 123456 (BCrypt encoded)
INSERT INTO users (username, email, password_hash, full_name, is_active, created_at, updated_at)
SELECT 'admin', 'admin@smartgrocery.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'System Admin', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- Assign USER role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'USER'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

