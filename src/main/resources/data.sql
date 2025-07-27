insert into actions (action_id, name)
values
(1, 'add'),
(2, 'delete'),
(3, 'modify');

insert into time_frame (name)
values
('day'),
('month'),
('year');

-- Insert 3 sample carts
insert into cart (id, user_id, date_created, date_modified, transaction_id)
values
(1, 'user123', CURRENT_DATE, NULL, 'b28a5923-2af9-413a-a74e-1271412d320f'),
(2, 'user456', CURRENT_DATE - 1, CURRENT_DATE, '542321c3-c135-4f18-9c73-711627ec7bc0'),
(3, 'user789', CURRENT_DATE - 2, NULL, 'd5658850-87c5-47f6-bdb9-bd10712a456c');

--
---- Insert one-time purchase items (with explicit IDs to match relationships)
--INSERT INTO item (id, source_id, name, description, stock_quantity, is_active, pricing_type_id) VALUES
--(1, 'ITEM-001', 'Laptop', 'High-performance laptop', 10, TRUE, 1),
--(2, 'ITEM-002', 'Wireless Mouse', 'Ergonomic wireless mouse', 50, TRUE, 1),
--(3, 'ITEM-003', 'Monitor', '27" 4K display', 15, TRUE, 1);
--
---- Insert corresponding one-time prices (1:1 relationship)
--INSERT INTO one_time_price (item_id, price) VALUES
--(1, 1299.99),
--(2, 29.99),
--(3, 349.99);
--
---- Insert subscription items
--INSERT INTO item (id, source_id, name, description, stock_quantity, is_active, pricing_type_id) VALUES
--(4, 'SUB-001', 'Netflix Premium', 'Streaming service', NULL, TRUE, 2),
--(5, 'SUB-002', 'Gym Membership', 'Premium gym access', NULL, TRUE, 2),
--(6, 'SUB-003', 'Amazon Prime', 'Annual membership', NULL, TRUE, 2),
--(7, 'SUB-004', 'Software Support', 'Premium technical support', NULL, TRUE, 2);
--
---- Insert recurring prices (1:N relationship - multiple options per item)
--INSERT INTO recurring_price (item_id, time_frame_id, duration_units, price) VALUES
---- Netflix options
--(4, 2, 1, 15.99),  -- Monthly
--(4, 3, 1, 149.99), -- Yearly (discounted)
--
---- Gym options
--(5, 1, 30, 120.00),  -- 30-day pass
--(5, 2, 1, 50.00),    -- Monthly
--(5, 3, 1, 500.00),   -- Yearly
--
---- Amazon Prime (only yearly)
--(6, 3, 1, 119.00),
--
---- Software Support options
--(7, 2, 1, 29.99),    -- Monthly
--(7, 3, 1, 299.99);   -- Yearly

-- Insert items (removed pricing_type_id since it's now handled by the price tables)

--INSERT INTO item (id, source_id, name, description, stock_quantity, is_active) VALUES
--(1, 'ITEM-001', 'Laptop', 'High-performance laptop', 10, TRUE),
--(2, 'ITEM-002', 'Wireless Mouse', 'Ergonomic wireless mouse', 50, TRUE),
--(3, 'ITEM-003', 'Monitor', '27" 4K display', 15, TRUE),
--(4, 'SUB-001', 'Netflix Premium', 'Streaming service', NULL, TRUE),
--(5, 'SUB-002', 'Gym Membership', 'Premium gym access', NULL, TRUE),
--(6, 'SUB-003', 'Amazon Prime', 'Annual membership', NULL, TRUE),
--(7, 'SUB-004', 'Software Support', 'Premium technical support', NULL, TRUE),
--(8, 'SUB-002', 'Gym Membership', 'Premium gym access', NULL, TRUE);

INSERT INTO item (id, source_id, name, description, is_active) VALUES
(1, 'ITEM-001', 'Laptop', 'High-performance laptop', TRUE),
(2, 'ITEM-002', 'Wireless Mouse', 'Ergonomic wireless mouse', TRUE),
(3, 'ITEM-003', 'Monitor', '27" 4K display', TRUE),
(4, 'SUB-001', 'Netflix Premium', 'Streaming service', TRUE),
(5, 'SUB-002', 'Gym Membership', 'Premium gym access', TRUE),
(6, 'SUB-003', 'Amazon Prime', 'Annual membership', TRUE),
(7, 'SUB-004', 'Software Support', 'Premium technical support', TRUE),
(8, 'SUB-002', 'Gym Membership', 'Premium gym access', TRUE);

-- Insert prices (one entry per item)
INSERT INTO price (id, item_id, one_time_price) VALUES
-- Physical products with one-time prices
(1, 1, 1299.99),  -- Laptop
(2, 2, 29.99),    -- Mouse
(3, 3, 349.99),   -- Monitor
-- Subscriptions (one_time_price is NULL)
(4, 4, NULL),     -- Netflix
(5, 5, NULL),     -- Gym
(6, 6, NULL),     -- Amazon Prime
(7, 7, NULL),     -- Software Support
(8, 8, NULL);     -- Gym

-- Insert recurring prices (multiple options for subscription items)
INSERT INTO recurring_price (id, price_id, time_frame_id, duration_units, amount) VALUES
-- Netflix options
(1, 4, 2, 1, 15.99),   -- Monthly
(2, 4, 3, 1, 149.99),  -- Yearly (discounted)

-- Gym options
(3, 5, 1, 30, 120.00), -- 30-day pass
(4, 5, 2, 1, 50.00),   -- Monthly
(5, 5, 3, 1, 500.00),  -- Yearly
(6, 8, 3, 1, 500.00),  -- Yearly

-- Amazon Prime (only yearly)
(7, 6, 3, 1, 119.00),

-- Software Support options
(8, 7, 2, 1, 29.99),   -- Monthly
(9, 7, 3, 1, 299.99);  -- Yearly


-- Insert items for cart 1 (user123) - Fixed to use numeric item_ids
INSERT INTO cart_content (cart_id, item_id, action_id, quantity)
VALUES
(1, 1, 1, 1),  -- Laptop (one-time purchase)
--(1, 5, 1),  -- Amazon Prime (yearly subscription)
(1, 3, 2, 1),  -- Netflix Premium (monthly subscription)
(1, 8, 1, 1);  -- Netflix Premium (monthly subscription)

-- Insert items for cart 2 (user456)
INSERT INTO cart_content (cart_id, item_id, action_id, quantity)
VALUES
(2, 2, 1, 2),  -- Wireless Mouse
--(2, 2, 1),  -- Another Wireless Mouse (same item)
(2, 4, 3, 1);  -- Netflix Premium

-- Insert items for cart 3 (user789)
INSERT INTO cart_content (cart_id, item_id, action_id, quantity)
VALUES
(3, 1, 1, 2),  -- Laptop
(3, 3, 2, 1),  -- Netflix
(3, 5, 1, 1);  -- Amazon Prime
--- Insert items for cart 1 (belongs to user123)
--insert into cart_content (cart_id, item_id, action_id)
--values
--(1, 'item_001', 1),
--(1, 'item_005', 1),
--(1, 'item_003', 2);



---- Insert items for cart 2 (belongs to user456)
--insert into cart_content (cart_id, item_id, action_id)
--values
--(2, 'item_010', 1),
--(2, 'item_005', 1);

-- Cart 3 (user789) is empty for demonstration