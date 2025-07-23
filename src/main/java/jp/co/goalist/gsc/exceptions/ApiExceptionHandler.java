package jp.co.goalist.gsc.exceptions;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jp.co.goalist.gsc.common.ErrorMessage;
import jp.co.goalist.gsc.common.SystemMessage;
import jp.co.goalist.gsc.gen.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private void printParentCause(Throwable ex) {
        if (ex.getCause() != null) {
            Throwable parent = ex.getCause();
            log.error("Parent{} | {}", parent.getClass(), parent.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(
            Exception ex,
            HttpServletRequest request) {
        ex.printStackTrace();
        log.error("InternalServerError: {}", ex.getMessage());
        return ErrorResponse.builder()
                .statusCode(SystemMessage.UNKNOWN_ERROR.getStatusCode())
                .message(SystemMessage.UNKNOWN_ERROR.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        printParentCause(ex);

        return ErrorResponse.builder()
                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(BadValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadValidationException(Exception ex) {
        return ((BadValidationException) ex).getError();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        printParentCause(ex);
        Optional<FieldError> opFieldError = ex.getBindingResult().getFieldErrors().stream().findFirst();
        if (opFieldError.isPresent()) {
            FieldError fieldError = opFieldError.get();
            return ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), fieldError.getField()))
                    .fieldError(fieldError.getField())
                    .build();
        }

        Optional<ObjectError> opObjectError = ex.getBindingResult().getGlobalErrors().stream().findFirst();
        if (opObjectError.isPresent()) {
            ObjectError objectError = opObjectError.get();
            String field = null;
            if (objectError.getArguments() != null) {
                // get the first argument
                @SuppressWarnings("null")
                String[] secondArg = (String[]) objectError.getArguments()[1];
                field = secondArg[0];
            }
            return ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(objectError.getDefaultMessage())
                    .fieldError(field)
                    .build();
        }

        return ErrorResponse.builder()
                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                .message(ErrorMessage.INVALID_DATA.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        log.error("AuthenticationException: {}", ex.getLocalizedMessage());
        return ErrorResponse.builder()
                .statusCode(ErrorMessage.BAD_AUTHENTICATION_ERROR.getStatusCode())
                .message(ErrorMessage.BAD_AUTHENTICATION_ERROR.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {
        return ex.getError();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request) {
        return ErrorResponse.builder()
                .statusCode(ErrorMessage.UNAUTHORIZED.getStatusCode())
                .message(ErrorMessage.UNAUTHORIZED.getMessage())
                .fieldError("email")
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        return ErrorResponse.builder()
                .statusCode(ErrorMessage.PERMISSION_DENIED.getStatusCode())
                .message(ErrorMessage.PERMISSION_DENIED.getMessage())
                .build();
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleHttpMediaTypeNotSupportedException(
            org.springframework.web.HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {
        return ErrorResponse.builder()
                .statusCode(SystemMessage.UNSUPPORTED_API.getStatusCode())
                .message(SystemMessage.UNSUPPORTED_API.getMessage())
                .build();
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(
            org.springframework.web.servlet.resource.NoResourceFoundException ex,
            HttpServletRequest request) {
        return ErrorResponse.builder()
                .statusCode(SystemMessage.UNSUPPORTED_API.getStatusCode())
                .message(SystemMessage.UNSUPPORTED_API.getMessage())
                .build();
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        ex.printStackTrace();
        // Extract the cause from the exception
        Throwable cause = ex.getCause();
        String fieldName = "";

        // Check if the cause is a JsonMappingException (a common cause for invalid field names)
        if (cause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) cause;

            // Extract the path that caused the exception
            Iterator<JsonMappingException.Reference> pathIterator = jsonMappingException.getPath().iterator();
            if (pathIterator.hasNext()) {
                JsonMappingException.Reference reference = pathIterator.next();
                // Get the field name from the path
                fieldName = reference.getFieldName();
            }
        }
        return ErrorResponse.builder()
                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                .message(String.format(ErrorMessage.INVALID_DATA.getMessage(), fieldName))
                .fieldError(fieldName)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        Iterator<ConstraintViolation<?>> iterator = ex.getConstraintViolations().iterator();
        if (iterator.hasNext()) {
            ConstraintViolation<?> violation = iterator.next();
            return ErrorResponse.builder()
                    .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                    .message(violation.getMessage())
                    .fieldError(violation.getPropertyPath().toString())
                    .build();
        }

        return ErrorResponse.builder()
                .statusCode(ErrorMessage.INVALID_DATA.getStatusCode())
                .message(ErrorMessage.INVALID_DATA.getMessage())
                .build();
    }

    @ExceptionHandler(BulkImportParseException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBulkImportParseException(Exception ex) {
        return ((BulkImportParseException) ex).getError();
    }

    @ExceptionHandler(BulkImportInvalidFileContentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBulkImportInvalidFileContentException(Exception ex) {
        return ((BulkImportInvalidFileContentException) ex).getError();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            Exception ex) {
        return ((ForbiddenException) ex).getError();
    }
}
