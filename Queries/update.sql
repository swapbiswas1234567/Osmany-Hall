UPDATE previousstudents SET hall = 'Osmany Hall' WHERE roomno > 0 AND roomno < 999

UPDATE stuinfo SET hall = 'Osmany Hall' WHERE roomno > 0 AND roomno < 999

UPDATE stuinfo SET hall = 'Ext-A' WHERE roomno LIKE 'A%';
UPDATE stuinfo SET hall = 'Ext-B' WHERE roomno LIKE 'B%';
UPDATE stuinfo SET hall = 'Ext-C' WHERE roomno LIKE 'C%';
UPDATE stuinfo SET hall = 'Ext-D' WHERE roomno LIKE 'D%';
UPDATE stuinfo SET hall = 'Ext-E' WHERE roomno LIKE 'E%';

UPDATE previousstudents SET hall = 'Ext-A' WHERE roomno LIKE 'A%';
UPDATE previousstudents SET hall = 'Ext-B' WHERE roomno LIKE 'B%';
UPDATE previousstudents SET hall = 'Ext-C' WHERE roomno LIKE 'C%';
UPDATE previousstudents SET hall = 'Ext-D' WHERE roomno LIKE 'D%';
UPDATE previousstudents SET hall = 'Ext-E' WHERE roomno LIKE 'E%';
