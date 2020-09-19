SELECT hallid, SUM(paidamount) FROM paymenthistory WHERE paymentdate >= 20200910 AND paymentdate <= 20200910 GROUP BY hallid

SELECT hallid, SUM(paidamount) FROM paymenthistory WHERE paymentdate >= 20200910 AND paymentdate <= 20200910 AND hallid = 1315

SELECT SUM(paidamount) FROM paymenthistory