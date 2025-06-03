package api

import (
	"time"

	"github.com/gofiber/fiber/v2"
)

func HealthHandler(c *fiber.Ctx) error {
	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"status":  "ok",
		"message": "Server is healthy",
		"uptime":  time.Since(startTime).String(),
		"time":    time.Now().Format(time.RFC3339),
	})
}

var startTime = time.Now()
