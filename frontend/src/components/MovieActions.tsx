import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Movie } from "../types/Movie";

interface Props {
    movie: Movie;
    onEdit: () => void;
    onDelete: () => void;
}

const MovieActions = ({ movie, onEdit, onDelete }: Props) => {
    const navigate = useNavigate();
    return (
        <>
            <Button variant="outlined" color="info" onClick={() => navigate(`/movies/${movie.id}`)}>
                View
            </Button>
            <Button variant="outlined" color="primary" onClick={onEdit} sx={{ ml: 2 }}>
                Edit
            </Button>
            <Button variant="outlined" color="error" onClick={onDelete} sx={{ ml: 2 }}>
                Delete
            </Button>
        </>
    );
};

export default MovieActions;
