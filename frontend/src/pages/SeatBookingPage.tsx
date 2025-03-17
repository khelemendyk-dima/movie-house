import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Button, Container, Typography, Grid, Box, TextField, Tooltip } from "@mui/material";
import { Cancel } from "@mui/icons-material";
import { MuiTelInput } from "mui-tel-input";
import { parsePhoneNumber } from "libphonenumber-js";

const SeatBookingPage = () => {
    const { sessionId } = useParams();
    const navigate = useNavigate();
    const [session, setSession] = useState(null);
    const [movie, setMovie] = useState(null);
    const [seats, setSeats] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [userInfo, setUserInfo] = useState({ name: "", email: "", phone: "" });
    const [seatCountInRow, setSeatCountInRow] = useState(0);
    const [errors, setErrors] = useState({ name: "", email: "", phone: "" });

    useEffect(() => {
        fetch(`http://localhost:8080/api/sessions/${sessionId}`)
            .then((res) => res.json())
            .then((data) => {
                setSession(data);
                return fetch(`http://localhost:8080/api/movies/${data.movieId}`);
            })
            .then((res) => res.json())
            .then(setMovie);

        fetch(`http://localhost:8080/api/sessions/${sessionId}/occupancy`)
            .then((res) => res.json())
            .then((data) => {
                setSeats(data);
                // Count the number of seats in the first row (assuming all rows have the same number of seats)
                if (data.length > 0) {
                    setSeatCountInRow(data.filter((seat) => seat.rowNumber === data[0].rowNumber).length);
                }
            });
    }, [sessionId]);

    const toggleSeatSelection = (seatId) => {
        setSelectedSeats((prev) =>
            prev.includes(seatId) ? prev.filter((id) => id !== seatId) : [...prev, seatId]
        );
    };

    const validateFields = () => {
        const newErrors = { name: "", email: "", phone: "" };
        let isValid = true;

        if (!userInfo.name.trim()) {
            newErrors.name = "Name is required.";
            isValid = false;
        }

        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!userInfo.email.trim()) {
            newErrors.email = "Email is required.";
            isValid = false;
        } else if (!emailRegex.test(userInfo.email)) {
            newErrors.email = "Please enter a valid email address.";
            isValid = false;
        }

        try {
            const phoneNumber = parsePhoneNumber(userInfo.phone);
            if (!phoneNumber?.isValid()) {
                newErrors.phone = "Please enter a valid phone number.";
                isValid = false;
            }
        } catch (error) {
            newErrors.phone = "Invalid phone number format.";
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const formatPhoneForBackend = (phone: string) => phone.replace(/[^\d+]/g, "");

    const handleBooking = () => {
        if (!validateFields()) return;

        const bookingData = {
            name: userInfo.name,
            email: userInfo.email,
            phone: formatPhoneForBackend(userInfo.phone),
            sessionId: Number(sessionId),
            seatIds: selectedSeats,
        };

        fetch("http://localhost:8080/api/bookings", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(bookingData),
        })
            .then((res) => res.json())
            .then((booking) => {
                return fetch("http://localhost:8080/api/payments/checkout-session", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        bookingId: booking.bookingId,
                        successUrl: `http://localhost:5173/booking/${booking.bookingId}/success`,
                        cancelUrl: "http://localhost:5173/booking/cancel",
                    }),
                });
            })
            .then((res) => res.text())
            .then((paymentUrl) => {
                window.location.href = paymentUrl;
            });
    };

    if (!session || !movie) return;

    const groupedSeats = seats.reduce((acc, seat) => {
        if (!acc[seat.rowNumber]) acc[seat.rowNumber] = [];
        acc[seat.rowNumber].push(seat);
        return acc;
    }, {});

    const screenArcWidth = seatCountInRow * 58;

    return (
        <Container>
            {/* Booking info */}
            <Grid container spacing={3} sx={{ mt: 3 }}>
                {/* Movie Poster and Info */}
                <Grid item xs={12} md={3}>
                    <Box
                        component="img"
                        src={movie.posterUrl}
                        alt={movie.title}
                        sx={{ width: "80%", borderRadius: 2, maxWidth: "100%", ml: 3 }}
                    />
                </Grid>

                {/* Movie Info */}
                <Grid item xs={12} md={5}>
                    <Typography variant="h5" sx={{ fontWeight: "bold" }}>
                        {movie.title}
                    </Typography>
                    <Typography variant="subtitle1" sx={{ mt: 2, ml: 3, fontSize: 20 }}>
                        Date: {new Date(session.startTime).toLocaleDateString()}
                    </Typography>
                    <Typography variant="subtitle1" sx={{ ml: 3, fontSize: 20 }}>
                        Time: {new Date(session.startTime).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}
                    </Typography>
                    <Typography variant="subtitle1" sx={{ ml: 3, fontSize: 20 }}>
                        Duration: {movie.duration} min
                    </Typography>
                </Grid>

                {/* Booking Form */}
                <Grid item xs={12} md={4}>
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
                            defaultCountry="US" // можно поменять на нужную страну
                            forceCallingCode // оставляет код страны всегда видимым
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
                </Grid>
            </Grid>

            {/* Seat Selection - Display the Hall */}
            <Box sx={{ position: "relative", mt: 13 }}>
                <Box
                    sx={{
                        width: `${screenArcWidth}px`,
                        height: "40px",
                        borderRadius: "50% 50% 0 0",
                        backgroundColor: "#e0e0e0",
                        position: "absolute",
                        top: -60,
                        left: `calc(50% - ${screenArcWidth / 2}px)`,
                    }}
                />
                <Typography
                    variant="h6"
                    sx={{
                        position: "absolute",
                        top: -50,
                        left: "50%",
                        transform: "translateX(-50%)",
                    }}
                >
                    Screen
                </Typography>
            </Box>

            <Grid container spacing={1} justifyContent="center" sx={{ mt: 2 }}>
                {Object.keys(groupedSeats).map((row) => (
                    <Grid item xs={12} key={row}>
                        <Grid container spacing={1} justifyContent="center">
                            {groupedSeats[row].map((seat) => (
                                <Grid item key={seat.seatId}>
                                    <Tooltip
                                        title={
                                            seat.status === "RESERVED"
                                                ? "Reserved"
                                                : `Row: ${seat.rowNumber}, Seat: ${seat.seatNumber}`
                                        }
                                        arrow
                                    >
                                        <Button
                                            variant="outlined"
                                            color={
                                                selectedSeats.includes(seat.seatId)
                                                    ? "error"
                                                    : seat.status === "RESERVED"
                                                        ? "default"
                                                        : "primary"
                                            }
                                            disabled={seat.status === "RESERVED"}
                                            onClick={() => toggleSeatSelection(seat.seatId)}
                                            sx={{
                                                minWidth: 50,
                                                minHeight: 50,
                                                backgroundColor:
                                                    seat.status === "RESERVED"
                                                        ? "#B0B0B0"
                                                        : selectedSeats.includes(seat.seatId)
                                                            ? "red"
                                                            : "transparent",
                                                borderColor:
                                                    seat.status === "RESERVED"
                                                        ? "grey"
                                                        : "red",
                                                position: "relative",
                                                '&:hover': {
                                                    backgroundColor:
                                                        seat.status === "RESERVED"
                                                            ? "#B0B0B0"
                                                            : selectedSeats.includes(seat.seatId)
                                                                ? "darkred"
                                                                : "#ddd",
                                                },
                                            }}
                                        >
                                            {seat.status === "RESERVED" && (
                                                <Cancel sx={{ color: "white", fontSize: 18 }} />
                                            )}
                                        </Button>
                                    </Tooltip>
                                </Grid>
                            ))}
                        </Grid>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default SeatBookingPage;
