package com.be.shoackserver.application.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class ImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private var bucket: String,
    private val amazonS3Client: AmazonS3

) {
    fun uploadMultipartFile(file: MultipartFile, category: String) : String {
        val objectMetaData = ObjectMetadata().apply {
            contentType = file.contentType
            contentLength = file.size
        }

        val uniqueFileName = file.originalFilename?.let { generateUniqueFileName(it) } ?: throw IllegalArgumentException("file name is empty")
        val path = generatePath(category, uniqueFileName)

        val putObjectRequest = PutObjectRequest(
            bucket,
            path,
            file.inputStream,
            objectMetaData
        )

        return try {
            amazonS3Client.putObject(putObjectRequest)
            uniqueFileName
        } catch (e: AmazonS3Exception) {
            throw IOException("Failed to upload file to S3", e)
        }
    }

    fun deleteImage(category: String, fileName: String) {
        val filePath = generatePath(category, fileName)
        amazonS3Client.deleteObject(bucket, filePath)
    }

    fun generatePath(category: String, fileName: String): String {
        return "$category/$fileName"
    }

    fun generateUniqueFileName(fileName: String): String {
        return UUID.randomUUID().toString() + fileName
    }

    fun generateS3URL(category: String, fileName: String): String {
        val filePath = generatePath(category, fileName)
        return amazonS3Client.getUrl(bucket, filePath).toString()
    }
}
