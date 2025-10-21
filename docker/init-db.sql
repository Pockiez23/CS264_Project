DECLARE @db sysname = '$(MSSQL_DB)';

IF DB_ID(@db) IS NULL
BEGIN
  EXEC('CREATE DATABASE ' + QUOTENAME(@db));
END
GO

USE [$(MSSQL_DB)];
GO

IF OBJECT_ID('dbo.petitions', 'U') IS NULL
BEGIN
  CREATE TABLE dbo.petitions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    advisor_name NVARCHAR(255) NULL,
    file_name NVARCHAR(255) NULL,
    status NVARCHAR(100) NULL,
    student_id NVARCHAR(50) NULL,
    student_name NVARCHAR(255) NULL,
    upload_date DATETIME2 NULL
  );
END
GO
