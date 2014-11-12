SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[database_version](
	[major_release] [bigint] NOT NULL,
	[minor_release] [bigint] NOT NULL,
	[version] [bigint] NOT NULL,
	[script_type] [varchar](50) NOT NULL,
	[date_executed] [datetime] NOT NULL DEFAULT(GETDATE()),
	[script_info] [varchar](500) NOT NULL,
	[status] [varchar](10) NOT NULL,
	[executed_by] [varchar](50) NOT NULL,
 CONSTRAINT [PK_database_version] PRIMARY KEY CLUSTERED
(
	[major_release] ASC,
	[minor_release] ASC,
	[version] ASC,
	[script_type] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO


