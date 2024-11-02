export interface NoteDTO {
  id?: number;
  content: string;
  createdAt: Date;
}

export interface DocumentDTO {
  id: number;
  fileName: string;
  fileType: string;
  uploadedAt: Date;
}

export interface DetailedCandidateDTO {
  applicationCode: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  status: string;
  rating: number;
  city: string;
  notes: NoteDTO[];
  documents: DocumentDTO[];
}
