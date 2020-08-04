#!/usr/bin/env kotlin

/**
 * Generates a random base64 string for usage as API-Key
 *
 * $ kotlinc -script apiKeyGenerator.kts
 */

import java.security.SecureRandom
import java.util.Base64

private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

val randomString = (1..32)
      .map { kotlin.random.Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("");

val key = Base64.getEncoder().encodeToString(randomString.toByteArray(Charsets.UTF_8))

println("API Key:")
println(key)
