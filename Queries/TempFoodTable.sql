INSERT INTO tempfood VALUES (1315, '20200807', 27);
INSERT INTO tempfood VALUES (1317, '20200817', 27);
INSERT INTO tempfood VALUES (1328, '20200827', 27);
INSERT INTO tempfood VALUES (1332, '20200808', 27);
INSERT INTO tempfood VALUES (1348, '20200818', 27);
INSERT INTO tempfood VALUES (1358, '20200828', 27);
INSERT INTO tempfood VALUES (1452, '20200811', 27);
INSERT INTO tempfood VALUES (1467, '20200812', 27);
INSERT INTO tempfood VALUES (1468, '20200813', 27);
INSERT INTO tempfood VALUES (1469, '20200817', 27);
INSERT INTO tempfood VALUES (1470, '20200815', 27);
INSERT INTO tempfood VALUES (1470, '20200820', 27);
INSERT INTO tempfood VALUES (1470, '20200031', 27);

SELECT hallid, dateserial, bill, name, roll, roomno from tempfood JOIN stuinfo USING (hallid) ORDER BY dateserial, hallid