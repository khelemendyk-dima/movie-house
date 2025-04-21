import { Box, Grid, Typography } from "@mui/material";

const BookingMovieInfo = ({ movie, session }) => {
    return (
        <>
            <Grid item xs={12} md={3}>
                <Box
                    component="img"
                    src={movie.posterUrl}
                    alt={movie.title}
                    sx={{ width: "80%", borderRadius: 2, maxWidth: "100%", ml: 3 }}
                />
            </Grid>

            <Grid item xs={12} md={5}>
                <Typography variant="h5" sx={{ fontWeight: "bold" }}>
                    {movie.title}
                </Typography>
                <Typography variant="subtitle1" sx={{ mt: 2, ml: 3, fontSize: 20 }}>
                    Date: {new Date(session.startTime).toLocaleDateString()}
                </Typography>
                <Typography variant="subtitle1" sx={{ ml: 3, fontSize: 20 }}>
                    Time: {new Date(session.startTime).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
                </Typography>
                <Typography variant="subtitle1" sx={{ ml: 3, fontSize: 20 }}>
                    Duration: {movie.duration} min
                </Typography>
            </Grid>
        </>
    );
};

export default BookingMovieInfo;
