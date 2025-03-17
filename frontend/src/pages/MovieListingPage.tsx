import { useEffect, useState } from "react";
import { Container, Typography, CircularProgress } from "@mui/material";
import DatePagination from "../components/DatePagination";
import MovieGrid from "../components/MovieGrid";
import { fetchMoviesByDate } from "../services/movieService";
import { Movie } from "../types";

const MovieListingPage = () => {
    const today = new Date().toISOString().split("T")[0]; // YYYY-MM-DD
    const [selectedDate, setSelectedDate] = useState(today);
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadMovies = async () => {
            if (movies.length === 0) setLoading(true);
            setError(null);
            try {
                const data = await fetchMoviesByDate(selectedDate);
                setMovies(data);
            } catch (err) {
                setError("Failed to load movies");
            } finally {
                setLoading(false);
            }
        };

        loadMovies();
    }, [selectedDate]);

    return (
        <Container maxWidth="lg">
            <DatePagination selectedDate={selectedDate} onDateChange={setSelectedDate} />

            {loading && <CircularProgress sx={{ display: "block", mx: "auto", my: 4 }} />}
            {error && <Typography color="error" textAlign="center">{error}</Typography>}

            {!loading && !error && <MovieGrid movies={movies} />}
        </Container>
    );
};

export default MovieListingPage;
