import React from "react";
import { Dialog, DialogActions, DialogContent, DialogTitle, Button, Typography, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

interface DeleteConfirmationModalProps {
    open: boolean;
    handleClose: () => void;
    handleConfirm: () => void;
    itemName: string;
}

const DeleteConfirmationModal: React.FC<DeleteConfirmationModalProps> = ({ open, handleClose, handleConfirm, itemName }) => {
    return (
        <Dialog open={open} onClose={handleClose} sx={{ "& .MuiDialog-paper": { borderRadius: 2, padding: 1, pb: 3, mt: -25,} }}>
            <DialogTitle sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                Confirm Deletion
                <IconButton onClick={handleClose} size="small">
                    <CloseIcon />
                </IconButton>
            </DialogTitle>
            <DialogContent>
                <Typography>Are you sure you want to delete "<strong>{itemName}</strong>"?</Typography>
            </DialogContent>
            <DialogActions sx={{ mr: 2 }}>
                <Button onClick={handleClose} variant="outlined" color="primary">
                    Cancel
                </Button>
                <Button onClick={handleConfirm} variant="outlined" color="error">
                    Delete
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default DeleteConfirmationModal;
