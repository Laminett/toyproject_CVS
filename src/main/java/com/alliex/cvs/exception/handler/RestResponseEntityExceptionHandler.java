package com.alliex.cvs.exception.handler;

import com.alliex.cvs.exception.ErrorCode;
import com.alliex.cvs.exception.ErrorResponse;
import com.alliex.cvs.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    //========================================================================================================
    //  Handle intentional exceptions
    //========================================================================================================

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<Object> handleInternalException(InternalException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getHttpStatus(), ex.getErrorCode(), ex.getMessage(), request);
    }

    //========================================================================================================
    //  Handle bad request exceptions
    //========================================================================================================

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    /**
     * Validate request message syntax.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getMessage().contains("Required request body is missing")) {
            String message = "Required request body is missing.";

            return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, message, request);
        }

        String message = "The request could not be understood by the server due to malformed syntax.";

        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, message, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler({MultipartException.class})
    protected ResponseEntity<Object> handleMultipartException(MultipartException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    /**
     * Conversion service error.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleConverterException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    //========================================================================================================
    //  Handle validation exceptions
    //========================================================================================================

    /**
     * Validate required request parameter that it is missing or not.
     */
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    /**
     * Validate request parameter by @Valid.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        String message = StringUtils.EMPTY;
        BindingResult result = ex.getBindingResult();

        for (ObjectError error : result.getAllErrors()) {
            if (StringUtils.isNotBlank(message)) {
                message += StringUtils.SPACE;
            }

            message += error.getDefaultMessage();
        }

        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, message, request);
    }

    /**
     * Validate multipart maxUploadSize.
     */
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
    }

    /**
     * Validate request parameter by programmatically.
     */
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handlePreConditionsException(RuntimeException ex, WebRequest request) {
        if (ex instanceof NullPointerException || ex instanceof IllegalArgumentException) {
            if (ex.getMessage() != null) {
                return handleExceptionInternal(ex, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_REQUEST, ex.getMessage(), request);
            }
        }

        return handleBaseException(ex, request);
    }

    //========================================================================================================
    //  Handle security exceptions
    //========================================================================================================

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        if (ex instanceof BadCredentialsException) {
            return handleExceptionInternal(ex, HttpStatus.UNAUTHORIZED, ErrorCode.BAD_CREDENTIAL, ex.getMessage(), request);
        }

        return handleExceptionInternal(ex, HttpStatus.UNAUTHORIZED, ErrorCode.ACCESS_DENIED, ex.getMessage(), request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, ex.getMessage(), request);
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    protected ResponseEntity<Object> handleUnsupportedOperationException(UnsupportedOperationException ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, ex.getMessage(), request);
    }

    //========================================================================================================
    //  Handle base exceptions
    //========================================================================================================

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleBaseException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, "Server failed to complete the request due to unexpected error.", request);
    }

    //========================================================================================================
    //  Handle exceptions finally
    //========================================================================================================

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status, ErrorCode code, String message, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(code, message);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception occurred: {}\n" +
                        "{}\n" +
                        "HttpStatus: {} {}\n" +
                        "Response: {}\n" +
                        "StackTrace:",
                ex, request, status.value(), status.getReasonPhrase(), body, ex);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
