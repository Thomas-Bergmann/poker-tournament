<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
 xmlns="http://www.w3.org/1999/xhtml"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/">
     <form method="POST" action="action">
       <table class="table table-striped">
         <tr>
           <th>Select</th>
           <th>Name</th>
           <th>Date</th>
           <th>Buy-In</th>
         </tr>
         <xsl:apply-templates />
       </table>
       <xsl:call-template name="button">
         <xsl:with-param name="name">delete</xsl:with-param>
       </xsl:call-template>
     </form>
  </xsl:template>
  <xsl:template match="tournaments">
    <tr>
      <td>
        <xsl:call-template name="checkbox">
          <xsl:with-param name="name">tournamentID</xsl:with-param>
          <xsl:with-param name="value"><xsl:value-of select="id" /></xsl:with-param>
        </xsl:call-template>
      </td>
      <td>
      <a>
        <xsl:attribute name="href"><xsl:value-of select="uri" /></xsl:attribute>
        <xsl:value-of select="name" />
      </a>
      </td>
      <td>
        <xsl:call-template name="formatDate">
          <xsl:with-param name="date">
            <xsl:value-of select="date" />
          </xsl:with-param>
        </xsl:call-template>
      </td>
      <td>
        <xsl:call-template name="formatMoney">
          <xsl:with-param name="money">
            <xsl:value-of select="buyIn" />
          </xsl:with-param>
        </xsl:call-template>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>