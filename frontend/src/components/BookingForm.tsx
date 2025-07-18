import { Box, Button, TextField, Typography } from "@mui/material";
import { MuiTelInput } from "mui-tel-input";
import { Session } from "../types/Session.ts";

interface BookingUserInfo {
    name: string;
    email: string;
    phone: string;
}

interface Errors {
    name?: string;
    email?: string;
    phone?: string;
}

interface BookingFormProps {
    bookingUserInfo: BookingUserInfo;
    setBookingUserInfo: (info: BookingUserInfo) => void;
    errors: Errors;
    session: Session;
    selectedSeats: number[];
    handleBooking: () => void;
}

const BookingForm = ({
                         bookingUserInfo,
                         setBookingUserInfo,
                         errors,
                         session,
                         selectedSeats,
                         handleBooking,
                     }: BookingFormProps) => {
    return (
        <Box sx={{ border: 1, borderColor: "gray", p: 2, borderRadius: 2, backgroundColor: "#f9f9f9" }}>
            <TextField
                fullWidth
                label="Name"
                value={bookingUserInfo.name}
                onChange={(e) => setBookingUserInfo({ ...bookingUserInfo, name: e.target.value })}
                sx={{ mb: 2 }}
                error={!!errors.name}
                helperText={errors.name}
            />
            <TextField
                fullWidth
                label="Email"
                value={bookingUserInfo.email}
                onChange={(e) => setBookingUserInfo({ ...bookingUserInfo, email: e.target.value })}
                sx={{ mb: 2 }}
                error={!!errors.email}
                helperText={errors.email}
            />
            <MuiTelInput
                fullWidth
                label="Phone"
                value={bookingUserInfo.phone}
                onChange={(value) => setBookingUserInfo({ ...bookingUserInfo, phone: value })}
                defaultCountry="US"
                forceCallingCode
                sx={{ mb: 2 }}
                error={!!errors.phone}
                helperText={errors.phone}
            />
            <Typography sx={{ mb: 2 }}>
                Total: ${(selectedSeats.length * session.price).toFixed(2)}
            </Typography>
            <Button
                variant="contained"
                color="error"
                onClick={handleBooking}
                disabled={selectedSeats.length === 0}
                sx={{ width: "100%" }}
            >
                Proceed to Payment
            </Button>
        </Box>
    );
};

export default BookingForm;
