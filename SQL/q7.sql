SELECT 
    c.client_id,
    c.full_name,
    COUNT(t.transaction_id) AS operations_count,
    CASE 
        WHEN COUNT(t.transaction_id) = 0 THEN 'inactive'
        WHEN COUNT(t.transaction_id) BETWEEN 1 AND 5 THEN 'low'
        WHEN COUNT(t.transaction_id) BETWEEN 6 AND 20 THEN 'medium'
        WHEN COUNT(t.transaction_id) > 20 THEN 'high'
    END AS activity_level
FROM clients c
LEFT JOIN accounts a ON c.client_id = a.client_id
LEFT JOIN transactions t ON a.account_id = t.account_id 
    AND t.txn_date >= CURRENT_DATE - INTERVAL '90 days'
GROUP BY c.client_id, c.full_name
ORDER BY operations_count DESC;