import React from "react";
import {
  Card,
  CardContent,
  Typography,
  Button,
  Stack,
  Box,
  Divider,
} from "@mui/material";
import DownloadIcon from "@mui/icons-material/Download";
import DeleteIcon from "@mui/icons-material/Delete";
import {
  downloadDocument,
  deleteDocument,
  type DocumentResponse,
} from "../api/DocumentApi";
import toast from "react-hot-toast";

interface Props {
  docs: DocumentResponse[];
  refresh: () => void;
}

export default function DocumentList({ docs, refresh }: Props) {
  const handleDownload = async (id: number, filename: string) => {
    const res = await downloadDocument(id);
    const blob = new Blob([res.data], { type: "application/pdf" });
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = filename;
    a.click();

    toast.success("Download started!");
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this document?")) return;

    await deleteDocument(id);
    toast.success("Document deleted");
    refresh();
  };

  return (
    <Box>
      <Typography variant="h6" gutterBottom>
        Uploaded Documents
      </Typography>

      {docs.length === 0 && (
        <Typography>No documents uploaded yet.</Typography>
      )}

      <Stack spacing={2} mt={2}>
        {docs.map((doc) => (
          <Card key={doc.id} elevation={3} sx={{ borderRadius: 3 }}>
            <CardContent>
              <Typography variant="subtitle1" fontWeight="bold">
                {doc.filename}
              </Typography>

              <Typography variant="body2" color="text.secondary">
                {(doc.size / 1024).toFixed(2)} KB — Uploaded on{" "}
                {new Date(doc.createdAt).toLocaleDateString()}
              </Typography>

              <Divider sx={{ my: 2 }} />

              <Stack direction="row" spacing={2}>
                <Button
                  variant="contained"
                  startIcon={<DownloadIcon />}
                  onClick={() => handleDownload(doc.id, doc.filename)}
                >
                  Download
                </Button>

                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<DeleteIcon />}
                  onClick={() => handleDelete(doc.id)}
                >
                  Delete
                </Button>
              </Stack>
            </CardContent>
          </Card>
        ))}
      </Stack>
    </Box>
  );
}
