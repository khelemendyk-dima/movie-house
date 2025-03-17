import axiosInstance from "../api/apiClient";
import {Hall} from "../types/Hall";

export const fetchHalls = async () => {
    const response = await axiosInstance.get("/halls");
    return response.data;
};

export const createHall = async (hall: Hall) => {
    const response = await axiosInstance.post("/halls", hall);
    return response.data;
};

export const updateHall = async (id: number, hall: Hall) => {
    const response = await axiosInstance.put(`/halls/${id}`, hall);
    return response.data;
};

export const deleteHall = async (id: number) => {
    await axiosInstance.delete(`/halls/${id}`);
};
