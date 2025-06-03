package handlers

import (
	"log"
	"statement-service/internal/models"
)

var PDFJobQueue chan models.ConsumerObject

func StartPDFWorkers(n int) {
	PDFJobQueue = make(chan models.ConsumerObject, 100)
	for i := 0; i < n; i++ {
		go func(workerID int) {
			for consumerObj := range PDFJobQueue {
				log.Printf("[Worker-%d] Processing PDF job for %s\n", workerID, consumerObj.Username)
				MakePDF(consumerObj)
			}
			log.Printf("[Worker-%d] Stopped\n", workerID)
		}(i)
	}
}

func StopPDFWorkers() {
	close(PDFJobQueue)
	log.Println("PDF job queue closed, workers will stop")
}
