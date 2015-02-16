<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib" exclude-result-prefixes="hatoka"
>
  <xsl:template name="input">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <xsl:param name="type">text</xsl:param>
    <xsl:param name="isRequired">false</xsl:param>
    <xsl:param name="position">single</xsl:param>
    <xsl:param name="placeholderKey">
      <xsl:value-of select="concat('placeholder.', $name)" />
    </xsl:param>
    <label class="sr-only">
      <xsl:attribute name="for">
        <xsl:value-of select="concat('input', $name)" />
      </xsl:attribute>
      <xsl:value-of select="hatoka:getText($localizer, $placeholderKey, $placeholderKey)" />
    </label>
    <input class="form-control" id="input{$name}" type="{$type}" name="{$name}" value="{$value}">
      <xsl:attribute name="placeholder">
          <xsl:value-of select="hatoka:getText($localizer, $placeholderKey, $placeholderKey)" />
      </xsl:attribute>
      <xsl:if test="$type = 'password'">
        <xsl:attribute name="autocomplete">off</xsl:attribute>
        <xsl:attribute name="type">password</xsl:attribute>
        <xsl:attribute name="required">required</xsl:attribute>
      </xsl:if>
      <xsl:if test="$isRequired = 'true'">
        <xsl:attribute name="required">required</xsl:attribute>
      </xsl:if>
      <xsl:if test="$position = 'first'">
        <xsl:attribute name="autofocus">on</xsl:attribute>
        <xsl:attribute name="class">form-control first</xsl:attribute>
      </xsl:if>
      <xsl:if test="$position = 'middle'">
        <xsl:attribute name="class">form-control middle</xsl:attribute>
      </xsl:if>
      <xsl:if test="$position = 'last'">
        <xsl:attribute name="class">form-control last</xsl:attribute>
      </xsl:if>
    </input>
  </xsl:template>

  <xsl:template name="checkbox">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <label>
      <input class="checkbox" name="{$name}" value="{$value}" type="checkbox" />
    </label>
  </xsl:template>
  <xsl:template name="radiobutton">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <label>
      <input class="radio" name="{$name}" value="{$value}" type="radio" />
    </label>
  </xsl:template>

  <xsl:template name="button">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <xsl:param name="cssClass">btn btn-primary</xsl:param>
    <xsl:variable name="buttonKey">
      <xsl:value-of select="concat('button.', $name)" />
    </xsl:variable>
    <button class="{$cssClass}" type="submit" name="{$name}">
      <xsl:value-of select="hatoka:getText($localizer, $buttonKey, $buttonKey)" />
    </button>
  </xsl:template>

  <xsl:template name="formatDate">
    <xsl:param name="date" />
    <xsl:value-of select="hatoka:formatDate($localizer, $date)" />
  </xsl:template>
  <xsl:template name="formatMoney">
    <xsl:param name="money" />
    <xsl:if test="$money = '0USD'">-</xsl:if>
    <xsl:if test="$money != '0USD'">
      <xsl:value-of select="$money" />
    </xsl:if>
  </xsl:template>

  <xsl:template name="head">
    <xsl:param name="title">Title</xsl:param>
    <xsl:param name="cssHRef"></xsl:param>
    <head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
      <xsl:if test="$cssHRef != ''">
        <link rel="stylesheet" href="{$cssHRef}" />
      </xsl:if>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js">//</script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js">//</script>
    <title><xsl:value-of select="$title" /></title>
    </head>
  </xsl:template>
</xsl:stylesheet>