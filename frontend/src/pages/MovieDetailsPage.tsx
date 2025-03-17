import { useEffect, useState, useCallback } from "react";
import { useParams } from "react-router-dom";
import { Container, Grid, CircularProgress, Typography } from "@mui/material";
import MovieInfo from "../components/MovieInfo";
import DateDropdown from "../components/DateDropdown";
import Sessions from "../components/Sessions";
import { useUserStore } from "../stores/userStore";
import SessionsAdminTable from "../components/SessionsAdminTable";
import { Movie, Session } from "../types";
import { fetchMovieById } from "../services/movieService";
import { fetchSessionsByMovie } from "../services/sessionService";

const MovieDetailsPage = () => {
    const user = useUserStore((state) => state.user);
    const isAdmin = user?.role === "ADMIN";
    const gridValueForSessions = isAdmin ? 8 : 6;

    const { id } = useParams<{ id: string }>();
    const [movie, setMovie] = useState<Movie | null>(null);
    const [sessions, setSessions] = useState<Session[]>([]);
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split("T")[0]);
    const [loading, setLoading] = useState(true);

    const fetchMovieData = useCallback(async () => {
        if (!id) return;
        setLoading(true);

        try {
            const [movieRes, sessionsRes] = await Promise.all([
                fetchMovieById(id),
                fetchSessionsByMovie(id, selectedDate)
            ]);

            setMovie(movieRes);
            setSessions(sessionsRes);
        } catch (error) {
            console.error("Error loading data:", error);
        } finally {
            setLoading(false);
        }
    }, [id, selectedDate]);

    useEffect(() => {
        fetchMovieData();
    }, [fetchMovieData]);

    if (loading) return <CircularProgress sx={{ display: "block", margin: "auto", mt: 4 }} />;

    if (!movie) return <Typography variant="h5" align="center" mt={4}>Movie not found</Typography>;

    return (
        <Container sx={{ mt: 4, mb: 4 }}>
            <Grid container spacing={4} justifyContent="space-evenly">
                <Grid item xs={12} md={4}>
                    <MovieInfo movie={movie} />
                </Grid>

                <Grid item xs={12} md={gridValueForSessions}>
                    {isAdmin ? (
                        <SessionsAdminTable movieId={movie.id} />
                    ) : (
                        <>
                            <DateDropdown selectedDate={selectedDate} onDateChange={setSelectedDate} />
                            <Sessions sessions={sessions} />
                        </>
                    )}
                </Grid>
            </Grid>
        </Container>
    );
};

export default MovieDetailsPage;
