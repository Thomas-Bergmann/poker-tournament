<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <xsl:param name="uriInfo" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <xsl:call-template name="head">
          <xsl:with-param name="title"><xsl:value-of select="hatoka:getText($localizer, 'title.tournament', 'Tournament', tournamentPlayerListModel/tournament/buyIn)" /></xsl:with-param>
      </xsl:call-template>
      <body>
        <div class="menu">
          <a>
            <xsl:attribute name="href"><xsl:value-of select="tournamentPlayerListModel/listUri" /></xsl:attribute>
            <xsl:value-of select="hatoka:getText($localizer, 'link.backToList', 'Back to list')" />
          </a>
        </div>
        <div class="col-md-3">
        <h2>
          <xsl:value-of select="hatoka:getText($localizer, 'info.create.player', 'Create new player:')" />
        </h2>
        <form method="POST" action="createPlayer">
          <xsl:call-template name="input">
            <xsl:with-param name="name">name</xsl:with-param>
            <xsl:with-param name="placeholderKey">placeholder.name.player</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="button">
            <xsl:with-param name="name">create</xsl:with-param>
          </xsl:call-template>
        </form>
        </div>
        <xsl:if test="tournamentPlayerListModel/unassignedPlayers" >
	      <div class="col-md-3">
	        <h2>
	          <xsl:value-of select="hatoka:getText($localizer, 'info.unassigned.player', 'Assign xyz')" />
	        </h2>
	        <form method="POST" action="assignPlayer">
	          <div class="input">
	            <select name="playerID">
	              <xsl:for-each select="tournamentPlayerListModel/unassignedPlayers" xmlns="http://www.w3.org/1999/xhtml">
	                <option>
	                  <xsl:attribute name="value"><xsl:value-of select="id" /></xsl:attribute>
	                  <xsl:value-of select="name" />
	                </option>
	              </xsl:for-each>
	            </select>
	          </div>
	          <xsl:call-template name="button">
	            <xsl:with-param name="name">buyin</xsl:with-param>
	          </xsl:call-template>
	        </form>
          </div>
        </xsl:if>
        <div class="col-md-6">
        <h2>
          <xsl:value-of select="hatoka:getText($localizer, 'info.assigned.player', 'Select xyz')" />
        </h2>
        <form method="POST" action="actionPlayerList">
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
                  <xsl:call-template name="checkbox">
                    <xsl:with-param name="name">competitorID</xsl:with-param>
                    <xsl:with-param name="value"><xsl:value-of select="id" /></xsl:with-param>
                  </xsl:call-template>
                </td>
                <td><xsl:value-of select="playerName" /></td>
                <td>
                  <xsl:if test="active = 'true'">active</xsl:if>
                  <xsl:if test="active != 'true'">inactive</xsl:if>
                </td>
                <td><xsl:call-template name="formatMoney">
                    <xsl:with-param name="money"><xsl:value-of select="inPlay" /></xsl:with-param>
                </xsl:call-template></td>
                <td><xsl:call-template name="formatMoney">
                    <xsl:with-param name="money"><xsl:value-of select="result" /></xsl:with-param>
                </xsl:call-template></td>
              </tr>
            </xsl:for-each>
          </table>
          <p class="bg-info">
            Average: <xsl:value-of select="tournamentPlayerListModel/tournament/average/amount" />
            <br />Sum: <xsl:value-of select="tournamentPlayerListModel/tournament/sumInPlay/amount" />
          </p>
          <xsl:call-template name="input">
            <xsl:with-param name="name">amount</xsl:with-param>
            <xsl:with-param name="placeholderKey">placeholder.amount</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="button">
            <xsl:with-param name="name">rebuy</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="button">
            <xsl:with-param name="name">seatopen</xsl:with-param>
            <xsl:with-param name="cssClass">btn</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="button">
            <xsl:with-param name="name">sort</xsl:with-param>
            <xsl:with-param name="cssClass">btn</xsl:with-param>
          </xsl:call-template>
        </form>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>