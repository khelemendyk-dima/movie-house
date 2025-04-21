import axiosInstance from "../api/apiClient";
import { BookingRequest, BookingResponse } from "../types/Booking";

export const createBooking = async (bookingData: BookingRequest): Promise<BookingResponse> => {
    const response = await axiosInstance.post("/bookings", bookingData);
    return response.data;
};
