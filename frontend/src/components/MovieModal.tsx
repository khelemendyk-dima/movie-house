import React, { useEffect, useState } from "react";
import {
    Modal, Box, TextField, Button, Stack, FormControl, InputLabel, Select,
    Checkbox, ListItemText, MenuItem, DialogTitle, IconButton, Typography, Alert,
    SelectChangeEvent
} from "@mui/material";
import { Movie } from "../types/Movie";
import { createMovie, updateMovie, uploadPoster } from "../services/movieService";
import CloseIcon from "@mui/icons-material/Close";
import FileDownloadOutlinedIcon from '@mui/icons-material/FileDownloadOutlined';
import axios from "axios";

const GENRES = [
    "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary",
    "Drama", "Fantasy", "Historical", "Horror", "Musical", "Mystery",
    "Romance", "Sci-Fi", "Sports", "Thriller", "War", "Western"
];

interface MovieModalProps {
    open: boolean;
    handleClose: () => void;
    movie: Movie | null;
    reloadMovies: () => void;
}

const MovieModal = ({
                        open,
                        handleClose,
                        movie,
                        reloadMovies
                    }: MovieModalProps) => {
    const [formData, setFormData] = useState<Movie>({
        id: 0,
        title: "",
        description: "",
        duration: 0,
        ageRating: "",
        releaseDate: "",
        posterUrl: "",
        genres: [],
    });

    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setFormData(movie ?? {
            id: 0,
            title: "",
            description: "",
            duration: 0,
            ageRating: "",
            releaseDate: "",
            posterUrl: "",
            genres: [],
        });
        setError(null);
    }, [movie, open]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | SelectChangeEvent<string[]>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name!]: value }));
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            try {
                const posterUrl = await uploadPoster(e.target.files[0]);
                setFormData((prev) => ({ ...prev, posterUrl }));
            } catch (error) {
                console.error("Failed to upload poster:", error);
                setError("Failed to upload poster. Please try again.");
            }
        }
    };

    const handleSubmit = async () => {
        try {
            if (movie) {
                await updateMovie(formData.id, formData);
            } else {
                await createMovie(formData);
            }
            await reloadMovies();
            handleClose();
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                console.error("API Error:", error.response?.data);
                setError(error.response?.data?.message || "An error occurred.");
            } else {
                setError("Network error. Please try again.");
            }
        }
    };

    return (
        <Modal open={open} onClose={handleClose}>
            <Box sx={{
                width: 400, padding: 4, pt: 2, bgcolor: "background.paper",
                margin: "auto", mt: 10, borderRadius: 3, boxShadow: 24,
            }}>
                <DialogTitle sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography sx={{ ml: -3, fontSize: 20 }}>{movie ? "Edit Movie" : "Create Movie"}</Typography>
                    <IconButton onClick={handleClose} size="small" sx={{ mb: 1, mr: -4 }}>
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

                <TextField fullWidth label="Title" name="title" value={formData.title} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Description" name="description" value={formData.description} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Duration (min)" type="number" name="duration" value={formData.duration} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth label="Age Rating" name="ageRating" value={formData.ageRating} onChange={handleChange} sx={{ mb: 2 }} />
                <TextField fullWidth type="date" name="releaseDate" value={formData.releaseDate} onChange={handleChange} sx={{ mb: 2 }} />

                <FormControl fullWidth sx={{ mb: 2 }}>
                    <InputLabel>Genres</InputLabel>
                    <Select multiple name="genres" value={formData.genres} onChange={handleChange} renderValue={(selected) => selected.join(', ')}>
                        {GENRES.map((genre) => (
                            <MenuItem key={genre} value={genre}>
                                <Checkbox checked={formData.genres.includes(genre)} />
                                <ListItemText primary={genre} />
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <Stack direction="column" alignItems="center" sx={{ my: 1 }}>
                    <Button variant="outlined" component="label" sx={{ mb: 2 }}>
                        Upload File <FileDownloadOutlinedIcon sx={{ ml: 1, fontSize: 20 }} />
                        <input type="file" accept="image/*" hidden onChange={handleFileChange} />
                    </Button>
                    {formData.posterUrl && (
                        <Box sx={{ maxWidth: 150, maxHeight: 150, display: "flex" }}>
                            <img src={formData.posterUrl} alt="Poster Preview"
                                 style={{ maxWidth: "100%", maxHeight: "100%", borderRadius: 8 }} />
                        </Box>
                    )}
                </Stack>

                <Button fullWidth variant="contained" color="primary" onClick={handleSubmit} sx={{ mt: 2 }}>
                    Save
                </Button>
            </Box>
        </Modal>
    );
};

export default MovieModal;
