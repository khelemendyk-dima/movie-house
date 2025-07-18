import { useEffect, useState, useCallback } from "react";
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
    TextField,
} from "@mui/material";
import HallModal from "../components/HallModal";
import DeleteConfirmationModal from "../components/DeleteConfirmationModal";
import { fetchHalls, deleteHall } from "../services/hallService";
import { Hall } from "../types/Hall";

const HallsPage = () => {
    const [halls, setHalls] = useState<Hall[]>([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedHall, setSelectedHall] = useState<Hall | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const loadHalls = useCallback(async () => {
        try {
            const data = await fetchHalls();
            setHalls(data);
        } catch (error) {
            console.error("Failed to load halls", error);
        }
    }, []);

    useEffect(() => {
        loadHalls();
    }, [loadHalls]);

    const handleOpenModal = (hall?: Hall) => {
        setSelectedHall(hall || null);
        setIsModalOpen(true);
    };

    const handleOpenDeleteModal = (hall?: Hall) => {
        setSelectedHall(hall || null);
        setIsDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!selectedHall) {
            setIsDeleteModalOpen(false);
            return;
        }

        try {
            await deleteHall(selectedHall.id);
            await loadHalls();
        } catch (error) {
            console.error("Failed to delete hall", error);
        }

        setSelectedHall(null);
        setIsDeleteModalOpen(false);
    };

    return (
        <Container>
            <h2>Halls</h2>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 16 }}>
                <TextField
                    label="Search halls..."
                    variant="outlined"
                    size="small"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <Button variant="outlined" color="success" onClick={() => handleOpenModal()}>
                    Create Hall
                </Button>
            </div>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Hall Name</TableCell>
                            <TableCell>Rows</TableCell>
                            <TableCell>Seats per Row</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {halls
                            .filter((hall) => hall.name.toLowerCase().includes(searchTerm.toLowerCase()))
                            .map((hall) => (
                                <TableRow key={hall.id}>
                                    <TableCell>{hall.name}</TableCell>
                                    <TableCell>{hall.rowCount}</TableCell>
                                    <TableCell>{hall.seatsPerRow}</TableCell>
                                    <TableCell>
                                        <Button variant="outlined" color="primary" onClick={() => handleOpenModal(hall)}>
                                            Edit
                                        </Button>
                                        <Button variant="outlined" color="error" onClick={() => handleOpenDeleteModal(hall)} sx={{ ml: 2 }}>
                                            Delete
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <HallModal
                open={isModalOpen}
                handleClose={() => setIsModalOpen(false)}
                hall={selectedHall}
                reloadHalls={loadHalls}
            />
            <DeleteConfirmationModal
                open={isDeleteModalOpen}
                handleClose={() => setIsDeleteModalOpen(false)}
                handleConfirm={handleDeleteConfirm}
                itemName={selectedHall?.name || ""}
            />
        </Container>
    );
};

export default HallsPage;
