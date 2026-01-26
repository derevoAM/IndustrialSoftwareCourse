SELECT 
    l.loan_id,
    l.client_id,
    l.principal,
    COALESCE(SUM(lp.amount), 0) AS total_paid,
    l.principal * 0.5 AS half_principal,
    l.status
FROM loans l
LEFT JOIN loan_payments lp ON l.loan_id = lp.loan_id 
    AND lp.status = 'success'
GROUP BY l.loan_id, l.client_id, l.principal, l.status
HAVING COALESCE(SUM(lp.amount), 0) < l.principal * 0.5;