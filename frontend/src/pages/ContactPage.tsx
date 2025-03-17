import React from "react";
import { Box, Typography, Container, TextField, Button } from "@mui/material";

const ContactPage = () => {
    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 10, textAlign: "center" }}>
                <Typography variant="h3" fontWeight="bold" gutterBottom>
                    Contact Us
                </Typography>
                <Typography variant="body1" sx={{ mb: 4 }}>
                    Have questions or feedback? Reach out to us, and we’ll get back to you soon!
                </Typography>
                <Box component="form" sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                    <TextField label="Your Name" variant="outlined" fullWidth required />
                    <TextField label="Your Email" variant="outlined" fullWidth required type="email" />
                    <TextField label="Message" variant="outlined" fullWidth required multiline rows={4} />
                    <Button
                        type="submit"
                        sx={{
                            mt: 2,
                            py: 1.5,
                            fontSize: "1rem",
                            fontWeight: "bold",
                            background: "linear-gradient(135deg, #000, #333)",
                            color: "white",
                            '&:hover': { background: "linear-gradient(135deg, #222, #555)" },
                        }}
                    >
                        Send Message
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default ContactPage;
