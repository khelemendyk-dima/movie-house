import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import {Box} from "@mui/material";
import {useEffect} from "react";
import {useUserStore} from "./stores/userStore.ts";
import Header from "./components/Header";
import MovieListingPage from "./pages/MovieListingPage";
import Footer from "./components/Footer";
import MovieDetailsPage from "./pages/MovieDetailsPage.tsx";
import SeatBookingPage from "./pages/SeatBookingPage.tsx";
import SuccessPage from "./pages/SuccessPage.tsx";
import CancelPaymentPage from "./pages/CancelPaymentPage.tsx";
import SignInPage from "./pages/SignInPage.tsx";
import SignUpPage from "./pages/SignUpPage.tsx";
import AboutPage from "./pages/AboutPage.tsx";
import ContactPage from "./pages/ContactPage.tsx";
import AdminDashboardPage from "./pages/AdminDashboardPage.tsx";
import MoviesPage from "./pages/MoviesPage.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import HallsPage from "./pages/HallsPage.tsx";
import UsersPage from "./pages/UsersPage.tsx";
import ProfilePage from "./pages/ProfilePage.tsx";

function App() {
    const fetchUser = useUserStore((state) => state.fetchUser);

    useEffect(() => {
        fetchUser();
    }, [fetchUser]);

    return (
        <Router>
            <Box sx={{display: "flex", flexDirection: "column", minHeight: "100vh", }}>
                <Header/>

                <Box component="main" sx={{flexGrow: 1}}>
                    <Routes>
                        <Route path="/" element={<MovieListingPage/>}/>
                        <Route path="/about" element={<AboutPage/>}/>
                        <Route path="/contact" element={<ContactPage/>}/>
                        <Route path="/admin/sign-in" element={<SignInPage/>}/>
                        <Route path="/admin/sign-up" element={<SignUpPage/>}/>
                        <Route path="/movies/:id" element={<MovieDetailsPage/>}/>
                        <Route path="/sessions/:sessionId" element={<SeatBookingPage/>}/>
                        <Route path="/booking/:bookingId/success" element={<SuccessPage/>}/>
                        <Route path="/booking/cancel" element={<CancelPaymentPage/>}/>

                        <Route element={<ProtectedRoute allowedRoles={["ADMIN"]}/>}>
                            <Route path="/admin/dashboard" element={<AdminDashboardPage/>}/>
                            <Route path="/movies" element={<MoviesPage/>}/>
                            <Route path="/halls" element={<HallsPage/>}/>
                            <Route path="/users" element={<UsersPage/>}/>
                        </Route>

                        <Route element={<ProtectedRoute allowedRoles={["ADMIN", "USER"]}/>}>
                            <Route path="/profile" element={<ProfilePage/>}/>
                        </Route>
                    </Routes>
                </Box>

                <Footer/>
            </Box>
        </Router>
    );
}

export default App;
