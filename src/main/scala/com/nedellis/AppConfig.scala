package com.nedellis

import java.net.InetAddress

object AppConfig {
  val ipAddress = InetAddress.getLocalHost.getHostAddress

  // Wrap TypesafeConfig
  // Additional Utilities
}
