import { useState, useEffect } from "react";
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
  Alert,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Header from "../components/Header";

export default function LeaguesPage() {
  const [leagues, setLeagues] = useState([]);

  async function loadData() {
    try {
      const leagueRes = await fetch("http://localhost:8080/leagues");
      const leagueData = await leagueRes.json();
      setLeagues(leagueData);


      
    } catch (err) {
      console.error("Failed to load leagues:", err);
    }
  }
  

  const [newLeagueName, setNewLeagueName] = useState("");
  const [newLeagueRank, setNewLeagueRank] = useState(null);
  const [editingLeagueId, setEditingLeagueId] = useState(null);
  const [error, setError] = useState("");
  const [dialogOpen, setDialogOpen] = useState(false);

  const handleAddLeague = async () => {
    if (!newLeagueName.trim()) {
      setError("League name is required.");
      return;
    }
    if (
      leagues.some(
        (l) =>
          l.description.toLowerCase() === newLeagueName.trim().toLowerCase() &&
          l.leagueId !== editingLeagueId
      )
    ) {
      setError("League with that name already exists.");
      return;
    }

  if (editingLeagueId) {
    await fetch(`http://localhost:8080/leagues/${editingLeagueId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ description: newLeagueName.trim(), rank: newLeagueRank }),
    });
  } else {
    await fetch("http://localhost:8080/leagues", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ description: newLeagueName.trim(), rank: newLeagueRank }),
    });
  }

await loadData();

    setNewLeagueName("");
    setNewLeagueRank(null);
    setEditingLeagueId(null);
    setDialogOpen(false);
    setError("");
  };

  const handleDelete = async (id) => {
  const confirm = window.confirm("Are you sure you want to delete this league?");
  if (!confirm) return;

  try {
    await fetch(`http://localhost:8080/leagues/${id}`, {
      method: "DELETE",
    });
    await loadData();
  } catch (err) {
    console.error(err);
    setError("Error deleting team");
  }
};

  const handleEdit = (league) => {
    setNewLeagueName(league.description);
    setNewLeagueRank(league.rank);
    setEditingLeagueId(league.leagueId);
    setError("");
    setDialogOpen(true);
  };

    const closeDialog = () => {
    setNewLeagueName("");
    setNewLeagueRank(null);
    setEditingLeagueId(null);
    setDialogOpen(false);
    setError("");
  };

  useEffect(() => {
  
    loadData();
  }, []);

  return (
    <div>
        <Header/>
      <div style={{ padding: 24, width: "100%" }}>
        <Typography variant="h5" color="black" gutterBottom>Leagues</Typography>

        <div style={{ display: "flex", gap: "12px", alignItems: "center", marginBottom: "16px" }}>
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
                <TableRow key={league.leagueId}>
                  <TableCell>{league.leagueId}</TableCell>
                  <TableCell>{league.description}</TableCell>
                  <TableCell align="right">
                    <IconButton onClick={() => handleEdit(league)}><EditIcon /></IconButton>
                    <IconButton onClick={() => handleDelete(league.leagueId)} color="error"><DeleteIcon /></IconButton>
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
          <TextField
            autoFocus
            fullWidth
            label="League Rank"
            value={newLeagueRank}
            type="number"
            onChange={(e) => setNewLeagueRank(e.target.value)}
            margin="dense"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDialog}>Cancel</Button>
          <Button onClick={handleAddLeague} variant="contained">
            {editingLeagueId ? "Update" : "Add"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
