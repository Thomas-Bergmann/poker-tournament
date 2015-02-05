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
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="../resources/css/login.css" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js">//</script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js">//</script>
        <title>
          <xsl:value-of select="hatoka:getText($localizer, 'login.head.title', 'Login')" />
        </title>
      </head>
      <body>
        <div class="container">
          <xsl:apply-templates />
        </div>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="loginForm" xmlns="http://www.w3.org/1999/xhtml">
    <form class="form-signin" action="login" method="POST">
      <h2>
        <xsl:value-of select="hatoka:getText($localizer, 'login.info.pleaseLogin', 'Please login:')" />
      </h2>
    <xsl:if test="loginFailed = 'true'">
        <p class="bg-warning">
            <xsl:value-of
                select="hatoka:getText($localizer, 'login.warn.verifyCredentials', 'Please verify your credentials.')" />
        </p>
    </xsl:if>
    <xsl:if test="nowActive = 'true'">
        <p class="bg-info">
            <xsl:value-of
                select="hatoka:getText($localizer, 'signin.success', 'Sign in successfully finished.')" />
        </p>
    </xsl:if>
      <input type="hidden" name="origin">
          <xsl:attribute name="value"><xsl:value-of select="origin" /></xsl:attribute>
      </input>
      <xsl:call-template name="input">
        <xsl:with-param name="name">email</xsl:with-param>
        <xsl:with-param name="type">email</xsl:with-param>
        <xsl:with-param name="value"><xsl:value-of select="email" /></xsl:with-param>
        <xsl:with-param name="isRequired">true</xsl:with-param>
        <xsl:with-param name="position">first</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="input">
        <xsl:with-param name="type">password</xsl:with-param>
        <xsl:with-param name="name">password</xsl:with-param>
        <xsl:with-param name="position">last</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="button">
        <xsl:with-param name="name">login</xsl:with-param>
      </xsl:call-template>
    </form>
  </xsl:template>
  <xsl:template match="signUpForm" xmlns="http://www.w3.org/1999/xhtml">
      <xsl:choose>
        <xsl:when test="successfullyRegistered = 'true'">
          <p class="bg-info">
            <xsl:value-of select="hatoka:getText($localizer, 'login.info.successfullyRegistered', 'Successfully registered. We send an email to you. Please follow the instruction.', email)" />
          </p>
        </xsl:when>
        <xsl:otherwise>
          <form method="POST" action="signup" class="form-signin">
            <h2>
              <xsl:value-of select="hatoka:getText($localizer, 'login.info.signUp', 'Sign up:')" />
            </h2>
             <xsl:if test="fillMandatoryFields = 'true'">
               <p class="bg-warning">
                 <xsl:value-of
                   select="hatoka:getText($localizer, 'login.warn.fillMandatoryFields', 'Please fill all mandatory fields.')" />
               </p>
             </xsl:if>
             <xsl:if test="emailExists = 'true'">
               <p class="bg-warning">
                 <xsl:value-of
                   select="hatoka:getText($localizer, 'signup.warn.emailExists', 'The entered email address exists.')" />
               </p>
             </xsl:if>
             <xsl:if test="tokenValidationFailed = 'true'">
               <p class="bg-warning">
                 <xsl:value-of
                   select="hatoka:getText($localizer, 'signup.validationFailed', 'Validation failed.')" />
               </p>
             </xsl:if>
              <xsl:call-template name="input">
              <xsl:with-param name="name">name</xsl:with-param>
              <xsl:with-param name="isRequired">true</xsl:with-param>
              <xsl:with-param name="position">first</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="input">
              <xsl:with-param name="name">email</xsl:with-param>
              <xsl:with-param name="type">email</xsl:with-param>
              <xsl:with-param name="isRequired">true</xsl:with-param>
              <xsl:with-param name="position">middle</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="input">
              <xsl:with-param name="name">newPassword</xsl:with-param>
              <xsl:with-param name="type">password</xsl:with-param>
              <xsl:with-param name="position">last</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="button">
              <xsl:with-param name="name">signUp</xsl:with-param>
            </xsl:call-template>
          </form>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>
</xsl:stylesheet>