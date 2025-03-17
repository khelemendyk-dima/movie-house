import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from "@mui/material";
import MovieActions from "./MovieActions";
import { Movie } from "../types";

interface Props {
    movies: Movie[];
    onEdit: (movie: Movie) => void;
    onDelete: (movie: Movie) => void;
}

const MovieTable = ({ movies, onEdit, onDelete }: Props) => {
    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Title</TableCell>
                        <TableCell>Release Date</TableCell>
                        <TableCell>Duration</TableCell>
                        <TableCell>Age Rating</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {movies.map((movie) => (
                        <TableRow key={movie.id}>
                            <TableCell>{movie.title}</TableCell>
                            <TableCell>{movie.releaseDate}</TableCell>
                            <TableCell>{movie.duration} min</TableCell>
                            <TableCell>{movie.ageRating}</TableCell>
                            <TableCell>
                                <MovieActions movie={movie} onEdit={() => onEdit(movie)} onDelete={() => onDelete(movie)} />
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default MovieTable;
