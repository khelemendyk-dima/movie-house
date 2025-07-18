import React, { useState } from "react";
import { TextField, Button, Typography, Box, Link, IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "../stores/userStore";
import { login } from "../services/userService";

const SignInPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errors, setErrors] = useState({ email: "", password: "" });
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();
    const setUser = useUserStore((state) => state.setUser);

    const validate = () => {
        const newErrors = { email: "", password: "" };
        if (!email.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            newErrors.email = "Please enter a valid email address.";
        }
        if (password.length < 6) {
            newErrors.password = "Password must be at least 6 characters long.";
        }
        setErrors(newErrors);
        return !newErrors.email && !newErrors.password;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (validate()) {
            setLoading(true);
            try {
                const response = await login( { email, password } );
                const user = response.user;
                setUser(user);
                navigate("/admin/dashboard");
            } catch (error) {
                console.error("Login failed", error);
                setErrors({ email: "Invalid email or password.", password: "" });
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <Box sx={{ mt: 17, display: "flex", flexDirection: "column", alignItems: "center" }}>
            <Box sx={{ p: 4, width: 400, bgcolor: "white", borderRadius: 2, boxShadow: 3, textAlign: "center" }}>
                <Typography variant="h4" fontWeight="bold" textAlign="left" gutterBottom>
                    Sign in
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField label="Email" fullWidth margin="normal" variant="outlined" value={email} onChange={(e) => setEmail(e.target.value)} error={!!errors.email} helperText={errors.email} />
                    <TextField
                        label="Password"
                        type={showPassword ? "text" : "password"}
                        fullWidth
                        margin="normal"
                        variant="outlined"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        error={!!errors.password}
                        helperText={errors.password}
                        InputProps={{
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Button type="submit" fullWidth sx={{ mt: 2, py: 1.5, fontSize: "1rem", fontWeight: "bold", background: "linear-gradient(135deg, #000, #333)", color: "white", '&:hover': { background: "linear-gradient(135deg, #222, #555)" } }} disabled={loading}>
                        {loading ? "Logging in..." : "Sign in"}
                    </Button>
                </form>
                <Typography sx={{ mt: 2 }}>
                    Don't have an account? <Link onClick={() => navigate("/admin/sign-up")} sx={{ color: "black", fontWeight: "bold" }}>Sign up</Link>
                </Typography>
            </Box>
        </Box>
    );
};

export default SignInPage;
