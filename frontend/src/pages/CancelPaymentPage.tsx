import { useNavigate } from "react-router-dom";
import { Container, Typography, Button, Grid } from "@mui/material";
import HomeIcon from "@mui/icons-material/Home";

const CancelPaymentPage = () => {
    const navigate = useNavigate();

    return (
        <Container sx={{ mt: 6 }}>
            <Grid container spacing={4} alignItems="center" sx={{ml: 5}}>
                <Grid item xs={12} md={7} sx={{ textAlign: { xs: "center", md: "left" } }}>
                    <Typography variant="h4" color="error">
                        Payment Failed
                    </Typography>
                    <Typography variant="h6" sx={{ mt: 2 }}>
                        Oops! Something went wrong with your payment. Try again later or contact support team.
                    </Typography>

                    <Button
                        variant="outlined"
                        startIcon={<HomeIcon />}
                        sx={{ mt: 3 }}
                        onClick={() => navigate("/")}
                    >
                        Go to Home
                    </Button>
                </Grid>

                <Grid item xs={12} md={5}>
                    <img
                        src="/payment-failed.jpg"
                        alt="Payment Failed"
                        style={{ width: "100%", maxWidth: "300px", borderRadius: "10px" }}
                    />
                </Grid>
            </Grid>
        </Container>
    );
};

export default CancelPaymentPage;
