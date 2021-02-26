package com.example.demo

import org.apache.kafka.clients.admin.NewTopic
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@SpringBootApplication
class DemoApplication {
    private val logger = LoggerFactory.getLogger(DemoApplication::class.java)
    private val exec: TaskExecutor = SimpleAsyncTaskExecutor()

    @Bean
    fun converter(): RecordMessageConverter = StringJsonMessageConverter()

    @KafkaListener(topics = ["topic1"])
    fun listen(foo: User) {
        logger.info("Received: $foo")
        exec.execute { println("Received: $foo") }
    }

    @Bean
    fun topic(): NewTopic {
        return NewTopic("topic1", 1, 1.toShort())
    }

    @Bean
    @Profile("default") // Don't run from test(s)
    fun runner(): ApplicationRunner {
        return ApplicationRunner { _: ApplicationArguments ->
            println("Hit Enter to terminate...")
            System.`in`.read()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
