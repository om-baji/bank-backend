package config

import (
	"os"

	"github.com/cloudinary/cloudinary-go/v2"
)

func GetCld() (*cloudinary.Cloudinary, error) {

	return cloudinary.NewFromURL(os.Getenv("CLOUDINARY_URI"))
}
