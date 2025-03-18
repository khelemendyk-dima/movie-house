import React, { useState } from "react";
import { TextField, Button, Typography, Box, Link, IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import {createUser} from "../services/userService";

const SignUpPage = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errors, setErrors] = useState({
        name: "",
        email: "",
        password: "",
        confirmPassword: "",
    });
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const navigate = useNavigate();

    const validate = () => {
        let newErrors = { name: "", email: "", password: "", confirmPassword: "" };

        if (!name) {
            newErrors.name = "Name is required.";
        }
        if (!email.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
            newErrors.email = "Please enter a valid email address.";
        }
        if (password.length < 6) {
            newErrors.password = "Password must be at least 6 characters long.";
        }
        if (password !== confirmPassword) {
            newErrors.confirmPassword = "Passwords do not match.";
        }

        setErrors(newErrors);
        return !Object.values(newErrors).some((error) => error !== "");
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (validate()) {
            setLoading(true);
            try {
                const response = await createUser( {
                    name,
                    email,
                    password,
                    confirmPassword,
                });

                navigate("/admin/sign-in");
            } catch (error) {
                console.error("Registration failed", error);
                setErrors((prevErrors) => ({
                    ...prevErrors,
                    password: error.response.data.message,
                }));
            } finally {
                setLoading(false);
            }
        }
    };

    return (
        <Box
            sx={{
                mt: 17,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
            }}
        >
            <Box
                sx={{
                    p: 4,
                    width: 400,
                    bgcolor: "white",
                    borderRadius: 2,
                    boxShadow: 3,
                    textAlign: "center",
                }}
            >
                <Typography variant="h4" fontWeight="bold" textAlign="left" gutterBottom>
                    Sign up
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Name"
                        fullWidth
                        margin="normal"
                        variant="outlined"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        error={!!errors.name}
                        helperText={errors.name}
                    />
                    <TextField
                        label="Email"
                        fullWidth
                        margin="normal"
                        variant="outlined"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        error={!!errors.email}
                        helperText={errors.email}
                    />
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
                                    <IconButton
                                        onClick={() => setShowPassword(!showPassword)}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }}
                    />
                    <TextField
                        label="Confirm Password"
                        type={showConfirmPassword ? "text" : "password"}
                        fullWidth
                        margin="normal"
                        variant="outlined"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        error={!!errors.confirmPassword}
                        helperText={errors.confirmPassword}
                        InputProps={{
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton
                                        onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                        edge="end"
                                    >
                                        {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        sx={{
                            mt: 2,
                            py: 1.5,
                            fontSize: "1rem",
                            fontWeight: "bold",
                            background: "linear-gradient(135deg, #000, #333)",
                            color: "white",
                            '&:hover': { background: "linear-gradient(135deg, #222, #555)" },
                        }}
                        disabled={loading}
                    >
                        {loading ? "Registering..." : "Sign up"}
                    </Button>
                </form>
                <Typography sx={{ mt: 2 }}>
                    Already have an account? <Link onClick={() => navigate("/admin/sign-in")} sx={{ color: "black", fontWeight: "bold" }}>Sign in</Link>
                </Typography>
            </Box>
        </Box>
    );
};

export default SignUpPage;
