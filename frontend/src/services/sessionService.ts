import axiosInstance from "../api/apiClient";
import Session from "../types";

export const fetchSessions = async (movieId: number, date: string) => {
    const response = await axiosInstance.get("/sessions", { params: { movieId, date } });
    return response.data;
};

export const fetchSessionsByMovieId = async (movieId: number) => {
    const response = await axiosInstance.get("/sessions", { params: { movieId } });
    return response.data;
};

export const fetchSessionsByMovie = async (movieId: number, date?: string): Promise<Session[]> => {
    console.log(date)
    const response = await axiosInstance.get("/sessions", { params: { movieId, date } });
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
