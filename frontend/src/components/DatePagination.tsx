import { Stack, Button } from "@mui/material";

interface DatePaginationProps {
    selectedDate: string;
    onDateChange: (date: string) => void;
}

const DatePagination: React.FC<DatePaginationProps> = ({ selectedDate, onDateChange }) => {
    const today = new Date();

    const generateDates = () => {
        return Array.from({ length: 7 }, (_, i) => {
            const date = new Date(today);
            date.setDate(today.getDate() + i);
            return date;
        });
    };

    const dates = generateDates();

    return (
        <Stack direction="row" spacing={4} justifyContent="center" sx={{ mb: 6, mt: 4 }}>
            {dates.map((date) => {
                const dateString = date.toISOString().split("T")[0];
                return (
                    <Button
                        key={dateString}
                        variant={selectedDate === dateString ? "contained" : "outlined"}
                        sx={{
                            bgcolor: selectedDate === dateString ? "#5E5E5E" : "transparent",
                            color: selectedDate === dateString ? "#fff" : "#5E5E5E",
                            borderColor: "#5E5E5E",
                            fontSize: "1.1rem",
                            padding: "10px 20px",
                            borderRadius: "8px",
                            "&:hover": {
                                bgcolor: selectedDate === dateString ? "#4A4A4A" : "#E0E0E0",
                            },
                        }}
                        onClick={() => onDateChange(dateString)}
                    >
                        {date.getDate()} {date.toLocaleString("en-US", { month: "long" })}
                    </Button>
                );
            })}
        </Stack>
    );
};

export default DatePagination;
