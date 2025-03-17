import { useEffect, useState } from "react";
import {
    Box,
    Button,
    Container,
    Table,
    TableBody,
    TableCell,
    Typography,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    TextField,
} from "@mui/material";
import MovieModal from "../components/MovieModal";
import DeleteConfirmationModal from "../components/DeleteConfirmationModal";
import { fetchMovies, deleteMovie } from "../services/movieService";
import { Movie } from "../types";
import { useNavigate } from "react-router-dom";
import MovieTable from "../components/MovieTable";

const MoviesPage = () => {
    const navigate = useNavigate();
    const [movies, setMovies] = useState<Movie[]>([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    useEffect(() => {
        loadMovies();
    }, []);

    const loadMovies = async () => {
        const data = await fetchMovies();
        setMovies(data);
    };

    const handleOpenModal = (movie?: Movie) => {
        setSelectedMovie(movie || null);
        setIsModalOpen(true);
    };

    const handleOpenDeleteModal = (movie?: Movie) => {
        setSelectedMovie(movie || null);
        setIsDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!selectedMovie) {
            setIsDeleteModalOpen(false);
            return;
        }

        try {
            await deleteMovie(selectedMovie.id);
            await loadMovies();
        } catch (error) {
            console.error("Failed to delete movie", error);
        }

        setSelectedMovie(null);
        setIsDeleteModalOpen(false);
    };

    return (
        <Container>
            <Typography variant="h4" sx={{ mb: 2, mt: 4 }}>Movies</Typography>

            <Box sx={{ display: "flex", justifyContent: "space-between", marginBottom: 2 }}>
                <TextField
                    label="Search movies..."
                    variant="outlined"
                    size="small"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <Button variant="outlined" color="success" onClick={() => handleOpenModal(false)}>
                    Create Movie
                </Button>
            </Box>

            <MovieTable
                movies={movies.filter((movie) => movie.title.toLowerCase().includes(searchTerm.toLowerCase()))}
                onEdit={handleOpenModal}
                onDelete={handleOpenDeleteModal}
            />

            <MovieModal
                open={isModalOpen}
                handleClose={() => setIsModalOpen(false)}
                movie={selectedMovie}
                reloadMovies={loadMovies}
            />

            <DeleteConfirmationModal
                open={isDeleteModalOpen}
                handleClose={() => setIsDeleteModalOpen(false)}
                handleConfirm={handleDeleteConfirm}
                itemName={selectedMovie?.title || ""}
            />
        </Container>
    );
};

export default MoviesPage;
