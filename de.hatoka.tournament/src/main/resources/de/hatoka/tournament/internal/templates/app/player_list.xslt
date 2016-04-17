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
           <th>EMail</th>
           <th>Tournaments</th>
           <th>CashGames</th>
         </tr>
         <xsl:apply-templates />
       </table>
       <xsl:call-template name="button">
         <xsl:with-param name="name">delete</xsl:with-param>
       </xsl:call-template>
     </form>
  </xsl:template>
  <xsl:template match="players">
    <tr>
      <td>
        <xsl:call-template name="checkbox">
          <xsl:with-param name="name">playerID</xsl:with-param>
          <xsl:with-param name="value"><xsl:value-of select="id" /></xsl:with-param>
        </xsl:call-template>
      </td>
      <td>
        <xsl:value-of select="name" />
      </td>
      <td>
        <xsl:value-of select="eMail" />
      </td>
      <td>
        <xsl:value-of select="countTournaments" />
      </td>
      <td>
        <xsl:value-of select="countCashGames" />
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>