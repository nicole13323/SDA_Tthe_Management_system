-- Reference schema (PostgreSQL). The application creates the same structure through Hibernate (ddl-auto: update).
CREATE TABLE churches (
  id            BIGSERIAL PRIMARY KEY,
  code          VARCHAR(20)  NOT NULL,
  name          VARCHAR(150) NOT NULL,
  district      VARCHAR(100),
  sector        VARCHAR(100),
  password_hash VARCHAR(255) NOT NULL,
  CONSTRAINT uk_church_code UNIQUE (code),
  CONSTRAINT uk_church_name UNIQUE (name)
);

CREATE TABLE members (
  id                 BIGSERIAL PRIMARY KEY,
  church_id          BIGINT       NOT NULL REFERENCES churches(id),
  full_name          VARCHAR(150) NOT NULL,
  phone              VARCHAR(15)  NOT NULL,
  email              VARCHAR(150),
  password_hash      VARCHAR(255) NOT NULL,
  preferred_language VARCHAR(5)   NOT NULL DEFAULT 'rw',
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT now(),
  CONSTRAINT uk_member_phone UNIQUE (phone),
  CONSTRAINT uk_member_email UNIQUE (email)
);
CREATE INDEX idx_members_church ON members (church_id);

CREATE TABLE tithe_managers (
  id            BIGSERIAL PRIMARY KEY,
  username      VARCHAR(50)  NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  CONSTRAINT uk_manager_username UNIQUE (username)
);

CREATE TABLE payments (
  id          BIGSERIAL PRIMARY KEY,
  member_id   BIGINT         NOT NULL REFERENCES members(id),
  church_id   BIGINT         NOT NULL REFERENCES churches(id),
  amount      NUMERIC(14,2)  NOT NULL CHECK (amount > 0),
  currency    VARCHAR(3)     NOT NULL DEFAULT 'RWF',
  method      VARCHAR(20)    NOT NULL CHECK (method IN ('MTN_MOMO','AIRTEL_MONEY','CARD','BANK','CASH')),
  status      VARCHAR(20)    NOT NULL,
  reference   VARCHAR(40)    NOT NULL,
  recorded_by BIGINT         REFERENCES tithe_managers(id),   -- only for CASH
  paid_at     TIMESTAMPTZ    NOT NULL,
  CONSTRAINT uk_payment_reference UNIQUE (reference)
);
CREATE INDEX idx_pay_church_date ON payments (church_id, paid_at);
CREATE INDEX idx_pay_member_date ON payments (member_id, paid_at);

CREATE TABLE receipts (
  id         BIGSERIAL PRIMARY KEY,
  payment_id BIGINT      NOT NULL UNIQUE REFERENCES payments(id),
  receipt_no VARCHAR(30) NOT NULL,
  issued_at  TIMESTAMPTZ NOT NULL,
  CONSTRAINT uk_receipt_no UNIQUE (receipt_no)
);
