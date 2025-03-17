import React from "react";
import { Box, Typography, Container } from "@mui/material";

const AboutPage = () => {
    return (
        <Container maxWidth="md">
            <Box sx={{ mt: 10, textAlign: "" }}>
                <Typography variant="h3" fontWeight="bold" gutterBottom>
                    About Us
                </Typography>
                <Typography variant="body1" sx={{ mt: 2, lineHeight: 1.8, fontSize: 20 }}>
                    Welcome to our cinema! We offer the best movie-watching experience with
                    state-of-the-art screens, comfortable seating, and a wide variety of films.
                    Whether you're a fan of action, drama, or comedy, we have something for everyone.
                </Typography>
                <Typography variant="body1" sx={{ mt: 2, lineHeight: 1.8, fontSize: 20 }}>
                    Our mission is to bring people together through the magic of movies. Join us
                    and immerse yourself in an unforgettable cinematic journey!
                </Typography>
            </Box>
        </Container>
    );
};

export default AboutPage;
