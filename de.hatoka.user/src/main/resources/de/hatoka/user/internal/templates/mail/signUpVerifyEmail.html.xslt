<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:param name="localizer" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/signUpVerifyMailModel">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>
          <xsl:value-of select="hatoka:getText($localizer, 'mail.verifyEmail.title', 'Email Verification')" />
        </title>
      </head>
      <body>
        <h2>
          <xsl:value-of select="hatoka:getText($localizer, 'mail.verifyEmail.clickLink', 'Please click the following link:')" />
        </h2>
        <a>
          <xsl:attribute name="href"><xsl:value-of select="link" /></xsl:attribute>
          <xsl:value-of select="link" />
        </a>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>