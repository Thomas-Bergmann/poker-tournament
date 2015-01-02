<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:param name="localizer" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" xmlns="http://www.w3.org/1999/xhtml" indent="yes"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
  <xsl:template match="/errorModel">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>
          <xsl:value-of select="hatoka:getText($localizer, 'error.title', 'General Error')" />
        </title>
      </head>
      <body>
        <h2>
          <xsl:value-of select="hatoka:getText($localizer, 'error.head', 'Error No', id)" />
        </h2>
        <div class="message">
          <xsl:value-of select="message" />
        </div>
        <div class="stacktrace">
          <xsl:value-of select="stacktrace" />
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>