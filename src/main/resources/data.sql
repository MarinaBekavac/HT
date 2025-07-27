-- Insert 3 sample carts
insert into cart (id, user_id, date_created, date_modified, transaction_id)
values
(1, 'user123', CURRENT_DATE, NULL, 'b28a5923-2af9-413a-a74e-1271412d320f'),
(2, 'user456', CURRENT_DATE - 1, CURRENT_DATE, '542321c3-c135-4f18-9c73-711627ec7bc0');
--(3, 'user789', CURRENT_DATE - 2, NULL, 'd5658850-87c5-47f6-bdb9-bd10712a456c');

insert into finished_cart (id, user_id, date_created, date_modified, date_bought, transaction_id)
values
(3, 'user789', CURRENT_DATE - 2,  NULL, CURRENT_DATE - 1, 'd5658850-87c5-47f6-bdb9-bd10712a456c');

INSERT INTO item_price (id, source_id, name, description, one_time_price, time_frame_id, duration_units, amount) VALUES
-- Physical products with one-time prices
(1, 'ITEM-001', 'Laptop', 'High-performance laptop', 1300, null, null, null),  -- Laptop
(2, 'ITEM-002', 'Wireless Mouse', 'Ergonomic wireless mouse', 29.99, null, null, null),    -- Mouse
(3, 'ITEM-003', 'Monitor', '27" 4K display', 349.99, null, null, null),   -- Monitor
-- Subscriptions (one_time_price is NULL)
(4, 'SUB-001', 'Netflix Premium Monthly', 'Streaming service', NULL, 1, 1, 9.99),     -- Netflix Monthly
(5, 'SUB-002', 'Gym Membership', 'Premium gym access', NULL, 2, 1, 45),     -- Gym
(6, 'SUB-003', 'Amazon Prime', 'Annual membership', NULL, 2, 1, 9.99),     -- Amazon Prime
(7, 'SUB-004', 'Software Support', 'Premium technical support', NULL, 3, 1, 149),     -- Software Support
(8, 'SUB-002', 'Gym Membership', 'Premium gym access', NULL, 1, 1, 7),     -- Gym
(9, 'SUB-001', 'Netflix Premium Yearly', 'Streaming service', NULL, 2, 1, 70);     -- Netflix Yearly

-- Insert items for cart 1 (user123) - Fixed to use numeric item_ids
INSERT INTO cart_content (id, cart_id, item_id, action_id, quantity)
VALUES
(1, 1, 1, 1, 1),  -- Laptop (one-time purchase)
--(1, 5, 1),  -- Amazon Prime (yearly subscription)
(2, 1, 3, 2, 1),  -- Monitor
(3, 1, 8, 1, 1);  -- Netflix Premium Monthly (monthly subscription)

-- Insert items for cart 2 (user456)
INSERT INTO cart_content (id, cart_id, item_id, action_id, quantity)
VALUES
(4, 2, 2, 1, 2),  -- Wireless Mouse
--(2, 2, 1),  -- Another Wireless Mouse (same item)
(5, 2, 8, 3, 1);  -- Netflix Premium yearly

-- Insert items for cart 3 (user789)
INSERT INTO finished_cart_content (id, cart_id, item_id, action_id, quantity)
VALUES
(6, 3, 1, 1, 2),  -- Laptop
(7, 3, 3, 2, 1),  -- Netflix Monthly
(8, 3, 5, 1, 1);  -- Amazon Prime

