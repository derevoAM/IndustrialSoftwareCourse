SELECT 
    c.full_name,
    ca.account_id,
    ca.card_id,
    ca.card_type,
    ca.issued_at,
    ca.expires_at
FROM cards ca
JOIN accounts a ON ca.account_id = a.account_id
JOIN clients c ON a.client_id = c.client_id
WHERE ca.status = 'active'
ORDER BY c.full_name, ca.expires_at;