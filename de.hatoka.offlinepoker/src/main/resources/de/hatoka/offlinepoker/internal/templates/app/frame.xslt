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
						select="hatoka:getText($localizer, frameModel/@titleKey, frameModel/@titleKey)" />
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
				<nav class="navbar navbar-inverse navbar-fixed-top">
					<div class="container-fluid">
						<!-- Brand and toggle get grouped for better mobile display -->
						<div class="navbar-header">
							<button type="button" class="navbar-toggle collapsed"
								data-toggle="collapse" data-target="#bs-example-navbar-collapse-9">
								<span class="sr-only">Toggle navigation</span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>
							<a class="navbar-brand" href="{frameModel/@uriHome}">
								<xsl:value-of
									select="hatoka:getText($localizer, 'program.title', 'Poker Planner')" />
							</a>
						</div>

						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-9">
							<ul class="nav navbar-nav">
								<xsl:apply-templates select="frameModel/mainMenu" />
							</ul>
						</div><!-- /.navbar-collapse -->
					</div>
				</nav>
				<div class="row">
					<div class="col-sm-3 col-md-2 sidebar">
						<ul class="nav-sidebar">
							<xsl:apply-templates select="frameModel/sideMenu" />
						</ul>
					</div>
					<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<xsl:choose>
					  <xsl:when test="frameModel/@title">
						<h1 class="page-header"><xsl:value-of select="frameModel/@title" /></h1>
					  </xsl:when>
					  <xsl:when test="frameModel/@titleKey">
						<h1 class="page-header"><xsl:value-of select="hatoka:getText($localizer, frameModel/@titleKey, frameModel/@titleKey)" /></h1>
					  </xsl:when>
					  <xsl:otherwise>
						<h1 class="page-header">no title</h1>
					  </xsl:otherwise>
					</xsl:choose>
					<xsl:apply-templates select="frameModel/messages" />
					<xsl:value-of select="frameModel/content" disable-output-escaping="yes" />
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="message" xmlns="http://www.w3.org/1999/xhtml">
      <p class="bg-info">
        <xsl:value-of select="hatoka:getText($localizer, @messageKey, '', @parameter1, @parameter2, @parameter3)" />
      </p>
	</xsl:template>
	<!-- MAIN MENU TEMPLATE -->
	<xsl:template match="menuItem" xmlns="http://www.w3.org/1999/xhtml">
		<li>
			<xsl:if test="@isActive = 'true'">
				<xsl:attribute name="class">active</xsl:attribute>
			</xsl:if>
			<a href="{@uri}">
				<xsl:if test="@hasAddUri = 'false'">
					<xsl:attribute name="class">no-glyphicon</xsl:attribute>
				</xsl:if>
				<xsl:choose>
				  <xsl:when test="@title">
				   <xsl:value-of select="@title" />
				  </xsl:when>
				  <xsl:when test="@titleKey">
				   <xsl:value-of select="hatoka:getText($localizer, @titleKey, @titleKey)" />
				  </xsl:when>
				  <xsl:otherwise>
				    ... (no name) ...
				  </xsl:otherwise>
				</xsl:choose>
				<xsl:if test="@count">
					 <span class="badge"><xsl:value-of select="@count" /></span>
				</xsl:if>
			</a>
			<xsl:if test="@hasAddUri = 'true'">
				<a class="glyphicon glyphicon-plus" href="{@uriAdd}">&#160;</a>
			</xsl:if>
		</li>
	</xsl:template>
</xsl:stylesheet>
