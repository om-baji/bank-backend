package events

import (
	"log"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func Consumer() *kafka.Consumer {

	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": "localhost:9092",
		"group.id":          "micro-1",
		"auto.offset.reset": "earliest",
	})

	if err != nil {
		log.Panic("Something went wrong!", err)
	}

	topic := "bank.statement.event"

	err = consumer.Subscribe(topic, nil)

	if err != nil {
		log.Panic("Something went wrong!", err)
	}

	return consumer
}
