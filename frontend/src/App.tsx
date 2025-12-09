import React, { useEffect, useState } from "react";
import FileUpload from "./components/FileUpload";
import DocumentList from "./components/DocumentList";
import { getDocuments, type DocumentResponse } from "./api/documentApi";
import { Container, Typography, Box, Paper } from "@mui/material";
import { Toaster } from "react-hot-toast";
import "./index.css";

export default function App() {
  const [documents, setDocuments] = useState<DocumentResponse[]>([]);

  const loadDocuments = async () => {
    const response = await getDocuments();
    setDocuments(response.data);
  };

  useEffect(() => {
    loadDocuments();
  }, []);

  return (
    <Container maxWidth="md" sx={{ mt: 5 }}>
      <Toaster position="top-right" />

      <Paper elevation={4} sx={{ p: 4, borderRadius: 4 }}>
        <Typography variant="h4" align="center" gutterBottom>
          Patient Document Portal
        </Typography>

        <Box mt={4}>
          <FileUpload onUploadSuccess={loadDocuments} />
        </Box>

        <Box mt={5}>
          <DocumentList docs={documents} refresh={loadDocuments} />
        </Box>
      </Paper>
    </Container>
  );
}
