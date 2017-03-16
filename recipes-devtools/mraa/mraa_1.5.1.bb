require mraa.inc

#SRC_URI = "git://github.com/intel-iot-devkit/mraa.git;protocol=git;tag=v${PV}"
SRC_URI = "git://github.com/intel-iot-devkit/mraa.git;protocol=git;rev=1c4b1fc329a282199974224786faa1d3fe695c49"

do_install_append_intel-aero() {
   install -d ${D}${datadir}/mraa
   install -m 0644 ${S}/examples/platform/intel-aero.json ${D}${datadir}/mraa
}
