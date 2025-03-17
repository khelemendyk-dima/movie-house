import {useEffect, useState} from "react";
import {
    Button,
    Container,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
} from "@mui/material";
import SessionModal from "./SessionModal";
import DeleteConfirmationModal from "./DeleteConfirmationModal";
import { Session } from "../types";
import { deleteSession } from "../services/sessionService";
import { useNavigate } from "react-router-dom";
import {fetchSessionsByMovieId} from "../services/sessionService";
import TicketsModal from "../components/TicketsModal.tsx";

const SessionsAdminTable = ({ movieId }: { movieId: number;}) => {
    const navigate = useNavigate();
    const [sessions, setSessions] = useState<Session[]>([]);
    const [selectedSession, setSelectedSession] = useState<Session | null>(null);
    const [isViewModalOpen, setIsViewModalOpen] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);



    useEffect(() => {
        loadSessions();
    }, []);

    const loadSessions = async () => {
        const data = await fetchSessionsByMovieId(movieId);
        setSessions(data);
    };

    const handleOpenViewModal = (session?: Session) => {
        setSelectedSession(session || null);
        setIsViewModalOpen(true);
    };

    const handleOpenModal = (isEditing: boolean, session?: Session) => {
        setIsEditing(isEditing);
        setSelectedSession(session || null);
        setIsModalOpen(true);
    };

    const handleOpenDeleteModal = (session?: Session) => {
        setSelectedSession(session || null);
        setIsDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!selectedSession) {
            setIsDeleteModalOpen(false);
            return;
        }

        try {
            await deleteSession(selectedSession.id);
            loadSessions();
        } catch (error) {
            console.error("Failed to delete session", error);
        }

        setSelectedSession(null);
        setIsDeleteModalOpen(false);
    };

    return (
        <Container>
            <h3>Sessions</h3>
            <Button variant="outlined" color="success" onClick={() => handleOpenModal(false)}>
                Create Session
            </Button>

            <TableContainer component={Paper} sx={{ mt: 2 }}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Start Time</TableCell>
                            <TableCell>Hall</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sessions.map((session) => (
                            <TableRow key={session.id}>
                                <TableCell>{session.startTime}</TableCell>
                                <TableCell>{session.hallId}</TableCell>
                                <TableCell>${session.price}</TableCell>
                                <TableCell>
                                    <Button variant="outlined" color="info" onClick={() => handleOpenViewModal(session) }>
                                        View Tickets
                                    </Button>
                                    <Button variant="outlined" color="primary" onClick={() => handleOpenModal(true, session)} sx={{ ml: 2 }}>
                                        Edit
                                    </Button>
                                    <Button variant="outlined" color="error" onClick={() => handleOpenDeleteModal(session)} sx={{ ml: 2 }}>
                                        Delete
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <TicketsModal open={isViewModalOpen} handleClose={() => setIsViewModalOpen(false)} session={selectedSession} />
            <SessionModal open={isModalOpen} handleClose={() => setIsModalOpen(false)} session={selectedSession} sessionMovieId={movieId} reloadSessions={loadSessions} isEditing={isEditing} />
            <DeleteConfirmationModal open={isDeleteModalOpen} handleClose={() => setIsDeleteModalOpen(false)} handleConfirm={handleDeleteConfirm} itemName={`Session ${selectedSession?.id || ""}`} />
        </Container>
    );
};

export default SessionsAdminTable;
