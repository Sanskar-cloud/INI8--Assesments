import React, { useState } from "react";
import { uploadDocument } from "../api/DocumentApi";
import {
  Button,
  Box,
  Typography,
  CircularProgress,
  Paper,
} from "@mui/material";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import toast from "react-hot-toast";

interface Props {
  onUploadSuccess: () => void;
}

export default function FileUpload({ onUploadSuccess }: Props) {
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const handleUpload = async () => {
    if (!file) {
      toast.error("Please select a PDF file");
      return;
    }

    setLoading(true);

    try {
      const response = await uploadDocument(file);
      toast.success(response.message);
      setFile(null);
      onUploadSuccess();
    } catch (error) {
      toast.error("Upload failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper sx={{ p: 3, borderRadius: 3, textAlign: "center" }} elevation={2}>
      <Typography variant="h6" gutterBottom>
        Upload PDF Document
      </Typography>

      <Box mt={2}>
        <input
          type="file"
          accept=".pdf"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
          style={{ marginBottom: "12px" }}
        />
      </Box>

      <Button
        variant="contained"
        startIcon={<CloudUploadIcon />}
        onClick={handleUpload}
        disabled={loading}
      >
        {loading ? <CircularProgress size={22} color="inherit" /> : "Upload"}
      </Button>

      {file && (
        <Typography variant="body2" sx={{ mt: 1, opacity: 0.7 }}>
          Selected: {file.name}
        </Typography>
      )}
    </Paper>
  );
}
