import axios, { AxiosInstance, AxiosResponse, AxiosError } from "axios";

export const baseUrl = "http://localhost:8080/api";

const apiClient: AxiosInstance = axios.create({
    baseURL: baseUrl,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true,
});

const handleError = (error: AxiosError) => {
    if (error.response) {
        console.error("Error response:", error.response.data);
    } else if (error.request) {
        console.error("Error request:", error.request);
    } else {
        console.error("Error message:", error.message);
    }
    return Promise.reject(error);
};

apiClient.interceptors.response.use(
    (response: AxiosResponse) => response,
    handleError
);

export default apiClient;
