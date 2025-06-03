package handlers

import (
	"encoding/json"
	"fmt"
	"log"
	"os"
	"path/filepath"
	"statement-service/internal/config"
	"statement-service/internal/events"
	"statement-service/internal/models"
	"time"

	"github.com/jung-kurt/gofpdf"
)

func MakePDF(consumerObj models.ConsumerObject) {
	pdf := gofpdf.New("P", "mm", "A4", "")
	pdf.AddPage()
	pdf.SetFont("Arial", "B", 16)
	pdf.Cell(190, 10, fmt.Sprintf("Monthly Statement for %s", consumerObj.Username))
	pdf.Ln(15)

	grouped := make(map[string][]models.Transaction)
	for _, tx := range consumerObj.Transactions {
		grouped[tx.FromAccount] = append(grouped[tx.FromAccount], tx)
	}

	headers := []string{"Txn ID", "From Acc", "To Acc", "By", "Amount", "Timestamp"}
	colWidths := []float64{30, 30, 30, 25, 25, 45}

	for fromAcc, txs := range grouped {
		pdf.SetFont("Arial", "B", 13)
		pdf.Cell(190, 10, fmt.Sprintf("From Account: %s", fromAcc))
		pdf.Ln(10)

		pdf.SetFont("Arial", "B", 12)
		for i, header := range headers {
			pdf.CellFormat(colWidths[i], 10, header, "1", 0, "C", false, 0, "")
		}
		pdf.Ln(-1)

		pdf.SetFont("Arial", "", 11)
		for _, tx := range txs {
			row := []string{
				tx.TransactionID,
				tx.FromAccount,
				tx.ToAccount,
				tx.From,
				"₹" + tx.Amount,
				tx.Timestamp,
			}
			for i, val := range row {
				pdf.CellFormat(colWidths[i], 10, val, "1", 0, "C", false, 0, "")
			}
			pdf.Ln(-1)
		}
		pdf.Ln(5)
	}

	timestamp := time.Now().Format("20060102_150405")
	filename := fmt.Sprintf("statement_%s_%s.pdf", consumerObj.Username, timestamp)
	outputPath := filepath.Join("static", filename)

	err := os.MkdirAll("static", os.ModePerm)
	if err != nil {
		log.Panic("Failed to create output directory:", err)
	}

	err = pdf.OutputFileAndClose(outputPath)
	if err != nil {
		log.Panic("Failed to generate PDF:", err)
	}

	log.Println("PDF generated:", outputPath)

	url, _ := config.SetCloud(outputPath)

	producerMsg := models.ProducerObject{
		Statement: url,
		EventType: "statement_report",
		Username:  consumerObj.Username,
	}

	producerBytes, err := json.Marshal(producerMsg)
	if err != nil {
		log.Panic("Failed to marshal producer message:", err)
	}

	events.Producer(producerBytes)
}
