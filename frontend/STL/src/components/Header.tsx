import React from "react";
import { AppBar, Toolbar, Typography, Stack, Button } from "@mui/material";
import { Link } from "react-router-dom";

export default function Header() {
  return (
    <AppBar position="static">
      <Toolbar disableGutters>
        <Typography variant="h6" sx={{ ml: 2, mr: 2 }}>
          STL
        </Typography>
        <Stack direction="row" spacing={2} sx={{ ml: "auto", mr: 2 }}>
          <Button variant="outlined" color="inherit" component={Link} to="/teams">Teams</Button>
          <Button variant="outlined" color="inherit" component={Link} to="/leagues">Leagues</Button>
        </Stack>
      </Toolbar>
    </AppBar>
  );
}
