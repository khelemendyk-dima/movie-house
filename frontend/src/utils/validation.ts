import { parsePhoneNumber } from "libphonenumber-js";

export const validateUserInfo = (userInfo: {
    name: string;
    email: string;
    phone: string;
}) => {
    const newErrors = { name: "", email: "", phone: "" };
    let isValid = true;

    if (!userInfo.name.trim()) {
        newErrors.name = "Name is required.";
        isValid = false;
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!userInfo.email.trim()) {
        newErrors.email = "Email is required.";
        isValid = false;
    } else if (!emailRegex.test(userInfo.email)) {
        newErrors.email = "Please enter a valid email address.";
        isValid = false;
    }

    try {
        const phoneNumber = parsePhoneNumber(userInfo.phone);
        if (!phoneNumber?.isValid()) {
            newErrors.phone = "Please enter a valid phone number.";
            isValid = false;
        }
    } catch (error) {
        newErrors.phone = "Invalid phone number format.";
        isValid = false;
    }

    return { isValid, errors: newErrors };
};
