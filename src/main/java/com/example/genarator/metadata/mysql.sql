/* ��ѯ���ݿ� db_name ���б�ע�� */
SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE upper(table_schema)=upper('<db_name>');

/* ��ѯ���ݿ� db_name �±� table_name �����ֶ�ע�� */
SELECT COLUMN_NAME,column_comment FROM INFORMATION_SCHEMA.Columns WHERE upper(table_name)=upper('<table_name>') AND table_schema='<db_name>'


SELECT t.TABLE_NAME,t.TABLE_COMMENT,c.COLUMN_NAME,c.COLUMN_COMMENT,c.COLUMN_TYPE FROM information_schema.TABLES t,INFORMATION_SCHEMA.Columns c 
WHERE c.TABLE_NAME=t.TABLE_NAME AND t.`TABLE_SCHEMA`='<db_name>'



