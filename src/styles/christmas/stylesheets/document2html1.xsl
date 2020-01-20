<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/XSL/Transform/1.0">

  <xsl:param name="stylebook.project"/>
  <xsl:param name="copyright"/>
  <xsl:param name="id"/>
  <xsl:key name="id" match="*" use="@id"/> 
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="s1">
    <html>
      <head>
        <script language="JavaScript" type="text/javascript" src="resources/script.js"/>
        <title><xsl:value-of select="@title"/></title>
      </head>
      <body text="#000000" link="#990000" vlink="#660000" alink="#ff0000"
            topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"
            bgcolor="#ffffff">
        <!-- THE TOP BAR (HEADER) -->
        <table border="0" cellspacing="0" cellpadding="0" width="725">
          <tr height="60">
            <td width="137" rowspan="2">
              <img src="resources/logo-1.gif" width="137" height="65" border="0" vspace="0" hspace="0" align="left"/></td>
            <td width="583">
              <img src="graphics/{$id}-header.jpg" width="583" height="60" border="0" vspace="0" hspace="0" align="left"/></td>
            <td width="5">
              <img src="resources/red-lo.gif" width="5" height="60" border="0" vspace="0" hspace="0" align="left"/></td>
          </tr>
          <tr height="5">
            <td width="588" colspan="2">
              <img src="resources/red-lo.gif" width="588" height="5" border="0" vspace="0" hspace="0" align="left"/></td>
          </tr>
          <tr height="16">
            <td width="137" rowspan="2">
              <img src="resources/logo-2.gif" width="137" height="16" border="0" vspace="0" hspace="0" align="left"/></td>
            <td width="588" colspan="2">
              <img src="resources/green-bar.gif" width="173" height="16" border="0" vspace="0" hspace="0" align="left"/>
              <img src="resources/green-bar-1.gif" width="8" height="16" border="0" vspace="0" hspace="0" align="left"/>
              <a href="http://www.xml.org/" target="new">
                <img src="resources/www.xml.org.gif" width="100" height="16" border="0" vspace="0" hspace="0" align="left"/></a>
              <a href="http://www.w3.org/" target="new">
                <img src="resources/www.w3.org.gif" width="100" height="16" border="0" vspace="0" hspace="0" align="left"/></a>
              <a href="http://www.apache.org/" target="new">
                <img src="resources/www.apache.org.gif" width="100" height="16" border="0" vspace="0" hspace="0" align="left"/></a>
              <a href="http://xml.apache.org/" target="new">
                <img src="resources/xml.apache.org.gif" width="100" height="16" border="0" vspace="0" hspace="0" align="left"/></a>
              <img src="resources/green-bar-2.gif" width="7" height="16" border="0" vspace="0" hspace="0" align="left"/></td>
          </tr>
      </table>
        <!-- THE MAIN PANEL (SIDEBAR AND CONTENT) -->
        <table border="0" cellspacing="0" cellpadding="0" width="725">
          <tr>
            <!-- THE SIDE BAR -->
            <td width="130" valign="top" align="left">
              <img src="resources/logo-3.gif" width="130" height="33" border="0" vspace="0" hspace="0"/><br/>
              <xsl:apply-templates select="document($stylebook.project)"/>
            </td>
            <!-- THE CONTENT PANEL -->
            <td width="595" valign="top" align="left">
              <table border="0" cellspacing="0" cellpadding="3">
                <tr><td><font face="arial,helvetica,sanserif" color="#000000">
                  <xsl:apply-templates select="s2"/>
                </font></td></tr>
              </table>
            </td>
          </tr>
        </table>
        <br/>
        <!-- THE COPYRIGHT NOTICE -->
        <table border="0" cellspacing="0" cellpadding="0" width="725">
          <tr>
            <td align="center" width="725">
              <img src="resources/green-hi.gif" width="715" height="1" border="0" vspace="0" hspace="0"/><br/>
              <img src="resources/green-lo.gif" width="715" height="1" border="0" vspace="0" hspace="0"/>
            </td>
          </tr>
          <tr>
            <td align="center" width="725"><font size="-1"><i>
              Copyright &#169; <xsl:value-of select="$copyright"/>.
              All Rights Reserved.
            </i><br/></font></td>
          </tr>
        </table>
      </body>
    </html>
  </xsl:template>

