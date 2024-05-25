package com.facade.facadecore.core.awsServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Puck Wang
 * @project facade-cloud
 * @created 5/25/2024
 */
public interface AwsController {

  ResponseEntity<String> upload(MultipartFile file);
}
