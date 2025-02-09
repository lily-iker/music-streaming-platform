import { create } from "zustand";
import axiosInstance from '../lib/axios-custom'
import { toast } from "react-hot-toast";

type AuthUserType = {
  lastName?: string;
  firstName?: string;
  gender?: "MALE" | "FEMALE" | "OTHER";
  dateOfBirth?: string;
  phoneNumber?: string;
  email?: string;
  username?: string;
};

type AuthState = {
  authUser: AuthUserType | null;
  isLoading: boolean;
  isAdmin: boolean;

  setAuthUser: (user: AuthUserType | null) => void;
  fetchAuthUser: () => Promise<void>;
  login: (username: string, password: string) => Promise<void>
  signup: (username: string, password: string, confirmPassword: string) => Promise<void>
  logout: () => Promise<void>

};

export const useAuthStore = create<AuthState>((set) => ({
  authUser: null,
  isLoading: false,
  isAdmin: false,

  setAuthUser: (user) => set({ authUser: user }),

  fetchAuthUser: async () => {
    set({ isLoading: true });
    try {
      const res = await axiosInstance.get("/api/user/my-info");
      
      // Should return role when get my info to check for admin
      const regex = /admin/i;
      const isAdmin = regex.test(res.data.result.username);
      
      set({ authUser: res.data.result, isAdmin });
    } catch (error) {
      console.error(error);
    } finally {
      set({ isLoading: false });
    }
  },

  login: async (username: string, password: string) => {
    set({ isLoading: true })
    try {
      await axiosInstance.post("/api/auth/login", { username, password })
      await useAuthStore.getState().fetchAuthUser()
      toast.success("Logged in successfully")
    } catch (error: any) {
      toast.error(error.response?.result?.message || "Something went wrong")
      throw error
    } finally {
      set({ isLoading: false })
    }
  },

  signup: async (username: string, password: string, confirmPassword: string) => {
    set({ isLoading: true })
    try {
      await axiosInstance.post("/api/auth/register", { username, password, confirmPassword })
      await useAuthStore.getState().fetchAuthUser()
      toast.success("Signed up successfully")
    } catch (error: any) {
      toast.error(error.response?.result?.message || "Something went wrong")
      throw error
    } finally {
      set({ isLoading: false })
    }
  },

  logout: async () => {
    set({ isLoading: true })
    try {
      await axiosInstance.post("/api/auth/logout")
      set({ authUser: null })
      toast.success("Logged out successfully")
    } catch (error: any) {
      toast.error(error.response?.result?.message || "Something went wrong")
    } finally {
      set({ isLoading: false })
    }
  },
  
}));