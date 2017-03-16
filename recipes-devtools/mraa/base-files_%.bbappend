do_install_append_intel-aero() {
   printf "\n" >> ${D}${sysconfdir}/profile
   printf "MRAA_JSON_PLATFORM=/usr/share/mraa/intel-aero.json\n" >> ${D}${sysconfdir}/profile
   printf "export MRAA_JSON_PLATFORM\n" >> ${D}${sysconfdir}/profile
}
