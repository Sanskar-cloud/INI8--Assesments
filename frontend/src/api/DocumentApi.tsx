import axios from "axios";

const API = "http://localhost:8080/documents";

export interface DocumentResponse {
  id: number;
  filename: string;
  size: number;
  createdAt: string;
}

export interface BaseApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export const uploadDocument = async (file: File): Promise<BaseApiResponse<DocumentResponse>> => {
  const formData = new FormData();
  formData.append("file", file);

  const res = await axios.post(`${API}/upload`, formData);
  return res.data;
};

export const getDocuments = async (): Promise<BaseApiResponse<DocumentResponse[]>> => {
  const res = await axios.get(API);
  return res.data;
};

export const deleteDocument = async (id: number): Promise<BaseApiResponse<null>> => {
  const res = await axios.delete(`${API}/${id}`);
  return res.data;
};

export const downloadDocument = async (id: number) => {
  return axios.get(`${API}/${id}`, { responseType: "blob" });
};
