<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib"
  exclude-result-prefixes="hatoka"
>
  <xsl:import href="de/hatoka/common/capi/app/xslt/ui.xsl" />
  <xsl:param name="localizer" />
  <!-- BEGIN OUTPUT -->
  <xsl:output method="html" encoding="UTF-8" indent="yes" />
  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
        <link rel="stylesheet/less" type="text/css" href="../resources/css/account.less" />
        <script src="../resources/js/libs/less-1.3.3.min.js" type="text/javascript">//</script>
        <title>
          <xsl:value-of select="hatoka:getText($localizer, 'login.head.title', 'Login')" />
        </title>
      </head>
      <body>
        <ul>
          <xsl:apply-templates />
        </ul>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="loginForm" xmlns="http://www.w3.org/1999/xhtml">
    <li class="login">
      <xsl:if test="loginFailed = 'true'">
        <div class="warn">
          <xsl:value-of select="hatoka:getText($localizer, 'login.warn.verifyCredentials', 'Please verify your credentials.')" />
        </div>
      </xsl:if>
      <xsl:if test="nowActive = 'true'">
        <div class="info">
          <xsl:value-of select="hatoka:getText($localizer, 'signin.success', 'Sign in successfully finished.')" />
        </div>
      </xsl:if>
      <h2>
        <xsl:value-of select="hatoka:getText($localizer, 'login.info.pleaseLogin', 'Please login:')" />
      </h2>
      <form action="login" method="POST">
        <input type="hidden" name="origin">
            <xsl:attribute name="value"><xsl:value-of select="origin" /></xsl:attribute>
        </input>
        <xsl:call-template name="input">
          <xsl:with-param name="name">login</xsl:with-param>
          <xsl:with-param name="value"><xsl:value-of select="login" /></xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="input">
          <xsl:with-param name="isPassword">true</xsl:with-param>
          <xsl:with-param name="name">password</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="button">
          <xsl:with-param name="name">login</xsl:with-param>
        </xsl:call-template>
      </form>
    </li>
  </xsl:template>
  <xsl:template match="signUpForm" xmlns="http://www.w3.org/1999/xhtml">
    <li class="registration">
      <xsl:if test="fillMandatoryFields = 'true'">
        <div class="warn">
          <xsl:value-of
            select="hatoka:getText($localizer, 'login.warn.fillMandatoryFields', 'Please fill all mandatory fields.')" />
        </div>
      </xsl:if>
      <xsl:if test="emailExists = 'true'">
        <div class="warn">
          <xsl:value-of
            select="hatoka:getText($localizer, 'signup.warn.emailExists', 'The entered email address exists.')" />
        </div>
      </xsl:if>
      <xsl:if test="tokenValidationFailed = 'true'">
        <div class="warn">
          <xsl:value-of
            select="hatoka:getText($localizer, 'signup.validationFailed', 'Validation failed.')" />
        </div>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="successfullyRegistered = 'true'">
          <div class="info">
            <xsl:value-of select="hatoka:getText($localizer, 'login.info.successfullyRegistered', 'Successfully registered. We send an email to you. Please follow the instruction.', email)" />
          </div>
        </xsl:when>
        <xsl:otherwise>
          <h2>
            <xsl:value-of select="hatoka:getText($localizer, 'login.info.signUp', 'Sign up now:')" />
          </h2>
          <form method="POST" action="signup">
            <xsl:call-template name="input">
              <xsl:with-param name="name">name</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="input">
              <xsl:with-param name="name">email</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="input">
              <xsl:with-param name="name">newPassword</xsl:with-param>
              <xsl:with-param name="isPassword">true</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="button">
              <xsl:with-param name="name">signUp</xsl:with-param>
            </xsl:call-template>
          </form>
        </xsl:otherwise>
      </xsl:choose>
    </li>
  </xsl:template>
</xsl:stylesheet>