<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
	exclude-result-prefixes="hatoka">
	<xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
	<xsl:param name="localizer" />
	<xsl:param name="uriInfo" />
	<!-- BEGIN OUTPUT -->
	<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<!-- MAIN TEMPLATE -->
	<xsl:template match="/">
      <html xmlns="http://www.w3.org/1999/xhtml">
		<xsl:call-template name="head">
			<xsl:with-param name="title">
				<xsl:choose>
					<xsl:when test="frameModel/@title">
						<xsl:value-of select="frameModel/@title" />
					</xsl:when>
					<xsl:when test="frameModel/@titleKey">
					<xsl:value-of
						select="hatoka:getText($localizer, frameModel/@titleKey, 'no title')" />
					</xsl:when>
					<xsl:otherwise>
						no title
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="cssHRef">
				<xsl:value-of select="hatoka:getResourceURI($uriInfo, 'css/frame.css')" />
			</xsl:with-param>
		</xsl:call-template>
			<body>
				<div class="row">
					<div class="main bigscreen">
						<xsl:value-of select="frameModel/content" disable-output-escaping="yes" />
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
