require upm.inc

EXTRA_OECMAKE_light-lake = "-DMODULE_LIST="lp8860;si1132;ads1x15;hlg150h;max44009;bmp180;bmpx8x;si7005;ds1808lc;t6713;bme280" "
EXTRA_OECMAKE_intel-aero = "-DMODULE_LIST="bmi160;bmx055;ms5611" -DBUILDEXAMPLES=ON -DCMAKE_SKIP_BUILD_RPATH=TRUE"

do_install_append_intel-aero() {
   install -d ${D}${bindir}
   install -m 0755 ${B}/examples/bmi160-example-cxx ${D}${bindir}
   install -m 0755 ${B}/examples/bmm150-example-cxx ${D}${bindir}
   install -m 0755 ${B}/examples/ms5611-example-cxx ${D}${bindir}
   install -d ${D}${datadir}/upm
   install -d ${D}${datadir}/upm/javascript
   install -m 0644 ${S}/examples/javascript/bmi160.js ${D}${datadir}/upm/javascript
   install -m 0644 ${S}/examples/javascript/bmm150.js ${D}${datadir}/upm/javascript
   install -m 0644 ${S}/examples/javascript/ms5611.js ${D}${datadir}/upm/javascript
}

FILES_${PN} += "${bindir}"
FILES_node-${PN} += "${datadir}/upm/javascript"
