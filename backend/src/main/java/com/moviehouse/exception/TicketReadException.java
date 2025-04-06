package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.TICKET_PDF_READING_ERROR;
import static java.lang.String.format;

public class TicketReadException extends RuntimeException {
    public TicketReadException(Long bookingId, String message) {
        super(format(TICKET_PDF_READING_ERROR, bookingId, message));
    }
}
