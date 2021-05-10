package com.github.mpetuska.shak.service

import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.repository.MessageRepository
import com.github.mpetuska.shak.util.SHA256
import com.github.mpetuska.shak.util.sha256

class MessageService(
  private val repository: MessageRepository,
) {
  suspend fun save(message: String): Pair<Message, Boolean> {
    val sha256 = message.sha256()
    return repository.find(sha256)?.let { it to false } ?: (repository.save(sha256, message) to true)
  }

  suspend fun find(sha256: SHA256): Message? = repository.find(sha256)

  suspend fun findMessage(sha256: SHA256): String? = find(sha256)?.message

  suspend fun delete(sha256: SHA256): Int = repository.delete(sha256)
}
