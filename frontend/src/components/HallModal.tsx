import { useEffect, useState } from "react";
import {
    Modal,
    Box,
    TextField,
    Button,
    DialogTitle,
    IconButton,
    Typography, Alert,
} from "@mui/material";
import { createHall, updateHall } from "../services/hallService";
import Hall from "../types/Hall";
import CloseIcon from "@mui/icons-material/Close";

const HallModal = ({
                       open,
                       handleClose,
                       hall,
                       reloadHalls,
                   }: {
    open: boolean;
    handleClose: () => void;
    hall: Hall | null;
    reloadHalls: () => void;
}) => {
    const [formData, setFormData] = useState<Hall>({
        id: 0,
        name: "",
        rowCount: 0,
        seatsPerRow: 0,
    });

    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setFormData(hall ?? { id: 0, name: "", rowCount: 0, seatsPerRow: 0 });
        setError(null);
    }, [hall, open]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === "rowCount" || name === "seatsPerRow" ? Number(value) : value
        }));
    };

    const handleSubmit = async () => {
        try {
            if (hall) {
                await updateHall(formData.id, formData);
            } else {
                await createHall(formData);
            }
            await reloadHalls();
            handleClose();
        } catch (error: any) {
            if (error.response) {
                console.error("API Error:", error.response.data);
                setError(error.response.data.message || "An error occurred.");
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
                <DialogTitle
                    sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}
                >
                    <Typography sx={{ ml: -3, fontSize: 20 }}>
                        {hall ? "Edit Hall" : "Create Hall"}
                    </Typography>
                    <IconButton onClick={handleClose} size="small" sx={{ mb: 1, mr: -4 }}>
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

                <TextField
                    fullWidth
                    label="Hall Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    sx={{ mb: 2 }}
                />
                <TextField
                    fullWidth
                    label="Number of Rows"
                    type="number"
                    name="rowCount"
                    value={formData.rowCount}
                    onChange={handleChange}
                    sx={{ mb: 2 }}
                />
                <TextField
                    fullWidth
                    label="Seats per Row"
                    type="number"
                    name="seatsPerRow"
                    value={formData.seatsPerRow}
                    onChange={handleChange}
                    sx={{ mb: 2 }}
                />
                <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    onClick={handleSubmit}
                    sx={{ mt: 2 }}
                    disabled={!formData.name.trim() || formData.rowCount <= 0 || formData.seatsPerRow <= 0}
                >
                    Save
                </Button>
            </Box>
        </Modal>
    );
};

export default HallModal;
