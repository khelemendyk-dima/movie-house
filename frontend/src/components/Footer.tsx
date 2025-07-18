import { Box, Typography, Stack, Link } from "@mui/material";

const Footer= () => {
    return (
        <Box
            component="footer"
            sx={{
                bgcolor: "#f5f5f5",
                py: 3,
                mt: 5,
                textAlign: "center",
            }}
        >
            <Typography variant="h6" gutterBottom>
                Movie House
            </Typography>
            <Stack direction="row" justifyContent="center" spacing={4}>
                <Link href="#" color="inherit" underline="hover">
                    Privacy Policy
                </Link>
                <Link href="#" color="inherit" underline="hover">
                    Terms of Service
                </Link>
                <Link href="#" color="inherit" underline="hover">
                    Contact
                </Link>
            </Stack>
            <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
                © {new Date().getFullYear()} Movie House. All rights reserved.
            </Typography>
        </Box>
    );
};

export default Footer;
