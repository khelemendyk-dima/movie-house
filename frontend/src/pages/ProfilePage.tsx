import { Box, Typography, Avatar, Button, Paper, Stack } from "@mui/material";
import { useUserStore } from "../stores/userStore";
import EditIcon from "@mui/icons-material/Edit";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate } from "react-router-dom";
import UserModal from "../components/UserModal.tsx";
import { useState } from "react";

const ProfilePage = () => {
    const { user, logoutUser, fetchUser } = useUserStore();
    const navigate = useNavigate();
    const [isModalOpen, setIsModalOpen] = useState(false);

    const loadUsers = async () => {
        await fetchUser();
    };

    const handleLogout = async () => {
        await logoutUser();
        navigate("/admin/sign-in");
    };

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
                    {user?.name.charAt(0).toUpperCase()}
                </Avatar>

                <Typography variant="h5" sx={{ fontWeight: "bold" }}>
                    {user?.name}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                    {user?.email}
                </Typography>
                <Typography variant="body2" color="primary" sx={{ mt: 1 }}>
                    Role: {user?.role}
                </Typography>

                <Stack direction="row" spacing={2} justifyContent="center" sx={{ mt: 3 }}>
                    <Button variant="contained" startIcon={<EditIcon />} onClick={() => setIsModalOpen(true)}>
                        Edit Profile
                    </Button>
                    <Button variant="outlined" startIcon={<LogoutIcon />} onClick={handleLogout} color="error">
                        Logout
                    </Button>
                </Stack>
            </Paper>

            <UserModal open={isModalOpen} handleClose={() => setIsModalOpen(false)} user={user} reloadUsers={loadUsers} />
        </Box>
    );
};

export default ProfilePage;
