-- Smart Grocery Database Schema
-- Version 3: Add Friendships and Family Image

-- ============================================
-- 1. ADD IMAGE TO FAMILIES
-- ============================================
ALTER TABLE families ADD COLUMN image_url VARCHAR(500);

-- ============================================
-- 2. FRIENDSHIPS TABLE
-- ============================================
CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    addressee_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_friendship UNIQUE (requester_id, addressee_id),
    CONSTRAINT chk_friendship_self CHECK (requester_id != addressee_id)
);

CREATE INDEX idx_friendships_requester ON friendships(requester_id);
CREATE INDEX idx_friendships_addressee ON friendships(addressee_id);
CREATE INDEX idx_friendships_status ON friendships(status);

-- ============================================
-- 3. FAMILY INVITATIONS TABLE (for tracking invited friends when creating family)
-- ============================================
CREATE TABLE family_invitations (
    id BIGSERIAL PRIMARY KEY,
    family_id BIGINT NOT NULL REFERENCES families(id) ON DELETE CASCADE,
    inviter_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    invitee_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT uq_family_invitation UNIQUE (family_id, invitee_id)
);

CREATE INDEX idx_family_invitations_family ON family_invitations(family_id);
CREATE INDEX idx_family_invitations_invitee ON family_invitations(invitee_id);
CREATE INDEX idx_family_invitations_status ON family_invitations(status);

