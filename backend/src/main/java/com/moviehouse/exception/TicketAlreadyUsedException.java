package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.TICKET_IS_USED;
import static java.lang.String.format;

public class TicketAlreadyUsedException extends AlreadyExistsException {
    public TicketAlreadyUsedException(Long ticketId) {
        super(format(TICKET_IS_USED, ticketId));
    }
}
