import { Box, Button, TextField, Typography } from "@mui/material";
import { MuiTelInput } from "mui-tel-input";

const BookingForm = ({
                         userInfo,
                         setUserInfo,
                         errors,
                         session,
                         selectedSeats,
                         handleBooking
                     }) => {
    return (
        <Box sx={{ border: 1, borderColor: "gray", p: 2, borderRadius: 2, backgroundColor: "#f9f9f9" }}>
            <TextField
                fullWidth
                label="Name"
                value={userInfo.name}
                onChange={(e) => setUserInfo({ ...userInfo, name: e.target.value })}
                sx={{ mb: 2 }}
                error={!!errors.name}
                helperText={errors.name}
            />
            <TextField
                fullWidth
                label="Email"
                value={userInfo.email}
                onChange={(e) => setUserInfo({ ...userInfo, email: e.target.value })}
                sx={{ mb: 2 }}
                error={!!errors.email}
                helperText={errors.email}
            />
            <MuiTelInput
                fullWidth
                label="Phone"
                value={userInfo.phone}
                onChange={(value) => setUserInfo({ ...userInfo, phone: value })}
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
