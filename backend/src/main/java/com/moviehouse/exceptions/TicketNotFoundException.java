package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.TICKET_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(Long ticketId) {
        super(format(TICKET_BY_ID_NOT_FOUND, ticketId));
    }
}
