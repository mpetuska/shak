package com.github.mpetuska.shak.service

import com.github.mpetuska.shak.model.Message
import com.github.mpetuska.shak.repository.MessageRepository
import com.github.mpetuska.shak.util.sha256
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.mockk

class MessageServiceTest : ShouldSpec({
  beforeTest {
    clearAllMocks()
  }

  val sample = "Hello there!"
  val message = Message(sample.sha256(), sample)
  val (sha, msg) = message
  context("find") {
    should("return a message when found") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns message
      val target = MessageService(repository)
      target.find(sha) shouldBe message

      coVerify { repository.find(sha) }
      confirmVerified(repository)
    }
    should("return null when not found") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns null
      val target = MessageService(repository)
      target.find(sha) shouldBe null

      coVerify { repository.find(sha) }
      confirmVerified(repository)
    }
  }

  context("findMessage") {
    should("return a message string when found") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns message
      val target = MessageService(repository)
      target.findMessage(sha) shouldBe msg

      coVerify { repository.find(sha) }
      confirmVerified(repository)
    }
    should("return null when not found") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns null
      val target = MessageService(repository)
      target.findMessage(sha) shouldBe null

      coVerify { repository.find(sha) }
      confirmVerified(repository)
    }
  }

  context("save") {
    should("generate SHA256, save and return a message when it does not exist") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns null
      coEvery { repository.save(sha, msg) } returns message
      val target = MessageService(repository)
      target.save(msg) shouldBe (message to true)

      coVerifySequence {
        repository.find(sha)
        repository.save(sha, msg)
      }
      confirmVerified(repository)
    }
    should("find and return a message when it does exist") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.find(sha) } returns message
      val target = MessageService(repository)
      target.save(msg) shouldBe (message to false)

      coVerifySequence { repository.find(sha) }
    }
  }

  context("delete") {
    should("delete a message and return 1 when it exists") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.delete(sha) } returns 1
      val target = MessageService(repository)
      target.delete(sha) shouldBeExactly 1

      coVerify { repository.delete(sha) }
      confirmVerified(repository)
    }
    should("return 0 if the message does not exist") {
      val repository = mockk<MessageRepository>()
      coEvery { repository.delete(sha) } returns 0
      val target = MessageService(repository)
      target.delete(sha) shouldBeExactly 0

      coVerify { repository.delete(sha) }
      confirmVerified(repository)
    }
  }
})
