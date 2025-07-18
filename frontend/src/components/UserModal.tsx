import { useState, useEffect } from "react";
import {
    Modal,
    Box,
    DialogTitle,
    Button,
    TextField,
    MenuItem,
    Typography,
    IconButton, Alert
} from "@mui/material";
import { updateUser } from "../services/userService";
import { User } from "../types/User";
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";

interface UserModalProps {
    open: boolean;
    handleClose: () => void;
    user: User | null;
    reloadUsers: () => void;
}

const UserModal = ({
                       open,
                       handleClose,
                       user,
                       reloadUsers
                   }: UserModalProps) => {
    const [formData, setFormData] = useState<User>({
        id: 0,
        name: "",
        email: "",
        role: "",
    });

    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setFormData(user ?? {
            id: 0,
            name: "",
            email: "",
            role: "",
        });
        setError(null);
    }, [user, open]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async () => {
        try {
            if (user) {
                await updateUser(user.id, formData);
            }
            await reloadUsers();
            handleClose();
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                console.error("API Error:", error.response?.data);
                setError(error.response?.data?.message || "An error occurred.");
            } else {
                setError("Network error. Please try again.");
            }
        }
    };

    return (
        <Modal open={open} onClose={handleClose}>
            <Box
                sx={{
                    width: 400,
                    padding: 4,
                    pt: 2,
                    bgcolor: "background.paper",
                    margin: "auto",
                    mt: 10,
                    borderRadius: 3,
                    boxShadow: 24,
                }}
            >
                <DialogTitle sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography sx={{ml: -3, fontSize: 20}}>{user ? "Edit User" : "Create User"}</Typography>
                    <IconButton onClick={handleClose} size="small" sx={{mb: 1, mr: -4}}>
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
                
                <TextField name="name" fullWidth label="Name" margin="dense" value={formData.name} onChange={handleChange} />
                <TextField name="email" fullWidth label="Email" margin="dense" value={formData.email} onChange={handleChange} />
                <TextField name="role" fullWidth select label="Role" margin="dense" value={formData.role} onChange={handleChange}>
                    <MenuItem value="USER">User</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                </TextField>
                <Button fullWidth variant="contained" color="primary" onClick={handleSubmit} sx={{ mt: 2 }}>
                    Save
                </Button>
            </Box>
        </Modal>
    );
};

export default UserModal;
