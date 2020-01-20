<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:param name="label"/>

  <xsl:template match="/">
    <image width="583" height="60" bgcolor="ff0000">
      <xsl:apply-templates/>
    </image>
  </xsl:template>

  <xsl:template match="s1|faqs|changes">
      <xsl:variable name="title">
        <xsl:if test="string-length(@title)=0">
          <xsl:value-of select="$label"/>
        </xsl:if>
        <xsl:if test="string-length(@title)>0">
          <xsl:value-of select="@title"/>
        </xsl:if>
      </xsl:variable>

      <text font="Viner Hand ITC" size="34" x="572" y="12" halign="right" valign="top" color="990000"
            text="{$title}"/>
      <text font="Viner Hand ITC" size="34" x="571" y="11" halign="right" valign="top" color="990000"
            text="{$title}"/>
      <text font="Viner Hand ITC" size="34" x="570" y="10" halign="right" valign="top" color="ffffff"
            text="{$title}"/>
  </xsl:template>

</xsl:stylesheet>