import React from "react";
import { Box, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

interface MovieCardProps {
    id: number;
    title: string;
    posterUrl: string;
}

const MovieCard: React.FC<MovieCardProps> = ({ id, title, posterUrl }) => {
    const navigate = useNavigate();

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                cursor: "pointer",
                width: "100%",
                maxWidth: 250,
                ml: 1.8
            }}
            onClick={() => navigate(`/movies/${id}`)}
        >
            {/* Плитка с постером */}
            <Box
                sx={{
                    width: "100%",
                    height: 375,
                    borderRadius: 3,
                    overflow: "hidden",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    backgroundColor: "#000",
                }}
            >
                <Box
                    component="img"
                    src={posterUrl}
                    alt={title}
                    sx={{
                        width: "100%",
                        height: "100%",
                        objectFit: "cover",
                    }}
                />
            </Box>

            <Typography variant="h6" sx={{ mt: 1, textAlign: "center", fontWeight: "bold" }}>
                {title}
            </Typography>
        </Box>
    );
};

export default MovieCard;
