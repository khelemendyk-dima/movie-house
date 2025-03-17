import { Grid } from "@mui/material";
import MovieCard from "./MovieCard";
import Movie from "../types";

interface MovieGridProps {
    movies: Movie[];
}

const MovieGrid: React.FC<MovieGridProps> = ({ movies }) => {
    return (
        <Grid container spacing={2} justifyContent="space-evenly">
            {movies.map((movie) => (
                <Grid item key={movie.id} lg={3}>
                    <MovieCard id={movie.id} title={movie.title} posterUrl={movie.posterUrl} />
                </Grid>
            ))}
        </Grid>
    );
};

export default MovieGrid;
