import { create } from "zustand";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { User } from "../types";
import { authMe, logout } from "../services/userService.ts";

interface UserStore {
    user: User | null;
    isLoading: boolean;
    setUser: (user: User) => void;
    fetchUser: () => Promise<void>;
    logoutUser: () => Promis<void>;
}

export const useUserStore = create<UserStore>((set) => ({
    user: null,
    isLoading: true,

    setUser: (user) => set({ user }),

    fetchUser: async () => {
        try {
            const response = await authMe();
            set({ user: response, isLoading: false });
        } catch (error) {
            set({ user: null, isLoading: false });
        }
    },

    logoutUser: async () => {
        try {
            await logout();
            set({ user: null });
        } catch (error) {
            console.error("Logout failed", error);
        }
    },
}));

