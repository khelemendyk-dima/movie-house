package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.TICKET_PDF_SAVING_ERROR;
import static java.lang.String.format;

public class TicketSaveException extends RuntimeException {
    public TicketSaveException(Long bookingId, String message) {
        super(format(TICKET_PDF_SAVING_ERROR, bookingId, message));
    }
}
