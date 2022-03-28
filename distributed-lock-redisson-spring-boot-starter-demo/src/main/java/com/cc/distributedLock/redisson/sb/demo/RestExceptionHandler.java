package com.cc.distributedLock.redisson.sb.demo;

import com.cc.distributedLock.core.DistributedLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DistributedLockException.class)
    public ResponseEntity LockException(DistributedLockException distributedLockException){
        return new ResponseEntity<>(distributedLockException.getMsg(),
                null, HttpStatus.OK);
    }
}
