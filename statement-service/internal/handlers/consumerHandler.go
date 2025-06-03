package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"statement-service/internal/events"
	"statement-service/internal/models"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func ConsumerHandler(ctx context.Context) {
	log.Default().Println("Consumer Running ", time.Now().GoString())
	StartPDFWorkers(5)

	consumer := events.Consumer()
	defer func() {
		consumer.Close()
		StopPDFWorkers()
	}()

	for {
		select {
		case <-ctx.Done():
			log.Println("Consumer received shutdown signal")
			return
		default:
			ev := consumer.Poll(100)
			switch e := ev.(type) {
			case *kafka.Message:
				var consumerObj models.ConsumerObject
				if err := json.Unmarshal(e.Value, &consumerObj); err != nil {
					fmt.Fprintf(os.Stderr, "Error unmarshalling Kafka message: %v\n", err)
					continue
				}
				PDFJobQueue <- consumerObj

			case kafka.Error:
				fmt.Fprintf(os.Stderr, "Kafka error: %v\n", e)
				return

			default:
				log.Printf("Ignored event: %T\n", e)
			}
		}
	}
}
