-- Smart Grocery Database Schema
-- Version 4: Add Sample Users for Testing

-- Password cho tất cả user là: 123456
-- BCrypt hash: $2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS

-- ============================================
-- THÊM CÁC USER MẪU
-- ============================================

INSERT INTO users (username, email, password_hash, full_name, is_active, created_at, updated_at) VALUES
    ('nguyenvana', 'nguyenvana@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Nguyễn Văn A', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('tranthib', 'tranthib@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Trần Thị B', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('levanc', 'levanc@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Lê Văn C', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('phamthid', 'phamthid@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Phạm Thị D', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('hoangvane', 'hoangvane@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Hoàng Văn E', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('vuthif', 'vuthif@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Vũ Thị F', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('dangvang', 'dangvang@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Đặng Văn G', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('buithih', 'buithih@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Bùi Thị H', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('dovani', 'dovani@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Đỗ Văn I', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ngothik', 'ngothik@gmail.com', '$2a$10$9zGlWWg/P8JzAnES/Is4hemnmW4VE9B7P6k9lhHfOfUKuVnw/jjyS', 'Ngô Thị K', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- GÁN ROLE USER CHO CÁC USER MỚI
-- ============================================

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username IN ('nguyenvana', 'tranthib', 'levanc', 'phamthid', 'hoangvane', 'vuthif', 'dangvang', 'buithih', 'dovani', 'ngothik')
AND r.name = 'USER';

