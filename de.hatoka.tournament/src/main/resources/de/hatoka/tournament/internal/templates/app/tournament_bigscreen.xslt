<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
	exclude-result-prefixes="hatoka">
	<xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
	<xsl:param name="localizer" />
	<xsl:param name="uriInfo" />
	<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<xsl:template match="/" xmlns="http://www.w3.org/1999/xhtml">
		<div>
			<table class="bigscreen" border="1">
				<tr>
					<td class="currentTime">
						Current Time
						<br />
						<xsl:call-template name="formatTime">
							<xsl:with-param name="date">
								<xsl:value-of select="tournamentBigScreenModel/currentTime" />
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td rowspan="2" class="currentLevel">
						Current Level
						<br />
						<xsl:call-template name="formatMinutes">
							<xsl:with-param name="duration">
								<xsl:value-of select="tournamentBigScreenModel/@duration" />
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="playersLeft">
						Players left
						<br />
						<xsl:value-of select="tournamentBigScreenModel/currentAmountPlayer" />
						/
						<xsl:value-of select="tournamentBigScreenModel/maxAmountPlayers" />
					</td>
				</tr>
				<tr>
					<td class="nextPause">
						Next Pause
						<br />
						<xsl:call-template name="formatTime">
							<xsl:with-param name="date">
								<xsl:value-of select="tournamentBigScreenModel/nextPauseTime" />
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="averageStack">
						Stack Average
						<br />
						<xsl:value-of select="tournamentBigScreenModel/averageStackSize" />
					</td>
				</tr>
				<tr>
					<td class="smallBlind">
						Small Blind
						<br />
						<xsl:value-of
							select="tournamentBigScreenModel/currentBlindLevel/smallBlind" />
					</td>
					<td class="bigBlind">
						Big Blind
						<br />
						<xsl:value-of
							select="tournamentBigScreenModel/currentBlindLevel/bigBlind" />
					</td>
					<td class="ante">
						Ante
						<br />
						<xsl:value-of select="tournamentBigScreenModel/currentBlindLevel/ante" />
					</td>
				</tr>
				<tr>
					<td colspan="3" class="nextLevel">
						(Next Level
						<xsl:value-of select="tournamentBigScreenModel/nextBlindLevel/smallBlind" />
						/
						<xsl:value-of select="tournamentBigScreenModel/nextBlindLevel/bigBlind" />
						/
						<xsl:value-of select="tournamentBigScreenModel/nextBlindLevel/ante" />
						)
					</td>
				</tr>
			</table>
			<form method="POST" action="action">
				<xsl:call-template name="button">
					<xsl:with-param name="name">back.to.blinds</xsl:with-param>
				</xsl:call-template>
			</form>
		</div>
	</xsl:template>
</xsl:stylesheet>
