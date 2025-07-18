import { Box, Typography } from "@mui/material";

interface ScreenProps {
    screenArcWidth: number
}

const Screen = ({
                    screenArcWidth
                }: ScreenProps) => (
    <Box sx={{ position: "relative", mt: 13 }}>
        <Box
            sx={{
                width: `${screenArcWidth}px`,
                height: "40px",
                borderRadius: "50% 50% 0 0",
                backgroundColor: "#e0e0e0",
                position: "absolute",
                top: -60,
                left: `calc(50% - ${screenArcWidth / 2}px)`,
            }}
        />
        <Typography
            variant="h6"
            sx={{
                position: "absolute",
                top: -50,
                left: "50%",
                transform: "translateX(-50%)",
            }}
        >
            Screen
        </Typography>
    </Box>
);

export default Screen;
