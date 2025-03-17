import axiosInstance from "../api/apiClient";
import { Movie } from "../types/Movie";

export const fetchMovies = async () => {
    const response = await axiosInstance.get("/movies");
    return response.data;
};

export const fetchMoviesByDate = async (date: string) => {
    const response = await axiosInstance.get(`/movies?date=${date}`);
    return response.data;
};

export const fetchMovieById = async (id: number): Promise<Movie> => {
    const response = await axiosInstance.get(`/movies/${id}`);
    return response.data;
};

export const uploadPoster = async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const response = await axiosInstance.post("/movies/poster/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
    });

    return response.data; // posterUrl
};

export const createMovie = async (movie: Movie) => {
    const response = await axiosInstance.post("/movies", movie);
    return response.data;
};

export const updateMovie = async (id: number, movie: Movie) => {
    const response = await axiosInstance.put(`/movies/${id}`, movie);
    return response.data;
};

export const deleteMovie = async (id: number) => {
    await axiosInstance.delete(`/movies/${id}`);
};





