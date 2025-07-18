import { MenuItem, Select, FormControl } from "@mui/material";

interface DateDropdownProps {
    selectedDate: string;
    onDateChange: (date: string) => void;
}

const DateDropdown = ({
                          selectedDate,
                          onDateChange
                      }: DateDropdownProps) => {
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
        <FormControl fullWidth sx={{ mb: 2 }}>
            <Select
                value={selectedDate}
                onChange={(e) => onDateChange(e.target.value)}
            >
                {dates.map((date) => {
                    const dateString = date.toISOString().split("T")[0];
                    return (
                        <MenuItem key={dateString} value={dateString}>
                            {date.getDate()} {date.toLocaleString("en-US", { month: "long" })}
                        </MenuItem>
                    );
                })}
            </Select>
        </FormControl>
    );
};

export default DateDropdown;
