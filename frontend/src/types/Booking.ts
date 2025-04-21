export interface BookingRequest {
    name: string;
    email: string;
    phone: string;
    sessionId: number;
    seatIds: number[];
}

export interface BookingResponse {
    bookingId: number;
    name: string;
    email: string;
    phone: string;
    sessionId: number;
    seatIds: number[];
    totalAmount: number;
    createdAt: string;
}
