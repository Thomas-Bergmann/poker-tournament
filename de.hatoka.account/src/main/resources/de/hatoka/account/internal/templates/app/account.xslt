<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" xmlns="http://www.w3.org/1999/xhtml" indent="yes"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" />
  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
        <link rel="stylesheet/less" type="text/css" href="../resources/css/account.less" />
        <script src="../resources/js/libs/less-1.3.3.min.js" type="text/javascript">//</script>
        <title>
          <xsl:value-of select="hatoka:getText($localizer, 'account.title.list', 'List of accounts')" />
        </title>
      </head>
      <body>
        <xsl:apply-templates />
        <h2>
          <xsl:value-of select="hatoka:getText($localizer, 'account.info.createAccount', 'Create new account:')" />
        </h2>
        <form method="POST" action="create">
          <xsl:call-template name="input">
            <xsl:with-param name="name">name</xsl:with-param>
          </xsl:call-template>
          <xsl:call-template name="button">
            <xsl:with-param name="name">create</xsl:with-param>
          </xsl:call-template>
        </form>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="accountListModel" xmlns="http://www.w3.org/1999/xhtml">
    <form method="POST" action="action">
      <table>
        <tr>
          <th>Select</th>
          <th>Name</th>
          <th>Owner</th>
          <th>Is active</th>
          <th>Is selected</th>
        </tr>
        <xsl:apply-templates />
      </table>
      <xsl:call-template name="button">
        <xsl:with-param name="name">select</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="button">
        <xsl:with-param name="name">delete</xsl:with-param>
      </xsl:call-template>
    </form>
  </xsl:template>
  <xsl:template match="accounts" xmlns="http://www.w3.org/1999/xhtml">
    <tr>
      <td>
        <xsl:call-template name="checkbox">
          <xsl:with-param name="name">accountID</xsl:with-param>
          <xsl:with-param name="value">
            <xsl:value-of select="id" />
          </xsl:with-param>
        </xsl:call-template>
      </td>
      <td>
        <xsl:value-of select="name" />
      </td>
      <td>
        <xsl:value-of select="owner" />
      </td>
      <td>
        <xsl:if test="active = 'true'">active</xsl:if>
        <xsl:if test="active != 'true'">inactive</xsl:if>
      </td>
      <td>
        <xsl:if test="selected = 'true'">yes</xsl:if>
        <xsl:if test="selected != 'true'">no</xsl:if>
      </td>
    </tr>
  </xsl:template>
</xsl:stylesheet>