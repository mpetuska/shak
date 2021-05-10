package com.github.mpetuska.shak.util

import java.security.MessageDigest

typealias MD5 = String
fun String.md5(): MD5 {
  return hashString(this, "MD5")
}

typealias SHA256 = String
fun String.sha256(): SHA256 {
  return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
  return MessageDigest
    .getInstance(algorithm)
    .digest(input.toByteArray())
    .fold("") { str, it -> str + "%02x".format(it) }
}
