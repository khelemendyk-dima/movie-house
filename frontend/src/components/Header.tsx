import { AppBar, Toolbar, Typography, Box, Container } from "@mui/material";
import { useUserStore } from "../stores/userStore";
import { Link } from "react-router-dom";

const Header = () => {
    const user = useUserStore((state) => state.user);

    return (
        <AppBar position="static" sx={{ backgroundColor: "white", boxShadow: 1 }}>
            <Container maxWidth="lg">
                <Toolbar sx={{ justifyContent: "space-between" }}>
                    <Box>
                        <Link to="/">
                            <img src="/logo.jpg" alt="Logo" style={{ height: 80 }} />
                        </Link>
                    </Box>

                    <Box sx={{ display: "flex", gap: 3, alignItems: "center" }}>
                        <Box sx={{ display: "flex", alignItems: "center" }}>
                            {user && user.role=="ADMIN" && (
                                <Typography component={Link} to="/admin/dashboard" sx={linkStyle}>
                                    Dashboard
                                </Typography>
                            )}
                            {user && user.role && (
                                <Box sx={separatorStyle} />
                            )}

                            <Typography component={Link} to="/" sx={linkStyle}>
                                Now in cinemas
                            </Typography>
                            <Box sx={separatorStyle} />

                            <Typography component={Link} to="/about" sx={linkStyle}>
                                About us
                            </Typography>
                            <Box sx={separatorStyle} />

                            <Typography component={Link} to="/contact" sx={linkStyle}>
                                Contact
                            </Typography>

                            {user && user.role && (
                                <Box sx={separatorStyle} />
                            )}
                            {user && user.role && (
                                <Typography component={Link} to="/profile" sx={linkStyle}>
                                    Profile
                                </Typography>
                            )}
                        </Box>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
};

const linkStyle = {
    textDecoration: "none",
    color: "black",
    fontSize: "1.3rem",
    fontWeight: 500,
    paddingX: 2,
    "&:hover": { color: "gray" },
};

const separatorStyle = {
    width: "1px",
    height: "20px",
    backgroundColor: "#ccc",
    marginX: 2,
};

export default Header;
