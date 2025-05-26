import React, { useState, useEffect } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Alert,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
} from "@mui/material";
import { Link } from "react-router-dom";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

const dummyLeagues = [
  { id: 1, name: "Zagrebačka Liga" },
  { id: 2, name: "Splitska Liga" },
];

function formatDate([year, month, day]) {
  return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
}

export default function TeamList() {
  const [teams, setTeams] = useState([]);
  const [players, setPlayers] = useState([]);
  const [selectedTeam, setSelectedTeam] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [dialogOpen, setDialogOpen] = useState(false);
  const [formDialogOpen, setFormDialogOpen] = useState(false);
  const [formTeam, setFormTeam] = useState({ id: null, name: "", founded: "", leagueId: "", players: [] });
  const [formPlayer, setFormPlayer] = useState({
    userId: null,
    firstName: "",
    lastName: "",
    address: "",
    email: "",
    password: "",
    rating: "",
    registeredOn: "",
    dateOfBirth: "",
  });
  const [editPlayerId, setEditPlayerId] = useState(null);
  const [error, setError] = useState("");
  const [leagueFilter, setLeagueFilter] = useState("");

  const handleView = (team) => {
    setSelectedTeam(team);
    setDialogOpen(true);
  };

  const handleEdit = (team) => {
    setFormTeam(team);
    setFormDialogOpen(true);
    setError("");
  };

  const handleClose = () => {
    setDialogOpen(false);
    setSelectedTeam(null);
  };

  const handleFormClose = () => {
    setFormDialogOpen(false);
    resetForm();
  };

  const resetForm = () => {
    setFormTeam({ id: null, name: "", founded: "", leagueId: "", players: [] });
    setFormPlayer({ userId: null, firstName: "", lastName: "", address: "", email: "", password: "", rating: "", registeredOn: "", dateOfBirth: "" });
    setEditPlayerId(null);
    setError("");
  };

  const isEmailValid = (email) => /\S+@\S+\.\S+/.test(email);
  const isPastDate = (date) => new Date(date) <= new Date();

  const handleFormSubmit = async () => {
    if (!formTeam.name.trim() || !formTeam.founded || !formTeam.leagueId) {
      setError("Team name, founded date, and league are required.");
      return;
    }
    if (!isPastDate(formTeam.founded)) {
      setError("Founded date must be in the past.");
      return;
    }
    if (formTeam.players.length === 0) {
      setError("Team must have at least one player.");
      return;
    }
    try {
      console.log(formTeam.id)
      const payload = {
        name: formTeam.name,
        founded: formTeam.founded,
        leagueId: parseInt(formTeam.leagueId),
      };
      if (formTeam.id) {
        await fetch(`http://localhost:8080/teams/${formTeam.id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });

        for (const p of formTeam.players) {
          const playerPayload = {
            ...p,
            rating: parseInt(p.rating),
            role: "PLAYER",
            teamId: formTeam.id,
          };

          if (p.id) {
            await fetch(`http://localhost:8080/players/${p.id}`, {
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(playerPayload),
            });
          } else {
            await fetch("http://localhost:8080/players", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(playerPayload),
            });
          }
        }
      } else {
        const res = await fetch("http://localhost:8080/teams", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });

        const saved = await res.json();
        const teamId = saved.teamId;

        for (const p of formTeam.players) {
          const playerPayload = {
            ...p,
            rating: parseInt(p.rating),
            role: "PLAYER",
            teamId: teamId,
          };

          await fetch("http://localhost:8080/players", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(playerPayload),
          });
        }
      }
      handleFormClose();
      await loadData();  
    } catch (err) {
      console.error(err);
      setError("Error saving team");
    }
    
  };

  const handleAddOrEditPlayer = () => {
    const p = formPlayer;
    if (!p.firstName || !p.lastName || !p.address || !isEmailValid(p.email) || p.password.length < 6 || isNaN(p.rating) || p.rating < 0 || p.rating > 3000 || !isPastDate(p.dateOfBirth) || !isPastDate(p.registeredOn)) {
      setError("Please fill out all player fields correctly.");
      return;
    }
    setFormTeam((prev) => ({
      ...prev,
      players: editPlayerId
        ? prev.players.map((x) => (x.id === editPlayerId ? { ...formPlayer, id: editPlayerId } : x))
        : [...prev.players, { ...formPlayer, id: Date.now() }],
    }));
    setFormPlayer({ id: null, firstName: "", lastName: "", address: "", email: "", password: "", rating: "", registeredOn: "", dateOfBirth: "" });
    setEditPlayerId(null);
    setError("");
  };

  const handleRemovePlayer = (id) => {
    setFormTeam((prev) => ({ ...prev, players: prev.players.filter((p) => p.id !== id) }));
  };

  const handleEditPlayer = (player) => {
    setFormPlayer(player);
    setEditPlayerId(player.id);
  };

