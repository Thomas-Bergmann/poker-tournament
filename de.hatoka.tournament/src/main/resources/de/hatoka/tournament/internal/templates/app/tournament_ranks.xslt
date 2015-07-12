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
          <th>First Position</th>
          <th>Last Position</th>
          <th>Percentage</th>
          <th>Amount Per Player</th>
          <th>Amount Sum</th>
        </tr>
        <xsl:for-each select="tournamentRankModel/ranks" xmlns="http://www.w3.org/1999/xhtml">
          <tr>
            <td>
               <xsl:call-template name="checkbox">
                 <xsl:with-param name="name">rankID</xsl:with-param>
                 <xsl:with-param name="value"><xsl:value-of select="@id" /></xsl:with-param>
               </xsl:call-template>
            </td>
            <td>
               <xsl:call-template name="formatInteger">
                 <xsl:with-param name="amount"><xsl:value-of select="@firstPosition" /></xsl:with-param>
               </xsl:call-template>
            </td>
            <td>
               <xsl:call-template name="formatInteger">
                 <xsl:with-param name="amount"><xsl:value-of select="@lastPosition" /></xsl:with-param>
               </xsl:call-template>
            </td>
            <td>
              <xsl:if test="@percentageFilled = 'false'">-</xsl:if>
              <xsl:if test="@percentageFilled = 'true'">
                <xsl:call-template name="formatInteger">
                  <xsl:with-param name="amount"><xsl:value-of select="@percentage" /></xsl:with-param>
                </xsl:call-template>
              </xsl:if>
            </td>
            <td>
               <xsl:call-template name="formatInteger">
                 <xsl:with-param name="amount"><xsl:value-of select="@amountPerPlayer" /></xsl:with-param>
               </xsl:call-template>
            </td>
            <td><xsl:call-template name="formatInteger">
                <xsl:with-param name="amount"><xsl:value-of select="@amount" /></xsl:with-param>
            </xsl:call-template></td>
          </tr>
        </xsl:for-each>
        <xsl:for-each select="tournamentRankModel/prefilled" xmlns="http://www.w3.org/1999/xhtml">
          <tr>
            <td> </td>
            <td><xsl:call-template name="input">
              <xsl:with-param name="name">firstPosition_<xsl:value-of select="@id" /></xsl:with-param>
              <xsl:with-param name="value"><xsl:value-of select="@firstPosition" /></xsl:with-param>
              <xsl:with-param name="placeholderKey">placeholder.rank.firstPosition</xsl:with-param>
            </xsl:call-template></td>
            <td>
              <xsl:call-template name="input">
                <xsl:with-param name="name">lastPosition_<xsl:value-of select="@id" /></xsl:with-param>
                <xsl:with-param name="value"><xsl:value-of select="@lastPosition" /></xsl:with-param>
                <xsl:with-param name="placeholderKey">placeholder.rank.lastPosition</xsl:with-param>
              </xsl:call-template>
            </td>
            <td>
             <xsl:call-template name="input">
               <xsl:with-param name="name">percentage_<xsl:value-of select="@id" /></xsl:with-param>
               <xsl:with-param name="value"><xsl:value-of select="@percentage" /></xsl:with-param>
               <xsl:with-param name="placeholderKey">placeholder.rank.percentage</xsl:with-param>
             </xsl:call-template>
            </td>
            <td>-</td>
            <td><xsl:call-template name="input">
                <xsl:with-param name="name">amount_<xsl:value-of select="@id" /></xsl:with-param>
                <xsl:with-param name="placeholderKey">placeholder.rank.amount</xsl:with-param>
                <xsl:with-param name="value"><xsl:value-of select="@amount" /></xsl:with-param>
              </xsl:call-template></td>
          </tr>
        </xsl:for-each>
      </table>
      <xsl:call-template name="button">
        <xsl:with-param name="name">save</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="button">
        <xsl:with-param name="name">delete</xsl:with-param>
        <xsl:with-param name="cssClass">btn</xsl:with-param>
      </xsl:call-template>
    </form>
  </xsl:template>
</xsl:stylesheet>