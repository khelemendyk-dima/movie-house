import axiosInstance from "../api/apiClient";
import { CheckoutSessionRequest } from "../types/CheckoutSession";

export const createCheckoutSession = async (
    checkoutData: CheckoutSessionRequest
): Promise<string> => {
    const response = await axiosInstance.post("/payments/checkout-session", checkoutData, {
        responseType: "text",
    });
    return response.data;
};
