<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"
    encoding="UTF-8"
    xmlns="http://www.w3.org/1999/xhtml"
    indent="yes"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />

    <xsl:template match="loginForm">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><xsl:value-of select="messageInfo" /></title>
</head>
<body>
</body>
</html>

    </xsl:template>
</xsl:stylesheet>
