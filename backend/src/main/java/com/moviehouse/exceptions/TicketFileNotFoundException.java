package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.TICKET_PDF_BY_BOOKING_ID_NOT_FOUND;
import static java.lang.String.format;

public class TicketFileNotFoundException extends NotFoundException {
    public TicketFileNotFoundException(Long bookingId) {
        super(format(TICKET_PDF_BY_BOOKING_ID_NOT_FOUND, bookingId));
    }
}
