export interface Seat {
    seatId: number;
    rowNumber: number;
    seatNumber: number;
    status: "RESERVED" | "FREE";
}