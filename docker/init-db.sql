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
    file_content_type NVARCHAR(255) NULL,
    file_size BIGINT NULL,
    status NVARCHAR(100) NULL,
    student_id NVARCHAR(50) NOT NULL,
    student_name NVARCHAR(255) NOT NULL,
    petition_date DATE NULL,
    curriculum NVARCHAR(255) NULL,
    major NVARCHAR(255) NULL,
    phone_number NVARCHAR(50) NULL,
    email NVARCHAR(255) NULL,
    details NVARCHAR(MAX) NULL,
    address NVARCHAR(MAX) NULL,
    purpose NVARCHAR(MAX) NULL,
    file_path NVARCHAR(255) NULL,
    file_data VARBINARY(MAX) NULL,
    upload_date DATETIME2 NULL
  );
END
ELSE
BEGIN
  IF COL_LENGTH('dbo.petitions', 'petition_date') IS NULL
    ALTER TABLE dbo.petitions ADD petition_date DATE NULL;
  IF COL_LENGTH('dbo.petitions', 'curriculum') IS NULL
    ALTER TABLE dbo.petitions ADD curriculum NVARCHAR(255) NULL;
  IF COL_LENGTH('dbo.petitions', 'major') IS NULL
    ALTER TABLE dbo.petitions ADD major NVARCHAR(255) NULL;
  IF COL_LENGTH('dbo.petitions', 'phone_number') IS NULL
    ALTER TABLE dbo.petitions ADD phone_number NVARCHAR(50) NULL;
  IF COL_LENGTH('dbo.petitions', 'email') IS NULL
    ALTER TABLE dbo.petitions ADD email NVARCHAR(255) NULL;
  IF COL_LENGTH('dbo.petitions', 'details') IS NULL
    ALTER TABLE dbo.petitions ADD details NVARCHAR(MAX) NULL;
  IF COL_LENGTH('dbo.petitions', 'address') IS NULL
    ALTER TABLE dbo.petitions ADD address NVARCHAR(MAX) NULL;
  IF COL_LENGTH('dbo.petitions', 'purpose') IS NULL
    ALTER TABLE dbo.petitions ADD purpose NVARCHAR(MAX) NULL;
  IF COL_LENGTH('dbo.petitions', 'file_path') IS NULL
    ALTER TABLE dbo.petitions ADD file_path NVARCHAR(255) NULL;
  IF COL_LENGTH('dbo.petitions', 'file_content_type') IS NULL
    ALTER TABLE dbo.petitions ADD file_content_type NVARCHAR(255) NULL;
  IF COL_LENGTH('dbo.petitions', 'file_size') IS NULL
    ALTER TABLE dbo.petitions ADD file_size BIGINT NULL;
  IF COL_LENGTH('dbo.petitions', 'file_data') IS NULL
    ALTER TABLE dbo.petitions ADD file_data VARBINARY(MAX) NULL;
  IF COL_LENGTH('dbo.petitions', 'upload_date') IS NULL
    ALTER TABLE dbo.petitions ADD upload_date DATETIME2 NULL;
END
GO
