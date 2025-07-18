import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Container, Grid} from "@mui/material";

import {validateUserInfo} from "../utils/validation";
import BookingMovieInfo from "../components/BookingMovieInfo";
import BookingForm from "../components/BookingForm";
import SeatGrid from "../components/SeatGrid";
import Screen from "../components/Screen";
import {fetchSessionById, fetchSessionOccupancy} from "../services/sessionService";
import {fetchMovieById} from "../services/movieService";
import {createBooking} from "../services/bookingService";
import {createCheckoutSession} from "../services/paymentService";
import {BookingRequest} from "../types/Booking";
import {CheckoutSessionRequest} from "../types/CheckoutSession";
import {Movie} from "../types/Movie.ts";
import {Seat} from "../types/Seat.ts";

const SeatBookingPage = () => {
    const { sessionId } = useParams();
    const [session, setSession] = useState(null);
    const [movie, setMovie] = useState<Movie>();
    const [seats, setSeats] = useState<Seat[]>([]);
    const [selectedSeats, setSelectedSeats] = useState<number[]>([]);
    const [userInfo, setUserInfo] = useState({ name: "", email: "", phone: "" });
    const [seatCountInRow, setSeatCountInRow] = useState(0);
    const [errors, setErrors] = useState({ name: "", email: "", phone: "" });

    useEffect(() => {
        const loadData = async () => {
            try {
                const sessionData = await fetchSessionById(Number(sessionId));
                setSession(sessionData);

                const movieData = await fetchMovieById(sessionData.movieId);
                setMovie(movieData);

                const occupancyData = await fetchSessionOccupancy(Number(sessionId));
                setSeats(occupancyData);

                if (occupancyData.length > 0) {
                    const count = occupancyData.filter(
                        (seat: Seat) => seat.rowNumber === occupancyData[0].rowNumber
                    ).length;
                    setSeatCountInRow(count);
                }
            } catch (error) {
                console.error("Error fetching booking data:", error);
            }
        };

        if (sessionId) loadData();
    }, [sessionId]);

    const toggleSeatSelection = (seatId: number) => {
        setSelectedSeats((prev) =>
            prev.includes(seatId) ? prev.filter((id) => id !== seatId) : [...prev, seatId]
        );
    };

    const formatPhoneForBackend = (phone: string) => phone.replace(/[^\d+]/g, "");

    const handleBooking = async () => {
        const { isValid, errors: validationErrors } = validateUserInfo(userInfo);
        if (!isValid) {
            setErrors(validationErrors);
            return;
        }

        const bookingData: BookingRequest = {
            name: userInfo.name,
            email: userInfo.email,
            phone: formatPhoneForBackend(userInfo.phone),
            sessionId: Number(sessionId),
            seatIds: selectedSeats,
        };

        try {
            const booking = await createBooking(bookingData);

            const checkoutData: CheckoutSessionRequest = {
                bookingId: booking.bookingId,
                successUrl: `http://localhost:5173/booking/${booking.bookingId}/success`,
                cancelUrl: "http://localhost:5173/booking/cancel",
            };

            window.location.href = await createCheckoutSession(checkoutData);
        } catch (error) {
            console.error("Booking or checkout failed", error);
        }
    };

    if (!session || !movie) return;

    const groupedSeats = seats.reduce((acc: Record<string, Seat[]>, seat: Seat) => {
        if (!acc[seat.rowNumber]) acc[seat.rowNumber] = [];
        acc[seat.rowNumber].push(seat);
        return acc;
    }, {});

    const screenArcWidth = seatCountInRow * 58;

    return (
        <Container>
            <Grid container spacing={3} sx={{ mt: 3 }}>
                <BookingMovieInfo movie={movie} session={session} />
                <Grid item xs={12} md={4}>
                    <BookingForm
                        bookingUserInfo={userInfo}
                        setBookingUserInfo={setUserInfo}
                        errors={errors}
                        session={session}
                        selectedSeats={selectedSeats}
                        handleBooking={handleBooking}
                    />
                </Grid>
            </Grid>

            <Screen screenArcWidth={screenArcWidth} />

            <SeatGrid
                groupedSeats={groupedSeats}
                selectedSeats={selectedSeats}
                toggleSeatSelection={toggleSeatSelection}
            />
        </Container>
    );
};

export default SeatBookingPage;
