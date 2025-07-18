import { useEffect, useState } from "react";
import {
    Modal, Box, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button,
    DialogTitle, IconButton, Typography
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { Session } from "../types/Session";
import { Ticket } from "../types/Ticket";
import { fetchTicketsBySession } from "../services/ticketService";

interface TicketsModalProps {
    open: boolean;
    handleClose: () => void;
    session: Session | null;
}

const TicketsModal = ({
                          open,
                          handleClose,
                          session
                      }: TicketsModalProps) => {
    const [tickets, setTickets] = useState<Ticket[]>([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        if (open && session?.id) {
            fetchTicketsBySession(session.id)
                .then(setTickets)
                .catch((err) => console.error("Error fetching tickets:", err));
        }
    }, [open, session]);

    const filteredTickets = tickets.filter((ticket) =>
        ticket.username.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <Modal open={open} onClose={handleClose}>
            <Box
                sx={{
                    width: 1200,
                    padding: 3,
                    pt: 2,
                    bgcolor: "background.paper",
                    margin: "auto",
                    mt: 10,
                    borderRadius: 3,
                    boxShadow: 24,
                }}
            >
                <DialogTitle sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography sx={{ ml: -3, fontSize: 20 }}>Purchased Tickets</Typography>
                    <IconButton onClick={handleClose} size="small" sx={{ mb: 1, mr: -4 }}>
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>

                <TextField
                    label="Search by Name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />

                <TableContainer component={Paper} sx={{ maxHeight: 700 }}>
                    <Table stickyHeader>
                        <TableHead>
                            <TableRow>
                                <TableCell>Name</TableCell>
                                <TableCell>Email</TableCell>
                                <TableCell>Phone</TableCell>
                                <TableCell>Created At</TableCell>
                                <TableCell>Row</TableCell>
                                <TableCell>Seat</TableCell>
                                <TableCell>Used</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {filteredTickets.length > 0 ? (
                                filteredTickets.map((ticket, index) => (
                                    <TableRow key={index}>
                                        <TableCell>{ticket.username}</TableCell>
                                        <TableCell>{ticket.email}</TableCell>
                                        <TableCell>{ticket.phone}</TableCell>
                                        <TableCell>{new Date(ticket.createdAt).toLocaleString()}</TableCell>
                                        <TableCell>{ticket.rowNumber}</TableCell>
                                        <TableCell>{ticket.seatNumber}</TableCell>
                                        <TableCell>{ticket.used ? "✅" : "❌"}</TableCell>
                                    </TableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={7} align="center">
                                        No tickets found
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>

                <Button onClick={handleClose} fullWidth variant="contained" sx={{ mt: 2 }}>
                    Close
                </Button>
            </Box>
        </Modal>
    );
};

export default TicketsModal;
