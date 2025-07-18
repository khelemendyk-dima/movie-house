import { Card, CardActionArea, CardContent, Typography, Grid, Container } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MovieIcon from "@mui/icons-material/Movie";
import PeopleIcon from "@mui/icons-material/People";
import ChairOutlinedIcon from '@mui/icons-material/ChairOutlined';

const adminOptions = [
    { label: "Movies", icon: <MovieIcon fontSize="large" />, path: "/movies" },
    { label: "Halls", icon: <ChairOutlinedIcon fontSize="large" />, path: "/halls" },
    { label: "Users", icon: <PeopleIcon fontSize="large" />, path: "/users" }
];

const AdminDashboardPage = () => {
    const navigate = useNavigate();

    return (
        <Container sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom align="center">
                Admin Dashboard
            </Typography>
            <Grid container spacing={3} justifyContent="center">
                {adminOptions.map(({ label, icon, path }) => (
                    <Grid item xs={12} sm={6} md={4} key={label}>
                        <Card sx={{ textAlign: "center", p: 2, cursor: "pointer" }}>
                            <CardActionArea onClick={() => navigate(path)}>
                                <CardContent>
                                    {icon}
                                    <Typography variant="h6" sx={{ mt: 1 }}>
                                        {label}
                                    </Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default AdminDashboardPage;