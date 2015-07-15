<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:param name="uriInfo" />
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/"  xmlns="http://www.w3.org/1999/xhtml">
    <form method="POST" action="saveConfiguration">
      <xsl:call-template name="input">
        <xsl:with-param name="name">name</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="tournamentConfigurationModel/tournament/name" /></xsl:with-param>
        <xsl:with-param name="placeholderKey">placeholder.name.tournament</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="input">
        <xsl:with-param name="name">buyIn</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="tournamentConfigurationModel/tournament/buyIn" /></xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="input">
        <xsl:with-param name="name">initialStack</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="tournamentConfigurationModel/tournament/initialStack" /></xsl:with-param>
        <xsl:with-param name="placeholderKey">placeholder.tournament.initialStack</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="input">
        <xsl:with-param name="type">dateTime</xsl:with-param>
        <xsl:with-param name="name">date</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="tournamentConfigurationModel/tournament/date" /></xsl:with-param>
        <xsl:with-param name="placeholderKey">placeholder.tournament.date</xsl:with-param>
      </xsl:call-template>
    <label><xsl:value-of select="hatoka:getText($localizer, 'placeholder.tournament.reBuyOption', 'Rebuy Option')" /></label>
    <div class="select-radio">
      <xsl:for-each select="tournamentConfigurationModel/tournament/reBuyOption/option">
        <div class="select-option">
        <input name="rebuyOption" type="radio">
            <xsl:attribute name="value"><xsl:value-of select="@name" /></xsl:attribute>
            <xsl:if test="@isSelected = 'true'">
            <xsl:attribute name="checked">checked</xsl:attribute>
            </xsl:if>
        </input>
        <xsl:value-of select="hatoka:getText($localizer, concat('option.tournament.reBuyOption.', @name))" />
        </div>
      </xsl:for-each>
    </div>
      <xsl:call-template name="input">
        <xsl:with-param name="name">largestTable</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="tournamentConfigurationModel/tournament/largestTable" /></xsl:with-param>
        <xsl:with-param name="placeholderKey">placeholder.tournament.largestTable</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="button">
        <xsl:with-param name="name">save</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="button">
        <xsl:with-param name="name">cancel</xsl:with-param>
        <xsl:with-param name="cssClass">btn</xsl:with-param>
      </xsl:call-template>
    </form>
  </xsl:template>
</xsl:stylesheet>