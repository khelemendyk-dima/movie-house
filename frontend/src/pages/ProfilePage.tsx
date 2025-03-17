import { Box, Typography, Avatar, Button, Paper, Stack } from "@mui/material";
import { useUserStore } from "../stores/userStore";
import EditIcon from "@mui/icons-material/Edit";

const ProfilePage = () => {
    const user = useUserStore((state) => state.user);

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: "45vh",
            }}
        >
            <Paper
                sx={{
                    padding: 4,
                    borderRadius: 3,
                    boxShadow: 4,
                    width: 400,
                    textAlign: "center",
                }}
            >
                <Avatar sx={{ width: 80, height: 80, margin: "auto", mb: 2 }}>
                    {user.name.charAt(0).toUpperCase()}
                </Avatar>

                <Typography variant="h5" sx={{ fontWeight: "bold" }}>
                    {user.name}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                    {user.email}
                </Typography>
                <Typography variant="body2" color="primary" sx={{ mt: 1 }}>
                    Role: {user.role}
                </Typography>

                <Stack direction="row" spacing={2} justifyContent="center" sx={{ mt: 3 }}>
                    <Button
                        variant="contained"
                        startIcon={<EditIcon />}
                    >
                        Edit Profile
                    </Button>
                </Stack>
            </Paper>
        </Box>
    );
};

export default ProfilePage;
