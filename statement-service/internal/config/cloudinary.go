package config

import (
	"context"
	"fmt"
	"os"

	"github.com/cloudinary/cloudinary-go/v2"
	"github.com/cloudinary/cloudinary-go/v2/api"
	"github.com/cloudinary/cloudinary-go/v2/api/uploader"
)

func SetCloud(file string) (string, error) {
	cld, err := cloudinary.NewFromURL(os.Getenv("CLOUDINARY_URI"))
	if err != nil {
		return "", fmt.Errorf("cloudinary config error: %w", err)
	}

	cld.Config.URL.Secure = true

	uploadResult, err := cld.Upload.Upload(context.Background(), file, uploader.UploadParams{
		Overwrite:      api.Bool(true),
		UniqueFilename: api.Bool(true),
	})
	if err != nil {
		return "", fmt.Errorf("upload failed: %w", err)
	}

	return uploadResult.SecureURL, nil
}
