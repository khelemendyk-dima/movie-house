import { Card, CardMedia, CardContent, Typography, Stack } from "@mui/material";

interface MovieInfoProps {
    movie: {
        title: string;
        description: string;
        duration: number;
        ageRating: string;
        releaseDate: string;
        posterUrl: string;
        genres: string[];
    };
}

const MovieInfo: React.FC<MovieInfoProps> = ({ movie }) => {
    return (
        <Card>
            <CardMedia
                component="img"
                image={movie.posterUrl}
                alt={movie.title}
                sx={{ borderRadius: 2 }}
            />
            <CardContent>
                <Typography variant="h5" fontWeight="bold">{movie.title}</Typography>
                <Typography color="text.secondary">{movie.releaseDate} • {movie.duration} min • {movie.ageRating}</Typography>
                <Stack direction="row" spacing={1} mt={1}>
                    {movie.genres.map((genre) => (
                        <Typography key={genre} variant="body2" sx={{ bgcolor: "#eee", px: 1, borderRadius: 1 }}>
                            {genre}
                        </Typography>
                    ))}
                </Stack>
                <Typography variant="body1" mt={2}>{movie.description}</Typography>
            </CardContent>
        </Card>
    );
};

export default MovieInfo;
