import React, { useState, useEffect } from "react";
import axios from "axios";

const MovieForm = () => {
    const [movies, setMovies] = useState([]);
    const [form, setForm] = useState({
        title: "",
        description: "",
        duration: 0,
        ageRating: "",
        releaseDate: "",
        posterUrl: "",
    });
    const [file, setFile] = useState<File | null>(null); // Для загрузки изображения

    useEffect(() => {
        loadMovies();
    }, []);

    const loadMovies = async () => {
        const response = await axios.get("http://localhost:8080/api/movies");
        setMovies(response.data);
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setFile(e.target.files[0]); // Устанавливаем выбранный файл
        }
    };

    const handleSubmit = async () => {
        try {
            let posterUrl = form.posterUrl;

            // Если загружается изображение, сначала отправляем его на сервер
            if (file) {
                const formData = new FormData();
                formData.append("file", file);

                const uploadResponse = await axios.post(
                    "http://localhost:8080/api/movies/upload",
                    formData,
                    { headers: { "Content-Type": "multipart/form-data" } }
                );

                posterUrl = uploadResponse.data; // Получаем URL изображения
            }

            // Создаём фильм
            await axios.post("http://localhost:8080/api/movies", {
                ...form,
                posterUrl, // Сохраняем путь к изображению
            });

            alert("Movie created successfully!");
            setForm({ title: "", description: "", duration: 0, ageRating: "", releaseDate: "", posterUrl: "" });
            setFile(null); // Очищаем файл
            loadMovies();
        } catch (error) {
            console.error("Error creating movie", error);
            alert("Error creating movie");
        }
    };

    return (
        <div>
            <h1>Movie Form</h1>
            <div>
                <input
                    type="text"
                    name="title"
                    placeholder="Title"
                    value={form.title}
                    onChange={handleChange}
                />
                <textarea
                    name="description"
                    placeholder="Description"
                    value={form.description}
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="duration"
                    placeholder="Duration (minutes)"
                    value={form.duration}
                    onChange={handleChange}
                />
                <input
                    type="text"
                    name="ageRating"
                    placeholder="Age Rating"
                    value={form.ageRating}
                    onChange={handleChange}
                />
                <input
                    type="date"
                    name="releaseDate"
                    placeholder="Release Date"
                    value={form.releaseDate}
                    onChange={handleChange}
                />
                <input type="file" onChange={handleFileChange} /> {/* Поле для загрузки изображения */}
                <button onClick={handleSubmit}>Submit</button>
            </div>

            <h2>Movies List</h2>
            <ul>
                {movies.map((movie: any) => (
                    <li key={movie.id}>
                        {movie.title} - {movie.releaseDate}
                        {movie.posterUrl && (
                            <img
                                src={`http://localhost:8080/api/movies/poster/${movie.posterUrl}`}
                                alt={movie.title}
                                style={{ width: "100px", height: "150px", objectFit: "cover", marginLeft: "10px" }}
                            />
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MovieForm;
