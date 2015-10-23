<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:param name="uriInfo" />
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/"  xmlns="http://www.w3.org/1999/xhtml">
    <form method="POST" action="actionList">
      <table class="table table-striped">
        <tr>
          <th>Select</th>
          <th>Name</th>
          <th>Active</th>
          <th>In Play</th>
          <th>Result</th>
        </tr>
        <xsl:for-each select="tournamentPlayerListModel/competitors" xmlns="http://www.w3.org/1999/xhtml">
          <tr>
            <td>
            <xsl:if test="active != 'true'">
              <xsl:call-template name="checkbox">
                <xsl:with-param name="name">competitorID</xsl:with-param>
                <xsl:with-param name="value"><xsl:value-of select="id" /></xsl:with-param>
              </xsl:call-template>
              </xsl:if>
            </td>
            <td><xsl:value-of select="playerName" /></td>
            <td>
              <xsl:if test="active = 'true'">active</xsl:if>
              <xsl:if test="active != 'true'">inactive</xsl:if>
            </td>
            <td><xsl:call-template name="formatMoney">
                <xsl:with-param name="amount"><xsl:value-of select="inPlay/@amount" /></xsl:with-param>
                <xsl:with-param name="currency"><xsl:value-of select="inPlay/@currencyCode" /></xsl:with-param>
            </xsl:call-template></td>
            <td><xsl:call-template name="formatMoney">
                <xsl:with-param name="amount"><xsl:value-of select="result/@amount" /></xsl:with-param>
                <xsl:with-param name="currency"><xsl:value-of select="result/@currencyCode" /></xsl:with-param>
            </xsl:call-template></td>
          </tr>
        </xsl:for-each>
      </table>
      <xsl:call-template name="button">
        <xsl:with-param name="name">buyin</xsl:with-param>
      </xsl:call-template>
    </form>
  </xsl:template>
</xsl:stylesheet>