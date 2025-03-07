package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.TICKET_IS_USED;
import static java.lang.String.format;

public class TicketAlreadyUsedException extends AlreadyExistsException {
    public TicketAlreadyUsedException(Long ticketId) {
        super(format(TICKET_IS_USED, ticketId));
    }
}
