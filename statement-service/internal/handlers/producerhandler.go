package handlers

import (
	"encoding/json"
	"statement-service/internal/events"
	"statement-service/internal/models"

	"github.com/gofiber/fiber/v2"
)

func StatementHandler(c *fiber.Ctx) error {
	var consumer models.ConsumerObject

	if err := c.BodyParser(&consumer); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"error": "Failed to parse request body: " + err.Error(),
		})
	}

	payload, err := json.Marshal(consumer)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{
			"error": "Failed to marshal request body: " + err.Error(),
		})
	}

	if err := events.Producer(payload); err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{
			"error": "Failed to produce event: " + err.Error(),
		})
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"message": "Statement processed successfully",
	})
}
