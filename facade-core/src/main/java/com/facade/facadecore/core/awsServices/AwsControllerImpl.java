package com.facade.facadecore.core.awsServices;

import static com.facade.facadecore.constant.RestEndpoint.REST_V1_AWS_SERVICES;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Puck Wang
 * @project facade-cloud
 * @created 5/25/2024
 */


@RestController
@RequestMapping(REST_V1_AWS_SERVICES)
@AllArgsConstructor
@Slf4j
public class AwsControllerImpl implements AwsController {

  private final AwsClient amazonClient;

  @Override
  @PostMapping("/upload")
  public ResponseEntity<String> upload(@RequestPart(value = "file") MultipartFile file) {
    String url = amazonClient.uploadFile(file);
    return ResponseEntity.ok(url);
  }


}
