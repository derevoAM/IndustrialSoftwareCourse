SELECT 
    c.client_id,
    c.full_name,
    COUNT(a.account_id) AS accounts_count
FROM clients c
LEFT JOIN accounts a ON c.client_id = a.client_id
GROUP BY c.client_id, c.full_name
ORDER BY c.client_id;