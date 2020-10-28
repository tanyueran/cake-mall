package com.github.tanyueran.controller;

import cn.hutool.core.lang.Snowflake;
import io.minio.*;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequestMapping("/file")
@RestController
@Api(tags = "FileController", description = "文件操作")
public class FileController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private Snowflake snowflake;

    @Value("${minio.bucket}")
    private String bucket;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        InputStream inputStream = file.getInputStream();
        long l = snowflake.nextId();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String[] arr = file.getOriginalFilename().split("\\.");
        String name = date + "/" + l + "." + (arr)[arr.length - 1];
        ObjectWriteResponse response = minioClient.putObject(
                PutObjectArgs.builder().bucket(bucket).object(name).stream(
                        inputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return response.object();
    }

    @GetMapping("/preview")
    @ApiOperation("文件预览")
    public void preview(@RequestParam("fileId") String fileId, HttpServletResponse response)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(fileId).build();
        InputStream is = minioClient.getObject(args);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }

    @DeleteMapping("/delete")
    @ApiOperation("文件删除")
    public Boolean deleteFile(@RequestParam("fileId") String fileId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucket).object(fileId).build());
        return true;
    }

    @GetMapping("/download")
    @ApiOperation("文件下载")
    public void download(@RequestParam("fileId") String fileId, HttpServletResponse response)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(fileId).build();
        InputStream is = minioClient.getObject(args);
        ObjectStat objectStat = minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(fileId).build());
        response.setContentType(objectStat.contentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileId, "UTF-8"));
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }
}
