import axiosInstance from "../api/apiClient";
import { User } from "../types/User";

export const authMe = async (): Promise<User> => {
    const response = await axiosInstance.get("/auth/me");
    return response.data;
}

export const login = async (userData: Partial<User> & { password: string }) => {
    const response = await axiosInstance.post("/auth/login", userData);
    return response.data;
}

export const logout = async () => {
    await axiosInstance.post("/auth/logout");
};

export const fetchUsers = async (): Promise<User[]> => {
    const response = await axiosInstance.get("/users");
    return response.data;
};

export const createUser = async (userData: Partial<User> & { password: string; confirmPassword: string }) => {
    const response = await axiosInstance.post("/auth/register", userData);
    return response.data;
};

export const updateUser = async (id: number, userData: User): Promise<User> => {
    const response = await axiosInstance.put(`/users/${id}`, userData);
    return response.data;
};

export const deleteUser = async (id: number) => {
    await axiosInstance.delete(`/users/${id}`);
};
