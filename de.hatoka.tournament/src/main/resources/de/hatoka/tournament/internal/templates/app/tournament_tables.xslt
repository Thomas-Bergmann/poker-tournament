<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:param name="uriInfo" />
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/"  xmlns="http://www.w3.org/1999/xhtml">
    <div>
     <xsl:for-each select="tournamentTableModel/tables" xmlns="http://www.w3.org/1999/xhtml">
        <h2><xsl:value-of select="hatoka:getText($localizer, 'info.tournament.table', 'Tab {0]', @number)" /></h2>
        <table class="tournamentTable table table-striped">
        <xsl:for-each select="competitors" xmlns="http://www.w3.org/1999/xhtml">
            <tr>
                <td class="player"><xsl:value-of select="playerName" /></td>
                <td>
                <xsl:if test="active = 'true'">
                    <xsl:for-each select="actions" xmlns="http://www.w3.org/1999/xhtml">
                        <form method="POST" action="{@uri}"><button class="glyphicon glyphicon-{@glyphicon}"><xsl:value-of select="@name" /></button></form>
                    </xsl:for-each>
                </xsl:if>
                <xsl:if test="active = 'false'">
                <span class="position"><xsl:value-of select="position" /></span>
                </xsl:if>
                </td>
            </tr>
        </xsl:for-each>
          </table>
        </xsl:for-each>
    <form method="POST" action="actionTables">
      <xsl:call-template name="button">
        <xsl:with-param name="name">assignTables</xsl:with-param>
      </xsl:call-template>
    </form>
    </div>
  </xsl:template>
</xsl:stylesheet>