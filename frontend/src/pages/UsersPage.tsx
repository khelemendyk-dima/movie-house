import { useEffect, useState } from "react";
import { Container, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField } from "@mui/material";
import UserModal from "../components/UserModal";
import DeleteConfirmationModal from "../components/DeleteConfirmationModal";
import { fetchUsers, deleteUser } from "../services/userService";
import { User } from "../types/User";

const UsersPage = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        const data = await fetchUsers();
        setUsers(data);
    };

    const handleOpenModal = (user?: User) => {
        setSelectedUser(user || null);
        setIsModalOpen(true);
    };

    const handleOpenDeleteModal = (user?: User) => {
        setSelectedUser(user || null);
        setIsDeleteModalOpen(true);
    };

    const handleDeleteConfirm = async () => {
        if (!selectedUser) {
            setIsDeleteModalOpen(false);
            return;
        }

        try {
            await deleteUser(selectedUser.id);
            await loadUsers();
        } catch (error) {
            console.error("Failed to delete user", error);
        }

        setSelectedUser(null);
        setIsDeleteModalOpen(false);
    };

    return (
        <Container>
            <h2>Users</h2>
            <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 16 }}>
                <TextField label="Search users..." variant="outlined" size="small" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
            </div>

            <TableContainer component={Paper} >
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Role</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users
                            .filter((user) => user.name.toLowerCase().includes(searchTerm.toLowerCase()))
                            .map((user) => (
                                <TableRow key={user.id}>
                                    <TableCell>{user.name}</TableCell>
                                    <TableCell>{user.email}</TableCell>
                                    <TableCell>{user.role}</TableCell>
                                    <TableCell>
                                        <Button variant="outlined" color="primary" onClick={() => handleOpenModal(user)}>Edit</Button>
                                        <Button variant="outlined" color="error" onClick={() => handleOpenDeleteModal(user)} sx={{ ml: 2 }}>Delete</Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <UserModal open={isModalOpen} handleClose={() => setIsModalOpen(false)} user={selectedUser} reloadUsers={loadUsers}/>
            <DeleteConfirmationModal open={isDeleteModalOpen} handleClose={() => setIsDeleteModalOpen(false)} handleConfirm={handleDeleteConfirm} itemName={selectedUser?.name || ""} />
        </Container>
    );
};

export default UsersPage;
