SELECT 
    a.account_id,
    a.client_id,
    COUNT(t.transaction_id) AS operations_count,
    COALESCE(SUM(
        CASE 
            WHEN t.txn_type IN ('withdrawal', 'transfer_out', 'fee') 
            THEN t.amount 
            ELSE 0 
        END
    ), 0) AS total_withdrawals
FROM accounts a
LEFT JOIN transactions t ON a.account_id = t.account_id
GROUP BY a.account_id, a.client_id, a.account_type, a.currency
ORDER BY a.account_id;