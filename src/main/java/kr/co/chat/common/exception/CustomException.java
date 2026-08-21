package kr.co.chat.common.exception;

import kr.co.chat.common.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	 private final ResponseCode responseCode;
}
