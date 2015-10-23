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
        <h2><xsl:value-of select="hatoka:getText($localizer, 'info.tournament.table', 'Table {0}', @number)" /></h2>
        <table class="tournamentTable table table-striped">
        <xsl:for-each select="competitors" xmlns="http://www.w3.org/1999/xhtml">
            <tr>
                <td class="player"><xsl:value-of select="playerName" /></td>
                <td class="actions">
                    <xsl:for-each select="actions" xmlns="http://www.w3.org/1999/xhtml">
                        <form method="POST" action="{@uri}"><button class="glyphicon glyphicon-{@glyphicon}"><xsl:value-of select="@name" /></button></form>
                    </xsl:for-each>
                </td>
            </tr>
        </xsl:for-each>
          </table>
        </xsl:for-each>
        <h2><xsl:value-of select="hatoka:getText($localizer, 'info.tournament.inactivePlayers', 'Inactive', @number)" /></h2>
        <table class="tournamentTable table table-striped">
		  <tr>
			<th><xsl:value-of select="hatoka:getText($localizer, 'table.inactive.player', 'Player')" /></th>
			<th><xsl:value-of select="hatoka:getText($localizer, 'table.inactive.position', 'Position')" /></th>
			<th><xsl:value-of select="hatoka:getText($localizer, 'table.inactive.result', 'Result')" /></th>
		  </tr>
        <xsl:for-each select="tournamentTableModel/placedCompetitors" xmlns="http://www.w3.org/1999/xhtml">
            <tr>
                <td class="player"><xsl:value-of select="playerName" /></td>
                <td class="position"><xsl:value-of select="position" /></td>
                <td class="result"><xsl:call-template name="formatMoney">
                  <xsl:with-param name="amount"><xsl:value-of select="result/@amount" /></xsl:with-param>
                  <xsl:with-param name="currency"><xsl:value-of select="result/@currencyCode" /></xsl:with-param>
                </xsl:call-template></td>
            </tr>
        </xsl:for-each>
          </table>
    <form method="POST" action="action">
      <xsl:call-template name="button">
        <xsl:with-param name="name">shufflePlayer</xsl:with-param>
      </xsl:call-template>
    </form>
    </div>
  </xsl:template>
</xsl:stylesheet>