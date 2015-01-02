<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:hatoka="xalan://de.hatoka.common.capi.app.xslt.Lib" exclude-result-prefixes="hatoka"
>
  <xsl:template name="input">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <xsl:param name="isPassword">false</xsl:param>
    <xsl:param name="placeholderKey">
      <xsl:value-of select="concat('placeholder.', $name)" />
    </xsl:param>
    <div class="input">
      <input class="text" name="{$name}" value="{$value}" type="text">
        <xsl:attribute name="placeholder">
            <xsl:value-of select="hatoka:getText($localizer, $placeholderKey, $placeholderKey)" />
        </xsl:attribute>
        <xsl:if test="$isPassword = 'true'">
          <xsl:attribute name="autocomplete">off</xsl:attribute>
          <xsl:attribute name="type">password</xsl:attribute>
        </xsl:if>
      </input>
    </div>
  </xsl:template>
  <xsl:template name="checkbox">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <div class="checkbox">
      <input class="checkbox" name="{$name}" value="{$value}" type="checkbox" />
    </div>
  </xsl:template>
  <xsl:template name="radiobutton">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <div class="radio">
      <input class="radio" name="{$name}" value="{$value}" type="radio" />
    </div>
  </xsl:template>
  <xsl:template name="button">
    <xsl:param name="name" />
    <xsl:param name="value" />
    <xsl:variable name="buttonKey">
      <xsl:value-of select="concat('button.', $name)" />
    </xsl:variable>
    <div class="button">
      <input class="button" type="submit" name="{$name}">
        <xsl:attribute name="value">
            <xsl:value-of select="hatoka:getText($localizer, $buttonKey, $buttonKey)" />
        </xsl:attribute>
      </input>
    </div>
  </xsl:template>
  <xsl:template name="formatDate">
    <xsl:param name="date" />
    <xsl:value-of select="hatoka:formatDate($localizer, $date)" />
  </xsl:template>
</xsl:stylesheet>