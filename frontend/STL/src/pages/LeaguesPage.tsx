import React, { useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TextField,
  Button,
  Typography,
  Stack,
  Alert,
  AppBar,
  Toolbar,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { Link } from "react-router-dom";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Header from "../components/Header";

export default function LeaguesPage() {
  const [leagues, setLeagues] = useState([
    { id: 1, name: "Zagrebačka Liga" },
    { id: 2, name: "Splitska Liga" },
  ]);

  const [newLeagueName, setNewLeagueName] = useState("");
  const [editingLeagueId, setEditingLeagueId] = useState(null);
  const [error, setError] = useState("");
  const [dialogOpen, setDialogOpen] = useState(false);

  const handleAddLeague = () => {
    if (!newLeagueName.trim()) {
      setError("League name is required.");
      return;
    }
    if (
      leagues.some(
        (l) =>
          l.name.toLowerCase() === newLeagueName.trim().toLowerCase() &&
          l.id !== editingLeagueId
      )
    ) {
      setError("League with that name already exists.");
      return;
    }

    if (editingLeagueId) {
      setLeagues((prev) =>
        prev.map((l) =>
          l.id === editingLeagueId ? { ...l, name: newLeagueName.trim() } : l
        )
      );
    } else {
      setLeagues((prev) => [
        ...prev,
        { id: Date.now(), name: newLeagueName.trim() },
      ]);
    }

    setNewLeagueName("");
    setEditingLeagueId(null);
    setDialogOpen(false);
    setError("");
  };

  const handleDelete = (id) => {
    setLeagues((prev) => prev.filter((l) => l.id !== id));
  };

  const handleEdit = (league) => {
    setNewLeagueName(league.name);
    setEditingLeagueId(league.id);
    setError("");
    setDialogOpen(true);
  };

  return (
    <div>
        <Header/>
      <div style={{ padding: 24, width: "100%" }}>
        <Typography variant="h5" color="black" gutterBottom>Leagues</Typography>

        <div style={{ display: "flex", gap: "12px", alignItems: "center", marginBottom: "16px" }}>
          <TextField
            label="New League Name"
            value={newLeagueName}
            onChange={(e) => setNewLeagueName(e.target.value)}
          />
          <Button variant="contained" onClick={() => {
            setEditingLeagueId(null);
            setDialogOpen(true);
            setError("");
          }}>
            Add League
          </Button>
        </div>

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Name</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {leagues.map((league) => (
                <TableRow key={league.id}>
                  <TableCell>{league.id}</TableCell>
                  <TableCell>{league.name}</TableCell>
                  <TableCell align="right">
                    <IconButton onClick={() => handleEdit(league)}><EditIcon /></IconButton>
                    <IconButton onClick={() => handleDelete(league.id)} color="error"><DeleteIcon /></IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </div>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
        <DialogTitle>{editingLeagueId ? "Edit League" : "Add New League"}</DialogTitle>
        <DialogContent>
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
          <TextField
            autoFocus
            fullWidth
            label="League Name"
            value={newLeagueName}
            onChange={(e) => setNewLeagueName(e.target.value)}
            margin="dense"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleAddLeague} variant="contained">
            {editingLeagueId ? "Update" : "Add"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
