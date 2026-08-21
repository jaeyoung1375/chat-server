package kr.co.chat.common.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

	String getCode();

	HttpStatus getHttpStatus();

	String getMessage();

}
