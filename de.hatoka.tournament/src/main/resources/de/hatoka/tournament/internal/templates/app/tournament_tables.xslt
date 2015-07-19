<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:param name="uriInfo" />
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/"  xmlns="http://www.w3.org/1999/xhtml">
    <form method="POST" action="actionList">
        <xsl:for-each select="tournamentTableModel/tables" xmlns="http://www.w3.org/1999/xhtml">
		  <div class="table">
		    <h2><xsl:value-of select="hatoka:getText($localizer, 'info.tournament.table', 'Tab {0]', @number)" /></h2>
        <xsl:for-each select="competitors" xmlns="http://www.w3.org/1999/xhtml">
		    <p><span class="seat"><xsl:value-of select="id" /></span><span class="player"><xsl:value-of select="playerName" /></span></p>
        </xsl:for-each>
		  </div>
        </xsl:for-each>
    </form>
  </xsl:template>
</xsl:stylesheet>