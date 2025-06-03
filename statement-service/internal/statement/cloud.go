package statement

import (
	"fmt"
	"statement-service/internal/config"
)

func SetCloud() {
	cld, _ := config.GetCld()

	cld.Upload()

	fmt.Println(cld)
}