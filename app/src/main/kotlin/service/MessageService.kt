package com.github.mpetuska.shak.service

import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.repository.MessageRepository
import com.github.mpetuska.shak.util.SHA256
import com.github.mpetuska.shak.util.sha256


class MessageService(
  private val repository: MessageRepository,
) {
  suspend fun save(message: String): Message = repository.save(message.sha256(), message)

  suspend fun find(sha256: SHA256): Message? = repository.find(sha256)

  suspend fun findMessage(sha256: SHA256): String? = find(sha256)?.message

  suspend fun delete(sha256: SHA256): Int = repository.delete(sha256)
}