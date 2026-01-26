SELECT 
    account_id,
    client_id,
    account_type,
    currency,
    opened_at,
    status
FROM accounts
WHERE currency = 'EUR'
    AND opened_at > '2024-01-01'
    AND status = 'active'
ORDER BY opened_at;