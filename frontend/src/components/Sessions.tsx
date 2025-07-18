import {Card, CardContent, Typography, Stack, Button} from "@mui/material";
import {Link} from "react-router-dom";
import {Session} from "../types/Session.ts";

export interface SessionsProps {
    sessions: Session[];
}

const Sessions: React.FC<SessionsProps> = ({sessions}) => {
    if (sessions.length === 0) {
        return <Typography variant="body1" textAlign="center" mt={2}>No available sessions</Typography>;
    }

    return (
        <Card>
            <CardContent>
                <Typography variant="h6" fontWeight="bold" mb={2}>Available Sessions</Typography>
                <Stack direction="row" spacing={2} flexWrap="wrap">
                    {sessions.map((session) => (
                        <Button
                            key={session.id}
                            variant="outlined"
                            component={Link}
                            to={`/sessions/${session.id}`}
                            sx={{
                                px: 3,
                                py: 1,
                                borderRadius: 2,
                                borderColor: "#D32F2F",
                                color: "#D32F2F",
                                "&:hover": {
                                    bgcolor: "#FFEBEE",
                                    borderColor: "#B71C1C",
                                    color: "#B71C1C",
                                },
                            }}
                        >
                            {new Date(session.startTime).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})}
                        </Button>
                    ))}
                </Stack>
            </CardContent>
        </Card>
    );
};

export default Sessions;
