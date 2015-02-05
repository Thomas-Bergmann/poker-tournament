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
    <button class="btn btn-lg btn-primary btn-block" type="submit" name="{$name}">
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
</xsl:stylesheet>