<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:param name="image"/>
  <xsl:param name="color"/>

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="context">
    <image source="{$image}">
      <xsl:apply-templates/>
    </image>
  </xsl:template>

  <xsl:template match="parameter">
    <xsl:if test="@name='label'">
      <text font="Viner Hand ITC" size="14" x="13" y="12" halign="left"
            valign="top" style="bold" color="{$color}" text="{@value}"/>
      <text font="Viner Hand ITC" size="14" x="12" y="11" halign="left"
            valign="top" style="bold" color="000000" text="{@value}"/>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>