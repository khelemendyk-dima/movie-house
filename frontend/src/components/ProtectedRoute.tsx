import { Navigate, Outlet } from "react-router-dom";
import { useUserStore } from "../stores/userStore";

interface ProtectedRouteProps {
    allowedRoles: string[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
    const { user, isLoading } = useUserStore();

    if (isLoading) {
        return;
    }

    if (!user) {
        return <Navigate to="/admin/sign-in" replace />;
    }

    return allowedRoles.includes(user.role) ? <Outlet /> : <Navigate to="/" replace />;
};

export default ProtectedRoute;
