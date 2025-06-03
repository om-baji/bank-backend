package events

import (
	"log"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func Producer(item []byte) error {

	producer, err := kafka.NewProducer(&kafka.ConfigMap{
		"bootstrap.servers": "localhost:9092",
		"client.id":         "myProducer",
		"acks":              "all",
	})

	if err != nil {
		log.Panic("Something went wrong!")
	}

	topic := "statement.generate"

	return producer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Value:          []byte(item),
	}, nil)
}
