import axiosInstance from "../api/apiClient";
import { Ticket } from "../types/Ticket";
import { baseUrl } from "../api/apiClient";

export const fetchTicketsBySession = async (sessionId: number): Promise<Ticket[]> => {
    const response = await axiosInstance.get(`/sessions/${sessionId}/tickets`);
    return response.data;
};

export const getDownloadTicketUrl = (bookingId: string): string => {
    return `${baseUrl}/bookings/${bookingId}/tickets/download`;
};
