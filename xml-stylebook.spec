Name:          xml-stylebook
Version:       1.0
Release:       0.14.b3_xalan2.svn313293%{?dist}
Summary:       Apache XML Stylebook
License:       ASL 1.1
URL:           http://xml.apache.org/

# How to generate this tarball:
#  $ svn export http://svn.apache.org/repos/asf/xml/stylebook/trunk/@313293 xml-stylebook-1.0
#  $ rm -rf xml-stylebook-1.0/bin/* # unclear licensing
#  $ rm -rf xml-stylebook-1.0/styles/ibm-style # better not to include the logos
#  $ tar zcf xml-stylebook-1.0.tar.gz xml-stylebook-1.0
Source0:       %{name}-%{version}.tar.gz

# Patch to fix an NPE in Xalan-J2's docs generation (from JPackage)
Patch0:        %{name}-image-printer.patch

# Patch the build script to build javadocs
Patch1:        %{name}-build-javadoc.patch

BuildArch:     noarch

BuildRequires: java-devel >= 1:1.6.0
BuildRequires: java-javadoc
BuildRequires: jpackage-utils
BuildRequires: ant
BuildRequires: xml-commons-apis
BuildRequires: jaxp_parser_impl
BuildRequires: dejavu-sans-fonts
Requires:      java
Requires:      jpackage-utils
Requires:      xml-commons-apis
Requires:      jaxp_parser_impl

%description
Apache XML Stylebook is a HTML documentation generator.

%package       javadoc
Summary:       API documentation for %{name}
Group:         Documentation
Requires:      java-javadoc

%description   javadoc
%{summary}.

%package       demo
Summary:       Examples for %{name}
Group:         Development/Libraries
Requires:      %{name} = %{version}-%{release}

%description   demo
Examples demonstrating the use of %{name}.

%prep
%setup -q
%patch0 -p0
%patch1 -p0

# Don't include this sample theme because it contains an errant font
rm -r styles/christmas/

# Make sure upstream hasn't sneaked in any jars we don't know about
JARS=""
for j in `find -name "*.jar"`; do
  if [ ! -L $j ]; then
    JARS="$JARS $j"
  fi
done
if [ ! -z "$JARS" ]; then
   echo "These jars should be deleted and symlinked to system jars: $JARS"
   exit 1
fi

%build
export CLASSPATH=$(build-classpath xalan-j2 jaxp_parser_impl)
ant

# Build the examples (this serves as a good test suite)
pushd docs
rm run.bat
java -classpath "$(build-classpath xml-commons-apis):$(build-classpath jaxp_parser_impl):../bin/stylebook-%{version}-b3_xalan-2.jar" \
  org.apache.stylebook.StyleBook "targetDirectory=../results" book.xml ../styles/apachexml
popd

%install
# jars
install -pD -T bin/stylebook-%{version}-b3_xalan-2.jar \
  %{buildroot}%{_javadir}/%{name}.jar

# javadoc
install -d %{buildroot}%{_javadocdir}/%{name}
cp -pr build/javadoc/* %{buildroot}%{_javadocdir}/%{name}

# examples
install -d %{buildroot}%{_datadir}/%{name}
cp -pr docs %{buildroot}%{_datadir}/%{name}
cp -pr styles %{buildroot}%{_datadir}/%{name}
cp -pr results %{buildroot}%{_datadir}/%{name}

%files
%doc LICENSE.txt
%{_javadir}/*

%files javadoc
%{_javadocdir}/%{name}

%files demo
%{_datadir}/%{name}

%changelog
* Fri Dec 27 2013 Daniel Mach <dmach@redhat.com> - 1.0-0.14.b3_xalan2.svn313293
- Mass rebuild 2013-12-27

* Mon Jul 29 2013 Stanislav Ochotnicky <sochotnicky@redhat.com> - 1.0-0.13.b3_xalan2.svn313293
- Cleanup tarball content with unclear license
- Update to latest packaging guidelines

* Fri Jun 28 2013 Mikolaj Izdebski <mizdebsk@redhat.com> - 1.0-0.12.b3_xalan2.svn313293
- Rebuild to regenerate API documentation
- Resolves: CVE-2013-1571

* Fri Feb 15 2013 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.0-0.11.b3_xalan2.svn313293
- Rebuilt for https://fedoraproject.org/wiki/Fedora_19_Mass_Rebuild

* Sun Jul 22 2012 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.0-0.10.b3_xalan2.svn313293
- Rebuilt for https://fedoraproject.org/wiki/Fedora_18_Mass_Rebuild

* Sat Jan 14 2012 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.0-0.9.b3_xalan2.svn313293
- Rebuilt for https://fedoraproject.org/wiki/Fedora_17_Mass_Rebuild

* Mon Feb 07 2011 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.0-0.8.b3_xalan2.svn313293
- Rebuilt for https://fedoraproject.org/wiki/Fedora_15_Mass_Rebuild

* Sun Dec 12 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.7.b3_xalan2.svn313293
- Really fix FTBFS this time.

* Sun Dec 12 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.6.b3_xalan2.svn313293
- Fix FTBFS due to ant upgrade.

* Sat Jun 12 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.5.b3_xalan2.svn313293
- Link to local java API docs properly and fix requires on javadoc package.
- Build with source and target levels of 1.5 so we don't have to require 1.6.

* Mon Apr 22 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.4.b3_xalan2.svn313293
- Remove font from demo package to comply with guidelines. RHBZ #567912

* Mon Jan 11 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.3.b3_xalan2.svn313293
- Build the examples (this serves as a good test suite.)
- Patch the build script to build javadocs.
- Add a build dep on a font package because the JDK is missing a dependency
  to function correctly in headless mode. See RHBZ #478480 and #521523.

* Tue Jan 5 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.2.b3_xalan2.svn313293
- Add patch from JPackage to fix NPE in Xalan-J2 doc generation.

* Tue Jan 5 2010 Mat Booth <fedora@matbooth.co.uk> - 1.0-0.1.b3_xalan2.svn313293
- Initial stab at packaging trunk version of stylebook.
