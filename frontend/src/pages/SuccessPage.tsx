import { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Typography, Button, Box, Paper } from "@mui/material";
import DownloadIcon from "@mui/icons-material/Download";
import HomeIcon from "@mui/icons-material/Home";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import { motion } from "framer-motion";

const SuccessPage = () => {
    const { bookingId } = useParams();
    const navigate = useNavigate();

    const handleDownloadTicket = () => {
        window.open(`http://localhost:8080/api/bookings/${bookingId}/tickets/download`, "_blank");
    };

    return (
        <Box
            sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                p: 2,
                mt: 4
            }}
        >
            <motion.div
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6 }}
            >
                <Paper
                    elevation={3}
                    sx={{
                        backgroundColor: "white",
                        color: "#333",
                        borderRadius: "12px",
                        padding: 4,
                        maxWidth: "480px",
                        textAlign: "center",
                        boxShadow: "0px 4px 12px rgba(0, 0, 0, 0.1)"
                    }}
                >
                    <CheckCircleIcon sx={{ fontSize: 90, color: "#4CAF50", mb: 2 }} />

                    <Typography variant="h4" fontWeight="bold">
                        Payment Successful!
                    </Typography>

                    <Box sx={{ mt: 3 }}>
                        <img
                            src="/movie-ticket.jpg"
                            alt="Movie Ticket"
                            style={{ width: "50%", borderRadius: "8px" }}
                        />
                    </Box>

                    <Box sx={{ mt: 4, display: "flex", flexDirection: "column", gap: 2 }}>
                        <Button
                            variant="contained"
                            size="large"
                            color="success"
                            startIcon={<DownloadIcon />}
                            onClick={handleDownloadTicket}
                            sx={{ fontWeight: "bold" }}
                        >
                            Download Ticket
                        </Button>

                        <Button
                            variant="outlined"
                            size="large"
                            color="primary"
                            startIcon={<HomeIcon />}
                            onClick={() => navigate("/")}
                            sx={{ fontWeight: "bold" }}
                        >
                            Go to Home
                        </Button>
                    </Box>
                </Paper>
            </motion.div>
        </Box>
    );
};

export default SuccessPage;
