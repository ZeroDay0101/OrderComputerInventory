package org.example.ecommerceorderandinventory.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//
//        // Collect field errors
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );
//
//        // Optional: Include global errors
//        ex.getBindingResult().getGlobalErrors().forEach(error ->
//                errors.put(error.getObjectName(), error.getDefaultMessage())
//        );
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<String> entityNotFound(EntityNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//    @ExceptionHandler(EmptyResultDataAccessException.class)
//    public ResponseEntity<String> handleEmptyResult(EmptyResultDataAccessException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ex.getMessage());
//    }
//
//    @ExceptionHandler(AuthorizationDeniedException.class)
//    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials");
//    }


    // Handle generic exceptions
    @ExceptionHandler(Exception.class) // catches all exceptions
    public ResponseEntity<ProblemDetail> handleAllExceptions(Exception ex) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle(ex.getClass().getSimpleName());
        pd.setDetail(ex.getMessage());


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }

}
