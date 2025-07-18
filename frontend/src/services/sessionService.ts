import axiosInstance from "../api/apiClient";
import { Session } from "../types/Session";

export const fetchSessionsByMovieId = async (movieId: number) => {
    const response = await axiosInstance.get("/sessions", { params: { movieId } });
    return response.data;
};

export const fetchSessionsByMovie = async (movieId: string, date?: string): Promise<Session[]> => {
    const response = await axiosInstance.get("/sessions", { params: { movieId, date } });
    return response.data;
};

export const fetchSessionById = async (sessionId: number) => {
    const response = await axiosInstance.get(`/sessions/${sessionId}`);
    return response.data;
};

export const fetchSessionOccupancy = async (sessionId: number) => {
    const response = await axiosInstance.get(`/sessions/${sessionId}/occupancy`);
    return response.data;
};

export const createSession = async (session: Session) => {
    const response = await axiosInstance.post("/sessions", session);
    return response.data;
};

export const updateSession = async (id: number, session: Session) => {
    const response = await axiosInstance.put(`/sessions/${id}`, session);
    return response.data;
};

export const deleteSession = async (id: number) => {
    await axiosInstance.delete(`/sessions/${id}`);
};