<!-- ###################################################################### -->
<!-- book -->

  <xsl:template match="book">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="document|faqs|changes|group">
    <xsl:if test="@id=$id">
      <img src="graphics/{@id}-label-1.jpg" width="130" height="30" hspace="0" vspace="0" border="0" alt="{@label}"/>
    </xsl:if>
    <xsl:if test="@id!=$id">
      <a href="{@id}.html" onMouseOver="rolloverOn('side-{@id}');" onMouseOut="rolloverOff('side-{@id}');">
        <img onLoad="rolloverLoad('side-{@id}','graphics/{@id}-label-2.jpg','graphics/{@id}-label-3.jpg');"
             name="side-{@id}" src="graphics/{@id}-label-3.jpg" width="130" height="30" hspace="0" vspace="0" border="0" alt="{@label}"/>
      </a>
    </xsl:if>
    <br/>
  </xsl:template>

  <xsl:template match="external">
    <xsl:variable name="extid" select="concat('ext-',position())"/>
    <a href="{@href}" onMouseOver="rolloverOn('side-{$extid}');" onMouseOut="rolloverOff('side-{$extid}');">
      <img onLoad="rolloverLoad('side-{$extid}','graphics/{$extid}-label-2.jpg','graphics/{$extid}-label-3.jpg');"
           name="side-{$extid}" src="graphics/{$extid}-label-3.jpg" width="130" height="30" hspace="0" vspace="0" border="0" alt="{@label}"/>
    </a>
    <br/>
  </xsl:template>

  <xsl:template match="separator">
    <img src="resources/separator.gif" width="130" height="20" hspace="0" vspace="0" border="0"/><br/>
  </xsl:template>


<!-- ###################################################################### -->
<!-- document -->

  <xsl:template match="s2">
    <table width="589" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td bgcolor="ffffff" colspan="2" width="589">
          <table width="589" cellspacing="0" cellpadding="0" border="0">
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="587" height="1"><img src="resources/green-lo.gif" width="587" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void-lo.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="00cc00" width="587">
                <font size="+1" face="arial,helvetica,sanserif" color="#ffffff">
                  <img src="resources/void.gif" width="2" height="2" vspace="0" hspace="0" border="0"/>
                  <b><xsl:value-of select="@title"/></b>
                </font>
              </td>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="587" height="1"><img src="resources/green-lo.gif" width="587" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
          </table>
        </td>
      </tr>  
      <tr>
        <td width="10">&#160;</td>
        <td width="579">
          <font face="arial,helvetica,sanserif" color="#000000">
            <xsl:apply-templates/>
          </font>
        </td>
      </tr>  
    </table>
    <br/>
  </xsl:template>

  <xsl:template match="s3">
    <table width="579" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td bgcolor="ffffff" colspan="2" width="579">
          <table width="579" cellspacing="0" cellpadding="0" border="0">
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="577" height="1"><img src="resources/green-lo.gif" width="577" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void-lo.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="00cc00" width="577">
              <font face="arial,helvetica,sanserif" color="#ffffff">
                <img src="resources/void.gif" width="2" height="2" vspace="0" hspace="0" border="0"/>
                <b><xsl:value-of select="@title"/></b>
              </font>
              </td>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="577" height="1"><img src="resources/green-lo.gif" width="577" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
          </table>
        </td>
      </tr>  
      <tr>
        <td width="10">&#160;</td>
        <td width="569">
          <font size="-1" face="arial,helvetica,sanserif" color="#000000">
            <xsl:apply-templates/>
          </font>
        </td>
      </tr>  
    </table>
    <br/>
  </xsl:template>

  <xsl:template match="s4">
    <table width="569" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td bgcolor="ffffff" colspan="2" width="569">
          <table width="569" cellspacing="0" cellpadding="0" border="0">
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="567" height="1"><img src="resources/green-lo.gif" width="567" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void-lo.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="00cc00" width="567">
              <font size="-1" face="arial,helvetica,sanserif" color="#ffffff">
                <img src="resources/void.gif" width="2" height="2" vspace="0" hspace="0" border="0"/>
                <b><xsl:value-of select="@title"/></b>
              </font>
              </td>
              <td bgcolor="009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
            <tr>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="567" height="1"><img src="resources/green-lo.gif" width="567" height="1" vspace="0" hspace="0" border="0"/></td>
              <td bgcolor="009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
            </tr>
          </table>
        </td>
      </tr>  
      <tr>
        <td width="10">&#160;</td>
        <td width="559">
          <font size="-1" face="arial,helvetica,sanserif" color="#000000">
            <xsl:apply-templates/>
          </font>
        </td>
      </tr>  
    </table>
    <br/>
  </xsl:template>

