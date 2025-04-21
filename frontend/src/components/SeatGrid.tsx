import { Grid, Button, Tooltip } from "@mui/material";
import { Cancel } from "@mui/icons-material";

const SeatGrid = ({ groupedSeats, selectedSeats, toggleSeatSelection }) => {
    return (
        <Grid container spacing={1} justifyContent="center" sx={{ mt: 2 }}>
            {Object.keys(groupedSeats).map((row) => (
                <Grid item xs={12} key={row}>
                    <Grid container spacing={1} justifyContent="center">
                        {groupedSeats[row].map((seat) => (
                            <Grid item key={seat.seatId}>
                                <Tooltip
                                    title={
                                        seat.status === "RESERVED"
                                            ? "Reserved"
                                            : `Row: ${seat.rowNumber}, Seat: ${seat.seatNumber}`
                                    }
                                    arrow
                                >
                                    <span>
                                        <Button
                                            variant="outlined"
                                            color={
                                                selectedSeats.includes(seat.seatId)
                                                    ? "error"
                                                    : seat.status === "RESERVED"
                                                        ? "default"
                                                        : "primary"
                                            }
                                            disabled={seat.status === "RESERVED"}
                                            onClick={() => toggleSeatSelection(seat.seatId)}
                                            sx={{
                                                minWidth: 50,
                                                minHeight: 50,
                                                backgroundColor:
                                                    seat.status === "RESERVED"
                                                        ? "#B0B0B0"
                                                        : selectedSeats.includes(seat.seatId)
                                                            ? "red"
                                                            : "transparent",
                                                borderColor:
                                                    seat.status === "RESERVED"
                                                        ? "grey"
                                                        : "red",
                                                position: "relative",
                                                '&:hover': {
                                                    backgroundColor:
                                                        seat.status === "RESERVED"
                                                            ? "#B0B0B0"
                                                            : selectedSeats.includes(seat.seatId)
                                                                ? "darkred"
                                                                : "#ddd",
                                                },
                                            }}
                                        >
                                            {seat.status === "RESERVED" && (
                                                <Cancel sx={{ color: "white", fontSize: 18 }} />
                                            )}
                                        </Button>
                                    </span>
                                </Tooltip>
                            </Grid>
                        ))}
                    </Grid>
                </Grid>
            ))}
        </Grid>
    );
};

export default SeatGrid;
