WITH account_incoming AS (
    SELECT 
        account_id,
        SUM(amount) AS total_incoming
    FROM transactions
    WHERE txn_type IN ('deposit', 'transfer_in')
    GROUP BY account_id
),
avg_incoming AS (
    SELECT AVG(total_incoming) AS avg_amount
    FROM account_incoming
)
SELECT 
    ai.account_id,
    ai.total_incoming,
    av.avg_amount
FROM account_incoming ai
CROSS JOIN avg_incoming av
WHERE ai.total_incoming > av.avg_amount
ORDER BY ai.total_incoming DESC;