<!-- ###################################################################### -->
<!-- blocks -->

  <xsl:template match="p">
    <p><xsl:apply-templates/></p>
  </xsl:template>

  <xsl:template match="note">
    <table width="100%" cellspacing="3" cellpadding="0" border="0">
      <tr>
        <td width="20" valign="top">
          <img src="resources/note.gif" width="20" height="24" vspace="0" hspace="0" border="0" alt="Note"/>
        </td>
        <td valign="top">
          <font size="-1" face="arial,helvetica,sanserif" color="#000000">
            <i>
              <xsl:apply-templates/>
            </i>
          </font>
        </td>
      </tr>  
    </table>
  </xsl:template>

  <xsl:template match="ul">
    <ul><xsl:apply-templates/></ul>
  </xsl:template>

  <xsl:template match="ol">
    <ol><xsl:apply-templates/></ol>
  </xsl:template>

  <xsl:template match="li">
    <li><xsl:apply-templates/></li>
  </xsl:template>

  <xsl:template match="source">
  <div align="right">
  <table width="464" cellspacing="4" cellpadding="0" border="0">
    <tr>
      <td bgcolor="#009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
      <td bgcolor="#009900" width="462" height="1"><img src="resources/void.gif" width="462" height="1" vspace="0" hspace="0" border="0"/></td>
      <td bgcolor="#009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
    </tr>
    <tr>
      <td bgcolor="#009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
      <td bgcolor="#ffffff"  width="462">
          <font size="-1"><pre><xsl:apply-templates/></pre></font>
      </td>
      <td bgcolor="#009900" width="1"><img src="resources/void.gif" width="1" height="1" vspace="0" hspace="0" border="0"/></td>
    </tr>
    <tr>
      <td bgcolor="#009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
      <td bgcolor="#009900" width="462" height="1"><img src="resources/void.gif" width="462" height="1" vspace="0" hspace="0" border="0"/></td>
      <td bgcolor="#009900" width="1"   height="1"><img src="resources/void.gif" width="1"   height="1" vspace="0" hspace="0" border="0"/></td>
    </tr>
  </table>
  </div>
  </xsl:template>

  <xsl:template match="table">
    <table width="100%" border="0" cellspacing="2" cellpadding="2">
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="tr">
    <tr><xsl:apply-templates/></tr>
  </xsl:template>

  <xsl:template match="th">
    <td bgcolor="#006600" colspan="{@colspan}" rowspan="{@rowspan}" valign="center" align="center">
      <font color="#ffffff" size="-1" face="arial,helvetica,sanserif">
        <b><xsl:apply-templates/></b>&#160;
      </font>
    </td>
  </xsl:template>

  <xsl:template match="td">
    <td bgcolor="#009900" colspan="{@colspan}" rowspan="{@rowspan}" valign="top" align="left">
      <font color="#000000" size="-1" face="arial,helvetica,sanserif">
        <xsl:apply-templates/>&#160;
      </font>
    </td>
  </xsl:template>

  <xsl:template match="tn">
    <td bgcolor="#ffffff" colspan="{@colspan}" rowspan="{@rowspan}">
      &#160;
    </td>
  </xsl:template>

<!-- ###################################################################### -->
<!-- markup -->

  <xsl:template match="em">
    <b><xsl:apply-templates/></b>
  </xsl:template>

  <xsl:template match="ref">
    <i><xsl:apply-templates/></i>
  </xsl:template>
  
  <xsl:template match="code">
    <code><font face="courier, monospaced"><xsl:apply-templates/></font></code>
  </xsl:template>
  
  <xsl:template match="br">
    <br/>
  </xsl:template>
  
<!-- ###################################################################### -->
<!-- links -->

  <xsl:template match="link">
    <xsl:if test="string-length(@anchor)=0">
      <xsl:if test="string-length(@idref)=0">
        <xsl:apply-templates/>
      </xsl:if>
      <xsl:if test="string-length(@idref)>0">
        <a href="{@idref}.html"><xsl:apply-templates/></a>
      </xsl:if>
    </xsl:if>

    <xsl:if test="string-length(@anchor)>0">
      <xsl:if test="string-length(@idref)=0">
        <a href="#{@anchor}"><xsl:apply-templates/></a>
      </xsl:if>
      <xsl:if test="string-length(@idref)>0">
        <a href="{@idref}.html#{@anchor}"><xsl:apply-templates/></a>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <xsl:template match="anchor">
    <a name="{@name}"><xsl:comment>anchor</xsl:comment></a>
  </xsl:template>

  <xsl:template match="jump">
    <a href="{@href}"><xsl:apply-templates/></a>
  </xsl:template>

  <xsl:template match="img">
    <img src="images/{@src}" border="0" vspace="4" hspace="4" align="right"/>
  </xsl:template>

<!-- ###################################################################### -->
<!-- copy

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
-->
</xsl:stylesheet>