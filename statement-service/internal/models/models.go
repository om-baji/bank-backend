package models

type ConsumerObject struct {
	Transactions []Transaction
	Username     string
}

type Transaction struct {
	TransactionID string
	FromAccount   string
	ToAccount     string
	From          string
	Amount        string
	Timestamp     string
}

type ProducerObject struct {
	Username  string
	EventType string
	Statement string
}