const filteredTeams = teams.filter((t) => {
  const matchLeague = !leagueFilter || String(t.leagueId) === String(leagueFilter);
  const matchSearch = t.name.toLowerCase().includes(searchTerm.toLowerCase());
  return matchLeague && matchSearch;
});

  const getLeagueName = (id) => dummyLeagues.find((l) => l.id === id)?.name || "Unknown";

  async function loadData() {
    try {
      const teamRes = await fetch("http://localhost:8080/teams");
      const playerRes = await fetch("http://localhost:8080/players");
      const teamData = await teamRes.json();
      const playerData = await playerRes.json();

      const teamsWithPlayers = teamData.map((team) => ({
        id: team.teamId,
        name: team.name,
        founded: formatDate(team.founded),
        leagueId: team.leagueId,
        players: playerData
          .filter((p) => p.teamId === team.teamId)
          .map((p) => ({
            id: p.userId,
            firstName: p.firstName,
            lastName: p.lastName,
            email: p.email,
            address: p.address,
            password: p.password,
            rating: p.rating,
            registeredOn: formatDate(p.registeredOn),
            dateOfBirth: formatDate(p.dateOfBirth),
          })),
      }));

      setTeams(teamsWithPlayers);
      setPlayers(playerData);
    } catch (err) {
      console.error("Failed to load teams or players:", err);
    }
  }
  const handleDelete = async (id) => {
  const confirm = window.confirm("Are you sure you want to delete this team?");
  if (!confirm) return;

  try {
    await fetch(`http://localhost:8080/teams/${id}`, {
      method: "DELETE",
    });
    await loadData();
  } catch (err) {
    console.error(err);
    setError("Error deleting team");
  }
};



  useEffect(() => {

  loadData();
}, []);


  return (
    <div>

      <div style={{ padding: "24px" }}>
        <Typography variant="h5" gutterBottom color="black">Teams</Typography>

        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel>Filter by League</InputLabel>
          <Select sx={{ mb: 2 }} value={leagueFilter} onChange={(e) => setLeagueFilter(e.target.value)} label="Filter by League">
            <MenuItem value="">All</MenuItem>
            {dummyLeagues.map((l) => (
              <MenuItem key={l.id} value={l.id}>{l.name}</MenuItem>
            ))}
          </Select>
          <TextField
            label="Search by Team Name"
            fullWidth
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            sx={{ mb: 2 }}
            />
        </FormControl>

        <Button variant="contained" startIcon={<AddIcon />} onClick={() => setFormDialogOpen(true)} sx={{ mb: 2 }}>
          Add New Team
        </Button>

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Founded</TableCell>
                <TableCell>League</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredTeams.map((team) => (
                <TableRow key={team.id}>
                  <TableCell>{team.name}</TableCell>
                  <TableCell>{team.founded}</TableCell>
                  <TableCell>{getLeagueName(team.leagueId)}</TableCell>
                  <TableCell align="right">
                    <Button variant="contained" onClick={() => handleView(team)} sx={{ mr: 1 }}>View</Button>
                    <IconButton onClick={() => handleEdit(team)}><EditIcon /></IconButton>
                    <Button variant="outlined" color="error" onClick={() => handleDelete(team.id)}>Delete</Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        
<Dialog open={dialogOpen} onClose={handleClose} maxWidth="sm" fullWidth>
  <DialogTitle>{selectedTeam?.name}</DialogTitle>
  <DialogContent>
    <Typography variant="subtitle1">Founded: {selectedTeam?.founded}</Typography>
    <Typography variant="subtitle1">League: {getLeagueName(selectedTeam?.leagueId)}</Typography>
    <Typography variant="h6" sx={{ mt: 2 }}>Players</Typography>
    <ul style={{ paddingLeft: "20px" }}>
      {selectedTeam?.players.map((p) => (
        <li key={p.id}>
          {p.firstName} {p.lastName} ({p.email}), rating: {p.rating}
        </li>
      ))}
    </ul>
  </DialogContent>
</Dialog>

<Dialog open={formDialogOpen} onClose={handleFormClose} maxWidth="sm" fullWidth>
  <DialogTitle>{formTeam.id ? "Edit Team" : "Add New Team"}</DialogTitle>
  <DialogContent>
    {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

    <TextField
      label="Team Name"
      fullWidth
      margin="dense"
      value={formTeam.name}
      onChange={(e) => setFormTeam({ ...formTeam, name: e.target.value })}
    />
    <TextField
      label="Founded"
      type="date"
      fullWidth
      margin="dense"
      InputLabelProps={{ shrink: true }}
      value={formTeam.founded}
      onChange={(e) => setFormTeam({ ...formTeam, founded: e.target.value })}
    />
    <TextField
      label="League"
      select
      fullWidth
      margin="dense"
      value={formTeam.leagueId}
      onChange={(e) => setFormTeam({ ...formTeam, leagueId: parseInt(e.target.value) })}
    >
      {dummyLeagues.map((l) => (
        <MenuItem key={l.id} value={l.id}>{l.name}</MenuItem>
      ))}
    </TextField>

    <Typography variant="h6" sx={{ mt: 3 }}>Add / Edit Player</Typography>
    {[
      { label: "First Name", key: "firstName" },
      { label: "Last Name", key: "lastName" },
      { label: "Address", key: "address" },
      { label: "Email", key: "email" },
      { label: "Password", key: "password", type: "password" },
      { label: "Rating", key: "rating", type: "number" },
      { label: "Registered On", key: "registeredOn", type: "date" },
      { label: "Date of Birth", key: "dateOfBirth", type: "date" },
    ].map(({ label, key, type }) => (
      <TextField
        key={key}
        label={label}
        type={type || "text"}
        fullWidth
        margin="dense"
        value={formPlayer[key]}
        onChange={(e) => setFormPlayer({ ...formPlayer, [key]: e.target.value })}
        InputLabelProps={type === "date" ? { shrink: true } : undefined}
      />
    ))}
    <Button onClick={handleAddOrEditPlayer} sx={{ mt: 1 }}>
      {editPlayerId ? "Update Player" : "Add Player"}
    </Button>

    <ul style={{ paddingLeft: "20px", marginTop: "12px" }}>
      {formTeam.players.map((p) => (
        <li key={p.id} style={{ display: "flex", justifyContent: "space-between" }}>
          <span>{p.firstName} {p.lastName} ({p.email})</span>
          <span>
            <IconButton size="small" onClick={() => handleEditPlayer(p)}><EditIcon fontSize="small" /></IconButton>
            <IconButton size="small" onClick={() => handleRemovePlayer(p.id)} color="error"><DeleteIcon fontSize="small" /></IconButton>
          </span>
        </li>
      ))}
    </ul>
  </DialogContent>
  <DialogActions>
    <Button onClick={handleFormClose}>Cancel</Button>
    <Button variant="contained" onClick={handleFormSubmit}>Save</Button>
  </DialogActions>
</Dialog>

      </div>
    </div>
  );
}
