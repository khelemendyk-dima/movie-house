import axiosInstance from "../api/apiClient";
import { Ticket } from "../types/Ticket";

export const fetchTicketsBySession = async (sessionId: number): Promise<Ticket[]> => {
    const response = await axiosInstance.get(`/sessions/${sessionId}/tickets`);
    return response.data;
};
