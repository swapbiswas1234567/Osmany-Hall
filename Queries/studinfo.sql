Stuinfo.sql

INSERT INTO stuinfo (hallid, roll, name, dept, batch, roomno, entrydate, contno) VALUES (5050, '201714030', 'Tarek', 'CSE', 17, '620', '20200808', '01515288484' );

SELECT MAX(hallid) FROM stuinfo


INSERT INTO stuinfo VALUES (5050, '201714030', 'Tarek','','', 'CSE', 17, '20200808', '01515288484', '', 'MALE', '', '', '', '', '620');

UPDATE stuinfo SET roll = , name = , fname = , mname = , dept = , batch = , contno = , bgrp = , rel = , dob = , peraadd = , presentadd = , roomno = where hallid = ;


UPDATE stuinfo SET roll = ?, name = ?, fname = ?, mname = ?, dept = ?, batch = ?, contno = ?, bgrp = ?, rel = ?, dob = ?, peradd = ?, presentadd = ?, roomno = ? WHERE hallid = ?

SELECT hallid, name, roll, dept, roomno, totaldue, securitymoney, messad, idcard, contno FROM stuinfo

INSERT INTO previousstudents (hallid, roll, name, fname, mname, dept, batch, entrydate, contno, email, bgrp, sex, rel, dob, peradd, presentadd, roomno, image, securitymoney, messad, idcard) SELECT hallid, roll, name, fname, mname, dept, batch, entrydate, contno, email, bgrp, sex, rel, dob, peradd, presentadd, roomno, image, securitymoney, messad, idcard FROM stuinfo WHERE hallid = 1488

