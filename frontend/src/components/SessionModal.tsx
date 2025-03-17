import { useEffect, useState } from "react";
import { Modal, Box, TextField, Button, FormControl, InputLabel, Select, MenuItem, DialogTitle, IconButton, Typography } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { createSession, updateSession } from "../services/sessionService";
import { Session } from "../types/Session";
import { fetchHalls } from "../services/hallService.ts";
import { Hall } from "../types/Hall";

const SessionModal = ({
                          open,
                          handleClose,
                          session,
                          sessionMovieId,
                          reloadSessions,
                          isEditing }: {
    open: boolean;
    handleClose: () => void;
    session: Session | null;
    sessionMovieId: number;
    reloadSessions: () => void;
    isEditing: boolean
}) => {
    const [halls, setHalls] = useState<Hall[]>([]);

    const [formData, setFormData] = useState<Session>({
        movieId: sessionMovieId,
        hallId: 1,
        startTime: "",
        startDate: "",
        endDate: "",
        price: 0,
    });

    useEffect(() => {
        if (!isEditing) {
            setFormData({
                movieId: sessionMovieId,
                hallId: 1,
                startTime: "",
                startDate: "",
                endDate: "",
                price: 0,
            });
        }
    }, [isEditing, open]);

    useEffect(() => {
        if (session) setFormData(session);
    }, [session]);

    useEffect(() => {
        loadHalls();
    }, []);

    const loadHalls = async () => {
        const data = await fetchHalls();
        setHalls(data);
    };

    const handleChange = (e: React.ChangeEvent<{ name?: string; value: unknown }>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name!]: value }));
    };

    const handleSubmit = async () => {
        try {
            if (isEditing && session) {
                await updateSession(session.id, formData);
            } else {
                await createSession(formData);
            }
            reloadSessions();
            handleClose();
        } catch (error) {
            console.error("Failed to save session", error);
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
                    <Typography sx={{ ml: -3, fontSize: 20 }}>{isEditing ? "Edit Session" : "Create Session"}</Typography>
                    <IconButton onClick={handleClose} size="small" sx={{ mb: 1, mr: -4 }}>
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>
                {!isEditing && (
                    <>
                        <TextField
                            fullWidth
                            label="Start Date"
                            name="startDate"
                            type="date"
                            value={formData.startDate}
                            onChange={handleChange}
                            sx={{ mb: 2 }}
                            InputLabelProps={{ shrink: true }}
                        />
                        <TextField
                            fullWidth
                            label="End Date"
                            name="endDate"
                            type="date"
                            value={formData.endDate}
                            onChange={handleChange}
                            sx={{ mb: 2 }}
                            InputLabelProps={{ shrink: true }}
                        />
                        <TextField
                            fullWidth
                            label="Start Time"
                            name="startTime"
                            type="time"
                            value={formData.startTime}
                            onChange={handleChange}
                            sx={{ mb: 2 }}
                            InputLabelProps={{ shrink: true }}
                        />
                    </>
                )}

                {isEditing && (
                    <>
                        <TextField
                            fullWidth
                            label="Start Time"
                            name="startTime"
                            type="datetime-local"
                            value={formData.startTime}
                            onChange={handleChange}
                            sx={{ mb: 2 }}
                        />
                    </>
                )}

                <TextField
                    fullWidth
                    label="Price"
                    name="price"
                    type="number"
                    value={formData.price}
                    onChange={handleChange}
                    sx={{ mb: 2 }}
                />
                <FormControl fullWidth sx={{ mb: 2 }}>
                    <InputLabel>Hall</InputLabel>
                    <Select name="hallId" value={formData.hallId} onChange={handleChange}>
                        {halls.map((hall) => (
                            <MenuItem key={hall.name} value={hall.id}>
                                {hall.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <Button variant="contained" fullWidth color="primary" onClick={handleSubmit}>
                    {isEditing ? "Update" : "Create"}
                </Button>
            </Box>
        </Modal>
    );
};

export default SessionModal;
