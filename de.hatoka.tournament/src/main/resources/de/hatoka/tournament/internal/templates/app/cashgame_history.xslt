<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/"  xmlns="http://www.w3.org/1999/xhtml">
    <table class="table table-striped">
      <tr>
        <th>Name</th>
        <th>Action</th>
        <th>Amount</th>
        <th>Date</th>
      </tr>
      <xsl:for-each select="historyModel/entries" xmlns="http://www.w3.org/1999/xhtml">
        <tr>
          <td><xsl:value-of select="playerName" /></td>
          <td><xsl:value-of select="hatoka:getText($localizer, ./@actionKey, ./@actionKey)" /></td>
          <td><xsl:call-template name="formatMoney">
              <xsl:with-param name="money"><xsl:value-of select="amount" /></xsl:with-param>
          </xsl:call-template></td>
	      <td>
	        <xsl:call-template name="formatDateTime">
	          <xsl:with-param name="date">
	            <xsl:value-of select="date" />
	          </xsl:with-param>
	        </xsl:call-template>
	      </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>
</xsl:stylesheet>